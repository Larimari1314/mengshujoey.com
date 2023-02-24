import axios from 'axios'
import { backendUrl } from '@/config/setting.config'

//查询角色列表
export const findAdminByName = (param, config) => {
  return axios.post(backendUrl + 'adminManager/findAdminByName', param, config)
}

//获取角色下拉列表
export const getRoleList = (config) => {
  return axios.get(backendUrl + 'adminManager/getRoleList', config)
}

//修改管理员信息
export const editAdminInformation = (param, config) => {
  return axios.post(
    backendUrl + 'adminManager/editAdminInformation',
    param,
    config
  )
}

//重置密码
export const resetPassword = (id, param, config) => {
  return axios.post(
    backendUrl + 'adminManager/resetPassword/' + id,
    param,
    config
  )
}

//重置密码
export const addAdmin = (param, config) => {
  return axios.post(backendUrl + 'adminManager/addAdmin/', param, config)
}

//修改状态
export const changeAdminStatus = (id, param, config) => {
  return axios.get(
    backendUrl + 'adminManager/changeAdminStatus/' + id + '?status=' + param,
    config
  )
}
