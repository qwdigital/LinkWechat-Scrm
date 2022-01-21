package com.linkwechat.wecom.domain.callback;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author danmo
 * @description 异步任务完成通知
 * @date 2021/11/20 13:14
 **/
@ApiModel
@Data
public class WeBackAsynTaskVo extends WeBackBaseVo{

    @ApiModelProperty("异步任务")
    private BatchJobVo BatchJob;

    @ApiModel
    @Data
   public class  BatchJobVo{
       @ApiModelProperty("异步任务id")
       private String JobId;

       @ApiModelProperty("操作类型 目前分别有：sync_user(增量更新成员)、 replace_user(全量覆盖成员）、invite_user(邀请成员关注）、replace_party(全量覆盖部门)")
       private String JobType;

       @ApiModelProperty("返回码")
       private Integer ErrCode;

       @ApiModelProperty("对返回码的文本描述内容")
       private String ErrMsg;
   }

}
