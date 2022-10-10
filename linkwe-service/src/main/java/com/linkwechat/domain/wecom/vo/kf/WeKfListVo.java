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
public class WeKfListVo extends WeResultVo {

    /**
     * 帐号信息列表
     */
    private List<WeKfDetailVo> accountList;
}
