import config from './contant'
export function getUrlParam(name) {
  let reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)')
  let r = window.location.search.substr(1).match(reg)
  if (r != null) {
    return unescape(r[2])
  }
  return null
}
export function getWxCode() {
  let appid = 'wx8bfe6bc2ca5c45ae' // 公众号appid
  let code = getUrlParam('code') //是否存在code
  let local = window.location.origin.includes('localhost')
    ? 'http://h5.linkwechat.cn/test.html'
    : window.location.href
  // let local = 'http://h5.x*****o.com/'
  if (code == null || code === '') {
    //不存在就打开上面的地址进行授权
    window.location.href = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${encodeURIComponent(
      local
    )}&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect`

    return false
    // window.location.href =
    //     `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${encodeURIComponent(local)}&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect`;
  } else {
    config.code = code
  }
}
