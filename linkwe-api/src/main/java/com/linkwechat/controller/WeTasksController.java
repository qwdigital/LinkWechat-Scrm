package com.linkwechat.controller;


import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.task.vo.WeTasksVO;
import com.linkwechat.service.IWeTasksService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 待办任务 前端控制器
 * </p>
 *
 * @author WangYX
 * @since 2023-07-20
 */
@RestController
@RequestMapping("/weTasks")
public class WeTasksController extends BaseController {

    @Resource
    private IWeTasksService weTasksService;

    /**
     * 我的任务
     *
     * @return {@link TableDataInfo} 分页数据
     * @author WangYX
     * @date 2023/07/20 17:57
     */
    @ApiOperation("我的任务")
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        List<WeTasksVO> vos = weTasksService.myList();
        TableDataInfo dataTable = getDataTable(vos);
        return dataTable;
    }


    /**
     * 历史任务
     *
     * @return {@link TableDataInfo} 分页数据
     * @author WangYX
     * @date 2023/07/20 17:57
     */
    @ApiOperation("历史任务")
    @GetMapping("/history")
    public TableDataInfo history() {
        startPage();
        return getDataTable(weTasksService.history());
    }

    /**
     * 完成代办任务
     *
     * @param id 任务Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/07/21 14:43
     */
    @ApiOperation("完成代办任务")
    @PutMapping("/finish/{id}")
    public AjaxResult finish(@PathVariable("id") Long id) {
        weTasksService.finish(id);
        return AjaxResult.success();
    }


}

