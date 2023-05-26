package com.linkwechat.domain.msg;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.domain.media.WeMessageTemplate;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @description 应用通知对象
 * @date 2022/4/14 15:55
 **/
@Data
public class QwAppMsgBody {

    /**
     * 业务类型
     */
    private Integer businessType = 1;

    /**
     * 回调主键
     */
    private Long callBackId;

    /**
     * 回调业务类
     */
    private String callBackService;


    /**
     * 企业ID
     */
    private String corpId;

    /**
     * 企业员工ID
     */
    private List<String> corpUserIds;

    /**
     * 企业部门ID
     */
    private List<String> deptIds;

    /**
     * 客户标签ID
     */
    private List<String> tagIds;

    /**
     * 消息模板
     */
    private WeMessageTemplate messageTemplates;

    /**
     * 业务数据
     */
    private JSONObject businessData;
}
