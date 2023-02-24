<template>
  <div v-loading="fullscreenLoading" class="pc-project-information">
    <el-row :gutter="24">
      <el-col :span="8">
        <a :href="projectItem.videoAddress" target="_blank">
          <el-image
            style="width: 100%; height: 50%; border-radius: 20px"
            :src="projectItem.coverImage"
            lazy
          >
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
        </a>
      </el-col>
      <el-col :span="16">
        <div class="main-title">
          {{ projectItem.mainTitle }}
          <div class="tag-value-label">
            <span>{{ projectItem.labelValue }}</span>
          </div>
        </div>
        <div class="subheading">
          {{ projectItem.subheading }}
        </div>
        <div class="clean-data">
          <el-button class="clean-button btn-clean" @click="cleanNowData">
            {{ $t('mainIndex.errorData') }}
          </el-button>
          <span v-if="projectItem.mode === 2" class="recommended-images">
            <el-link @click="showEvaluationImage">
              {{ $t('mainIndex.recommendedListImages') }}
              <i class="el-icon-view el-icon--right"></i>
            </el-link>
            <el-image-viewer
              v-if="showViewImage"
              :on-close="showEvaluationImageClose"
              :url-list="[projectItem.evaluationImage]"
            ></el-image-viewer>
          </span>
        </div>
      </el-col>
    </el-row>
    <el-row v-if="projectItem.mode === 2" :gutter="24">
      <el-col :span="14">
        <div id="evaluatePie" class="evaluate-pie"></div>
      </el-col>
      <el-col :span="10">
        <div id="evaluatePillar" class="evaluate-Pillar"></div>
      </el-col>
    </el-row>
    <el-row :gutter="24" class="row-project-sin">
      <el-col
        v-for="(item, index) in projectChildList"
        :key="index + 'child'"
        :span="8"
      >
        <div class="project-title">
          {{ item.name }}
          <el-tag v-if="item.levelValue === '红榜'" effect="plain" type="info">
            {{ item.levelValue }}
          </el-tag>
          <el-tag
            v-if="item.levelValue === '黄榜'"
            effect="plain"
            type="success"
          >
            {{ item.levelValue }}
          </el-tag>
          <el-tag v-if="item.levelValue === '黑榜'" effect="plain" type="error">
            {{ item.levelValue }}
          </el-tag>
        </div>
        <div v-if="projectItem.mode === 2" class="project-score">
          总分： {{ item.score }}
        </div>
        <div v-if="projectItem.mode === 1" class="project-image-one">
          <el-image
            style="width: 100px; height: 130px; border-radius: 20px"
            :src="item.image"
            :preview-src-list="[item.image]"
            fit="fill"
          ></el-image>
        </div>
        <div v-if="projectItem.mode === 2" class="project-image-two">
          <el-row :gutter="24">
            <el-col :span="10">
              <el-image
                style="width: 100px; height: 130px; border-radius: 20px"
                :src="item.image"
                :preview-src-list="[item.image]"
                fit="fill"
              ></el-image>
            </el-col>
            <el-col :span="10">
              <el-descriptions
                :column="1"
                :title="$t('mainIndex.referralInformation')"
              >
                <el-descriptions-item
                  v-for="(items, indexs) in item.keys"
                  :key="'key' + indexs"
                  :label="items"
                >
                  {{ item.detail[items] }}
                </el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>
        </div>
        <!--        {{ item.image }}-->
        <div
          v-if="projectItem.mode === 2"
          :id="`childEchars_${index}`"
          class="evaluate-child"
        ></div>
      </el-col>
    </el-row>
  </div>
</template>

<script src="../components/js/PCindexProject.js"></script>

<style scoped>
  .pc-project-information .project-title .el-tag--plain.el-tag--info {
    color: red;
  }
  .pc-project-information .project-title .el-tag--plain.el-tag--success {
    color: #ffb700;
  }
  .pc-project-information .project-title .el-tag--plain.el-tag--error {
    color: black;
  }
  .pc-project-information .row-project-sin .project-image-two {
    padding-left: 40px;
    padding-top: 15px;
  }
  .pc-project-information .row-project-sin .project-image-one {
    padding-left: 130px;
    padding-top: 15px;
  }
  .pc-project-information .row-project-sin .project-score {
    padding-left: 150px;
    font-size: 20px;
    color: palevioletred;
    font-family: 楷体, serif;
  }
  .pc-project-information .row-project-sin .project-title {
    padding-top: 20px;
    text-align: center;
    font-weight: bold;
    font-size: 20px;
    font-family: 草书, serif;
  }
  .pc-project-information .evaluate-child {
    width: 400px;
    height: 300px;
    padding-top: 20px;
  }
  .pc-project-information .row-project-sin {
    padding-top: 20px;
  }
  .pc-project-information .evaluate-Pillar {
    width: 500px;
    height: 400px;
    padding-top: 50px;
  }
  .pc-project-information .evaluate-pie {
    width: 900px;
    height: 400px;
    padding-top: 50px;
  }
  .pc-project-information .clean-data .recommended-images {
    padding-left: 100px;
  }
  .pc-project-information .clean-data .clean-button {
    margin-top: 80px;
    font-family: 华文楷体, serif;
    width: 130px;
    color: #fff;
    border-radius: 5px;
    padding: 10px 25px;
    font-weight: 500;
    background: transparent;
    cursor: pointer;
    transition: all 0.3s ease;
    position: relative;
    display: inline-block;
    box-shadow: inset 2px 2px 2px 0 rgba(255, 255, 255, 0.5),
      7px 7px 20px 0 rgba(0, 0, 0, 0.1), 4px 4px 5px 0 rgba(0, 0, 0, 0.1);
    outline: none;
  }

  .pc-project-information .clean-data .btn-clean {
    background: rgb(36, 46, 193);
    background: linear-gradient(
      0deg,
      rgb(82, 92, 237) 0%,
      rgb(65, 77, 218) 100%
    );
    border: none;
  }
  .pc-project-information .clean-data .btn-clean:hover {
    background: #5355ffff;
    background: linear-gradient(
      0deg,
      rgba(0, 3, 255, 1) 0%,
      rgba(2, 126, 251, 1) 100%
    );
  }
  .pc-project-information .main-title {
    font-weight: bold;
    font-size: 40px;
    font-family: 楷体, serif;
  }
  .pc-project-information .main-title .tag-value-label {
    width: 200px;
    height: 200px;
    position: relative;
    background: #fff;
    overflow: hidden;
  }
  .pc-project-information .main-title .tag-value-label {
    width: 100px;
    height: 100px;
    position: absolute;
    /*background: green;*/
    top: -50px;
    right: -50px;
    transform: rotate(45deg);
  }
  .pc-project-information .main-title .tag-value-label span {
    position: absolute;
    bottom: 0;
    display: block;
    font-size: 20px;
    color: #fff;
    background: #a6b5ff;
    width: 90px;
    text-align: center;
  }
  .pc-project-information .subheading {
    font-size: 20px;
    font-family: 隶书, serif;
    padding-right: 100px;
    padding-top: 20px;
  }
</style>
