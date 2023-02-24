import {
  deleteDetailInformationById,
  editDetailInformations,
  findDetailInformation,
} from '@/api/assessmentItems'
import { backendUrl } from '@/config/setting.config'
import { getReferralLevel, getStudioMember } from '@/api/dropDownOption'
import store from '@/store'

export default {
  name: 'editDetailInformationManager',
  components: {},
  data() {
    return {
      editDetailInfomationDis: false,
      activeNames: undefined,
      dialogFormVisible: false,
      project: [],
      project_rules: {
        projectName: [
          {
            required: true,
            message: '请输入项目名称',
            trigger: 'blur',
          },
        ],
        recommendLevel: [
          {
            required: true,
            message: '请选择推荐等级',
            trigger: 'change',
          },
        ],
        detailtitle: [],
        detailInformation: [],
        fraction: [],
      },
      recommendLevelOptions: [],
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      projectImageAction: backendUrl + 'evaluation/uploadProjectAddress',
      projectImagefileList: [],
      detailInformationKeys: [],
      editDetailInformationKeys: [],
      assessmentMode: false,
      editDetailVisible: false,
      detailInformation: {},
      studioMemberInitList: [],
      studioMemberList: [],
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      permissions: store.getters['user/permissions'],
    }
  },
  created() {},
  mounted() {
    if (this.permissions.indexOf('10') !== -1) {
      getStudioMember(this.configs).then((res) => {
        this.studioMemberList = res.data.data
        this.studioMemberInitList = res.data.data
      })
      getReferralLevel(this.configs).then((res) => {
        this.recommendLevelOptions = res.data.data
      })
    }
  },
  methods: {
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      )
    },
    studioMemberChange(e, row) {
      row.fraction = e
      this.$forceUpdate()
    },
    editDetailInformation() {
      this.$refs['project'].validate((valid) => {
        if (!valid) return
        this.editDetailInfomationDis = true
        //修改项目信息
        this.$confirm('此操作将修改该项目, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            //分小组测评项目和个人测评项目
            let submitData = {
              projectImage: this.detailInformation.projectImage,
              recommendLevel: this.detailInformation.recommendLevel,
              projectName: this.detailInformation.projectName,
              id: this.detailInformation.id,
            }
            if (!this.assessmentMode) {
              //团队测评需要加信息
              let fractionList = []
              //分数
              let score = 0
              this.studioMemberList.forEach((item) => {
                //id+分数构成
                if (item.fraction !== undefined && item.fraction !== '') {
                  score += parseInt(item.fraction)
                  fractionList.push(item.id + '_' + item.fraction)
                }
              })
              submitData.fraction = fractionList
              let detailInformation = ''
              //测评基本信息
              this.editDetailInformationKeys.forEach((item) => {
                if (item.value !== undefined && item.value !== '') {
                  //说明当前存在此值
                  // {"温度":"少冰","价格":"14元","甜度":"七分甜"}
                  if (detailInformation === '') {
                    detailInformation +=
                      '"' + item.name + '":"' + item.value + '"'
                  } else {
                    detailInformation +=
                      ',"' + item.name + '":"' + item.value + '"'
                  }
                }
              })
              submitData.score = score
              submitData.detailInformation = '{' + detailInformation + '}'
            }
            submitData.assessmentMode = this.assessmentMode
            editDetailInformations(submitData, this.configs).then((res) => {
              this.editDetailInfomationDis = false
              if (res.data.code === '200') {
                //更新数据成功
                this.$message({
                  type: 'success',
                  message: '修改成功!',
                })
                this.detailOnClose()
              }
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消修改',
            })
          })
      })
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
    successProjectCover(response, file, fileList) {
      this.detailInformation.projectImage = response
    },
    deleteDetailProjectInformation(row) {
      this.$confirm('此操作将删除该项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          deleteDetailInformationById(row.id, this.configs)
            .then((res) => {
              if (res.data.code === '200') {
                this.onClose()
                this.$message({
                  type: 'success',
                  message: '删除成功!',
                })
              }
            })
            .catch(() => {
              this.$message({
                type: 'error',
                message: '删除失败，请稍后重试',
              })
            })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    editDetailProjectInformation(row) {
      this.detailInformation = Object.assign({}, row)
      this.projectImagefileList = [{ url: row.projectImage }]
      this.studioMemberList = Object.assign(
        [],
        JSON.parse(JSON.stringify(this.studioMemberInitList))
      )
      if (!this.assessmentMode) {
        //说明当前是团队测评
        let information = JSON.parse(row.detailInformation)
        Object.keys(information).forEach((item) => {
          this.editDetailInformationKeys.push({
            name: item,
            value: information[item] + '',
          })
        })
        //整理分数相关数据
        //判断分数是否存在
        let fractionName = [
          'fractionOne',
          'fractionTwo',
          'fractionThree',
          'fractionFour',
          'fractionFive',
          'fractionSix',
          'fractionSeven',
        ]
        this.studioMemberList.forEach((item) => {
          fractionName.forEach((items) => {
            if (row[items] !== undefined) {
              // console.log(row[items].split('_')[0])
              if (item.id === row[items].split('_')[0]) {
                item.fraction = row[items].split('_')[1] + ''
                return false
              }
            }
          })
        })
        // console.log(JSON.stringify(this.studioMemberList))
      } else {
        this.studioMemberList = []
        this.editDetailInformationKeys = []
      }
      this.dialogFormVisible = false
      this.editDetailVisible = true
    },
    handleChange(val) {
      //当前索引位置
      if (val !== '') {
        if (!this.assessmentMode) {
          let nowProject = JSON.parse(this.project[val].detailInformation)
          this.detailInformationKeys = Object.keys(nowProject)
        } else {
          this.detailInformationKeys = []
        }
      }
    },
    onOpen() {
      this.activeNames = undefined
      this.dialogFormVisible = true
    },
    onshow(index, val) {
      findDetailInformation(index, this.configs).then((res) => {
        if (res.data.code === '200') {
          this.dialogFormVisible = true
          this.project = res.data.data
        } else {
          this.$message.error('数据获取失败，请稍后重试！')
        }
      })
      if (val === '团队测评') {
        //默认团队测评
        this.assessmentMode = false
      } else if (val === '个人测评') {
        this.assessmentMode = true
      }
    },
    detailOnClose() {
      this.projectImagefileList = []
      this.dialogFormVisible = true
      this.editDetailVisible = false
      this.detailInformation = this.$data.detailInformation
      this.editDetailInformationKeys = []
      this.studioMemberList = Object.assign(
        [],
        JSON.parse(JSON.stringify(this.studioMemberInitList))
      )
    },
    onClose() {
      this.dialogFormVisible = false
    },
    close() {
      this.dialogFormVisible = false
      this.project = []
    },
    handelConfirm() {
      this.$refs['project'].validate((valid) => {
        if (!valid) return
      })
    },
  },
}
