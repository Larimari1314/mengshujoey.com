<template>
  <div class="text-acknowledge">
    <div style="font-size: 80px">{{ $t('licence.mingxie') }}</div>
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
      <el-link style="color: white" target="_blank">阿奇</el-link>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'PcAcknowledgement',
  }
</script>

<style scoped>
  .text-acknowledge {
    font-family: 楷书, serif;
    font-size: 20px;
    padding: 20px;
    color: white;
    text-align: center;
  }
</style>
