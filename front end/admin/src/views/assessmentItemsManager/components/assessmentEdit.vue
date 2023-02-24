<template>
  <div class="el-popup-parent--hidden">
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
    >
      <div class="select-assessment-mode">
        <el-button size="medium" @click="teamAssessment">团队测评</el-button>
        <el-button type="primary" size="medium" @click="save">
          单人测评
        </el-button>
      </div>
      <div slot="footer" class="dialog-footer"></div>
    </el-dialog>
    <el-dialog
      :title="editTitle"
      :visible.sync="dialogTeamVisible"
      width="700px"
      @close="closeTeam"
    >
      <div>
        <el-alert
          title="添加数据须知"
          type="warning"
          :closable="false"
          description="如果测评项目过多，建议先存入几条数据，之后在表单界面添加数据，防止数据因操作不当丢失"
          show-icon
        ></el-alert>
        <el-divider><i class="el-icon-trophy"></i></el-divider>
        <el-row :gutter="10">
          <el-form
            ref="teamProject"
            :model="teamProject"
            :rules="teamProject_rules"
            size="small"
            label-width="100px"
          >
            <el-col :span="24"></el-col>
            <el-col :span="20">
              <el-form-item label="视频地址" prop="videoAddress">
                <el-input
                  v-model="teamProject.videoAddress"
                  placeholder="请输入视频地址"
                  clearable
                  prefix-icon="el-icon-link"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <!--<el-form-item label-width="0">
                <el-button
                  :loading="importVisible"
                  type="primary"
                  icon="el-icon-refresh"
                  size="medium"
                  @click="oneClickImport"
                >
                  一键导入
                </el-button>
              </el-form-item>-->
            </el-col>
            <el-col :span="24">
              <el-form-item label="主标题" prop="mainTitle">
                <el-input
                  v-model="teamProject.mainTitle"
                  placeholder="请输入主标题"
                  show-word-limit
                  clearable
                  prefix-icon="el-icon-sunrise-1"
                  :style="{ width: '80%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="副标题" prop="subheading">
                <el-input
                  v-model="teamProject.subheading"
                  placeholder="请输入副标题"
                  clearable
                  type="textarea"
                  prefix-icon="el-icon-s-unfold"
                  :autosize="{ minRows: 4, maxRows: 10 }"
                  :style="{ width: '80%' }"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="测评日期" prop="evaluationDate">
                <el-date-picker
                  v-model="teamProject.evaluationDate"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  :style="{ width: '80%' }"
                  placeholder="请选择测评日期"
                  clearable
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="标签" prop="labelId">
                <el-select
                  v-model="teamProject.labelId"
                  placeholder="请择选标签"
                  clearable
                  :style="{ width: '80%' }"
                >
                  <el-option
                    v-for="(item, index) in labelIdOptions"
                    :key="'info-' + index"
                    :label="item.label"
                    :value="item.value"
                    :disabled="item.disabled"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="上传封面" prop="coverImageList">
                <el-upload
                  ref="coverImageList"
                  :limit="1"
                  :on-exceed="fileNumberGoBeyond"
                  :headers="imageConfig"
                  :file-list="coverImageListfileList"
                  :action="coverImageListAction"
                  :before-upload="coverImageListBeforeUpload"
                  :on-success="successCover"
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
            </el-col>
            <el-col :span="24">
              <el-form-item label="测评图表" prop="evaluationImage">
                <el-upload
                  ref="evaluationImage"
                  :limit="1"
                  :on-exceed="fileNumberGoBeyond"
                  :headers="imageConfig"
                  :file-list="assessmentImageList"
                  :action="evaluationImageListAction"
                  :before-upload="coverImageListBeforeUpload"
                  list-type="picture-card"
                  :on-success="successEvaluation"
                  accept="image/*"
                  name="evaluationImage"
                >
                  <i class="el-icon-plus"></i>
                  <div slot="tip" class="el-upload__tip">
                    只能上传不超过 2MB 的image/*文件
                  </div>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <div
                v-for="(items, index) in teamProject.projectItem"
                :key="'info1-' + index"
              >
                <el-collapse v-model="projectModel" accordion>
                  <el-collapse-item :title="items.projectName" :name="index">
                    <el-col :span="23">
                      <el-row>
                        <el-col :span="5">
                          <el-image
                            style="width: 100px; height: 80px"
                            :src="items.projectImage"
                            fit="fill"
                          ></el-image>
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
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="closeTeam">取消</el-button>

        <el-button
          type="primary"
          size="medium"
          :loading="addProjectButton"
          @click="submitProjectDetail"
        >
          添加
        </el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="detailTitle"
      :visible.sync="dialogDetailVisible"
      width="700px"
      @close="closedDetail"
    >
      <el-row :gutter="10">
        <el-form
          ref="detailProject"
          :model="detailProject"
          :rules="detailProject_rules"
          size="small"
          label-width="80px"
        >
          <el-col :span="24">
            <el-form-item label="产品名称" prop="projectName">
              <el-input
                v-model="detailProject.projectName"
                placeholder="请输入产品名称"
                show-word-limit
                clearable
                prefix-icon="el-icon-house"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
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
          <el-col :span="24">
            <el-form-item label="推荐级别" prop="recommendLevel">
              <el-select
                v-model="detailProject.recommendLevel"
                placeholder="请选择推荐级别"
                clearable
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in recommendLevelOptions"
                  :key="'info3-' + index"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label-width="0" prop="recommendOne">
              <el-input
                v-model="detailProject.recommendOne"
                placeholder="基础信息"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label-width="0" prop="informationOne">
              <el-input
                v-model="detailProject.informationOne"
                placeholder="请输入推荐数值"
                clearable
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label-width="0" prop="recommendTwo">
              <el-input
                v-model="detailProject.recommendTwo"
                placeholder="基础信息"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label-width="0" prop="informationTwo">
              <el-input
                v-model="detailProject.informationTwo"
                placeholder="请输入推荐数值"
                clearable
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label-width="0" prop="recommendThree">
              <el-input
                v-model="detailProject.recommendThree"
                placeholder="基础信息"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label-width="0" prop="informationThree">
              <el-input
                v-model="detailProject.informationThree"
                placeholder="请输入推荐数值"
                clearable
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="23">
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
        </el-form>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="medium" @click="closedDetail">取消</el-button>
        <el-button type="primary" size="medium" @click="submitProject">
          添加
        </el-button>
      </div>
    </el-dialog>
    <solo-assessment ref="soloAssessment"></solo-assessment>
  </div>
</template>

<script src="../js/assessmentEdit.js" />
<style lang="scss" scoped>
  .select-assessment-mode {
    text-align: center;
    margin: auto;
  }
</style>
