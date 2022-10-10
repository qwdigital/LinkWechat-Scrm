package com.linkwechat.domain.wecom.callback.third;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 成员变更通知
 * @date 2021/11/20 13:02
 **/
@ApiModel
@Data
public class WeThirdBackUserVo extends WeThirdBackBaseVo {

    @ApiModelProperty("成员UserID")
    private String UserID;

    @ApiModelProperty("对于同一个服务商，不同应用获取到企业内同一个成员的OpenUserID是相同的")
    private String OpenUserID;

    @ApiModelProperty("成员名称")
    private String Name;

    @ApiModelProperty("成员部门列表")
    private String Department;

    @ApiModelProperty("主部门")
    private String MainDepartment;

    @ApiModelProperty("表示所在部门是否为上级，0-否，1-是")
    private String IsLeaderInDept;

    @ApiModelProperty("职位信息")
    private String Position;

    @ApiModelProperty("手机号码")
    private String Mobile;

    @ApiModelProperty("性别")
    private Integer Gender;

    @ApiModelProperty("邮箱")
    private String Email;

    @ApiModelProperty("激活状态")
    private Integer Status;

    @ApiModelProperty("头像url")
    private String Avatar;

    @ApiModelProperty("成员别名")
    private String Alias;

    @ApiModelProperty("座机")
    private String Telephone;
}
