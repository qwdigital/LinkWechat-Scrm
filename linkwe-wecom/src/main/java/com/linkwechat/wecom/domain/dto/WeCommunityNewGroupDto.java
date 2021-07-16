package com.linkwechat.wecom.domain.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Data
public class WeCommunityNewGroupDto {

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
