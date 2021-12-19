package com.linkwechat.wecom.factory;

import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import org.springframework.stereotype.Service;

/**
 * @author danmo
 * @description 企微回调工厂接口
 * @date 2020/11/9 17:12
 **/
@Service
public interface WeCallBackEventFactory {
    public void eventHandle(String message);
}
