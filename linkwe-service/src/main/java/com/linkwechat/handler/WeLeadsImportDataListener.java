package com.linkwechat.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.LeadsCenterConstants;
import com.linkwechat.common.enums.SexEnums;
import com.linkwechat.common.enums.leads.leads.LeadsStatusEnum;
import com.linkwechat.common.enums.leads.record.ImportSourceTypeEnum;
import com.linkwechat.common.enums.leads.template.DataAttrEnum;
import com.linkwechat.common.enums.leads.template.DatetimeTypeEnum;
import com.linkwechat.domain.leads.leads.entity.WeLeads;
import com.linkwechat.domain.leads.leads.entity.WeLeadsImportRecord;
import com.linkwechat.domain.leads.leads.vo.Properties;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateSettingsVO;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateTableEntryContentVO;
import com.linkwechat.service.IWeLeadsImportRecordService;
import com.linkwechat.service.IWeLeadsService;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 线索导入监听器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/14 14:05
 */
@Slf4j
public class WeLeadsImportDataListener extends AnalysisEventListener<Map<Integer, String>> {

    /**
     * excel表头
     */
    private Map<Integer, String> headTitleMap = new HashMap<>();
    /**
     * 批量处理数量
     */
    private int batchSize = 100;
    /**
     * excel表中数据的总数量
     */
    public int analysisIndex = 0;
    /**
     * excel表中导入成功的数量
     */
    public int successNum = 0;
    /**
     * excel表中导入失败的数量
     */
    public int failNum = 0;
    /**
     * 每次写入数据库的集合
     */
    private final List<Map<String, String>> resultList = new ArrayList<>();
    /**
     * 模板数据
     */
    private final Map<String, WeLeadsTemplateSettingsVO> settingsMap;

    /**
     * 导入文件名
     */
    private final String originalFilename;

    /**
     * 公海Id
     */
    private final Long seaId;
    /**
     * 线索服务类
     */
    private IWeLeadsService weLeadsService;
    /**
     * 导入记录服务类
     */
    private IWeLeadsImportRecordService weLeadsImportRecordService;
    /**
     * 导入记录Id
     */
    private final Long importRecordId = IdUtil.getSnowflakeNextId();
    /**
     * 数字正则
     */
    private final Pattern numberPattern = Pattern.compile("^[0-9]*$");


    public WeLeadsImportDataListener(Long seaId,
                                     String originalFilename,
                                     IWeLeadsService weLeadsService,
                                     Map<String, WeLeadsTemplateSettingsVO> settingsMap,
                                     IWeLeadsImportRecordService weLeadsImportRecordService) {
        this.seaId = seaId;
        this.settingsMap = settingsMap;
        this.weLeadsService = weLeadsService;
        this.originalFilename = originalFilename;
        this.weLeadsImportRecordService = weLeadsImportRecordService;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        //获取第二行的头数据
        if (context.readRowHolder().getRowIndex() == 1) {
            headMap.entrySet().forEach(i -> headTitleMap.put(i.getKey(), i.getValue().replace("*", "")));
        }
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        Map<String, String> dataMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : data.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            String entryName = headTitleMap.get(key);
            WeLeadsTemplateSettingsVO templateSettings = settingsMap.get(entryName);
            if (Objects.nonNull(templateSettings)) {
                dataMap.put(entryName, value);
            }
        }
        resultList.add(dataMap);
        analysisIndex++;
        //每1000条执行落库一次，执行完后清理
        if (analysisIndex % batchSize == 0) {
            flushToDBAndClearListMap();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //解析完后如果列表还有剩余，直接一次性写入到数据库里
        if (resultList.size() > 0) {
            flushToDBAndClearListMap();
        }
        //添加导入记录表
        WeLeadsImportRecord build = WeLeadsImportRecord.builder()
                .id(importRecordId)
                .seaId(seaId)
                .importSourceFileName(originalFilename)
                .importSourceType(ImportSourceTypeEnum.EXCEL.getCode())
                .totalNum(analysisIndex)
                .successNum(successNum)
                .failNum(failNum)
                .delFlag(Constants.COMMON_STATE)
                .build();
        weLeadsImportRecordService.save(build);
        log.info("excel解析完成，一共解析{}行", analysisIndex);
    }

    /**
     * 写入数据到数据库
     *
     * @author WangYX
     * @date 2023/07/14 16:48
     */
    public void flushToDBAndClearListMap() {
        handExcelDataList(seaId, resultList, settingsMap);
        //执行完后清理数据，避免内存不足
        resultList.clear();
    }

