package com.linkwechat.factory.impl.user;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.core.domain.dto.SysUserDTO;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackUserVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.service.IWeCorpAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author danmo
 * @description 新建外部联系人事件
 * @date 2021/1/20 22:19
 **/
@Slf4j
@Component("create_user")
public class WeCallBackCreateUserImpl extends WeEventStrategy {
    @Resource
    private QwSysUserClient qwSysUserClient;

    @Resource
    private IWeCorpAccountService iWeCorpAccountService;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackUserVo userInfo = (WeBackUserVo) message;
            SysUserDTO user = new SysUserDTO();
            user.setWeUserId(userInfo.getUserID());
            user.setCorpId(userInfo.getToUserName());
            try {
                WeCorpAccount account = iWeCorpAccountService.getCorpAccountByCorpId(userInfo.getToUserName());
                if (account != null) {
                    qwSysUserClient.add(user);
                } else {
                    throw new Exception(String.format("%s 所属租户不存在", userInfo.getToUserName()));
                }
            } catch (Exception e) {
                log.error("添加用户异常 params:{}", JSONObject.toJSONString(message), e);
        }
    }
}
