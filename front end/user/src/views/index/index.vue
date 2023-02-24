<template>
  <div>
    <div v-if="'horizontal' === layout" class="index-container">
      <pc-index />
    </div>
    <div v-else class="index-container">
      <cell-phone-index />
    </div>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import pcIndex from '@/views/index/components/PcIndex'
  import cellPhoneIndex from '@/views/index/components/cellPhoneIndex'
  import { countDailyActivity } from '@/api/mainIndex'
  export default {
    name: 'PersonalCenter',
    components: {
      pcIndex,
      cellPhoneIndex,
    },
    data() {
      return {}
    },
    computed: {
      ...mapGetters({
        layout: 'settings/layout',
      }),
    },
    created() {
      let item = localStorage.getItem('countDailyCountDate')
      if (item !== null && item !== undefined) {
        let parse = JSON.parse(item)
        if (new Date(parse).getDay() !== new Date().getDay()) {
          this.dailyActivity()
        }
      } else {
        this.dailyActivity()
      }
    },
    methods: {
      dailyActivity() {
        //日活量统计
        countDailyActivity().then((res) => {
          if (res.data.code === '200') {
            localStorage.setItem(
              'countDailyCountDate',
              JSON.stringify(new Date())
            )
          }
        })
      },
    },
  }
</script>
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
