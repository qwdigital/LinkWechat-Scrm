package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 社群运营 新客自动拉群 Controller
 *
 * @author kewen
 * @date 2021-02-19
 */
@Controller
@RequestMapping(value = "communityNewGroup")
public class WeCommunityNewGroupController {

     @Autowired
     private IWeCommunityNewGroupService weCommunityNewGroupService;

    /**
     * 新增员工活码
     */
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:add')")
    @Log(title = "新客自动拉群", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody @Validated WeCommunityNewGroupDto communityNewGroupDto) {
        try {
            weCommunityNewGroupService.add(communityNewGroupDto);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof WeComException){
                return AjaxResult.error(e.getMessage());
            }else {
                return AjaxResult.error("请求接口异常！");
            }
        }

    }


}
