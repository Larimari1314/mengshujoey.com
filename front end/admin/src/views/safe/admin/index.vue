<template>
  <div class="roleManager_index">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-row
          v-show="
            permissions.indexOf('7') !== -1 || permissions.indexOf('23') !== -1
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
                  @click="queryAdminData"
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
            permissions.indexOf('7') !== -1 || permissions.indexOf('24') !== -1
          "
          icon="el-icon-plus"
          type="primary"
          @click="addAdminInfo"
        >
          添加
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-show="
        permissions.indexOf('7') !== -1 || permissions.indexOf('23') !== -1
      "
      :data="adminList"
      style="width: 100%"
    >
      <el-table-column
        label="管理员id"
        width="70"
        header-align="center"
        align="center"
        prop="id"
      ></el-table-column>
      <el-table-column label="头像" header-align="center" align="center">
        <template slot-scope="scope">
          <el-avatar :size="100" :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column
        label="管理员名称"
        header-align="center"
        align="center"
        prop="name"
      ></el-table-column>
      <el-table-column
        label="年龄"
        header-align="center"
        align="center"
        prop="age"
      ></el-table-column>
      <el-table-column
        label="生日"
        header-align="center"
        align="center"
        prop="birthday"
      ></el-table-column>
      <el-table-column
        label="邮件"
        header-align="center"
        align="center"
        prop="email"
      ></el-table-column>
      <el-table-column
        label="操作权限"
        header-align="center"
        align="center"
        prop="roleName"
      ></el-table-column>
      <el-table-column
        label="操作"
        header-align="center"
        align="center"
        width="180px"
      >
        <template slot-scope="scope">
          <el-switch
            v-show="
              permissions.indexOf('7') !== -1 ||
              permissions.indexOf('25') !== -1
            "
            v-model="scope.row.statusCode"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="激活"
            inactive-text="禁用"
            @change="changeSwith(scope.row.id, scope.row.status)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        header-align="center"
        align="center"
        width="250px"
      >
        <template slot-scope="scope">
          <el-button
            v-show="
              permissions.indexOf('7') !== -1 ||
              permissions.indexOf('26') !== -1
            "
            type="primary"
            @click="resetPasswordRoleInfo(scope.row)"
          >
            重置密码
          </el-button>
          <el-button
            v-show="
              permissions.indexOf('7') !== -1 ||
              permissions.indexOf('26') !== -1
            "
            type="primary"
            @click="editRoleInfo(scope.row)"
          >
            修改信息
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
      title="添加管理员"
      :visible.sync="dialogAddAdminVisible"
      width="700px"
      @close="onAddAdminClose"
    >
      <el-form
        ref="adminAddInfo"
        :model="adminAddInfo"
        :rules="adminAddInfo_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="昵称" prop="name">
          <el-input
            v-model="adminAddInfo.name"
            placeholder="请输入昵称"
            clearable
            prefix-icon="el-icon-user-solid"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="头像" prop="avatar" required>
          <el-upload
            ref="avatar"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="addAvatarFileList"
            :action="avatarAction"
            :on-success="uploadAddAvatarSuccess"
            :before-upload="avatarBeforeUpload"
            list-type="picture-card"
            name="avatar"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select
            v-model="adminAddInfo.sex"
            placeholder="请选择性别"
            clearable
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in sexOptions"
              :key="index"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="adminAddInfo.birthday"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            :style="{ width: '100%' }"
            placeholder="请选择出生日期"
            clearable
          ></el-date-picker>
        </el-form-item>
        <el-divider>
          <i class="el-icon-mobile-phone"></i>
          登录信息
        </el-divider>
        <el-row>
          <el-form-item label="登录邮件" prop="email">
            <el-input
              v-model="adminAddInfo.email"
              placeholder="请输入登录邮件"
              clearable
              prefix-icon="el-icon-lollipop"
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              v-model="adminAddInfo.password"
              placeholder="请输入密码"
              clearable
              prefix-icon="el-icon-ice-drink"
              show-password
              :style="{ width: '100%' }"
            ></el-input>
          </el-form-item>
          <el-form-item label="控制角色" prop="permissions">
            <el-select
              v-model="adminAddInfo.permissions"
              placeholder="请选择控制角色"
              clearable
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in permissionsOptions"
                :key="index"
                :label="item.label"
                :value="item.value"
                :disabled="item.disabled"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-row>
        <el-form-item size="large">
          <el-button
            type="primary"
            :loading="addFormDialog"
            @click="submitAddForm"
          >
            提交
          </el-button>
          <el-button @click="onAddAdminClose">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="密码重置"
      :visible.sync="resetPasswordVisible"
      width="700px"
      @close="resetPasswordClose"
    >
      <el-form
        ref="resetPassword"
        :model="resetPassword"
        :rules="resetPassword_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="resetPassword.password"
            placeholder="请输入密码"
            clearable
            prefix-icon="el-icon-s-tools"
            show-password
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item size="large">
          <el-button
            type="primary"
            :loading="resetPassowrdDis"
            @click="submitFormPassword"
          >
            提交
          </el-button>
          <el-button @click="resetPasswordClose">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      :title="editTitle"
      :visible.sync="dialogFormVisible"
      width="700px"
      @close="onEditAdminClose"
    >
      <el-form
        ref="editAdmininfo"
        :model="editAdmininfo"
        :rules="editAdmininfo_rules"
        size="small"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="用户名" prop="name">
          <el-input
            v-model="editAdmininfo.name"
            placeholder="请输入用户名"
            clearable
            prefix-icon="el-icon-user-solid"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="电子邮件" prop="email">
          <el-alert
            title="此电子邮件不是登录邮件"
            type="warning"
            :closable="false"
            center
            show-icon
          ></el-alert>
          <el-input
            v-model="editAdmininfo.email"
            placeholder="请输入电子邮件"
            clearable
            prefix-icon="el-icon-phone-outline"
            :style="{ width: '100%' }"
          ></el-input>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker
            v-model="editAdmininfo.birthday"
            :style="{ width: '100%' }"
            placeholder="请选择出生日期"
            :picker-options="pickerOptions"
            clearable
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="上传" prop="avatar" required>
          <el-upload
            ref="avatar"
            :limit="1"
            :on-exceed="fileNumberGoBeyond"
            :headers="imageConfig"
            :file-list="avatarfileList"
            :action="avatarAction"
            :on-success="uploadAvatarSuccess"
            :before-upload="avatarBeforeUpload"
            list-type="picture-card"
            accept="image/*"
            name="avatar"
          >
            <i class="el-icon-plus"></i>
            <div slot="tip" class="el-upload__tip">
              只能上传不超过 2MB 的image/*文件
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="角色" prop="permissions">
          <el-select
            v-model="editAdmininfo.permissions"
            placeholder="请选择角色"
            clearable
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in permissionsOptions"
              :key="index"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select
            v-model="editAdmininfo.sex"
            placeholder="请选择性别"
            clearable
            :style="{ width: '100%' }"
          >
            <el-option
              v-for="(item, index) in sexOptions"
              :key="index"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item size="large">
          <el-button
            type="primary"
            :loading="editFormDialog"
            @click="submitForm"
          >
            提交
          </el-button>
          <el-button @click="onEditAdminClose">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script src="../admin/js/adminManager.js"></script>

<style scoped></style>
