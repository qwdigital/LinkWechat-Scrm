package com.linkwechat.domain.wecom.vo.customer.moment;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 评论列表
 * @date 2021/12/3 10:54
 **/
@ApiModel
@Data
public class WeMomentCommentListVo extends WeResultVo {

    @ApiModelProperty("评论列表")
    private List<Comment> commentList;

    @ApiModelProperty("点赞列表")
    private List<Comment> likeList;


    @ApiModel
    @Data
    public static class Comment {
        @ApiModelProperty("外部联系人userid")
        private String externalUserId;

        @ApiModelProperty("企业成员userid")
        private String userId;

        @ApiModelProperty("触发时间")
        private Long createTime;
    }
}
