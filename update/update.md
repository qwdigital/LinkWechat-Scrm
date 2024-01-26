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
                     qrShortLinkDomainName: sl.linkwechat.net/st/pqr/
                     qrGroupShortLinkDomainName: sl.linkwechat.net/st/gqr/ 
           linkwe-gateway.yml
                security:
                    ignore:
                      whites:
                          - /open/gqr/**
                          - /open/pqr/**
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
       ● xxl-job更新日志
         INSERT INTO `xxl_job_info` ( `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`, `trigger_status`, `trigger_last_time`, `trigger_next_time`) VALUES ( 3, '通用短链统计任务', '2023-07-24 16:41:02', '2023-11-23 11:33:15', 'sxw', '', 'CRON', '59 59 23 * * ?', 'DO_NOTHING', 'ROUND', 'weCommonLinkStatisticTask', '', 'SERIAL_EXECUTION', 60, 3, 'BEAN', '', 'GLUE代码初始化', '2023-07-24 16:41:02', '', 1, 1701878399000, 1701964799000);

## V5.0.2 (2023-12-07) 
      ● 升级日志
         1.新增客群去重功能。
         2.客户SOP与客群SOP新增一键复制功能,同时移除编辑功能。
         3.系统相关bug修复,以及yml相关文件优化。
      
     ● yml文件更新 
        linkwe-common.yml
                linkwechat:
                     shortDomain: sl.linkwechat.net #短域名
                     shortLinkDomainName: ${linkwechat.shortDomain}/st/t/
                     customerShortLinkDomainName: ${linkwechat.shortDomain}/st/l/
                     qrShortLinkDomainName: ${linkwechat.shortDomain}/st/pqr/
                     qrGroupShortLinkDomainName: ${linkwechat.shortDomain}/st/gqr/
        linkwe-gateway.yml
            security:
                ignore:
                   whites:
                      - /open/qr/getBydetail/**
                      - /open/groupCode/getBydetail/**
     ● 菜单更新日志
      UPDATE `sys_menu` SET `menu_name` = '详情', `parent_id` = 2102, `order_num` = 10, `path` = 'detail', `component` = 'communityOperating/oldCustomer/detail', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = '', `icon` = 'code', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-11-13 14:12:12', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-12-13 15:33:53', `remark` = '' WHERE `menu_id` = 2476;
      INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2482, '客群去重', 2223, 15, 'groupRepeat', 'customerManage/groupRepeat/list', 1, 'C', '0', '0', '', '#', 'admin', NULL, '2023-12-09 01:42:33', 'admin', NULL, '2023-12-14 13:12:36', '');

## V5.0.4 (2023-12-21)
      ● 升级日志
         1.智能表单统计优化重构
         2.更新企微配置重构缓存。
         3.相关功能bug修复。

     ● yml文件更新
        linkwe-gateway.yml
            security:
                ignore:
                   whites:
                      - /wx-api/form/survey/getInfo/**
     ● 菜单更新日志
       INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2453, '销售洞察', 2366, 1, 'saleInsight', NULL, 1, 'M', '0', '0', '', '#', 'admin', NULL, '2023-08-07 11:34:28', 'admin', NULL, '2023-08-07 11:35:15', '');
       INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2454, '线索分析', 2453, 5, 'clueAnalysis', 'clue/clueAnalysis/index', 1, 'C', '0', '0', NULL, '#', 'admin', NULL, '2023-08-07 11:35:08', NULL, NULL, NULL, '');
    
     ● sql更新日志
       CREATE TABLE `we_form_survey_count` (
            `id` bigint(11) NOT NULL COMMENT '主键',
            `total_visits` bigint(20) DEFAULT NULL COMMENT '总访问量(每天记录对应当天对应ip的访问量)',
            `belong_id` bigint(20) DEFAULT NULL COMMENT '问卷id',
            `total_time` bigint(20) DEFAULT NULL COMMENT '完成总时间',
            `data_source` varchar(255) DEFAULT NULL COMMENT '数据来源',
            `visitor_ip` varchar(255) DEFAULT NULL COMMENT '访问ip',
            `create_by` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
            `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `update_by` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
            `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0 正常 1 删除',
            PRIMARY KEY (`id`) USING BTREE
       ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='智能表单统计(按照每天的维度统计相关客户数据；ip+当天定位每一条记录)';

     ● xxl-job更新日志
      DELETE  FROM xxl_job_info WHERE id=2
## V5.0.5 (2023-12-21)
      ● 升级日志
         1.门店活码相关功能升级。
         2.相关功能bug修复。

     ● sql更新日志
        DROP TABLE IF EXISTS `we_store_code`;
        CREATE TABLE `we_store_code` (
                `id` bigint(20) NOT NULL COMMENT '主键',
                `store_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '门店名称',
                `province_id` int(11) DEFAULT NULL COMMENT '省id',
                `city_id` int(11) DEFAULT NULL COMMENT '市id',
                `area_id` int(11) DEFAULT NULL COMMENT '区id',
                `area` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '省/市/区',
                `address` varchar(300) CHARACTER SET utf8 DEFAULT NULL COMMENT '详细地址',
                `longitude` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '纬度',
                `latitude` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '经度',
                `shop_guide_id` text CHARACTER SET utf8 COMMENT '导购id(we_user_id)，多个使用逗号隔开',
                `shop_guide_name` text CHARACTER SET utf8 COMMENT '导购名称，多个使用逗号隔开',
                `shop_guide_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '导购活码url',
                `shop_guide_state` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '导购码渠道标识',
                `shop_guide_config_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '导购码configId',
                `group_code_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码',
                `group_code_id` bigint(20) DEFAULT NULL COMMENT '群活码id',
                `group_code_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码名称',
                `group_code_config_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群码config',
                `group_code_state` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群渠道标识',
                `add_we_user_or_group_code` text CHARACTER SET utf8 COMMENT '添加员工或群活码',
                `store_state` tinyint(1) DEFAULT '1' COMMENT '门店状态(0:启用;1:关闭)',
                `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                `create_by_id` bigint(20) DEFAULT NULL COMMENT '创建人id',
                `create_time` datetime DEFAULT NULL COMMENT '创建人时间',
                `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
                `update_by_id` bigint(20) DEFAULT NULL COMMENT '更新人id',
                `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                `del_flag` tinyint(1) DEFAULT '0' COMMENT '0:正常;1:删除;',
                 PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
## V5.0.6 (2024-01-05)
     ● 升级日志
         1.关键词群功能全新升级。
         2.系统数据权限功能升级优化。
         3.识客码状态启动关闭时数据丢失等相关bug修复。

     ● yml文件更新 
        linkwe-common.yml
             linkwechat:
                 keyWordGroupUrl: ${linkwechat.h5Domain}/#/keywords?id={0} #关键词群
    ● 菜单更新日志
       UPDATE `sys_menu` SET `menu_name` = '关键词群', `parent_id` = 2100, `order_num` = 50, `path` = 'keywords', `component` = 'Layout', `is_frame` = 1, `menu_type` = 'M', `visible` = '0', `status` = '0', `perms` = '', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2020-12-30 21:31:17', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2023-02-28 18:36:00', `remark` = '' WHERE `menu_id` = 2103;
       UPDATE `sys_menu` SET `menu_name` = '{新增}', `parent_id` = 2103, `order_num` = 10, `path` = 'aev', `component` = 'communityOperating/keywords/aev', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = 'communityOperating/keywords/aev', `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2020-12-31 19:38:49', `update_by` = 'admin', `update_by_id` = NULL, `update_time` = '2024-01-04 11:17:36', `remark` = '' WHERE `menu_id` = 2108;
       UPDATE `sys_menu` SET `menu_name` = '列表', `parent_id` = 2103, `order_num` = 5, `path` = 'list', `component` = 'communityOperating/keywords/list', `is_frame` = 1, `menu_type` = 'C', `visible` = '1', `status` = '0', `perms` = NULL, `icon` = '#', `create_by` = 'admin', `create_by_id` = NULL, `create_time` = '2023-02-28 18:36:26', `update_by` = NULL, `update_by_id` = NULL, `update_time` = NULL, `remark` = '' WHERE `menu_id` = 2377;
       INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_by_id`, `create_time`, `update_by`, `update_by_id`, `update_time`, `remark`) VALUES (2483, '详情', 2103, 15, 'detail', 'communityOperating/keywords/detail', 1, 'C', '1', '0', NULL, '#', 'admin', NULL, '2023-12-26 23:23:14', NULL, NULL, NULL, '');
  
    ● sql更新日志
        DROP TABLE IF EXISTS `we_key_word_group_sub`;
        CREATE TABLE `we_key_word_group_sub` (
            `id` bigint(20) NOT NULL COMMENT '主键',
            `keyword` varchar(255) DEFAULT NULL COMMENT '关键词',
            `code_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '活码名称',
            `keyword_group_id` bigint(20) DEFAULT NULL COMMENT '关键词主表主键',
            `chat_id_list` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '实际群id，多个使用逗号隔开',
            `auto_create_room` tinyint(1) DEFAULT '0' COMMENT '当群满了后，是否自动新建群。0-否；1-是。 默认为0',
            `room_base_name` varchar(255) DEFAULT NULL COMMENT '自动建群的群名前缀，当auto_create_room为1时有效。最长40个utf8字符',
            `room_base_id` int(11) DEFAULT NULL COMMENT '自动建群的群起始序号，当auto_create_room为1时有效',
            `group_code_config_id` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码企微信的configId\n',
            `group_code_state` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码渠道标识\n',
            `group_code_url` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '群活码图片地址\n',
            `group_code_name` varchar(255) DEFAULT NULL COMMENT '群名',
            `sort` tinyint(4) DEFAULT '0' COMMENT '排序',
            `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
            `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id ',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `del_flag` int(1) DEFAULT '0' COMMENT '0:正常;1:删除;',
            PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关键词群子表';

        DROP TABLE IF EXISTS `we_keyword_group`;
        CREATE TABLE `we_keyword_group` (
            `id` bigint(20) NOT NULL COMMENT '关键词拉群任务主键',
            `title` varchar(100) NOT NULL COMMENT '标题',
            `descrition` varchar(255) DEFAULT NULL COMMENT '描述',
            `keyword_group_url` varchar(255) DEFAULT NULL COMMENT '关键词群链接二维码链接',
            `keyword_group_qr_url` varchar(255) DEFAULT NULL COMMENT '关键词群链接二维码链接',
            `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
            `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
            `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记 0 未删除 1已删除',
            PRIMARY KEY (`id`) USING BTREE
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='关键词拉群任务表';
        
        
        DROP TABLE IF EXISTS `we_keyword_group_view_count`;
        CREATE TABLE `we_keyword_group_view_count` (
            `id` bigint(20) NOT NULL COMMENT '主键',
            `view_num` int(11) DEFAULT '0' COMMENT '访问数量',
            `keyword_group_id` bigint(20) DEFAULT NULL COMMENT '关键词主表主键',
            `union_id` varchar(255) DEFAULT NULL COMMENT 'unionld',
            `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
            `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
            `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
            `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id ',
            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
            `del_flag` int(1) DEFAULT '0' COMMENT '0:正常;1:删除;',
            PRIMARY KEY (`id`) USING BTREE
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关键词群访问统计';

## V5.1.0 (2024-01-12)
     ● 升级日志
         1.系统全面接入AI【目前仅支持:腾讯混元】
         2.系统相关bug修复。
     ● yml文件更新 
        linkwe-common.yml
             linkwechat:
                 txAiSecretId: 
                 txAiSecretKey: 
                 txAiRegion: 
        linkwe-ai.yml
              server:
                 port: 6060
              servlet:
                 context-path: /ai
              tomcat:
              uri-encoding: UTF-8
 
    ● sql更新日志
        CREATE TABLE `we_ai_msg` (
                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
                `session_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '会话ID',
                `msg_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'AI对话ID',
                `user_id` bigint(20) NOT NULL COMMENT '员工ID',
                `role` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色',
                `content` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
                `request_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '结果ID',
                `send_time` datetime NOT NULL COMMENT '发送时间',
                `note` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '免责声明',
                `collection` tinyint(4) NOT NULL DEFAULT '0' COMMENT '收藏 0-未收藏 1-收藏',
                `prompt_tokens` int(11) DEFAULT '0' COMMENT '请求消耗token数',
                `completion_tokens` int(11) DEFAULT '0' COMMENT '回复消耗token数',
                `total_tokens` int(11) DEFAULT '0' COMMENT '总消耗token数',
                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
                `create_by_id` bigint(11) DEFAULT NULL COMMENT '创建人id',
                `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
                `update_by_id` bigint(11) DEFAULT NULL COMMENT '更新人id',
                `del_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除:0有效,1删除',
                PRIMARY KEY (`id`)
        ) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ai助手消息表';

## V5.1.1 (2024-01-19)
     ● 升级日志
         1.系统新增群发一键复制功能。
         2.系统新增AI服务校验,如果linkwe-ai服务未启动,则关闭隐藏前端ai相关功能。
         3.系统相关bug修复等
     ● yml文件更新 
        gateway-router #以下直接覆盖即可
              [{
                    "id": "linkwe-auth",
                    "order": 0,
                    "predicates": [{
                        "args": {
                            "pattern": "/auth/**"
                        },
                        "name": "Path"
                    }],
                    "filters":[{
                        "name":"ValidateCodeFilter"
                    },{
                        "name":"CacheRequestFilter"
                    }
                    ],
                    "uri": "lb://linkwe-auth"
                },{
                "id": "linkwe-auth-system",
                "order": 0,
                "predicates": [{
                "args": {
                "pattern": "/system/**"
                },
                "name": "Path"
                }],
                "uri": "lb://linkwe-auth"
                },{
                "id": "linkwe-wecom",
                "order": 2,
                "predicates": [{
                "args": {
                "pattern": "/wecom/**"
                },
                "name": "Path"
                }],
                "filters":[{
                "args": {
                "_genkey_0":"1"
                },
                "name":"StripPrefix"
                }],
                "uri": "lb://linkwe-wecom"
                },{
                "id": "linkwe-api",
                "order": 3,
                "predicates": [{
                "args": {
                "pattern": "/open/**"
                },
                "name": "Path"
                }],
                "uri": "lb://linkwe-api"
                },{
                "id": "linkwe-file",
                "order": 4,
                "predicates": [{
                "args": {
                "pattern": "/file/**"
                },
                "name": "Path"
                }],
                "uri": "lb://linkwe-file"
                },{
                "id": "linkwe-auth-common",
                "order": 0,
                "predicates": [{
                "args": {
                "pattern": "/common/**"
                },
                "name": "Path"
                }],
                "uri": "lb://linkwe-auth"
                },{
                "id": "linkwe-wx-api",
                "order": 3,
                "predicates": [{
                "args": {
                "pattern": "/wx-api/**"
                },
                "name": "Path"
                }],
                "uri": "lb://linkwe-wx-api"
                },{
                "id": "linkwe-ai",
                "order": 6,
                "predicates": [{
                "args": {
                "pattern": "/ai/**"
                },
                "name": "Path"
                }],
                "uri": "lb://linkwe-ai"
                }]
 
    ● sql更新日志
       ALTER TABLE we_group_message_template ADD COLUMN all_send tinyint;
       ALTER TABLE we_group_message_template ADD COLUMN we_customers_or_group_query text;
## V5.1.2 (2024-01-26)
     ● 升级日志
         1.文件上传新增适配minio
         2.系统相关bug修复等