package com.linkwechat.controller;

import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.fegin.QwCorpClient;
import com.linkwechat.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/corp")
public class WeCorpAccountController extends BaseController {

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;


    /**
     * 获取当前租户信息
     *
     * @return
     */
    @GetMapping("/findCurrentCorpAccount")
    public AjaxResult<WeCorpAccount> findCurrentCorpAccount() {
        WeCorpAccount corpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);
        return AjaxResult.success(corpAccount);
    }


    /**
     * 新增或更新企业配置相关
     *
     * @param weCorpAccount
     * @return
     */
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody WeCorpAccount weCorpAccount) {
        if(linkWeChatConfig.isDemoEnviron()){
            return AjaxResult.error("当前为演示环境,无法修改配置");
        }

        iWeCorpAccountService.addOrUpdate(weCorpAccount);
        return AjaxResult.success();
    }


    /**
     * 客户流失通知开关
     *
     * @param status
     * @return
     */
    @PutMapping("/startCustomerChurnNoticeSwitch/{status}")
    public AjaxResult startCustomerChurnNoticeSwitch(@PathVariable String status) {
        iWeCorpAccountService.startCustomerChurnNoticeSwitch(status);
        return AjaxResult.success();
    }


    /**
     * 客户流失通知开关查询
     *
     * @return
     */
    @GetMapping("/getCustomerChurnNoticeSwitch")
    public AjaxResult getCustomerChurnNoticeSwitch() {
        return AjaxResult.success("操作成功",iWeCorpAccountService.getCustomerChurnNoticeSwitch());
    }


}
