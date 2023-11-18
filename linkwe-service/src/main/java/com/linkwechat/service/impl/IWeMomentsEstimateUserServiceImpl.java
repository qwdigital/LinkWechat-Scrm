package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.domain.moments.entity.WeMomentsEstimateUser;
import com.linkwechat.domain.moments.query.WeMomentsStatisticUserRecordRequest;
import com.linkwechat.domain.moments.vo.WeMomentsEstimateUserVO;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeMomentsEstimateUserMapper;
import com.linkwechat.service.IWeMomentsEstimateUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Override
    public void batchInsert(Long weMomentsTaskId, List<String> weUserIds) {
        AjaxResult<List<SysUser>> sysUser = qwSysUserClient.findAllSysUser(CollectionUtil.join(weUserIds, ","), null, null);
        List<WeMomentsEstimateUser> list = new ArrayList<>();
        sysUser.getData().forEach(i -> list.add(
                WeMomentsEstimateUser.builder()
                        .id(IdUtil.getSnowflake().nextId())
                        .userId(i.getUserId())
                        .weUserId(i.getWeUserId())
                        .userName(i.getUserName())
                        .deptId(i.getDeptId())
                        .deptName(i.getDeptName())
                        .momentsTaskId(weMomentsTaskId)
                        .executeCount(0)
                        .executeStatus(0)
                        .build()));
        if(CollectionUtil.isNotEmpty(list)){
            List<List<WeMomentsEstimateUser>> partitions = Lists.partition(list, 1000);
            for(List<WeMomentsEstimateUser> partition:partitions){

                this.baseMapper.insertBatchSomeColumn(partition);
            }
        }



    }

    @Override
    public List<WeMomentsEstimateUser> getNonExecuteUser(Long weMomentsTaskId) {
        return this.baseMapper.getNonExecuteUser(weMomentsTaskId);
    }

    @Override
    public List<WeMomentsEstimateUserVO> getExecuteUsers(WeMomentsStatisticUserRecordRequest request) {
        return this.baseMapper.getExecuteUsers(request);
    }
}
