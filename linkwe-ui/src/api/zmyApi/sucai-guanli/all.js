import request from '@/utils/request'
import axios from 'axios'
export function getList(data) {
    return request({url: '/wagesApi/listWages', method: 'get',data})
}
export function info(data) {
    return request({url: '/wecom/material', method: 'get',data})
}
export function add(data) {
    return request({url: '/wecom/material', method: 'post',data})
}
export function edit(data) {
    return request({url: '/wagesApi/editWages', method: 'post',data})
}
export function del(data) {
    return request({url: '/wagesApi/delWages', method: 'post',data})
}
export function doExport(data) {
    return request({  url: '/wagesApi/doExport', method: 'post', responseType: 'blob', data })
}
export function doImport(data) {
    return request({  url: '/wagesApi/doImport', method: 'post', responseType: 'blob', data })
}
export function getTemplate(data) {
    return request({  url: '/wagesApi/getTemplate', method: 'get', responseType: 'blob', data })
}
