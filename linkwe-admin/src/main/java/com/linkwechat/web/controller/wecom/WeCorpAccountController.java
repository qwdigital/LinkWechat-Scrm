package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/wecom/corp")
public class WeCorpAccountController {

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;


    /**
     * 获取当前企业可用密钥
     * @return
     */
    @GetMapping("/findCurrentCorpAccount")
    public AjaxResult findCurrentCorpAccount(){

        return AjaxResult.success(
                iWeCorpAccountService.findValidWeCorpAccount()
        );
    }


    /**
     * 新增或更新企业配置相关
     * @param weCorpAccount
     * @return
     */
    @Log(title = "新增或更新企业配置相关", businessType = BusinessType.INSERT)
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody WeCorpAccount weCorpAccount)
    {
        iWeCorpAccountService.saveOrUpdate(weCorpAccount);

        return AjaxResult.success();
    }


    /**
     *  客户流失通知开关
     * @param status
     * @return
     */
    @Log(title = "客户流失通知开关", businessType = BusinessType.UPDATE)
    @PutMapping("/startCustomerChurnNoticeSwitch/{status}")
    public AjaxResult startCustomerChurnNoticeSwitch(@PathVariable String status)
    {
        iWeCorpAccountService.startCustomerChurnNoticeSwitch(status);

        return AjaxResult.success();
    }


    /**
     * 客户流失通知开关查询
     * @return
     */
    @Log(title = "客户流失通知开关查询", businessType = BusinessType.OTHER)
    @GetMapping("/getCustomerChurnNoticeSwitch")
    public AjaxResult getCustomerChurnNoticeSwitch()
    {
        return AjaxResult.success("操作成功",iWeCorpAccountService.getCustomerChurnNoticeSwitch());
    }
}
