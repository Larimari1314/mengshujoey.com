<template>
  <div class="index-container">
    <el-row :gutter="24">
      <el-col :span="8">
        <el-card class="box-card">
          <div class="text item" style="text-align: center">
            <el-image
              style="width: 150px; height: 150px; border-radius: 50%"
              :src="userInformation.adminAvatar"
              fit="fill"
            ></el-image>
            <el-divider></el-divider>
            <el-descriptions :column="1" class="information">
              <el-descriptions-item label="用户名">
                {{ userInformation.adminName }}
              </el-descriptions-item>
              <el-descriptions-item label="性别">
                {{ userInformation.sex }}
              </el-descriptions-item>
              <el-descriptions-item label="年龄">
                {{ userInformation.age }}
              </el-descriptions-item>
              <el-descriptions-item label="电子邮件">
                {{ userInformation.email }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card
          v-infinite-scroll="loadAdminLog"
          infinite-scroll-distance="5"
          infinite-scroll-immediate="false"
          class="admin-log-info"
        >
          <div class="text item">
            <ul class="infinite-list" style="overflow: auto">
              <li
                v-for="(item, index) in adminLogList"
                :key="index + 'adminLog'"
                class="infinite-list-item"
              >
                管理员
                <span style="color: red; font-size: 20px">{{ item.name }}</span>
                在{{ item.operationTime }} 时刻执行
                <span style="color: #ff9999; font-size: 20px">
                  {{ item.level }}
                </span>
                具体内容为
                <div>{{ item.content }}</div>
              </li>
            </ul>
            <div v-loading="loadingAdminLog"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>用户日活量</span>
          </div>
          <div class="text item">
            <div id="dateEchars" class="evaluate-child"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div class="text item">
            <div id="dateEcharsProject" class="evaluate-child-project"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { decrypto } from '@/utils/encrypt'
  import Vue from 'vue'
  import {
    findAdminLog,
    queryDailyActivity,
    queryProjectData,
  } from '@/api/adminIndex'
  import * as echarts from 'echarts'

  export default {
    name: 'PersonalCenter',
    data() {
      return {
        echarsDataProject: {
          xAxis: {
            axisLabel: {
              interval: 0,
              rotate: -30,
            },
            type: 'category',
            data: [],
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: [],
              type: 'line',
            },
          ],
        },
        echarsDataLive: {
          xAxis: {
            type: 'category',
            data: [],
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: [],
              type: 'line',
              smooth: true,
            },
          ],
        },
        loadingAdminLog: false,
        userInformation: null,
        pageLog: 1,
        maxPage: undefined,
        configs: {
          headers: {
            token: sessionStorage.getItem('accessTokens'),
          },
        },
        adminLogList: [],
      }
    },
    created() {
      var userInformation = JSON.parse(
        decrypto(sessionStorage.getItem('userInformation'), 125, 20)
      )
      this.userInformation = userInformation
      const hour = new Date().getHours()
      const thisTime =
        hour < 8
          ? '早上好'
          : hour <= 11
          ? '上午好'
          : hour <= 13
          ? '中午好'
          : hour < 18
          ? '下午好'
          : '晚上好'
      Vue.prototype.$baseNotify(
        userInformation.adminName + `欢迎来到 ` + Vue.prototype.$baseTitle,
        `${thisTime}！`
      )
      this.queryAdminLog()
      setTimeout(() => {
        this.echarsLoading()
      }, 1000)
    },
    methods: {
      echarsLoading() {
        queryDailyActivity(this.configs).then((res) => {
          let data = []
          let dataName = []
          res.data.data.forEach((item) => {
            data.push(item.count)
            dataName.push(item.date)
          })
          this.echarsDataLive.xAxis.data = dataName
          this.echarsDataLive.series[0].data = data
          var childChart = echarts.init(document.getElementById('dateEchars'))
          childChart.setOption(this.echarsDataLive)
        })
        queryProjectData(this.configs).then((res) => {
          if (res.data.code === '200') {
            let data = []
            let dataname = []
            res.data.data.forEach((item) => {
              data.push(item.number)
              dataname.push(item.title)
            })
            console.log(dataname)
            console.log(data)
            this.echarsDataProject.xAxis.data = dataname
            this.echarsDataProject.series[0].data = data
            console.log(this.echarsDataProject)
            var childChart = echarts.init(
              document.getElementById('dateEcharsProject')
            )
            childChart.setOption(this.echarsDataProject)
          }
        })
      },
      loadAdminLog() {
        //判断当前数据是否到达顶端
        if (!this.loadingAdminLog) {
          if (this.pageLog <= this.maxPage) {
            this.loadingAdminLog = true
            setTimeout(() => {
              this.pageLog = this.pageLog + 1
              this.queryAdminLog()
            }, 2000)
          } else {
            this.loadingAdminLog = false
          }
        }
      },
      queryAdminLog() {
        let params = {
          page: this.pageLog,
        }
        findAdminLog(params, this.configs).then((res) => {
          if (res.data.code === '200') {
            this.maxPage = res.data.data.pages
            this.loadingAdminLog = false
            res.data.data.list.forEach((item) => {
              this.adminLogList.push(item)
            })
          }
        })
      },
    },
  }
</script>
<style scoped>
  .evaluate-child-project {
    width: 500px;
    height: 350px;
  }
  .evaluate-child {
    width: 500px;
    height: 300px;
  }
  .infinite-list-item {
    font-size: 15px;
    color: #7b7b7b;
    padding: 10px;
  }
  .index-container .information {
    font-family: 楷书, serif;
    font-weight: bold;
    font-size: 50px;
  }
  .admin-log-info {
    overflow-y: auto;
    height: 330px;
    width: 100%;
    border-radius: 20px;
  }
</style>
<style lang="scss" scoped>
  .index-container {
    padding: 0 !important;
    margin: 0 !important;
    background: #f5f7f8 !important;

    ::v-deep {
      .el-alert {
        padding: $base-padding;

        &--info.is-light {
          min-height: 82px;
          padding: $base-padding;
          margin-bottom: 15px;
          color: #909399;
          background-color: $base-color-white;
          border: 1px solid #ebeef5;
        }
      }

      .el-card__body {
        .echarts {
          width: 100%;
          height: 115px;
        }
      }
    }

    .card {
      ::v-deep {
        .el-card__body {
          .echarts {
            width: 100%;
            height: 305px;
          }
        }
      }
    }

    .bottom {
      padding-top: 20px;
      margin-top: 5px;
      color: #595959;
      text-align: left;
      border-top: 1px solid $base-border-color;
    }

    .table {
      width: 100%;
      color: #666;
      border-collapse: collapse;
      background-color: #fff;

      td {
        position: relative;
        min-height: 20px;
        padding: 9px 15px;
        font-size: 14px;
        line-height: 20px;
        border: 1px solid #e6e6e6;

        &:nth-child(odd) {
          width: 20%;
          text-align: right;
          background-color: #f7f7f7;
        }
      }
    }

    .icon-panel {
      height: 117px;
      text-align: center;
      cursor: pointer;

      svg {
        font-size: 40px;
      }

      p {
        margin-top: 10px;
      }
    }

    .bottom-btn {
      button {
        margin: 5px 10px 15px 0;
      }
    }
  }
</style>
