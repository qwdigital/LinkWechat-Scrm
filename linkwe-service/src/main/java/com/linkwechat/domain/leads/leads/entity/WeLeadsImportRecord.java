package com.linkwechat.domain.leads.leads.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 线索导入记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/14 13:49
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "we_leads_import_record")
public class WeLeadsImportRecord extends BaseEntity {

    @TableId(value = "id")
    private Long id;

    /**
     * 所属公海
     */
    @TableField(value = "sea_id")
    private Long seaId;


    /**
     * 导入的表单或excel的文件名
     */
    @TableField(value = "import_source_file_name")
    private String importSourceFileName;


    /**
     * 导入来源类型 0 excel 1 智能表单 2 手动新增
     *
     * @see com.linkwechat.common.enums.leads.record.ImportSourceTypeEnum
     */
    @TableField(value = "import_source_type")
    private Integer importSourceType;

    /**
     * 智能表单id，当导入来源类型为智能表单时，这个值不为空
     */
    @TableField(value = "from_id")
    private Long formId;

    /**
     * excel的线索总数
     */
    @TableField(value = "total_num")
    private Integer totalNum;

    /**
     * 线索导入的成功数
     */
    @TableField(value = "success_num")
    private Integer successNum;

    /**
     * 线索导入的失败数
     */
    @TableField(value = "fail_num")
    private Integer failNum;

    /**
     * 删除标识 0 正常 1 删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(value = "del_flag")
    private Integer delFlag;

}

