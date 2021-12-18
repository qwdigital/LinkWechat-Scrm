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
  {
    path: '/chat',
    name: 'chat',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/chat'),
    meta: {
      title: 'chat'
    }
  },
  // 用户画像
  {
    path: '/portrait',
    name: 'portrait',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/portrait/index'),
    meta: {
      title: 'portrait'
    }
  },
  //  用户画像详情
  {
    path: '/customerDetail',
    name: 'customerDetail',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(
        /* webpackChunkName: "about" */ '../views/portrait/customerDetail.vue'
      ),
    hidden: true,
    meta: {
      title: ''
    }
  },
  // 社群关系
  {
    path: '/community',
    name: 'community',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/portrait/community.vue'),
    hidden: true,
    meta: {
      title: ''
    }
  },
  // 群活码扫描跳转页面
  {
    path: '/groupCode',
    name: 'groupCode',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ '../views/groupCode/index'),
    hidden: true,
    meta: {
      title: '',
      noAuth: true
    }
  },
  // 群活码扫描跳转页面
  {
    path: '/task',
    // name: 'task',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(
        /* webpackChunkName: "about" */ '../views/communityOperating/task/index'
      ),
    children: [
      {
        path: '',
        component: () =>
          import(
            /* webpackChunkName: "about" */ '../views/communityOperating/task/list'
          ),
        hidden: true,
        meta: {
          title: ''
          // noAuth: true
        }
      },
      {
        path: 'state',
        name: 'taskState',
        component: () =>
          import(
            /* webpackChunkName: "about" */ '../views/communityOperating/task/state.vue'
          ),
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
  // 群活码扫描跳转页面
  {
    path: '/keywords',
    name: 'keywords',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(
        /* webpackChunkName: "about" */ '../views/communityOperating/keywords/index'
      ),
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
      title: '客户公海'
    }
  }
]

const router = new VueRouter({
  routes
})

export default router
