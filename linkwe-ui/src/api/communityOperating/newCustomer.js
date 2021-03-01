import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/communityNewGroup'

/**
 * 获取新客自动拉群列表
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
 * 获取新客自动拉群详情
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
 * 新增新客自动拉群
 * @param {Object} data 
{
  "activityScene": "string",
  "groupCodeId": 0,
  "isJoinConfirmFriends": true,
  "mediaId": 0,
  "qrCode": "string",
  "weEmpleCodeTags": [
    {
      "delFlag": 0,
      "empleCodeId": 0,
      "id": 0,
      "tagId": "string",
      "tagName": "string"
    }
  ],
  "weEmpleCodeUseScops": [
    {
      "businessId": "string",
      "businessIdType": 0,
      "businessName": "string",
      "delFlag": 0,
      "empleCodeId": 0,
      "id": 0,
      "mobile": "string"
    }
  ],
  "welcomeMsg": "string"
}
 */
export function add(data) {
  return request({
    url: service + '/add',
    method: 'post',
    data,
  })
}

/**
   * 修改新客自动拉群
   * @param {*} data 
{
  "activityScene": "string",
  "groupCodeId": 0,
  "isJoinConfirmFriends": true,
  "mediaId": 0,
  "qrCode": "string",
  "weEmpleCodeTags": [
    {
      "delFlag": 0,
      "empleCodeId": 0,
      "id": 0,
      "tagId": "string",
      "tagName": "string"
    }
  ],
  "weEmpleCodeUseScops": [
    {
      "businessId": "string",
      "businessIdType": 0,
      "businessName": "string",
      "delFlag": 0,
      "empleCodeId": 0,
      "id": 0,
      "mobile": "string"
    }
  ],
  "welcomeMsg": "string"
}
   */
export function update(data) {
  return request({
    url: service + '/update',
    method: 'put',
    data,
  })
}

/**
 * 删除新客自动拉群
 * @param {*} ids
 */
export function remove(ids) {
  return request({
    url: service + '/delete' + ids,
    method: 'DELETE',
  })
}

/**
 * 批量下载
 * @param {*} 	员工活码ids,多个逗号隔开
 */
export function downloadBatch(ids) {
  return request({
    url: service + '/downloadBatch',
    params: {
      ids,
    },
    responseType: 'blob',
  })
}

export function download(id) {
  return request({
    url: service + '/download',
    params: {
      id,
    },
    responseType: 'blob',
  })
}
