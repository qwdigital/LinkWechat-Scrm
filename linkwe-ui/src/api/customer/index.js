import request from '@/utils/request'
const service = config.services.wecom + '/customer'

/**
 * 客户同步接口
 */
export function sync() {
  return request({
    url: service + '/synchWeCustomer',
  })
}
