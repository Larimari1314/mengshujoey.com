<template>
  <div v-loading="fullscreenLoading" class="cell-phone-project-single">
    <div class="image-project">
      <a :href="projectItem.videoAddress" target="_blank">
        <el-image
          style="width: 100%; height: 20%; border-radius: 20px"
          :src="projectItem.coverImage"
          lazy
        >
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </a>
      <div class="cell-title">
        {{ projectItem.mainTitle }}
      </div>
      <div class="cell-date">
        {{ projectItem.evaluationDate }}
      </div>
      <!--<div class="cell-subheading">
        {{ projectItem.subheading }}
      </div>-->
      <div class="cell-red-list">
        <el-card class="box-card">
          <el-row :gutter="24" class="text-item">
            <span class="project-single-title">
              {{ $t('mainIndex.redPlacard') }}
            </span>
            <el-link
              style="float: right; font-size: 10px; padding-top: 10px"
              icon="el-icon-view"
              @click="cleanNowData"
            >
              {{ $t('mainIndex.errorData') }}
            </el-link>
            <el-divider></el-divider>
            <el-col
              v-for="(item, index) in projectItem.projectList"
              :key="index + 'col'"
              :span="12"
              class="text-project"
            >
              <div v-if="item.levelValue === '红榜'">
                <!--显示图片-->
                <el-image
                  v-if="projectItem.mode === 2"
                  style="
                    width: 80px;
                    height: 100px;
                    padding-bottom: 10px;
                    border-radius: 20px;
                    padding-top: 10px;
                  "
                  :src="item.projectImage"
                  fit="fill"
                  @click="clickProject(item)"
                ></el-image>
                <el-image
                  v-if="projectItem.mode === 1"
                  style="
                    width: 80px;
                    height: 100px;
                    padding-bottom: 10px;
                    border-radius: 20px;
                    padding-top: 10px;
                  "
                  :src="item.projectImage"
                  fit="fill"
                  :preview-src-list="[item.projectImage]"
                ></el-image>
                <div>
                  <div class="project-main-title">
                    {{ item.projectName }}
                    <div v-if="projectItem.mode === 2" class="project-score">
                      总分数: {{ item.score }}
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="box-card-two">
          <el-row :gutter="24" class="text-item">
            <div class="project-single-title-yello">
              {{ $t('mainIndex.yellowPlacard') }}
            </div>
            <el-divider></el-divider>
            <el-col
              v-for="(item, index) in projectItem.projectList"
              :key="index + 'col'"
              :span="12"
              class="text-project"
            >
              <div v-if="item.levelValue === '黄榜'">
                <!--显示图片-->
                <el-image
                  v-if="projectItem.mode === 2"
                  style="
                    width: 80px;
                    height: 100px;
                    padding-bottom: 10px;
                    border-radius: 20px;
                    padding-top: 10px;
                  "
                  :src="item.projectImage"
                  fit="fill"
                  @click="clickProject(item)"
                ></el-image>
                <el-image
                  v-if="projectItem.mode === 1"
                  style="
                    width: 80px;
                    height: 100px;
                    padding-bottom: 10px;
                    border-radius: 20px;
                    padding-top: 10px;
                  "
                  :src="item.projectImage"
                  fit="fill"
                  :preview-src-list="[item.projectImage]"
                ></el-image>
                <div>
                  <div class="project-main-title">
                    {{ item.projectName }}
                    <div v-if="projectItem.mode === 2" class="project-score">
                      总分数: {{ item.score }}
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="box-card-two">
          <el-row :gutter="24" class="text-item">
            <div class="project-single-title-bl">
              {{ $t('mainIndex.blackPlacard') }}
            </div>
            <el-divider></el-divider>
            <el-col
              v-for="(item, index) in projectItem.projectList"
              :key="index + 'col'"
              :span="12"
              class="text-project"
            >
              <div v-if="item.levelValue === '黑榜'">
                <!--显示图片-->
                <el-image
                  v-if="projectItem.mode === 2"
                  style="
                    width: 80px;
                    height: 100px;
                    padding-bottom: 10px;
                    border-radius: 20px;
                    padding-top: 10px;
                  "
                  :src="item.projectImage"
                  fit="fill"
                  @click="clickProject(item)"
                ></el-image>
                <el-image
                  v-if="projectItem.mode === 1"
                  style="
                    width: 80px;
                    height: 100px;
                    padding-bottom: 10px;
                    border-radius: 20px;
                    padding-top: 10px;
                  "
                  :src="item.projectImage"
                  fit="fill"
                  :preview-src-list="[item.projectImage]"
                ></el-image>
                <div>
                  <div class="project-main-title">
                    {{ item.projectName }}
                    <div v-if="projectItem.mode === 2" class="project-score">
                      总分数: {{ item.score }}
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        <el-drawer
          :with-header="false"
          size="70%"
          :visible.sync="childProject"
          direction="btt"
          :show-close="false"
          :before-close="handleClose"
        >
          <div class="child-drawer">
            <el-image
              style="
                width: 80px;
                height: 120px;
                border-radius: 30px;
                padding-top: 20px;
              "
              :src="itemChildList.projectImage"
              fit="fill"
              :preview-src-list="[itemChildList.projectImage]"
            ></el-image>
            <div>
              <div class="child-main-title">
                {{ itemChildList.projectName }}
                <div class="project-score">
                  总分数: {{ itemChildList.score }}
                </div>
              </div>
              <el-row v-if="projectItem.mode === 2" :gutter="24">
                <el-col
                  v-for="(items, indexs) in Object.keys(
                    JSON.parse(itemChildList.detailInformation)
                  )"
                  :key="indexs + 'projectChild'"
                  style="font-family: 草书, serif; font-size: 15px"
                  :span="8"
                  :label="items"
                >
                  {{ items }} :
                  {{ JSON.parse(itemChildList.detailInformation)[items] }}
                </el-col>
              </el-row>
              <div
                v-if="projectItem.mode === 2"
                id="evaluateChild"
                class="evaluate-child"
              ></div>
            </div>
          </div>
        </el-drawer>
      </div>
    </div>
  </div>
