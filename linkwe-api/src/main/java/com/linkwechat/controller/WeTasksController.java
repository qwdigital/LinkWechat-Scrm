package com.linkwechat.controller;


import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.task.vo.WeTasksVO;
import com.linkwechat.service.IWeTasksService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
        return getDataTable(weTasksService.myList());
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


}

