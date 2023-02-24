<template>
  <div class="cell-phone-index">
    <el-card v-loading="fullLoading" class="box-card-label">
      <span v-for="(item, index) in labelIdOptions" :key="'label' + index">
        <el-link
          :underline="false"
          type="primary"
          target="_blank"
          @click="clickLabel(item)"
        >
          {{ item.label }}
        </el-link>
      </span>
    </el-card>
    <el-row
      v-loading="fullLoading"
      :gutter="24"
      style="margin: auto; text-align: center"
    >
      <el-col :span="12" style="padding-left: 2px; padding-right: 2px">
        <el-card class="box-card-love" shadow="hover">
          <el-empty
            v-if="loveList.length === 0"
            :description="$t('mainIndex.emptyMain')"
            image="https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/empty.png"
          ></el-empty>
        </el-card>
      </el-col>
      <el-col :span="12" style="padding-left: 2px; padding-right: 2px">
        <el-card class="box-card-mick-tea" shadow="hover">
          <el-carousel :interval="5000" arrow="never">
            <el-carousel-item
              v-for="(item, index) in mickTeaList"
              :key="index + 'mick_tea'"
            >
              <el-image
                style="width: 100%; height: 120px"
                :src="item.coverImage"
                fit="fill"
                @click="clickProject(item.id)"
              ></el-image>
              <div class="box-carousel-title" @click="clickProject(item.id)">
                {{ item.mainTitle }}
              </div>
            </el-carousel-item>
          </el-carousel>
        </el-card>
      </el-col>
    </el-row>
    <el-card
      v-loading="fullLoading"
      v-infinite-scroll="loadQueryVideo"
      infinite-scroll-distance="10"
      class="box-card-update-video"
      shadow="hover"
    >
      <div class="update-date-card-cell">
        <div
          v-for="(item, index) in evaluationBasicList"
          :key="index + 'update_date'"
          class="update-date-for"
        >
          <el-row :gutter="24">
            <!--          <div class="update-date-for-left">-->
            <el-col :span="12">
              <el-image
                class="update-date-img"
                :src="item.coverImage"
                fit="fill"
                @click="clickProject(item.id)"
              ></el-image>
            </el-col>
            <!--          </div>-->
            <div class="update-date-for-right">
              <div style="padding-left: 5px">
                <div class="title-main" @click="clickProject(item.id)">
                  {{ item.mainTitle }}
                </div>
                <div>
                  <p class="describe-date-p-d" @click="clickProject(item.id)">
                    {{ item.evaluationDate }}
                    <span style="color: #53a5ff">
                      {{ item.labelValue }}
                    </span>
                  </p>
                </div>
                <div>
                  <p
                    class="update-date-card-subheading"
                    @click="clickProject(item.id)"
                  >
                    {{ item.subheading }}
                  </p>
                </div>
              </div>
            </div>
          </el-row>
        </div>
        <div style="width: 100%; font-size: 20px; text-align: center">
          <square-loader
            :loading="updateDateLoading"
            :color="updateLoadingColor"
            :size="updateLoadingSize"
          ></square-loader>
          <el-image
            v-if="updateLoadingEmpty"
            style="width: 50%; height: 50%"
            src="https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/empty.png"
            fit="fill"
          ></el-image>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script src="../../index/components/js/cellPhoneIndex.js"></script>

<style scoped>
  .box-card-label::-webkit-scrollbar {
    width: 10px;
    height: 10px;
    /**/
  }
  .box-card-label::-webkit-scrollbar-track {
    background: #ffffffff;
    border-radius: 2px;
  }
  .box-card-label::-webkit-scrollbar-thumb {
    background: #ffffff;
    border-radius: 10px;
  }
  .box-card-label::-webkit-scrollbar-thumb:hover {
    background: #6579fffd;
  }

  .update-date-for .update-date-for-left {
    width: 50%;
    float: left;
    margin-bottom: 10px;
  }
  .update-date-card-cell {
    height: 100%;
  }
  .update-date-for .update-date-for-right {
    width: 50%;
    float: right;
    margin-bottom: 5px;
  }
  .cell-phone-index .box-card-update-video .describe-date-p-d {
    font-family: 宋体, serif;
    font-size: 10px;
    color: #5d5d5d;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
  .cell-phone-index .box-card-update-video .update-date-card-subheading {
    padding-right: 10px;
    font-family: 楷体, serif;
    font-size: 10px;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
  }
  .cell-phone-index .box-card-update-video ::v-deep .el-card__body {
    padding: 5px 0 0;
  }
  .cell-phone-index
    .box-card-update-video
    .update-date-card-cell
    ::v-deep
    .el-image__inner {
    border-top-right-radius: 15px;
    border-top-left-radius: 15px;
    width: 100%;
    height: 100%;
  }
  .cell-phone-index
    .box-card-update-video
    .update-date-card-cell
    ::v-deep
    .el-descriptions-item__label:not(.is-bordered-label) {
    display: none;
  }
  .cell-phone-index .box-card-update-video .update-date-card-cell .title-main {
    padding-right: 10px;
    padding-top: 10px;
    font-family: 隶书, serif;
    font-size: 13px;
  }
  .cell-phone-index
    .box-card-update-video
    .update-date-card-cell
    ::v-deep
    .el-descriptions {
    width: 100%;
  }
  .cell-phone-index
    .box-card-update-video
    .update-date-card-cell
    ::v-deep
    .el-image {
    width: 100%;
    height: 100%;
  }
  .box-card-update-video {
    overflow-y: auto;
    height: 360px;
    width: 100%;
    border-radius: 20px;
  }
  .cell-phone-index .box-card-mick-tea .box-carousel-title {
    font-family: 楷体, serif;
    font-size: 16px;
    text-align: left;
    padding-right: 5px;
    padding-top: 5px;
    padding-left: 5px;
  }
  .cell-phone-index .box-card-mick-tea ::v-deep .el-card__body {
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    padding: 0;
  }
  .box-card-label ::v-deep .el-link--inner {
    padding-right: 10px;
  }
  .box-card-label a {
    color: #aaafff;
  }
  .cell-phone-index .box-card-label {
    overflow-x: auto;
    white-space: nowrap;
    margin: 5px;
  }
  .cell-phone-index .box-card-love {
    border-radius: 10px;
    width: 100%;
    height: 180px;
  }
  .cell-phone-index .box-card-mick-tea {
    border-radius: 10px;
    width: 100%;
    height: 180px;
  }
</style>
