import { backendUrl } from '@/config/setting.config'
import { editBasicInformation } from '@/api/assessmentItems'
import { tabDropDown } from '@/api/dropDownOption'
import store from '@/store'
export default {
  name: 'editEvaluatiomInformation',
  data() {
    return {
      editEvaluationDis: false,
      project: {
        videoAddress: undefined,
        mainTitle: undefined,
        subheading: undefined,
        evaluationDateCode: undefined,
        coverImage: null,
        evaluationImage: null,
        labelId: undefined,
      },
      project_rules: {
        labelId: [
          {
            required: true,
            message: '请选择标签地址',
            trigger: 'blur',
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
        evaluationDateCode: [
          {
            required: true,
            message: '请输入测评时间',
            trigger: 'blur',
          },
        ],
        coverImage: [
          {
            required: true,
            message: '请上传封面图片',
            trigger: 'blur',
          },
        ],
        evaluationImage: [
          {
            required: true,
            message: '请输入上传测评图片',
            trigger: 'blur',
          },
        ],
      },
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      evaluationImageVisible: false,
      dialogFormVisible: false,
      coverImageAction: backendUrl + 'evaluation/uploadCoverAddress',
      coverImagefileList: [],
      evaluationImageAction: backendUrl + 'evaluation/uploadEvaluationAddress',
      evaluationImagefileList: [],
      labelIdOptions: [],
      permissions: store.getters['user/permissions'],
    }
  },
  created() {
    if (this.permissions.indexOf('10') !== -1) {
      tabDropDown(this.configs).then((res) => {
        this.labelIdOptions = res.data.data
      })
    }
  },
  methods: {
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      )
    },
    onOpen() {
      this.dialogFormVisible = true
      this.project = this.$data.project
    },
    onShow(row) {
      this.project = Object.assign({}, row)
      this.coverImagefileList = [{ url: row.coverImage }]
      if (row.scoringClass === '团队测评') {
        this.evaluationImageVisible = true
        this.evaluationImagefileList = [{ url: row.evaluationImage }]
      } else {
        this.evaluationImageVisible = false
        this.project.evaluationImage = this.project.coverImage
      }
      this.dialogFormVisible = true
    },
    onClose() {
      this.project = this.$data.project
      this.dialogFormVisible = false
    },
    close() {
      this.project = this.$data.project
      this.dialogFormVisible = false
    },
    handelConfirm() {
      this.$refs['project'].validate((valid) => {
        if (!valid) return
        this.editEvaluationDis = true
        //判断当前是否是团队测评和个人测评
        if (this.project.scoringClass === '团队测评') {
          this.project.scoringClass = false
        } else if (this.project.scoringClass === '个人测评') {
          this.project.scoringClass = true
        }
        editBasicInformation(this.project, this.configs).then((res) => {
          this.editEvaluationDis = false
          if (res.data.code === '200') {
            //更新成功
            this.$message.success('更新项目基本信息成功!')
            this.close()
          } else {
            this.$message.success('更新项目失败，请稍后重试!')
            this.close()
          }
        })
        // console.log(JSON.stringify(this.project))
      })
    },
    successUploadCoverImage(response, file, fileList) {
      this.project.coverImage = response
    },
    successUploadEvaluationImage(response, file, fileList) {
      this.project.evaluationImage = response
    },
    coverImageBeforeUpload(file) {
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
    evaluationImageBeforeUpload(file) {
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
  },
}
