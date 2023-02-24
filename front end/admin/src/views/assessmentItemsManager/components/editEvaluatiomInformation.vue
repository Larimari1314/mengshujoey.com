<template>
  <div class="el-popup-parent--hidden">
    <el-dialog
      title="修改项目基本信息"
      :visible.sync="dialogFormVisible"
      width="700px"
      @open="onOpen"
      @close="onClose"
    >
      <el-form
        ref="project"
        :model="project"
        :rules="project_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="视频地址" prop="videoAddress">
          <el-input
            v-model="project.videoAddress"
            placeholder="请输入视频地址"
            clearable
            prefix-icon="el-icon-link"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="主标题" prop="mainTitle">
          <el-input
            v-model="project.mainTitle"
            placeholder="请输入主标题"
            clearable
            prefix-icon="el-icon-s-management"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="副标题" prop="subheading">
          <el-input
            v-model="project.subheading"
            type="textarea"
            placeholder="请输入副标题"
            prefix-icon="el-icon-s-unfold"
            clearable
            :autosize="{ minRows: 4, maxRows: 10 }"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="labelId">
          <el-select
            v-model="project.labelId"
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
        <el-form-item label="测评时间" prop="evaluationDateCode">
          <el-input
            v-model="project.evaluationDateCode"
            placeholder="请输入测评时间"
            clearable
            prefix-icon="el-icon-time"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="封面截图" prop="coverImage">
          <el-upload
            ref="coverImage"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="coverImagefileList"
            :action="coverImageAction"
            :before-upload="coverImageBeforeUpload"
            :on-success="successUploadCoverImage"
            list-type="picture-card"
            accept="image/*"
            name="coverImage"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item
          v-if="evaluationImageVisible"
          label="测评图片"
          prop="evaluationImage"
        >
          <el-upload
            ref="evaluationImage"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="evaluationImagefileList"
            :action="evaluationImageAction"
            :before-upload="evaluationImageBeforeUpload"
            :on-success="successUploadEvaluationImage"
            list-type="picture-card"
            accept="image/*"
            name="evaluationImage"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="close">取消</el-button>
        <el-button
          type="primary"
          :loading="editEvaluationDis"
          @click="handelConfirm"
        >
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script src="../js/editEvaluationInformation.js" />

<style scoped>
  .el-upload__tip {
    line-height: 1.2;
  }
  .select-assessment-mode {
    text-align: center;
    margin: auto;
  }
</style>