</template>

<script src="../components/js/cellPhoneProject.js"></script>

<style scoped>
  .cell-phone-project-single .text-item .project-single-title-yello {
    font-size: 26px;
    font-family: 楷书, serif;
    font-weight: bold;
    color: #ff8611;
  }
  .cell-phone-project-single .text-item .project-single-title-bl {
    font-size: 26px;
    font-family: 楷书, serif;
    font-weight: bold;
    color: #0e0e0e;
  }
  .cell-phone-project-single .child-drawer .child-main-title {
    padding-top: 10px;
    font-size: 15px;
    font-weight: bold;
    font-family: 楷体, serif;
  }
  .cell-phone-project-single .child-drawer .child-main-title .project-score {
    padding-bottom: 20px;
    font-size: 10px;
    font-family: 楷体, serif;
  }
  .cell-phone-project-single .child-drawer {
    text-align: center;
  }
  .evaluate-child {
    width: 400px;
    height: 300px;
  }
  .cell-phone-project-single ::v-deep .el-drawer.btt {
    background: rgba(255, 255, 255, 0.7);
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);
  }
  .cell-phone-project-single .text-item .text-project .project-score {
    font-size: 10px;
  }
  .cell-phone-project-single .text-item .project-single-title {
    padding-left: 50px;
    font-size: 26px;
    font-family: 楷书, serif;
    color: red;
    font-weight: bold;
  }
  .cell-phone-project-single .text-item .text-project .project-main-title {
    height: 40px;
    padding-top: 10px;
    font-size: 13px;
    font-weight: bold;
    font-family: 楷书, serif;
  }
  .cell-phone-project-single .text-item .text-project {
    /*padding-bottom: 15px;*/
  }
  .cell-phone-project-single .text-item .el-tag--plain.el-tag--info {
    margin-left: 10px;
    font-size: 10px;
    color: red;
  }
  .cell-phone-project-single .text-item .el-tag--plain.el-tag--success {
    font-size: 10px;
    color: #ffb700;
  }
  .cell-phone-project-single .text-item .el-tag--plain.el-tag--error {
    font-size: 10px;
    color: black;
  }
  .cell-phone-project-single .text-item {
    text-align: center;
  }
  .cell-phone-project-single .cell-red-list {
    padding-top: 10px;
  }
  .cell-phone-project-single .cell-subheading {
    text-align: left;
    padding-top: 5px;
    font-size: 12px;
    font-family: 草书, serif;
    text-indent: 2em;
  }
  .cell-phone-project-single .cell-date {
    padding-top: 10px;
    font-size: 10px;
    font-family: 草书, serif;
  }
  .cell-phone-project-single .cell-title {
    padding-top: 10px;
    font-size: 20px;
    font-weight: bold;
    font-family: 楷书, serif;
  }
  .cell-phone-project-single .image-project {
    text-align: center;
  }
  ::v-deep
    .vue-admin-beautiful-wrapper
    .app-main-container
    > [class*='-container'] {
    padding: 0;
  }
</style>
