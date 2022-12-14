package com.linkwechat.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.constant.SiteStasConstants;
import com.linkwechat.common.constant.SiteStasConstants;
import com.linkwechat.common.constant.SiteStatsConstants;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.vo.SysAreaVo;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.ExcelUtil;
import com.linkwechat.domain.*;
import com.linkwechat.domain.form.query.WeFormSiteStasQuery;
import com.linkwechat.domain.form.query.WeFormSurveyRadioQuery;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;
import com.linkwechat.domain.form.vo.WeFormSurveyAnswerVO;
import com.linkwechat.domain.form.vo.WeFormSurveyStatisticsVO;
import com.linkwechat.fegin.QwSysAreaClient;
import com.linkwechat.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 智能表单统计Controller
 *
 * @author HXD
 * @date 2022-06-08 09.32
 */
@RestController
@RequestMapping("/form/statistic")
@Api(tags = "智能表单统计接口")
@Slf4j
public class WeFormSurveyStatisticsController extends BaseController {

    @Autowired
    private IWeFormSurveyStatisticsService weFormSurveyStatisticsService;

    @Autowired
    private IWeFormSurveyRadioService weFormSurveyRadioService;

    @Autowired
    private IWeFormSurveyAnswerService weFormSurveyAnswerService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;

    @Resource
    private QwSysAreaClient qwSysAreaClient;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private IWeFormSurveySiteStasService weFormSurveySiteStasService;

    /**
     * 新增访问记录
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增访问记录", httpMethod = "POST")
    public AjaxResult addStatistics(@RequestBody @Validated WeFormSurveyStatistics query) {
        weFormSurveyStatisticsService.addStatistics(query);
        return AjaxResult.success();
    }

    /**
     * 查询基本表单统计信息
     */
    @GetMapping("/getStatistics")
    @ApiOperation(value = "查询基本表单统计信息", httpMethod = "GET")
    public AjaxResult getStatistics(WeFormSurveyStatistics query) {
        List<WeFormSurveyStatistics> Statistics = weFormSurveyStatisticsService.getStatistics(query);
        return AjaxResult.success(Statistics);
    }


