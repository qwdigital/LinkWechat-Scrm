import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/chat'

/**
 * 侧边栏列表
 * @param {*} params
 */
export function getList() {
  return request({
    url: service + '/side/list'
  })
}

/**
 * 查询素材列表
 * @param {*} params 
 * { categoryId:类目id
search:搜索的值
mediaType: '' 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file), 4 文本
 }
 */
export function getMaterialList(params) {
  return request({ url: '/wecom/chat/item/mList', params })
}

/**
 * 更新侧边栏信息
 * @param {*} data
 * {
    "sideId": "",
    "mediaType": "0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报",
    "sideName": "聊天工具栏名称",
    "total": "已抓取素材数量",
    "using": "是否启用 0 启用 1 未启用"
}
 */
export function update(data) {
  return request({
    url: service + '/side',
    method: 'put',
    data
  })
}

/**
 * 侧边栏抓取素材
 * @param {*} data
 * {
    "sideId": "",
    "materialIds": "素材id列表",
    "mediaType": "素材类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报",
    "checkAll": "是否全选 0 全选 1 非全选"
}
 */
export function getMaterial(data) {
  return request({
    url: service + '/item',
    method: 'put',
    data
  })
}
