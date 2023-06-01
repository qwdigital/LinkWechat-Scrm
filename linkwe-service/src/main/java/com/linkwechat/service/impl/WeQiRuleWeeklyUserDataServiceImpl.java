package com.linkwechat.service.impl;

import com.linkwechat.domain.qirule.query.WeQiRuleWeeklyDetailListQuery;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyDetailListVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeQiRuleWeeklyUserDataMapper;
import com.linkwechat.domain.WeQiRuleWeeklyUserData;
import com.linkwechat.service.IWeQiRuleWeeklyUserDataService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 会话质检周报员工数据表(WeQiRuleWeeklyUserData)
 *
 * @author danmo
 * @since 2023-05-18 17:36:35
 */
@Service
public class WeQiRuleWeeklyUserDataServiceImpl extends ServiceImpl<WeQiRuleWeeklyUserDataMapper, WeQiRuleWeeklyUserData> implements IWeQiRuleWeeklyUserDataService {


    @Override
    public List<WeQiRuleWeeklyDetailListVo> getWeeklyDetailList(WeQiRuleWeeklyDetailListQuery query) {
        return this.baseMapper.getWeeklyDetailList(query);
    }
}
