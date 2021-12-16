import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    roles: ['admin','editor']    // 设置该路由进入的权限，支持多个权限叠加
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: (resolve) => require(['@/views/redirect'], resolve)
      }
    ]
  },
  {
    path: '',
    redirect: '/index',
    hidden: true
  },
  {
    path: '/index',
    component: Layout,
    redirect: '/index',
    name: '运营中心',
    meta: { title: '运营中心' },
    children: [
      {
        path: '/index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: '运营中心',
        hidden: true,
        meta: {
          title: '运营中心',
          icon: 'dashboard',
          breadcrumb: false,
          noCache: true,
          affix: true
        }
      }
    ]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/authCallback',
    component: (resolve) => require(['@/views/authCallback'], resolve),
    hidden: true
  },
  // {
  //   path: '/customerManage',
  //   component: Layout,
  //   redirect: '/customerManage/customer',
  //   meta: { title: '客户管理', icon: 'user' },
  //   children: [
  //     {
  //       path: 'customer',
  //       name: 'Customer',
  //       component: (resolve) => require(['@/views/customerManage/customer'], resolve),
  //       meta: { title: '企业客户', icon: '' }
  //     },
  //     {
  //       path: 'customerDetail',
  //       hidden: true,
  //       component: (resolve) => require(['@/views/customerManage/customerDetail'], resolve),
  //       meta: { hidden: true, title: '客户', icon: '', activeMenu: '/customerManage/customer' }
  //     },
  //     {
  //       path: 'group',
  //       name: 'CustomerGroup',
  //       component: (resolve) => require(['@/views/customerManage/group'], resolve),
  //       meta: { title: '企业客群', icon: '' }
  //     },
  //     {
  //       path: 'groupDetail',
  //       hidden: true,
  //       component: (resolve) => require(['@/views/customerManage/groupDetail'], resolve),
  //       meta: { hidden: true, title: '客户群', icon: '', activeMenu: '/customerManage/group' }
  //     },
  //     {
  //       path: 'tag',
  //       name: 'CustomerTag',
  //       component: (resolve) => require(['@/views/customerManage/tag'], resolve),
  //       meta: { title: '客户标签', icon: '' }
  //     },
  //     {
  //       path: 'dimission',
  //       name: 'Dimission',
  //       component: (resolve) => require(['@/views/customerManage/dimission'], resolve),
  //       meta: { title: '离职继承', icon: '' }
  //     },
  //     {
  //       path: 'lossRemind',
  //       name: 'lossRemind',
  //       component: (resolve) =>
  //         require(['@/views/customerManage/lossRemind'], resolve),
  //       meta: { title: '流失提醒', icon: '' },
  //     },
  //   ]
  // },
  // {
  //   path: "/communityOperating",
  //   component: Layout,
  //   redirect: "/communityOperating/newCustomer",
  //   meta: { title: "社群运营", icon: "peoples" },
  //   children: [
  //     {
  //       path: "newCustomer",
  //       name: "newCustomer",
  //       component: resolve =>
  //         require(["@/views/communityOperating/newCustomer/list"], resolve),
  //       meta: { title: "新客自动拉群", icon: "" }
  //     },
  //     {
  //       path: "newCustomerAev",
  //       name: "newCustomerAev",
  //       component: resolve =>
  //         require(["@/views/communityOperating/newCustomer/aev"], resolve),
  //       meta: { title: "新客自动拉群", icon: "" }
  //     },
  //     {
  //       path: "oldCustomer",
  //       name: "oldCustomer",
  //       component: resolve =>
  //         require(["@/views/communityOperating/oldCustomer/list"], resolve),
  //       meta: { title: "老客标签建群", icon: "" }
  //     },
  //     {
  //       path: "oldCustomerAev",
  //       name: "oldCustomerAev",
  //       component: resolve =>
  //         require(["@/views/communityOperating/oldCustomer/aev"], resolve),
  //       meta: { title: "新客自动拉群", icon: "" }
  //     },
  //     {
  //       path: "keywords",
  //       name: "keywords",
  //       component: resolve =>
  //         require(["@/views/communityOperating/keywords/list"], resolve),
  //       meta: { title: "关键词拉群", icon: "" }
  //     },
  //     {
  //       path: "keywordsAev",
  //       name: "keywordsAev",
  //       component: resolve =>
  //         require(["@/views/communityOperating/keywords/aev"], resolve),
  //       meta: { title: "新客自动拉群", icon: "" }
  //     },
  //     {
  //       path: "groupSOP",
  //       name: "groupSOP",
  //       component: resolve =>
  //         require(["@/views/communityOperating/groupSOP/list"], resolve),
  //       meta: { title: "群SOP", icon: "" }
  //     },
  //     {
  //       path: "groupSOPAev",
  //       name: "groupSOPAev",
  //       component: resolve =>
  //         require(["@/views/communityOperating/groupSOP/aev"], resolve),
  //       meta: { title: "新客自动拉群", icon: "" }
  //     },
  //   ]
  // },
  // {
  //   path: '/groupMessage',
  //   component: Layout,
  //   redirect: '/groupMessage/add',
  //   meta: { title: '群发消息', icon: 'guide' },
  //   children: [
  //     {
  //       path: 'add',
  //       name: 'GroupMessageAdd',
  //       component: (resolve) => require(['@/views/groupMessage/add'], resolve),
  //       meta: { title: '新增群发', icon: '' }
  //     },
  //     {
  //       path: 'record',
  //       name: 'GroupMessageRecord',
  //       component: (resolve) => require(['@/views/groupMessage/record'], resolve),
  //       meta: { title: '群发记录', icon: '' }
  //     },
  //   ]
  // },
  // {
  //   path: '/drainageCode',
  //   component: Layout,
  //   redirect: '/drainageCode/staff',
  //   meta: { title: '引流获客', icon: 'qrcode' },
  //   children: [
  //     {
  //       path: 'staff',
  //       name: 'CodeStaff',
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/staff/list'], resolve),
  //       meta: { title: '员工活码', icon: '' },
  //     },
  //     {
  //       path: 'staffAdd',
  //       hidden: true,
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/staff/add'], resolve),
  //       meta: {
  //         title: '新建员工活码',
  //         icon: '',
  //         activeMenu: '/drainageCode/staff',
  //       },
  //     },
  //     {
  //       path: 'staffDetail',
  //       hidden: true,
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/staff/detail'], resolve),
  //       meta: {
  //         title: '员工活码详情',
  //         icon: '',
  //         activeMenu: '/drainageCode/staff',
  //       },
  //     },
  //     {
  //       path: 'group',
  //       name: 'CodeGroup',
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/group/list'], resolve),
  //       meta: { title: '客户群活码', icon: '' },
  //     },
  //     {
  //       path: 'groupAdd',
  //       hidden: true,
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/group/add'], resolve),
  //       meta: {
  //         title: '新增客户群活码',
  //         icon: '',
  //         activeMenu: '/drainageCode/group',
  //       },
  //     },
  //     {
  //       path: 'groupDetail',
  //       hidden: true,
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/group/detail'], resolve),
  //       meta: {
  //         title: '客户群活码信息',
  //         icon: '',
  //         activeMenu: '/drainageCode/group',
  //       },
  //     },
  //     {
  //       path: 'groupBaseInfo',
  //       hidden: true,
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/group/baseInfo'], resolve),
  //       meta: {
  //         title: '客户群活码',
  //         icon: '',
  //         activeMenu: '/drainageCode/group',
  //       },
  //     },
  //     {
  //       path: 'welcome',
  //       name: 'Welcome',
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/welcome/list'], resolve),
  //       meta: { title: '欢迎语', icon: '' },
  //     },
  //     {
  //       path: 'welcomeAdd',
  //       hidden: true,
  //       component: (resolve) =>
  //         require(['@/views/drainageCode/welcome/add'], resolve),
  //       meta: {
  //         title: '新建欢迎语',
  //         icon: '',
  //         activeMenu: '/drainageCode/welcome',
  //       },
  //     },
  //   ],
  // },
  // {
  //   path: '/material',
  //   component: Layout,
  //   redirect: '/material/poster',
  //   meta: { title: '素材中心', icon: 'material' },
  //   children: [
  //     {
  //       path: 'poster',
  //       name: 'MaterialPoster',
  //       component: (resolve) => require(['@/views/material/poster'], resolve),
  //       meta: { title: '海报', icon: '' },
  //     },
  // {
  //   path: 'text',
  //   name: 'MaterialText',
  //   component: (resolve) => require(['@/views/material/text'], resolve),
  //   meta: { title: '文本', icon: '' },
  // },
  // {
  //   path: 'image',
  //   name: 'MaterialImage',
  //   component: (resolve) => require(['@/views/material/image'], resolve),
  //   meta: { title: '图片', icon: '' },
  // },
  // {
  //   path: 'web',
  //   name: 'MaterialWeb',
  //   component: (resolve) => require(['@/views/material/web'], resolve),
  //   meta: { title: '网页', icon: '' },
  // },
  // {
  //   path: 'audio',
  //   name: 'MaterialAudio',
  //   component: (resolve) => require(['@/views/material/audio'], resolve),
  //   meta: { title: '语音', icon: '' },
  // },
  // {
  //   path: 'video',
  //   name: 'MaterialVideo',
  //   component: (resolve) => require(['@/views/material/video'], resolve),
  //   meta: { title: '视频', icon: '' },
  // },
  // {
  //   path: 'file',
  //   name: 'MaterialFile',
  //   component: (resolve) => require(['@/views/material/file'], resolve),
  //   meta: { title: '文件', icon: '' },
  // },
  // {
  //   path: 'applet',
  //   name: 'MaterialApplet',
  //   component: (resolve) => require(['@/views/material/applet'], resolve),
  //   meta: { title: '小程序', icon: '' },
  // },
  //   ],
  // },
  // {
  //   path: '/appTool',
  //   component: Layout,
  //   redirect: '/appTool/text',
  //   meta: { title: '应用工具', icon: 'app' },
  //   children: [
  //     {
  //       path: 'selfApp',
  //       name: 'selfApp',
  //       component: (resolve) =>
  //         require(['@/views/appTool/selfApp/list'], resolve),
  //       meta: { title: '自建应用', icon: '' },
  //     },
  //     {
  //       path: "chatToolbar",
  //       name: "ChatToolbar",
  //       component: resolve =>
  //         require(["@/views/appTool/chatToolbar/list"], resolve),
  //       meta: { title: "聊天工具栏", icon: "" }
  //     },
  //     {
  //       path: "explain",
  //       hidden: true,
  //       component: resolve =>
  //         require(["@/views/appTool/chatToolbar/explain"], resolve),
  //       meta: { title: "图文详解", icon: "" }
  //     },
  //     {
  //       path: "config",
  //       hidden: true,
  //       component: resolve =>
  //         require(["@/views/appTool/chatToolbar/config"], resolve),
  //       meta: { title: "查看已配置信息", icon: "" }
  //     },
  //     {
  //       path: "friendCircle",
  //       name: "FriendCircle",
  //       component: resolve =>
  //         require(["@/views/appTool/friendCircle/list"], resolve),
  //       meta: { title: "朋友圈", icon: "" }
  //     },
  //     {
  //       path: "friendIntroduce",
  //       hidden: true,
  //       component: resolve =>
  //         require(["@/views/appTool/friendCircle/introduce"], resolve),
  //       meta: { title: "功能介绍", icon: "" }
  //     },
  //     {
  //       path: "friendPublish",
  //       hidden: true,
  //       component: resolve =>
  //         require(["@/views/appTool/friendCircle/publish"], resolve),
  //       meta: { title: "发布动态", icon: "" }
  //     },
  //     {
  //       path: "friendBackground",
  //       hidden: true,
  //       component: resolve =>
  //         require(["@/views/appTool/friendCircle/background"], resolve),
  //       meta: { title: "设置顶部背景", icon: "" }
  //     },

  //     {
  //       path: "task",
  //       name: "Task",
  //       component: resolve => require(["@/views/appTool/task/list"], resolve),
  //       meta: { title: "任务宝", icon: "" }
  //     },
  //     {
  //       path: "taskAev",
  //       name: "Task",
  //       component: resolve => require(["@/views/appTool/task/aev"], resolve),
  //       meta: { title: "任务宝", icon: "" }
  //     },

  //     {
  //       path: "groupFission",
  //       name: "groupFission",
  //       component: resolve =>
  //         require(["@/views/appTool/groupFission/list"], resolve),
  //       meta: { title: "群裂变", icon: "" }
  //     },
  //     {
  //       path: "groupFissionAev",
  //       name: "groupFission",
  //       component: resolve =>
  //         require(["@/views/appTool/groupFission/aev"], resolve),
  //       meta: { title: "群裂变", icon: "" }
  //     }
  //   ],
  // },
  // {
  //   path: '/contacts',
  //   component: Layout,
  //   redirect: '/contacts/organization',
  //   meta: { title: '通讯录', icon: 'users' },
  //   children: [
  //     {
  //       path: 'organization',
  //       name: 'Organization',
  //       component: (resolve) => require(['@/views/contacts/organization'], resolve),
  //       meta: { title: '组织架构', icon: 'tree' }
  //     },
  //   ]
  // },
  // {
  //   path: '/enterpriseWechat',
  //   component: Layout,
  //   meta: { title: '', icon: 'wechat' },
  //   children: [
  //     {
  //       path: 'index',
  //       name: 'EnterpriseWechat',
  //       component: (resolve) => require(['@/views/enterpriseWechat/list'], resolve),
  //       meta: { title: '企业微信管理', icon: '' }
  //     },
  //   ]
  // },
  //    {
  //       path: '/application',
  //       component: Layout,
  //       meta: { title: '', icon: 'wechat' },
  //       children: [
  //         {
  //           path: 'taskGroup',
  //           name: 'taskGroup',
  //           component: (resolve) => require(['@/views/application/taskGroup'], resolve),
  //           meta: { title: '任务宝', icon: '' }
  //         },
  //         {
  //           path: 'taskDetail',
  //           name: 'taskDetail',
  //           hidden: true,
  //           component: (resolve) => require(['@/views/application/taskDetail'], resolve),
  //           meta: { title: '任务详情', icon: '' }
  //         },
  //         {
  //           path: 'editTask',
  //           name: 'editTask',
  //           hidden: true,
  //           component: (resolve) => require(['@/views/application/taskDetail/editTask'], resolve),
  //           meta: { title: '编辑活动任务', icon: '' }
  //         },
  //         {
  //           path: 'addTask',
  //           name: 'addTask',
  //           hidden: true,
  //           component: (resolve) => require(['@/views/application/taskDetail/addTask'], resolve),
  //           meta: { title: '新增活动任务', icon: '' }
  //         },
  //         {
  //             path: 'groupFission',
  //             name: 'groupFission',
  //             component: (resolve) => require(['@/views/application/groupFission'], resolve),
  //             meta: { title: '群裂变', icon: '' }
  //         },
  //         {
  //             path: 'addFission',
  //             name: 'addFission',
  //             hidden: true,
  //             component: (resolve) => require(['@/views/application/fissionDetail/addFission'], resolve),
  //             meta: { title: '新增群裂变', icon: '' }
  //           },
  //           {
  //             path: 'fissionDetail',
  //             name: 'fissionDetail',
  //             hidden: true,
  //             component: (resolve) => require(['@/views/application/fissionDetail'], resolve),
  //             meta: { title: '任务详情', icon: '' }
  //           },
  //       ]
  //     },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  {
    path: '/dict',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'type/data/:dictId(\\d+)',
        component: (resolve) => require(['@/views/system/dict/data'], resolve),
        name: 'Data',
        meta: { title: '字典数据', icon: '' }
      }
    ]
  },
  {
    path: '/job',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'log',
        component: (resolve) => require(['@/views/monitor/job/log'], resolve),
        name: 'JobLog',
        meta: { title: '调度日志' }
      }
    ]
  },
  {
    path: '/gen',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'edit/:tableId(\\d+)',
        component: (resolve) => require(['@/views/tool/gen/editTable'], resolve),
        name: 'GenEdit',
        meta: { title: '修改生成配置' }
      }
    ]
  },
  // {
  //   path: '/test',
  //   component: (resolve) => require(['@/views/test'], resolve),
  //   hidden: true,
  // },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  }
]

export default new Router({
  // mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
