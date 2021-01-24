import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/chat'

/**
 * 侧边栏列表
 * @param {*} params
 */
export function getTypeList() {
  return request({
    url: service + '/side/h5List',
  })
}

/**
 * 素材列表
 * @param {*} sideId
 */
export function getMaterialList(params) {
  return request({
    url: service + '/item/list',
    params,
  })
}

/**
 * 收藏列表(h5我的)
 * @param {*} userId
 */
export function getCollectionList(params) {
  return request({
    url: service + '/collection/list',
    params,
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
 * 取消收藏
 * @param {*} data
 * {
    materialId:素材id
userId:用户id
}
 */
export function cancleCollection(data) {
  return request({
    url: service + '/collection/cancleCollection',
    method: 'post',
    data,
  })
}
