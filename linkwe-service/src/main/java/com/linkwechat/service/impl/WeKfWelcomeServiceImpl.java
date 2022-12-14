package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeKfWelcome;
import com.linkwechat.domain.kf.WeKfMenuList;
import com.linkwechat.domain.kf.WeKfWelcomeInfo;
import com.linkwechat.mapper.WeKfWelcomeMapper;
import com.linkwechat.service.IWeKfWelcomeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 客服欢迎语表(WeKfWelcome)
 *
 * @author danmo
 * @since 2022-04-15 15:53:39
 */
@Service
public class WeKfWelcomeServiceImpl extends ServiceImpl<WeKfWelcomeMapper, WeKfWelcome> implements IWeKfWelcomeService {

    @Override
    public List<WeKfWelcomeInfo> getWelcomeByKfId(Long kfId) {
        List<WeKfWelcomeInfo> weKfWelcomeInfos = new ArrayList<>();
        List<WeKfWelcome> welcomeList = list(new LambdaQueryWrapper<WeKfWelcome>()
                .eq(WeKfWelcome::getKfId, kfId)
                .eq(WeKfWelcome::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(welcomeList)) {
            List<WeKfWelcomeInfo> artificialList = welcomeList.stream().filter(item -> ObjectUtil.equal(1, item.getType())).map(item -> {
                WeKfWelcomeInfo weKfWelcomeInfo = new WeKfWelcomeInfo();
                BeanUtil.copyProperties(item, weKfWelcomeInfo);
                return weKfWelcomeInfo;
            }).collect(Collectors.toList());
            List<WeKfWelcomeInfo> intelligentList = welcomeList.stream().filter(item -> ObjectUtil.equal(2, item.getType())).map(item -> {
                WeKfMenuList kfMenuList = JSONObject.parseObject(item.getContent(), WeKfMenuList.class);
                WeKfWelcomeInfo weKfWelcomeInfo = new WeKfWelcomeInfo();
                weKfWelcomeInfo.setType(item.getType());
                weKfWelcomeInfo.setContent(kfMenuList.getHeadContent());
                weKfWelcomeInfo.setMenuList(kfMenuList.getList());
                return weKfWelcomeInfo;
            }).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(artificialList)) {
                weKfWelcomeInfos.addAll(artificialList);
            }

            if (CollectionUtil.isNotEmpty(intelligentList)) {
                weKfWelcomeInfos.addAll(intelligentList);
            }
        }
        return weKfWelcomeInfos;
    }

    @Override
    public void delWelcomByKfId(Long kfId) {
        WeKfWelcome weKfWelcome = new WeKfWelcome();
        weKfWelcome.setDelFlag(1);
        this.update(weKfWelcome,new LambdaQueryWrapper<WeKfWelcome>()
                .eq(WeKfWelcome::getKfId,kfId)
                .eq(WeKfWelcome::getDelFlag,0));
    }
}
