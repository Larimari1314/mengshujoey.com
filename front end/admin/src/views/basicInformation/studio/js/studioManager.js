import {
  addStudioMember,
  deleteStudioMember,
  editStudioMember,
  queryStudioData,
} from '@/api/studioApi'
import { backendUrl } from '@/config/setting.config'
import store from '@/store'

export default {
  name: 'studioManager',
  data() {
    return {
      dialogEditVisible: false,
      lazy: true,
      pageSize: 5,
      total: 0,
      queryFormageSize: 1,
      background: true,
      page: 1,
      StudioData: [],
      queryData: {
        studioName: undefined,
      },
      editStudioInformation: {},
      editStudioInformation_rules: {
        memberName: [
          {
            required: true,
            message: '请输入成员名称',
            trigger: 'blur',
          },
        ],
        memberAvatar: [
          {
            required: true,
            message: '请上传成员头像',
            trigger: 'blur',
          },
        ],
      },
      multipleSelection: [],
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      studioInformation: {
        memberName: undefined,
        memberAvatar: null,
      },
      dialogFormVisible: false,
      studioInformation_rules: {
        memberName: [
          {
            required: true,
            message: '请输入成员名称',
            trigger: 'blur',
          },
        ],
        memberAvatar: [
          {
            required: true,
            message: '请上传成员头像',
            trigger: 'blur',
          },
        ],
      },
      memberAvatarAction: backendUrl + 'studioManage/uploadMemberAvatarAddress',
      memberAvatarfileList: [],
      editMemberAvatarfileList: [],
      imageConfig: { token: sessionStorage.getItem('accessTokens') },
      permissions: store.getters['user/permissions'],
    }
  },
  created() {
    if (
      this.permissions.indexOf('6') !== -1 ||
      this.permissions.indexOf('19') !== -1
    ) {
      this.queryStudioData()
    }
  },
  methods: {
    onEditClose() {
      this.$refs.memberEditAvatar.clearFiles()
      this.dialogEditVisible = false
    },
    deleteStudioByIds() {
      this.$confirm('此操作将删除该工作室成员, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          let ids = []
          this.multipleSelection.forEach((item) => {
            ids.push(item.id)
          })

          deleteStudioMember(ids, this.configs).then((res) => {
            if (res.data.code === '200') {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.queryStudioData()
            } else {
              this.$message({
                type: 'error',
                message: '删除失败，待删除的成员已经测评过项目',
              })
            }
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    addStudio() {
      this.studioInformation = []
      this.dialogFormVisible = true
    },
    onClose() {
      this.$refs.memberAvatar.clearFiles()
      this.studioInformation = []
      this.dialogFormVisible = false
    },
    handelConfirm() {
      this.$refs['studioInformation'].validate((valid) => {
        if (!valid) return
        let submitData = {
          memberName: this.studioInformation.memberName,
          memberAvatar: this.studioInformation.memberAvatar,
        }
        addStudioMember(submitData, this.configs).then((res) => {
          if (res.data.code === '200') {
            this.onClose()
            his.$message.success('添加成员成功')
            this.queryStudioData()
          }
        })
      })
    },
    editHandelConfirm() {
      this.$refs['editStudioInformation'].validate((valid) => {
        if (!valid) return
        editStudioMember(this.editStudioInformation, this.configs).then(
          (res) => {
            if (res.data.code === '200') {
              this.queryStudioData()
              this.onEditClose()
              this.$message({
                type: 'success',
                message: '修改成员信息成功!',
              })
            }
          }
        )
      })
    },
    successEditUploadStudioImage(response, file, fileList) {
      this.editStudioInformation.memberAvatar = response
    },
    successUploadStudioImage(response, file, fileList) {
      this.studioInformation.memberAvatar = response
    },
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      )
    },
    memberAvatarBeforeUpload(file) {
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
    queryStudioData() {
      let queryData = {
        page: this.page,
        value: this.queryData.studioName,
      }
      queryStudioData(queryData, this.configs).then((res) => {
        this.StudioData = res.data.data.list
        this.total = res.data.data.total
      })
    },
    editThisStudio(row) {
      this.dialogEditVisible = true
      this.editStudioInformation = row
      this.editMemberAvatarfileList = [{ url: row.memberAvatar }]
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleCurrentChange(val) {
      //分页逻辑
      this.page = val
      this.queryStudioData()
    },
    handleSizeChange(val) {
      this.page = val
      this.queryStudioData()
    },
  },
}
