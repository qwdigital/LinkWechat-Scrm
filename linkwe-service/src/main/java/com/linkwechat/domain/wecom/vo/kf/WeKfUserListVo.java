package com.linkwechat.domain.wecom.vo.kf;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author danmo
 * @Description 客服
 * @date 2021/12/13 10:57
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class WeKfUserListVo extends WeResultVo {

    /**
     * 操作结果
     */
    private List<WeKfUserVo> resultList;

    /**
     * 客服帐号的接待人员列表
     */
    private List<WeKfUserVo> servicerList;
}
