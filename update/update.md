# **系统更新升级**
 ## V5.0.0 (2023-11-23)
      ● 升级日志
         1.朋友圈功能升级调整;
         2.客户标签功能调整,以及部分场景下导致we_customer表中tag_ids字段对丢失功能优化。
         3.客户新增流失时间字段，以及系统其他相关bug修复。
         4.更新日志文档记录规整。
     
       ● xxl-job更新日志
          INSERT INTO `xxl_job_info` (`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES (65, 3, '朋友圈任务执行', '2023-11-18 22:58:29', '2023-11-23 11:35:05', 'HaoN', '', 'CRON', '0/5 * * * * ?', 'DO_NOTHING', 'FIRST', 'sendWeMomentHandle', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2023-11-18 22:58:29', '', 1, 1700710730000, 1700710735000);
      
       ● 菜单更新日志
            UPDATE `sys_menu` SET `menu_name` = '超级朋友圈', `parent_id` = 2198, `order_num` = 1, `path` = 'superFriendCicle', `component` = NULL, `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-07-05 15:27:18', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-11-20 15:26:39', `remark` = '' WHERE `menu_id` = 2451;
            INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2477, '列表', 2451, 5, 'list', 'friendCircle/list', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-11-20 15:27:12', NULL, NULL, NULL, '');
            INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2478, '任务统计', 2451, 10, 'statistics', 'friendCircle/statistics', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-11-20 15:30:03', NULL, NULL, NULL, '');
            INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2479, '{新增}', 2451, 8, 'publish', 'friendCircle/publish', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-11-20 15:31:09', NULL, NULL, NULL, '');
       
       ● sql更新日志
         1.ALTER table we_customer add  loss_time datetime;
    
         2.DROP TABLE IF EXISTS `we_moments_attachments`;
            CREATE TABLE `we_moments_attachments` (
            `id` bigint(20) NOT NULL COMMENT '主键id',
            `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `is_material` tinyint(1) NOT NULL COMMENT '是否内容中心素材: 0不是 1是',
            `msg_type` tinyint(1) DEFAULT NULL COMMENT '附件类型:0图片 1视频 2链接 3位置',
            `media_id` varchar(255) DEFAULT NULL COMMENT '企微素材id,有效期3天',
            `media_id_expire` datetime DEFAULT NULL COMMENT '企微素材id失效时间',
            `media_id_url` varchar(255) DEFAULT NULL COMMENT '获取企微临时素材后，上传到oss',
            `thumb_media_id` varchar(255) DEFAULT NULL COMMENT '视频封面media_id',
            `thumb_media_id_expire` datetime DEFAULT NULL COMMENT '视频封面media_id的失效时间',
            `thumb_media_id_url` varchar(255) DEFAULT NULL COMMENT '视频封面media_id的url地址',
            `link_title` varchar(255) DEFAULT NULL COMMENT '网页链接标题',
            `link_url` varchar(255) DEFAULT NULL COMMENT '网页链接url',
            `location_latitude` varchar(255) DEFAULT NULL COMMENT '地理位置纬度',
            `location_longitude` varchar(255) DEFAULT NULL COMMENT '地理位置经度',
            `location_name` varchar(255) DEFAULT NULL COMMENT '地理位置名称',
            `material_id` bigint(20) DEFAULT NULL COMMENT '素材中心Id',
            `real_type` tinyint(2) DEFAULT NULL COMMENT '真实素材类型',
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈附件';
    
         3.DROP TABLE IF EXISTS `we_moments_customer`;
            CREATE TABLE `we_moments_customer` (
            `id` bigint(20) NOT NULL COMMENT '主键id',
            `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `moments_id` varchar(64) DEFAULT NULL COMMENT '朋友圈id',
            `user_id` bigint(20) DEFAULT NULL COMMENT '员工id',
            `we_user_id` varchar(255) NOT NULL COMMENT '企微员工id',
            `user_name` varchar(255) NOT NULL COMMENT '员工名称',
            `external_userid` varchar(255) DEFAULT NULL COMMENT '客户id',
            `customer_name` varchar(255) DEFAULT NULL COMMENT '客户名称',
            `delivery_status` int(11) DEFAULT NULL COMMENT '送达状态 0已送达 1未送达',
            `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
            `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
            `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标识 0:正常 1:删除',
            PRIMARY KEY (`id`),
            KEY `moments_task_id` (`moments_task_id`,`moments_id`,`user_id`,`external_userid`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈可见客户';
    
         4.DROP TABLE IF EXISTS `we_moments_estimate_customer`;
            CREATE TABLE `we_moments_estimate_customer` (
            `id` bigint(20) NOT NULL COMMENT '主键id',
            `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `user_id` bigint(20) DEFAULT NULL COMMENT '员工id',
            `we_user_id` varchar(255) NOT NULL COMMENT '企微员工id',
            `user_name` varchar(255) DEFAULT NULL COMMENT '员工名称',
            `external_userid` varchar(255) NOT NULL COMMENT '客户id',
            `customer_name` varchar(255) DEFAULT NULL COMMENT '客户名称',
            `delivery_status` int(11) DEFAULT NULL COMMENT '送达状态 0已送达 1未送达',
            `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
            `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
            `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标识 0:正常 1:删除',
            PRIMARY KEY (`id`) USING BTREE,
            KEY `moments_task_id` (`moments_task_id`,`we_user_id`,`external_userid`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预估朋友圈可见客户';
    
         5.DROP TABLE IF EXISTS `we_moments_estimate_user`;
            CREATE TABLE `we_moments_estimate_user` (
            `id` bigint(20) NOT NULL COMMENT '主键Id',
            `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `user_id` bigint(20) DEFAULT NULL COMMENT '员工id',
            `we_user_id` varchar(255) NOT NULL COMMENT '企微员工id',
            `user_name` varchar(255) NOT NULL COMMENT '员工名称',
            `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
            `dept_name` varchar(255) DEFAULT NULL COMMENT '部门名称',
            `execute_count` int(11) DEFAULT '0' COMMENT '提醒执行次数',
            `execute_status` tinyint(1) NOT NULL COMMENT '执行状态:0未执行，1已执行',
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预估朋友圈执行员工';
    
         6.DROP TABLE IF EXISTS `we_moments_interacte`;
            CREATE TABLE `we_moments_interacte` (
            `id` bigint(11) NOT NULL COMMENT '主键',
            `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `moment_id` varchar(64) NOT NULL COMMENT '朋友圈id',
            `we_user_id` varchar(255) DEFAULT NULL COMMENT '企业发表成员userid',
            `interacte_user_id` varchar(255) DEFAULT NULL COMMENT '互动人员名称id',
            `interacte_type` tinyint(1) DEFAULT NULL COMMENT '互动类型:0:评论；1:点赞',
            `interacte_user_type` tinyint(1) DEFAULT NULL COMMENT '互动人员类型:0:员工；1:客户',
            `interacte_time` datetime DEFAULT NULL COMMENT '互动时间',
            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
            `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
            `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
            `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
            `del_flag` tinyint(1) DEFAULT '0' COMMENT '是否删除:0有效,1删除',
            PRIMARY KEY (`id`) USING BTREE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='朋友圈互动列表';
    
         7.DROP TABLE IF EXISTS `we_moments_task`;
            CREATE TABLE `we_moments_task` (
            `id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主键ID',
            `name` varchar(20) DEFAULT NULL COMMENT '任务名称',
            `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '朋友圈类型:0:企业动态;1:个人动态',
            `send_type` tinyint(1) NOT NULL COMMENT '发送方式: 0企微群发，1个人发送，2成员群发',
            `is_lw_push` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否是在lw平台发布的:1:是;0:否;',
            `scope_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '发送范围: 0全部客户 1按条件筛选',
            `customer_num` int(11) DEFAULT '0' COMMENT '朋友圈可见客户数',
            `dept_ids` varchar(1000) DEFAULT NULL COMMENT '部门id集合',
            `post_ids` varchar(1000) DEFAULT NULL COMMENT '岗位id集合',
            `user_ids` varchar(1000) DEFAULT NULL COMMENT '员工id集合',
            `customer_tag` text COMMENT '客户标签，多个使用逗号隔开',
            `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '朋友圈部分内容',
            `execute_time` datetime DEFAULT NULL COMMENT '执行时间',
            `execute_end_time` datetime DEFAULT NULL COMMENT '结束时间',
            `like_tag_ids` text COMMENT '点赞标签',
            `we_customers_query` text COMMENT '客户查询条件\n',
            `comment_tag_ids` text COMMENT '评论标签',
            `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '任务状态：1未开始，2进行中，3已结束',
            `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
            `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
            `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:正常 1:删除',
            `establish_time` datetime DEFAULT NULL COMMENT '企微创建时间',
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='朋友圈';
    
            8.DROP TABLE IF EXISTS `we_moments_task_relation`;
            CREATE TABLE `we_moments_task_relation` (
            `id` bigint(20) NOT NULL COMMENT '主键id',
            `moment_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `job_id` varchar(64) DEFAULT NULL COMMENT '异步任务id，24小时有效',
            `job_id_expire` datetime DEFAULT NULL COMMENT '异步任务id失效时间',
            `moment_id` varchar(64) DEFAULT NULL COMMENT '朋友圈id',
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈任务和企微朋友圈关联表';
    
         8.DROP TABLE IF EXISTS `we_moments_user`;
            CREATE TABLE `we_moments_user` (
            `id` bigint(20) NOT NULL COMMENT '主键id',
            `moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
            `moments_id` varchar(64) DEFAULT NULL COMMENT '朋友圈id',
            `user_id` bigint(20) DEFAULT NULL COMMENT '员工id',
            `we_user_id` varchar(255) NOT NULL COMMENT '企微员工id',
            `user_name` varchar(255) NOT NULL COMMENT '员工名称',
            `dept_id` bigint(20) DEFAULT NULL COMMENT '部门id',
            `dept_name` varchar(255) DEFAULT NULL COMMENT '部门名称',
            `execute_status` tinyint(1) NOT NULL COMMENT '执行状态:0未执行，1已执行',
            `execute_count` int(11) NOT NULL COMMENT '提醒执行次数',
            `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
            `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
            `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标识 0:正常 1:删除',
            PRIMARY KEY (`id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈执行员工';

## V5.0.1 (2023-11-30)
      ● 升级日志
         1.客户sop,企为群发，任务裂变客户筛选功能重构与优化;
         2.群裂变条件为全部时任务通知循环发送相关BUG。

       ● sql更新日志
         ALTER TABLE we_fission ADD COLUMN we_customers_query text  COMMENT '客户查询条件';
         ALTER TABLE we_fission ADD COLUMN scope_type tinyint  COMMENT '发送范围: 0全部客户 1按条件筛选';
         ALTER TABLE we_sop_base ADD COLUMN we_customers_query text  COMMENT '客户查询条件';
         ALTER TABLE we_sop_base ADD COLUMN scope_type tinyint  COMMENT '发送范围: 0全部客户 1按条件筛选';
         ALTER TABLE we_group_message_task ADD COLUMN we_customers_query text  COMMENT '客户查询条件';
         ALTER TABLE we_group_message_task ADD COLUMN scope_type tinyint  COMMENT '发送范围: 0全部客户 1按条件筛选';
## V5.0.2 (2023-12-07)
      ● 升级日志
         1.员工活码新增H5链接与智能短链功能,与活码统计升级。
         2.客群活码活码新增H5链接与智能短链功能,与活码统计升级。
         3.系统相关bug修复。

       ● yml文件更新 
           linkwe-common.yml
                linkwechat:
                     qrShortLinkDomainName: sl.linkwechat.net/st/qr/
                     qrGroupShortLinkDomainName: sl.linkwechat.net/st/gqr/ 
       ● 菜单更新日志
         UPDATE `sys_menu` SET `menu_name` = '员工活码', `parent_id` = 2218, `order_num` = 1, `path` = 'staff', `component` = ' ', `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2020-11-08 12:13:21', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-02-07 21:06:31', `remark` = '' WHERE `menu_id` = 2053;
         UPDATE `sys_menu` SET `menu_name` = '详情', `parent_id` = 2053, `order_num` = 11, `path` = 'detail', `component` = 'drainageCode/staff/detail', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'drainageCode:staff:detail', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2021-02-22 16:32:05', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-11-30 17:17:33', `remark` = '' WHERE `menu_id` = 2159;
         UPDATE `sys_menu` SET `menu_name` = '{新建}', `parent_id` = 2053, `order_num` = 12, `path` = 'add', `component` = 'drainageCode/staff/add', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'drainageCode:staff:add', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2021-02-22 16:38:11', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-11-30 17:18:06', `remark` = '' WHERE `menu_id` = 2160;
         UPDATE `sys_menu` SET `menu_name` = '列表', `parent_id` = 2053, `order_num` = 1, `path` = 'list', `component` = 'drainageCode/staff/list', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2022-09-12 23:07:48', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2022-09-14 22:11:13', `remark` = '' WHERE `menu_id` = 2283;
         UPDATE `sys_menu` SET `menu_name` = '客群活码', `parent_id` = 2218, `order_num` = 2, `path` = 'customerGroup', `component` = 'Layout', `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2020-11-08 12:26:15', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-02-28 16:35:30', `remark` = '' WHERE `menu_id` = 2056;
         UPDATE `sys_menu` SET `menu_name` = '{新增}', `parent_id` = 2056, `order_num` = 50, `path` = 'add', `component` = 'drainageCode/group/baseInfo', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'drainageCode:group:add', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2021-02-23 00:11:41', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-12-03 21:33:08', `remark` = '' WHERE `menu_id` = 2164;
         UPDATE `sys_menu` SET `menu_name` = '详情', `parent_id` = 2056, `order_num` = 10, `path` = 'detail', `component` = 'drainageCode/group/detail', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'drainageCode:group:detail', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2021-02-23 00:14:50', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-12-03 21:31:35', `remark` = '' WHERE `menu_id` = 2165;
         UPDATE `sys_menu` SET `menu_name` = '列表', `parent_id` = 2056, `order_num` = 1, `path` = 'list', `component` = 'drainageCode/group/list', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2022-09-12 23:08:33', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2022-09-13 00:09:31', `remark` = '' WHERE `menu_id` = 2284;
         UPDATE `sys_menu` SET `menu_name` = '统计', `parent_id` = 2056, `order_num` = 2, `path` = 'analyse', `component` = 'drainageCode/group/analyse', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-02-22 12:42:39', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-02-28 16:34:07', `remark` = '' WHERE `menu_id` = 2364;
  
  
      