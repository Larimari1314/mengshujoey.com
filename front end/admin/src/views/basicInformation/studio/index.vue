<template>
  <div class="assessmentItemsManager_index">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-row
          v-show="
            permissions.indexOf('6') !== -1 || permissions.indexOf('19') !== -1
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
            <el-col :span="14">
              <el-form-item label-width="0" prop="studioName">
                <el-input
                  v-model="queryData.studioName"
                  placeholder="请输入工作室昵称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label-width="0">
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="small"
                  @click="queryStudioData"
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
            permissions.indexOf('6') !== -1 || permissions.indexOf('20') !== -1
          "
          icon="el-icon-plus"
          type="primary"
          @click="addStudio"
        >
          添加
        </el-button>
        <el-button
          v-show="
            permissions.indexOf('6') !== -1 || permissions.indexOf('21') !== -1
          "
          icon="el-icon-delete"
          type="danger"
          :disabled="multipleSelection.length === 0"
          @click="deleteStudioByIds"
        >
          删除
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="StudioData"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column label="成员头像" header-align="center" align="center">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.memberAvatar"
            :lazy="lazy"
            :preview-src-list="[scope.row.memberAvatar]"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column
        label="成员id"
        width="100"
        header-align="center"
        align="center"
        prop="id"
      ></el-table-column>
      <el-table-column
        label="工作室成员名"
        header-align="center"
        align="center"
        prop="memberName"
      ></el-table-column>
      <el-table-column label="操作" header-align="center" align="center">
        <template slot-scope="scope">
          <el-button
            v-show="
              permissions.indexOf('6') !== -1 ||
              permissions.indexOf('22') !== -1
            "
            icon="el-icon-edit"
            type="primary"
            round
            @click="editThisStudio(scope.row)"
          >
            修改成员数据
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :background="background"
      :current-page="queryFormageSize"
      :total="total"
      :page-size="pageSize"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    ></el-pagination>
    <el-dialog
      title="添加成员信息"
      width="700px"
      :visible.sync="dialogFormVisible"
      @close="onClose"
    >
      <el-form
        ref="studioInformation"
        :model="studioInformation"
        :rules="studioInformation_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="成员名称" prop="memberName">
          <el-input
            v-model="studioInformation.memberName"
            placeholder="请输入成员名称"
            clearable
            prefix-icon="el-icon-user"
            :style="{ width: '50%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="成员头像" prop="memberAvatar">
          <el-upload
            ref="memberAvatar"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="memberAvatarfileList"
            :action="memberAvatarAction"
            :before-upload="memberAvatarBeforeUpload"
            :on-success="successUploadStudioImage"
            list-type="picture-card"
            accept="image/*"
            name="memberAvatar"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="onClose">取消</el-button>
        <el-button type="primary" @click="handelConfirm">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="修改成员信息"
      width="700px"
      :visible.sync="dialogEditVisible"
      @close="onEditClose"
    >
      <el-form
        ref="editStudioInformation"
        :model="editStudioInformation"
        :rules="editStudioInformation_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="成员名称" prop="memberName">
          <el-input
            v-model="editStudioInformation.memberName"
            placeholder="请输入成员名称"
            clearable
            prefix-icon="el-icon-user"
            :style="{ width: '50%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="成员头像" prop="memberAvatar">
          <el-upload
            ref="memberEditAvatar"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="editMemberAvatarfileList"
            :action="memberAvatarAction"
            :before-upload="memberAvatarBeforeUpload"
            :on-success="successEditUploadStudioImage"
            list-type="picture-card"
            accept="image/*"
            name="memberAvatar"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="onEditClose">取消</el-button>
        <el-button type="primary" @click="editHandelConfirm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="../studio/js/studioManager.js"></script>

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
