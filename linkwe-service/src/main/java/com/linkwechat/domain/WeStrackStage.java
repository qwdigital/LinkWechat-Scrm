package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 商机阶段
* @TableName we_strack_stage
*/
@Data
@TableName("we_strack_stage")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeStrackStage extends BaseEntity {

    /**
    * 主键
    */
    @TableId
    private Long id;
    /**
    * 阶段名称
    */
    private String stageKey;
    /**
    * 阶段描述
    */
    private String stageDesc;
    /**
    * 阶段值
    */
    private Integer stageVal;
    /**
    * 阶段状态(1:待更进,2:更进中,3:已结束)
    */
    private Integer stageState;
    /**
    * 0:非默认;1:默认
    */
    private Integer isDefault;

    /**
     * 排序
     */
    private Integer sort;

    /**
    * 删除标志（0代表存在 1代表删除）
    */
    @TableLogic
    private Integer delFlag;



}
