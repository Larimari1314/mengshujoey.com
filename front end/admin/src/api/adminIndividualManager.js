import axios from 'axios'
import { backendUrl } from '@/config/setting.config'

//修改个人信息
export const editIndividualAdminInformation = (param, config) => {
  return axios.post(
    backendUrl + 'personalInformation/editAdminInformation',
    param,
    config
  )
}
export const modifyLoginInformation = (param, config) => {
  return axios.post(
    backendUrl + 'personalInformation/modifyLoginInformation',
    param,
    config
  )
}
