import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/fission'

export function getList(params) {
    return request({
      url: service + '/list',
      method: 'get',
      params,
    })
  }

  export function addTask (data) {
    return request({
      url: service + '/add',
      method: 'post',
      data,
    })
  }

  export function getTaskDetail (id) {
    return request({
      url:  `${service}/getInfo/${id}`,
      method: 'get',
    })
  }


  export function sendFission (id) {
    return request({
      url:  `${service}/send/${id}`,
      method: 'get',
    })
  }