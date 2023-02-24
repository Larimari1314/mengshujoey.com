import axios from 'axios'
//查询标签数据
import { backendUrl } from '@/config/setting.config'

//查询标签数据
export const queryStudioData = (params, config) => {
  return axios.post(backendUrl + 'studioManage/queryStudioData', params, config)
}

//添加工作室成员信息
export const addStudioMember = (params, config) => {
  return axios.post(backendUrl + 'studioManage/addStudioMember', params, config)
}
//删除工作室成员信息
export const deleteStudioMember = (params, config) => {
  return axios.post(
    backendUrl + 'studioManage/deleteStudioMember',
    params,
    config
  )
}
//修改工作室成员信息
export const editStudioMember = (params, config) => {
  return axios.post(
    backendUrl + 'studioManage/editStudioMember',
    params,
    config
  )
}
