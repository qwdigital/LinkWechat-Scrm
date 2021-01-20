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
export function getMaterialList(sideId) {
  return request({
    url: service + '/item/list',
    params: {
      sideId,
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
