import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/group'

/**
 * 客户群列表
 * @param {*} data
 * {
pageNum	是	当前页
pageSize	是	每页显示条数
groupName	是	群名
userIds	是	群主id,多个使用逗号隔开
tagIds	是	标签id,多个使用逗号隔开
beginTime:
endTime:
}
 */
export function getList(params) {
  return request({
    url: service + '/chat/list',
    method: 'get',
    params
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
    params
  })
}

/**
 * 客户群同步接口
 */
export function sync() {
  return request({
    url: service + '/chat/synchWeGroup'
  })
}
