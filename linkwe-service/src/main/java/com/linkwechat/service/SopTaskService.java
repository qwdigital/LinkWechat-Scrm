package com.linkwechat.service;

import com.linkwechat.domain.sop.dto.WeSopBaseDto;

public interface SopTaskService {


    /**
     * 更新或新建sop执行计划
     * @param weSopBaseDto
     */
    void createOrUpdateSop(WeSopBaseDto weSopBaseDto);

    /**
     * 构建新客计划
     */
    void builderXkPlan();
}
