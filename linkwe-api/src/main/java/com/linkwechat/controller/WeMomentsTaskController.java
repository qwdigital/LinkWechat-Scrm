package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.constant.SynchRecordConstants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.moments.query.WeMomentsSyncGroupSendRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskAddRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskEstimateCustomerNumRequest;
import com.linkwechat.domain.moments.query.WeMomentsTaskListRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;
import com.linkwechat.service.IWeMomentsCustomerService;
import com.linkwechat.service.IWeMomentsTaskService;
import com.linkwechat.service.IWeSynchRecordService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/06 17:17
 */
@Api(tags = "朋友圈")
@RestController
@RequestMapping("/moments")
public class WeMomentsTaskController extends BaseController {

    @Resource
    private IWeMomentsTaskService weMomentsTaskService;
    @Resource
    private IWeSynchRecordService iWeSynchRecordService;
    @Resource
    private IWeMomentsCustomerService weMomentsCustomerService;


    /**
     * 获取朋友圈列表
     *
     * @param request 列表请求参数
     * @return {@link TableDataInfo}
     * @author WangYX
     * @date 2023/06/06 18:11
     */
    @GetMapping("/list")
    public TableDataInfo list(WeMomentsTaskListRequest request) {
        startPage();
        List<WeMomentsTaskVO> moments = weMomentsTaskService.selectList(request);
        //转化率
        moments.stream().forEach(i -> {
            if (i.getTotal().equals(0)) {
                i.setFinishRate("0%");
            } else {
                i.setFinishRate(i.getExecuted() / i.getTotal() / 100 + "%");
            }
        });
        TableDataInfo dataTable = getDataTable(moments);
        //最近同步时间
        dataTable.setLastSyncTime(iWeSynchRecordService.findUpdateLatestTime(SynchRecordConstants.SYNCH_MOMENTS));
        return dataTable;
    }

    /**
     * 新增朋友圈
     *
     * @param request 新增朋友圈参数
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/06/13 15:02
     */
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody WeMomentsTaskAddRequest request) {
        return AjaxResult.success(weMomentsTaskService.add(request));
    }

    /**
     * 预估客户数量
     *
     * @author WangYX
     * @date 2023/06/07 17:01
     * @version 1.0.0
     */
    @PostMapping("/estimate/num")
    public AjaxResult getEstimateCustomerNum(@Validated @RequestBody WeMomentsTaskEstimateCustomerNumRequest request) {
        Integer integer = weMomentsCustomerService.estimateCustomerNum(request);
        return AjaxResult.success(integer);
    }

    /**
     * 详情
     *
     * @author WangYX
     * @date 2023/06/09 11:28
     * @version 1.0.0
     */
    @GetMapping("/get/{weMomentsTaskId}")
    public AjaxResult get(@PathVariable("weMomentsTaskId") Long weMomentsTaskId) {
        return AjaxResult.success(weMomentsTaskService.get(weMomentsTaskId));
    }

    /**
     * 停止发送朋友圈
     *
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/06/12 10:46
     */
    @GetMapping("/cancel/{weMomentsTaskId}")
    public AjaxResult cancel(@PathVariable("weMomentsTaskId") Long weMomentsTaskId) {
        weMomentsTaskService.cancelSendMoments(weMomentsTaskId);
        return AjaxResult.success();
    }

    /**
     * 提醒执行
     *
     * @author WangYX
     * @date 2023/06/13 17:35
     * @version 1.0.0
     */
    @GetMapping("/reminder/execution/{weMomentsTaskId}")
    public AjaxResult reminderExecution(@PathVariable("weMomentsTaskId") Long weMomentsTaskId) {
        weMomentsTaskService.reminderExecution(weMomentsTaskId);
        return AjaxResult.success();
    }

    /**
     * 同步朋友圈
     *
     * @param filterType 朋友圈类型 0：企业发表  1：个人发表  2：所有，包括个人创建以及企业创建，默认情况下为所有类型
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/06/12 10:53
     */
    @GetMapping("/sync/{filterType}")
    public AjaxResult sync(@PathVariable(value = "filterType") Integer filterType) {
        //TODO 这里的同步必须进行执行的次数限制，短时间内多次执行没有任何意义
        weMomentsTaskService.syncMoments(filterType);
        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }

    /**
     * 成员群发类型任务，员工完成任务
     *
     * @author WangYX
     * @date 2023/06/13 18:17
     * @version 1.0.0
     */
    @PostMapping("/group/send/finish")
    public AjaxResult groupSendFinish(@RequestBody WeMomentsSyncGroupSendRequest request) {
        weMomentsTaskService.groupSendFinish(request);
        return AjaxResult.success();
    }


    //---------------------------------------------------------------------------------------------------------------//


    /**
     * 新增或者编辑朋友圈
     *
     * @return
     */
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody WeMomentsTask weMoments) {
        weMomentsTaskService.addOrUpdateMoments(weMoments);
        return AjaxResult.success();
    }


    /**
     * 获取朋友圈详情
     *
     * @param id
     * @return
     */
    @GetMapping("/findMomentsDetail/{id}")
    public AjaxResult findMomentsDetail(@PathVariable("id") Long id) {
        return AjaxResult.success(weMomentsTaskService.findMomentsDetail(id));
    }


    /**
     * 朋友圈同步
     *
     * @param filterType 0:企业朋友圈；1:个人朋友圈
     * @return {@link AjaxResult}
     * @author WangYX
     * @date 2023/06/12 18:03
     */
    @GetMapping("/syncMoments")
    public AjaxResult syncMoments(@RequestParam(defaultValue = "0") Integer filterType) {
        weMomentsTaskService.syncMoments(filterType);
        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }


    /**
     * 个人朋友圈互动数据同步
     *
     * @param userIds
     * @return
     */
    @GetMapping("/synchMomentsInteracte/{userIds}")
    public AjaxResult synchMomentsInteracte(@PathVariable String[] userIds) {
        weMomentsTaskService.synchMomentsInteracte(CollectionUtil.newArrayList(userIds));
        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }
}
