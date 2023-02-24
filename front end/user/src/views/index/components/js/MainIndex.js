import {
  evaluationVideoUpdateCode,
  findAllVideo,
  getLabelValue,
  getMilkTeaEvaluationData,
  milkTeaUpdateCode,
  obtainPublicKey,
} from '@/api/mainIndex'
import { decryptedData, decrypto, encryptoPSW } from '@/utils/encrypt'
import SquareLoader from 'vue-spinner/src/PulseLoader.vue'

export default {
  name: 'PcIndex',
  components: {
    SquareLoader,
  },
  data() {
    return {
      fullLoading: true,
      loveProjectList: [],
      showEmptyVistable: true,
      updateLoadingEmpty: false,
      updateLoadingColor: '#ababff',
      updateLoadingSize: '20px',
      updateDateLoading: true,
      hotClickSrc:
        'https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/webImage/hotClickNumber.png',
      maxPage: undefined,
      page: 1,
      evaluation_page: 1,
      evaluation_max_page: undefined,
      mickTeaList: [],
      publicKey: undefined,
      labelIdOptions: [],
      textAddressVisable: false,
      evaluationBasicList: [],
    }
  },
  created() {
    //更新
    setTimeout(() => {
      //获取公钥
      this.obtainPublicKey()
      //获取更新码
      this.queryMilkCode()
      //获取标签
      this.findLabel()
      //获取奶茶
      this.queryMilkTeaData()
      //查询全部数据
      this.queryAllVideo()
      this.fullLoading = false
    }, 2000)
  },
  methods: {
    queryLabels(value) {
      //跳转页面
      this.$router
        .push({
          path: '/queryData/queryDataInformation',
          query: {
            levelId: value.value,
            levelValue: encodeURIComponent(value.label),
          },
        })
        .catch(() => {})
    },
    clickProject(id) {
      //跳转页面
      this.$router.push({
        path: '/project/projectInformation/' + id,
        query: {},
      })
    },
    findLabel() {
      getLabelValue().then((res) => {
        if (res.data.code === '200') {
          let labelEncrpt = res.data.data
          this.labelIdOptions = decryptedData(this.publicKey, labelEncrpt)
        }
      })
    },
    loadQueryVideo() {
      if (this.updateDateLoading === true) {
        //表示当前正在加载中
        return true
      }
      this.updateDateLoading = true
      this.updateLoadingEmpty = false
      //判断是否超出页码
      if (this.evaluation_page >= this.evaluation_max_page) {
        //超出页数，给出提示
        this.updateDateLoading = false
        this.updateLoadingEmpty = true
        if (this.showEmptyVistable) {
          this.$message.warning(this.$t('HintText.mainEvaluationEmpty'))
        }
        this.updateLoadingEmpty = false
        this.showEmptyVistable = false
        return false
      }
      this.evaluation_page = this.evaluation_page + 1
      //没有超出限制
      setTimeout(() => {
        this.queryAllVideo()
      }, 2000)
    },
    queryAllVideo() {
      //查询缓存数据
      let item = localStorage.getItem('evaluationVideo')
      let boolEvaluation = true
      if (item !== null) {
        boolEvaluation =
          JSON.parse(decrypto(item, 100, 30))[this.evaluation_page] === null ||
          JSON.parse(decrypto(item, 100, 30))[this.evaluation_page] ===
            undefined
      }
      if (item === null || boolEvaluation || this.evaluation_page > 10) {
        //查询数据
        let queryData = {
          page: this.evaluation_page,
        }
        findAllVideo(queryData).then((res) => {
          this.updateDateLoading = false
          if (res.data.code === '200') {
            //解密
            let evaluationBasicList = decryptedData(
              this.publicKey,
              res.data.data
            )
            let list = evaluationBasicList.list
            list.forEach((item) => {
              this.evaluationBasicList.push(item)
            })
            this.evaluation_max_page = evaluationBasicList.pages
            if (this.evaluation_page <= 10) {
              this.updateDateLoading = false
              if (item !== null) {
                //解码
                let itemDecrypto = JSON.parse(decrypto(item, 100, 30))
                let pages = this.evaluation_page
                itemDecrypto[pages] = evaluationBasicList
                //加密
                localStorage.setItem(
                  'evaluationVideo',
                  encryptoPSW(JSON.stringify(itemDecrypto), 100, 30)
                )
              } else {
                let pages = this.evaluation_page
                //说明是空的
                let nullItem = {}
                nullItem[pages] = evaluationBasicList
                localStorage.setItem(
                  'evaluationVideo',
                  encryptoPSW(JSON.stringify(nullItem), 100, 30)
                )
              }
            }
          } else if (res.data.code === '400') {
            //提示用户
            this.$message.error(this.$t('HintText.viewNotInit'))
            //删除页面存储的加密信息
            localStorage.removeItem('privateKey')
          }
        })
      } else {
        //解码
        let itemDecrypto = JSON.parse(decrypto(item, 100, 30))
        itemDecrypto[this.evaluation_page].list.forEach((item) => {
          this.evaluationBasicList.push(item)
        })
        this.updateDateLoading = false
        this.evaluation_max_page = itemDecrypto[this.evaluation_page].pages
      }
    },
    queryMilkCode() {
      //检测缓存是否过期
      let updateCode = localStorage.getItem('mainMilkTeaCode')
      milkTeaUpdateCode().then((res) => {
        if (res.data.code === '200') {
          //获取更新码对比
          if (updateCode === null || updateCode === undefined) {
            localStorage.setItem('mainMilkTeaCode', res.data.data)
          } else {
            if (updateCode !== res.data.data) {
              //如果奶茶更新码对不上相当于更新了则删除界面缓存数据
              localStorage.removeItem('mainMilkTea')
              //更新更新码
              localStorage.setItem('mainMilkTeaCode', res.data.data)
            }
          }
        }
      })
      //首页更新视频更新码
      let videoCode = localStorage.getItem('evaluationVideoCode')
      evaluationVideoUpdateCode().then((res) => {
        if (res.data.code === '200') {
          //获取更新码对比
          if (videoCode === null || videoCode === undefined) {
            localStorage.setItem('evaluationVideoCode', res.data.data)
          } else {
            if (videoCode !== res.data.data) {
              //如果奶茶更新码对不上相当于更新了则删除界面缓存数据
              localStorage.removeItem('evaluationVideo')
              //更新更新码
              localStorage.setItem('evaluationVideoCode', res.data.data)
            }
          }
        }
      })
    },
    switchCardsMikeTea() {
      //修改页码
      this.page = this.page + 1
      if (this.page > this.maxPage) {
        this.page = 1
      }
      this.queryMilkTeaData()
    },
    queryMilkTeaData() {
      try {
        let item = localStorage.getItem('mainMilkTea')
        let booleanMilk = true
        if (item !== null) {
          booleanMilk =
            JSON.parse(decrypto(item, 100, 25))[this.page] === null ||
            JSON.parse(decrypto(item, 100, 25))[this.page] === undefined
        }
        // console.log(booleanMilk)
        if (item === null || booleanMilk || this.page > 10) {
          //从缓存中拿取数据
          let queryData = {
            page: this.page,
          }
          getMilkTeaEvaluationData(queryData).then((res) => {
            if (res.data.code === '200') {
              //解密
              let arrayBufferPromiseLike = decryptedData(
                this.publicKey,
                res.data.data
              )
              this.mickTeaList = arrayBufferPromiseLike.list
              this.maxPage = arrayBufferPromiseLike.pages
              if (this.page <= 10) {
                if (item !== null) {
                  //解码
                  let itemDecrypto = JSON.parse(decrypto(item, 100, 25))
                  let pages = this.page
                  itemDecrypto[pages] = arrayBufferPromiseLike
                  //加密
                  localStorage.setItem(
                    'mainMilkTea',
                    encryptoPSW(JSON.stringify(itemDecrypto), 100, 25)
                  )
                } else {
                  let pages = this.page
                  //说明是空的
                  let nullItem = {}
                  nullItem[pages] = arrayBufferPromiseLike
                  localStorage.setItem(
                    'mainMilkTea',
                    encryptoPSW(JSON.stringify(nullItem), 100, 25)
                  )
                }
              }
            } else if (res.data.code === '400') {
              //提示用户
              this.$message.error(this.$t('HintText.viewNotInit'))
              //删除页面存储的加密信息
              localStorage.removeItem('privateKey')
            }
          })
        } else {
          //解码
          let itemDecrypto = JSON.parse(decrypto(item, 100, 25))
          // console.log(JSON.stringify(itemDecrypto))
          // console.log(this.page)
          this.mickTeaList = itemDecrypto[this.page].list
          this.maxPage = itemDecrypto[this.page].pages
        }
      } catch (e) {
        //删除页面存储的加密信息
        localStorage.removeItem('privateKey')
        this.$message.error('数据更新出错，请稍后重试！')
      }
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
