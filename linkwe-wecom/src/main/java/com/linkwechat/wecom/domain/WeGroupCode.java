package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.utils.SnowFlakeUtil;
import lombok.Data;

import java.util.List;


/**
 * 客户群活码对象 we_group_code
 * 
 * @author ruoyi
 * @date 2020-10-07
 */
@Data
@TableName("we_group_code")
public class WeGroupCode
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private Long id= SnowFlakeUtil.nextId();

    /** 活动头像 */
    private String activityHeadUrl;

    /** 活动名称 */
    private String activityName;

    /** 场景 */
    private String activityScene;

    /** 引导语 */
    private String guide;

    /** 进群是否提示:1:是;0:否; */
    private Long joinGroupIsTip;

    /** 进群提示语 */
    private String tipMsg;

    /** 客服二维码 */
    private String customerServerQrCode;

    /** 0:正常;2:删除; */
    private Long delFlag;


    @TableField(exist = false)
    private List<WeGroupCodeActual> actualList;

}
