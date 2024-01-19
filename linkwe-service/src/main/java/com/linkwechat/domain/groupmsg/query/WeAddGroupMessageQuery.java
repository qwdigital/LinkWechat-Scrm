package com.linkwechat.domain.groupmsg.query;

import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.domain.WeGroupMessageAttachments;
import com.linkwechat.domain.WeGroupMessageTemplate;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @author danmo
 * @description 添加群发消息入参
 * @date 2021/10/24 12:14
 **/
@Data
public class WeAddGroupMessageQuery extends WeGroupMessageTemplate {

    /**
     * 是否全部发送
     */
    private Boolean isAll=true;



    /**
     * 指定接收消息的成员及对应客户列表
     */
    private List<SenderInfo> senderList;


    /**
     * 附件列表(新建编辑入参)
     */
    private List<WeMessageTemplate> attachmentsList;


    /**
     * 附件列表展示
     */
    private List<WeGroupMessageAttachments> weGroupMessageAttachmentsVo;

    /**
     * 当前用户
     */
    private LoginUser loginUser;


    //消息来源 1:通常 2:sop 3:直播 ,4:裂变，5:短链推广 6:标签建群
    private Integer msgSource=1;


    /**
     * 客户查询条件
     */

    private WeCustomersQuery weCustomersQuery;




    public void setCurrentUserInfo(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @ApiModel
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SenderInfo {

        @ApiModelProperty("成员id")
        private String userId;

        @ApiModelProperty("客户列表")
        private List<String> customerList;

        @ApiModelProperty("群组列表")
        private List<String> chatList;


    }

}
