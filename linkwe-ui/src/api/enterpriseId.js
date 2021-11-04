import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/corp'
// 企业id

/**
 * 获取企业id列表
 */
export function getList(params) {
  return request({
    url: service + '/list',
    params,
  })
}

/**
 * 获取企业id相关详情
 * @param {String} id
 */
export function getDetail(id) {
  return request({
    url: service + '/' + id,
  })
}

/**
 * 新增企业id
 * @param {Object} data
 */
export function add(data) {
  return request({
    url: service,
    method: 'post',
    data,
  })
}
/**
 * 修改企业id
 * @param {Object} data 
 * {
  "companyName": "string",
  "corpId": "string",
  "corpSecret": "string",
  "createBy": "string",
  "createTime": "2020-09-08T15:29:14.206Z",
  "delFlag": "string",
  "id": 0,
  "params": {},
  "remark": "string",
  "searchValue": "string",
  "status": "string",
  "updateBy": "string",
  "updateTime": "2020-09-08T15:29:14.206Z"
}
 */
export function update(data) {
  return request({
    url: service,
    method: 'put',
    data,
  })
}

/**
 * 启用有效企业微信账号
 * @param {*} corpId
 */
export function start(corpId) {
  return request({
    url: service + '/startVailWeCorpAccount/' + corpId,
    method: 'put',
  })
}
