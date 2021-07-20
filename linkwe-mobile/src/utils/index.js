/**
 * query请求路径参数转为对象
 * @param {*} url
 */
export function param2Obj(url) {
  url = url == null ? window.location.href : url

  let search = decodeURIComponent(url).split('?')[1]
  search = search && search.split('#')[0]
  if (!search) {
    return {}
  }
  return JSON.parse(
    '{"' +
      search
        .replace(/"/g, '\\"')
        .replace(/&/g, '","')
        .replace(/=/g, '":"') +
      '"}'
  )
}
