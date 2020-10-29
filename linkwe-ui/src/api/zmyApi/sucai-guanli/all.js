import request from '@/utils/request'
import axios from 'axios'
export function getList(data) {
    return request({url: '/wecom/material/list', method: 'get',params:data})
}
export function info(data) {
    return request({url: '/wecom/material', method: 'get',params:data})
}
export function add(data) {
    return request({url: '/wecom/material', method: 'post',params:data})
}
export function edit(data) {
    return request({url: '/wecom/material', method: 'put',params:data})
}
export function del(data) {
    return request({url: '/wecom/material', method: 'del',params:data})
}
export function doExport(data) {
    return request({  url: '/wecom/material', method: 'post', responseType: 'blob',params:data})
}
export function doImport(data) {
    return request({  url: '/wecom/material', method: 'post', responseType: 'blob',params:data})
}
export function getTemplate(data) {
    return request({  url: '/wecom/material', method: 'get', responseType: 'blob',params:data})
}
export function upload(data) {
    return request({  url: '/wecom/material', method: 'get',params:data})
}

export function getTreeList(data) {
    return request({  url: '/wecom/category/list', method: 'get',params:data})
}
export function infoTree(data) {
    return request({  url: '/wecom/category', method: 'get',params:data})
}
export function addTree(data) {
    return request({  url: '/wecom/category', method: 'post',params:data})
}
export function editTree(data) {
    return request({  url: '/wecom/category', method: 'put',params:data})
}
export function delTree(data) {
    return request({  url: '/wecom/category', method: 'del',params:data})
}