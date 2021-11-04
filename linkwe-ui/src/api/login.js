import request from '@/utils/request';

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid,
  };
  return request({
    url: '/login',
    method: 'post',
    data: data,
  });
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get',
  });
}

// 退出方法
export function logout() {
  return request({
    url: '/logout',
    method: 'post',
  });
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get',
  });
}

/**
 * 企业微信扫码回调接口
 * @param {*} auth_code
 */
export function wxQrLogin(auth_code) {
  return request({
    url: '/wxQrLogin',
    method: 'get',
    params: {
      auth_code,
    },
  });
}

/**
 * 获取扫码登陆相关参数
 */
export function findWxQrLoginInfo() {
  return request({
    url: '/findWxQrLoginInfo',
    method: 'get',
  });
}
