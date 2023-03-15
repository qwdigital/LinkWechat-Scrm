package com.linkwechat.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.service.IWeFissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/**
 * 裂变相关
 */
@RestController
@RequestMapping("/new/fission")
public class WeFissionController  extends BaseController {

    @Autowired
    IWeFissionService iWeFissionService;


    /**
     * 获取裂变列表
     * @param weFission
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<List<WeFission>> list(WeFission weFission){
        startPage();

        return getDataTable(
                iWeFissionService.findWeFissions(weFission)
        );

    }


    /**
     * 创建裂变
     * @param weFission
     * @return
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody WeFission weFission){

        iWeFissionService.save(weFission);
        return AjaxResult.success();
    }


    /**
     * 更新裂变
     * @param weFission
     * @return
     */
    @PutMapping("/update")
    public AjaxResult update(@RequestBody WeFission weFission){

        iWeFissionService.updateById(weFission);

        return AjaxResult.success();
    }


    /**
     * 获取任务信息
     * @param id
     * @return
     */
    @GetMapping("/getWeFissionDetail/{id}")
    public AjaxResult<WeFission> getWeFissionDetail(@PathVariable Long id){

        return AjaxResult.success(
                iWeFissionService.getById(id)
        );

    }



    /**
     * 通过id批量删除裂变
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteFission(@PathVariable("ids") Long[] ids) {
        iWeFissionService.removeByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


}
