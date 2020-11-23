import request from '@/utils/request'

/**
 * 下载网络连接文件
 * @param {*} params 
{
  url=文件路径&
  name=文件名称，带后缀
}
 */
export function download(url, name) {
  return `/common/download/url?url=${url}&name=${name}`
}
