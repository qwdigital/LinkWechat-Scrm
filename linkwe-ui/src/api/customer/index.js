import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/customer'

/**
 * 客户列表
 * @param {*} params 
 * {
    "pageNum": "当前页",
    "pageSize": "每页显示条数",
    "name": "客户名称",
    "userId": "添加人id",
    "tagIds": "标签id,多个标签，id使用逗号隔开",
    "beginTime": "开始时间",
    "endTime": "结束时间"
    status: 0 客户状态 0正常 1删除
}
 */
export function getList(params) {
  return request({
    url: service + '/list',
    method: 'get',
    params,
  })
}

/**
 * 客户同步接口
 */
export function sync() {
  return request({
    url: service + '/synchWeCustomer',
  })
}

/**
 * 编辑客户标签
 * @param {*} data 
 * {
    "externalUserid": "外部联系人userid",
    "addTag": [{
        "id": "标签id",
        "groupId": "标签组id",
        "name": "标签名"
    }]
}
 */
export function makeLabel(data) {
  return request({
    url: service + '/makeLabel',
    method: 'post',
    data,
  })
}

/**
 * 移除客户标签
 * @param {*} data 
 * {
    "externalUserid": "外部联系人userid",
    "addTag": [{
        "id": "标签id",
        "groupId": "标签组id",
        "name": "标签名"
    }]
}
 */
export function removeLabel(data) {
  return request({
    url: service + '/removeLabel',
    method: 'DELETE',
    data,
  })
}

/**
 * 客户生日编辑
 * @param {*} data 
 * {
  "externalUserid": "",
  "birthday": ""
}
 */
export function updateBirthday(data) {
  return request({
    url: service,
    method: 'PUT',
    data,
  })
}

// 导出用户
export function exportCustomer(query) {
  return request({
    url: service + '/export',
    method: 'get',
    params: query,
  })
}

/**
 *客户详情
 * @param {*} externalUserid
 */
export function getDetail(externalUserid) {
  return request({
    url: service + '/getCustomersByUserId/' + externalUserid,
  })
}

/**
 * 客户流失通知开关
 * @param {*} status [string]	是	客户流失通知开关 0:关闭 1:开启
 */
export function lossRemind(status) {
  return request({
    url:
      window.CONFIG.services.wecom +
      '/corp/startCustomerChurnNoticeSwitch/' +
      status,
    method: 'PUT',
  })
}

export function getLossRemindStatus(status) {
  return request({
    url: window.CONFIG.services.wecom + '/corp/getCustomerChurnNoticeSwitch/',
  })
}
