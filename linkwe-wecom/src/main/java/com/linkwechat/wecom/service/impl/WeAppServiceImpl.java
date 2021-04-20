package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.client.WeAppClient;
import com.linkwechat.wecom.domain.WeApp;
import com.linkwechat.wecom.domain.WeDepartment;
import com.linkwechat.wecom.domain.dto.WeAppDetailDto;
import com.linkwechat.wecom.domain.dto.WeAppDto;
import com.linkwechat.wecom.mapper.WeAppMapper;
import com.linkwechat.wecom.service.IWeAppService;
import com.linkwechat.wecom.service.IWeDepartmentService;
import com.linkwechat.wecom.service.IWeUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: HaoN
 * @create: 2021-01-26 18:44
 **/
@Service
public class WeAppServiceImpl extends ServiceImpl<WeAppMapper, WeApp> implements IWeAppService {

    @Autowired
    WeAppClient weAppClient;


    @Autowired
    IWeDepartmentService iWeDepartmentService;


    @Autowired
    IWeUserService iWeUserService;


    /**
     * 保存应用
     * @param weApp
     */
    @Override
    @Transactional
    public void addWeApp(WeApp weApp) {

      if(null !=this.getOne(new LambdaQueryWrapper<WeApp>()
              .eq(WeApp::getAgentId, weApp.getAgentId())
              .eq(WeApp::getDelFlag, Constants.SUCCESS))){
          throw new WeComException("AgentId已经存在");
      }



        WeAppDetailDto weAppDetailDto
                = weAppClient.findAgentById(weApp.getAgentId());
        if(null != weAppDetailDto){
            weApp.setAgentName(weAppDetailDto.getName());
            weApp.setCreateTime(new Date());
            weApp.setSquareLogoUrl(weAppDetailDto.getSquare_logo_url());
            weApp.setDescription(weAppDetailDto.getDescription());
            weApp.setAllowPartys(StringUtils.join(weAppDetailDto.getAllow_partys().getPartyid(),","));
            weApp.setAllowUserinfos(StringUtils.join(weAppDetailDto.getAllow_userinfos().getUser(),","));
            this.save(weApp);
        }

    }



    @Override
    @Transactional
    public void updateWeApp(WeApp weApp) {
        if(this.updateById(weApp)){
            weAppClient.updateAgentById(
                    WeAppDetailDto.builder()
                            .agentid(weApp.getAgentId())
                            .logo_mediaid(weApp.getLogoMediaid())
                            .description(weApp.getDescription())
                            .name(weApp.getAgentName())
                            .build()
            );
        }


    }
}
