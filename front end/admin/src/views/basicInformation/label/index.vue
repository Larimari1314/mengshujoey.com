<template>
  <div class="assessmentItemsManager_index">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-row
          v-show="
            permissions.indexOf('5') !== -1 || permissions.indexOf('15') !== -1
          "
          :gutter="2"
        >
          <el-form
            ref="labelData"
            :model="queryData"
            size="small"
            label-width="80px"
            label-position="left"
          >
            <el-col :span="12">
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
                  @click="queryLabelData"
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
            permissions.indexOf('5') !== -1 || permissions.indexOf('16') !== -1
          "
          icon="el-icon-plus"
          type="primary"
          @click="addLabel"
        >
          添加
        </el-button>
        <el-button
          v-show="
            permissions.indexOf('5') !== -1 || permissions.indexOf('17') !== -1
          "
          icon="el-icon-delete"
          type="danger"
          :disabled="multipleSelection.length === 0"
          @click="deleteLabel"
        >
          删除
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-show="
        permissions.indexOf('5') !== -1 || permissions.indexOf('15') !== -1
      "
      :data="labelData"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        label="标签id"
        width="70"
        header-align="center"
        align="center"
        prop="id"
      ></el-table-column>
      <el-table-column
        label="标签值"
        header-align="center"
        align="center"
        prop="value"
      ></el-table-column>
      <el-table-column label="绑定视频" header-align="center" align="center">
        <template slot-scope="scope">
          <div
            v-for="(item, index) in scope.row.labelList"
            :key="'lableList_' + index"
          >
            <span v-if="0 <= index < 4">
              <el-link target="_blank" :href="item.address" icon="el-icon-link">
                {{ item.mainTitle }}
              </el-link>
            </span>
            <span v-if="index > 4">...</span>
          </div>
          <span
            v-if="
              scope.row.labelList === undefined ||
              scope.row.labelList.length === 0
            "
            style="color: #949494"
          >
            当前暂无数据
          </span>
        </template>
      </el-table-column>
      <el-table-column label="操作" header-align="center" align="center">
        <template slot-scope="scope">
          <el-button
            v-show="
              permissions.indexOf('5') !== -1 ||
              permissions.indexOf('18') !== -1
            "
            icon="el-icon-edit"
            type="primary"
            round
            @click="editThisLabel(scope.row)"
          >
            修改标签数据
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页条-->
    <el-pagination
      :background="background"
      :current-page="queryFormageSize"
      :total="total"
      :page-size="pageSize"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    ></el-pagination>
    <el-dialog
      :title="addTitle"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="onAddlabelClose"
    >
      <el-form
        ref="labelAddFromRef"
        :model="labelAddFrom"
        :rules="labelAddFrom_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="标签值" prop="labelValue">
          <el-input
            v-model="labelAddFrom.labelValue"
            placeholder="请输入标签值"
            :maxlength="10"
            clearable
            prefix-icon="el-icon-goblet-square-full"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitAddLabelForm">添加</el-button>
        <el-button @click="onAddlabelClose">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="editTitle"
      :visible.sync="dialogEditFormVisible"
      width="500px"
      @close="onEditlabelClose"
    >
      <el-form
        ref="labelAddFromRef"
        :model="labelEditFrom"
        :rules="labelEditFrom_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="标签值" prop="value">
          <el-input
            v-model="labelEditFrom.value"
            placeholder="请输入标签值"
            :maxlength="10"
            clearable
            prefix-icon="el-icon-goblet-square-full"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="submitEditLabelForm">添加</el-button>
        <el-button @click="onEditlabelClose">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="../label/js/labelManager.js"></script>

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
