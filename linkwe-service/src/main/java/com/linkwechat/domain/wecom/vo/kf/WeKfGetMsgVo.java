package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 客服
 * @date 2021/12/13 13:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfGetMsgVo extends WeResultVo {
    /**
     * 是否还有更多数据。0-否；1-是
     */
    private Integer hasMore;

    /**
     * 消息列表
     */
    private List<JSONObject> msgList;
}
