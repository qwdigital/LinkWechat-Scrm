import request from '@/utils/request'
const service = window.CONFIG.services.common

export function upload(data) {
  return request({
    url: service + '/uploadFile2Cos',
    method: 'POST',
    data,
  })
}

/**
 * 下载网络连接文件
 * @param {*} params 
{
  url=文件路径&
  name=文件名称，带后缀
}
 */
export function download(url, name) {
  return (
    process.env.VUE_APP_BASE_API +
    `/common/download/url?url=${url}&name=${name}`
  )
}
