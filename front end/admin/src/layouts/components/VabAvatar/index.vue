<template>
  <el-dropdown @command="handleCommand">
    <span class="avatar-dropdown">
      <!--<el-avatar class="user-avatar" :src="avatar"></el-avatar>-->
      <img class="user-avatar" :src="avatar" alt="" />
      <div class="user-name">
        {{ username }}
        <i class="el-icon-arrow-down el-icon--right"></i>
      </div>
    </span>
    <el-dialog
      :title="editAdmininfo"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="onEditAdminInfoClose"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane label="基本信息修改" name="first">
          <el-form ref="adminInfo" :model="adminInfo" :rules="adminInfo_rules" size="small" label-width="80px"
                   label-position="left">
            <el-form-item label="用户名称" prop="adminName">
              <el-input v-model="adminInfo.adminName" placeholder="请输入用户名称" :maxlength="10" show-word-limit
                        clearable prefix-icon="el-icon-user" :style="{width: '100%'}"></el-input>
            </el-form-item>
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker v-model="adminInfo.birthday"
                              :style="{width: '100%'}" placeholder="请选择出生日期" clearable></el-date-picker>
            </el-form-item>
            <el-form-item label="性别" prop="admin_sex">
              <el-select v-model="adminInfo.admin_sex" placeholder="请选择性别" clearable :style="{width: '100%'}">
                <el-option v-for="(item, index) in admin_sexOptions" :key="index" :label="item.label"
                           :value="item.value" :disabled="item.disabled"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="上传" prop="adminAvatar" required>
              <el-upload ref="avatar"
                         :limit="1"
                         :on-exceed="fileNumberGoBeyond"
                         :headers="imageConfig"
                         :file-list="adminAvatarfileList"
                         :action="adminAvatarAction"
                         :before-upload="adminAvatarBeforeUpload"
                         :on-success="successAdminAvatar"
                         list-type="picture-card"
                         accept="image/*"
                         name="avatar">
                <i class="el-icon-plus"></i>
                <div slot="tip" class="el-upload__tip">只能上传不超过 2MB 的image/*文件</div>
              </el-upload>
            </el-form-item>
            <el-form-item size="large">
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="onEditAdminInfoClose">取消</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="登录信息修改" name="second">
          <el-form ref="adminsafe" :model="adminsafe" :rules="adminsafe_rules" size="small" label-width="80px"
                   label-position="left">
            <el-form-item label="登陆邮件" prop="loginEmail">
              <el-input v-model="adminsafe.loginEmail" placeholder="请输入登陆邮件" clearable
                        prefix-icon="el-icon-phone-outline" :style="{width: '100%'}"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input v-model="adminsafe.password" placeholder="请输入密码" clearable prefix-icon="el-icon-warning"
                        show-password :style="{width: '100%'}"></el-input>
            </el-form-item>
            <el-form-item size="large">
              <el-button type="primary" @click="submitFormSafe">提交</el-button>
              <el-button @click="onEditAdminInfoClose">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item command="editAdminInformation" divided>修改个人信息</el-dropdown-item>
      <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { mapGetters } from "vuex";
import { recordRoute } from "@/config";
import { decrypto, encryptedData } from "@/utils/encrypt";
import { getSexList } from "@/api/dropDownOption";
import { backendUrl } from "@/config/setting.config";
import { editIndividualAdminInformation, modifyLoginInformation } from "@/api/adminIndividualManager";

export default {
  name: "VabAvatar",
  computed: {
    ...mapGetters({
      avatar: "user/avatar",
      username: "user/username"
    })
  },
  data() {
    return {
      adminsafe: {
        loginEmail: undefined,
        password: undefined
      },
      adminsafe_rules: {
        loginEmail: [{
          required: true,
          message: "请输入登陆邮件",
          trigger: "blur"
        }, {
          pattern: /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
          message: "邮件格式错误",
          trigger: "blur"
        }],
        password: [{
          required: true,
          message: "请输入密码",
          trigger: "blur"
        }, {
          pattern: /^[a-zA-Z0-9_-]{6,18}$/,
          message: "请按要求输入6-18位由字母、数字、下划线构成的密码",
          trigger: "blur"
        }]
      },
      activeName: "first",
      imageConfig: { token: sessionStorage.getItem("accessTokens") },
      dialogFormVisible: false,
      editAdmininfo: "修改个人信息",
      adminInfo: {
        adminName: undefined,
        birthday: null,
        admin_sex: undefined,
        adminAvatar: null
      },
      configs: {
        headers: {
          token: sessionStorage.getItem("accessTokens")
        }
      },
      adminInfo_rules: {
        adminName: [{
          required: true,
          message: "请输入用户名称",
          trigger: "blur"
        }],
        birthday: [{
          required: true,
          message: "请选择出生日期",
          trigger: "change"
        }],
        admin_sex: [{
          required: true,
          message: "请选择性别",
          trigger: "change"
        }]
      },
      adminAvatarAction: backendUrl + "adminManager/uploadAvatarAddress",
      adminAvatarfileList: [],
      admin_sexOptions: []
    };
  },
  methods: {
    submitFormSafe() {
      this.$refs["adminsafe"].validate(valid => {
        if (!valid) return;
        // TODO 提交表单
        var publicKeys = JSON.parse(decrypto(localStorage.getItem("mengshuPubey"), 520, 21));
        var forms = {
          LoginCredentials: encryptedData(publicKeys["publickKey"], {
            loginEmail: this.adminsafe.loginEmail,
            password: this.adminsafe.password,
            id: this.adminInfo.id
          })
        };
        modifyLoginInformation(forms, this.configs).then((res) => {
          if (res.data.code === "200") {
            //数据修改成功，退出登录
            this.$store.dispatch("user/logout");
            if (recordRoute) {
              const fullPath = this.$route.fullPath;
              this.$router.push(`/login?redirect=${fullPath}`);
            } else {
              this.$router.push("/login");
            }
          } else if (res.data.code === "304") {
            this.$message({
              type: "error",
              message: "当前登陆邮件已存在！"
            });
          }
        });
      });
    },
    successAdminAvatar(response, file, fileList) {
      this.adminInfo.adminAvatar = response;
    },
    fileNumberGoBeyond(files, fileList) {
      this.$message.warning(
        `当前限制选择 1 个文件，本次选择了 ${files.length} 个文件`
      );
    },
    submitForm() {
      this.$refs["adminInfo"].validate(valid => {
        if (!valid) return;
        // TODO 提交表单
        editIndividualAdminInformation(this.adminInfo, this.configs).then((res) => {
          if (res.data.code === "200") {
            this.$message({
              type: "success",
              message: "修改个人信息成功，下次登录生效!"
            });
            this.onEditAdminInfoClose();
          }
        });
      });
    },
    adminAvatarBeforeUpload(file) {
      let isRightSize = file.size / 1024 / 1024 < 2;
      if (!isRightSize) {
        this.$message.error("文件大小超过 2MB");
      }
      let isAccept = new RegExp("image/*").test(file.type);
      if (!isAccept) {
        this.$message.error("应该选择image/*类型的文件");
      }
      return isRightSize && isAccept;
    },
    onEditAdminInfoClose() {
      this.dialogFormVisible = false;
      this.adminInfo = {};
      this.$refs.avatar.clearFiles();
    },
    editAdminInformation() {
      let userInformation = decrypto(sessionStorage.getItem("userInformation"), 125, 20);
      this.dialogFormVisible = true;
      this.adminInfo = JSON.parse(userInformation);
      this.adminAvatarfileList = [{ url: this.adminInfo.adminAvatar }];
      getSexList(this.configs).then((res) => {
        if (res.data.code === "200") {
          this.admin_sexOptions = res.data.data;
        }
      });
    },
    handleCommand(command) {
      switch (command) {
        case "logout":
          this.logout();
          break;
        case "personalCenter":
          this.personalCenter();
          break;
        case "editAdminInformation":
          this.editAdminInformation();
          break;
      }
    },
    personalCenter() {
      this.$router.push("/personalCenter/personalCenter");
    },
    logout() {
      this.$baseConfirm(
        "您确定要退出" + this.$baseTitle + "吗?",
        null,
        async () => {
          await this.$store.dispatch("user/logout");
          if (recordRoute) {
            const fullPath = this.$route.fullPath;
            this.$router.push(`/login?redirect=${fullPath}`);
          } else {
            this.$router.push("/login");
          }
        }
      );
    }
  }
};
</script>
<style lang="scss" scoped>
.avatar-dropdown {
  display: flex;
  align-content: center;
  align-items: center;
  justify-content: center;
  justify-items: center;
  height: 50px;
  padding: 0;

  .user-avatar {
    width: 40px;
    height: 40px;
    cursor: pointer;
    border-radius: 50%;
  }

  .user-name {
    position: relative;
    margin-left: 5px;
    margin-left: 5px;
    cursor: pointer;
  }
}
</style>
