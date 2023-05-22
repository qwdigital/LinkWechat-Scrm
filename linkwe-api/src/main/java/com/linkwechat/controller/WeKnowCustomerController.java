package com.linkwechat.controller;


import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.know.WeKnowCustomerCode;
import com.linkwechat.domain.know.vo.WeKnowCustomerCountTrendOrTableVo;
import com.linkwechat.service.IWeKnowCustomerCodeCountService;
import com.linkwechat.service.IWeKnowCustomerCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Arrays;


/**
 * 客识码
 */
@Slf4j
@RestController
@RequestMapping("/knowCustomer/")
public class WeKnowCustomerController extends BaseController {

    @Autowired
    private IWeKnowCustomerCodeService iWeKnowCustomerCodeService;

    @Autowired
    private IWeKnowCustomerCodeCountService iWeKnowCustomerCodeCountService;


    /**
     * 新增或更新客识码
     * @param weKnowCustomerCode
     * @return
     */
    @PostMapping("/addOrUpdateKnowCustomer")
    public AjaxResult addOrUpdateKnowCustomer(@RequestBody WeKnowCustomerCode weKnowCustomerCode) throws Exception {

        iWeKnowCustomerCodeService.addOrUpdateKnowCustomer(weKnowCustomerCode,weKnowCustomerCode.getId()==null?false:true);

        return AjaxResult.success();


    }


    /**
     * 获取客识码列表
     * @param knowCustomerName 客识码名称
     * @return
     */
    @GetMapping("/findWeKnowCustomerCodes")
    public TableDataInfo findWeKnowCustomerCodes(String knowCustomerName){
        startPage();
        return getDataTable(
                iWeKnowCustomerCodeService.findWeKnowCustomerCodes(knowCustomerName)
        );

    }





    /**
     * 通过id列表批量删除识客码
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteKnowCustomer(@PathVariable("ids") Long[] ids) {
        iWeKnowCustomerCodeService.removeByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /**
     * 下载识客码
     */
    @GetMapping("/downloadKnowCustomerUrl/{id}")
    public void downloadKnowCustomerUrl(@PathVariable Long id) {
        WeKnowCustomerCode weKnowCustomerCode = iWeKnowCustomerCodeService.getById(id);
        try {
            if (weKnowCustomerCode != null && StringUtils.isNotEmpty(weKnowCustomerCode.getKnowCustomerQr())) {
                FileUtils.downloadFile(weKnowCustomerCode.getKnowCustomerQr(),  ServletUtils.getResponse().getOutputStream());
            }
        } catch (IOException e) {
            log.error("识客码下载失败:"+e.getMessage());
            throw new CustomException("识客码下载失败");
        }
    }


    /**
     * 根据id获取识客码编辑详情
     * @param id
     * @return
     */
    @GetMapping("/getKnowCustomerDetail/{id}")
    public AjaxResult getKnowCustomerDetail(@PathVariable Long id){


        return AjaxResult.success(
                iWeKnowCustomerCodeService.findWeKnowCustomerCodeDetailById(id)
        );
    }


    /**
     * 导出识客码
     * @return
     */
    @GetMapping("/exportKnowCustomers")
    public void exportKnowCustomers(){

        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeKnowCustomerCode.class,  iWeKnowCustomerCodeService.findWeKnowCustomerCodes(null),"识客码"
        );

    }


    /**
     * 获取详情统计头部tab
     * @param weKnowCustomerCode
     * @return
     */
    @GetMapping("/findWKCountDetailTab")
    public AjaxResult findWeKnowCustomerCountDetailTab(WeKnowCustomerCode weKnowCustomerCode){


        return AjaxResult.success(
                iWeKnowCustomerCodeCountService.findWeKnowCustomerCountDetailTab(weKnowCustomerCode.getAddWeUserState(), weKnowCustomerCode.getId())
        );
    }


    /**
     * 获取统计折线图数据
     * @param weKnowCustomerCode
     * @return
     */
    @GetMapping("/findWeKCountTrend")
    public AjaxResult findWeKnowCustomerCountTrend(WeKnowCustomerCode weKnowCustomerCode){

        return AjaxResult.success(
                iWeKnowCustomerCodeCountService.findWeKnowCustomerCountTrend(WelcomeMsgTypeEnum.WE_KNOW_CUSTOMER_CODE_PREFIX.getType() + weKnowCustomerCode.getId(), weKnowCustomerCode.getId(), weKnowCustomerCode.getBeginTime(), weKnowCustomerCode.getEndTime())
        );
    }


    /**
     * 获取统计table数据
     * @param weKnowCustomerCode
     * @return
     */
    @GetMapping("/findWeKCounTtable")
    public TableDataInfo findWeKnowCustomerCounTtable(WeKnowCustomerCode weKnowCustomerCode){
        startPage();

        return getDataTable(
                iWeKnowCustomerCodeCountService.findWeKnowCustomerCounTtable(WelcomeMsgTypeEnum.WE_KNOW_CUSTOMER_CODE_PREFIX.getType() + weKnowCustomerCode.getId(), weKnowCustomerCode.getId())
        );
    }



    /**
     * 导出获取统计table数据
     * @return
     */
    @GetMapping("/exportWeKnowCustomerCounTtable")
    public void exportWeKnowCustomerCounTtable(WeKnowCustomerCode weKnowCustomerCode){

        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeKnowCustomerCountTrendOrTableVo.class, iWeKnowCustomerCodeCountService.findWeKnowCustomerCounTtable(WelcomeMsgTypeEnum.WE_KNOW_CUSTOMER_CODE_PREFIX.getType() + weKnowCustomerCode.getId(), weKnowCustomerCode.getId()),"识客码数据明细"
        );

    }


}
