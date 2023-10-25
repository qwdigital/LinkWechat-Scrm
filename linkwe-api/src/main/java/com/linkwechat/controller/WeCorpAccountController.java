package com.linkwechat.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.welcomemsg.WeDefaultWelcomeMsg;
import com.linkwechat.domain.welcomemsg.vo.WeDefaultWelcomeMsgVo;
import com.linkwechat.fegin.QwCorpClient;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeDefaultWelcomeMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping(value = "/corp")
public class WeCorpAccountController extends BaseController {

    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Autowired
    private IWeDefaultWelcomeMsgService iWeDefaultWelcomeMsgService;


    @Autowired
    private  QwCorpClient qwCorpClient;

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
        if (linkWeChatConfig.isDemoEnviron()) {
            return AjaxResult.error("当前为演示环境,无法修改配置");
        }

        if (iWeCorpAccountService.saveOrUpdate(weCorpAccount)) {
            qwCorpClient.removeAllWeAccessToken(weCorpAccount.getCorpId());
            //商城配置
//            saveOrUpdateShopConfig(weCorpAccount);
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
        return AjaxResult.success("操作成功", iWeCorpAccountService.getCustomerChurnNoticeSwitch());
    }

//    /**
//     * 商城配置
//     *
//     * @param weCorpAccount
//     */
//    private void saveOrUpdateShopConfig(WeCorpAccount weCorpAccount) {
//        //商城系统新增或修改
//        try {
//            JSONObject jsonObject = new JSONObject();
//            //小程序配置
//            if (StringUtils.isNotBlank(weCorpAccount.getShopAppId())) {
//                jsonObject.put("wxapp_appId", weCorpAccount.getShopAppId().trim());
//            }
//            if (StringUtils.isNotBlank(weCorpAccount.getShopSecret())) {
//                jsonObject.put("wxapp_secret", weCorpAccount.getShopSecret().trim());
//            }
//            //支付配置
//            if (StringUtils.isNotBlank(weCorpAccount.getMerChantNumber())) {
//                jsonObject.put("wxpay_mchId", weCorpAccount.getMerChantNumber().trim());
//            }
//            if (StringUtils.isNotBlank(weCorpAccount.getMerChantSecret())) {
//                jsonObject.put("wxpay_mchKey", weCorpAccount.getMerChantSecret().trim());
//            }
//            if (StringUtils.isNotBlank(weCorpAccount.getCertP12Url())) {
//                jsonObject.put("wxpay_keyPath", weCorpAccount.getCertP12Url().trim());
//            }
//            //小程序消息消息推送配置
//            if (StringUtils.isNotBlank(weCorpAccount.getShopMaToken())) {
//                jsonObject.put("wechat_ma_token", weCorpAccount.getShopMaToken().trim());
//            }
//            if (StringUtils.isNotBlank(weCorpAccount.getShopMaEncodingaeskey())) {
//                jsonObject.put("wechat_ma_encodingaeskey", weCorpAccount.getShopMaEncodingaeskey().trim());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("商城系统-小程序配置失败！！！");
//        }
//    }
//

    /**
     * 设置默认欢迎语
     * @param attachments  WeDefaultWelcomeMsgVo welcomeMsgVo
     * @return
     */
    @PostMapping("/editWelComeMsg")
    public AjaxResult editWelComeMsg(@RequestBody List<WeMessageTemplate> attachments){
        iWeDefaultWelcomeMsgService.saveOrUpdateBatchWeComeMsg(attachments);
        return AjaxResult.success();
    }



    /**
     * 获取默认欢迎语
     * @return
     */
    @GetMapping("/findDefaultWelcomeMsg")
    public AjaxResult<WeDefaultWelcomeMsg> findDefaultWelcomeMsg(){

        return AjaxResult.success(
                iWeDefaultWelcomeMsgService.list(new LambdaQueryWrapper<WeDefaultWelcomeMsg>())
        );
    }
}
