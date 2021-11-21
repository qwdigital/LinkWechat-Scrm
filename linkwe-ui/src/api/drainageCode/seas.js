import request from '@/utils/request'
let
  seas_download_template,
  seas_list,
  seas_upload,
  seas_remove,
  seas_info,
  seas_total_list,
  seas_import_list,
  seas_staff_list


/**
 * 获取公海列表
 * @param {*} params 
{
    "pageNum": "当前页",
    "pageSize": "每页显示条数",
}
 */
export function getList (params) {
  return request({
    url: '/wecom/seas/list',
    method: "GET",
    params
  })
}

/**
 * 删除
 * @param {*} ids
 */
export function remove (ids) {
  return request({
    url: "/wecom/seas/" + ids,
    method: 'DELETE'
  })
}

// 提醒
export function alertFn (ids) {
  return request({
    url: "/wecom/seas/remidUser/" + ids,
    method: 'POST'
  })
}

// 下载模板
export function downloadTemplate () {
  return request({
    url: '/wecom/seas/importTemplate',
  })
}

// 数据导入
export function upload (data) {
  return request({
    url: '/wecom/seas/importData',
    method: 'POST',
    data: data
  })
}

export function detail (context, id) {
  return request(context, {
    url: seas_info,
    params: { id }
  })
}

// 公海统计  抬头
export function getTotal () {
  return request({
    url: '/wecom/seas/countCustomerSeas',
    method: "GET",
  })
}

// 公海统计 导入记录 员工记录  groupByType 1导入 2员工
export function getImportAndStaffList (params) {
  return request({
    url: '/wecom/seas/findSeasRecord',
    method: "GET",
    params
  })
}

