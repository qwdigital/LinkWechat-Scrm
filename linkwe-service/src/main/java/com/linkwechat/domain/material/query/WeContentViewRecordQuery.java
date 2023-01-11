package com.linkwechat.domain.material.query;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 保存查看人记录
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2022/10/21 17:57
 */
@ApiModel(value = "查看人记录")
@Data
public class WeContentViewRecordQuery {

    /**
     * 话术Id
     */
    private Long talkId;

    /**
     * 素材Id
     */
    private Long contentId;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 微信unionid
     */
    private String unionid;

    /**
     * 发送者Id
     * <p>
     * 客户查看了员工从侧边栏发送的轨迹素材在关闭后，发送的员工将会在应用中收到一条消息；
     * 轨迹素材发送时必传，其余不用填写
     */
    private Long sendUserId;

    /**
     * 查看时间
     */
    private Date viewTime;

    /**
     * 查看耗时
     */
    private Long viewWatchTime;

    /**
     * 素材类型(1素材，2企业话术，2客服话术)
     */
    private Integer resourceType;
}
