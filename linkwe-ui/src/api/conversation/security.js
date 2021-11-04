/***
 * @file:
 * @author: qi.yeqing
 * @Date: 2021-05-25 11:19:34
 */
import request from '@/utils/request'
const service = window.CONFIG.services.wecom + '/sensitive'

/**
 * 获取敏感词触发列表
 * @param {*} params
 *
 */
export function getSecurityList(params) {
  return request({
    url: service + '/hit/list',
    method: 'get',
    params,
  })
}

/**
 * 获取敏感词设置列表
 * @param {*} params
 *
 */
export function getSettingSensitiveList(params) {
  return request({
    url: service + '/list',
    method: 'get',
    params,
  })
}
/**
 * 添加敏感词设置
 * @param {*} params
 *
 */
export function addSettingSensitive(data) {
  return request({
    url: service,
    method: 'post',
    data: data
  })
}
/**
 * 修改敏感词设置
 * @param {*} params
 *
 */
export function modifySettingSensitive(data) {
  return request({
    url: service,
    method: 'put',
    data: data
  })
}
/**
 * 获取敏感词详情
 * @param {*} params
 *
 */
export function getSensitiveDetails(tableId) {
  return request({
    url: service + '/' + tableId,
    method: 'get'
  })
}
/**
 * 删除敏感词
 * @param {*} params
 *
 */
export function deleteSensitive(tableId) {
  return request({
    url: service + '/' + tableId,
    method: 'delete'
  })
}
/**
 * 获取敏感行为警告敏感记录列表
 * @param {*} params
 *
 */
export function getSensitiveRecord(params) {
  return request({
    url: service + '/list',
    method: 'get',
    params,
  })
}
/**
 * 获取敏感行为管理列表
 * @param {*} params
 *
 */
export function getSensitiveManagement(params) {
  return request({
    url: service + '/act/list',
    method: 'get',
    params,
  })
}
