package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.constant.LeadsCenterConstants;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.leads.template.CanEditEnum;
import com.linkwechat.common.enums.leads.template.DataAttrEnum;
import com.linkwechat.common.enums.leads.template.DatetimeTypeEnum;
import com.linkwechat.common.enums.leads.template.TableEntryAttrEnum;
import com.linkwechat.common.exception.ServiceException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.EasyExcelUtils;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateSettings;
import com.linkwechat.domain.leads.template.entity.WeLeadsTemplateTableEntryContent;
import com.linkwechat.domain.leads.template.query.WeLeadsTemplateSettingsRequest;
import com.linkwechat.domain.leads.template.query.WeLeadsTemplateTableEntryContentRequest;
import com.linkwechat.domain.leads.template.query.WeTemplateSettingsReRankRequest;
import com.linkwechat.domain.leads.template.vo.WeLeadsTemplateSettingsVO;
import com.linkwechat.mapper.WeLeadsTemplateSettingsMapper;
import com.linkwechat.service.IWeLeadsTemplateSettingsService;
import com.linkwechat.service.IWeLeadsTemplateTableEntryContentService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公海模版配置表(WeLeadsTemplateSettings)表服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 11:18
 */
@Slf4j
@Service
public class WeLeadsTemplateSettingsServiceImpl extends ServiceImpl<WeLeadsTemplateSettingsMapper, WeLeadsTemplateSettings> implements IWeLeadsTemplateSettingsService {

    @Resource
    private MapperFactory mapperFactory;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private IWeLeadsTemplateTableEntryContentService weTableEntryContentService;

    @Override
    public List<WeLeadsTemplateSettingsVO> queryAll() {
        List<WeLeadsTemplateSettingsVO> list = baseMapper.queryWithTableEntryContent();
        list.sort(Comparator.comparing(WeLeadsTemplateSettingsVO::getRank));
        return list;
    }

    @Override
    public boolean saveLeadsTemplateSettings(WeLeadsTemplateSettingsRequest param) {
        checkSaveParam(param);
        WeLeadsTemplateSettings settings = mapperFactory.getMapperFacade().map(param, WeLeadsTemplateSettings.class);
        settings.setId(IdUtil.getSnowflake().nextId());
        boolean b = save(settings);
        Long settingsId = settings.getId();
        if (CollectionUtil.isNotEmpty(param.getTableEntryContent())) {
            List<WeLeadsTemplateTableEntryContent> weTableEntryContentList = mapperFactory.getMapperFacade().mapAsList(param.getTableEntryContent(), WeLeadsTemplateTableEntryContent.class);
            for (WeLeadsTemplateTableEntryContent weTableEntryContent : weTableEntryContentList) {
                weTableEntryContent.setId(IdUtil.getSnowflake().nextId());
                weTableEntryContent.setLeadsTemplateSettingsId(settingsId);
                weTableEntryContent.setCreateTime(new Date());
            }
            weTableEntryContentService.saveBatch(weTableEntryContentList);
        }
        return b;
    }

    @Override
    public boolean updateLeadsTemplateSettings(WeLeadsTemplateSettingsRequest param) {
        //更新模版表仅仅只能更新表项名称
        checkUpdateParam(param);
        WeLeadsTemplateSettings settings = new WeLeadsTemplateSettings();
        settings.setId(param.getId());
        settings.setTableEntryName(param.getTableEntryName());
        settings.setUpdateTime(new Date());
        return updateById(settings);
    }

    @Override
    public boolean deleteLeadsTemplateSettings(Long id) {
        //只能删除可被编辑的模版项
        return remove(Wrappers.<WeLeadsTemplateSettings>lambdaUpdate()
                .eq(WeLeadsTemplateSettings::getCanEdit, CanEditEnum.ALLOW.getCode())
                .eq(WeLeadsTemplateSettings::getId, id)
        );

    }

    @Override
    public boolean reRank(List<WeTemplateSettingsReRankRequest> paramList) {
        if (CollectionUtil.isEmpty(paramList)) {
            throw new ServiceException("重排序列表不能为空", HttpStatus.BAD_REQUEST);
        }
        List<WeLeadsTemplateSettings> settings = mapperFactory.getMapperFacade().mapAsList(paramList, WeLeadsTemplateSettings.class);
        return updateBatchById(settings);
    }

