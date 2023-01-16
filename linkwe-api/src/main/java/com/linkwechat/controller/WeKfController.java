package com.linkwechat.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.file.FileUtils;
import com.linkwechat.domain.kf.query.*;
import com.linkwechat.domain.kf.vo.*;
import com.linkwechat.service.*;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 客服管理
 * @date 2022/1/18 21:44
 **/
@Slf4j
@Api(tags = "客服管理")
@RestController
@RequestMapping("/kf/")
public class WeKfController extends BaseController {

    @Autowired
    private IWeKfInfoService weKfInfoService;

    @Autowired
    private IWeKfServicerService weKfServicerService;

    @Autowired
    private IWeKfScenesService weKfScenesService;

    @Autowired
    private IWeKfPoolService weKfPoolService;

    @Autowired
    private IWeKfMsgService weKfMsgService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @ApiOperation("创建客服")
    @PostMapping("/add")
    public AjaxResult addAccount(@Validated @RequestBody WeAddKfInfoQuery query) {
        log.info("创建客服入参：param:{}", JSONObject.toJSONString(query));
        Long id = weKfInfoService.addAccount(query);
        return AjaxResult.success(id);
    }

    @ApiOperation("新增客服欢迎语")
    @PostMapping("/add/welcome")
    public AjaxResult addAccountWelcome(@Validated @RequestBody WeAddKfWelcomeQuery query) {
        log.info("新增客服欢迎语：param:{}", JSONObject.toJSONString(query));
        weKfInfoService.addAccountWelcome(query);
        return AjaxResult.success();
    }

    @ApiOperation("添加客服接待人员")
    @PostMapping("/add/servicer")
    public AjaxResult addAccountServicer(@RequestBody WeAddKfServicerQuery query) {
        log.info("添加客服接待人员入参：param:{}", JSONObject.toJSONString(query));
        weKfInfoService.addAccountServicer(query);
        return AjaxResult.success();
    }

    @ApiOperation("客服账号修改")
    @PostMapping("/edit/account")
    public AjaxResult editAccountInfo(@RequestBody WeAddKfInfoQuery query) {
        log.info("客服账号修改入参：param:{}", JSONObject.toJSONString(query));
        weKfInfoService.editAccountInfo(query);
        return AjaxResult.success();
    }

    @ApiOperation("客服欢迎语修改")
    @PostMapping("/edit/welcome")
    public AjaxResult editAccountWelcome(@RequestBody WeAddKfWelcomeQuery query) {
        log.info("客服欢迎语修改入参：param:{}", JSONObject.toJSONString(query));
        weKfInfoService.editAccountWelcome(query);
        return AjaxResult.success();
    }

    @ApiOperation("客服接待人员修改")
    @PostMapping("/edit/servicer")
    public AjaxResult editAccountServicer(@RequestBody WeAddKfServicerQuery query) {
        log.info("客服接待人员修改入参：param:{}", JSONObject.toJSONString(query));
        weKfInfoService.editAccountServicer(query);
        return AjaxResult.success();
    }

