import request from '@/utils/request'
const wecom = window.CONFIG.services.wecom
const service = wecom + '/portrait'

//  根据客户id和当前企业员工id获取客户详细信息
export function getCustomerInfo(params) {
    return request({
      url: service + '/findWeCustomerInfo',
      params
    })
  }
//   获取当前系统所有可用标签
export function getAllTags() {
    return request({ 
      url: service + '/findAllTags'
    })
  }