package com.linkwechat.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKeyWordGroupSub;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.domain.community.WeKeywordGroupTask;
import com.linkwechat.service.IWeCommunityKeywordToGroupService;
import com.linkwechat.service.IWeKeyWordGroupSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 关键词群
 */
@RestController
@RequestMapping(value = "/keywordGroup")
public class WxKeywordGroupController extends BaseController {


    @Autowired
    private IWeKeyWordGroupSubService iWeKeyWordGroupSubService;


    @Autowired
    private IWeCommunityKeywordToGroupService keywordToGroupService;





    /**
     * 获取关键词群基础信息
     * @return
     */
    @GetMapping("/getBaseInfo")
    public AjaxResult<WeKeywordGroupTask> getKeyWordGroupBaseInfo(WeKeywordGroupViewCount keywordGroupViewCount){

        return AjaxResult.success(
                keywordToGroupService.findBaseInfo(keywordGroupViewCount.getKeywordGroupId(),keywordGroupViewCount.getUnionId(),true)
        );
    }



    /**
     * 关键词群列表
     * @return
     */
    @GetMapping("/findGroupSubs")
    public TableDataInfo<List<WeKeyWordGroupSub>> findWeKeyWordGroupSubs(WeKeyWordGroupSub keyWordGroupSub){
        startPage();
        List<WeKeyWordGroupSub> weKeyWordGroupSubs = iWeKeyWordGroupSubService.list(new LambdaQueryWrapper<WeKeyWordGroupSub>()
                        .like(StringUtils.isNotEmpty(keyWordGroupSub.getKeyword()),WeKeyWordGroupSub::getKeyword,keyWordGroupSub.getKeyword())
                .eq(WeKeyWordGroupSub::getKeywordGroupId,keyWordGroupSub.getKeywordGroupId())
                .orderByAsc(WeKeyWordGroupSub::getSort));
        return getDataTable(weKeyWordGroupSubs);
    }




}