    @PostMapping("/lineChart")
    @ApiOperation("折线图")
    public AjaxResult lineChart(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        String type = query.getType();
        startPage();
        Date startTime = null;
        Date endTime = null;
        Map<String, Object> legendData = new HashMap<>();
        String[] legendArray = new String[]{"总访问量", "总访问用户", "有效收集量"};
        legendData.put("data", legendArray);
        Map<String, Map<String, Object>> legendDataMap = new HashMap<>();
        legendDataMap.put("legend", legendData);
        Map<String, Object> xAxisData = new HashMap<>();
        if (!type.equals("customization")) {
            if (type.equals("week")) {
                startTime = DateUtil.offsetWeek(new Date(), -1);
            } else if (type.equals("month")) {
                startTime = DateUtil.offsetMonth(new Date(), -1);
            }
            endTime = new Date();
        } else {
            startTime = query.getStartDate();
            endTime = query.getEndDate();
        }
        List<DateTime> timeList = DateUtil.rangeToList(startTime, endTime, DateField.DAY_OF_YEAR);
        String[] xAxisArray = new String[timeList.size()];
        //使用for循环得到数组
        int[] totalVisitsArray = new int[timeList.size()];
        int[] totalUserArray = new int[timeList.size()];
        int[] collectionVolumeArray = new int[timeList.size()];
        String[] collectionRateArray = new String[timeList.size()];
        int[] averageTimeArray = new int[timeList.size()];
        for (int i = 0; i < timeList.size(); i++) {
            xAxisArray[i] = timeList.get(i).toDateStr();
            WeFormSurveyStatisticQuery surveyStatistics = new WeFormSurveyStatisticQuery();
            surveyStatistics.setStartDate(DateUtil.parseDate(timeList.get(i).toDateStr()));
            surveyStatistics.setEndDate(DateUtil.parseDate(timeList.get(i).toDateStr()));
            surveyStatistics.setDataSource(query.getDataSource());
            surveyStatistics.setBelongId(query.getBelongId());

            List<WeFormSurveyStatistics> statistics = weFormSurveyStatisticsService.dataList(surveyStatistics);
            if (CollectionUtil.isNotEmpty(statistics)) {
                for (WeFormSurveyStatistics list : statistics) {
                    Integer totalVisits = list.getTotalVisits();
                    Integer totalUser = list.getTotalUser();
                    Integer collectionVolume = list.getCollectionVolume();
                    String collectionRate = list.getCollectionRate();
                    Integer averageTime = list.getAverageTime();
                    totalVisitsArray[i] = totalVisits;
                    totalUserArray[i] = totalUser;
                    collectionVolumeArray[i] = collectionVolume;
                    collectionRateArray[i] = collectionRate;
                    averageTimeArray[i] = averageTime;
                }
            } else {
                totalVisitsArray[i] = 0;
                totalUserArray[i] = 0;
                collectionVolumeArray[i] = 0;
                collectionRateArray[i] = "0";
                averageTimeArray[i] = 0;
            }
        }
        Map<String, Object> totalVisitsData = new HashMap<>();
        Map<String, Object> totalUserData = new HashMap<>();
        Map<String, Object> collectionVolumeData = new HashMap<>();
        Map<String, Object> collectionRateData = new HashMap<>();
        Map<String, Object> averageTimeData = new HashMap<>();
        Map<String, Object> seriesMap = new HashMap<>();
        totalVisitsData.put("data", totalVisitsArray);
        totalVisitsData.put("name", "总访问量");
        totalUserData.put("data", totalUserArray);
        totalUserData.put("name", "总访问用户");
        collectionVolumeData.put("data", collectionVolumeArray);
        collectionVolumeData.put("name", "有效收集量");
        collectionRateData.put("data", collectionRateArray);
        collectionRateData.put("name", "收集率");
        averageTimeData.put("data", averageTimeArray);
        averageTimeData.put("name", "平均完成时间");
        xAxisData.put("data", xAxisArray);
        Map<String, Map<String, Object>> xAxisDataMap = new HashMap<>();
        xAxisDataMap.put("xAxis", xAxisData);
        List list = new ArrayList();
        list.add(totalVisitsData);
        list.add(totalUserData);
        list.add(collectionVolumeData);
        list.add(collectionRateData);
        list.add(averageTimeData);
        seriesMap.put("series", list);
        Map map = new HashMap();
        map.put("legend", legendData);
        map.put("xAxis", xAxisData);
        map.put("series", list);
        return AjaxResult.success(map);
    }


    @PostMapping("/insertPieValue")
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

