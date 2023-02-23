package com.linkwechat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


/**
 * 自定义表单字段模板表
 * @TableName we_sys_field_template
 */
@TableName(value ="we_sys_field_template")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeSysFieldTemplate extends BaseEntity{
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 信息项名称
     */
    private String labelName;

    /**
     * 信息名称值
     */
    private String labelVal;

    /**
     * 业务类型(1:客户画像)
     */
    private Integer businessType;

    /**
     * 可视化标签id，多个id使用逗号隔开
     */
    private String visualTagIds;


    /**
     * 标签名称
     */
    @TableField(exist = false)
    private String visualTagName;

    /**
     * 信息项属性(1:文本;2:时间;3:勾选项)
     */
    private Integer type;

    /**
     * 信息属性子类型(目前type字段为3的时候 1:单选;2:多选)
     */
    private Integer typeSub;

    /**
     * 其他类容，这里特指勾选项的内容
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<OtherContent> otherContent;

    /**
     * 标签排序
     */
    private Integer labelSort;


    /**
     * 0:正常 1:删除
     */
    @TableLogic
    private Integer delFlag;


    /**
     * 是否默认字段1:是 0:不是
     */
    private Integer isDefault;


    /**
     * 多选下的内容
     */
    @Data
    public static class OtherContent{
        private String labelName;

        private String labelVal;

        private Integer labelSort;
    }

}