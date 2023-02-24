<template>
  <div class="tap-card">
    <div class="label-header">
      <el-card v-loading="fullLoading" class="box-card" shadow="hover">
        <div class="clearfix">
          <span v-for="(item, index) in labelIdOptions" :key="'label' + index">
            <el-tooltip
              :enterable="false"
              class="item"
              effect="light"
              :content="$t('mainIndex.LabelButtonHintText')"
              placement="top"
            >
              <span class="frame">
                <button class="custom-btn btn-6" @click="queryLabels(item)">
                  <span>{{ item.label }}</span>
                </button>
              </span>
            </el-tooltip>
            &nbsp;&nbsp;&nbsp;&nbsp;
          </span>
        </div>
      </el-card>
    </div>
    <el-card
      v-if="loveProjectList.length !== 0"
      v-loading="fullLoading"
      class="love-box"
      shadow="hover"
    ></el-card>
    <el-card v-loading="fullLoading" class="milk-tea-box" shadow="hover">
      <div slot="header" class="milk-tea-title">
        <span>{{ $t('mainIndex.mickTeaTitle') }}</span>
      </div>
      <el-carousel
        :interval="5000"
        arrow="never"
        trigger="click"
        height="200px"
        indicator-position="outside"
        @change="switchCardsMikeTea"
      >
        <span class="mickTea">
          <el-carousel-item
            v-for="(item, index) in maxPage"
            :key="index + 'mickTeaIndex'"
          >
            <div class="card-show">
              <div
                v-for="(mickTea, mickIndex) in mickTeaList"
                :key="mickIndex + 'mickTea'"
                class="container"
              >
                <div class="card">
                  <div class="el-image" style="width: 100%; height: 100%">
                    <div class="img-card">
                      <img
                        class="el-image__inner"
                        :src="mickTea.coverImage"
                        style="object-fit: fill"
                      />
                    </div>
                    <div class="border">
                      <div class="describe-list">
                        <el-descriptions
                          :title="mickTea.mainTitle"
                          :column="1"
                          label-class-name="mick-tea-title"
                        >
                          <template slot="extra">
                            <el-tag
                              v-if="mickTea.clickNumber < 100"
                              type="success"
                            >
                              {{ mickTea.clickNumber }}
                            </el-tag>
                            <el-tooltip
                              v-else
                              class="item"
                              effect="dark"
                              :content="
                                $t('mainIndex.clickNumberText') +
                                mickTea.clickNumber
                              "
                              placement="top"
                            >
                              <el-image
                                style="width: 50px; height: 25px"
                                :src="hotClickSrc"
                                fit="cover"
                              ></el-image>
                            </el-tooltip>
                          </template>
                          <el-descriptions-item>
                            <p class="describe-list-p-d">
                              {{ mickTea.evaluationDate }}
                            </p>
                          </el-descriptions-item>
                          <el-descriptions-item>
                            <p class="describe-list-p">
                              {{ mickTea.subheading }}
                            </p>
                          </el-descriptions-item>
                          <el-descriptions-item>
                            <el-button
                              class="custom-btn-information btn-information"
                              @click="clickProject(mickTea.id)"
                            >
                              {{ $t('mainIndex.showInformationText') }}
                            </el-button>
                          </el-descriptions-item>
                        </el-descriptions>
                      </div>
                      <div class="click-video-details">
                        <el-tooltip
                          effect="dark"
                          :content="$t('mainIndex.toVideoText')"
                          placement="right"
                        >
                          <a
                            target="_blank"
                            :href="mickTea.videoAddress"
                            class="click-video-details-a"
                          >
                            <i class="el-icon-d-arrow-right"></i>
                          </a>
                        </el-tooltip>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </span>
      </el-carousel>
    </el-card>
    <el-card
      v-loading="fullLoading"
      v-infinite-scroll="loadQueryVideo"
      infinite-scroll-distance="15"
      shadow="hover"
      class="update-date-card-fa"
    >
      <div class="update-date-card">
        <div
          v-for="(item, index) in evaluationBasicList"
          :key="index + 'update_date'"
          class="update-date-for"
        >
          <el-row v-if="index % 2 === 0" :key="index" :gutter="100">
            <el-col :span="12">
              <el-row :gutter="24">
                <el-col :span="12">
                  <el-tooltip
                    effect="light"
                    :content="item.mainTitle"
                    placement="right"
                  >
                    <a
                      :href="item.videoAddress"
                      style="height: 100%"
                      target="_blank"
                    >
                      <el-image
                        class="update-date-img"
                        :src="item.coverImage"
                        fit="fill"
                      ></el-image>
                    </a>
                  </el-tooltip>
                </el-col>
                <!--              <div style="float: left">-->

                <!--              </div>-->
                <!--              <div style="float: right">-->
                <el-col :span="12">
                  <el-descriptions :column="1" :title="item.mainTitle">
                    <el-descriptions-item>
                      <p class="describe-date-p-d">
                        {{ item.evaluationDate }}
                        <span style="color: #53a5ff">
                          {{ item.labelValue }}
                        </span>
                      </p>
                    </el-descriptions-item>
                    <el-descriptions-item>
                      <p class="update-date-card-subheading">
                        {{ item.subheading }}
                      </p>
                    </el-descriptions-item>
                    <el-descriptions-item>
                      <el-button
                        class="custom-btn-evaluation btn-13"
                        style="
                          margin-block: 4px;
                          margin-bottom: 10px;
                          margin-left: 20px;
                        "
                        @click="clickProject(item.id)"
                      >
                        {{ $t('mainIndex.showInformationText') }}
                      </el-button>
                    </el-descriptions-item>
                  </el-descriptions>
                </el-col>
              </el-row>

              <!--              </div>-->
            </el-col>
            <el-col v-if="index + 1 < evaluationBasicList.length" :span="12">
              <!--              <div style="float: left" class="update-date-left">-->
              <el-row :gutter="24">
                <el-col :span="12">
                  <el-tooltip
                    effect="light"
                    :content="evaluationBasicList[index + 1].mainTitle"
                    placement="right"
                  >
                    <a
                      :href="evaluationBasicList[index + 1].videoAddress"
                      style="height: 100%"
                      target="_blank"
                    >
                      <el-image
                        class="update-date-img"
                        :src="evaluationBasicList[index + 1].coverImage"
                        fit="fill"
                      ></el-image>
                    </a>
                  </el-tooltip>
                </el-col>
                <!--              </div>-->
                <!--              <div style="float: right" class="update-date-right">-->
                <el-col :span="12">
                  <el-descriptions
                    :column="1"
                    :title="evaluationBasicList[index + 1].mainTitle"
                  >
                    <el-descriptions-item>
                      <p class="describe-date-p-d">
                        {{ evaluationBasicList[index + 1].evaluationDate }}
                        <span style="color: #53a5ff">
                          {{ evaluationBasicList[index + 1].labelValue }}
                        </span>
                      </p>
                    </el-descriptions-item>
                    <el-descriptions-item>
                      <p class="update-date-card-subheading">
                        {{ evaluationBasicList[index + 1].subheading }}
                      </p>
                    </el-descriptions-item>
                    <el-descriptions-item>
                      <el-button
                        class="custom-btn-evaluation btn-13"
                        style="
                          margin-block: 4px;
                          margin-bottom: 10px;
                          margin-left: 20px;
                        "
                        @click="clickProject(evaluationBasicList[index + 1].id)"
                      >
                        {{ $t('mainIndex.showInformationText') }}
                      </el-button>
                    </el-descriptions-item>
                  </el-descriptions>
                  <!--              </div>-->
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>
        <square-loader
          style="
            width: 100%;
            font-size: 20px;
            text-align: center;
            padding-bottom: 30px;
          "
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

