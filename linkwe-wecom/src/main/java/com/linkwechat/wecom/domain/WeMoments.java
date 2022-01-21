package com.linkwechat.wecom.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.wecom.handler.GenericTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * 朋友圈
 */
@Data
@TableName(value ="we_moments",autoResultMap = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class WeMoments extends BaseEntity {


    /**可见类型:0:部分可见;1:公开;*/
    private Integer scopeType;

    /**朋友圈类型:0:企业动态;1:个人动态*/
    private Integer type=new Integer(0);

    /**内容类型*/
    private String contentType;

    /**客户标签，多个使用逗号隔开*/
    private String customerTag;

    /**添加人，多个使用逗号隔开,(已发送员工)*/
    private String addUser;

    /**发送员工名称,使用逗号隔开*/
    @TableField(exist = false)
    private String addUserName;

    /**未发送员工，使用逗号隔开*/
    private String noAddUser;

    /**未发送员工名称，使用逗号隔开*/
    @TableField(exist = false)
    private String noAddUserName;

    /**列表展示*/
    private String content;

    /**创建人*/
    private String creator;
    /**附件*/
    @TableField(typeHandler = GenericTypeHandler.class)
    private List<OtherContent> otherContent;

    @TableLogic
    private Integer delFlag;


    /**是否是在lw平台发布的:1:是;0:否; */
    private Boolean isLwPush;

    @TableId
    private String momentId;


    //发布时间
    private Date pushTime;


    /**评论数*/
    @TableField(exist = false)
    private Integer commentNum;

    /**点赞数*/
    @TableField(exist = false)
    private Integer pointNum;


     //评论员工名
    @TableField(exist = false)
    private  String momentUserName;

    //点赞员工名
    @TableField(exist = false)
    private String pointUserName;

    //评论客户名
    @TableField(exist = false)
    private String momentCustomerName;

    //点赞客户名
    @TableField(exist = false)
    private String pointCustomerName;







    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class  OtherContent{

        //附件类型：1:image 2:video 3:link  4:text
        private String annexType;

        //资源url
        private String annexUrl;

        //资源id
//        private String annexMediaid;

        //如视频封面,图文标题,文字内容
        private String other;
    }

}
