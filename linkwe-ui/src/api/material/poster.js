import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/material'
const serviceCategory = window.CONFIG.services.wecom + '/poster'

/**
 * 查询海报列表
 * @param {*} params
 */
export function getList(params) {
  return request({ url: service + '/list', params })
}

/**
 * 更新海报
 * @param {*} params
 */
export function updatePoster(data) {
  console.log('poster change:', JSON.stringify(data))
  return request({ url: serviceCategory + '/update', method: 'put', data })
}

/**
 * 添加海报
 * @param {*} data
 */
export function addPoster(data) {
  return request({ url: serviceCategory + '/insert', method: 'post', data })
}

/**
 * 查询海报详情
 * @param {*} data
 */
export function getPosterInfo(id) {
  return request({ url: serviceCategory + '/entity/' + id })
}

/**
 * 删除海报
 * @param {*} data
 */
export function removePoster(ids) {
  return request({
    url: serviceCategory + '/delete/' + ids,
    method: 'DELETE'
  })
}
