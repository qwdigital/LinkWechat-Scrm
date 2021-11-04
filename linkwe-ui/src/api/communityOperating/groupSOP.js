import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/communityGroupSop'

/**
 * 获取群SOP列表
 * @param {*} params
 *
 */
export function getList(params) {
  return request({
    url: service + '/list',
    method: 'get',
    params,
  })
}

/**
 * 获取群SOP详情
 * @param {*} params
 *
 */
export function getDetail(id) {
  return request({
    url: service + '/' + id,
    method: 'get',
  })
}

/**
 * 新增群SOP
 * @param {Object} data 
{
  "ruleName": "string",
  "groupIdList": [],
  "title": "string",
  "content": "string",
  "materialIdList": [],
  "startExeTime": "string",
  "stopExeTime": "string",
}
 */
export function add(data) {
  return request({
    url: service + '/',
    method: 'post',
    data,
  })
}

/**
   * 修改群SOP
   * @param {*} data 
{
  "ruleName": "string",
  "groupIdList": [],
  "title": "string",
  "content": "string",
  "materialIdList": [],
  "startExeTime": "string",
  "stopExeTime": "string",
}
   */
export function update(id, data) {
  return request({
    url: service + '/' + id,
    method: 'put',
    data,
  })
}

/**
 * 删除群SOP 
 * @param {*} ids
 */
export function remove(ids) {
  return request({
    url: service + '/' + ids,
    method: 'DELETE',
  })
}
