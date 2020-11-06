package com.linkwechat.web.controller.wecom;

import java.util.List;

import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.Threads;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.wecom.domain.vo.WeMakeCustomerTag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.service.IWeCustomerService;

/**
 * 企业微信客户Controller
 * 
 * @author ruoyi
 * @date 2020-09-13
 */
@RestController
@RequestMapping("/wecom/customer")
public class WeCustomerController extends BaseController
{
    @Autowired
    private IWeCustomerService weCustomerService;

    /**
     * 查询企业微信客户列表
     */
    @PreAuthorize("@ss.hasPermi('customerManage:customer:list')")
    @GetMapping("/list")
    public TableDataInfo list(WeCustomer weCustomer)
    {
        startPage();
        List<WeCustomer> list = weCustomerService.selectWeCustomerList(weCustomer);
        return getDataTable(list);
    }


    /**
     * 根据员工ID获取客户
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage:customer:list')")
    @GetMapping("/getCustomersByUserId/{userId}")
    public AjaxResult getCustomersByUserId(@PathVariable String userId){


         return AjaxResult.success(weCustomerService.getCustomersByUserId(userId));
    }

    /**
     * 导出企业微信客户列表
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:export')")
    @Log(title = "企业微信客户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(WeCustomer weCustomer)
    {
        List<WeCustomer> list = weCustomerService.selectWeCustomerList(weCustomer);
        ExcelUtil<WeCustomer> util = new ExcelUtil<WeCustomer>(WeCustomer.class);
        return util.exportExcel(list, "customer");
    }

    /**
     * 获取企业微信客户详细信息
     */
    @PreAuthorize("@ss.hasPermi('customerManage:customer:view')")
    @GetMapping(value = "/{externalUserId}")
    public AjaxResult getInfo(@PathVariable("externalUserId") String externalUserId)
    {
        return AjaxResult.success(weCustomerService.selectWeCustomerById(externalUserId));
    }



    /**
     * 修改企业微信客户
     */
    @PreAuthorize("@ss.hasPermi('wecom:customer:edit')")
    @Log(title = "企业微信客户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody WeCustomer weCustomer)
    {
        weCustomerService.saveOrUpdate(weCustomer);
        return AjaxResult.success();
    }


    /**
     * 客户同步接口
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage:customer:sync')")
    @Log(title = "企业微信客户同步接口", businessType = BusinessType.DELETE)
    @GetMapping("/synchWeCustomer")
    public AjaxResult synchWeCustomer(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        try {
            Threads.SINGLE_THREAD_POOL.execute(new Runnable() {
                @Override
                public void run() {
                    SecurityContextHolder.setContext(securityContext);
                    weCustomerService.synchWeCustomer();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success(WeConstans.SYNCH_TIP);

    }


    /**
     * 客户打标签
     * @param weMakeCustomerTag
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage/customer:makeTag')")
    @Log(title = "客户打标签", businessType = BusinessType.UPDATE)
    @PostMapping("/makeLabel")
    public AjaxResult makeLabel(@RequestBody WeMakeCustomerTag weMakeCustomerTag){

        weCustomerService.makeLabel(weMakeCustomerTag);

        return AjaxResult.success();
    }


    /**
     * 移除客户标签
     * @return
     */
    @PreAuthorize("@ss.hasPermi('customerManage:customer:removeTag')")
    @Log(title = "移除客户标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeLabel")
    public AjaxResult removeLabel(@RequestBody WeMakeCustomerTag weMakeCustomerTag){

        weCustomerService.removeLabel(weMakeCustomerTag);

        return AjaxResult.success();

    }

}
