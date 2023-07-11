   # **sql更新记录**
   #### 注：当前为每次版本升级时涉及到的数据库变更记录
---
### ● 密码重置，重制完以后为123456
  ```
  UPDATE sys_user set PASSWORD='Q4JLD0EN11Anoekx+Iz8AQZX0vWNhVeU106MP8J7gKTUc3gOcEw1othO4xaJth7LwebyDt35o71uPU+jLdskmg==' WHERE user_name='admin'
```

---

---
### ● 密码重置，重制完以后为123456
  ```
  UPDATE sys_user set PASSWORD='Q4JLD0EN11Anoekx+Iz8AQZX0vWNhVeU106MP8J7gKTUc3gOcEw1othO4xaJth7LwebyDt35o71uPU+jLdskmg==' WHERE user_name='admin'
```

---

---
  ### ● 日期：2023.06.09
  ```
  alter table we_qr_code add rule_mode tinyint default 1 null comment '排班方式 1：轮询 2：顺序 3：随机' after rule_type;
alter table we_qr_code add open_spare_user tinyint default 0 null comment '开启备用员工 0：否 1：是' after rule_mode;
alter table we_qr_scope add scheduling_num int default 0 null comment '排班次数' after status;
alter table we_qr_scope add is_spare_user tinyint default 0 null comment '是否备用员工 0：否 1：是' after scheduling_num;
alter table we_qr_code add is_exclusive tinyint default 0 null comment '是否开启同一外部企业客户只能添加同一个员工，开启后，同一个企业的客户会优先添加到同一个跟进人  0-不开启 1-开启' after qr_code;
```

---

---
### ● 日期：2023.06.26
 ```
CREATE TABLE `we_group_code_range` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`code_id` bigint(20) NOT NULL COMMENT '活码ID',
`chat_id` varchar(64)  NOT NULL COMMENT '群聊ID',
`del_flag` tinyint(1) DEFAULT '0' COMMENT '0:正常;1:删除;',
`create_by` varchar(255)  DEFAULT NULL COMMENT '创建人',
`create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
`create_time` datetime DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`),
KEY `cha_id_index` (`code_id`,`chat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户群活码范围';
```
---

---
### ● 日期：2023.07.9
  ```
 ALTER TABLE `we_allocate_customer` ADD COLUMN `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称' AFTER `external_userid`;

ALTER TABLE `we_allocate_customer` ADD COLUMN `takeover_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接替成员名称' AFTER `customer_name`;

ALTER TABLE `we_allocate_customer` ADD COLUMN `takeover_dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接替成员部门名称' AFTER `takeover_name`;

ALTER TABLE `we_allocate_group` ADD COLUMN `chat_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客群名称' AFTER `chat_id`;

ALTER TABLE `we_allocate_group` ADD COLUMN `takeover_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接替员工名称' AFTER `chat_name`;

ALTER TABLE `we_allocate_group` ADD COLUMN `takeover_dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接替员工部门名称' AFTER `takeover_name`;


ALTER TABLE `we_chat_contact_msg` MODIFY COLUMN `room_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '群聊id' AFTER `to_list`;



ALTER TABLE `we_content_view_record` MODIFY COLUMN `is_customer` tinyint(4) NULL DEFAULT 1 COMMENT '是否企业客户 0否1是(当前字段废弃，跟客户表关联查询)' AFTER `resource_type`;

ALTER TABLE `we_content_view_record` MODIFY COLUMN `external_user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外部联系人姓名' AFTER `external_user_id`;



ALTER TABLE `we_form_survey_answer` MODIFY COLUMN `answer` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '答案' AFTER `ip_addr`;



