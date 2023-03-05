<template>
  <!--  <div class="login-container">
    <el-row>
      <el-col :xs="24" :sm="24" :md="12" :lg="16" :xl="16">
        <div style="color: transparent">占位符</div>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8" :xl="8">
        <el-form
          ref="form"
          :model="form"
          :rules="rules"
          class="login-form"
          label-position="left"
        >
          <div class="title">您好 !</div>
          <div class="title-tips">欢迎来到{{ title }}！</div>
          <el-form-item style="margin-top: 40px" prop="username">
            <span class="svg-container svg-container-admin">
              <vab-icon :icon="['fas', 'user']" />
            </span>
            <el-input
              v-model.trim="form.username"
              v-focus
              placeholder="请输入用户名"
              tabindex="1"
              type="text"
            />
          </el-form-item>
          <el-form-item prop="password">
            <span class="svg-container">
              <vab-icon :icon="['fas', 'lock']" />
            </span>
            <el-input
              :key="passwordType"
              ref="password"
              v-model.trim="form.password"
              :type="passwordType"
              tabindex="2"
              placeholder="请输入密码"
              @keyup.enter.native="handleLogin"
            />
            <span
              v-if="passwordType === 'password'"
              class="show-password"
              @click="handlePassword"
            >
              <vab-icon :icon="['fas', 'eye-slash']"></vab-icon>
            </span>
            <span v-else class="show-password" @click="handlePassword">
              <vab-icon :icon="['fas', 'eye']"></vab-icon>
            </span>
          </el-form-item>
          <el-button
            :loading="loading"
            class="login-btn"
            type="primary"
            @click="verify"
          >
            登录
          </el-button>
        </el-form>
        <div class="dialog">
          <el-dialog
            :visible.sync="dialogVisible"
            width="300px"
            center
            :show-close="false"
            :before-close="handleClose"
          >
            <div class="previewDialog">
              <drag-verify-img-rotate
                ref="dragVerify"
                :is-passing.sync="isPassing"
                imgsrc="https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/mengshujoey.jpg"
                :show-tips="true"
                text="请按住滑块拖动"
                :show-refresh="true"
                success-text="验证通过"
                handler-icon="el-icon-d-arrow-right"
                success-icon="el-icon-circle-check"
                @refresh="reimg"
                @passcallback="onSuccess"
              ></drag-verify-img-rotate>
            </div>
          </el-dialog>
        </div>
      </el-col>
    </el-row>
  </div>-->
  <div class="login_parent">
    <div class="content">
      <div class="left">
        <img
          src="../../assets/login_images/img2.png"
          class="people p-animtion"
          alt="people"
        />
        <img
          src="../../assets/login_images/img1.png"
          class="sphere s-animtion"
          alt="sphere"
        />
      </div>
      <div class="right">
        <div class="form-wrappepr">
          <h1>欢迎登录mengshujory管理端</h1>
          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            class="login-form"
            label-position="left"
          >
            <el-form-item prop="username">
              <input
                v-model.trim="form.username"
                v-focus
                placeholder="请输入用户名"
                type="text"
                class="inputs user"
              />
            </el-form-item>
            <el-form-item prop="password">
              <input
                ref="password"
                v-model.trim="form.password"
                :type="passwordType"
                tabindex="2"
                placeholder="请输入密码"
                class="inputs pwd"
                @keyup.enter="handleLogin"
              />
            </el-form-item>
          </el-form>
          <span class="tips">遗忘密码请找管理员</span>
          <button @click="verify">登录</button>
          <div class="other-login">
            <div class="divider">
              <span class="line"></span>
              <span class="divider-text">mengshujoey管理端</span>
              <span class="line"></span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="dialog">
      <el-dialog
        :visible.sync="dialogVisible"
        width="300px"
        center
        :show-close="false"
        :before-close="handleClose"
      >
        <div class="previewDialog">
          <drag-verify-img-rotate
            ref="dragVerify"
            :is-passing.sync="isPassing"
            imgsrc="https://java-ljw.obs.cn-north-4.myhuaweicloud.com:443/mengshujoey/Guide%20pages/mengshujoey.jpg"
            :show-tips="true"
            text="请按住滑块拖动"
            :show-refresh="true"
            success-text="验证通过"
            handler-icon="el-icon-d-arrow-right"
            success-icon="el-icon-circle-check"
            @refresh="reimg"
            @passcallback="onSuccess"
          ></drag-verify-img-rotate>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script src="../login/js/login.js" />
