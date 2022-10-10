package com.linkwechat.domain.groupmsg.query;

import com.baomidou.mybatisplus.annotation.TableField;
import com.linkwechat.common.core.domain.model.LoginUser;
import com.linkwechat.domain.WeGroupMessageTemplate;
import com.linkwechat.domain.media.WeMessageTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 添加群发消息入参
 * @date 2021/10/24 12:14
 **/
@ApiModel
@Data
public class WeAddGroupMessageQuery extends WeGroupMessageTemplate {

    @ApiModelProperty("是否全部发送")
    private Boolean isAll=true;
    /**
     * 指定接收消息的成员及对应客户列表
     */
    @ApiModelProperty("指定接收消息的成员及对应客户列表")
    private List<SenderInfo> senderList;


    @ApiModelProperty("附件列表")
    private List<WeMessageTemplate> attachmentsList;

    @ApiModelProperty(value = "当前用户",hidden = true)
    private LoginUser loginUser;

    public void setCurrentUserInfo(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @ApiModel
    @Data
    public static class SenderInfo {

        @ApiModelProperty("成员id")
        private String userId;

        @ApiModelProperty("客户列表")
        private List<String> customerList;

        @ApiModelProperty("群组列表")
        private List<String> chatList;
    }

}
