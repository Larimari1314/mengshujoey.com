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
    path: '/acknowledgement',
    component: () => import('@/views/acknowledgement/index'),
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
    path: '/project',
    name: 'project',
    component: Layout,
    redirect: '/project',
    children: [
      {
        path: 'projectInformation/:id',
        name: 'project',
        component: () => import('@/views/projectInformation/index'),
        meta: {
          title: '项目',
          icon: 'air-freshener',
          affix: true,
        },
      },
    ],
  },
  {
    path: '/queryData',
    name: 'queryData',
    component: Layout,
    redirect: '/queryData',
    children: [
      {
        path: 'queryDataInformation',
        name: 'queryDataInformation',
        component: () => import('@/views/queryResult/index'),
        meta: {
          title: '查询结果',
          icon: 'air-freshener',
          affix: true,
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
