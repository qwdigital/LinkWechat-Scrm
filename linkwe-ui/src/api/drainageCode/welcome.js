import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/tlp'

/**
 * 获取欢迎语列表
 * @param {*} params 
{
    "pageNum": "当前页",
    "pageSize": "每页显示条数",
    "welcomeMsg": "欢迎语关键词",
    "welcomeMsgTplType": " 欢迎语模板类型:1:员工欢迎语;2:部门员工欢迎语;3:客户群欢迎语"
}
 */
export function getList(params) {
  return request({
    url: service + '/list',
    params
  })
}

/**
 * 根据欢迎语模板id获取模板使用范围
 * @param {*} id
 */
export function getScop(id) {
  return request({
    url: service + '/scop/' + id
  })
}

/**
 * 新增或更新欢迎语模板
 * @param {*} data
{
"id":12312, //主键，存在为更新，不存在为新增
"welcomeMsg":"", //欢迎语
"picUrl":"", //图片
"applet":[{ //小程序
"appTile":"", //小程序标题
"appId":"", //小程序id
"appPath":"", //小程序路径
"appPic":"" //小程序封面
}],
"imageText":[{ //图文
"imageTextTile":"",//图文标题
"imageTextUrl":"" //图文路径
}],
"welcomeMsgTplType":2, //1:活码欢迎语;2:员工欢迎语;3:入群欢迎语
"userIds":"ShengXiYong,SunXiWang" //员工欢迎语使用范围，员工id用逗号隔开
}
 */
export function addOrUpdate(data) {
  return request({
    url: service + '/addorUpdate',
    method: 'POST',
    data
  })
}

// /**
//  * 新增欢迎语模板
//  * @param {*} data
// {
//     "welcomeMsg": "欢迎语",
//     "mediaId": "素材的id",
//     "welcomeMsgTplType": "欢迎语模板类型:1:员工欢迎语;2:部门员工欢迎语;3:客户群欢迎语 ",
//     "weMsgTlpScopes": {
//         "useUserId": "使用人的id"
//     }
// }
//  */
// export function add(data) {
//   return request({
//     url: service,
//     method: 'POST',
//     data
//   })
// }

/**
 * 删除欢迎语
 * @param {*} ids
 */
export function remove(ids) {
  return request({
    url: service + '/' + ids,
    method: 'DELETE'
  })
}

// 预览数据
export function getPreview(id) {
  return request({
    url: service + '/tlp/preview',
    params: {
      id
    }
  })
}
