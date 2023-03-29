package com.linkwechat.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysDictData;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.MapUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.domain.WeCustomerSeas;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.storecode.entity.WeStoreCode;
import com.linkwechat.domain.storecode.entity.WeStoreCodeConfig;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.storecode.entity.WeStoreCodeCount;
import com.linkwechat.domain.storecode.vo.WeStoreCodesVo;
import com.linkwechat.domain.storecode.vo.datareport.WeStoreGroupReportVo;
import com.linkwechat.domain.storecode.vo.datareport.WeStoreShopGuideReportVo;
import com.linkwechat.domain.storecode.vo.drum.WeStoreGroupDrumVo;
import com.linkwechat.domain.storecode.vo.drum.WeStoreShopGuideDrumVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreGroupTabVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreShopGuideTabVo;
import com.linkwechat.domain.storecode.vo.tab.WeStoreTabVo;
import com.linkwechat.domain.storecode.vo.trend.WeStoreGroupTrendVo;
import com.linkwechat.domain.storecode.vo.trend.WeStoreShopGuideTrendVo;
import com.linkwechat.service.IWeQrAttachmentsService;
import com.linkwechat.service.IWeStoreCodeConfigService;
import com.linkwechat.service.IWeStoreCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 门店活码
 */
@RestController
@RequestMapping("/storeCode")
public class WeStoreCodeController extends BaseController {

    @Autowired
    private IWeStoreCodeConfigService iWeStoreCodeConfigService;


    @Autowired
    private IWeStoreCodeService iWeStoreCodeService;

    @Autowired
    private IWeQrAttachmentsService attachmentsService;

    @Autowired
    private MapUtils mapUtils;


    /**
     *  获取门店导购码或门店群活码
     * @param storeCodeType
     * @return
     */
    @GetMapping("/config/{storeCodeType}")
    public AjaxResult<WeStoreCodeConfig> getStoreCodeConfig(@PathVariable Integer storeCodeType){

        WeStoreCodeConfig storeCodeConfig = iWeStoreCodeConfigService.getOne(new LambdaQueryWrapper<WeStoreCodeConfig>()
                .eq(WeStoreCodeConfig::getStoreCodeType, storeCodeType));
        if(null != storeCodeConfig){
            storeCodeConfig.setWeQrAttachments(
                    attachmentsService.list(new LambdaQueryWrapper<WeQrAttachments>()
                            .eq(WeQrAttachments::getQrId, storeCodeConfig.getId())
                            .eq(WeQrAttachments::getBusinessType,2))
            );
        }

        return AjaxResult.success(
                storeCodeConfig
        );
    }


