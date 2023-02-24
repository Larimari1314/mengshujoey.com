/**
 * @description router全局配置，如有必要可分文件抽离，其中asyncRoutes只有在intelligence模式下才会用到
 */

import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layouts'
import EmptyLayout from '@/layouts/EmptyLayout'
import { publicPath, routerMode } from '@/config'

Vue.use(VueRouter)
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true,
  },
  {
    path: '/401',
    name: '401',
    component: () => import('@/views/401'),
    hidden: true,
  },
  {
    path: '/404',
    name: '404',
    component: () => import('@/views/404'),
    hidden: true,
  },
]

export const asyncRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: 'index',
        name: 'Index',
        component: () => import('@/views/index/index'),
        meta: {
          title: '首页',
          icon: 'home',
          affix: true,
        },
      },
    ],
  },
  {
    path: '/assessment',
    name: 'assessmentItemsManager',
    component: Layout,
    redirect: '/assessmentItemsManager',
    children: [
      {
        path: 'assessmentItemsManager',
        name: 'assessmentItemsManager',
        component: () => import('@/views/assessmentItemsManager/index.vue'),
        meta: {
          title: '测评项目管理',
          icon: 'air-freshener',
          affix: true,
          permissions: ['3', '11', '12', '13', '14'],
        },
      },
    ],
  },
  {
    path: '/basic',
    component: Layout,
    redirect: 'noRedirect',
    name: 'Vab',
    alwaysShow: true,
    meta: {
      title: '基本信息维护',
      icon: 'database',
      permissions: ['5', '15', '16', '17', '18', '6', '19', '20', '21', '22'],
    },
    children: [
      {
        path: 'label',
        name: 'Label',
        component: () => import('@/views/basicInformation/label/index'),
        meta: {
          title: '标签管理',
          permissions: ['5', '15', '16', '17', '18'],
        },
      },
      {
        path: 'studio',
        name: 'studio',
        component: () => import('@/views/basicInformation/studio/index'),
        meta: {
          title: '工作室成员管理',
          permissions: ['6', '19', '20', '21', '22'],
        },
      },
    ],
  },
  {
    path: '/safe',
    component: Layout,
    redirect: 'noRedirect',
    name: 'safe',
    alwaysShow: true,
    meta: {
      title: '系统安全设置',
      icon: 'user-secret',
      permissions: ['2', '7', '23', '24', '25', '26'],
    },
    children: [
      {
        path: 'roleManager',
        name: 'roleManager',
        component: () => import('@/views/safe/role/index'),
        meta: {
          title: '角色管理',
          permissions: ['2'],
        },
      },
      {
        path: 'adminManager',
        name: 'adminManager',
        component: () => import('@/views/safe/admin/index'),
        meta: {
          title: '管理员管理',
          permissions: ['7', '23', '24', '25', '26'],
        },
      },
    ],
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true,
  },
]

const router = new VueRouter({
  base: publicPath,
  mode: routerMode,
  scrollBehavior: () => ({
    y: 0,
  }),
  routes: constantRoutes,
})

export function resetRouter() {
  location.reload()
}

export default router
