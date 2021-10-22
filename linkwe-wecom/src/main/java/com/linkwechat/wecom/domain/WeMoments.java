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

/**
 * 朋友圈
 */
@Data
@TableName("we_moments")
public class WeMoments extends BaseEntity {

    @TableId
    private Long id;

    /**可见类型:1:全部;2:部分;*/
    private Integer scopeType;

    /**朋友圈类型:1:企业动态;2:个人动态*/
    private Integer type;

    /**客户标签，多个使用逗号隔开*/
    private String customerTag;

    /**添加人，多个使用逗号隔开,(已发送员工)*/
    private String addUser;

    /**发送员工名称,使用逗号隔开*/
    private String addUserName;

    /**未发送员工，使用逗号隔开*/
    private String noAddUser;

    /**未发送员工名称，使用逗号隔开*/
    private String noAddUserName;

    /**文字内容*/
    private String textContent;

    /**附件*/
    @TableField(typeHandler = GenericTypeHandler.class)
    private OtherContent otherContent;

    @TableLogic
    private Integer delFlag;

    /**异步任务id，最大长度为64字节，24小时有效；可使用获取发表朋友圈任务结果查询任务状态*/
    private String jobId;





    @Data
    public static class  OtherContent{

        //附件类型：1:图片 2:视频 3:网页
        private Integer annexType;

        //多个资源url,使用逗号隔开
        private String annexUrl;

        //多个媒体id,使用逗号隔开
        private String annexMediaid;
    }

}
