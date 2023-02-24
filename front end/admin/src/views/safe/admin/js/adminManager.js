import store from '@/store'
import {
  addAdmin,
  changeAdminStatus,
  editAdminInformation,
  findAdminByName,
  getRoleList,
  resetPassword,
} from '@/api/adminApi'
import { getSexList } from '@/api/dropDownOption'
import { backendUrl } from '@/config/setting.config'

export default {
  name: 'adminManager',
  data() {
    return {
      adminAddInfo: {
        name: undefined,
        avatar:
          'https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/15922_100.gif',
        sex: undefined,
        birthday: '',
        email: undefined,
        password: undefined,
        permissions: undefined,
      },
      adminAddInfo_rules: {
        name: [
          {
            required: true,
            message: '请输入昵称',
            trigger: 'blur',
          },
        ],
        sex: [
          {
            required: true,
            message: '请选择性别',
            trigger: 'change',
          },
        ],
        birthday: [
          {
            required: true,
            message: '请选择出生日期',
            trigger: 'change',
          },
        ],
        email: [
          {
            required: true,
            message: '请输入登录邮件',
            trigger: 'blur',
          },
          {
            pattern: /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
            message: '邮件格式错误',
            trigger: 'blur',
          },
        ],
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
          {
            pattern: /^[a-zA-Z0-9_-]{6,18}$/,
            message: '请按要求输入6-18位由字母、数字、下划线构成的密码',
            trigger: 'blur',
          },
        ],
        permissions: [
          {
            required: true,
            message: '请选择控制角色',
            trigger: 'change',
          },
        ],
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
      },
      dialogAddAdminVisible: false,
      dialogFormVisible: false,
      editTitle: '修改管理员信息',
      adminList: [],
      pageSize: 4,
      page: 1,
      background: true,
      queryFormageSize: 1,
      total: 0,
      queryDataDialog: false,
      addFormDialog: false,
      queryData: {},
      //['7':全部权限, '23':查, '24'：增, '25'：删, '26'：修改]
      permissions: store.getters['user/permissions'],
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      editAdmininfo: {
        name: undefined,
        email: undefined,
        birthday: null,
        avatar: null,
        permissions: undefined,
        sex: undefined,
      },
      editAdmininfo_rules: {
        name: [
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
        ],
        email: [
          {
            required: true,
            message: '请输入电子邮件',
            trigger: 'blur',
          },
          {
            pattern: /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
            message: '请输入正确的电子邮件地址',
            trigger: 'blur',
          },
        ],
        birthday: [
          {
            required: true,
            message: '请选择出生日期',
            trigger: 'change',
          },
        ],
        permissions: [
          {
            required: true,
            message: '请选择角色',
            trigger: 'change',
          },
        ],
        sex: [
          {
            required: true,
            message: '请选择性别',
            trigger: 'change',
          },
        ],
      },
      avatarAction: backendUrl + 'adminManager/uploadAvatarAddress',
      addAvatarFileList: [
        {
          url: 'https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/15922_100.gif',
        },
      ],
      editFormDialog: false,
      avatarfileList: [],
      permissionsOptions: [],
      sexOptions: [],
      resetPasswordVisible: false,
      resetPassowrdDis: false,
      resetPassword: {
        password: undefined,
        id: undefined,
      },
      resetPassword_rules: {
        password: [
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
          {
            pattern: /^[a-zA-Z0-9_-]{6,18}$/,
            message: '请按要求输入6-18位由字母、数字、下划线构成的密码',
            trigger: 'blur',
          },
        ],
      },
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      adminInitList: [],
    }
  },
  created() {
    this.queryAdminData()
    getSexList(this.configs).then((res) => {
      if (res.data.code === '200') {
        this.sexOptions = res.data.data
      }
    })
    getRoleList(this.configs).then((res) => {
      if (res.data.code === '200') {
        this.permissionsOptions = res.data.data
      }
    })
  },
  methods: {
    changeSwith(id, status) {
      this.$confirm('此操作将禁用该管理账号，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          let statusCode
          if (status === 'AC001') {
            statusCode = true
          } else {
            statusCode = false
          }
          changeAdminStatus(id, statusCode, this.configs).then((res) => {
            if (res.data.code === '200') {
              this.$message({
                type: 'success',
                message: '修改管理员状态成功!',
              })
              this.queryAdminData()
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消禁用',
          })
          this.adminList = JSON.parse(JSON.stringify(this.adminInitList))
        })
    },
    submitAddForm() {
      this.$refs['adminAddInfo'].validate((valid) => {
        if (!valid) return
        this.addFormDialog = true
        //上传添加信息
        addAdmin(this.adminAddInfo, this.configs)
          .then((res) => {
            this.addFormDialog = false
            if (res.data.code === '200') {
              this.queryAdminData()
              this.onAddAdminClose()
              this.$message({
                message: '添加管理员信息成功！',
                type: 'success',
              })
            } else if (res.data.code === '304') {
              this.$message({
                message: '当前邮件已经被注册！',
                type: 'error',
              })
            }
          })
          .catch((res) => {
            this.addFormDialog = false
          })
      })
    },
    uploadAddAvatarSuccess(response, file, fileList) {
      this.adminAddInfo.avatar = response
    },
    uploadAvatarSuccess(response, file, fileList) {
      this.editAdmininfo.avatar = response
    },
    onAddAdminClose() {
      //关闭窗口
      this.dialogAddAdminVisible = false
      this.adminAddInfo = {
        name: undefined,
        avatar:
          'https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/15922_100.gif',
        sex: undefined,
        birthday: '',
        email: undefined,
        password: undefined,
        permissions: undefined,
      }
    },
    resetPasswordClose() {
      this.resetPassword = {}
      this.resetPasswordVisible = false
    },
    resetPasswordRoleInfo(row) {
      //重置密码
      this.resetPasswordVisible = true
      this.resetPassword.id = row.id
    },
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      )
    },
    submitFormPassword() {
      this.$refs['resetPassword'].validate((valid) => {
        if (!valid) return
        this.resetPassowrdDis = true
        // TODO 提交表单
        resetPassword(
          this.resetPassword.id,
          { value: this.resetPassword.password },
          this.configs
        )
          .then((res) => {
            this.resetPassowrdDis = false
            if (res.data.code === '200') {
              this.resetPasswordClose()
              this.$message({
                message: '重置密码成功！',
                type: 'success',
              })
            }
          })
          .catch((res) => {
            this.resetPassowrdDis = false
          })
      })
    },
    submitForm() {
      this.$refs['editAdmininfo'].validate((valid) => {
        if (!valid) return
        this.editFormDialog = true
        // TODO 提交表单
        editAdminInformation(this.editAdmininfo, this.configs)
          .then((res) => {
            this.editFormDialog = false
            if (res.data.code === '200') {
              //修改成功
              this.$message({
                message: '修改信息成功',
                type: 'success',
              })
              this.onEditAdminClose()
              this.queryAdminData()
            }
          })
          .catch((res) => {
            this.editFormDialog = false
          })
      })
    },
    resetForm() {
      this.$refs['editAdmininfo'].resetFields()
    },
    avatarBeforeUpload(file) {
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
    onEditAdminClose() {
      this.dialogFormVisible = false
    },
    queryAdminData() {
      //查询数据
      this.queryDataDialog = true
      let queryData = {
        page: this.page,
        value: this.queryData.value,
      }
      findAdminByName(queryData, this.configs)
        .then((res) => {
          this.queryDataDialog = false
          if (res.data.code === '200') {
            let data = res.data.data.list
            data.forEach((item) => {
              if (item.status === 'AC001') {
                item.statusCode = true
              } else if (item.status === 'AC002') {
                item.statusCode = false
              }
            })
            this.adminList = data
            this.adminInitList = JSON.parse(JSON.stringify(data))
            this.total = res.data.data.total
          }
        })
        .catch(() => {
          this.queryDataDialog = true
        })
    },
    addAdminInfo() {
      this.dialogAddAdminVisible = true
    },
    handleCurrentChange(val) {
      //分页逻辑
      this.page = val
      this.queryAdminData()
    },
    editRoleInfo(row) {
      this.avatarfileList = [{ url: row.avatar }]
      this.editAdmininfo = Object.assign({}, row)
      this.dialogFormVisible = true
    },
    handleSizeChange(val) {
      this.page = val
      this.queryAdminData()
    },
  },
}
