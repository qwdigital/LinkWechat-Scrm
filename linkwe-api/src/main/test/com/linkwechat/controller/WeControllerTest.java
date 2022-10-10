package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.LinkWeApiApplication;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.service.IWeCorpAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = LinkWeApiApplication.class)
class WeControllerTest {

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Test
    public void test() throws Exception {
        WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
        if(null != weCorpAccount && StringUtils.isNotEmpty(weCorpAccount.getAgentId())){
            throw new WeComException("应用相关配置不可为空");
        }
        System.out.println(JSONObject.toJSONString(weCorpAccount));
    }

}