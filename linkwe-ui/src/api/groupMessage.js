import request from '@/utils/request'
const service = '/wecom/groupmsg/template'

/**
 * 新增群发
 * @param {Object} data
{
    "pushType": "群发类型 0 发给客户 1 发给客户群",
    "pushRange": "消息范围 0 全部客户  1 指定客户",
    "tag": "客户标签id列表",
    "department": "部门id",
    "staffId": "员工id",
    "settingTime": "发送时间 为空表示立即发送 ，不为空为指定时间发送",
    "messageType": "消息类型 0 文本消息  1 图片消息 2 链接消息   3 小程序消息",
    "imageMessage": {
        "media_id": "图片的media_id",
        "pic_url": "图片的链接"
    },
    "linkMessage": {
        "title": "图文消息标题",
        "picurl": "图文消息封面的url",
        "desc": "图文消息的描述，最多512个字节",
        "url": "图文消息的链接"
    },
    "textMessage": {
        "content": "消息文本内容，最多4000个字节"
    },
    "miniprogramMessage": {
        "title": "小程序消息标题，最多64个字节",
        "pic_media_id": "小程序消息封面的mediaid，封面图建议尺寸为520*416",
        "appid": "小程序appid，必须是关联到企业的小程序应用",
        "page": "小程序page路径"
    }
}
 */
export function add (data) {
  return request({
    url: service + '/add',
    method: 'post',
    data
  })
}

/**
 *
 * @param {*} data
 * {
 * sender:创建人
content:内容
pushType:群发类型 0 发给客户 1 发给客户群
beginTime:开始时间
endTime:结束时间}
 */
export function getList (params) {
  return request({
    url: service + '/list',
    params
  })
}

/**
 * 群发消息详情
 * @param {*} messageId:微信消息id
 */
export function getDetail (messageId) {
  return request({
    url: service + '/getInfo',
    params: { messageId }
  })
}

export function cancelSend (ids) {
  return request({
    url: service + '/cancel/' + ids,
  })
}

/**
 *
 * @param {*}
 * messageId:微信消息id
status:发送状态 0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败
 */
export function getPushResult (params) {
  return request({
    url: service + '/pushResults',
    params
  })
}

/**
 * 同步消息发送结果
 * @param {*} data 
 * msgids:列表msgid
messageId:消息id
 */
export function syncMsg (data) {
  return request({
    url: service + '/asyncResult',
    method: 'post',
    data
  })
}