    @Override
    public void outPutTemplateExcel(HttpServletResponse response) throws IOException {
        List<WeLeadsTemplateSettingsVO> settingsList = baseMapper.queryWithTableEntryContent();
        settingsList.sort(Comparator.comparing(WeLeadsTemplateSettingsVO::getRank));
        //CustomSheetWriteHandler customSheetWriteHandler = new CustomSheetWriteHandler();
        //Map<Integer, List<String>> selectOptionMap = customSheetWriteHandler.getSelectOptionMap();
        for (int i = 0; i < settingsList.size(); i++) {
            WeLeadsTemplateSettingsVO leadsTemplateSettings = settingsList.get(i);
            Integer tableEntryAttr = leadsTemplateSettings.getTableEntryAttr();
            /*if (tableEntryAttr.intValue() == TableEntryAttrEnum.COMBOBOX.getCode()) {
                List<WeLeadsTemplateTableEntryContent> tableEntryContent = leadsTemplateSettings.getTableEntryContent();
                if (CollectionUtil.isNotEmpty(tableEntryContent)) {
                    List<String> selectOptionList = tableEntryContent.stream().map(WeLeadsTemplateTableEntryContent::getContent).collect(Collectors.toList());
                    // todo 简单的下拉框值超过20个就会报错，需要开一个隐藏的sheet引用值，后续再优化，暂时先用简单的下拉框
                    if (selectOptionList.size() < 20) {
                        selectOptionMap.put(i, selectOptionList);
                    }
                }
            }*/
        }

        List<List<Object>> dataList = new ArrayList<>();
        //这里只加一行数据
        List<Object> oneRowList = new ArrayList<>();
        //Random rand = new Random();
        for (int i = 0; i < settingsList.size(); i++) {
            WeLeadsTemplateSettingsVO leadsTemplateSettings = settingsList.get(i);
            Integer tableEntryAttr = leadsTemplateSettings.getTableEntryAttr();
            if (tableEntryAttr.intValue() == TableEntryAttrEnum.COMBOBOX.getCode()) {
                /*List<String> optionList = selectOptionMap.get(i);
                if (CollectionUtil.isEmpty(optionList)) {
                    oneRowList.add(null);
                } else {
                    String randomElement = optionList.get(rand.nextInt(optionList.size()));
                    oneRowList.add(randomElement);
                }*/
                oneRowList.add(null);
            } else if (tableEntryAttr.intValue() == TableEntryAttrEnum.INPUT.getCode()) {
                Integer dataAttr = leadsTemplateSettings.getDataAttr();
                DataAttrEnum dataAttrEnum = DataAttrEnum.of(dataAttr);
                if (DataAttrEnum.DATE == dataAttrEnum) {
                    Integer datetimeType = leadsTemplateSettings.getDatetimeType();
                    DatetimeTypeEnum datetimeTypeEnum = DatetimeTypeEnum.of(datetimeType);
                    if (DatetimeTypeEnum.DATETIME == datetimeTypeEnum) {
                        oneRowList.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
                    } else {
                        oneRowList.add(DateUtils.datePath());
                    }
                } else if (DataAttrEnum.NUMBER == dataAttrEnum) {
                    oneRowList.add(0);
                } else {
                    oneRowList.add(leadsTemplateSettings.getTableEntryName() + "的值");
                }
            } else {
                oneRowList.add(null);
            }
        }
        dataList.add(oneRowList);
        String fileName = DateUtils.getTime() + " 线索导入模板.xlsx";
        String encode = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        log.debug("fileName = {}", fileName);
        log.debug("encode = {}", encode);
        /*解决axios无法获取响应头headers的Content-Disposition*/
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + encode);
        response.setContentType(LeadsCenterConstants.XLSX_FILE_HEAD);
        response.setCharacterEncoding("utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        List<String> head = settingsList.stream().map(WeLeadsTemplateSettingsVO::getTableEntryName).collect(Collectors.toList());
        try {
            EasyExcelUtils.simpleWrite(head, dataList, outputStream, false);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(objectMapper.writeValueAsString(AjaxResult.error(e.getMessage())));
        }
    }

