import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/code'

/**
 * 获取员工活码列表
 * @param {*} params 
{
    "pageNum": "当前页",
    "pageSize": "每页显示条数",
    "useUserName": "使用员工姓名",
    "mobile": "使用员工手机号",
    "activityScene": "活动场景",
    "createBy": "createBy",
    "beginTime": "开始时间",
    "endTime": "结束时间"
}
 */
export function getList(params) {
  return request({
    url: service + '/list',
    params
  })
}

/**
 * 根据id获取员工活码详情
 * @param {*} params
 */
export function getDetail(id) {
  return request({
    url: service + '/' + id
  })
}

/**
 * 编辑员工活码
 * @param {*} data
{
    "id": "员工活码id",
    "isJoinConfirmFriends": "客户添加时无需经过确认自动成为好友",
    "codeType": "活码类型:1:单人;2:多人;3:批量;",
    "activityScene": "活动场景",
    "welcomeMsg": "欢迎语",
    "mediaId": "素材的id",
    "weEmpleCodeUseScops": [{
        "id": "主键",
        "empleCodeId": "员工活码id",
        "useUserId": "活码类型下业务使用人的id"
    }],
    "weEmpleCodeTags": [{
        "id": "",
        "tagId": "标签id",
        "tagName": "标签",
        "empleCodeId": "员工活码id"
    }]
}
 */
export function update(data) {
  return request({
    url: service + '/update',
    method: 'put',
    data
  })
}

/**
 * 新增员工活码
 * @param {*} data 
{
    "codeType": "活码类型:1:单人;2:多人;3:批量;",
    "isJoinConfirmFriends": "客户添加时无需经过确认自动成为好友:1:是;0:否",
    "activityScene": "活动场景",
    "welcomeMsg": "欢迎语",
    "weEmpleCodeUseScops": [{
        "businessId": "使用人员id或者组织架构id",
        "businessName": "使用名称",
        "businessIdType": "业务id类型1:组织机构id,2:成员id"
    }],
    "weEmpleCodeTags": [{
        "tagId": "标签id",
        "tagName": "标签"
    }],
    mediaId: ''
}
 */
export function add(data) {
  return request({
    url: service + '/add',
    method: 'post',
    data
  })
}

/**
 * 批量新增员工活码
 * @param {*} data
 */
export function batchAdd(data) {
  return request({
    url: service + '/batchAdd',
    method: 'post',
    data
  })
}

/**
 * 删除员工活码
 * @param {*} id
 */
export function remove(id) {
  return request({
    url: service + '/delete/' + id,
    method: 'delete'
  })
}

/**
 *获取员工二维码
 * @param {*} params
{
  userIds=员工id,多个逗号隔离&
  departmentIds=部门id,多个逗号隔离
}
 */
export function getQrcode(params) {
  return request({
    url: service + '/getQrcode',
    params
  })
}

/**
 * 员工活码批量下载
 * @param {*} 	员工活码ids,多个逗号隔开
 */
export function downloadBatch(ids) {
  return request({
    url: service + '/downloadBatch',
    params: {
      ids
    },
    responseType: 'blob'
  })
}

export function download(id) {
  return request({
    url: service + '/download',
    params: {
      id
    },
    responseType: 'blob'
  })
}

/**
 * 成员添加客户统计
 * @param {*} params
 * codeId=活码id&beginTime=开始时间&endTime=结束时间
 */
export function getUserAddCustomerStat(params) {
  return request({
    url: service + '/getUserAddCustomerStat',
    params
  })
}
