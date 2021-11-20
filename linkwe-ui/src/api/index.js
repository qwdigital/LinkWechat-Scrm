import request from '@/utils/request'

export const indexTable = () => {
  return request({
    url: '/wecom/page/getCorpBasicData',
    method: 'get'
  })
}

export const indexEchart = () => {
  return request({
    url: '/wecom/page/getCorpRealTimeData',
    method: 'get'
  })
}

export const refresh = () => {
  return request({
    url: '/wecom/page/refresh',
    method: 'get'
  })
}
