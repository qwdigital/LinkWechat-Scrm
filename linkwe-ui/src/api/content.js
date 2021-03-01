import request from '@/utils/request'
const getTree = (params) => {
  return request({
    url: '/chat/mapping/list',
    method: 'get',
    params,
  })
}

const chatList = (params) => {
  return request({
    url: '/wecom/finance/getChatContactList',
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
  getTree,
  chatList,
  listByCustomer,
  getChatAllList,
  chatGrounpList
}
