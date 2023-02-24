import request from '@/utils/request'
import { backendUrl } from '@/config/setting.config'
import axios from 'axios'

export function getPublicKeyLogin() {
  return request({
    url: backendUrl + 'adminLogin/obtainPrivateKey',
    method: 'get',
  })
}

/*export function adminLogin(data) {
  return request({
    url: backendUrl + 'loginAdmin/login',
    method: 'post',
    data,
  })
}*/
export const adminLogin = (params) => {
  return axios.post(backendUrl + 'loginAdmin/login', params)
}
//导入信息
export const getVideoInformation = (params) => {
  return axios.get('https://api.szfx.top/bilibili/api.php?url=' + params)
}

//添加团队测评项目
export const addProject = (params, config) => {
  return axios.post(backendUrl + 'evaluation/addProject', params, config)
}

//添加个人测评项目
export const addSingleProject = (params, config) => {
  return axios.post(backendUrl + 'evaluation/addSingleProject', params, config)
}

//查询全部项目数据
export const queryAssessmentData = (params, config) => {
  return axios.post(
    backendUrl + 'evaluation/queryAssessmentData',
    params,
    config
  )
}

//删除项目
export const deleteEvaluationProject = (params, config) => {
  return axios.post(
    backendUrl + 'evaluation/deleteEvaluationProject',
    params,
    config
  )
}

//修改数据基本信息
export const editBasicInformation = (params, config) => {
  return axios.post(
    backendUrl + 'evaluation/editBasicInformation',
    params,
    config
  )
}

//根据id获取数据基本信息
export const findDetailInformation = (params, config) => {
  return axios.get(
    backendUrl + 'evaluation/findDetailInformation/' + params,
    config
  )
}

export const editDetailInformations = (params, config) => {
  return axios.post(
    backendUrl + 'evaluation/editDetailInformation',
    params,
    config
  )
}

//根据id删除基本信息
export const deleteDetailInformationById = (params, config) => {
  return axios.delete(
    backendUrl + 'evaluation/deleteDetailInformationById/' + params,
    config
  )
}

//添加基本项目
export const addDetailInformation = (params, config) => {
  return axios.post(
    backendUrl + 'evaluation/addDetailInformation',
    params,
    config
  )
}
