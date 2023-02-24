import { backendUrl } from '@/config/setting.config'
import axios from 'axios'

//获取私钥
export const obtainPublicKey = () => {
  return axios.get(backendUrl + 'initialize/obtainPrivateKey')
}
//获取奶茶数据
export const getMilkTeaEvaluationData = (param) => {
  return axios.post(
    backendUrl + 'evaluationDetail/getMilkTeaEvaluationData',
    param
  )
}
//获取更新码
export const milkTeaUpdateCode = () => {
  return axios.get(backendUrl + 'evaluationDetail/milkTeaUpdateCode')
}
//获取首页更新码
export const evaluationVideoUpdateCode = () => {
  return axios.get(backendUrl + 'evaluationDetail/evaluationVideoUpdateCode')
}
//获取全部数据
export const findAllVideo = (param) => {
  return axios.post(backendUrl + 'evaluationDetail/findAllVideo', param)
}
//获取标签数据
export const getLabelValue = (param) => {
  return axios.get(backendUrl + 'evaluationDetail/getLabelValue', param)
}
//查询单个项目数据
export const queryEvaluationBasic = (param) => {
  return axios.get(backendUrl + 'evaluationBasic/queryEvaluationBasic/' + param)
}
//查询团队列表
export const queryStudioMember = () => {
  return axios.get(backendUrl + 'evaluationBasic/queryStudioMember')
}
//查询数据
export const queryDataByUser = (params) => {
  return axios.post(backendUrl + 'evaluationBasic/queryData', params)
}
//日活量统计
export const countDailyActivity = () => {
  return axios.get(backendUrl + 'statistics/countDailyActivity')
}
//单项点击量
export const individualClicks = (params) => {
  return axios.get(backendUrl + 'statistics/individualClicks/' + params)
}
