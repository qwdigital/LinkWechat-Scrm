import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '首页'
    }
  },
  // 聊天素材
  {
    path: '/chat',
    name: 'chat',
    component: () => import('../views/chat'),
    meta: {
      title: 'chat'
    }
  },
  // 用户画像
  {
    path: '/portrait',
    name: 'portrait',
    component: () => import('../views/portrait/index'),
    meta: {
      title: 'portrait'
    }
  },
  //  用户画像详情
  {
    path: '/customerDetail',
    name: 'customerDetail',
    component: () => import('../views/portrait/customerDetail.vue'),
    hidden: true,
    meta: {
      title: '',
      noAuth: true
    }
  },
  // 社群关系
  {
    path: '/community',
    name: 'community',
    component: () => import('../views/portrait/community.vue'),
    hidden: true,
    meta: {
      title: ''
    }
  },
  // 群活码扫描跳转页面
  {
    path: '/groupCode',
    name: 'groupCode',
    component: () => import('../views/groupCode/index'),
    hidden: true,
    meta: {
      title: '',
      noAuth: true
    }
  },
  // 标签建群+群sop
  {
    path: '/task',
    // name: 'task',
    component: () => import('../views/communityOperating/task/index'),
    children: [
      {
        path: '',
        component: () => import('../views/communityOperating/task/list'),
        hidden: true,
        meta: {
          title: ''
          // noAuth: true
        }
      },
      {
        path: 'state',
        name: 'taskState',
        component: () => import('../views/communityOperating/task/state.vue'),
        hidden: true,
        meta: {
          title: '',
          noAuth: true
        }
      }
    ],
    hidden: true,
    meta: {
      title: '',
      noAuth: true
    }
  },
  // 关键词群
  {
    path: '/keywords',
    name: 'keywords',
    component: () => import('../views/communityOperating/keywords/index'),
    hidden: true,
    meta: {
      title: ''
    }
  },
  // 客户公海
  {
    path: '/highSeas',
    name: 'highSeas',
    component: () => import('../views/highSeas/index'),
    meta: {
      title: '公海分配'
    }
  }
]

const router = new VueRouter({
  routes
})
router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router
