package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.WeCommunityNewGroup;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 社群运营 新客自动拉群 Controller
 *
 * @author kewen
 * @date 2021-02-19
 */
@Api(description = "新客自动拉群 Controller")
@Controller
@RequestMapping(value = "communityNewGroup")
public class WeCommunityNewGroupController extends BaseController {

     @Autowired
     private IWeCommunityNewGroupService weCommunityNewGroupService;

    /**
     * 查询新客自动拉群列表
     */
    @ApiOperation(value = "查询新客自动拉群列表",httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeCommunityNewGroup communityNewGroup) {
        startPage();
        List<WeCommunityNewGroupVo> communityNewGroupVos = weCommunityNewGroupService.selectWeCommunityNewGroupList(communityNewGroup);
        return getDataTable(communityNewGroupVos);
    }

    /**
     * 获取新客自动拉群详细信息
     */
    @ApiOperation(value = "获取新客自动拉群详细信息",httpMethod = "GET")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult<WeCommunityNewGroupVo> getInfo(@PathVariable("newGroupId") @ApiParam("主键ID") Long newGroupId) {
        return AjaxResult.success(weCommunityNewGroupService.selectWeCommunityNewGroupById(newGroupId));
    }

    /**
     * 修改新客自动拉群
     */
    @ApiOperation(value = "修改新客自动拉群",httpMethod = "PUT")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:edit')")
    @Log(title = "新客自动拉群", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody @Validated WeCommunityNewGroupDto communityNewGroupDto) {
        weCommunityNewGroupService.updateWeCommunityNewGroup(communityNewGroupDto);

        return AjaxResult.success();
    }

    /**
     * 删除新客自动拉群
     */
    @ApiOperation(value = "删除新客自动拉群",httpMethod = "DELETE")
    @PreAuthorize("@ss.hasPermi('wecom:communityNewGroup:remove')")
    @Log(title = "新客自动拉群", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/{ids}")
    public AjaxResult remove(@PathVariable String ids) {
        List<String> idList = Arrays.stream(StringUtils.split(ids, ",")).collect(Collectors.toList());
        return toAjax(weCommunityNewGroupService.batchRemoveWeCommunityNewGroupIds(idList));
    }


    /**
     * 新增新客自动拉群
     */
    @ApiOperation(value = "新增新客自动拉群",httpMethod = "POST")
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
