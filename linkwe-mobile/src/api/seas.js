import request from '@/utils/request'

export function getTypeList (data) {
  return request({
    url: '/wecom/seas/findEmployeeCustomer',
    params: data
  })
}
