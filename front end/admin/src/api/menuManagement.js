import request from '@/utils/request'

export function doEdit(data) {
  return request({
    url: '/menuManagement/doEdit',
    method: 'post',
    data,
  })
}
