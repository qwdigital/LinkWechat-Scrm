export const dictAddType = Object.freeze({
  0: '未知来源',
  1: '扫描二维码',
  2: '搜索手机号',
  3: '名片分享',
  4: '群聊',
  5: '手机通讯录',
  6: '微信联系人',
  7: '来自微信好友的添加申请',
  8: '安装第三方应用时自动添加的客服人员',
  9: '搜索邮箱',
  10: '视频号主页添加',
  11: '员工活码',
  12: '新客拉群',
  13: '活动裂变',
  201: '内部成员共享',
  202: '管理员/负责人分配'
})

export const dictJoinGroupType = Object.freeze({
  1: '由成员邀请入群（直接邀请入群)',
  2: '由成员邀请入群（通过邀请链接入群）',
  3: '通过扫描群二维码入群'
})

export const dictTrackState = Object.freeze({
  1: { name: '待跟进', color: '' },
  2: { name: '跟进中', color: 'warning' },
  3: { name: '已成交', color: 'success' },
  4: { name: '无意向', color: 'info' },
  5: { name: '已流失', color: 'danger' }
})
