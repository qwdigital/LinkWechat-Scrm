import request from '@/utils/request'
import config from '@/config'
const service = config.services.wecom + '/ticket'

/**
 * 获取应用的jsapi_ticket
 * @param {*} url 页面url
 */
export function getAgentTicket(url) {
  return request({
    url: service + '/getAgentTicket',
    params: {
      url,
    },
  })
}

/**
 * 获取企业的jsapi_ticket
 * @param {*} url 页面url
 */
export function getAppTicket(url) {
  return request({
    url: service + '/getAppTicket',
    params: {
      url,
    },
  })
}

/**
 * 获取企业的jsapi_ticket
 * @param {*} url 页面url
 */
export function getLoginUserId() {
  return request({
    url: '/system/user/findCurrentLoginUser',
  })
}
