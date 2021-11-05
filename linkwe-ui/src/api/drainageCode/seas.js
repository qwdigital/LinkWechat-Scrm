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
export function getList (context, params) {
  return request(context, {
    url: seas_list,
    method: "GET",
    params
  })
}

/**
 * 删除
 * @param {*} ids
 */
export function remove (context, ids) {
  return request(context, {
    url: seas_remove,
    method: 'DELETE',
    params: { seasIds: ids }
  })
}

// 下载模板
export function download (context) {
  return request(context, {
    url: seas_download_template,
    responseType: 'blob'
  })
}

// 数据导入
export function upload (context, data) {
  return request(context, {
    url: seas_upload,
    method: 'POST',
    params: data
  })
}

export function detail (context, id) {
  return request(context, {
    url: seas_info,
    params: { id }
  })
}

// 公海统计  抬头
export function getTotal (context, params) {
  return request(context, {
    url: seas_total_list,
    method: "GET",
    params
  })
}

// 公海统计 导入记录
export function getImportList (context, params) {
  return request(context, {
    url: seas_import_list,
    method: "GET",
    params
  })
}

// 公海统计 员工记录
export function getStaffList (context, params) {
  return request(context, {
    url: seas_staff_list,
    method: "GET",
    params
  })
}
