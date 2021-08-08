import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/weapp'

/**
 * 列表
 * @param {*} params
 */
export function getList() {
  return request({
    url: service + '/list'
  })
}

/**
 * 更新
 * @param {*} data
 * {
 * agentId	number	非必须企业应用的id	
reportLocationFlag	number	非必须企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；	
logoMediaid	number	非必须企业应用头像的mediaid，通过素材管理接口上传图片获得mediaid，上传后会自动裁剪成方形和圆形两个头像	
agentName	number	非必须企业应用名称，长度不超过32个utf8字符	
description	number	非必须企业应用详情，长度为4至120个utf8字符	
redirectDomain	number	非必须企业应用可信域名	
isreportenter	number	非必须是否上报用户进入应用事件。0：不接收；1：接收。	
home_url	number	非必须应用主页url。url必须以http或者https开头	
id	string	必须主键id
 * }
 */
export function update(data) {
  return request({
    url: service + '/updateWeApp',
    method: 'put',
    data
  })
}

// 添加
export function add(data) {
  return request({
    url: service + '/addWeApp',
    method: 'post',
    data
  })
}
