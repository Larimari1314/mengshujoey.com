import { obtainPublicKey, queryDataByUser } from '@/api/mainIndex'
import { decryptedData, decrypto, encryptoPSW } from '@/utils/encrypt'
import SquareLoader from 'vue-spinner/src/PulseLoader'
export default {
  name: 'CellPhoneIndex',
  data() {
    return {
      publicKey: undefined,
      fullscreenLoading: true,
      levelList: [],
      levelId: undefined,
      text: undefined,
      page: 1,
      queryList: [],
      updateLoadingColor: '#ababff',
      updateLoadingSize: '20px',
      maxPage: 0,
      levelValue: '',
      updateDateLoading: false,
      updateLoadingEmpty: false,
    }
  },
  components: {
    SquareLoader,
  },
  mounted() {
    let levelValue = this.$route.query.levelValue
    let levelId = this.$route.query.levelId
    let text = this.$route.query.text
    this.levelId = levelId
    this.text = decodeURIComponent(text)
    this.levelValue = decodeURIComponent(levelValue)
    if (levelValue !== null && levelValue !== undefined) {
      this.levelList.push(decodeURIComponent(levelValue))
    }
    if (text !== null && text !== undefined) {
      this.levelList.push(decodeURIComponent(text))
    }
    if (this.levelList.length <= 0) {
      this.$router.push({
        path: '/index',
        query: {},
      })
      return false
    }
    setTimeout(() => {
      this.obtainPublicKey()
      this.initQueryData()
      this.fullscreenLoading = false
    }, 2000)
  },
  methods: {
    loadQuery() {
      if (this.updateDateLoading) {
        //当前正在加载过程中
        return false
      }
      //更改为true
      this.updateDateLoading = true
      if (this.page >= this.maxPage) {
        //到达尾部
        this.updateDateLoading = false
        this.updateLoadingEmpty = true
        return false
      }
      this.updateLoadingEmpty = false
      //当前可以加载
      this.page = this.page + 1
      //存储数据
      setTimeout(() => {
        this.initQueryData()
      }, 2000)
    },
    handleClose(item, index) {
      //删除队列
      //判断删除的信息是什么信息
      if (this.levelList.length <= 1) {
        //说明没东西了
        this.$router.push({
          path: '/',
          query: {},
        })
      }
      if (this.text === item) {
        //删除搜索信息
        this.$router
          .push({
            path: '/queryData/queryDataInformation',
            query: {
              levelId: this.$route.query.levelId,
              levelValue: this.$route.query.levelValue,
            },
          })
          .catch(() => {})
        if (this.$route.path === '/queryData/queryDataInformation') {
          location.reload()
        }
      }
      if (this.levelValue === item) {
        //删除标签信息
        this.$router
          .push({
            path: '/queryData/queryDataInformation',
            query: {
              text: this.$route.query.text,
            },
          })
          .catch(() => {})
        if (this.$route.path === '/queryData/queryDataInformation') {
          location.reload()
        }
      }
    },
    clickProject(id) {
      this.$router.push({
        path: '/project/projectInformation/' + id,
        query: {},
      })
    },
    initQueryData() {
      //查询数据
      let queryData = {
        page: this.page,
        pageSize: 5,
      }
      if (this.levelId !== null && this.levelId !== undefined) {
        queryData.levelId = this.levelId
      }
      if (
        this.text !== null &&
        this.text !== undefined &&
        this.text !== 'undefined'
      ) {
        queryData.data = this.text
      }
      if (
        (this.levelId === null || this.levelId === undefined) &&
        (this.text === undefined ||
          this.text === null ||
          this.text === 'undefined' ||
          this.text === '')
      ) {
        this.$router.push({
          path: '/',
          query: {},
        })
        return false
      }
      queryDataByUser(queryData).then((res) => {
        if (res.data.code === '200') {
          let queryLists = decryptedData(this.publicKey, res.data.data).list
          //搜索结果高亮
          queryLists.forEach((item) => {
            item.mainTitle = this.changeColor(item.mainTitle)
            item.projectList.forEach((items) => {
              items.projectName = this.changeColor(items.projectName)
            })
            this.queryList.push(item)
          })
          this.updateDateLoading = false
          this.maxPage = decryptedData(this.publicKey, res.data.data).pages
          // console.log(decryptedData(this.publicKey, res.data.data))
        }
      })
    },
    changeColor(result) {
      //result为接口返回的数据
      if (this.text) {
        /**
         * 使用正则表达式进行全文匹配关键词
         * ig : 表示 全文查找 ,忽略大小写
         *  i : 忽略大小写
         *  g : 全文查找
         * 使用字符串的replace方法进行替换
         * stringObject.replace('被替换的值',替换的值)
         */
        let text = this.text.replace(/\s*/g, '')
        let replaceReg = new RegExp(text, 'ig')
        let replaceString = `<span style="color: #ed4014;font-weight: bold;">${text}</span>`
        result = result.replace(replaceReg, replaceString)
      }
      return result
    },
    obtainPublicKey() {
      let publicLocal = localStorage.getItem('privateKey')
      if (publicLocal === undefined || publicLocal === null) {
        this.updatePublicKey()
      } else {
        //说明存在数据
        let publicObject = JSON.parse(decrypto(publicLocal, 120, 120))
        var now = new Date()
        //经典问题两个日期间是否夹另一个日期
        var datePublic = new Date(publicObject.createDate)
        if (
          datePublic.getMonth() < now.getMonth() ||
          (datePublic.getDay() < 15 && 15 < now.getDay())
        ) {
          //更新
          this.updatePublicKey()
        } else {
          //说明数据没问题
          this.publicKey = publicObject.publicKey
        }
      }
    },
    updatePublicKey() {
      obtainPublicKey().then((res) => {
        if (res.data.code === '200') {
          this.publicKey = res.data.data
          //说明获取到公钥了
          let encryptData = {
            publicKey: res.data.data,
            createDate: new Date(),
          }
          //加密数据
          let result = encryptoPSW(JSON.stringify(encryptData), 120, 120)
          localStorage.setItem('privateKey', result)
        }
      })
    },
  },
}
