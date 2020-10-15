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

1、导入LinkWeChat到Eclipse，菜单 File -> Import，然后选择 Maven -> Existing Maven Projects，点击 Next> 按钮，选择工作目录，然后点击 Finish 按钮，即可成功导入Eclipse会自动加载Maven依赖包，初次加载会比较慢（根据自身网络情况而定）
2、创建数据库LW-vue并导入数据脚本
3、打开运行com.linkwechat. LinkWeChatApplication.java


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
编辑resources目录下的application-druid.yml
url: 服务器地址
username: 账号
password: 密码

2、开发环境配置
编辑resources目录下的application.yml
port: 端口
context-path: 部署路径


### 项目介绍

#### 文件结构

##### 后端结构

##### 前端结构

#### 配置文件 

#### 核心技术


- 前端采用Vue、Element UI。
- 后端采用Spring Boot、Spring Security、Redis & Jwt。
- 权限认证使用Jwt，支持多终端认证系统。
- 支持加载动态权限菜单，多方式轻松权限控制。
- 感谢[ruoyi-vue](https://gitee.com/y_project/RuoYi-Vue)提供后台框架



### 在线体验


1. 演示演示（暂未开放）：http://www.topitclub.cn/
2. 交流群

![输入图片说明](https://images.gitee.com/uploads/images/2020/0924/140420_fb631f6a_409467.png "屏幕截图.png")

### 开发进度

![输入图片说明](https://images.gitee.com/uploads/images/2020/1015/095236_c808865f_409467.png "屏幕截图.png")

### 部分演示图，持续更新
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092211_447d288e_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092227_74c199cf_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092322_df5e338b_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092306_99975664_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092338_503e44f7_409467.png "屏幕截图.png")
![输入图片说明](https://images.gitee.com/uploads/images/2020/1014/092358_e465cb54_409467.png "屏幕截图.png")





