   # **sql更新记录**
   #### 注：当前为每次版本升级时涉及到的数据库变更记录
---

---
### ● 密码重置，重制完以后为123456
  ```
UPDATE sys_user 
SET PASSWORD = 'jTk6muJjbpIDarA1fKPulLHvjwjF+Yv1IrUeJOkU0CEAbfPowtswo0akbokVTl6DMoH8OhDQtXwOAxqrsaAT8A==' 
WHERE
	user_name = 'admin'

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
### ● 日期：2023.08.22 【线索中心|获客助手】
```
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2455, '私域孵化', 2366, 2, 'privateIncubation', NULL, 1, 'M', '0', '0', '', '#', 'admin', NULL, '2023-08-07 11:38:36', 'admin', NULL, '2023-08-07 12:16:26', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2456, '线索公海', 2455, 5, 'clueHighseas', NULL, 1, 'M', '0', '0', NULL, '#', 'admin', NULL, '2023-08-07 11:39:09', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2457, '列表', 2456, 5, 'index', 'clue/clueHighseas/index', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-08-07 11:40:11', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2458, '{新增}公海', 2456, 10, 'addEdit', 'clue/clueHighseas/addEdit', 1, 'C', '1', '0', '', '#', 'admin', NULL, '2023-08-07 11:41:01', 'admin', NULL, '2023-08-07 11:41:32', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2459, '公海统计', 2456, 15, 'highseasStatistics', 'clue/clueHighseas/highseasStatistics', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-08-07 11:42:02', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2460, '客户详情', 2456, 20, 'clueDetail', 'clue/clueHighseas/clueDetail', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-08-07 11:42:43', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2461, '线索模板', 2455, 10, 'clueTemplate', 'clue/clueTemplate/index', 1, 'C', '0', '0', NULL, '#', 'admin', NULL, '2023-08-07 11:43:23', NULL, NULL, NULL, '');

UPDATE `sys_menu` SET `menu_name` = '老客迁移', `parent_id` = 2285, `order_num` = 1, `path` = 'index', `component` = 'drainageCode/highSeas/index', `is_frame` = 1, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2021-08-20 14:20:37', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-08-18 10:22:24', `remark` = '' WHERE `menu_id` = 2187;
UPDATE `sys_menu` SET `menu_name` = '迁移分析', `parent_id` = 2285, `order_num` = 1, `path` = 'statistics', `component` = 'drainageCode/highSeas/statistics', `is_frame` = 1, `menu_type` = 'C', `visible` = '0', `status` = '0', `perms` = 'highSeas:statistics', `icon` = '#', `create_by` = 'admin_prod', `create_by_id` = NULL, `create_time` = '2021-11-21 15:32:40', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-08-18 10:22:04', `remark` = '' WHERE `menu_id` = 2189;
UPDATE `sys_menu` SET `menu_name` = '老客孵化', `parent_id` = 2366, `order_num` = 3, `path` = 'highSeas', `component` = NULL, `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2022-09-12 23:56:37', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-08-18 10:22:51', `remark` = '' WHERE `menu_id` = 2285;


 ALTER TABLE `sys_user` ADD COLUMN `is_open_daily` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否开启动态日报 0开启，1关闭 默认开启0' AFTER `kf_status`;

 DROP TABLE IF EXIST `we_leads_template_settings`
 CREATE TABLE `we_leads_template_settings` (
`id` bigint(20) NOT NULL COMMENT '主键',
`table_entry_name` varchar(255) NOT NULL COMMENT '表项名称',
`table_entry_id` varchar(255) NOT NULL COMMENT '表项Id',
`table_entry_attr` tinyint(4) DEFAULT NULL COMMENT '表项属性 0 填写项 1 下拉项',
`data_attr` tinyint(4) DEFAULT NULL COMMENT '数据属性 0 文本 1 数字 2 日期',
`datetime_type` tinyint(4) DEFAULT NULL COMMENT '日期类型 0 日期 1 日期+时间',
`max_input_len` int(11) DEFAULT NULL COMMENT '输入长度',
`can_edit` tinyint(4) DEFAULT 0 COMMENT '是否可被编辑 0 可被编辑 1 不可被编辑',
`is_required` tinyint(1) DEFAULT NULL COMMENT '是否必填项 0选填 1必填',
`create_time` datetime NOT NULL COMMENT '创建时间',
`rank` int(255) DEFAULT NULL COMMENT '排序',
`update_time` datetime DEFAULT NULL COMMENT '更新时间',
`create_by` varchar(255) NOT NULL COMMENT '创建人',
`update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
`create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
`del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='线索模版配置表';

