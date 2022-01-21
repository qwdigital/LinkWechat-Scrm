const baseUrl = window.location.origin.includes('localhost')
  ? window.location.origin
  : 'http://demo.linkwechat.cn:8090'
// 'http://106.13.201.219'
// const baseUrl = window.location.origin+'/mock/11'
// const baseUrl = 'http://47.112.117.15:40001/mock/11'

const request = (url, params, method = 'get') => {
  url = baseUrl + url
  params = method != 'get' && params ? JSON.stringify(params) : params
  return new Promise((relosve, reject) => {
    $.ajax({
      url,
      data: params,
      dataType: 'JSON',
      type: method,
      contentType: 'application/json',
      success(res) {
        if (res.code !== 200) {
          alert(res.msg)
        }
        relosve(res)
      },
      error(err) {
        // console.log(err.status)
        alert(url + '，错误码：' + err.status)
        reject(err)
      }
    })
  })
}
export default request
