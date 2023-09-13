package com.linkwechat.domain.leads.sea.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 线索公海
 * </p>
 *
 * @author WangYX
 * @since 2023-04-03
 */
@Data
public class WeLeadsSeaListVo {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 公海名称
     */
    @ApiModelProperty(value = "公海名称")
    private String name;

    /**
     * 公海可见范围
     */
    private List<String> visibleRange;

    /**
     * 公海线索数
     */
    @ApiModelProperty(value = "公海线索数")
    private Integer num;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建者Id
     */
    @ApiModelProperty(value = "创建者Id")
    private Long createById;


}
