<template xmlns:http="http://www.w3.org/1999/xhtml">
  <div v-if="routerView" class="app-main-container">
    <transition mode="out-in" name="fade-transform">
      <keep-alive :include="cachedRoutes" :max="keepAliveMaxNum">
        <router-view :key="key" class="app-main-height" />
      </keep-alive>
    </transition>
    <footer v-show="footerCopyright" class="footer-copyright">
      <div>
        <a href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=2022011437" class="footer-copyright" target="_blank">
          <img src="https://pic3.zhimg.com/80/v2-d0289dc0a46fc5b15b3363ffa78cf6c7.png" />
          辽ICP备2022011437号-2
        </a>
      </div>
      @mengshujoey
        <vab-icon :icon="['fas', 'copyright']"></vab-icon>
        {{ fullYear }}
      <i class="el-icon-right" @click="creditsList">{{ $t('licence.author') }}</i>
    </footer>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import { copyright, footerCopyright, keepAliveMaxNum, title } from "@/config";

export default {
  name: "VabAppMain",
  data() {
    return {
      show: false,
      fullYear: new Date().getFullYear(),
      copyright,
      title,
      keepAliveMaxNum,
      routerView: true,
      footerCopyright
    };
  },
  computed: {
    ...mapGetters({
      visitedRoutes: "tabsBar/visitedRoutes",
      device: "settings/device"
    }),
    cachedRoutes() {
      const cachedRoutesArr = [];
      this.visitedRoutes.forEach((item) => {
        if (!item.meta.noKeepAlive) {
          cachedRoutesArr.push(item.name);
        }
      });
      return cachedRoutesArr;
    },
    key() {
      return this.$route.path;
    }
  },
  watch: {
    $route: {
      handler(route) {
        if ("mobile" === this.device) this.foldSideBar();
      },
      immediate: true
    }
  },
  created() {
    //重载所有路由
    this.$baseEventBus.$on("reload-router-view", () => {
      this.routerView = false;
      this.$nextTick(() => {
        this.routerView = true;
      });
    });
  },
  mounted() {
  },
  methods: {
    creditsList(){
      this.$router
        .push({
          path: '/acknowledgement',
          query: {},
        })
        .catch(() => {})
    },
    ...mapActions({
      foldSideBar: "settings/foldSideBar"
    })
  }
};
</script>

<style lang="scss" scoped>
.app-main-container {
  position: relative;
  width: 100%;
  overflow: hidden;

  .vab-keel {
    margin: $base-padding;
  }

  .app-main-height {
    min-height: $base-app-main-height;
  }

  .footer-copyright {
    min-height: 55px;
    line-height: 55px;
    color: rgba(0, 0, 0, 0.45);
    text-align: center;
    border-top: 1px dashed $base-border-color;
  }
}
</style>
