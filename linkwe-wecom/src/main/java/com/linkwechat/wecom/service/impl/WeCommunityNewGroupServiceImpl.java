package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Service
@Slf4j
public class WeCommunityNewGroupServiceImpl  extends ServiceImpl<WeCommunityNewGroupMapper, WeCommunityNewGroup> implements IWeCommunityNewGroupService {


    @Override
    public int add(WeCommunityNewGroupDto communityNewGroupDto) {

        //检查群活码是否存在
        //生成员工活码信息
        //保存新客自动拉群信息

        return 0;
    }

}
