<template>
  <div class="el-popup-parent--hidden">
    <el-dialog
      title="修改基本信息"
      :visible.sync="dialogFormVisible"
      width="700px"
      @open="onOpen"
      @close="onClose"
    >
      <el-collapse
        v-for="(item, index) in project"
        :key="index + 'project'"
        v-model="activeNames"
        accordion
        @change="handleChange"
      >
        <el-collapse-item :title="item.projectName" :name="index">
          <el-descriptions :title="item.projectName">
            <el-descriptions-item label="项目封面">
              <el-image
                style="width: 100px; height: 100px"
                :src="item.projectImage"
                fit="fill"
              ></el-image>
            </el-descriptions-item>
            <el-descriptions-item label="推荐等级">
              {{ item.recommendValue }}
            </el-descriptions-item>
            <el-descriptions-item
              v-for="(detailItem, indexes) in detailInformationKeys"
              :key="'detailInformation' + indexes"
              :label="detailItem"
            >
              <span v-if="!assessmentMode">
                {{ JSON.parse(item.detailInformation)[detailItem] }}
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="操作">
              <el-tooltip
                v-show="
                  permissions.indexOf('14') !== -1 ||
                  permissions.indexOf('3') !== -1
                "
                class="item"
                effect="dark"
                content="修改数据"
                placement="top"
              >
                <el-button
                  type="primary"
                  icon="el-icon-edit"
                  circle
                  @click="editDetailProjectInformation(item)"
                ></el-button>
              </el-tooltip>
              <el-tooltip
                v-show="
                  permissions.indexOf('13') !== -1 ||
                  permissions.indexOf('3') !== -1
                "
                class="item"
                effect="dark"
                content="删除数据"
                placement="top"
              >
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  circle
                  @click="deleteDetailProjectInformation(item)"
                ></el-button>
              </el-tooltip>
            </el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
      </el-collapse>
      <div slot="footer">
        <el-button @click="close">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="修改项目信息"
      :visible.sync="editDetailVisible"
      width="700px"
      @close="detailOnClose"
    >
      <el-row :gutter="10">
        <el-form
          ref="project"
          :model="detailInformation"
          :rules="project_rules"
          size="small"
          label-width="80px"
          label-position="right"
        >
          <el-row>
            <el-col :span="24">
              <el-form-item label="项目名称" prop="projectName">
                <el-input
                  v-model="detailInformation.projectName"
                  placeholder="请输入项目名称"
                  clearable
                  prefix-icon="el-icon-s-home"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="推荐等级" prop="recommendLevel">
                <el-select
                  v-model="detailInformation.recommendLevel"
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
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="产品封面" prop="projectImage">
                <el-upload
                  ref="projectImage"
                  :limit="1"
                  :on-exceed="fileNumberGoBeyond"
                  :headers="imageConfig"
                  :file-list="projectImagefileList"
                  :action="projectImageAction"
                  :before-upload="coverImageListBeforeUpload"
                  list-type="picture-card"
                  :on-success="successProjectCover"
                  accept="image/*"
                  name="projectImage"
                >
                  <i class="el-icon-plus"></i>
                  <div slot="tip" class="el-upload__tip">
                    只能上传不超过 2MB 的image/*文件
                  </div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <div
            v-for="(item, index) in editDetailInformationKeys"
            :key="'editDetailInformation_' + index"
          >
            <el-row>
              <el-col :span="3">
                <el-form-item label-width="0">
                  <el-input
                    v-model="item.name"
                    placeholder="信息"
                    :style="{ width: '100%' }"
                  ></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="21">
                <el-form-item label-width="0">
                  <el-input
                    v-model="item.value"
                    placeholder="请输入推荐值"
                    clearable
                    prefix-icon="el-icon-check"
                    :style="{ width: '100%' }"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <div
            v-for="(studioMember, indexs) in studioMemberList"
            :key="'index2_' + indexs"
          >
            <el-row>
              <el-col :span="1">
                <el-avatar size="small" :src="studioMember.avatar"></el-avatar>
                &nbsp;
              </el-col>
              <el-col :span="19">
                <el-form-item :label="studioMember.name">
                  <el-input
                    v-model="studioMember.fraction"
                    type="number"
                    placeholder="请输入分数"
                    clearable
                    prefix-icon="el-icon-message-solid"
                    :style="{ width: '100%' }"
                    @input="studioMemberChange($event, studioMember)"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
          <el-row>
            <div>
              <el-button @click="detailOnClose">取消</el-button>
              <el-button
                type="primary"
                :loading="editDetailInfomationDis"
                @click="editDetailInformation"
              >
                确定
              </el-button>
            </div>
          </el-row>
        </el-form>
      </el-row>
    </el-dialog>
  </div>
</template>

<script src="../js/editDetailInformation.js" />

<style scoped></style>
