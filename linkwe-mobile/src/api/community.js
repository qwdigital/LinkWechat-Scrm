import request from '@/utils/request'
const wecom = window.CONFIG.services.wecom
const service = wecom + '/community/h5'


/**
 * 获取用户相关的所有任务
 * @param {*} userId 用户ID
 * @param {*} taskType 0: 全部  1: 老客标签建群  2: 群SOP
 * @returns 
 */
export function getTasks (userId, taskType) {
  return request({
    url: service + '/' + userId,
    params: {
      type: taskType
    }
  })
}


/**
 * 获取任务相关员工及完成情况
 * @param {*} taskId 任务ID
 * @param {*} taskType 1: 老客标签建群  2: 群SOP
 * @returns 
 */
export function getState (taskId, taskType) {
  return request({
    url: service + '/scope/' + taskId,
    params: {
      type: taskType
    }
  })
}


/**
 * 更新任务状态
 * @param {*} userId 用户ID
 * @param {*} taskId 任务ID
 * @param {*} taskType 0: 老客标签建群   1: 群SOP
 * @returns 
 */
 export function changeStatus (userId, taskId, taskType) {
  return request({
    url: service + '/changeStatus',
    params: {
      emplId: userId,
      taskId: taskId,
      type: taskType
    }
  })
}


/**
 * 获取用户相关的所有关键词任务任务
 * @param {*} word 搜索的关键词或活码名称
 * @returns 
 */
 export function getKeywordTasks (word) {
  return request({
    url: service + '/filter',
    params: {
      word: word
    }
  })
}
