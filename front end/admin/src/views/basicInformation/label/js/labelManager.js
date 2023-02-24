import { getAllBasicMainTitle, tabDropDown } from '@/api/dropDownOption'
import {
  addLabelData,
  deleteLabelById,
  editLabelData,
  queryLabelData,
} from '@/api/labelApi'
import store from '@/store'

export default {
  name: 'labelManager',
  data() {
    return {
      editTitle: '修改标签',
      dialogEditFormVisible: false,
      labelEditFrom: {},
      labelAddFrom: {},
      labelAddFrom_rules: {
        labelValue: [
          {
            required: true,
            message: '请输入标签值',
            trigger: 'blur',
          },
        ],
      },
      labelEditFrom_rules: {
        value: [
          {
            required: true,
            message: '请输入标签值',
            trigger: 'blur',
          },
        ],
      },
      addTitle: '添加标签',
      dialogFormVisible: false,
      labelIdOptions: [],
      configs: {
        headers: {
          token: sessionStorage.getItem('accessTokens'),
        },
      },
      queryData: {
        labelId: '',
      },
      pageSize: 5,
      page: 1,
      background: true,
      queryFormageSize: 1,
      total: 0,
      labelData: [],
      multipleSelection: [],
      basicMainTitleDropDown: [],
      permissions: store.getters['user/permissions'],
    }
  },
  created() {
    if (
      this.permissions.indexOf('5') !== -1 ||
      this.permissions.indexOf('15') !== -1
    ) {
      this.queryLabelData()
    }
    if (this.permissions.indexOf('10') !== -1) {
      tabDropDown(this.configs).then((res) => {
        this.labelIdOptions = res.data.data
      })
    }
  },
  methods: {
    editThisLabel(row) {
      this.dialogEditFormVisible = true
      this.labelEditFrom = Object.assign({}, row)
    },
    submitEditLabelForm() {
      let editFromData = {
        id: this.labelEditFrom.id,
        value: this.labelEditFrom.value,
      }
      editLabelData(editFromData, this.configs).then((res) => {
        if (res.data.code === '200') {
          this.$message({
            type: 'success',
            message: '修改标签数据成功',
          })
          this.onEditlabelClose()
          this.queryLabelData()
        }
      })
    },
    onEditlabelClose() {
      this.labelEditFrom = {}
      this.dialogEditFormVisible = false
    },
    deleteLabel() {
      this.$confirm('此操作将永久删除该测评项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          let bingValue = false
          let ids = []
          this.multipleSelection.forEach((item) => {
            //检测删除条件
            if (item.labelList.length !== 0) {
              //说明当前有数据绑定
              this.$message({
                type: 'error',
                message: '当前删除的数据存在项目绑定值，不可删除',
              })
              bingValue = true
            }
            ids.push(item.id)
          })
          if (!bingValue) {
            deleteLabelById(ids, this.configs).then((res) => {
              if (res.data.code === '200') {
                //删除成功
                this.$message({
                  type: 'success',
                  message: '删除成功',
                })
                this.queryLabelData()
              }
            })
          } else {
            this.multipleSelection = []
          }
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          })
        })
    },
    submitAddLabelForm() {
      this.$refs['labelAddFromRef'].validate((valid) => {
        if (!valid) return
        //提交添加数据
        addLabelData(
          { value: this.labelAddFrom.labelValue },
          this.configs
        ).then((res) => {
          if (res.data.code === '200') {
            this.$message({
              type: 'success',
              message: '添加数据成功',
            })
            this.queryLabelData()
            this.onAddlabelClose()
          }
        })
      })
    },
    onAddlabelClose() {
      this.labelAddFrom = {}
      this.dialogFormVisible = false
    },
    addLabel() {
      this.dialogFormVisible = true
    },
    getAllBasicMainTitleDropDown() {
      getAllBasicMainTitle(this.configs).then((res) => {
        this.basicMainTitleDropDown = res.data.data
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    queryLabelData() {
      let queryData = {
        page: this.page,
        value: this.queryData.labelId + '',
      }
      queryLabelData(queryData, this.configs).then((res) => {
        this.total = res.data.data.total
        this.labelData = res.data.data.list
      })
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
