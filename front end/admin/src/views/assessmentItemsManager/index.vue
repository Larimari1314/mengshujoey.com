<template>
  <div class="assessmentItemsManager_index">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-row
          v-show="
            permissions.indexOf('11') !== -1 || permissions.indexOf('3') !== -1
          "
          :gutter="2"
        >
          <el-form
            ref="detailProject"
            :model="queryData"
            size="small"
            label-width="80px"
            label-position="left"
          >
            <el-col :span="8">
              <el-form-item label-width="0" prop="text">
                <el-input
                  v-model="queryData.text"
                  placeholder="主/副标题"
                  clearable
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label-width="0" prop="labelId">
                <el-select
                  v-model="queryData.labelId"
                  placeholder="查询标签"
                  clearable
                  :style="{ width: '100%' }"
                >
                  <el-option
                    v-for="(item, index) in labelIdOptions"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                    :disabled="item.disabled"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label-width="0">
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="small"
                  @click="queryEvaluationData"
                >
                  搜索
                </el-button>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button
          v-show="
            permissions.indexOf('12') !== -1 || permissions.indexOf('3') !== -1
          "
          icon="el-icon-plus"
          type="primary"
          @click="handleAdd"
        >
          添加
        </el-button>
        <el-button
          v-show="
            permissions.indexOf('13') !== -1 || permissions.indexOf('3') !== -1
          "
          icon="el-icon-delete"
          type="danger"
          :disabled="multipleSelection.length === 0"
          @click="handleDelete"
        >
          删除
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-show="
        permissions.indexOf('11') !== -1 || permissions.indexOf('3') !== -1
      "
      :data="evaluationProject"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="封面图片" header-align="center" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 180px; height: 100px"
            :lazy="lazy"
            :src="scope.row.coverImage"
            :preview-src-list="[scope.row.coverImage, scope.row.coverImage]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="标题信息">
        <template slot-scope="scope">
          <el-popover trigger="hover" placement="top">
            <p>主标题: {{ scope.row.mainTitle }}</p>
            <p>副标题: {{ scope.row.subheading }}</p>
            <div slot="reference" class="name-wrapper">
              <el-link
                icon="el-icon-link"
                :href="scope.row.videoAddress"
                target="_blank"
              >
                {{ scope.row.mainTitle }}
              </el-link>
            </div>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="测评标签" header-align="center" width="80">
        <template slot-scope="scope">
          <el-tag type="success" size="medium">
            {{ scope.row.labelValue }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="测评模式" header-align="center" width="80">
        <template slot-scope="scope">
          <el-tag
            v-if="scope.row.scoringClass === '团队测评'"
            style="color: #dbccb1"
            size="medium"
            color="#94aa67"
          >
            {{ scope.row.scoringClass }}
          </el-tag>
          <el-tag
            v-if="scope.row.scoringClass === '个人测评'"
            style="color: #467897"
            size="medium"
            color="#E7CD79"
          >
            {{ scope.row.scoringClass }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="点击量"
        header-align="center"
        prop="clickNumber"
        sortable
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-tooltip
            v-if="parseInt(scope.row.clickNumber) > 100"
            class="item"
            effect="dark"
            :content="scope.row.clickNumber + '点击量'"
            placement="left"
          >
            <el-image style="width: 100px; height: 100px" :src="hotImage">
              {{ scope.row.clickNumber }}
            </el-image>
          </el-tooltip>
          <div v-if="scope.row.clickNumber <= 100">
            {{ scope.row.clickNumber }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="evaluationDate"
        label="测评日期"
        align="center"
      ></el-table-column>
      <el-table-column label="操作" width="250">
        <template slot-scope="scope">
          <el-button
            v-show="
              permissions.indexOf('14') !== -1 ||
              permissions.indexOf('3') !== -1
            "
            icon="el-icon-edit"
            type="primary"
            @click="editDetailInformation(scope.row)"
          >
            基本
          </el-button>
          <el-button
            v-show="
              permissions.indexOf('14') !== -1 ||
              permissions.indexOf('3') !== -1
            "
            icon="el-icon-edit"
            type="primary"
            @click="editEvaluationInformation(scope.row)"
          >
            测评
          </el-button>
          <el-tooltip
            class="item"
            effect="dark"
            content="添加项目"
            placement="top"
          >
            <el-button
              v-show="
                permissions.indexOf('12') !== -1 ||
                permissions.indexOf('3') !== -1
              "
              type="primary"
              icon="el-icon-plus"
              circle
              @click="addProjectByDetailId(scope.row)"
            ></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <!--分页条-->
    <el-pagination
      v-show="
        permissions.indexOf('11') !== -1 || permissions.indexOf('3') !== -1
      "
      :background="background"
      :current-page="queryFormageSize"
      :total="total"
      :page-size="pageSize"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    ></el-pagination>
    <assessment-edit ref="edit"></assessment-edit>
    <edit-evaluatiom-information
      ref="editEvaluation"
    ></edit-evaluatiom-information>
    <edit-detail-information
      ref="editDetailInformation"
    ></edit-detail-information>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="700px"
      @close="onClose"
    >
      <el-row :gutter="10">
        <el-form
          ref="projectInformation"
          :model="projectInformation"
          :rules="projectInformation_rules"
          size="small"
          label-width="80px"
          label-position="right"
        >
          <el-col :span="24">
            <el-form-item label="项目名称" prop="projectName">
              <el-input
                v-model="projectInformation.projectName"
                placeholder="请输入项目名称"
                clearable
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="上传封面" prop="projectImage">
              <el-upload
                ref="projectImage"
                :headers="imageConfig"
                :limit="1"
                :on-exceed="fileNumberGoBeyond"
                :file-list="projectImagefileList"
                :action="projectImageAction"
                :on-success="projectImageSuceess"
                :before-upload="projectImageBeforeUpload"
                list-type="picture-card"
                name="projectImage"
              >
                <i class="el-icon-plus"></i>
                <div slot="tip" class="el-upload__tip">
                  只能上传不超过 2MB 的文件
                </div>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="测评级别" prop="recommendLevel">
              <el-select
                v-model="projectInformation.recommendLevel"
                placeholder="请选择测评级别"
                clearable
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in recommendLevelOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <div v-if="nowAddProject.scoringClass === '团队测评'">
            <el-col :span="3">
              <el-form-item label-width="0" prop="recommendOne">
                <el-input
                  v-model="projectInformation.recommendOne"
                  placeholder="信息"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="21">
              <el-form-item label-width="0" prop="recommendOneValue">
                <el-input
                  v-model="projectInformation.recommendOneValue"
                  placeholder="请输入推荐数值"
                  clearable
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label-width="0" prop="recommendTwo">
                <el-input
                  v-model="projectInformation.recommendTwo"
                  placeholder="信息"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="21">
              <el-form-item label-width="0" prop="recommendTwoValue">
                <el-input
                  v-model="projectInformation.recommendTwoValue"
                  placeholder="请输入推荐数值"
                  clearable
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-form-item label-width="0" prop="recommendThree">
                <el-input
                  v-model="projectInformation.recommendThree"
                  placeholder="信息"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="21">
              <el-form-item label-width="0" prop="recommendThreeValue">
                <el-input
                  v-model="projectInformation.recommendThreeValue"
                  placeholder="请输入推荐数值"
                  clearable
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <div
                v-for="(studioMember, index) in studioMemberList"
                :key="'info4-' + index"
              >
                <el-row>
                  <el-col :span="1">
                    <el-avatar
                      size="small"
                      :src="studioMember.avatar"
                    ></el-avatar>
                  </el-col>
                  <el-col :span="19">
                    <el-form-item :label="studioMember.name">
                      <el-input
                        v-model="studioMember.fraction"
                        type="number"
                        placeholder="请输入分数"
                        clearable
                        prefix-icon="el-icon-coffee"
                        :style="{ width: '80%' }"
                      ></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </div>
            </el-col>
          </div>
          <el-col :span="24">
            <el-form-item size="large">
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="resetForm">取消</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
    </el-dialog>
  </div>
</template>

<script src="../assessmentItemsManager/js/assessmentManager.js"></script>

<style lang="scss" scoped>
  .assessmentItemsManager_index {
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
