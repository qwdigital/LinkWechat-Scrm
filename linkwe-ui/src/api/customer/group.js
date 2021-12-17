import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/group'

export function getDetail(chatId) {
  return request({
    url: service + `/chat/chatDetail/${chatId}`,
    method: 'get'
  })
}

/**
 * 客户群列表
 * @param {*} data
 * {
pageNum	是	当前页
pageSize	是	每页显示条数
groupName	是	群名
groupLeaderName	是	群主,多个使用逗号隔开
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
  "chatId": "群id",
  "name": "成员名称",
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

/**
 * 编辑群标签
 * {
  "chatId": "ad cillum",
  "weeGroupTagRel": [
    {
      "chatId": "consectetur deserunt eu in",
      "id": "Ut sed ullamco non",
      "tagId": "Lorem"
    }
  ]
}
 * @param {*} data
 * @returns
 */
export function makeGroupTag(data) {
  return request({
    url: service + '/chat/makeGroupTag',
    method: 'post',
    data
  })
}
