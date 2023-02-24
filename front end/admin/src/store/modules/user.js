/**
 * @description 登录、获取用户信息、退出登录、清除accessToken逻辑，不建议修改
 */

import Vue from 'vue'
import {
  getAccessToken,
  removeAccessToken,
  setAccessToken,
} from '@/utils/accessToken'
import { resetRouter } from '@/router'
import { tokenTableName } from '@/config/setting.config'
import { decrypto } from '@/utils/encrypt'

const state = () => ({
  accessToken: getAccessToken(),
  username: '',
  avatar: '',
  permissions: [],
})
const getters = {
  accessToken: (state) => state.accessToken,
  username: (state) => state.username,
  avatar: (state) => state.avatar,
  permissions: (state) => state.permissions,
}
const mutations = {
  setAccessToken(state, accessToken) {
    state.accessToken = accessToken
    setAccessToken(accessToken)
  },
  setUsername(state, username) {
    state.username = username
  },
  setAvatar(state, avatar) {
    state.avatar = avatar
  },
  setPermissions(state, permissions) {
    state.permissions = permissions
  },
}
const actions = {
  setPermissions({ commit }, permissions) {
    commit('setPermissions', permissions)
  },
  async login({ commit }, userInfo) {
    //登录逻辑
    /*    const { data } = await login(userInfo)
    const accessToken = data[tokenName]
    if (accessToken) {
      commit('setAccessToken', accessToken)
      const hour = new Date().getHours()
      const thisTime =
        hour < 8
          ? '早上好'
          : hour <= 11
          ? '上午好'
          : hour <= 13
          ? '中午好'
          : hour < 18
          ? '下午好'
          : '晚上好'
      Vue.prototype.$baseNotify(`欢迎登录${title}`, `${thisTime}！`)
    } else {
      Vue.prototype.$baseMessage(
        `登录接口异常，未正确返回${tokenName}...`,
        'error'
      )
    }*/
  },
  async getUserInfo({ commit, state }) {
    var data = sessionStorage.getItem(tokenTableName)
    if (data === undefined) {
      Vue.prototype.$baseMessage('验证失败，请重新登录...', 'error')
      return false
    }
    var userInformation = JSON.parse(
      decrypto(sessionStorage.getItem('userInformation'), 125, 20)
    )
    var permission = JSON.parse(
      decrypto(sessionStorage.getItem('permissions'), 125, 20)
    )
    if (
      permission !== undefined &&
      userInformation !== undefined &&
      Array.isArray(permission)
    ) {
      commit('setPermissions', permission)
      commit('setUsername', userInformation.adminName)
      commit('setAvatar', userInformation.adminAvatar)
      return permission
    } else {
      Vue.prototype.$baseMessage('用户信息接口异常', 'error')
      return false
    }
  },
  async logout({ dispatch }) {
    sessionStorage.clear()
    await resetRouter()
  },
  resetAccessToken({ commit }) {
    commit('setPermissions', [])
    commit('setAccessToken', '')
    removeAccessToken()
  },
}
export default { state, getters, mutations, actions }
