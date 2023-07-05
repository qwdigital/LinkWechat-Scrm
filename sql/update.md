   # **sql更新记录**
   #### 注：当前为每次版本升级时涉及到的数据库变更记录

---
  ### ● 日期：2023.06.09
  ```sql
  alter table we_qr_code add rule_mode tinyint default 1 null comment '排班方式 1：轮询 2：顺序 3：随机' after rule_type;
alter table we_qr_code add open_spare_user tinyint default 0 null comment '开启备用员工 0：否 1：是' after rule_mode;
alter table we_qr_scope add scheduling_num int default 0 null comment '排班次数' after status;
alter table we_qr_scope add is_spare_user tinyint default 0 null comment '是否备用员工 0：否 1：是' after scheduling_num;
alter table we_qr_code add is_exclusive tinyint default 0 null comment '是否开启同一外部企业客户只能添加同一个员工，开启后，同一个企业的客户会优先添加到同一个跟进人  0-不开启 1-开启' after qr_code;
```

### ● 日期：2023.07.05 朋友圈相关sql
```sql
-- 删除第一版朋友圈表
DROP TABLE IF EXISTS we_moments;
DROP TABLE IF EXISTS we_moments_interacte;

-- 新增第二版朋友圈相关表
DROP TABLE IF EXISTS `we_moments_task`;
CREATE TABLE `we_moments_task` (
`id` bigint(20) NOT NULL DEFAULT '0' COMMENT '主键ID',
`name` varchar(20) DEFAULT NULL COMMENT '任务名称',
`type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '朋友圈类型:0:企业动态;1:个人动态',
`send_type` tinyint(1) NOT NULL COMMENT '发送方式: 0企微群发，1个人发送，2成员群发',
`is_lw_push` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否是在lw平台发布的:1:是;0:否;',
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
`comment_tag_ids` text COMMENT '评论标签',
`status` tinyint(4) NOT NULL DEFAULT '3' COMMENT '任务状态：1未开始，2进行中，3已结束',
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

DROP TABLE IF EXISTS `we_moments_task_relation`;
CREATE TABLE `we_moments_task_relation` (
`id` bigint(20) NOT NULL COMMENT '主键id',
`moment_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
`job_id` varchar(64) DEFAULT NULL COMMENT '异步任务id，24小时有效',
`job_id_expire` datetime DEFAULT NULL COMMENT '异步任务id失效时间',
`moment_id` varchar(64) DEFAULT NULL COMMENT '朋友圈id',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈任务和企微朋友圈关联表';

DROP TABLE IF EXISTS `we_moments_attachments`;
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

DROP TABLE IF EXISTS `we_moments_estimate_user`;
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

DROP TABLE IF EXISTS `we_moments_user`;
CREATE TABLE `we_moments_user` (
`id` bigint(20) NOT NULL COMMENT '主键id',
`moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
`moments_id` varchar(64) NOT NULL COMMENT '朋友圈id',
`user_id` bigint(20) NOT NULL COMMENT '员工id',
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
`del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识 0:正常 1:删除',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈执行员工';

DROP TABLE IF EXISTS `we_moments_estimate_customer`;
CREATE TABLE `we_moments_estimate_customer` (
`id` bigint(20) NOT NULL COMMENT '主键id',
`moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
`user_id` bigint(20) DEFAULT NULL COMMENT '员工id',
`we_user_id` varchar(255) NOT NULL COMMENT '企微员工id',
`user_name` varchar(255) DEFAULT NULL COMMENT '员工名称',
`external_userid` varchar(255) NOT NULL COMMENT '客户id',
`customer_name` varchar(255) DEFAULT NULL COMMENT '客户名称',
PRIMARY KEY (`id`),
KEY `moments_task_id` (`moments_task_id`,`we_user_id`,`external_userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预估朋友圈可见客户';

DROP TABLE IF EXISTS `we_moments_customer`;
CREATE TABLE `we_moments_customer` (
`id` bigint(20) NOT NULL COMMENT '主键id',
`moments_task_id` bigint(20) NOT NULL COMMENT '朋友圈任务id',
`moments_id` varchar(64) NOT NULL COMMENT '朋友圈id',
`user_id` bigint(20) NOT NULL COMMENT '员工id',
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
`del_flag` tinyint(4) DEFAULT NULL COMMENT '删除标识 0:正常 1:删除',
PRIMARY KEY (`id`),
KEY `moments_task_id` (`moments_task_id`,`moments_id`,`user_id`,`external_userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈可见客户';

DROP TABLE IF EXISTS `we_moments_interacte`;
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


-- 素材表we_material新增字段
ALTER TABLE `we_material` ADD `pixel_size` bigint(20) DEFAULT NULL COMMENT '像素大小';
ALTER TABLE `we_material` ADD `memory_size` bigint(20) DEFAULT NULL COMMENT '内存大小';
ALTER TABLE `we_material` MODIFY `width` int DEFAULT NULL COMMENT '图片宽（类型为图片时为图片的宽，视频为封面的宽，图文时为封面的宽，小程序为封面的宽，文章时为封面宽，海报时为海报的宽）';
ALTER TABLE `we_material` MODIFY `height` int DEFAULT NULL COMMENT '图片高（类型为图片时为图片的高，视频为封面的高，图文时为封面的高，小程序为封面的高，文章时为封面高，海报时为海报的高）';
ALTER TABLE `we_material` MODIFY `media_type` varchar(255) DEFAULT NULL COMMENT '资源类型(0图片，1语音，2视频，3文件，4文本，5海报，9图文，10链接，11小程序，12文章)';
```
---