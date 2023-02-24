<template>
  <div class="roleManager_index">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-row :gutter="2">
          <el-form
            ref="labelData"
            :model="queryData"
            size="small"
            label-width="80px"
            label-position="left"
          >
            <el-col :span="12">
              <el-form-item label-width="0" prop="value">
                <el-input
                  v-model="queryData.value"
                  placeholder="角色名称"
                  clearable
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label-width="0">
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="small"
                  :loading="queryDataDialog"
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
        <el-button icon="el-icon-plus" type="primary" @click="addRoleInfo">
          添加
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table :data="roleList" style="width: 100%">
      <el-table-column
        label="角色id"
        width="70"
        header-align="center"
        align="center"
        prop="id"
      ></el-table-column>
      <el-table-column
        label="角色名称"
        header-align="center"
        align="center"
        prop="name"
      ></el-table-column>
      <el-table-column label="操作权限" header-align="center" align="center">
        <template slot-scope="scope">
          <el-tree
            ref="tree"
            class="editTree"
            :data="moduleData"
            show-checkbox
            node-key="id"
            accordion
            render-after-expand
            highlight-current
            :default-checked-keys="scope.row.moduleList"
            @check="nodeClick"
          ></el-tree>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        header-align="center"
        align="center"
        width="200px"
      >
        <template slot-scope="scope">
          <el-button type="primary" @click="editRoleInfo(scope.row)">
            修改角色信息
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
      @close="onAddRoleClose"
    >
      <el-form
        ref="addRole"
        :model="addRole"
        :rules="addRole_rule"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input
            v-model="addRole.name"
            placeholder="请输入角色名称"
            :maxlength="10"
            clearable
            prefix-icon="el-icon-user-solid"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="权限列表">
          <el-tree
            ref="editTree"
            :data="editModuleList"
            show-checkbox
            node-key="id"
            accordion
            render-after-expand
            highlight-current
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          :loading="addRoleInfoDialog"
          @click="submitForm"
        >
          提交
        </el-button>
        <el-button @click="onAddRoleClose">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="修改角色信息"
      :visible.sync="dialogEditRoleVisible"
      width="500px"
      @close="onEditRoleClose"
    >
      <el-form
        ref="editRole"
        :model="editRole"
        :rules="editRole_rule"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input
            v-model="editRole.name"
            placeholder="请输入角色名称"
            :maxlength="10"
            clearable
            prefix-icon="el-icon-user-solid"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="权限列表">
          <el-tree
            ref="editRoleTree"
            :data="editModuleList"
            show-checkbox
            node-key="id"
            accordion
            :default-checked-keys="editRole.moduleList"
            render-after-expand
            highlight-current
          ></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button
          type="primary"
          :loading="editRoleInfoDialog"
          @click="submitEditForm"
        >
          提交
        </el-button>
        <el-button @click="onEditRoleClose">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="../role/js/roleManager.js"></script>
<style lang="scss" scoped>
  .roleManager_index {
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
