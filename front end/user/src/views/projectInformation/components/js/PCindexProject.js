import { decryptedData, decrypto, encryptoPSW } from '@/utils/encrypt'
import {
  obtainPublicKey,
  queryEvaluationBasic,
  queryStudioMember,
} from '@/api/mainIndex'
import elImageViewer from 'element-ui/packages/image/src/image-viewer'
import * as echarts from 'echarts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
} from 'echarts/components'
import { PieChart } from 'echarts/charts'
import { LabelLayout } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'
export default {
  name: 'PcIndex',
  components: {
    elImageViewer,
  },
  data() {
    return {
      projectChildList: [],
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
      evaluationPillarChart: {
        title: {
          text: this.$t('mainIndex.evaluationPillarTitle'),
          subtext: '',
        },
        xAxis: {
          axisLabel: {
            color: '#688bfd',
            interval: 0,
            rotate: -30,
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          z: 10,
          data: [],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: [],
            type: 'bar',
            showBackground: true,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' },
              ]),
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' },
                ]),
              },
            },
            backgroundStyle: {
              color: 'rgba(180, 180, 180, 0.2)',
            },
          },
        ],
      },
      evaluatePieChart: {
        title: {
          text: this.$t('mainIndex.evaluatePieChartTitle'),
          subtext: '',
          left: 'center',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
        },
        series: [
          {
            name: this.$t('mainIndex.evaluatePieChartTitle'),
            type: 'pie',
            radius: '50%',
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)',
              },
            },
          },
        ],
      },
      showViewImage: false,
      fullscreenLoading: true,
      publicKey: undefined,
      projectItem: {
        projectList: [],
        evaluationDate: '',
      },
      studioMember: [],
      itemChildList: [],
    }
  },
  mounted() {
    setTimeout(() => {
      //调用初始化界面
      this.obtainPublicKey()
      this.queryStudio()
      let projectId = this.$route.params.id
      this.initProject(projectId)
      this.fullscreenLoading = false
    }, 2000)
  },
  methods: {
    queryStudio() {
      queryStudioMember().then((res) => {
        if (res.data.code === '200') {
          //解密数据
          this.studioMember = decryptedData(this.publicKey, res.data.data)
          // console.log(this.studioMember)
        }
      })
    },
    createdChar() {
      if (this.projectItem.mode === 2) {
        if (
          this.projectItem.projectList !== null &&
          this.projectItem.projectList !== undefined
        ) {
          //分析数据生成饼图
          // { value: 1048, name: 'Search Engine' },
          let data = []
          this.projectItem.projectList.forEach((item) => {
            let dataItem = {}
            dataItem.value = item.score
            dataItem.name = item.projectName
            data.push(dataItem)
          })
          //循环结束
          this.evaluatePieChart.series[0].data = data
          var myChart = echarts.init(document.getElementById('evaluatePie'))
          myChart.setOption(this.evaluatePieChart)
          //生成柱状图
          //分析数据生成数据模板
          let dataTitle = []
          let dataList = []
          this.projectItem.projectList.forEach((item) => {
            dataTitle.push(item.score)
            dataList.push(item.projectName)
          })
          this.evaluationPillarChart.xAxis.data = dataList
          this.evaluationPillarChart.series[0].data = dataTitle
          var myChartPillar = echarts.init(
            document.getElementById('evaluatePillar')
          )
          myChartPillar.setOption(this.evaluationPillarChart)
          //生成单项数据
          //子列
          this.projectItem.projectList.forEach((item) => {
            //更新分数
            let strings = Object.keys(item)
            let keys = []
            strings.forEach((it) => {
              let split = it.split('fraction')
              if (split.length >= 2) {
                keys.push(it)
              }
            })
            let dataList = []
            let studioNameList = []
            let dataItems = []
            let studioList = {
              value: {
                lineHeight: 30,
                align: 'center',
              },
            }
            if (keys.length >= 1) {
              //表示分数
              let score = []
              keys.forEach((sco) => {
                score.push(item[sco])
              })
              score.forEach((ss) => {
                this.studioMember.forEach((item) => {
                  //获取id
                  if (item.id == ss.split('_')[0]) {
                    //说明对上了
                    let dataitem = {
                      value: ss.split('_')[1],
                      symbol: 'image://' + item.avatar,
                      symbolSize: [30, 30],
                    }
                    dataItems.push(dataitem)
                    dataList.push(ss.split('_')[1])
                    studioNameList.push(item.name)
                    studioList[item.id] = {
                      height: 30,
                      align: 'center',
                      backgroundColor: {
                        image: item.avatar,
                      },
                    }
                  }
                })
              })
              let chars = JSON.parse(JSON.stringify(this.evaluationChildChart))
              chars.xAxis.data = studioNameList
              chars.xAxis.axisLabel.rich = studioList
              chars.series[0].data = dataList
              chars.series[1].data = dataItems
              let childProject = {}
              childProject.echars = chars
              childProject.score = item.score
              childProject.levelValue = item.levelValue
              childProject.name = item.projectName
              childProject.image = item.projectImage
              childProject.detail = JSON.parse(item.detailInformation)
              childProject.keys = Object.keys(
                JSON.parse(item.detailInformation)
              )
              //存储数值
              this.projectChildList.push(childProject)
            }
          })
          // console.log(this.projectChildList)
          setTimeout(() => {
            for (let i = 0; i < this.projectChildList.length; i++) {
              var childChart = echarts.init(
                document.getElementById('childEchars_' + i)
              )
              childChart.setOption(this.projectChildList[i].echars)
            }
          }, 1000)
        }
      } else {
        if (
          this.projectItem.projectList !== null &&
          this.projectItem.projectList !== undefined
        ) {
          this.projectItem.projectList.forEach((item) => {
            let childProject = {}
            childProject.levelValue = item.levelValue
            childProject.name = item.projectName
            childProject.image = item.projectImage
            //存储数值
            this.projectChildList.push(childProject)
          })
        }
      }
    },
    showEvaluationImage() {
      const loading = this.$loading({
        lock: true,
        text: this.$t('mainIndex.loading'),
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })
      setTimeout(() => {
        loading.close()
      }, 1000)
      this.showViewImage = true
    },
    showEvaluationImageClose() {
      this.showViewImage = false
    },
    cleanNowData() {
      this.$confirm(
        this.$t('mainIndex.cleanDataPromptTitle'),
        this.$t('mainIndex.prompt'),
        {
          confirmButtonText: this.$t('mainIndex.sure'),
          cancelButtonText: this.$t('mainIndex.none'),
          type: 'warning',
        }
      )
        .then(() => {
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
          //刷新页面
          location.reload()
        })
        .catch(() => {})
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
          //生成饼状图
          setTimeout(() => {
            this.createdChar()
          }, 500)
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
              // console.log(projectItem)
              // console.log(encryptoPSW(JSON.stringify(projectItem), 120, 21))
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
      setTimeout(() => {
        this.createdChar()
      }, 500)
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