<script src="../../index/components/js/MainIndex.js"></script>
<style scoped>
  .custom-btn-evaluation button {
    padding-bottom: 3px;
  }
  .custom-btn-evaluation {
    text-align: center;
    width: 230px;
    height: 30px;
    color: #fff;
    border-radius: 5px;
    padding: 2px;
    font-family: 'Lato', sans-serif;
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
  /* 13 */
  .btn-13 {
    background-color: #a3b0ff;
    background-image: linear-gradient(315deg, #bcd7ff 0%, #75a1ff 74%);
    border: none;
    z-index: 1;
  }
  .btn-13:after {
    position: absolute;
    content: '';
    width: 100%;
    height: 0;
    bottom: 0;
    left: 0;
    z-index: -1;
    border-radius: 5px;
    background-color: #6d9eff;
    background-image: linear-gradient(315deg, #8194ff 0%, #aec7ff 74%);
    box-shadow: -7px -7px 20px 0px #fff9, -4px -4px 5px 0px #fff9,
      7px 7px 20px 0px #0002, 4px 4px 5px 0px #0001;
    transition: all 0.3s ease;
  }
  .btn-13:hover {
    color: #fff;
  }
  .btn-13:hover:after {
    top: 0;
    height: 100%;
  }
  .btn-13:active {
    top: 2px;
  }
  .update-date-card
    .update-date-for
    ::v-deep
    html
    body
    .vue-admin-beautiful-wrapper
    .app-main-container
    > [class*='-container']
    * {
    background: rgba(238, 238, 255, 0.71);
  }
  .update-date-card
    .update-date-for
    .update-date-left
    ::v-deep
    .el-descriptions-item__container
    .el-descriptions-item__content {
    background: #eeeeffe8;
  }
  .update-date-card
    .update-date-for
    .update-date-right
    ::v-deep
    .el-descriptions-item__container
    .el-descriptions-item__content {
    background: rgba(255, 255, 255, 0.75);
  }
  .update-date-card
    .update-date-for
    ::v-deep
    .el-descriptions--small:not(.is-bordered)
    .el-descriptions-item__cell {
    padding: 0;
  }
  .update-date-card .update-date-for .describe-date-p-d {
    padding-left: 10px;
    font-family: 宋体, serif;
    font-size: 3px;
    color: #5d5d5d;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
  .update-date-card .update-date-for .update-date-card-subheading {
    /*height: 50px;*/
    padding-left: 10px;
    font-family: 楷体, serif;
    font-size: 10px;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
  }
  .update-date-card .update-date-for ::v-deep .el-descriptions__title {
    padding-top: 10px;
    padding-left: 5px;
    padding-bottom: 0;
    font-family: 楷体, serif;
  }
  .update-date-card
    .update-date-for
    ::v-deep
    .el-descriptions-item__label:not(.is-bordered-label) {
    display: none;
  }
  .update-date-card ::v-deep .el-descriptions {
    padding-left: 20px;
    width: 280px;
  }
  .update-date-card .update-date-img {
    width: 300px;
    height: 200px;
    border-radius: 20px;
  }
  .update-date-card .update-date-for {
    padding: 5px;
  }
  .update-date-card-fa {
    overflow-y: auto;
    height: 450px;
    width: 100%;
    border-radius: 20px;
  }
  .update-date-card {
    /*overflow: auto;*/
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.7);
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);
    width: 100%;
    height: 100%;
  }
  .describe-list ::v-deep .el-descriptions__header {
    margin-bottom: 5px;
  }
  .describe-list
    ::v-deep
    .el-descriptions
    :not(.is-bordered)
    .el-descriptions-item__cell {
    padding-bottom: 1px;
  }
  .milk-tea-box {
    border-radius: 20px;
  }
  .custom-btn-information {
    width: 200px;
    height: 30px;
    color: #ffffff;
    font-family: 楷体, serif;
    border-radius: 5px;
    padding: 10px 25px;
    font-family: 'Lato', sans-serif;
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
  .milk-tea-box .milk-tea-title {
    color: #53a5ff;
    font-weight: bold;
    font-family: 楷体, serif;
    font-size: 25px;
  }
  .btn-information {
    background: #8093ff;
    border: none;
    z-index: 1;
  }
  .btn-information:after {
    position: absolute;
    content: '';
    width: 0;
    height: 100%;
    top: 0;
    right: 0;
    z-index: -1;
    background-color: #a387ff;
    border-radius: 5px;
    box-shadow: inset 2px 2px 2px 0px rgba(255, 255, 255, 0.5),
      7px 7px 20px 0px rgba(0, 0, 0, 0.1), 4px 4px 5px 0px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
  }
  .btn-information:hover {
    color: #fff;
  }
  .btn-information:hover:after {
    left: 0;
    width: 100%;
  }
  .btn-information:active {
    top: 2px;
  }
  .border .describe-list {
    height: 100%;
    width: 90%;
  }
  .describe-list .describe-list-p {
    font-family: 思源宋体, serif;
    font-size: 10px;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
  .describe-list .describe-list-p-d {
    font-family: 宋体, serif;
    font-size: 3px;
    color: #5d5d5d;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    overflow: hidden;
  }
  .click-video-details-a {
    border-radius: 10px;
    background-color: rgba(236, 236, 236, 0.7);
    height: 70%;
    display: flex;
    align-items: center;
  }
  .border .click-video-details {
    height: 100%;
    display: flex;
    align-items: center;
    width: 10%;
  }
  .border ::v-deep .el-descriptions__title {
    font-size: 15px;
    font-family: 楷体, serif;
  }
  .border ::v-deep .el-descriptions {
    transition: opacity 1s;
    color: #000000;
    width: 95%;
    height: 100%;
    text-align: left;
  }
  .border
    .describe-list
    ::v-deep
    .el-descriptions-item__label:not(.is-bordered-label) {
    display: none;
  }
  .mickTea .card-show {
    justify-content: center;
    flex-direction: row;
    display: flex;
  }
  .mickTea .container {
    display: flex;
  }
  .mickTea .card {
    box-shadow: 0 23px 20px -20px #787878;
    width: 280px;
    height: 180px;
    border-radius: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 15px;
    position: relative;
    overflow: hidden;
    transition: 0.8s;
  }
  .mickTea .el-image {
    position: relative;
  }
  .mickTea .el-image .el-image__inner {
    position: absolute;
    z-index: 9;
  }
  .mickTea .el-image .border {
    display: flex;
    text-align: center;
    padding: 10px;
    position: absolute;
    z-index: 8;
    opacity: 0;
    /*background-color: #cbf0ff;*/
    width: 100%;
    height: 100%;
    border-radius: 20px;
    /*transition: border 1s;*/
  }
  .mickTea .el-image .border:hover {
    border: 1px solid #fff;
    transition: opacity 0.3s;
  }
  /* 卡片一 */
  .mickTea .card .el-image .img-card {
    background-position: right bottom;
    background-size: 300px;
  }
  .mickTea .card :hover .el-image__inner {
    transition: opacity 0.3s;
    opacity: 0;
    /*display: none !important;*/
  }
  .mickTea .card :hover .border {
    transition: opacity 0.3s;
    opacity: 1;
    z-index: 10;
  }

  .clearfix .frame {
    width: 90%;
    margin: 40px auto;
    text-align: center;
  }
  .clearfix .frame button {
    margin-left: 10px;
    margin-top: 4px;
  }
  .custom-btn {
    width: 90px;
    height: 40px;
    color: #fff;
    border-radius: 10px;
    padding: 10px 25px;
    font-family: 'Lato', sans-serif;
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
  .btn-6 {
    background: rgb(115, 159, 239);
    background: radial-gradient(circle, rgb(113, 134, 244) 0%, #abc8ff 100%);
    line-height: 42px;
    padding: 0;
    border: none;
  }
  .btn-6 span {
    position: relative;
    display: block;
    width: 100%;
    height: 100%;
  }
  .btn-6:before,
  .btn-6:after {
    position: absolute;
    content: '';
    height: 0%;
    width: 1px;
    box-shadow: -1px -1px 20px 0px rgba(255, 255, 255, 1),
      -4px -4px 5px 0px rgba(255, 255, 255, 1),
      7px 7px 20px 0px rgba(0, 0, 0, 0.4), 4px 4px 5px 0px rgba(0, 0, 0, 0.3);
  }
  .btn-6:before {
    right: 0;
    top: 0;
    transition: all 500ms ease;
  }
  .btn-6:after {
    left: 0;
    bottom: 0;
    transition: all 500ms ease;
  }
  .btn-6:hover {
    background: transparent;
    color: #a1beffff;
    box-shadow: none;
  }
  .btn-6:hover:before {
    transition: all 500ms ease;
    height: 100%;
  }
  .btn-6:hover:after {
    transition: all 500ms ease;
    height: 100%;
  }
  .btn-6 span:before,
  .btn-6 span:after {
    position: absolute;
    content: '';
    box-shadow: -1px -1px 20px 0px rgba(255, 255, 255, 1),
      -4px -4px 5px 0px rgba(255, 255, 255, 1),
      7px 7px 20px 0px rgba(0, 0, 0, 0.4), 4px 4px 5px 0px rgba(0, 0, 0, 0.3);
  }
  .btn-6 span:before {
    left: 0;
    top: 0;
    width: 0%;
    height: 0.5px;
    transition: all 500ms ease;
  }
  .btn-6 span:after {
    right: 0;
    bottom: 0;
    width: 0%;
    height: 0.5px;
    transition: all 500ms ease;
  }
  .btn-6 span:hover:before {
    width: 100%;
  }
  .btn-6 span:hover:after {
    width: 100%;
  }

  .label-header ::v-deep .el-card__body {
    padding: 10px;
  }
  .label-header .box-card {
    text-align: center;
    background: #edf9ff;
    border-radius: 30px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: '';
  }

  .clearfix:after {
    clear: both;
  }

  .box-card {
    width: 100%;
    height: 15%;
  }
</style>
