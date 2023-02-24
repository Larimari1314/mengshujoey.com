import {
  addRoleInfo,
  editRoleInfo,
  findModuleInfo,
  findRoleList,
} from '@/api/roleApi'

export default {
  name: 'roleManager',
  data() {
    return {
      editRoleInfoDialog: false,
      addRoleInfoDialog: false,
      queryDataDialog: false,
      dialogEditRoleVisible: false,
      addRole: {},
      editRole: {},
      addRole_rule: {
        name: [
          {
            required: true,
            message: '请输入角色名称',
            trigger: 'blur',
          },
        ],
      },
      editRole_rule: {
        name: [
          {
            required: true,
            message: '请输入角色名称',
            trigger: 'blur',
          },
        ],
      },
      addTitle: '添加角色',
      dialogFormVisible: false,
      editModuleList: [],
      moduleList: [],
      roleList: [],
      queryData: {
        value: undefined,
      },
      pageSize: 4,
      page: 1,
      background: true,
      queryFormageSize: 1,
      total: 0,
      moduleData: [],
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
    }
  },
  created() {
    this.queryLabelData()
    findModuleInfo(this.configs).then((res) => {
      let data = res.data.data
      this.editModuleList = JSON.parse(JSON.stringify(data))
      data.forEach((item) => {
        if (item.children.length === 0) {
          delete item.children
        } else {
          item.children.forEach((items) => {
            if (items.children === null) {
              delete items.children
            }
          })
        }
        item.disabled = true
      })
      this.moduleData = data
    })
  },
  methods: {
    nodeUtils(nodes) {
      let deleteId = []
      nodes.forEach((node) => {
        //检测当前选中是否存在父节点
        let parentId = undefined
        this.editModuleList.forEach((item) => {
          if (node === item.id) {
            return false
          }
          parentId = item.id
          if (item.children.length !== 0) {
            //说明存在节点
            item.children.forEach((chidrens) => {
              if (node === chidrens.id) {
                //表示当前是子节点，需检查父节点是否存在
                if (nodes.indexOf(parentId) !== -1) {
                  //表示当前父节点存在，记录删除的子节点
                  deleteId.push(node)
                }
              }
            })
          }
        })
      })
      deleteId.forEach((item) => {
        //删除子节点
        nodes.splice(nodes.indexOf(item), 1)
      })
      return nodes
    },
    nodeClick(list, li) {
      this.$message({
        message: '当前为展示数据，修改无法生效',
        type: 'warning',
      })
    },
    editRoleInfo(row) {
      this.editRoleInfoDialog = false
      this.addRoleInfoDialog = false
      this.queryDataDialog = false
      this.editRole = Object.assign({}, row)
      this.dialogEditRoleVisible = true
    },
    submitEditForm() {
      this.$refs['editRole'].validate((valid) => {
        if (!valid) return
        this.editRoleInfoDialog = true
        let nodes = this.$refs.editRoleTree.getCheckedKeys()
        let node = this.nodeUtils(nodes)
        //获取删除之后数据
        let addData = {
          id: this.editRole.id,
          name: this.editRole.name,
          moduleList: node,
        }
        editRoleInfo(addData, this.configs)
          .then((res) => {
            this.editRoleInfoDialog = false
            if (res.data.code === '200') {
              //表示当前修改成功
              this.$message({
                message: '角色修改成功',
                type: 'success',
              })
              this.onEditRoleClose()
              this.queryLabelData()
            }
          })
          .catch((data) => {
            this.editRoleInfoDialog = false
          })
      })
    },
    submitForm() {
      this.$refs['addRole'].validate((valid) => {
        if (!valid) return
        // TODO 提交表单
        this.addRoleInfoDialog = true
        let node = this.$refs.editTree.getCheckedKeys()
        let nodes = this.nodeUtils(node)
        let roleInfo = {
          name: this.addRole.name,
          moduleList: nodes,
        }
        addRoleInfo(roleInfo, this.configs)
          .then((res) => {
            this.addRoleInfoDialog = false
            if (res.data.code === '200') {
              this.$message({
                message: '角色添加成功',
                type: 'success',
              })
              this.onAddRoleClose()
              this.queryLabelData()
            }
          })
          .catch((data) => {
            this.this.addRoleInfoDialog = false
          })
      })
    },
    onEditRoleClose() {
      this.dialogEditRoleVisible = false
      this.editRole = {}
      this.$refs.editRoleTree.setCheckedKeys([])
    },
    onAddRoleClose() {
      this.dialogFormVisible = false
      this.editRole = {}
      this.$refs.editTree.setCheckedKeys([])
    },
    queryLabelData() {
      this.queryDataDialog = true
      //查询数据
      let queryData = {
        page: this.page,
        value: this.queryData.value,
      }
      findRoleList(queryData, this.configs).then((res) => {
        this.queryDataDialog = false
        if (res.data.code === '200') {
          this.roleList = res.data.data.list
          this.total = res.data.data.total
        }
      })
    },
    addRoleInfo() {
      this.editRoleInfoDialog = false
      this.addRoleInfoDialog = false
      this.queryDataDialog = false
      this.addRole = {}
      this.dialogFormVisible = true
    },
    handleCurrentChange(val) {
      //分页逻辑
      this.page = val
      this.queryLabelData()
    },
    handleSizeChange(val) {
      this.page = val
      this.queryLabelData()
    },
  },
}
