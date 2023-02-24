### (DATABASECHANGELOG)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|ID | |varchar |null |不为空 |
|AUTHOR | |varchar |null |不为空 |
|FILENAME | |varchar |null |不为空 |
|DATEEXECUTED | |datetime |null |不为空 |
|ORDEREXECUTED | |int |null |不为空 |
|EXECTYPE | |varchar |null |不为空 |
|MD5SUM | |varchar |null |是 |
|DESCRIPTION | |varchar |null |是 |
|COMMENTS | |varchar |null |是 |
|TAG | |varchar |null |是 |
|LIQUIBASE | |varchar |null |是 |
|CONTEXTS | |varchar |null |是 |
|LABELS | |varchar |null |是 |
|DEPLOYMENT_ID | |varchar |null |是 |

### (DATABASECHANGELOGLOCK)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|ID | |int |null |不为空 |
|LOCKED | |bit |null |不为空 |
|LOCKGRANTED | |datetime |null |是 |
|LOCKEDBY | |varchar |null |是 |

### 行政区划(sys_area)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |int |null |不为空 |
|parent_id |父ID |int |null |不为空 |
|level |层级 |tinyint |0 |是 |
|name |区域名称 |varchar |null |不为空 |
|e_prefix |拼音首字母 |varchar |null |是 |
|e_name |拼音名称 |varchar |null |是 |
|ext_id |对外区域ID |bigint |null |是 |
|ext_name |区域对外名称 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 参数配置表(sys_config)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|config_id |参数主键 |int |null |不为空 |
|config_name |参数名称 |varchar | |是 |
|config_key |参数键名 |varchar | |是 |
|config_value |参数键值 |varchar | |是 |
|config_type |系统内置（Y是 N否） |char |N |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |

### 部门表(sys_dept)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|dept_id |部门id |bigint |null |不为空 |
|parent_id |父部门id |bigint |0 |是 |
|ancestors |祖级列表 |varchar | |是 |
|dept_name |部门名称 |varchar | |是 |
|dept_en_name |部门英文名称 |varchar |null |是 |
|order_num |显示顺序 |int |0 |是 |
|leader |负责人 |varchar |null |是 |
|phone |联系电话 |varchar |null |是 |
|email |邮箱 |varchar |null |是 |
|status |部门状态（0正常 1停用） |char |0 |是 |
|del_flag |删除标志（0代表存在 2代表删除） |char |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 字典数据表(sys_dict_data)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|dict_code |字典编码 |bigint |null |不为空 |
|dict_sort |字典排序 |int |0 |是 |
|dict_label |字典标签 |varchar | |是 |
|dict_value |字典键值 |varchar | |是 |
|dict_type |字典类型 |varchar | |是 |
|css_class |样式属性（其他样式扩展） |varchar |null |是 |
|list_class |表格回显样式 |varchar |null |是 |
|is_default |是否默认（Y是 N否） |char |N |是 |
|status |状态（0正常 1停用） |char |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |

### 字典类型表(sys_dict_type)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|dict_id |字典主键 |bigint |null |不为空 |
|dict_name |字典名称 |varchar | |是 |
|dict_type |字典类型 |varchar | |是 |
|status |状态（0正常 1停用） |char |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |

### (sys_dim_date)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |int |null |不为空 |
|date |日期 2000-01-01 |date |null |是 |
|year |年 2000 |smallint |null |是 |
|month |月 01 |smallint |null |是 |
|day |日 01 |smallint |null |是 |
|quarter |当年所属季度 |smallint |null |是 |
|year_of_week |当前周所属年份 |smallint |null |是 |
|week |当年所属周 |smallint |null |是 |
|day_of_week |星期几 |smallint |null |是 |

### 系统访问记录(sys_logininfor)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|info_id |访问ID |bigint |null |不为空 |
|user_name |用户账号 |varchar | |是 |
|ipaddr |登录IP地址 |varchar | |是 |
|login_location |登录地点 |varchar | |是 |
|browser |浏览器类型 |varchar | |是 |
|os |操作系统 |varchar | |是 |
|status |登录状态（0成功 1失败） |char |0 |是 |
|msg |提示消息 |varchar | |是 |
|login_time |访问时间 |datetime |null |是 |

### 菜单权限表(sys_menu)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|menu_id |菜单ID |bigint |null |不为空 |
|menu_name | |varchar |null |不为空 |
|parent_id |父菜单ID |bigint |0 |是 |
|order_num |显示顺序 |int |0 |是 |
|path |路由地址 |varchar | |是 |
|component |组件路径 |varchar |null |是 |
|is_frame |是否为外链（0是 1否） |int |1 |是 |
|menu_type |菜单类型（M目录 C菜单 F按钮） |char | |是 |
|visible |菜单状态（0显示 1隐藏） |char |0 |是 |
|status |菜单状态（0正常 1停用） |char |0 |是 |
|perms |权限标识 |varchar |null |是 |
|icon |菜单图标 |varchar |# |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar | |是 |

### 通知公告表(sys_notice)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|notice_id |公告ID |int |null |不为空 |
|notice_title | |varchar |null |不为空 |
|notice_type | |char |null |不为空 |
|notice_content |公告内容 |varchar |null |是 |
|status |公告状态（0正常 1关闭） |char |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |

### 操作日志记录(sys_oper_log)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|oper_id |日志主键 |bigint |null |不为空 |
|title |模块标题 |varchar | |是 |
|business_type |业务类型（0其它 1新增 2修改 3删除） |int |0 |是 |
|method |方法名称 |varchar | |是 |
|request_method |请求方式 |varchar | |是 |
|operator_type |操作类别（0其它 1后台用户 2手机端用户） |int |0 |是 |
|oper_name |操作人员 |varchar | |是 |
|dept_name |部门名称 |varchar | |是 |
|oper_url |请求URL |varchar | |是 |
|oper_ip |主机地址 |varchar | |是 |
|oper_location |操作地点 |varchar | |是 |
|oper_param |请求参数 |varchar | |是 |
|json_result |返回参数 |varchar | |是 |
|status |操作状态（0正常 1异常） |int |0 |是 |
|error_msg |错误消息 |varchar | |是 |
|oper_time |操作时间 |datetime |null |是 |

### 岗位信息表(sys_post)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|post_id |岗位ID |bigint |null |不为空 |
|post_code |岗位编码 |varchar |null |不为空 |
|post_name |岗位名称 |varchar |null |不为空 |
|post_sort |显示顺序 |int |null |不为空 |
|status |状态（0正常 1停用） |char |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |

### 角色信息表(sys_role)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|role_id |角色ID |bigint |null |不为空 |
|role_name |角色名称 |varchar |null |不为空 |
|role_key |角色权限字符串 |varchar |null |不为空 |
|role_sort |显示顺序 |int |null |不为空 |
|data_scope |数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） |char |1 |是 |
|status |角色状态（0正常 1停用） |char |null |不为空 |
|del_flag |删除标志（0代表存在 2代表删除） |char |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |
|base_role | 0 自定义角色 1 默认基础角色 |int |0 |不为空 |

### 角色和部门关联表(sys_role_dept)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|role_id |角色ID |bigint |null |不为空 |
|dept_id |部门ID |bigint |null |不为空 |

### 角色和菜单关联表(sys_role_menu)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|role_id |角色ID |bigint |null |不为空 |
|menu_id |菜单ID |bigint |null |不为空 |

### 用户信息表(sys_user)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|user_id |用户ID |bigint |null |不为空 |
|dept_id |部门ID |bigint |null |是 |
|user_name |用户账号 |varchar |null |是 |
|user_type |用户类型（00:超级管理员)(01:分级普通用户)(02:普通成员)(03:自建角色成员) |char |null |是 |
|nick_name |用户昵称 |varchar |null |是 |
|email |用户邮箱 |varchar | |是 |
|phone_number |手机号码 |varchar | |是 |
|sex |用户性别（0男 1女 2未知） |char |0 |是 |
|avatar |头像地址 |varchar | |是 |
|password |密码 |varchar | |是 |
|status |帐号状态（0正常 1停用） |char |0 |是 |
|login_ip |最后登陆IP |varchar | |是 |
|login_date |最后登陆时间 |datetime |null |是 |
|we_user_id |企业微信返回的员工id |varchar |null |不为空 |
|position |职位 |varchar |null |是 |
|thumb_avatar |头像缩略图 |varchar |null |是 |
|biz_mail |企业邮箱 |varchar |null |是 |
|leader |直属上级 |varchar |null |是 |
|telephone |座机 |varchar |null |是 |
|ext_attr |扩展属性 |varchar |null |是 |
|we_user_status |1=已激活，2=已禁用，4=未激活，5=退出企业 |char |null |是 |
|qr_code |员工二维码链接 |varchar |null |是 |
|external_profile |成员对外属性 |varchar |null |是 |
|external_position |对外职务 |varchar |null |是 |
|address |地址 |varchar |null |是 |
|open_userid |openUserId |varchar |null |是 |
|is_allocate |离职状态员工，数据分配状态:0:未分配;1:已分配 |tinyint |0 |是 |
|dimission_time |员工离职时间 |datetime |null |是 |
|is_open_chat |是否开启会话存档 0-未开启 1-开启 |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |
|del_flag |删除标志（0代表存在 1代表删除） |tinyint |0 |是 |
|kf_status |客服接待状态。1:接待中,2:停止接待 |tinyint |1 |是 |

### 用户部门表(sys_user_dept)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|user_dept_id |用户部门关联表 |bigint |null |不为空 |
|user_id |用户主键 |bigint |null |是 |
|dept_id |部门id |bigint |null |是 |
|we_user_id |企微用户id |varchar |null |是 |
|open_userid |openUserId |varchar |null |是 |
|order_in_dept |用户在部门中顺序 |varchar |null |是 |
|leader_in_dept |在所在的部门内是否为部门负责人，0-否；1-是 |char |null |是 |
|del_flag |删除标志（0代表存在 2代表删除） |char |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 用户与岗位关联表(sys_user_post)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|user_id |用户ID |bigint |null |不为空 |
|post_id |岗位ID |bigint |null |不为空 |

