package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.wecom.client.WeMomentsClient;
import com.linkwechat.wecom.constants.SynchRecordConstants;
import com.linkwechat.wecom.domain.WeMoments;
import com.linkwechat.wecom.domain.dto.moments.MomentsCreateResultDto;
import com.linkwechat.wecom.service.IWeMomentsService;
import com.linkwechat.wecom.service.IWeSynchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 朋友圈相关
 */
@RestController
@RequestMapping("/wecom/moments")
public class WeMomentsController extends BaseController {


    @Autowired
    IWeMomentsService iWeMomentsService;

    @Autowired
    IWeSynchRecordService iWeSynchRecordService;


    @Autowired
    WeMomentsClient weMomentsClient;



    /**
     * 获取朋友圈列表
     * @param weMoments
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo list(WeMoments weMoments) {
        startPage();
        List<WeMoments> moments = iWeMomentsService.findMoments(weMoments);
        TableDataInfo dataTable = getDataTable(moments);
        dataTable.setLastSyncTime(
                iWeSynchRecordService.findUpdateLatestTime( weMoments.getType().equals(new Integer(0))?
                        SynchRecordConstants.SYNCH_CUSTOMER_ENTERPRISE_MOMENTS:SynchRecordConstants.SYNCH_CUSTOMER_PERSON_MOMENTS)
        );//最近同步时间

        return dataTable;
    }


    /**
     * 获取朋友圈详情
     * @param momentId
     * @return
     */
    @GetMapping("/findMomentsDetail/{momentId}")
    public AjaxResult findMomentsDetail(@PathVariable String momentId){


        return AjaxResult.success(
                iWeMomentsService.findMomentsDetail(momentId)
        );
    }

    /**
     * 新增或者编辑朋友圈
     * @return
     */
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody WeMoments weMoments) throws InterruptedException {

        iWeMomentsService.addOrUpdateMoments(weMoments);


        return AjaxResult.success();
    }


    /**
     * 朋友圈同步
     * @param filterType 0:企业朋友圈；1:个人朋友圈
     * @return
     */
    @GetMapping("/synchMoments")
    public AjaxResult synchMoments(@RequestParam(defaultValue = "0") Integer filterType){

        try {
            if(filterType.equals(new Integer(0))){
                iWeMomentsService.synchEnterpriseMoments(filterType);
            }else if(filterType.equals(new Integer(1))){
                iWeMomentsService.synchPersonMoments(filterType);
            }
        }catch (CustomException e){
            return AjaxResult.error(e.getMessage());
        }


        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }


    /**
     * 个人朋友圈互动数据同步
     * @param userIds
     * @return
     */
    @GetMapping("/synchMomentsInteracte/{userIds}")
    public AjaxResult synchMomentsInteracte(@PathVariable String[] userIds){

        iWeMomentsService.synchMomentsInteracte(CollectionUtil.newArrayList(userIds));

        return AjaxResult.success(WeConstans.SYNCH_TIP);
    }




}
