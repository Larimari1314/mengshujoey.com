<template>
  <div class="vab-top-search-box">
    <input @keyup.enter="queryData" class="search-txt" type="text" name="" v-model="text"
           :placeholder="$t('licence.searchBoxMain')">
    <a class="search-btn">
      <i class="el-icon-search" @click="queryData"></i>
    </a>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      text: undefined
    };
  },
  methods: {
    queryData() {
      //将中文转成url格式
      if (this.text !== null && this.text !== undefined && !this.text.match(/^\s+$/)&&this.text!==''){
        let textUrl = encodeURIComponent(this.text);
        this.text = "";
        //检测房前是否有标签数据
        this.$router.push({
          path: "/queryData/queryDataInformation",
          query: {
            levelId: this.$route.query.levelId,
            levelValue: this.$route.query.levelValue,
            text: textUrl
          }
        }).catch(() => {
        });
        if (this.$route.path === "/queryData/queryDataInformation") {
          location.reload();
        }
      } else {
        this.$message.info("请输入数据搜索哦");
      }
    }
  }
};
</script>

<style scoped>
.vab-top-search-box {
  position: absolute;
  right: 14%;
  /*transform: translate(-35%, -50%);*/
  /*transform: translateX(-224px);*/
  background: #ffffff;
  height: 20px;
  border-radius: 8px;
  padding: 10px;
}

.vab-top-search-box:hover > .search-txt {
  width: 250px;
  padding: 0 6px;
}

.vab-top-search-box:hover > .search-btn {
  background: #92afc4;
}

.search-btn {
  color: #fff;
  float: right;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #86bdff;
  display: flex;
  justify-content: center;
  text-decoration: none;
  align-items: center;
  transition: 0.4s;
}

.search-txt {
  border: none;
  background: none;
  outline: none;
  float: left;
  padding: 0;
  color: #000000;
  font-size: 10px;
  font-family: "Poppins", sans-serif;
  transition: 0.4s;
  line-height: 20px;
  width: 0px;
}
</style>