    /**
     * 处理导入的数据
     *
     * @param seaId       公海Id
     * @param resultList  excel导入的数据
     * @param settingsMap 模板数据
     */
    private void handExcelDataList(Long seaId,
                                   List<Map<String, String>> resultList,
                                   Map<String, WeLeadsTemplateSettingsVO> settingsMap) {

        List<WeLeads> list = new ArrayList<>();
        for (Map<String, String> rowMap : resultList) {
            if (!isSizeUp(rowMap, settingsMap)) {
                failNum++;
                continue;
            }
            WeLeads weLeads = new WeLeads();
            weLeads.setId(IdUtil.getSnowflake().nextId());
            weLeads.setLeadsStatus(LeadsStatusEnum.WAIT_FOR_DISTRIBUTION.getCode());
            weLeads.setDelFlag(Constants.COMMON_STATE);
            weLeads.setSeaId(seaId);
            weLeads.setImportRecordId(importRecordId);
            weLeads.setSource(ImportSourceTypeEnum.EXCEL.getCode());
            weLeads.setSex(0);

            Set<Map.Entry<String, String>> entries = rowMap.entrySet();
            List<Properties> propertiesList = new ArrayList<>();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                WeLeadsTemplateSettingsVO setting = settingsMap.get(key);
                if (key.equals(LeadsCenterConstants.NAME)) {
                    weLeads.setName(value);
                } else if (key.equals(LeadsCenterConstants.PHONE)) {
                    weLeads.setPhone(value);
                } else if (key.equals(LeadsCenterConstants.SEX)) {
                    if (value.equals(SexEnums.SEX_WZ.getInfo())) {
                        weLeads.setSex(0);
                    } else if (value.equals(SexEnums.SEX_NAN.getInfo())) {
                        weLeads.setSex(1);
                    } else {
                        weLeads.setSex(2);
                    }
                } else {
                    propertiesList.add(Properties.builder().id(setting.getId())
                            .name(setting.getTableEntryName())
                            .key(setting.getTableEntryName())
                            .keyEn(setting.getTableEntryId())
                            .value(value)
                            .build());
                }
            }
            weLeads.setProperties(JSONObject.toJSONString(propertiesList));
            list.add(weLeads);
            successNum++;
        }
        weLeadsService.saveBatch(list);
    }

    /**
     * 判断一条数据中所有的表项是否符合要求
     *
     * @param rowMap      一条行数据
     * @param settingsMap 模板数据
     * @return {@link boolean}
     * @author WangYX
     * @date 2023/07/14 18:25
     */
    private boolean isSizeUp(Map<String, String> rowMap, Map<String, WeLeadsTemplateSettingsVO> settingsMap) {
        boolean flag = true;
        Set<Map.Entry<String, String>> entries = rowMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            WeLeadsTemplateSettingsVO templateSettingsVO = settingsMap.get(entry.getKey());
            if (!isSizeUp(entry.getValue(), templateSettingsVO)) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    /**
     * 判断表项是否符合要求
     *
     * @param value   值
     * @param setting 模板数据
     * @return {@link boolean}
     * @author WangYX
     * @date 2023/07/14 15:59
     */
    private boolean isSizeUp(String value, WeLeadsTemplateSettingsVO setting) {
        if (StrUtil.isBlank(value)) {
            //是否必填 0 非必填 1 必填
            return setting.getRequired().equals(0);
        }
        Integer dataAttr = setting.getDataAttr();
        Integer tableEntryAttr = setting.getTableEntryAttr();
        if (tableEntryAttr.equals(0)) {
            //填写项
            if (dataAttr.equals(DataAttrEnum.TEXT.getCode())) {
                //文本 不能超过最大长度
                return value.length() <= setting.getMaxInputLen();
            } else if (dataAttr.equals(DataAttrEnum.NUMBER.getCode())) {
                //数字
                Matcher matcher = numberPattern.matcher(value);
                return matcher.matches();
            } else if (dataAttr.equals(DataAttrEnum.DATE.getCode())) {
                //日期
                try {
                    Integer datetimeType = setting.getDatetimeType();
                    DatetimeTypeEnum datetimeTypeEnum = DatetimeTypeEnum.of(datetimeType);
                    String format = datetimeTypeEnum.getFormat();
                    Date parse = new SimpleDateFormat(format).parse(value);
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            //下拉项
            List<WeLeadsTemplateTableEntryContentVO> tableEntryContent = setting.getTableEntryContent();
            Optional<WeLeadsTemplateTableEntryContentVO> first = tableEntryContent.stream().filter(i -> i.getContent().equals(value)).findFirst();
            return first.isPresent();
        }
        return true;
    }
}
