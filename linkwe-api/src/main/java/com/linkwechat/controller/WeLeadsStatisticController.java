package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.leads.leads.vo.WeLeadsDataTrendVO;
import com.linkwechat.service.IWeLeadsStatisticService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 线索统计
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/19 10:08
 */
@Api(tags = "线索统计")
@RestController
@RequestMapping("/leads/statistic")
public class WeLeadsStatisticController extends BaseController {

    @Resource
    private IWeLeadsStatisticService weLeadsStatisticService;

    /**
     * 统计
     *
     * @param
     * @return {@link AjaxResult<Map<String,Object>>}
     * @author WangYX
     * @date 2023/07/19 10:11
     */
    @ApiOperation("统计")
    @GetMapping("")
    public AjaxResult<Map<String, Object>> statistic() {
        return AjaxResult.success(weLeadsStatisticService.statistic());
    }

    /**
     * 数据趋势
     *
     * @param beginTime 开始时间 yyyy-MM-dd
     * @param endTime   结束时间 yyyy-MM-dd
     * @return {@link AjaxResult<List<WeLeadsDataTrendVO>>}
     * @author WangYX
     * @date 2023/07/19 11:05
     */
    @ApiOperation("数据趋势")
    @GetMapping("/data/trend")
    public AjaxResult<List<WeLeadsDataTrendVO>> dataTrend(@RequestParam("beginTime") String beginTime, @RequestParam("beginTime") String endTime) {
        return AjaxResult.success(weLeadsStatisticService.dataTrend(beginTime, endTime));
    }

    @ApiOperation("线索转化top5")
    @GetMapping("/conversion/top")
    public AjaxResult conversionTop5() {

        return null;
    }

    @ApiOperation("线索日跟进top5")
    @GetMapping("/follow/top")
    public AjaxResult followTop5() {

        return null;
    }

    /**
     * 线索导入记录
     *
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/07/19 10:18
     */
    @ApiOperation("导入记录")
    @GetMapping("/import/record")
    public TableDataInfo importRecord() {

        return null;
    }

    @ApiOperation("员工统计")
    @GetMapping("/user")
    public TableDataInfo userStatistic() {

        return null;
    }


}
