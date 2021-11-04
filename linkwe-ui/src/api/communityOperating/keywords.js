import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/communityKeywordGroup'

/**
 * 获取关键词拉群列表
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
 * 获取关键词拉群详情
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
 * 新增关键词拉群
 * @param {Object} data 
{
  "taskName": "string",
  "groupCodeId": 0,
  "welcomeMsg": "string",
  "keywords": "string",
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
   * 修改关键词拉群
   * @param {*} data 
{
  "taskName": "string",
  "groupCodeId": 0,
  "welcomeMsg": "string",
  "keywords": "string",
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
 * 删除关键词拉群 
 * @param {*} ids
 */
export function remove(ids) {
  return request({
    url: service + '/' + ids,
    method: 'DELETE',
  })
}