### 用户和角色关联表(sys_user_role)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|user_id |用户ID |bigint |null |不为空 |
|role_id |角色ID |bigint |null |不为空 |

### 应用信息表(we_agent_info)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |int |null |不为空 |
|agent_id |应用ID |int |null |不为空 |
|secret |应用密钥 |varchar |null |不为空 |
|name |应用名称 |varchar |null |是 |
|logo_url |企业应用方形头像 |varchar |null |是 |
|description |应用详情 |varchar |null |是 |
|allow_userinfo_id |应用可见范围员工ID |varchar |null |是 |
|allow_party_id |应用可见范围部门ID |varchar |null |是 |
|allow_tag_id |应用可见范围标签ID |varchar |null |是 |
|close |是否被停用 |tinyint |null |是 |
|redirect_domain |可信域名 |varchar |null |是 |
|report_location_flag |是否打开地理位置上报 0：不上报；1：进入会话上报 |tinyint |null |是 |
|is_reporter |上报用户进入应用事件 0-不接收 1-接收 |tinyint |null |是 |
|home_url |应用主页url |varchar |null |是 |
|customized_publish_status |发布状态。0-待开发 1-开发中 2-已上线 3-存在未上线版本 |int |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 应用消息表(we_agent_msg)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_title |消息标题 |varchar |null |不为空 |
|agent_id |应用ID |int |null |是 |
|scope_type |范围类型 1-全部 2-自定义 |tinyint |2 |不为空 |
|to_user |接收消息的成员 |varchar |null |是 |
|to_party |接收消息的部门 |varchar |null |是 |
|to_tag |接收消息的标签 |varchar |null |是 |
|send_type |发送方式 1-立即发送 2-定时发送 |tinyint |null |是 |
|send_time |发送时间 |datetime |null |是 |
|plan_send_time |计划时间 |datetime |null |是 |
|status |消息状态：0-草稿 1-待发送 2-已发送 3-发送失败 4-已撤回 |tinyint |null |是 |
|invalid_user |无效成员ID |varchar |null |是 |
|invalid_party |无效部门ID |varchar |null |是 |
|invalid_tag |无效标签ID |varchar |null |是 |
|unlicensed_user |没有基础接口许可(包含已过期)的userid |varchar |null |是 |
|msg_id |消息ID |varchar |null |是 |
|response_code |更新模版卡片消息CODE |varchar |null |是 |
|msg_type |消息类型 具体见企微文档 |varchar |null |是 |
|content |消息内容 |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 离职分配的客户列表(we_allocate_customer)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|takeover_userid |接替成员的userid |varchar |null |是 |
|external_userid |被分配的客户id |varchar |null |不为空 |
|allocate_time |分配时间 |datetime |null |不为空 |
|handover_userid |原跟进成员的userid |varchar |null |不为空 |
|status |接替状态:1:等待接替 2:接替中(等待微信接替) 3:接替成功 4:接替失败 |tinyint |2 |是 |
|takeover_time |接替客户的时间，如果是等待接替状态，则为未来的自动接替时间 |datetime |null |是 |
|fail_reason |失败原因 |varchar |null |是 |
|extent_type |0:离职继承;1:在职继承; |tinyint |0 |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |

### 分配的群租(we_allocate_group)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|chat_id |分配的群id |varchar |null |是 |
|new_owner |新群主 |varchar |null |是 |
|err_msg |接替失败原因 |varchar |null |是 |
|status |1:等待接替 2:接替中(等待微信接替) 3:接替成功 4:接替失败 |tinyint |0 |是 |
|old_owner |原群主 |varchar |null |是 |
|allocate_time |分配时间 |datetime |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |

### (we_category)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|media_type |0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报、 6 活码 7-人群 8-旅程 |int |null |不为空 |
|name |分类名称 |varchar |null |是 |
|parent_id |父分类的id |bigint |0 |是 |
|flag |可删除标识 0 可删除 1 不可删除 |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 素材收藏表(we_chat_collection)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|material_id |素材ID |bigint |null |不为空 |
|user_id |员工ID |varchar |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 会话消息(we_chat_contact_msg)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_id |消息id |varchar |null |不为空 |
|from_id |发送人id |varchar |null |不为空 |
|to_list |接收人id（列表） |mediumtext |null |不为空 |
|room_id |群聊id |varchar |null |是 |
|action |消息类型 |varchar |null |不为空 |
|msg_type |消息类型(如：文本，图片) |varchar |null |是 |
|msg_time |发送时间 |datetime |null |不为空 |
|seq |消息标识 |bigint |null |是 |
|contact |消息内容 |mediumtext |null |是 |
|is_external |是否为外部聊天 0 外部 1 内部 |tinyint |0 |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flg |删除标识 0正常 1 删除 |tinyint |0 |不为空 |

### 会话触发敏感词记录(we_chat_contact_sensitive_msg)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_id |消息id |varchar |null |不为空 |
|send_status |通知发送状态 |tinyint |0 |不为空 |
|pattern_words |匹配词 |varchar | |不为空 |
|content |匹配内容 |text |null |是 |
|from_id |发送人id |varchar |null |不为空 |
|msg_time |发送时间 |datetime |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### (we_chat_item)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|side_id |聊天工具栏id |bigint |null |是 |
|material_id |素材id |bigint |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### (we_chat_side)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|media_type |素材类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file) 4 文本 5 海报 |bigint |null |是 |
|side_name |聊天工具栏名称 |varchar |null |是 |
|total |已抓取素材数量 |int |null |是 |
|using |是否启用 0 启用 1 未启用 |int |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0  未删除 1 已删除 |int |0 |是 |

### 新客自动拉群(we_community_new_group)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|empl_code_name |员工活码名称 |varchar |null |不为空 |
|group_code_id |群活码ID |bigint |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id  |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0:正常;1:删除; |int |0 |是 |
|empl_code_id |员工活码id |bigint |null |不为空 |

### 内容中心发送记录表(we_content_send_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|content_id |素材id |bigint |null |是 |
|send_by |发送人 |varchar |null |是 |
|send_by_id |发送人id |bigint |null |是 |
|send_time |发送时间 |datetime |CURRENT_TIMESTAMP |是 |
|resource_type |素材来源(1素材，2话术) |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0 未删除 1 已删除 |tinyint |0 |是 |
|talk_id |话术id |bigint |null |是 |

### 话术中心表(we_content_talk)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|category_id |分组id |bigint |null |是 |
|talk_title |话术标题 |varchar |null |是 |
|talk_type |0企业话术1客服话术 |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0 未删除 1 已删除 |int |0 |是 |

### 内容中心查看记录表(we_content_view_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|content_id |素材id |bigint |null |是 |
|view_by |查看人 |varchar |null |是 |
|view_unionid |查看人unionid |varchar |null |是 |
|view_openid |查看人openid |varchar |null |是 |
|view_time |查看时间 |datetime |CURRENT_TIMESTAMP |是 |
|resource_type |素材来源 1素材中心， 2话术中心 |tinyint |null |是 |
|is_customer |是否企业客户 0否1是 |tinyint |1 |是 |
|view_watch_time |观看时间 |bigint |0 |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0 未删除 1 已删除 |tinyint |0 |是 |
|external_user_id |外部联系人Id |varchar |null |是 |
|external_user_name |外部联系人姓名 |varchar |null |是 |
|external_avatar |外部联系人头像 |varchar |null |是 |
|is_auth |是否授权（0否1是） |tinyint |0 |是 |
|talk_id |话术id |bigint |null |是 |

### 企业信息表(we_corp_account)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |int |null |不为空 |
|company_name |企业名称 |varchar |null |是 |
|logo_url |企业logo |varchar |null |是 |
|corp_id |企业ID |varchar |null |是 |
|corp_secret |通讯录密钥 |varchar |null |是 |
|contact_secret |外部联系人密钥 |varchar |null |是 |
|live_secret |直播密钥 |varchar |null |是 |
|chat_secret |会话存档密钥 |varchar |null |是 |
|kf_secret |客服密钥 |varchar |null |是 |
|agent_id |应用id |varchar |null |是 |
|agent_secret |应用密钥 |varchar |null |是 |
|back_url |回调url |varchar |null |是 |
|token |回调token |varchar |null |是 |
|encoding_aes_key |回调EncodingAESKey |varchar |null |是 |
|finance_private_key |会话存档私钥 |text |null |是 |
|mer_chant_number |商户号 |varchar |null |是 |
|mer_chant_name |商户名称 |varchar |null |是 |
|mer_chant_secret |商户密钥 |varchar |null |是 |
|cert_p12_url |API证书文件p12 |varchar |null |是 |
|wx_app_id |公众号id |varchar |null |是 |
|wx_secret |公众号密钥 |varchar |null |是 |
|customer_churn_notice_switch |客户流失通知开关 0:关闭 1:开启 |char |0 |是 |
|bill_secret |对外收款秘钥 |varchar |null |是 |
|mini_app_id |微信小程序ID |varchar |null |是 |
|mini_secret |微信小程序密钥 |varchar |null |是 |
|wx_applet_original_id |微信小程序原始ID |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人ID |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人ID |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标志（0代表存在 1代表删除） |tinyint |0 |是 |

