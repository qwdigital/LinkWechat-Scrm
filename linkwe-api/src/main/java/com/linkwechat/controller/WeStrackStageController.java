package com.linkwechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeStrackStage;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeStrackStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 商机阶段
 */
@RestController
@RequestMapping("/strackStage")
public class WeStrackStageController extends BaseController {

    @Autowired
    private IWeStrackStageService iWeStrackStageService;

    @Autowired
    private IWeCustomerService iWeCustomerService;


    /**
     * 列表
     * @return
     */
    @GetMapping("/findWeStrackStage")
    public AjaxResult<List<WeStrackStage>> findWeStrackStage(){

        return AjaxResult.success(
                iWeStrackStageService.list(new LambdaQueryWrapper<WeStrackStage>()
                        .orderByAsc(WeStrackStage::getSort))
        );
    }

    /**
     * 新增
     * @param weStrackStage
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeStrackStage weStrackStage){

        iWeStrackStageService.save(weStrackStage);
        return AjaxResult.success();
    }


    /**
     * 更新
     * @param weStrackStages
     * @return
     */
    @PutMapping("/batchUpdate")
    public AjaxResult batchUpdate(@RequestBody List<WeStrackStage> weStrackStages){

        iWeStrackStageService.updateBatchById(weStrackStages);
        return AjaxResult.success();
    }


    /**
     * 通过id删除
     *
     * @param id
     * @return 结果
     */
    @DeleteMapping(path = "/delete/{id}")
    public AjaxResult delete(@PathVariable String id) {
        WeStrackStage weStrackStage = iWeStrackStageService.getById(id);
        if(null != weStrackStage){
            int count = iWeCustomerService.count(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getTrackState, weStrackStage.getStageVal()));
            if(count>0){
                return AjaxResult.error("当前共有"+count+"个客户正处于【"+weStrackStage.getStageKey()+"】阶段，删除阶段需要为这些客户分配一个新状态，分配后立即生效且不可撤销，请谨慎操作。");
            }

        }

        iWeStrackStageService.removeById(id);

        return AjaxResult.success();
    }
}
