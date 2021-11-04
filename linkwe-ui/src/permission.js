import router from './router';
import store from './store';
import { Message } from 'element-ui';
import NProgress from 'nprogress';
// import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth';

// NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/authCallback', '/bind', '/register', '/test'];

router.beforeEach((to, from, next) => {
  // NProgress.start()
  if (getToken()) {
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' });
      // NProgress.done()
    } else {
      if (store.getters.roles.length === 0) {
        // 判断当前用户是否已拉取完user_info信息
        store
          .dispatch('GetInfo')
          .then((res) => {
            // 拉取user_info
            const roles = res.roles;
            store.dispatch('GenerateRoutes', { roles }).then((accessRoutes) => {
              // 测试 默认静态页面
              // store.dispatch('permission/generateRoutes', { roles }).then(accessRoutes => {
              // 根据roles权限生成可访问的路由表
              router.addRoutes(accessRoutes); // 动态添加可访问路由表
              // 判断是否有设置企业微信id
              if (res.user && res.user.validCropId) {
                // 设置了企业微信，正常跳转
                next({ ...to, replace: true }); // hack方法 确保addRoutes已完成
              } else {
                // 没有设置企业微信的
                let isEnterpriseWechat = accessRoutes.findIndex((element) => {
                  element.path === '/_enterpriseWechat';
                });
                // 若有设置企业微信的页面权限，默认跳转到设置企业微信页面
                if (isEnterpriseWechat > -1) {
                  next({ path: '/enterpriseWechat', replace: true });
                } else {
                  // 没有权限的给予提示，并退回登录页面
                  Message.error('没有设置企业微信的权限，请联系系统管理员');
                  next({ path: '/login', replace: true });
                }
              }
            });
          })
          .catch((err) => {
            store.dispatch('FedLogOut').then(() => {
              Message.error(err);
              next({ path: '/' });
            });
          });
      } else {
        next();
        // 没有动态改变权限的需求可直接next() 删除下方权限判断 ↓
        // if (hasPermission(store.getters.roles, to.meta.roles)) {
        //   next()
        // } else {
        //   next({ path: '/401', replace: true, query: { noGoBack: true }})
        // }
        // 可删 ↑
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next();
    } else {
      next(`/login?redirect=${to.fullPath}`); // 否则全部重定向到登录页
      // NProgress.done()
    }
  }
});

router.afterEach(() => {
  // NProgress.done()
});
