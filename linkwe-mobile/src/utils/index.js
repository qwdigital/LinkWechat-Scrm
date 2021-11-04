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

// 日期时间格式化
export function dateFormat(dateString, fmt = 'yyyy-MM-dd hh:mm:ss') {
  if (!dateString) {
    return
  }
  var date = new Date(dateString.replace(/-/g, '/'))
  var o = {
    'M+': date.getMonth() + 1, //月份
    'd+': date.getDate(), //日
    'h+': date.getHours(), //小时
    'm+': date.getMinutes(), //分
    's+': date.getSeconds(), //秒
    'q+': Math.floor((date.getMonth() + 3) / 3), //季度
    'S+': date.getMilliseconds(), //毫秒
    'w+': '星期' + '日一二三四五六'.charAt(date.getDay()) //星期
  }

  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }

  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, RegExp.$1.length == 1 || String(o[k]).length > 1 ? o[k] : '0' + o[k])
    }
  }

  return fmt
}
