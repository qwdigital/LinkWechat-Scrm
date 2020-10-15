# LinkWeChat

### 平台介绍

LinkWeChat，是一款基于企业微信的开源SCRM系统，为企业构建私域流量系统的综合解决方案，显著提升企业社交运营效率！
![输入图片说明](https://images.gitee.com/uploads/images/2020/0825/144910_68578056_409467.png "屏幕截图.png")


#### 内置功能


![输入图片说明](https://images.gitee.com/uploads/images/2020/0825/145413_3a0cab42_409467.png "屏幕截图.png")


### 环境部署
#### 准备工作


```
JDK >= 1.8 (推荐1.8版本)
Mysql >= 5.5.0 (推荐5.7版本)
Redis >= 3.0
Maven >= 3.0
Node >= 10
```
#### 运行系统

##### 后端运行


- 导入LinkWeChat到Eclipse，菜单 File -> Import，然后选择 Maven -> Existing Maven Projects，点击 Next> 按钮，选择工作目录，然后点击 Finish 按钮，即可成功导入Eclipse会自动加载Maven依赖包，初次加载会比较慢（根据自身网络情况而定）
- 创建数据库LW-vue并导入数据脚本
- 打开运行com.linkwechat. LinkWeChatApplication.java


##### 前端运行


```
# 进入项目目录
cd linkwe-ui

# 安装依赖
npm install

# 强烈建议不要用直接使用 cnpm 安装，会有各种诡异的 bug，可以通过重新指定 registry 来解决 npm 安装速度慢的问题。
npm install --registry=https://registry.npm.taobao.org

# 本地开发 启动项目
npm run dev
```
4、打开浏览器，输入：http://localhost:80 （默认账户 admin/admin123）
若能正确展示登录页面，并能成功登录，菜单及页面展示正常，则表明环境搭建成功

##### 必要配置

1、修改数据库连接

- 编辑resources目录下的application-druid.yml
- url: 服务器地址
- username: 账号
- password: 密码


2、开发环境配置

- 编辑resources目录下的application.yml
- port: 端口
- context-path: 部署路径

#### 部署系统
##### 后端部署


- bin/package.bat 在项目的目录下执行
- 然后会在项目下生成 target文件夹包含 war 或jar （多模块生成在linkwe-admin）
- 1、jar部署方式
- 使用命令行执行：java –jar LinkWeChat.jar 或者执行脚本：bin/run.bat
- 2、war部署方式
- pom.xml packaging修改为war 放入tomcat服务器webapps


##### 前端部署

当项目开发完毕，只需要运行一行命令就可以打包你的应用

```
# 打包正式环境
npm run build:prod

# 打包预发布环境
npm run build:stage
```
构建打包成功之后，会在根目录生成 dist 文件夹，里面就是构建打包好的文件，通常是 ***.js 、***.css、index.html 等静态文件。

通常情况下 dist 文件夹的静态文件发布到你的 nginx 或者静态服务器即可，其中的 index.html 是后台服务的入口页面。

### 项目介绍

#### 文件结构

##### 后端结构

```
com.linkwechat     
├── common            // 工具类
│       └── annotation                    // 自定义注解
│       └── config                        // 全局配置
│       └── constant                      // 通用常量
│       └── core                          // 核心控制
│       └── enums                         // 通用枚举
│       └── exception                     // 通用异常
│       └── filter                        // 过滤器处理
│       └── utils                         // 通用类处理
├── framework         // 框架核心
│       └── aspectj                       // 注解实现
│       └── config                        // 系统配置
│       └── datasource                    // 数据权限
│       └── interceptor                   // 拦截器
│       └── manager                       // 异步处理
│       └── security                      // 权限控制
│       └── web                           // 前端控制
├── linkwe-generator   // 代码生成（可移除）
├── linkwe-quartz      // 定时任务（可移除）
├── linkwe-system      // 系统代码
├── linkwe-admin       // 后台服务
├── linkwe--wecom      // 企业微信功能

```

##### 前端结构


```
├── build                      // 构建相关  
├── bin                        // 执行脚本
├── public                     // 公共文件
│   ├── favicon.ico            // favicon图标
│   └── index.html             // html模板
├── src                        // 源代码
│   ├── api                    // 所有请求
│   ├── assets                 // 主题 字体等静态资源
│   ├── components             // 全局公用组件
│   ├── directive              // 全局指令
│   ├── layout                 // 布局
│   ├── router                 // 路由
│   ├── store                  // 全局 store管理
│   ├── utils                  // 全局公用方法
│   ├── views                  // view
│   ├── App.vue                // 入口页面
│   ├── main.js                // 入口 加载组件 初始化等
│   ├── permission.js          // 权限管理
│   └── settings.js            // 系统配置
├── .editorconfig              // 编码格式
├── .env.development           // 开发环境配置
├── .env.production            // 生产环境配置
├── .env.staging               // 测试环境配置
├── .eslintignore              // 忽略语法检查
├── .eslintrc.js               // eslint 配置项
├── .gitignore                 // git 忽略项
├── babel.config.js            // babel.config.js
├── package.json               // package.json
└── vue.config.js              // vue.config.js
```


#### 核心技术

- 前端技术栈 ES6、vue、vuex、vue-router、vue-cli、axios、element-ui

- 后端技术栈 SpringBoot、MyBatis、Spring Security、Jwt



#### 在线体验

1. 演示演示（暂未开放）：http://www.topitclub.cn/
2. 交流群
![输入图片说明](https://images.gitee.com/uploads/images/2020/0924/140420_fb631f6a_409467.png "屏幕截图.png")

#### 开发进度

![输入图片说明](https://images.gitee.com/uploads/images/2020/1015/095236_c808865f_409467.png "屏幕截图.png")

### 部分演示图，持续更新
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092211_447d288e_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092227_74c199cf_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092322_df5e338b_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092306_99975664_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092338_503e44f7_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092358_e465cb54_409467.png "屏幕截图.png")





