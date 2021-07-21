package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.wecom.domain.WeGroupSop;
import com.linkwechat.wecom.domain.dto.WeGroupSopDto;
import com.linkwechat.wecom.domain.vo.WeGroupSopVo;
import com.linkwechat.wecom.service.IWeGroupSopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社区运营 - 群sop controller
 */
@Api(description = "新客自动拉群 Controller")
@RestController
@RequestMapping(value = "/wecom/communityGroupSop")
public class WeCommunityGroupSopController extends BaseController {

    @Autowired
    private IWeGroupSopService groupSopService;

    /**
     * 通过过滤条件获取群sop列表
     *
     * @param ruleName  规则名称
     * @param createBy  创建者
     * @param beginTime 创建区间 - 开始时间
     * @param endTime   创建区间 - 结束时间
     * @return 群sop规则列表
     */
    @ApiOperation(value = "通过过滤条件获取群sop列表", httpMethod = "GET")
//    @PreAuthorize("@ss.hasPermi('wecom:communityGroupSop:list')")
    @GetMapping(path = "/list")
    public TableDataInfo<List<WeGroupSopVo>> getSopList(
            @RequestParam(value = "ruleName", required = false) String ruleName,
            @RequestParam(value = "createBy", required = false) String createBy,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime
    )
    {
        startPage();
        List<WeGroupSopVo> groupSopVoList = groupSopService.getGroupSopList(ruleName, createBy, beginTime, endTime);
        return getDataTable(groupSopVoList);
    }

    /**
     * 新增SOP规则
     *
     * @param groupSopDto 更新数据
     * @return 结果
     */
    @ApiOperation(value = "新增SOP规则", httpMethod = "POST")
//    @PreAuthorize("@ss.hasPermi('wecom:communityGroupSop:add')")
    @PostMapping(path = "/")
    public AjaxResult addGroupSop(@Validated @RequestBody WeGroupSopDto groupSopDto) {

        WeGroupSop weGroupSop = new WeGroupSop();
        BeanUtils.copyProperties(groupSopDto, weGroupSop);

        if (groupSopService.isNameOccupied(weGroupSop)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "规则名称已存在");
        }
        weGroupSop.setCreateBy(SecurityUtils.getUsername());
        // 群聊id列表
        List<String> groupIdList = groupSopDto.getChatIdList();
        // 素材URL
        List<Long> materialIdList = groupSopDto.getMaterialIdList();
        // 上传的图片的URl列表
        List<String> picList = groupSopDto.getPicList();
        int affectedRows = groupSopService.addGroupSop(weGroupSop, groupIdList, materialIdList, picList);
        if (affectedRows > 0) {
            // 添加成功后进行异步消息推送
            groupSopService.sendMessage(groupIdList);
        }
        return toAjax(affectedRows);
    }

    /**
     * 通过规则id获取sop规则
     *
     * @param ruleId 规则id
     * @return 结果
     */
    @ApiOperation(value = "通过规则id获取sop规则详情", httpMethod = "GET")
//    @PreAuthorize("@ss.hasPermi('wecom:communityGroupSop:query')")
    @GetMapping(path = "/{ruleId}")
    public AjaxResult getGroupSop(@PathVariable("ruleId") Long ruleId) {
        WeGroupSopVo groupSopVo = groupSopService.getGroupSopById(ruleId);
        if (null == groupSopVo) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "该群SOP规则不存在");
        }
        return AjaxResult.success(groupSopVo);
    }

    /**
     * 更改SOP规则
     *
     * @param ruleId      SOP规则 id
     * @param groupSopDto 更新数据
     * @return 结果
     */
    @ApiOperation(value = "更改SOP规则", httpMethod = "PUT")
//    @PreAuthorize("@ss.hasPermi('wecom:communityGroupSop:edit')")
    @PutMapping(path = "/{ruleId}")
    public AjaxResult updateGroupSop(@PathVariable Long ruleId, @Validated @RequestBody WeGroupSopDto groupSopDto) {
        // 校验是否存在
        if (null == groupSopService.getGroupSopById(ruleId)) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "该群SOP规则不存在");
        }
        WeGroupSop weGroupSop = new WeGroupSop();
        weGroupSop.setRuleId(ruleId);
        BeanUtils.copyProperties(groupSopDto, weGroupSop);

        // 校验规则名是否可用
        if (groupSopService.isNameOccupied(weGroupSop)) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "规则名称已存在");
        }

        weGroupSop.setUpdateBy(SecurityUtils.getUsername());
        // 群聊id列表
        List<String> groupIdList = groupSopDto.getChatIdList();
        // 素材id列表
        List<Long> materialIdList = groupSopDto.getMaterialIdList();
        // 上传的图片的URl列表
        List<String> picList = groupSopDto.getPicList();
        return toAjax(groupSopService.updateGroupSop(weGroupSop, groupIdList, materialIdList, picList));
    }

    /**
     * 根据id列表批量删除群sop规则
     *
     * @param ids 群sop规则列表
     * @return 结果
     */
    @ApiOperation(value = "根据id列表批量删除群sop规则", httpMethod = "DELETE")
//    @PreAuthorize("@ss.hasPermi('wecom:communityGroupSop:remove')")
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteSopRule(@PathVariable("ids") Long[] ids) {
        return toAjax(groupSopService.batchRemoveGroupSopByIds(ids));
    }

}