<style scoped>
  :root {
    font-size: 15px;
  }

  .login_parent {
    margin: 0;
    min-height: 100vh;
    background-color: #abc6f8;
    background-image: radial-gradient(
        closest-side,
        rgb(255, 255, 255),
        rgba(235, 105, 78, 0)
      ),
      radial-gradient(closest-side, rgb(250, 203, 203), rgba(243, 11, 164, 0)),
      radial-gradient(closest-side, rgb(237, 252, 202), rgba(254, 234, 131, 0)),
      radial-gradient(closest-side, rgb(197, 248, 241), rgba(170, 142, 245, 0)),
      radial-gradient(closest-side, rgb(206, 200, 243), rgba(248, 192, 147, 0));
    background-size: 130vmax 130vmax, 80vmax 80vmax, 90vmax 90vmax,
      110vmax 110vmax, 90vmax 90vmax;
    background-position: -80vmax -80vmax, 60vmax -30vmax, 10vmax 10vmax,
      -30vmax -10vmax, 50vmax 50vmax;
    background-repeat: no-repeat;
    animation: 10s movement linear infinite;
  }

  .login_parent::after {
    content: '';
    display: block;
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
  }

  .content {
    width: 90vw;
    height: 90vh;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    z-index: 1;
    border-radius: 30px;
    background: rgba(255, 255, 255, 0.6);
    border: 1px solid rgba(255, 255, 255, 0.18);
    display: flex;
  }
  .content .left {
    flex: 1;
    position: relative;
  }
  .content .left .sphere {
    position: absolute;
    left: 30%;
    width: 90%;
    z-index: 1;
    animation: sphereAnimation 2s;
    animation-fill-mode: forwards;
    animation-timing-function: ease;
  }
  .content .left .people {
    position: absolute;
    left: -50%;
    top: 20%;
    width: 70%;
    z-index: 2;
  }
  .content .left .p-animtion {
    animation: peopleAnimation 2s;
    animation-fill-mode: forwards;
    animation-timing-function: ease;
  }
  .content .left .p-other-animtion {
    animation-name: pOtherAnimation;
    animation-direction: alternate;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
    animation-duration: 3s;
  }
  .content .left .s-animtion {
    animation: sphereAnimation 2s;
    animation-fill-mode: forwards;
    animation-timing-function: ease;
  }
  .content .left .s-other-animtion {
    animation-name: sOtherAnimation;
    animation-direction: alternate;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
    animation-duration: 3s;
  }
  .content .right {
    flex: 1;
    position: relative;
    z-index: 12;
  }
  .content .right .top {
    width: 80%;
    margin-left: 38px;
    color: rgb(51, 52, 124);
    font-size: 20px;
    font-weight: 600;
    font-family: 'Century Gothic', Times, serif;
    position: absolute;
    left: 50%;
    top: 5%;
    transform: translate(-50%, 0);
  }
  .content .right .top .top-item {
    float: left;
    width: 150px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    margin-right: 10px;
    transition: 0.5s;
  }
  .content .right .top .top-item:hover {
    border: 0;
    background-color: #fff;
    border-radius: 50px;
    cursor: pointer;
    box-shadow: -20px 10px 32px 1px rgba(182, 183, 185, 0.37);
  }
  .content .right .form-wrappepr {
    width: 60%;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    text-align: right;
  }
  .content .right .form-wrappepr h1 {
    float: left;
    font-family: 'Century Gothic', Times, serif;
    margin: 30px 0;
  }
  .content .right .form-wrappepr .inputs {
    display: block;
    width: 100%;
    /*height: 70px;*/
    margin: 10px 0;
    border-radius: 10px;
    border: 0;
    background-color: rgb(210, 223, 237);
    color: rgb(80, 82, 84);
    font-family: 'Century Gothic', Times, serif;
    outline: none;
    padding: 20px;
    box-sizing: border-box;
    font-size: 20px;
  }
  .content .right .form-wrappepr .tips {
    display: block;
    margin-top: -15px;
    color: rgb(160, 170, 182);
    cursor: pointer;
  }
  .content .right .form-wrappepr button {
    width: 100%;
    height: 50px;
    background-color: rgb(68, 96, 241);
    border-radius: 10px;
    font-size: 15px;
    color: #fff;
    border: 0;
    font-weight: 600;
    margin: 30px 0;
    cursor: pointer;
    box-shadow: -20px 28px 42px 0 rgba(62, 145, 255, 0.37);
    font-family: 'Century Gothic', Times, serif;
  }
  .content .right .form-wrappepr .other-login .divider {
    width: 100%;
    margin: 20px 0;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .content .right .form-wrappepr .other-login .divider .line {
    display: inline-block;
    max-width: 35%;
    width: 35%;
    flex: 1;
    height: 1px;
    background-color: rgb(162, 172, 185);
  }
  .content .right .form-wrappepr .other-login .divider .divider-text {
    vertical-align: middle;
    margin: 0px 20px;
    display: inline-block;
    width: 150px;
    color: rgb(162, 172, 185);
    white-space: normal;
  }
  .content .right .form-wrappepr .other-login .other-login-wrapper {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .content
    .right
    .form-wrappepr
    .other-login
    .other-login-wrapper
    .other-login-item {
    width: 70px;
    padding: 10px;
    text-align: center;
    border-radius: 10px;
    cursor: pointer;
    font-weight: 600;
    color: rgb(51, 49, 116);
    margin: 0 10px;
    transition: 0.4s;
  }
  .content
    .right
    .form-wrappepr
    .other-login
    .other-login-wrapper
    .other-login-item
    img {
    width: 40px;
    height: 40px;
    vertical-align: middle;
  }
  .content
    .right
    .form-wrappepr
    .other-login
    .other-login-wrapper
    .other-login-item
    span {
    vertical-align: middle;
  }
  .content
    .right
    .form-wrappepr
    .other-login
    .other-login-wrapper
    .other-login-item:hover {
    width: 80px;
    height: 50%;
    background-color: #fff;
    border: 0;
    box-shadow: -20px 10px 32px 1px rgba(182, 183, 185, 0.37);
  }

  @keyframes movement {
    0%,
    100% {
      background-size: 130vmax 130vmax, 80vmax 80vmax, 90vmax 90vmax,
        110vmax 110vmax, 90vmax 90vmax;
      background-position: -80vmax -80vmax, 60vmax -30vmax, 10vmax 10vmax,
        -30vmax -10vmax, 50vmax 50vmax;
    }
    25% {
      background-size: 100vmax 100vmax, 90vmax 90vmax, 100vmax 100vmax,
        90vmax 90vmax, 60vmax 60vmax;
      background-position: -60vmax -90vmax, 50vmax -40vmax, 0vmax -20vmax,
        -40vmax -20vmax, 40vmax 60vmax;
    }
    50% {
      background-size: 80vmax 80vmax, 110vmax 110vmax, 80vmax 80vmax,
        60vmax 60vmax, 80vmax 80vmax;
      background-position: -50vmax -70vmax, 40vmax -30vmax, 10vmax 0vmax,
        20vmax 10vmax, 30vmax 70vmax;
    }
    75% {
      background-size: 90vmax 90vmax, 90vmax 90vmax, 100vmax 100vmax,
        90vmax 90vmax, 70vmax 70vmax;
      background-position: -50vmax -40vmax, 50vmax -30vmax, 20vmax 0vmax,
        -10vmax 10vmax, 40vmax 60vmax;
    }
  }
  @keyframes sphereAnimation {
    0% {
      width: 10%;
    }
    100% {
      width: 90%;
      transform: translate(-30%, 5%);
    }
  }
  @keyframes peopleAnimation {
    0% {
      width: 40%;
    }
    100% {
      width: 70%;
      transform: translate(90%, -10%);
    }
  }
  @keyframes pOtherAnimation {
    0% {
      transform: translate(90%, -10%);
    }
    100% {
      transform: translate(90%, -15%);
    }
  }
  @keyframes sOtherAnimation {
    0% {
      transform: translate(-30%, 5%);
    }
    100% {
      transform: translate(-30%, 10%);
    }
  }
</style>
<style lang="scss" scoped>
  .dialog ::v-deep .el-dialog__wrapper {
    background: rgba(229, 227, 227, 0.12);
    -webkit-backdrop-filter: blur(10px);
    backdrop-filter: blur(10px);
  }

  .dialog ::v-deep .el-dialog__header {
    padding: 0;
    height: 0;
    background: rgba(229, 227, 227, 0.12);
  }

  .dialog ::v-deep .el-dialog__body {
    border-top: 1px solid #e5e3e300 !important;
    background: #e5e3e300;
  }

  .dialog ::v-deep .el-dialog {
    box-shadow: 0 0 15px 10px rgba(229, 227, 227, 0.12);
    background: rgba(229, 227, 227, 0.12);
  }

  .previewDialog ::v-deep .drag-verify-container .drag_verify {
    margin-top: 20px !important;
  }

  .login-container {
    height: 100vh;
    background: url('~@/assets/login_images/background.jpg') center center fixed
      no-repeat;
    background-size: cover;

    .title {
      font-size: 54px;
      font-weight: 500;
      color: rgba(14, 18, 26, 1);
    }

    .title-tips {
      margin-top: 29px;
      font-size: 26px;
      font-weight: 400;
      color: rgba(14, 18, 26, 1);
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .login-btn {
      display: inherit;
      width: 220px;
      height: 60px;
      margin-top: 5px;
      border: 0;

      &:hover {
        opacity: 0.9;
      }
    }

    .login-form {
      position: relative;
      max-width: 100%;
      margin: calc((100vh - 425px) / 2) 10% 10%;
      overflow: hidden;

      .forget-password {
        width: 100%;
        margin-top: 40px;
        text-align: left;

        .forget-pass {
          width: 129px;
          height: 19px;
          font-size: 20px;
          font-weight: 400;
          color: rgba(92, 102, 240, 1);
        }
      }
    }

    .tips {
      margin-bottom: 10px;
      font-size: $base-font-size-default;
      color: $base-color-white;

      span {
        &:first-of-type {
          margin-right: 16px;
        }
      }
    }

    .title-container {
      position: relative;

      .title {
        margin: 0 auto 40px auto;
        font-size: 34px;
        font-weight: bold;
        color: $base-color-blue;
        text-align: center;
      }
    }

    .svg-container {
      position: absolute;
      top: 14px;
      left: 15px;
      z-index: $base-z-index;
      font-size: 16px;
      color: #d7dee3;
      cursor: pointer;
      user-select: none;
    }

    .show-password {
      position: absolute;
      top: 14px;
      right: 25px;
      font-size: 16px;
      color: #d7dee3;
      cursor: pointer;
      user-select: none;
    }

    ::v-deep {
      .el-form-item {
        padding-right: 0;
        margin: 20px 0;
        color: #454545;
        background: transparent;
        border: 1px solid transparent;
        border-radius: 2px;

        &__content {
          min-height: $base-input-height;
          line-height: $base-input-height;
        }

        &__error {
          position: absolute;
          top: 100%;
          left: 18px;
          font-size: $base-font-size-small;
          line-height: 18px;
          color: $base-color-red;
        }
      }

      .el-input {
        box-sizing: border-box;

        input {
          height: 58px;
          padding-left: 45px;
          font-size: $base-font-size-default;
          line-height: 58px;
          color: $base-font-color;
          background: #f6f4fc;
          border: 0;
          caret-color: $base-font-color;
        }
      }
    }
  }
</style>
