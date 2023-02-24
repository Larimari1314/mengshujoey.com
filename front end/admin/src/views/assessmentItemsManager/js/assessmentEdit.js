import { addProject, getVideoInformation } from '@/api/assessmentItems'
import { backendUrl } from '@/config/setting.config'
import soloAssessment from '@/views/assessmentItemsManager/components/soloAssessment'
import {
  getReferralLevel,
  getStudioMember,
  tabDropDown,
} from '@/api/dropDownOption'
import store from '@/store'
export default {
  name: 'assessmentEdit',
  components: {
    soloAssessment,
  },
  data() {
    return {
      addProjectButton: false,
      editTitle: '团队测评项目添加',
      detailTitle: '添加测评项目',
      title: '',
      projectModel: 1,
      teamProject: {
        mainTitle: '',
        subheading: '',
        evaluationDate: new Date(),
        coverImage: null,
        coverImageList: null,
        evaluationImage: null,
        videoAddress: '',
        labelId: undefined,
        projectItem: [],
      },
      importVisible: false,
      dialogFormVisible: false,
      dialogTeamVisible: false,
      dialogDetailVisible: false,
      teamProject_rules: {
        mainTitle: [
          {
            required: true,
            message: '请输入主标题',
            trigger: 'blur',
          },
        ],
        subheading: [
          {
            required: true,
            message: '请输入副标题',
            trigger: 'blur',
          },
        ],
        evaluationDate: [
          {
            required: true,
            message: '请选择测评日期',
            trigger: 'change',
          },
        ],
        videoAddress: [
          {
            required: true,
            message: '请输入视频地址',
            trigger: 'blur',
          },
          {
            pattern:
              /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/,
            message: '请输入正确的视频地址',
            trigger: 'blur',
          },
        ],
        labelId: [
          {
            required: true,
            message: '请选择标签',
            trigger: 'change',
          },
        ],
        coverImageList: [
          {
            required: true,
            message: '封面图片请上传',
            trigger: 'change',
          },
        ],
        evaluationImage: [
          {
            required: true,
            message: '测评图片请上传',
            trigger: 'change',
          },
        ],
      },
      coverImageListAction: backendUrl + 'evaluation/uploadCoverAddress',
      evaluationImageListAction:
        backendUrl + 'evaluation/uploadEvaluationAddress',
      coverImageListfileList: [],
      assessmentImageList: [],
      labelIdOptions: [],
      detailProject: {
        projectName: '',
        projectImage: null,
        recommendLevel: undefined,
        recommendOne: '温度',
        informationOne: '',
        recommendTwo: '价格',
        informationTwo: '',
        recommendThree: '甜度',
        informationThree: '',
        fraction: [],
      },
      detailProject_rules: {
        projectName: [
          {
            required: true,
            message: '请输入产品名称',
            trigger: 'blur',
          },
        ],
        recommendLevel: [
          {
            required: true,
            message: '请选择推荐级别',
            trigger: 'change',
          },
        ],
        projectImage: [
          {
            required: true,
            message: '项目图片请上传',
            trigger: 'change',
          },
        ],
      },
      projectImageAction: backendUrl + 'evaluation/uploadProjectAddress',
      projectImagefileList: [],
      recommendLevelOptions: [],
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      studioMemberList: [],
      studioMemberInitList: [],
      editProjectInformation: false,
      projectIndex: -1,
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      permissions: store.getters['user/permissions'],
    }
  },
  created() {
    if (this.permissions.indexOf('10') !== -1) {
      tabDropDown(this.configs).then((res) => {
        this.labelIdOptions = res.data.data
      })
      getReferralLevel(this.configs).then((res) => {
        this.recommendLevelOptions = res.data.data
      })
      getStudioMember(this.configs).then((res) => {
        this.studioMemberList = res.data.data
        this.studioMemberInitList = res.data.data
      })
    }
  },
  methods: {
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      )
    },
    deleteProjectInformations(row, index) {
      this.$confirm('此操作将永久删除该测评项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.teamProject.projectItem.splice(index, 1)
          this.$message.success('移除测评信息成功')
          this.close()
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消移除',
          })
        })
    },
    editProjectInformations(row, index) {
      this.editProjectInformation = true
      this.projectIndex = index
      var projectDetailInformation = Object.assign({}, row)
      this.detailProject = projectDetailInformation
      this.studioMemberList = row.studioMemberList

      // console.log(JSON.stringify(projectDetailInformation))
      // console.log(JSON.stringify(projectDetailInformation.studioMemberList))
      // console.log(JSON.stringify(this.studioMemberList))
      this.projectImagefileList = [
        { url: projectDetailInformation.projectImage },
      ]
      // console.log(row.fraction)
      this.dialogDetailVisible = true
      this.dialogTeamVisible = false
    },
    successProjectCover(response, file, fileList) {
      this.detailProject.projectImage = response
    },
    closedDetail() {
      this.$refs.projectImage.clearFiles()
      this.dialogDetailVisible = false
      this.dialogTeamVisible = true
    },
    addDetailProject() {
      this.projectIndex = -1
      this.editProjectInformation = false
      this.studioMemberList.forEach((item) => {
        delete item.fraction
      })
      this.detailProject = this.$options.data().detailProject
      this.dialogDetailVisible = true
      this.dialogTeamVisible = false
    },
    successEvaluation(response, file, fileList) {
      this.teamProject.evaluationImage = response
    },
    successCover(response, file, fileList) {
      this.teamProject.coverImageList = response
    },
    oneClickImport() {
      let host = window.location.host
      var per = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/
      if (per.test(host)) {
        if (this.teamProject.videoAddress === '') {
          this.$message.error('请填写视频地址')
        }
        //一键导入逻辑实现
        getVideoInformation(this.teamProject.videoAddress).then((res) => {
          alert(JSON.stringify(res))
        })
        this.importVisible = true
      } else {
        this.$message.error('当前接口故障，请手动填写信息')
      }
    },
    closeTeam() {
      // this.teamProject = this.$options.data().teamProject
      this.$emit('fetch-data')
      this.dialogTeamVisible = false
    },
    submitProjectDetail() {
      // console.log(JSON.stringify(this.teamProject))
      this.$refs['teamProject'].validate((valid) => {
        if (!valid) return
        this.addProjectButton = true
        addProject(this.teamProject, this.configs).then((res) => {
          if (res.data.code === '200') {
            this.addProjectButton = false
            //添加成功
            this.closeTeam()
            this.$message.success('添加信息成功！，请刷新界面即可看见数据')
            this.$refs.evaluationImage.clearFiles()
            this.$refs.coverImageList.clearFiles()
          }
        })
        this.addProjectButton = false
      })
    },
    submitProject() {
      this.$refs['detailProject'].validate((valid) => {
        if (!valid) return
        //整理团队评分
        let score = 0
        //清空测评信息
        this.detailProject.fraction = []
        this.studioMemberList.forEach((item) => {
          if (item.fraction !== undefined && item.fraction !== '') {
            //说明此时参加了测评
            score = score + parseInt(item.fraction)
            this.detailProject.fraction.push(item.id + '_' + item.fraction)
          }
        })
        let detailInformationString = ''
        if (
          this.detailProject.informationOne !== '' &&
          this.detailProject.informationOne !== undefined
        ) {
          if (detailInformationString !== '') {
            detailInformationString += ','
          }
          detailInformationString +=
            '"' +
            this.detailProject.recommendOne +
            '"' +
            ':"' +
            this.detailProject.informationOne +
            '"'
        }
        if (
          this.detailProject.informationTwo !== '' &&
          this.detailProject.informationTwo !== undefined
        ) {
          if (detailInformationString !== '') {
            detailInformationString += ','
          }
          detailInformationString +=
            '"' +
            this.detailProject.recommendTwo +
            '"' +
            ':"' +
            this.detailProject.informationTwo +
            '"'
        }
        if (
          this.detailProject.informationThree !== '' &&
          this.detailProject.informationThree !== undefined
        ) {
          if (detailInformationString !== '') {
            detailInformationString += ','
          }
          detailInformationString +=
            '"' +
            this.detailProject.recommendThree +
            '"' +
            ':"' +
            this.detailProject.informationThree +
            '"'
        }
        this.detailProject.detailInformation =
          '{' + detailInformationString + '}'
        //添加到项目中
        this.$refs.projectImage.clearFiles()
        let projectItem = Object.assign({}, this.detailProject)
        projectItem.score = score
        projectItem.studioMemberList = JSON.parse(
          JSON.stringify(this.studioMemberList)
        )
        this.studioMemberList = this.studioMemberInitList
        //表示当前数据并不是修改数据
        if (!this.editProjectInformation) {
          this.teamProject.projectItem.push(projectItem)
        } else {
          //当前数据已经修改数据
          this.teamProject.projectItem.splice(this.projectIndex, 1, projectItem)
        }
        // console.log(JSON.stringify(this.teamProject.projectItem))
        this.detailProject = this.$options.data().detailProject
        this.dialogDetailVisible = false
        this.dialogTeamVisible = true
      })
    },
    resetForm() {
      this.$refs['teamProject'].resetFields()
    },
    coverImageListBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2
      if (!isRightSize) {
        this.$message.error('文件大小超过 2MB')
      }
      let isAccept = new RegExp('image/*').test(file.type)
      if (!isAccept) {
        this.$message.error('应该选择image/*类型的文件')
      }
      return isRightSize && isAccept
    },
    teamAssessment() {
      this.assessmentImageList = []
      this.coverImageListfileList = []
      this.teamProject = this.$options.data().teamProject
      this.dialogFormVisible = false
      this.$emit('fetch-data')
      this.dialogTeamVisible = true
    },
    showEdit(row) {
      if (!row) {
        this.title = '选择测评模式'
      } else {
        this.title = '选择测评模式'
        this.teamProject = Object.assign({}, row)
      }
      this.dialogFormVisible = true
    },
    close() {
      this.dialogFormVisible = false
      this.assessmentImageList = []
      this.coverImageListfileList = []
      this.teamProject = this.$options.data().teamProject
      this.$emit('fetch-data')
    },
    save() {
      this.dialogFormVisible = false
      this.$refs['soloAssessment'].showEdit()
    },
  },
}
