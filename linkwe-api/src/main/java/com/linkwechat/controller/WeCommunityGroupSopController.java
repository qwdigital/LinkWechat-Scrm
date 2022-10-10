package com.linkwechat.controller;

import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.community.WeGroupSop;
import com.linkwechat.domain.community.query.WeGroupSopQuery;
import com.linkwechat.domain.community.vo.WeGroupSopVo;
import com.linkwechat.service.IWeGroupSopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 社群运营群sop
 */
@RestController
@RequestMapping(value = "/communityGroupSop")
public class WeCommunityGroupSopController extends BaseController {

    @Autowired
    private IWeGroupSopService iWeGroupSopService;



    /**
     * 通过过滤条件获取群sop列表
     *
     * @param ruleName  规则名称
     * @param createBy  创建者
     * @param beginTime 创建区间 - 开始时间
     * @param endTime   创建区间 - 结束时间
     * @return 群sop规则列表
     */
    @GetMapping(path = "/list")
    public TableDataInfo<List<WeGroupSopVo>> getSopList(
            @RequestParam(value = "ruleName", required = false) String ruleName,
            @RequestParam(value = "createBy", required = false) String createBy,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime
    )
    {
        startPage();
        List<WeGroupSopVo> groupSopVoList = iWeGroupSopService.getGroupSopList(ruleName, createBy, beginTime, endTime);
        return getDataTable(groupSopVoList);
    }


    /**
     * 通过规则id获取sop规则
     *
     * @param ruleId 规则id
     * @return 结果
     */
    @GetMapping(path = "/{ruleId}")
    public AjaxResult<WeGroupSopVo> getGroupSop(@PathVariable("ruleId") Long ruleId) {
        WeGroupSopVo groupSopVo = iWeGroupSopService.getGroupSopById(ruleId);
        if (null == groupSopVo) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "该群SOP规则不存在");
        }
        return AjaxResult.success(groupSopVo);
    }

    /**
     * 新增SOP规则
     *
     * @param groupSopDto 更新数据
     * @return 结果
     */
    @PostMapping(path = "/")
    public AjaxResult addGroupSop(@Validated @RequestBody WeGroupSopQuery groupSopDto) {

        WeGroupSop weGroupSop = new WeGroupSop();
        BeanUtils.copyProperties(groupSopDto, weGroupSop);

        if (iWeGroupSopService.isNameOccupied(weGroupSop.getRuleName())) {
            return AjaxResult.error(HttpStatus.BAD_REQUEST, "规则名称已存在");
        }

        try {
            iWeGroupSopService.addGroupSop(weGroupSop, groupSopDto.getChatIdList(), groupSopDto.getMaterialIdList(), groupSopDto.getPicList());


        }catch (Exception e){
            return AjaxResult.error(e.getMessage());
        }

        return AjaxResult.success();
    }



    /**
     * 更改SOP规则
     *
     * @param ruleId      SOP规则 id
     * @param groupSopDto 更新数据
     * @return 结果
     */
    @PutMapping(path = "/{ruleId}")
    public AjaxResult updateGroupSop(@PathVariable Long ruleId, @Validated @RequestBody WeGroupSopQuery groupSopDto) {
        // 校验是否存在
        WeGroupSop oldWeGroupSop = iWeGroupSopService.getById(ruleId);

        if (null == oldWeGroupSop) {
            return AjaxResult.error(HttpStatus.NOT_FOUND, "该群SOP规则不存在");
        }
        WeGroupSop weGroupSop = new WeGroupSop();
        weGroupSop.setRuleId(ruleId);
        BeanUtils.copyProperties(groupSopDto, weGroupSop);

        // 校验规则名是否可用
        if(!oldWeGroupSop.getRuleName().equals(groupSopDto.getRuleName())){
            if (iWeGroupSopService.isNameOccupied(weGroupSop.getRuleName())) {
                return AjaxResult.error(HttpStatus.BAD_REQUEST, "规则名称已存在");
            }
        }



        iWeGroupSopService.updateGroupSop(weGroupSop, groupSopDto.getChatIdList(), groupSopDto.getMaterialIdList(), groupSopDto.getPicList());

        return AjaxResult.success();
    }


    /**
     * 根据id列表批量删除群sop规则
     *
     * @param ids 群sop规则列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteSopRule(@PathVariable("ids") Long[] ids) {
        iWeGroupSopService.batchRemoveGroupSopByIds(ids);
        return AjaxResult.success();
    }




}
