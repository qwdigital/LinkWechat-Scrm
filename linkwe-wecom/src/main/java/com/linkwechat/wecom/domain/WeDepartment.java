package com.linkwechat.wecom.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.wecom.domain.dto.WeDepartMentDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 企业微信组织架构相关对象 we_department
 *
 * @author ruoyi
 * @date 2020-09-01
 */
@ApiModel
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("we_department")
public class WeDepartment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("部门id")
    private Long id;


    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @NotBlank(message = "部门名称不可为空")
    private String name;


    /**
     * 父节点的id
     */
    @ApiModelProperty("父节点的id")
    @NotNull(message = "父节点的id不可为空")
    private Long parentId;


    /**
     * 系统通讯录部门转化为企业微信通讯录部门的dto对象
     */
    public WeDepartMentDto.DeartMentDto transformDeartMentDto(WeDepartment weDepartment) {
        WeDepartMentDto.DeartMentDto deartMentDto = new WeDepartMentDto().new DeartMentDto();
        deartMentDto.setId(this.getId());
        deartMentDto.setName(this.getName());
        deartMentDto.setParentid(this.getParentId());
        return deartMentDto;

    }

    /**
     * 企业微信通讯录部门的dto对象转化为系统通讯录部门对象
     */
    public static WeDepartment transformWeDepartment(WeDepartMentDto.DeartMentDto deartMentDto) {
        WeDepartment weDepartment = new WeDepartment();
        weDepartment.setId(deartMentDto.getId());
        weDepartment.setName(deartMentDto.getName());
        weDepartment.setParentId(deartMentDto.getParentid());
        return weDepartment;
    }


}
