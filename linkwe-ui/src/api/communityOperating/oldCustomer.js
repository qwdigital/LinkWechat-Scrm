import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/communityPresTagGroup'

/**
 * 获取老客标签建群列表
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
 * 获取老客标签建群详情
 * @param {*} params
 * id: 老客标签建群任务ID
 */
export function getDetail(id) {
  return request({
    url: service + '/' + id,
    method: 'get',
  })
}

/**
 * 新增老客标签建群
 * @param {Object} data 
{
  "taskName": "string",
  "welcomeMsg": "string",
  "sendType": 0,
  "groupCodeId": "string",
  "weOldGroupTagList": [],
  "weOldGroupUseScopeList": [],
  "sendScope": 0,
  "sendGender": 0,
  "cusBeginTime": "string",
  "cusEndTime": "string",
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
   * 修改老客标签建群
   * @param {*} data 
{
  "taskName": "string",
  "welcomeMsg": "string",
  "sendType": 0,
  "groupCodeId": "string",
  "weOldGroupTagList": [],
  "weOldGroupUseScopeList": [],
  "sendScope": 0,
  "sendGender": 0,
  "cusBeginTime": "string",
  "cusEndTime": "string",
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
 * 删除老客标签建群
 * @param {*} ids
 */
export function remove(ids) {
  return request({
    url: service + '/' + ids,
    method: 'DELETE',
  })
}


/**
 * 客户统计
 * @param {*} params
 * id: 老客标签建群任务ID
 */
 export function getStat(id, params) {
  return request({
    url: service + '/stat/' + id,
    method: 'get',
    params
  })
}