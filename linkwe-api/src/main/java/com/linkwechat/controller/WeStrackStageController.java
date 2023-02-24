package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.MapUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeStrackStage;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeStrackStageService;
import org.apache.ibatis.util.MapUtil;
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
    public AjaxResult<WeStrackStage> add(@RequestBody WeStrackStage weStrackStage){

        iWeStrackStageService.add(weStrackStage);
        return AjaxResult.success(weStrackStage);
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
    @DeleteMapping(path = "/delete")
    public AjaxResult delete(String id,Integer growStageVal) {

        iWeStrackStageService.remove(id,growStageVal);
        return AjaxResult.success();
    }


    /**
     * 获取指定商阶状态下的客户数量
     * @param stageVal
     * @return
     */
    @GetMapping("/getStrackStageCustomerNum/{stageVal}")
    public AjaxResult getStrackStageCustomerNum(@PathVariable Integer stageVal){


        return AjaxResult.success(
                MapUtil.entry("customerNumbwe",iWeCustomerService.count(new LambdaQueryWrapper<WeCustomer>()
                        .eq(WeCustomer::getTrackState,stageVal)))
        );

    }
}
