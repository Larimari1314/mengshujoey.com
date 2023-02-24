<template>
  <div class="el-popup-parent--hidden">
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="700px"
      @close="teamAssessment"
    >
      <div>
        <el-form
          ref="singleProject"
          :model="singleProject"
          :rules="singleProject_rules"
          size="small"
          label-width="80px"
          label-position="left"
        >
          <el-form-item label="视频地址" prop="videoAddress">
            <el-input
              v-model="singleProject.videoAddress"
              placeholder="请输入视频地址"
              clearable
              prefix-icon="el-icon-link"
              :style="{ width: '80%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="主标题" prop="mainTitle">
            <el-input
              v-model="singleProject.mainTitle"
              placeholder="请输入主标题"
              clearable
              :style="{ width: '80%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="副标题" prop="subheading">
            <el-input
              v-model="singleProject.subheading"
              placeholder="请输入副标题"
              clearable
              type="textarea"
              prefix-icon="el-icon-s-unfold"
              :autosize="{ minRows: 4, maxRows: 10 }"
              :style="{ width: '80%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="测评日期" prop="evaluationDate">
            <el-date-picker
              v-model="singleProject.evaluationDate"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '80%' }"
              placeholder="请选择测评日期"
              clearable
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="标签" prop="labelId">
            <el-select
              v-model="singleProject.labelId"
              placeholder="请选择标签"
              clearable
              :style="{ width: '80%' }"
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
          <el-form-item label="上传封面" prop="coverImage">
            <el-upload
              ref="coverImage"
              :limit="1"
              :on-exceed="fileNumberGoBeyond"
              :headers="imageConfig"
              :file-list="coverImagefileList"
              :action="coverImageAction"
              :before-upload="coverImageBeforeUpload"
              :on-success="successCover"
              list-type="picture-card"
              accept="image/*"
              name="coverImage"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
          <el-col :span="24">
            <div
              v-for="(items, index) in singleProject.projectItem"
              :key="'info1-' + index"
            >
              <el-collapse v-model="projectModel" accordion>
                <el-collapse-item :title="items.projectName" :name="index">
                  <el-col :span="23">
                    <el-row>
                      <el-col :span="5">
                        <el-avatar
                          shape="square"
                          size="large"
                          :src="items.projectImage"
                        ></el-avatar>
                      </el-col>
                      <el-col :span="15">
                        <el-descriptions :title="items.projectName">
                          <el-descriptions-item label="产品名称">
                            {{ items.projectName }}
                          </el-descriptions-item>
                          <el-descriptions-item>
                            <el-button
                              type="primary"
                              size="small"
                              @click="editProjectInformations(items, index)"
                            >
                              修改测评信息
                            </el-button>
                          </el-descriptions-item>
                          <el-descriptions-item>
                            <el-button
                              type="danger"
                              size="small"
                              @click="deleteProjectInformations(items, index)"
                            >
                              删除测评信息
                            </el-button>
                          </el-descriptions-item>
                        </el-descriptions>
                      </el-col>
                    </el-row>
                  </el-col>
                </el-collapse-item>
              </el-collapse>
            </div>
          </el-col>
          <el-col :span="24">
            <el-button
              style="width: 100%"
              type="primary"
              icon="el-icon-plus"
              plain
              @click="addDetailProject"
            >
              添加测评项目
            </el-button>
          </el-col>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="resetForm">取消</el-button>
        <el-button
          type="primary"
          size="medium"
          :loading="addProjectButton"
          @click="submitForm"
        >
          添加
        </el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="projectTitle"
      :visible.sync="dialogProjectVisible"
      width="700px"
      @close="teamAssessmentProject"
    >
      <el-form
        ref="detailProject"
        :model="detailProject"
        :rules="detailProject_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="项目名称" prop="projectName">
          <el-input
            v-model="detailProject.projectName"
            placeholder="请输入项目名称"
            clearable
            prefix-icon="el-icon-help"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="推荐等级" prop="recommendLevel">
          <el-select
            v-model="detailProject.recommendLevel"
            placeholder="请选择推荐等级"
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
        <el-form-item label="上传" prop="projectImage">
          <el-upload
            ref="projectImage"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="projectImagefileList"
            :action="projectImageAction"
            :before-upload="coverImageBeforeUpload"
            :on-success="successProjectCover"
            list-type="picture-card"
            accept="image/*"
            name="projectImage"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item size="large">
          <el-button type="primary" @click="submitProjectForm">提交</el-button>
          <el-button @click="resetProjectForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script src="../js/soloAssessment.js" />
<style lang="scss" scoped>
  .select-Single-mode {
    text-align: center;
    margin: auto;
  }
</style>
