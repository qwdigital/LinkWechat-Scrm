import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/push'

/**
 * 新增企业id
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
export function add(data) {
  return request({
    url: service + '/add',
    method: 'post',
    data,
  })
}
