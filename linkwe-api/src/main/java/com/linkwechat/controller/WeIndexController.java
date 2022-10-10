package com.linkwechat.controller;

import cn.hutool.core.date.DateUtil;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.index.vo.WeIndexVo;
import com.linkwechat.domain.operation.vo.WeCustomerAnalysisVo;
import com.linkwechat.domain.operation.vo.WeGroupAnalysisVo;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeOperationCenterService;
import com.linkwechat.service.IWeSynchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * 首页相关
 */
@RestController
public class WeIndexController {
    @Autowired
    private IWeCustomerService iWeCustomerService;

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Autowired
    private IWeOperationCenterService weOperationCenterService;

    @Autowired
    private IWeSynchRecordService iWeSynchRecordService;


    /**
     * 系统首页相关基础数据获取
     * @return
     * @throws ParseException
     */
    @GetMapping("/getWeIndex")
    public AjaxResult<WeIndexVo> getWeIndex() throws ParseException {
        WeIndexVo weIndexVo=new WeIndexVo();
        weIndexVo.setCurrentEdition("标准版");
        weIndexVo.setUserNumbers(iWeCustomerService.findAllSysUser().size());

        WeCustomerAnalysisVo customerAnalysis = weOperationCenterService.getCustomerAnalysis();
        if(null != customerAnalysis){
            weIndexVo.setCustomerTotalNumber(
                    customerAnalysis.getTotalCnt()
            );
        }

        WeGroupAnalysisVo groupAnalysis = weOperationCenterService.getGroupAnalysis();
        if(null != groupAnalysis){
            weIndexVo.setGroupTotalNumber(
                    groupAnalysis.getTotalCnt()
            );
            weIndexVo.setGroupMemberTotalNumber(
                    groupAnalysis.getMemberTotalCnt()
            );
        }

        weIndexVo.setSynchTime(DateUtil.date());

        return AjaxResult.success(weIndexVo);
    }
}