    /**
     * 下载导购或门前群活码
     * 门店码类型(1:门店导购码;2:门店群活码)
     */
    @Log(title = "群活码下载", businessType = BusinessType.OTHER)
    @GetMapping("/downloadStoreCodeConfigUrl")
    public void downloadStoreCodeConfigUrl(Integer storeCodeType, HttpServletResponse response) {
        WeStoreCodeConfig storeCodeConfig = iWeStoreCodeConfigService.getOne(new LambdaQueryWrapper<WeStoreCodeConfig>()
                .eq(WeStoreCodeConfig::getStoreCodeType, storeCodeType));
        try {
            if(storeCodeConfig != null && StringUtils.isNotEmpty(storeCodeConfig.getStoreCodeConfigQr())){
                FileUtils.downloadFile(storeCodeConfig.getStoreCodeConfigQr(), response.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 新增或边际门店活码或群活码
     * @param storeCodeConfig
     * @return
     */
    @PostMapping("/config/createOrUpdate")
    public AjaxResult createOrUpdateConfig(@RequestBody WeStoreCodeConfig storeCodeConfig) throws IOException {

        iWeStoreCodeConfigService.createOrUpdate(storeCodeConfig);

        return AjaxResult.success();
    }


    /**
     * 新建或编辑门店
     * @param weStoreCode
     * @return
     */
    @PostMapping("/code/createOrUpdateStoreCode")
    public AjaxResult createOrUpdateStoreCode(@RequestBody WeStoreCode weStoreCode){

        iWeStoreCodeService.createOrUpdateStoreCode(weStoreCode);

        return AjaxResult.success();
    }


    /**
     * 获取门店列表
     * @param weStoreCode
     * @return
     */
    @GetMapping("/storeCodes")
    public TableDataInfo storeCodes(WeStoreCode weStoreCode){
        startPage();
        return getDataTable(
             iWeStoreCodeService.storeCodes(weStoreCode)
        );
    }

    /**
     * 跟进门店id获取门店详情
     * @param storeId
     * @return
     */
    @GetMapping("/getWeStoreCodeById/{storeId}")
    public AjaxResult<WeStoreCode> getWeStoreCodeById(@PathVariable Long storeId){
        return AjaxResult.success(
                iWeStoreCodeService.getById(storeId)
        );
    }

    /**
     * 通过id列表批量删除任务
     *
     * @param ids id列表
     * @return 结果
     */
    @DeleteMapping(path = "/{ids}")
    public AjaxResult batchDeleteStoreCode(@PathVariable("ids") Long[] ids) {
        iWeStoreCodeService.removeByIds(Arrays.asList(ids));
        return AjaxResult.success();
    }


    /**
     * 批量启用或停用门店
     * @param ids
     * @param weStoreCode
     * @return
     */
    @PutMapping("/batchStartOrStop/{ids}")
    public AjaxResult batchStartOrStop(@PathVariable("ids") Long[] ids,@RequestBody WeStoreCode weStoreCode){

        iWeStoreCodeService.update(WeStoreCode.builder()
                        .storeState(weStoreCode.getStoreState())
                .build(), new LambdaQueryWrapper<WeStoreCode>()
                .in(WeStoreCode::getId,Arrays.asList(ids)));

        return AjaxResult.success();

    }


    /***************************************************************
     **************************统计相关tab相关 start*******************
     ****************************************************************/



    /**
     * 门店导购码统计-头部tab
     * @return
     */
    @GetMapping("/countWeStoreShopGuideTab")
    public AjaxResult<WeStoreShopGuideTabVo> countWeStoreShopGuideTab(){

        return AjaxResult.success(
                iWeStoreCodeService.countWeStoreShopGuideTab()
        );
    }


    /**
     * 门店码统计-头部tab
     * @param storeCodeId
     * @return
     */
    @GetMapping("/countWeStoreTab/{storeCodeId}")
    public AjaxResult<WeStoreTabVo> countWeStoreTab(@PathVariable Long storeCodeId){


        return AjaxResult.success(
                iWeStoreCodeService.countWeStoreTab(storeCodeId)
        );
    }


    /**
     *  门店群活码统计-头部tab
     * @return
     */
    @GetMapping("/countWeStoreGroupTab")
    public AjaxResult<WeStoreGroupTabVo> countWeStoreGroupTab(){

        return AjaxResult.success(
                iWeStoreCodeService.countWeStoreGroupTab()
        );
    }



    /***************************************************************
     **************************统计相关tab相关 end********************
     ****************************************************************/




    /***************************************************************
     **************************统计相关趋势图相关 start****************
     ****************************************************************/

    /**
     * 门店导购码趋势图
     * @param weStoreCode
     * @return
     */
    @GetMapping("/countStoreShopGuideTrend")
    public AjaxResult<List<WeStoreShopGuideTrendVo>> countStoreShopGuideTrend(WeStoreCode weStoreCode){


        return AjaxResult.success(
                iWeStoreCodeService.countStoreShopGuideTrend(weStoreCode)
        );
    }


    /**
     * 门店群码趋势图
     * @param weStoreCode
     * @return
     */
    @GetMapping("/countStoreGroupTrend")
    public AjaxResult<List<WeStoreGroupTrendVo>> countStoreGroupTrend(WeStoreCode weStoreCode){


        return AjaxResult.success(
                iWeStoreCodeService.countStoreGroupTrend(weStoreCode)
        );
    }

    /***************************************************************
     **************************统计相关趋势图相关 end******************
     ****************************************************************/




    /***************************************************************
     **************************统计相关Top 10相关 start****************
     ****************************************************************/


    /**
     *  门店导购码统计-门店新增客户top10
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/countStoreShopGuideDrum")
    public AjaxResult<List<WeStoreShopGuideDrumVo>> countStoreShopGuideDrum(String beginTime,String endTime){

        return AjaxResult.success(
                iWeStoreCodeService.countStoreShopGuideDrum(beginTime,endTime)
        );
    }


    /**
     *  门店群码统计-门店客群新增客户top10
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/countStoreShopGroupDrum")
    public AjaxResult<List<WeStoreGroupDrumVo>> countStoreShopGroupDrum(String beginTime,String endTime){

        return AjaxResult.success(
                iWeStoreCodeService.countStoreShopGroupDrum(beginTime,endTime)
        );
    }





    /***************************************************************
     **************************统计相关Top 10相关 end****************
     ****************************************************************/


    /***************************************************************
     **************************统计数据报表相关 start****************
     ****************************************************************/

    /**
     * 门店导购统计-数据报表
     * @return
     */
    @GetMapping("/countShopGuideReport")
    public TableDataInfo<WeStoreShopGuideReportVo> countShopGuideReport(WeStoreCode weStoreCode){
           startPage();
           return getDataTable(
                   iWeStoreCodeService.countShopGuideReport(weStoreCode)
           );
    }

    /**
     *  门店导购统计-数据报表导出
     * @param weStoreCode
     * @return
     */
    @GetMapping("/exportCountShopGuideReport")
    public AjaxResult exportCountShopGuideReport(WeStoreCode weStoreCode){
        List<WeStoreShopGuideReportVo> weStoreShopGuideReportVos
                = iWeStoreCodeService.countShopGuideReport(weStoreCode);
        ExcelUtil<WeStoreShopGuideReportVo> util = new ExcelUtil<WeStoreShopGuideReportVo>(WeStoreShopGuideReportVo.class);
        return util.exportExcel(weStoreShopGuideReportVos, DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD)+"_门店导购统计");

    }


    /**
     *  门店群码统计-数据报表
     * @param weStoreCode
     * @return
     */
    @GetMapping("/countStoreGroupReport")
    public TableDataInfo<WeStoreGroupReportVo> countStoreGroupReport(WeStoreCode weStoreCode){
        startPage();
        return getDataTable(
                iWeStoreCodeService.countStoreGroupReport(weStoreCode)
        );
    }


    /**
     * 门店群码统计-数据报表导出
     * @param weStoreCode
     * @return
     */
    @GetMapping("/exportCountStoreGroupReport")
    public AjaxResult exportCountStoreGroupReport(WeStoreCode weStoreCode){
        List<WeStoreGroupReportVo> weStoreGroupReportVos
                = iWeStoreCodeService.countStoreGroupReport(weStoreCode);
        ExcelUtil<WeStoreGroupReportVo> util = new ExcelUtil<WeStoreGroupReportVo>(WeStoreGroupReportVo.class);
        return util.exportExcel(weStoreGroupReportVos, DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD)+"_门店群码统计");

    }


    /***************************************************************
     **************************统计数据报表相关 end********************
     ****************************************************************/



    /**
     * 门店模版下载
     * @return
     */
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {

        ExcelUtil<WeStoreCode> util = new ExcelUtil<WeStoreCode>(WeStoreCode.class);
        return util.importTemplateExcel( DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD)+"_门店模版");
    }



    /**
     * 导入门店
     * @param file
     * @return
     */
    @PostMapping("/importData")
    public AjaxResult  importData(MultipartFile file) throws Exception {
        ExcelUtil<WeStoreCode> util = new ExcelUtil<WeStoreCode>(WeStoreCode.class);
        List<WeStoreCode> weStoreCode = util.importExcel(file.getInputStream());
        String tip=new String("成功导入{0}条");
        if(CollectionUtil.isNotEmpty(weStoreCode)){
            //过滤字段为空的数据
            List<WeStoreCode> deduplicationWeStoreCode = weStoreCode.stream().filter(s -> StringUtils.isNotEmpty(s.getStoreName())
                    && StringUtils.isNotEmpty(s.getArea()) && StringUtils.isNotEmpty(s.getAddress()) ).collect(Collectors.toList());
            if(CollectionUtil.isEmpty(deduplicationWeStoreCode)){
                return AjaxResult.error("导入用户数据不能为空！");
            }

            //根据门店名称去重(去除excel中重复的号码)
            List<WeStoreCode> deduplicationSeasNoRepeat=deduplicationWeStoreCode.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                    new TreeSet<>(Comparator.comparing(WeStoreCode :: getStoreName))), ArrayList::new));