### 企业微信客户表(we_customer)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|external_userid |外部联系人的userid |varchar |null |不为空 |
|customer_name |外部联系人名称 |varchar |null |是 |
|customer_full_name |客户姓名 |varchar |null |是 |
|customer_type |外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户 |tinyint |null |是 |
|avatar |外部联系人头像 |varchar |null |是 |
|gender |外部联系人性别 0-未知 1-男性 2-女性 |tinyint |0 |不为空 |
|unionid |外部联系人在微信开放平台的唯一身份标识,通过此字段企业可将外部联系人与公众号/小程序用户关联起来。 |varchar |null |是 |
|birthday |生日 |datetime |null |是 |
|corp_name |客户企业名称 |varchar |null |是 |
|position |客户职位 |varchar |null |是 |
|is_open_chat |是否开启会话存档 0：关闭 1：开启 |tinyint |0 |是 |
|open_chat_time |开通会话存档时间 |datetime |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建热id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |
|add_user_id |添加人id |varchar |null |不为空 |
|add_time |添加时间 |datetime |null |是 |
|track_state |1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失 |tinyint |1 |是 |
|track_content |跟进内容 |varchar |null |是 |
|track_time |跟进时间 |datetime |null |是 |
|phone |手机号 |varchar |null |是 |
|email |邮箱 |varchar |null |是 |
|qq |QQ号 |varchar |null |是 |
|other_descr |其他描述 |varchar |null |是 |
|address |地址 |varchar |null |是 |
|takeover_user_id |当前接替人 |varchar |null |是 |
|tag_ids |标签id冗余字段，方便查询 |text |null |是 |
|add_method |添加方式 |int |null |是 |
|state |渠道,当前用户通过哪个活码添加 |varchar |null |是 |
|province_id |省id |int |null |是 |
|city_id |市id |int |null |是 |
|area_id |区id |int |null |是 |
|area |省/市/区 |varchar |null |是 |
|remark_name |备注名 |varchar |null |是 |

