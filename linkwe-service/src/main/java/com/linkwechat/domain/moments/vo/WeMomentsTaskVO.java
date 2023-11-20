package com.linkwechat.domain.moments.vo;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/06 17:26
 */
@ApiModel
@Data
public class WeMomentsTaskVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 朋友圈类型:0:企业动态;1:个人动态
     */
    private Integer type;

    /**
     * 发送方式: 0企微群发，1成员群发
     */
    private Integer sendType;

    /**
     * 是否是在lw平台发布的:1:是;0:否;
     */
    private Integer isLwPush;

    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    private Integer scopeType;

    /**
     * 朋友圈可见客户数
     */
    private Integer customerNum;

    /**
     * 部门id集合
     */
    private List<Long> deptIds;

    /**
     * 岗位id集合
     */
    private List<String> posts;

    /**
     * 员工id集合
     */
    private List<String> userIds;

    /**
     * 客户标签
     */
    private List<String> customerTag;

    /**
     * 朋友圈文本内容
     */
    private String content;

    /**
     * 执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime executeTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime executeEndTime;

    /**
     * 点赞标签
     */
    private List<String> likeTagIds;

    /**
     * 评论标签
     */
    private List<String> commentTagIds;

    /**
     * 任务状态：1未开始，2进行中，3已结束
     */
    private Integer status;

    /**
     * 完成率
     */
    private String finishRate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 朋友圈任务已完成员工数
     */
    private Integer executed;

    /**
     * 朋友圈任务总的员工数
     */
    private Integer total;

    /**
     * 附件详情
     */
    List<WeMaterial> materialList;


    /**
     * 客户查询条件
     */
    private WeCustomersQuery weCustomersQuery;


}
