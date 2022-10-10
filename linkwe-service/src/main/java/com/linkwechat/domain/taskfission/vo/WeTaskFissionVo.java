package com.linkwechat.domain.taskfission.vo;

import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeTaskFission;
import com.linkwechat.domain.WeTaskFissionStaff;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel
@Data
public class WeTaskFissionVo extends WeTaskFission {

    /**
     * 发起成员
     */
    @ApiModelProperty(value = "发起成员")
    private List<WeTaskFissionStaff> taskFissionStaffs;

    /**
     * 客户群
     */
    @ApiModelProperty(value = "客户群")
    private List<WeGroup> taskFissionWeGroups;
}