    @PostMapping("/pieChart")
    @ApiOperation(value = "饼图", httpMethod = "POST")
    public AjaxResult pieChart(@RequestBody WeFormSurveyRadioQuery query) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        startPage();
        query.setLabel("pie");
        List<WeFormSurveyRadio> radioList = weFormSurveyRadioService.selectNumber(query);
        for (WeFormSurveyRadio number : radioList) {
            String questionNumber = number.getQuestionNumber();
            String options = number.getOptions();
            String label = number.getLabel();
            WeFormSurveyRadioQuery newQuery = new WeFormSurveyRadioQuery();
            newQuery.setQuestionNumber(questionNumber);
            newQuery.setFormId(query.getFormId());
            newQuery.setLabel("pie");
            if (StringUtils.isNotBlank(query.getDataSource())) {
                newQuery.setDataSource(query.getDataSource());
            }
            List<WeFormSurveyRadio> tQuRadios = weFormSurveyRadioService.selectDefaultValue(newQuery);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList();
            if (tQuRadios.size() > 0) {
                for (WeFormSurveyRadio newList : tQuRadios) {
                    Map<String, Object> map = new HashMap<>();
                    String defaultValue = newList.getDefaultValue();
                    WeFormSurveyRadioQuery newTQuRadio = new WeFormSurveyRadioQuery();
                    newTQuRadio.setFormId(query.getFormId());
                    if (StringUtils.isNotBlank(query.getDataSource())) {
                        newTQuRadio.setDataSource(query.getDataSource());
                    }
                    newTQuRadio.setDefaultValue(defaultValue);
                    newTQuRadio.setQuestionNumber(questionNumber);
                    newTQuRadio.setLabel("pie");
                    Integer value = weFormSurveyRadioService.countDefaultValue(newTQuRadio);
                    map.put("value", value);
                    map.put("name", defaultValue);
                    list.add(map);
                }
                result.put("questionNumber", questionNumber);
                result.put("options", options);
                result.put("label", label);
                result.put("data", list);
            }
            resultList.add(result);
        }
        return AjaxResult.success(resultList);
    }


    @PostMapping("/customer")
    @ApiOperation("用户统计列表")
    public AjaxResult customer(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<WeFormSurveyAnswer> total = weFormSurveyAnswerService.selectCustomerList(query);
        startPage();
        List<WeFormSurveyAnswer> customerList = weFormSurveyAnswerService.selectCustomerList(query);
        for (WeFormSurveyAnswer list : customerList) {
            Map<String, Object> map = new HashMap<>();
            String unionId = list.getUnionId();
            List<WeCustomer> weCustomers = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getUnionid, unionId).eq(WeCustomer::getDelFlag, 0));
            if (weCustomers.size() > 0) {
                map.put("isOfficeCustomer", true);
            } else {
                map.put("isOfficeCustomer", false);
            }
            map.put("data", list);
            map.put("total", total.size());
            resultList.add(map);
        }
        if (customerList.size() > 0) {
            return AjaxResult.success(resultList);
        }
        return AjaxResult.success("数据为空", resultList);
    }

    @ApiOperation(value = "数据概览列表", httpMethod = "POST")
    @PostMapping("/dataList")
    public AjaxResult dataList(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        String type = query.getType();
        int count = 0;
        if (StringUtils.isNotBlank(type) && type.equals("week")) {
            query.setStartDate(DateUtil.offsetWeek(new Date(), -1));
            query.setEndDate(DateUtil.date());
        }
        if (StringUtils.isNotBlank(type) && type.equals("month")) {
            query.setStartDate(DateUtil.offsetMonth(new Date(), -1));
            query.setEndDate(DateUtil.date());
        }
        startPage();
        List<WeFormSurveyStatistics> tSurveyList = weFormSurveyStatisticsService.dataList(query);
        List<WeFormSurveyStatisticsVO> result = new ArrayList<>();
        for (WeFormSurveyStatistics weFormSurveyStatistics : tSurveyList) {
            WeFormSurveyStatisticsVO weFormSurveyStatisticsVO = new WeFormSurveyStatisticsVO();
            weFormSurveyStatisticsVO.setCreateTime(DateUtil.offsetDay(weFormSurveyStatistics.getCreateTime(), -1));
            weFormSurveyStatisticsVO.setTotalVisits(weFormSurveyStatistics.getTotalVisits());
            weFormSurveyStatisticsVO.setTotalUser(weFormSurveyStatistics.getTotalUser());
            weFormSurveyStatisticsVO.setCollectionRate(weFormSurveyStatistics.getCollectionRate());
            weFormSurveyStatisticsVO.setCollectionVolume(weFormSurveyStatistics.getCollectionVolume());
            weFormSurveyStatisticsVO.setAverageTime(weFormSurveyStatistics.getAverageTime());
            result.add(weFormSurveyStatisticsVO);
        }

        count = tSurveyList.size();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", result);
        resultMap.put("total", count);
        if (tSurveyList.size() == 0) {
            return AjaxResult.success("该时间段内数据为空！", resultMap);
        }
        return AjaxResult.success(resultMap);
    }


    /**
     * 省级联动
     */
    @Log(title = "省级联动")
    @PostMapping("/areaStatistic")
    @ApiOperation("省级联动")
    public AjaxResult areaStatistic(WeFormSurveyRadioQuery query) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        query.setLabel("area");
        List<WeFormSurveyRadio> selectNumber = weFormSurveyRadioService.selectNumber(query);
        for (WeFormSurveyRadio number : selectNumber) {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            String questionNumber = number.getQuestionNumber();
            String label = number.getLabel();
            List<SysAreaVo> areaDataList = qwSysAreaClient.getChildListById(query.getParentCode()).getData();
            for (SysAreaVo areaData : areaDataList) {
                Map<String, Object> map = new HashMap<>();
                String name = areaData.getName();
                query.setCode(areaData.getId());
                query.setQuestionNumber(questionNumber);
                int value = weFormSurveyRadioService.selectCountArea(query);
                map.put("name", name);
                map.put("value", value);
                list.add(map);
            }
            result.put("questionNumber", questionNumber);
            result.put("label", label);
            result.put("data", list);
            resultList.add(result);
        }
        return AjaxResult.success(resultList);
    }

    /**
     * 站点统计
     *
     * @return
     */
    @PostMapping("/siteStas")
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
        String pvKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_PV, belongId, dataSource);
        redisTemplate.opsForValue().increment(pvKey);
        //IP
        String ipKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_IP, belongId, dataSource);
        log.info("请求的IP地址：{}", weFormSiteStasQuery.getIpAddr());
        redisTemplate.opsForSet().add(ipKey, weFormSiteStasQuery.getIpAddr());


        return AjaxResult.success();
    }


    /**
     * 统计数据导出
     */
    @ApiOperation("导出用户统计")
    @Log(title = "导出用户统计")
    @PostMapping("/user/export")
    public AjaxResult userExport(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        Date startTime = null;
        Date endTime = null;
        String type = query.getType();
        if (!type.equals("customization")) {
            if (type.equals("week")) {
                startTime = DateUtil.offsetWeek(new Date(), -1);
            } else if (type.equals("month")) {
                startTime = DateUtil.offsetMonth(new Date(), -1);
            }
            endTime = new Date();
        } else {
            startTime = query.getStartDate();
            endTime = query.getEndDate();
        }
        query.setStartDate(startTime);
        query.setEndDate(endTime);
        QueryWrapper<WeFormSurveyAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getBelongId, query.getBelongId());
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getDataSource, query.getDataSource());
        queryWrapper.apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) >= '" + DateUtil.formatDate(query.getStartDate()) + "'");
        queryWrapper.apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) <= '" + DateUtil.formatDate(query.getEndDate()) + "'");
        List<WeFormSurveyAnswer> total = weFormSurveyAnswerService.list(queryWrapper);

        List<WeFormSurveyAnswerVO> list = new ArrayList<>();
        for (WeFormSurveyAnswer weFormSurveyAnswer : total) {
            WeFormSurveyAnswerVO weFormSurveyAnswerVO = new WeFormSurveyAnswerVO();
            weFormSurveyAnswerVO.setCreateTime(weFormSurveyAnswer.getCreateTime());
            weFormSurveyAnswerVO.setName(weFormSurveyAnswer.getName());
            weFormSurveyAnswerVO.setDataSource(weFormSurveyAnswer.getDataSource());
            weFormSurveyAnswerVO.setMobile(weFormSurveyAnswer.getMobile());
            weFormSurveyAnswerVO.setOpenId(weFormSurveyAnswer.getOpenId());
            weFormSurveyAnswerVO.setUnionId(weFormSurveyAnswer.getUnionId());
            weFormSurveyAnswerVO.setIsCorpUser("否");
            list.add(weFormSurveyAnswerVO);
        }
        ExcelUtil<WeFormSurveyAnswerVO> util = new ExcelUtil<>(WeFormSurveyAnswerVO.class);
        return util.exportExcel(list, "user");
    }


    /**
     * 统计数据导出
     */
    @ApiOperation("统计数据导出")
    @Log(title = "统计数据导出")
    @PostMapping("/data/export")
    public AjaxResult dataExport(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        String type = query.getType();
        if (StringUtils.isNotBlank(type) && type.equals("week")) {
            query.setStartDate(DateUtil.offsetWeek(new Date(), -1));
            query.setEndDate(DateUtil.date());
        }
        if (StringUtils.isNotBlank(type) && type.equals("month")) {
            query.setStartDate(DateUtil.offsetMonth(new Date(), -1));
            query.setEndDate(DateUtil.date());
        }
        List<WeFormSurveyStatistics> tSurveyList = weFormSurveyStatisticsService.dataList(query);

        List<WeFormSurveyStatisticsVO> list = new ArrayList<>();
        if (tSurveyList != null && tSurveyList.size() > 0) {
            for (WeFormSurveyStatistics weFormSurveyStatistics : tSurveyList) {
                WeFormSurveyStatisticsVO weFormSurveyStatisticsVO = new WeFormSurveyStatisticsVO();
                weFormSurveyStatisticsVO.setCreateTime(DateUtil.offsetDay(weFormSurveyStatistics.getCreateTime(), -1));
                weFormSurveyStatisticsVO.setTotalVisits(weFormSurveyStatistics.getTotalVisits());
                weFormSurveyStatisticsVO.setTotalUser(weFormSurveyStatistics.getTotalUser());
                weFormSurveyStatisticsVO.setCollectionRate(weFormSurveyStatistics.getCollectionRate());
                weFormSurveyStatisticsVO.setCollectionVolume(weFormSurveyStatistics.getCollectionVolume());
                weFormSurveyStatisticsVO.setAverageTime(weFormSurveyStatistics.getAverageTime());
                list.add(weFormSurveyStatisticsVO);
            }
        }
        ExcelUtil<WeFormSurveyStatisticsVO> util = new ExcelUtil<>(WeFormSurveyStatisticsVO.class);
        return util.exportExcel(list, "data");
    }

    /**
     * 问卷答案数据导出
     *
     * @param query
     * @throws IOException
     */
    @ApiOperation("问卷答案数据导出")
    @Log(title = "问卷答案数据导出")
    @GetMapping("/answer/export")
    public void answerExport(WeFormSurveyStatisticQuery query) throws IOException {
        //时间类型处理
        String type = query.getType();
        if (StringUtils.isNotBlank(type) && type.equals("week")) {
            query.setStartDate(DateUtil.offsetWeek(new Date(), -1));
            query.setEndDate(DateUtil.date());
        }
        if (StringUtils.isNotBlank(type) && type.equals("month")) {
            query.setStartDate(DateUtil.offsetMonth(new Date(), -1));
            query.setEndDate(DateUtil.date());
        }

        //表单信息
        WeFormSurveyCatalogue weFormSurveyCatalogue = weFormSurveyCatalogueService.getWeFormSurveyCatalogueById(query.getBelongId());

        //导出数据的头部
        List<List<String>> head = new ArrayList<>();
        if (ObjectUtil.isNotNull(weFormSurveyCatalogue)) {
            //导出数据的头部
            List<String> head0 = new ArrayList<>();
            head0.add("日期");
            head.add(head0);

            String styles = (String) weFormSurveyCatalogue.getStyles();
            JSONArray jsonArray = JSON.parseArray(styles).getJSONArray(0);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                List<String> header = new ArrayList<>();
                header.add(jsonObject.getString("label"));
                head.add(header);
            }
        }

        //查询列表
        QueryWrapper<WeFormSurveyAnswer> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getBelongId, query.getBelongId());
        queryWrapper.lambda().eq(WeFormSurveyAnswer::getDataSource, query.getDataSource());
        queryWrapper.apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) >= '" + DateUtil.formatDate(query.getStartDate()) + "'");
        queryWrapper.apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) <= '" + DateUtil.formatDate(query.getEndDate()) + "'");
        List<WeFormSurveyAnswer> list = weFormSurveyAnswerService.list(queryWrapper);

        //导出的数据
        List<List<Object>> exportList = new ArrayList<>();
        //填充数据
        if (list != null && list.size() > 0) {
            for (WeFormSurveyAnswer weFormSurveyAnswer : list) {
                List<Object> item = new ArrayList<>();
                item.add(DateUtil.format(weFormSurveyAnswer.getCreateTime(), "yyyy-MM-dd"));

                //表单数据
                String answer = weFormSurveyAnswer.getAnswer();
                JSONArray jsonArray = JSON.parseArray(answer);
                //根据问题编号，将表单分组
                Map<String, List<Object>> answerList = jsonArray.stream().collect(Collectors.groupingBy(o -> JSON.parseObject(o.toString()).getString("questionNumber")));
                //遍历问题
                answerList.forEach((k, v) -> {
                    if (v.size() > 1) {
                        //多选框的处理
                        Object o = v.get(0);
                        JSONObject jsonObject = JSON.parseObject(o.toString());
                        String options = jsonObject.getString("options");
                        String[] split = options.split(",");
                        StringBuffer defaultValue = new StringBuffer();
                        for (int i = 0; i < v.size(); i++) {
                            JSONObject jsonObject1 = JSON.parseObject(v.get(i).toString());
                            if (i == 0) {
                                defaultValue.append(split[jsonObject1.getInteger("defaultValue")]);
                            } else {
                                defaultValue.append("," + split[jsonObject1.getInteger("defaultValue")]);
                            }
                        }
                        item.add(defaultValue.toString());
                    } else {
                        Object o = v.get(0);
                        JSONObject jsonObject = JSON.parseObject(o.toString());

                        //省市联动
                        String cascader = "el-cascader";
                        //日期
                        String date = "el-date-picker";

                        String tag = jsonObject.getString("tag");
                        if (tag.equals(cascader)) {
                            String defaultValue = jsonObject.getString("defaultValue");
                            if (defaultValue.contains("[") || defaultValue.contains("]")) {
                                //级联选择
                                item.add(defaultValue);
                            } else {
                                //省市联动处理
                                String[] split = defaultValue.split(",");
                                StringBuffer value = new StringBuffer();
                                for (int i = 0; i < split.length; i++) {
                                    AjaxResult<SysAreaVo> area = qwSysAreaClient.getAreaById(Integer.valueOf(split[i]));
                                    if (area != null && area.getCode() == 200 && area.getData() != null) {
                                        SysAreaVo data = area.getData();
                                        if (i == 0) {
                                            value.append(data.getName());
                                        } else {
                                            value.append("-" + data.getName());
                                        }
                                    }
                                }
                                item.add(value.toString());
                            }
                        } else if (tag.equals(date)) {
                            //日期处理
                            String defaultValue = jsonObject.getString("defaultValue");
                            DateTime parse = DateUtil.parse(defaultValue);
                            String format = DateUtil.format(parse, "yyyy-MM-dd");
                            item.add(format);
                        } else {
                            item.add(jsonObject.getString("defaultValue"));
                        }
                    }
                });
                exportList.add(item);
            }
        }
        HttpServletResponse response = ServletUtils.getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("智能表单答案数据报表", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream()).head(head).sheet("智能表单答案").doWrite(exportList);
    }


    /**
     * 表单站点统计初始化
     *
     * @author WangYX
     * @date 2022/10/14 10:59
     */
    @PostConstruct
    public void siteStasInit() {
        log.info("表单站点统计初始化=>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //所有的表单
        List<WeFormSurveyCatalogue> weFormSurveyCatalogues = weFormSurveyCatalogueService.getListIgnoreTenantId();
        //上次的站点统计的数据
        List<WeFormSurveySiteStas> list = weFormSurveySiteStasService.list();
        Map<Long, Integer> collect = list.stream().collect(Collectors.toMap(WeFormSurveySiteStas::getBelongId, WeFormSurveySiteStas::getTotalVisits));

        if (weFormSurveyCatalogues != null && weFormSurveyCatalogues.size() > 0) {
            for (WeFormSurveyCatalogue weFormSurveyCatalogue : weFormSurveyCatalogues) {
                String channelsName = weFormSurveyCatalogue.getChannelsName();
                if (StringUtils.isNotBlank(channelsName)) {
                    String[] split = channelsName.split(",");
                    for (String channelName : split) {
                        //PV
                        String pvKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_PV, weFormSurveyCatalogue.getId(), channelName);
                        if (!redisTemplate.hasKey(pvKey)) {
                            redisTemplate.opsForValue().set(pvKey, collect.get(weFormSurveyCatalogue.getId()) != null ? collect.get(weFormSurveyCatalogue.getId()) : 0);
                        }
                        //IP
                        String ipKey = StringUtils.format(SiteStasConstants.PREFIX_KEY_IP, weFormSurveyCatalogue.getId(), channelName);
                        if (!redisTemplate.hasKey(ipKey)) {
                            redisTemplate.opsForSet().add(ipKey, "");
                        }
                    }
                }
            }
        }
    }
}
