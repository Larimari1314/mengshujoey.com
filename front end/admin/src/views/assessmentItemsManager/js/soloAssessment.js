import { addSingleProject } from '@/api/assessmentItems'
import { backendUrl } from '@/config/setting.config'
import { getReferralLevel, tabDropDown } from '@/api/dropDownOption'
import store from '@/store'
export default {
  name: 'TableEdit',
  data() {
    return {
      dialogProjectVisible: false,
      projectTitle: '测评项目',
      projectModel: 1,
      singleProject: {
        videoAddress: '',
        mainTitle: undefined,
        subheading: undefined,
        evaluationDate: new Date(),
        labelId: undefined,
        coverImage: '',
        projectItem: undefined,
      },
      singleProject_rules: {
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
        evaluationDate: [
          {
            required: true,
            message: '请选择测评日期',
            trigger: 'change',
          },
        ],
        labelId: [
          {
            required: true,
            message: '请选择标签',
            trigger: 'change',
          },
        ],
        coverImage: [
          {
            required: true,
            message: '请上传封面',
            trigger: 'change',
          },
        ],
      },
      coverImageAction: backendUrl + 'evaluation/uploadCoverAddress',
      coverImagefileList: [],
      dialogFormVisible: false,
      editTitle: '团队测评项目添加',
      detailTitle: '添加测评项目',
      title: '',
      addProjectButton: false,
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      recommendLevelOptions: [],
      labelIdOptions: [],
      detailProject: {
        projectName: undefined,
        recommendLevel: undefined,
        projectImage: '',
      },
      detailProject_rules: {
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
        projectImage: [
          {
            required: true,
            message: '请上传项目封面',
            trigger: 'change',
          },
        ],
      },
      projectImageAction: backendUrl + 'evaluation/uploadProjectAddress',
      projectImagefileList: [],
      editProjectVisible: false,
      editProjectIndex: -1,
      permissions: store.getters['user/permissions'],
    }
  },
  mounted() {
    if (this.permissions.indexOf('10') !== -1) {
      tabDropDown(this.configs).then((res) => {
        this.labelIdOptions = res.data.data
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
    successProjectCover(response, file, fileList) {
      this.detailProject.projectImage = response
    },
    successCover(response, file, fileList) {
      this.singleProject.coverImage = response
    },
    editProjectInformations(row, index) {
      //修改测评信息的内容
      //数据覆盖给detailProject中
      let thisDetailProject = Object.assign({}, row)
      this.detailProject = thisDetailProject
      this.projectImagefileList = [{ url: thisDetailProject.projectImage }]
      this.editProjectVisible = true
      this.editProjectIndex = index
      this.dialogProjectVisible = true
      this.dialogFormVisible = false
    },
    deleteProjectInformations(row, index) {
      this.$confirm('此操作将永久删除该测评项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          this.singleProject.projectItem.splice(index, 1)
          this.$message.success('移除测评信息成功')
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '取消移除',
          })
        })
    },
    teamAssessment() {},
    submitProjectForm() {
      this.$refs['detailProject'].validate((valid) => {
        if (!valid) return
        let projectItem = Object.assign({}, this.detailProject)
        if (this.editProjectVisible) {
          //当前为修改数据
          this.singleProject.projectItem.splice(
            this.editProjectIndex,
            1,
            projectItem
          )
        } else {
          //当前为未修改数据
          //为项目数据中添加数据
          if (this.singleProject.projectItem === undefined) {
            this.singleProject.projectItem = []
          }
          this.singleProject.projectItem.push(projectItem)
        }
        //返回项目界面
        this.dialogProjectVisible = false
        this.dialogFormVisible = true
        //清空上传文件内文件内容
        this.$refs.projectImage.clearFiles()
      })
    },
    resetProjectForm() {
      this.$refs['detailProject'].resetFields()
      this.$refs.projectImage.clearFiles()
    },
    teamAssessmentProject() {
      this.$refs.projectImage.clearFiles()
      this.dialogProjectVisible = false
      this.dialogFormVisible = true
    },
    addDetailProject() {
      this.detailProject = this.$options.data().detailProject
      this.editProjectIndex = this.$options.data().editProjectIndex
      this.editProjectVisible = false
      this.dialogProjectVisible = true
      this.dialogFormVisible = false
    },
    closeTeam() {},
    showEdit(row) {
      if (!row) {
        this.title = '单人测评项目添加'
      } else {
        this.title = '单人测评项目添加'
      }
      this.singleProject = Object.assign({}, row)
      this.dialogFormVisible = true
    },
    close() {},
    submitForm() {
      this.$refs['singleProject'].validate((valid) => {
        if (!valid) return
        this.addProjectButton = true
        // TODO 提交表单
        // console.log(JSON.stringify(this.singleProject))
        addSingleProject(this.singleProject, this.configs).then((res) => {
          this.addProjectButton = false
          if (res.data.code === '200') {
            //添加成功,关闭页面
            this.$message.success('项目添加成功！，请刷新界面即可看见数据')
            this.dialogFormVisible = false
            this.$refs.coverImage.clearFiles()
          }
        })
      })
    },
    resetForm() {
      this.dialogFormVisible = false
      //清空数据
      this.detailProject = this.$options.data().detailProject
      this.$refs.coverImage.clearFiles()
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
  },
}