            //根据手机号去除数据库与excel中重复号码
            List<WeStoreCode> dbExist = iWeStoreCodeService.list(new LambdaQueryWrapper<WeStoreCode>()
                    .in(WeStoreCode::getStoreName, deduplicationSeasNoRepeat.stream()
                            .map(WeStoreCode::getStoreName).collect(Collectors.toList())));
            if(CollectionUtil.isNotEmpty(dbExist)){
                List<WeStoreCode> noRepetWeCustomerSeas
                        = deduplicationSeasNoRepeat.stream().filter(item ->
                        !dbExist.stream().map(e->e.getStoreName()).collect(Collectors.toList())
                                .contains(item.getStoreName())).collect(Collectors.toList());
                deduplicationSeasNoRepeat.clear();
                deduplicationSeasNoRepeat.addAll(
                        noRepetWeCustomerSeas
                );
            }

            if(CollectionUtil.isNotEmpty(deduplicationSeasNoRepeat)){
                deduplicationSeasNoRepeat.stream().forEach(k->{
                    Map<String, String> lMap
                            = mapUtils.addressTolongitudea(k.getArea() + k.getAddress());
                    k.setLatitude(lMap.get(MapUtils.lat));
                    k.setLongitude(lMap.get(MapUtils.lng));
                });
//                List<WeStoreCode> weStoreCodeList = iWeStoreCodeService.getBaseMapper().selectList(new LambdaQueryWrapper<WeStoreCode>()
//                        .eq(WeStoreCode::getDelFlag, 0));
//                List<String> lst = weStoreCodeList.stream().map(WeStoreCode::getStoreName).collect(Collectors.toList());
                deduplicationSeasNoRepeat = deduplicationSeasNoRepeat.stream().filter(item -> item.getStoreName().length()<30).collect(Collectors.toList());
                if(iWeStoreCodeService.saveBatch(deduplicationSeasNoRepeat)){
                    tip = MessageFormat.format(tip, new Object[]{new Integer(deduplicationSeasNoRepeat.size()).toString()});
                }

            }else{
                tip = MessageFormat.format(tip, new Object[] { "0"});
            }

        }else{
            return AjaxResult.error("导入用户数据不能为空！");
        }
        return AjaxResult.success(
                tip
        );
    }


    /**
     * 获取附件门店
     * @param storeCodeType 门店码类型(1:门店导购码;2:门店群活码)
     * @param unionid 微信unionid
     * @param longitude 经度
     * @param latitude 纬度
     * @param area 区域
     * @return
     */
    @GetMapping("/findStoreCode")
    public AjaxResult<WeStoreCodesVo> findStoreCode(Integer storeCodeType, String unionid, String longitude, String latitude, String area){

        return AjaxResult.success(
                iWeStoreCodeService.findStoreCode(storeCodeType,unionid,longitude,latitude,area)
        );
    }


    /**
     * 获取门店对应的配置相关
     * @param storeCodeType
     * @return
     */
    @GetMapping("/findWeStoreCodeConfig")
    public AjaxResult<WeStoreCodeConfig> findWeStoreCodeConfig(Integer storeCodeType){


        return AjaxResult.success(
                iWeStoreCodeConfigService.getWeStoreCodeConfig(storeCodeType)
        );
    }


    /**
     * 记录用户扫码行为
     * @return
     */
    @PostMapping("/countUserBehavior")
    public AjaxResult countUserBehavior(@RequestBody WeStoreCodeCount weStoreCodeCount){
        iWeStoreCodeService.countUserBehavior(weStoreCodeCount);
        return AjaxResult.success();
    }













}
