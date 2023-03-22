package com.linkwechat.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.RedEnvelopesType;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.envelopes.WeRedEnvelopes;
import com.linkwechat.domain.envelopes.WeRedEnvelopesLimit;
import com.linkwechat.domain.envelopes.WeRedEnvelopesRecord;
import com.linkwechat.domain.envelopes.WeUserRedEnvelopsLimit;
import com.linkwechat.domain.envelopes.query.H5RedEnvelopesParmQuery;
import com.linkwechat.domain.envelopes.query.WeRedEnvelopeListQuery;
import com.linkwechat.domain.envelopes.vo.RedEnvelopesBaseInfoVo;
import com.linkwechat.domain.envelopes.vo.WeCutomerRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeGroupRedEnvelopesVo;
import com.linkwechat.service.IWeCorpAccountService;
import com.linkwechat.service.IWeRedEnvelopesService;
import com.linkwechat.service.IWeUserRedEnvelopsLimitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 红包相关接口
 */
@Api(tags = "红包接口管理")
@RestController
@RequestMapping(value = "/RedEnvelopes")
public class WxRedEnvelopesController extends BaseController {

    @Autowired
    private IWeRedEnvelopesService iWeRedEnvelopesService;


    @Autowired
    private IWeUserRedEnvelopsLimitService iWeUserRedEnvelopsLimitService;


    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    /**
     * 红包领取
     * @param orderNo
     * @param openId
     * @param receiveName
     * @param avatar
     * @return
     * @throws Exception
     */
    @GetMapping("/receiveRedEnvelopes")
    public AjaxResult receiveRedEnvelopes(String orderNo,String openId,String receiveName,String avatar) throws Exception {

        String returnMsg = iWeRedEnvelopesService.customerReceiveRedEnvelopes(orderNo, openId,receiveName,avatar);

        if(StringUtils.isNotEmpty(returnMsg)){
            return AjaxResult.error(HttpStatus.NOT_IMPLEMENTED,returnMsg);
        }
        return AjaxResult.success(
                returnMsg
        );
    }


    /**
     * 红包领取列表
     * @param orderNo
     * @param openId
     * @param chatId
     * @return
     */
    @GetMapping("/receiveRedEnvelopesLists")
    public AjaxResult receiveRedEnvelopesLists(String orderNo,String openId,String chatId){

        return AjaxResult.success(
                iWeRedEnvelopesService.detailDto(orderNo,openId,chatId)
        );
    }

    /**
     * 获取红包基础信息
     * @return
     */
    @GetMapping("/findRedEnvelopesInfo")
    public AjaxResult<RedEnvelopesBaseInfoVo> findRedEnvelopesInfo(){
        RedEnvelopesBaseInfoVo baseInfo=RedEnvelopesBaseInfoVo.builder().build();
        WeCorpAccount validWeCorpAccount = iWeCorpAccountService.getCorpAccountByCorpId(null);
        if(validWeCorpAccount !=null){
            baseInfo.setLogo(validWeCorpAccount.getLogoUrl());
            baseInfo.setCorpName(validWeCorpAccount.getCompanyName());
//            baseInfo.setWeAppId(validWeCorpAccount.getWxAppId());
//            baseInfo.setWeAppSecret(baseInfo.getWeAppSecret());
        }
        return AjaxResult.success(baseInfo);
    }


    /**
     * 校验客户是否领取红包
     * @param orderNo 订单id
     * @param openId 客户id
     * @return
     */
    @GetMapping("/checkRecevieRedEnvelopes")
    public AjaxResult checkRecevieRedEnvelopes(String orderNo,String openId){

        return AjaxResult.success(
                iWeRedEnvelopesService.checkRecevieRedEnvelopes(orderNo,openId)
        );

    }

}
