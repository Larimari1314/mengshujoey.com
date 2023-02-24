import axios from 'axios'
import { backendUrl } from '@/config/setting.config'
//获取推荐等级下拉列表
export const findModuleInfo = (config) => {
  return axios.get(backendUrl + 'roleManager/findModuleInfo', config)
}
//查询角色列表
export const findRoleList = (param, config) => {
  return axios.post(backendUrl + 'roleManager/findRoleList', param, config)
}
//添加角色数据
export const addRoleInfo = (param, config) => {
  return axios.post(backendUrl + 'roleManager/addRole', param, config)
}
//修改角色数据
export const editRoleInfo = (param, config) => {
  return axios.post(backendUrl + 'roleManager/editRole', param, config)
}
