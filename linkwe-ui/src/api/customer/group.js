import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/group'

/**
 * 客户群列表
 * @param {*} data
 * {
 * pageNum:
pageSize:
groupName:
beginTime:
endTime:}
 */
export function getList(params) {
  return request({
    url: service + '/chat/list',
    method: 'get',
    params,
  })
}

/**
 * 群成员列表
 * @param {*} params 
 * {
  "groupId": "群id",
  "memberName": "成员名称",
  "pageNum": "",
  "pageSize": ""
}
 */
export function getMembers(params) {
  return request({
    url: service + '/chat/members',
    params,
  })
}

/**
 * 客户群同步接口
 */
export function sync() {
  return request({
    url: service + '/chat/synchWeGroup',
  })
}