### 客户公海(we_customer_seas)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|phone |手机号 |varchar |null |是 |
|customer_name |客户备注名称 |varchar |null |是 |
|tag_names |标签名，多个使用逗号隔开 |tinytext |null |是 |
|tag_ids |标签id，多个使用逗号隔开 |tinytext |null |是 |
|add_user_name |添加人名称 |varchar |null |是 |
|add_user_id |添加人id |varchar |null |是 |
|table_excel_name |导入的excel的名称 |varchar |null |是 |
|table_excel_id |导入的excel的同一批次下的id |bigint |null |是 |
|add_state |当前状态:0:待添加;1:已添加;3:待通过 |tinyint |0 |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 员工跟进轨迹表(we_customer_track_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|track_time |跟进时间 |datetime |null |是 |
|track_state |1:待跟进;2:跟进中;3:已成交;4:无意向;5:已流失;6:其他 |tinyint |null |是 |
|track_title |跟进标题 |varchar |null |是 |
|track_content |跟进内容 |varchar |null |是 |
|external_userid |客户id |varchar |null |是 |
|we_user_id |员工id |varchar |null |是 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id | |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id | |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 客户轨迹表(we_customer_trajectory)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|trajectory_type |轨迹类型(1:客户动态;2:员工动态;3:互动动态4:跟进动态5:客群动态) |tinyint |null |是 |
|trajectory_scene_type |轨迹场景类型，详细描述，见TrajectorySceneType |tinyint |null |是 |
|operator_type |操作人类型:1:客户;2:员工; |tinyint |null |是 |
|operator_id |操作人id |varchar |null |是 |
|operator_name |操作人姓名 |varchar |null |是 |
|operatored_object_type |被操作对象类型:1:客户;2:员工:3:客群 |tinyint |null |是 |
|operatored_object_id |被操作对象的id |varchar |null |是 |
|operatored_object_name |被操作对象名称 |varchar |null |是 |
|external_userid_or_chatid |客户id或群id，查询字段冗余,档该id不存在的时候代表 |varchar |null |是 |
|we_user_id |员工id，查询字段冗余 |varchar |null |是 |
|action |动作 |varchar |null |是 |
|title |标题 |varchar |null |是 |
|content |文案内容,整体内容 |varchar |null |是 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|material_id |素材Id |bigint |null |是 |

### 员工活码表(we_emple_code)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|code_type |活码类型:1:批量;2:单人;3:多人; |tinyint |null |是 |
|skip_verify |客户添加时无需经过确认自动成为好友:1:是;0:否 |tinyint |1 |不为空 |
|scenario |活动场景 |varchar |null |是 |
|welcome_msg |欢迎语 |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|del_flag |0:正常;1:删除; |tinyint |null |是 |
|config_id |新增联系方式的配置id |varchar |null |是 |
|qr_code |二维码链接 |varchar |null |是 |
|media_id |素材id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|scan_times |该员工活码的扫码次数 |int |0 |不为空 |
|is_join_confirm_friends | 客户添加时是否需要经过确认自动成为好友  1:是  0:否 |tinyint |0 |是 |
|state |用于区分客户具体是通过哪个「联系我」添加。不能超过30个字符 |varchar | |是 |

### 员工活码标签(we_emple_code_tag)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|tag_id |标签id |varchar |null |是 |
|tag_name |标签名称 |varchar |null |是 |
|emple_code_id |员工活码id |bigint |null |是 |
|del_flag |0:正常;2:删除; |tinyint |0 |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |varchar |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |

### 员工活码使用人(we_emple_code_use_scop)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|emple_code_id |员工活码id |bigint |null |是 |
|business_id_type |业务id类型1:组织机构id,2:成员id |tinyint |null |是 |
|business_name |活码下使用人姓名 |varchar |null |是 |
|business_id |活码类型下业务使用人的id |varchar |null |是 |
|del_flag |0:正常;2:删除; |tinyint |null |是 |
|party_id |部门id列表，只在多人时有效 |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |

### 客户标签关系表(we_flower_customer_tag_rel)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|user_id |员工id |varchar |null |不为空 |
|external_userid |客户id |varchar |null |不为空 |
|tag_id |标签id |varchar |null |是 |
|is_company_tag |是否是企业标签:1:是;0:否 |tinyint |1 |是 |
|create_by |创建人 |varchar |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |

### 答题-用户主表(we_form_survey_answer)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|mobile |手机号 |varchar |null |是 |
|name |用户姓名 |varchar |null |是 |
|avatar |用户头像 |varchar |null |是 |
|addr |详细地址 |varchar |null |是 |
|city |城市 |varchar |null |是 |
|open_id |微信openID |varchar |null |是 |
|union_id |微信unionID |varchar |null |是 |
|an_time |答题开始时间 |datetime |null |是 |
|total_time |答题用时 |float |null |是 |
|ip_addr |ip地址 |varchar |null |是 |
|answer |答案 |blob |null |是 |
|belong_id |问卷id |bigint |null |是 |
|an_effective |是否完成;0完成，1未完成 |tinyint |null |是 |
|qu_num |答题数 |int |null |是 |
|data_source |数据来源 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 问卷-目录列表(we_form_survey_catalogue)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|answer_num |答卷人数 |int |null |是 |
|an_share |是否分享 |tinyint |null |是 |
|an_auth |是否授权 |tinyint |null |是 |
|sid |编码id |varchar |null |是 |
|survey_name |名称 |varchar |null |是 |
|survey_qu_num |题数 |int |null |是 |
|survey_state |表单状态;0默认设计状态未发布，1收集中，2已暂停, 3已结束 |tinyint |0 |是 |
|visibility |是否显示;0显示，1不显示 |tinyint |0 |是 |
|group_id |分组id |bigint |null |是 |
|form_description |表单描述 |varchar |null |是 |
|form_logo |表单logo |varchar |null |是 |
|an_timing |是否定时 |tinyint |null |是 |
|timing_start |定时开始时间 |datetime |null |是 |
|timing_end |定时结束时间 |datetime |null |是 |
|filling_rules |填写规则;0每人填写一次，1每人每天填写一次 |tinyint |null |是 |
|html_path |页面地址 |varchar |null |是 |
|an_channels |是否多渠道 |tinyint |null |是 |
|channels_path |多渠道地址 |varchar |null |是 |
|styles |表单样式 |mediumtext |null |是 |
|qr_code |二维码 |varchar |null |是 |
|channels_name |渠道名称 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 问卷-单选、多选题-选项(we_form_survey_radio)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|form_code_id |表单控件Id |varchar |null |是 |
|label |控件名称 |varchar |null |是 |
|form_id |表单Id |varchar |null |不为空 |
|default_value |选择内容 |varchar |null |是 |
|options |所有选项 |varchar |0 |是 |
|data_source |渠道 |varchar |null |是 |
|question_number |题号 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 智能表单站点统计数据(we_form_survey_site_stas)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|belong_id |问卷Id |bigint |null |不为空 |
|total_visits |总访问量 |int |null |是 |
|total_user |总访问用户量 |int |null |是 |
|collection_volume |有效收集量 |int |null |是 |

### 问卷-统计表(we_form_survey_statistics)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|belong_id |问卷id |bigint |null |是 |
|total_visits |总访问量 |int |null |是 |
|total_user |总访问用户量 |int |null |是 |
|collection_volume |有效收集量 |int |null |是 |
|collection_rate |收集率 |varchar |null |是 |
|average_time |平均完成时间 |int |null |是 |
|yes_total_visits |较昨日总访问量 |int |null |是 |
|yes_total_user |较昨日总访问用户量 |int |null |是 |
|yes_collection_volume |较昨日有效收集量 |int |null |是 |
|data_source |来源渠道 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 企业微信群(we_group)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|chat_id |群聊id |varchar |null |不为空 |
|group_name |群名 |varchar |群聊 |是 |
|add_time |群创建时间 |datetime |null |不为空 |
|notice |群公告 |text |null |是 |
|owner |群主userId |varchar |null |是 |
|admin_user_id |群管理员id |varchar |null |是 |
|status |跟进状态 0-正常;1-跟进人离职;2-离职继承中;3-离职继承完成 |tinyint |0 |是 |
|allocate_state |分配状态:0-被接替成功;1-待接替;2-接替失败;3-正常状态  |tinyint |3 |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 客户群活码(we_group_code)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|code_url |二维码链接 |varchar |null |是 |
|activity_name |活码名称 |varchar |null |是 |
|auto_create_room |当群满了后，是否自动新建群。0-否；1-是。 默认为0 |tinyint |0 |是 |
|room_base_name |自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符 |varchar |null |是 |
|room_base_id |自动建群的群起始序号，当auto_create_room为1时有效 |int |null |是 |
|chat_id_list |实际群id，多个实用逗号隔开 |varchar |null |是 |
|config_id |配置id |varchar |null |是 |
|state |qhm _开头，企业自定义的state参数，用于区分不同的入群渠道。 |char |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 群活码标签关系(we_group_code_tag_rel)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|group_code_id |群活码id |bigint |null |是 |
|tag_id |标签id |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标志 0-正常 1-删除 |tinyint |0 |是 |

### 企业微信群成员(we_group_member)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|chat_id |群id |varchar |null |是 |
|user_id |群成员id |varchar |null |是 |
|union_id |外部联系人在微信开放平台的唯一身份标识 |varchar |null |是 |
|join_time |加群时间 |datetime |null |是 |
|join_scene |加入方式 1 - 由群成员邀请入群（直接邀请入群) 2 - 由群成员邀请入群（通过邀请链接入群）3 - 通过扫描群二维码入群 |tinyint |null |是 |
|type |成员类型:1 - 企业成员;2 - 外部联系人 |tinyint |null |是 |
|group_nick_name |在群里的昵称 |varchar |null |是 |
|name |名字 |varchar |null |是 |
|invitor_user_id |邀请人userId |varchar |null |是 |
|quit_scene |员的退群方式 0-自己退群 1-群主/群管理员移出 |tinyint |null |是 |
|quit_time |退出时间 |datetime |null |是 |
|state |用户加群渠道id |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 群发消息附件表(we_group_message_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_template_id |消息模板id |bigint |null |不为空 |
|msg_id |企业群发消息的id |varchar |null |是 |
|msg_type |消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  |varchar |null |不为空 |
|content |消息内容 |mediumblob |null |是 |
|media_id |媒体id |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |
|real_type |真实类型 |tinyint |null |是 |
|material_id |素材中心id |bigint |null |是 |

### 群发消息列表(we_group_message_list)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_id |企业群发消息的id |varchar |null |是 |
|chat_type |群发任务的类型，默认为single，表示发送给客户，group表示发送给客户群 |varchar |single |不为空 |
|user_id |群发消息创建者userid |varchar |null |是 |
|send_time |发送时间 |datetime |null |是 |
|create_type |群发消息创建来源。0：企业 1：个人 |tinyint |null |是 |
|msg_template_id |群发消息模板id |bigint |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 群发消息成员执行结果表(we_group_message_send_result)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_template_id |消息模板id |bigint |null |不为空 |
|msg_id |企业群发消息的id |varchar |null |是 |
|user_id |企业服务人员的userid |varchar |null |是 |
|external_userid |外部联系人userid |varchar |null |是 |
|chat_id |外部客户群id |varchar |null |是 |
|status |发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败 |tinyint |0 |是 |
|send_time |发送时间，发送状态为1时返回 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 群发消息成员发送任务表(we_group_message_task)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|msg_template_id |消息模板id |bigint |null |不为空 |
|msg_id |企业群发消息的id |varchar |null |是 |
|user_id |企业服务人员的userid |varchar |null |是 |
|status |发送状态：0-未发送 2-已发送 |tinyint |0 |是 |
|send_time |发送时间，未发送时不返回 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 群发消息模板(we_group_message_template)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|chat_type |群发任务的类，1：表示发送给客户，2：表示发送给客户群 |tinyint |1 |不为空 |
|content |群发内容 |varchar |null |是 |
|send_time |发送时间 |datetime |null |是 |
|is_task |是否定时任务 0 立即发送 1 定时发送 |tinyint |0 |是 |
|status |是否执行 -1：失败  0：未执行 1：完成 2：取消 |tinyint |0 |是 |
|business_id |业务id |bigint |null |是 |
|source |来源 0 群发 1 其他 |tinyint |0 |是 |
|refresh_time |刷新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 群机器人信息表(we_group_robot_info)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|group_name |群名称名称 |varchar |null |不为空 |
|web_hook_url |群机器人链接 |varchar |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 群机器人消息表(we_group_robot_msg)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|robot_id |群机器人ID |bigint |null |不为空 |
|msg_title |消息标题 |varchar |null |不为空 |
|send_time |发送时间 |datetime |null |是 |
|status |消息状态：0-草稿 1-待发送 2-已发送 3-发送失败 |tinyint |1 |不为空 |
|msg_type |消息类型 具体见企微文档 |varchar |null |是 |
|content |消息内容 |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 群SOP规则表(we_group_sop)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|rule_name |规则名称 |varchar |null |不为空 |
|title |标题 |varchar |null |不为空 |
|content |内容 |varchar |null |不为空 |
|start_time |开始执行时间 |datetime |null |不为空 |
|end_time |结束时间 |datetime |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar |null |是 |
|del_flag |逻辑删除字段， 0:未删除 1:已删除 |tinyint |0 |是 |

### SOP规则 - 群聊 关联表(we_group_sop_chat)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|rule_id |SOP id |bigint |null |不为空 |
|chat_id |群聊id |varchar |null |不为空 |
|is_done |规则是否已发送0：未发送 1：已发送 |smallint |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 群SOP规则 - 素材关联表(we_group_sop_material)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|rule_id |SOP规则id |bigint |null |不为空 |
|material_id |素材id |bigint |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 群SOP规则图片(we_group_sop_pic)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|rule_id |群SOP规则ID |bigint |null |不为空 |
|pic_url |图片URL |varchar |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 群聊数据统计数据
(we_group_statistic)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|stat_time |数据日期 |datetime |null |不为空 |
|new_chat_cnt |新增客户群数量 |int |null |是 |
|chat_total |截至当天客户群总数量 |int |null |是 |
|chat_has_msg |截至当天有发过消息的客户群数量 |int |null |是 |
|new_member_cnt |客户群新增群人数 |int |null |是 |
|member_total |截至当天客户群总人数 |int |null |是 |
|member_has_msg |截至当天有发过消息的群成员数 |int |null |是 |
|msg_total |截至当天客户群消息总数 |int |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 群标签关系(we_group_tag_rel)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|chat_id |群id |varchar |null |是 |
|tag_id |标签id |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标志 0-正常 1-删除 |tinyint |0 |是 |

### 群聊群主数据统计数据(we_group_user_statistic)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|stat_time |数据日期 |datetime |null |不为空 |
|user_id |群主ID |varchar |0 |不为空 |
|new_chat_cnt |新增客户群数量 |int |null |是 |
|chat_total |截至当天客户群总数量 |int |null |是 |
|chat_has_msg |截至当天有发过消息的客户群数量 |int |null |是 |
|new_member_cnt |客户群新增群人数 |int |null |是 |
|member_total |截至当天客户群总人数 |int |null |是 |
|member_has_msg |截至当天有发过消息的群成员数 |int |null |是 |
|msg_total |截至当天客户群消息总数 |int |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 关键词拉群任务表(we_keyword_group)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|task_name |任务名称 |varchar |null |不为空 |
|group_code_id |群活码id |bigint |null |不为空 |
|welcome_msg |加群欢迎语 |varchar |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|remark |备注 |varchar | |是 |
|del_flag |删除标记 0 未删除 1已删除 |char |0 |不为空 |
|keywords |关键词，以逗号分隔 |varchar |null |不为空 |

### 客服客户表(we_kf_customer)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|external_userid |微信客户的external_userid |varchar |null |不为空 |
|nick_name | |varchar |null |是 |
|avatar |微信头像 |varchar |null |是 |
|union_id |unionid |varchar |null |是 |
|gender |性别 0-未知 1-男性 2-女性 |tinyint |0 |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服客户统计表(we_kf_customer_stat)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|date_time |日期 |varchar |null |是 |
|session_cnt |会话总数 |int |0 |是 |
|evaluate_cnt |参评总数 |int |0 |是 |
|good_cnt |好评数 |int |0 |是 |
|common_cnt |一般数 |int |0 |是 |
|bad_cnt |差评数 |int |0 |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服事件消息表(we_kf_event_msg)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|msg_id |消息id |varchar |null |不为空 |
|open_kf_id |客服帐号ID |varchar |null |不为空 |
|send_time |消息发送时间 |datetime |null |不为空 |
|origin |消息来源。3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息 |tinyint |null |是 |
|event_type |事件类型 |varchar |null |不为空 |
|external_userid |客户UserID |varchar |null |是 |
|servicer_userid |接待人员userid |varchar |null |是 |
|old_servicer_userid |老的接待人员userid |varchar |null |是 |
|new_servicer_userid |新的接待人员userid |varchar |null |是 |
|change_type |变更类型。1-从接待池接入会话 2-转接会话 3-结束会话 4-重新接入已结束/已转接会话 |tinyint |null |是 |
|status |状态类型。1-接待中 2-停止接待 |tinyint |null |是 |
|scene |进入会话的场景值 |varchar |null |是 |
|scene_param |进入会话的自定义参数 |varchar |null |是 |
|msg_code |用于发送事件响应消息的code |varchar |null |是 |
|welcome_code |欢迎语code |varchar |null |是 |
|fail_msg_id |发送失败的消息msgid |varchar |null |是 |
|fail_type |失败类型:0-未知原因 1-客服账号已删除 2-应用已关闭 4-会话已过期，超过48小时 5-会话已关闭 6-超过5条限制 7-未绑定视频号 8-主体未验证 9-未绑定视频号且主体未验证 10-用户拒收 |tinyint |0 |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服信息表(we_kf_info)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|name |客服名称 |varchar |null |不为空 |
|avatar |客服头像 |varchar |null |是 |
|open_kf_id |客服帐号ID |varchar |null |不为空 |
|reception_type |接待方式: 1-人工客服 2-智能助手 |tinyint |1 |不为空 |
|split_time |是否分时段: 1-否 2-是 |tinyint |1 |不为空 |
|allocation_way |分配方式: 1-轮流 2-空闲 |tinyint |1 |不为空 |
|is_priority |是否有限分配: 1-否 2-是 |tinyint |1 |不为空 |
|receive_limit |接待限制 |int |2 |不为空 |
|queue_notice |排队提醒: 1-开启 2-关闭 |tinyint |1 |不为空 |
|queue_notice_content |排队提醒内容 |varchar |null |是 |
|time_out_notice |超时未回复提醒: 1-开启 2-关闭 |tinyint |1 |不为空 |
|time_out |超时时间 |int |null |是 |
|time_out_type |超时时间类型 1-分钟 2-小时 |tinyint |1 |不为空 |
|time_out_content |超时未回复提醒内容 |varchar |null |是 |
|kf_time_out_notice |客服超时未回复提醒: 1-开启 2-关闭 |tinyint |null |是 |
|kf_time_out_type |客户超时时间类型 1-分钟 2-小时 |tinyint |null |是 |
|kf_time_out |客服超时时间 |int |null |是 |
|end_notice |自动结束提醒: 1-开启 2-关闭 |tinyint |1 |不为空 |
|end_notice_time |自动结束时间 |int |null |是 |
|end_time_type |自动结束时间类型 1-分钟 2-小时 |tinyint |1 |不为空 |
|end_content_type |自动结束提醒内容类型 1-会话质量评价 2-会话结束语 |tinyint |null |是 |
|end_content |自动结束提醒内容 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服消息表(we_kf_msg)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|msg_id |消息id |varchar |null |不为空 |
|open_kf_id |消息id |varchar |null |不为空 |
|external_userid |客户UserID |varchar |null |不为空 |
|servicer_userid |接待人员userid |varchar |null |是 |
|send_time |消息发送时间 |datetime |null |不为空 |
|origin |消息来源:3-微信客户发送的消息 4-系统推送的事件消息 5-接待人员在企业微信客户端发送的消息 |tinyint |4 |不为空 |
|msg_type |消息类型 text、image、voice、video、file、location、link、business_card、miniprogram、msgmenu、event |varchar |null |不为空 |
|content |消息内容 |mediumtext |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服消息偏移量表(we_kf_msg_cursor)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|next_cursor |上一次调用时返回的next_cursor |varchar |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服员工通知日志表(we_kf_notice_log)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|user_id |员工id |varchar |null |不为空 |
|external_user_id |客户id |varchar |null |是 |
|open_kf_id |客服id |varchar |null |是 |
|corp_id |企业id |varchar |null |是 |
|send_time |C端发送时间 |datetime |null |是 |
|send_status |发送状态 1-成功 2-失败 |int |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服接待池表(we_kf_pool)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|open_kf_id |客服id |varchar |null |不为空 |
|external_userid |客户UserID |varchar |null |不为空 |
|status |状态 0-未处理,1-机器人,2-接待池,3-人工接待,4-已结束/未开始 |tinyint |4 |不为空 |
|user_id |员工id |varchar |null |是 |
|scene |场景值 |varchar |null |是 |
|enter_time |进入会话时间 |datetime |null |不为空 |
|session_start_time |会话开始时间 |datetime |null |是 |
|session_end_time |会话结束时间 |datetime |null |是 |
|reception_time |接待时间 |datetime |null |是 |
|evaluation_type |评价类型 101-好评 102-一般 103-差评 |varchar |null |是 |
|evaluation |评价语 |varchar |null |是 |
|msg_code |消息code |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服场景信息表(we_kf_scenes)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|name |场景名称 |varchar |null |不为空 |
|type |场景类型 1-公众号 2-小程序 3-视频号 4-搜一搜 5-微信支付 6-app 7-网页场景类型  |tinyint |1 |是 |
|kf_id |客服id |bigint |null |不为空 |
|open_kf_id |客服账号ID |varchar |null |不为空 |
|scenes |场景值 |varchar |null |不为空 |
|url |客服链接 |varchar |null |不为空 |
|qr_code |二维码链接 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服接待人员表(we_kf_servicer)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|kf_id |客服id |bigint |null |不为空 |
|open_kf_id |客服帐号ID |varchar |null |不为空 |
|user_id |接待人员userid |varchar |null |是 |
|department_id |接待人员部门的id |int |null |是 |
|status |接待人员的接待状态。0:接待中,1:停止接待 |tinyint |0 |是 |
|reception_num |接待人数 |int |0 |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服员工统计表(we_kf_user_stat)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|date_time |日期 |varchar |null |是 |
|open_kf_id |客服ID |varchar |null |是 |
|user_id |员工ID |varchar |null |是 |
|session_cnt |会话总数 |int |0 |是 |
|evaluate_cnt |参评总数 |int |0 |是 |
|good_cnt |好评数 |int |0 |是 |
|common_cnt |一般数 |int |0 |是 |
|bad_cnt |差评数 |int |0 |是 |
|talk_cnt |对话数 |int |0 |是 |
|time_out_cnt |超时数 |int |0 |是 |
|time_out_duration |超时时长 |varchar |0 |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 客服欢迎语表(we_kf_welcome)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|corp_id |企业id |varchar |null |是 |
|kf_id |客服id |bigint |null |不为空 |
|work_cycle |周期时间 |varchar |null |是 |
|begin_time |开始时间 |varchar |null |是 |
|end_time |结束时间 |varchar |null |是 |
|type |欢迎语类型 1-文本 2-菜单 |tinyint |1 |不为空 |
|content |欢迎语内容 |text |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 活码附件表(we_know_customer_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|know_customer_id |识客码id |bigint |null |不为空 |
|msg_type |消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  |varchar |null |不为空 |
|content |消息内容 |text |null |是 |
|media_id |媒体id |varchar |null |是 |
|msg_id |企业微信端返回的消息id |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|real_type |素材真实类型 |tinyint |null |是 |
|material_id |素材id |bigint |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 识客码(we_know_customer_code)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|know_customer_name |识客码名称 |varchar |null |是 |
|add_we_user |添加的成员,相关信息 |json |null |是 |
|add_we_user_config |添加成员活码config |varchar |null |是 |
|add_we_user_url |添加成员活码url |varchar |null |是 |
|add_we_user_state |员工二维码渠道标识
 |varchar |null |是 |
|poster_url |海报的url |varchar |null |是 |
|posters_id |海报id |bigint |null |是 |
|know_customer_url |识客码url |varchar |null |是 |
|know_customer_qr | |varchar |null |是 |
|is_add_all_user |是否添加所有成员:1:已添加任意成员;0:已添加指定成员 |tinyint |0 |是 |
|appoint_we_user |已添加指定成员的条件 |json |null |是 |
|jump_content_type |跳转指定内容类型(1:员工活码;2:链接;3:小程序) |tinyint |1 |是 |
|jump_content |跳转实际内容 |json |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |创建人id |int |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 识客码统计相关(we_know_customer_code_count)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|know_customer_id |识客码主键id |bigint |null |是 |
|new_or_old |0:新客;1:老客 |tinyint |0 |是 |
|unionid |客户在微信平台的unionid |varchar |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 员工活码标签(we_know_customer_code_tag)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|tag_id |标签id |varchar |null |是 |
|tag_name |标签名称 |varchar |null |是 |
|know_customer_code_id |识客码id |bigint |null |是 |
|is_old_customer |是否是老客,1:是;0否 |tinyint |0 |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |varchar |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |int |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 直播主表(we_live)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|live_title |直播标题 |varchar |null |是 |
|live_we_userid |直播成员 |varchar |null |是 |
|live_start_time |直播开始时间(预计) |time |null |是 |
|live_start_date |直播开始日期(预计) |date |null |是 |
|live_end_time |直播结束时间(预计) |datetime |null |是 |
|live_end_date |直播结束日期(预计) |date |null |是 |
|living_duration |直播预约时长，存秒入1小时存3600秒(预计) |bigint |0 |是 |
|actual_start_time |直播实际开始时间 |datetime |null |是 |
|actual_end_time |直播实际结束时间 |datetime |null |是 |
|living_actual_duration |直播实际持续市场,秒 |bigint |0 |是 |
|live_state |直播状态(0:预约中;1:直播中;2:已结束;3:已过期;4:已取消) |tinyint |0 |是 |
|live_desc |直播简介 |tinytext |null |是 |
|living_id |企业微信api返回的直播id |varchar |null |是 |
|viewer_num |观看直播总人数 |bigint |0 |是 |
|online_count |当前在线观看人数 |bigint |0 |是 |
|mic_num |连麦发言人数 |bigint |0 |是 |
|comment_num |直播评论人数 |bigint |0 |是 |
|subscribe_count |直播预约人数 |bigint |0 |是 |
|start_reminder |开播提醒时间比如，开播五分钟前,一小时前，一天前等，全部转成秒数值存储 |int |null |是 |
|start_spec_reminder |开播提醒具体时间,根据live_start_time与start_reminder换算的 |datetime |null |是 |
|send_we_user |为空的时候则为全部成员 |tinytext |null |是 |
|open_replay |是否开启回放，1表示开启，0表示关闭 |tinyint |1 |是 |
|replay_status |open_replay为1时才返回该字段。0表示生成成功，1表示生成中，2表示回放已删除，3表示生成失败 |tinyint |null |是 |
|target_type |发送目标1:客户;2:客群 |tinyint |1 |是 |
|send_target |发送客户目标条件为空则为全部客户，其他条件则为部分客户 |tinytext |null |是 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |是 |

### 活码附件表(we_live_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|live_id |直播主表id |bigint |null |不为空 |
|msg_type |消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  |varchar |null |不为空 |
|content |消息内容 |text |null |是 |
|media_id |媒体id |varchar |null |是 |
|msg_id |企业微信端返回的消息id |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|real_type |素材真实类型 |tinyint |null |是 |
|material_id |素材id |bigint |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 直播员工群发通知消息表(we_live_tip)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|send_we_userid |发送人id |varchar |null |是 |
|send_target_id |发送目标id |varchar |null |是 |
|send_target_type |发送目标1:客户;2:客群 |tinyint |1 |是 |
|live_id |直播主表id |bigint |null |是 |
|send_state |发送状态：0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败 |tinyint |0 |是 |
|msg_id |企业群发消息的id |varchar |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |更新人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |是 |

### 观看成员(we_live_watch_user)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|watch_user_name |观看成员名 |varchar |null |是 |
|watch_user_type |1:员工;2:客户 |tinyint |null |是 |
|is_company_customer |是否为企业客户 1是,0否

 |tinyint |null |是 |
|watch_user_id |观看用户id |varchar |null |是 |
|watch_avatar |头像 |varchar |null |是 |
|watch_time |观看时长 |varchar |null |是 |
|live_id |直播id |bigint |null |是 |
|remarks |光看人员类型@微信@企业 |varchar |null |是 |
|gender |外部联系人性别 0-未知 1-男性 2-女性 |tinyint |0 |是 |
|is_comment |是否评论。0-否；1-是 |tinyint |0 |是 |
|is_mic |是否连麦发言。0-否；1-是 |tinyint |0 |是 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |

### 素材(we_material)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|category_id |分类id |bigint |null |是 |
|module_type |素材所属模块(1素材2话术3模板,4关联) |int |1 |是 |
|material_url |本地资源文件地址 |varchar |null |是 |
|content |文本内容、图片文案 |mediumblob |null |是 |
|material_name |图片名称 |varchar |null |是 |
|digest |摘要 |varchar |null |是 |
|cover_url |封面本地资源文件 |varchar |null |是 |
|audio_time |音频时长 |varchar |0 |是 |
|background_img_url |背景图片 |varchar |null |是 |
|type |类型 1 通用海报 2裂变海报 |bigint |null |是 |
|width |背景图片宽 |int |null |是 |
|height |背景图片高 |int |null |是 |
|media_type |资源类型 |varchar |null |是 |
|other_field |拓展字段 |text |null |是 |
|material_status |状态 0 启用 1 不启用 |tinyint |0 |是 |
|front_order |排序 |tinyint |null |是 |
|subassembly |海报组件数组 |text |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0 未删除 1 已删除 |int |0 |是 |
|link_url |轨迹素材生成的H5链接 |varchar |null |是 |
|file_name |文件名 |varchar |null |是 |

### 朋友圈(we_moments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|scope_type |可见类型:1:全部;2:部分; |tinyint |1 |是 |
|moment_id |朋友圈id |varchar |null |不为空 |
|content_type |内容类型 |varchar |null |是 |
|type |朋友圈类型:1:企业动态;2:个人动态 |tinyint |null |是 |
|customer_tag |客户标签，多个使用逗号隔开 |text |null |是 |
|creator |创建人 |varchar |null |是 |
|add_user |添加人,多个使用逗号隔开 |text |null |是 |
|no_add_user |未发送员工，使用逗号隔开 |text |null |是 |
|content |朋友圈部分内容 |varchar |null |是 |
|other_content |附件 |text |null |是 |
|create_by |创建人 |varchar |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|push_time |发布时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0:正常 1:删除 |tinyint |0 |是 |
|is_lw_push |是否是在lw平台发布的:1:是;0:否; |tinyint |0 |是 |
|real_type |真实素材类型 |tinyint |null |是 |
|material_id |素材中心Id |bigint |null |是 |

### 朋友圈互动列表(we_moments_interacte)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|interacte_user_id |互动人员名称id |varchar |null |是 |
|interacte_type |互动类型:0:评论；1:点赞 |tinyint |null |是 |
|moment_id |朋友圈id |varchar |null |是 |
|interacte_user_type |互动人员类型:0:员工；1:客户 |tinyint |null |是 |
|interacte_time |互动时间 |datetime |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |是 |

### 欢迎语模板表(we_msg_tlp)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|category_id |分组id |bigint |null |是 |
|user_ids |使用员工名称，用逗号隔开 |text |null |是 |
|user_names |使用员工名称，逗号隔开 |text |null |是 |
|template_type |1欢迎语模板2群发模板3sop模板 |tinyint |null |是 |
|template_info |模板内容 |text |null |是 |
|tpl_type |模板类型:1:活码欢迎语;2:员工欢迎语;3:入群欢迎语 |tinyint |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 欢迎语模板素材表(we_msg_tlp_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|template_id |模板id |bigint |null |不为空 |
|msg_type |消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  |varchar |null |不为空 |
|content |消息内容 |text |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 老客户标签建群(we_pres_tag_group)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|task_id |老客户标签建群任务id |bigint |null |不为空 |
|task_name |任务名称 |varchar |null |不为空 |
|send_type |发送方式 0: 企业群发 1：个人群发 |tinyint |0 |是 |
|group_code_id |群活码id |bigint |null |不为空 |
|send_scope |发送范围 0: 全部客户 1：部分客户 |tinyint |0 |不为空 |
|send_gender |发送性别 0: 全部 1： 男 2： 女 3：未知 |int |0 |不为空 |
|cus_begin_time |目标客户被添加起始时间 |datetime |null |是 |
|cus_end_time |目标客户被添加结束时间 |datetime |null |是 |
|welcome_msg |加群引导语 |varchar |null |不为空 |
|message_template_id |群发消息的id |bigint |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |逻辑删除字段， 0:未删除 1:已删除 |tinyint |0 |是 |

### 老客标签建群使用范围表(we_pres_tag_group_scope)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|task_id |老客户标签建群任务id |bigint |null |不为空 |
|we_user_id |员工id |varchar |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |

### 老客标签建群客户统计表(we_pres_tag_group_stat)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|task_id |老客标签建群任务id |bigint |null |不为空 |
|external_userid |客户id |varchar |null |不为空 |
|user_id |跟进者id |varchar |null |是 |
|sent |是否送达 |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |

### 老客标签建群标签关联表(we_pres_tag_group_tag)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|task_id |老客户标签建群任务id |bigint |null |不为空 |
|tag_id |标签id |varchar |null |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |

### 商品信息表(we_product)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|product_id |商品ID |varchar |null |是 |
|picture |图片 |varchar |null |不为空 |
|describe |描述 |varchar |null |是 |
|price |商品的价格，单位为分；最大不超过5万元 |varchar |null |不为空 |
|product_sn |商品编码；只能输入数字和字母 |varchar |null |不为空 |
|attachments |商品附件 |text |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 商品订单每日统计表(we_product_day_statistics)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|day_order_total_num |订单总数 |int |0 |不为空 |
|day_order_total_fee |订单总金额 |varchar |0 |不为空 |
|day_refund_total_fee |退款总额 |varchar |0 |不为空 |
|day_net_income |净收入 |varchar |0 |不为空 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 商品订单表(we_product_order)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|order_no |订单号 |varchar |null |不为空 |
|mch_no |商户单号 |varchar |null |不为空 |
|order_state |订单状态（1已完成，2已完成有退款） |tinyint |null |不为空 |
|total_fee |付款总金额(单位：分) |varchar |null |不为空 |
|pay_time |交易时间 |datetime |null |不为空 |
|product_id |产品Id |bigint |null |不为空 |
|product_num |购买数量 |int |null |不为空 |
|contact |订单联系人 |varchar |null |是 |
|phone |订单联系人电话 |varchar |null |是 |
|address |订单联系人详细地址 |varchar |null |是 |
|external_userid |付款人的userid,微信openid |varchar |null |不为空 |
|external_name |付款人名称 |varchar |null |是 |
|external_avatar |付款人头像 |varchar |null |是 |
|external_type |外部联系人的类型，1微信用户，2企业微信用户 |tinyint |null |不为空 |
|we_user_id |发送员工 |varchar |null |不为空 |
|we_user_name |发送员工名称 |varchar |null |是 |
|mch_name |收款商户名称 |varchar |null |不为空 |
|mch_id |收款商户号 |varchar |null |不为空 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |null |是 |
|payment_type |收款方式。0：在聊天中收款 1：收款码收款 2：在直播间收款 3：用产品图册收款 |tinyint |null |不为空 |
|remark |收款备注 |varchar |null |是 |
|total_refund_fee |退款总金额 |varchar |null |不为空 |

### 商品订单退款表(we_product_order_refund)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|order_no |订单号 |varchar |null |不为空 |
|refund_no |退款单号 |varchar |null |不为空 |
|refund_time |退款发起时间 |datetime |null |不为空 |
|refund_user_id |退款发起人Id |varchar |null |不为空 |
|refund_user_name |退款发起人姓名 |varchar |null |是 |
|remark |退款备注 |varchar |null |是 |
|refund_fee |退款金额 |varchar |null |不为空 |
|refund_state |退款状态 |tinyint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |是 |

### 商品订单统计表(we_product_statistics)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|order_total_num |订单总数 |int |0 |不为空 |
|order_total_fee |订单总金额：单位分 |varchar |0 |不为空 |
|refund_total_fee |退款总额：单位分 |varchar |0 |不为空 |
|net_income |净收入：单位分 |varchar |0 |不为空 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |是否删除:0有效,1删除 |tinyint |0 |不为空 |

### 活码附件表(we_qr_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|tenant_id |租户ID |int |null |是 |
|qr_id |活码id |bigint |null |不为空 |
|msg_type |消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  |varchar |null |不为空 |
|content |消息内容 |mediumblob |null |是 |
|media_id |媒体id |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |
|real_type |素材真实类型 |tinyint |null |是 |
|material_id |素材Id |bigint |null |是 |

### 活码信息表(we_qr_code)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|name |活码名称 |varchar |single |不为空 |
|group_id |活码分组id |bigint |null |是 |
|auto_add |添加是否无需验证 0：否 1：是 |tinyint |0 |是 |
|type |活码类型 1：单人 2：多人 |tinyint |0 |是 |
|rule_type |排期类型 1：全天 2：自定义 |tinyint |0 |是 |
|state |添加渠道 |varchar |0 |是 |
|scan_num |扫码次数 |int |0 |是 |
|config_id |二维码配置id |varchar |0 |是 |
|qr_code |二维码地址 |varchar |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 活码使用范围表(we_qr_scope)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|qr_id |活码id |bigint |null |不为空 |
|scope_id |排期分组id |varchar |null |不为空 |
|type |消息类型 0 默认排期 1 自定义排期 |tinyint |0 |不为空 |
|scope_type |范围类型 1-员工 2-部门 |tinyint |0 |不为空 |
|party |部门id |varchar |null |是 |
|user_id |员工id |varchar |null |是 |
|work_cycle |周期时间 |varchar |null |是 |
|begin_time |开始时间 |varchar |null |是 |
|end_time |结束时间 |varchar |null |是 |
|status |启用状态 0 未启用 1 启用 |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 活码标签关联表(we_qr_tag_rel)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|qr_id |活码id |bigint |null |不为空 |
|tag_id |标签id |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 红包(we_red_envelopes)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|money |金额分存储 |int |0 |是 |
|scene_type |1:客户;2:客群;3:客户与客群 |tinyint |null |是 |
|name |红包名称 |varchar |null |是 |
|status |0:启用;1:停用 |tinyint |0 |是 |
|send_times |发送次数 |int |0 |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|red_envelopes_type |红包类型:0:企业红包;1:个人红包 |tinyint |0 |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 红包限制(we_red_envelopes_limit)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|single_day_pay |单日付款总额 |int |null |是 |
|single_customer_receive_num |单日客户收红包次数 |int |null |是 |
|single_customer_receive_money |单日每客户收红包总额 |int |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 客户红包发放记录(we_red_envelopes_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|we_user_id |员工id |varchar |null |是 |
|open_id |领取人公众号id |varchar |null |是 |
|chat_id |群id |varchar |null |是 |
|red_envelope_num |红包个数 |int |null |是 |
|err_code |领取错误码 |varchar |null |是 |
|receive_type |红包领取人:1:好友客户;2:群成员 |tinyint |1 |是 |
|red_envelope_money |红包金额 |int |null |是 |
|red_envelope_name |红包名称 |varchar |null |是 |
|red_envelope_type |1: 普通红包2:拼手气红包 |tinyint |1 |是 |
|send_state |发送状态:1:待领取;2:已领取;3:发放失败;4:退款中;5:已退款 |tinyint |null |是 |
|receive_order_no |群客户领取订单号 |varchar |null |是 |
|order_no |交易订单号 |varchar |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|err_code_des |红包领取错误原因 |varchar |null |是 |
|from_type |红包发布自:1:个人发送；2:模版发送 |tinyint |1 |是 |
|avatar |领取人头像 |varchar |null |是 |
|reason |详细原因 |tinytext |null |是 |
|receive_name |领取人姓名 |varchar |null |是 |
|source |红包来源1:群红包;2:群成员或个人客户 |tinyint |1 |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 敏感词设置表(we_sensitive)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|strategy_name |策略名称 |varchar |null |不为空 |
|pattern_words |匹配词 |text |null |不为空 |
|audit_user_id |审计人id |varchar |null |不为空 |
|audit_user_name |审计人 |varchar |null |不为空 |
|alert_flag |消息通知,1 开启 0 关闭 |tinyint |1 |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 敏感行为表(we_sensitive_act)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|act_name |敏感行为名称 |varchar |null |不为空 |
|order_num |排序字段 |int |0 |不为空 |
|enable_flag |记录敏感行为,1 开启 0 关闭 |tinyint |1 |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 敏感行为记录表(we_sensitive_act_hit)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|operator_id |敏感行为操作人id |varchar |null |不为空 |
|operator |敏感行为操作人 |varchar |null |不为空 |
|operate_target_id |敏感行为操作对象id |varchar |null |不为空 |
|operate_target |敏感行为操作对象 |varchar |null |不为空 |
|sensitive_act_id |敏感行为id |bigint |null |不为空 |
|sensitive_act |敏感行为名称 |varchar |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 敏感词审计范围(we_sensitive_audit_scope)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|sensitive_id |敏感词表主键 |bigint |null |不为空 |
|scope_type |审计范围类型, 1 组织机构 2 成员 |tinyint |null |不为空 |
|audit_scope_id |审计对象id |text |null |不为空 |
|audit_scope_name |审计对象名称 |text |null |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 短链信息表(we_short_link)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|jump_type |跳转类型 1-微信 2-其他 |tinyint |1 |不为空 |
|extension_type |推广类型 1-公众号 2-个人微信 3-企业微信 4-小程序 |tinyint |null |是 |
|touch_type |1-文章 2-二维码 |tinyint |null |是 |
|short_link_name |短链名称 |varchar |null |不为空 |
|long_link |长链接 |varchar |null |是 |
|scheme_link |短链接 |varchar |null |是 |
|type |业务类型 1-公众号二维码 2-个人二维码 3-群二维码 4-员工活码 5-客群活码 6-门店导购活码 7-个人小程序 8-门店群活码 9-企业小程序 10-小程序二维码 |tinyint |null |是 |
|name |名称 |varchar |null |是 |
|describe |描述 |varchar |null |是 |
|avatar |头像 |varchar |null |是 |
|qr_code_id |二维码ID |varchar |null |是 |
|qr_code |二维码地址 |varchar |null |是 |
|app_id |小程序或公众号ID |varchar |null |是 |
|secret |小程序密钥 |varchar |null |是 |
|status |状态 1-启用 2-关闭 |tinyint |null |是 |
|term_time |有效期 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 短链统计表(we_short_link_stat)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|short_id |短链ID |bigint |null |不为空 |
|date_time |日期 |datetime |null |不为空 |
|pv_num |PV数量 |int |0 |是 |
|uv_num |UV数量 |int |0 |是 |
|open_num |打开小程序数量 |int |0 |是 |
|remark |备用 |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人ID |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1 删除 |tinyint |0 |是 |

### sop素材附件(we_sop_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|sop_base_id |sop基础id |bigint |null |不为空 |
|sop_push_time_id |推送时间周期,如果该字段为空，则表示附件素材为sop完成以后需要执行的任务 |bigint |null |是 |
|source |来源 1:手动添加的 2:设置sop结束条件时附加的素材 |tinyint |1 |是 |
|msg_type |消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file  |varchar |null |不为空 |
|content |消息内容 |varchar |null |是 |
|media_id |媒体id |varchar |null |是 |
|title |消息标题 |varchar |null |是 |
|description |消息描述 |varchar |null |是 |
|file_url |文件路径 |varchar |null |是 |
|link_url |消息链接 |varchar |null |是 |
|pic_url |消息图片地址 |varchar |null |是 |
|app_id |小程序appid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### Sop base表(we_sop_base)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|base_type |sop基础类型(1:客户sop;2:客群sop) |tinyint |1 |是 |
|business_type |sop业务类(1:新客sop;2:活动节日sop;3:客户转化sop;4:新群培育sop;5:周期营销sop;6:特定宣发sop) |tinyint |1 |是 |
|sop_name |sop名称 |varchar |null |是 |
|sop_state |sop状态(1:执行中;2:暂停) |tinyint |1 |是 |
|send_type |sop发送类型(1:企业微信发送;2:手动发送) |tinyint |null |是 |
|execute_we_user |执行成员条件,如果为空,则为全部成员 |json |null |是 |
|execute_we_user_ids |符合条件执行成员id,多个id逗号隔开 |text |null |是 |
|execute_customer_or_group |被执行的客户或群条件,如果为空,则为全部客户或群 |json |null |是 |
|execute_customer_swipe |只有生效客户为部分客户的时候该条件有效，其他包括全部客户，全部群，部门群该字段都不适用，主要为了便于人群那块做计算 |json |null |是 |
|early_end |是否提前结束:1:提前结束;0:不提前结束 |tinyint |0 |是 |
|end_content |sop结束内容 |json |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |是 |

### 目标执行对象表(we_sop_execute_target)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|sop_base_id |sop主键 |bigint |null |是 |
|execute_we_user_id |执行根据成员id，无论选择部门还是啥最终都落实到具体员工 |varchar |null |是 |
|execute_end_time |sop执行结束时间 |datetime |null |是 |
|target_type |目标类型1:客户 2:群 |tinyint |null |是 |
|target_id |目标id |varchar |null |是 |
|execute_state |sop执行的状态(1:进行中;2:提前结束;3:正常结束;4:异常结束) |tinyint |1 |是 |
|execute_sub_state |0:当前sop下一条任务信息都未推送(待推送);1:当前sop下信息推送完(已推送) |tinyint |0 |是 |
|add_customer_or_create_goup_time |执行人添加该客户的时间或创建该群时间 |datetime |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |是 |

### 目标执行内容(we_sop_execute_target_attachments)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|execute_target_id |目标执行对象主键 |bigint |null |是 |
|sop_attachment_id |执行内容的主键 |bigint |null |是 |
|push_time_type |推送时间类型(1:特定时间推送，比如2022-08-21推送日期;
2:周期推送，数字字符串型1-7，对应周一到周日;3:相对推送时间,数字字符串型，比如2022-08-21添加的客户，那么相对这个时间第一天推送，则值为1，但是对应的实际推送时间为，2022-08-22) 注:此处只供前端做展示 |tinyint |null |是 |
|push_time_pre |推送时间前缀，分为数字型跟日期格式行字符串 注:前端做展示 |varchar |null |是 |
|push_start_time |推送具体开始时间 |datetime |null |是 |
|push_end_time |推送具体结束时间 |datetime |null |是 |
|execute_time |实际推送时间执行完成时间 |datetime |null |是 |
|execute_state |执行状态(0:未执行;1:已执行) |tinyint |0 |是 |
|send_type |1:企业微信发送;2:手动发送
 |tinyint |1 |是 |
|is_push_on_time |是否准时推送(0:准时推送;1:迟到推送;) |tinyint |null |是 |
|is_tip |是否发送:0:未发送;1:已发送全局提醒;2:已发送到期提醒 |tinyint |0 |是 |
|msg_id |企业群发消息的id，可用于获取群发消息发送结果,手动发送方式没有 |varchar |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |是 |

### (we_sop_push_time)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|push_start_time |推送开时间 |time |null |是 |
|push_end_time |推送结束时间 |time |null |是 |
|sop_base_id |sop主键 |bigint |null |是 |
|push_time_type |推送时间类型(1:特定时间推送，比如2022-08-21推送日期;
2:周期推送，数字字符串型1-7，对应周一到周日;3:相对推送时间,数字字符串型，比如2022-08-21添加的客户，那么相对这个时间第一天推送，则值为1，但是对应的实际推送时间为，2022-08-22) |tinyint |null |是 |
|push_time_pre |推送时间前缀，分为数字型跟日期格式行字符串 |varchar |null |是 |
|create_by |创建人名称 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人名称 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |是 |

### 门店活码(we_store_code)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|store_name |门店名称 |varchar |null |是 |
|province_id |省id |int |null |是 |
|state |渠道id |varchar |null |是 |
|city_id |市id |int |null |是 |
|area_id |区id |int |null |是 |
|area |省/市/区 |varchar |null |是 |
|address |详细地址 |varchar |null |是 |
|longitude |纬度 |varchar |null |是 |
|latitude |经度 |varchar |null |是 |
|shop_guide_id |导购id(we_user_id)，多个使用逗号隔开 |text |null |是 |
|shop_guide_name |导购名称，多个使用逗号隔开 |text |null |是 |
|shop_guide_url |导购活码url |varchar |null |是 |
|group_code_url |群活码 |varchar |null |是 |
|group_code_id |群活码id |bigint |null |是 |
|group_code_name |群活码名称 |varchar |null |是 |
|store_state |门店状态(0:启用;1:关闭) |tinyint |1 |是 |
|config_id |企业微信返回的configid |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 门店对应的活码配置(门店导购码;门店群活码)(we_store_code_config)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|out_of_range_tip |超范围提示 |varchar |null |不为空 |
|welcome_msg |入群欢迎语 |varchar |null |是 |
|code_state |客服活码状态(0:启用;1:关闭) |tinyint |1 |是 |
|code_url |客服活码url |varchar |null |是 |
|state |渠道id |varchar |null |是 |
|tag_ids |客户标签id,多个使用逗号隔开 |text |null |是 |
|tag_names |客户标签名,多个使用逗号隔开 |text |null |是 |
|raidus |方圆多少公里 |varchar |null |是 |
|customer_service_id |客服id |varchar |null |是 |
|store_code_type |门店码类型(1:门店导购码;2:门店群活码) |tinyint |1 |是 |
|customer_service_url |客服二维码url |varchar |null |是 |
|customer_service_name |客服名称 |varchar |null |是 |
|store_code_config_url |导购或群对应唯一二维码 |varchar |null |是 |
|store_code_config_qr |导购或群对应唯一二维码 |varchar |null |是 |
|config_id |客服id |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 门店活码统计相关(we_store_code_count)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|store_code_id |门店活码id |bigint |null |是 |
|unionid |用户的Unionid |varchar |null |是 |
|source |来源(1:导购码;2:群码) |tinyint |null |是 |
|current_lng |当前纬度 |varchar |null |是 |
|current_lat |当前经度 |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

### 同步记录表(we_synch_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|synch_type |同步类型:1:客户模块同步 2-客群 |tinyint |null |是 |
|synch_time |同步时间 |datetime |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by_id |更新人id |bigint |null |是 |

### 企业微信标签(we_tag)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|tag_id |微信端返回的id |varchar |null |是 |
|group_id |标签组id |varchar |null |是 |
|name |标签名 |varchar |null |是 |
|tag_type |1:客户企业标签;2:群标签;3:客户个人标签 |tinyint |1 |是 |
|create_time |创建时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|owner |标签所属人 |varchar |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 标签组(we_tag_group)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|group_id |企业微信返回的id |varchar |null |是 |
|group_name |标签组名 |varchar |null |是 |
|group_tag_type |标签分组类型(1:客户企业标签;2:群标签;3:客户个人标签) |tinyint |1 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |
|tag_source |标签来源(1:应用自建;2:企业微信后台建立) |tinyint |1 |是 |
|owner |标签所属人 |varchar |admin |是 |

### 话术素材关联表(we_talk_material)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|talk_id |话术ID |bigint |null |不为空 |
|material_id |素材ID |bigint |null |不为空 |
|sort |排序 |int |null |是 |

### 任务宝表(we_task_fission)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|fission_type |活动类型，1 任务宝 2 群裂变 |int |1 |不为空 |
|task_name |任务活动名称 |varchar | |不为空 |
|fiss_info |裂变引导语 |varchar |null |是 |
|fiss_num |裂变客户数量 |int |1 |不为空 |
|start_time |活动开始时间 |datetime |null |不为空 |
|over_time |活动结束时间 |datetime |null |不为空 |
|customer_tag_id |客户标签id列表，当为全部时保存为all |mediumtext |null |不为空 |
|customer_tag |客户标签名称列表，为all是可为空 |mediumtext |null |是 |
|posters_id |海报id |bigint |null |是 |
|posters_url |裂变海报路径 |varchar | |不为空 |
|fission_target_id |任务裂变目标员工/群裂变id |varchar | |不为空 |
|fission_target |任务裂变目标员工姓名/群裂变二维码地址 |varchar | |不为空 |
|fiss_qrcode |任务裂变目标二维码 |varchar | |不为空 |
|reward_url |兑奖链接 |varchar | |不为空 |
|reward_image_url |兑奖链接图片 |varchar | |不为空 |
|reward_rule |兑奖规则 |mediumtext |null |是 |
|fiss_status |任务裂变活动状态，0 未开始 1 进行中 2 已结束 |tinyint |0 |不为空 |
|welcome_msg |新客欢迎语 |mediumtext |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |
|remark |备注 |varchar |null |是 |

### 裂变任务完成记录(we_task_fission_complete_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|task_fission_id |任务裂变表id |bigint |null |不为空 |
|fission_record_id |任务裂变记录表id |bigint |null |不为空 |
|customer_id |裂变客户id |varchar |null |不为空 |
|customer_name |裂变客户姓名 |varchar |null |是 |
|customer_avatar |客户头像 |varchar |null |是 |
|status |状态 0 有效 1无效 |tinyint |1 |不为空 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 裂变任务记录(we_task_fission_record)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|task_fission_id |任务裂变表id |bigint |null |不为空 |
|customer_id |裂变任务客户id |varchar |null |不为空 |
|customer_name |裂变任务客户姓名 |varchar |null |是 |
|fiss_num |裂变客户数量 |int |0 |不为空 |
|qr_code |二维码链接 |varchar |null |是 |
|config_id |活码ID |varchar |null |是 |
|qr_status |活码有效状态 0有效 1无效 |tinyint |0 |是 |
|complete_time |完成时间 |datetime |null |是 |
|poster |海报链接 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 任务裂变奖励(we_task_fission_reward)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|task_fission_id |任务裂变id |bigint |null |不为空 |
|reward_code |兑奖码 |varchar |NULL |不为空 |
|reward_code_status |兑奖码状态，0 未使用 1 已使用 |tinyint |0 |不为空 |
|reward_user_id |兑奖用户id |varchar |null |是 |
|reward_user |兑奖人姓名 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 裂变任务员工列表(we_task_fission_staff)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|task_fission_id |任务裂变表id |bigint |null |不为空 |
|staff_type |员工或机构，1 组织机构 2 成员 3 全部 |tinyint |null |不为空 |
|staff_id |员工或组织机构id,为全部时为空 |varchar |null |是 |
|staff_name |员工或组织机构姓名，类型为全部时，为空 |varchar |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |删除标识 0 正常 1 删除 |tinyint |0 |不为空 |

### 模板素材关联表(we_tlp_material)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|tlp_id |模板ID |bigint |null |不为空 |
|material_id |素材ID |bigint |null |不为空 |

### 轨迹素材隐私政策客户授权表(we_track_material_privacy_auth)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|openid |查看人openid |varchar |null |是 |
|unionid |查看人uniodid |varchar |null |是 |
|is_auth |是否授权(0否，1是) |tinyint |1 |是 |
|auth_time |授权时间 |datetime |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 未删除 1 已删除 |int |null |是 |

### unionid与external_userid关联表(we_unionid_external_userid_relation)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|openid |微信客户的openid |varchar |null |是 |
|unionid |微信客户的unionid |varchar |null |是 |
|external_userid |该授权企业的外部联系人ID |varchar |null |是 |
|pending_id |临时外部联系人ID |varchar |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |0 未删除 1 已删除 |int |0 |是 |

### 联系客户统计数据 (we_user_behavior_data)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|user_id |成员id |varchar |null |是 |
|stat_time |数据日期，为当日0点的时间戳 |datetime |null |是 |
|new_apply_cnt |发起申请数 |int |null |是 |
|new_contact_cnt |新增客户数，成员新添加的客户数量 |int |null |是 |
|chat_cnt |聊天总数， 成员有主动发送过消息的单聊总数 |int |null |是 |
|message_cnt |发送消息数，成员在单聊中发送的消息总数 |int |null |是 |
|reply_percentage |已回复聊天占比，浮点型，客户主动发起聊天后，成员在一个自然日内有回复过消息的聊天数/客户主动发起的聊天数比例，不包括群聊，仅在确有聊天时返回 |varchar |null |是 |
|avg_reply_time |平均首次回复时长 |int |null |是 |
|negative_feedback_cnt |删除/拉黑成员的客户数，即将成员删除或加入黑名单的客户数 |int |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |CURRENT_TIMESTAMP |不为空 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |
|del_flag |删除标识 0 有效 1删除 |tinyint |0 |不为空 |

### 员工发送红包限额(we_user_red_envelops_limit)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|we_user_id |员工id |varchar |null |不为空 |
|single_customer_receive_num |单日每员工发红包次数 |int |null |是 |
|single_customer_receive_money |单日每员工发红包总额 |int |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_time |更新时间 |datetime |null |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|del_flag |0:正常;1:删除; |tinyint |0 |是 |

### 微信用户表(wx_user)
| 字段名 | 字段说明 | 字段类型 | 默认值 | 是否为空 |
|------ |------ |------ |------ |------ |
|id |id |bigint |null |不为空 |
|open_id |微信用户ID |varchar |null |是 |
|union_id |开放平台用户ID |varchar |null |是 |
|nick_name |用户昵称 |varchar |null |是 |
|avatar |头像 |varchar |null |是 |
|sex |性别 0-未知 1-男性 2-女性 |tinyint |0 |不为空 |
|phone |手机号 |varchar |null |是 |
|province |用户个人资料填写的省份 |varchar |null |是 |
|city |普通用户个人资料填写的城市 |varchar |null |是 |
|country |国家，如中国为CN |varchar |null |是 |
|privilege |用户特权信息，json 数组 |varchar |null |是 |
|del_flag |删除标志（0代表存在 1代表删除） |tinyint |0 |是 |
|create_by |创建人 |varchar |null |是 |
|create_by_id |创建人id |bigint |null |是 |
|create_time |创建时间 |datetime |null |是 |
|update_by |更新人 |varchar |null |是 |
|update_by_id |更新人id |bigint |null |是 |
|update_time |更新时间 |datetime |null |是 |

