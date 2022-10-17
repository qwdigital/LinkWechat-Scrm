package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @description 客服知识库
 * @date 2022/10/11 10:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfKnowledgeGroupListVo extends WeResultVo {

    /**
     * 	分页游标，再下次请求时填写以获取之后分页的记录
     */
    @ApiModelProperty("分页游标，再下次请求时填写以获取之后分页的记录")
    private String nextCursor;

    @ApiModelProperty("是否还有更多数据。0-没有 1-有")
    private Integer hasMore;

    @ApiModelProperty("分组列表")
    private List<KnowledgeGroupVo> groupList;

    @ApiModel
    @Data
    public static class KnowledgeGroupVo {

        @ApiModelProperty("分组ID")
        private String groupId;

        @ApiModelProperty("分组名")
        private String name;

        @ApiModelProperty("是否为默认分组。0-否 1-是。默认分组为系统自动创建，不可修改/删除")
        private String isDefault;
    }
}
