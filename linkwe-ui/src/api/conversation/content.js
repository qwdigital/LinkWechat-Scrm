import request from '@/utils/request'
const getExternalChatList = (params) => {
  return request({
    url: '/chat/msg/selectExternalChatList/'+ params.fromId,
    method: 'get'
  })
}

const selectAloneChatList = (params) => {
  return request({
    url: '/chat/msg/selectAloneChatList\n/',
    method: 'get',
    params
  })
}


const getInternalChatList = (params) => {
  return request({
    url: '/chat/msg/selectInternalChatList/'+ params.fromId,
    method: 'get'
  })
}

const getGroupChatList = (params) => {
  return request({
    url: '/chat/msg/selectGroupChatList/'+ params.fromId,
    method: 'get'
  })
}

const chatList = (params) => {
  return request({
    url: '/chat/msg/list',
    method: 'get',
    params,
  })
}
const indexTable = () => {
  return request({
    url: '/wecom/page/getCorpBasicData',
    method: 'get'
  })
}
const indexEchart = () => {
  return request({
    url: '/wecom/page/getCorpRealTimeData',
    method: 'get'
  })
}
//
const chatGrounpList = (params) => {
  return request({
    url: '/wecom/finance/getChatRoomContactList',
    method: 'get',
    params,
  })
}
const listByCustomer = (params) => {
  return request({
    url: '/wecom/customer/list',
    method: 'get',
    params,
  })
}
const getChatAllList = (params) => {
  return request({
    url: '/wecom/finance/getChatAllList',
    method: 'get',
    params,
  })
}
//
export const content = {
  indexEchart,
  indexTable,
  getExternalChatList,
  selectAloneChatList,
  getInternalChatList,
  getGroupChatList,
  chatList,
  listByCustomer,
  getChatAllList,
  chatGrounpList
}
