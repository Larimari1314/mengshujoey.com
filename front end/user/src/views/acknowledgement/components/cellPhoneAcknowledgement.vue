<template>
  <div class="text-acknowledge">
    <div style="font-size: 50px">{{ $t('licence.mingxie') }}</div>
    <div style="padding-top: 20px">
      {{ $t('licence.genju') }}
      <el-link
        style="color: white"
        href="https://github.com/chuzhixin/vue-admin-better"
        target="_blank"
      >
        vue-admin-better
      </el-link>
      {{ $t('licence.dingfzhi') }}
    </div>
    <div style="padding-top: 20px">
      {{ $t('licence.houduanyijiqianduan') }}：
      <el-link
        style="color: white"
        href="https://github.com/Larimari1314"
        target="_blank"
      >
        {{ $t('licence.Author') }}
      </el-link>
    </div>
    <div style="padding-top: 20px">
      {{ $t('licence.xitozotisj') }}：
      <el-link
        style="color: white"
        href="https://github.com/Larimari1314"
        target="_blank"
      >
        {{ $t('licence.Author') }}
      </el-link>
    </div>
    <div style="padding-top: 20px">
      {{ $t('licence.houduanbianxie') }}：
      <el-link
        style="color: white"
        href="https://github.com/Larimari1314"
        target="_blank"
      >
        {{ $t('licence.Author') }}
      </el-link>
    </div>
    <div style="padding-top: 20px">
      {{ $t('licence.qianduanbianxie') }}：
      <el-link
        style="color: white"
        href="https://blog.csdn.net/weixin_45677046"
        target="_blank"
      >
        {{ $t('licence.Author') }}
      </el-link>
    </div>
    <div style="padding-top: 20px">
      {{ $t('licence.yemiansheji') }} ：
      <el-link
        style="color: white"
        href="https://blog.csdn.net/weixin_45677046"
        target="_blank"
      >
        {{ $t('licence.Author') }}
      </el-link>
      、
      <el-link style="color: white" target="_blank">阿齐</el-link>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'CellPhoneAcknowledgement',
  }
</script>

<style scoped>
  .text-acknowledge {
    font-family: 楷体, serif;
    color: white;
    font-size: 20px;
    text-align: center;
  }
</style>