    @ApiOperation("客服详情")
    @GetMapping("/get/{id}")
    public AjaxResult<WeKfInfoVo> getKfInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(weKfInfoService.getKfInfo(id));
    }

    @ApiOperation("客服列表(分页)")
    @GetMapping("/list/page")
    public TableDataInfo<QwKfListVo> getKfPageList(WeKfListQuery query) {
        log.info("客服列表入参(分页)：param:{}", JSONObject.toJSONString(query));
        startPage();
        PageInfo<QwKfListVo> kfList = weKfInfoService.getKfPageList(query);
        return getDataTable(kfList);
    }

    @ApiOperation("客服列表")
    @GetMapping("/list")
    public TableDataInfo<QwKfListVo> getKfList(WeKfListQuery query) {
        log.info("客服列表入参：param:{}", JSONObject.toJSONString(query));
        List<QwKfListVo> kfList = weKfInfoService.getKfList(query);
        return getDataTable(kfList);
    }

    @ApiOperation("同步客服列表")
    @GetMapping("/list/async")
    public AjaxResult asyncKfList() {
        log.info("同步客服列表");
        weKfInfoService.asyncKfList();
        return AjaxResult.success();
    }

    @ApiOperation("删除客服")
    @DeleteMapping("/delete/{id}")
    public AjaxResult delKfInfo(@PathVariable("id") Long id) {
        if(linkWeChatConfig.isDemoEnviron()){
            return AjaxResult.error("当前环境不可删除");
        }
        weKfInfoService.delKfInfo(id);
        return AjaxResult.success();
    }


    @ApiOperation("客服接待人员列表")
    @GetMapping("/servicer/list")
    public TableDataInfo<WeKfServicerListVo> getKfServicerList(WeKfServicerListQuery query) {
        log.info("客服接待人员列表入参：param:{}", JSONObject.toJSONString(query));
        startPage();
        List<WeKfServicerListVo> kfServicerList = weKfServicerService.getKfServicerList(query);
        return getDataTable(kfServicerList);
    }


    @ApiOperation("新增场景")
    @PostMapping("/scenes/add")
    public AjaxResult addKfScenes(@Validated @RequestBody WeAddKfScenesQuery query) {
        log.info("新增场景入参：param:{}", JSONObject.toJSONString(query));
        weKfScenesService.addKfScenes(query);
        return AjaxResult.success();
    }

    @ApiOperation("删除场景")
    @DeleteMapping("/scenes/delete/{ids}")
    public AjaxResult delKfScenes(@PathVariable("ids") List<Long> ids) {
        if(linkWeChatConfig.isDemoEnviron()){
            return AjaxResult.error("当前环境不可删除");
        }
        weKfScenesService.delKfScenes(ids);
        return AjaxResult.success();
    }

    @ApiOperation("修改场景")
    @PutMapping("/scenes/edit")
    public AjaxResult editKfScenes(@Validated @RequestBody WeEditKfScenesQuery query) {
        log.info("修改场景入参：param:{}", JSONObject.toJSONString(query));
        weKfScenesService.editKfScenes(query);
        return AjaxResult.success();
    }

    @ApiOperation("场景列表")
    @GetMapping("/scenes/list")
    public TableDataInfo<WeKfScenesListVo> getScenesList(WeKfScenesQuery query) {
        log.info("场景列表入参：param:{}", JSONObject.toJSONString(query));
        startPage();
        List<WeKfScenesListVo> scenesList = weKfScenesService.getScenesList(query);
        return getDataTable(scenesList);
    }

    /**
     * 场景链接批量下载
     *
     * @param ids 场景值ids
     * @param request 请求
     * @param response 输出
     * @throws Exception
     */
    @ApiOperation(value = "场景链接批量下载", httpMethod = "GET")
    @GetMapping("/scenes/batch/download")
    public void batchDownload(@RequestParam("ids") List<Long> ids, HttpServletRequest request, HttpServletResponse response) throws IOException {
        WeKfScenesQuery query = new WeKfScenesQuery();
        query.setIds(ids);
        List<WeKfScenesListVo> scenesList = weKfScenesService.getScenesList(query);
        if(CollectionUtil.isNotEmpty(scenesList)){
            List<FileUtils.FileEntity> fileList = scenesList.stream().map(item -> {
                return  FileUtils.FileEntity.builder()
                        .fileName( item.getKfName() + "-" + item.getName())
                        .url(item.getQrCode())
                        .suffix(".jpg")
                        .build();
            }).collect(Collectors.toList());
            FileUtils.batchDownloadFile(fileList, response.getOutputStream());
        }
    }

    @ApiOperation("咨询记录列表")
    @GetMapping("/record/list")
    public TableDataInfo<WeKfRecordListVo> getRecordList(WeKfRecordQuery query) {
        log.info("咨询记录列表入参：param:{}", JSONObject.toJSONString(query));
        startPage();
        List<WeKfRecordListVo> recordList = weKfPoolService.getRecordList(query);
        return getDataTable(recordList);
    }

    @ApiOperation("咨询记录详情")
    @GetMapping("/record/detail")
    public TableDataInfo<WeKfRecordVo> getRecordDetail(WeKfRecordQuery query) {
        if(query == null){
            throw new WeComException("参数不能为空");
        }
        if(StringUtils.isEmpty(query.getOpenKfId())){
            throw new WeComException("客服账号ID不能为空");
        }
        if(StringUtils.isEmpty(query.getExternalUserId())){
            throw new WeComException("客户ID不能为空");
        }
        log.info("咨询记录详情入参：param:{}", JSONObject.toJSONString(query));
        startPage();
        List<WeKfRecordVo> recordList = weKfMsgService.getRecordDetail(query);
        return getDataTable(recordList);
    }

    @ApiOperation(value = "咨询记录导出-导出", httpMethod = "GET")
    @GetMapping("/record/export")
    public void getRecordExport(WeKfRecordQuery query) throws IOException {
        List<WeKfRecordListVo> recordList = weKfPoolService.getRecordList(query);
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("咨询记录", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), WeKfRecordListVo.class).sheet("咨询记录").doWrite(recordList);
    }


    @ApiOperation("会话分析接口")
    @GetMapping("/msg/analyze")
    public AjaxResult<WeKfMsgAnalyzeVo> getMsgAnalyze(WeKfRecordQuery query) {
        if(Objects.isNull(query)){
            throw new WeComException("参数不能为空");
        }
        if(StringUtils.isEmpty(query.getOpenKfId())){
            throw new WeComException("客服ID不能为空");
        }
        if(StringUtils.isEmpty(query.getExternalUserId())){
            throw new WeComException("客户ID不能为空");
        }
        if(StringUtils.isEmpty(query.getBeginTime())){
            throw new WeComException("开始时间不能为空");
        }
        WeKfMsgAnalyzeVo weKfMsgAnalyzeVo = weKfMsgService.getMsgAnalyze(query);
        return AjaxResult.success(weKfMsgAnalyzeVo);
    }
}
