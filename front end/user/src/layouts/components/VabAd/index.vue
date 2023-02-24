<template>
</template>
<script>
  export default {
    name: 'VabAd',
    data() {
      return {
        nodeEnv: process.env.NODE_ENV,
        adList: [],
      }
    },
    created() {
    },
    methods: {
    },
  }
</script>
<style lang="scss" scoped>
  .vab-ad {
    height: 30px;
    padding-right: $base-padding;
    padding-left: $base-padding;
    margin-bottom: -20px;
    line-height: 30px;
    cursor: pointer;

    a {
      color: #999;
    }
  }
</style>
