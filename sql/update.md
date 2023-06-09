   # **sql更新记录**
   #### 注：当前为每次版本升级时涉及到的数据库变更记录

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