    @Override
    public String autoGenerate() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public List<WeLeadsTemplateTableEntryContent> getConsultSelectItem() {
        WeLeadsTemplateSettings weSeaLeadsTemplateSettings = getOne(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                //.eq(WeLeadsTemplateSettings::getTableEntryName, LeadsCenterConstants.CONSULT)
                .eq(WeLeadsTemplateSettings::getTableEntryId, LeadsCenterConstants.CONSULT_EN)
                .eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE)
        );
        Long settingsId = weSeaLeadsTemplateSettings.getId();
        return weTableEntryContentService.getByLeadsTemplateSettingsId(settingsId);
    }

    @Override
    public void syncKinship(List<String> values) {
        WeLeadsTemplateSettings weSeaLeadsTemplateSettings = getOne(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                .eq(WeLeadsTemplateSettings::getTableEntryId, LeadsCenterConstants.KINSHIP_EN)
                .eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE)
        );
        Long settingsId = weSeaLeadsTemplateSettings.getId();
        weTableEntryContentService.saveBatchTableEntryContent(settingsId, values);
    }

    @Override
    public List<WeLeadsTemplateTableEntryContent> getKinshipSelectItem() {
        WeLeadsTemplateSettings weSeaLeadsTemplateSettings = getOne(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                .eq(WeLeadsTemplateSettings::getTableEntryId, LeadsCenterConstants.KINSHIP_EN)
                .eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE)
        );
        Long settingsId = weSeaLeadsTemplateSettings.getId();
        return weTableEntryContentService.getByLeadsTemplateSettingsId(settingsId);
    }

    @Override
    public WeLeadsTemplateSettings queryByTableEntryId(String tableEntryId) {
        return getOne(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                .eq(WeLeadsTemplateSettings::getTableEntryId, tableEntryId)
                .eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE)
        );
    }

    @Override
    public List<WeLeadsTemplateTableEntryContent> getAgeSelectItem() {
        WeLeadsTemplateSettings weSeaLeadsTemplateSettings = getOne(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                .eq(WeLeadsTemplateSettings::getTableEntryId, LeadsCenterConstants.AGE_EN)
                .eq(WeLeadsTemplateSettings::getDelFlag, Constants.COMMON_STATE)
        );
        Long settingsId = weSeaLeadsTemplateSettings.getId();
        return weTableEntryContentService.getByLeadsTemplateSettingsId(settingsId);
    }

    /**
     * 信泽校验
     *
     * @param param 前端入参
     */
    private void checkSaveParam(WeLeadsTemplateSettingsRequest param) {
        checkBaseParam(param);
        List<WeLeadsTemplateSettings> existList = list(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                .eq(WeLeadsTemplateSettings::getTableEntryName, param.getTableEntryName())
                .or()
                .eq(WeLeadsTemplateSettings::getTableEntryId, param.getTableEntryId()));
        if (CollectionUtil.isNotEmpty(existList)) {
            throw new ServiceException("存在相同的表项名称或表项ID", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 更新校验
     *
     * @param param 前端入参
     */
    private void checkUpdateParam(WeLeadsTemplateSettingsRequest param) {
        if (Objects.isNull(param.getId())) {
            throw new ServiceException("更新需要携带id", HttpStatus.BAD_REQUEST);
        }
        checkBaseParam(param);
        List<WeLeadsTemplateSettings> existList = list(Wrappers.<WeLeadsTemplateSettings>lambdaQuery()
                .eq(WeLeadsTemplateSettings::getTableEntryName, param.getTableEntryName())
                .or()
                .eq(WeLeadsTemplateSettings::getTableEntryId, param.getTableEntryId()));
        //更新模式下需要去掉自身的重复性检测
        existList = existList.stream().filter(f -> !f.getId().equals(param.getId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(existList)) {
            throw new ServiceException("存在相同的表项名称或表项ID", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 基础校验
     *
     * @param param 前端入参
     */
    private void checkBaseParam(WeLeadsTemplateSettingsRequest param) {
        String tableEntryName = param.getTableEntryName();
        if (Objects.isNull(tableEntryName)) {
            throw new ServiceException("表项名称不能为空", HttpStatus.BAD_REQUEST);
        }

        String tableEntryId = param.getTableEntryId();
        if (Objects.isNull(tableEntryId)) {
            throw new ServiceException("表项ID不能为空", HttpStatus.BAD_REQUEST);
        }

        Integer tableEntryAttr = param.getTableEntryAttr();
        TableEntryAttrEnum tableEntryAttrEnum = TableEntryAttrEnum.of(tableEntryAttr);
        if (Objects.isNull(tableEntryAttrEnum)) {
            throw new ServiceException("非法的表项属性", HttpStatus.BAD_REQUEST);
        }
        if (TableEntryAttrEnum.COMBOBOX == tableEntryAttrEnum) {
            List<WeLeadsTemplateTableEntryContentRequest> tableEntryContent = param.getTableEntryContent();
            if (CollectionUtil.isEmpty(tableEntryContent)) {
                throw new ServiceException("选择下拉框时表项内容不能为空", HttpStatus.BAD_REQUEST);
            }
        } else {
            Integer dataAttr = param.getDataAttr();
            DataAttrEnum dataAttrEnum = DataAttrEnum.of(dataAttr);
            if (Objects.isNull(dataAttrEnum)) {
                throw new ServiceException("非法的数据属性", HttpStatus.BAD_REQUEST);
            }
            if (DataAttrEnum.TEXT == dataAttrEnum || DataAttrEnum.NUMBER == dataAttrEnum) {
                if (Objects.isNull(param.getMaxInputLen())) {
                    throw new ServiceException("数据属性为文本和数字时，最大输入长度不能为空", HttpStatus.BAD_REQUEST);
                }
            } else if (DataAttrEnum.DATE == dataAttrEnum) {
                Integer datetimeType = param.getDatetimeType();
                DatetimeTypeEnum datetimeTypeEnum = DatetimeTypeEnum.of(datetimeType);
                if (Objects.isNull(datetimeTypeEnum)) {
                    throw new ServiceException("非法的日期类型", HttpStatus.BAD_REQUEST);
                }
            }
        }
    }
}

