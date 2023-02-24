<template>
  <div v-loading="fullscreenLoading">
    <div>
      <el-tag
        v-for="(item, index) in levelList"
        :key="index + 'level'"
        style="margin-bottom: 10px"
        closable
        @close="handleClose(item, index)"
      >
        {{ item }}
      </el-tag>
      <el-card
        v-infinite-scroll="loadQuery"
        infinite-scroll-distance="5"
        shadow="hover"
        class="query-card"
      >
        <div v-for="(item, index) in queryList" :key="'queryList' + index">
          <div>
            <el-row :gutter="24" class="query-list">
              <el-col :span="12" class="query-image">
                <a :href="item.videoAddress" target="_blank">
                  <el-image
                    style="width: 100%; height: 100%; border-radius: 20px"
                    :src="item.coverImage"
                    fit="fill"
                    lazy
                  >
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline"></i>
                    </div>
                  </el-image>
                </a>
              </el-col>
              <el-col :span="12" class="query-main-title">
                <div
                  @click="clickProject(item.id)"
                  v-html="item.mainTitle"
                ></div>
                <div
                  style="
                    font-weight: lighter;
                    font-size: 8px;
                    font-family: 宋体, serif;
                    color: black;
                    padding-top: 5px;
                  "
                  @click="clickProject(item.id)"
                >
                  {{ item.evaluationDate }}
                </div>
                <p
                  style="
                    font-weight: lighter;
                    color: black;
                    font-family: 楷体, serif;
                    font-size: 10px;
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2;
                    overflow: hidden;
                  "
                >
                  {{ item.subheading }}
                </p>
              </el-col>
            </el-row>
          </div>
          <div>
            <el-row
              v-for="(items, indexs) in item.projectList"
              :key="'queryList' + indexs"
              :gutter="24"
              class="query-list-child"
            >
              <el-col :span="5" class="query-placeholder-child"></el-col>
              <el-col :span="19" class="query-image-child">
                <el-row :gutter="24" class="query-list-child-single">
                  <el-col :span="8">
                    <el-image
                      style="width: 70px; height: 100px; border-radius: 10px"
                      :src="items.projectImage"
                      fit="fill"
                      @click="clickProject(item.id)"
                    ></el-image>
                  </el-col>
                  <el-col :span="16">
                    <div
                      @click="clickProject(item.id)"
                      v-html="items.projectName"
                    ></div>
                    <div
                      v-if="items.level === 1"
                      style="
                        padding-top: 8px;
                        color: #ffb3b3;
                        font-weight: bold;
                      "
                    >
                      {{ items.levelValue }}
                    </div>
                    <div
                      v-if="items.level === 2"
                      style="
                        padding-top: 8px;
                        color: #ffbc41;
                        font-weight: bold;
                      "
                    >
                      {{ items.levelValue }}
                    </div>
                    <div
                      v-if="items.level === 3"
                      style="
                        padding-top: 8px;
                        color: #0e0e0e;
                        font-weight: bold;
                      "
                    >
                      {{ items.levelValue }}
                    </div>
                    <div
                      v-if="items.score !== 0"
                      style="padding-top: 10px; font-weight: lighter"
                    >
                      总分： {{ items.score }}
                    </div>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </div>
        </div>
        <div style="width: 100%; font-size: 20px; text-align: center">
          <square-loader
            :loading="updateDateLoading"
            :color="updateLoadingColor"
            :size="updateLoadingSize"
          ></square-loader>
          <el-image
            v-if="updateLoadingEmpty"
            style="width: 50%; height: 50%; padding-top: 20px"
            src="https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/empty.png"
            fit="fill"
          ></el-image>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script src="@/views/queryResult/components/js/cellPhoneQuery.js"></script>

<style scoped>
  /*  .query-card .query-list-child .query-placeholder-child .above {
    margin: 0;
  }
  .query-card .query-list-child .query-placeholder-child .middle {
    margin: 0;
  }
  .query-card .query-list-child .query-placeholder-child .under {
    margin: 0;
  }*/
  .query-card::-webkit-scrollbar-track {
    background: #ffffff;
    border-radius: 2px;
  }
  .query-card::-webkit-scrollbar {
    width: 5px;
    /**/
  }
  .query-card::-webkit-scrollbar-thumb {
    background: #cfcaff;
    border-radius: 10px;
  }
  .query-card::-webkit-scrollbar-thumb:hover {
    background: #6579fffd;
  }
  .query-card {
    overflow-y: auto;
    height: 500px;
    width: 100%;
    border-radius: 20px;
  }
  .query-card .query-list .query-main-title {
    color: #5994ff;
    font-family: 草书, serif;
    font-size: 15px;
    font-weight: bold;
  }
  .query-card
    .query-list-child
    .query-placeholder-child
    .above
    ::v-deep
    .el-divider--vertical {
    margin-left: 20px;
    background: #53a5ff;
    width: 7px;
    height: 50px;
  }
  .query-card
    .query-list-child
    .query-placeholder-child
    .middle
    ::v-deep
    .el-divider--horizontal {
    margin-left: 20px;
    margin-top: 0;
    margin-bottom: 0;
    background: #53a5ff;
    width: 60px;
    height: 7px;
  }
  .query-card
    .query-list-child
    .query-placeholder-child
    .under
    ::v-deep
    .el-divider--vertical {
    margin-left: 20px;
    background: #53a5ff;
    width: 7px;
    height: 60px;
  }
  .query-card .query-list-child .query-list-child-single {
    font-family: 楷书, serif;
    text-align: left;
    padding-bottom: 20px;
  }
  .query-card .query-list-child .query-placeholder-child {
    border: 1px solid transparent;
  }
</style>
