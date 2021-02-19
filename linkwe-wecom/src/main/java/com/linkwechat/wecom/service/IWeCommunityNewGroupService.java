package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
public interface IWeCommunityNewGroupService extends IService<WeCommunityNewGroup> {

    /**
     * 添加新客自动拉群信息
     *
     * @param communityNewGroupDto 信息
     * @return 结果
     */
    int add(WeCommunityNewGroupDto communityNewGroupDto);


}
