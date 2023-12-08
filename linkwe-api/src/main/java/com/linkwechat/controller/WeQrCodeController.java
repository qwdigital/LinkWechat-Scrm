package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.exception.CustomException;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.domain.qirule.vo.WeQiRuleWeeklyDetailListVo;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import com.linkwechat.domain.qr.query.WeQrCodeListQuery;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.domain.shortlink.vo.WeShortLinkVo;
import com.linkwechat.handler.WeQiRuleWeeklyUserDetailWriteHandler;
import com.linkwechat.service.IWeQrCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 活码管理
 * @date 2021/11/12 18:22
 **/
@Slf4j
@RestController
@RequestMapping(value = "qr")
@Api(tags = "活码管理")
public class WeQrCodeController extends BaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IWeQrCodeService weQrCodeService;

    @ApiOperation(value = "新增活码", httpMethod = "POST")
    @Log(title = "活码管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addQrCode(@RequestBody @Validated WeQrAddQuery weQrAddQuery) {
        weQrCodeService.addQrCode(weQrAddQuery);
        return AjaxResult.success();
    }

    @ApiOperation(value = "修改活码", httpMethod = "POST")
    @Log(title = "活码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/update")
    public AjaxResult updateQrCode(@RequestBody @Validated WeQrAddQuery weQrAddQuery) {
        if(Objects.isNull(weQrAddQuery.getQrId())){
            throw new WeComException("活码ID不能为空！");
        }
        weQrCodeService.updateQrCode(weQrAddQuery);
        return AjaxResult.success();
    }


    @ApiOperation(value = "获取活码详情", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @GetMapping("/get/{id}")
    public AjaxResult<WeQrCodeDetailVo> getQrDetail(@PathVariable("id") Long Id) {
        WeQrCodeDetailVo qrDetail = weQrCodeService.getQrDetail(Id);
        return AjaxResult.success(qrDetail);
    }

    @ApiOperation(value = "获取活码列表", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<WeQrCodeDetailVo> getQrCodeList(WeQrCodeListQuery qrCodeListQuery) {
        startPage();
        PageInfo<WeQrCodeDetailVo> qrCodeList = weQrCodeService.getQrCodeList(qrCodeListQuery);
        return getDataTable(qrCodeList);
    }

    @ApiOperation(value = "删除活码", httpMethod = "DELETE")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @DeleteMapping("/del/{ids}")
    public AjaxResult<WeQrCodeDetailVo> delQrCode(@PathVariable("ids") List<Long> ids) {
        weQrCodeService.delQrCode(ids);
        return AjaxResult.success();
    }

    @ApiOperation(value = "获取活码统计", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.SELECT)
    @GetMapping("/scan/count")
    public AjaxResult<WeQrCodeScanCountVo> getWeQrCodeScanCount(WeQrCodeListQuery qrCodeListQuery) {
        WeQrCodeScanCountVo weQrCodeScanCount = weQrCodeService.getWeQrCodeScanCount(qrCodeListQuery);
        return AjaxResult.success(weQrCodeScanCount);
    }





    /**
     * 员工活码批量下载
     *
     * @param ids      员工活码ids
     * @param request  请求
     * @param response 输出
     * @throws Exception
     */
    @ApiOperation(value = "员工活码批量下载", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.OTHER)
    @GetMapping("/batch/download")
    public void batchDownload(@RequestParam("ids") List<Long> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<WeQrCodeDetailVo> qrCodeList = weQrCodeService.getQrDetailByQrIds(ids);
        if (CollectionUtil.isNotEmpty(qrCodeList)) {
            List< FileUtils.FileEntity> fileList = qrCodeList.stream().map(item -> {
                List<WeQrScopeUserVo> userVoList = item.getQrUserInfos().stream().map(WeQrScopeVo::getWeQrUserList).filter(CollectionUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.toList());
                String fileName = userVoList.stream().map(WeQrScopeUserVo::getUserName).collect(Collectors.joining(","));

                return  FileUtils.FileEntity.builder()
                        .fileName( fileName + "-" + item.getName())
                        .url(item.getQrCode())
                        .suffix(".jpg")
                        .build();
            }).collect(Collectors.toList());
            FileUtils.batchDownloadFile(fileList, response.getOutputStream());
        }
    }

    /**
     * 员工活码下载
     *
     * @param id       员工活码id
     * @param request  请求
     * @param response 输出
     * @throws Exception
     */
    @ApiOperation(value = "员工活码下载", httpMethod = "GET")
    @Log(title = "活码管理", businessType = BusinessType.OTHER)
    @GetMapping("/download")
    public void download(@RequestParam("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WeQrCodeDetailVo qrDetail = weQrCodeService.getQrDetail(id);
        if (StringUtils.isEmpty(qrDetail.getQrCode())) {
            throw new CustomException("活码不存在");
        } else {
            FileUtils.downloadFile(qrDetail.getQrCode(), response.getOutputStream());
        }
    }

    @ApiOperation(value = "删除活码分组", httpMethod = "GET")
    @GetMapping("/deleteGroup")
    public AjaxResult deleteQrGroup(@RequestParam("groupId") Long groupId){
        weQrCodeService.deleteQrGroup(groupId);
        return AjaxResult.success();
    }

    @ApiOperation(value = "更新多人员工活码", httpMethod = "GET")
    @Log(title = "更新多人员工活码", businessType = BusinessType.SELECT)
    @GetMapping("/update/multiplePeople")
    public AjaxResult updateQrMultiplePeople(@RequestParam("state") String state) {
        weQrCodeService.updateQrMultiplePeople(state);
        return AjaxResult.success();
    }

    @ApiOperation(value = "通过短链接获取活码详情", httpMethod = "GET")
    @GetMapping("/getByDetail/{shortUrl}")
    public AjaxResult<WeQrCodeDetailVo> getQrCodeInfoByShortUrl(@PathVariable("shortUrl") String shortUrl) {
        long id = Base62NumUtil.decode(shortUrl);
        WeQrCodeDetailVo qrDetail = weQrCodeService.getQrDetail(id);
        redisService.increment(WeConstans.WE_SHORT_LINK_COMMON_KEY + WeConstans.OPEN_APPLET + "qr:" +shortUrl);
        return AjaxResult.success(qrDetail);
    }

    @ApiOperation(value = "获取活码总数统计", httpMethod = "GET")
    @GetMapping("/scan/total")
    public AjaxResult<WeQrCodeScanCountVo> getWeQrCodeScanTotalCount(WeQrCodeListQuery qrCodeListQuery) {
        WeQrCodeScanCountVo weQrCodeScanCount = weQrCodeService.getWeQrCodeScanTotalCount(qrCodeListQuery);
        return AjaxResult.success(weQrCodeScanCount);
    }

    @ApiOperation(value = "获取活码折线图统计", httpMethod = "GET")
    @GetMapping("/scan/line")
    public AjaxResult<List<WeQrCodeScanLineCountVo>> getWeQrCodeScanLineCount(WeQrCodeListQuery qrCodeListQuery) {
        List<WeQrCodeScanLineCountVo> weQrCodeScanCount = weQrCodeService.getWeQrCodeScanLineCount(qrCodeListQuery);
        return AjaxResult.success(weQrCodeScanCount);
    }

    @ApiOperation(value = "获取活码表格统计", httpMethod = "GET")
    @GetMapping("/scan/sheet")
    public TableDataInfo<List<WeQrCodeScanLineCountVo>> getWeQrCodeScanSheetCount(WeQrCodeListQuery qrCodeListQuery) {
        List<WeQrCodeScanLineCountVo> weQrCodeScanCount = weQrCodeService.getWeQrCodeScanSheetCount(qrCodeListQuery);
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        PageInfo<WeQrCodeScanLineCountVo> pageInfo = new PageInfo<>();
        pageInfo.setTotal(weQrCodeScanCount.size());
        pageInfo.setList(startPage(weQrCodeScanCount,pageNum,pageSize));
        return getDataTable(pageInfo);
    }

    @ApiOperation(value = "获取活码表格统计导出", httpMethod = "GET")
    @GetMapping("/scan/sheet/export")
    public void getWeQrCodeScanSheetExport(WeQrCodeListQuery qrCodeListQuery) {
        List<WeQrCodeScanLineCountVo> weQrCodeScanCount = weQrCodeService.getWeQrCodeScanSheetCount(qrCodeListQuery);
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("活码数据报表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            ExcelWriterBuilder write = EasyExcel.write(response.getOutputStream(), WeQrCodeScanLineCountVo.class);
            write.sheet("数据明细").doWrite(weQrCodeScanCount);
        } catch (IOException e) {
            log.error("获取活码表格统计导出异常：query:{}", JSONObject.toJSONString(qrCodeListQuery), e);
        }
    }
}