CREATE TABLE `we_group_code_range`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code_id` bigint(20) NOT NULL COMMENT '活码ID',
  `chat_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '群聊ID',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '0:正常;1:删除;',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `cha_id_index`(`code_id`, `chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户群活码范围' ROW_FORMAT = Dynamic;



ALTER TABLE `we_kf_welcome` MODIFY COLUMN `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '欢迎语内容' AFTER `type`;


ALTER TABLE `we_material` ADD COLUMN `pixel_size` bigint(20) NULL DEFAULT NULL COMMENT '像素大小' AFTER `poster_qr_type`;

ALTER TABLE `we_material` ADD COLUMN `memory_size` bigint(20) NULL DEFAULT NULL COMMENT '内存大小' AFTER `pixel_size`;

ALTER TABLE `we_material` MODIFY COLUMN `width` int(11) NULL DEFAULT NULL COMMENT '图片宽（类型为图片时为图片的宽，视频为封面的宽，图文时为封面的宽，小程序为封面的宽，文章时为封面宽，海报时为海报的宽）' AFTER `type`;

ALTER TABLE `we_material` MODIFY COLUMN `height` int(11) NULL DEFAULT NULL COMMENT '图片高（类型为图片时为图片的高，视频为封面的高，图文时为封面的高，小程序为封面的高，文章时为封面高，海报时为海报的高）' AFTER `width`;

ALTER TABLE `we_material` MODIFY COLUMN `media_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资源类型(0图片，1语音，2视频，3文件，4文本，5海报，9图文，10链接，11小程序，12文章)' AFTER `height`;

CREATE TABLE `we_moments_attachments`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
  `is_material` tinyint(1) NOT NULL COMMENT '是否内容中心素材: 0不是 1是',
  `msg_type` tinyint(1) NULL DEFAULT NULL COMMENT '附件类型:0图片 1视频 2链接 3位置',
  `media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企微素材id,有效期3天',
  `media_id_expire` datetime NULL DEFAULT NULL COMMENT '企微素材id失效时间',
  `media_id_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获取企微临时素材后，上传到oss',
  `thumb_media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频封面media_id',
  `thumb_media_id_expire` datetime NULL DEFAULT NULL COMMENT '视频封面media_id的失效时间',
  `thumb_media_id_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频封面media_id的url地址',
  `link_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网页链接标题',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网页链接url',
  `location_latitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地理位置纬度',
  `location_longitude` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地理位置经度',
  `location_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地理位置名称',
  `material_id` bigint(20) NULL DEFAULT NULL COMMENT '素材中心Id',
  `real_type` tinyint(2) NULL DEFAULT NULL COMMENT '真实素材类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈附件' ROW_FORMAT = Dynamic;

CREATE TABLE `we_moments_customer`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
  `moments_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '朋友圈id',
  `user_id` bigint(20) NOT NULL COMMENT '员工id',
  `we_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企微员工id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `external_userid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户id',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `delivery_status` int(11) NULL DEFAULT NULL COMMENT '送达状态 0已送达 1未送达',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识 0:正常 1:删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `moments_task_id`(`moments_task_id`, `moments_id`, `user_id`, `external_userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈可见客户' ROW_FORMAT = Dynamic;

CREATE TABLE `we_moments_estimate_customer`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `we_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企微员工id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '员工名称',
  `external_userid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户id',
  `customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `moments_task_id`(`moments_task_id`, `we_user_id`, `external_userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预估朋友圈可见客户' ROW_FORMAT = Dynamic;

CREATE TABLE `we_moments_estimate_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '员工id',
  `we_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企微员工id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `execute_count` int(11) NULL DEFAULT 0 COMMENT '提醒执行次数',
  `execute_status` tinyint(1) NOT NULL COMMENT '执行状态:0未执行，1已执行',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '预估朋友圈执行员工' ROW_FORMAT = Dynamic;


ALTER TABLE `we_moments_interacte` ADD COLUMN `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id' AFTER `id`;

ALTER TABLE `we_moments_interacte` ADD COLUMN `we_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业发表成员userid' AFTER `moment_id`;

ALTER TABLE `we_moments_interacte` MODIFY COLUMN `id` bigint(11) NOT NULL COMMENT '主键' FIRST;

ALTER TABLE `we_moments_interacte` MODIFY COLUMN `moment_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '朋友圈id' AFTER `moments_task_id`;

CREATE TABLE `we_moments_task`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT '主键ID',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '朋友圈类型:0:企业动态;1:个人动态',
  `send_type` tinyint(1) NOT NULL COMMENT '发送方式: 0企微群发，1个人发送，2成员群发',
  `is_lw_push` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否是在lw平台发布的:1:是;0:否;',
  `scope_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '发送范围: 0全部客户 1按条件筛选',
  `customer_num` int(11) NULL DEFAULT 0 COMMENT '朋友圈可见客户数',
  `dept_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门id集合',
  `post_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '岗位id集合',
  `user_ids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工id集合',
  `customer_tag` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '客户标签，多个使用逗号隔开',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '朋友圈部分内容',
  `execute_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `execute_end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `like_tag_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '点赞标签',
  `comment_tag_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '评论标签',
  `status` tinyint(4) NOT NULL DEFAULT 3 COMMENT '任务状态：1未开始，2进行中，3已结束',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标识 0:正常 1:删除',
  `establish_time` datetime NULL DEFAULT NULL COMMENT '企微创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '朋友圈' ROW_FORMAT = Dynamic;

CREATE TABLE `we_moments_task_relation`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `moment_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
  `job_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异步任务id，24小时有效',
  `job_id_expire` datetime NULL DEFAULT NULL COMMENT '异步任务id失效时间',
  `moment_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '朋友圈id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈任务和企微朋友圈关联表' ROW_FORMAT = Dynamic;

CREATE TABLE `we_moments_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
  `moments_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '朋友圈id',
  `user_id` bigint(20) NOT NULL COMMENT '员工id',
  `we_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '企微员工id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `execute_status` tinyint(1) NOT NULL COMMENT '执行状态:0未执行，1已执行',
  `execute_count` int(11) NOT NULL COMMENT '提醒执行次数',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(4) NULL DEFAULT NULL COMMENT '删除标识 0:正常 1:删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友圈执行员工' ROW_FORMAT = Dynamic;

ALTER TABLE `we_msg_tlp` ADD COLUMN `template_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '欢迎语模版id' AFTER `category_id`;



ALTER TABLE `we_qi_rule_msg` MODIFY COLUMN `room_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群聊ID' AFTER `receive_id`;



DROP TABLE IF EXISTS `we_moments`;

ALTER TABLE `we_allocate_customer` ADD COLUMN `leave_user_id` bigint(20) NULL DEFAULT NULL COMMENT '离职表主键 ' AFTER `id`;

ALTER TABLE `we_allocate_group` ADD COLUMN `leave_user_id` bigint(20) NULL DEFAULT NULL COMMENT '离职表主键' AFTER `id`;

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2451, '朋友圈管理', 2198, 1, 'list', 'friendCircle/list', 1, 'C', '0', '0', 'friendCircle:list', '#', 'admin', NULL, '2023-07-05 15:27:18', NULL, NULL, NULL, '');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2452, '任务统计', 2198, 2, 'statistics', 'friendCircle/statistics', 1, 'C', '1', '0', 'friendCircle:statistics', '#', 'admin', NULL, '2023-07-05 15:28:40', NULL, NULL, NULL, '');

UPDATE `sys_menu` SET `menu_name` = '员工动态', `parent_id` = 2198, `order_num` = 45, `path` = 'circle', `component` = 'friendCircle/index', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'friendCircle:index', `icon` = 'build', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2021-11-24 17:48:12', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-07-05 15:26:32', `remark` = '' WHERE `menu_id` = 2190;

UPDATE `sys_menu` SET `menu_name` = '企业动态', `parent_id` = 2198, `order_num` = 44, `path` = 'enterprise', `component` = 'friendCircle/enterprise', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'friendCircle:enterprise', `icon` = 'dict', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2021-11-24 17:57:13', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-07-05 15:26:27', `remark` = '' WHERE `menu_id` = 2191;

UPDATE `sys_menu` SET `menu_name` = '微信客服', `parent_id` = 2052, `order_num` = 4, `path` = 'customerService', `component` = NULL, `is_frame` = 1, `menu_type` = 'M', `visible` = '1', `status` = '0', `perms` = '', `icon` = 'wechat', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2022-01-09 21:06:51', `update_by` = 'lw', `update_by_id` = NULL, `update_time` = '2023-07-07 16:50:25', `remark` = '' WHERE `menu_id` = 2199;

UPDATE `sys_menu` SET `menu_name` = '私域洞察', `parent_id` = 2279, `order_num` = 2, `path` = 'operateAnalysis', `component` = NULL, `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = 'card', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2022-01-09 21:46:35', `update_by` = 'lw', `update_by_id` = NULL, `update_time` = '2023-07-09 15:38:06', `remark` = '' WHERE `menu_id` = 2204;

UPDATE `sys_menu` SET `menu_name` = '场景管理', `parent_id` = 2199, `order_num` = 2, `path` = 'sceneManage', `component` = 'customerService/sceneManage/index', `is_frame` = 1, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'service:scene:index', `icon` = 'build', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2022-01-10 17:06:42', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-05-30 13:43:09', `remark` = '' WHERE `menu_id` = 2208;

UPDATE `sys_menu` SET `menu_name` = '咨询记录', `parent_id` = 2199, `order_num` = 3, `path` = 'searchRecord', `component` = 'customerService/searchRecord/index', `is_frame` = 1, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'service:search:index', `icon` = 'checkbox', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2022-01-12 14:19:24', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-05-30 13:43:22', `remark` = '' WHERE `menu_id` = 2214;

UPDATE `sys_menu` SET `menu_name` = '表单统计', `parent_id` = 2293, `order_num` = 3, `path` = 'smartFormStatistics', `component` = 'drainageCode/smartForms/statistics/index', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'drainageCode/smartForms/statistics/index', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2022-10-21 16:17:14', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-05-28 17:05:27', `remark` = '' WHERE `menu_id` = 2297;

UPDATE `sys_menu` SET `menu_name` = '列表', `parent_id` = 2002, `order_num` = 0, `path` = 'list', `component` = 'customerManage/customer', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-02-28 17:50:38', `update_by` = 'lw', `update_by_id` = NULL, `update_time` = '2023-06-12 15:47:42', `remark` = '' WHERE `menu_id` = 2372;

UPDATE `sys_menu` SET `menu_name` = '商城中心', `parent_id` = 0, `order_num` = 8, `path` = 'microStore/managerShop', `component` = NULL, `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '1', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-03-13 15:43:42', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-06-19 12:23:41', `remark` = '' WHERE `menu_id` = 2408;

UPDATE `sys_menu` SET `menu_name` = '{新增}', `parent_id` = 2443, `order_num` = 2, `path` = 'add', `component` = 'conversation/quality/add', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-05-26 10:08:42', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-07-07 14:58:00', `remark` = '' WHERE `menu_id` = 2445;

```



---