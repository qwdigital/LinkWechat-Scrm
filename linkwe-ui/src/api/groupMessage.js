import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/push'

/**
 * 新增企业id
 * @param {Object} data
 * {
    "pushType": "发类型 0 发给客户 1 发给客户群",
    "messageType": "消息类型 0 文本消息  1 图片消息 2 语音消息  3 视频消息    4 文件消息 5 文本卡片消息 6 图文消息 7 图文消息（mpnews） 8 markdown消息 9 小程序通知消息 10 任务卡片消息 ",
    "messageJson": "消息体",
    "pushRange": "消息范围 0 全部客户  1 指定客户",
    "toUser": "指定接收消息的成员,多个通过逗号拼接",
    "toParty": "指定接收消息的部门,多个通过逗号拼接",
    "toTag": "指定接收消息的标签,多个通过逗号拼接",
    "settingTime": "设置时间(格式为： 2020-11-17 09:00:00 ) 为空时立即发送"
}
 */
export function add(data) {
  return request({
    url: service + '/add',
    method: 'post',
    data,
  })
}
