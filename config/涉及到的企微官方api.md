

# SCRM应用到的企微官方 API

> v1.0.0

企业微信开放平台服务端 API。企业办公管理工具。与微信一致的沟通体验,丰富免费的OA应用,并与微信消息、小程序、微信支付等互通,助力企业高效办公和管理。

# 企业内部开发/通讯录管理/成员管理

## POST userid与openid互换-userid转openid

POST /cgi-bin/user/convert_to_openid

该接口使用场景为企业支付，在使用企业红包和向员工付款时，需要自行将企业微信的userid转成openid。

注：需要成员使用微信登录企业微信或者关注微信插件（原企业号）才能转成openid;
如果是外部联系人，请使用[外部联系人openid转换](https://developer.work.weixin.qq.com/document/path/90202#18820)转换openid

**权限说明：**
成员必须处于应用的可见范围内

> Body 请求参数

```json
{
  "userid": "zhangsan"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» userid|body|string| 是 |企业内的成员id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "openid": "oDjGHs-1yCnGrRovBj2yHij5JAAA"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» openid|string|false|none||企业微信成员userid对应的openid|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET 读取成员

GET /cgi-bin/user/get

权限说明：应用须拥有指定成员的查看权限。

### 应用获取敏感字段的说明

为保护企业数据与用户隐私，从6月20号20点开始，新创建的自建应用与代开发应用，调用该接口时，不再返回以下字段：头像、性别、手机、邮箱、企业邮箱、员工个人二维码、地址，应用需要通过[oauth2手工授权](https://developer.work.weixin.qq.com/document/path/90196#15232)的方式获取管理员与员工本人授权的字段。

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|userid|query|string| 是 |成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userid": "zhangsan",
  "name": "张三",
  "department": [
    1,
    2
  ],
  "order": [
    1,
    2
  ],
  "position": "后台工程师",
  "mobile": "13800000000",
  "gender": "1",
  "email": "zhangsan@gzdev.com",
  "biz_mail": "zhangsan@qyycs2.wecom.work",
  "is_leader_in_dept": [
    1,
    0
  ],
  "direct_leader": [
    "lisi",
    "wangwu"
  ],
  "avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
  "thumb_avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/100",
  "telephone": "020-123456",
  "alias": "jackzhang",
  "address": "广州市海珠区新港中路",
  "open_userid": "xxxxxx",
  "main_department": 1,
  "extattr": {
    "attrs": [
      {
        "type": 0,
        "name": "文本名称",
        "text": {
          "value": "文本"
        }
      },
      {
        "type": 1,
        "name": "网页名称",
        "web": {
          "url": "http://www.test.com",
          "title": "标题"
        }
      }
    ]
  },
  "status": 1,
  "qr_code": "https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=xxx",
  "external_position": "产品经理",
  "external_profile": {
    "external_corp_name": "企业简称",
    "wechat_channels": {
      "nickname": "视频号名称",
      "status": 1
    },
    "external_attr": [
      {
        "type": 0,
        "name": "文本名称",
        "text": {
          "value": "文本"
        }
      },
      {
        "type": 1,
        "name": "网页名称",
        "web": {
          "url": "http://www.test.com",
          "title": "标题"
        }
      },
      {
        "type": 2,
        "name": "测试app",
        "miniprogram": {
          "appid": "wx8bd80126147dFAKE",
          "pagepath": "/index",
          "title": "my miniprogram"
        }
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» userid|string|true|none||成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节；第三方应用返回的值为open_userid|
|» name|string|true|none||成员名称；第三方不可获取，调用时返回userid以代替name；代开发自建应用需要管理员授权才返回；对于非第三方创建的成员，第三方通讯录应用也不可获取；未返回name的情况需要通过[通讯录展示组件](https://developer.work.weixin.qq.com/document/path/90196#17172)来展示名字|
|» department|[integer]|true|none||成员所属部门id列表，仅返回该应用有查看权限的部门id；[成员授权模式](https://developer.work.weixin.qq.com/document/path/90196#30245)下，固定返回根部门id，即固定为1。对授权了“组织架构信息”权限的第三方应用，返回成员所属的全部部门id|
|» order|[integer]|true|none||部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)。[成员授权模式](https://developer.work.weixin.qq.com/document/path/90196#30245)下不返回该字段|
|» position|string|true|none||职务信息；代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» mobile|string|true|none||手机号码，代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» gender|string|true|none||性别。0表示未定义，1表示男性，2表示女性。代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段。注：不可获取指返回值0|
|» email|string|true|none||邮箱，代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» biz_mail|string|true|none||企业邮箱，代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» is_leader_in_dept|[integer]|true|none||表示在所在的部门内是否为部门负责人，数量与department一致；第三方通讯录应用或者授权了“组织架构信息-应用可获取企业的部门组织架构信息-部门负责人”权限的第三方应用可获取；对于非第三方创建的成员，第三方通讯录应用不可获取；上游企业不可获取下游企业成员该字段|
|» direct_leader|[string]|true|none||直属上级UserID，返回在应用可见范围内的直属上级列表，最多有五个直属上级；第三方通讯录应用或者授权了“组织架构信息-应用可获取可见范围内成员组织架构信息-直属上级”权限的第三方应用可获取；对于非第三方创建的成员，第三方通讯录应用不可获取；上游企业不可获取下游企业成员该字段；代开发自建应用不可获取该字段|
|» avatar|string|true|none||头像url。 代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» thumb_avatar|string|true|none||头像缩略图url。第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» telephone|string|true|none||座机。代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» alias|string|true|none||别名；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» address|string|true|none||地址。代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» open_userid|string|true|none||全局唯一。对于同一个服务商，不同应用获取到企业内同一个成员的open_userid是相同的，最多64个字节。仅第三方应用可获取|
|» main_department|integer|true|none||主部门，仅当应用对主部门有查看权限时返回。|
|» extattr|object|true|none||扩展属性，代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» attrs|[object]|true|none||none|
|»»» type|integer|true|none||none|
|»»» name|string|true|none||none|
|»»» text|object|false|none||none|
|»»»» value|string|true|none||none|
|»»» web|object|false|none||none|
|»»»» url|string|true|none||none|
|»»»» title|string|true|none||none|
|» status|integer|true|none||激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。 已激活代表已激活企业微信或已关注微信插件（原企业号）。未激活代表既未激活企业微信又未关注微信插件（原企业号）。|
|» qr_code|string|true|none||员工个人二维码，扫描可添加为外部联系人(注意返回的是一个url，可在浏览器上打开该url以展示二维码)；代开发自建应用需要[管理员授权且成员oauth2授权](https://developer.work.weixin.qq.com/document/path/90196#应用获取敏感字段的说明)获取；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» external_position|string|true|none||对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|» external_profile|object|true|none||成员对外属性，字段详情见[对外属性](https://developer.work.weixin.qq.com/document/path/90196#13450)；代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» external_corp_name|string|true|none||none|
|»» wechat_channels|object|true|none||none|
|»»» nickname|string|true|none||none|
|»»» status|integer|true|none||none|
|»» external_attr|[object]|true|none||none|
|»»» type|integer|true|none||none|
|»»» name|string|true|none||none|
|»»» text|object|false|none||none|
|»»»» value|string|true|none||none|
|»»» web|object|false|none||none|
|»»»» url|string|true|none||none|
|»»»» title|string|true|none||none|
|»»» miniprogram|object|false|none||none|
|»»»» appid|string|true|none||none|
|»»»» pagepath|string|true|none||none|
|»»»» title|string|true|none||none|

## POST 创建成员

POST /cgi-bin/user/create

权限说明：仅通讯录同步助手或第三方通讯录应用可调用。

> 注意，每个部门下的部门、成员总数不能超过3万个。建议保证创建department对应的部门和创建成员是串行化处理。

> Body 请求参数

```json
{
  "userid": "zhangsan",
  "name": "张三",
  "alias": "jackzhang",
  "mobile": "+86 13800000000",
  "department": [
    1,
    2
  ],
  "order": [
    10,
    40
  ],
  "position": "产品经理",
  "gender": "1",
  "email": "zhangsan@gzdev.com",
  "biz_mail": "zhangsan@qyycs2.wecom.work",
  "is_leader_in_dept": [
    1,
    0
  ],
  "direct_leader": [
    "lisi",
    "wangwu"
  ],
  "enable": 1,
  "avatar_mediaid": "2-G6nrLmr5EC3MNb_-zL1dDdzkd0p7cNliYu9V5w7o8K0",
  "telephone": "020-123456",
  "address": "广州市海珠区新港中路",
  "main_department": 1,
  "extattr": {
    "attrs": [
      {
        "type": 0,
        "name": "文本名称",
        "text": {
          "value": "文本"
        }
      },
      {
        "type": 1,
        "name": "网页名称",
        "web": {
          "url": "http://www.test.com",
          "title": "标题"
        }
      }
    ]
  },
  "to_invite": true,
  "external_position": "高级产品经理",
  "external_profile": {
    "external_corp_name": "企业简称",
    "wechat_channels": {
      "nickname": "视频号名称"
    },
    "external_attr": [
      {
        "type": 0,
        "name": "文本名称",
        "text": {
          "value": "文本"
        }
      },
      {
        "type": 1,
        "name": "网页名称",
        "web": {
          "url": "http://www.test.com",
          "title": "标题"
        }
      },
      {
        "type": 2,
        "name": "测试app",
        "miniprogram": {
          "appid": "wx8bd8012614784fake",
          "pagepath": "/index",
          "title": "my miniprogram"
        }
      }
    ]
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |none|
|body|body|object| 否 |none|
|» userid|body|string| 是 |成员UserID。对应管理端的帐号，企业内必须唯一。长度为1~64个字节。只能由数字、字母和“_-@.”四种字符组成，且第一个字符必须是数字或字母。系统进行唯一性检查时会忽略大小写。|
|» name|body|string| 是 |成员名称。长度为1~64个utf8字符|
|» alias|body|string| 否 |成员别名。长度1~64个utf8字符|
|» mobile|body|string| 否 |手机号码。企业内必须唯一，mobile/email二者不能同时为空|
|» department|body|[integer]| 是 |成员所属部门id列表，不超过100个|
|» order|body|[integer]| 否 |部门内的排序值，默认为0，成员次序以创建时间从小到大排列。个数必须和参数department的个数一致，数值越大排序越前面。有效的值范围是[0, 2^32)|
|» position|body|string| 否 |职务信息。长度为0~128个字符|
|» gender|body|string| 否 |性别。1表示男性，2表示女性|
|» email|body|string| 否 |邮箱。长度6~64个字节，且为有效的email格式。企业内必须唯一，mobile/email二者不能同时为空|
|» biz_mail|body|string| 否 |企业邮箱。仅对开通企业邮箱的企业有效。长度6~64个字节，且为有效的企业邮箱格式。企业内必须唯一。未填写则系统会为用户生成默认企业邮箱（由系统生成的邮箱可修改一次，2022年4月25日之后创建的成员需通过企业管理后台-协作-邮件-邮箱管理-成员邮箱修改）|
|» is_leader_in_dept|body|[integer]| 否 |个数必须和参数department的个数一致，表示在所在的部门内是否为部门负责人。1表示为部门负责人，0表示非部门负责人。在审批([自建](https://developer.work.weixin.qq.com/document/path/90195#17893)、[第三方](https://developer.work.weixin.qq.com/document/path/90195#18403))等应用里可以用来标识上级审批人|
|» direct_leader|body|[string]| 是 |直属上级UserID，设置范围为企业内成员，可以设置最多5个上级|
|» enable|body|integer| 否 |启用/禁用成员。1表示启用成员，0表示禁用成员|
|» avatar_mediaid|body|string| 否 |成员头像的mediaid，通过[素材管理](https://developer.work.weixin.qq.com/document/path/90195#90000/90135/91054)接口上传图片获得的mediaid|
|» telephone|body|string| 否 |座机。32字节以内，由纯数字、“-”、“+”或“,”组成。|
|» address|body|string| 否 |地址。长度最大128个字符|
|» main_department|body|integer| 否 |主部门|
|» extattr|body|object| 否 |自定义字段。自定义字段需要先在WEB管理端添加，见[扩展属性添加方法](https://developer.work.weixin.qq.com/document/path/90195#10016/扩展属性的添加方法)，否则忽略未知属性的赋值。|
|»» attrs|body|[object]| 是 |none|
|»»» type|body|integer| 是 |属性类型: 0-文本 1-网页 2-小程序|
|»»» name|body|string| 是 |属性名称： 需要先确保在管理端有创建该属性，否则会忽略|
|»»» text|body|object| 否 |文本类型的属性|
|»»»» value|body|string| 是 |文本属性内容，长度限制64个UTF8字符|
|»»» web|body|object| 否 |网页类型的属性，url和title字段要么同时为空表示清除该属性，要么同时不为空|
|»»»» url|body|string| 是 |网页的url,必须包含http或者https头|
|»»»» title|body|string| 是 |网页的展示标题,长度限制12个UTF8字符|
|» to_invite|body|boolean| 否 |是否邀请该成员使用企业微信（将通过微信服务通知或短信或邮件下发邀请，每天自动下发一次，最多持续3个工作日），默认值为true。|
|» external_position|body|string| 否 |对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。长度12个汉字内|
|» external_profile|body|object| 否 |成员对外属性，字段详情见[对外属性](https://developer.work.weixin.qq.com/document/path/90195#13450)|
|»» external_corp_name|body|string| 是 |none|
|»» wechat_channels|body|object| 是 |none|
|»»» nickname|body|string| 是 |none|
|»» external_attr|body|[object]| 是 |none|
|»»» type|body|integer| 是 |none|
|»»» name|body|string| 是 |none|
|»»» text|body|object| 否 |none|
|»»»» value|body|string| 是 |none|
|»»» web|body|object| 否 |none|
|»»»» url|body|string| 是 |none|
|»»»» title|body|string| 是 |none|
|»»» miniprogram|body|object| 否 |none|
|»»»» appid|body|string| 是 |none|
|»»»» pagepath|body|string| 是 |none|
|»»»» title|body|string| 是 |none|
|» nickname|body|string| 否 |视频号名字（设置后，成员将对外展示该视频号）。须从企业绑定到企业微信的视频号中选择，可在“我的企业”页中查看绑定的视频号|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "created"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET 二次验证

GET /cgi-bin/user/authsucc

​	

此接口可以满足安全性要求高的企业进行成员验证。开启二次验证后，当且仅当成员登录时，需跳转至企业自定义的页面进行验证。验证频率可在设置页面选择。
开启二次验证方法如下图：
![img](https://wework.qpic.cn/wwpic/653186_thzpIRm-Txa3p6a_1584677372/0)企业在开启二次验证时，必须在管理端填写企业二次验证页面的url。
当成员登录企业微信或关注微信插件（原企业号）进入企业时，会自动跳转到企业的验证页面。在跳转到企业的验证页面时，会带上如下参数：code=CODE。
企业收到code后，使用“通讯录同步助手”调用接口["根据code获取成员信息"](https://developer.work.weixin.qq.com/document/path/90203#15047)获取成员的userid。
如果成员是首次加入企业，企业获取到userid，并验证了成员信息后，调用如下接口即可让成员成功加入企业。

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|userid|query|string| 是 |成员UserID。对应管理端的帐号|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET  邮箱获取userid 

GET /cgi-bin/user/get_userid_by_email

通过邮箱获取其所对应的userid。
**权限说明：**

应用须拥有指定成员的查看权限。

> Body 请求参数

```json
{
  "email": "12345@qq.com",
  "email_type": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证，授权企业的token（通过[获取企业凭证](https://developer.work.weixin.qq.com/document/path/95895#14944)获取）或上游获取的下游企业的token（通过[获取下级/下游企业的access_token](https://developer.work.weixin.qq.com/document/path/95895#24919)获取）|
|body|body|object| 否 |none|
|» email|body|string| 是 |邮箱|
|» email_type|body|integer| 是 |邮箱类型：1-企业邮箱（默认）；2-个人邮箱|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userid": "zhangsan"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» userid|string|true|none||成员UserID。注意：已升级openid的代开发或第三方，获取的是密文userid|

## GET 获取部门成员

GET /cgi-bin/user/simplelist

企业通讯录安全特别重要，企业微信将持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取成员ID列表](https://developer.work.weixin.qq.com/document/path/90200#40412)」和「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/90200#36259)」接口获取userid和部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/90200#40802)。

**参数说明：**

> 如需获取该部门及其子部门的所有成员，需先获取该部门下的子部门，然后再获取子部门下的部门成员，逐层递归获取。

**权限说明：**

应用须拥有指定部门的查看权限。

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|department_id|query|string| 是 |获取的部门id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userlist": [
    {
      "userid": "zhangsan",
      "name": "张三",
      "department": [
        1,
        2
      ],
      "open_userid": "xxxxxx"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» userlist|[object]|true|none||成员列表|
|»» userid|string|false|none||成员UserID。对应管理端的帐号|
|»» name|string|false|none||成员名称，代开发自建应用需要管理员授权才返回；此字段从2019年12月30日起，对新创建第三方应用不再返回真实name，使用userid代替name，2020年6月30日起，对所有历史第三方应用不再返回真实name，使用userid代替name，后续第三方仅通讯录应用可获取，未返回名称的情况需要通过[通讯录展示组件](https://developer.work.weixin.qq.com/document/path/90200#17172)来展示名字|
|»» department|[integer]|false|none||成员所属部门列表。列表项为部门ID，32位整型|
|»» open_userid|string|false|none||全局唯一。对于同一个服务商，不同应用获取到企业内同一个成员的open_userid是相同的，最多64个字节。仅第三方应用可获取|

## GET 删除成员

GET /cgi-bin/user/delete

权限说明：仅通讯录同步助手或第三方通讯录应用可调用。 若是绑定了腾讯企业邮，则会同时删除邮箱帐号。

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|userid|query|string| 是 |成员UserID。对应管理端的帐号|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "deleted"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET 获取部门成员详情

GET /cgi-bin/user/list

应用只能获取可见范围内的成员信息，且每种应用获取的字段有所不同，在返回结果说明中会逐个说明。企业通讯录安全特别重要，企业微信持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- 从2022年6月20号20点开始，除通讯录同步以外的基础应用（如客户联系、微信客服、会话存档、日程等），以及新创建的自建应用与代开发应用，调用该接口时，不再返回以下字段：头像、性别、手机、邮箱、企业邮箱、员工个人二维码、地址，应用需要通过[oauth2手工授权](https://developer.work.weixin.qq.com/document/path/90201#15232)的方式获取管理员与员工本人授权的字段。

  

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取成员ID列表](https://developer.work.weixin.qq.com/document/path/90201#40412)」和「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/90201#36259)」接口获取userid和部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/90201#40802)。

**参数说明：**

> 如需获取该部门及其子部门的所有成员，需先获取该部门下的子部门，然后再获取子部门下的部门成员，逐层递归获取。

**权限说明：**

应用须拥有指定部门的查看权限。

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|fetch_child|query|string| 否 |1/0：是否递归获取子部门下面的成员|
|department_id|query|string| 是 |获取的部门id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userlist": [
    {
      "userid": "zhangsan",
      "name": "李四",
      "department": [
        1,
        2
      ],
      "order": [
        1,
        2
      ],
      "position": "后台工程师",
      "mobile": "13800000000",
      "gender": "1",
      "email": "zhangsan@gzdev.com",
      "biz_mail": "zhangsan@qyycs2.wecom.work",
      "is_leader_in_dept": [
        1,
        0
      ],
      "direct_leader": [
        "lisi",
        "wangwu"
      ],
      "avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
      "thumb_avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/100",
      "telephone": "020-123456",
      "alias": "jackzhang",
      "status": 1,
      "address": "广州市海珠区新港中路",
      "english_name": "jacky",
      "open_userid": "xxxxxx",
      "main_department": 1,
      "extattr": {
        "attrs": [
          {
            "type": 0,
            "name": "文本名称",
            "text": {
              "value": "文本"
            }
          },
          {
            "type": 1,
            "name": "网页名称",
            "web": {
              "url": "http://www.test.com",
              "title": "标题"
            }
          }
        ]
      },
      "qr_code": "https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=xxx",
      "external_position": "产品经理",
      "external_profile": {
        "external_corp_name": "企业简称",
        "wechat_channels": {
          "nickname": "视频号名称",
          "status": 1
        },
        "external_attr": [
          {
            "type": 0,
            "name": "文本名称",
            "text": {
              "value": "文本"
            }
          },
          {
            "type": 1,
            "name": "网页名称",
            "web": {
              "url": "http://www.test.com",
              "title": "标题"
            }
          },
          {
            "type": 2,
            "name": "测试app",
            "miniprogram": {
              "appid": "wx8bd80126147dFAKE",
              "pagepath": "/index",
              "title": "miniprogram"
            }
          }
        ]
      }
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» userlist|[object]|true|none||成员列表|
|»» userid|string|false|none||成员UserID。对应管理端的帐号|
|»» name|string|false|none||成员名称；第三方不可获取，调用时返回userid以代替name；代开发自建应用需要管理员授权才返回；对于非第三方创建的成员，第三方通讯录应用也不可获取；未返回名称的情况需要通过[通讯录展示组件](https://developer.work.weixin.qq.com/document/path/90201#17172)来展示名字|
|»» department|[integer]|false|none||成员所属部门id列表，仅返回该应用有查看权限的部门id。对授权了“组织架构信息”的第三方应用，返回成员所属的全部部门id列表|
|»» order|[integer]|false|none||部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)|
|»» position|string|false|none||职务信息；代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» mobile|string|false|none||手机号码，代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» gender|string|false|none||性别。0表示未定义，1表示男性，2表示女性。第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段。注：不可获取指返回值为0|
|»» email|string|false|none||邮箱，代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» biz_mail|string|false|none||企业邮箱，代开发自建应用不返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» is_leader_in_dept|[integer]|false|none||表示在所在的部门内是否为部门负责人。0-否；1-是。是一个列表，数量必须与department一致。第三方通讯录应用或者授权了“组织架构信息-应用可获取企业的部门组织架构信息-部门负责人”权限的第三方应用可获取；对于非第三方创建的成员，第三方通讯录应用不可获取；上游企业不可获取下游企业成员该字段|
|»» direct_leader|[string]|false|none||直属上级UserID，返回在应用可见范围内的直属上级列表，最多有五个直属上级；第三方通讯录应用或者授权了“组织架构信息-应用可获取可见范围内成员组织架构信息-直属上级”权限的第三方应用可获取；对于非第三方创建的成员，第三方通讯录应用不可获取；上游企业不可获取下游企业成员该字段；代开发自建应用不可获取该字段|
|»» avatar|string|false|none||头像url。 第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» thumb_avatar|string|false|none||头像缩略图url。第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» telephone|string|false|none||座机。代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» alias|string|false|none||别名；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» status|integer|false|none||激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。 已激活代表已激活企业微信或已关注微信插件（原企业号）。未激活代表既未激活企业微信又未关注微信插件（原企业号）。|
|»» address|string|false|none||地址。代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» english_name|string|false|none||none|
|»» open_userid|string|false|none||全局唯一。对于同一个服务商，不同应用获取到企业内同一个成员的open_userid是相同的，最多64个字节。仅第三方应用可获取|
|»» main_department|integer|false|none||主部门，仅当应用对主部门有查看权限时返回。|
|»» extattr|object|false|none||扩展属性，代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»»» attrs|[object]|true|none||none|
|»»»» type|integer|true|none||none|
|»»»» name|string|true|none||none|
|»»»» text|object|false|none||none|
|»»»»» value|string|true|none||none|
|»»»» web|object|false|none||none|
|»»»»» url|string|true|none||none|
|»»»»» title|string|true|none||none|
|»» qr_code|string|false|none||员工个人二维码，扫描可添加为外部联系人(注意返回的是一个url，可在浏览器上打开该url以展示二维码)；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» external_position|string|false|none||对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»» external_profile|object|false|none||成员对外属性，字段详情见[对外属性](https://developer.work.weixin.qq.com/document/path/90201#13450)；代开发自建应用需要管理员授权才返回；第三方仅通讯录应用可获取；对于非第三方创建的成员，第三方通讯录应用也不可获取；上游企业不可获取下游企业成员该字段|
|»»» external_corp_name|string|true|none||none|
|»»» wechat_channels|object|true|none||none|
|»»»» nickname|string|true|none||none|
|»»»» status|integer|true|none||none|
|»»» external_attr|[object]|true|none||none|
|»»»» type|integer|true|none||none|
|»»»» name|string|true|none||none|
|»»»» text|object|false|none||none|
|»»»»» value|string|true|none||none|
|»»»» web|object|false|none||none|
|»»»»» url|string|true|none||none|
|»»»»» title|string|true|none||none|
|»»»» miniprogram|object|false|none||none|
|»»»»» appid|string|true|none||none|
|»»»»» pagepath|string|true|none||none|
|»»»»» title|string|true|none||none|

## POST 邀请成员

POST /cgi-bin/batch/invite

企业可通过接口批量邀请成员使用企业微信，邀请后将通过短信或邮件下发通知。

权限说明：
须拥有指定成员、部门或标签的查看权限。
第三方仅通讯录应用可调用。

更多说明：
user, party, tag三者不能同时为空；
如果部分接收人无权限或不存在，邀请仍然执行，但会返回无效的部分（即invaliduser或invalidparty或invalidtag）;
同一用户只须邀请一次，被邀请的用户如果未安装企业微信，在3天内每天会收到一次通知，最多持续3天。
因为邀请频率是异步检查的，所以调用接口返回成功，并不代表接收者一定能收到邀请消息（可能受上述频率限制无法接收）。

 

> Body 请求参数

```json
"{\"user\":[\"UserID1\",\"UserID2\",\"UserID3\"],\"party\":[PartyID1,PartyID2],\"tag\":[TagID1,TagID2]}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» access_token|body|[string]| 是 |调用接口凭证|
|» tag|body|[string]| 否 |标签ID列表，最多支持100个。|
|» user|body|[string]| 否 |成员ID列表, 最多支持1000个。|
|» party|body|[string]| 否 |部门ID列表，最多支持100个。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "invaliduser": [
    "UserID1",
    "UserID2"
  ],
  "invalidparty": [
    "PartyID1",
    "PartyID2"
  ],
  "invalidtag": [
    "TagID1",
    "TagID2"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» invaliduser|[string]|true|none||非法成员列表|
|» invalidparty|[string]|true|none||非法部门列表|
|» invalidtag|[string]|true|none||非法标签列表|

## GET 获取加入企业二维码

GET /cgi-bin/corp/get_join_qrcode

支持企业用户获取实时成员加入二维码。

权限说明：
须拥有通讯录的管理权限，使用通讯录同步的Secret。

 

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|size_type|query|string| 否 |qrcode尺寸类型，1: 171 x 171; 2: 399 x 399; 3: 741 x 741; 4: 2052 x 2052|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "join_qrcode": "https://work.weixin.qq.com/wework_admin/genqrcode?action=join&amp;vcode=3db1fab03118ae2aa1544cb9abe84&amp;r=hb_share_api_mjoin&amp;qr_size=3"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» join_qrcode|string|false|none||二维码链接，有效期7天|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET  获取成员ID列表 

GET /cgi-bin/user/list_id

获取企业成员的userid与对应的部门ID列表，预计于2022年8月8号发布。若需要获取其他字段，参见「[适配建议](https://developer.work.weixin.qq.com/document/path/96067#适配建议：)」。

#### **适配建议：**

为保障企业数据安全，应用应当尽量减少通过服务端接口获取通讯录信息，尤其是成员的敏感字段。

若需要获取员工基本信息（姓名、部门名）

- 企业：企业自建应用可通过「[读取成员](https://developer.work.weixin.qq.com/document/path/96067#10019)」等通讯录接口获取。

- 服务商：代开发应用经企业管理员授权后可通过「[读取成员](https://developer.work.weixin.qq.com/document/path/96067#10019)」等通讯录接口获取。第三方应用不可直接获取姓名和部门名，可以通过“[通讯录展示组件](https://developer.work.weixin.qq.com/document/path/96067#17172)”在页面内展示姓名和部门名，完成应用功能。

  

若需要获取员工敏感信息（手机号、邮箱）

- 企业：
  企业在6月20日前创建的自建应用可通过「[读取成员](https://developer.work.weixin.qq.com/document/path/96067#10019)」等通讯录接口获取。
  企业在6月20日后创建的自建应用经员工[自主授权敏感信息](https://developer.work.weixin.qq.com/document/path/96067#15232)后获取。

  

- 服务商：代开发应用经企业管理员授权且[员工自主授权敏感信息](https://developer.work.weixin.qq.com/document/path/96067#15232)后可获取。第三方应用不可获取员工的手机号和邮箱。

> Body 请求参数

```json
{
  "cursor": "xxxxxxx",
  "limit": 10000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 是 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用不填|
|» limit|body|integer| 是 |分页，预期请求的数据量，取值范围 1 ~ 10000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "aaaaaaaaa",
  "dept_user": [
    {
      "userid": "zhangsan",
      "department": 1
    },
    {
      "userid": "zhangsan",
      "department": 2
    },
    {
      "userid": "lisi",
      "department": 2
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» next_cursor|string|true|none||分页游标，下次请求时填写以获取之后分页的记录。如果该字段返回空则表示已没有更多数据|
|» dept_user|[object]|true|none||用户-部门关系列表|
|»» userid|string|true|none||用户userid，当用户在多个部门下时会有多条记录|
|»» department|integer|true|none||用户所属部门|

## POST 手机号获取userid

POST /cgi-bin/user/getuserid

通过手机号获取其所对应的userid。
权限说明：应用须拥有指定成员的查看权限。
更多说明：请确保手机号的正确性，若出错的次数较多，会导致1天不可调用。

> Body 请求参数

```json
{
  "mobile": "13430388888"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» mobile|body|string| 是 |手机号码。长度为5~32个字节|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userid": "zhangsan"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» userid|string|false|none||成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节|

## POST 批量删除成员

POST /cgi-bin/user/batchdelete

权限说明：仅通讯录同步助手或第三方通讯录应用可调用。

> Body 请求参数

```json
{
  "useridlist": [
    "zhangsan",
    "lisi"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» useridlist|body|[string]| 是 |成员UserID列表。对应管理端的帐号。最多支持200个。若存在无效UserID，直接返回错误|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "deleted"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 更新成员

POST /cgi-bin/user/update

> 特别地，如果userid由系统自动生成，**则仅允许修改一次**。新值可由new_userid字段指定。

> 如果创建时『企业邮箱』为系统默认分配的，**则仅允许修改一次**，若创建时填入了合规的『企业邮箱』，**则无法修改**

 

**权限说明：**

仅通讯录同步助手或第三方通讯录应用可调用。

> 注意，每个部门下的部门、成员总数不能超过3万个。

> Body 请求参数

```json
"{\r\n\t\"userid\": \"zhangsan\",\r\n\t\"name\": \"李四\",\r\n\t\"department\": [1],\r\n\t\"order\": [10],\r\n\t\"position\": \"后台工程师\",\r\n\t\"mobile\": \"13800000000\",\r\n\t\"gender\": \"1\",\r\n\t\"email\": \"zhangsan@gzdev.com\",\r\n\t\"biz_mail\":\"zhangsan@qyycs2.wecom.work\",\r\n\t\"is_leader_in_dept\": [1],\r\n\t\"direct_leader\":[\"lisi\",\"wangwu\"],\r\n\t\"enable\": 1,\r\n\t\"avatar_mediaid\": \"2-G6nrLmr5EC3MNb_-zL1dDdzkd0p7cNliYu9V5w7o8K0\",\r\n\t\"telephone\": \"020-123456\",\r\n\t\"alias\": \"jackzhang\",\r\n\t\"address\": \"广州市海珠区新港中路\",\r\n\t\"main_department\": 1,\r\n\t\"extattr\": {\r\n\t\t\"attrs\": [\r\n\t\t\t{\r\n\t\t\t\t\"type\": 0,\r\n\t\t\t\t\"name\": \"文本名称\",\r\n\t\t\t\t\"text\": {\r\n\t\t\t\t\t\"value\": \"文本\"\r\n\t\t\t\t}\r\n\t\t\t},\r\n\t\t\t{\r\n\t\t\t\t\"type\": 1,\r\n\t\t\t\t\"name\": \"网页名称\",\r\n\t\t\t\t\"web\": {\r\n\t\t\t\t\t\"url\": \"http://www.test.com\",\r\n\t\t\t\t\t\"title\": \"标题\"\r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t]\r\n\t},\r\n\t\"external_position\": \"工程师\",\r\n\t\"external_profile\": {\r\n\t\t\"external_corp_name\": \"企业简称\",\r\n\t\t\"wechat_channels\": {\r\n\t\t\t\"nickname\": \"视频号名称\",\r\n\t\t},\r\n\t\t\"external_attr\": [\r\n\t\t\t{\r\n\t\t\t\t\"type\": 0,\r\n\t\t\t\t\"name\": \"文本名称\",\r\n\t\t\t\t\"text\": {\r\n\t\t\t\t\t\"value\": \"文本\"\r\n\t\t\t\t}\r\n\t\t\t},\r\n\t\t\t{\r\n\t\t\t\t\"type\": 1,\r\n\t\t\t\t\"name\": \"网页名称\",\r\n\t\t\t\t\"web\": {\r\n\t\t\t\t\t\"url\": \"http://www.test.com\",\r\n\t\t\t\t\t\"title\": \"标题\"\r\n\t\t\t\t}\r\n\t\t\t},\r\n\t\t\t{\r\n\t\t\t\t\"type\": 2,\r\n\t\t\t\t\"name\": \"测试app\",\r\n\t\t\t\t\"miniprogram\": {\r\n\t\t\t\t\t\"appid\": \"wx8bd80126147dFAKE\",\r\n\t\t\t\t\t\"pagepath\": \"/index\",\r\n\t\t\t\t\t\"title\": \"my miniprogram\"\r\n\t\t\t\t}\r\n\t\t\t}\r\n\t\t]\r\n\t}\r\n}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» userid|body|string| 是 |成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节|
|» name|body|string| 否 |成员名称。长度为1~64个utf8字符|
|» department|body|[integer]| 否 |成员所属部门id列表，不超过100个|
|» order|body|[integer]| 否 |部门内的排序值，默认为0。当有传入department时有效。数量必须和department一致，数值越大排序越前面。有效的值范围是[0, 2^32)|
|» position|body|string| 否 |职务信息。长度为0~128个字符|
|» mobile|body|string| 否 |手机号码。企业内必须唯一。若成员已激活企业微信，则需成员自行修改（此情况下该参数被忽略，但不会报错）|
|» gender|body|string| 否 |性别。1表示男性，2表示女性|
|» email|body|string| 否 |邮箱。长度不超过64个字节，且为有效的email格式。企业内必须唯一。若是绑定了腾讯企业邮箱的企业微信，则需要在腾讯企业邮箱中修改邮箱（此情况下该参数被忽略，但不会报错）|
|» biz_mail|body|string| 否 |企业邮箱。仅对开通企业邮箱的企业有效。长度6~64个字节，且为有效的企业邮箱格式。企业内必须唯一。企业邮箱为系统自动生成的成员可修改一次，仅2022年4月25日以前创建的成员可通过此接口修改，之后创建的成员需通过企业管理后台-协作-邮件-邮箱管理-成员邮箱修改|
|» is_leader_in_dept|body|[integer]| 否 |部门负责人字段，个数必须和department一致，表示在所在的部门内是否为负责人。0-否，1-是|
|» direct_leader|body|[string]| 否 |直属上级，可以设置企业范围内成员为直属上级，最多设置5个|
|» enable|body|integer| 否 |启用/禁用成员。1表示启用成员，0表示禁用成员|
|» avatar_mediaid|body|string| 否 |成员头像的mediaid，通过[素材管理](https://developer.work.weixin.qq.com/document/path/90197#10112)接口上传图片获得的mediaid|
|» telephone|body|string| 否 |座机。由1-32位的纯数字、“-”、“+”或“,”组成|
|» alias|body|string| 否 |别名。长度为1-64个utf8字符|
|» address|body|string| 否 |地址。长度最大128个字符|
|» main_department|body|integer| 否 |主部门|
|» extattr|body|object| 否 |自定义字段。自定义字段需要先在WEB管理端添加，见[扩展属性添加方法](https://developer.work.weixin.qq.com/document/path/90197#10016/扩展属性的添加方法)，否则忽略未知属性的赋值。|
|»» attrs|body|[object]| 是 |none|
|»»» type|body|integer| 是 |属性类型: 0-文本 1-网页 2-小程序|
|»»» name|body|string| 是 |属性名称： 需要先确保在管理端有创建该属性，否则会忽略|
|»»» text|body|object| 否 |文本类型的属性|
|»»»» value|body|string| 是 |文本属性内容,长度限制64个UTF8字符|
|»»» web|body|object| 否 |网页类型的属性，url和title字段要么同时为空表示清除该属性，要么同时不为空|
|»»»» url|body|string| 是 |网页的url,必须包含http或者https头|
|»»»» title|body|string| 是 |网页的展示标题,长度限制12个UTF8字符|
|» external_position|body|string| 否 |对外职务，如果设置了该值，则以此作为对外展示的职务，否则以position来展示。不超过12个汉字|
|» external_profile|body|object| 否 |成员对外属性，字段详情见[对外属性](https://developer.work.weixin.qq.com/document/path/90197#13450)|
|»» external_corp_name|body|string| 是 |none|
|»» wechat_channels|body|object| 是 |none|
|»»» nickname|body|string| 是 |none|
|»» external_attr|body|[object]| 是 |none|
|»»» type|body|integer| 是 |none|
|»»» name|body|string| 是 |none|
|»»» text|body|object| 否 |none|
|»»»» value|body|string| 是 |none|
|»»» web|body|object| 否 |none|
|»»»» url|body|string| 是 |none|
|»»»» title|body|string| 是 |none|
|»»» miniprogram|body|object| 否 |none|
|»»»» appid|body|string| 是 |none|
|»»»» pagepath|body|string| 是 |none|
|»»»» title|body|string| 是 |none|
|» nickname|body|string| 否 |视频号名字（设置后，成员将对外展示该视频号）。须从企业绑定到企业微信的视频号中选择，可在“我的企业”页中查看绑定的视频号|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "updated"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST userid与openid互换-openid转userid

POST /cgi-bin/user/convert_to_userid

该接口主要应用于使用企业支付之后的结果查询。
开发者需要知道某个结果事件的openid对应企业微信内成员的信息时，可以通过调用该接口进行转换查询。

> Body 请求参数

```json
{
  "openid": "oDjGHs-1yCnGrRovBj2yHij5JAAA"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» openid|body|string| 是 |在使用企业支付之后，返回结果的openid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userid": "zhangsan"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» userid|string|false|none||该openid在企业微信对应的成员userid|

# 企业内部开发/通讯录管理/部门管理

## GET 删除部门

GET /cgi-bin/department/delete

权限说明：应用须拥有指定部门的管理权限。 第三方仅通讯录应用可以调用。

文档ID: 10079
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90207
https://open.work.weixin.qq.com/api/doc/90001/90143/90343
https://open.work.weixin.qq.com/api/doc/90002/90151/90826

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|id|query|string| 是 |部门id。（注：不能删除根部门；不能删除含有子部门、成员的部门）|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "deleted"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET 获取单个部门详情

GET /cgi-bin/department/get

企业通讯录安全特别重要，企业微信将持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/95351#36259)」接口获取部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/95351#40802)。

**权限说明：**

| 应用类型         | 权限说明                                                     |
| ---------------- | ------------------------------------------------------------ |
| 第三方普通应用   | 若企业授权了组织架构信息权限，可获取企业所有部门ID、部门负责人、父部门ID; 若未授权组织架构信息权限，只能拉取token对应的应用的可见范围内部门详情 |
| 第三方通讯录应用 | 可获取企业所有部门详情，部门名字除外                         |
| 代开发自建应用   | 只能拉取token对应的应用的权限范围内的部门详情                |
| 普通自建应用     | 只能拉取token对应的应用的权限范围内的部门详情                |
| 通讯录同步助手   | 可获取企业所有部门详情                                       |

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|id|query|string| 否 |部门id。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "department": {
    "id": 2,
    "name": "广州研发中心",
    "name_en": "RDGZ",
    "department_leader": [
      "zhangsan",
      "lisi"
    ],
    "parentid": 1,
    "order": 10
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» department|object|true|none||部门详情。|
|»» id|integer|true|none||部门id|
|»» name|string|true|none||部门名称，代开发自建应用需要管理员授权才返回；第三方不可获取，需要通过[通讯录展示组件](https://developer.work.weixin.qq.com/document/path/95351#17172)来展示部门名称|
|»» name_en|string|true|none||部门英文名称，代开发自建应用需要管理员授权才返回；第三方不可获取，需要通过[通讯录展示组件](https://developer.work.weixin.qq.com/document/path/95351#17172)来展示部门名称|
|»» department_leader|[string]|true|none||部门负责人的UserID，返回在应用可见范围内的部门负责人列表；第三方仅通讯录应用或者授权了“组织架构信息-应用可获取企业的部门组织架构信息-部门负责人”的第三方应用可获取|
|»» parentid|integer|true|none||父部门id。根部门为1。|
|»» order|integer|true|none||在父部门中的次序值。order值大的排序靠前。值范围是[0, 2^32)|

## POST 更新部门

POST /cgi-bin/department/update

权限说明 ：
应用须拥有指定部门的管理权限。如若要移动部门，需要有新父部门的管理权限。
第三方仅通讯录应用可以调用。

注意，部门的最大层级为15层；部门总数不能超过3万个；每个部门下的节点不能超过3万个。

文档ID: 10077
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90206
https://open.work.weixin.qq.com/api/doc/90001/90143/90342
https://open.work.weixin.qq.com/api/doc/90002/90151/90825

> Body 请求参数

```json
{
  "id": 2,
  "name": "广州研发中心",
  "name_en": "RDGZ",
  "parentid": 1,
  "order": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 否 |部门名称。长度限制为1~32个字符，字符不能包括\:*?”<>｜|
|» id|body|integer(int32)| 是 |部门id|
|» parentid|body|integer(int32)| 否 |父部门id|
|» name_en|body|string| 否 |英文名称，需要在管理后台开启多语言支持才能生效。长度限制为1~32个字符，字符不能包括\:*?”<>｜|
|» order|body|integer(int32)| 否 |在父部门中的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "updated"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 创建部门

POST /cgi-bin/department/create

第三方仅通讯录应用可以调用。

注意，部门的最大层级为15层；部门总数不能超过3万个；每个部门下的节点不能超过3万个。建议保证创建的部门和对应部门成员是串行化处理。

文档ID: 10076
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90205
https://open.work.weixin.qq.com/api/doc/90001/90143/90341
https://open.work.weixin.qq.com/api/doc/90002/90151/90824

> Body 请求参数

```json
{
  "name": "广州研发中心",
  "name_en": "RDGZ",
  "parentid": 1,
  "order": 1,
  "id": 2
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 是 |部门名称。同一个层级的部门名称不能重复。长度限制为1~32个字符，字符不能包括\:*?”<>｜|
|» id|body|integer(int32)| 否 |部门id，32位整型，指定时必须大于1。若不填该参数，将自动生成id|
|» parentid|body|integer(int32)| 是 |父部门id，32位整型|
|» name_en|body|string| 否 |英文名称。同一个层级的部门名称不能重复。需要在管理后台开启多语言支持才能生效。长度限制为1~32个字符，字符不能包括\:*?”<>｜|
|» order|body|integer(int32)| 否 |在父部门中的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "created",
  "id": 2
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» id|integer(int32)|false|none||创建的部门id|

## GET 获取子部门ID列表

GET /cgi-bin/department/simplelist

**权限说明：**

| 应用类型         | 权限说明                                                     |
| ---------------- | ------------------------------------------------------------ |
| 第三方普通应用   | 若企业授权了“组织架构信息”权限，可获取企业所有部门id; 若未授权“组织架构信息”权限，只能拉取token对应的应用的权限范围内的部门列表 |
| 第三方通讯录应用 | 可获取企业所有部门id                                         |
| 代开发自建应用   | 只能拉取token对应的应用的权限范围内的部门列表                |
| 普通自建应用     | 只能拉取token对应的应用的权限范围内的部门列表                |
| 通讯录同步助手   | 可获取企业所有部门id                                         |

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|id|query|string| 否 |部门id。获取指定部门及其下的子部门（以及子部门的子部门等等，递归）。 如果不填，默认获取全量组织架构|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "department_id": [
    {
      "id": 2,
      "parentid": 1,
      "order": 10
    },
    {
      "id": 3,
      "parentid": 2,
      "order": 40
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» department_id|[object]|true|none||部门列表数据。|
|»» id|integer|true|none||创建的部门id|
|»» parentid|integer|true|none||父部门id。根部门为1。|
|»» order|integer|true|none||在父部门中的次序值。order值大的排序靠前。值范围是[0, 2^32)。|

## GET 获取部门列表

GET /cgi-bin/department/list

​	

企业通讯录安全特别重要，企业微信将持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/90208#36259)」接口获取部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/90208#40802)。

由于该接口性能较低，建议换用[获取子部门ID列表](https://developer.work.weixin.qq.com/document/path/90208#36259)与[获取单个部门详情](https://developer.work.weixin.qq.com/document/path/90208#36260)。

**权限说明：**

只能拉取token对应的应用的权限范围内的部门列表

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|id|query|string| 是 |	部门id。获取指定部门及其下的子部门（以及及子部门的子部门等等，递归）。 如果不填，默认获取全量组织架构|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "department": [
    {
      "id": 2,
      "name": "广州研发中心",
      "name_en": "RDGZ",
      "parentid": 1,
      "order": 10
    },
    {
      "id": 3,
      "name": "邮箱产品部",
      "name_en": "mail",
      "parentid": 2,
      "order": 40
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» department|[object]|false|none||部门列表数据。|
|»» id|integer|true|none||创建的部门id|
|»» name|string|true|none||部门名称，代开发自建应用需要管理员授权才返回；此字段从2019年12月30日起，对新创建第三方应用不再返回，2020年6月30日起，对所有历史第三方应用不再返回name，返回的name字段使用id代替，后续第三方仅通讯录应用可获取，未返回名称的情况需要通过通讯录展示组件来展示部门名称|
|»» name_en|string|true|none||英文名称，此字段从2019年12月30日起，对新创建第三方应用不再返回，2020年6月30日起，对所有历史第三方应用不再返回该字段|
|»» parentid|integer|true|none||父部门id。根部门为1|
|»» order|integer|true|none||在父部门中的次序值。order值大的排序靠前。值范围是[0, 2^32)|

# 企业内部开发/通讯录管理/标签管理

## POST 创建标签

POST /cgi-bin/tag/create

权限说明：创建的标签属于该应用，只有该应用的secret才可以增删成员。
注意，标签总数不能超过3000个。

文档ID: 10915
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90210
https://open.work.weixin.qq.com/api/doc/90001/90143/90346
https://open.work.weixin.qq.com/api/doc/90002/90151/90829

> Body 请求参数

```json
{
  "tagname": "UI",
  "tagid": 12
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» tagname|body|string| 是 |标签名称，长度限制为32个字以内（汉字或英文字母），标签名不可与其他标签重名。|
|» tagid|body|integer(int32)| 否 |标签id，非负整型，指定此参数时新增的标签会生成对应的标签id，不指定时则以目前最大的id自增。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "created",
  "tagid": 12
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» tagid|integer(int32)|false|none||标签id|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 增加标签成员

POST /cgi-bin/tag/addtagusers

权限说明：调用的应用必须是指定标签的创建者；成员属于应用的可见范围。
注意，每个标签下部门数和人员数总和不能超过3万个。
文档ID: 10923
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90214
https://open.work.weixin.qq.com/api/doc/90001/90143/90350
https://open.work.weixin.qq.com/api/doc/90002/90151/90833

> Body 请求参数

```json
{
  "tagid": 12,
  "userlist": [
    "user1",
    "user2"
  ],
  "partylist": [
    4
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |none|
|body|body|object| 否 |none|
|» userlist|body|[string]| 否 |企业成员ID列表，注意：userlist、partylist不能同时为空，单次请求个数不超过1000|
|» partylist|body|[integer]| 否 |企业部门ID列表，注意：userlist、partylist不能同时为空，单次请求个数不超过100|
|» tagid|body|integer(int32)| 是 |标签ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "invalidlist": "usr1|usr2|usr",
  "invalidparty": [
    2,
    4
  ]
}
```

```json
{
  "errcode": 40070,
  "errmsg": "all list invalid "
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» invalidlist|string|false|none||非法的成员帐号列表|
|» invalidparty|[integer]|false|none||非法的部门id列表|

## GET 删除标签

GET /cgi-bin/tag/delete

权限说明：调用的应用必须是指定标签的创建者。

文档ID: 10920
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90212
https://open.work.weixin.qq.com/api/doc/90001/90143/90348
https://open.work.weixin.qq.com/api/doc/90002/90151/90831

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|tagid|query|string| 是 |标签ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "deleted"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 更新标签名字

POST /cgi-bin/tag/update

权限说明：调用的应用必须是指定标签的创建者。

文档ID: 10919
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90211
https://open.work.weixin.qq.com/api/doc/90001/90143/90347
https://open.work.weixin.qq.com/api/doc/90002/90151/90830

> Body 请求参数

```json
{
  "tagid": 12,
  "tagname": "UIdesign"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» tagname|body|string| 是 |标签名称，长度限制为32个字（汉字或英文字母），标签不可与其他标签重名。|
|» tagid|body|integer(int32)| 是 |标签ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "updated"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 删除标签成员

POST /cgi-bin/tag/deltagusers

权限说明：调用的应用必须是指定标签的创建者；成员属于应用的可见范围。

文档ID: 10925
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90215
https://open.work.weixin.qq.com/api/doc/90001/90143/90351
https://open.work.weixin.qq.com/api/doc/90002/90151/90834

> Body 请求参数

```json
{
  "tagid": 12,
  "userlist": [
    "user1",
    "user2"
  ],
  "partylist": [
    2,
    4
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |none|
|body|body|object| 否 |none|
|» userlist|body|[string]| 否 |企业成员ID列表，注意：userlist、partylist不能同时为空，单次请求长度不超过1000|
|» partylist|body|[integer]| 否 |企业部门ID列表，注意：userlist、partylist不能同时为空，单次请求长度不超过100|
|» tagid|body|integer(int32)| 是 |标签ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "deleted"
}
```

```json
{
  "errcode": 0,
  "errmsg": "deleted",
  "invalidlist": "usr1|usr2|usr",
  "invalidparty": [
    2,
    4
  ]
}
```

```json
{
  "errcode": 40031,
  "errmsg": "all list invalid"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» invalidlist|string|false|none||非法的成员帐号列表|
|» invalidparty|[integer]|false|none||非法的部门id列表|

## GET 获取标签列表

GET /cgi-bin/tag/list

权限说明：自建应用或通讯同步助手可以获取所有标签列表；第三方应用仅可获取自己创建的标签。

文档ID: 10926
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90216
https://open.work.weixin.qq.com/api/doc/90001/90143/90352
https://open.work.weixin.qq.com/api/doc/90002/90151/90835

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "taglist": [
    {
      "tagid": 1,
      "tagname": "a"
    },
    {
      "tagid": 2,
      "tagname": "b"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» taglist|[object]|false|none||标签列表|
|»» tagid|integer|true|none||none|
|»» tagname|string|true|none||none|

## GET 获取标签成员

GET /cgi-bin/tag/get

权限说明：无限制，但返回列表仅包含应用可见范围的成员；第三方可获取自己创建的标签及应用可见范围内的标签详情

文档ID: 10921
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90213
https://open.work.weixin.qq.com/api/doc/90001/90143/90349
https://open.work.weixin.qq.com/api/doc/90002/90151/90832

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|tagid|query|string| 是 |标签ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "tagname": "乒乓球协会",
  "userlist": [
    {
      "userid": "zhangsan",
      "name": "李四"
    }
  ],
  "partylist": [
    2
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» userlist|[object]|false|none||标签中包含的成员列表|
|»» userid|string|true|none||none|
|»» name|string|true|none||none|
|» tagname|string|false|none||标签名|
|» partylist|[number]|false|none||标签中包含的部门id列表|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/通讯录管理/异步批量接口

## GET 获取异步任务结果

GET /cgi-bin/batch/getresult

权限说明：只能查询已经提交过的历史任务。

文档ID: 15017
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90983
https://open.work.weixin.qq.com/api/doc/90001/90143/91133
https://open.work.weixin.qq.com/api/doc/90002/90151/91166

------
result结构：type为sync_user、replace_user时字段解析：
userid	成员UserID。对应管理端的帐号
errcode	该成员对应操作的结果错误码
errmsg	错误信息，例如无权限错误，键值冲突，格式错误等
---
result结构：type为replace_party时字段解析：
action	操作类型（按位或）：1 新建部门 ，2 更改部门名称， 4 移动部门， 8 修改部门排序
partyid	部门ID
errcode	该部门对应操作的结果错误码
errmsg	错误信息，例如无权限错误，键值冲突，格式错误等

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|jobid|query|string| 是 |异步任务id，最大长度为64字节|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "status": 1,
  "type": "replace_user",
  "total": 3,
  "percentage": 33,
  "result": [
    {},
    {}
  ]
}
```

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "status": 1,
  "type": "replace_user",
  "total": 3,
  "percentage": 33,
  "result": [
    {
      "userid": "lisi",
      "errcode": 0,
      "errmsg": "ok"
    },
    {
      "userid": "zhangsan",
      "errcode": 0,
      "errmsg": "ok"
    }
  ]
}
```

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "status": 1,
  "type": "replace_user",
  "total": 3,
  "percentage": 33,
  "result": [
    {
      "action": 1,
      "partyid": 1,
      "errcode": 0,
      "errmsg": "ok"
    },
    {
      "action": 4,
      "partyid": 2,
      "errcode": 0,
      "errmsg": "ok"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||该部门对应操作的结果错误码|
|» result|[object]|false|none||详细的处理结果，具体格式参考下面说明。当任务完成后此字段有效|
|» total|integer(int32)|false|none||none|
|» percentage|integer(int32)|false|none||目前运行百分比，当任务完成时为100|
|» errmsg|string|false|none||错误信息，例如无权限错误，键值冲突，格式错误等|
|» type|string|false|none||操作类型，字节串，目前分别有：1. sync_user(增量更新成员) 2. replace_user(全量覆盖成员)3. replace_party(全量覆盖部门)|
|» status|integer(int32)|false|none||none|

## POST 增量更新成员

POST /cgi-bin/batch/syncuser

本接口以userid（帐号）为主键，增量更新企业微信通讯录成员。请先下载CSV模板(下载增量更新成员模版)，根据需求填写文件内容。

注意事项：

- 模板中的部门需填写部门ID，多个部门用分号分隔，部门ID必须为数字，根部门的部门id默认为1
- 文件中存在、通讯录中也存在的成员，更新成员在文件中指定的字段值
- 文件中存在、通讯录中不存在的成员，执行添加操作
- 通讯录中存在、文件中不存在的成员，保持不变
- 成员字段更新规则：可自行添加扩展字段。文件中有指定的字段，以指定的字段值为准；文件中没指定的字段，不更新

权限说明：
须拥有通讯录的写权限。

文档ID: 15014
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90980
https://open.work.weixin.qq.com/api/doc/90001/90143/91130
https://open.work.weixin.qq.com/api/doc/90002/90151/91163

> Body 请求参数

```json
{
  "media_id": "xxxxxx",
  "to_invite": true,
  "callback": {
    "url": "xxx",
    "token": "xxx",
    "encodingaeskey": "xxx"
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» media_id|body|string| 是 |上传的csv文件的media_id|
|» to_invite|body|boolean| 是 |是否邀请新建的成员使用企业微信（将通过微信服务通知或短信或邮件下发邀请，每天自动下发一次，最多持续3个工作日），默认值为true。|
|» callback|body|object| 否 |回调信息。如填写该项则任务完成后，通过callback推送事件给企业。具体请参考应用回调模式中的相应选项|
|»» url|body|string| 否 |企业应用接收企业微信推送请求的访问协议和地址，支持http或https协议|
|»» token|body|string| 否 |用于生成签名|
|»» encodingaeskey|body|string| 否 |用于消息体的加密，是AES密钥的Base64编码|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "xxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||异步任务id，最大长度为64字节|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 全量覆盖部门

POST /cgi-bin/batch/replaceparty

本接口以partyid为键，全量覆盖企业的通讯录组织架构，任务完成后企业的通讯录组织架构与提交的文件完全保持一致。请先下载CSV文件(下载全量覆盖部门模版)，根据需求填写文件内容。

注意事项：

文件中存在、通讯录中也存在的部门，执行修改操作
文件中存在、通讯录中不存在的部门，执行添加操作
文件中不存在、通讯录中存在的部门，当部门下没有任何成员或子部门时，执行删除操作
文件中不存在、通讯录中存在的部门，当部门下仍有成员或子部门时，暂时不会删除，当下次导入成员把人从部门移出后自动删除
CSV文件中，部门名称、部门ID、父部门ID为必填字段，部门ID必须为数字，根部门的部门id默认为1；排序为可选字段，置空或填0不修改排序, order值大的排序靠前。

权限说明：
须拥有通讯录的写权限。

文档ID: 15016
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90982
https://open.work.weixin.qq.com/api/doc/90001/90143/91132
https://open.work.weixin.qq.com/api/doc/90002/90151/91165

> Body 请求参数

```json
{
  "media_id": "xxxxxx",
  "callback": {
    "url": "xxx",
    "token": "xxx",
    "encodingaeskey": "xxx"
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» media_id|body|string| 是 |上传的csv文件的media_id|
|» callback|body|object| 否 |回调信息。如填写该项则任务完成后，通过callback推送事件给企业。具体请参考应用回调模式中的相应选项|
|»» url|body|string| 否 |企业应用接收企业微信推送请求的访问协议和地址，支持http或https协议|
|»» token|body|string| 否 |用于生成签名|
|»» encodingaeskey|body|string| 否 |用于消息体的加密，是AES密钥的Base64编码|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "xxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||异步任务id，最大长度为64字节|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 全量覆盖成员

POST /cgi-bin/batch/replaceuser

本接口以userid为主键，全量覆盖企业的通讯录成员，任务完成后企业的通讯录成员与提交的文件完全保持一致。请先下载CSV文件(下载全量覆盖成员模版)，根据需求填写文件内容。

注意事项：
模板中的部门需填写部门ID，多个部门用分号分隔，部门ID必须为数字，根部门的部门id默认为1
文件中存在、通讯录中也存在的成员，完全以文件为准
文件中存在、通讯录中不存在的成员，执行添加操作
通讯录中存在、文件中不存在的成员，执行删除操作。出于安全考虑，下面两种情形系统将中止导入并返回相应的错误码。
需要删除的成员多于50人，且多于现有人数的20%以上
需要删除的成员少于50人，且多于现有人数的80%以上
成员字段更新规则：可自行添加扩展字段。文件中有指定的字段，以指定的字段值为准；文件中没指定的字段，不更新

权限说明：须拥有通讯录的写权限

文档ID: 15015
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90981
https://open.work.weixin.qq.com/api/doc/90001/90143/91131
https://open.work.weixin.qq.com/api/doc/90002/90151/91164

> Body 请求参数

```json
{
  "media_id": "xxxxxx",
  "to_invite": true,
  "callback": {
    "url": "xxx",
    "token": "xxx",
    "encodingaeskey": "xxx"
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» media_id|body|string| 是 |上传的csv文件的media_id|
|» to_invite|body|boolean| 是 |是否邀请新建的成员使用企业微信（将通过微信服务通知或短信或邮件下发邀请，每天自动下发一次，最多持续3个工作日），默认值为true。|
|» callback|body|object| 否 |回调信息。如填写该项则任务完成后，通过callback推送事件给企业。具体请参考应用回调模式中的相应选项|
|»» url|body|string| 否 |企业应用接收企业微信推送请求的访问协议和地址，支持http或https协议|
|»» token|body|string| 否 |用于生成签名|
|»» encodingaeskey|body|string| 否 |用于消息体的加密，是AES密钥的Base64编码|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "xxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||异步任务id，最大长度为64字节|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/通讯录管理/互联企业

## POST 获取互联企业部门成员

POST /cgi-bin/linkedcorp/user/simplelist

权限说明：仅自建应用可调用，应用须拥有指定部门的查看权限。

> Body 请求参数

```json
{
  "department_id": "LINKEDID/DEPARTMENTID",
  "fetch_child": true
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» fetch_child|body|boolean| 否 |是否递归获取子部门下面的成员：1-递归获取，0-只获取本部门，不传默认只获取本部门成员|
|» department_id|body|string| 是 |该字段用的是互联应用可见范围接口返回的department_ids参数，用的是 linkedid + ’/‘ + department_id 拼成的字符串|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userlist": [
    {
      "userid": "zhangsan",
      "name": "张三",
      "department": [
        "LINKEDID/1",
        "LINKEDID/2"
      ],
      "corpid": "xxxxxx"
    },
    {
      "userid": "lisi",
      "name": "李四",
      "department": [
        "LINKEDID/1"
      ],
      "corpid": "xxxxxx"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» userlist|[object]|true|none||成员列表|
|»» userid|string|true|none||成员UserID。对应管理端的帐号|
|»» name|string|true|none||成员真实名称|
|»» department|[string]|true|none||成员所属部门id列表，这个字段只会返回传入的department_id所属的互联企业里的部门id|
|»» corpid|string|true|none||所属企业的corpid|

## POST 获取互联企业部门列表

POST /cgi-bin/linkedcorp/department/list

权限说明：仅自建应用可调用，应用须拥有指定部门的查看权限。

文档ID: 24273
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/93170

> Body 请求参数

```json
{
  "department_id": "LINKEDID/DEPARTMENTID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» department_id|body|string| 是 |该字段用的是互联应用可见范围接口返回的department_ids参数，用的是 linkedid + ’/‘ + department_id 拼成的字符串|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "department_list": [
    {
      "department_id": "1",
      "department_name": "测试部门1",
      "parentid": "0",
      "order": 100000000
    },
    {
      "department_id": "2",
      "department_name": "测试部门2",
      "parentid": "1",
      "order": 99999999
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» department_list|[object]|true|none||none|
|»» department_id|string|true|none||部门id|
|»» department_name|string|true|none||部门名称|
|»» parentid|string|true|none||上级部门的id|
|»» order|integer|true|none||排序值|

## POST 获取应用的可见范围

POST /cgi-bin/linkedcorp/agent/get_perm_list

本接口只返回互联企业中非本企业内的成员和部门的信息，如果要获取本企业的可见范围，请调用“获取应用”接口

权限说明：仅自建应用可调用。

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userids": [
    "CORPID/USERID"
  ],
  "department_ids": [
    "LINKEDID/DEPARTMENTID"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» userids|[string]|false|none||可见的userids，是用 CorpId + ’/‘ + USERID 拼成的字符串|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» department_ids|[string]|false|none||可见的department_ids，是用 linkedid + ’/‘ + department_id 拼成的字符串|

## POST 获取互联企业成员详细信息

POST /cgi-bin/linkedcorp/user/get

权限说明：仅自建应用可调用，应用须拥有指定成员的查看权限。

> Body 请求参数

```json
{
  "userid": "CORPID/USERID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» userid|body|string| 是 |该字段用的是互联应用可见范围接口返回的userids参数，用的是 CorpId + ’/‘ + USERID 拼成的字符串|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "user_info": {
    "userid": "zhangsan",
    "name": "张三",
    "department": [
      "LINKEDID/1",
      "LINKEDID/2"
    ],
    "mobile": "+8612345678901",
    "telephone": "10086",
    "email": "zhangsan@tencent.com",
    "position": "后台开发",
    "corpid": "xxxxxx",
    "extattr": {
      "attrs": [
        {
          "name": "自定义属性(文本)",
          "value": "10086",
          "type": 0,
          "text": {
            "value": "10086"
          }
        },
        {
          "name": "自定义属性(网页)",
          "type": 1,
          "web": {
            "url": "https://work.weixin.qq.com/",
            "title": "官网"
          }
        }
      ]
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» user_info|object|true|none||成员的详细信息，user包含的属性可在管理端配置|
|»» userid|string|true|none||成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节|
|»» name|string|true|none||成员真实名称|
|»» department|[string]|true|none||成员所属部门id列表，这个字段会返回在应用可见范围内，该用户所在的所有互联企业的部门|
|»» mobile|string|true|none||none|
|»» telephone|string|true|none||none|
|»» email|string|true|none||none|
|»» position|string|true|none||职务信息|
|»» corpid|string|true|none||所属企业的corpid|
|»» extattr|object|true|none||扩展属性|
|»»» attrs|[object]|true|none||none|
|»»»» name|string|true|none||none|
|»»»» value|string|false|none||none|
|»»»» type|integer|true|none||none|
|»»»» text|object|false|none||none|
|»»»»» value|string|true|none||none|
|»»»» web|object|false|none||none|
|»»»»» url|string|true|none||none|
|»»»»» title|string|true|none||none|

## POST 获取互联企业部门成员详情

POST /cgi-bin/linkedcorp/user/list

权限说明：仅自建应用可调用，应用须拥有指定部门的查看权限。

 

> Body 请求参数

```json
{
  "department_id": "LINKEDID/DEPARTMENTID",
  "fetch_child": true
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» errcode|body|integer| 是 |返回码|
|» errmsg|body|string| 是 |对返回码的文本描述内容|
|» userlist|body|[object]| 是 |成员列表，user包含的属性可在管理端配置|
|»» userid|body|string| 否 |成员UserID。对应管理端的帐号，企业内必须唯一。不区分大小写，长度为1~64个字节|
|»» name|body|string| 否 |成成员真实名称|
|»» department|body|[string]| 否 |成员所属部门id列表，这个字段只会返回传入的department_id所属的互联企业里的部门id|
|»» mobile|body|string| 否 |手机号码|
|»» telephone|body|string| 否 |座机|
|»» email|body|string| 否 |邮箱|
|»» position|body|string| 否 |职务信息|
|»» corpid|body|string| 否 |所属企业的corpid|
|»» extattr|body|object| 否 |扩展属性|
|»»» attrs|body|[object]| 是 |none|
|»»»» name|body|string| 是 |none|
|»»»» value|body|string| 否 |none|
|»»»» type|body|integer| 是 |none|
|»»»» text|body|object| 否 |none|
|»»»»» value|body|string| 是 |none|
|»»»» web|body|object| 否 |none|
|»»»»» url|body|string| 是 |none|
|»»»»» title|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userlist": [
    {
      "userid": "zhangsan",
      "name": "张三",
      "department": [
        "LINKEDID/1",
        "LINKEDID/2"
      ],
      "mobile": "+8612345678901",
      "telephone": "10086",
      "email": "zhangsan@tencent.com",
      "position": "后台开发",
      "corpid": "xxxxxx",
      "extattr": {
        "attrs": [
          {
            "name": "自定义属性(文本)",
            "value": "10086",
            "type": 0,
            "text": {
              "value": "10086"
            }
          },
          {
            "name": "自定义属性(网页)",
            "type": 1,
            "web": {
              "url": "https://work.weixin.qq.com/",
              "title": "官网"
            }
          }
        ]
      }
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» userlist|[string]|false|none||成员列表，user包含的属性可在管理端配置|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/通讯录管理/异步导出接口

## GET 获取导出结果

GET /cgi-bin/export/get_result

权限说明：获取任务结果的调用身份需要与提交任务的一致

数据格式
① 成员
userlist内容与获取部门成员接口一致
{
   "userlist": [
           {
                  "userid": "zhangsan",
                  "name": "张三",
                  "department": [1, 2],
                  "open_userid": "xxxxxx"
           },
           {
                  "userid": "lisi",
                  "name": "李四",
                  "department": [1, 2],
                  "open_userid": "xxxxxx"
           }
     ]
}
② 成员详情
userlist内容与获取部门成员详情接口一致

{
    "userlist": [{
        "userid": "zhangsan",
        "name": "李四",
        "department": [1, 2],
        "order": [1, 2],
        "position": "后台工程师",
        "mobile": "13800000000",
        "gender": "1",
        "email": "zhangsan@gzdev.com",
        .....其他字段省略.....
    }]
}
③ 部门
department内容与获取部门列表接口一致

{
   "department": [
       {
           "id": 2,
           "name": "广州研发中心",
           "name_en": "RDGZ",
           "parentid": 1,
           "order": 10
       },
       {
           "id": 3,
           "name": "邮箱产品部",
           "name_en": "mail",
           "parentid": 2,
           "order": 40
       }
   ]
}
④ 标签成员
userlist和partylist内容与获取标签成员接口一致
{
   "tagname": "乒乓球协会",
   "userlist": [
         {
             "userid": "zhangsan",
             "name": "李四"
         }
     ],
   "partylist": [2]
}

----------------------------------------
文档ID: 31884
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94854
https://open.work.weixin.qq.com/api/doc/90001/90143/94954

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |none|
|jobid|query|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "status": 2,
  "data_list": [
    {
      "url": "https://xxxxx",
      "size": "xxx",
      "md5": "xxxxxxxxx"
    },
    {
      "url": "https://xxxxx",
      "size": "xxx",
      "md5": "xxxxxxxx"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» data_list|[string]|false|none||数据文件列表|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» status|integer(int32)|false|none||任务状态:0-未处理，1-处理中，2-完成，3-异常失败|

## POST 导出成员

POST /cgi-bin/export/simple_user

企业通讯录安全特别重要，企业微信将持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取成员ID列表](https://developer.work.weixin.qq.com/document/path/94849#40412)」和「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/94849#36259)」接口获取userid和部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/94849#40802)。

权限说明：仅会返回有权限的人员列表

文档ID: 31876
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94849
https://open.work.weixin.qq.com/api/doc/90001/90143/94950

> Body 请求参数

```json
{
  "encoding_aeskey": "IJUiXNpvGbODwKEBSEsAeOAPAhkqHqNCF6g19t9wfg2",
  "block_size": 1000000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» encoding_aeskey|body|string| 是 |base64encode的加密密钥，长度固定为43，加密方式采用aes-256-cbc方式|
|» block_size|body|integer(int32)| 否 |每块数据的人员数，支持范围[10^4,10^6]，默认值为10^6|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "jobid_xxxxxxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||任务ID，可通过获取导出结果接口查询任务结果|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 导出成员详情

POST /cgi-bin/export/user

企业通讯录安全特别重要，企业微信将持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取成员ID列表](https://developer.work.weixin.qq.com/document/path/94849#40412)」和「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/94849#36259)」接口获取userid和部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/94849#40802)。

权限说明：仅会返回有权限的人员列表

文档ID: 31880
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94851
https://open.work.weixin.qq.com/api/doc/90001/90143/94951

> Body 请求参数

```json
{
  "encoding_aeskey": "IJUiXNpvGbODwKEBSEsAeOAPAhkqHqNCF6g19t9wfg2",
  "block_size": 1000000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» encoding_aeskey|body|string| 是 |base64encode的加密密钥，长度固定为43，加密方式采用aes-256-cbc方式|
|» block_size|body|integer(int32)| 否 |每块数据的人员数，支持范围[10^4,10^6]，默认值为10^6|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "jobid_xxxxxxxxxxxxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||任务ID，可通过获取导出结果接口查询任务结果|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 导出部门

POST /cgi-bin/export/department

企业通讯录安全特别重要，企业微信将持续升级加固通讯录接口的安全机制，以下是关键的变更点：

- **【重要】**从2022年8月15日10点开始，“企业管理后台 - 管理工具 - 通讯录同步”的新增IP将不能再调用此接口，企业可通过「[获取成员ID列表](https://developer.work.weixin.qq.com/document/path/94849#40412)」和「[获取部门ID列表](https://developer.work.weixin.qq.com/document/path/94849#36259)」接口获取userid和部门ID列表。[查看调整详情](https://developer.work.weixin.qq.com/document/path/94849#40802)。

权限说明：仅返回有权限的部门列表

文档ID: 31882
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94852
https://open.work.weixin.qq.com/api/doc/90001/90143/94952

> Body 请求参数

```json
{
  "encoding_aeskey": "IJUiXNpvGbODwKEBSEsAeOAPAhkqHqNCF6g19t9wfg2",
  "block_size": 1000000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» encoding_aeskey|body|string| 是 |base64encode的加密密钥，长度固定为43，加密方式采用aes-256-cbc方式|
|» block_size|body|integer(int32)| 否 |每块数据的部门数，支持范围[10^4,10^6]，默认值为10^6|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "jobid_xxxxxxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||任务ID，可通过获取导出结果接口查询任务结果|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 导出标签成员

POST /cgi-bin/export/taguser

权限说明：要求对标签有读取权限

文档ID: 31883
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94853
https://open.work.weixin.qq.com/api/doc/90001/90143/94953

> Body 请求参数

```json
{
  "tagid": 1,
  "encoding_aeskey": "IJUiXNpvGbODwKEBSEsAeOAPAhkqHqNCF6g19t9wfg2",
  "block_size": 1000000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» tagid|body|integer(int32)| 是 |需要导出的标签|
|» encoding_aeskey|body|string| 是 |base64encode的加密密钥，长度固定为43，加密方式采用aes-256-cbc方式|
|» block_size|body|integer(int32)| 否 |每块数据的人员数和部门数之和，支持范围[10^4,10^6]，默认值为10^6|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "jobid_xxxxxxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» jobid|string|false|none||任务ID，可通过获取导出结果接口查询任务结果|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系

## POST 上传附件资源

POST /cgi-bin/media/upload_attachment

素材上传得到media_id，该media_id仅三天内有效
media_id在同一企业内应用之间可以共享

使用multipart/form-data POST上传文件， 文件标识名为”media”

POST的请求包中，form-data中媒体文件标识，应包含有 filename、filelength、content-type等信息

filename标识文件展示的名称。比如，使用该media_id发消息时，展示的文件名由该字段控制

## **上传的媒体文件限制**

所有文件size必须大于5个字节

- 图片（image）：2MB，支持JPG,PNG格式
- 语音（voice） ：2MB，播放长度不超过60s，**仅支持**AMR格式
- 视频（video） ：10MB，支持MP4格式
- 普通文件（file）：20MB

> Body 请求参数

```yaml
media: string

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|media_type|query|string| 是 |媒体文件类型，分别有图片（image）、视频（video）、普通文件（file）|
|attachment_type|query|string| 是 |附件类型，不同的附件类型用于不同的场景。1：朋友圈；2:商品图册|
|body|body|object| 否 |none|
|» media|body|string(binary)| 否 |none|

> 返回示例

> 成功

```json
"{\r\n   \"errcode\": 0,\r\n   \"errmsg\": \"\"，\r\n   \"type\": \"image\",\r\n   \"media_id\": \"1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0\",\r\n   \"created_at\": 1380000000\r\n}"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» type|string|true|none||媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)|
|» media_id|string|true|none||媒体文件上传后获取的唯一标识，3天内有效|
|» created_at|string|true|none||媒体文件上传时间戳|

# 企业内部开发/客户联系/企业服务人员管理

## GET 获取配置了客户联系功能的成员列表

GET /cgi-bin/externalcontact/get_follow_user_list

企业和第三方服务商可通过此接口获取配置了客户联系功能的成员列表。

权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）；
第三方应用需具有“企业客户权限->客户基础信息”权限
第三方/自建应用只能获取到可见范围内的配置了客户联系功能的成员。

权限说明：返回结果：

文档ID: 16683
 原文档地址：
https://open.work.weixin.qq.com/api/doc/92284

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "follow_user": [
    "zhangsan",
    "lissi"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» follow_user|[string]|false|none||配置了家校沟通功能的成员userid列表|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 客户联系「联系我」管理-获取企业已配置的「联系我」方式

POST /cgi-bin/externalcontact/get_contact_way

获取企业配置的「联系我」二维码和「联系我」小程序按钮。

 

文档ID: 15645
 原文档地址：
https://open.work.weixin.qq.com/api/doc/92228
https://open.work.weixin.qq.com/api/doc/90000/90135/92572
https://open.work.weixin.qq.com/api/doc/90001/90143/92577

> Body 请求参数

```json
{
  "config_id": "42b34949e138eb6e027c123cba77fad7"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» config_id|body|string| 是 |联系方式的配置id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "contact_way": {
    "config_id": "42b34949e138eb6e027c123cba77fAAA",
    "type": 1,
    "scene": 1,
    "style": 2,
    "remark": "test remark",
    "skip_verify": true,
    "state": "teststate",
    "qr_code": "http://p.qpic.cn/wwhead/duc2TvpEgSdicZ9RrdUtBkv2UiaA/0",
    "user": [
      "zhangsan",
      "lisi",
      "wangwu"
    ],
    "party": [
      2,
      3
    ],
    "is_temp": true,
    "expires_in": 86400,
    "chat_expires_in": 86400,
    "unionid": "oxTWIuGaIt6gTKsQRLau2M0AAAA",
    "conclusions": {
      "text": {
        "content": "文本消息内容"
      },
      "image": {
        "pic_url": "http://p.qpic.cn/pic_wework/XXXXX"
      },
      "link": {
        "title": "消息标题",
        "picurl": "https://example.pic.com/path",
        "desc": "消息描述",
        "url": "https://example.link.com/path"
      },
      "miniprogram": {
        "title": "消息标题",
        "pic_media_id": "MEDIA_ID",
        "appid": "wx8bd80126147dfAAA",
        "page": "/path/index"
      }
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» contact_way|object|true|none||none|
|»» config_id|string|true|none||新增联系方式的配置id|
|»» type|integer|true|none||联系方式类型，1-单人，2-多人|
|»» scene|integer|true|none||场景，1-在小程序中联系，2-通过二维码联系|
|»» style|integer|true|none||小程序中联系按钮的样式，仅在scene为1时返回，详见附录|
|»» remark|string|true|none||none|
|»» skip_verify|boolean|true|none||none|
|»» state|string|true|none||none|
|»» qr_code|string|true|none||联系二维码的URL，仅在scene为2时返回|
|»» user|[string]|true|none||使用该联系方式的用户userID列表|
|»» party|[integer]|true|none||使用该联系方式的部门id列表|
|»» is_temp|boolean|true|none||none|
|»» expires_in|integer|true|none||临时会话二维码有效期，以秒为单位|
|»» chat_expires_in|integer|true|none||临时会话有效期，以秒为单位|
|»» unionid|string|true|none||可进行临时会话的客户unionid|
|»» conclusions|[conclusions](#schemaconclusions)|true|none||结束语，可参考“结束语定义”|
|»»» text|object|true|none||none|
|»»»» content|string|true|none||消息文本内容,最长为4000字节|
|»»» image|object|true|none||none|
|»»»» pic_url|string|true|none||图片的url|
|»»» link|object|true|none||none|
|»»»» title|string|true|none||图文消息标题，最长为128字节|
|»»»» picurl|string|true|none||图文消息封面的url|
|»»»» desc|string|true|none||图文消息的描述，最长为512字节|
|»»»» url|string|true|none||图文消息的链接|
|»»» miniprogram|object|true|none||none|
|»»»» title|string|true|none||小程序消息标题，最长为64字节|
|»»»» pic_media_id|string|true|none||小程序消息封面的mediaid，封面图建议尺寸为520*416|
|»»»» appid|string|true|none||小程序appid，必须是关联到企业的小程序应用|
|»»»» page|string|true|none||小程序page路径|

## POST 客户联系「联系我」管理-获取企业已配置的「联系我」列表

POST /cgi-bin/externalcontact/list_contact_way

获取企业配置的「联系我」二维码和「联系我」小程序插件列表。不包含临时会话。
注意，该接口仅可获取2021年7月10日以后创建的「联系我」

文档ID: 15645
 原文档地址：
https://open.work.weixin.qq.com/api/doc/92228
https://open.work.weixin.qq.com/api/doc/90000/90135/92572
https://open.work.weixin.qq.com/api/doc/90001/90143/92577

> Body 请求参数

```json
{
  "start_time": 1622476800,
  "end_time": 1625068800,
  "cursor": "CURSOR",
  "limit": 1000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |分页查询使用的游标，为上次请求返回的 next_cursor|
|» start_time|body|integer(int32)| 否 |「联系我」创建起始时间戳, 默认为90天前|
|» end_time|body|integer(int32)| 否 |「联系我」创建结束时间戳, 默认为当前时间|
|» limit|body|integer(int32)| 否 |每次查询的分页大小，默认为100条，最多支持1000条|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "contact_way": [
    {
      "config_id": "534b63270045c9ABiKEE814ef56d91c62f"
    },
    {
      "config_id": "87bBiKEE811c62f63270041c62f5c9A4ef"
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页参数，用于查询下一个分页的数据，为空时表示没有更多的分页|
|» contact_way|[object]|false|none||none|
|»» config_id|string|true|none||联系方式的配置id|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户管理

## POST 批量获取客户详情

POST /cgi-bin/externalcontact/batch/get_by_user

企业/第三方可通过此接口获取指定成员添加的客户信息列表。

权限说明：企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）； 第三方应用需具有“企业客户权限->客户基础信息”权限 第三方/自建应用调用此接口时，userid需要在相关应用的可见范围内。 规则组标签仅可通过“客户联系”获取。

文档ID: 23414
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92994
https://open.work.weixin.qq.com/api/doc/90001/90143/93010

> Body 请求参数

```json
{
  "userid_list": [
    "zhangsan",
    "lisi"
  ],
  "cursor": "",
  "limit": 100
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|userid_list|query|string| 是 |企业成员的userid列表，字符串类型，最多支持100个|
|cursor|query|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|limit	|query|string| 否 |返回的最大记录数，整型，最大值100，默认值50，超过最大值时取最大值|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值100，默认值50，超过最大值时取最大值|
|» userid_list|body|[string]| 是 |企业成员的userid列表，字符串类型，最多支持100个|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "external_contact_list": [
    {
      "external_contact": {
        "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACHAAA",
        "name": "李四",
        "position": "Manager",
        "avatar": "http://p.qlogo.cn/bizmail/IcsdgagqefergqerhewSdage/0",
        "corp_name": "腾讯",
        "corp_full_name": "腾讯科技有限公司",
        "type": 2,
        "gender": 1,
        "unionid": "ozynqsulJFCZ2z1aYeS8h-nuasdAAA",
        "external_profile": {
          "external_attr": [
            {
              "type": 0,
              "name": "文本名称",
              "text": {
                "value": "文本"
              }
            },
            {
              "type": 1,
              "name": "网页名称",
              "web": {
                "url": "http://www.test.com",
                "title": "标题"
              }
            },
            {
              "type": 2,
              "name": "测试app",
              "miniprogram": {
                "appid": "wx8bd80126147df384",
                "pagepath": "/index",
                "title": "myminiprogram"
              }
            }
          ]
        }
      },
      "follow_info": {
        "userid": "rocky",
        "remark": "李部长",
        "description": "对接采购事务",
        "createtime": 1525779812,
        "tag_id": [
          "etAJ2GCAAAXtWyujaWJHDDGi0mACHAAA"
        ],
        "remark_corp_name": "腾讯科技",
        "remark_mobiles": [
          "13800000001",
          "13000000002"
        ],
        "oper_userid": "rocky",
        "add_way": 1
      }
    },
    {
      "external_contact": {
        "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACHBBB",
        "name": "王五",
        "position": "Engineer",
        "avatar": "http://p.qlogo.cn/bizmail/IcsdgagqefergqerhewSdage/0",
        "corp_name": "腾讯",
        "corp_full_name": "腾讯科技有限公司",
        "type": 2,
        "gender": 1,
        "unionid": "ozynqsulJFCZ2asdaf8h-nuasdAAA"
      },
      "follow_info": {
        "userid": "lisi",
        "remark": "王助理",
        "description": "采购问题咨询",
        "createtime": 1525881637,
        "tag_id": [
          "etAJ2GCAAAXtWyujaWJHDDGi0mACHAAA",
          "stJHDDGi0mAGi0mACHBBByujaW"
        ],
        "state": "外联二维码1",
        "oper_userid": "woAJ2GCAAAd1asdasdjO4wKmE8AabjBBB",
        "add_way": 3
      }
    }
  ],
  "next_cursor": "r9FqSqsI8fgNbHLHE5QoCP50UIg2cFQbfma3l2QsmwI"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» external_contact_list|[object]|true|none||none|
|»» external_contact|object|true|none||客户的基本信息，可以参考获取客户详情|
|»»» external_userid|string|true|none||none|
|»»» name|string|true|none||none|
|»»» position|string|true|none||none|
|»»» avatar|string|true|none||none|
|»»» corp_name|string|true|none||none|
|»»» corp_full_name|string|true|none||none|
|»»» type|integer|true|none||none|
|»»» gender|integer|true|none||none|
|»»» unionid|string|true|none||none|
|»»» external_profile|object|false|none||none|
|»»»» external_attr|[object]|true|none||none|
|»»»»» type|integer|true|none||none|
|»»»»» name|string|true|none||none|
|»»»»» text|object|false|none||none|
|»»»»»» value|string|true|none||none|
|»»»»» web|object|false|none||none|
|»»»»»» url|string|true|none||none|
|»»»»»» title|string|true|none||none|
|»»»»» miniprogram|object|false|none||none|
|»»»»»» appid|string|true|none||none|
|»»»»»» pagepath|string|true|none||none|
|»»»»»» title|string|true|none||none|
|»» follow_info|object|true|none||企业成员客户跟进信息，可以参考获取客户详情，但标签信息只会返回企业标签和规则组标签的tag_id，个人标签将不再返回|
|»»» userid|string|true|none||none|
|»»» remark|string|true|none||none|
|»»» description|string|true|none||none|
|»»» createtime|integer|true|none||none|
|»»» tag_id|[string]|true|none||none|
|»»» remark_corp_name|string|false|none||none|
|»»» remark_mobiles|[string]|false|none||none|
|»»» oper_userid|string|true|none||none|
|»»» add_way|integer|true|none||none|
|»»» state|string|true|none||none|
|» next_cursor|string|true|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|

## GET 获取客户详情

GET /cgi-bin/externalcontact/get

权限说明：
- 企业需要使用系统应用“客户联系”或配置到“可调用应用”列表中的自建应用的secret所获取的accesstoken来调用（accesstoken如何获取？）； 
- 第三方应用需具有“企业客户权限->客户基础信息”权限 
- 第三方/自建应用调用时，返回的跟进人follow_user仅包含应用可见范围之内的成员。 
-当客户在企业内的跟进人超过500人时需要使用cursor参数进行分页获取
-------------
- 如何绑定微信开发者ID
1.登录企业的管理后台-客户联系-客户-api（注：若企业管理端没有客户联系的入口，而是家校沟通的入口，则通过 家校沟通-家校沟通-api ），点击绑定去到微信公众平台进行授权，支持绑定公众号和小程序（需要同时绑定微信开放平台）；绑定的公众号或小程序主体需与企业微信主体一致，暂且支持绑定一个
2.绑定完成，即可通过接口获取微信联系人所对应的微信unionid
3.第三方服务商若需要unionid，则只需要在服务商自身企业管理后台的客户联系-客户-api中关联微信开发者ID；第三方调用接口返回的unionid是该服务商所关联的微信开发者帐号的unionid。也就是说，同一个企业客户，企业自己调用，与第三方服务商调用，所返回的unionid不同；不同的服务商调用，所返回的unionid也不同
- 来源定义
add_way表示添加客户的来源，有固定的值，而state表示此客户的渠道，可以由企业进行自定义的配置，请注意二者的不同。

值	含义
0	未知来源
1	扫描二维码
2	搜索手机号
3	名片分享
4	群聊
5	手机通讯录
6	微信联系人
7	来自微信的添加好友申请
8	安装第三方应用时自动添加的客服人员
9	搜索邮箱
201	内部成员共享
202	管理员/负责人分配
-------------
文档ID: 13878
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92114
https://open.work.weixin.qq.com/api/doc/90001/90143/92265

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|external_userid|query|string| 是 |外部联系人的userid，注意不是企业成员的帐号|
|cursor|query|string| 否 |上次请求返回的next_cursor|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "external_contact": {
    "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACHAAA",
    "name": "李四",
    "position": "Manager",
    "avatar": "http://p.qlogo.cn/bizmail/IcsdgagqefergqerhewSdage/0",
    "corp_name": "腾讯",
    "corp_full_name": "腾讯科技有限公司",
    "type": 2,
    "gender": 1,
    "unionid": "ozynqsulJFCZ2z1aYeS8h-nuasdAAA",
    "external_profile": {
      "external_attr": [
        {
          "type": 0,
          "name": "文本名称",
          "text": {
            "value": "文本"
          }
        },
        {
          "type": 1,
          "name": "网页名称",
          "web": {
            "url": "http://www.test.com",
            "title": "标题"
          }
        },
        {
          "type": 2,
          "name": "测试app",
          "miniprogram": {
            "appid": "wx8bd80126147df384",
            "pagepath": "/index",
            "title": "myminiprogram"
          }
        }
      ]
    }
  },
  "follow_user": [
    {
      "userid": "rocky",
      "remark": "李部长",
      "description": "对接采购事务",
      "createtime": 1525779812,
      "tags": [
        {
          "group_name": "标签分组名称",
          "tag_name": "标签名称",
          "tag_id": "etAJ2GCAAAXtWyujaWJHDDGi0mACHAAA",
          "type": 1
        },
        {
          "group_name": "标签分组名称",
          "tag_name": "标签名称",
          "type": 2
        },
        {
          "group_name": "标签分组名称",
          "tag_name": "标签名称",
          "tag_id": "stAJ2GCAAAXtWyujaWJHDDGi0mACHAAA",
          "type": 3
        }
      ],
      "remark_corp_name": "腾讯科技",
      "remark_mobiles": [
        "13800000001",
        "13000000002"
      ],
      "oper_userid": "rocky",
      "add_way": 1
    },
    {
      "userid": "tommy",
      "remark": "李总",
      "description": "采购问题咨询",
      "createtime": 1525881637,
      "state": "外联二维码1",
      "oper_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACHAAA",
      "add_way": 3
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» external_contact|object|true|none||none|
|»» external_userid|string|true|none||外部联系人的userid|
|»» name|string|true|none||外部联系人的userid|
|»» position|string|true|none||外部联系人的职位，如果外部企业或用户选择隐藏职位，则不返回，仅当联系人类型是企业微信用户时有此字段|
|»» avatar|string|true|none||外部联系人头像，代开发自建应用需要管理员授权才可以获取，第三方不可获取|
|»» corp_name|string|true|none||外部联系人所在企业的简称，仅当联系人类型是企业微信用户时有此字段|
|»» corp_full_name|string|true|none||外部联系人所在企业的主体名称，仅当联系人类型是企业微信用户时有此字段|
|»» type|integer|true|none||外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户|
|»» gender|integer|true|none||外部联系人性别 0-未知 1-男性 2-女性外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户|
|»» unionid|string|true|none||外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。仅当联系人类型是微信用户，且企业或第三方服务商绑定了微信开发者ID有此字段。查看绑定方法|
|»» external_profile|object|true|none||外部联系人的自定义展示信息，可以有多个字段和多种类型，包括文本，网页和小程序，仅当联系人类型是企业微信用户时有此字段，字段详情见对外属性；|
|»»» external_attr|[object]|true|none||none|
|»»»» type|integer|true|none||none|
|»»»» name|string|true|none||none|
|»»»» text|object|false|none||none|
|»»»»» value|string|true|none||none|
|»»»» web|object|false|none||none|
|»»»»» url|string|true|none||none|
|»»»»» title|string|true|none||none|
|»»»» miniprogram|object|false|none||none|
|»»»»» appid|string|true|none||none|
|»»»»» pagepath|string|true|none||none|
|»»»»» title|string|true|none||none|
|» follow_user|[object]|true|none||none|
|»» userid|string|true|none||添加了此外部联系人的企业成员userid|
|»» remark|string|true|none||该成员对此外部联系人的备注|
|»» description|string|true|none||该成员对此外部联系人的描述|
|»» createtime|integer|true|none||该成员添加此外部联系人的时间|
|»» tags|[object]|false|none||none|
|»»» group_name|string|true|none||该成员添加此外部联系人所打标签的分组名称（标签功能需要企业微信升级到2.7.5及以上版本）|
|»»» tag_name|string|true|none||该成员添加此外部联系人所打标签名称|
|»»» tag_id|string|true|none||该成员添加此外部联系人所打企业标签的id，用户自定义类型标签（type=2）不返回|
|»»» type|integer|true|none||该成员添加此外部联系人所打标签类型, 1-企业设置，2-用户自定义，3-规则组标签（仅系统应用返回）|
|»» remark_corp_name|string|false|none||该成员对此客户备注的企业名称|
|»» remark_mobiles|[string]|false|none||该成员对此客户备注的手机号码，代开发自建应用需要管理员授权才可以获取，第三方不可获取|
|»» oper_userid|string|true|none||发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid|
|»» add_way|integer|true|none||该成员添加此客户的来源，具体含义详见来源定义|
|»» state|string|false|none||企业自定义的state参数，用于区分客户具体是通过哪个「联系我」添加，由企业通过创建「联系我」方式指定|
|» next_cursor|string|true|none||分页的cursor，当跟进人多于500人时返回|

## POST 修改客户备注信息

POST /cgi-bin/externalcontact/get

企业可通过此接口修改指定用户添加的客户的备注信息。

remark_company只在此外部联系人为微信用户时有效。
remark，description，remark_company，remark_mobiles和remark_pic_mediaid不可同时为空。
如果填写了remark_mobiles，将会覆盖旧的备注手机号。
如果要清除所有备注手机号,请在remark_mobiles填写一个空字符串(“”)。
remark_pic_mediaid可以通过素材管理接口获得。

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用需具有“企业客户权限->客户基础信息”权限

> Body 请求参数

```json
{
  "userid": "zhangsan",
  "external_userid": "woAJ2GCAAAd1asdasdjO4wKmE8Aabj9AAA",
  "remark": "备注信息",
  "description": "描述信息",
  "remark_company": "腾讯科技",
  "remark_mobiles": [
    "13800000001",
    "13800000002"
  ],
  "remark_pic_mediaid": "MEDIAID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|external_userid|query|string| 是 |外部联系人的userid，注意不是企业成员的帐号|
|cursor|query|string| 否 |上次请求返回的next_cursor|
|body|body|object| 否 |none|
|» userid|body|string| 是 |企业成员的userid|
|» external_userid|body|string| 是 |外部联系人userid|
|» remark|body|string| 否 |此用户对外部联系人的备注，最多20个字符|
|» description|body|string| 否 |此用户对外部联系人的描述，最多150个字符|
|» remark_company|body|string| 否 |此用户对外部联系人备注的所属公司名称，最多20个字符|
|» remark_mobiles|body|[string]| 否 |此用户对外部联系人备注的手机号|
|» remark_pic_mediaid|body|string| 否 |备注图片的mediaid，|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|false|none||none|
|» errmsg|string|false|none||none|

## GET 获取客户列表

GET /cgi-bin/externalcontact/list

企业可通过此接口获取指定成员添加的客户列表。客户是指配置了客户联系功能的成员所添加的外部联系人。没有配置客户联系功能的成员，所添加的外部联系人将不会作为客户返回。

权限说明：
企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）；
第三方应用需具有“企业客户权限->客户基础信息”权限
第三方/自建应用只能获取到可见范围内的配置了客户联系功能的成员。

文档ID: 16684
 原文档地址：
https://open.work.weixin.qq.com/api/doc/92287

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|userid|query|string| 是 |企业成员的userid|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» external_userid|[string]|false|none||外部联系人的userid列表|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户管理/客户联系规则组管理

## POST 客户联系规则组管理-获取规则组详情

POST /cgi-bin/externalcontact/customer_strategy/get

文档ID: 32023
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94883

> Body 请求参数

```json
{
  "strategy_id": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer(int32)| 是 |规则组id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "strategy": {
    "strategy_id": 1,
    "parent_id": 0,
    "strategy_name": "NAME",
    "create_time": 1557838797,
    "admin_list": [
      "zhangsan",
      "lisi"
    ],
    "privilege": {
      "view_customer_list": true,
      "view_customer_data": true,
      "view_room_list": true,
      "contact_me": true,
      "join_room": true,
      "share_customer": false,
      "oper_resign_customer": true,
      "oper_resign_group": true,
      "send_customer_msg": true,
      "edit_welcome_msg": true,
      "view_behavior_data": true,
      "view_room_data": true,
      "send_group_msg": true,
      "room_deduplication": true,
      "rapid_reply": true,
      "onjob_customer_transfer": true,
      "edit_anti_spam_rule": true,
      "export_customer_list": true,
      "export_customer_data": true,
      "export_customer_group_list": true,
      "manage_customer_tag": true
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» strategy|object|false|none||none|
|»» create_time|integer(int32)|false|none||规则组创建时间戳|
|»» parent_id|integer(int32)|false|none||父规则组id， 如果当前规则组没父规则组，则为0|
|»» strategy_id|integer(int32)|false|none||规则组id|
|»» privilege|object|false|none||none|
|»»» contact_me|boolean|false|none||可使用联系我，基础权限，不可取消|
|»»» send_customer_msg|boolean|false|none||允许给企业客户发送消息，默认为true|
|»»» export_customer_list|boolean|false|none||导出客户列表，默认为true|
|»»» view_customer_data|boolean|false|none||导出成员客户统计，默认为true|
|»»» onjob_customer_transfer|boolean|false|none||转接在职成员的客户，默认为true|
|»»» export_customer_group_list|boolean|false|none||导出客户群列表，默认为true|
|»»» view_room_data|boolean|false|none||允许查看群聊数据统计，默认为true|
|»»» oper_resign_customer|boolean|false|none||允许分配离职成员客户，默认为true|
|»»» export_customer_data|boolean|false|none||导出成员客户统计，默认为true|
|»»» view_room_list|boolean|false|none||查看群聊列表，基础权限，不可取消|
|»»» oper_resign_group|boolean|false|none||允许分配离职成员客户群，默认为true|
|»»» room_deduplication|boolean|false|none||允许对企业客户群进行去重，默认为true|
|»»» send_group_msg|boolean|false|none||允许发送消息到企业的客户群，默认为true|
|»»» view_behavior_data|boolean|false|none||允许查看成员联系客户统计|
|»»» manage_customer_tag|boolean|false|none||配置企业客户标签，默认为true|
|»»» join_room|boolean|false|none||可加入群聊，基础权限，不可取消|
|»»» share_customer|boolean|false|none||允许分享客户给其他成员，默认为true|
|»»» edit_anti_spam_rule|boolean|false|none||编辑企业成员防骚扰规则，默认为true|
|»»» rapid_reply|boolean|false|none||配置快捷回复，默认为true|
|»»» view_customer_list|boolean|false|none||查看客户列表，基础权限，不可取消|
|»»» edit_welcome_msg|boolean|false|none||允许配置欢迎语，默认为true|
|»» strategy_name|string|false|none||规则组名称|
|»» admin_list|[string]|false|none||规则组管理员userid列表|

## POST 客户联系规则组管理-创建新的规则组

POST /cgi-bin/externalcontact/customer_strategy/create

企业可通过此接口创建一个新的客户规则组。该接口仅支持串行调用，请勿并发创建规则组。
文档ID: 32023
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94883

> Body 请求参数

```json
"{\n    \"parent_id\":0,\n    \"strategy_name\": \"NAME\",\n    \"admin_list\":[\n        \"zhangsan\",\n        \"lisi\"\n    ],\n    \"privilege\":\n    {\n            \"view_customer_list\":true,\n            \"view_customer_data\":true,\n            \"view_room_list\":true,\n            \"contact_me\":true,\n            \"join_room\":true,\n            \"share_customer\":false,\n            \"oper_resign_customer\":true,\n            \"send_customer_msg\":true,\n            \"edit_welcome_msg\":true,\n            \"view_behavior_data\":true,\n            \"view_room_data\":true,\n            \"send_group_msg\":true,\n            \"room_deduplication\":true,\n            \"rapid_reply\":true,\n            \"onjob_customer_transfer\":true,\n            \"edit_anti_spam_rule\":true,\n            \"export_customer_list\":true,\n            \"export_customer_data\":true,\n            \"export_customer_group_list\":true，\n            \"manage_customer_tag\":true\n    },\n    \"range\":\n    [\n        {\n            \"type\":1,\n            \"userid\":\"zhangsan\"\n        },\n        {\n            \"type\":2,\n            \"partyid\":1\n        }\n    ]\n}\n"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» parent_id|body|integer| 是 |父规则组id|
|» strategy_name|body|string| 是 |规则组名称|
|» admin_list|body|[string]| 是 |规则组管理员userid列表，不可配置超级管理员，每个规则组最多可配置20个负责人|
|» privilege|body|object| 否 |none|
|»» contact_me|body|boolean| 否 |可使用联系我，基础权限，不可取消|
|»» send_customer_msg|body|boolean| 否 |允许给企业客户发送消息，默认为true|
|»» export_customer_list|body|boolean| 否 |导出客户列表，默认为true|
|»» view_customer_data|body|boolean| 否 |导出成员客户统计，默认为true|
|»» onjob_customer_transfer|body|boolean| 否 |转接在职成员的客户，默认为true|
|»» export_customer_group_list|body|boolean| 否 |导出客户群列表，默认为true|
|»» view_room_data|body|boolean| 否 |允许查看群聊数据统计，默认为true|
|»» oper_resign_customer|body|boolean| 否 |允许分配离职成员客户，默认为true|
|»» export_customer_data|body|boolean| 否 |导出成员客户统计，默认为true|
|»» view_room_list|body|boolean| 否 |查看群聊列表，基础权限，不可取消|
|»» room_deduplication|body|boolean| 否 |允许对企业客户群进行去重，默认为true|
|»» send_group_msg|body|boolean| 否 |允许发送消息到企业的客户群，默认为true|
|»» view_behavior_data|body|boolean| 否 |允许查看成员联系客户统计|
|»» manage_customer_tag|body|boolean| 否 |配置企业客户标签，默认为true|
|»» join_room|body|boolean| 否 |可加入群聊，基础权限，不可取消|
|»» share_customer|body|boolean| 否 |允许分享客户给其他成员，默认为true|
|»» edit_anti_spam_rule|body|boolean| 否 |编辑企业成员防骚扰规则，默认为true|
|»» rapid_reply|body|boolean| 否 |配置快捷回复，默认为true|
|»» view_customer_list|body|boolean| 否 |查看客户列表，基础权限，不可取消|
|»» edit_welcome_msg|body|boolean| 否 |允许配置欢迎语，默认为true|
|» range|body|[object]| 是 |none|
|»» type|body|integer| 是 |规则组的管理范围节点类型 1-成员 2-部门|
|»» userid|body|string| 否 |规则组的管理成员id|
|»» partyid|body|integer| 否 |规则组的管理部门id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "strategy_id": 1
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» strategy_id|integer|true|none||规则组id|

## POST 客户联系规则组管理-获取规则组管理范围

POST /cgi-bin/externalcontact/customer_strategy/get_range

企业可通过此接口获取某个客户规则组管理的成员和部门列表

文档ID: 32023
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94883

> Body 请求参数

```json
{
  "strategy_id": 1,
  "cursor": "CURSOR",
  "limit": 1000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |分页游标|
|» strategy_id|body|integer(int32)| 是 |规则组id|
|» limit|body|integer(int32)| 否 |每个分页的成员/部门节点数，默认为1000，最大为1000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "range": [
    {
      "type": 1,
      "userid": "zhangsan"
    },
    {
      "type": 2,
      "partyid": 1
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，用于查询下一个分页的数据，无更多数据时不返回|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» range|[object]|false|none||none|
|»» type|any|true|none||none|

*oneOf*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»»» *anonymous*|object|false|none||none|
|»»»» type|integer|true|none||节点类型，1-成员 2-部门|
|»»»» userid|string|true|none||管理范围内配置的成员userid，仅type为1时返回|

*xor*

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|»»» *anonymous*|object|false|none||none|
|»»»» type|integer|true|none||节点类型，1-成员 2-部门|
|»»»» partyid|string|true|none||管理范围内配置的部门partyid，仅type为2时返回|

## POST 客户联系规则组管理-删除规则组

POST /cgi-bin/externalcontact/customer_strategy/del

企业可通过此接口删除某个规则组。
文档ID: 32023
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94883

> Body 请求参数

```json
{
  "strategy_id": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer(int32)| 是 |规则组id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 客户联系规则组管理-获取规则组列表

POST /cgi-bin/externalcontact/customer_strategy/list

企业可通过此接口获取企业配置的所有客户规则组id列表。

> Body 请求参数

```json
{
  "cursor": "CURSOR",
  "limit": 1000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |分页查询游标，首次调用可不填|
|» limit|body|integer(int32)| 否 |分页大小,默认为1000，最大不超过1000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "strategy": [
    {
      "strategy_id": 1
    },
    {
      "strategy_id": 2
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，用于查询下一个分页的数据，无更多数据时不返回|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» strategy|[object]|false|none||none|
|»» strategy_id|integer(int32)|false|none||规则组id|

## POST 客户联系规则组管理-编辑规则组及其管理范围

POST /cgi-bin/externalcontact/customer_strategy/edit

企业可通过此接口编辑规则组的基本信息和修改客户规则组管理范围。该接口仅支持串行调用，请勿并发修改规则组。

文档ID: 32023
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94883

> Body 请求参数

```json
{
  "strategy_id": 0,
  "strategy_name": "string",
  "admin_list": [
    "string"
  ],
  "privilege": {
    "view_customer_list": true,
    "view_customer_data": true,
    "view_room_list": true,
    "contact_me": true,
    "join_room": true,
    "share_customer": true,
    "oper_resign_customer": true,
    "oper_resign_group": true,
    "send_customer_msg": true,
    "edit_welcome_msg": true,
    "view_behavior_data": true,
    "view_room_data": true,
    "send_group_msg": true,
    "room_deduplication": true,
    "rapid_reply": true,
    "onjob_customer_transfer": true,
    "edit_anti_spam_rule": true,
    "export_customer_list": true,
    "export_customer_data": true,
    "export_customer_group_list": true,
    "manage_customer_tag": true
  },
  "range_add": [
    {
      "type": 0,
      "userid": "string",
      "partyid": 0
    }
  ],
  "range_del": [
    {
      "type": 0,
      "userid": "string",
      "partyid": 0
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer| 是 |规则组id|
|» strategy_name|body|string| 否 |规则组名称|
|» admin_list|body|[string]| 否 |管理员列表，如果为空则不对负责人做编辑，如果有则覆盖旧的负责人列表|
|» privilege|body|object| 否 |权限配置，如果为空则不对权限做编辑，如果有则覆盖旧的权限配置|
|»» view_customer_list|body|boolean| 是 |none|
|»» view_customer_data|body|boolean| 是 |none|
|»» view_room_list|body|boolean| 是 |none|
|»» contact_me|body|boolean| 是 |none|
|»» join_room|body|boolean| 是 |none|
|»» share_customer|body|boolean| 是 |none|
|»» oper_resign_customer|body|boolean| 是 |none|
|»» oper_resign_group|body|boolean| 是 |none|
|»» send_customer_msg|body|boolean| 是 |none|
|»» edit_welcome_msg|body|boolean| 是 |none|
|»» view_behavior_data|body|boolean| 是 |none|
|»» view_room_data|body|boolean| 是 |none|
|»» send_group_msg|body|boolean| 是 |none|
|»» room_deduplication|body|boolean| 是 |none|
|»» rapid_reply|body|boolean| 是 |none|
|»» onjob_customer_transfer|body|boolean| 是 |none|
|»» edit_anti_spam_rule|body|boolean| 是 |none|
|»» export_customer_list|body|boolean| 是 |none|
|»» export_customer_data|body|boolean| 是 |none|
|»» export_customer_group_list|body|boolean| 是 |none|
|»» manage_customer_tag|body|boolean| 是 |none|
|» range_add|body|[object]| 否 |none|
|»» type|body|integer| 是 |向管理范围添加的节点类型 1-成员 2-部门|
|»» userid|body|string| 否 |向管理范围添加成员的userid,仅type为1时有效|
|»» partyid|body|integer| 否 |向管理范围添加部门的partyid，仅type为2时有效|
|» range_del|body|[object]| 否 |none|
|»» type|body|integer| 否 |从管理范围删除的节点类型 1-成员 2-部门|
|»» userid|body|string| 否 |从管理范围删除的成员的userid,仅type为1时有效|
|»» partyid|body|integer| 否 |从管理范围删除的部门的partyid，仅type为2时有效|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|

# 企业内部开发/客户联系/客户标签管理

## POST 编辑客户企业标签

POST /cgi-bin/externalcontact/mark_tag

企业可通过此接口为指定成员的客户添加上由企业统一配置的标签。

**权限说明：**

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/92118#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/92118#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/92118#10013/第三步：获取access_token)）
- 第三方应用需具有“企业客户权限->客户基础信息”权限
- 如果要使用某个规则组下的企业客户标签，则仅可使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/92118#13473/开始开发)来获取accesstoken进行调用，并且`userid`要在此规则组的管理范围内

文档ID: 16666
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92118
https://open.work.weixin.qq.com/api/doc/90001/90143/92697

> Body 请求参数

```json
{
  "userid": "zhangsan",
  "external_userid": "woAJ2GCAAAd1NPGHKSD4wKmE8Aabj9AAA",
  "add_tag": [
    "TAGID1",
    "TAGID2"
  ],
  "remove_tag": [
    "TAGID3",
    "TAGID4"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» external_userid|body|string| 是 |外部联系人userid|
|» add_tag|body|[string]| 否 |要标记的标签列表|
|» userid|body|string| 是 |添加外部联系人的userid|
|» remove_tag|body|[string]| 否 |要移除的标签列表|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户标签管理/管理企业标签

## POST 管理企业标签-获取企业标签库

POST /cgi-bin/externalcontact/get_corp_tag_list

企业可通过此接口获取企业客户标签详情。
文档ID: 17298
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92117
https://open.work.weixin.qq.com/api/doc/90001/90143/92696

> Body 请求参数

```json
{
  "tag_id": [
    "etXXXXXXXXXX",
    "etYYYYYYYYYY"
  ],
  "group_id": [
    "etZZZZZZZZZZZZZ",
    "etYYYYYYYYYYYYY"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» group_id|body|[string]| 否 |要查询的标签组id，返回该标签组以及其下的所有标签信息|
|» tag_id|body|[string]| 否 |要查询的标签id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "tag_group": [
    {
      "group_id": "TAG_GROUPID1",
      "group_name": "GOURP_NAME",
      "create_time": 1557838797,
      "order": 1,
      "deleted": false,
      "tag": [
        {
          "id": "TAG_ID1",
          "name": "NAME1",
          "create_time": 1557838797,
          "order": 1,
          "deleted": false
        },
        {
          "id": "TAG_ID2",
          "name": "NAME2",
          "create_time": 1557838797,
          "order": 2,
          "deleted": true
        }
      ]
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» tag_group|[object]|false|none||标签组列表|
|»» deleted|boolean|false|none||none|
|»» create_time|integer(int32)|false|none||none|
|»» group_id|string|false|none||none|
|»» group_name|string|false|none||none|
|»» tag|[object]|false|none||none|
|»»» deleted|boolean|false|none||none|
|»»» create_time|integer(int32)|false|none||none|
|»»» name|string|false|none||none|
|»»» id|string|false|none||none|
|»»» order|integer(int32)|false|none||none|
|»» order|integer(int32)|false|none||none|

## POST 管理企业标签-删除企业客户标签

POST /cgi-bin/externalcontact/del_corp_tag

企业可通过此接口删除客户标签库中的标签，或删除整个标签组。

tag_id和group_id不可同时为空。
如果一个标签组下所有的标签均被删除，则标签组会被自动删除。

文档ID: 17298
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92117
https://open.work.weixin.qq.com/api/doc/90001/90143/92696

> Body 请求参数

```json
{
  "tag_id": [
    "TAG_ID_1",
    "TAG_ID_2"
  ],
  "group_id": [
    "GROUP_ID_1",
    "GROUP_ID_2"
  ],
  "agentid": 1000014
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» agentid|body|integer(int32)| 否 |授权方安装的应用agentid。仅旧的第三方多应用套件需要填此参数|
|» group_id|body|[string]| 否 |标签组的id列表|
|» tag_id|body|[string]| 否 |标签的id列表|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 管理企业标签-添加企业客户标签

POST /cgi-bin/externalcontact/add_corp_tag

企业可通过此接口向客户标签库中添加新的标签组和标签，每个企业最多可配置3000个企业标签。
注意:
如果要向指定的标签组下添加标签，需要填写group_id参数；如果要创建一个全新的标签组以及标签，则需要通过group_name参数指定新标签组名称，如果填写的groupname已经存在，则会在此标签组下新建标签。
如果填写了group_id参数，则group_name和标签组的order参数会被忽略。
不支持创建空标签组。
标签组内的标签不可同名，如果传入多个同名标签，则只会创建一个。

文档ID: 17298
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92117
https://open.work.weixin.qq.com/api/doc/90001/90143/92696

> Body 请求参数

```json
{
  "group_id": "GROUP_ID",
  "group_name": "GROUP_NAME",
  "order": 1,
  "tag": [
    {
      "name": "TAG_NAME_1",
      "order": 1
    },
    {
      "name": "TAG_NAME_2",
      "order": 2
    }
  ],
  "agentid": 1000014
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» agentid|body|integer(int32)| 否 |授权方安装的应用agentid。仅旧的第三方多应用套件需要填此参数|
|» group_id|body|string| 否 |标签组id|
|» group_name|body|string| 否 |标签组名称，最长为30个字符|
|» tag|body|[object]| 否 |none|
|»» name|body|string| 否 |none|
|»» order|body|integer(int32)| 否 |标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|
|» order|body|integer(int32)| 否 |标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "tag_group": {
    "group_id": "TAG_GROUPID1",
    "group_name": "GOURP_NAME",
    "create_time": 1557838797,
    "order": 1,
    "tag": [
      {
        "id": "TAG_ID1",
        "name": "NAME1",
        "create_time": 1557838797,
        "order": 1
      },
      {
        "id": "TAG_ID2",
        "name": "NAME2",
        "create_time": 1557838797,
        "order": 2
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» tag_group|object|false|none||none|
|»» create_time|integer(int32)|false|none||标签组创建时间|
|»» group_id|string|false|none||标签组id|
|»» group_name|string|false|none||标签组名称|
|»» tag|[object]|false|none||标签组内的标签列表|
|»»» create_time|integer(int32)|false|none||标签创建时间|
|»»» name|string|false|none||新建标签名称|
|»»» id|string|false|none||新建标签id|
|»»» order|integer(int32)|false|none||标签次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|
|»» order|integer(int32)|false|none||标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

## POST 管理企业标签-编辑企业客户标签

POST /cgi-bin/externalcontact/edit_corp_tag

企业可通过此接口编辑客户标签/标签组的名称或次序值。
注意:修改后的标签组不能和已有的标签组重名，标签也不能和同一标签组下的其他标签重名。

文档ID: 17298
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92117
https://open.work.weixin.qq.com/api/doc/90001/90143/92696

> Body 请求参数

```json
{
  "id": "TAG_ID",
  "name": "NEW_TAG_NAME",
  "order": 1,
  "agentid": 1000014
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» agentid|body|integer(int32)| 否 |授权方安装的应用agentid。仅旧的第三方多应用套件需要填此参数|
|» name|body|string| 否 |新的标签或标签组名称，最长为30个字符|
|» id|body|string| 是 |标签或标签组的id|
|» order|body|integer(int32)| 否 |标签/标签组的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户标签管理/管理企业规则组下的客户标签

## POST 管理企业规则组下的客户标签-为指定规则组创建企业客户标签

POST /cgi-bin/externalcontact/add_strategy_tag

企业可通过此接口向规则组中添加新的标签组和标签，每个企业的企业标签和规则组标签合计最多可配置3000个。

注意:
如果填写了group_id参数，则group_name和标签组的order参数会被忽略。
如果填写的group_name和此规则组下的其他标签组同名，则会将相关标签加入已存在的同名标签组下
不支持创建空标签组。
标签组内的标签不可同名，如果传入多个同名标签，则只会创建一个。

文档ID: 32022
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94882

> Body 请求参数

```json
{
  "strategy_id": 1,
  "group_id": "GROUP_ID",
  "group_name": "GROUP_NAME",
  "order": 1,
  "tag": [
    {
      "name": "TAG_NAME_1",
      "order": 1
    },
    {
      "name": "TAG_NAME_2",
      "order": 2
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer| 是 |规则组id|
|» group_id|body|string| 否 |标签组id|
|» group_name|body|string| 否 |标签组名称，最长为30个字符|
|» order|body|integer| 否 |标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|
|» tag|body|[object]| 否 |none|
|»» name|body|string| 是 |添加的标签名称，最长为30个字符|
|»» order|body|integer| 否 |标签次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "tag_group": {
    "group_id": "TAG_GROUPID1",
    "group_name": "GOURP_NAME",
    "create_time": 1557838797,
    "order": 1,
    "tag": [
      {
        "id": "TAG_ID1",
        "name": "NAME1",
        "create_time": 1557838797,
        "order": 1
      },
      {
        "id": "TAG_ID2",
        "name": "NAME2",
        "create_time": 1557838797,
        "order": 2
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» tag_group|object|true|none||none|
|»» group_id|string|true|none||标签组id|
|»» group_name|string|true|none||标签组创建时间组名称|
|»» create_time|integer|true|none||none|
|»» order|integer|true|none||标签组次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|
|»» tag|[object]|true|none||标签组内的标签列表|
|»»» id|string|true|none||新建标签id|
|»»» name|string|true|none||新建标签名称|
|»»» create_time|integer|true|none||标签创建时间|
|»»» order|integer|true|none||标签次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

## POST 管理企业规则组下的客户标签-编辑指定规则组下的企业客户标签

POST /cgi-bin/externalcontact/edit_strategy_tag

企业可通过此接口编辑指定规则组下的客户标签/标签组的名称或次序值，但不可重新指定标签/标签组所属规则组。

注意:修改后的标签组不能和已有的标签组重名，标签也不能和同一标签组下的其他标签重名。

文档ID: 32022
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94882

> Body 请求参数

```json
"{\"id\":\"TAG_ID\",\"name\":\"NEW_TAG_NAME\",\"order\":1,}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 否 |新的标签或标签组名称，最长为30个字符|
|» id|body|string| 是 |标签或标签组的id|
|» order|body|integer(int32)| 否 |标签/标签组的次序值。order值大的排序靠前。有效的值范围是[0, 2^32)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 管理企业规则组下的客户标签-获取指定规则组下的企业客户标签

POST /cgi-bin/externalcontact/get_strategy_tag_list

**权限说明**:

- 仅可使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/94882#13473/开始开发)获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94882#10013/第三步：获取access_token)）

企业可通过此接口获取某个规则组内的企业客户标签详情

若tag_id和group_id均为空，则返回所有标签。
同时传递tag_id和group_id时，忽略tag_id，仅以group_id作为过滤条件。

文档ID: 32022
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94882

> Body 请求参数

```json
{
  "strategy_id": 1,
  "tag_id": [
    "etXXXXXXXXXX",
    "etYYYYYYYYYY"
  ],
  "group_id": [
    "etZZZZZZZZZZZZZ",
    "etYYYYYYYYYYYYY"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» group_id|body|[string]| 否 |要查询的标签组id，返回该标签组以及其下的所有标签信息|
|» strategy_id|body|integer(int32)| 否 |规则组id|
|» tag_id|body|[string]| 否 |要查询的标签id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "tag_group": [
    {
      "group_id": "TAG_GROUPID1",
      "group_name": "GOURP_NAME",
      "create_time": 1557838797,
      "order": 1,
      "strategy_id": 1,
      "tag": [
        {
          "id": "TAG_ID1",
          "name": "NAME1",
          "create_time": 1557838797,
          "order": 1
        },
        {
          "id": "TAG_ID2",
          "name": "NAME2",
          "create_time": 1557838797,
          "order": 2
        }
      ]
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» tag_group|[object]|false|none||标签组列表|
|»» create_time|integer(int32)|false|none||none|
|»» group_id|string|false|none||none|
|»» group_name|string|false|none||none|
|»» strategy_id|integer(int32)|false|none||none|
|»» tag|[object]|false|none||none|
|»»» create_time|integer(int32)|false|none||none|
|»»» name|string|false|none||none|
|»»» id|string|false|none||none|
|»»» order|integer(int32)|false|none||none|
|»» order|integer(int32)|false|none||none|

## POST 管理企业规则组下的客户标签-删除指定规则组下的企业客户标签

POST /cgi-bin/externalcontact/del_strategy_tag

企业可通过此接口删除某个规则组下的标签，或删除整个标签组。

文档ID: 32022
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94882

**参数说明**:

> tag_id和group_id不可同时为空。
> 如果一个标签组下所有的标签均被删除，则标签组会被自动删除。

 

> Body 请求参数

```json
"{\"tag_id\":[\"TAG_ID_1\",\"TAG_ID_2\"],\"group_id\":[\"GROUP_ID_1\",\"GROUP_ID_2\"],}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» group_id|body|[string]| 否 |标签组的id列表|
|» tag_id|body|[string]| 否 |标签的id列表|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/在职继承

## POST 查询客户接替状态

POST /cgi-bin/externalcontact/transfer_result

企业和第三方可通过此接口查询在职成员的客户转接情况。

权限说明：企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。 第三方应用需拥有“企业客户权限->客户联系->在职继承”权限 接替成员必须在此第三方应用或自建应用的可见范围内。

文档ID: 27284
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94088
https://open.work.weixin.qq.com/api/doc/90001/90143/94097

原接口[查询客户接替结果](https://developer.work.weixin.qq.com/document/path/94088#23225)后续将不再更新维护，请使用新接口

> Body 请求参数

```json
{
  "handover_userid": "zhangsan",
  "takeover_userid": "lisi",
  "cursor": "CURSOR"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» handover_userid|body|string| 是 |原添加成员的userid|
|» cursor|body|string| 否 |分页查询的cursor，每个分页返回的数据不会超过1000条；不填或为空表示获取第一个分页；|
|» takeover_userid|body|string| 是 |接替成员的userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "customer": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACCCC",
      "status": 1,
      "takeover_time": 1588262400
    },
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB",
      "status": 2,
      "takeover_time": 1588482400
    },
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
      "status": 3,
      "takeover_time": 0
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||下个分页的起始cursor|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» customer|[object]|false|none||none|
|»» external_userid|string|false|none||转接客户的外部联系人userid|
|»» takeover_time|integer(int32)|false|none||接替客户的时间，如果是等待接替状态，则为未来的自动接替时间|
|»» status|integer(int32)|false|none||接替状态， 1-接替完毕 2-等待接替 3-客户拒绝 4-接替成员客户达到上限 5-无接替记录|

## POST 分配在职成员的客户

POST /cgi-bin/externalcontact/transfer_customer

企业可通过此接口，转接在职成员的客户给其他成员

external_userid必须是handover_userid的客户（即配置了客户联系功能的成员所添加的联系人）。

为保障客户服务体验，90个自然日内，在职成员的每位客户仅可被转接2次。

权限说明：企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。 第三方应用需拥有“企业客户权限->客户联系->在职继承”权限 接替成员必须在此第三方应用或自建应用的可见范围内。 接替成员需要配置了客户联系功能。 接替成员需要在企业微信激活且已经过实名认证。

文档ID: 27271
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92125
https://open.work.weixin.qq.com/api/doc/90001/90143/94096

参数说明：原接口[分配在职或离职成员的客户](https://developer.work.weixin.qq.com/document/path/92125#14020)后续将不再更新维护，请使用新接口

> Body 请求参数

```json
{
  "handover_userid": "zhangsan",
  "takeover_userid": "lisi",
  "external_userid": [
    "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
    "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB"
  ],
  "transfer_success_msg": "您好，您的服务已升级，后续将由我的同事李四@腾讯接替我的工作，继续为您服务。"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» handover_userid|body|string| 是 |原跟进成员的userid|
|» external_userid|body|[string]| 是 |客户的external_userid列表，每次最多分配100个客户|
|» transfer_success_msg|body|string| 否 |转移成功后发给客户的消息，最多200个字符，不填则使用默认文案|
|» takeover_userid|body|string| 是 |接替成员的userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "customer": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
      "errcode": 40096
    },
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB",
      "errcode": 0
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» customer|[object]|false|none||none|
|»» errcode|integer(int32)|false|none||对此客户进行分配的结果, 具体可参考[全局错误码](https://developer.work.weixin.qq.com/document/path/92125#10649), **0表示成功发起接替,待24小时后自动接替,并不代表最终接替成功**|
|»» external_userid|string|false|none||客户的external_userid|

## POST 分配在职成员的客户群

POST /cgi-bin/externalcontact/groupchat/onjob_transfer

企业可通过此接口，将在职成员为群主的群，分配给另一个客服成员。

**注意：**

1. 继承给的新群主，必须是配置了客户联系功能的成员
2. 继承给的新群主，必须有设置实名
3. 继承给的新群主，必须有激活企业微信
4. 同一个人的群，限制每天最多分配300个给新群主

> 为保障客户服务体验，90个自然日内，在职成员的每个客户群仅可被转接2次。

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/95703#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/95703#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95703#10013/第三步：获取access_token)）。
- 第三方应用需拥有“企业客户权限->客户联系->分配在职成员的客户群”权限
- 对于第三方/自建应用，群主必须在应用的可见范围。

> Body 请求参数

```json
{
  "chat_id_list": [
    "wrOgQhDgAAcwMTB7YmDkbeBsgT_AAAA",
    "wrOgQhDgAAMYQiS5ol9G7gK9JVQUAAAA"
  ],
  "new_owner": "zhangsan"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» chat_id_list|body|[string]| 是 |需要转群主的客户群ID列表。取值范围： 1 ~ 100|
|» new_owner|body|string| 是 |新群主ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "failed_chat_list": [
    {
      "chat_id": "wrOgQhDgAAcwMTB7YmDkbeBsgT_KAAAA",
      "errcode": 90501,
      "errmsg": "chat is not external group chat"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» failed_chat_list|[object]|true|none||没能成功继承的群|
|»» chat_id|string|false|none||没能成功继承的群ID|
|»» errcode|integer|false|none||没能成功继承的群，错误码|
|»» errmsg|string|false|none||没能成功继承的群，错误描述|

# 企业内部开发/客户联系/离职继承

## POST 分配离职成员的客户群

POST /cgi-bin/externalcontact/groupchat/transfer

企业可通过此接口，将已离职成员为群主的群，分配给另一个客服成员。

注意：：

群主离职了的客户群，才可继承
继承给的新群主，必须是配置了客户联系功能的成员
继承给的新群主，必须有设置实名
继承给的新群主，必须有激活企业微信
同一个人的群，限制每天最多分配300个给新群主
权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用需拥有“企业客户权限->客户联系->分配离职成员的客户群”权限
对于第三方/自建应用，群主必须在应用的可见范围。

文档ID: 19416
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93242
https://open.work.weixin.qq.com/api/doc/90000/90135/92127

> Body 请求参数

```json
{
  "chat_id_list": [
    "wrOgQhDgAAcwMTB7YmDkbeBsgT_AAAA",
    "wrOgQhDgAAMYQiS5ol9G7gK9JVQUAAAA"
  ],
  "new_owner": "zhangsan"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» chat_id_list|body|[string]| 是 |需要转群主的客户群ID列表。取值范围： 1 ~ 100|
|» new_owner|body|string| 是 |新群主ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "failed_chat_list": [
    {
      "chat_id": "wrOgQhDgAAcwMTB7YmDkbeBsgT_KAAAA",
      "errcode": 90500,
      "errmsg": "the owner of this chat is not resigned"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» failed_chat_list|[object]|false|none||没能成功继承的群|
|»» errcode|integer(int32)|false|none||没能成功继承的群，错误码|
|»» errmsg|string|false|none||对返回码的文本描述内容|
|»» chat_id|string|false|none||没能成功继承的群ID|

## POST 分配离职成员的客户

POST /cgi-bin/externalcontact/resigned/transfer_customer

企业可通过此接口，分配离职成员的客户给其他成员。

handover_userid必须是已离职用户。
external_userid必须是handover_userid的客户（即配置了客户联系功能的成员所添加的联系人）。

权限说明：企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。 第三方应用需拥有“企业客户权限->客户联系->离职分配”权限 接替成员必须在此第三方应用或自建应用的可见范围内。 接替成员需要配置了客户联系功能。 接替成员需要在企业微信激活且已经过实名认证。

文档ID: 27272
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94081
https://open.work.weixin.qq.com/api/doc/90001/90143/94100

> Body 请求参数

```json
{
  "handover_userid": "zhangsan",
  "takeover_userid": "lisi",
  "external_userid": [
    "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB",
    "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» handover_userid|body|string| 是 |原跟进成员的userid|
|» external_userid|body|[string]| 是 |客户的external_userid列表，最多一次转移100个客户|
|» takeover_userid|body|string| 是 |接替成员的userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "customer": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB",
      "errcode": 0
    },
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
      "errcode": 40096
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||对此客户进行分配的结果, 具体可参考全局错误码, 0表示开始分配流程,待24小时后自动接替,并不代表最终分配成功|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» customer|[object]|false|none||none|
|»» errcode|integer(int32)|false|none||返回码|
|»» external_userid|string|false|none||客户的external_userid|

## POST 查询客户接替状态

POST /cgi-bin/externalcontact/resigned/transfer_result

权限说明：企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。 第三方应用需拥有“企业客户权限->客户联系->在职继承”权限 接替成员必须在此第三方应用或自建应用的可见范围内。

文档ID: 27286
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94082
https://open.work.weixin.qq.com/api/doc/90001/90143/94101

> Body 请求参数

```json
{
  "handover_userid": "zhangsan",
  "takeover_userid": "lisi",
  "cursor": "CURSOR"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» handover_userid|body|string| 是 |原添加成员的userid|
|» cursor|body|string| 否 |分页查询的cursor，每个分页返回的数据不会超过1000条；不填或为空表示获取第一个分页|
|» takeover_userid|body|string| 是 |接替成员的userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "customer": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACCCC",
      "status": 1,
      "takeover_time": 1588262400
    },
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB",
      "status": 2,
      "takeover_time": 1588482400
    },
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
      "status": 3,
      "takeover_time": 0
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||下个分页的起始cursor|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» customer|[object]|false|none||none|
|»» external_userid|string|false|none||转接客户的外部联系人userid|
|»» takeover_time|integer(int32)|false|none||接替客户的时间，如果是等待接替状态，则为未来的自动接替时间|
|»» status|integer(int32)|false|none||接替状态， 1-接替完毕 2-等待接替 3-客户拒绝 4-接替成员客户达到上限|

## POST 获取待分配的离职成员列表

POST /cgi-bin/externalcontact/get_unassigned_list

企业和第三方可通过此接口，获取所有离职成员的客户列表，并可进一步调用[分配离职成员的客户](https://developer.work.weixin.qq.com/document/path/92124#27272)接口将这些客户重新分配给其他企业成员。

**权限说明：**

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/92124#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/92124#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/92124#10013/第三步：获取access_token)）
- 第三方应用需拥有“企业客户权限->客户联系->分配离职成员的客户”权限

> Body 请求参数

```json
{
  "cursor": "",
  "page_size": 100
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |分页查询游标，字符串类型，适用于数据量较大的情况，如果使用该参数则无需填写page_id，该参数由上一次调用返回|
|» page_size|body|integer| 否 |每次返回的最大记录数，默认为1000，最大值为1000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "info": [
    {
      "handover_userid": "zhangsan",
      "external_userid": "woAJ2GCAAAd4uL12hdfsdasassdDmAAAAA",
      "dimission_time": 1550838571
    },
    {
      "handover_userid": "lisi",
      "external_userid": "wmAJ2GCAAAzLTI123ghsdfoGZNqqAAAA",
      "dimission_time": 1550661468
    }
  ],
  "is_last": false,
  "next_cursor": "aSfwejksvhToiMMfFeIGZZ"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» info|[object]|true|none||none|
|»» handover_userid|string|true|none||离职成员的userid|
|»» external_userid|string|true|none||外部联系人userid|
|»» dimission_time|integer|true|none||成员离职时间|
|» is_last|boolean|true|none||是否是最后一条记录|
|» next_cursor|string|true|none||分页查询游标,已经查完则返回空(“”)|

# 企业内部开发/客户联系/客户群管理

## POST 客户群opengid转换

POST /cgi-bin/externalcontact/opengid_to_chatid

用户在微信里的客户群里打开小程序时，某些场景下可以获取到群的opengid，如果该群是企业微信的客户群，则企业或第三方可以调用此接口将一个opengid转换为客户群chat_id

权限说明：企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？） 第三方应用需具有“企业客户权限->客户基础信息”权限 对于第三方/自建应用，群主必须在应用的可见范围 仅支持企业服务人员创建的客户群 仅可转换出自己企业下的客户群chat_id

文档ID: 31650
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94822

> Body 请求参数

```json
{
  "opengid": "oAAAAAAA"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» opengid|body|string| 是 |小程序在微信获取到的群ID，参见wx.getGroupEnterInfo|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "chat_id": "ooAAAAAAAAAAA"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» chat_id|string|false|none||客户群ID，可以用来调用获取客户群详情|

## POST 获取客户群列表

POST /cgi-bin/externalcontact/groupchat/list

该接口用于获取配置过客户群管理的客户群列表。

如果不指定 owner_filter，会拉取应用可见范围内的所有群主的数据，但是不建议这样使用。如果可见范围内人数超过1000人，为了防止数据包过大，会报错 81017。此时，调用方需通过指定 owner_filter 来缩小拉取范围
旧版接口以offset+limit分页，要求offset+limit不能超过50000，该方案将废弃，请改用cursor+limit分页

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用需具有“企业客户权限->客户基础信息”权限
对于第三方/自建应用，群主必须在应用的可见范围。

文档ID: 19409
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92120
https://open.work.weixin.qq.com/api/doc/90001/90143/93414

> Body 请求参数

```json
{
  "status_filter": 0,
  "owner_filter": {
    "userid_list": [
      "abel"
    ]
  },
  "cursor": "r9FqSqsI8fgNbHLHE5QoCP50UIg2cFQbfma3l2QsmwI",
  "limit": 10
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» status_filter|body|integer| 是 |{     "status_filter": 0,     "owner_filter": {         "userid_list": ["abel"]     },     "cursor" : "r9FqSqsI8fgNbHLHE5QoCP50UIg2cFQbfma3l2QsmwI",     "limit" : 10 }|
|» owner_filter|body|object| 否 |群主过滤。 如果不填，表示获取应用可见范围内全部群主的数据（但是不建议这么用，如果可见范围人数超过1000人，为了防止数据包过大，会报错 81017）|
|»» userid_list|body|[string]| 否 |用户ID列表。最多100个|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用不填|
|» limit|body|integer| 是 |分页，预期请求的数据量，取值范围 1 ~ 1000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "group_chat_list": [
    {
      "chat_id": "wrOgQhDgAAMYQiS5ol9G7gK9JVAAAA",
      "status": 0
    },
    {
      "chat_id": "wrOgQhDgAAcwMTB7YmDkbeBsAAAA",
      "status": 0
    }
  ],
  "next_cursor": "tJzlB9tdqfh-g7i_J-ehOz_TWcd7dSKa39_AqCIeMFw"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，下次请求时填写以获取之后分页的记录。如果该字段返回空则表示已没有更多数据|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» group_chat_list|[object]|false|none||客户群列表|
|»» chat_id|string|false|none||客户群ID|
|»» status|integer(int32)|false|none||客户群跟进状态。 0 - 跟进人正常 1 - 跟进人离职 2 - 离职继承中 3 - 离职继承完成|

## POST 获取客户群详情

POST /cgi-bin/externalcontact/groupchat/get

通过客户群ID，获取详情。包括群名、群成员列表、群成员入群时间、入群方式。（客户群是由具有客户群使用权限的成员创建的外部群）

需注意的是，如果发生群信息变动，会立即收到群变更事件，但是部分信息是异步处理，可能需要等一段时间调此接口才能得到最新结果

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）
第三方应用需具有“企业客户权限->客户基础信息”权限
对于第三方/自建应用，群主必须在应用的可见范围。

文档ID: 19412
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92122
https://open.work.weixin.qq.com/api/doc/90001/90143/92707

> Body 请求参数

```json
{
  "chat_id": "wrOgQhDgAAMYQiS5ol9G7gK9JVAAAA",
  "need_name": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» chat_id|body|string| 是 |客户群ID|
|» need_name|body|integer| 否 |是否需要返回群成员的名字group_chat.member_list.name。0-不返回；1-返回。默认不返回|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "group_chat": {
    "chat_id": "wrOgQhDgAAMYQiS5ol9G7gK9JVAAAA",
    "name": "销售客服群",
    "owner": "ZhuShengBen",
    "create_time": 1572505490,
    "notice": "文明沟通，拒绝脏话",
    "member_list": [
      {
        "userid": "abel",
        "type": 1,
        "join_time": 1572505491,
        "join_scene": 1,
        "invitor": {
          "userid": "jack"
        },
        "group_nickname": "客服小张",
        "name": "张三丰"
      },
      {
        "userid": "wmOgQhDgAAuXFJGwbve4g4iXknfOAAAA",
        "type": 2,
        "unionid": "ozynqsulJFCZ2z1aYeS8h-nuasdAAA",
        "join_time": 1572505491,
        "join_scene": 1,
        "group_nickname": "顾客老王",
        "name": "王语嫣"
      }
    ],
    "admin_list": [
      {
        "userid": "sam"
      },
      {
        "userid": "pony"
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» group_chat|object|false|none||客户群详情|
|»» owner|string|false|none||群主ID|
|»» create_time|integer(int32)|false|none||群的创建时间|
|»» name|string|false|none||群名|
|»» member_list|[object]|false|none||群成员列表|
|»»» group_nickname|string|false|none||none|
|»»» unionid|string|false|none||外部联系人在微信开放平台的唯一身份标识（微信unionid），通过此字段企业可将外部联系人与公众号/小程序用户关联起来。仅当群成员类型是微信用户（包括企业成员未添加好友），且企业或第三方服务商绑定了微信开发者ID有此字段。查看绑定方法|
|»»» name|string|false|none||none|
|»»» join_scene|integer(int32)|false|none||入群方式。 1 - 由群成员邀请入群（直接邀请入群） 2 - 由群成员邀请入群（通过邀请链接入群） 3 - 通过扫描群二维码入群|
|»»» type|integer(int32)|false|none||成员类型。 1 - 企业成员 2 - 外部联系人|
|»»» userid|string|false|none||群成员id|
|»»» join_time|integer(int32)|false|none||入群时间|
|»» admin_list|[object]|false|none||群管理员列表|
|»»» userid|string|false|none||none|
|»» chat_id|string|false|none||客户群ID|
|»» notice|string|false|none||群公告|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/联系我与客户入群方式/客户联系「联系我」管理

## POST 配置客户联系「联系我」方式

POST /cgi-bin/externalcontact/add_contact_way

企业可以在管理后台-客户联系-加客户中配置成员的「联系我」的二维码或者小程序按钮，客户通过扫描二维码或点击小程序上的按钮，即可获取成员联系方式，主动联系到成员。
企业可通过此接口为具有客户联系功能的成员生成专属的「联系我」二维码或者「联系我」按钮。
如果配置的是「联系我」按钮，需要开发者的小程序接入[小程序插件](https://developer.work.weixin.qq.com/document/path/92228#15517)。

**注意:**
通过API添加的「联系我」不会在管理端进行展示，每个企业可通过API最多配置**50万**个「联系我」。
用户需要妥善存储返回的config_id，**config_id丢失可能导致用户无法编辑或删除「联系我」**。
临时会话模式不占用「联系我」数量，但每日最多添加**10万**个，并且仅支持单人。
临时会话模式的二维码，添加好友完成后该二维码即刻失效。

> Body 请求参数

```json
{
  "type": 0,
  "scene": 0,
  "style": 0,
  "remark": "string",
  "skip_verify": true,
  "state": "string",
  "user": [
    "string"
  ],
  "party": [
    0
  ],
  "is_temp": true,
  "expires_in": 0,
  "chat_expires_in": 0,
  "unionid": "string",
  "conclusions": {
    "text": {
      "content": "string"
    },
    "image": {
      "media_id": "string"
    },
    "link": {
      "title": "string",
      "picurl": "string",
      "desc": "string",
      "url": "string"
    },
    "miniprogram": {
      "title": "string",
      "pic_media_id": "string",
      "appid": "string",
      "page": "string"
    }
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» type|body|integer| 是 |联系方式类型,1-单人, 2-多人|
|» scene|body|integer| 是 |场景，1-在小程序中联系，2-通过二维码联系|
|» style|body|integer| 否 |在小程序中联系时使用的控件样式，详见附表|
|» remark|body|string| 否 |联系方式的备注信息，用于助记，不超过30个字符|
|» skip_verify|body|boolean| 否 |外部客户添加时是否无需验证，默认为true|
|» state|body|string| 否 |企业自定义的state参数，用于区分不同的添加渠道，在调用“[获取外部联系人详情](https://developer.work.weixin.qq.com/document/path/92228#13878)”时会返回该参数值，不超过30个字符|
|» user|body|[string]| 否 |使用该联系方式的用户userID列表，在type为1时为必填，且只能有一个|
|» party|body|[integer]| 是 |使用该联系方式的部门id列表，只在type为2时有效|
|» is_temp|body|boolean| 是 |是否临时会话模式，true表示使用临时会话模式，默认为false|
|» expires_in|body|integer| 是 |临时会话二维码有效期，以秒为单位。该参数仅在is_temp为true时有效，默认7天，最多为14天|
|» chat_expires_in|body|integer| 是 |临时会话有效期，以秒为单位。该参数仅在is_temp为true时有效，默认为添加好友后24小时，最多为14天|
|» unionid|body|string| 是 |可进行临时会话的客户unionid，该参数仅在is_temp为true时有效，如不指定则不进行限制|
|» conclusions|body|object| 是 |结束语，会话结束时自动发送给客户，可参考“[结束语定义](https://developer.work.weixin.qq.com/document/path/92228#15645/结束语定义)”，仅在is_temp为true时有效|
|»» text|body|object| 是 |none|
|»»» content|body|string| 是 |none|
|»» image|body|object| 是 |none|
|»»» media_id|body|string| 是 |none|
|»» link|body|object| 是 |none|
|»»» title|body|string| 是 |none|
|»»» picurl|body|string| 是 |none|
|»»» desc|body|string| 是 |none|
|»»» url|body|string| 是 |none|
|»» miniprogram|body|object| 是 |none|
|»»» title|body|string| 是 |none|
|»»» pic_media_id|body|string| 是 |none|
|»»» appid|body|string| 是 |none|
|»»» page|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "config_id": "42b34949e138eb6e027c123cba77fAAA",
  "qr_code": "http://p.qpic.cn/wwhead/duc2TvpEgSdicZ9RrdUtBkv2UiaA/0"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» config_id|string|true|none||新增联系方式的配置id|
|» qr_code|string|true|none||联系我二维码链接，仅在scene为2时返回|

## POST 结束临时会话

POST /cgi-bin/externalcontact/close_temp_chat

将指定的企业成员和客户之前的临时会话断开，断开前会自动下发已配置的结束语。
注意：请保证传入的企业成员和客户之间有仍然有效的临时会话, 通过其他方式的添加外部联系人无法通过此接口关闭会话。

> Body 请求参数

```json
{
  "userid": "zhangyisheng",
  "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACHAAA"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» userid|body|string| 是 |企业成员的userid|
|» external_userid|body|string| 是 |客户的外部联系人userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

## POST 更新企业已配置的「联系我」方式

POST /cgi-bin/externalcontact/update_contact_way

更新企业配置的「联系我」二维码和「联系我」小程序按钮中的信息，如使用人员和备注等。
注意：已失效的临时会话联系方式无法进行编辑
当临时会话模式时（即is_temp为true），联系人仅支持配置为单人，暂不支持多人

> Body 请求参数

```json
{
  "config_id": "42b34949e138eb6e027c123cba77fAAA",
  "remark": "渠道客户",
  "skip_verify": true,
  "style": 1,
  "state": "teststate",
  "user": [
    "zhangsan",
    "lisi",
    "wangwu"
  ],
  "party": [
    2,
    3
  ],
  "expires_in": 86400,
  "chat_expires_in": 86400,
  "unionid": "oxTWIuGaIt6gTKsQRLau2M0AAAA",
  "conclusions": {
    "text": {
      "content": "文本消息内容"
    },
    "image": {
      "media_id": "MEDIA_ID"
    },
    "link": {
      "title": "消息标题",
      "picurl": "https://example.pic.com/path",
      "desc": "消息描述",
      "url": "https://example.link.com/path"
    },
    "miniprogram": {
      "title": "消息标题",
      "pic_media_id": "MEDIA_ID",
      "appid": "wx8bd80126147dfAAA",
      "page": "/path/index"
    }
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |	调用接口凭证|
|body|body|object| 否 |none|
|» config_id|body|string| 是 |企业联系方式的配置id|
|» remark|body|string| 否 |联系方式的备注信息，不超过30个字符，将覆盖之前的备注|
|» skip_verify|body|boolean| 否 |外部客户添加时是否无需验证|
|» style|body|integer| 否 |样式，只针对“在小程序中联系”的配置生效|
|» state|body|string| 否 |企业自定义的state参数，用于区分不同的添加渠道，在调用“获取外部联系人详情”时会返回该参数值|
|» user|body|[string]| 否 |使用该联系方式的用户列表，将覆盖原有用户列表|
|» party|body|[integer]| 否 |使用该联系方式的部门列表，将覆盖原有部门列表，只在配置的type为2时有效|
|» expires_in|body|integer| 否 |临时会话二维码有效期，以秒为单位，该参数仅在临时会话模式下有效|
|» chat_expires_in|body|integer| 否 |临时会话有效期，以秒为单位，该参数仅在临时会话模式下有效|
|» unionid|body|string| 否 |可进行临时会话的客户unionid，该参数仅在临时会话模式有效，如不指定则不进行限制|
|» conclusions|body|object| 否 |结束语，会话结束时自动发送给客户，可参考“结束语定义”，仅临时会话模式（is_temp为true）可设置|
|»» text|body|object| 是 |none|
|»»» content|body|string| 是 |none|
|»» image|body|object| 是 |none|
|»»» media_id|body|string| 是 |none|
|»» link|body|object| 是 |none|
|»»» title|body|string| 是 |none|
|»»» picurl|body|string| 是 |none|
|»»» desc|body|string| 是 |none|
|»»» url|body|string| 是 |none|
|»» miniprogram|body|object| 是 |none|
|»»» title|body|string| 是 |none|
|»»» pic_media_id|body|string| 是 |none|
|»»» appid|body|string| 是 |none|
|»»» page|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

## POST 删除企业已配置的「联系我」方式

POST /cgi-bin/externalcontact/del_contact_way

删除一个已配置的「联系我」二维码或者「联系我」小程序按钮。

> Body 请求参数

```json
{
  "config_id": "42b34949e138eb6e027c123cba77fAAA"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» config_id|body|string| 是 |企业联系方式的配置id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/联系我与客户入群方式/客户群「加入群聊」管理

## POST 删除客户群进群方式配置

POST /cgi-bin/externalcontact/groupchat/del_join_way

{
	"config_id":"42b34949e138eb6e027c123cba77faaa"
}

> Body 请求参数

```json
{
  "config_id": "42b34949e138eb6e027c123cba77faaa"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» config_id|body|string| 是 |企业联系方式的配置id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

## POST 配置客户群进群方式

POST /cgi-bin/externalcontact/groupchat/add_join_way

企业可以在管理后台-客户联系中配置「加入群聊」的二维码或者小程序按钮，客户通过扫描二维码或点击小程序上的按钮，即可加入特定的客户群。
企业可通过此接口为具有客户联系功能的成员生成专属的二维码或者小程序按钮。
如果配置的是小程序按钮，需要开发者的小程序接入小程序插件。
注意:
通过API添加的配置不会在管理端进行展示，每个企业可通过API最多配置50万个「加入群聊」(与「联系我」共用50万的额度)。
room_base_name 和 room_base_id 两个参数配合，用于指定自动新建群的群名
例如，假如 room_base_name = "销售客服群", room_base_id = 10
那么，自动创建的第一个群，群名为“销售客服群10”；自动创建的第二个群，群名为“销售客服群11”，依次类推

> Body 请求参数

```json
{
  "scene": 2,
  "remark": "aa_remark",
  "auto_create_room": 1,
  "room_base_name": "销售客服群",
  "room_base_id": 10,
  "chat_id_list": [
    "wrOgQhDgAAH2Yy-CTZ6POca8mlBEdaaa",
    "wrOgQhDgAALPUthpRAKvl7mgiQRwAAA"
  ],
  "state": "klsdup3kj3s1"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» scene|body|integer| 是 |场景。 1 - 群的小程序插件 2 - 群的二维码插件|
|» remark|body|string| 否 |联系方式的备注信息，用于助记，超过30个字符将被截断|
|» auto_create_room|body|integer| 否 |当群满了后，是否自动新建群。0-否；1-是。 默认为1|
|» room_base_name|body|string| 否 |自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符|
|» room_base_id|body|integer| 否 |自动建群的群起始序号，当auto_create_room为1时有效|
|» chat_id_list|body|[string]| 是 |使用该配置的客户群ID列表，支持5个。见[客户群ID获取方法](https://developer.work.weixin.qq.com/document/path/92229#19330)|
|» state|body|string| 否 |企业自定义的state参数，用于区分不同的入群渠道。不超过30个UTF-8字符 如果有设置此参数，在调用获取客户群详情接口时会返回每个群成员对应的该参数值，详见文末[附录2](https://developer.work.weixin.qq.com/document/path/92229#附录2：获取客户群详情，返回state参数)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "config_id": "9ad7fa5cdaa6511298498f979c472aaa"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» config_id|string|true|none||配置id|

## POST 获取客户群进群方式配置

POST /cgi-bin/externalcontact/groupchat/get_join_way

获取企业配置的群二维码或小程序按钮。

> Body 请求参数

```json
{
  "config_id": "9ad7fa5cdaa6511298498f979c472aaa"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» config_id|body|string| 是 |联系方式的配置id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "join_way": {
    "config_id": "9ad7fa5cdaa6511298498f979c472aaa",
    "type": 3,
    "scene": 3,
    "remark": "aa_remark",
    "auto_create_room": 1,
    "room_base_name": "销售客服群",
    "room_base_id": 10,
    "chat_id_list": [
      "wrOgQhDgAAH2Yy-CTZ6POca8mlBEdaaa",
      "wrOgQhDgAALPUthpRAKvl7mgiQRw_aaa"
    ],
    "qr_code": "http://p.qpic.cn/wwhead/nMl9ssowtibVGyrmvBiaibzDtp703nXuzpibnKtbSDBRJTLwS3ic4ECrf3ibLVtIFb0N6wWwy5LVuyvMQ22/0",
    "state": "klsdup3kj3s1"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» join_way|object|true|none||配置详情|
|»» config_id|string|true|none||新增联系方式的配置id|
|»» type|integer|true|none||none|
|»» scene|integer|true|none||场景。 1 - 群的小程序插件 2 - 群的二维码插件|
|»» remark|string|true|none||联系方式的备注信息，用于助记，超过30个字符将被截断|
|»» auto_create_room|integer|true|none||当群满了后，是否自动新建群。0-否；1-是。 默认为1|
|»» room_base_name|string|true|none||自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符|
|»» room_base_id|integer|true|none||自动建群的群起始序号，当auto_create_room为1时有效|
|»» chat_id_list|[string]|true|none||使用该配置的客户群ID列表。见[客户群ID获取方法](https://developer.work.weixin.qq.com/document/path/92229#19330)|
|»» qr_code|string|true|none||联系二维码的URL，仅在配置为群二维码时返回|
|»» state|string|true|none||企业自定义的state参数，用于区分不同的入群渠道。不超过30个UTF-8字符 如果有设置此参数，在调用获取客户群详情接口时会返回每个群成员对应的该参数值，详见文末[附录2](https://developer.work.weixin.qq.com/document/path/92229#附录2：获取客户群详情，返回state参数)|

## POST 更新客户群进群方式配置

POST /cgi-bin/externalcontact/groupchat/update_join_way

更新进群方式配置信息。注意：使用覆盖的方式更新。

> Body 请求参数

```json
{
  "config_id": "string",
  "scene": 0,
  "remark": "string",
  "auto_create_room": 0,
  "room_base_name": "string",
  "room_base_id": 0,
  "chat_id_list": [
    "string"
  ],
  "state": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» config_id|body|string| 是 |企业联系方式的配置id|
|» scene|body|integer| 是 |场景。 1 - 群的小程序插件 2 - 群的二维码插件|
|» remark|body|string| 是 |联系方式的备注信息，用于助记，超过30个字符将被截断|
|» auto_create_room|body|integer| 是 |当群满了后，是否自动新建群。0-否；1-是。 默认为1|
|» room_base_name|body|string| 是 |自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符|
|» room_base_id|body|integer| 是 |自动建群的群起始序号，当auto_create_room为1时有效|
|» chat_id_list|body|[string]| 是 |使用该配置的客户群ID列表，支持5个。见[客户群ID获取方法](https://developer.work.weixin.qq.com/document/path/92229#19330)|
|» state|body|string| 是 |企业自定义的state参数，用于区分不同的入群渠道。不超过30个UTF-8字符 如果有设置此参数，在调用获取客户群详情接口时会返回每个群成员对应的该参数值，详见文末[附录2](https://developer.work.weixin.qq.com/document/path/92229#附录2：获取客户群详情，返回state参数)|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户朋友圈

## GET 停止发表企业朋友圈

GET /cgi-bin/externalcontact/cancel_moment_task

企业和第三方应用可调用此接口，停止尚未发送的企业朋友圈发送任务。

**权限说明：**

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/97612#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/97612#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/97612#10013/第三步：获取access_token)）。
- 第三方应用或代开发自建应用调用需要企业授权**客户朋友圈**下**发表到成员客户的朋友圈**的权限

注意，此接口无法撤回已经发表到客户朋友圈的信息。

> Body 请求参数

```json
{
  "moment_id": "momXXXXXXXXXX"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» moment_id|body|string| 是 |朋友圈id，可通过[获取客户朋友圈企业发表的列表](https://developer.work.weixin.qq.com/document/path/97612#25254/获取客户朋友圈企业发表的列表)接口获取朋友圈企业发表的列表|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户朋友圈/企业发表内容到客户的朋友圈

## POST 企业发表内容到客户的朋友圈-创建发表任务

POST /cgi-bin/externalcontact/add_moment_task

企业和第三方应用可通过该接口创建客户朋友圈的发表任务。

可见范围说明

visible_range，分以下几种情况：

若只指定sender_list，则可见的客户范围为该部分执行者的客户，目前执行者支持传userid与部门id列表，注意不在应用可见范围内的执行者会被忽略。

若只指定external_contact_list，即指定了可见该朋友圈的目标客户，此时会将该发表任务推给这些目标客户的应用可见范围内的跟进人。

若同时指定sender_list以及external_contact_list，会将该发表任务推送给sender_list指定的且在应用可见范围内的执行者，执行者发表后仅external_contact_list指定的客户可见。

若未指定visible_range，则可见客户的范围为该应用可见范围内执行者的客户，执行者为应用可见范围内所有成员。

注：若指定external_contact_list列表，则该条朋友圈为部分可见；否则为公开

权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用或代开发自建应用调用需要企业授权客户朋友圈下发表到成员客户的朋友圈的权限

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

> Body 请求参数

```json
{
  "text": {
    "content": "文本消息内容"
  },
  "attachments": [
    {
      "msgtype": "image",
      "image": {
        "media_id": "MEDIA_ID"
      }
    },
    {
      "msgtype": "video",
      "video": {
        "media_id": "MEDIA_ID"
      }
    },
    {
      "msgtype": "link",
      "link": {
        "title": "消息标题",
        "url": "https://example.link.com/path",
        "media_id": "MEDIA_ID"
      }
    }
  ],
  "visible_range": {
    "sender_list": {
      "user_list": [
        "zhangshan",
        "lisi"
      ],
      "department_list": [
        2,
        3
      ]
    },
    "external_contact_list": {
      "tag_list": [
        "etXXXXXXXXXX",
        "etYYYYYYYYYY"
      ]
    }
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» text|body|object| 否 |文本消息|
|»» content|body|string| 是 |消息文本内容，最多支持传入2000个字符，若超出长度报错’invalid text size’|
|» attachments|body|[object]| 否 |附件，最多支持9个图片类型，或者1个视频，或者1个链接。类型只能三选一，若传了不同类型，报错’invalid attachments msgtype’|
|»» msgtype|body|string| 是 |附件类型，可选image、link或者video|
|»» image|body|object| 否 |图片消息附件。普通图片：建议不超过 1440 x 1080，长图片：长边建议不超过 10800px。图片不超过10M。最多支持传入9个；超过9个报错’invalid attachments size’|
|»»» media_id|body|string| 是 |图片的素材id。可通过上传附件资源接口获得|
|»» video|body|object| 否 |视频消息附件，建议不超过 1280 x 720，帧率 30 FPS，视频码率 1.67 Mbps，最长不超过30S，最大不超过10MB。只支持1个；若超过1个报错’invalid attachments size’|
|»»» media_id|body|string| 是 |视频的素材id，未填写报错”invalid msg”。可通过上传附件资源接口获得|
|»» link|body|object| 否 |图文消息附件。只支持1个；若超过1个报错’invalid attachments size’|
|»»» title|body|string| 否 |图文消息标题，最多64个字节|
|»»» url|body|string| 是 |图文消息链接|
|»»» media_id|body|string| 是 |图片链接封面，普通图片：建议不超过 1440 x 1080，可通过上传附件资源接口获得|
|» visible_range|body|object| 否 |指定的发表范围；若未指定，则表示执行者为应用可见范围内所有成员|
|»» sender_list|body|object| 是 |发表任务的执行者列表，详见下文的“可见范围说明”|
|»»» user_list|body|[string]| 是 |发表任务的执行者用户列表，最多支持10万个|
|»»» department_list|body|[integer]| 是 |发表任务的执行者部门列表|
|»» external_contact_list|body|object| 是 |可见到该朋友圈的客户列表，详见下文的“可见范围说明”|
|»»» tag_list|body|[string]| 是 |可见到该朋友圈的客户标签列表|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "xxxx"
}
```

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "xxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» jobid|string|true|none||异步任务id，最大长度为64字节，24小时有效；可使用获取发表朋友圈任务结果查询任务状态|

## GET 企业发表内容到客户的朋友圈-获取任务创建结果

GET /cgi-bin/externalcontact/get_moment_task_result

由于发表任务的创建是异步执行的，应用需要再调用该接口以获取创建的结果。

权限说明：只能查询已经提交过的历史任务。

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|jobid|query|string| 是 |异步任务id，最大长度为64字节，由创建发表内容到客户朋友圈任务接口获取|

> 返回示例

> 成功

```json
"{\r\n    \"errcode\": 0,\r\n    \"errmsg\": \"ok\",\r\n    \"status\": 1,\r\n    \"type\": \"add_moment_task\",\r\n\t\"result\": {\r\n\t\t\"errcode\":0,\r\n\t\t\"errmsg\":\"ok\"\r\n\t\t\"moment_id\":\"xxxx\",\r\n\t\t\"invalid_sender_list\":{\r\n\t\t\t\"user_list\":[\"zhangshan\",\"lisi\"],\r\n\t\t\t\"department_list\":[2,3]\r\n\t\t},\r\n\t\t\"invalid_external_contact_list\":{\r\n\t\t\t\"tag_list\":[\"xxx\"]\r\n\t\t}\r\n\t}\r\n}"
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» status|integer|true|none||任务状态，整型，1表示开始创建任务，2表示正在创建任务中，3表示创建任务已完成|
|» type|string|true|none||操作类型，字节串，此处固定为add_moment_task|
|» result|object|true|none||详细的处理结果。当任务完成后此字段有效|
|»» errcode|integer|true|none||none|
|»» errmsg|string|true|none||none|
|»» moment_id|string|true|none||朋友圈id，可通过获取客户朋友圈企业发表的列表接口获取朋友圈企业发表的列表|
|»» invalid_sender_list|object|true|none||none|
|»»» user_list|[string]|true|none||none|
|»»» department_list|[integer]|true|none||none|
|»» invalid_external_contact_list|object|true|none||不合法的执行者列表，包括不存在的id以及不在应用可见范围内的部门或者成员|
|»»» tag_list|string|true|none||none|

# 企业内部开发/客户联系/客户朋友圈/获取客户朋友圈全部的发表记录

## POST 获取客户朋友圈全部的发表记录-获取客户朋友圈发表时选择的可见范围

POST /cgi-bin/externalcontact/get_moment_customer_list

企业和第三方应用可通过该接口获取客户朋友圈创建时，选择的客户可见范围
权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户朋友圈下获取企业全部的发表记录的权限

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

> Body 请求参数

```json
{
  "moment_id": "momxxx",
  "userid": "xxx",
  "cursor": "CURSOR",
  "limit": 10
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» moment_id|body|string| 是 |朋友圈id|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值1000，默认值500，超过最大值时取默认值|
|» userid|body|string| 是 |企业发表成员userid，如果是企业创建的朋友圈，可以通过获取客户朋友圈企业发表的列表获取已发表成员userid，如果是个人创建的朋友圈，创建人userid就是企业发表成员userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "customer_list": [
    {
      "userid": "xxx",
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACCCC  "
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» customer_list|[object]|false|none||成员可见客户列表|
|»» external_userid|string|false|none||发送成功的外部联系人userid|
|»» userid|string|false|none||发表成员用户userid|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 获取客户朋友圈全部的发表记录-获取客户朋友圈企业发表的列表

POST /cgi-bin/externalcontact/get_moment_task

企业和第三方应用可通过该接口获取企业发表的朋友圈成员执行情况
权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户朋友圈下获取企业全部的发表记录的权限

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

> Body 请求参数

```json
{
  "moment_id": "momxxx",
  "cursor": "CURSOR",
  "limit": 10
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |none|
|body|body|object| 否 |none|
|» moment_id|body|string| 是 |朋友圈id,仅支持企业发表的朋友圈id|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值1000，默认值500，超过最大值时取默认值|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "task_list": [
    {
      "userid": "zhangsan",
      "publish_status": 1
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» task_list|[object]|false|none||发表任务列表|
|»» userid|string|false|none||发表成员用户userid|
|»» publish_status|integer(int32)|false|none||成员发表状态。0:未发表 1：已发表|

## POST 获取客户朋友圈全部的发表记录-获取企业全部的发表列表

POST /cgi-bin/externalcontact/get_moment_list

企业和第三方应用可通过该接口获取企业全部的发表内容。
补充说明:

朋友圈记录的起止时间间隔不能超过1个月
web管理端会展示企业成员所有已经发表的朋友圈（包括已经删除朋友圈），而API接口将不会返回已经删除的朋友圈记录

权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户朋友圈下获取企业全部的发表记录的权限

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

> Body 请求参数

```json
{
  "start_time": 1605000000,
  "end_time": 1605172726,
  "creator": "zhangsan",
  "filter_type": 1,
  "cursor": "CURSOR",
  "limit": 10
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» start_time|body|integer(int32)| 是 |朋友圈记录开始时间。Unix时间戳|
|» creator|body|string| 否 |朋友圈创建人的userid|
|» filter_type|body|integer(int32)| 否 |朋友圈类型。0：企业发表 1：个人发表 2：所有，包括个人创建以及企业创建，默认情况下为所有类型|
|» end_time|body|integer(int32)| 是 |朋友圈记录结束时间。Unix时间戳|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值100，默认值100，超过最大值时取默认值|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "moment_list": [
    {
      "moment_id": "momxxx",
      "creator": "xxxx",
      "create_time": 1605000000,
      "create_type": 1,
      "visible_type": 1,
      "text": {
        "content": "test"
      },
      "image": [
        {
          "media_id": "WWCISP_xxxxx"
        }
      ],
      "video": {
        "media_id": "WWCISP_xxxxx",
        "thumb_media_id": "WWCISP_xxxxx"
      },
      "link": {
        "title": "腾讯网-QQ.COM",
        "url": "https://www.qq.com"
      },
      "location": {
        "latitude": "23.10647",
        "longitude": "113.32446",
        "name": "广州市 · 广州塔"
      }
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» moment_list|[object]|false|none||朋友圈列表|
|»» moment_id|string|false|none||朋友圈id|
|»» image|[object]|false|none||none|
|»»» media_id|string|false|none||图片的media_id列表，可以通过获取临时素材下载资源|
|»» creator|string|false|none||朋友圈创建者userid|
|»» create_time|string|false|none||创建时间|
|»» link|object|false|none||none|
|»»» title|string|false|none||网页链接标题|
|»»» url|string|false|none||网页链接url|
|»» create_type|integer(int32)|false|none||朋友圈创建来源。0：企业 1：个人|
|»» visible_type|integer(int32)|false|none||可见范围类型。0：部分可见 1：公开|
|»» location|object|false|none||none|
|»»» latitude|string|false|none||地理位置纬度|
|»»» name|string|false|none||地理位置名称|
|»»» longitude|string|false|none||地理位置经度|
|»» text|object|false|none||none|
|»»» content|string|false|none||文本消息结构|
|»» video|object|false|none||none|
|»»» thumb_media_id|string|false|none||视频封面media_id，可以通过获取临时素材下载资源|
|»»» media_id|string|false|none||视频media_id，可以通过获取临时素材下载资源|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 获取客户朋友圈全部的发表记录-获取客户朋友圈的互动数据

POST /cgi-bin/externalcontact/get_moment_comments

企业和第三方应用可通过此接口获取客户朋友圈的互动数据。

**权限说明：**

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/93333#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/93333#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/93333#10013/第三步：获取access_token)）。
- 自建应用调用，只会返回应用可见范围内用户的发送情况。
- 第三方应用调用需要企业授权**客户朋友圈**下**获取企业全部的发表记录**的权限

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

> Body 请求参数

```json
{
  "moment_id": "momxxx",
  "userid": "xxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» moment_id|body|string| 是 |朋友圈id|
|» userid|body|string| 是 |企业发表成员userid，如果是企业创建的朋友圈，可以通过获取客户朋友圈企业发表的列表获取已发表成员userid，如果是个人创建的朋友圈，创建人userid就是企业发表成员userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "comment_list": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA ",
      "create_time": 1605172726
    },
    {
      "userid": "zhangshan ",
      "create_time": 1605172729
    }
  ],
  "like_list": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACBBBB ",
      "create_time": 1605172726
    },
    {
      "userid": "zhangshan ",
      "create_time": 1605172720
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» comment_list|[object]|false|none||评论列表|
|»» create_time|integer(int32)|false|none||评论时间|
|»» userid|string|false|none||评论的企业成员userid，userid与external_userid不会同时出现|
|»» external_userid|string|false|none||评论的外部联系人userid|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» like_list|[object]|false|none||点赞列表|
|»» create_time|integer(int32)|false|none||点赞时间|
|»» userid|string|false|none||点赞的企业成员userid，userid与external_userid不会同时出现|
|»» external_userid|string|false|none||点赞的外部联系人userid|

## POST 获取客户朋友圈全部的发表记录-获取客户朋友圈发表后的可见客户列表

POST /cgi-bin/externalcontact/get_moment_send_result

企业和第三方应用可通过该接口获取客户朋友圈发表后，可在微信朋友圈中查看的客户列表

权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户朋友圈下获取企业全部的发表记录的权限

文档ID: 25254
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93443
https://open.work.weixin.qq.com/api/doc/90000/90135/93333

> Body 请求参数

```json
{
  "moment_id": "momxxx",
  "userid": "xxx",
  "cursor": "CURSOR",
  "limit": 100
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» moment_id|body|string| 是 |朋友圈id|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值5000，默认值3000，超过最大值时取默认值|
|» userid|body|string| 是 |企业发表成员userid，如果是企业创建的朋友圈，可以通过获取客户朋友圈企业发表的列表获取已发表成员userid，如果是个人创建的朋友圈，创建人userid就是企业发表成员userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "customer_list": [
    {
      "external_userid": "woAJ2GCAAAXtWyujaWJHDDGi0mACCCC"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» customer_list|[object]|false|none||成员发送成功客户列表|
|»» external_userid|string|false|none||成员发送成功的外部联系人userid|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/客户朋友圈/客户朋友圈规则组管理

## POST 客户朋友圈规则组管理-删除规则组

POST /cgi-bin/externalcontact/moment_strategy/del

企业可通过此接口删除某个客户朋友圈规则组。

文档ID: 32104
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94890

> Body 请求参数

```json
{
  "strategy_id": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer(int32)| 是 |规则组id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 客户朋友圈规则组管理-获取规则组列表

POST /cgi-bin/externalcontact/moment_strategy/list

企业可通过此接口获取企业配置的所有客户朋友圈规则组id列表。

文档ID: 32104
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94890

> Body 请求参数

```json
{
  "cursor": "CURSOR",
  "limit": 1000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |分页查询游标，首次调用可不填|
|» limit|body|integer(int32)| 否 |分页大小,默认为1000，最大不超过1000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "strategy": [
    {
      "strategy_id": 1
    },
    {
      "strategy_id": 2
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，用于查询下一个分页的数据，无更多数据时不返回|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» strategy|[object]|false|none||none|
|»» strategy_id|integer(int32)|false|none||规则组id|

## POST 客户朋友圈规则组管理-编辑规则组及其管理范围

POST /cgi-bin/externalcontact/moment_strategy/edit

企业可通过此接口编辑规则组的基本信息和修改客户朋友圈规则组管理范围。该接口仅支持串行调用，请勿并发修改规则组。

如果规则组具有父规则组，则其管理范围必须是父规则组的子集，且将完全继承父规则组的权限配置(privilege将被忽略)
每个管理组的管理范围内最多支持3000个节点

文档ID: 32104
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94890

> Body 请求参数

```json
{
  "strategy_id": 0,
  "strategy_name": "string",
  "admin_list": [
    "string"
  ],
  "privilege": {
    "view_moment_list": true,
    "send_moment": true,
    "manage_moment_cover_and_sign": true
  },
  "range_add": [
    {
      "type": 0,
      "userid": "string",
      "partyid": 0
    }
  ],
  "range_del": [
    {
      "type": 0,
      "userid": "string",
      "partyid": 0
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer| 是 |规则组id|
|» strategy_name|body|string| 否 |规则组名称|
|» admin_list|body|[string]| 否 |管理员列表，如果为空则不对负责人做编辑，如果有则覆盖旧的负责人列表|
|» privilege|body|object| 否 |权限配置，如果为空则不对权限做编辑，如果有则覆盖旧的权限配置|
|»» view_moment_list|body|boolean| 是 |none|
|»» send_moment|body|boolean| 是 |none|
|»» manage_moment_cover_and_sign|body|boolean| 是 |none|
|» range_add|body|[object]| 否 |none|
|»» type|body|integer| 否 |向管理范围添加的节点类型 1-成员 2-部门|
|»» userid|body|string| 否 |向管理范围添加成员的userid,仅type为1时有效|
|»» partyid|body|integer| 否 |向管理范围添加部门的partyid，仅type为2时有效|
|» range_del|body|[object]| 否 |none|
|»» type|body|integer| 否 |从管理范围删除的节点类型 1-成员 2-部门|
|»» userid|body|string| 否 |从管理范围删除的成员的userid,仅type为1时有效|
|»» partyid|body|integer| 否 |从管理范围删除的部门的partyid，仅type为2时有效|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|

## POST 客户朋友圈规则组管理-获取规则组管理范围

POST /cgi-bin/externalcontact/moment_strategy/get_range

企业可通过此接口获取某个朋友圈规则组管理的成员和部门列表
文档ID: 32104
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94890

> Body 请求参数

```json
{
  "strategy_id": 1,
  "cursor": "CURSOR",
  "limit": 1000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |分页游标|
|» strategy_id|body|integer(int32)| 是 |规则组id|
|» limit|body|integer(int32)| 否 |每个分页的成员/部门节点数，默认为1000，最大为1000|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "range": [
    {
      "type": 1,
      "userid": "zhangsan"
    },
    {
      "type": 2,
      "partyid": 1
    }
  ],
  "next_cursor": "NEXT_CURSOR"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，用于查询下一个分页的数据，无更多数据时不返回|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» range|[object]|false|none||none|
|»» type|integer(int32)|true|none||none|
|»» userid|integer(int32)|false|none||管理范围内配置的成员userid，仅type为1时返回|
|»» field14|string|false|none||管理范围内配置的部门partyid，仅type为2时返回|

## POST 客户朋友圈规则组管理-创建新的规则组

POST /cgi-bin/externalcontact/moment_strategy/create

企业可通过此接口创建一个新的客户朋友圈规则组。该接口仅支持串行调用，请勿并发创建规则组。

如果要创建的规则组具有父规则组，则其管理范围必须是父规则组的子集，且将完全继承父规则组的权限配置(privilege将被忽略)
管理组的最大层级为5层
每个管理组的管理范围内最多支持3000个节点

文档ID: 32104
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94890

> Body 请求参数

```json
"{\n    \"parent_id\":0,\n    \"strategy_name\": \"NAME\",\n    \"admin_list\":[\n        \"zhangsan\",\n        \"lisi\"\n    ],\n    \"privilege\"\n    {\n            \"send_moment\":true,\n            \"view_moment_list\":true,\n            \"manage_moment_cover_and_sign\":true\n    },\n    \"range\":\n    [\n        {\n            \"type\":1,\n            \"userid\":\"zhangsan\"\n        },\n        {\n            \"type\":2,\n            \"partyid\":1\n        }\n    ]\n}\n"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» parent_id|body|integer| 是 |父规则组id|
|» strategy_name|body|string| 是 |规则组名称|
|» admin_list|body|[string]| 是 |规则组管理员userid列表，不可配置超级管理员，每个规则组最多可配置20个负责人|
|» privilege|body|object| 否 |none|
|»» send_moment|body|boolean| 是 |允许成员发表客户朋友圈，默认为true|
|»» view_moment_list|body|boolean| 是 |允许查看成员的全部客户朋友圈发表，默认为true|
|»» manage_moment_cover_and_sign|body|boolean| 是 |配置封面和签名，默认为true|
|» range|body|[object]| 否 |none|
|»» type|body|integer| 是 |规则组管理范围节点类型，1-成员 2-部门|
|»» userid|body|string| 否 |规则组的管理成员id|
|»» partyid|body|integer| 否 |规则组的管理部门id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "strategy_id": 1
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» strategy_id|integer|true|none||规则组id|

## POST 客户朋友圈规则组管理-获取规则组详情

POST /cgi-bin/externalcontact/moment_strategy/get

文档ID: 32104
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94890

> Body 请求参数

```json
{
  "strategy_id": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» strategy_id|body|integer(int32)| 是 |规则组id|

> 返回示例

> 成功

```json
{
  "errmsg": "ok",
  "strategy": {
    "strategy_id": 1,
    "parent_id": 0,
    "strategy_name": "NAME",
    "create_time": 1557838797,
    "admin_list": [
      "zhangsan",
      "lisi"
    ],
    "privilege": {
      "view_moment_list": true,
      "send_moment": true,
      "manage_moment_cover_and_sign": true
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» strategy|object|false|none||none|
|»» create_time|integer(int32)|false|none||规则组创建时间戳|
|»» parent_id|integer(int32)|false|none||父规则组id， 如果当前规则组没父规则组，则为0|
|»» strategy_id|integer(int32)|false|none||规则组id|
|»» privilege|object|false|none||none|
|»»» manage_moment_cover_and_sign|boolean|false|none||配置封面和签名，默认为true|
|»»» send_moment|boolean|false|none||允许成员发表客户朋友圈，默认为true|
|»»» view_moment_list|boolean|false|none||允许查看成员的全部客户朋友圈发表|
|»» strategy_name|string|false|none||规则组名称|
|»» admin_list|[string]|false|none||规则组管理员userid列表|

# 企业内部开发/客户联系/消息推送

## POST 停止企业群发

POST /cgi-bin/externalcontact/cancel_groupmsg_send

企业和第三方应用可调用此接口，停止无需成员继续发送的企业群发

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/97611#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/97611#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/97611#10013/第三步：获取access_token)）。
- 自建应用调用，只会返回应用可见范围内用户的发送情况。
- 第三方应用调用需要企业授权**客户联系**下**群发消息给客户和客户群**的权限

> Body 请求参数

```json
"{\r\n    \"msgid\": \"msgGCAAAXtWyujaWJHDDGi0mACAAAA\",\r\n}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» msgid|body|string| 是 |群发消息的id，通过[获取群发记录列表](https://developer.work.weixin.qq.com/document/path/97611#获取群发记录列表)接口返回|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

## POST 创建企业群发

POST /cgi-bin/externalcontact/add_msg_template

企业跟第三方应用可通过此接口添加企业群发消息的任务并通知成员发送给相关客户或客户群。（注：企业微信终端需升级到2.7.5版本及以上）
注意：调用该接口并不会直接发送消息给客户/客户群，需要成员确认后才会执行发送（客服人员的企业微信需要升级到2.7.5及以上版本）
旧接口创建企业群发已经废弃，接口升级后支持发送视频文件，并且支持最多同时发送9个附件。
同一个企业每个自然月内仅可针对一个客户/客户群发送4条消息，超过接收上限的客户将无法再收到群发消息。

text和attachments不能同时为空
attachments中每个附件信息必须与msgtype一致，例如，msgtype指定为image，则需要填写image.pic_url或者image.media_id，否则会报错。
media_id和pic_url只需填写一个，两者同时填写时使用media_id，二者不可同时为空
权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用只能给应用可见范围内的成员进行推送。
第三方应用需具有“企业客户权限->客户联系->群发消息给客户和客户群”权限。
当只提供sender参数时，相当于选取了这个成员所有的客户。
注意：2019-8-1之后，取消了 “无法向未回复消息的客户发送企业群发消息” 的限制。

文档ID: 15836
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/92698
https://open.work.weixin.qq.com/api/doc/90000/90135/92135

> Body 请求参数

```json
{
  "chat_type": "single",
  "external_userid": [
    "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
    "wmqfasd1e1927831123109rBAAAA"
  ],
  "sender": "zhangsan",
  "text": {
    "content": "文本消息内容"
  },
  "attachments": [
    {
      "msgtype": "image",
      "image": {
        "media_id": "MEDIA_ID",
        "pic_url": "http://p.qpic.cn/pic_wework/3474110808/7a6344sdadfwehe42060/0"
      }
    },
    {
      "msgtype": "link",
      "link": {
        "title": "消息标题",
        "picurl": "https://example.pic.com/path",
        "desc": "消息描述",
        "url": "https://example.link.com/path"
      }
    },
    {
      "msgtype": "miniprogram",
      "miniprogram": {
        "title": "消息标题",
        "pic_media_id": "MEDIA_ID",
        "appid": "wx8bd80126147dfAAA",
        "page": "/path/index.html"
      }
    },
    {
      "msgtype": "video",
      "video": {
        "media_id": "MEDIA_ID"
      }
    },
    {
      "msgtype": "file",
      "file": {
        "media_id": "MEDIA_ID"
      }
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» chat_type|body|string| 否 |群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群|
|» external_userid|body|[string]| 否 |客户的外部联系人id列表，仅在chat_type为single时有效，不可与sender同时为空，最多可传入1万个客户|
|» attachments|body|[object]| 否 |附件，最多支持添加9个附件|
|»» file|body|object| 是 |文件的media_id，可以通过素材管理接口获得|
|»»» media_id|body|string| 否 |none|
|»» msgtype|body|string| 是 |附件类型，可选image、link、miniprogram或者video|
|» sender|body|string| 否 |发送企业群发消息的成员userid，当类型为发送给客户群时必填|
|» text|body|object| 否 |none|
|»» content|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "fail_list": [
    "wmqfasd1e1927831123109rBAAAA"
  ],
  "msgid": "msgGCAAAXtWyujaWJHDDGi0mAAAA"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» fail_list|[string]|false|none||无效或无法发送的external_userid列表|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» msgid|string|false|none||企业群发消息的id，可用于获取群发消息发送结果|

## POST 入群欢迎语素材管理-编辑

POST /cgi-bin/externalcontact/group_welcome_template/edit

企业可通过此API编辑入群欢迎语素材库中的素材，且仅能够编辑调用方自己创建的入群欢迎语素材。

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/92366#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/92366#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/92366#10013/第三步：获取access_token)）。
- 第三方应用需具有“企业客户权限->客户联系->配置入群欢迎语素材”权限
- 仅可编辑本应用创建的入群欢迎语素材

文档ID: 19635
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92366
https://open.work.weixin.qq.com/api/doc/90001/90143/93438

> Body 请求参数

```json
{
  "template_id": "msgXXXXXXX",
  "text": {
    "content": "文本消息内容"
  },
  "image": {
    "media_id": "MEDIA_ID",
    "pic_url": "http://p.qpic.cn/pic_wework/3474110808/7a6344sdadfwehe42060/0"
  },
  "link": {
    "title": "消息标题",
    "picurl": "https://example.pic.com/path",
    "desc": "消息描述",
    "url": "https://example.link.com/path"
  },
  "miniprogram": {
    "title": "消息标题",
    "pic_media_id": "MEDIA_ID",
    "appid": "wx8bd80126147df384",
    "page": "/path/index"
  },
  "file": {
    "media_id": "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"
  },
  "video": {
    "media_id": "MEDIA_ID"
  },
  "agentid": 1000014
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» image|body|[image](#schemaimage)| 否 |none|
|»» media_id|body|string| 否 |图片的media_id，可以通过素材管理接口获得|
|»» pic_url|body|string| 否 |图片的链接，仅可使用上传图片接口得到的链接|
|» agentid|body|integer(int32)| 否 |授权方安装的应用agentid。仅旧的第三方多应用套件需要填此参数|
|» file|body|[file](#schemafile)| 否 |none|
|»» media_id|body|string| 是 |文件id，可以通过素材管理接口获得|
|» link|body|[link](#schemalink)| 否 |none|
|»» picurl|body|string| 否 |图文消息封面的url	图文消息封面的url|
|»» title|body|string| 是 |图文消息标题，最长为128字节|
|»» url|body|string| 是 |图文消息的链接|
|»» desc|body|string| 否 |图文消息的描述，最长为512字节|
|» template_id|body|string| 是 |欢迎语素材id|
|» text|body|[text](#schematext)| 否 |none|
|»» media_id|body|string| 是 |文件id，可以通过素材管理接口获得|
|» video|body|[video](#schemavideo)| 否 |none|
|»» media_id|body|string| 是 |视频媒体文件id，可以通过素材管理接口获得|
|» miniprogram|body|[miniprogram](#schemaminiprogram)| 否 |none|
|»» appid|body|string| 是 |小程序appid，必须是关联到企业的小程序应用|
|»» pic_media_id|body|string| 是 |小程序消息封面的mediaid，封面图建议尺寸为520*416|
|»» page|body|string| 是 |小程序page路径|
|»» title|body|string| 是 |小程序消息标题，最长为64字节|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 入群欢迎语素材管理-删除

POST /cgi-bin/externalcontact/group_welcome_template/del

企业可通过此API删除入群欢迎语素材，且仅能删除调用方自己创建的入群欢迎语素材。

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用需具有“企业客户权限->客户联系->配置入群欢迎语素材”权限
仅可删除本应用创建的入群欢迎语素材

文档ID: 19635
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92366
https://open.work.weixin.qq.com/api/doc/90001/90143/93438

> Body 请求参数

```json
{
  "template_id": "msgXXXXXXX",
  "agentid": 1000014
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» agentid|body|integer(int32)| 否 |授权方安装的应用agentid。仅旧的第三方多应用套件需要填此参数|
|» template_id|body|string| 是 |群欢迎语的素材id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 发送新客户欢迎语

POST /cgi-bin/externalcontact/send_welcome_msg

企业微信在向企业推送添加外部联系人事件时，会额外返回一个welcome_code，企业以此为凭据调用接口，即可通过成员向新添加的客户发送个性化的欢迎语。
为了保证用户体验以及避免滥用，企业仅可在收到相关事件后20秒内调用，且只可调用一次。
如果企业已经在管理端为相关成员配置了可用的欢迎语，则推送添加外部联系人事件时不会返回welcome_code。
每次添加新客户时可能有多个企业自建应用/第三方应用收到带有welcome_code的回调事件，但仅有最先调用的可以发送成功。后续调用将返回41051（externaluser has started chatting）错误，请用户根据实际使用需求，合理设置应用可见范围，避免冲突。
旧接口发送新客户欢迎语已经废弃，接口升级后支持发送视频文件，并且最多支持同时发送9个附件

text和attachments不能同时为空；
text与附件信息可以同时发送，此时将会以多条消息的形式触达客户
attachments中每个附件信息必须与msgtype一致，例如，msgtype指定为image，则需要填写image.pic_url或者image.media_id，否则会报错。
media_id和pic_url只需填写一个，两者同时填写时使用media_id，二者不可同时为空。
权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用需要拥有“企业客户权限->客户联系->给客户发送欢迎语”权限
企业成员需在应用的可见范围内

文档ID: 16489
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92137
https://open.work.weixin.qq.com/api/doc/90001/90143/92599

> Body 请求参数

```json
{
  "welcome_code": "CALLBACK_CODE",
  "text": {
    "content": "文本消息内容"
  },
  "attachments": [
    {
      "msgtype": "image",
      "image": {
        "media_id": "MEDIA_ID",
        "pic_url": "http://p.qpic.cn/pic_wework/3474110808/7a6344sdadfwehe42060/0"
      }
    },
    {
      "msgtype": "link",
      "link": {
        "title": "消息标题",
        "picurl": "https://example.pic.com/path",
        "desc": "消息描述",
        "url": "https://example.link.com/path"
      }
    },
    {
      "msgtype": "miniprogram",
      "miniprogram": {
        "title": "消息标题",
        "pic_media_id": "MEDIA_ID",
        "appid": "wx8bd80126147dfAAA",
        "page": "/path/index.html"
      }
    },
    {
      "msgtype": "video",
      "video": {
        "media_id": "MEDIA_ID"
      }
    },
    {
      "msgtype": "file",
      "file": {
        "media_id": "MEDIA_ID"
      }
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» welcome_code|body|string| 是 |通过添加外部联系人事件推送给企业的发送欢迎语的凭证，有效期为20秒|
|» text|body|object| 否 |none|
|»» content|body|string| 是 |消息文本内容,最长为4000字节|
|» attachments|body|[object]| 是 |附件，最多可添加9个附件|
|»» msgtype|body|string| 是 |附件类型，可选image、link、miniprogram或者video|
|»» image|body|object| 否 |none|
|»»» media_id|body|string| 是 |图片的media_id，可以通过素材管理接口获得|
|»»» pic_url|body|string| 是 |图片的链接，仅可使用上传图片接口得到的链接|
|»» link|body|object| 否 |none|
|»»» title|body|string| 是 |图文消息标题，最长为128字节|
|»»» picurl|body|string| 否 |图文消息封面的url|
|»»» desc|body|string| 否 |图文消息的描述，最长为512字节|
|»»» url|body|string| 是 |图文消息的链接|
|»» miniprogram|body|object| 否 |none|
|»»» title|body|string| 是 |小程序消息标题，最长为64字节|
|»»» pic_media_id|body|string| 是 |小程序消息封面的mediaid，封面图建议尺寸为520*416|
|»»» appid|body|string| 是 |小程序appid，必须是关联到企业的小程序应用|
|»»» page|body|string| 是 |小程序page路径|
|»» video|body|object| 否 |none|
|»»» media_id|body|string| 是 |视频的media_id，可以通过素材管理接口获得|
|»» file|body|object| 否 |none|
|»»» media_id|body|string| 是 |文件的media_id, 可以通过素材管理接口获得|

> 返回示例

> 成功

```json
null
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 入群欢迎语素材管理-添加

POST /cgi-bin/externalcontact/group_welcome_template/add

企业可通过此API向企业的入群欢迎语素材库中添加素材。每个企业的入群欢迎语素材库中，最多容纳100个素材。

> text中支持配置多个**%NICKNAME%**(大小写敏感)形式的欢迎语，当配置了欢迎语占位符后，发送给客户时会自动替换为客户的昵称;
> text、image、link、miniprogram、file、video不能全部为空；
> text与其它消息类型可以同时发送，此时将会以两条消息的形式触达客户
> text以外的消息类型，只能有一个，如果三者同时填，则按image、link、miniprogram、file、video的优先顺序取参。例如：image与link同时传值，则只有image生效。
> 图片消息中，media_id和pic_url只需填写一个，两者同时填写时使用media_id，二者不可同时为空。

 

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/92366#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/92366#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/92366#10013/第三步：获取access_token)）。
- 第三方应用需具有“企业客户权限->客户联系->配置入群欢迎语素材”权限

 

文档ID: 19635
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92366
https://open.work.weixin.qq.com/api/doc/90001/90143/93438

> Body 请求参数

```json
{
  "text": {
    "content": "亲爱的%NICKNAME%用户，你好"
  },
  "image": {
    "media_id": "MEDIA_ID",
    "pic_url": "http://p.qpic.cn/pic_wework/3474110808/7a6344sdadfwehe42060/0"
  },
  "link": {
    "title": "消息标题",
    "picurl": "https://example.pic.com/path",
    "desc": "消息描述",
    "url": "https://example.link.com/path"
  },
  "miniprogram": {
    "title": "消息标题",
    "pic_media_id": "MEDIA_ID",
    "appid": "wx8bd80126147dfAAA",
    "page": "/path/index"
  },
  "file": {
    "media_id": "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"
  },
  "video": {
    "media_id": "MEDIA_ID"
  },
  "agentid": 1000014,
  "notify": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» image|body|object| 否 |none|
|»» media_id|body|string| 否 |none|
|»» pic_url|body|string| 否 |none|
|» agentid|body|integer(int32)| 否 |授权方安装的应用agentid。仅旧的第三方多应用套件需要填此参数|
|» file|body|object| 否 |none|
|»» media_id|body|string| 否 |none|
|» link|body|object| 否 |none|
|»» picurl|body|string| 否 |none|
|»» title|body|string| 否 |none|
|»» url|body|string| 否 |none|
|»» desc|body|string| 否 |none|
|» text|body|object| 否 |none|
|»» content|body|string| 否 |none|
|» video|body|object| 否 |none|
|»» media_id|body|string| 否 |none|
|» miniprogram|body|object| 否 |none|
|»» appid|body|string| 否 |none|
|»» pic_media_id|body|string| 否 |none|
|»» page|body|string| 否 |none|
|»» title|body|string| 否 |none|
|» notify|body|integer(int32)| 否 |是否通知成员将这条入群欢迎语应用到客户群中，0-不通知，1-通知， 不填则通知|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "template_id": "msgXXXXXX"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» template_id|string|false|none||欢迎语素材id|

## POST 入群欢迎语素材管理-获取

POST /cgi-bin/externalcontact/group_welcome_template/get

企业可通过此API获取入群欢迎语素材。

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/92366#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/92366#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/92366#10013/第三步：获取access_token)）。
- 第三方应用需具有“企业客户权限->客户联系->配置入群欢迎语素材”权限

文档ID: 19635
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92366
https://open.work.weixin.qq.com/api/doc/90001/90143/93438

> Body 请求参数

```json
{
  "template_id": "msgXXXXXXX"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» template_id|body|string| 是 |群欢迎语的素材id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "text": {
    "content": "文本消息内容"
  },
  "image": {
    "pic_url": "http://p.qpic.cn/pic_wework/XXXXX"
  },
  "link": {
    "title": "消息标题",
    "picurl": "https://example.pic.com/path",
    "desc": "消息描述",
    "url": "https://example.link.com/path"
  },
  "miniprogram": {
    "title": "消息标题",
    "pic_media_id": "MEDIA_ID",
    "appid": "wx8bd80126147df384",
    "page": "/path/index"
  },
  "file": {
    "media_id": "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"
  },
  "video": {
    "media_id": "MEDIA_ID"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» image|object|false|none||none|
|»» pic_url|string|false|none||图片的url|
|» file|[file](#schemafile)|false|none||none|
|»» media_id|string|true|none||文件id，可以通过素材管理接口获得|
|» link|[link](#schemalink)|false|none||none|
|»» picurl|string|false|none||图文消息封面的url	图文消息封面的url|
|»» title|string|true|none||图文消息标题，最长为128字节|
|»» url|string|true|none||图文消息的链接|
|»» desc|string|false|none||图文消息的描述，最长为512字节|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» text|[text](#schematext)|false|none||none|
|»» media_id|string|true|none||文件id，可以通过素材管理接口获得|
|» video|[video](#schemavideo)|false|none||none|
|»» media_id|string|true|none||视频媒体文件id，可以通过素材管理接口获得|
|» miniprogram|[miniprogram](#schemaminiprogram)|false|none||none|
|»» appid|string|true|none||小程序appid，必须是关联到企业的小程序应用|
|»» pic_media_id|string|true|none||小程序消息封面的mediaid，封面图建议尺寸为520*416|
|»» page|string|true|none||小程序page路径|
|»» title|string|true|none||小程序消息标题，最长为64字节|

## POST 提醒成员群发

POST /cgi-bin/externalcontact/remind_groupmsg_send

企业和第三方应用可调用此接口，重新触发群发通知，提醒成员完成群发任务，24小时内每个群发最多触发三次提醒。

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/97610#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/97610#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/97610#10013/第三步：获取access_token)）。
- 自建应用调用，只会返回应用可见范围内用户的发送情况。
- 第三方应用调用需要企业授权**客户联系**下**群发消息给客户和客户群**的权限

> Body 请求参数

```json
"{\r\n    \"msgid\": \"msgGCAAAXtWyujaWJHDDGi0mACAAAA\",\r\n}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» msgid|body|string| 是 |群发消息的id，通过[获取群发记录列表](https://developer.work.weixin.qq.com/document/path/97610#获取群发记录列表)接口返回|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/消息推送/获取企业的全部群发记录

## POST 获取企业的全部群发记录-获取群发记录列表

POST /cgi-bin/externalcontact/get_groupmsg_list_v2

企业和第三方应用可通过此接口获取企业与成员的群发记录。

补充说明:

群发任务记录的起止时间间隔不能超过1个月
3.1.6版本之前不支持多附件，请参考获取群发记录列表接口获取群发记录列表

权限说明：

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户联系下群发消息给客户和客户群的权限

文档ID: 25429
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93439
https://open.work.weixin.qq.com/api/doc/90000/90135/93338

> Body 请求参数

```json
{
  "chat_type": "single",
  "start_time": 1605171726,
  "end_time": 1605172726,
  "creator": "zhangshan",
  "filter_type": 1,
  "limit": 50,
  "cursor": "CURSOR"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» chat_type|body|string| 是 |群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» start_time|body|integer(int32)| 是 |群发任务记录开始时间|
|» creator|body|string| 否 |群发任务创建人企业账号id|
|» filter_type|body|integer(int32)| 否 |创建人类型。0：企业发表 1：个人发表 2：所有，包括个人创建以及企业创建，默认情况下为所有类型|
|» end_time|body|integer(int32)| 是 |群发任务记录结束时间|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值100，默认值50，超过最大值时取默认值|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "group_msg_list": [
    {
      "msgid": "msgGCAAAXtWyujaWJHDDGi0mAAAA",
      "creator": "xxxx",
      "create_time": "xxxx",
      "create_type": 1,
      "text": {
        "content": "文本消息内容"
      },
      "attachments": [
        {
          "msgtype": "image",
          "image": {
            "media_id": "MEDIA_ID",
            "pic_url": "http://p.qpic.cn/pic_wework/3474110808/7a6344sdadfwehe42060/0"
          }
        },
        {
          "msgtype": "link",
          "link": {
            "title": "消息标题",
            "picurl": "https://example.pic.com/path",
            "desc": "消息描述",
            "url": "https://example.link.com/path"
          }
        },
        {
          "msgtype": "miniprogram",
          "miniprogram": {
            "title": "消息标题",
            "pic_media_id": "MEDIA_ID",
            "appid": "wx8bd80126147dfAAA",
            "page": "/path/index.html"
          }
        },
        {
          "msgtype": "video",
          "video": {
            "media_id": "MEDIA_ID"
          }
        },
        {
          "msgtype": "file",
          "file": {
            "media_id": "MEDIA_ID"
          }
        }
      ]
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» group_msg_list|[object]|false|none||群发记录列表|
|»» creator|string|false|none||群发消息创建者userid，API接口创建的群发消息不返回该字段|
|»» attachments|[object]|false|none||none|
|»»» msgtype|string|true|none||值值必须是video是image|
|»»» image|object|false|none||none|
|»»»» media_id|string|true|none||图片的media_id，可以通过获取临时素材下载资源|
|»»»» pic_url|string|true|none||图片的url，与图片的media_id不能共存优先吐出media_id|
|»»» link|object|false|none||none|
|»»»» title|string|true|none||图文消息标题|
|»»»» picurl|string|true|none||图文消息封面的url|
|»»»» desc|string|true|none||图文消息的描述，最多512个字节|
|»»»» url|string|true|none||图文消息的描述，最多512个字节|
|»»» miniprogram|object|false|none||none|
|»»»» title|string|true|none||小程序消息标题，最多64个字节|
|»»»» pic_media_id|string|true|none||none|
|»»»» appid|string|true|none||小程序appid，必须是关联到企业的小程序应用|
|»»»» page|string|true|none||小程序page路径|
|»»» video|object|false|none||none|
|»»»» media_id|string|true|none||视频的media_id，可以通过获取临时素材下载资源|
|»»» file|object|false|none||none|
|»»»» media_id|string|true|none||none|
|»» create_time|string|false|none||创建时间|
|»» msgid|string|false|none||企业群发消息的id，可用于获取企业群发成员执行结果|
|»» create_type|integer(int32)|false|none||群发消息创建来源。0：企业 1：个人|
|»» text|object|false|none||none|
|»»» content|string|false|none||消息文本内容，最多4000个字节|

## POST 获取企业的全部群发记录-获取企业群发成员执行结果

POST /cgi-bin/externalcontact/get_groupmsg_send_result

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户联系下群发消息给客户和客户群的权限

文档ID: 25429
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93439
https://open.work.weixin.qq.com/api/doc/90000/90135/93338

> Body 请求参数

```json
{
  "msgid": "msgGCAAAXtWyujaWJHDDGi0mACAAAA",
  "userid": "zhangsan",
  "limit": 50,
  "cursor": "CURSOR"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值1000，默认值500，超过最大值时取默认值|
|» msgid|body|string| 是 |群发消息的id，通过获取群发记录列表接口返回|
|» userid|body|string| 是 |发送成员userid，通过获取群发成员发送任务列表接口返回|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "send_list": [
    {
      "external_userid": "wmqfasd1e19278asdasAAAA",
      "chat_id": "wrOgQhDgAAMYQiS5ol9G7gK9JVAAAA",
      "userid": "zhangsan",
      "status": 1,
      "send_time": 1552536375
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» send_list|[object]|false|none||群成员发送结果列表|
|»» external_userid|string|false|none||none|
|»» send_time|integer(int32)|false|none||none|
|»» userid|string|false|none||none|
|»» chat_id|string|false|none||none|
|»» status|integer(int32)|false|none||none|
|» next_cursor|string|false|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 获取企业的全部群发记录-获取群发成员发送任务列表

POST /cgi-bin/externalcontact/get_groupmsg_task

权限说明:
企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的发送情况。
第三方应用调用需要企业授权客户联系下群发消息给客户和客户群的权限

文档ID: 25429
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/93439
https://open.work.weixin.qq.com/api/doc/90000/90135/93338

2020-11-17日之前创建的消息无发送任务列表，请通过[获取企业群发成员执行结果](https://developer.work.weixin.qq.com/document/path/93338#16251)接口获取群发结果

> Body 请求参数

```json
{
  "msgid": "msgGCAAAXtWyujaWJHDDGi0mACAAAA",
  "limit": 50,
  "cursor": "CURSOR"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|
|» limit|body|integer(int32)| 否 |返回的最大记录数，整型，最大值1000，默认值500，超过最大值时取默认值|
|» msgid|body|string| 是 |群发消息的id，通过获取群发记录列表接口返回|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "CURSOR",
  "task_list": [
    {
      "userid": "zhangsan",
      "status": 1,
      "send_time": 1552536375
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» next_cursor|string|false|none||分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» task_list|[object]|false|none||群发成员发送任务列表|
|»» send_time|integer(int32)|false|none||none|
|»» userid|string|false|none||none|
|»» status|integer(int32)|false|none||none|

# 企业内部开发/客户联系/统计管理

## POST 获取「群聊数据统计」数据-按群主聚合的方式

POST /cgi-bin/externalcontact/groupchat/statistic

文档ID: 19418
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92133
https://open.work.weixin.qq.com/api/doc/90001/90143/93476

> Body 请求参数

```json
{
  "day_begin_time": 1600272000,
  "day_end_time": 1600444800,
  "owner_filter": {
    "userid_list": [
      "zhangsan"
    ]
  },
  "order_by": 2,
  "order_asc": 0,
  "offset": 0,
  "limit": 1000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» day_begin_time|body|integer| 是 |none|
|» day_end_time|body|integer| 是 |none|
|» owner_filter|body|object| 是 |none|
|»» userid_list|body|[string]| 是 |none|
|» order_by|body|integer| 是 |none|
|» order_asc|body|integer| 是 |none|
|» offset|body|integer| 是 |none|
|» limit|body|integer| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "total": 2,
  "next_offset": 2,
  "items": [
    {
      "owner": "zhangsan",
      "data": {
        "new_chat_cnt": 2,
        "chat_total": 2,
        "chat_has_msg": 0,
        "new_member_cnt": 0,
        "member_total": 6,
        "member_has_msg": 0,
        "msg_total": 0,
        "migrate_trainee_chat_cnt": 3
      }
    },
    {
      "owner": "lisi",
      "data": {
        "new_chat_cnt": 1,
        "chat_total": 3,
        "chat_has_msg": 2,
        "new_member_cnt": 0,
        "member_total": 6,
        "member_has_msg": 0,
        "msg_total": 0,
        "migrate_trainee_chat_cnt": 3
      }
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» total|integer(int32)|false|none||命中过滤条件的记录总个数|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» next_offset|integer(int32)|false|none||当前分页的下一个offset。当next_offset和total相等时，说明已经取完所有|
|» items|[object]|false|none||记录列表。表示某个群主所拥有的客户群的统计数据|
|»» owner|string|false|none||none|
|»» data|object|false|none||none|
|»»» chat_has_msg|integer(int32)|false|none||none|
|»»» member_has_msg|integer(int32)|false|none||none|
|»»» chat_total|integer(int32)|false|none||none|
|»»» new_member_cnt|integer(int32)|false|none||none|
|»»» msg_total|integer(int32)|false|none||none|
|»»» member_total|integer(int32)|false|none||none|
|»»» new_chat_cnt|integer(int32)|false|none||none|
|»»» migrate_trainee_chat_cnt|integer(int32)|false|none||none|

## POST 获取「群聊数据统计」数据-按自然日聚合的方式

POST /cgi-bin/externalcontact/groupchat/statistic_group_by_day

文档ID: 19418
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92133
https://open.work.weixin.qq.com/api/doc/90001/90143/93476

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
null
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» items|[object]|false|none||记录列表。表示某个自然日客户群的统计数据|
|»» data|object|false|none||none|
|»»» chat_has_msg|integer(int32)|false|none||none|
|»»» member_has_msg|integer(int32)|false|none||none|
|»»» chat_total|integer(int32)|false|none||none|
|»»» new_member_cnt|integer(int32)|false|none||none|
|»»» msg_total|integer(int32)|false|none||none|
|»»» member_total|integer(int32)|false|none||none|
|»»» new_chat_cnt|integer(int32)|false|none||none|
|»»» migrate_trainee_chat_cnt|integer(int32)|false|none||none|
|»» stat_time|integer(int32)|false|none||none|

## POST 获取「联系客户统计」数据

POST /cgi-bin/externalcontact/get_user_behavior_data

文档ID: 16392
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/92132
https://open.work.weixin.qq.com/api/doc/90001/90143/92275

> Body 请求参数

```json
{
  "userid": [
    "zhangsan",
    "lisi"
  ],
  "partyid": [
    1001,
    1002
  ],
  "start_time": 1536508800,
  "end_time": 1536595200
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» start_time|body|integer(int32)| 是 |数据起始时间|
|» end_time|body|integer(int32)| 是 |数据结束时间|
|» partyid|body|[integer]| 否 |部门ID列表，最多100个|
|» userid|body|[string]| 否 |成员ID列表，最多100个|

> 返回示例

> 成功

```json
null
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» behavior_data|[object]|false|none||none|
|»» chat_cnt|integer(int32)|false|none||none|
|»» negative_feedback_cnt|integer(int32)|false|none||none|
|»» avg_reply_time|integer(int32)|false|none||none|
|»» new_apply_cnt|integer(int32)|false|none||none|
|»» reply_percentage|integer(int32)|false|none||none|
|»» new_contact_cnt|integer(int32)|false|none||none|
|»» message_cnt|integer(int32)|false|none||none|
|»» stat_time|integer(int32)|false|none||none|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 企业内部开发/客户联系/管理商品图册

## POST 获取商品图册列表

POST /cgi-bin/externalcontact/get_product_album_list

企业和第三方应用可以通过此接口导出商品

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
自建应用调用，只会返回应用可见范围内用户的情况。
第三方应用或代开发自建应用调用需要企业授权客户联系下管理商品图册的权限

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用或代开发自建应用调用需要企业授权客户联系下管理商品图册的权限
可获取企业内所有企业级的商品图册

https://open.work.weixin.qq.com/api/doc/90000/90135/95096

> Body 请求参数

```json
{
  "limit": 50,
  "cursor": "CURSOR"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» limit|body|integer| 是 |返回的最大记录数，整型，最大值100，默认值50，超过最大值时取默认值|
|» cursor|body|string| 是 |用于分页查询的游标，字符串类型，由上一次调用返回，首次调用可不填|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» next_cursor|string|true|none||用于分页查询的游标，字符串类型，用于下一次调用|
|» product_list|[object]|true|none||商品列表|
|»» product_id|string|true|none||商品id|
|»» description|string|true|none||商品的名称、特色等|
|»» price|integer|true|none||商品的价格，单位为分|
|»» product_sn|string|true|none||商品编码|
|»» attachments|[object]|true|none||附件类型|
|»»» type|string|false|none||附件类型，目前仅支持image|
|»»» image|object|false|none||none|
|»»»» media_id|string|true|none||图片的media_id，可以通过获取临时素材下载资源|

## POST 删除商品图册

POST /cgi-bin/externalcontact/delete_product_album

企业和第三方应用可以通过此接口删除商品信息

权限说明:
- 企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
- 第三方应用或代开发自建应用调用需要企业授权客户联系下管理商品图册的权限
- 应用只可删除应用自己创建的商品图册；客户联系系统应用可删除所有商品图册

https://open.work.weixin.qq.com/api/doc/90000/90135/95096

> Body 请求参数

```json
{
  "product_id": "xxxxxxxxxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» product_id|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

## POST 编辑商品图册

POST /cgi-bin/externalcontact/update_product_album

企业和第三方应用可以通过此接口修改商品信息

注：除product_id外，需要更新的字段才填，不需更新的字段可不填。

**权限说明**:
- 企业需要使用[“客户联系”secret](https://open.work.weixin.qq.com/api/doc/90000/90135/95096#13473/开始开发)或配置到“[可调用应用](https://open.work.weixin.qq.com/api/doc/90000/90135/95096#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://open.work.weixin.qq.com/api/doc/90000/90135/95096#10013/第三步：获取access_token)）。
- 第三方应用或代开发自建应用调用需要企业授权**客户联系**下**管理商品图册**的权限
- 应用只修改应用自己创建的商品图册；客户联系系统应用可修改所有商品图册

https://open.work.weixin.qq.com/api/doc/90000/90135/95096

> Body 请求参数

```json
{
  "product_id": "xxxxxxxxxx",
  "description": "世界上最好的商品",
  "price": 30000,
  "product_sn": "xxxxxx",
  "attachments": [
    {
      "type": "image",
      "image": {
        "media_id": "MEDIA_ID"
      }
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» description|body|string| 是 |商品的名称、特色等;不超过300个字|
|» product_id|body|string| 是 |商品id|
|» price|body|integer| 是 |商品的价格，单位为分；最大不超过5万元|
|» product_sn|body|string| 否 |商品编码；不超过128个字节；只能输入数字和字母|
|» attachments|body|[object]| 是 |附件类型，仅支持image，最多不超过9个附件|
|»» type|body|string| 否 |none|
|»» image|body|object| 否 |none|
|»»» media_id|body|string| 是 |图片的media_id，可以通过上传附件资源接口获得|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|

## POST 获取商品图册

POST /cgi-bin/externalcontact/get_product_album

企业和第三方应用可以通过此接口增加商品

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用或代开发自建应用调用需要企业授权客户联系下管理商品图册的权限
可获取企业内所有企业级的商品图册

https://open.work.weixin.qq.com/api/doc/90000/90135/95096

> Body 请求参数

```json
{
  "product_id": "xxxxxxxxxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» product_id|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "product": {
    "product_id": "xxxxxxxxxx",
    "description": "世界上最好的商品",
    "price": 30000,
    "create_time": 1600000000,
    "product_sn": "xxxxxxxx",
    "attachments": [
      {
        "type": "image",
        "image": {
          "media_id": "MEDIA_ID"
        }
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» product|object|true|none||商品详情|
|»» product_id|string|true|none||商品id|
|»» description|string|true|none||商品的名称、特色等|
|»» price|integer|true|none||商品的价格，单位为分|
|»» create_time|integer|true|none||商品图册创建时间|
|»» product_sn|string|true|none||商品编码|
|»» attachments|[object]|true|none||附件类型|
|»»» type|string|false|none||附件类型，目前仅支持image|
|»»» image|object|false|none||none|
|»»»» media_id|string|true|none||图片的media_id，可以通过获取临时素材下载资源|

## POST 创建商品图册

POST /cgi-bin/externalcontact/add_product_album

企业和第三方应用可以通过此接口增加商品

权限说明:

企业需要使用“客户联系”secret或配置到“可调用应用”列表中的自建应用secret所获取的accesstoken来调用（accesstoken如何获取？）。
第三方应用或代开发自建应用调用需要企业授权客户联系下管理商品图册的权限

https://open.work.weixin.qq.com/api/doc/90000/90135/95096

> Body 请求参数

```json
{
  "description": "世界上最好的商品",
  "price": 30000,
  "product_sn": "xxxxxxxx",
  "attachments": [
    {
      "type": "image",
      "image": {
        "media_id": "MEDIA_ID"
      }
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» description|body|string| 是 |商品的名称、特色等;不超过300个字|
|» price|body|integer| 是 |商品的价格，单位为分；最大不超过5万元|
|» product_sn|body|string| 否 |商品编码；不超过128个字节；只能输入数字和字母|
|» attachments|body|[object]| 是 |附件类型，仅支持image，最多不超过9个附件|
|»» type|body|string| 否 |none|
|»» image|body|object| 否 |none|
|»»» media_id|body|string| 是 |图片的media_id，可以通过上传附件资源接口获得|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "product_id": "xxxxxxxxxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» product_id|string|true|none||商品id|

# 企业内部开发/客户联系/管理聊天敏感词

## POST 管理聊天敏感词-获取敏感词规则详情

POST /cgi-bin/externalcontact/get_intercept_rule

企业和第三方应用可以通过此接口获取敏感词规则详情

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95097#10013/第三步：获取access_token)）。
- 第三方应用或者代开发自建应用调用需要企业授权**客户联系**下**管理敏感词**的权限
- 使用范围只返回应用可见范围内的成员跟部门

文档ID: 34656
 原文档地址：https://open.work.weixin.qq.com/api/doc/90000/90135/95097

> Body 请求参数

```json
{
  "rule_id": "xxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» rule_id|body|string| 是 |规则id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "rule": {
    "rule_id": 1,
    "rule_name": "rulename",
    "word_list": [
      "敏感词1",
      "敏感词2"
    ],
    "extra_rule": {
      "semantics_list": [
        1,
        2,
        3
      ]
    },
    "intercept_type": 1,
    "applicable_range": {
      "user_list": [
        "zhangshan"
      ],
      "department_list": [
        2,
        3
      ]
    }
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» rule|object|false|none||none|
|»» rule_id|integer(int32)|false|none||规则id|
|»» rule_name|string|false|none||规则名称，长度上限20个字符|
|»» applicable_range|object|false|none||附件类型，仅支持image，userid与department不能同时为不填|
|»»» department_list|[integer]|false|none||none|
|»»» user_list|[string]|false|none||none|
|»» word_list|[string]|false|none||敏感词列表，敏感词不能超过30个字符，列表大小不能超过300个|
|»» extra_rule|object|false|none||额外的规则|
|»»» semantics_list|[integer]|false|none||额外的拦截语义规则，1：手机号、2：邮箱地:、3：红包|
|»» intercept_type|integer(int32)|false|none||拦截方式，1:警告并拦截发送；2:仅发警告|

## POST 管理聊天敏感词-修改敏感词规则

POST /cgi-bin/externalcontact/update_intercept_rule

企业和第三方应用可以通过此接口修改敏感词规则

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95097#10013/第三步：获取access_token)）。
- 第三方应用或者代开发自建应用调用需要企业授权**客户联系**下**管理敏感词**的权限
- 应用只可修改应用自己创建的敏感词规则；客户联系系统应用可修改所有规则

文档ID: 34656
 原文档地址：https://open.work.weixin.qq.com/api/doc/90000/90135/95097

> Body 请求参数

```json
"{\"rule_id\":\"xxxx\",\"rule_name\":\"rulename\",\"word_list\":[\"敏感词1\",\"敏感词2\"],\"extra_rule\":{\"semantics_list\":[1,2,3],},\"intercept_type\":1,\"add_applicable_range\":{\"user_list\":[\"zhangshan\"],\"department_list\":[2,3]},\"remove_applicable_range\":{\"user_list\":[\"zhangshan\"],\"department_list\":[2,3]}}"
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» rule_id|body|string| 是 |规则id|
|» rule_name|body|string| 否 |规则名称，长度1~20个utf8字符|
|» word_list|body|[string]| 否 |敏感词列表，敏感词长度1~32个utf8字符，列表大小不能超过300个；若为空忽略该字段|
|» extra_rule|body|object| 否 |额外的规则|
|»» semantics_list|body|[integer]| 否 |额外的拦截语义规则，1：手机号、2：邮箱地:、3：红包；若为空表示清楚所有的语义规则|
|» remove_applicable_range|body|object| 否 |需要删除的使用范围|
|»» department_list|body|[integer]| 否 |none|
|»» user_list|body|[string]| 否 |none|
|» intercept_type|body|integer(int32)| 否 |拦截方式，1:警告并拦截发送；2:仅发警告|
|» add_applicable_range|body|object| 否 |需要新增的使用范围|
|»» department_list|body|[integer]| 否 |none|
|»» user_list|body|[string]| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 管理聊天敏感词 删除敏感词规则

POST /cgi-bin/externalcontact/del_intercept_rule

企业和第三方应用可以通过此接口修改敏感词规则

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95097#10013/第三步：获取access_token)）。
- 第三方应用或者代开发自建应用调用需要企业授权**客户联系**下**管理敏感词**的权限
- 应用只可删除应用自己创建的敏感词规则；客户联系系统应用可删除所有规则

文档ID: 34656
 原文档地址：https://open.work.weixin.qq.com/api/doc/90000/90135/95097

> Body 请求参数

```json
{
  "rule_id": "xxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» rule_id|body|string| 是 |规则id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 管理聊天敏感词-新建敏感词规则

POST /cgi-bin/externalcontact/add_intercept_rule

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95097#10013/第三步：获取access_token)）。
- 第三方应用或者代开发自建应用调用需要企业授权**客户联系**下**管理敏感词**的权限

文档ID: 34656
 原文档地址：https://open.work.weixin.qq.com/api/doc/90000/90135/95097

> Body 请求参数

```json
{
  "rule_name": "rulename",
  "word_list": [
    "敏感词1",
    "敏感词2"
  ],
  "semantics_list": [
    1,
    2,
    3
  ],
  "intercept_type": 1,
  "applicable_range": {
    "user_list": [
      "zhangshan"
    ],
    "department_list": [
      2,
      3
    ]
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» rule_name|body|string| 是 |规则名称，长度1~20个utf8字符|
|» applicable_range|body|object| 是 |附件类型，仅支持image，userid与department不能同时为不填|
|»» department_list|body|[integer]| 否 |none|
|»» user_list|body|[string]| 否 |none|
|» word_list|body|[string]| 是 |敏感词列表，敏感词长度1~32个utf8字符，列表大小不能超过300个|
|» intercept_type|body|integer(int32)| 是 |拦截方式，1:警告并拦截发送；2:仅发警告|
|» semantics_list|body|[integer]| 否 |额外的拦截语义规则，1：手机号、2：邮箱地:、3：红包|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "rule_id": "xxx"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» rule_id|string|false|none||规则id|
|» errmsg|string|false|none||对返回码的文本描述内容|

## GET 管理聊天敏感词-获取敏感词规则列表

GET /cgi-bin/externalcontact/get_intercept_rule_list

**权限说明**:

- 企业需要使用[“客户联系”secret](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)或配置到“[可调用应用](https://developer.work.weixin.qq.com/document/path/95097#13473/开始开发)”列表中的自建应用secret所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95097#10013/第三步：获取access_token)）。
- 第三方应用或者代开发自建应用调用需要企业授权**客户联系**下**管理敏感词**的权限
- 可获取企业所有敏感词规则

文档ID: 34656
 原文档地址：https://open.work.weixin.qq.com/api/doc/90000/90135/95097

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "rule_list": [
    {
      "rule_id": "xxxx",
      "rule_name": "rulename",
      "create_time": 1600000000
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» rule_list|[object]|false|none||none|
|»» rule_id|string|false|none||规则id|
|»» create_time|integer(int32)|false|none||创建时间|
|»» rule_name|string|false|none||规则名称，长度上限20个字符|

# 企业内部开发/微信客服/客服帐号管理

## POST 添加客服帐号

POST /cgi-bin/kf/account/add

添加客服帐号，并可设置客服名称和头像。目前一家企业最多可添加10个客服帐号。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94662#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94662#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限

注：通过接口创建的客服帐号，将自动拥有该客服帐号的管理权限。企业可在管理后台“微信客服-通过API管理微信客服帐号”处设置对应的客服帐号通过API来管理。

文档ID: 31140
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94662
https://open.work.weixin.qq.com/api/doc/90001/90143/94688

> Body 请求参数

```json
{
  "name": "新建的客服帐号",
  "media_id": "294DpAog3YA5b9rTK4PjjfRfYLO0L5qpDHAJIzhhQ2jAEWjb9i661Q4lk8oFnPtmj"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 否 |none|
|» media_id|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "open_kfid": "wkAJ2GCAAAZSfhHCt7IFSvLKtMPxyJTw"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» open_kfid|string|false|none||none|

## POST 修改客服帐号

POST /cgi-bin/kf/account/update

修改已有的客服帐号，可修改客服名称和头像。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94664#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94664#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 只能通过API管理企业指定的客服帐号。企业可在管理后台“微信客服-通过API管理微信客服帐号”处设置对应的客服帐号通过API来管理。
文档ID: 31142
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94664
https://open.work.weixin.qq.com/api/doc/90001/90143/94690

> Body 请求参数

```json
{
  "open_kfid": "wkAJ2GCAAAZSfhHCt7IFSvLKtMPxyJTw",
  "name": "修改客服名",
  "media_id": "294DpAog3YA5b9rTK4PjjfRfYLO0L5qpDHAJIzhhQ2jAEWjb9i661Q4lk8oFnPtmj"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 否 |none|
|» media_id|body|string| 否 |none|
|» open_kfid|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

## GET 获取客服帐号列表

GET /cgi-bin/kf/account/list

获取客服帐号列表，包括所有的客服帐号的客服ID、名称和头像。

当客服较多时，需要使用参数**offset**及**limit** 分页获取，注意offset是**以0为起点**，这里以图例简单说明：
![page_size/page_index图示说明](https://p.qpic.cn/pic_wework/23479275/c4c278d43a5f745881cf2b83b6c955e1a7f0091d48fb0822/0)当获取到的 **account_list**中的账号数量小于 **指定的limit** 的时候，表示已经没有更多的数据，此时应终止获取。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94661#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94661#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->获取基础信息”权限
- 代开发自建应用需具有“微信客服->获取基础信息”权限

> Body 请求参数

```json
{
  "offset": 0,
  "limit": 100
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» offset|body|integer| 是 |分页，偏移量, 默认为0|
|» limit|body|integer| 是 |分页，预期请求的数据量，默认为100，取值范围 1 ~ 100|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "account_list": [
    {
      "open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
      "name": "咨询客服",
      "avatar": "https://wework.qpic.cn/wwhead/duc2TvpEgSSjibPZlNR6chpx9W3dtd9Ogp8XEmSNKGa6uufMWn2239HUPuwIFoYYZ7Ph580FPvo8/0"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||错误码描述|
|» account_list|[object]|false|none||帐号信息列表|
|»» name|string|false|none||客服名称|
|»» avatar|string|false|none||客服头像URL|
|»» open_kfid|string|false|none||客服帐号ID|

## POST 获取客服帐号链接

POST /cgi-bin/kf/add_contact_way

企业可通过此接口获取带有不同参数的客服链接，不同客服帐号对应不同的客服链接。获取后，企业可将链接嵌入到网页等场景中，微信用户点击链接即可向对应的客服帐号发起咨询。企业可依据参数来识别用户的咨询来源等。

> 1. 若scene非空，返回的客服链接开发者可拼接`scene_param=SCENE_PARAM`参数使用，[用户进入会话事件](https://open.work.weixin.qq.com/api/doc/90000/90135/94665#31078/用户进入会话事件)会将SCENE_PARAM原样返回。**其中SCENE_PARAM需要urlencode，且长度不能超过128字节**。
>    如 https://work.weixin.qq.com/kf/kfcbf8f8d07ac7215f?enc_scene=ENCGFSDF567DF**&scene_param=a%3D1%26b%3D2**
> 2. **历史调用接口**返回的客服链接（包含`encScene=XXX`参数），**不支持**scene_param参数。
> 3. 返回的客服链接，**不能修改或复制参数到其他链接使用**。否则进入会话事件参数校验不通过，导致无法回调。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94665#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94665#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->获取基础信息”权限
- 代开发自建应用需具有“微信客服->获取基础信息”权限

文档ID: 31144
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/94692
https://open.work.weixin.qq.com/api/doc/90000/90135/94665

> Body 请求参数

```json
{
  "open_kfid": "OPEN_KFID",
  "scene": "12345"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID|
|» scene|body|string| 是 |场景值，字符串类型，由开发者自定义。 不多于32字节 字符串取值范围(正则表达式)：[0-9a-zA-Z_-]*|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "url": "https://work.weixin.qq.com/kf/kfcbf8f8d07ac7215f?enc_scene=ENCGFSDF567DF"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» url|string|true|none||客服链接，开发者可将该链接嵌入到H5页面中，用户点击链接即可向对应的微信客服帐号发起咨询。开发者也可根据该url自行生成需要的二维码图片|

## POST 删除客服帐号

POST /cgi-bin/kf/account/del

删除已有的客服帐号
**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94663#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94663#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 只能通过API管理企业指定的客服帐号。企业可在管理后台“微信客服-通过API管理微信客服帐号”处设置对应的客服帐号通过API来管理。

文档ID: 31141
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94663
https://open.work.weixin.qq.com/api/doc/90001/90143/94689

> Body 请求参数

```json
{
  "open_kfid": "wkAJ2GCAAAZSfhHCt7IFSvLKtMPxyJTw"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

# 企业内部开发/微信客服/接待人员管理

## POST 添加接待人员

POST /cgi-bin/kf/servicer/add

添加指定客服帐号的接待人员，每个客服帐号目前最多可添加2000个接待人员，20个部门。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94646#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94646#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->管理帐号、分配会话和收发消息”权限。仅可将应用可见范围内的成员添加为接待人员
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限。仅可将应用可见范围内的成员添加为接待人员
- 只能通过API管理企业指定的客服帐号。企业可在管理后台“微信客服-通过API管理微信客服帐号”处设置对应的客服帐号通过API来管理。

参数说明：userid_list和department_id_list至少需要填其中一个

文档ID: 31065
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94646
https://open.work.weixin.qq.com/api/doc/90001/90143/94695

> Body 请求参数

```json
{
  "open_kfid": "kfxxxxxxxxxxxxxx",
  "userid_list": [
    "zhangsan",
    "lisi"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID|
|» userid_list|body|[string]| 是 |接待人员userid列表。第三方应用填密文userid，即open_userid 可填充个数：1 ~ 100。超过100个需分批调用。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "result_list": [
    {
      "userid": "zhangsan",
      "errcode": 0,
      "errmsg": "success"
    },
    {
      "userid": "lisi",
      "errcode": 0,
      "errmsg": "ignored"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» result_list|[object]|false|none||操作结果|
|»» errcode|integer(int32)|false|none||该userid的添加结果|
|»» errmsg|string|false|none||结果信息|
|»» userid|string|false|none||接待人员的userid|

## GET 获取接待人员列表

GET /cgi-bin/kf/servicer/list

获取某个客服帐号的接待人员列表

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94645#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94645#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->获取基础信息”权限
- 代开发自建应用需具有“微信客服权限->获取基础信息”权限

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|open_kfid|query|string| 是 |客服帐号ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "servicer_list": [
    {
      "userid": "zhangsan",
      "status": 0
    },
    {
      "userid": "lisi",
      "status": 1
    },
    {
      "department_id": 2
    },
    {
      "department_id": 3
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» servicer_list|[object]|true|none||客服帐号的接待人员列表|
|»» userid|string|true|none||接待人员的userid。第三方应用获取到的为密文userid，即open_userid|
|»» status|integer|true|none||接待人员的接待状态。0:接待中,1:停止接待。 注：企业内部开发，需有该客服账号的管理权限；第三方/代开发应用需具有“管理帐号、分配会话和收发消息”权限，且有该客服账号的管理权限，才可获取|
|»» department_id|integer|true|none||接待人员部门的id|

## POST 删除接待人员

POST /cgi-bin/kf/servicer/del

从客服帐号删除接待人员

参数说明：userid_list和departmentid_list至少需要填其中一个

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94647#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94647#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 只能通过API管理企业指定的客服帐号。企业可在管理后台“微信客服-通过API管理微信客服帐号”处设置对应的客服帐号通过API来管理。

> Body 请求参数

```json
{
  "open_kfid": "kfxxxxxxxxxxxxxx",
  "userid_list": [
    "zhangsan",
    "lisi"
  ],
  "department_id_list": [
    2,
    4
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID|
|» userid_list|body|[string]| 是 |接待人员userid列表。第三方应用填密文userid，即open_userid 可填充个数：1 ~ 100。超过100个需分批调用。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "result_list": [
    {
      "userid": "zhangsan",
      "errcode": 0,
      "errmsg": "success"
    },
    {
      "userid": "lisi",
      "errcode": 0,
      "errmsg": "ignored"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» result_list|[object]|false|none||操作结果|
|»» errcode|integer(int32)|false|none||该userid的删除结果|
|»» errmsg|string|false|none||结果信息|
|»» userid|string|false|none||接待人员的userid|
|»» department_id|integer|false|none||接待人员部门的id|

# 企业内部开发/微信客服/会话分配与消息收发

## POST 发送事件响应消息

POST /cgi-bin/kf/send_msg_on_event

## 概述

当特定的[事件回调消息](https://open.work.weixin.qq.com/api/doc/90000/90135/95122#31078/事件消息)包含code字段，或通过接口变更到特定的[会话状态](https://open.work.weixin.qq.com/api/doc/90000/90135/95122#31080/分配客服会话)，会返回code字段。
开发者可以此code为凭证，**调用该接口给用户发送相应事件场景下的消息**，**如客服欢迎语、客服提示语和会话结束语等。**
除”用户进入会话事件”以外，**响应消息仅支持会话处于获取该code的会话状态时发送**，如将会话转入待接入池时获得的code仅能在会话状态为”待接入池排队中“时发送。

目前支持的事件场景和相关约束如下：

| 事件场景                                                     | 允许下发条数 | code有效期 | 支持的消息类型 | 获取code途径           |
| ------------------------------------------------------------ | ------------ | ---------- | -------------- | ---------------------- |
| 用户进入会话，用于发送客服欢迎语                             | 1条          | 20秒       | 文本、菜单     | 事件回调               |
| 进入接待池，用于发送排队提示语等                             | 1条          | 48小时     | 文本           | 转接会话接口           |
| 从接待池接入会话，用于发送非工作时间的提示语或超时未回复的提示语等 | 1条          | 48小时     | 文本           | 事件回调、转接会话接口 |
| 结束会话，用于发送结束会话提示语或满意度评价等               | 1条          | 20秒       | 文本、菜单     | 事件回调、转接会话接口 |

> 「进入会话事件」响应消息：
> 如果满足通过API下发欢迎语条件（条件为：**用户在过去48小时里未收过欢迎语，且未向客服发过消息**），则[用户进入会话事件](https://open.work.weixin.qq.com/api/doc/90000/90135/95122#31078/用户进入会话事件)会额外返回一个`welcome_code`，开发者以此为凭据调用接口（填到该接口`code`参数），即可向客户发送客服欢迎语。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://open.work.weixin.qq.com/api/doc/90000/90135/95122#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://open.work.weixin.qq.com/api/doc/90000/90135/95122#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->管理帐号、分配会话和收发消息”权限

## 消息类型

### 文本消息

**请求示例：**

```
{   "code": "CODE",   "msgid": "MSG_ID",   "msgtype" : "text",   "text" : {       "content" : "欢迎咨询"   }}
```

**参数说明：**

| 参数         | 是否必须 | 类型   | 说明                           |
| ------------ | -------- | ------ | ------------------------------ |
| msgtype      | 是       | string | 消息类型，此时固定为：text     |
| text         | 是       | obj    | 文本消息                       |
| text.content | 是       | string | 消息内容，最长不超过2048个字节 |

### 菜单消息

**请求示例：**

```
{  "code": "CODE",  "msgid": "MSG_ID",  "msgtype": "msgmenu",  "msgmenu": {    "head_content": "欢迎咨询",    "list": [      {        "type": "click",        "click":        {            "id": "101",            "content": "接入人工"        }      },      {        "type": "click",        "click":        {            "id": "102",            "content": "继续跟机器人聊天"        }      },      {        "type": "view",        "view":        {            "url": "https://work.weixin.qq.com",            "content": "点击跳转到自助查询页面"        }      },      {        "type": "miniprogram",        "miniprogram":        {            "appid": "wx123123123123123",            "pagepath": "pages/index?userid=zhangsan&orderid=123123123",            "content": "点击打开小程序查询更多"        }      }    ],    "tail_content": "如有问题，随时转人工服务"  }}
```

**参数说明：**

| 参数                              | 必须 | 类型   | 说明                                                         |
| :-------------------------------- | :--- | :----- | :----------------------------------------------------------- |
| msgtype                           | 是   | string | 消息类型，此时固定为：msgmenu                                |
| msgmenu                           | 是   | obj    | 菜单消息                                                     |
| msgmenu.head_content              | 否   | string | 起始文本 不多于1024字节                                      |
| msgmenu.list                      | 否   | obj[]  | 菜单项配置                                                   |
| msgmenu.list.type                 | 是   | string | 菜单类型。 `click`-回复菜单 `view`-超链接菜单 `miniprogram`-小程序菜单 |
| msgmenu.list.click                | 否   | obj    | type为`click`的菜单项                                        |
| msgmenu.list.click.id             | 否   | string | 菜单ID。 不少于1字节 不多于64字节                            |
| msgmenu.list.click.content        | 是   | string | 菜单显示内容 不少于1字节 不多于128字节                       |
| msgmenu.list.view                 | 否   | obj    | type为`view`的菜单项                                         |
| msgmenu.list.view.url             | 是   | string | 点击后跳转的链接。 不少于1字节 不多于2048字节                |
| msgmenu.list.view.content         | 是   | string | 菜单显示内容。 不少于1字节 不多于1024字节                    |
| msgmenu.list.miniprogram          | 否   | obj    | type为`miniprogram`的菜单项                                  |
| msgmenu.list.miniprogram.appid    | 是   | string | 小程序appid。 不少于1字节 不多于32字节                       |
| msgmenu.list.miniprogram.pagepath | 是   | string | 点击后进入的小程序页面。 不少于1字节 不多于1024字节          |
| msgmenu.list.miniprogram.content  | 是   | string | 菜单显示内容。 不多于1024字节                                |
| msgmenu.tail_content              | 否   | string | 结束文本 不多于1024字节                                      |

文档ID: 32344
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/95122
https://open.work.weixin.qq.com/api/doc/90001/90143/94910

> Body 请求参数

```json
{
  "code": "CODE",
  "msgid": "MSG_ID",
  "msgtype": "MSG_TYPE"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» code|body|string| 是 |事件响应消息对应的code。通过事件回调下发，仅可使用一次。|
|» msgid|body|string| 否 |消息ID。如果请求参数指定了msgid，则原样返回，否则系统自动生成并返回。 不多于32字节 字符串取值范围(正则表达式)：[0-9a-zA-Z_-]*|
|» msgtype|body|string| 是 |消息类型。对不同的msgtype，有相应的结构描述，详见消息类型|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "msgid": "MSG_ID"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||错误码描述|
|» msgid|string|true|none||消息ID|

## POST 接收消息和事件-读取消息

POST /cgi-bin/kf/sync_msg

# 概述

当微信客户、接待人员发消息或有行为动作时，企业微信后台会将事件的回调数据包发送到企业指定URL；企业收到请求后，再通过读取消息接口主动读取具体的消息内容。

# 回调事件

接收并解析事件的方法见：[接收事件](https://developer.work.weixin.qq.com/document/path/94670#12977)。
**示例**

```javascript
<xml>
   <ToUserName><![CDATA[ww12345678910]]></ToUserName>
   <CreateTime>1348831860</CreateTime>
   <MsgType><![CDATA[event]]></MsgType>
   <Event><![CDATA[kf_msg_or_event]]></Event>
   <Token><![CDATA[ENCApHxnGDNAVNY4AaSJKj4Tb5mwsEMzxhFmHVGcra996NR]]></Token>
   <OpenKfId><![CDATA[wkxxxxxxx]]></OpenKfId>
</xml>
```

**说明**

| 参数       | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| ToUserName | 企业微信CorpID                                               |
| CreateTime | 消息创建时间，unix时间戳                                     |
| MsgType    | 消息的类型，此时固定为：event                                |
| Event      | 事件的类型，此时固定为：kf_msg_or_event                      |
| Token      | 调用拉取消息接口时，需要传此token，用于校验请求的合法性      |
| OpenKfId   | 有新消息的客服账号。可通过sync_msg接口指定`open_kfid`获取此客服账号的消息 |

# 读取消息

微信客户发送的消息、接待人员在企业微信回复的消息、[发送消息](https://developer.work.weixin.qq.com/document/path/94670#25217)接口发送失败事件（如被用户拒收）、客户点击菜单消息的回复消息，可以通过该接口获取**最近3天内**具体的消息内容和事件。**不支持读取通过发送消息接口发送的消息**。
支持的消息类型：文本、图片、语音、视频、文件、位置、链接、名片、小程序、菜单、事件。

## 消息类型

### 文本消息

**返回示例：**

```javascript
{
   "msgtype" : "text",
   "text" : {
        "content" : "hello world",
		"menu_id" : "101"
   }
}
```

**参数说明：**

| 参数         | 类型   | 说明                                                         |
| ------------ | ------ | ------------------------------------------------------------ |
| msgtype      | string | 消息类型，此时固定为：text                                   |
| text         | obj    | 文本消息                                                     |
| text.content | string | 文本内容                                                     |
| text.menu_id | string | 客户点击[菜单消息](https://developer.work.weixin.qq.com/document/path/94670#25217/菜单消息)，触发的回复消息中附带的菜单ID |

### 图片消息

**返回示例：**

```javascript
{
   "msgtype" : "image",
   "image" : {
        "media_id" : "2iSLeVyqzk4eX0IB5kTi9Ljfa2rt9dwfq5WKRQ4Nvvgw"
   }
}
```

**参数说明：**

| 参数           | 类型   | 说明                        |
| -------------- | ------ | --------------------------- |
| msgtype        | string | 消息类型，此时固定为：image |
| image          | obj    | 图片消息                    |
| image.media_id | string | 图片文件id                  |

### 语音消息

**返回示例：**

```javascript
{
   "msgtype" : "voice",
   "voice" : {
        "media_id" : "2iSLeVyqzk4eX0IB5kTi9Ljfa2rt9dwfq5WKRQ4Nvvgw"
   }
}
```

**参数说明：**

| 参数           | 类型   | 说明                        |
| -------------- | ------ | --------------------------- |
| msgtype        | string | 消息类型，此时固定为：voice |
| voice          | obj    | 语音消息                    |
| voice.media_id | string | 语音文件ID                  |

### 视频消息

**返回示例：**

```javascript
{
   "msgtype" : "video",
   "video" : {
        "media_id" : "2iSLeVyqzk4eX0IB5kTi9Ljfa2rt9dwfq5WKRQ4Nvvgw"
   }
}
```

**参数说明：**

| 参数           | 类型   | 说明                        |
| -------------- | ------ | --------------------------- |
| msgtype        | string | 消息类型，此时固定为：video |
| video          | obj    | 视频消息                    |
| video.media_id | string | 文件id                      |

### 文件消息

**返回示例：**

```javascript
{
   "msgtype" : "file",
   "file" : {
        "media_id" : "2iSLeVyqzk4eX0IB5kTi9Ljfa2rt9dwfq5WKRQ4Nvvgw"
   }
}
```

**参数说明：**

| 参数          | 类型   | 说明                       |
| ------------- | ------ | -------------------------- |
| msgtype       | string | 消息类型，此时固定为：file |
| file          | obj    | 文件消息                   |
| file.media_id | string | 文件id                     |

### 位置消息

**返回示例：**

```javascript
{
   "msgtype" : "location",
   "location" : {
		"latitude": 23.106021881103501,
		"longitude": 113.320503234863,
		"name": "广州国际媒体港(广州市海珠区)",
		"address": "广东省广州市海珠区滨江东路"
   }
}
```

**参数说明：**

| 参数               | 类型   | 说明                           |
| ------------------ | ------ | ------------------------------ |
| msgtype            | string | 消息类型，此时固定为：location |
| location           | obj    | 地理位置消息                   |
| location.latitude  | float  | 纬度                           |
| location.longitude | float  | 经度                           |
| location.name      | string | 位置名                         |
| location.address   | string | 地址详情说明                   |

### 链接消息

**返回示例：**

```javascript
{
   "msgtype" : "link",
   "link" : {
		"title": "TITLE",
		"desc": "DESC",
		"url": "URL",
		"pic_url": "PIC_URL"
   }
}
```

**参数说明：**

| 参数         | 类型   | 说明                       |
| ------------ | ------ | -------------------------- |
| msgtype      | string | 消息类型，此时固定为：link |
| link         | obj    | 链接消息                   |
| link.title   | string | 标题                       |
| link.desc    | string | 描述                       |
| link.url     | string | 点击后跳转的链接           |
| link.pic_url | string | 缩略图链接                 |

### 名片消息

**返回示例：**

```javascript
{
   "msgtype" : "business_card",
   "business_card" : {
		"userid": "USERID"
   }
}
```

**参数说明：**

| 参数                 | 类型   | 说明                                |
| -------------------- | ------ | ----------------------------------- |
| msgtype              | string | 消息类型，此时固定为：business_card |
| business_card        | obj    | 名片消息                            |
| business_card.userid | string | 名片userid                          |

 

### 小程序消息

**返回示例：**

```javascript
{
   "msgtype" : "miniprogram",
   "miniprogram" : {
		"title": "TITLE",
		"appid": "APPID",
		"pagepath": "PAGE_PATH",
		"thumb_media_id": "THUMB_MEDIA_ID"
   }
}
```

**参数说明：**

| 参数                       | 类型   | 说明                               |
| -------------------------- | ------ | ---------------------------------- |
| msgtype                    | string | 消息类型，此时固定为：miniprogram  |
| miniprogram                | obj    | 小程序消息                         |
| miniprogram.title          | string | 标题                               |
| miniprogram.appid          | string | 小程序appid                        |
| miniprogram.pagepath       | string | 点击消息卡片后进入的小程序页面路径 |
| miniprogram.thumb_media_id | string | 小程序消息封面的mediaid            |

### 菜单消息

**返回示例：**

```javascript
{
   "msgtype" : "msgmenu",
   "msgmenu": {
    "head_content": "您对本次服务是否满意呢? ",
    "list": [
      {
	    "type": "click",
		"click":
		{
        	"id": "101",
        	"content": "满意"
		}
      },
      {
	    "type": "click",
		"click":
		{
        	"id": "102",
        	"content": "不满意"
		}
      },
	  {
	    "type": "view",
		"view":
		{
        	"url": "https://work.weixin.qq.com",
        	"content": "点击跳转到自助查询页面"
		}
      },
	  {
	    "type": "miniprogram",
		"miniprogram":
		{
        	"appid": "wx123123123123123",
			"pagepath": "pages/index?userid=zhangsan&orderid=123123123",
        	"content": "点击打开小程序查询更多"
		}
      }
    ],
    "tail_content": "欢迎再次光临"
  }
}
```

**参数说明：**

| 参数                              | 类型   | 说明                                                         |
| --------------------------------- | ------ | ------------------------------------------------------------ |
| msgtype                           | string | 消息类型，此时固定为：msgmenu                                |
| msgmenu                           | obj    | 菜单消息                                                     |
| msgmenu.head_content              | string | 起始文本                                                     |
| msgmenu.list                      | obj[]  | 菜单项配置                                                   |
| msgmenu.list.type                 | string | 菜单类型。 `click`-回复菜单 `view`-超链接菜单 `miniprogram`-小程序菜单 |
| msgmenu.list.click                | obj    | type为`click`的菜单项                                        |
| msgmenu.list.click.id             | string | 菜单ID                                                       |
| msgmenu.list.click.content        | string | 菜单显示内容                                                 |
| msgmenu.list.view                 | obj    | type为`view`的菜单项                                         |
| msgmenu.list.view.url             | string | 点击后跳转的链接                                             |
| msgmenu.list.view.content         | string | 菜单显示内容                                                 |
| msgmenu.list.miniprogram          | obj    | type为`miniprogram`的菜单项                                  |
| msgmenu.list.miniprogram.appid    | string | 小程序appid                                                  |
| msgmenu.list.miniprogram.pagepath | string | 点击后进入的小程序页面                                       |
| msgmenu.list.miniprogram.content  | string | 菜单显示内容                                                 |
| msgmenu.tail_content              | string | 结束文本                                                     |

### 视频号商品消息

**返回示例：**

```javascript
{
   "msgtype" : "channels_shop_product",
   "channels_shop_product" : {
		"product_id": "PRODUCT_ID",
		"head_img": "PRODUCT_IMANGE_URL",
		"title": "TITLE",
		"sales_price": "SALES_PRICE",
		"shop_nickname": "SHOP_NICKNAME",
		"shop_head_img": "SHOP_HEAD_IMG"
   }
}
```

**参数说明：**

| 参数                                | 类型   | 说明                                        |
| ----------------------------------- | ------ | ------------------------------------------- |
| msgtype                             | string | 消息类型，此时固定为：channels_shop_product |
| channels_shop_product               | obj    | 视频号商品消息                              |
| channels_shop_product.product_id    | string | 商品ID                                      |
| channels_shop_product.head_img      | string | 商品图片                                    |
| channels_shop_product.title         | string | 商品标题                                    |
| channels_shop_product.sales_price   | string | 商品价格，以分为单位                        |
| channels_shop_product.shop_nickname | string | 店铺名称                                    |
| channels_shop_product.shop_head_img | string | 店铺头像                                    |

### 视频号订单消息

**返回示例：**

```javascript
{
   "msgtype" : "channels_shop_order",
   "channels_shop_order" : {
		"order_id": "ORDER_ID",
		"product_titles":"PRODUCT_TITLES",
		"price_wording":"PRICE_WORDING",
		"state":"STATE",
		"image_url":"IMAGE_URL",
		"shop_nickname":"SHOP_NICKNAME"
   }
}
```

**参数说明：**

| 参数                               | 类型   | 说明                                      |
| ---------------------------------- | ------ | ----------------------------------------- |
| msgtype                            | string | 消息类型，此时固定为：channels_shop_order |
| channels_shop_order                | obj    | 视频号订单消息                            |
| channels_shop_order.order_id       | string | 订单号                                    |
| channels_shop_order.product_titles | string | 商品标题                                  |
| channels_shop_order.price_wording  | string | 订单价格描述                              |
| channels_shop_order.state          | string | 订单状态                                  |
| channels_shop_order.image_url      | string | 订单缩略图                                |
| channels_shop_order.shop_nickname  | string | 店铺名称                                  |

### 事件消息

#### 用户进入会话事件

**返回示例：**

```javascript
{
   "msgtype" : "event",
   "event" : {
        "event_type": "enter_session",
		"open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
        "external_userid": "wmAJ2GCAAAme1XQRC-NI-q0_ZM9ukoAw",
		"scene": "123",
		"scene_param": "abc",
		"welcome_code": "aaaaaa",
		"wechat_channels": {
			"nickname": "进入会话的视频号名称",
			"scene":1
		}
   }
}
```

**参数说明：**

| 参数                                | 类型   | 说明                                                         |
| ----------------------------------- | ------ | ------------------------------------------------------------ |
| msgtype                             | string | 消息类型，此时固定为：event                                  |
| event                               | obj    | 事件消息                                                     |
| event.event_type                    | string | 事件类型。此处固定为：enter_session                          |
| event.open_kfid                     | string | 客服账号ID                                                   |
| event.external_userid               | string | 客户UserID                                                   |
| event.scene                         | string | 进入会话的场景值，[获取客服帐号链接](https://developer.work.weixin.qq.com/document/path/94670#31144)开发者自定义的场景值 |
| event.scene_param                   | string | 进入会话的自定义参数，[获取客服帐号链接](https://developer.work.weixin.qq.com/document/path/94670#31144)返回的url，开发者按规范拼接的scene_param参数 |
| event.welcome_code                  | string | 如果满足发送欢迎语条件（条件为：**用户在过去48小时里未收过欢迎语，且未向客服发过消息**），会返回该字段。 可用该welcome_code调用[发送事件响应消息](https://developer.work.weixin.qq.com/document/path/94670#32344)接口给客户发送欢迎语。 |
| event.wechat_channels               | obj    | 进入会话的视频号信息，从视频号进入会话才有值                 |
| event.wechat_channels.nickname      | string | 视频号名称，视频号场景值为1、2、3时返回此项                  |
| event.wechat_channels.shop_nickname | string | 视频号小店名称，视频号场景值为4、5时返回此项                 |
| event.wechat_channels.scene         | uint32 | 视频号场景值。1：视频号主页，2：视频号直播间商品列表页，3：视频号商品橱窗页，4：视频号小店商品详情页，5：视频号小店订单页 |

 

#### 消息发送失败事件

**返回示例：**

```javascript
{
   "msgtype" : "event",
   "event" : {
        "event_type": "msg_send_fail",
		"open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
        "external_userid": "wmAJ2GCAAAme1XQRC-NI-q0_ZM9ukoAw",
		"fail_msgid": "FAIL_MSGID",
		"fail_type": 4
   }
}
```

**参数说明：**

| 参数                  | 类型   | 说明                                                         |
| --------------------- | ------ | ------------------------------------------------------------ |
| msgtype               | string | 消息类型，此时固定为：event                                  |
| event                 | obj    | 事件消息                                                     |
| event.event_type      | string | 事件类型。此处固定为：msg_send_fail                          |
| event.open_kfid       | string | 客服账号ID                                                   |
| event.external_userid | string | 客户UserID                                                   |
| event.fail_msgid      | string | 发送失败的消息msgid                                          |
| event.fail_type       | uint32 | 失败类型。0-未知原因 1-客服账号已删除 2-应用已关闭 4-会话已过期，超过48小时 5-会话已关闭 6-超过5条限制 7-未绑定视频号 8-主体未验证 9-未绑定视频号且主体未验证 10-用户拒收 |

#### 接待人员接待状态变更事件

**返回示例：**

```javascript
{
   "msgtype" : "event",
   "event" : {
        "event_type": "servicer_status_change",
		"servicer_userid": "SERVICER_USERID",
		"status": 1,
		"open_kfid": "OPEN_KFID"
   }
}
```

**参数说明：**

| 参数                  | 类型   | 说明                                         |
| --------------------- | ------ | -------------------------------------------- |
| msgtype               | string | 消息类型，此时固定为：event                  |
| event                 | obj    | 事件消息                                     |
| event.event_type      | string | 事件类型。此处固定为：servicer_status_change |
| event.servicer_userid | string | 接待人员userid                               |
| event.status          | uint32 | 状态类型。1-接待中 2-停止接待                |
| event.open_kfid       | string | 客服帐号ID                                   |

#### 会话状态变更事件

**返回示例：**

```javascript
{
   "msgtype" : "event",
   "event" : {
        "event_type": "session_status_change",
		"open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
        "external_userid": "wmAJ2GCAAAme1XQRC-NI-q0_ZM9ukoAw",
		"change_type": 1,
		"old_servicer_userid": "OLD_SERVICER_USERID",
		"new_servicer_userid": "NEW_SERVICER_USERID",
		"msg_code": "MSG_CODE"
   }
}
```

**参数说明：**

| 参数                      | 类型   | 说明                                                         |
| ------------------------- | ------ | ------------------------------------------------------------ |
| msgtype                   | string | 消息类型，此时固定为：event                                  |
| event                     | obj    | 事件消息                                                     |
| event.event_type          | string | 事件类型。此处固定为：session_status_change                  |
| event.open_kfid           | string | 客服帐号ID                                                   |
| event.external_userid     | string | 客户UserID                                                   |
| event.change_type         | uint32 | 变更类型，**均为接待人员在企业微信客户端操作触发**。1-从接待池接入会话 2-转接会话 3-结束会话 4-重新接入已结束/已转接会话 |
| event.old_servicer_userid | string | 老的接待人员userid。仅`change_type`为2、3和4有值             |
| event.new_servicer_userid | string | 新的接待人员userid。仅`change_type`为1、2和4有值             |
| event.msg_code            | string | 用于发送事件响应消息的code，仅change_type为1和3时，会返回该字段。 可用该msg_code调用[发送事件响应消息](https://developer.work.weixin.qq.com/document/path/94670#32344)接口给客户发送回复语或结束语。 |

 

#### 用户撤回消息事件

**返回示例：**

```javascript
{
   "msgtype" : "event",
   "event" : {
        "event_type": "user_recall_msg",
        "open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
        "external_userid": "wmAJ2GCAAAme1XQRC-NI-q0_ZM9ukoAw",
        "recall_msgid": "RECALL_MSGID"
   }
}
```

**参数说明：**

| 参数                  | 类型   | 说明                                    |
| --------------------- | ------ | --------------------------------------- |
| msgtype               | string | 消息类型，此时固定为：event             |
| event                 | obj    | 事件消息                                |
| event.event_type      | string | 事件类型。此处固定为：`user_recall_msg` |
| event.open_kfid       | string | 客服账号ID                              |
| event.external_userid | string | 客户UserID                              |
| event.recall_msgid    | string | 撤回的消息msgid                         |

#### 接待人员撤回消息事件

**返回示例：**

```javascript
{
   "msgtype" : "event",
   "event" : {
        "event_type": "servicer_recall_msg",
        "open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
        "external_userid": "wmAJ2GCAAAme1XQRC-NI-q0_ZM9ukoAw",
        "recall_msgid": "RECALL_MSGID",
        "servicer_userid": "SERVICER_USERID"
   }
}
```

**参数说明：**

| 参数                  | 类型   | 说明                                        |
| --------------------- | ------ | ------------------------------------------- |
| msgtype               | string | 消息类型，此时固定为：event                 |
| event                 | obj    | 事件消息                                    |
| event.event_type      | string | 事件类型。此处固定为：`servicer_recall_msg` |
| event.open_kfid       | string | 客服账号ID                                  |
| event.external_userid | string | 客户UserID                                  |
| event.recall_msgid    | string | 撤回的消息msgid                             |
| event.servicer_userid | string | 接待人员userid                              |

> Body 请求参数

```json
{
  "cursor": "4gw7MepFLfgF2VC5npN",
  "token": "ENCApHxnGDNAVNY4AaSJKj4Tb5mwsEMzxhFmHVGcra996NR",
  "limit": 1000,
  "voice_format": 0,
  "open_kfid": "wkxxxxxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |上一次调用时返回的next_cursor，第一次拉取可以不填。若不填，从3天内最早的消息开始返回。 不多于64字节|
|» token|body|string| 否 |回调事件返回的token字段，10分钟内有效；可不填，如果不填接口有严格的频率限制。 不多于128字节|
|» limit|body|integer| 否 |期望请求的数据量，默认值和最大值都为1000。 注意：可能会出现返回条数少于limit的情况，需结合返回的has_more字段判断是否继续请求。|
|» voice_format|body|integer| 否 |语音消息类型，0-Amr 1-Silk，默认0。可通过该参数控制返回的语音格式，开发者可按需选择自己程序支持的一种格式|
|» open_kfid|body|string| 否 |指定拉取某个客服账号的消息，否则默认返回有权限的客服账号的消息。当客服账号较多，建议按open_kfid来拉取以获取更好的性能。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "4gw7MepFLfgF2VC5npN",
  "has_more": 1,
  "msg_list": [
    {
      "msgid": "from_msgid_4622416642169452483",
      "open_kfid": "wkAJ2GCAAASSm4_FhToWMFea0xAFfd3Q",
      "external_userid": "wmAJ2GCAAAme1XQRC-NI-q0_ZM9ukoAw",
      "send_time": 1615478585,
      "origin": 3,
      "servicer_userid": "Zhangsan",
      "msgtype": "MSG_TYPE"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» msg_list|[object]|false|none||消息列表|
|»» external_userid|string|false|none||客户UserID（msgtype为event，该字段不返回）|
|»» send_time|integer(int32)|false|none||消息发送时间|
|»» origin|integer(int32)|false|none||消息来源。3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息|
|»» msgid|string|false|none||消息ID|
|»» servicer_userid|string|false|none||从企业微信给客户发消息的客服人员userid（msgtype为event，该字段不返回）|
|»» open_kfid|string|false|none||客服帐号ID（msgtype为event，该字段不返回|
|»» msgtype|string|false|none||对不同的msgtype，有相应的结构描述，下面进一步说明|
|» next_cursor|string|false|none||下次调用带上该值，则从当前的位置继续往后拉，以实现增量拉取。 强烈建议对改该字段入库保存，每次请求读取带上，请求结束后更新。避免因意外丢，导致必须从头开始拉取，引起消息延迟。|
|» errmsg|string|false|none||none|
|» has_more|integer(int32)|false|none||是否还有更多数据。0-否；1-是。 不能通过判断msg_list是否空来停止拉取，可能会出现has_more为1，而msg_list为空的情况|

## POST 发送消息

POST /cgi-bin/kf/send_msg

## 概述

当微信客户处于[“新接入待处理”或“由智能助手接待”状态](https://open.work.weixin.qq.com/api/doc/90000/90135/94677#31080)下，可调用该接口给用户发送消息。
注意仅当微信客户在主动发送消息给客服后的48小时内，企业可发送消息给客户，最多可发送5条消息；若用户继续发送消息，企业可再次下发消息。
支持发送消息类型：文本、图片、语音、视频、文件、图文、小程序、菜单消息、地理位置。
目前该接口允许下发消息条数和下发时限如下：

| 用户动作     | 允许下发条数限制 | 下发时限 |
| ------------ | ---------------- | -------- |
| 用户发送消息 | 5条              | 48 小时  |

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94677#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94677#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限
- 只能通过API管理企业指定的客服帐号。企业可在管理后台“微信客服-通过API管理微信客服帐号”处设置对应的客服帐号通过API来管理。

## 消息类型

### 文本消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "text",   "text" : {       "content" : "你购买的物品已发货，可点击链接查看物流状态http://work.weixin.qq.com/xxxxxx"   }}
```

**参数说明：**

| 参数         | 是否必须 | 类型   | 说明                           |
| ------------ | -------- | ------ | ------------------------------ |
| touser       | 是       | string | 指定接收消息的客户UserID       |
| open_kfid    | 是       | string | 指定发送消息的客服帐号ID       |
| msgid        | 否       | string | 指定消息ID                     |
| msgtype      | 是       | string | 消息类型，此时固定为：text     |
| text         | 是       | obj    | 文本消息                       |
| text.content | 是       | string | 消息内容，最长不超过2048个字节 |

### 图片消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "image",   "image" : {        "media_id" : "MEDIA_ID"   }}
```

**请求参数：**

| 参数           | 是否必须 | 类型   | 说明                                     |
| -------------- | -------- | ------ | ---------------------------------------- |
| touser         | 是       | string | 指定接收消息的客户UserID                 |
| open_kfid      | 是       | string | 指定发送消息的客服帐号ID                 |
| msgid          | 否       | string | 指定消息ID                               |
| msgtype        | 是       | string | 消息类型，此时固定为：image              |
| image          | 是       | obj    | 图片消息                                 |
| image.media_id | 是       | string | 图片文件id，可以调用上传临时素材接口获取 |

### 语音消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgtype" : "voice",   "voice" : {        "media_id" : "MEDIA_ID"   }}
```

**参数说明：**

| 参数           | 是否必须 | 类型   | 说明                                                         |
| -------------- | -------- | ------ | ------------------------------------------------------------ |
| touser         | 是       | string | 指定接收消息的客户UserID                                     |
| open_kfid      | 是       | string | 指定发送消息的客服帐号ID                                     |
| msgid          | 否       | string | 指定消息ID                                                   |
| msgtype        | 是       | string | 消息类型，此时固定为：voice                                  |
| voice          | 是       | obj    | 语音消息                                                     |
| voice.media_id | 是       | string | 语音文件id，可以调用[上传临时素材](https://open.work.weixin.qq.com/api/doc/90000/90135/94677#25551)接口获取 |

### 视频消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "video",   "video" : {        "media_id" : "MEDIA_ID"   }}
```

**参数说明：**

| 参数           | 是否必须 | 类型   | 说明                                                         |
| -------------- | -------- | ------ | ------------------------------------------------------------ |
| touser         | 是       | string | 指定接收消息的客户UserID                                     |
| open_kfid      | 是       | string | 指定发送消息的客服帐号ID                                     |
| msgid          | 否       | string | 指定消息ID                                                   |
| msgtype        | 是       | string | 消息类型，此时固定为：video                                  |
| video          | 是       | obj    | 视频消息                                                     |
| video.media_id | 是       | string | 视频媒体文件id，可以调用[上传临时素材](https://open.work.weixin.qq.com/api/doc/90000/90135/94677#25551)接口获取 |

**视频消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/170804-tBAZwI.png?imageslim)

### 文件消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "file",   "file" : {        "media_id" : "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"   }}
```

**参数说明：**

| 参数          | 是否必须 | 类型   | 说明                                 |
| ------------- | -------- | ------ | ------------------------------------ |
| touser        | 是       | string | 指定接收消息的客户UserID             |
| open_kfid     | 是       | string | 指定发送消息的客服帐号ID             |
| msgid         | 否       | string | 指定消息ID                           |
| msgtype       | 是       | string | 消息类型，此时固定为：file           |
| file          | 是       | obj    | 文件消息                             |
| file.media_id | 是       | string | 文件id，可以调用上传临时素材接口获取 |

**文件消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/170804-zD0Uev.png?imageslim)

### 图文链接消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "link",   "link" :   {           "title" : "企业如何增长？企业微信给出3个答案",           "desc" : "今年中秋节公司有豪礼相送",           "url" : "URL",           "thumb_media_id": "MEDIA_ID"   }}
```

**参数说明：**

| 参数                | 是否必须 | 类型   | 说明                                                         |
| ------------------- | -------- | ------ | ------------------------------------------------------------ |
| touser              | 是       | string | 指定接收消息的客户UserID                                     |
| open_kfid           | 是       | string | 指定发送消息的客服帐号ID                                     |
| msgid               | 否       | string | 指定消息ID                                                   |
| msgtype             | 是       | string | 消息类型，此时固定为：link                                   |
| link                | 是       | obj    | 链接消息                                                     |
| link.title          | 是       | string | 标题，不超过128个字节，超过会自动截断                        |
| link.desc           | 否       | string | 描述，不超过512个字节，超过会自动截断                        |
| link.url            | 是       | string | 点击后跳转的链接。 最长2048字节，请确保包含了协议头(http/https) |
| link.thumb_media_id | 是       | string | 缩略图的media_id, 可以通过[素材管理](https://open.work.weixin.qq.com/api/doc/90000/90135/94677#25551)接口获得。此处thumb_media_id即上传接口返回的media_id |

**图文链接消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/170804-759T33.png?imageslim)

### 小程序消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "miniprogram"   "miniprogram" : {       "appid": "APPID",       "title": "欢迎报名夏令营",       "thumb_media_id": "MEDIA_ID",       "pagepath": "PAGE_PATH"   }}
```

**参数说明：**

| 参数                       | 是否必须 | 类型   | 说明                                                         |
| -------------------------- | -------- | ------ | ------------------------------------------------------------ |
| touser                     | 是       | string | 指定接收消息的客户UserID                                     |
| open_kfid                  | 是       | string | 指定发送消息的客服帐号ID                                     |
| msgid                      | 否       | string | 指定消息ID                                                   |
| msgtype                    | 是       | string | 消息类型，此时固定为：miniprogram                            |
| miniprogram                | 是       | obj    | 小程序消息                                                   |
| miniprogram.appid          | 是       | string | 小程序appid                                                  |
| miniprogram.title          | 否       | string | 小程序消息标题，最多64个字节，超过会自动截断                 |
| miniprogram.thumb_media_id | 是       | string | 小程序消息封面的mediaid，封面图建议尺寸为520*416             |
| miniprogram.pagepath       | 是       | string | 点击消息卡片后进入的小程序页面路径。注意路径要以.html为后缀，否则在微信中打开会提示找不到页面 |

### 菜单消息

**请求示例：**

```
{  "touser": "EXTERNAL_USERID",  "open_kfid": "OPEN_KFID",  "msgid": "MSGID",  "msgtype": "msgmenu",  "msgmenu": {    "head_content": "您对本次服务是否满意呢? ",    "list": [      {        "type": "click",        "click":        {            "id": "101",            "content": "满意"        }      },      {        "type": "click",        "click":        {            "id": "102",            "content": "不满意"        }      },      {        "type": "view",        "view":        {            "url": "https://work.weixin.qq.com",            "content": "点击跳转到自助查询页面"        }      },      {        "type": "miniprogram",        "miniprogram":        {            "appid": "wx123123123123123",            "pagepath": "pages/index?userid=zhangsan&orderid=123123123",            "content": "点击打开小程序查询更多"        }      }    ],    "tail_content": "欢迎再次光临"  }}
```

**参数说明：**

| 参数                              | 必须 | 类型   | 说明                                                         |
| :-------------------------------- | :--- | :----- | :----------------------------------------------------------- |
| touser                            | 是   | string | 指定接收消息的客户UserID                                     |
| open_kfid                         | 是   | string | 指定发送消息的客服帐号ID                                     |
| msgid                             | 否   | string | 指定消息ID                                                   |
| msgtype                           | 是   | string | 消息类型，此时固定为：msgmenu                                |
| msgmenu                           | 是   | obj    | 菜单消息                                                     |
| msgmenu.head_content              | 否   | string | 起始文本 不多于1024字节                                      |
| msgmenu.list                      | 否   | obj[]  | 菜单项配置                                                   |
| msgmenu.list.type                 | 是   | string | 菜单类型。 `click`-回复菜单 `view`-超链接菜单 `miniprogram`-小程序菜单 |
| msgmenu.list.click                | 否   | obj    | type为`click`的菜单项                                        |
| msgmenu.list.click.id             | 否   | string | 菜单ID。 不少于1字节 不多于64字节                            |
| msgmenu.list.click.content        | 是   | string | 菜单显示内容 不少于1字节 不多于128字节                       |
| msgmenu.list.view                 | 否   | obj    | type为`view`的菜单项                                         |
| msgmenu.list.view.url             | 是   | string | 点击后跳转的链接。 不少于1字节 不多于2048字节                |
| msgmenu.list.view.content         | 是   | string | 菜单显示内容。 不少于1字节 不多于1024字节                    |
| msgmenu.list.miniprogram          | 否   | obj    | type为`miniprogram`的菜单项                                  |
| msgmenu.list.miniprogram.appid    | 是   | string | 小程序appid。 不少于1字节 不多于32字节                       |
| msgmenu.list.miniprogram.pagepath | 是   | string | 点击后进入的小程序页面。 不少于1字节 不多于1024字节          |
| msgmenu.list.miniprogram.content  | 是   | string | 菜单显示内容。 不多于1024字节                                |
| msgmenu.tail_content              | 否   | string | 结束文本 不多于1024字节                                      |

其中，“满意”和“不满意”两个菜单当用户点击后，用户会自动回复一条文本消息，同时附带对应的菜单ID。

**菜单消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/170804-8kuJ2v.png?imageslim)

### 地理位置消息

**请求示例：**

```
{   "touser" : "EXTERNAL_USERID",   "open_kfid": "OPEN_KFID",   "msgid": "MSGID",   "msgtype" : "location",   "location": {            "name": "测试小区",            "address": "实例小区，不真实存在，经纬度无意义",            "latitude": 0,            "longitude": 0    }}
```

**参数说明：**

| 参数               | 是否必须 | 类型   | 说明                           |
| ------------------ | -------- | ------ | ------------------------------ |
| touser             | 是       | string | 指定接收消息的客户UserID       |
| open_kfid          | 是       | string | 指定发送消息的客服帐号ID       |
| msgid              | 否       | string | 指定消息ID                     |
| msgtype            | 是       | string | 消息类型，此时固定为：location |
| location           | 是       | obj    | 地理位置消息                   |
| location.name      | 否       | string | 位置名                         |
| location.address   | 否       | string | 地址详情说明                   |
| location.latitude  | 是       | float  | 纬度，浮点数，范围为90 ~ -90   |
| location.longitude | 是       | float  | 经度，浮点数，范围为180 ~ -180 |

权限说明:
企业需要使用“微信客服”secret所获取的accesstoken来调用（accesstoken如何获取？）
第三方应用需具有“微信客服权限->管理帐号、分配会话和收发消息”权限

文档ID: 31152
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94677
https://open.work.weixin.qq.com/api/doc/90001/90143/94700

> Body 请求参数

```json
{
  "msgtype": "string",
  "text": {
    "content": "string",
    "menu_id": "string"
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|any| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "msgid": "MSG_ID"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» msgid|string|false|none||消息ID。如果请求参数指定了msgid，则原样返回，否则系统自动生成并返回。 不多于32字节 字符串取值范围(正则表达式)：[0-9a-zA-Z_-]*|

# 企业内部开发/微信客服/会话分配与消息收发/分配客服会话

## POST 分配客服会话-变更会话状态

POST /cgi-bin/kf/service_state/trans

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94669#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94669#10013/第三步：获取access_token)），同时开启“会话消息管理”开关
- 第三方应用需具有“微信客服权限->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限

文档ID: 31080
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94669
https://open.work.weixin.qq.com/api/doc/90001/90143/94698

> Body 请求参数

```json
{
  "open_kfid": "wkxxxxxxxxxxxxxxxxxx",
  "external_userid": "wmxxxxxxxxxxxxxxxxxx",
  "service_state": 3,
  "servicer_userid": "zhangsan"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID客服帐号ID|
|» external_userid|body|string| 是 |微信客户的external_userid微信客户的external_userid|
|» service_state|body|integer| 是 |变更的目标状态，状态定义和所允许的变更可参考概述中的流程图和表格变更的目标状态，状态定义和所允许的变更可参考概述中的流程图和表格|
|» servicer_userid|body|string| 否 |接待人员的userid，当state=3时要求必填，接待人员须处于“正在接待”中。接待人员的userid，当state=3时要求必填，接待人员须处于“正在接待”中。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "msg_code": "MSG_CODE"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» msg_code|string|true|none||用于发送响应事件消息的code，将会话初次变更为service_state为2和3时，返回回复语code，service_state为4时，返回结束语code。 可用该code调用发送事件响应消息接口给客户发送事件响应消息|

## POST 分配客服会话-获取会话状态

POST /cgi-bin/kf/service_state/get

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94669#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94669#10013/第三步：获取access_token)），同时开启“会话消息管理”开关
- 第三方应用需具有“微信客服权限->管理帐号、分配会话和收发消息”权限
- 代开发自建应用需具有“微信客服->管理帐号、分配会话和收发消息”权限

文档ID: 31080
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/94669
https://open.work.weixin.qq.com/api/doc/90001/90143/94698

> Body 请求参数

```json
{
  "open_kfid": "wkxxxxxxxxxxxxxxxxxx",
  "external_userid": "wmxxxxxxxxxxxxxxxxxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID|
|» external_userid|body|string| 是 |微信客户的external_userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "service_state": 3,
  "servicer_userid": "zhangsan"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» service_state|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» servicer_userid|string|false|none||none|

# 企业内部开发/微信客服/「升级服务」配置

## GET 「升级服务」配置-获取配置的专员与客户群

GET /cgi-bin/kf/customer/get_upgrade_service_config

企业需要在管理后台或移动端中的「微信客服」-「升级服务」中，配置专员和客户群。该接口提供获取配置的专员与客户群列表的能力。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94674#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94674#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->服务工具->配置「升级服务」”权限
- 代开发自建应用需具有“微信客服权限->服务工具->配置「升级服务」”权限

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "member_range": {
    "userid_list": [
      "zhangsan",
      "lisi"
    ],
    "department_id_list": [
      2,
      3
    ]
  },
  "groupchat_range": {
    "chat_id_list": [
      "wraaaaaaaaaaaaaaaa",
      "wrbbbbbbbbbbbbbbb"
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» groupchat_range|object|false|none||客户群配置范围|
|»» chat_id_list|[string]|false|none||客户群列表|
|» errmsg|string|false|none||none|
|» member_range|object|false|none||专员服务配置范围|
|»» department_id_list|[integer]|false|none||专员部门列表|
|»» userid_list|[string]|false|none||专员userid列表|

## POST 「升级服务」配置-为客户取消推荐

POST /cgi-bin/kf/customer/cancel_upgrade_service

当企业通过 API 为客户指定了专员或客户群后，如果客户已经完成服务升级，或是企业需要取消推荐，则可调用该接口清空之前为客户指定的专员或客户群。清空后，企业微信中的特殊状态提示也会同步消失。

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94674#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94674#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->服务工具->配置「升级服务」”权限
- 代开发自建应用需具有“微信客服权限->服务工具->配置「升级服务」”权限

文档ID: 31085
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/94674
https://open.work.weixin.qq.com/api/doc/90001/94702

> Body 请求参数

```json
{
  "open_kfid": "kfxxxxxxxxxxxxxx",
  "external_userid": "wmxxxxxxxxxxxxxxxxxx"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» external_userid|body|string| 否 |客服帐号ID|
|» open_kfid|body|string| 否 |微信客户的external_userid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||错误码描述|

## POST 「升级服务」配置-为客户升级为客户群服务

POST /cgi-bin/kf/customer/upgrade_service

企业可通过其他接口获知客户的 external_userid 以及客户与接待人员的聊天内容，因此可以结合实际业务场景，为客户推荐指定的服务专员或客户群。
通过该 API 为客户指定专员或客户群后，接待人员可在企业微信中，见到特殊的状态提示（Windows 为 icon 样式变化，移动端为出现一条 bar ），便于接待人员知晓企业的指定动作。
![img](https://cdn3.apifox.cn/markdown-img/202110/08/172431-UUxycA.png?imageslim)

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/94674#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/94674#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->服务工具->配置「升级服务」”权限
- 代开发自建应用需具有“微信客服权限->服务工具->配置「升级服务」”权限
- 要求userid/chatid已配置在微信客服中的“升级服务”中专员服务或客户群服务才可使用API进行设置，否则会返回95021错误码。
- 要求userid在“客户联系->权限配置->客户联系和客户群"的使用范围内

文档ID: 31085
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/94674
https://open.work.weixin.qq.com/api/doc/90001/94702

> Body 请求参数

```json
{
  "open_kfid": "kfxxxxxxxxxxxxxx",
  "external_userid": "wmxxxxxxxxxxxxxxxxxx",
  "type": 2,
  "groupchat": {
    "chat_id": "wraaaaaaaaaaaaaaaa",
    "wording": "欢迎加入你的专属服务群"
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID|
|» external_userid|body|string| 是 |微信客户的external_userid|
|» type|body|integer| 是 |表示是升级到专员服务还是客户群服务。1:专员服务。2:客户群服务|
|» groupchat|body|object| 否 |推荐的客户群，type等于2时有效|
|»» userid|body|string| 是 |客户群id|
|»» wording|body|string| 是 |推荐语|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

# 企业内部开发/微信客服/其他基础信息获取

## POST 获取客户基础信息

POST /cgi-bin/kf/customer/batchget

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/95159#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95159#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->获取基础信息”权限
- 代开发自建应用需具有“微信客服权限->获取基础信息”权限

### 如何获取微信客户的unionid

1. 在企业微信管理后台“应用管理-微信客服-通过API管理微信客服”处，点击“绑定”去到微信公众平台进行授权，支持绑定公众号和小程序（需要同时绑定微信开放平台）；绑定的公众号或小程序主体需与企业微信主体一致，暂且支持绑定一个
2. 绑定完成后，即可通过此接口获取微信客服所对应的微信unionid
3. 第三方服务商若需要unionid，则只需要在服务商自身企业微信管理后台的“应用管理-微信客服-通过API管理微信客服”处绑定微信开发者ID；第三方调用接口返回的unionid是该服务商所关联的微信开发者帐号的unionid。也就是说，同一个微信客户，企业自己调用，与第三方服务商调用，所返回的unionid不同；不同的服务商调用，所返回的unionid也不同

![img](https://cdn3.apifox.cn/markdown-img/202110/08/171548-gP89HY.png?imageslim)

> 注：第三方服务商调用上述接口时，获取到的unionid是第三方服务商主体下的unionid。如果第三方服务商已通过其他方式获取到企业主体下的unionid，可调用 [外部联系人unionid转换](https://open.work.weixin.qq.com/api/doc/90000/90135/95159#25017) 接口，将对应的unionid换成服务商名下的external_userid，基于external_userid识别同一客户。

文档ID: 31072
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/90143/95149
https://open.work.weixin.qq.com/api/doc/90000/90135/95159

> Body 请求参数

```json
{
  "external_userid_list": [
    "wmxxxxxxxxxxxxxxxxxxxxxx",
    "zhangsan"
  ],
  "need_enter_session_context": 0
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» external_userid_list|body|[string]| 是 |external_userid列表 可填充个数：1 ~ 100。超过100个需分批调用。|
|» need_enter_session_context|body|integer| 否 |是否需要返回客户48小时内最后一次进入会话的上下文信息。 0-不返回 1-返回。默认不返回|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "customer_list": [
    {
      "external_userid": "wmxxxxxxxxxxxxxxxxxxxxxx",
      "nickname": "张三",
      "avatar": "http://xxxxx",
      "gender": 1,
      "unionid": "oxasdaosaosdasdasdasd",
      "enter_session_context": {
        "scene": "123",
        "scene_param": "abc",
        "wechat_channels": {
          "nickname": "进入会话的视频号名称",
          "scene": 1
        }
      }
    }
  ],
  "invalid_external_userid": [
    "zhangsan"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» customer_list|[object]|true|none||返回结果|
|»» external_userid|string|false|none||微信客户的external_userid|
|»» nickname|string|false|none||微信昵称|
|»» avatar|string|false|none||微信头像。第三方不可获取|
|»» gender|integer|false|none||性别|
|»» unionid|string|false|none||unionid，需要绑定微信开发者帐号才能获取到，查看[绑定方法](https://developer.work.weixin.qq.com/document/path/95159#如何获取微信客户的unionid)。第三方不可获取|
|»» enter_session_context|object|false|none||48小时内最后一次进入会话的上下文信息。 请求的need_enter_session_context参数设置为1才返回|
|»»» scene|string|true|none||进入会话的场景值，[获取客服帐号链接](https://developer.work.weixin.qq.com/document/path/95159#31144)开发者自定义的场景值|
|»»» scene_param|string|true|none||进入会话的自定义参数，[获取客服帐号链接](https://developer.work.weixin.qq.com/document/path/95159#31144)返回的url，开发者按规范拼接的scene_param参数|
|»»» wechat_channels|object|true|none||进入会话的视频号信息，从视频号进入会话才有值|
|»»»» nickname|string|true|none||视频号名称，视频号场景值为1、2、3时返回此项|
|»»»» shop_nickname|string|true|none||视频号小店名称，视频号场景值为4、5时返回此项|
|»»»» scene|integer|true|none||视频号场景值。1：视频号主页，2：视频号直播间商品列表页，3：视频号商品橱窗页，4：视频号小店商品详情页，5：视频号小店订单页|
|» invalid_external_userid|[string]|true|none||none|

# 企业内部开发/微信客服/统计管理

## POST 获取「客户数据统计」企业汇总数据

POST /cgi-bin/kf/get_corp_statistic

通过此接口，可以获取咨询会话数、咨询客户数等企业汇总统计数据

> 查询时间区间[start_time, end_time]为闭区间，最大查询跨度为31天，用户最多可获取最近180天内的数据。当天的数据需要等到第二天才能获取，建议在第二天早上六点以后再调用此接口获取前一天的数据

> 当传入的时间不为0点时，会向下取整，如传入1554296400(Wed Apr 3 21:00:00 CST 2019)会被自动转换为1554220800（Wed Apr 3 00:00:00 CST 2019）;

> 开启API或授权第三方应用管理会话，没有2022年3月11日以前的统计数据

 

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/95489#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95489#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->服务工具->获取客服数据统计”权限
- 代开发自建应用需具有“微信客服权限->服务工具->获取客服数据统计”权限

> Body 请求参数

```json
{
  "open_kfid": "OPEN_KFID",
  "start_time": 1645545600,
  "end_time": 1645632000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID。不传入时返回的数据为企业维度汇总的数据|
|» start_time|body|integer| 是 |起始日期的时间戳，填这一天的0时0分0秒（否则系统自动处理为当天的0分0秒）。取值范围：昨天至前180天|
|» end_time|body|integer| 是 |结束日期的时间戳，填这一天的0时0分0秒（否则系统自动处理为当天的0分0秒）。取值范围：昨天至前180天|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "statistic_list": [
    {
      "stat_time": 1645545600,
      "statistic": {
        "session_cnt": 2,
        "customer_cnt": 1,
        "customer_msg_cnt": 6,
        "upgrade_service_customer_cnt": 0,
        "ai_session_reply_cnt": 1,
        "ai_transfer_rate": 1,
        "ai_knowledge_hit_rate": 0
      }
    },
    {
      "stat_time": 1645632000,
      "statistic": {}
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» statistic_list|[object]|true|none||统计数据列表|
|»» stat_time|integer|true|none||数据统计日期，为当日0点的时间戳|
|»» statistic|object|true|none||一天的统计数据。若当天未产生任何下列统计数据或统计数据还未计算完成则不会返回此项|
|»»» session_cnt|integer|true|none||咨询会话数。客户发过消息并分配给接待人员或智能助手的客服会话数，转接不会产生新的会话|
|»»» customer_cnt|integer|true|none||咨询客户数。在会话中发送过消息的客户数量，若客户多次咨询只计算一个客户|
|»»» customer_msg_cnt|integer|true|none||咨询消息总数。客户在会话中发送的消息的数量|
|»»» upgrade_service_customer_cnt|integer|true|none||升级服务客户数。通过「升级服务」功能成功添加专员或加入客户群的客户数，若同一个客户添加多个专员或客户群，只计算一个客户。在2022年3月10日以后才会有对应统计数据|
|»»» ai_session_reply_cnt|integer|true|none||智能回复会话数。客户发过消息并分配给智能助手的咨询会话数。通过API发消息或者开启智能回复功能会将客户分配给智能助手|
|»»» ai_transfer_rate|integer|true|none||转人工率。一个自然日内，客户给智能助手发消息的会话中，转人工的会话的占比。|
|»»» ai_knowledge_hit_rate|integer|true|none||知识命中率。一个自然日内，客户给智能助手发送的消息中，命中知识库的占比。只有在开启了智能回复原生功能并配置了知识库的情况下，才会产生该项统计数据。当api托管了会话分配，智能回复原生功能失效。若不返回，代表没有向配置知识库的智能接待助手发送消息，该项无法计算|

## POST 获取「客户数据统计」接待人员明细数据

POST /cgi-bin/kf/get_servicer_statistic

通过此接口，可获取接入人工会话数、咨询会话数等与接待人员相关的统计信息

> open_kfid和servicer_userid均为非必填参数:
> \1. 不指定open_kfid，指定servicer_userid，返回单个接待人员的汇总数据；
> \2. 指定open_kfid，不指定servicer_userid，返回客服帐号维度汇总数据；
> \3. 不指定open_kfid，不指定servicer_userid，返回企业维度汇总数据；
> \4. 指定open_kfid，指定servicer_userid，返回该接待人员在此客服账号下的数据。

> 查询时间区间[start_time, end_time]为闭区间，最大查询跨度为31天，用户最多可获取最近180天内的数据。当天的数据需要等到第二天才能获取，建议在第二天早上六点以后再调用此接口获取前一天的数据

> 当传入的时间不为0点时，会向下取整，如传入1554296400(Wed Apr 3 21:00:00 CST 2019)会被自动转换为1554220800（Wed Apr 3 00:00:00 CST 2019）;

> 开启API或授权第三方应用管理会话，没有2022年3月11日以前的统计数据

 

**权限说明**:

- 企业需要使用[“微信客服”secret](https://developer.work.weixin.qq.com/document/path/95490#31106/如何开启API)所获取的accesstoken来调用（[accesstoken如何获取？](https://developer.work.weixin.qq.com/document/path/95490#10013/第三步：获取access_token)）
- 第三方应用需具有“微信客服权限->服务工具->获取客服数据统计”权限
- 代开发自建应用需具有“微信客服权限->服务工具->获取客服数据统计”权限

> Body 请求参数

```json
{
  "open_kfid": "OPEN_KFID",
  "servicer_userid": "zhangsan",
  "start_time": 1645545600,
  "end_time": 1645632000
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» open_kfid|body|string| 是 |客服帐号ID|
|» servicer_userid|body|string| 是 |接待人员的userid。第三方应用为密文userid，即open_userid|
|» start_time|body|integer| 是 |起始日期的时间戳，填当天的0时0分0秒（否则系统自动处理为当天的0分0秒）。取值范围：昨天至前180天|
|» end_time|body|integer| 是 |结束日期的时间戳，填当天的0时0分0秒（否则系统自动处理为当天的0分0秒）。取值范围：昨天至前180天|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "statistic_list": [
    {
      "stat_time": 1645545600,
      "statistic": {
        "session_cnt": 1,
        "customer_cnt": 1,
        "customer_msg_cnt": 1,
        "reply_rate": 1,
        "first_reply_average_sec": 17,
        "satisfaction_investgate_cnt": 1,
        "satisfaction_participation_rate": 1,
        "satisfied_rate": 1,
        "middling_rate": 0,
        "dissatisfied_rate": 0,
        "upgrade_service_customer_cnt": 0,
        "upgrade_service_member_invite_cnt": 0,
        "upgrade_service_member_customer_cnt": 0,
        "upgrade_service_groupchat_invite_cnt": 0,
        "upgrade_service_groupchat_customer_cnt": 0
      }
    },
    {
      "stat_date": 1645632000,
      "statistic": {}
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» statistic_list|[object]|true|none||统计数据列表|
|»» stat_time|integer|false|none||数据统计日期，为当日0点的时间戳|
|»» statistic|object|true|none||一天的统计数据。若当天未产生任何下列统计数据或统计数据还未计算完成则不会返回此项|
|»»» session_cnt|integer|true|none||接入人工会话数。客户发过消息并分配给接待人员的咨询会话数|
|»»» customer_cnt|integer|true|none||咨询客户数。在会话中发送过消息且接入了人工会话的客户数量，若客户多次咨询只计算一个客户|
|»»» customer_msg_cnt|integer|true|none||none|
|»»» reply_rate|integer|true|none||人工回复率。一个自然日内，客户给接待人员发消息的会话中，接待人员回复了的会话的占比。若数据项不返回，代表没有给接待人员发送消息的客户，此项无法计算。|
|»»» first_reply_average_sec|integer|true|none||平均首次响应时长，单位：秒。一个自然日内，客户给接待人员发送的第一条消息至接待人员回复之间的时长，为首次响应时长。所有的首次回复总时长/已回复的咨询会话数，即为平均首次响应时长 。若数据项不返回，代表没有给接待人员发送消息的客户，此项无法计算|
|»»» satisfaction_investgate_cnt|integer|true|none||满意度评价发送数。当api托管了会话分配，满意度原生功能失效，满意度评价发送数为0|
|»»» satisfaction_participation_rate|integer|true|none||满意度参评率 。当api托管了会话分配，满意度原生功能失效。若数据项不返回，代表没有发送满意度评价，此项无法计算|
|»»» satisfied_rate|integer|true|none||“满意”评价占比 。在客户参评的满意度评价中，评价是“满意”的占比。当api托管了会话分配，满意度原生功能失效。若数据项不返回，代表没有客户参评的满意度评价，此项无法计算|
|»»» middling_rate|integer|true|none||“一般”评价占比 。在客户参评的满意度评价中，评价是“一般”的占比。当api托管了会话分配，满意度原生功能失效。若数据项不返回，代表没有客户参评的满意度评价，此项无法计算|
|»»» dissatisfied_rate|integer|true|none||“不满意”评价占比。在客户参评的满意度评价中，评价是“不满意”的占比。当api托管了会话分配，满意度原生功能失效。若数据项不返回，代表没有客户参评的满意度评价，此项无法计算|
|»»» upgrade_service_customer_cnt|integer|true|none||升级服务客户数。通过「升级服务」功能成功添加专员或加入客户群的客户数，若同一个客户添加多个专员或客户群，只计算一个客户。在2022年3月10日以后才会有对应统计数据|
|»»» upgrade_service_member_invite_cnt|integer|true|none||专员服务邀请数。接待人员通过「升级服务-专员服务」向客户发送服务专员名片的次数。在2022年3月10日以后才会有对应统计数据|
|»»» upgrade_service_member_customer_cnt|integer|true|none||添加专员的客户数 。客户成功添加专员为好友的数量，若同一个客户添加多个专员，则计算多个客户数。在2022年3月10日以后才会有对应统计数据|
|»»» upgrade_service_groupchat_invite_cnt|integer|true|none||客户群服务邀请数。接待人员通过「升级服务-客户群服务」向客户发送客户群二维码的次数。在2022年3月10日以后才会有对应统计数据|
|»»» upgrade_service_groupchat_customer_cnt|integer|true|none||加入客户群的客户数。客户成功加入客户群的数量，若同一个客户加多个客户群，则计算多个客户数。在2022年3月10日以后才会有对应统计数据|
|»» stat_date|integer|false|none||none|

# 企业内部开发/微信客服/机器人管理/知识库分组管理

## POST 获取分组列表

POST /cgi-bin/kf/knowledge/list_group

可通过此接口分页获取所有的知识库分组。

> Body 请求参数

```json
{
  "cursor": "string",
  "limit": 0,
  "group_id": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» cursor|body|string| 是 |上一次调用时返回的next_cursor，第一次拉取可以不填|
|» limit|body|integer| 是 |每次拉取的数据量，默认值500，最大值为1000|
|» group_id|body|string| 是 |分组ID。可指定拉取特定的分组|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "NEXT_CURSOR",
  "has_more": 1,
  "group_list": [
    {
      "group_id": "GROUP_ID",
      "name": "NAME",
      "is_default": 1
    },
    {
      "group_id": "GROUP_ID",
      "name": "NAME",
      "is_default": 0
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» next_cursor|string|true|none||分页游标，再下次请求时填写以获取之后分页的记录|
|» has_more|integer|true|none||是否还有更多数据。0-没有 1-有|
|» group_list|[object]|true|none||分组列表|
|»» group_id|string|true|none||分组ID|
|»» name|string|true|none||分组名|
|»» is_default|integer|true|none||是否为默认分组。0-否 1-是。默认分组为系统自动创建，不可修改/删除|

## GET 添加分组

GET /cgi-bin/kf/knowledge/add_group

可通过此接口创建新的知识库分组。

> Body 请求参数

```json
{
  "name": "分组名"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 是 |分组名。不超过12个字|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "group_id": "GROUP_ID"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» group_id|string|true|none||分组ID|

## GET 删除分组

GET /cgi-bin/kf/knowledge/del_group

可通过此接口删除已有的知识库分组，但不能删除系统创建的默认分组。

> Body 请求参数

```json
{
  "group_id": "GROUP_ID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» group_id|body|string| 是 |分组ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|

## POST 修改分组

POST /cgi-bin/kf/knowledge/mod_group

可通过此接口修改已有的知识库分组，但不能修改系统创建的默认分组。

> Body 请求参数

```json
{
  "group_id": "string",
  "name": "string"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» group_id|body|string| 是 |分组ID|
|» name|body|string| 是 |分组名。不超过12个字|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||返回码|
|» group_id|string|true|none||分组ID|

# 企业内部开发/微信客服/机器人管理/知识库问答管理

## POST 删除问答

POST /cgi-bin/kf/knowledge/del_intent

> Body 请求参数

```json
{
  "intent_id": "INTENT_ID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» intent_id|body|string| 是 |问答ID|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|

## POST 修改问答

POST /cgi-bin/kf/knowledge/mod_intent

可通过此接口修改已有的知识库问答。
`question`/`similar_questions`/`answers`这三部分可以按需更新，但更新的每一部分是覆盖写，需要传完整的字段。

> Body 请求参数

```json
{
  "intent_id": "INTENT_ID",
  "question": {
    "text": {
      "content": "主问题"
    }
  },
  "similar_questions": {
    "items": [
      {
        "text": {
          "content": "相似问题1"
        }
      },
      {
        "text": {
          "content": "相似问题2"
        }
      }
    ]
  },
  "answers": [
    {
      "text": {
        "content": "问题的回复"
      },
      "attachments": [
        {
          "msgtype": "image",
          "image": {
            "media_id": "MEDIA_ID"
          }
        }
      ]
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |none|
|body|body|object| 否 |none|
|» intent_id|body|string| 是 |问答ID|
|» question|body|object| 否 |主问题|
|»» text|body|object| 否 |主问题文本|
|»»» content|body|string| 是 |主问题文本内容|
|» similar_questions|body|object| 否 |相似问题|
|»» items|body|[object]| 否 |相似问题列表。最多支持100个|
|»»» text|body|object| 是 |相似问题文本|
|»»»» content|body|string| 是 |相似问题文本内容|
|» answers|body|[object]| 否 |回答列表。目前仅支持1个|
|»» text|body|object| 是 |回答文本|
|»»» content|body|string| 是 |回答文本内容|
|»» attachments|body|[object]| 否 |回答附件列表。最多支持4个|
|»»» msgtype|body|string| 否 |none|
|»»» image|body|object| 否 |none|
|»»»» media_id|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» intent_id|string|true|none||问答ID|

## POST 添加问答

POST /cgi-bin/kf/knowledge/add_intent

可通过此接口创建新的知识库问答。

> Body 请求参数

```json
{
  "group_id": "GROUP_ID",
  "question": {
    "text": {
      "content": "主问题"
    }
  },
  "similar_questions": {
    "items": [
      {
        "text": {
          "content": "相似问题1"
        }
      },
      {
        "text": {
          "content": "相似问题2"
        }
      }
    ]
  },
  "answers": [
    {
      "text": {
        "content": "问题的回复"
      },
      "attachments": [
        {
          "msgtype": "image",
          "image": {
            "media_id": "MEDIA_ID"
          }
        }
      ]
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» group_id|body|string| 是 |分组ID|
|» question|body|object| 是 |主问题|
|»» text|body|object| 是 |主问题文本|
|»»» content|body|string| 是 |主问题文本内容。不超过200个字|
|» similar_questions|body|object| 否 |相似问题|
|»» items|body|[object]| 否 |相似问题列表。最多支持100个|
|»»» text|body|object| 是 |相似问题文本|
|»»»» content|body|string| 是 |相似问题文本内容。不超过200个字|
|» answers|body|[object]| 是 |回答列表。目前仅支持1个|
|»» text|body|object| 否 |回答文本|
|»»» content|body|string| 是 |回答文本内容。不超过500个字|
|»» attachments|body|[object]| 否 |回答附件列表。最多支持4个|
|»»» msgtype|body|string| 否 |none|
|»»» image|body|object| 否 |none|
|»»»» media_id|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "intent_id": "INTENT_ID"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» intent_id|string|true|none||问答ID|

## POST 获取问答列表

POST /cgi-bin/kf/knowledge/list_intent

可通过此接口分页获取的知识库问答详情列表。

> Body 请求参数

```json
{
  "cursor": "CURSOR",
  "limit": 100,
  "group_id": "GROUP_ID",
  "intent_id": "INTENT_ID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |none|
|body|body|object| 否 |none|
|» cursor|body|string| 否 |上一次调用时返回的next_cursor，第一次拉取可以不填|
|» limit|body|integer| 否 |每次拉取的数据量，默认值500，最大值为1000|
|» group_id|body|string| 否 |分组ID。可指定拉取特定分组下的问答|
|» intent_id|body|string| 否 |问答ID。可指定拉取特定的问答|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "next_cursor": "NEXT_CURSOR",
  "has_more": 1,
  "intent_list": [
    {
      "group_id": "GROUP_ID",
      "intent_id": "INTENT_ID",
      "question": {
        "text": {
          "content": "主问题"
        },
        "similar_questions": {
          "items": [
            {
              "text": {
                "content": "相似问题1"
              }
            },
            {
              "text": {
                "content": "相似问题2"
              }
            }
          ]
        },
        "answers": [
          {
            "text": {
              "content": "问题的回复"
            },
            "attachments": [
              {
                "msgtype": "image",
                "image": {
                  "name": "图片（仅返回名字）.jpg"
                }
              }
            ]
          }
        ]
      }
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» next_cursor|string|true|none||分页游标，再下次请求时填写以获取之后分页的记录|
|» has_more|integer|true|none||是否还有更多数据。0-没有 1-有|
|» intent_list|[object]|true|none||问答摘要列表|
|»» group_id|string|false|none||分组ID|
|»» intent_id|string|false|none||问答ID|
|»» question|object|false|none||主问题|
|»»» text|object|true|none||主问题文本|
|»»»» content|string|true|none||主问题文本内容|
|»»» similar_questions|object|true|none||相似问题|
|»»»» items|[object]|true|none||相似问题列表。最多支持100个|
|»»»»» text|object|true|none||相似问题文本|
|»»»»»» content|string|true|none||相似问题文本内容|
|»»» answers|[object]|true|none||回答列表。目前仅支持1个|
|»»»» text|object|false|none||回答文本|
|»»»»» content|string|true|none||回答文本内容|
|»»»» attachments|[object]|false|none||回答附件列表。最多支持4个|
|»»»»» msgtype|string|false|none||none|
|»»»»» image|object|false|none||none|
|»»»»»» name|string|true|none||none|

# 企业内部开发/身份验证/网页授权登录

## GET 获取访问用户身份

GET /cgi-bin/user/getuserinfo

该接口用于根据code获取成员信息

权限说明：
跳转的域名须完全匹配access_token对应应用的可信域名，否则会返回50001错误。

文档ID: 15047
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/91023

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|accesstoken|query|string| 是 |调用接口凭证|
|code|query|string| 是 |通过成员授权获取到的code，最大为512字节。每次成员授权带上的code将不一样，code只能使用一次，5分钟未被使用自动过期。|

> 返回示例

> a) 当用户为企业成员时（无论是否在应用可见范围之内）

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "UserId": "USERID",
  "DeviceId": "DEVICEID"
}
```

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "OpenId": "OPENID",
  "DeviceId": "DEVICEID",
  "external_userid": "EXTERNAL_USERID"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|a) 当用户为企业成员时（无论是否在应用可见范围之内）|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» UserId|string|false|none||none|
|» DeviceId|string|false|none||手机设备号(由企业微信在安装时随机生成，删除重装会改变，升级不受影响)|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 获取访问用户敏感信息

POST /cgi-bin/auth/getuserdetail

自建应用与代开发应用可通过该接口获取成员授权的敏感字段

**权限说明：**
成员必须在应用的可见范围内。

**参数说明：**

注：对于自建应用与代开发应用，敏感字段需要管理员在应用详情里选择，且成员oauth2授权时确认后才返回。敏感字段包括：性别、头像、员工个人二维码、手机、邮箱、企业邮箱、地址。

> Body 请求参数

```json
{
  "user_ticket": "USER_TICKET"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» user_ticket|body|string| 是 |成员票据|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "userid": "lisi",
  "gender": "1",
  "avatar": "http://shp.qpic.cn/bizmp/xxxxxxxxxxx/0",
  "qr_code": "https://open.work.weixin.qq.com/wwopen/userQRCode?vcode=vcfc13b01dfs78e981c",
  "mobile": "13800000000",
  "email": "zhangsan@gzdev.com",
  "biz_mail": "zhangsan@qyycs2.wecom.work",
  "address": "广州市海珠区新港中路"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» userid|string|true|none||成员UserID|
|» gender|string|true|none||性别。0表示未定义，1表示男性，2表示女性。仅在用户同意snsapi_privateinfo授权时返回真实值，否则返回0.|
|» avatar|string|true|none||头像url。仅在用户同意snsapi_privateinfo授权时返回|
|» qr_code|string|true|none||员工个人二维码（扫描可添加为外部联系人），仅在用户同意snsapi_privateinfo授权时返回|
|» mobile|string|true|none||手机，仅在用户同意snsapi_privateinfo授权时返回，第三方应用不可获取|
|» email|string|true|none||邮箱，仅在用户同意snsapi_privateinfo授权时返回，第三方应用不可获取|
|» biz_mail|string|true|none||企业邮箱，仅在用户同意snsapi_privateinfo授权时返回，第三方应用不可获取|
|» address|string|true|none||仅在用户同意snsapi_privateinfo授权时返回，第三方应用不可获取|

# 企业内部开发/应用管理

## GET 获取应用-获取指定的应用详情

GET /cgi-bin/agent/get

对于互联企业的应用，如果需要获取应用可见范围内其他互联企业的部门与成员，请调用[互联企业-获取应用可见范围接口](https://open.work.weixin.qq.com/api/doc/90000/90135/90227#24275)

权限说明：
企业仅可获取当前凭证对应的应用；第三方仅可获取被授权的应用。
文档ID: 10087
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90227
https://open.work.weixin.qq.com/api/doc/90001/90363
https://open.work.weixin.qq.com/api/doc/90002/90845

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|agentid|query|string| 是 |应用id|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "agentid": 1000005,
  "name": "HR助手",
  "square_logo_url": "https://p.qlogo.cn/bizmail/FicwmI50icF8GH9ib7rUAYR5kicLTgP265naVFQKnleqSlRhiaBx7QA9u7Q/0",
  "description": "HR服务与员工自助平台",
  "allow_userinfos": {
    "user": [
      {
        "userid": "zhangshan"
      },
      {
        "userid": "lisi"
      }
    ]
  },
  "allow_partys": {
    "partyid": [
      1
    ]
  },
  "allow_tags": {
    "tagid": [
      1,
      2,
      3
    ]
  },
  "close": 0,
  "redirect_domain": "open.work.weixin.qq.com",
  "report_location_flag": 0,
  "isreportenter": 0,
  "home_url": "https://open.work.weixin.qq.com",
  "customized_publish_status": 1
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||出错返回码，为0表示成功，非0表示调用失败|
|» square_logo_url|string|false|none||企业应用方形头像|
|» agentid|integer(int32)|false|none||企业应用id|
|» home_url|string|false|none||应用主页url|
|» errmsg|string|false|none||返回码提示语|
|» description|string|false|none||企业应用详情|
|» allow_tags|object|false|none||企业应用可见范围（标签）|
|»» tagid|[integer]|false|none||none|
|» isreportenter|integer(int32)|false|none||是否上报用户进入应用事件。0：不接收；1：接收|
|» redirect_domain|string|false|none||企业应用可信域名|
|» name|string|false|none||企业应用名称|
|» allow_partys|object|false|none||企业应用可见范围（部门）|
|»» partyid|[integer]|false|none||none|
|» allow_userinfos|object|false|none||企业应用可见范围（人员），其中包括userid|
|»» user|[object]|false|none||none|
|»»» userid|string|false|none||none|
|» close|integer(int32)|false|none||企业应用是否被停用|
|» report_location_flag|integer(int32)|false|none||企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；|

## POST 设置应用

POST /cgi-bin/agent/set

**权限说明：**
仅企业可调用，可设置当前凭证对应的应用；第三方以及代开发自建应用不可调用。

文档ID: 10088
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90228

> Body 请求参数

```json
{
  "agentid": 1000005,
  "report_location_flag": 0,
  "logo_mediaid": "j5Y8X5yocspvBHcgXMSS6z1Cn9RQKREEJr4ecgLHi4YHOYP-plvom-yD9zNI0vEl",
  "name": "财经助手",
  "description": "内部财经服务平台",
  "redirect_domain": "open.work.weixin.qq.com",
  "isreportenter": 0,
  "home_url": "https://open.work.weixin.qq.com"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» isreportenter|body|integer(int32)| 否 |是否上报用户进入应用事件。0：不接收；1：接收。|
|» agentid|body|integer(int32)| 是 |企业应用的id|
|» redirect_domain|body|string| 否 |企业应用可信域名。注意：域名需通过所有权校验，否则jssdk功能将受限，此时返回错误码85005|
|» home_url|body|string| 否 |应用主页url。url必须以http或者https开头（为了提高安全性，建议使用https）。|
|» name|body|string| 否 |企业应用名称，长度不超过32个utf8字符|
|» description|body|string| 否 |企业应用详情，长度为4至120个utf8字符|
|» report_location_flag|body|integer(int32)| 否 |企业应用是否打开地理位置上报 0：不上报；1：进入会话上报；|
|» logo_mediaid|body|string| 否 |企业应用头像的mediaid，通过素材管理接口上传图片获得mediaid，上传后会自动裁剪成方形和圆形两个头像|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

## GET 获取应用-获取access_token对应的应用列表

GET /cgi-bin/agent/list

权限说明：
企业仅可获取当前凭证对应的应用；第三方仅可获取被授权的应用。

文档ID: 10087
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90227
https://open.work.weixin.qq.com/api/doc/90001/90363
https://open.work.weixin.qq.com/api/doc/90002/90845

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "agentlist": [
    {
      "agentid": 1000005,
      "name": "HR助手",
      "square_logo_url": "https://p.qlogo.cn/bizmail/FicwmI50icF8GH9ib7rUAYR5kicLTgP265naVFQKnleqSlRhiaBx7QA9u7Q/0"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||出错返回码，为0表示成功，非0表示调用失败|
|» agentlist|[object]|false|none||当前凭证可访问的应用列表|
|»» square_logo_url|string|false|none||企业应用方形头像url|
|»» agentid|integer(int32)|false|none||企业应用id|
|»» name|string|false|none||企业应用名称|
|» errmsg|string|false|none||返回码提示语|

# 企业内部开发/应用管理/自定义菜单

## GET 获取菜单

GET /cgi-bin/menu/get

权限说明：仅企业可调用；第三方不可调用。
返回结果：

返回结果与请参考菜单创建接口

文档ID: 10787
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90232

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|agentid|query|string| 是 |应用id|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 创建菜单

POST /cgi-bin/menu/create

**示例：构造click和view类型的请求包如下**

```
{
   "button":[
       {    
           "type":"click",
           "name":"今日歌曲",
           "key":"V1001_TODAY_MUSIC"
       },
       {
           "name":"菜单",
           "sub_button":[
               {
                   "type":"view",
                   "name":"搜索",
                   "url":"http://www.soso.com/"
               },
               {
                   "type":"click",
                   "name":"赞一下我们",
                   "key":"V1001_GOOD"
               }
           ]
      }
   ]
}

```

**示例：其他新增按钮类型的请求**

```
{
    "button": [
        {
            "name": "扫码", 
            "sub_button": [
                {
                    "type": "scancode_waitmsg", 
                    "name": "扫码带提示", 
                    "key": "rselfmenu_0_0", 
                    "sub_button": [ ]
                }, 
                {
                    "type": "scancode_push", 
                    "name": "扫码推事件", 
                    "key": "rselfmenu_0_1", 
                    "sub_button": [ ]
                },
                {
                    "type":"view_miniprogram",
                    "name":"小程序",
                    "pagepath":"pages/lunar/index",
                    "appid":"wx4389ji4kAAA"
                }
            ]
        }, 
        {
            "name": "发图", 
            "sub_button": [
                {
                    "type": "pic_sysphoto", 
                    "name": "系统拍照发图", 
                    "key": "rselfmenu_1_0", 
                   "sub_button": [ ]
                 }, 
                {
                    "type": "pic_photo_or_album", 
                    "name": "拍照或者相册发图", 
                    "key": "rselfmenu_1_1", 
                    "sub_button": [ ]
                }, 
                {
                    "type": "pic_weixin", 
                    "name": "微信相册发图", 
                    "key": "rselfmenu_1_2", 
                    "sub_button": [ ]
                }
            ]
        }, 
        {
            "name": "发送位置", 
            "type": "location_select", 
            "key": "rselfmenu_2_0"
        }
    ]
}

```

**参数说明：**

| 参数         | 必须                     | 说明                                                         |
| ------------ | ------------------------ | ------------------------------------------------------------ |
| access_token | 是                       | 调用接口凭证                                                 |
| agentid      | 是                       | 企业应用的id，整型。可在应用的设置页面查看                   |
| button       | 是                       | 一级菜单数组，个数应为1~3个                                  |
| sub_button   | 否                       | 二级菜单数组，个数应为1~5个                                  |
| type         | 是                       | 菜单的响应动作类型                                           |
| name         | 是                       | 菜单的名字。不能为空，主菜单不能超过16字节，子菜单不能超过40字节。 |
| key          | click等点击类型必须      | 菜单KEY值，用于消息接口推送，不超过128字节                   |
| url          | view类型必须             | 网页链接，成员点击菜单可打开链接，不超过1024字节。为了提高安全性，建议使用https的url |
| pagepath     | view_miniprogram类型必须 | 小程序的页面路径                                             |
| appid        | view_miniprogram类型必须 | 小程序的appid（仅与企业绑定的小程序可配置）                  |

权限说明 :
仅企业可调用；第三方不可调用。

> Body 请求参数

```json
{
  "button": [
    {
      "type": "click",
      "name": "今日歌曲",
      "key": "V1001_TODAY_MUSIC"
    },
    {
      "name": "菜单",
      "sub_button": [
        {
          "type": "view",
          "name": "搜索",
          "url": "http://www.soso.com/"
        },
        {
          "type": "click",
          "name": "赞一下我们",
          "key": "V1001_GOOD"
        }
      ]
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|agentid|query|string| 否 |企业应用的id，整型。可在应用的设置页面查看|
|body|body|object| 否 |none|
|» button|body|[object]| 是 |一级菜单数组，个数应为1~3个|
|»» type|body|string| 否 |菜单的响应动作类型|
|»» name|body|string| 是 |菜单的名字。不能为空，主菜单不能超过16字节，子菜单不能超过40字节。|
|»» key|body|string| 否 |click等点击类型必须,菜单KEY值，用于消息接口推送，不超过128字节|
|»» sub_button|body|[object]| 否 |二级菜单数组，个数应为1~5个|
|»»» type|body|string| 是 |none|
|»»» name|body|string| 是 |none|
|»»» url|body|string| 否 |网页链接，成员点击菜单可打开链接，不超过1024字节。为了提高安全性，建议使用https的url|
|»»» key|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

## GET 删除菜单

GET /cgi-bin/menu/delete

权限说明：仅企业可调用；第三方不可调用。

文档ID: 10788
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90233

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|agentid|query|string| 是 |应用id|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

# 企业内部开发/应用管理/设置工作台自定义展示

## POST 设置工作台自定义展示-设置应用在用户工作台展示的数据

POST /cgi-bin/agent/set_workbench_data

权限说明：
可设置当前凭证对应的应用；设置的userid必须在应用可见范围
若为第三方应用，目前仅支持行业类型为 学前教育、初中等教育、教育行政单位 的企业调用该接口
每个用户每个应用接口限制10次/分钟

文档ID: 20102
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/92535
https://open.work.weixin.qq.com/api/doc/90001/94620

> Body 请求参数

```json
{
  "agentid": 1000005,
  "userid": "test",
  "type": "keydata",
  "keydata": {
    "items": [
      {
        "key": "待审批",
        "data": "2",
        "jump_url": "http://www.qq.com",
        "pagepath": "pages/index"
      },
      {
        "key": "带批阅作业",
        "data": "4",
        "jump_url": "http://www.qq.com",
        "pagepath": "pages/index"
      },
      {
        "key": "成绩录入",
        "data": "45",
        "jump_url": "http://www.qq.com",
        "pagepath": "pages/index"
      },
      {
        "key": "综合评价",
        "data": "98",
        "jump_url": "http://www.qq.com",
        "pagepath": "pages/index"
      }
    ]
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» agentid|body|integer(int32)| 是 |应用id|
|» keydata|body|object| 否 |若type指定为 “keydata”，则需要设置关键数据型模版数据,数据结构参考“关键数据型”|
|»» items|body|[object]| 否 |none|
|»»» pagepath|body|string| 否 |none|
|»»» jump_url|body|string| 否 |none|
|»»» data|body|string| 否 |none|
|»»» key|body|string| 否 |none|
|» type|body|string| 是 |目前支持 “keydata”、 “image”、 “list” 、”webview”|
|» userid|body|string| 是 |需要设置的用户的userid|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## POST 设置工作台自定义展示-设置应用在工作台展示的模版

POST /cgi-bin/agent/set_workbench_template

该接口指定应用自定义模版类型。同时也支持设置企业默认模版数据。若type指定为 “normal” 则为取消自定义模式，改为普通展示模式

权限说明：
可设置当前凭证对应的应用；
若为第三方应用，目前仅支持行业类型为 学前教育、初中等教育、教育行政单位 的企业调用该接口

文档ID: 20102
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/92535
https://open.work.weixin.qq.com/api/doc/90001/94620

> Body 请求参数

```json
{
  "agentid": 1000005,
  "type": "image",
  "image": {
    "url": "xxxx",
    "jump_url": "http://www.qq.com",
    "pagepath": "pages/index"
  },
  "replace_user_data": true
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» image|body|object| 否 |若type指定为 “image”，且需要设置企业级别默认数据，则需要设置图片型模版数据,数据结构参考“图片型”|
|»» pagepath|body|string| 否 |none|
|»» jump_url|body|string| 否 |none|
|»» url|body|string| 否 |none|
|» agentid|body|integer(int32)| 是 |应用id|
|» replace_user_data|body|boolean| 否 |是否覆盖用户工作台的数据。设置为true的时候，会覆盖企业所有用户当前设置的数据。若设置为false,则不会覆盖用户当前设置的所有数据。默认为false|
|» type|body|string| 是 |模版类型，目前支持的自定义类型包括 “keydata”、 “image”、 “list”、 “webview” 。若设置的type为 “normal”,则相当于从自定义模式切换为普通宫格或者列表展示模式|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|

## POST 设置工作台自定义展示-获取应用在工作台展示的模版

POST /cgi-bin/agent/get_workbench_template

权限说明：
可设置当前凭证对应的应用；
若为第三方应用，目前仅支持行业类型为 学前教育、初中等教育、教育行政单位 的企业调用该接口
文档ID: 20102
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/92535
https://open.work.weixin.qq.com/api/doc/90001/94620

> Body 请求参数

```json
{
  "agentid": 1000005
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» agentid|body|integer(int32)| 是 |应用id|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "type": "image",
  "image": {
    "url": "xxxx",
    "jump_url": "http://www.qq.com",
    "pagepath": "pages/index"
  },
  "replace_user_data": true
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» image|object|false|none||none|
|»» pagepath|string|false|none||none|
|»» jump_url|string|false|none||none|
|»» url|string|false|none||none|
|» errmsg|string|false|none||none|
|» replace_user_data|boolean|false|none||none|
|» type|string|false|none||none|

# 企业内部开发/消息推送

## POST 撤回应用消息

POST /cgi-bin/message/recall

本接口可以撤回24小时内通过发送应用消息接口推送的消息，仅可撤回企业微信端的数据，微信插件端的数据不支持撤回。

文档ID: 31947
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/94867
https://open.work.weixin.qq.com/api/doc/90001/94947

> Body 请求参数

```json
{
  "msgid": "vcT8gGc-7dFb4bxT35ONjBDz901sLlXPZw1DAMC_Gc26qRpK-AK5sTJkkb0128t"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» msgid|body|string| 是 |消息ID。从应用发送消息接口处获得。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|

## POST 更新模版卡片消息

POST /cgi-bin/message/update_template_card

应用可以发送模板卡片消息，发送之后可再通过接口更新可回调的用户任务卡片消息的替换文案信息（仅原卡片为 按钮交互型、投票选择型、多项选择型的卡片以及填写了action_menu字段的文本通知型、图文展示型可以调用本接口更新）。

请注意，当应用调用发送模版卡片消息后，接口会返回一个response_code，通过response_code用户可以调用本接口一次。后续如果有用户点击任务卡片，回调接口也会带上response_code，开发者通过该code也可以调用本接口一次，注意response_code的有效期是24小时，超过24小时后将无法使用。

如果部分指定的用户无权限或不存在，更新仍然执行，但会返回无效的部分（即invaliduser），常见的原因是用户不在应用的可见范围内或者不在消息的接收范围内。
------
## 更新按钮为不可点击状态

可回调的卡片可以将按钮更新为不可点击状态，并且自定义文案

```
{
    "userids" : ["userid1","userid2"],
    "partyids" : [2,3],
    "tagids" : [44,55],
    "atall" : 0,
    "agentid" : 1,
    "response_code": "response_code",
    "button":{
        "replace_name": "replace_name"
    }
}

```

**参数说明：**

| 参数          | 是否必须 | 说明                                                         |
| ------------- | -------- | ------------------------------------------------------------ |
| userids       | 否       | 企业的成员ID列表（最多支持1000个）                           |
| partyids      | 否       | 企业的部门ID列表（最多支持100个）                            |
| tagids        | 否       | 企业的标签ID列表（最多支持100个）                            |
| atall         | 否       | 更新整个任务接收人员                                         |
| agentid       | 是       | 应用的agentid                                                |
| response_code | 是       | 更新卡片所需要消费的code，可通过发消息接口和回调接口返回值获取，一个code只能调用一次该接口，且只能在24小时内调用 |
| replace_name  | 是       | 需要更新的按钮的文案                                         |

## 更新为新的卡片

可回调的卡片可以更新成任何一种模板卡片

### 文本通知型

![img](https://cdn3.apifox.cn/markdown-img/202110/08/194516-HtRQLh.png?imageslim)

```
{
    "userids" : ["userid1","userid2"],
    "partyids" : [2,3],
    "agentid" : 1,
    "response_code": "response_code",
    "template_card" : {
        "card_type" : "text_notice",
        "source" : {
            "icon_url": "图片的url",
            "desc": "企业微信",
            "desc_color": 1
        },
        "action_menu": {
            "desc": "卡片副交互辅助文本说明",
            "action_list": [
                {"text": "接受推送", "key": "A"},
                {"text": "不再推送", "key": "B"}
            ]
        },
        "main_title" : {
            "title" : "欢迎使用企业微信",
            "desc" : "您的好友正在邀请您加入企业微信"
        },
        "quote_area": {
            "type": 1,
            "url": "https://work.weixin.qq.com",
            "title": "企业微信的引用样式",
            "quote_text": "企业微信真好用呀真好用"
        },
        "emphasis_content": {
            "title": "100",
            "desc": "核心数据"
        },
        "sub_title_text" : "下载企业微信还能抢红包！",
        "horizontal_content_list" : [
            {
                "keyname": "邀请人",
                "value": "张三"
            },
            {
                "type": 1,
                "keyname": "企业微信官网",
                "value": "点击访问",
                "url": "https://work.weixin.qq.com"
            },
            {
                "type": 2,
                "keyname": "企业微信下载",
                "value": "企业微信.apk",
                "media_id": "文件的media_id"
            },
            {
                "type": 3,
                "keyname": "员工信息",
                "value": "点击查看",
                "userid": "zhangsan"
            }
        ],
        "jump_list" : [
            {
                "type": 1,
                "title": "企业微信官网",
                "url": "https://work.weixin.qq.com"
            },
            {
                "type": 2,
                "title": "跳转小程序",
                "appid": "小程序的appid",
                "pagepath": "/index.html"
            }
        ],
        "card_action": {
            "type": 2,
            "url": "https://work.weixin.qq.com",
            "appid": "小程序的appid",
            "pagepath": "/index.html"
        }
    }
}

```

**参数说明：**

| 参数                             | 是否必须 | 说明                                                         |
| -------------------------------- | -------- | ------------------------------------------------------------ |
| userids                          | 否       | 企业的成员ID列表（最多支持1000个）                           |
| partyids                         | 否       | 企业的部门ID列表（最多支持100个）                            |
| tagids                           | 否       | 企业的标签ID列表（最多支持100个）                            |
| atall                            | 否       | 更新整个任务接收人员                                         |
| agentid                          | 是       | 应用的agentid                                                |
| response_code                    | 是       | 更新卡片所需要消费的code，可通过发消息接口和回调接口返回值获取，一个code只能调用一次该接口，且只能在24小时内调用 |
| card_type                        | 是       | 模板卡片类型，文本通知型卡片填写 “text_notice”               |
| source                           | 否       | 卡片来源样式信息，不需要来源样式可不填写                     |
| source.icon_url                  | 否       | 来源图片的url                                                |
| source.desc                      | 否       | 来源图片的描述，建议不超过20个字                             |
| source.desc_color                | 否       | 来源文字的颜色，目前支持：0(默认) 灰色，1 黑色，2 红色，3 绿色 |
| action_menu                      | 否       | 卡片右上角更多操作按钮                                       |
| action_menu.desc                 | 否       | 更多操作界面的描述                                           |
| action_menu.action_list          | 是       | 操作列表，列表长度取值范围为 [1, 3]                          |
| action_menu.action_list.text     | 是       | 操作的描述文案                                               |
| action_menu.action_list.key      | 是       | 操作key值，用户点击后，会产生回调事件将本参数作为EventKey返回，回调事件会带上该key值，最长支持1024字节，不可重复 |
| main_title.title                 | 否       | 一级标题，建议不超过36个字，文本通知型卡片本字段非必填，但不可本字段和sub_title_text都不填 |
| main_title.desc                  | 否       | 标题辅助信息，建议不超过44个字                               |
| quote_area                       | 否       | 引用文献样式                                                 |
| quote_area.type                  | 否       | 引用文献样式区域点击事件，0或不填代表没有点击事件，1 代表跳转url，2 代表跳转小程序 |
| quote_area.url                   | 否       | 点击跳转的url，quote_area.type是1时必填                      |
| quote_area.appid                 | 否       | 点击跳转的小程序的appid，必须是与当前应用关联的小程序，quote_area.type是2时必填 |
| quote_area.pagepath              | 否       | 点击跳转的小程序的pagepath，quote_area.type是2时选填         |
| quote_area.title                 | 否       | 引用文献样式的标题                                           |
| quote_area.quote_text            | 否       | 引用文献样式的引用文案                                       |
| emphasis_content                 | 否       | 关键数据样式                                                 |
| emphasis_content.title           | 否       | 关键数据样式的数据内容，建议不超过14个字                     |
| emphasis_content.desc            | 否       | 关键数据样式的数据描述内容，建议不超过22个字                 |
| sub_title_text                   | 否       | 二级普通文本，建议不超过160个字                              |
| horizontal_content_list          | 否       | 二级标题+文本列表，该字段可为空数组，但有数据的话需确认对应字段是否必填，列表长度不超过6 |
| horizontal_content_list.type     | 否       | 链接类型，0或不填代表不是链接，1 代表跳转url，2 代表下载附件，3 代表点击跳转成员详情 |
| horizontal_content_list.keyname  | 是       | 二级标题，建议不超过5个字                                    |
| horizontal_content_list.value    | 否       | 二级文本，如果horizontal_content_list.type是2，该字段代表文件名称（要包含文件类型），建议不超过30个字 |
| horizontal_content_list.url      | 否       | 链接跳转的url，horizontal_content_list.type是1时必填         |
| horizontal_content_list.media_id | 否       | 附件的media_id，horizontal_content_list.type是2时必填        |
| horizontal_content_list.userid   | 否       | 成员详情的userid，horizontal_content_list.type是3时必填      |
| jump_list                        | 否       | 跳转指引样式的列表，该字段可为空数组，但有数据的话需确认对应字段是否必填，列表长度不超过3 |
| jump_list.type                   | 否       | 跳转链接类型，0或不填代表不是链接，1 代表跳转url，2 代表跳转小程序 |
| jump_list.title                  | 是       | 跳转链接样式的文案内容，建议不超过18个字                     |
| jump_list.url                    | 否       | 跳转链接的url，jump_list.type是1时必填                       |
| jump_list.appid                  | 否       | 跳转链接的小程序的appid，jump_list.type是2时必填             |
| jump_list.pagepath               | 否       | 跳转链接的小程序的pagepath，jump_list.type是2时选填          |
| card_action                      | 是       | 整体卡片的点击跳转事件，text_notice必填本字段                |
| card_action.type                 | 是       | 跳转事件类型，0或不填代表不是链接，1 代表跳转url，2 代表打开小程序 |
| card_action.url                  | 否       | 跳转事件的url，card_action.type是1时必填                     |
| card_action.appid                | 否       | 跳转事件的小程序的appid，card_action.type是2时必填           |
| card_action.pagepath             | 否       | 跳转事件的小程序的pagepath，card_action.type是2时选填        |

### 图文展示型

![img](https://cdn3.apifox.cn/markdown-img/202110/08/194516-7A1Lho.png?imageslim)

```
{
    "userids" : ["userid1","userid2"],
    "partyids" : [2,3],
    "agentid" : 1,
    "response_code": "response_code",
    "template_card" : {
        "card_type" : "news_notice",
        "source" : {
            "icon_url": "图片的url",
            "desc": "企业微信",
            "desc_color": 1
        },
        "action_menu": {
            "desc": "卡片副交互辅助文本说明",
            "action_list": [
                {"text": "接受推送", "key": "A"},
                {"text": "不再推送", "key": "B"}
            ]
        },
        "main_title" : {
            "title" : "欢迎使用企业微信",
            "desc" : "您的好友正在邀请您加入企业微信"
        },
        "quote_area": {
            "type": 1,
            "url": "https://work.weixin.qq.com",
            "title": "企业微信的引用样式",
            "quote_text": "企业微信真好用呀真好用"
        },
        "image_text_area": {
            "type": 1,
            "url": "https://work.weixin.qq.com",
            "title": "企业微信的左图右文样式",
            "desc": "企业微信真好用呀真好用",
            "image_url": "https://img.iplaysoft.com/wp-content/uploads/2019/free-images/free_stock_photo_2x.jpg"
        },
        "card_image": {
            "url": "图片的url",
            "aspect_ratio": 1.3
        },
        "vertical_content_list": [
            {
                "title": "惊喜红包等你来拿",
                "desc": "下载企业微信还能抢红包！"
            }
        ],
        "horizontal_content_list" : [
            {
                "keyname": "邀请人",
                "value": "张三"
            },
            {
                "type": 1,
                "keyname": "企业微信官网",
                "value": "点击访问",
                "url": "https://work.weixin.qq.com"
            },
            {
                "type": 2,
                "keyname": "企业微信下载",
                "value": "企业微信.apk",
                "media_id": "文件的media_id"
            },
            {
                "type": 3,
                "keyname": "员工信息",
                "value": "点击查看",
                "userid": "zhangsan"
            }
        ],
        "jump_list" : [
            {
                "type": 1,
                "title": "企业微信官网",
                "url": "https://work.weixin.qq.com"
            },
            {
                "type": 2,
                "title": "跳转小程序",
                "appid": "小程序的appid",
                "pagepath": "/index.html"
            }
        ],
        "card_action": {
            "type": 2,
            "url": "https://work.weixin.qq.com",
            "appid": "小程序的appid",
            "pagepath": "/index.html"
        }
    }
}

```

**参数说明：**

| 参数                             | 是否必须 | 说明                                                         |
| -------------------------------- | -------- | ------------------------------------------------------------ |
| userids                          | 否       | 企业的成员ID列表（最多支持1000个）                           |
| partyids                         | 否       | 企业的部门ID列表（最多支持100个）                            |
| tagids                           | 否       | 企业的标签ID列表（最多支持100个）                            |
| atall                            | 否       | 更新整个任务接收人员                                         |
| agentid                          | 是       | 应用的agentid                                                |
| response_code                    | 是       | 更新卡片所需要消费的code，可通过发消息接口和回调接口返回值获取，一个code只能调用一次该接口，且只能在24小时内调用 |
| card_type                        | 是       | 模板卡片类型，图文展示型卡片此处填写 “news_notice”           |
| source                           | 否       | 卡片来源样式信息，不需要来源样式可不填写                     |
| source.icon_url                  | 否       | 来源图片的url                                                |
| source.desc                      | 否       | 来源图片的描述，建议不超过20个字                             |
| source.desc_color                | 否       | 来源文字的颜色，目前支持：0(默认) 灰色，1 黑色，2 红色，3 绿色 |
| action_menu                      | 否       | 卡片右上角更多操作按钮                                       |
| action_menu.desc                 | 否       | 更多操作界面的描述                                           |
| action_menu.action_list          | 是       | 操作列表，列表长度取值范围为 [1, 3]                          |
| action_menu.action_list.text     | 是       | 操作的描述文案                                               |
| action_menu.action_list.key      | 是       | 操作key值，用户点击后，会产生回调事件将本参数作为EventKey返回，回调事件会带上该key值，最长支持1024字节，不可重复 |
| main_title.title                 | 是       | 一级标题，建议不超过36个字                                   |
| main_title.desc                  | 否       | 标题辅助信息，建议不超过44个字                               |
| quote_area                       | 否       | 引用文献样式                                                 |
| quote_area.type                  | 否       | 引用文献样式区域点击事件，0或不填代表没有点击事件，1 代表跳转url，2 代表跳转小程序 |
| quote_area.url                   | 否       | 点击跳转的url，quote_area.type是1时必填                      |
| quote_area.appid                 | 否       | 点击跳转的小程序的appid，必须是与当前应用关联的小程序，quote_area.type是2时必填 |
| quote_area.pagepath              | 否       | 点击跳转的小程序的pagepath，quote_area.type是2时选填         |
| quote_area.title                 | 否       | 引用文献样式的标题                                           |
| quote_area.quote_text            | 否       | 引用文献样式的引用文案                                       |
| image_text_area                  | 否       | 左图右文样式，news_notice类型的卡片，card_image和image_text_area两者必填一个字段，不可都不填 |
| image_text_area.type             | 否       | 左图右文样式区域点击事件，0或不填代表没有点击事件，1 代表跳转url，2 代表跳转小程序 |
| image_text_area.url              | 否       | 点击跳转的url，image_text_area.type是1时必填                 |
| image_text_area.appid            | 否       | 点击跳转的小程序的appid，必须是与当前应用关联的小程序，image_text_area.type是2时必填 |
| image_text_area.pagepath         | 否       | 点击跳转的小程序的pagepath，image_text_area.type是2时选填    |
| image_text_area.title            | 否       | 左图右文样式的标题                                           |
| image_text_area.desc             | 否       | 左图右文样式的描述                                           |
| image_text_area.image_url        | 是       | 左图右文样式的图片url                                        |
| card_image                       | 否       | 图片样式，news_notice类型的卡片，card_image和image_text_area两者必填一个字段，不可都不填 |
| card_image.url                   | 是       | 图片的url                                                    |
| card_image.aspect_ratio          | 否       | 图片的宽高比，宽高比要小于2.25，大于1.3，不填该参数默认1.3   |
| vertical_content_list            | 否       | 卡片二级垂直内容，该字段可为空数组，但有数据的话需确认对应字段是否必填，列表长度不超过4 |
| vertical_content_list.title      | 是       | 卡片二级标题，建议不超过38个字                               |
| vertical_content_list.desc       | 否       | 二级普通文本，建议不超过160个字                              |
| horizontal_content_list          | 否       | 二级标题+文本列表，该字段可为空数组，但有数据的话需确认对应字段是否必填，列表长度不超过6 |
| horizontal_content_list.type     | 否       | 链接类型，0或不填代表不是链接，1 代表跳转url，2 代表下载附件，3 代表点击跳转成员详情 |
| horizontal_content_list.keyname  | 是       | 二级标题，建议不超过5个字                                    |
| horizontal_content_list.value    | 否       | 二级文本，如果horizontal_content_list.type是2，该字段代表文件名称（要包含文件类型），建议不超过30个字 |
| horizontal_content_list.url      | 否       | 链接跳转的url，horizontal_content_list.type是1时必填         |
| horizontal_content_list.media_id | 否       | 附件的media_id，horizontal_content_list.type是2时必填        |
| horizontal_content_list.userid   | 否       | 成员详情的userid，horizontal_content_list.type是3时必填      |
| jump_list                        | 否       | 跳转指引样式的列表，该字段可为空数组，但有数据的话需确认对应字段是否必填，列表长度不超过3 |
| jump_list.type                   | 否       | 跳转链接类型，0或不填代表不是链接，1 代表跳转url，2 代表跳转小程序 |
| jump_list.title                  | 是       | 跳转链接样式的文案内容，建议不超过18个字                     |
| jump_list.url                    | 否       | 跳转链接的url，jump_list.type是1时必填                       |
| jump_list.appid                  | 否       | 跳转链接的小程序的appid，jump_list.type是2时必填             |
| jump_list.pagepath               | 否       | 跳转链接的小程序的pagepath，jump_list.type是2时选填          |
| card_action                      | 是       | 整体卡片的点击跳转事件，news_notice必填本字段                |
| card_action.type                 | 是       | 跳转事件类型，0或不填代表不是链接，1 代表跳转url，2 代表打开小程序 |
| card_action.url                  | 否       | 跳转事件的url，card_action.type是1时必填                     |
| card_action.appid                | 否       | 跳转事件的小程序的appid，card_action.type是2时必填           |
| card_action.pagepath             | 否       | 跳转事件的小程序的pagepath，card_action.type是2时选填        |

### 按钮交互型

![img](https://cdn3.apifox.cn/markdown-img/202110/08/194516-latM00.png?imageslim)

```
{
    "userids" : ["userid1","userid2"],
    "partyids" : [2,3],
    "agentid" : 1,
    "response_code": "response_code",
    "template_card" : {
        "card_type" : "button_interaction",
        "source" : {
            "icon_url": "图片的url",
            "desc": "企业微信",
            "desc_color": 1
        },
        "action_menu": {
            "desc": "卡片副交互辅助文本说明",
            "action_list": [
                {"text": "接受推送", "key": "A"},
                {"text": "不再推送", "key": "B"}
            ]
        },
        "main_title" : {
            "title" : "欢迎使用企业微信",
            "desc" : "您的好友正在邀请您加入企业微信"
        },
        "quote_area": {
            "type": 1,
            "url": "https://work.weixin.qq.com",
            "title": "企业微信的引用样式",
            "quote_text": "企业微信真好用呀真好用"
        },
        "sub_title_text" : "下载企业微信还能抢红包！",
        "horizontal_content_list" : [
            {
                "keyname": "邀请人",
                "value": "张三"
            },
            {
                "type": 1,
                "keyname": "企业微信官网",
                "value": "点击访问",
                "url": "https://work.weixin.qq.com"
            },
            {
                "type": 2,
                "keyname": "企业微信下载",
                "value": "企业微信.apk",
                "media_id": "文件的media_id"
            },
            {
                "type": 3,
                "keyname": "员工信息",
                "value": "点击查看",
                "userid": "zhangsan"
            }
        ],
        "card_action": {
            "type": 2,
            "url": "https://work.weixin.qq.com",
            "appid": "小程序的appid",
            "pagepath": "/index.html"
        },
        "button_selection": {
            "question_key": "btn_question_key1",
            "title": "企业微信评分",
            "option_list": [
                {
                    "id": "btn_selection_id1",
                    "text": "100分"
                },
                {
                    "id": "btn_selection_id2",
                    "text": "101分"
                }
            ],
            "selected_id": "btn_selection_id1"
        },
        "button_list": [
            {
                "text": "按钮1",
                "style": 1,
                "key": "button_key_1"
            },
            {
                "text": "按钮2",
                "style": 2,
                "key": "button_key_2"
            }
        ],
        "replace_text": "已提交"
    }
}

```

**参数说明：**

| 参数                              | 是否必须 | 说明                                                         |
| --------------------------------- | -------- | ------------------------------------------------------------ |
| userids                           | 否       | 企业的成员ID列表（最多支持1000个）                           |
| partyids                          | 否       | 企业的部门ID列表（最多支持100个）                            |
| tagids                            | 否       | 企业的标签ID列表（最多支持100个）                            |
| atall                             | 否       | 更新整个任务接收人员                                         |
| agentid                           | 是       | 应用的agentid                                                |
| response_code                     | 是       | 更新卡片所需要消费的code，可通过发消息接口和回调接口返回值获取，一个code只能调用一次该接口，且只能在24小时内调用 |
| card_type                         | 是       | 模板卡片类型，按钮交互型卡片填写”button_interaction”         |
| source                            | 否       | 卡片来源样式信息，不需要来源样式可不填写                     |
| source.icon_url                   | 否       | 来源图片的url                                                |
| source.desc                       | 否       | 来源图片的描述，建议不超过20个字                             |
| source.desc_color                 | 否       | 来源文字的颜色，目前支持：0(默认) 灰色，1 黑色，2 红色，3 绿色 |
| action_menu                       | 否       | 卡片右上角更多操作按钮                                       |
| action_menu.desc                  | 否       | 更多操作界面的描述                                           |
| action_menu.action_list           | 是       | 操作列表，列表长度取值范围为 [1, 3]                          |
| action_menu.action_list.text      | 是       | 操作的描述文案                                               |
| action_menu.action_list.key       | 是       | 操作key值，用户点击后，会产生回调事件将本参数作为EventKey返回，回调事件会带上该key值，最长支持1024字节，不可重复 |
| main_title.title                  | 是       | 一级标题，建议不超过36个字                                   |
| main_title.desc                   | 否       | 标题辅助信息，建议不超过44个字                               |
| quote_area                        | 否       | 引用文献样式                                                 |
| quote_area.type                   | 否       | 引用文献样式区域点击事件，0或不填代表没有点击事件，1 代表跳转url，2 代表跳转小程序 |
| quote_area.url                    | 否       | 点击跳转的url，quote_area.type是1时必填                      |
| quote_area.appid                  | 否       | 点击跳转的小程序的appid，必须是与当前应用关联的小程序，quote_area.type是2时必填 |
| quote_area.pagepath               | 否       | 点击跳转的小程序的pagepath，quote_area.type是2时选填         |
| quote_area.title                  | 否       | 引用文献样式的标题                                           |
| quote_area.quote_text             | 否       | 引用文献样式的引用文案                                       |
| sub_title_text                    | 否       | 二级普通文本，建议不超过160个字                              |
| horizontal_content_list           | 否       | 二级标题+文本列表，该字段可为空数组，但有数据的话需确认对应字段是否必填，列表长度不超过6 |
| horizontal_content_list.type      | 否       | 链接类型，0或不填代表不是链接，1 代表跳转url，2 代表下载附件，3 代表点击跳转成员详情 |
| horizontal_content_list.keyname   | 是       | 二级标题，建议不超过5个字                                    |
| horizontal_content_list.value     | 否       | 二级文本，如果horizontal_content_list.type是2，该字段代表文件名称（要包含文件类型），建议不超过30个字 |
| horizontal_content_list.url       | 否       | 链接跳转的url，horizontal_content_list.type是1时必填         |
| horizontal_content_list.media_id  | 否       | 附件的media_id，horizontal_content_list.type是2时必填        |
| horizontal_content_list.userid    | 否       | 成员详情的userid，horizontal_content_list.type是3时必填      |
| card_action                       | 否       | 整体卡片的点击跳转事件                                       |
| card_action.type                  | 否       | 跳转事件类型，0或不填代表不是链接，1 代表跳转url，2 代表打开小程序 |
| card_action.url                   | 否       | 跳转事件的url，card_action.type是1时必填                     |
| card_action.appid                 | 否       | 跳转事件的小程序的appid，card_action.type是2时必填           |
| card_action.pagepath              | 否       | 跳转事件的小程序的pagepath，card_action.type是2时选填        |
| button_selection.question_key     | 是       | 下拉式的选择器的key，用户提交选项后，会产生回调事件，回调事件会带上该key值表示该题，最长支持1024字节 |
| button_selection.title            | 否       | 下拉式的选择器左边的标题                                     |
| button_selection.option_list      | 是       | 选项列表，下拉选项不超过 10 个，最少1个                      |
| button_selection.selected_id      | 否       | 默认选定的id，不填或错填默认第一个                           |
| button_selection.option_list.id   | 是       | 下拉式的选择器选项的id，用户提交后，会产生回调事件，回调事件会带上该id值表示该选项，最长支持128字节，不可重复 |
| button_selection.option_list.text | 是       | 下拉式的选择器选项的文案，建议不超过16个字                   |
| button_list                       | 是       | 按钮列表，列表长度不超过6                                    |
| button_list.type                  | 否       | 按钮点击事件类型，0 或不填代表回调点击事件，1 代表跳转url    |
| button_list.text                  | 是       | 按钮文案，建议不超过10个字                                   |
| button_list.style                 | 否       | 按钮样式，目前可填1~4，不填或错填默认1                       |
| button_list.key                   | 否       | 按钮key值，用户点击后，会产生回调事件将本参数作为EventKey返回，回调事件会带上该key值，最长支持1024字节，不可重复，button_list.type是0时必填 |
| button_list.url                   | 否       | 跳转事件的url，button_list.type是1时必填                     |
| replace_text                      | 否       | 按钮替换文案，填写本字段后会展现灰色不可点击按钮             |

备注：
按钮样式
![img](https://cdn3.apifox.cn/markdown-img/202110/08/194516-zMfIf4.png?imageslim)

### 投票选择型

![img](https://cdn3.apifox.cn/markdown-img/202110/08/194516-byzsb5.png?imageslim)

```
{
    "userids" : ["userid1","userid2"],
    "partyids" : [2,3],
    "agentid" : 1,
    "response_code": "response_code",
    "template_card" : {
        "card_type" : "vote_interaction",
        "source" : {
            "icon_url": "图片的url",
            "desc": "企业微信"
        },
        "main_title" : {
            "title" : "欢迎使用企业微信",
            "desc" : "您的好友正在邀请您加入企业微信"
        },
        "checkbox": {
            "question_key": "question_key1",
            "option_list": [
                {
                    "id": "option_id1",
                    "text": "选择题选项1",
                    "is_checked": true
                },
                {
                    "id": "option_id2",
                    "text": "选择题选项2",
                    "is_checked": false
                }
            ],
            "disable": false,
            "mode": 1
        },
        "submit_button": {
            "text": "提交",
            "key": "key"
        },
        "replace_text": "已提交"
    }
}

```

**参数说明：**

| 参数                            | 是否必须 | 说明                                                         |
| ------------------------------- | -------- | ------------------------------------------------------------ |
| userids                         | 否       | 企业的成员ID列表（最多支持1000个）                           |
| partyids                        | 否       | 企业的部门ID列表（最多支持100个）                            |
| tagids                          | 否       | 企业的标签ID列表（最多支持100个）                            |
| atall                           | 否       | 更新整个任务接收人员                                         |
| agentid                         | 是       | 应用的agentid                                                |
| response_code                   | 是       | 更新卡片所需要消费的code，可通过发消息接口和回调接口返回值获取，一个code只能调用一次该接口，且只能在24小时内调用 |
| card_type                       | 是       | 模板卡片类型，投票选择型卡片填写”vote_interaction”           |
| source                          | 否       | 卡片来源样式信息，不需要来源样式可不填写                     |
| source.icon_url                 | 否       | 来源图片的url                                                |
| source.desc                     | 否       | 来源图片的描述，建议不超过20个字                             |
| main_title.title                | 是       | 一级标题，建议不超过16个字                                   |
| main_title.desc                 | 否       | 二级普通文本，建议不超过160个字                              |
| checkbox                        | 否       | 选择题样式                                                   |
| checkbox.question_key           | 是       | 选择题key值，用户提交选项后，会产生回调事件，回调事件会带上该key值表示该题，最长支持1024字节 |
| checkbox.disable                | 否       | 是否可以选择状态                                             |
| checkbox.mode                   | 否       | 选择题模式，单选：0，多选：1，不填默认0                      |
| checkbox.option_list            | 是       | 选项list，选项个数不超过 20 个，最少1个                      |
| checkbox.option_list.id         | 是       | 选项id，用户提交选项后，会产生回调事件，回调事件会带上该id值表示该选项，最长支持128字节，不可重复 |
| checkbox.option_list.text       | 是       | 选项文案描述，建议不超过17个字                               |
| checkbox.option_list.is_checked | 是       | 该选项是否要默选中                                           |
| submit_button                   | 否       | 提交按钮样式                                                 |
| submit_button.text              | 是       | 按钮文案，建议不超过10个字，不填默认为提交                   |
| submit_button.key               | 是       | 提交按钮的key，会产生回调事件将本参数作为EventKey返回，最长支持1024字节 |
| replace_text                    | 否       | 按钮替换文案，填写本字段后会展现灰色不可点击按钮             |

### 多项选择型

![img](https://cdn3.apifox.cn/markdown-img/202110/08/194517-GoXe1g.png?imageslim)

```
{
    "userids" : ["userid1","userid2"],
    "partyids" : [2,3],
    "tagids" : [44,55],
    "atall" : 0,
    "agentid" : 1,
    "response_code": "response_code",
    "template_card" : {
        "card_type" : "multiple_interaction",
        "source" : {
            "icon_url": "图片的url",
            "desc": "企业微信"
        },
        "main_title" : {
            "title" : "欢迎使用企业微信",
            "desc" : "您的好友正在邀请您加入企业微信"
        },
        "select_list": [
            {
                "question_key": "question_key1",
                "title": "选择器标签1",
                "selected_id": "selection_id1",
                "disable": false,
                "option_list": [
                    {
                        "id": "selection_id1",
                        "text": "选择器选项1"
                    },
                    {
                        "id": "selection_id2",
                        "text": "选择器选项2"
                    }
                ]
            },
            {
                "question_key": "question_key2",
                "title": "选择器标签2",
                "selected_id": "selection_id3",
                "disable": false,
                "option_list": [
                    {
                        "id": "selection_id3",
                        "text": "选择器选项3"
                    },
                    {
                        "id": "selection_id4",
                        "text": "选择器选项4"
                    }
                ]
            }
        ],
        "submit_button": {
            "text": "提交",
            "key": "key"
        },
        "replace_text": "已提交"
    }
}

```

**参数说明：**

| 参数                         | 是否必须 | 说明                                                         |
| ---------------------------- | -------- | ------------------------------------------------------------ |
| userids                      | 否       | 企业的成员ID列表（最多支持1000个）                           |
| partyids                     | 否       | 企业的部门ID列表（最多支持100个）                            |
| tagids                       | 否       | 企业的标签ID列表（最多支持100个）                            |
| atall                        | 否       | 更新整个任务接收人员                                         |
| agentid                      | 是       | 应用的agentid                                                |
| response_code                | 是       | 更新卡片所需要消费的code，可通过发消息接口和回调接口返回值获取，一个code只能调用一次该接口，且只能在24小时内调用 |
| card_type                    | 是       | 模板卡片类型，多项选择型卡片填写 “multiple_interaction”      |
| source                       | 否       | 卡片来源样式信息，不需要来源样式可不填写                     |
| source.icon_url              | 否       | 来源图片的url                                                |
| source.desc                  | 否       | 来源图片的描述，建议不超过20个字                             |
| main_title.title             | 是       | 一级标题，建议不超过36个字                                   |
| main_title.desc              | 否       | 标题辅助信息，建议不超过160个字                              |
| select_list                  | 是       | 下拉式的选择器列表，multiple_interaction类型的卡片该字段不可为空，一个消息最多支持 3 个选择器 |
| select_list.question_key     | 是       | 下拉式的选择器题目的key，用户提交选项后，会产生回调事件，回调事件会带上该key值表示该题，最长支持1024字节，不可重复 |
| select_list.title            | 否       | 下拉式的选择器上面的title                                    |
| select_list.option_list      | 是       | 选项列表，下拉选项不超过 10 个，最少1个                      |
| select_list.selected_id      | 否       | 默认选定的id，不填或错填默认第一个                           |
| select_list.disable          | 否       | 是否可以选择状态                                             |
| select_list.option_list.id   | 是       | 下拉式的选择器选项的id，用户提交选项后，会产生回调事件，回调事件会带上该id值表示该选项，最长支持128字节，不可重复 |
| select_list.option_list.text | 是       | 下拉式的选择器选项的文案，建议不超过16个字                   |
| submit_button                | 否       | 提交按钮样式                                                 |
| submit_button.text           | 是       | 按钮文案，建议不超过10个字，不填默认为提交                   |
| submit_button.key            | 是       | 提交按钮的key，会产生回调事件将本参数作为EventKey返回，最长支持1024字节 |
| replace_text                 | 否       | 按钮替换文案，填写本字段后会展现灰色不可点击按钮             |

文档ID: 32086
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90001/94945
https://open.work.weixin.qq.com/api/doc/90000/94888

> Body 请求参数

```json
{
  "userids": [
    "userid1",
    "userid2"
  ],
  "partyids": [
    2,
    3
  ],
  "tagids": [
    44,
    55
  ],
  "atall": 0,
  "agentid": 1,
  "response_code": "response_code",
  "button": {
    "replace_name": "replace_name"
  }
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» userids|body|[string]| 是 |none|
|» partyids|body|[integer]| 是 |none|
|» tagids|body|[integer]| 是 |none|
|» atall|body|integer| 是 |none|
|» agentid|body|integer| 是 |none|
|» response_code|body|string| 是 |none|
|» button|body|object| 是 |none|
|»» replace_name|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "invaliduser": [
    "userid1",
    "userid2"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» invaliduser|[string]|true|none||不区分大小写，返回的列表都统一转为小写|

# 企业内部开发/消息推送/接收消息与事件/概述

## GET 概述-获取企业微信服务器的ip段

GET /cgi-bin/getcallbackip

企业微信在回调企业指定的URL时，是通过特定的IP发送出去的。如果企业需要做防火墙配置，那么可以通过这个接口获取到所有相关的IP段。

权限说明：
无限定。

文档ID: 12977
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90238

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "ip_list": [
    "101.226.103.*",
    "101.226.62.*"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» errmsg|string|false|none||none|
|» ip_list|[string]|false|none||企业微信回调的IP段|

## POST 概述-使用接收消息

POST /%EF%BC%9Ahttp://api.3dept.com/

开启接收消息模式后，企业微信会将消息发送给企业填写的URL，企业后台需要做正确的响应。

### 接收消息协议的说明

- 企业微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。如果企业在调试中，发现成员无法收到被动回复的消息，可以检查是否消息处理超时。
- 当接收成功后，http头部返回200表示接收ok，其他错误码企业微信后台会一律当做失败并发起重试。
- 关于重试的消息排重，有msgid的消息推荐使用msgid排重。[事件类型消息](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12974)推荐使用FromUserName + CreateTime排重。
- 假如企业无法保证在五秒内处理并回复，或者不想回复任何内容，可以直接返回200（即以空串为返回包）。企业后续可以使用主动发消息接口进行异步回复。

### 接收消息请求的说明
假设企业的接收消息的URL设置为http://api.3dept.com。

企业收到消息后，需要作如下处理：

1. 对msg_signature进行[校验](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/消息体签名校验)
2. [解密](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/密文解密得到msg的过程)Encrypt，得到明文的消息结构体（消息结构体后面章节会详说）
3. 如果需要被动回复消息，构造被动响应包
4. 正确响应本次请求

以上1~2步骤可以直接使用[解密函数](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/解密函数)一步到位。
3步骤其实包含加密被动回复消息、生成新签名、构造被动响应包三个步骤，可以直接使用[加密函数](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/加密函数)一步到位。

文档ID: 12977
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90238

> Body 请求参数

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<xml>
  <ToUserName>string</ToUserName>
  <AgentID>string</AgentID>
  <Encrypt>string</Encrypt>
</xml>
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|msg_signature|query|string| 是 |none|
|timestamp|query|string| 是 |none|
|nonce|query|string| 是 |none|
|body|body|object| 否 |none|
|» ToUserName|body|string| 是 |企业微信的CorpID，当为第三方应用回调事件时，CorpID的内容为suiteid|
|» AgentID|body|string| 是 |接收的应用id，可在应用的设置页面获取。仅应用相关的回调会带该字段。|
|» Encrypt|body|string| 是 |消息结构体加密后的字符串|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» Encrypt|string|true|none||经过加密的消息结构体|
|» MsgSignature|string|true|none||消息签名|
|» TimeStamp|string|true|none||时间戳|
|» Nonce|string|true|none||随机数，由企业自行生成|

## GET 概述-验证URL有效性

GET /

当点击“保存”提交以上信息时，企业微信会发送一条验证消息到填写的URL，发送方法为GET。
企业的接收消息服务器接收到验证请求后，需要作出正确的响应才能通过URL验证。

企业在获取请求时需要做Urldecode处理，否则可能会验证不成功
你可以访问接口调试工具进行调试，依次选择 建立连接 > 接收消息。

假设接收消息地址设置为：http://api.3dept.com/，企业微信将向该地址发送如下验证请求：

企业后台收到请求后，需要做如下操作：

1. 对收到的请求做Urldecode处理
2. 通过参数msg_signature[对请求进行校验](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/消息体签名校验)，确认调用者的合法性。
3. [解密echostr](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/密文解密得到msg的过程)参数得到消息内容(即msg字段)
4. 在1秒内响应GET请求，响应内容为上一步得到的明文消息内容(不能加引号，不能带bom头，不能带换行符)

以上2~3步骤可以直接使用[验证URL函数](https://open.work.weixin.qq.com/api/doc/90000/90135/90238#12976/验证URL函数)一步到位。
之后接入验证生效，接收消息开启成功

文档ID: 12977
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90238

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|msg_signature|query|string| 是 |企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体。签名计算方法参考 消息体签名检验|
|timestamp|query|string| 是 |时间戳。与nonce结合使用，用于防止请求重放攻击。|
|nonce|query|string| 是 |随机数。与timestamp结合使用，用于防止请求重放攻击。|
|echostr|query|string| 是 |加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文|

> 返回示例

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 企业内部开发/消息推送/发送消息到群聊会话

## POST 应用推送消息

POST /cgi-bin/appchat/send

应用支持推送文本、图片、视频、文件、图文等类型。

**请求包体:**

> 各个消息类型的具体POST格式参考后面消息类型说明

**限制说明：**
只允许企业自建应用调用，且应用的可见范围必须是根部门；
chatid所代表的群必须是该应用所创建；
每企业消息发送量不可超过2万人次/分，不可超过30万人次/小时（若群有100人，每发一次消息算100人次）；
每个成员在群中收到的应用消息不可超过200条/分，1万条/天，超过会被丢弃（接口不会报错）；

## 消息类型

### 文本消息

**请求示例：**

```
{    "chatid": "CHATID",    "msgtype":"text",    "text":{        "content" : "你的快递已到\n请携带工卡前往邮件中心领取"    },    "safe":0}
```

**参数说明：**

| 参数    | 是否必须 | 说明                                        |
| ------- | -------- | ------------------------------------------- |
| chatid  | 是       | 群聊id                                      |
| msgtype | 是       | 消息类型，此时固定为：text                  |
| content | 是       | 消息内容，最长不超过2048个字节              |
| safe    | 否       | 表示是否是保密消息，0表示否，1表示是，默认0 |

**文本消息展现：**

![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-hTdHSo.png?imageslim)
**特殊说明：**
其中text参数的content字段可以支持换行，换行符请用转义过的’\n’。

### 图片消息

**请求示例：**

```
{    "chatid": "CHATID",    "msgtype":"image",    "image":{        "media_id": "MEDIAID"    },    "safe":0}
```

**请求参数：**

| 参数     | 是否必须 | 说明                                         |
| -------- | -------- | -------------------------------------------- |
| chatid   | 是       | 群聊id                                       |
| msgtype  | 是       | 消息类型，此时固定为：image                  |
| media_id | 是       | 图片媒体文件id，可以调用上传临时素材接口获取 |
| safe     | 否       | 表示是否是保密消息，0表示否，1表示是，默认0  |

**图片消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-nn5612.png?imageslim)

### 语音消息

**请求示例：**

```
{   "chatid" : "CHATID",   "msgtype" : "voice",   "voice" : {        "media_id" : "MEDIA_ID"   }}
```

**参数说明：**

| 参数     | 是否必须 | 说明                                     |
| -------- | -------- | ---------------------------------------- |
| chatid   | 是       | 群聊id                                   |
| msgtype  | 是       | 消息类型，此时固定为：voice              |
| media_id | 是       | 语音文件id，可以调用上传临时素材接口获取 |

**语音消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-jFUKhY.png?imageslim)

### 视频消息

**请求示例：**

```
{   "chatid" : "CHATID",   "msgtype" : "video",   "video" : {       "media_id" : "MEDIA_ID",       "description" : "Description",       "title": "Title"   },   "safe":0}
```

**参数说明：**

| 参数        | 是否必须 | 说明                                            |
| ----------- | -------- | ----------------------------------------------- |
| chatid      | 是       | 群聊id                                          |
| msgtype     | 是       | 消息类型，此时固定为：video                     |
| media_id    | 是       | 视频媒体文件id，可以调用上传临时素材接口获取    |
| title       | 否       | 视频消息的标题，不超过128个字节，超过会自动截断 |
| description | 否       | 视频消息的描述，不超过512个字节，超过会自动截断 |
| safe        | 否       | 表示是否是保密消息，0表示否，1表示是，默认0     |

**视频消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-sPDHdn.png?imageslim)

### 文件消息

**请求示例：**

```
{   "chatid" : "CHATID",   "msgtype" : "file",   "file" : {        "media_id" : "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"   },   "safe":0}
```

**参数说明：**

| 参数     | 是否必须 | 说明                                        |
| -------- | -------- | ------------------------------------------- |
| chatid   | 是       | 群聊id                                      |
| msgtype  | 是       | 消息类型，此时固定为：file                  |
| media_id | 是       | 文件id，可以调用上传临时素材接口获取        |
| safe     | 否       | 表示是否是保密消息，0表示否，1表示是，默认0 |

**文件消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-z93qRK.png?imageslim)

### 文本卡片消息

**请求示例：**

```
{    "chatid": "CHATID",    "msgtype":"textcard",    "textcard":{        "title" : "领奖通知",        "description" : "<div class=\"gray\">2016年9月26日</div> <div class=\"normal\"> 恭喜你抽中iPhone 7一台，领奖码:520258</div><div class=\"highlight\">请于2016年10月10日前联系行 政同事领取</div>",        "url":"https://work.weixin.qq.com/",        "btntxt":"更多"    },    "safe":0}
```

**参数说明：**

| 参数        | 是否必须 | 说明                                                    |
| ----------- | -------- | ------------------------------------------------------- |
| chatid      | 是       | 群聊id                                                  |
| msgtype     | 是       | 消息类型，此时固定为：textcard                          |
| title       | 是       | 标题，不超过128个字节，超过会自动截断                   |
| description | 是       | 描述，不超过512个字节，超过会自动截断                   |
| url         | 是       | 点击后跳转的链接。                                      |
| btntxt      | 否       | 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。 |

**特殊说明**：
卡片消息的展现形式非常灵活，支持使用br标签或者空格来进行换行处理，也支持使用div标签来使用不同的字体颜色，目前内置了3种文字颜色：灰色(gray)、高亮(highlight)、默认黑色(normal)，将其作为div标签的class属性即可，具体用法请参考上面的示例。

**文本卡片消息展现 ：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-ITWyU9.png?imageslim)

### 图文消息

**请求示例：**

```
{    "chatid": "CHATID",    "msgtype":"news",    "news":{        "articles" :        [            {                "title" : "中秋节礼品领取",                "description" : "今年中秋节公司有豪礼相送",                "url":"https://work.weixin.qq.com/",                "picurl":"http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"             }        ]    },    "safe":0}
```

**参数说明：**

| 参数        | 是否必须 | 说明                                                         |
| ----------- | -------- | ------------------------------------------------------------ |
| chatid      | 是       | 群聊id                                                       |
| msgtype     | 是       | 消息类型，此时固定为：news                                   |
| articles    | 是       | 图文消息，一个图文消息支持1到8条图文                         |
| title       | 是       | 标题，不超过128个字节，超过会自动截断                        |
| description | 否       | 描述，不超过512个字节，超过会自动截断                        |
| url         | 是       | 点击后跳转的链接。                                           |
| picurl      | 否       | 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图1068*455，小图150*150。 |

**图文消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-5HvmR7.png?imageslim)

### 图文消息（mpnews）

> mpnews类型的图文消息，跟普通的图文消息一致，唯一的差异是图文内容存储在企业微信。
> 多次发送mpnews，会被认为是不同的图文，阅读、点赞的统计会被分开计算。

**请求示例：**

```
{    "chatid": "CHATID",    "msgtype":"mpnews",    "mpnews":{        "articles":[            {                "title": "地球一小时",                "thumb_media_id": "biz_get(image)",                "author": "Author",                "content_source_url": "https://work.weixin.qq.com",                "content": "3月24日20:30-21:30 \n办公区将关闭照明一小时，请各部门同事相互转告",                "digest": "3月24日20:30-21:30 \n办公区将关闭照明一小时"            }         ]    },    "safe":0}
```

**参数说明：**

| 参数               | 是否必须 | 说明                                                         |
| ------------------ | -------- | ------------------------------------------------------------ |
| chatid             | 是       | 群聊id                                                       |
| msgtype            | 是       | 消息类型，此时固定为：mpnews                                 |
| articles           | 是       | 图文消息，一个图文消息支持1到8条图文                         |
| title              | 是       | 标题，不超过128个字节，超过会自动截断                        |
| thumb_media_id     | 是       | 图文消息缩略图的media_id, 可以通过[素材管理](https://open.work.weixin.qq.com/api/doc/90000/90135/90248#10112)接口获得。此处thumb_media_id即上传接口返回的media_id |
| author             | 否       | 图文消息的作者，不超过64个字节                               |
| content_source_url | 否       | 图文消息点击“阅读原文”之后的页面链接                         |
| content            | 是       | 图文消息的内容，支持html标签，不超过666 K个字节              |
| digest             | 否       | 图文消息的描述，不超过512个字节，超过会自动截断              |
| safe               | 否       | 表示是否是保密消息，0表示否，1表示是，默认0                  |

**图文消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-ZXfanT.png?imageslim)

### markdown消息

> 目前仅支持[markdown语法的子集](https://open.work.weixin.qq.com/api/doc/90000/90135/90248#10167/支持的markdown语法)
> 微工作台（原企业号）不支持展示markdown消息

**请求示例：**

```
{   "chatid": "CHATID",   "msgtype":"markdown",   "markdown": {        "content": "您的会议室已经预定，稍后会同步到`邮箱`                 >**事项详情**                 >事　项：<font color=\"info\">开会</font>                 >组织者：@miglioguan                 >参与者：@miglioguan、@kunliu、@jamdeezhou、@kanexiong、@kisonwang                >                >会议室：<font color=\"info\">广州TIT 1楼 301</font>                >日　期：<font color=\"warning\">2018年5月18日</font>                >时　间：<font color=\"comment\">上午9:00-11:00</font>                >                >请准时参加会议。                >                >如需修改会议信息，请点击：[修改会议信息](https://work.weixin.qq.com)"   }}
```

**参数说明：**

| 参数    | 是否必须 | 说明                                               |
| ------- | -------- | -------------------------------------------------- |
| chatid  | 是       | 群聊id                                             |
| msgtype | 是       | 消息类型，此时固定为：markdown                     |
| content | 是       | markdown内容，最长不超过2048个字节，必须是utf8编码 |

**示例效果：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201611-PStovM.png?imageslim)

文档ID: 13294
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90248

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|undefined|

## POST 创建群聊会话

POST /cgi-bin/appchat/create

限制说明：
只允许企业自建应用调用，且应用的可见范围必须是根部门；
群成员人数不可超过管理端配置的“群成员人数上限”，且最大不可超过2000人；
每企业创建群数不可超过1000/天；
注意：刚创建的群，如果没有下发消息，在企业微信不会出现该群。

文档ID: 13288
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90245

> Body 请求参数

```json
{
  "name": "NAME",
  "owner": "userid1",
  "userlist": [
    "userid1",
    "userid2",
    "userid3"
  ],
  "chatid": "CHATID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» owner|body|string| 否 |指定群主的id。如果不指定，系统会随机从userlist中选一人作为群主|
|» userlist|body|[string]| 是 |群成员id列表。至少2人，至多2000人|
|» chatid|body|string| 否 |群聊的唯一标志，不能与已有的群重复；字符串类型，最长32个字符。只允许字符0-9及字母a-zA-Z。如果不填，系统会随机生成群id|
|» name|body|string| 否 |群聊名，最多50个utf8字符，超过将截断|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "chatid": "CHATID"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» chatid|string|true|none||群聊的唯一标志|

## GET 获取群聊会话

GET /cgi-bin/appchat/get

权限说明：
只允许企业自建应用调用，且应用的可见范围必须是根部门；
chatid所代表的群必须是该应用所创建；
第三方不可调用。

文档ID: 13293
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90247

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|chatid|query|string| 是 |群聊id|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "chat_info": {
    "chatid": "CHATID",
    "name": "NAME",
    "owner": "userid2",
    "userlist": [
      "userid1",
      "userid2",
      "userid3"
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» chat_info|object|true|none||群聊信息|
|»» chatid|string|true|none||群聊唯一标志|
|»» name|string|true|none||群聊名|
|»» owner|string|true|none||群主id|
|»» userlist|[string]|true|none||群成员id列表|

## POST 修改群聊会话

POST /cgi-bin/appchat/update

限制说明：
只允许企业自建应用调用，且应用的可见范围必须是根部门；
chatid所代表的群必须是该应用所创建；
群成员人数不可超过2000人；
每企业变更群的次数不可超过1000次/小时；
文档ID: 13292
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90246

> Body 请求参数

```json
{
  "chatid": "CHATID",
  "name": "NAME",
  "owner": "userid2",
  "add_user_list": [
    "userid1",
    "userid2",
    "userid3"
  ],
  "del_user_list": [
    "userid3",
    "userid4"
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» owner|body|string| 否 |新群主的id。若不需更新，请忽略此参数|
|» chatid|body|string| 是 |群聊id|
|» del_user_list|body|[string]| 否 |踢出成员的id列表|
|» name|body|string| 否 |新的群聊名。若不需更新，请忽略此参数。最多50个utf8字符，超过将截断|
|» add_user_list|body|[string]| 否 |添加成员的id列表|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|

# 企业内部开发/消息推送/互联企业消息推送

## POST 发送应用消息

POST /cgi-bin/linkedcorp/message/send

接口概述：互联企业是企业微信提供的满足集团与子公司、企业与上下游供应商进行连接的功能，企业可以共享通讯录以及应用给互联企业，如需要，你可以前往管理后台-通讯录创建互联企业，之后你可以在自建应用的可见范围设置互联企业的通讯录；此接口主要满足开发者给互联企业成员推送消息的诉求。

如果部分接收人无权限或不存在，发送仍然执行，但会返回无效的部分（即invaliduser或invalidparty），常见的原因是接收人不在应用的可见范围内。

各个消息类型的具体POST格式参考以下文档。
## 消息类型

### 文本消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,    "msgtype" : "text",   "agentid" : 1,   "text" : {       "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。"   },   "safe":0}
```

**参数说明：**

| 参数    | 是否必须 | 说明                                                         |
| ------- | -------- | ------------------------------------------------------------ |
| touser  | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中，corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag   | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall   | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype | 是       | 消息类型，此时固定为：text                                   |
| agentid | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| content | 是       | 消息内容，最长不超过2048个字节                               |
| safe    | 否       | 表示是否是保密消息，0表示否，1表示是，默认0                  |

注意：linked_id为互联ID，此ID可以在管理后台-通讯录-互联企业-详情里查看
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201926-fB3dqx.png?imageslim)

### 图片消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "image",   "agentid" : 1,   "image" : {        "media_id" : "MEDIA_ID"   },   "safe":0}
```

**请求参数：**

| 参数     | 是否必须 | 说明                                                         |
| -------- | -------- | ------------------------------------------------------------ |
| touser   | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty  | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag    | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall    | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype  | 是       | 消息类型，此时固定为：image                                  |
| agentid  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id | 是       | 图片媒体文件id，可以调用上传临时素材接口获取                 |
| safe     | 否       | 表示是否是保密消息，0表示否，1表示是，默认0                  |

### 语音消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "voice",   "agentid" : 1,   "voice" : {        "media_id" : "MEDIA_ID"   }}
```

**参数说明：**

| 参数     | 是否必须 | 说明                                                         |
| -------- | -------- | ------------------------------------------------------------ |
| touser   | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty  | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag    | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall    | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype  | 是       | 消息类型，此时固定为：voice                                  |
| agentid  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id | 是       | 语音文件id，可以调用上传临时素材接口获取                     |

### 视频消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "video",   "agentid" : 1,   "video" : {        "media_id" : "MEDIA_ID",        "title" : "Title",       "description" : "Description"   },   "safe":0}
```

**参数说明：**

| 参数        | 是否必须 | 说明                                                         |
| ----------- | -------- | ------------------------------------------------------------ |
| touser      | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty     | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag       | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall       | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype     | 是       | 消息类型，此时固定为：video                                  |
| agentid     | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id    | 是       | 视频媒体文件id，可以调用上传临时素材接口获取                 |
| title       | 否       | 视频消息的标题，不超过128个字节，超过会自动截断              |
| description | 否       | 视频消息的描述，不超过512个字节，超过会自动截断              |
| safe        | 否       | 表示是否是保密消息，0表示否，1表示是，默认0                  |

### 文件消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "file",   "agentid" : 1,   "file" : {        "media_id" : "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"   },   "safe":0}
```

**参数说明：**

| 参数     | 是否必须 | 说明                                                         |
| -------- | -------- | ------------------------------------------------------------ |
| touser   | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty  | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag    | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall    | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype  | 是       | 消息类型，此时固定为：file                                   |
| agentid  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id | 是       | 文件id，可以调用上传临时素材接口获取                         |
| safe     | 否       | 表示是否是保密消息，0表示否，1表示是，默认0                  |

**文件消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201926-BjhBve.jpeg?imageslim)

### 文本卡片消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "textcard",   "agentid" : 1,   "textcard" : {            "title" : "领奖通知",            "description" : "<div class=\"gray\">2016年9月26日</div> <div class=\"normal\">恭喜你抽中iPhone 7一台，领奖码：xxxx</div><div class=\"highlight\">请于2016年10月10日前联系行政同事领取</div>",            "url" : "URL",            "btntxt":"更多"   }}
```

**参数说明：**

| 参数        | 是否必须 | 说明                                                         |
| ----------- | -------- | ------------------------------------------------------------ |
| touser      | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty     | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag       | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall       | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype     | 是       | 消息类型，此时固定为：textcard                               |
| agentid     | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| title       | 是       | 标题，不超过128个字节，超过会自动截断                        |
| description | 是       | 描述，不超过512个字节，超过会自动截断                        |
| url         | 是       | 点击后跳转的链接。                                           |
| btntxt      | 否       | 按钮文字。 默认为“详情”， 不超过4个文字，超过自动截断。      |

**文本卡片消息展现 ：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201926-DBsrjr.jpeg?imageslim)

**特殊说明**：
卡片消息的展现形式非常灵活，支持使用br标签或者空格来进行换行处理，也支持使用div标签来使用不同的字体颜色，目前内置了3种文字颜色：灰色(gray)、高亮(highlight)、默认黑色(normal)，将其作为div标签的class属性即可，具体用法请参考上面的示例。

### 图文消息

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "news",   "agentid" : 1,   "news" : {       "articles" : [           {               "title" : "中秋节礼品领取",               "description" : "今年中秋节公司有豪礼相送",               "url" : "URL",               "picurl" : "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png",               "btntxt":"更多"           }        ]   }}
```

**参数说明：**

| 参数        | 是否必须 | 说明                                                         |
| ----------- | -------- | ------------------------------------------------------------ |
| touser      | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中，corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty     | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag       | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall       | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype     | 是       | 消息类型，此时固定为：news                                   |
| agentid     | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| articles    | 是       | 图文消息，一个图文消息支持1到8条图文                         |
| title       | 是       | 标题，不超过128个字节，超过会自动截断                        |
| description | 否       | 描述，不超过512个字节，超过会自动截断                        |
| url         | 是       | 点击后跳转的链接。                                           |
| picurl      | 否       | 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 640x320，小图80x80。 |
| btntxt      | 否       | 按钮文字，仅在图文数为1条时才生效。 默认为“阅读全文”， 不超过4个文字，超过自动截断。该设置只在企业微信上生效，微工作台（原企业号）上不生效。 |

**图文消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201927-vpcjXq.jpeg?imageslim)

### 图文消息（mpnews）

> mpnews类型的图文消息，跟普通的图文消息一致，唯一的差异是图文内容存储在企业微信。
> 多次发送mpnews，会被认为是不同的图文，阅读、点赞的统计会被分开计算。

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],    "toall" : 0,   "msgtype" : "mpnews",   "agentid" : 1,   "mpnews" : {       "articles":[           {               "title": "Title",                "thumb_media_id": "MEDIA_ID",               "author": "Author",               "content_source_url": "URL",               "content": "Content",               "digest": "Digest description"            }       ]   },   "safe":0}
```

**参数说明：**

| 参数               | 是否必须 | 说明                                                         |
| ------------------ | -------- | ------------------------------------------------------------ |
| touser             | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty            | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag              | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall              | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype            | 是       | 消息类型，此时固定为：mpnews                                 |
| agentid            | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| articles           | 是       | 图文消息，一个图文消息支持1到8条图文                         |
| title              | 是       | 标题，不超过128个字节，超过会自动截断                        |
| thumb_media_id     | 是       | 图文消息缩略图的media_id, 可以通过[素材管理](https://open.work.weixin.qq.com/api/doc/90000/90135/90249#10112)接口获得。此处thumb_media_id即上传接口返回的media_id |
| author             | 否       | 图文消息的作者，不超过64个字节                               |
| content_source_url | 否       | 图文消息点击“阅读原文”之后的页面链接                         |
| content            | 是       | 图文消息的内容，支持html标签，不超过666 K个字节              |
| digest             | 否       | 图文消息的描述，不超过512个字节，超过会自动截断              |
| safe               | 否       | 表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，2表示仅限在企业内分享，默认为0；注意仅mpnews类型的消息支持safe值为2，其他消息类型不支持 |

### markdown消息

> 目前仅支持[markdown语法的子集](https://open.work.weixin.qq.com/api/doc/90000/90135/90249#10167/支持的markdown语法)
> 微工作台（原企业号）不支持展示markdown消息

**请求示例：**

```
{   "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],   "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],   "totag" : ["tagid1","tagid2"],   "toall" : 0,   "msgtype" : "markdown",   "agentid" : 1,   "markdown": {        "content": "您的会议室已经预定，稍后会同步到`邮箱`                >**事项详情**                >事　项：<font color=\"info\">开会</font>                >组织者：@miglioguan                >参与者：@miglioguan、@kunliu、@jamdeezhou、@kanexiong、@kisonwang                >                >会议室：<font color=\"info\">广州TIT 1楼 301</font>                >日　期：<font color=\"warning\">2018年5月18日</font>                >时　间：<font color=\"comment\">上午9:00-11:00</font>                >                >请准时参加会议。                >                >如需修改会议信息，请点击：[修改会议信息](https://work.weixin.qq.com)"   }}
```

**参数说明：**

| 参数    | 是否必须 | 说明                                                         |
| ------- | -------- | ------------------------------------------------------------ |
| touser  | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中，corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag   | 否       | 本企业的标签ID列表，最多支持100个。                          |
| toall   | 否       | 1表示发送给应用可见范围内的所有人（包括互联企业的成员），默认为0 |
| msgtype | 是       | 消息类型，此时固定为：markdown                               |
| agentid | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| content | 是       | markdown内容，最长不超过2048个字节，必须是utf8编码           |

**示例效果：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201926-MQjUeR.png?imageslim)

### 小程序通知消息

> 小程序通知消息只允许小程序应用发送，消息会通过【小程序通知】发送给用户。
> 小程序应用仅支持发送小程序通知消息，暂不支持文本、图片、语音、视频、图文等其他类型的消息。
> 不支持toall参数，即不支持全员发送

**请求示例：**

```
{    "touser" : ["userid1","userid2","CorpId1/userid1","CorpId2/userid2"],    "toparty" : ["partyid1","partyid2","LinkedId1/partyid1","LinkedId2/partyid2"],    "totag" : ["tagid1","tagid2"],   "msgtype" : "miniprogram_notice",   "miniprogram_notice" : {        "appid": "wx123123123123123",        "page": "pages/index?userid=zhangsan&orderid=123123123",        "title": "会议室预订成功通知",        "description": "4月27日 16:16",        "emphasis_first_item": true,        "content_item": [            {                "key": "会议室",                "value": "402"            },            {                "key": "会议地点",                "value": "广州TIT-402会议室"            },            {                "key": "会议时间",                "value": "2018年8月1日 09:00-09:30"            },            {                "key": "参与人员",                "value": "周剑轩"            }        ]    }}
```

**示例效果：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/201927-yBDznZ.png?imageslim)

**参数说明：**

| 参数                | 是否必须 | 说明                                                         |
| ------------------- | -------- | ------------------------------------------------------------ |
| touser              | 否       | 成员ID列表（消息接收者，最多支持1000个）。每个元素的格式为： corpid/userid，其中， corpid为该互联成员所属的企业，userid为该互联成员所属企业中的帐号。如果是本企业的成员，则直接传userid即可 |
| toparty             | 否       | 部门ID列表，最多支持100个。partyid在互联圈子内唯一。每个元素都是字符串类型，格式为：linked_id/party_id，其中linked_id是互联id，party_id是在互联圈子中的部门id。如果是本企业的部门，则直接传party_id即可。 |
| totag               | 否       | 本企业的标签ID列表，最多支持100个。                          |
| msgtype             | 是       | 消息类型，此时固定为：miniprogram_notice                     |
| appid               | 是       | 小程序appid，必须是与当前小程序应用关联的小程序              |
| page                | 否       | 点击消息卡片后的小程序页面，仅限本小程序内的页面。该字段不填则消息点击后不跳转。 |
| title               | 是       | 消息标题，长度限制4-12个汉字                                 |
| description         | 否       | 消息描述，长度限制4-12个汉字                                 |
| emphasis_first_item | 否       | 是否放大第一个content_item                                   |
| content_item        | 否       | 消息内容键值对，最多允许10个item                             |
| key                 | 是       | 长度10个汉字以内                                             |
| value               | 是       | 长度30个汉字以内                                             |

文档ID: 14689
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/90250

> Body 请求参数

```json
{}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "invaliduser": [
    "userid1",
    "userid2",
    "CorpId1/userid1",
    "CorpId2/userid2"
  ],
  "invalidparty": [
    "partyid1",
    "partyid2",
    "LinkedId1/partyid1",
    "LinkedId2/partyid2"
  ],
  "invalidtag": [
    "tagid1",
    "tagid2"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» invaliduser|[string]|true|none||none|
|» invalidparty|[string]|true|none||none|
|» invalidtag|[string]|true|none||none|

# 企业内部开发/消息推送/家校消息推送

## POST 发送「学校通知」

POST /cgi-bin/externalcontact/message/send

学校可以通过此接口来给家长发送不同类型的学校通知，来满足多种场景下的学校通知需求。目前支持的消息类型为文本、图片、语音、视频、文件、图文。
> 各个消息类型的具体POST格式参考以下文档。
> 支持id转译，将userid/部门id转成对应的企业通讯录内部的用户名/部门名，目前仅**文本/图文/图文（mpnews）/小程序消息**这四种消息类型的**部分字段**支持。具体支持的范围和语法，请查看附录[id转译说明](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#10167/id转译说明)。
> 支持重复消息检查，当指定 `"enable_duplicate_check": 1`开启: 表示在一定时间间隔内，同样内容（请求json）的消息，不会重复收到；时间间隔可通过`duplicate_check_interval`指定，默认`1800秒`。

**权限说明：**

- 学校管理员需要将应用配置在「家长可使用的应用」才可调用

> 如果部分接收人无权限或不存在，发送仍然执行，但会返回无效的部分（inavlid_external_user/invalid_parent_userid/invalid_student_userid/invalid_party）。

## 消息类型

### 文本消息

**请求示例：**

```
{    "to_external_user" : ["external_userid1", "external_userid2"],    "to_parent_userid": ["parent_userid1", "parent_userid2"],    "to_student_userid": ["student_userid1", "student_userid2"],    "to_party": ["partyid1", "partyid2"],    "toall" : 0,    "msgtype" : "text",    "agentid" : 1,    "text" : {        "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。"    },    "enable_id_trans": 0,    "enable_duplicate_check": 0,    "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：text                                   |
| agentid                  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| content                  | 是       | 消息内容，最长不超过2048个字节**（支持id转译）**             |
| enable_id_trans          | 否       | 表示是否开启id转译，0表示否，1表示是，默认0                  |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

### 图片消息

**请求示例：**

```
{    "to_external_user" : ["external_userid1", "external_userid2"],    "to_parent_userid": ["parent_userid1", "parent_userid2"],    "to_student_userid": ["student_userid1", "student_userid2"],    "to_party": ["partyid1", "partyid2"],    "toall" : 0,    "msgtype" : "image",    "agentid" : 1,    "image" : {        "media_id" : "MEDIA_ID"    },    "enable_duplicate_check": 0,    "duplicate_check_interval": 1800}
```

**请求参数：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：image                                  |
| agentid                  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id                 | 是       | 图片媒体文件id，可以调用上传临时素材接口获取                 |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

### 语音消息

**请求示例：**

```
{    "to_external_user" : ["external_userid1", "external_userid2"],    "to_parent_userid": ["parent_userid1", "parent_userid2"],    "to_student_userid": ["student_userid1", "student_userid2"],    "to_party": ["partyid1", "partyid2"],    "toall" : 0,    "msgtype" : "voice",    "agentid" : 1,    "voice" : {        "media_id" : "MEDIA_ID"    },    "enable_duplicate_check": 0,    "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：voice                                  |
| agentid                  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id                 | 是       | 语音文件id，可以调用上传临时素材接口获取                     |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

### 视频消息

**请求示例：**

```
{    "to_external_user" : ["external_userid1", "external_userid2"],    "to_parent_userid": ["parent_userid1", "parent_userid2"],    "to_student_userid": ["student_userid1", "student_userid2"],    "to_party": ["partyid1", "partyid2"],    "toall" : 0,    "msgtype" : "video",    "agentid" : 1,    "video" : {        "media_id" : "MEDIA_ID",        "title" : "Title",       "description" : "Description"    },    "enable_duplicate_check": 0,    "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：video                                  |
| agentid                  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id                 | 是       | 视频媒体文件id，可以调用上传临时素材接口获取                 |
| title                    | 否       | 视频消息的标题，不超过128个字节，超过会自动截断              |
| description              | 否       | 视频消息的描述，不超过512个字节，超过会自动截断              |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

### 文件消息

**请求示例：**

```
{    "to_external_user" : ["external_userid1", "external_userid2"],    "to_parent_userid": ["parent_userid1", "parent_userid2"],    "to_student_userid": ["student_userid1", "student_userid2"],    "to_party": ["partyid1", "partyid2"],    "toall" : 0,    "msgtype" : "file",    "agentid" : 1,    "file" : {        "media_id" : "1Yv-zXfHjSjU-7LH-GwtYqDGS-zz6w22KmWAT5COgP7o"    },    "enable_duplicate_check": 0,    "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：file                                   |
| agentid                  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| media_id                 | 是       | 文件id，可以调用上传临时素材接口获取                         |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

**文件消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/202300-INWhcA.jpeg?imageslim)

### 图文消息

**请求示例：**

```
{   "to_external_user" : ["external_userid1", "external_userid2"],   "to_parent_userid": ["parent_userid1", "parent_userid2"],   "to_student_userid": ["student_userid1", "student_userid2"],   "to_party": ["partyid1", "partyid2"],   "toall" : 0,   "msgtype" : "news",   "agentid" : 1,   "news" : {       "articles" : [           {               "title" : "中秋节礼品领取",               "description" : "今年中秋节公司有豪礼相送",               "url" : "URL",               "picurl" : "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"           }        ]   },   "enable_id_trans": 0,   "enable_duplicate_check": 0,   "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：news                                   |
| agentid                  | 是       | 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 [获取企业授权信息](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#10975/获取企业授权信息) 获取该参数值 |
| articles                 | 是       | 图文消息，一个图文消息支持1到8条图文                         |
| title                    | 是       | 标题，不超过128个字节，超过会自动截断**（支持id转译）**      |
| description              | 否       | 描述，不超过512个字节，超过会自动截断**（支持id转译）**      |
| url                      | 是       | 点击后跳转的链接。                                           |
| picurl                   | 否       | 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。 |
| enable_id_trans          | 否       | 表示是否开启id转译，0表示否，1表示是，默认0                  |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

**图文消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/202300-AJEoRQ.png?imageslim)

### 图文消息（mpnews）

> mpnews类型的图文消息，跟普通的图文消息一致，唯一的差异是图文内容存储在企业微信。
> 多次发送mpnews，会被认为是不同的图文，阅读、点赞的统计会被分开计算。

**请求示例：**

```
{   "to_external_user" : ["external_userid1", "external_userid2"],   "to_parent_userid": ["parent_userid1", "parent_userid2"],   "to_student_userid": ["student_userid1", "student_userid2"],   "to_party": ["partyid1", "partyid2"],   "toall" : 0,   "msgtype" : "mpnews",   "agentid" : 1,   "mpnews" : {       "articles":[           {               "title": "Title",                "thumb_media_id": "MEDIA_ID",               "author": "Author",               "content_source_url": "URL",               "content": "Content",               "digest": "Digest description"            }       ]   },   "enable_id_trans": 0,   "enable_duplicate_check": 0,   "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)，表示发给学生的所有家长（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)，表示发给班级的所有家长（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：mpnews                                 |
| agentid                  | 是       | 企业应用的id，整型。企业内部开发，可在应用的设置页面查看；第三方服务商，可通过接口 [获取企业授权信息](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#10975/获取企业授权信息) 获取该参数值 |
| articles                 | 是       | 图文消息，一个图文消息支持1到8条图文                         |
| title                    | 是       | 标题，不超过128个字节，超过会自动截断 **（支持id转译）**     |
| thumb_media_id           | 是       | 图文消息缩略图的media_id, 可以通过[素材管理](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#10112)接口获得。此处thumb_media_id即上传接口返回的media_id |
| author                   | 否       | 图文消息的作者，不超过64个字节                               |
| content_source_url       | 否       | 图文消息点击“阅读原文”之后的页面链接                         |
| content                  | 是       | 图文消息的内容，支持html标签，不超过666 K个字节 **（支持id转译）** |
| digest                   | 否       | 图文消息的描述，不超过512个字节，超过会自动截断 **（支持id转译）** |
| enable_id_trans          | 否       | 表示是否开启id转译，0表示否，1表示是，默认0                  |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

### 小程序消息

**请求示例：**

```
{   "to_external_user" : ["external_userid1", "external_userid2"],   "to_parent_userid": ["parent_userid1", "parent_userid2"],   "to_student_userid": ["student_userid1", "student_userid2"],   "to_party": ["partyid1", "partyid2"],   "toall" : 0,   "agentid" : 1,   "msgtype" : "miniprogram",   "miniprogram" : {       "appid": "APPID",       "title": "欢迎报名夏令营",       "thumb_media_id": "MEDIA_ID",       "pagepath": "PAGE_PATH"   },   "enable_id_trans": 0,   "enable_duplicate_check": 0,   "duplicate_check_interval": 1800}
```

**参数说明：**

| 参数                     | 是否必须 | 说明                                                         |
| ------------------------ | -------- | ------------------------------------------------------------ |
| to_external_user         | 否       | [已关注「学校通知」的家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#17388)，即将废弃（最多支持1000个） |
| to_parent_userid         | 否       | [家校通讯录家长列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_student_userid        | 否       | [家校通讯录学生列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18819)（最多支持1000个） |
| to_party                 | 否       | [家校通讯录部门列表](https://open.work.weixin.qq.com/api/doc/90000/90135/91608#18840)（最多支持100个） |
| toall                    | 否       | 1表示发送给学校的所有家长，默认为0                           |
| msgtype                  | 是       | 消息类型，此时固定为：miniprogram                            |
| agentid                  | 是       | 企业应用的id，整型。可在应用的设置页面查看                   |
| appid                    | 是       | 小程序appid，必须是关联到企业的小程序应用                    |
| title                    | 否       | 小程序消息标题，最多64个字节，超过会自动截断**（支持id转译）** |
| thumb_media_id           | 是       | 小程序消息封面的mediaid，封面图建议尺寸为520*416             |
| pagepath                 | 是       | 点击消息卡片后进入的小程序页面路径                           |
| enable_id_trans          | 否       | 表示是否开启id转译，0表示否，1表示是，默认0                  |
| enable_duplicate_check   | 否       | 表示是否开启重复消息检查，0表示否，1表示是，默认0            |
| duplicate_check_interval | 否       | 表示是否重复消息检查的时间间隔，默认1800s，最大不超过4小时   |

**小程序消息展现：**
![img](https://cdn3.apifox.cn/markdown-img/202110/08/202300-GcuUPJ.png?imageslim)

文档ID: 16504
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90135/91609
https://open.work.weixin.qq.com/api/doc/90001/90143/92291
https://open.work.weixin.qq.com/api/doc/90000/90135/92321

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "invalid_external_user": [
    "external_userid1"
  ],
  "invalid_parent_userid": [
    "parent_userid1"
  ],
  "invalid_student_userid": [
    "student_userid1"
  ],
  "invalid_party": [
    "party1"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» invalid_external_user|[string]|true|none||none|
|» invalid_parent_userid|[string]|true|none||none|
|» invalid_student_userid|[string]|true|none||none|
|» invalid_party|[string]|true|none||none|

# 企业内部开发/消息推送/发送应用消息

## POST 发送应用消息

POST /cgi-bin/message/send

应用支持推送文本、图片、视频、文件、图文等类型。

> \- 各个消息类型的具体POST格式请阅后续“消息类型”部分。
> \- 如果有在管理端对应用设置“在微工作台中始终进入主页”，应用在微信端只能接收到文本消息，并且文本消息的长度限制为20字节，超过20字节会被截断。同时其他消息类型也会转换为文本消息，提示用户到企业微信查看。
> \- 支持id转译，将userid/部门id转成对应的用户名/部门名，目前仅**文本/文本卡片/图文/图文（mpnews）/任务卡片/小程序通知/模版消息/模板卡片消息**这八种消息类型的**部分字段**支持。仅第三方应用需要用到，企业自建应用可以忽略。具体支持的范围和语法，请查看附录[id转译说明](https://developer.work.weixin.qq.com/document/path/90236#10167/id转译说明)。
> \- 支持重复消息检查，当指定 `"enable_duplicate_check": 1`开启: 表示在一定时间间隔内，同样内容（请求json）的消息，不会重复收到；时间间隔可通过`duplicate_check_interval`指定，默认`1800秒`。
> \- 从2021年2月4日开始，企业关联添加的「小程序」应用，也可以发送文本、图片、视频、文件、图文等各种类型的消息了。
> **调用建议**：大部分企业应用在每小时的0分或30分触发推送消息，容易造成资源挤占，从而投递不够及时，建议尽量避开这两个时间点进行调用。

如果部分接收人无权限或不存在，发送仍然执行，但会返回无效的部分（即invaliduser或invalidparty或invalidtag或unlicenseduser），常见的原因是**接收人不在应用的可见范围内**。
权限包含**应用可见范围**和**基础接口权限**(基础账号、互通账号均可)，unlicenseduser中的用户在应用可见范围内但没有基础接口权限。
如果**全部**接收人无权限或不存在，则本次调用返回失败，errcode为81013。
返回包中的userid，不区分大小写，统一转为小写

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |access_token|
|random|query|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "invaliduser": "userid1|userid2",
  "invalidparty": "partyid1|partyid2",
  "invalidtag": "tagid1|tagid2",
  "unlicenseduser": "userid3|userid4",
  "msgid": "xxxx",
  "response_code": "xyzxyz"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||对返回码的文本描述内容|
|» invaliduser|string|true|none||不合法的userid，不区分大小写，统一转为小写|
|» invalidparty|string|true|none||不合法的partyid|
|» invalidtag|string|true|none||不合法的标签id|
|» unlicenseduser|string|true|none||没有基础接口许可(包含已过期)的userid|
|» msgid|string|true|none||消息id，用于[撤回应用消息](https://developer.work.weixin.qq.com/document/path/90236#31947)|
|» response_code|string|true|none||仅消息类型为“按钮交互型”，“投票选择型”和“多项选择型”的模板卡片消息返回，应用可使用response_code调用[更新模版卡片消息](https://developer.work.weixin.qq.com/document/path/90236#32086)接口，72小时内有效，且只能使用一次|

# 企业内部开发/素材管理

## POST 上传图片

POST /cgi-bin/media/uploadimg

> 上传图片得到图片URL，该URL永久有效
> 返回的图片URL，仅能用于[图文消息](https://open.work.weixin.qq.com/api/doc/90000/90135/90256#10167/图文消息)正文中的图片展示，或者给客户发送欢迎语等；若用于非企业微信环境下的页面，图片将被屏蔽。
> 每个企业每天最多可上传100张图片

使用multipart/form-data POST上传文件。

POST的请求包中，form-data中媒体文件标识，应包含有filename、content-type等信息

**请求示例：**

```
---------------------------acebdf13572468Content-Disposition: form-data; name="fieldNameHere"; filename="20180103195745.png"Content-Type: image/pngContent-Length: 220<@INCLUDE *C:\Users\abelzhu\Pictures\企业微信截图_20180103195745.png*@>---------------------------acebdf13572468--
```

上传的图片大小限制:图片文件大小应在 5B ~ 2MB 之间
文档ID: 13219
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90256
https://open.work.weixin.qq.com/api/doc/90001/90392
https://open.work.weixin.qq.com/api/doc/90002/90874

> Body 请求参数

```yaml
name: string
filename: string

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» name|body|string| 否 |none|
|» filename|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "",
  "url": "http://p.qpic.cn/pic_wework/3474110808/7a7c8471673ff0f178f63447935d35a5c1247a7f31d9c060/0"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» url|string|true|none||上传后得到的图片URL。永久有效|

## POST 上传临时素材

POST /cgi-bin/media/upload

素材上传得到media_id，该media_id仅三天内有效
media_id在同一企业内应用之间可以共享

POST的请求包中，form-data中媒体文件标识，应包含有 **filename**、filelength、content-type等信息

> filename标识文件展示的名称。比如，使用该media_id发消息时，展示的文件名由该字段控制

**请求示例：**

```json
POST https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=accesstoken001&type=file HTTP/1.1Content-Type: multipart/form-data; boundary=-------------------------acebdf13572468Content-Length: 220---------------------------acebdf13572468Content-Disposition: form-data; name="media";filename="wework.txt"; filelength=6Content-Type: application/octet-streammytext---------------------------acebdf13572468--
```

上传的媒体文件限制
所有文件size必须大于5个字节

图片（image）：2MB，支持JPG,PNG格式
语音（voice） ：2MB，播放长度不超过60s，仅支持AMR格式
视频（video） ：10MB，支持MP4格式
普通文件（file）：20MB

文档ID: 10112
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90253
https://open.work.weixin.qq.com/api/doc/90001/90389
https://open.work.weixin.qq.com/api/doc/90002/90871

> Body 请求参数

```yaml
filename: filename标识文件展示的名称。比如，使用该media_id发消息时，展示的文件名由该字段控制
filelength: string
name: 使用multipart/form-data POST上传文件， 文件标识名为”media”

```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|type|query|string| 是 |媒体文件类型，分别有图片（image）、视频（video）、普通文件（file）|
|body|body|object| 否 |none|
|» filename|body|string| 是 |none|
|» filelength|body|string| 是 |none|
|» name|body|string| 是 |none|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "",
  "type": "image",
  "media_id": "1G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0",
  "created_at": "1380000000"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» type|string|true|none||媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件(file)|
|» media_id|string|true|none||媒体文件上传后获取的唯一标识，3天内有效|
|» created_at|string|true|none||媒体文件上传时间戳|

## GET 获取临时素材

GET /cgi-bin/media/get

**权限说明：**
完全公开，media_id在同一企业内所有应用之间可以共享。

**返回说明 ：**
正确时返回（和普通的http下载相同，请根据http头做相应的处理）：

```
   HTTP/1.1 200 OK   Connection: close   Content-Type: image/jpeg    Content-disposition: attachment; filename="MEDIA_ID.jpg"   Date: Sun, 06 Jan 2013 10:20:18 GMT   Cache-Control: no-cache, must-revalidate   Content-Length: 339721   Xxxx
```

错误时返回（这里省略了HTTP首部）：

```
{   "errcode": 40007,   "errmsg": "invalid media_id"}
```

## 附注：支持断点下载（分块下载）

本接口支持通过在http header里指定`Range`来分块下载。
在文件很大，可能下载超时的情况下，推荐使用分块下载。
以curl命令进行测试为例，假如我有一个2048字节的文件，
下面是获取文件前1024字节：

> curl ‘https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID’ -i -H “Range: bytes=0-1023”

生成如下http请求：

> GET /cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID HTTP/1.1
> Host: qyapi.weixin.qq.com
> Range: bytes 0-1023

服务器端会返回状态码为 206 Partial Content 的响应：

> HTTP/1.1 206 Partial Content
> Accept-Ranges: bytes
> Content-Range: bytes 0-1023/2048
> Content-Length: 1024
> …
> (1024 Bites binary content)

可以看到响应中有如下特点：

- 状态码是`206 Partial Content`，而非`200 ok`
- 返回的header中，`Accept-Ranges`首部表示可用于定义范围的单位
- 返回的header中，`Content-Range`首部表示这一部分内容在整个资源中所处的位置

更多协议详情参考[RFC: Range Requests](https://tools.ietf.org/html/rfc7233#section-4.1)

文档ID: 10115
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90254
https://open.work.weixin.qq.com/api/doc/90001/90390
https://open.work.weixin.qq.com/api/doc/90002/90872

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|media_id|query|string| 否 |媒体文件id, 见上传临时素材|
|access_token|query|string| 否 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 40007,
  "errmsg": "invalid media_id"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

## GET 获取高清语音素材

GET /cgi-bin/media/get/jssdk

可以使用本接口获取从JSSDK的uploadVoice接口上传的临时语音素材，格式为speex，16K采样率。该音频比上文的临时素材获取接口（格式为amr，8K采样率）更加清晰，适合用作语音识别等对音质要求较高的业务。

> 仅企业微信2.4及以上版本支持。

**权限说明：**
完全公开，media_id在同一企业内所有应用之间可以共享。

**返回说明 ：**
正确时返回（和普通的http下载相同，请根据http头做相应的处理）：

```
   HTTP/1.1 200 OK   Connection: close   Content-Type: voice/speex    Content-disposition: attachment; filename="XXX"   Date: Sun, 06 Jan 2013 10:20:18 GMT   Cache-Control: no-cache, must-revalidate   Content-Length: 339721   Xxxx
```

错误时返回（这里省略了HTTP首部）：

```
{   "errcode": 40007,   "errmsg": "invalid media_id"}
```
文档ID: 12250
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/90255
https://open.work.weixin.qq.com/api/doc/90001/90391
https://open.work.weixin.qq.com/api/doc/90002/90873

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|media_id|query|string| 是 |通过JSSDK的uploadVoice接口上传的语音文件id|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 40007,
  "errmsg": "invalid media_id"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

# 企业内部开发/素材管理/异步上传临时素材

## POST 生成异步上传任务

POST /cgi-bin/media/upload_by_url

## 概述

为了满足临时素材的大文件诉求（最高支持200M），支持指定文件的CDN链接（必须支持`Range`分块下载），由企微微信后台异步下载和处理，处理完成后回调通知任务完成，再通过接口主动查询任务结果。

跟普通临时素材一样，media_id仅三天内有效，media_id在同一企业内应用之间可以共享。

## 使用场景说明

跟[上传临时素材](https://developer.work.weixin.qq.com/document/path/96219#10112)拿到的media_id使用场景是**不通用的**，目前适配的接口如下：

| 接口                                                         | 适用场景值(scene) | 说明                                                         |
| ------------------------------------------------------------ | ----------------- | ------------------------------------------------------------ |
| [获取临时素材](https://developer.work.weixin.qq.com/document/path/96219#10115) | 所有              | 若文件大小超过`20M`，必须使用`Range`分块下载且分块大小不超过`20M`，否则返回错误`830002` |
| [入群欢迎语素材管理](https://developer.work.weixin.qq.com/document/path/96219#19635) | `1`               | [添加素材](https://developer.work.weixin.qq.com/document/path/96219#19635/添加入群欢迎语素材)、[编辑素材](https://developer.work.weixin.qq.com/document/path/96219#19635/编辑入群欢迎语素材)兼容`video`和`file`两种素材类型使用； [获取素材](https://developer.work.weixin.qq.com/document/path/96219#19635/获取入群欢迎语素材)返回的media_id类型则跟添加/编辑时的media_id类型对应 |

### **上传的媒体文件限制**

所有文件size必须大于5个字节

- 图片（image）：暂不支持
- 语音（voice） ：暂不支持
- 视频（video） ：**200MB**，仅支持MP4格式
- 普通文件（file）：**200MB**

> Body 请求参数

```json
{
  "scene": 1,
  "type": "video",
  "filename": "video.mp4",
  "url": "https://xxxx",
  "md5": "MD5"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» scene|body|integer| 是 |场景值。1-客户联系入群欢迎语素材（目前仅支持1）。 **注意：每个场景值有对应的使用范围，详见上面的「使用场景说明」**|
|» type|body|string| 是 |媒体文件类型。**目前仅支持`video`-视频，`file`-普通文件** 不超过32字节。|
|» filename|body|string| 是 |文件名，标识文件展示的名称。比如，使用该media_id发消息时，展示的文件名由该字段控制。 不超过128字节。|
|» url|body|string| 是 |文件cdn url。url要求支持[Range](https://developer.work.weixin.qq.com/document/path/96219#https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Range)分块下载 不超过1024字节。|
|» md5|body|string| 是 |文件md5。对比从url下载下来的文件md5是否一致。 不超过32字节。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "jobid": "jobid"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||none|
|» errmsg|string|true|none||none|
|» jobid|string|true|none||任务id。可通过此jobid查询结果|

## POST 查询异步任务结果

POST /cgi-bin/media/get_upload_by_url_result

**权限说明：**

- 客户联系权限

### 任务结果常见错误码列表（`detail.errcode`）

| 错误码 | 错误说明         | 排查方法                                          |
| ------ | ---------------- | ------------------------------------------------- |
| 830001 | url非法          | 确认url是否支持`Range`分块下载                    |
| 830003 | url下载数据失败  | 确认url本身是否能正常访问                         |
| 45001  | 文件大小超过限制 | 确认文件在5字节~200M范围内                        |
| 301019 | 文件MD5不匹配    | 确认url对应的文件内容md5，跟所填的md5参数是否一致 |

> Body 请求参数

```json
{
  "jobid": "JOBID"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 否 |调用接口凭证|
|body|body|object| 否 |none|
|» jobid|body|string| 是 |任务id。最长为128字节，**60分钟内有效**|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "status": 2,
  "detail": {
    "errcode": 0,
    "errmsg": "ok",
    "media_id": "3*1*G6nrLmr5EC3MMb_-zK1dDdzmd0p7cNliYu9V5w7o8K0",
    "created_at": "1380000000"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer|true|none||返回码|
|» errmsg|string|true|none||错误码描述|
|» status|integer|true|none||任务状态。1-处理中，2-完成，3-异常失败|
|» detail|object|true|none||结果明细|
|»» errcode|integer|true|none||任务失败返回码。当status为3时返回非0，其他返回0|
|»» errmsg|string|true|none||任务失败错误码描述|
|»» media_id|string|true|none||媒体文件上传后获取的唯一标识，3天内有效。当status为2时返回。|
|»» created_at|string|true|none||媒体文件创建的时间戳。当status为2时返回。|

# 企业内部开发/会话内容存档

## POST 获取会话内容存档内部群信息

POST /cgi-bin/msgaudit/groupchat/get

企业可通过此接口，获取会话内容存档本企业的内部群信息，包括群名称、群主id、公告、群创建时间以及所有群成员的id与加入时间。

此接口可以查询roomid对应的群信息，roomid可以从会话内容存档中获取到的roomid填充。只支持内部群。

权限说明：
企业需要使用会话内容存档应用secret所获取的accesstoken来调用（accesstoken如何获取？）；

**错误说明：**

| 返回码 | 说明           |
| ------ | -------------- |
| 301052 | 会话存档已过期 |

文档ID: 23100
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/92951

> Body 请求参数

```json
{
  "roomid": "wrNplhCgAAIVZohLe57zKnvIV7xBKrig"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» roomid|body|string| 是 |待查询的群id|

> 返回示例

> 成功

```json
{
  "roomname": "蓦然回首",
  "creator": "ZhangWenChao",
  "room_create_time": 1592361604,
  "notice": "",
  "members": [
    {
      "memberid": "ZhangWenChao",
      "jointime": 1592361605
    },
    {
      "memberid": "xujinsheng",
      "jointime": 1592377076
    }
  ],
  "errcode": 0,
  "errmsg": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||none|
|» roomname|string|false|none||roomid对应的群名称|
|» room_create_time|integer(int32)|false|none||roomid对应的群创建时间|
|» creator|string|false|none||roomid对应的群创建者，userid|
|» members|[object]|false|none||roomid对应的群成员列表|
|»» jointime|integer(int32)|false|none||roomid群成员的入群时间|
|»» memberid|string|false|none||roomid群成员的id，userid|
|» errmsg|string|false|none||none|
|» notice|string|false|none||roomid对应的群公告|

## POST 获取会话同意情况 单聊请求

POST /cgi-bin/msgaudit/check_single_agree

企业可通过下述接口，获取会话中外部成员的同意情况

此接口可以批量查询userid与externalopenid之间的会话同意情况。

备注说明：
目前一次请求只支持最多100个查询条目，超过此限制的请求会被拦截，请调用方减少单次请求的查询个数。

权限说明：
企业需要使用会话内容存档应用secret所获取的accesstoken来调用（accesstoken如何获取？）；
权限说明：
企业需要使用会话内容存档应用secret所获取的accesstoken来调用（accesstoken如何获取？）；

文档ID: 17367
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/91782

> Body 请求参数

```json
{
  "info": [
    {
      "userid": "XuJinSheng",
      "exteranalopenid": "wmeDKaCQAAGd9oGiQWxVsAKwV2HxNAAA"
    },
    {
      "userid": "XuJinSheng",
      "exteranalopenid": "wmeDKaCQAAIQ_p7ACn_jpLVBJSGocAAA"
    },
    {
      "userid": "XuJinSheng",
      "exteranalopenid": "wmeDKaCQAAPE_p7ABnxkpLBBJSGocAAA"
    }
  ]
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» info|body|[object]| 是 |待查询的会话信息，数组|
|»» userid|body|string| 是 |内部成员的userid|
|»» exteranalopenid|body|string| 是 |外部成员的exteranalopenid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "agreeinfo": [
    {
      "status_change_time": 1562766651,
      "userid": "XuJinSheng",
      "exteranalopenid": "wmeDKaCPAAGdvxciQWxVsAKwV2HxNAAA",
      "agree_status": "Agree"
    },
    {
      "status_change_time": 1562766651,
      "userid": "XuJinSheng",
      "exteranalopenid": "wmeDKaCQAAIQ_p7ACnxksfeBJSGocAAA",
      "agree_status": "Disagree"
    },
    {
      "status_change_time": 1562766651,
      "userid": "XuJinSheng",
      "exteranalopenid": "wmeDKaCwAAIQ_p7ACnxckLBBJSGocAAA",
      "agree_status": "Agree"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» agreeinfo|[object]|false|none||同意情况|
|»» status_change_time|integer(int32)|false|none||同意状态改变的具体时间，utc时间|
|»» agree_status|string|false|none||同意:”Agree”，不同意:”Disagree”|
|»» exteranalopenid|string|false|none||群内外部联系人的externalopenid|
|»» userid|string|true|none||none|

## POST 获取会话同意情况 群聊请求

POST /cgi-bin/msgaudit/check_room_agree

此接口可以查询对应roomid里面所有外企业的外部联系人的同意情况

权限说明：
企业需要使用会话内容存档应用secret所获取的accesstoken来调用（accesstoken如何获取？）；

文档ID: 17367
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/91782

> Body 请求参数

```json
{
  "roomid": "wrjc7bDwAASxc8tZvBErFE02BtPWyAAA"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» roomid|body|string| 是 |待查询的roomid|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "agreeinfo": [
    {
      "status_change_time": 1562766651,
      "exteranalopenid": "wmeDKaCQAAGdtHdiQWxVadfwV2HxNAAA",
      "agree_status": "Agree"
    },
    {
      "status_change_time": 1562766651,
      "exteranalopenid": "wmeDKaCQAAIQ_p9ACyiopLBBJSGocAAA",
      "agree_status": "Disagree"
    },
    {
      "status_change_time": 1562766651,
      "exteranalopenid": "wmeDKaCQAAIQ_p9ACnxacyBBJSGocAAA",
      "agree_status": "Agree"
    }
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» agreeinfo|[object]|false|none||同意情况|
|»» status_change_time|integer(int32)|false|none||同意状态改变的具体时间，utc时间|
|»» agree_status|string|false|none||同意:”Agree”，不同意:”Disagree”|
|»» exteranalopenid|string|false|none||群内外部联系人的externalopenid|

## POST 获取会话内容存档开启成员列表

POST /cgi-bin/msgaudit/get_permit_user_list

企业可通过此接口，获取企业开启会话内容存档的成员列表

权限说明：企业需要使用会话内容存档应用secret所获取的accesstoken来调用（accesstoken如何获取？）

注：开启范围可设置为具体成员、部门、标签。通过此接口拉取成员列表，会将部门、标签进行打散处理，获取部门、标签范围内的全部成员。最终以成员userid的形式返回。

文档ID: 16547
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/91614

> Body 请求参数

```json
{
  "type": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|access_token|query|string| 是 |调用接口凭证|
|body|body|object| 否 |none|
|» type|body|integer(int32)| 否 |拉取对应版本的开启成员列表。1表示办公版；2表示服务版；3表示企业版。非必填，不填写的时候返回全量成员列表。|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "ids": [
    "userid_111",
    "userid_222",
    "userid_333"
  ]
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» errmsg|string|false|none||对返回码的文本描述内容|
|» ids|[string]|false|none||设置在开启范围内的成员的userid列表|

# 企业内部开发/会话内容存档/获取会话内容

## GET 获取会话内容 获取机器人信息

GET /cgi-bin/msgaudit/get_robot_info

通过robot_id获取机器人的名称和创建者
权限说明：只能通过会话存档的access_token获取。

文档ID: 17312
 原文档地址：
https://open.work.weixin.qq.com/api/doc/90000/91774

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|robot_id|query|string| 是 |机器人ID。|
|access_token|query|string| 是 |调用接口凭证|

> 返回示例

> 成功

```json
{
  "errcode": 0,
  "errmsg": "ok",
  "data": {
    "robot_id": "wbxxxxxxxxxxxxxxxxxxxxxxxx",
    "name": "机器人A",
    "creator_userid": "zhangsan"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» errcode|integer(int32)|false|none||返回码|
|» data|object|false|none||none|
|»» robot_id|string|false|none||机器人ID人ID|
|»» name|string|false|none||机器人名称机器人名称|
|»» creator_userid|string|false|none||机器人创建者的UserID人创建者的UserID|
|» errmsg|string|false|none||对返回码的文本描述内容|

# 数据模型

<h2 id="tocS_事件消息">事件消息</h2>

<a id="schema事件消息"></a>
<a id="schema_事件消息"></a>
<a id="tocS事件消息"></a>
<a id="tocs事件消息"></a>

```json
{
  "msgtype": "string",
  "event": {
    "event_type": "string",
    "open_kfid": "string",
    "external_userid": "string",
    "scene": "string",
    "scene_param": "string",
    "welcome_code": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：event|
|event|object|true|none||事件消息|
|» event_type|string|true|none||事件类型。此处固定为：enter_session|
|» open_kfid|string|true|none||客服账号ID|
|» external_userid|string|true|none||客户UserID|
|» scene|string|true|none||进入会话的场景值，获取客服帐号链接开发者自定义的场景值|
|» scene_param|string|true|none||进入会话的自定义参数，获取客服帐号链接返回的url，开发者按规范拼接的scene_param参数|
|» welcome_code|string|true|none||如果满足发送欢迎语条件（条件为：用户在过去48小时里未收过欢迎语，且未向客服发过消息），会返回该字段。 可用该welcome_code调用发送事件响应消息接口给客户发送欢迎语。|

<h2 id="tocS_文件消息">文件消息</h2>

<a id="schema文件消息"></a>
<a id="schema_文件消息"></a>
<a id="tocS文件消息"></a>
<a id="tocs文件消息"></a>

```json
{
  "msgtype": "string",
  "file": {
    "media_id": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：file|
|file|object|true|none||文件消息|
|» media_id|string|true|none||文件id|

<h2 id="tocS_图片消息">图片消息</h2>

<a id="schema图片消息"></a>
<a id="schema_图片消息"></a>
<a id="tocS图片消息"></a>
<a id="tocs图片消息"></a>

```json
{
  "msgtype": "string",
  "image": {
    "media_id": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：image|
|image|object|true|none||图片消息文本消息|
|» media_id|string|true|none||客户点击菜单消息，触发的回复消息中附带的菜单ID图片文件id|

<h2 id="tocS_link">link</h2>

<a id="schemalink"></a>
<a id="schema_link"></a>
<a id="tocSlink"></a>
<a id="tocslink"></a>

```json
{
  "picurl": "string",
  "title": "string",
  "url": "string",
  "desc": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|picurl|string|false|none||图文消息封面的url	图文消息封面的url|
|title|string|true|none||图文消息标题，最长为128字节|
|url|string|true|none||图文消息的链接|
|desc|string|false|none||图文消息的描述，最长为512字节|

<h2 id="tocS_位置消息">位置消息</h2>

<a id="schema位置消息"></a>
<a id="schema_位置消息"></a>
<a id="tocS位置消息"></a>
<a id="tocs位置消息"></a>

```json
{
  "msgtype": "string",
  "location": {
    "latitude": 0,
    "longitude": 0,
    "name": "string",
    "address": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：location|
|location|object|true|none||地理位置消息|
|» latitude|number|true|none||纬度|
|» longitude|number|true|none||经度|
|» name|string|true|none||位置名|
|» address|string|true|none||地址详情说明|

<h2 id="tocS_名片消息">名片消息</h2>

<a id="schema名片消息"></a>
<a id="schema_名片消息"></a>
<a id="tocS名片消息"></a>
<a id="tocs名片消息"></a>

```json
{
  "msgtype": "string",
  "business_card": {
    "userid": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：business_card|
|business_card|object|true|none||名片消息|
|» userid|string|true|none||名片userid|

<h2 id="tocS_消息发送失败事件">消息发送失败事件</h2>

<a id="schema消息发送失败事件"></a>
<a id="schema_消息发送失败事件"></a>
<a id="tocS消息发送失败事件"></a>
<a id="tocs消息发送失败事件"></a>

```json
{
  "msgtype": "string",
  "event": {
    "event_type": "string",
    "open_kfid": "string",
    "external_userid": "string",
    "fail_msgid": "string",
    "fail_type": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：event|
|event|object|true|none||事件消息|
|» event_type|string|true|none||事件类型。此处固定为：msg_send_fail|
|» open_kfid|string|true|none||客服账号ID|
|» external_userid|string|true|none||客户UserID|
|» fail_msgid|string|true|none||发送失败的消息msgid|
|» fail_type|string|true|none||失败类型。0-未知原因 1-客服账号已删除 2-应用已关闭 4-会话已过期，超过48小时 5-会话已关闭 6-超过5条限制 7-未绑定视频号 8-主体未验证 9-未绑定视频号且主体未验证 10-用户拒收|

<h2 id="tocS_miniprogram">miniprogram</h2>

<a id="schemaminiprogram"></a>
<a id="schema_miniprogram"></a>
<a id="tocSminiprogram"></a>
<a id="tocsminiprogram"></a>

```json
{
  "appid": "string",
  "pic_media_id": "string",
  "page": "string",
  "title": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|appid|string|true|none||小程序appid，必须是关联到企业的小程序应用|
|pic_media_id|string|true|none||小程序消息封面的mediaid，封面图建议尺寸为520*416|
|page|string|true|none||小程序page路径|
|title|string|true|none||小程序消息标题，最长为64字节|

<h2 id="tocS_客服人员接待状态变更事件">客服人员接待状态变更事件</h2>

<a id="schema客服人员接待状态变更事件"></a>
<a id="schema_客服人员接待状态变更事件"></a>
<a id="tocS客服人员接待状态变更事件"></a>
<a id="tocs客服人员接待状态变更事件"></a>

```json
{
  "msgtype": "string",
  "event": {
    "event_type": "string",
    "servicer_userid": "string",
    "status": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：event|
|event|object|true|none||事件消息|
|» event_type|string|true|none||事件类型。此处固定为：servicer_status_change|
|» servicer_userid|string|true|none||客服人员userid|
|» status|string|true|none||状态类型。1-接待中 2-停止接待|

<h2 id="tocS_小程序消息">小程序消息</h2>

<a id="schema小程序消息"></a>
<a id="schema_小程序消息"></a>
<a id="tocS小程序消息"></a>
<a id="tocs小程序消息"></a>

```json
{
  "msgtype": "string",
  "miniprogram": {
    "title": "string",
    "appid": "string",
    "pagepath": "string",
    "thumb_media_id": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：miniprogram|
|miniprogram|object|true|none||小程序消息|
|» title|string|true|none||标题|
|» appid|string|true|none||小程序appid|
|» pagepath|string|true|none||点击消息卡片后进入的小程序页面路径|
|» thumb_media_id|string|true|none||小程序消息封面的mediaid|

<h2 id="tocS_group_msg_list">group_msg_list</h2>

<a id="schemagroup_msg_list"></a>
<a id="schema_group_msg_list"></a>
<a id="tocSgroup_msg_list"></a>
<a id="tocsgroup_msg_list"></a>

```json
{
  "group_msg_list": [
    {
      "msgid": "string",
      "creator": "string",
      "create_time": "string",
      "create_type": 0,
      "text": {
        "content": "string"
      },
      "attachments": [
        {
          "msgtype": "string",
          "image": {
            "media_id": null,
            "pic_url": null
          },
          "link": {
            "title": null,
            "picurl": null,
            "desc": null,
            "url": null
          },
          "miniprogram": {
            "title": null,
            "pic_media_id": null,
            "appid": null,
            "page": null
          },
          "video": {
            "media_id": null
          },
          "file": {
            "media_id": null
          }
        }
      ]
    }
  ]
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|group_msg_list|[object]|true|none||none|
|» msgid|string|false|none||none|
|» creator|string|false|none||none|
|» create_time|string|false|none||none|
|» create_type|integer|false|none||none|
|» text|object|false|none||none|
|»» content|string|true|none||none|
|» attachments|[object]|false|none||none|
|»» msgtype|string|true|none||none|
|»» image|object|false|none||none|
|»»» media_id|string|true|none||none|
|»»» pic_url|string|true|none||none|
|»» link|object|false|none||none|
|»»» title|string|true|none||none|
|»»» picurl|string|true|none||none|
|»»» desc|string|true|none||none|
|»»» url|string|true|none||none|
|»» miniprogram|object|false|none||none|
|»»» title|string|true|none||none|
|»»» pic_media_id|string|true|none||none|
|»»» appid|string|true|none||none|
|»»» page|string|true|none||none|
|»» video|object|false|none||none|
|»»» media_id|string|true|none||none|
|»» file|object|false|none||none|
|»»» media_id|string|true|none||none|

<h2 id="tocS_文本消息">文本消息</h2>

<a id="schema文本消息"></a>
<a id="schema_文本消息"></a>
<a id="tocS文本消息"></a>
<a id="tocs文本消息"></a>

```json
{
  "msgtype": "string",
  "text": {
    "content": "string",
    "menu_id": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：text|
|text|object|true|none||文本消息|
|» content|string|true|none||文本内容|
|» menu_id|string|true|none||客户点击菜单消息，触发的回复消息中附带的菜单ID|

<h2 id="tocS_privilege">privilege</h2>

<a id="schemaprivilege"></a>
<a id="schema_privilege"></a>
<a id="tocSprivilege"></a>
<a id="tocsprivilege"></a>

```json
{
  "contact_me": true,
  "send_customer_msg": true,
  "export_customer_list": true,
  "view_customer_data": true,
  "onjob_customer_transfer": true,
  "export_customer_group_list": true,
  "view_room_data": true,
  "oper_resign_customer": true,
  "export_customer_data": true,
  "view_room_list": true,
  "oper_resign_group": true,
  "room_deduplication": true,
  "send_group_msg": true,
  "view_behavior_data": true,
  "manage_customer_tag": true,
  "join_room": true,
  "share_customer": true,
  "edit_anti_spam_rule": true,
  "rapid_reply": true,
  "view_customer_list": true,
  "edit_welcome_msg": true
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|contact_me|boolean|false|none||可使用联系我，基础权限，不可取消|
|send_customer_msg|boolean|false|none||允许给企业客户发送消息，默认为true|
|export_customer_list|boolean|false|none||导出客户列表，默认为true|
|view_customer_data|boolean|false|none||导出成员客户统计，默认为true|
|onjob_customer_transfer|boolean|false|none||转接在职成员的客户，默认为true|
|export_customer_group_list|boolean|false|none||导出客户群列表，默认为true|
|view_room_data|boolean|false|none||允许查看群聊数据统计，默认为true|
|oper_resign_customer|boolean|false|none||允许分配离职成员客户，默认为true|
|export_customer_data|boolean|false|none||导出成员客户统计，默认为true|
|view_room_list|boolean|false|none||查看群聊列表，基础权限，不可取消|
|oper_resign_group|boolean|false|none||允许分配离职成员客户群，默认为true|
|room_deduplication|boolean|false|none||允许对企业客户群进行去重，默认为true|
|send_group_msg|boolean|false|none||允许发送消息到企业的客户群，默认为true|
|view_behavior_data|boolean|false|none||允许查看成员联系客户统计|
|manage_customer_tag|boolean|false|none||配置企业客户标签，默认为true|
|join_room|boolean|false|none||可加入群聊，基础权限，不可取消|
|share_customer|boolean|false|none||允许分享客户给其他成员，默认为true|
|edit_anti_spam_rule|boolean|false|none||编辑企业成员防骚扰规则，默认为true|
|rapid_reply|boolean|false|none||配置快捷回复，默认为true|
|view_customer_list|boolean|false|none||查看客户列表，基础权限，不可取消|
|edit_welcome_msg|boolean|false|none||允许配置欢迎语，默认为true|

<h2 id="tocS_video">video</h2>

<a id="schemavideo"></a>
<a id="schema_video"></a>
<a id="tocSvideo"></a>
<a id="tocsvideo"></a>

```json
{
  "media_id": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|media_id|string|true|none||视频媒体文件id，可以通过素材管理接口获得|

<h2 id="tocS_语音消息">语音消息</h2>

<a id="schema语音消息"></a>
<a id="schema_语音消息"></a>
<a id="tocS语音消息"></a>
<a id="tocs语音消息"></a>

```json
{
  "msgtype": "string",
  "voice": {
    "media_id": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：voice消息类型，此时固定为：voice|
|voice|object|true|none||语音消息|
|» media_id|string|true|none||语音文件ID|

<h2 id="tocS_链接消息">链接消息</h2>

<a id="schema链接消息"></a>
<a id="schema_链接消息"></a>
<a id="tocS链接消息"></a>
<a id="tocs链接消息"></a>

```json
{
  "msgtype": "string",
  "link": {
    "title": "string",
    "desc": "string",
    "url": "string",
    "pic_url": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：link|
|link|object|true|none||链接消息|
|» title|string|true|none||标题|
|» desc|string|true|none||描述|
|» url|string|true|none||点击后跳转的链接|
|» pic_url|string|true|none||缩略图链接|

<h2 id="tocS_image">image</h2>

<a id="schemaimage"></a>
<a id="schema_image"></a>
<a id="tocSimage"></a>
<a id="tocsimage"></a>

```json
{
  "media_id": "string",
  "pic_url": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|media_id|string|false|none||图片的media_id，可以通过素材管理接口获得|
|pic_url|string|false|none||图片的链接，仅可使用上传图片接口得到的链接|

<h2 id="tocS_text">text</h2>

<a id="schematext"></a>
<a id="schema_text"></a>
<a id="tocStext"></a>
<a id="tocstext"></a>

```json
{
  "media_id": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|media_id|string|true|none||文件id，可以通过素材管理接口获得|

<h2 id="tocS_file">file</h2>

<a id="schemafile"></a>
<a id="schema_file"></a>
<a id="tocSfile"></a>
<a id="tocsfile"></a>

```json
{
  "media_id": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|media_id|string|true|none||文件id，可以通过素材管理接口获得|

<h2 id="tocS_conclusions">conclusions</h2>

<a id="schemaconclusions"></a>
<a id="schema_conclusions"></a>
<a id="tocSconclusions"></a>
<a id="tocsconclusions"></a>

```json
{
  "text": {
    "content": "string"
  },
  "image": {
    "pic_url": "string"
  },
  "link": {
    "title": "string",
    "picurl": "string",
    "desc": "string",
    "url": "string"
  },
  "miniprogram": {
    "title": "string",
    "pic_media_id": "string",
    "appid": "string",
    "page": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|text|object|true|none||none|
|» content|string|true|none||消息文本内容,最长为4000字节|
|image|object|true|none||none|
|» pic_url|string|true|none||图片的url|
|link|object|true|none||none|
|» title|string|true|none||图文消息标题，最长为128字节|
|» picurl|string|true|none||图文消息封面的url|
|» desc|string|true|none||图文消息的描述，最长为512字节|
|» url|string|true|none||图文消息的链接|
|miniprogram|object|true|none||none|
|» title|string|true|none||小程序消息标题，最长为64字节|
|» pic_media_id|string|true|none||小程序消息封面的mediaid，封面图建议尺寸为520*416|
|» appid|string|true|none||小程序appid，必须是关联到企业的小程序应用|
|» page|string|true|none||小程序page路径|

<h2 id="tocS_菜单消息">菜单消息</h2>

<a id="schema菜单消息"></a>
<a id="schema_菜单消息"></a>
<a id="tocS菜单消息"></a>
<a id="tocs菜单消息"></a>

```json
{
  "msgtype": "string",
  "msgmenu": {
    "head_content": "string",
    "list": [
      {
        "type": "string",
        "click": {
          "id": "string",
          "content": "string"
        },
        "view": {
          "url": "string",
          "content": "string"
        },
        "miniprogram": {
          "appid": "string",
          "pagepath": "string",
          "content": "string"
        }
      }
    ],
    "tail_content": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：msgmenu|
|msgmenu|object|true|none||菜单消息|
|» head_content|string|true|none||起始文本|
|» list|[object]|true|none||菜单项配置|
|»» type|string|true|none||菜单类型。 click-回复菜单 view-超链接菜单 miniprogram-小程序菜单|
|»» click|object|true|none||type为click的菜单项|
|»»» id|string|true|none||菜单ID|
|»»» content|string|true|none||菜单显示内容|
|»» view|object|false|none||type为view的菜单项|
|»»» url|string|true|none||点击后跳转的链接|
|»»» content|string|true|none||菜单显示内容|
|»» miniprogram|object|false|none||none|
|»»» appid|string|true|none||小程序appid|
|»»» pagepath|string|true|none||点击后进入的小程序页面|
|»»» content|string|true|none||菜单显示内容|
|» tail_content|string|true|none||结束文本|

<h2 id="tocS_会话状态变更事件">会话状态变更事件</h2>

<a id="schema会话状态变更事件"></a>
<a id="schema_会话状态变更事件"></a>
<a id="tocS会话状态变更事件"></a>
<a id="tocs会话状态变更事件"></a>

```json
{
  "msgtype": "string",
  "event": {
    "event_type": "string",
    "open_kfid": "string",
    "external_userid": "string",
    "change_type": 0,
    "old_servicer_userid": "string",
    "new_servicer_userid": "string",
    "msg_code": "string"
  }
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|msgtype|string|true|none||消息类型，此时固定为：event|
|event|object|true|none||事件消息|
|» event_type|string|true|none||事件类型。此处固定为：session_status_change|
|» open_kfid|string|true|none||客服帐号ID|
|» external_userid|string|true|none||客户UserID|
|» change_type|integer|true|none||变更类型。1-从接待池接入会话 2-转接会话 3-结束会话|
|» old_servicer_userid|string|true|none||老的客服人员userid。仅change_type为2和3有值|
|» new_servicer_userid|string|true|none||新的客服人员userid。仅change_type为1和2有值|
|» msg_code|string|true|none||用于发送事件响应消息的code，仅change_type为1和3时，会返回该字段。 可用该msg_code调用发送事件响应消息接口给客户发送回复语或结束语。|

