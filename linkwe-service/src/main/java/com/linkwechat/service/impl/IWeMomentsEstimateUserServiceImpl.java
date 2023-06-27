package com.linkwechat.service.impl;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.moments.entity.WeMomentsEstimateUser;
import com.linkwechat.mapper.WeMomentsEstimateUserMapper;
import com.linkwechat.service.IWeMomentsEstimateUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 预估朋友圈执行员工 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/06/26 19:26
 */
@Service
public class IWeMomentsEstimateUserServiceImpl extends ServiceImpl<WeMomentsEstimateUserMapper, WeMomentsEstimateUser> implements IWeMomentsEstimateUserService {

    @Override
    public void batchInsert(Long weMomentsTaskId, List<String> weUserIds) {
        List<WeMomentsEstimateUser> list = new ArrayList<>();
        weUserIds.forEach(i -> list.add(WeMomentsEstimateUser.builder().id(IdUtil.getSnowflake().nextId()).weUserId(i).momentsTaskId(weMomentsTaskId).build()));
        this.saveBatch(list);
    }
}
