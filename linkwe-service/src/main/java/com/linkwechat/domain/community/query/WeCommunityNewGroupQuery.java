package com.linkwechat.domain.community.query;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新客自动拉群 入参
 */
@Data
public class WeCommunityNewGroupQuery {

    @TableId
    private Long id;
    /**
     * 活码名称
     */
    @NotNull(message = "活码名不能为空")
    private String codeName;

    /**
     * 指定的员工(id)
     */
    @NotNull(message = "使用员工不能为空")
    private List<String> emplList;

    /**
     * 欢迎语
     */
    @NotNull(message = "欢迎语不能为空")
    private String welcomeMsg;

    /**
     * 群活码ID
     */
    @NotNull(message = "群活码不能为空")
    private Long groupCodeId;

    /**
     * 标签id列表
     */
    private List<String> tagList;

    /**
     * 是否跳过验证自动加好友
     */
    private Boolean skipVerify = true;
}
