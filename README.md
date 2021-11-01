<p></p>
<p></p>

<p align="center">
  <img alt="logo" src="https://images.gitee.com/uploads/images/2021/1101/010135_60061e4a_1480777.png" style="margin-bottom: 0px;">
</p>

<div align="center">

[![star](https://gitee.com/LinkWeChat/link-wechat/badge/star.svg?theme=gvp)](https://gitee.com/LinkWeChat/link-wechat/stargazers)
[![fork](https://gitee.com/LinkWeChat/link-wechat/badge/fork.svg?theme=gvp)](https://gitee.com/LinkWeChat/link-wechat/members)
![LinkWeChat](https://img.shields.io/badge/LinkWeChat-V1.5-brightgreen)
![license](http://img.shields.io/badge/license-GPL%203.0-orange)


</div>

<p></p>
<p></p>
<p></p>
<p></p>

---

 **<p align="center">如果您觉得我们的开源项目很有帮助，请帮忙点击右上方的  :star: Star ，您的认可就是我们最大的动力，谢谢支持！:heart:</p>** 

---

[LinkWeChat 官方帮助手册——语雀](https://www.yuque.com/linkwechat/help)

### 产品简介

> LinkWeChat, Link to WeChat.

基于人工智能的企业微信 SCRM 系统——LinkWeChat基于企业微信开放能力，不仅集成了企微基础的客户管理和后台管理功能，而且通过引流获客、客情维系、社群运营等灵活高效的客户运营模块，让客户与企业之间建立强链接关系，同时进一步通过多元化的客户营销工具，帮助企业提高客户运营效率，强化营销能力，拓展盈利空间，是企业私域流量管理与营销的综合解决方案。

![输入图片说明](https://images.gitee.com/uploads/images/2021/1101/012040_64b8b918_1480777.png "仟微助手企业微信SCRM智能营销系统-V1.5_13.png")

#### 功能特性

系统分为八大模块：

*   **运营中心** ：客户、客群、会话等全功能数据报表，数据一目了然；
*   **引流获客** ：活码、群活码、公海、客服等多渠道引流，实现精准获客；
*   **客户中心** ：助力企业搭建私域流量池，高效运营客户；
*   **客情维系** ：企业客户运营精细化，朋友圈、红包工具提高客户活跃度；
*   **社群运营** ：客群运营场景全覆盖，快速拉群；
*   **全能营销** ：提供多类型、多场景客户营销工具；
*   **企业风控** ：会话合规存档，敏感内容全局风控；
*   **企业管理** ：组织架构、自建应用全融合，实现“一个后台”；


![输入图片说明](https://images.gitee.com/uploads/images/2020/1231/232207_6a0f4a67_1480777.png "LinkWeChat 企业微信私域流量营销专家-V1.0.4_13.png")

### 环境部署

#### 准备工作


```java
JDK >= 1.8 (推荐1.8版本)
Mysql >= 5.7.0 (推荐5.7版本)
Mysql >= 5.7.0 (推荐5.7版本)
Redis >= 3.0
Maven >= 3.0
Node >= 10
```

#### 运行系统

##### 后端运行


- 导入 `IDEA` 中 
- 创建数据库 `LW-vue` 并导入数据脚本
- 打开运行 `com.linkwechat.LinkWeChatApplication.java`

##### 前端运行


```bash
# 进入项目目录
cd linkwe-ui

# 安装依赖
npm install

# 强烈建议不要用直接使用 cnpm 安装，会有各种诡异的 bug，可以通过重新指定 registry 来解决 npm 安装速度慢的问题。
npm install --registry=https://registry.npm.taobao.org

# 本地开发 启动项目
npm  run serve
```

打开浏览器，输入 `http://localhost:80  `，默认账密为：`admin/admin123` 。

若能正确展示登录页面，并能成功登录，菜单及页面展示正常，则表明环境搭建成功。

##### 必要配置

1、修改数据库连接

- 编辑 `resources` 目录下的 `application-druid.yml`
- `url` : 服务器地址
- `username` : 账号
- `password `: 密码

数据库脚本：[https://gitee.com/LinkWeChat_admin/link-we-chat-db/tree/master](https://gitee.com/LinkWeChat_admin/link-we-chat-db/tree/master)


2、开发环境配置

- 编辑 `resources` 目录下的 `application.yml`
- `port` : 端口
- `context-path` : 部署路径

#### 部署系统

##### 后端部署


- `bin/package.bat` 在项目的目录下执行
- 然后会在项目下生成 ` target` 文件夹包含 `war`  或 `jar `（多模块生成在 `linkwe-admin`）
- `jar` 部署方式：使用命令行执行 `java –jar LinkWeChat.jar`
- `war` 部署方式：`pom.xml packaging` 修改为 `war`  放入 `tomcat` 服务器 `webapps`

##### 前端部署

当项目开发完毕，只需要运行一行命令就可以打包你的应用

```bash
# 打包正式环境
npm run build:prod

# 打包预发布环境
npm run build:stage
```

构建打包成功之后，会在根目录生成 `dist` 文件夹，里面就是构建打包好的文件，通常是 `.js` 、`.css`、`index.html` 等静态文件。

通常情况下 `dist` 文件夹的静态文件发布到你的 `nginx` 或者静态服务器即可，其中的 `index.html` 是后台服务的入口页面。

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
├── linkwe-generator   // 代码生成
├── linkwe-quartz      // 定时任务
├── linkwe-system      // 系统代码
├── linkwe-admin       // 后台服务
├── linkwe-ui          // 页面前端代码
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

- 前端技术栈 `ES6`、`vue`、`vuex`、`vue-router`、`vue-cli`、`axios`、`element-ui`

- 后端技术栈 `SpringBoot`、`MyBatis-plus`、`Spring Security`、`Jwt`

#### 业务架构

利用 NLP 技术对聊天记录进行智能语义分析，实现敏感词自动告警及自动打标签功能。

![输入图片说明](https://images.gitee.com/uploads/images/2020/1231/232301_2dcf24b2_1480777.png "LinkWeChat 企业微信私域流量营销专家-V1.0.4_22.png")

#### 在线体验

演示地址：http://demo.linkwechat.cn/  

演示账号/密码：Wecome/123456

#### 开发进度

![输入图片说明](https://images.gitee.com/uploads/images/2021/0521/161235_092f96cb_1480777.png "LinkWeChat 1.0.png")

### 联系作者加入群

![输入图片说明](https://images.gitee.com/uploads/images/2021/0414/093533_899b0110_1480777.png "江冬勤-linkwechat咨询.png")

### 特别鸣谢

感谢[RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue?_from=gitee_search)提供框架代码。

### 部分演示图，持续更新

![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092211_447d288e_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092227_74c199cf_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092322_df5e338b_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092306_99975664_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092338_503e44f7_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092358_e465cb54_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1109/094122_dfd73b9e_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1109/094154_e5052872_409467.png "屏幕截图.png")

### 合作伙伴

![输入图片说明](https://images.gitee.com/uploads/images/2020/1231/234054_ede1ef54_1480777.png "LinkWeChat 企业微信私域流量营销专家-V1.0.4_33.png")

### 版权声明

LinkWeChat 开源版遵循 [GPL-3.0](https://gitee.com/LinkWeChat/link-wechat/blob/master/LICENSE) 开源协议发布，并提供免费使用，但 **绝不允许修改后和衍生的代码做为闭源的商业软件发布和销售！** 

### 捐赠支持

#### 来一杯卡布奇诺

如果您觉得我们的开源项目 `LinkWeChat` 对您有帮助，那就请项目开发者们来一杯卡布奇诺吧！当前我们接受来自于**微信**、**支付宝**或者**码云**的捐赠，请在捐赠时备注自己的昵称或附言。

您的捐赠将用于支付该项目的一些费用支出，并激励开发者们以便更好的推动项目的发展，同时欢迎捐赠**公网服务器**用于提高在线演示系统体验。

![输入图片说明](https://images.gitee.com/uploads/images/2021/0222/221344_727a0e80_1480777.png "image-20210222214357456.png")

#### 长期捐赠

如果您是企业的经营者并且有计划将 `LinkWeChat` 用在公司的经营产品中，欢迎进行长期捐赠。长期捐赠有商业上的益处有：

* 积极响应，快速维护，及时更新；
* 企业名称、Logo 及官网链接将长期展示在开源仓库、`LinkWeChat` 官网及宣发材料中；
* 捐赠金额同比例抵扣未来 `LinkWeChat` 的付费产品价格。

如果您对长期赞助 `LinkWeChat` 团队感兴趣，或者有其他好想法，欢迎联系开发团队微信 `sxjiangdongqin`，或发送邮件到 iamxiarui@foxmail.com。

#### 捐赠记录

`LinkWeChat` 全体开发团队感谢以下全部小伙伴们的赞助（排名不分先后）：

|   昵称   |  金额   | 渠道 |        时间         |        附言        |
| :------: | :-----: | :--: | :-----------------: | :----------------: |
|   yang   | ￥10.00 | 码云 | 2021-02-22 22:08:59 | 感谢您的开源项目！ |
| 水库浪子 | ￥1.00  | 码云 | 2021-02-22 22:09:03 | 感谢您的开源项目！ |
|     楼*     |   ￥166.60      | 微信     |       2021-02-22 22:27:25              |     希望这个好项目能长久发展               |
|     joygezxp     |   ￥66.60      | 微信     |       2021-02-22 22:39:41              |     愿项目一路666               |
|     iamxiarui     |   ￥10.00      | 微信     |       2021-02-22 22:37:52              |     坚持开源不容易               |
|     godricV     |   ￥10.00      | 微信     |       2021-02-22 22:28:55              |     感谢您的开源项目！               |
|     *标     |   ￥20.00      | 微信     |       2021-03-05 16:03:19              |     感谢您的开源项目！               |
|     *涯     |   ￥30.00      | 微信     |       2021-03-09 12:14:02              |      加油，感谢开源！               |
|     *魂     |   ￥30.00      | 微信     |       2021-03-11 10:52:47              |       感谢开源供大家学习               |
|     *J     |   ￥10.00      | 微信     |       2021-03-12 14:20:22              |       感谢开源供大家学习               |
|     杨*源     |   ￥10.00      | 码云     |       2021-03-23 11:07:22              |       期待月底的大更新               |
|     骆*升     |   ￥66.66      |  支付宝     |       2021-03-23 11:31:58              |       感谢开源供大家学习               |
|     曲*旭     |   ￥20.00      |   码云     |       2021-03-23 11:31:58              |       感谢您的开源项目！               |
|     郑*     |   ￥ 50.00      |  码云     |       2021-03-23 11:31:58      | 虽然之前做过类似的，还是感谢下开源和分享的奉献精神       |
|     *喵     |   ￥  66.00      |   微信     |       2021-03-24 11:06:31      | 感谢您的开源项目！       |
|     q*s     |   ￥ 50.00      |  微信     |       2021-03-29 19:57:49      | 感谢您的开源项目！       |
|     *海     |   ￥  1.00      |  微信     |       2021-03-30 10:47:03      | 感谢您的开源项目！       |
|     m*r     |   ￥ 50.00      |  微信     |       2021-03-30 17:55:30      | 感谢您的开源项目！       |
|     *祺     |   ￥ 20.00      |  支付宝     |       2021-03-30 19:03:59      | 感谢您的开源项目！       |
|     *桥     |   ￥  66.66      |  支付宝     |       2021-04-07 16:38:31      |  祝项目一路 666       |
|     *力     |   ￥  66.00      |  微信     |       2021-04-09 10:30:11      |   支持开源       |
|     *生     |   ￥  66.00      |  微信     |       2021-04-15 21:36:01      |   支持开源       |
|     Q*N     |   ￥  10.00      |  微信     |       2021-04-19 20:24:11      |   感谢分享       |
|     大*k     |   ￥  10.00      |  微信     |       2021-04-22 12:25:02      |   感谢您的开源       |
|     J*s     |   ￥  10.00      |  微信     |       2021-05-19 17:33:10      |   感谢您的开源       |
|     *茶     |   ￥  10.00      |  微信     |       2021-05-17 20:46:05      |   感谢您的开源       |


`LinkWeChat` 全体开发团队感谢以下全部合作伙伴的服务器赞助（排名不分先后）：

|   公司/个人名称   |  服务器   | 
| :------: | :-----: | 
|   上海六感科技有限公司   | 八核 16G 服务器一台 |
|    Happy   | 两核 8G 服务器一台 |
|  平山阑槛倚晴空    | 两核 4G 服务器一台 |


#### 捐赠用途

患难与共，风雨同舟。

LinkWeChat 开源团队将目前所收项目捐赠全额捐出，仅尽绵薄之力，在此我们也感谢大家的捐赠和支持，希望河南尽早渡过难关。

同时也希望 LinkWeChat 不仅能为国内开源社区建设做一点贡献，也能为社会产生一些价值。

![输入图片说明](https://images.gitee.com/uploads/images/2021/0722/233536_67c02015_1480777.png "屏幕截图.png")

---

 **如果您觉得我们的开源项目很有帮助，请帮忙点击右上方的  :star: Star ，您的认可就是我们最大的动力，谢谢支持！:heart:** 