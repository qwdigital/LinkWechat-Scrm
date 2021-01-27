import request from '@/utils/request'
import config from '@/config'
const service = config.services.wecom

/**
 * 获取应用的jsapi_ticket
 * @param {*} url 页面url
 */
export function getAgentTicket(url) {
  return request({
    url: service + '/ticket/getAgentTicket',
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
    url: service + '/ticket/getAppTicket',
    params: {
      url,
    },
  })
}

/**
 * 获取登录用户id
 * @param {*} url 页面url
 */
export function getUserInfo(code) {
  return request({
    url: service + '/user/getUserInfo',
    params: {
      code,
      agentId: '1000005',
    },
  })
}
