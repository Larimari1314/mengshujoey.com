import { backendUrl } from '@/config/setting.config'
import axios from 'axios'
//获取标签下拉框
export const tabDropDown = (config) => {
  return axios.get(backendUrl + 'dropDownOption/tabDropDown', config)
}
