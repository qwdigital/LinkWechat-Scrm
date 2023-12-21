package com.linkwechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.SiteStatsConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeFormSurveyCatalogue;
import com.linkwechat.domain.WeFormSurveyRadio;
import com.linkwechat.domain.form.query.*;
import com.linkwechat.service.IWeFormSurveyAnswerService;
import com.linkwechat.service.IWeFormSurveyCatalogueService;
import com.linkwechat.service.IWeFormSurveyRadioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 智能表单Controller
 *
 * @author danmo
 * @date 2022-10-20 10:08
 */
@RestController
@RequestMapping("/form")
@Api(tags = "智能表单接口")
@Slf4j
public class WeFormSurveyController extends BaseController {

    @Autowired
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;

    @Autowired
    private IWeFormSurveyAnswerService weFormSurveyAnswerService;

    @Autowired
    private IWeFormSurveyRadioService weFormSurveyRadioService;

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 新增用户填写信息
     */
    @PostMapping("/add")
    @ApiOperation("新增用户填写信息")
    public AjaxResult addAnswer(@RequestBody @Validated WeAddFormSurveyAnswerQuery query) {
        weFormSurveyAnswerService.addAnswer(query);
        return AjaxResult.success();
    }

    /**
     * 查询表单详情
     */
    @GetMapping(value = "/survey/getInfo")
    public AjaxResult<WeFormSurveyCatalogue> getInfo(WeAddFormSurveyAnswerQuery query) {
        WeFormSurveyCatalogue info = weFormSurveyCatalogueService.getInfo(query.getId(),query.getAddr(), query.getDataSource(), true);
        return AjaxResult.success(info);
    }

    /**
     * 站点统计
     *
     * @return
     */
    @PostMapping("/statistic/siteStas")
    @ApiOperation("站点统计")
    public AjaxResult siteStas(@RequestBody @Validated WeFormSiteStasQuery weFormSiteStasQuery) {
        Long belongId = weFormSiteStasQuery.getBelongId();
        String dataSource = weFormSiteStasQuery.getDataSource();
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueService.getWeFormSurveyCatalogueById(belongId);
        String channelsPath = weFormSurveyCatalogue.getChannelsPath();
        String channelsName = weFormSurveyCatalogue.getChannelsName();
        String[] paths = channelsPath.split(",");
        String[] names = channelsName.split(",");

        for (int i = 0; i < paths.length; i++) {
            if (paths[i].equals(dataSource)) {
                dataSource = names[i];
                break;
            }
        }
        //PV
        String pvKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_PV, belongId, dataSource);
        redisTemplate.opsForValue().increment(pvKey);
        //IP
        String ipKey = StringUtils.format(SiteStatsConstants.PREFIX_KEY_IP, belongId, dataSource);
        log.info("请求的IP地址：{}", weFormSiteStasQuery.getIpAddr());
        redisTemplate.opsForSet().add(ipKey, weFormSiteStasQuery.getIpAddr());


        return AjaxResult.success();
    }

    @PostMapping("/statistic/insertPieValue")
    @ApiOperation("保存饼图数据")
    public AjaxResult insertPieValue(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        WeFormSurveyRadio tQuRadio = new WeFormSurveyRadio();
        List pieList = JSONObject.parseObject(query.getAnswer(), List.class);
        for (int i = 0; i < pieList.size(); i++) {
            Map newMap = (Map) pieList.get(i);
            String formCodeId = newMap.get("formCodeId").toString();
            if (formCodeId.equals("6") || formCodeId.equals("7")
                    || formCodeId.equals("8") || formCodeId.equals("9")) {
                String defaultValue = newMap.get("defaultValue").toString();
                String label = newMap.get("label").toString();
                String formId = newMap.get("formId").toString();
                if (!formCodeId.equals("9")) {
                    String options = newMap.get("options").toString();
                    tQuRadio.setOptions(options);
                }
                String questionNumber = newMap.get("questionNumber").toString();
                tQuRadio.setId(SnowFlakeUtil.nextId());
                tQuRadio.setFormCodeId(formCodeId);
                tQuRadio.setLabel(label);
                tQuRadio.setFormId(formId);
                tQuRadio.setDefaultValue(defaultValue);
                tQuRadio.setQuestionNumber(questionNumber);
                if (StringUtils.isNotBlank(query.getDataSource())) {
                    tQuRadio.setDataSource(query.getDataSource());
                }
                weFormSurveyRadioService.save(tQuRadio);
            }
        }
        return AjaxResult.success();
    }
}
