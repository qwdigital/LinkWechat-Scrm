package com.linkwechat.domain.wecom.vo.customer.msg;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 群发记录列表返回值
 * @date 2021/10/3 16:30
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeGroupMsgListVo extends WeResultVo {

    /**
     * 分页游标，再下次请求时填写以获取之后分页的记录，如果已经没有更多的数据则返回空
     */
    private String nextCursor;

    /**
     * 群发记录列表
     */
    private List<WeGroupMsgVo> groupMsgList;

    /**
     * 群发成员发送任务列表
     */
    private List<WeGroupMsgTaskVo> taskList;

    /**
     * 群成员发送结果列表
     */
    private List<WeGroupMsgSendVo> sendList;
}
