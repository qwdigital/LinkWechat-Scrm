package com.linkwechat.controller;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
import com.linkwechat.domain.storecode.query.WxStoreCodeQuery;
import com.linkwechat.domain.storecode.vo.WeStoreCodesVo;
import com.linkwechat.service.IWeStoreCodeConfigService;
import com.linkwechat.service.IWeStoreCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 门店活码
 */
@RestController
@RequestMapping("/storeCode")
public class WxStoreCodeController extends BaseController {

    @Autowired
    private IWeStoreCodeConfigService iWeStoreCodeConfigService;


    @Autowired
    private IWeStoreCodeService iWeStoreCodeService;

    /**
     * 获取附件门店
     * @param wxStoreCodeQuery
     * @return
     */
    @GetMapping("/findStoreCode")
    public AjaxResult<WeStoreCodesVo> findStoreCode(WxStoreCodeQuery wxStoreCodeQuery){
        wxStoreCodeQuery.setIsCount(true);
        return AjaxResult.success(
                iWeStoreCodeService.findStoreCode(wxStoreCodeQuery)
        );
    }


    /**
     * 获取门店对应的配置相关
     * @param wxStoreCodeQuery
     * @return
     */
    @GetMapping("/findWeStoreCodeConfig")
    public AjaxResult<WeStoreCodeConfig> findWeStoreCodeConfig(WxStoreCodeQuery wxStoreCodeQuery){

        wxStoreCodeQuery.setIsCount(true);
        return AjaxResult.success(
                iWeStoreCodeConfigService.getWeStoreCodeConfig(wxStoreCodeQuery)
        );
    }



}
