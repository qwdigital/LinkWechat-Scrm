import request from '@/utils/request'
let getway = window.CONFIG.services.wecom
const service = getway + '/customer'

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
    // delFlag: 0 客户状态 0正常 1删除
    customerType //客户类型  1:微信客户;2:企业客户
    trackState //跟进状态 1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失
    addMethod //添加方式 0:未知来源;1:扫描二维码;2:搜索手机号;3:名片分享;4:群聊;5:手机通讯录;6:微信联系人;7:来自微信好友的添加申请;8:安装第三方应用时自动添加的客服人员;9:搜索邮箱;10:视频号主页添加;11:员工活码;12:新客拉群;13:活动裂变;201:内部成员共享;202:管理员/负责人分配
    gender://0-未知 1-男性 2-女性
}
 */
export function getListNew(params) {
  return request({
    url: service + '/findWeCustomerList',
    method: 'get',
    params
  })
}

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
    params
  })
}

/**
 * 客户同步接口
 */
export function sync() {
  return request({
    url: service + '/synchWeCustomer'
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
    data
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
    data
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
    data
  })
}

// 导出用户
export function exportCustomer(query) {
  return request({
    url: service + '/export',
    method: 'get',
    params: query
  })
}

/**
 * 客户流失通知开关
 * @param {*} status [string]	是	客户流失通知开关 0:关闭 1:开启
 */
export function lossRemind(status) {
  return request({
    url: window.CONFIG.services.wecom + '/corp/startCustomerChurnNoticeSwitch/' + status,
    method: 'PUT'
  })
}

export function getLossRemindStatus(status) {
  return request({
    url: window.CONFIG.services.wecom + '/corp/getCustomerChurnNoticeSwitch/'
  })
}
/**
 * 在职继承
 * @param {*} data
{
        handoverUserId: '', //移交人
        takeoverUserId: '', //接受人
        externalUserid: '' //客户id，多个客户使用逗号隔开
      }
 * @returns
 */
export function jobExtends(data) {
  return request({
    url: service + '/jobExtends',
    method: 'POST',
    data
  })
}

/**
 *客户详情基础(基础信息+社交关系)
 * @param {*}
 * externalUserid	是	当前客户id
 * userId	是	当前跟进人id
 * delFlag: 1 用户是否流失 0 未流失，1流失
 */
export function getDetail(params) {
  return request({
    url: service + '/findWeCustomerBaseInfo',
    params
  })
}

/**
 *客户画像汇总
 * @param {*}
 * externalUserid	是	当前客户id
 * delFlag: 1 用户是否流失 0 未流失，1流失
 */
export function getSummary({ externalUserid, delFlag }) {
  return request({
    url: service + '/findWeCustomerInfoSummary',
    params: {
      externalUserid,
      delFlag
    }
  })
}
/**
 * 客户画像(单个跟进人) 获取单个跟进人的客户信息
 * @param {*} params
 * externalUserid	是	当前客户id
 * userId	是	当前跟进人id
 * delFlag: 1 用户是否流失 0 未流失，1流失
 * @returns
 */
export function getCustomerInfoByUserId({ externalUserid, userId, delFlag }) {
  return request({
    url: service + '/findWeCustomerInfoByUserId',
    params: {
      externalUserid,
      userId,
      delFlag
    }
  })
}

/**
 *跟进记录(客户轨迹)
 userId  trajectoryType 二个值得默认传null，则返回所有
 * @param {*}
 * externalUserid	是	当前客户id
 * userId		当前跟进人id
 * pageNum: 1,
        pageSize: 10,
        trajectoryType		1:信息动态;2:社交动态;3:跟进动态;4:待办动态
 */
export function getFollowUpRecord(params) {
  return request({
    url: service + '/followUpRecord',
    params
  })
}

/**
 * 客户轨迹同步/同步指定客户个人朋友圈互动数据
 * @param {*} userIds 指定员工id,多个用逗号隔开
 * @returns
 */
export function syncTrack(userIds) {
  return request({
    url: getway + `/moments/synchMomentsInteracte`
  })
}
