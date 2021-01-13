const config = {
  /**
   * @description token在Cookie中存储的天数，默认1天
   */
  cookieExpires: 1,
  /**
   * @description 是否使用国际化，默认为false
   *              如果不使用，则需要在路由中给需要在菜单中展示的路由设置meta: {title: 'xxx'}
   *              用来在菜单中显示文字
   */
  useI18n: false,
  /**
   * @description api请求基础路径
   */
  // 测试服
  domain:
    process.env.NODE_ENV === 'development'
      ? 'http://192.168.1.32:8888'
      : 'http://192.168.1.26:8888', //

  // 正式服
  //   domain: process.env.NODE_ENV === 'development' ? 'http://192.168.1.35:8118' : 'https://gateway.visualinsur.cn:8888', //
  /**
   * @description 默认打开的首页的路由name值，默认为home
   */
  homeName: 'home',
  /**
   * @description 需要加载的插件
   */
  plugin: {
    // 'error-store': {
    //   showInHeader: true, // 设为false后不会在顶部显示错误日志徽标
    //   developmentOff: false // 设为true后在开发环境不会收集错误信息，方便开发中排查错误
    // }
  },
  // get domain() {
  //   return process.env.NODE_ENV === 'development' ? this.baseUrl.dev : this.baseUrl.pro
  // }
}

config.services = {
  wecom: '/wecom',
}

export default config
