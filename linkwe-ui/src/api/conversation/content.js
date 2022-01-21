import request from '@/utils/request'

// 外部联系人 会话列表
export const getExternalChatList = (fromId) => {
  return request({
    url: '/chat/msg/selectExternalChatList/' + fromId,
    method: 'get'
  })
}

// 单聊 会话列表
export const selectAloneChatList = (params) => {
  return request({
    url: '/chat/msg/selectAloneChatList',
    method: 'get',
    params
  })
}

// 内部联系人列表
export const getInternalChatList = (fromId) => {
  return request({
    url: '/chat/msg/selectInternalChatList/' + fromId,
    method: 'get'
  })
}

export const getGroupChatList = (fromId) => {
  return request({
    url: '/chat/msg/selectGroupChatList/' + fromId,
    method: 'get'
  })
}

// export const chatList = (params) => {
//   return request({
//     url: '/chat/msg/list',
//     method: 'get',
//     params
//   })
// }

/**
 * 全文检索 会话列表
 * @param {*} params 
 * userName: '',
        customerName: '',
        contact: '',
        beginTime: '',
        endTime: '',
        pageNum: 1,
        pageSize: 10,
        orderByColumn: 't.msg_time',
        isAsc: 'desc' // asc 升序 desc 降序
 * @returns 
 */
export const getChatList = (params) => {
  return request({
    url: '/chat/msg/selectFullSearchChatList',
    method: 'get',
    params
  })
}

// 全文检索 导出列表
export const exportList = (params) => {
  return request({
    url: '/chat/msg/selectFullSearchChatList/export',
    method: 'get',
    params
    // responseType: 'blob'
  })
}
