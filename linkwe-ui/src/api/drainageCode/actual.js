import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/actual'

/**
 * 获取实际群码
 * @param {*} params 
  {
      "groupCodeId": "群活码ID",
      "pageNum": "当前页",
      "pageSize": "每页显示条数",
      "state": "状态",
  }
 */
export function getList(params) {
  return request({
    url: service + '/list',
    params
  })
}

/**
 * 添加实际群码
 * @param {*} data 
{
  "groupName": 群名称,
  "actualGroupQrCode": 实际群码图片,
  "effectTime": 有效期,
  "scanCodeTimesLimit": 扫码次数限制,
  "chatId": 客户群,
  "chatGroupName": 客户群名称
}
 */
export function add(data) {
  return request({
    url: service + '/',
    method: 'post',
    data
  })
}

/**
 * 添加实际群码
 * @param {*} data 
{
  "id": 实际群码ID,
  "groupName": 群名称,
  "actualGroupQrCode": 实际群码图片,
  "effectTime": 有效期,
  "scanCodeTimesLimit": 扫码次数限制,
  "chatId": 客户群,
  "chatGroupName": 客户群名称
}
 */
export function update(data) {
  return request({
    url: service + '/',
    method: 'put',
    data
  })
}

/**
 * 删除实际群码
 * @param {*}
 * "ids": 实际群活ID,多个ID以逗号分隔
 */
export function remove(ids) {
  return request({
    url: service + '/' + ids,
    method: 'delete',
  })
}
