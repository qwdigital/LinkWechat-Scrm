import request from '@/utils/request'
const wecom = window.CONFIG.services.wecom
const service = wecom + '/groupCode'


// 通过群活码UUID获取群活码详情及可用的实际群码
export function getDetail (uuid) {
  return request({
    url: service + '/getActualCode/' + uuid
  })
}
