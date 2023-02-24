import axios from 'axios'
import { backendUrl } from '@/config/setting.config'

//查询日志
export const findAdminLog = (param, config) => {
  return axios.post(backendUrl + 'adminLog/findAdminLog', param, config)
}
//查询日活
export const queryDailyActivity = (config) => {
  return axios.get(backendUrl + 'dailyActivity/queryDailyActivity', config)
}
//查询项目点击
export const queryProjectData = (config) => {
  return axios.get(backendUrl + 'dailyActivity/queryProjectData', config)
}
