package com.linkwechat.domain.wecom.vo.qr;

import com.linkwechat.domain.wecom.vo.WeResultVo;
import lombok.Data;

import java.util.List;

/**
 * @author danmo
 * @Description 配置了客户联系功能的成员userid列表
 * @date 2021/12/2 15:57
 **/
@Data
public class WeContactWayListVo extends WeResultVo {
    /**
     * 联系方式的配置列表
     */
    private List<WeAddWayVo> contactWay;
}
