const config = {
  /**
   * @description token在Cookie中存储的天数，默认1天
   */
  cookieExpires: 1,
  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home',
}

config.services = {
  wecom: '/wecom',
}

export default config
