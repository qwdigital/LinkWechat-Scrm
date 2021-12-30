package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.annotation.Excel;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 客户公海实体
 */
@Data
@TableName("we_customer_seas")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerSeas extends BaseEntity {
    //主键
    @TableId
    private Long id;

    //手机号
    @Excel(name = "手机号")
    private String phone;

    //客户备注名称
    @Excel(name = "客户名称")
    private String customerName;

    //标签名，多个使用逗号隔开
    private String tagNames;

    //标签id，多个使用逗号隔开
    private String tagIds;

    //添加人名称，多个使用逗号隔开
    private String addUserName;

    //添加人id,多个使用逗号隔开
    private String addUserId;

    //导入的excel的名称
    private String tableExcelName;

    //导入的excel的同一批次下的id
    private Long tableExcelId;

    //当前状态:0:待添加;1:已添加;3:待通过
    private Integer addState;

    @TableLogic
    private Integer delFlag;


}
