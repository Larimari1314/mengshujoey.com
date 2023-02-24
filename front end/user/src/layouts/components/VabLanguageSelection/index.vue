<template>
  <div class="selection-box-language">
    <el-dropdown size="mini">
      <el-button round>
      <i class="el-icon-location-outline"></i>
      {{ nowLanguage }}</el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          v-for="(item,index) in languagsList"
          :key="index+'language'"
          @click.native="editLanguage(item)">{{ item.label }}</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      nowLanguage: "zh",
      languagsList: [
        {
          label: "简体中文",
          value: "zh"
        },
        {
          label: "繁體中文",
          value: "zhc"
        },
        {
          label: "English",
          value: "en"
        },
        {
          label: "Português",
          value: "portugal"
        }
      ]
    };
  },
  created() {
    let nowLan = undefined;
    if (localStorage.getItem("lang")) {
      nowLan = localStorage.getItem("lang");
    }
    if (nowLan !== undefined && nowLan !== '') {
      this.languagsList.forEach((item) => {
        if (nowLan === item.value) {
          this.nowLanguage = item.label;
        }
      });
    } else {
      this.nowLanguage = "简体中文";
    }
  }, methods: {
    editLanguage(data) {
      if(data.label!==this.nowLanguage){
        localStorage.setItem("lang", data.value);
        location.reload();
      }
    }
  }
};
</script>

<style scoped>
.selection-box-language {
  position: absolute;
  right: 5%;
  margin-top: 20px;
  height: 50px;
  border-radius: 8px;
  padding: 10px;
}
</style>
