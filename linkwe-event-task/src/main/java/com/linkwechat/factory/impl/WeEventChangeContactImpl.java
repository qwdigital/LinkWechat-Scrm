package com.linkwechat.factory.impl;

import cn.hutool.core.util.XmlUtil;
import com.linkwechat.domain.wecom.callback.WeBackDeptVo;
import com.linkwechat.domain.wecom.callback.WeBackUserVo;
import com.linkwechat.factory.WeCallBackEventFactory;
import com.linkwechat.factory.WeStrategyBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danmo
 * @description 企微变更事件通知处理类
 * @date 2020/11/9 17:17
 **/
@Service
@Slf4j
public class WeEventChangeContactImpl implements WeCallBackEventFactory {
    @Autowired
    private WeStrategyBeanFactory weStrategyBeanFactory;

    private List<String> partEvent=new ArrayList<String>(){{
        add("delete_party");
        add("create_party");
        add("update_party");
    }
    };


    @Override
    public void eventHandle(String message) {
        //新增: create_user 更新: update_user 删除:delete_user
        WeBackUserVo weBackUserVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackUserVo.class);
        String changeType = weBackUserVo.getChangeType();
        if(partEvent.contains(changeType)){//部门相关
            WeBackDeptVo weBackDeptVo = XmlUtil.xmlToBean(XmlUtil.parseXml(message).getFirstChild(), WeBackDeptVo.class);
            weStrategyBeanFactory.getResource(weBackDeptVo.getChangeType(), weBackDeptVo);
        }else{//成员相关

            weStrategyBeanFactory.getResource(changeType, weBackUserVo);
        }

    }
}
