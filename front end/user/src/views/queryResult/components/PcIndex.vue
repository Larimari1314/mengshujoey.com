<template>
  <div>
    <el-tag
      v-for="(item, index) in levelList"
      :key="index + 'level'"
      style="margin: 10px; font-size: 15px"
      closable
      @close="handleClose(item, index)"
    >
      {{ item }}
    </el-tag>
    <el-card
      v-infinite-scroll="loadQuery"
      v-loading="fullLoading"
      infinite-scroll-distance="5"
      shadow="hover"
      class="query-card"
    >
      <div v-for="(item, index) in queryList" :key="'queryList' + index">
        <el-card>
          <div>
            <el-row :gutter="24" class="query-list">
              <el-col style="text-align: center" :span="10" class="query-image">
                <a :href="item.videoAddress" target="_blank">
                  <el-image
                    style="width: 400px; height: 200px; border-radius: 20px"
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
              <el-col :span="14" class="query-main-title">
                <div
                  style="
                    font-size: 30px;
                    font-weight: bold;
                    font-family: 楷书, serif;
                  "
                  v-html="item.mainTitle"
                ></div>
                <div
                  style="
                    font-weight: lighter;
                    font-size: 10px;
                    font-family: 宋体, serif;
                    color: black;
                    padding-top: 15px;
                  "
                >
                  {{ item.evaluationDate }}
                </div>
                <p
                  style="
                    padding-right: 15px;
                    font-weight: lighter;
                    color: black;
                    font-family: 楷体, serif;
                    font-size: 18px;
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 3;
                    overflow: hidden;
                  "
                >
                  {{ item.subheading }}
                </p>
                <button class="custom-btn btn" @click="clickProject(item.id)">
                  {{ $t('mainIndex.showInformationText') }}
                </button>
              </el-col>
            </el-row>
          </div>
          <div>
            <el-row :gutter="24" style="text-align: center">
              <div
                v-for="(items, indexs) in item.projectList"
                :key="'queryList' + indexs"
                :gutter="24"
                class="query-list-child"
              >
                <el-col :span="6">
                  <el-row :gutter="24" class="query-list-child-single">
                    <el-col :span="8">
                      <el-image
                        :preview-src-list="[items.projectImage]"
                        style="width: 70px; height: 100px; border-radius: 10px"
                        :src="items.projectImage"
                        fit="fill"
                      ></el-image>
                    </el-col>
                    <el-col :span="16" style="padding-left: 20px">
                      <div
                        style="font-size: 18px"
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
                        style="
                          font-size: 15px;
                          padding-top: 10px;
                          font-weight: lighter;
                        "
                      >
                        总分： {{ items.score }}
                      </div>
                    </el-col>
                  </el-row>
                </el-col>
              </div>
            </el-row>
          </div>
        </el-card>
      </div>
      <div style="width: 100%; font-size: 20px; text-align: center">
        <square-loader
          :loading="updateDateLoading"
          :color="updateLoadingColor"
          :size="updateLoadingSize"
        ></square-loader>
        <el-empty
          v-if="updateLoadingEmpty"
          image="https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/empty.png"
          :description="$t('HintText.mainEvaluationEmpty')"
        ></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script src="@/views/queryResult/components/js/PcIndexQuery.js"></script>

<style scoped>
  .query-list-child {
    height: 10px;
  }
  .query-card .query-list-child .query-list-child-single {
    font-family: 楷书, serif;
    text-align: left;
    padding: 20px 20px 20px 70px;
  }
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
  .query-card .query-list-child .query-placeholder-child {
    border: 1px solid transparent;
  }
  button {
    margin: 20px;
  }
  .custom-btn {
    width: 150px;
    height: 40px;
    color: #fff;
    border-radius: 5px;
    padding: 10px 25px;
    font-size: 15px;
    font-family: 楷体, serif;
    font-weight: 500;
    background: transparent;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    display: inline-block;
    box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, 0.5),
      7px 7px 20px 0px rgba(0, 0, 0, 0.1), 4px 4px 5px 0px rgba(0, 0, 0, 0.1);
    outline: none;
  }

  .btn {
    background: #7ba7ffff;
    border: none;
    z-index: 1;
  }
  .btn:after {
    position: absolute;
    content: '';
    width: 100%;
    height: 0;
    top: 0;
    left: 0;
    z-index: -1;
    border-radius: 5px;
    background-color: #1866f8;
    background-image: linear-gradient(315deg, #a6ccff 0%, #bec2ff 74%);
    box-shadow: inset 2px 2px 2px 0 rgba(255, 255, 255, 0.5);
    transition: all 0.3s ease;
  }
  .btn:hover {
    color: #000;
  }
  .btn:hover:after {
    top: auto;
    bottom: 0;
    height: 100%;
  }
  .btn:active {
    top: 2px;
  }
</style>
