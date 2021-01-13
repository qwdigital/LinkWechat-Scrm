import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/ticket'

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
 * 收藏列表(h5我的)
 * @param {*} userId
 */
export function getCollectionList(userId) {
  return request({
    url: service + '/collection/list',
    params: {
      userId,
    },
  })
}

/**
 * 添加收藏
 * @param {*} data
 * {
    materialId:素材id
userId:用户id
}
 */
export function addCollection(data) {
  return request({
    url: service + '/collection/addCollection',
    method: 'post',
    data,
  })
}

/**
 * 侧边栏抓取素材
 * @param {*} data
 * {
    materialId:素材id
userId:用户id
}
 */
export function cancleCollection(data) {
  return request({
    url: service + '/collection/cancleCollection',
    method: 'put',
    data,
  })
}
