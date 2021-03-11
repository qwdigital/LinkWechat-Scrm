import request from "@/utils/request";
const wecom = window.CONFIG.services.wecom;
const service = wecom + "/portrait";

//  根据客户id和当前企业员工id获取客户详细信息
export function getCustomerInfo(params) {
  return request({
    url: service + "/findWeCustomerInfo",
    params,
  });
}
// 客户画像资料更新
export function getWeCustomerInfo(data) {
  debugger
  return request({
    url: service + "/updateWeCustomerInfo",
    method: 'post',
    data:{
      "externalUserid": data.externalUserid, // 客户Id
        "userId": data.userId, // 员工Id
        "remarkMobiles": data.remarkMobiles, // 手机号
        "birthday": data.birthday, // 客户生日
        "email": data.email, // 邮箱
        "address": data.address, // 地址
        "qq": data.qq, // qq
        "position": data.position, // 职业
        "remarkCorpName": data.remarkCorpName, // 公司
        "description": data.description, // 其他描述
    }
  });
}
// //   获取当前系统所有可用标签
// export function getAllTags() {
//   return request({
    
//   });
// }
