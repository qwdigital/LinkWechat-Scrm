/**
 * 请求路径参数转为对象
 * @param {*} url
 */
export function param2Obj(url) {
  const search = decodeURIComponent(url)
    .split('?')[1]
    .split('#')[0]
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
