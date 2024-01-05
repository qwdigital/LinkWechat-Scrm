package com.linkwechat.controller;




import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.vo.SysAreaVo;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.ServletUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.poi.LwExcelUtil;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.form.query.WeFormSurveyRadioQuery;
import com.linkwechat.domain.form.query.WeFormSurveyStatisticQuery;
import com.linkwechat.domain.form.vo.WeFormSurveyAnswerVO;
import com.linkwechat.fegin.QwSysAreaClient;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
@Slf4j
public class WeFormSurveyStatisticsController extends BaseController {

    @Autowired
    private IWeFormSurveyStatisticsService weFormSurveyStatisticsService;

    @Autowired
    private IWeFormSurveyRadioService weFormSurveyRadioService;

    @Autowired
    private IWeFormSurveyAnswerService weFormSurveyAnswerService;

    @Autowired
    private IWeFormSurveyCountService iWeFormSurveyCountService;

    @Autowired
    private IWeFormSurveyCatalogueService weFormSurveyCatalogueService;



    @Resource
    private QwSysAreaClient qwSysAreaClient;




    /**
     * 查询基本表单统计信息
     */
    @GetMapping("/getStatistics")
    public AjaxResult getStatistics(WeFormSurveyStatistics query) {
        List<WeFormSurveyStatistics> Statistics = weFormSurveyStatisticsService.getStatistics(query);
        return AjaxResult.success(Statistics);
    }


    /**
     * 折线图
     * @param query
     * @return
     */
    @GetMapping("/lineChart")
    public AjaxResult lineChart(WeFormSurveyStatisticQuery query) {
        WeFormSurveyCount weFormSurveyCount = WeFormSurveyCount.builder()
                .channelsName(query.getDataSource())
                .belongId(query.getBelongId())
                .build();

        weFormSurveyCount.setBeginTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,query.getStartDate()));
        weFormSurveyCount.setEndTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,query.getEndDate()));
        List<WeFormSurveyStatistics> weFormSurveyStatistics = iWeFormSurveyCountService.lineChart(weFormSurveyCount);

        return AjaxResult.success(weFormSurveyStatistics);
    }





    /**
     * 用户统计列表
     * @param query
     * @return
     */
    @PostMapping("/customer")
    public TableDataInfo<List<WeFormSurveyAnswer>> customer(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
        startPage();
        List<WeFormSurveyAnswer> customerList = weFormSurveyAnswerService.selectCustomerList(query);
        return getDataTable(customerList);
    }


    /**
     * 数据概览列表
     * @param query
     * @return
     */
    @GetMapping("/dataList")
    public TableDataInfo dataList(WeFormSurveyStatisticQuery query) {

        startPage();
        List<WeFormSurveyStatistics> weFormSurveyStatistics = weFormSurveyStatisticsService.dataList(query);

        return getDataTable(
                weFormSurveyStatistics
        );
    }


    /**
     * 数据概览列表
     * @param query
     * @return
     */
    @GetMapping("/dataListExport")
    public void dataListExport(WeFormSurveyStatisticQuery query) {
        List<WeFormSurveyStatistics> weFormSurveyStatistics = weFormSurveyStatisticsService.dataList(query);
        LwExcelUtil.exprotForWeb(
                ServletUtils.getResponse(), WeFormSurveyStatistics.class,weFormSurveyStatistics,"智能表单数据报表_" + System.currentTimeMillis()
        );
    }


    /**
     * 省级联动
     */
    @PostMapping("/areaStatistic")
    public AjaxResult areaStatistic(@RequestBody WeFormSurveyRadioQuery query) {
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
     * 统计数据导出
     */
    @PostMapping("/user/export")
    public void userExport(@RequestBody @Validated WeFormSurveyStatisticQuery query) {
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
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户统计", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), WeFormSurveyAnswerVO.class).sheet("用户信息").doWrite(list);
        } catch (IOException e) {
            log.error("用户统计列表导出异常：query:{}", JSONObject.toJSONString(query), e);
        }
    }


    /**
     * 问卷答案数据导出
     *
     * @param query
     * @throws IOException
     */
    @GetMapping("/answer/export")
    public void answerExport(WeFormSurveyStatisticQuery query) throws  IOException {
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
                if (StringUtils.isNotBlank(jsonObject.getString("label"))) {
                    header.add(jsonObject.getString("label"));
                    head.add(header);
                }
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
                List<JSONObject> jsonArray = JSON.parseArray(answer, JSONObject.class);
                //根据问题编号，将表单分组
                Map<Integer, List<JSONObject>> answerList = jsonArray.stream().collect(Collectors.groupingBy(i -> i.getInteger("questionNumber")));
                //遍历问题
                answerList.forEach((k, v) -> {
                    if (v.size() > 1) {
                        //多选框的处理
                        JSONObject jsonObject = v.get(0);
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
                        JSONObject jsonObject = v.get(0);

                        Integer formCodeId = jsonObject.getInteger("formCodeId");

                        if (ObjectUtil.equal(6, formCodeId) ||  ObjectUtil.equal(8, formCodeId)) {
                            String options = jsonObject.getString("options");
                            String[] split = options.split(",");
                            item.add(split[jsonObject.getInteger("defaultValue")]);
                        } else if (ObjectUtil.equal(9, formCodeId)) {
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
                        } else if (ObjectUtil.equal(10, formCodeId)) {
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



}
