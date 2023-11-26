package com.linkwechat.domain.moments.query;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.linkwechat.domain.customer.query.WeCustomersQuery;
import com.linkwechat.domain.groupmsg.query.WeAddGroupMessageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 新增朋友圈任务
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/07 13:34
 */
@ApiModel("新增朋友圈任务")
@Data
public class WeMomentsTaskAddRequest {

    /**
     * 任务名称
     */
    @Size(max = 20)
    @NotBlank(message = "任务名称必填")
    private String name;

    /**
     * 发送方式: 0企微群发,1个人发送,2成员群发
     */
    @NotNull(message = "发送方式必填")
    private Integer sendType;

    /**
     * 发送范围: 0全部客户 1按条件筛选
     */
    @NotNull(message = "发送范围必填")
    private Integer scopeType;

    /**
     * 朋友圈可见客户数
     */
//    @NotNull(message = "朋友圈可见客户数必填")
    private Integer customerNum;

    /**
     * 部门id集合
     */
    private List<Long> deptIds;

    /**
     * 岗位id集合
     */
    @Deprecated
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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime executeTime;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间必填")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
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
     * 素材id集合
     */
    private List<Long> materialIds;


    /**
     * 客户查询条件
     */
    private WeCustomersQuery weCustomersQuery;


    /**
     * 指定接收消息的成员及对应客户列表
     */
    private List<WeAddGroupMessageQuery.SenderInfo> senderList;


}
