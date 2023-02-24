import { decryptedData, decrypto, encryptoPSW } from '@/utils/encrypt'
import {
  obtainPublicKey,
  queryEvaluationBasic,
  queryStudioMember,
} from '@/api/mainIndex'
import * as echarts from 'echarts'

export default {
  name: 'CellPhoneIndex',
  components() {},
  data() {
    return {
      childProject: false,
      evaluationChildChart: {
        yAxis: {
          type: 'value',
        },
        xAxis: {
          type: 'category',
          //列
          data: [],
          axisLabel: {
            interval: 0,
            // rotate: -20,
            formatter: undefined,
            margin: 10,
            rich: {},
          },
          inverse: true,
        },
        series: [
          {
            name: 'hill',
            type: 'bar',
            barWidth: '30%',
            barCategoryGap: '30%',
            data: [],
            z: 10,
          },
          {
            name: 'glyph',
            type: 'pictorialBar',
            barGap: '-100%',
            symbolPosition: 'end',
            symbolSize: 50,
            symbolOffset: [0, '-120%'],
            data: [],
          },
        ],
      },
      showViewImage: false,
      fullscreenLoading: true,
      publicKey: undefined,
      projectItem: {
        projectList: [
          {
            detailInformation: '{}',
          },
        ],
        evaluationDate: '',
      },
      studioMember: [],
      itemChildList: { detailInformation: '{}' },
      drawerList: {},
    }
  },
  created() {
    setTimeout(() => {
      this.fullscreenLoading = false
      this.obtainPublicKey()
      this.queryStudio()
      let projectId = this.$route.params.id
      if ('horizontal' == this.layout) {
        this.initProject(projectId)
      } else {
        this.initProject(projectId)
      }
    }, 2000)
  },
  methods: {
    clickProject(data) {
      this.itemChildList = data
      if (this.projectItem.mode === 2) {
        this.childProject = true
        this.drawerList = data
        //激活echars
        // console.log(data)
        let strings = Object.keys(data)
        let dataList = []
        let studioNameList = []
        let dataItems = []
        let studioList = {
          value: {
            lineHeight: 30,
            align: 'center',
          },
        }
        let chars = JSON.parse(JSON.stringify(this.evaluationChildChart))
        strings.forEach((res) => {
          this.studioMember.forEach((item) => {
            //获取id
            if (res.split('fraction').length >= 2) {
              if (item.id == data[res].split('_')[0]) {
                //说明对上了
                let dataitem = {
                  value: data[res].split('_')[1],
                  symbol: 'image://' + item.avatar,
                  symbolSize: [30, 30],
                }
                dataItems.push(dataitem)
                dataList.push(data[res].split('_')[1])
                studioNameList.push(item.name)
                studioList[item.id] = {
                  height: 30,
                  align: 'center',
                  backgroundColor: {
                    image: item.avatar,
                  },
                }
              }
            }
          })
        })
        chars.xAxis.data = studioNameList
        chars.xAxis.axisLabel.rich = studioList
        chars.series[0].data = dataList
        chars.series[1].data = dataItems
        // console.log(chars)
        setTimeout(() => {
          var myChartPillar = echarts.init(
            document.getElementById('evaluateChild')
          )
          myChartPillar.setOption(chars)
        }, 1000)
      }
    },
    queryStudio() {
      queryStudioMember().then((res) => {
        if (res.data.code === '200') {
          //解密数据
          this.studioMember = decryptedData(this.publicKey, res.data.data)
          // console.log(this.studioMember)
        }
      })
    },
    handleClose() {
      this.childProject = false
    },
    cleanNowData() {
      //删除当前页的缓存信息
      let projectItem = JSON.parse(
        decrypto(localStorage.getItem('projectList'), 120, 21)
      )
      //删除页面信息
      delete projectItem[this.projectItem.id]
      // console.log(projectItem)
      //存储到页面中
      localStorage.setItem(
        'projectList',
        encryptoPSW(JSON.stringify(projectItem), 120, 21)
      )
      this.$message({
        message: this.$t('mainIndex.cleanDataPrompy'),
        type: 'warning',
      })
      setTimeout(() => {
        location.reload()
      }, 1000)
      //刷新页面
    },
    initProject(id) {
      //初始化界面
      //缓存中查询信息
      let projectList = localStorage.getItem('projectList')
      let projectBoolean = true
      if (projectList !== null) {
        let projectItem = JSON.parse(decrypto(projectList, 120, 21))
        if (projectItem[id] !== undefined && projectItem[id] !== null) {
          projectBoolean = false
          this.projectItem = projectItem[id]
          // console.log(this.projectItem)
          return true
        }
      }
      if (projectBoolean) {
        //查询信息
        queryEvaluationBasic(id).then((res) => {
          if (res.data.code === '200') {
            this.projectItem = decryptedData(this.publicKey, res.data.data)
            //检查数据是否存在
            if (projectList !== null) {
              //存在
              let projectItem = JSON.parse(decrypto(projectList, 120, 21))
              //判断对象个数
              if (Object.keys(projectItem).length <= 10) {
                projectItem[id] = decryptedData(this.publicKey, res.data.data)
              } else {
                //超出缓存容量,删除最后一个对象信息
                delete projectItem[Object.keys(projectItem)[1]]
                //添加信息
                projectItem[id] = decryptedData(this.publicKey, res.data.data)
              }
              //加密 存储
              localStorage.setItem(
                'projectList',
                encryptoPSW(JSON.stringify(projectItem), 120, 21)
              )
            } else {
              //不存在
              let projectItem = {}
              projectItem[id] = decryptedData(this.publicKey, res.data.data)
              //加密 存储
              localStorage.setItem(
                'projectList',
                encryptoPSW(JSON.stringify(projectItem), 120, 21)
              )
            }
          } else if (res.data.code === '400') {
            //删除加密信息 刷新界面
            localStorage.removeItem('privateKey')
            location.reload()
          } else if (res.data.code === '404') {
            //表示不存在这个页面，跳转到首页
          }
        })
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
