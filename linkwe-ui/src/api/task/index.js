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

  export function getTaskDetail (id,type) {
      let url = type?`${service}/getInfo/${id}/${type}`:`${service}/getInfo/${id}`
    return request({
      url,
      method: 'get',
    })
  }


  export function sendFission (id) {
    return request({
      url:  `${service}/send/${id}`,
      method: 'get',
    })
  }

  export function getStat(params) {
    return request({
      url: service + '/stat',
      method: 'get',
      params,
    })
  }

  export function editTask (data) {
    return request({
      url: service + '/edit',
      method: 'put',
      data,
    })
  }