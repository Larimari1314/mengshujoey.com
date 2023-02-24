import { backendUrl } from '@/config/setting.config'
import axios from 'axios'
//获取标签下拉框
export const tabDropDown = (config) => {
  return axios.get(backendUrl + 'dropDownOption/tabDropDown', config)
}

//获取推荐等级下拉列表
export const getReferralLevel = (config) => {
  return axios.get(backendUrl + 'dropDownOption/getReferralLevel', config)
}

//获取工作室成员信息
export const getStudioMember = (config) => {
  return axios.get(backendUrl + 'dropDownOption/getStudioMember', config)
}

//获取项目全部主标题
export const getAllBasicMainTitle = (config) => {
  return axios.get(backendUrl + 'dropDownOption/getAllBasicMainTitle', config)
}

//获取项目全部主标题
export const getSexList = (config) => {
  return axios.get(backendUrl + 'dropDownOption/getSexList', config)
}
