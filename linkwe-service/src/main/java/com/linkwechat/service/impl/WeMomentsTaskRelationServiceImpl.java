package com.linkwechat.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.moments.entity.WeMomentsTaskRelation;
import com.linkwechat.mapper.WeMomentsTaskRelationMapper;
import com.linkwechat.service.IWeMomentsTaskRelationService;
import org.springframework.stereotype.Service;

/**
 * 朋友圈任务和企微朋友圈关联表 服务实现类
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/09 10:57
 */
@Service
public class WeMomentsTaskRelationServiceImpl extends ServiceImpl<WeMomentsTaskRelationMapper, WeMomentsTaskRelation> implements IWeMomentsTaskRelationService {

    @Override
    public void syncAddRelation(Long weMomentsTaskId, String momentsId) {
        WeMomentsTaskRelation relation = new WeMomentsTaskRelation();
        relation.setId(IdUtil.getSnowflake().nextId());
        relation.setMomentTaskId(weMomentsTaskId);
        relation.setMomentId(momentsId);
        this.save(relation);
    }

}
