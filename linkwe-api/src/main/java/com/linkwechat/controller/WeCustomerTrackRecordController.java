package com.linkwechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomerTrackRecord;
import com.linkwechat.service.IWeCustomerTrackRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 跟进记录相关
 */
@RestController
@RequestMapping("/trackRecord")
public class WeCustomerTrackRecordController extends BaseController {

    @Autowired
    private IWeCustomerTrackRecordService iWeCustomerTrackRecordService;

    /**
     * 跟进记录
     * @param externalUserid
     * @param weUserId
     * @return
     */
    @GetMapping("/followUpRecord")
    public TableDataInfo followUpRecord(String externalUserid, String weUserId){

        startPage();
        List<WeCustomerTrackRecord> trackRecords = iWeCustomerTrackRecordService.list(new LambdaQueryWrapper<WeCustomerTrackRecord>()
                .eq(StringUtils.isNotEmpty(externalUserid),WeCustomerTrackRecord::getExternalUserid, externalUserid)
                .eq(StringUtils.isNotEmpty(weUserId),WeCustomerTrackRecord::getWeUserId, weUserId));

        return getDataTable(trackRecords);

    }



}
