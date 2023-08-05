package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 活动轨迹相关
 */
@Data
@TableName("we_customer_trajectory")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeCustomerTrajectory extends BaseEntity {

    @TableId
    private String id;

    //轨迹类型(1:客户动态;2:员工动态;3:互动动态4:跟进动态5:客群动态)
    private Integer trajectoryType;

    //操作人类型:1:客户;2:员工;
    private Integer operatorType;

    //操作人id
    private String operatorId;

    //操作人姓名
    private String operatorName;

    //被操作对象类型:1:客户;2:员工:3:客群
    private Integer operatoredObjectType;

    //被操作对象的id
    private String operatoredObjectId;

    //被操作对象名称
    private String operatoredObjectName;

    //动作
    private String action;


    //标题
    private String title;

    //文案内容,整体内容
    private String content;

    //客户id获取群id(查询字段冗余)
    private String externalUseridOrChatid;

    //员工(查询字段冗余)
    private String weUserId;

    //轨迹场景类型，详细描述，见TrajectorySceneType
    private Integer trajectorySceneType;

    /**
     * 素材Id
     */
    private Long materialId;


    /**
     * 素材内容
     */
    @TableField(exist = false)
    private WeMaterialVo weMaterialVo;


}

