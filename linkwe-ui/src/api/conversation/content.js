import request from '@/utils/request'

// 外部联系人 会话列表
const getExternalChatList = fromId => {
  return request({
    url: '/chat/msg/selectExternalChatList/' + fromId,
    method: 'get'
  })
}

// 单聊 会话列表
const selectAloneChatList = params => {
  return request({
    url: '/chat/msg/selectAloneChatList',
    method: 'get',
    params
  })
}

// 内部联系人列表
const getInternalChatList = fromId => {
  return request({
    url: '/chat/msg/selectInternalChatList/' + fromId,
    method: 'get'
  })
}

const getGroupChatList = fromId => {
  return request({
    url: '/chat/msg/selectGroupChatList/' + fromId,
    method: 'get'
  })
}

// const chatList = (params) => {
//   return request({
//     url: '/chat/msg/list',
//     method: 'get',
//     params
//   })
// }

// 全文检索 会话列表
export const getChatList = params => {
  return request({
    url: '/chat/msg/selectFullSearchChatList',
    method: 'get',
    params
  })
}

// 全文检索 导出列表
export const exportList = params => {
  return request({
    url: '/chat/msg/selectFullSearchChatList/export',
    method: 'get',
    params
    // responseType: 'blob'
  })
}
//
export const content = {
  getExternalChatList,
  selectAloneChatList,
  getInternalChatList,
  getGroupChatList,
  // chatList,
  getChatList
}

export default content
