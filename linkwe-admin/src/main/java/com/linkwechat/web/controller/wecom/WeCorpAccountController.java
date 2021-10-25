package com.linkwechat.web.controller.wecom;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业id相关配置Controller
 *
 * @author ruoyi
 * @date 2020-08-24
 */
@RestController
@RequestMapping("/wecom/corp")
@Api("企业id配置接口")
public class WeCorpAccountController extends BaseController
{
    @Autowired
    private IWeCorpAccountService weCorpAccountService;


    //  @PreAuthorize("@ss.hasPermi('wechat:corp:list')")
    @GetMapping("/list")
    @ApiOperation("查询企业id相关配置列表")
    public TableDataInfo list(WeCorpAccount weCorpAccount)
    {
        startPage();
        List<WeCorpAccount> list = weCorpAccountService.list(new LambdaQueryWrapper<WeCorpAccount>()
                .eq(WeCorpAccount::getDelFlag,Constants.NORMAL_CODE)
                .like(WeCorpAccount::getCompanyName,weCorpAccount.getCompanyName()));
        return getDataTable(list);
    }


    /******************************************************
     *************************Lw2.0相关start****************
     *****************************************************/

    /**
     * 获取当前可用企业相关配置
     * @return
     */
    @GetMapping("/findCurrentCorpAccount")
    public AjaxResult findCurrentCorpAccount(){

        WeCorpAccount weCorpAccount=new WeCorpAccount();

        List<WeCorpAccount> weCorpAccounts = weCorpAccountService.list(new LambdaQueryWrapper<WeCorpAccount>()
                .eq(WeCorpAccount::getDelFlag,Constants.NORMAL_CODE));

        if(CollectionUtil.isNotEmpty(weCorpAccounts)){
            weCorpAccount=weCorpAccounts.stream().findFirst().get();
        }

        return AjaxResult.success(weCorpAccount);

    }


    /**
     * 新增或编辑企业配置相关
     * @param weCorpAccount
     * @return
     */
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody WeCorpAccount weCorpAccount){

        weCorpAccountService.saveOrUpdate(weCorpAccount);

        return AjaxResult.success();
    }


    /******************************************************
     *************************Lw2.0相关end****************
     *****************************************************/




    //   @PreAuthorize("@ss.hasPermi('wechat:corp:add')")
    @Log(title = "新增企业id相关配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增企业id相关配置")
    public AjaxResult add(@Validated @RequestBody WeCorpAccount weCorpAccount)
    {
        weCorpAccountService.save(weCorpAccount);

        return AjaxResult.success();
    }

    //   @PreAuthorize("@ss.hasPermi('wechat:corp:edit')")
    @Log(title = "修改企业id相关配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改企业id相关配置")
    public AjaxResult edit(@Validated @RequestBody WeCorpAccount weCorpAccount)
    {
        weCorpAccountService.updateById(weCorpAccount);
        return AjaxResult.success();
    }


    //  @PreAuthorize("@ss.hasPermi('wechat:corp:startVailWeCorpAccount')")
    @Log(title = "启用有效企业微信账号", businessType = BusinessType.DELETE)
    @PutMapping("/startVailWeCorpAccount/{corpId}")
    @ApiOperation("启用有效企业微信账号")
    public AjaxResult startWeCorpAccount(@PathVariable String corpId)
    {
        return toAjax(weCorpAccountService.startVailWeCorpAccount(corpId));
    }


    //   @PreAuthorize("@ss.hasPermi('wechat:corp:startCustomerChurnNoticeSwitch')")
    @Log(title = "客户流失通知开关", businessType = BusinessType.UPDATE)
    @PutMapping("/startCustomerChurnNoticeSwitch/{status}")
    @ApiOperation("客户流失通知开关")
    public AjaxResult startCustomerChurnNoticeSwitch(@PathVariable String status)
    {
        weCorpAccountService.startCustomerChurnNoticeSwitch(status);

        return AjaxResult.success();
    }


    //   @PreAuthorize("@ss.hasPermi('wechat:corp:getCustomerChurnNoticeSwitch')")
    @Log(title = "客户流失通知开关查询", businessType = BusinessType.OTHER)
    @GetMapping("/getCustomerChurnNoticeSwitch")
    @ApiOperation("客户流失通知开关查询")
    public AjaxResult getCustomerChurnNoticeSwitch()
    {
        return AjaxResult.success("操作成功",weCorpAccountService.getCustomerChurnNoticeSwitch());
    }

}
