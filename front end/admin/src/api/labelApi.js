import axios from 'axios'
//查询标签数据
import { backendUrl } from '@/config/setting.config'

//查询标签数据
export const queryLabelData = (params, config) => {
  return axios.post(backendUrl + 'labelManager/queryLabelData', params, config)
}

//添加标签数据
export const addLabelData = (params, config) => {
  return axios.post(backendUrl + 'labelManager/addLabelData', params, config)
}

//根据id删除标签数据
export const deleteLabelById = (params, config) => {
  return axios.post(backendUrl + 'labelManager/deleteLabelById', params, config)
}

//修改标签数据
export const editLabelData = (params, config) => {
  return axios.post(backendUrl + 'labelManager/editLabelData', params, config)
}
