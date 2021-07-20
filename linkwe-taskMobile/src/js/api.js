import request from './request'

export function getPoster(params) {
  return request('/wecom/fission/poster', params, 'post')
}

export function getCustomerList(params) {
  return request(
    `/wecom/fission/${params.fissionId}/progress/${params.eid}`,
    null,
    'get'
  )
}

export function getReward(params) {
  return request(
    `/wecom/reward/getRewardByFissionId/${params.fissionId}/${params.eid}`,
    null,
    'get'
  )
}
export function getUserInfo(params) {
  return request(`/wecom/user/getUserInfo`, params, 'get')
}
export function getToken(code) {
  return request(`/weixin/auth/getToken?code=${code}`, null, 'get')
}

export function getWXUserInfo(params) {
  return request(`/weixin/auth/getUserInfo`, params, 'get')
}
//   export function getReward(params) {
//     return request(`wecom/reward/getRewardByFissionId/${params.fissionId}/${params.eid}`,null,'get')
//   }

export function setFissionComplete(fissionId, recordId, params) {
  console.log('更新状态, 参数: ', params)
  return request(
    `/wecom/fission/complete/${fissionId}/records/${recordId}`,
    params,
    'post'
  )
}
