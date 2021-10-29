package com.linkwechat.wecom.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeUser;
import com.linkwechat.wecom.service.IWeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeMsgTlpMapper;
import com.linkwechat.wecom.domain.WeMsgTlp;
import com.linkwechat.wecom.service.IWeMsgTlpService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 欢迎语模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeMsgTlpServiceImpl extends ServiceImpl<WeMsgTlpMapper,WeMsgTlp> implements IWeMsgTlpService
{


    @Autowired
    private IWeUserService iWeUserService;

    /**
     * 新增欢迎语模板
     * 
     * @param weMsgTlp 欢迎语模板
     * @return 结果
     */
    @Override
    @Transactional
    public void addorUpdate(WeMsgTlp weMsgTlp)
    {


        if(weMsgTlp.getWelcomeMsgTplType()
                .equals(new Integer(2))) {

            String userIds = weMsgTlp.getUserIds();

            if (StringUtils.isNotEmpty(userIds)) {

                List<WeUser> weUsers = iWeUserService.listByIds(
                        ListUtil.toList(userIds.split(","))
                );

                if (CollectionUtil.isNotEmpty(weUsers)) {
                    weMsgTlp.setUserNames(String.join(",",
                            weUsers.stream().map(WeUser::getName).collect(Collectors.toList())));
                }
            }
        }

            this.saveOrUpdate(weMsgTlp);


    }



}
