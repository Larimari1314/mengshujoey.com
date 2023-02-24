import assessmentEdit from '../components/assessmentEdit'
import editEvaluatiomInformation from '@/views/assessmentItemsManager/components/editEvaluatiomInformation'
import editDetailInformation from '@/views/assessmentItemsManager/components/editDetailInformation'
import { backendUrl } from '@/config/setting.config'
import {
  addDetailInformation,
  deleteEvaluationProject,
  queryAssessmentData,
} from '@/api/assessmentItems'
import {
  getReferralLevel,
  getStudioMember,
  tabDropDown,
} from '@/api/dropDownOption'
import store from '@/store'

export default {
  name: 'assessmentManagerByAdmin',
  components: {
    editDetailInformation,
    assessmentEdit,
    editEvaluatiomInformation,
  },
  data: function () {
    return {
      lazy: true,
      title: '添加测评项目',
      dialogFormVisible: false,
      hotImage:
        'https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/webImage/hotClickNumber.png',
      pageSize: 4,
      page: 1,
      background: true,
      queryFormageSize: 1,
      total: 0,
      queryData: {
        text: undefined,
        labelId: undefined,
      },
      labelIdOptions: [],
      labelValue: '',
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      evaluationProject: [],
      multipleSelection: [],
      projectInformation: {
        projectName: undefined,
        projectImage: undefined,
        recommendLevel: undefined,
        recommendOne: '温度',
        recommendOneValue: undefined,
        recommendTwo: '价格',
        recommendTwoValue: undefined,
        recommendThree: '甜度',
        recommendThreeValue: undefined,
      },
      projectInformation_rules: {
        projectName: [
          {
            required: true,
            message: '请输入项目名称',
            trigger: 'blur',
          },
        ],
        projectImage: [
          {
            required: true,
            message: '请上传封面',
            trigger: 'change',
          },
        ],
        recommendLevel: [
          {
            required: true,
            message: '请选择测评级别',
            trigger: 'change',
          },
        ],
      },
      projectImageAction: backendUrl + 'evaluation/uploadProjectAddress',
      projectImagefileList: [],
      recommendLevelOptions: [],
      studioMemberList: [],
      studioMemberInitList: [],
      nowAddProject: {},
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      permissions: store.getters['user/permissions'],
    }
  },
  mounted() {
    if (
      this.permissions.indexOf('11') !== -1 ||
      this.permissions.indexOf('3') !== -1
    ) {
      this.queryEvaluationData()
    }
    if (
      this.permissions.indexOf('10') !== -1 ||
      this.permissions.indexOf('3') !== -1
    ) {
      tabDropDown(this.configs).then((res) => {
        this.labelIdOptions = res.data.data
      })
      getReferralLevel(this.configs).then((res) => {
        this.recommendLevelOptions = res.data.data
      })
      getStudioMember(this.configs).then((res) => {
        this.studioMemberList = res.data.data
      })
    }
  },
  methods: {
    projectImageSuceess(response, file, fileList) {
      this.projectInformation.projectImage = response
    },
    submitForm() {
      this.$refs['projectInformation'].validate((valid) => {
        if (!valid) return
        // TODO 提交表单
        //整理数据
        this.projectInformation.projectId = this.nowAddProject.id
        this.projectInformation.assessmentMode =
          this.nowAddProject.scoringClass === '个人测评'
        if (this.nowAddProject.scoringClass === '团队测评') {
          //团队测评
          //基本信息整理
          let detailInformation = ''
          if (
            this.projectInformation.recommendOneValue !== undefined &&
            this.projectInformation.recommendOneValue !== ''
          ) {
            if (detailInformation !== '') {
              detailInformation += ','
            }
            detailInformation +=
              '"' +
              this.projectInformation.recommendOne +
              '":' +
              '"' +
              this.projectInformation.recommendOneValue +
              '"'
          }
          if (
            this.projectInformation.recommendTwoValue !== undefined &&
            this.projectInformation.recommendTwoValue !== ''
          ) {
            if (detailInformation !== '') {
              detailInformation += ','
            }
            detailInformation +=
              '"' +
              this.projectInformation.recommendTwo +
              '":' +
              '"' +
              this.projectInformation.recommendTwoValue +
              '"'
          }
          if (
            this.projectInformation.recommendThreeValue !== undefined &&
            this.projectInformation.recommendThreeValue !== ''
          ) {
            if (detailInformation !== '') {
              detailInformation += ','
            }
            detailInformation +=
              '"' +
              this.projectInformation.recommendThree +
              '":' +
              '"' +
              this.projectInformation.recommendThreeValue +
              '"'
          }
          detailInformation = '{' + detailInformation + '}'
          this.projectInformation.detailInformation = detailInformation
          //分数整理
          let fraction = []
          let score = 0
          this.studioMemberList.forEach((item) => {
            if (item.fraction !== undefined && item.fraction !== '') {
              score += parseInt(item.fraction)
              fraction.push(item.id + '_' + item.fraction)
            }
          })
          this.projectInformation.fraction = fraction
          this.projectInformation.score = score
          // console.log(JSON.stringify(this.projectInformation))
        }
        addDetailInformation(this.projectInformation, this.configs).then(
          (res) => {
            this.onClose()
            this.queryEvaluationData()
            this.$message.success('添加项目成功')
          }
        )
      })
    },
    onClose() {
      this.dialogFormVisible = false
      this.nowAddProject = {}
      this.studioMemberList.forEach((item) => {
        delete item.fraction
      })
      this.$refs.projectImage.clearFiles()
      this.$refs['projectInformation'].resetFields()
    },
    resetForm() {
      this.dialogFormVisible = false
      this.studioMemberList.forEach((item) => {
        delete item.fraction
      })
      this.$refs.projectImage.clearFiles()
      this.$refs['projectInformation'].resetFields()
    },
    projectImageBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2
      if (!isRightSize) {
        this.$message.error('文件大小超过 2MB')
      }
      return isRightSize
    },
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      )
    },
    addProjectByDetailId(row) {
      this.nowAddProject = row
      this.dialogFormVisible = true
    },
    queryEvaluationData() {
      //查询数据
      let queryData = {
        page: this.page,
        text: this.queryData.text,
        labelId: this.queryData.labelId,
      }
      queryAssessmentData(queryData, this.configs).then((res) => {
        if (res.data.code === '200') {
          this.evaluationProject = res.data.data.list
          this.total = res.data.data.total
        }
      })
    },
    editEvaluationInformation(row) {
      this.$refs['editDetailInformation'].onshow(row.id, row.scoringClass)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleAdd() {
      this.$refs['edit'].showEdit()
    },
    handleDelete() {
      this.$confirm('此操作将删除该项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          //删除逻辑
          var deleteList = []
          this.multipleSelection.forEach((item) => {
            deleteList.push(item.id)
          })
          deleteEvaluationProject(deleteList, this.configs).then((res) => {
            if (res.data.code === '200') {
              //删除信息成功
              this.$message.success('删除项目成功')
              this.queryEvaluationData()
            } else {
              this.$message.error('删除项目失败，请稍后重试')
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消删除',
          })
        })
    },
    handleCurrentChange(val) {
      //分页逻辑
      this.page = val
      this.queryEvaluationData()
    },
    handleSizeChange(val) {
      this.page = val
      this.queryEvaluationData()
    },
    editDetailInformation(row) {
      this.$refs['editEvaluation'].onShow(row)
    },
  },
}
