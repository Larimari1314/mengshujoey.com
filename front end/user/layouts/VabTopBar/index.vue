<template>
  <div class="top-bar-container">
    <div class="vab-main">
      <el-row>
        <el-col>
          <div class="right-panel-top">
            <vab-language-selection/>
            <vab-top-searchBox/>
            <vab-full-screen-bar @refresh="refreshRoute" />
            <vab-theme-bar class="hidden-md-and-down" />
            <vab-icon
              title="重载路由"
              :pulse="pulse"
              :icon="['fas', 'redo']"
              @click="refreshRoute"
            />
            <vab-avatar />
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import variables from "@/styles/variables.scss";
import { mapGetters } from "vuex";
import vabTopSearchBox from "../../src/layouts/components/VabTopSearchBox";
import vabLanguageSelection from "../../src/layouts/components/VabLanguageSelection";
export default {
  name: "VabTopBar",
  data() {
    return {
      pulse: false,
      menuTrigger: "hover"
    };
  },
  components:{
    vabLanguageSelection,
    vabTopSearchBox
  },
  computed: {
    ...mapGetters({
      layout: "settings/layout",
      tabsBar: "settings/tabsBar",
      collapse: "settings/collapse",
      header: "settings/header",
      device: "settings/device",
      routes: "routes/routes",
      visitedRoutes: "tabsBar/visitedRoutes"
    }),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    variables() {
      return variables;
    }
  },
  methods: {
    async refreshRoute() {
      this.$baseEventBus.$emit("reload-router-view");
      this.pulse = true;
      setTimeout(() => {
        this.pulse = false;
      }, 1000);
    }
  }
};
</script>
<style lang="scss" scoped>
.top-bar-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-items: flex-end;
  height: $base-top-bar-height;
  background: $base-menu-background;

  .vab-main-top {
    width: 100%;
    background: $base-menu-background;

    ::v-deep {
      .el-menu {
        &.el-menu--horizontal {
          display: flex;
          align-items: center;
          justify-content: flex-end;
          height: $base-top-bar-height;
          border-bottom: 0 solid transparent !important;

          .el-menu-item,
          .el-submenu__title {
            padding: 0 15px;
          }

          @media only screen and (max-width: 767px) {
            .el-menu-item,
            .el-submenu__title {
              padding: 0 8px;
            }

            li:nth-child(4),
            li:nth-child(5) {
              display: none !important;
            }
          }

          > .el-menu-item {
            height: $base-top-bar-height;
            line-height: $base-top-bar-height;
          }

          > .el-submenu {
            .el-submenu__title {
              height: $base-top-bar-height;
              line-height: $base-top-bar-height;
            }
          }
        }

        svg {
          width: 1rem;
          margin-right: 3px;
        }

        &--horizontal {
          .el-menu {
            .el-menu-item,
            .el-submenu__title {
              height: $base-menu-item-height;
              line-height: $base-menu-item-height;
            }
          }

          .el-submenu,
          .el-menu-item {
            &.is-active {
              background-color: $base-color-blue !important;
              border-bottom: 0 solid transparent !important;

              .el-submenu__title {
                border-bottom: 0 solid transparent !important;
              }
            }
          }

          > .el-menu-item {
            .el-tag {
              margin-top: calc(#{$base-top-bar-height} / 2 - 7.5px);
              margin-left: 5px;
            }

            @media only screen and (max-width: 1199px) {
              .el-tag {
                display: none;
              }
            }

            &.is-active {
              background-color: transparent !important;
              border-bottom: 3px solid $base-color-blue !important;
            }
          }
        }
      }
    }
  }

  .right-panel-top {
    align-content: center;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    height: $base-top-bar-height;

    ::v-deep {
      .user-name {
        color: rgba($base-color-white, 0.9);
      }

      .user-name + i {
        color: rgba($base-color-white, 0.9);
      }

      svg {
        width: 1em;
        height: 1em;
        margin-right: 15px;
        font-size: $base-font-size-big;
        color: rgba($base-color-white, 0.9);
        cursor: pointer;
        fill: rgba($base-color-white, 0.9);
      }

      button {
        svg {
          margin-right: 0;
          color: rgba($base-color-white, 0.9);
          cursor: pointer;
          fill: rgba($base-color-white, 0.9);
        }
      }

      .el-badge {
        margin-right: 15px;
      }
    }
  }
}
</style>
