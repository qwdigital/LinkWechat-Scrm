import request from './request'

export function getPoster(params){
    return request('/wecom/fission/poster',params,'post')
}

export function getCustomerList(params){
    return request(`/wecom/fission/${params.fissionId}/progress/${params.eid}`,null,'get')
}

export function getReward(params){
    return request(`/wecom/reward/getRewardByFissionId/${params.fissionId}/${params.eid}`,null,'get')
}
export function getUserInfo(params) {
    return request(`/wecom/user/getUserInfo`,params,'get')
  }
  
//   export function getReward(params) {
//     return request(`wecom/reward/getRewardByFissionId/${params.fissionId}/${params.eid}`,null,'get')
//   }
  