INSERT INTO `we_leads_template_settings` VALUES (1678322167884226560, '姓名', 'name', 0, 0, NULL, 10, 1, 1, '2023-07-10 16:36:01', 1, '2023-07-10 16:36:01', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_leads_template_settings` VALUES (1679400302667051008, '电话号码', 'phone', 0, 0, NULL, 11, 1, 1, '2023-07-13 16:00:09', 3, '2023-08-03 10:44:50', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_leads_template_settings` VALUES (1691634393458208768, '性别', '1691634393122664448', 1, 0, 0, 100, 1, 0, '2023-08-16 10:14:03', 3, '2023-08-16 10:14:03', 'admin', 'admin', 1, 1, 0);


DROP TABLE IF EXIST `we_leads_template_table_entry_content`
CREATE TABLE `we_leads_template_table_entry_content` (
`id` bigint(20) NOT NULL COMMENT '主键Id',
`leads_template_settings_id` bigint(20) NOT NULL COMMENT '模版表id',
`content` varchar(255) NOT NULL COMMENT '内容',
`create_time` datetime NOT NULL COMMENT '创建时间',
`update_time` datetime DEFAULT NULL COMMENT '修改时间',
`del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='线索模版配置表项内容表';

INSERT INTO `we_leads_template_table_entry_content` VALUES (1691634393802141696, 1691634393458208768, '男', '2023-08-16 10:14:03', NULL, 0);
INSERT INTO `we_leads_template_table_entry_content` VALUES (1691634393802141697, 1691634393458208768, '女', '2023-08-16 10:14:03', NULL, 0);
INSERT INTO `we_leads_template_table_entry_content` VALUES (1679402002626850818, 1691634393458208768, '未知', '2023-07-13 16:06:54', NULL, 0);

DROP TABLE IF EXISTS `we_leads_sea`;
CREATE TABLE `we_leads_sea` (
  `id` bigint NOT NULL COMMENT '主键Id',
  `name` varchar(32)  DEFAULT NULL COMMENT '公海名称',
  `is_auto_recovery` tinyint DEFAULT NULL COMMENT '是否自动回收（1 表示是，0 表示否）',
  `num` int DEFAULT '0' COMMENT '公海线索数',
  `first` int DEFAULT NULL COMMENT '成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255)  NOT NULL COMMENT '创建人',
  `update_by` varchar(255)  DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint NOT NULL COMMENT '创建人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `version` bigint NOT NULL COMMENT '当前规则版本',
  PRIMARY KEY (`id`)
) COMMENT='线索公海';
INSERT INTO `we_leads_sea` VALUES (1, '默认公海', 0, 0, NULL, '2023-07-14 17:18:41', '2023-07-14 17:18:44', 'admin', 'admin', 1, 1, 0, 0);


DROP TABLE IF EXISTS `we_leads_sea_rule_record`;
CREATE TABLE `we_leads_sea_rule_record` (
  `id` bigint NOT NULL COMMENT '主键Id',
  `sea_id` bigint NOT NULL COMMENT '公海id',
  `is_auto_recovery` tinyint NOT NULL COMMENT '是否自动回收（1 表示是，0 表示否）',
  `first` int DEFAULT NULL COMMENT '成员领取线索超过A天未能实现几次跟进，则回收至公海池。且必须在前几天内完成首次跟进。(A)',
  `version` int NOT NULL COMMENT '历史规则版本',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255)  DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint DEFAULT NULL COMMENT '创建人Id',
  PRIMARY KEY (`id`)
) COMMENT='线索公海规则修改记录';

DROP TABLE IF EXISTS `we_leads_sea_visible_range`;
CREATE TABLE `we_leads_sea_visible_range` (
  `id` bigint NOT NULL COMMENT '主键Id',
  `sea_id` bigint NOT NULL COMMENT '公海池Id',
  `type` tinyint NOT NULL COMMENT '类型(0部门 1岗位 2员工)',
  `data_id` varchar(32)  DEFAULT NULL COMMENT '数据Id',
  `data_name` varchar(32)  DEFAULT NULL COMMENT '数据名称',
  `is_admin` tinyint(1) DEFAULT '0' COMMENT '是否管理员（1是，0否）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255)  NOT NULL COMMENT '创建人',
  `update_by` varchar(255)  DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint NOT NULL COMMENT '创建人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) COMMENT='公海可见范围';

DROP TABLE IF EXIST `we_leads`
CREATE TABLE `we_leads` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `phone` char(11) NOT NULL COMMENT '电话号码',
  `leads_status` tinyint(2) NOT NULL COMMENT '线索状态(0待分配，1跟进中，2已上门，3已退回)',
  `labels_ids` varchar(500) DEFAULT NULL COMMENT '备注标签',
  `properties` text COMMENT '自定义属性',
  `sea_id` bigint(20) DEFAULT NULL COMMENT '所属公海',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `create_by_id` bigint(20) NOT NULL COMMENT '创建人id',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `follower_id` bigint(20) DEFAULT NULL COMMENT '当前跟进人Id',
  `we_user_id` varchar(32) DEFAULT NULL COMMENT '当前跟进人企微Id',
  `follower_name` varchar(32) DEFAULT NULL COMMENT '当前跟进人名称',
  `dept_id` varchar(32) DEFAULT NULL COMMENT '当前跟进人部门Id',
  `recovery_times` int(11) NOT NULL DEFAULT '0' COMMENT '回收次数',
  `return_reason` tinyint(2) DEFAULT NULL COMMENT '末次回收原因',
  `source` tinyint(3) DEFAULT NULL COMMENT '线索来源 0excel导入',
  `sex` tinyint(2) DEFAULT NULL COMMENT ' 性别 0 = 未知, 1 = 男, 2 = 女',
  `import_record_id` bigint(20) DEFAULT NULL COMMENT '导入记录id',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户Id',
  `external_userid` varchar(255) DEFAULT NULL COMMENT '客户外部联系人Id',
  `bind_customer_time` datetime NOT NULL COMMENT '绑定客户时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `phone_unique`(`phone`) USING BTREE COMMENT '手机号码索引'
) ENGINE=InnoDB COMMENT='线索';

DROP TABLE IF EXIST `we_leads_follower`
CREATE TABLE `we_leads_follower` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `leads_id` bigint(20) NOT NULL COMMENT '线索Id',
  `follower_id` bigint(20) NOT NULL COMMENT '跟进人id',
  `follower_we_user_id` varchar(32) DEFAULT NULL COMMENT '跟进人企微Id',
  `follower_name` varchar(32) NOT NULL COMMENT '跟进人名称',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '跟进人所属部门Id',
  `dept_name` varchar(32) DEFAULT NULL COMMENT '跟进人所属部门名称',
  `get_type` tinyint(2) NOT NULL COMMENT '领取方式（0指定分配，1主动领取，2线索转接）',
  `follower_status` tinyint(2) NOT NULL COMMENT '跟进状态：0已领取，1跟进中，2已上门，3已退回',
  `return_type` tinyint(2) DEFAULT NULL COMMENT '退回方式 0成员主动退回 1超时自动退回 2管理员强制回收 3离职退回',
  `return_reason` varchar(500) DEFAULT NULL COMMENT '退回原因',
  `assigner_id` bigint(20) NULL COMMENT '分配人id',
  `assigner_name` varchar(32) NULL COMMENT '分配人名称',
  `follower_start_time` datetime DEFAULT NULL COMMENT '跟进开始时间',
  `follower_end_time` datetime DEFAULT NULL COMMENT '跟进结束时间',
  `is_current_follower` tinyint(2) DEFAULT NULL COMMENT '是否当前跟进人（0否，1是）',
  `sea_id` bigint(20) NOT NULL COMMENT '跟进时线索所处的公海Id',
  `is_latest` tinyint(2) DEFAULT NULL COMMENT '是否最新跟进人（0否，1是）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='线索跟进人';

DROP TABLE IF EXIST `we_leads_sea_base_settings`
CREATE TABLE `we_leads_sea_base_settings` (
  `id` bigint(20) NOT NULL,
  `max_claim` int(11) DEFAULT '1' COMMENT '员工每日领取上限',
  `stock_max_claim` int(11) DEFAULT '1' COMMENT '成员客户存量上限',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0 正常 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='公海基础配置表';
INSERT INTO `we_leads_sea_base_settings` VALUES (1, 10, 100, '2023-07-17 17:18:13', NULL, NULL, NULL, NULL, NULL, 0);


DROP TABLE IF EXIST `we_leads_follow_record`
CREATE TABLE `we_leads_follow_record` (
  `id` bigint(20) NOT NULL,
  `we_leads_id` bigint(20) DEFAULT NULL COMMENT '线索id',
  `sea_id` bigint(20) DEFAULT NULL COMMENT '所属公海',
  `follow_user_id` bigint(20) DEFAULT NULL COMMENT '线索跟进人表Id',
  `record_status` tinyint(4) DEFAULT NULL COMMENT '记录状态 0已领取 1跟进中 2已转化 3已退回',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='线索跟进记录';

DROP TABLE IF EXIST `we_leads_records_content`
CREATE TABLE `we_leads_records_content` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `record_id` bigint(20) NOT NULL COMMENT '跟进记录Id',
  `item_key` varchar(32) NOT NULL COMMENT '记录项目名',
  `item_value` varchar(500) NOT NULL COMMENT '记录项目值',
  `rank` int(11) DEFAULT NULL COMMENT '排序',
  `is_visible` tinyint(4) NOT NULL COMMENT '是否显示 0 显示 1 隐藏',
  `is_attachment` tinyint(4) NOT NULL COMMENT '是否存在附件 0否 1是',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父类id,无父类默认值为0',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `replier_from_id` bigint(20) DEFAULT NULL COMMENT '回复者id',
  `replier_from_we_user_Id` varchar(32) DEFAULT NULL COMMENT '回复者企微Id',
  `replier_from` varchar(32) DEFAULT NULL COMMENT '回复者',
  `replier_from_avatar` varchar(255) DEFAULT NULL COMMENT '回复者头像',
  `replier_to_id` bigint(20) DEFAULT NULL COMMENT '回复对象id',
  `replier_to` varchar(32) DEFAULT NULL COMMENT '回复对象',
  `replier_to_we_user_Id` varchar(32) DEFAULT NULL COMMENT '回复对象企微id',
  `replier_to_avatar` varchar(255) DEFAULT NULL COMMENT '回复对象头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='线索跟进记录内容';

DROP TABLE IF EXIST `we_leads_records_attachment`
CREATE TABLE `we_leads_records_attachment` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `content_id` bigint(20) NOT NULL COMMENT '跟进记录内容Id',
  `type` tinyint(2) NOT NULL COMMENT '附件类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)',
  `title` varchar(255) NOT NULL COMMENT '附件名称',
  `url` varchar(255) NOT NULL COMMENT '附件地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索跟进记录附件';

DROP TABLE IF EXIST `we_leads_auto_recovery`
CREATE TABLE `we_leads_auto_recovery` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `leads_id` bigint(20) NOT NULL COMMENT '线索id',
  `follower_id` bigint(20) NOT NULL COMMENT '跟进人id',
  `type` tinyint(2) NOT NULL COMMENT '自动回收类型',
  `recovery_time` datetime NOT NULL COMMENT '回收时间',
  `recovery_reason` tinyint(4) comment '回收原因',
  `executing_state` tinyint(2) NOT NULL COMMENT '执行状态 0待执行 1已执行 2已取消',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='线索自动回收';


DROP TABLE IF EXIST `we_leads_record_content_cooperate_user`
CREATE TABLE `we_leads_record_content_cooperate_user` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `content_id` bigint(20) NOT NULL COMMENT '记录内容id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `we_user_id` varchar(32) DEFAULT NULL COMMENT '用户企微Id',
  `user_name` varchar(32) DEFAULT NULL COMMENT '用户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟进记录内容协作成员';

DROP TABLE IF EXIST `we_leads_import_record`
CREATE TABLE `we_leads_import_record` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `sea_id` bigint(20) DEFAULT NULL COMMENT '所属公海',
  `import_source_file_name` varchar(255) DEFAULT NULL COMMENT '导入的表单或excel的文件名',
  `import_source_type` tinyint(4) DEFAULT NULL COMMENT '导入来源 0 excel 1 智能表单 2 手动新增',
  `form_id` bigint(20) DEFAULT NULL COMMENT '智能表单id，当导入来源类型为智能表单时，这个值不为空',
  `total_num` int(11) DEFAULT NULL COMMENT 'excel的线索总数',
  `success_num` int(11) DEFAULT NULL COMMENT '线索导入的成功数',
  `fail_num` int(11) DEFAULT NULL COMMENT '线索导入的失败数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0 正常 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索导入记录';

DROP TABLE IF EXIST `we_leads_manual_add_record`
CREATE TABLE `we_leads_manual_add_record` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `we_user_id` varchar(64) NOT NULL COMMENT '企微员工Id',
  `leads_id` bigint(20) NOT NULL COMMENT '线索Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索手动入线记录';

DROP TABLE IF EXIST `we_message_notification`
CREATE TABLE `we_message_notification` (
  `id` bigint NOT NULL COMMENT '主键Id',
  `title` varchar(32) NOT NULL COMMENT '通知标题',
  `content` varchar(255) NOT NULL COMMENT '通知内容',
  `notification_time` datetime NOT NULL COMMENT '通知时间',
  `we_user_id` varchar(32) NOT NULL COMMENT '员工Id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `is_read` tinyint(1) DEFAULT NULL COMMENT '是否已读 0未读 1已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='消息通知';


DROP TABLE IF EXIST `we_tasks`
CREATE TABLE `we_tasks` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `user_id` bigint(20) NOT NULL COMMENT '员工id',
  `we_user_id` varchar(32) NOT NULL COMMENT '员工企微Id',
  `type` tinyint(4) NOT NULL COMMENT '任务类型',
  `title` varchar(255) NOT NULL COMMENT '任务标题',
  `content` varchar(500) NOT NULL COMMENT '自定义任务内容',
  `send_time` datetime NOT NULL COMMENT '发送时间',
  `url` varchar(255) DEFAULT NULL COMMENT '链接',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态，0待执行，1已完成，2已取消',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `leads_id` bigint(20) NOT NULL COMMENT '线索中心-线索Id',
  `is_visible` tinyint(1) DEFAULT NULL COMMENT '是否显示（0不显示 1显示）',
  `record_id` bigint(20) DEFAULT NULL COMMENT '线索中心-跟进记录Id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='待办任务';















ALTER TABLE `we_corp_account` ADD COLUMN `customer_link_margin` bigint(20) NULL DEFAULT NULL COMMENT '获客助手剩余可用量' AFTER `customer_churn_notice_switch`;

ALTER TABLE `we_corp_account` ADD COLUMN `customer_link_total` bigint(20) NULL DEFAULT NULL COMMENT '获客助手总量' AFTER `customer_link_margin`;

DROP TABLE IF EXIST `we_customer_link`
CREATE TABLE `we_customer_link`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`link_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '链接名称',
`link_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信返回的获客链接id',
`link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信返回的获客链接',
`state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '渠道标识',
`link_short_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获客链接短链',
`skip_verify` tinyint(4) NULL DEFAULT 1 COMMENT '是否无需验证，默认为true 1:是 0:是',
`we_user_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '此获客链接关联的userid列表，最多可关联100个',
`department_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '此获客链接关联的部门id列表，部门覆盖总人数最多100个',
`tag_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '标签id多个使用逗号隔开',
`create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
`create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
`create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
`update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
`update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
`del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标识 0 有效 1删除',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '获客助手' ROW_FORMAT = Dynamic;

DROP TABLE IF EXIST `we_customer_link_attachments`
CREATE TABLE `we_customer_link_attachments`  (
`id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键id',
`customer_link_id` bigint(11) NOT NULL COMMENT '识客码id',
`msg_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ',
`content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
`media_id` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒体id',
`msg_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业微信端返回的消息id',
`title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
`description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息描述',
`file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
`link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息链接',
`pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息图片地址',
`app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序appid',
`create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
`create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
`update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
`real_type` tinyint(2) NULL DEFAULT NULL COMMENT '素材真实类型',
`material_id` bigint(20) NULL DEFAULT NULL COMMENT '素材id',
`del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标识 0 有效 1删除',
PRIMARY KEY (`id`) USING BTREE,
INDEX `qr_id_IDX`(`customer_link_id`, `del_flag`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1692356624150212610 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活码附件表' ROW_FORMAT = DYNAMIC;


DROP TABLE IF EXIST `we_customer_link_count`
CREATE TABLE `we_customer_link_count`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`external_userid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户external_userid',
`link_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '获客链接id',
`we_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通过获客链接添加此客户的跟进人userid',
`customer_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称',
`customer_type` tinyint(4) NULL DEFAULT NULL COMMENT '客户类型 1:微信用户，2:企业用户\n',
`gender` tinyint(4) NULL DEFAULT NULL COMMENT '0-未知 1-男性 2-女性\n',
`avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户头像',
`user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '添加人名称',
`chat_status` tinyint(4) NULL DEFAULT 0 COMMENT '会话状态，0-客户未发消息 1-客户已发送消息',
`add_time` datetime NULL DEFAULT NULL COMMENT '客户相关添加时间',
`state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用于区分客户具体是通过哪个获客链接进行添加，用户可在获客链接后拼接customer_channel=自定义字符串，字符串不超过64字节，超过会被截断。通过点击带有customer_channel参数的链接获取到的客户，调用获客信息接口或获取客户详情接口时，返回的state参数即为链接后拼接自定义字符串',
`create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
`create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
`create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人名称',
`update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
`update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
`del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标识 0 有效 1删除',
UNIQUE INDEX `onlykey`(`external_userid`, `we_user_id`) USING BTREE COMMENT '联合唯一建'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


ALTER TABLE `we_leads` MODIFY COLUMN `bind_customer_time` datetime NULL DEFAULT NULL COMMENT '绑定客户时间' AFTER `external_userid`;

ALTER TABLE `we_leads` ADD UNIQUE INDEX `phone_unique`(`phone`) USING BTREE COMMENT '手机号码索引';

ALTER TABLE `we_leads_follower` MODIFY COLUMN `assigner_id` bigint(20) NULL DEFAULT NULL COMMENT '分配人id' AFTER `return_reason`;

ALTER TABLE `we_leads_follower` MODIFY COLUMN `assigner_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分配人名称' AFTER `assigner_id`;

DROP TABLE IF EXIST `we_leads_record_attachment`
CREATE TABLE `we_leads_record_attachment`  (
`id` bigint(20) NOT NULL COMMENT '主键Id',
`content_id` bigint(20) NOT NULL COMMENT '跟进记录内容Id',
`type` tinyint(2) NOT NULL COMMENT '附件类型 0 图片（image）、1 语音（voice）、2 视频（video），3 普通文件(file)',
`title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '附件名称',
`url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '附件地址',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '线索跟进记录附件' ROW_FORMAT = Dynamic;

DROP TABLE IF EXIST `we_leads_record_content`
CREATE TABLE `we_leads_record_content`  (
`id` bigint(20) NOT NULL COMMENT '主键Id',
`record_id` bigint(20) NOT NULL COMMENT '跟进记录Id',
`item_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录项目名',
`item_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '记录项目值',
`rank` int(11) NULL DEFAULT NULL COMMENT '排序',
`is_visible` tinyint(4) NOT NULL COMMENT '是否显示  是否显示 0 显示 1 隐藏',
`is_attachment` tinyint(4) NOT NULL COMMENT '是否存在附件 0否 1是',
`parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父类id,无父类默认值为0',
`replier_from_id` bigint(20) NULL DEFAULT NULL COMMENT '回复者id',
`replier_from_we_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复者企微Id',
`replier_from` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复者',
`replier_from_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复者头像',
`replier_to_id` bigint(20) NULL DEFAULT NULL COMMENT '回复对象id',
`replier_to` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复对象',
`replier_to_we_user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复对象企微id',
`replier_to_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回复对象头像',
`create_time` datetime NOT NULL COMMENT '创建时间',
`sub_num` int(11) NULL DEFAULT 0 COMMENT '子类数量',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '线索跟进记录内容' ROW_FORMAT = Dynamic;

ALTER TABLE `we_qr_code` ADD COLUMN `qr_welcome_open` tinyint(4) NULL DEFAULT NULL COMMENT '欢迎语开关 1-不发送欢迎语，2-发送欢迎语' AFTER `qr_code`;

ALTER TABLE `we_qr_code` ADD COLUMN `qr_priority_user_welcome` tinyint(4) NULL DEFAULT NULL COMMENT '是否优先员工欢迎语 0-否，1-是（仅欢迎语开关为2是生效）' AFTER `qr_welcome_open`;

DROP TABLE IF EXISTS `we_substitute_customer_order`;
CREATE TABLE `we_substitute_customer_order` (
  `id` bigint(20) NOT NULL COMMENT '主键Id',
  `purchaser` varchar(255) NOT NULL COMMENT '购买人',
  `phone` char(11) NOT NULL COMMENT '联系方式',
  `source` varchar(20) NOT NULL COMMENT '订单来源',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `dept_id` bigint(20) NOT NULL COMMENT '归属部门',
  `user_id` bigint(20) NOT NULL COMMENT '归属员工',
  `order_status` varchar(20) NOT NULL COMMENT '订单状态',
  `product_name` varchar(255) NOT NULL COMMENT '商品名称',
  `product_url` varchar(1000) DEFAULT NULL COMMENT '商品图片',
  `product_unit_price` decimal(9,2) NOT NULL COMMENT '商品单价',
  `amount` int(11) NOT NULL COMMENT '商品数量',
  `total_price` decimal(10,0) NOT NULL COMMENT '付款总价',
  `discount` varchar(255) DEFAULT NULL COMMENT '付款折扣',
  `discount_amount` decimal(9,2) DEFAULT NULL COMMENT '折扣金额',
  `actual_payment` decimal(9,2) NOT NULL COMMENT '实际付款',
  `returned_money_type` varchar(20) NOT NULL COMMENT '回款方式',
  `returned_money` decimal(9,2) NOT NULL COMMENT '回款金额',
  `returned_date` datetime NOT NULL COMMENT '回款日期',
  `payer` varchar(255) NOT NULL COMMENT '打款人',
  `returned_receipt` varchar(1000) DEFAULT NULL COMMENT '回款凭证',
  `status` tinyint(2) NOT NULL COMMENT '状态：0暂存 1完成',
  `properties` varchar(1000) DEFAULT NULL COMMENT '自定义属性',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) NOT NULL COMMENT '创建人',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
  `create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `external_userid` varchar(255) NOT NULL COMMENT '外部联系人的userid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代客下单-订单';


DROP TABLE IF EXIST `we_substitute_customer_order_catalogue`
CREATE TABLE `we_substitute_customer_order_catalogue`  (
`id` bigint(20) NOT NULL COMMENT '主键Id',
`name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
`sort` int(11) NOT NULL COMMENT '排序',
`is_fixed` tinyint(4) NOT NULL COMMENT '是否固定值 0否 1是',
`create_time` datetime NOT NULL COMMENT '创建时间',
`update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
`create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
`update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
`create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
`del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代客下单字段分类' ROW_FORMAT = Dynamic;

DROP TABLE IF EXIST `we_substitute_customer_order_catalogue_property`
CREATE TABLE `we_substitute_customer_order_catalogue_property`  (
`id` bigint(20) NOT NULL COMMENT '主键id',
`catalogue_id` bigint(20) NOT NULL COMMENT '分类id',
`name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
`code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段编码',
`type` tinyint(2) NOT NULL COMMENT '字段类型',
`is_require` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否必填 0否 1是',
`expound` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
`value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段值',
`sort` int(11) NULL DEFAULT NULL COMMENT '排序',
`is_fixed` tinyint(4) NULL DEFAULT 0 COMMENT '是否固定字段，0否 1是',
`is_money` tinyint(4) NULL DEFAULT 0 COMMENT '是否金额，字段类型为数字时用，需要精确到小数点后两位  0否 1是',
`is_to_time` tinyint(4) NULL DEFAULT 0 COMMENT '是否精确到时间，字段类型为日期时用，0否 1是',
`is_multiple_choice` tinyint(4) NULL DEFAULT 0 COMMENT '是否多选，0否 1是',
`is_more` tinyint(4) NULL DEFAULT NULL COMMENT '是否支持多个，附件时用，0否 1时',
`create_time` datetime NOT NULL COMMENT '创建时间',
`update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
`create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
`update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
`create_by_id` bigint(11) NOT NULL COMMENT '创建人id',
`del_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代客下单分类字段' ROW_FORMAT = Dynamic;

ALTER TABLE `we_tasks` ADD COLUMN `record_id` bigint(20) NULL DEFAULT NULL COMMENT '线索中心-跟进记录Id' AFTER `is_visible`;

---


---
  ### ● 日期：2023.06.09
  ```

ALTER TABLE `we_substitute_customer_order` ADD COLUMN `external_userid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '外部联系人的userid' AFTER `del_flag`;

ALTER TABLE `we_substitute_customer_order` MODIFY COLUMN `discount` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '付款折扣' AFTER `total_price`;

ALTER TABLE `we_substitute_customer_order` MODIFY COLUMN `discount_amount` decimal(9, 2) NULL DEFAULT NULL COMMENT '折扣金额' AFTER `discount`;

ALTER TABLE `we_tasks` MODIFY COLUMN `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间' AFTER `content`;

ALTER TABLE we_leads DROP INDEX phone_unique;
ALTER TABLE `we_substitute_customer_order` ADD COLUMN `external_userid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '外部联系人的userid' AFTER `del_flag`;

DELETE FROM sys_menu WHERE menu_name = '系统管理' AND menu_id=1
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2462, '订单信息', 2367, 2, 'orderInfo', 'salesCenter/orderInfo/index', 1, 'C', '0', '0', 'salesCenter:orderInfo:index', '#', 'admin', NULL, '2023-08-10 16:08:45', 'admin', NULL, '2023-08-17 11:23:25', '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2470, '知识中心', 2230, 45, 'knowledgeBase', NULL, 1, 'M', '0', '0', NULL, '#', 'admin', NULL, '2023-08-21 15:59:46', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2471, '客服问答库', 2470, 1, 'kfKnowledge', NULL, 1, 'M', '0', '0', NULL, '#', 'admin', NULL, '2023-08-21 16:01:27', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2472, '列表', 2471, 1, 'index', 'customerService/knowledgeBase/index', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-08-21 16:02:34', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2473, '{新增}', 2471, 2, 'add', 'customerService/knowledgeBase/add', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-08-21 16:07:19', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2474, '详情', 2471, 3, 'detail', 'customerService/knowledgeBase/detail', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-08-21 16:08:16', NULL, NULL, NULL, '');
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687007498300997632, 1686684407914029056, '商品编码', 'productName', 0, 1, '请关联商品', NULL, 0, 1, 0, 0, 0, NULL, '2023-08-03 15:48:25', '2023-08-03 15:48:25', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687010197646004224, 1686684407914029056, '商品图片', 'productUrl', 7, 0, NULL, NULL, 1, 1, 0, 0, 0, 1, '2023-08-03 15:59:09', '2023-08-03 15:59:09', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687011073676726272, 1686684407914029056, '商品单价', 'productUnitPrice', 6, 1, '请关联商品', NULL, 2, 1, 1, 0, 0, NULL, '2023-08-03 16:02:38', '2023-08-03 16:02:38', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687011624535654400, 1686684407914029056, '商品数量', 'amount', 6, 1, '请填写数量', NULL, 3, 1, 0, 0, 0, NULL, '2023-08-03 16:04:49', '2023-08-03 16:04:49', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012068431429632, 1686684373675925504, '购买人', 'purchaser', 0, 1, '请填写购买人名称', NULL, 4, 1, 0, 0, 0, NULL, '2023-08-03 16:06:35', '2023-08-03 16:06:35', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012156713140224, 1686684373675925504, '联系方式', 'phone', 0, 1, '请填写联系方式', NULL, 6, 1, 0, 0, 0, NULL, '2023-08-03 16:06:56', '2023-08-03 16:06:56', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012507407286272, 1686684373675925504, '订单来源', 'source', 2, 1, NULL, '商品图册,小程序商城,其他', 5, 1, 0, 0, 0, NULL, '2023-08-03 16:08:20', '2023-08-03 16:08:20', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012835494133760, 1686684373675925504, '下单时间', 'orderTime', 3, 1, '请选择时间', NULL, 7, 1, 0, 1, 0, NULL, '2023-08-03 16:09:38', '2023-08-03 16:09:38', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687013105615699968, 1686684373675925504, '归属部门', 'deptId', 4, 1, '请选择部门', NULL, 8, 1, 0, 0, 0, NULL, '2023-08-03 16:10:42', '2023-08-03 16:10:42', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687013218333425664, 1686684373675925504, '归属员工', 'userId', 5, 1, '请选择员工', NULL, 9, 1, 0, 0, 0, NULL, '2023-08-03 16:11:09', '2023-08-03 16:11:09', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687013574748602368, 1686684373675925504, '订单状态', 'orderStatus', 2, 1, NULL, '待支付,待发货,待回款', 10, 1, 0, 0, 0, NULL, '2023-08-03 16:12:34', '2023-08-03 16:12:34', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014381204209664, 1686684432819806208, '付款总价', 'totalPrice', 6, 1, '请填写价格', NULL, 11, 1, 1, 0, 0, NULL, '2023-08-03 16:15:46', '2023-08-03 16:15:46', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014666773397504, 1686684432819806208, '付款折扣', 'discount', 6, 0, '请填写折扣', NULL, 12, 1, 0, 0, 0, NULL, '2023-08-03 16:16:54', '2023-08-03 16:16:54', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014724822564864, 1686684432819806208, '折扣金额', 'discountAmount', 6, 0, '请填写金额', NULL, 13, 1, 1, 0, 0, NULL, '2023-08-03 16:17:08', '2023-08-03 16:17:08', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014801678991360, 1686684432819806208, '实际付款', 'actualPayment', 6, 1, '请填写价格', NULL, 14, 1, 1, 0, 0, NULL, '2023-08-03 16:17:27', '2023-08-03 16:17:27', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015152255696896, 1686684460279914496, '回款方式', 'returnedMoneyType', 2, 1, NULL, '现金,微信,支付宝,银联,其他', 15, 1, 0, 0, 0, NULL, '2023-08-03 16:18:50', '2023-08-03 16:18:50', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015342861647872, 1686684460279914496, '回款金额', 'returnedMoney', 6, 1, '请填写金额', NULL, 16, 1, 1, 0, 0, NULL, '2023-08-03 16:19:36', '2023-08-03 16:19:36', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015505588060160, 1686684460279914496, '回款时间', 'returnedDate', 3, 1, '请选择时间', NULL, 17, 1, 0, 1, 0, NULL, '2023-08-03 16:20:14', '2023-08-03 16:20:14', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015583602114560, 1686684460279914496, '打款人', 'payer', 0, 1, '请填写打款人名称', NULL, 18, 1, 0, 0, 0, NULL, '2023-08-03 16:20:33', '2023-08-03 16:20:33', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015747247079424, 1686684460279914496, '回款凭证', 'returnedReceipt', 7, 0, NULL, NULL, 19, 1, 0, 0, 0, 1, '2023-08-03 16:21:12', '2023-08-03 16:21:12', 'admin', 'admin', 1, 1, 0);

CREATE TABLE `we_common_link_stat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `short_id` bigint(20) NOT NULL COMMENT '短链ID',
  `date_time` datetime NOT NULL COMMENT '日期',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '链接渠道',
  `pv_num` int(11) NULL DEFAULT 0 COMMENT 'PV数量',
  `uv_num` int(11) NULL DEFAULT 0 COMMENT 'UV数量',
  `open_num` int(11) NULL DEFAULT 0 COMMENT '打开小程序数量',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标识 0 有效 1 删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '短链通用统计表' ROW_FORMAT = DYNAMIC;


CREATE TABLE `we_kf_answer`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_id` bigint(64) NOT NULL COMMENT '活码分组id',
  `intent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企微问答ID',
  `qt_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题名称',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除:0有效,1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客服问答信息表' ROW_FORMAT = DYNAMIC;

CREATE TABLE `we_kf_answer_attachments`  (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `answer_id` bigint(64) NOT NULL COMMENT '活码id',
  `material_id` bigint(20) NULL DEFAULT NULL COMMENT '素材Id',
  `msg_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `media_id` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '媒体id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息描述',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息链接',
  `pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息图片地址',
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序appid',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标识 0 有效 1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客服问答回答表' ROW_FORMAT = DYNAMIC;

CREATE TABLE `we_kf_answer_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `group_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
  `is_default` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否为默认分组。0-否 1-是 默认分组为系统自动创建，不可修改/删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除:0有效,1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客服问答分组表' ROW_FORMAT = DYNAMIC;

CREATE TABLE `we_kf_answer_like_question`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `answer_id` bigint(64) NOT NULL COMMENT '问答id',
  `qt_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除:0有效,1删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客服问答相似问题表' ROW_FORMAT = DYNAMIC;

CREATE TABLE `we_material_seize_poster`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `material_id` bigint(20) NULL DEFAULT NULL COMMENT '素材id，为海报类型且海报上的二维码为占位符类型的素材',
  `real_post_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实际海报图片地址',
  `belong_we_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '海报上的二维码归属人id，为员工的we_user_id',
  `config_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新增联系方式的配置id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(1) NULL DEFAULT 0 COMMENT '0 未删除 1 已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '素材海报占位符海报生成的实际海报' ROW_FORMAT = DYNAMIC;

---
### ● 日期：2023.08.28
  ```
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2468, '系统监控', 2185, 9, 'system', NULL, 1, 'M', '0', '0', NULL, 'app', 'admin', NULL, '2023-08-26 16:41:35', NULL, NULL, NULL, '');
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2469, '企微异常', 2468, 1, 'system/exception', 'system/exception/index', 1, 'C', '0', '0', '', 'build', 'admin', NULL, '2023-08-26 17:01:15', 'admin', NULL, '2023-08-26 17:08:45', '');
CREATE TABLE `we_error_msg`  (
`id` bigint(20) NOT NULL COMMENT '主键',
`url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求url',
`error_code` bigint(20) NULL DEFAULT NULL COMMENT '错误码',
`error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
`we_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
`create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
`create_by_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
`create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
`update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
`update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '企业微信响应错误参数记录表' ROW_FORMAT = Dynamic;

ALTER TABLE `we_fission` ADD COLUMN `active_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动名称' AFTER `fission_url`;

ALTER TABLE `we_fission` ADD COLUMN `active_descr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动描述' AFTER `active_title`;

ALTER TABLE `we_fission` ADD COLUMN `active_cover_type` tinyint(4) NULL DEFAULT 1 COMMENT '1:海报缩略图 2:自定义' AFTER `active_descr`;

ALTER TABLE `we_fission` ADD COLUMN `active_cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '封面图片地址' AFTER `active_cover_type`;

ALTER TABLE `we_group_code_range` ADD COLUMN `status` tinyint(4) NULL DEFAULT NULL COMMENT '关联状态 0-未关联 1-关联' AFTER `chat_id`;

  ```

---
### ● 日期：2023.09.11
  ```
INSERT INTO `we_leads_template_settings` (`id`, `table_entry_name`, `table_entry_id`, `table_entry_attr`, `data_attr`, `datetime_type`, `max_input_len`, `can_edit`, `is_required`, `create_time`, `rank`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1678322167884226560, '姓名', 'name', 0, 0, NULL, 10, 1, 1, '2023-07-10 16:36:01', 4, '2023-09-10 20:51:35', 'admin', 'lw', 168, 1, 0);
INSERT INTO `we_leads_template_settings` (`id`, `table_entry_name`, `table_entry_id`, `table_entry_attr`, `data_attr`, `datetime_type`, `max_input_len`, `can_edit`, `is_required`, `create_time`, `rank`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1679400302667051008, '电话号码', 'phone', 0, 0, NULL, 11, 1, 1, '2023-07-13 16:00:09', 2, '2023-09-09 11:53:46', 'admin', 'lw', 168, 1, 0);
INSERT INTO `we_leads_template_settings` (`id`, `table_entry_name`, `table_entry_id`, `table_entry_attr`, `data_attr`, `datetime_type`, `max_input_len`, `can_edit`, `is_required`, `create_time`, `rank`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1691634393458208768, '性别', '1691634393122664448', 1, 0, 0, 100, 1, 0, '2023-08-16 10:14:03', 5, '2023-09-10 20:51:37', 'admin', 'lw', 168, 1, 0);
INSERT INTO `we_leads_template_settings` (`id`, `table_entry_name`, `table_entry_id`, `table_entry_attr`, `data_attr`, `datetime_type`, `max_input_len`, `can_edit`, `is_required`, `create_time`, `rank`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1691695879538970624, '年龄', '1691695879102763008', 0, 1, 0, 100, 0, 0, '2023-08-16 14:18:23', 3, '2023-09-10 20:51:37', 'admin', 'lw', 168, 1, 0);
INSERT INTO `we_leads_template_settings` (`id`, `table_entry_name`, `table_entry_id`, `table_entry_attr`, `data_attr`, `datetime_type`, `max_input_len`, `can_edit`, `is_required`, `create_time`, `rank`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1691695967267033088, '身高', '1691695966935683072', 0, 1, 0, 100, 0, 0, '2023-08-16 14:18:43', 1, '2023-09-10 20:51:35', 'admin', 'lw', 168, 1, 0);

ALTER TABLE `we_customer` MODIFY COLUMN `add_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `del_flag`;
ALTER TABLE `we_customer` MODIFY COLUMN `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `track_time`;


  ```

---
### ● 日期：2023.09.19
  ```
ALTER TABLE we_kf_info MODIFY COLUMN end_content TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '自动结束提醒内容';
ALTER TABLE `we_group_member` ADD UNIQUE INDEX `index`(`chat_id`, `user_id`) USING BTREE COMMENT '客群id与群成员id构建唯一索引';
  ```


---
### ● 日期：2023.09.26
  ```
ALTER TABLE `we_customer` ADD COLUMN `is_join_blacklist` tinyint(4) NULL DEFAULT 1 COMMENT '0:加入黑名单;1:不加入黑名单;' AFTER `add_user_leave`;
ALTER TABLE `we_customer` MODIFY COLUMN `add_user_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `del_flag`;
ALTER TABLE `we_material` ADD COLUMN `tag_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '标签id' AFTER `type`;
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2468, '系统监控', 2185, 9, 'system', NULL, 1, 'M', '0', '0', NULL, 'app', 'admin', NULL, '2023-08-26 16:41:35', NULL, NULL, NULL, '');

  ```
---
### ● 日期：2023.10.09
  ```
INSERT INTO `we_substitute_customer_order_catalogue` (`id`, `name`, `sort`, `is_fixed`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1686684373675925504, '订单信息', 0, 1, '2023-08-02 18:24:26', '2023-08-02 18:24:26', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue` (`id`, `name`, `sort`, `is_fixed`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1686684407914029056, '商品信息', 1, 1, '2023-08-02 18:24:35', '2023-08-02 18:24:35', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue` (`id`, `name`, `sort`, `is_fixed`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1686684432819806208, '付款信息', 2, 1, '2023-08-02 18:24:41', '2023-08-02 18:24:41', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue` (`id`, `name`, `sort`, `is_fixed`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1686684460279914496, '回款信息', 3, 1, '2023-08-02 18:24:47', '2023-08-02 18:24:47', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687007498300997632, 1686684407914029056, '商品编码', 'productName', 0, 1, '请关联商品', NULL, 0, 1, 0, 0, 0, NULL, '2023-08-03 15:48:25', '2023-08-03 15:48:25', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687010197646004224, 1686684407914029056, '商品图片', 'productUrl', 7, 0, NULL, NULL, 1, 1, 0, 0, 0, 1, '2023-08-03 15:59:09', '2023-08-03 15:59:09', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687011073676726272, 1686684407914029056, '商品单价', 'productUnitPrice', 6, 1, '请关联商品', NULL, 2, 1, 1, 0, 0, NULL, '2023-08-03 16:02:38', '2023-08-03 16:02:38', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687011624535654400, 1686684407914029056, '商品数量', 'amount', 6, 1, '请填写数量', NULL, 3, 1, 0, 0, 0, NULL, '2023-08-03 16:04:49', '2023-08-03 16:04:49', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012068431429632, 1686684373675925504, '购买人', 'purchaser', 0, 1, '请填写购买人名称', NULL, 4, 1, 0, 0, 0, NULL, '2023-08-03 16:06:35', '2023-08-03 16:06:35', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012156713140224, 1686684373675925504, '联系方式', 'phone', 0, 1, '请填写联系方式', NULL, 6, 1, 0, 0, 0, NULL, '2023-08-03 16:06:56', '2023-08-03 16:06:56', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012507407286272, 1686684373675925504, '订单来源', 'source', 2, 1, NULL, '商品图册,小程序商城,其他', 5, 1, 0, 0, 0, NULL, '2023-08-03 16:08:20', '2023-08-03 16:08:20', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687012835494133760, 1686684373675925504, '下单时间', 'orderTime', 3, 1, '请选择时间', NULL, 7, 1, 0, 1, 0, NULL, '2023-08-03 16:09:38', '2023-08-03 16:09:38', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687013105615699968, 1686684373675925504, '归属部门', 'deptId', 4, 1, '请选择部门', NULL, 8, 1, 0, 0, 0, NULL, '2023-08-03 16:10:42', '2023-08-03 16:10:42', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687013218333425664, 1686684373675925504, '归属员工', 'userId', 5, 1, '请选择员工', NULL, 9, 1, 0, 0, 0, NULL, '2023-08-03 16:11:09', '2023-08-03 16:11:09', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687013574748602368, 1686684373675925504, '订单状态', 'orderStatus', 2, 1, NULL, '待支付,待发货,待回款', 10, 1, 0, 0, 0, NULL, '2023-08-03 16:12:34', '2023-08-03 16:12:34', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014381204209664, 1686684432819806208, '付款总价', 'totalPrice', 6, 1, '请填写价格', NULL, 11, 1, 1, 0, 0, NULL, '2023-08-03 16:15:46', '2023-08-03 16:15:46', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014666773397504, 1686684432819806208, '付款折扣', 'discount', 6, 0, '请填写折扣', NULL, 12, 1, 0, 0, 0, NULL, '2023-08-03 16:16:54', '2023-08-03 16:16:54', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014724822564864, 1686684432819806208, '折扣金额', 'discountAmount', 6, 0, '请填写金额', NULL, 13, 1, 1, 0, 0, NULL, '2023-08-03 16:17:08', '2023-08-03 16:17:08', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687014801678991360, 1686684432819806208, '实际付款', 'actualPayment', 6, 1, '请填写价格', NULL, 14, 1, 1, 0, 0, NULL, '2023-08-03 16:17:27', '2023-08-03 16:17:27', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015152255696896, 1686684460279914496, '回款方式', 'returnedMoneyType', 2, 1, NULL, '现金,微信,支付宝,银联,其他', 15, 1, 0, 0, 0, NULL, '2023-08-03 16:18:50', '2023-08-03 16:18:50', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015342861647872, 1686684460279914496, '回款金额', 'returnedMoney', 6, 1, '请填写金额', NULL, 16, 1, 1, 0, 0, NULL, '2023-08-03 16:19:36', '2023-08-03 16:19:36', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015505588060160, 1686684460279914496, '回款时间', 'returnedDate', 3, 1, '请选择时间', NULL, 17, 1, 0, 1, 0, NULL, '2023-08-03 16:20:14', '2023-08-03 16:20:14', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015583602114560, 1686684460279914496, '打款人', 'payer', 0, 1, '请填写打款人名称', NULL, 18, 1, 0, 0, 0, NULL, '2023-08-03 16:20:33', '2023-08-03 16:20:33', 'admin', 'admin', 1, 1, 0);
INSERT INTO `we_substitute_customer_order_catalogue_property` (`id`, `catalogue_id`, `name`, `code`, `type`, `is_require`, `expound`, `value`, `sort`, `is_fixed`, `is_money`, `is_to_time`, `is_multiple_choice`, `is_more`, `create_time`, `update_time`, `create_by`, `update_by`, `update_by_id`, `create_by_id`, `del_flag`) VALUES (1687015747247079424, 1686684460279914496, '回款凭证', 'returnedReceipt', 7, 0, NULL, NULL, 19, 1, 0, 0, 0, 1, '2023-08-03 16:21:12', '2023-08-03 16:21:12', 'admin', 'admin', 1, 1, 0);
UPDATE sys_menu set visible=1 where menu_name = '知识中心';
INSERT INTO `we_leads_sea_base_settings` (`id`, `max_claim`, `stock_max_claim`, `create_time`, `create_by`, `create_by_id`, `update_time`, `update_by`, `update_by_id`, `del_flag`) VALUES (1, 1, 8, '2023-07-17 17:18:13', NULL, NULL, '2023-10-10 16:13:34', 'lw', 168, 0);

---
### ● 日期：2023.10.26
  ```
CREATE TABLE `we_default_welcome_msg`  (
`id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '主键id',
`msg_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ',
`content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
`title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息标题',
`description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息描述',
`file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件路径',
`link_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息链接',
`pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息图片地址',
`app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '小程序appid',
`create_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
`create_by_id` bigint(11) NULL DEFAULT NULL COMMENT '创建人id',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
`update_by_id` bigint(11) NULL DEFAULT NULL COMMENT '更新人id',
`material_id` bigint(20) NULL DEFAULT NULL COMMENT '素材id',
`update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
`real_type` tinyint(2) NULL DEFAULT NULL COMMENT '素材真实类型',
PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1717156930562121731 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '默认欢迎语附件' ROW_FORMAT = Dynamic;
```
---
### ● 日期：2023.10.03
  ```
ALTER TABLE `we_community_new_group` ADD COLUMN `code_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活码名称' AFTER `id`;

ALTER TABLE `we_community_new_group` ADD COLUMN `empl_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '多个员工id,使用逗号隔开' AFTER `code_name`;

ALTER TABLE `we_community_new_group` ADD COLUMN `tag_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签id，多个使用逗号隔开' AFTER `empl_list`;

ALTER TABLE `we_community_new_group` ADD COLUMN `skip_verify` tinyint(4) NULL DEFAULT 1 COMMENT '客户添加时无需经过确认自动成为好友:1:是;0:否' AFTER `tag_list`;

ALTER TABLE `we_community_new_group` ADD COLUMN `community_new_group_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新客h5具体页面\n' AFTER `skip_verify`;

ALTER TABLE `we_community_new_group` ADD COLUMN `empl_code_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工活码地址' AFTER `community_new_group_url`;

ALTER TABLE `we_community_new_group` ADD COLUMN `empl_code_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工活码渠道标识' AFTER `empl_code_url`;

ALTER TABLE `we_community_new_group` ADD COLUMN `empl_code_config_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '员工活码configId' AFTER `empl_code_state`;

ALTER TABLE `we_community_new_group` ADD COLUMN `welcome_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加群引导语' AFTER `empl_code_config_id`;

ALTER TABLE `we_community_new_group` ADD COLUMN `chat_id_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实际群id，多个使用逗号隔开' AFTER `welcome_msg`;

ALTER TABLE `we_community_new_group` ADD COLUMN `auto_create_room` tinyint(1) NULL DEFAULT 0 COMMENT '当群满了后，是否自动新建群。0-否；1-是。 默认为0' AFTER `chat_id_list`;

ALTER TABLE `we_community_new_group` ADD COLUMN `room_base_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符' AFTER `auto_create_room`;

ALTER TABLE `we_community_new_group` ADD COLUMN `room_base_id` int(11) NULL DEFAULT NULL COMMENT '自动建群的群起始序号，当auto_create_room为1时有效' AFTER `room_base_name`;

ALTER TABLE `we_community_new_group` ADD COLUMN `group_code_config_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群活码企微信的configId\n' AFTER `room_base_id`;

ALTER TABLE `we_community_new_group` ADD COLUMN `group_code_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群活码渠道标识\n' AFTER `group_code_config_id`;

ALTER TABLE `we_community_new_group` ADD COLUMN `group_code_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群活码图片地址\n' AFTER `group_code_state`;

ALTER TABLE `we_community_new_group` ADD COLUMN `link_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接标题' AFTER `group_code_url`;

ALTER TABLE `we_community_new_group` ADD COLUMN `link_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接描述' AFTER `link_title`;

ALTER TABLE `we_community_new_group` ADD COLUMN `link_cover_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接封面' AFTER `link_desc`;

ALTER TABLE `we_community_new_group` DROP COLUMN `empl_code_name`;

ALTER TABLE `we_community_new_group` DROP COLUMN `group_code_id`;

ALTER TABLE `we_community_new_group` DROP COLUMN `empl_code_id`;
UPDATE `sys_menu` SET `menu_name` = '新客拉群', `parent_id` = 2100, `order_num` = 10, `path` = 'newCustomer', `component` = 'Layout', `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2020-12-30 21:29:50', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-11-01 10:29:22', `remark` = '' WHERE `menu_id` = 2101;
UPDATE `sys_menu` SET `menu_name` = '{新增}', `parent_id` = 2101, `order_num` = 20, `path` = 'aev', `component` = 'communityOperating/newCustomer/aev', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2020-12-31 19:36:24', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-10-31 17:18:53', `remark` = '' WHERE `menu_id` = 2106;
UPDATE `sys_menu` SET `menu_name` = '列表', `parent_id` = 2101, `order_num` = 5, `path` = 'list', `component` = 'communityOperating/newCustomer/list', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = NULL, `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-02-28 18:25:24', `update_by` = NULL, `update_by_id` = NULL, `update_time` = NULL, `remark` = '' WHERE `menu_id` = 2375;
UPDATE `sys_menu` SET `menu_name` = '详情', `parent_id` = 2101, `order_num` = 10, `path` = 'detail', `component` = 'communityOperating/newCustomer/detail', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = NULL, `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-10-31 17:16:48', `update_by` = NULL, `update_by_id` = NULL, `update_time` = NULL, `remark` = '' WHERE `menu_id` = 2475;


```
---
### ● 日期：2023.11.13
  ```
drop TABLE we_pres_tag_group;
drop TABLE we_pres_tag_group_scope;
drop TABLE we_pres_tag_group_stat;
drop TABLE we_pres_tag_group_tag;
CREATE TABLE `we_pres_tag_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '老客户标签建群任务id',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `send_type` tinyint(1) DEFAULT '0' COMMENT '发送方式 0: 企业群发 1：个人群发',
  `group_code_id` bigint(11) DEFAULT NULL COMMENT '群活码id',
  `send_scope` tinyint(1) NOT NULL DEFAULT '0' COMMENT '发送范围 0: 全部客户 1：部分客户',
  `send_gender` int(11) NOT NULL DEFAULT '0' COMMENT '发送性别 0: 全部 1： 男 2： 女 3：未知',
  `cus_begin_time` datetime DEFAULT NULL COMMENT '目标客户被添加起始时间',
  `cus_end_time` datetime DEFAULT NULL COMMENT '目标客户被添加结束时间',
  `welcome_msg` varchar(255) NOT NULL COMMENT '加群引导语',
  `chat_id_list` varchar(255) DEFAULT NULL COMMENT '群id',
  `message_template_id` bigint(20) DEFAULT NULL COMMENT '群发消息的id',
  `auto_create_room` tinyint(1) DEFAULT '0' COMMENT '当群满了后，是否自动新建群。0-否；1-是。 默认为0',
  `tag_redirect_url` varchar(255) DEFAULT NULL COMMENT '实际链接\n',
  `room_base_name` varchar(255) DEFAULT NULL COMMENT '自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符',
  `room_base_id` int(11) DEFAULT NULL COMMENT '自动建群的群起始序号，当auto_create_room为1时有效',
  `group_code_config_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码企微信的configId\n',
  `group_code_state` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码渠道标识\n',
  `group_code_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码图片地址\n',
  `link_title` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '链接标题',
  `link_desc` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '链接描述',
  `link_cover_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '链接封面',
  `is_all` tinyint(4) DEFAULT NULL,
  `we_customers_query` text COMMENT '客户查询条件\n',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '逻辑删除字段， 0:未删除 1:已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1723644156355678209 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='老客户标签建群';
CREATE TABLE `we_pres_tag_group_stat` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) DEFAULT NULL COMMENT '老客标签建群任务id',
  `external_userid` varchar(255) DEFAULT NULL COMMENT '客户id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '跟进者id',
  `msg_id` varchar(255) DEFAULT NULL COMMENT '消息id ',
  `sent` tinyint(4) DEFAULT '0' COMMENT '是否送达(0-未发送 1-已发送 2-因客户不是好友导致发送失败 3-因客户已经收到其他群发消息导致发送失败\n)',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='老客标签建群客户统计表';
UPDATE `we_leads_template_settings` SET `table_entry_name` = '性别', `table_entry_id` = 'sex', `table_entry_attr` = 1, `data_attr` = 0, `datetime_type` = 0, `max_input_len` = 100, `can_edit` = 1, `is_required` = 0, `create_time` = '2023-08-16 10:14:03', `rank` = 5, `update_time` = '2023-09-10 20:51:37', `create_by` = 'admin', `update_by` = 'lw', `update_by_id` = 168, `create_by_id` = 1, `del_flag` = 0 WHERE `id` = 1691634393458208768;
ALTER TABLE `lw-cloud-bs`.`we_chat_contact_msg` MODIFY COLUMN `to_list` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '接收人id（列表）' AFTER `from_id`;
ALTER TABLE `lw-cloud-bs`.`we_product_order` MODIFY COLUMN `mch_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款商户名称' AFTER `we_user_name`;
ALTER TABLE `lw-cloud-bs`.`we_tasks` MODIFY COLUMN `leads_id` bigint(20) NULL DEFAULT NULL AFTER `del_flag`;
