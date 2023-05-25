package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.linkwechat.common.annotation.DataColumn;
import com.linkwechat.common.annotation.DataScope;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.common.EnumConfig;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.vo.WeCustomersVo;
import com.linkwechat.domain.strategic.crowd.query.WeAddStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdListQuery;
import com.linkwechat.domain.strategic.crowd.query.WeStrategicCrowdQuery;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdAnalyzelDataVo;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdAnalyzelVo;
import com.linkwechat.domain.strategic.crowd.vo.WeStrategicCrowdDetailVo;
import com.linkwechat.mapper.WeStrategicCrowdMapper;
import com.linkwechat.service.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 策略人群信息表(WeStrategicCrowd)
 *
 * @author danmo
 * @since 2022-07-05 15:33:28
 */
@Service
public class WeStrategicCrowdServiceImpl extends ServiceImpl<WeStrategicCrowdMapper, WeStrategicCrowd> implements IWeStrategicCrowdService {


    @Autowired
    private EnumConfig.CommonEnumMap commonEnumMap;

    @Autowired
    private IWeStrategicCrowdCustomerRelService weStrategicCrowdCustomerRelService;

    @Autowired
    private IWeTagGroupService weTagGroupService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private WeCategoryServiceImpl weCategoryService;

    @Autowired
    private IWeGroupService iWeGroupService;

    @Transactional
    @Override
    public void add(WeAddStrategicCrowdQuery query) {
        WeStrategicCrowd strategicCrowd = new WeStrategicCrowd();
        strategicCrowd.setGroupId(query.getGroupId());
        strategicCrowd.setName(query.getName());
        strategicCrowd.setType(query.getType());
        strategicCrowd.setSwipe(query.getSwipe2Str());
        strategicCrowd.setStatus(1);
        save(strategicCrowd);
    }

    @Transactional
    @Override
    public void update(WeAddStrategicCrowdQuery query) {
        WeStrategicCrowd strategicCrowd = new WeStrategicCrowd();
        strategicCrowd.setId(query.getId());
        strategicCrowd.setGroupId(query.getGroupId());
        strategicCrowd.setName(query.getName());
        strategicCrowd.setType(query.getType());
        strategicCrowd.setSwipe(query.getSwipe2Str());
        saveOrUpdate(strategicCrowd);
    }

    @Override
    public void renew(List<Long> ids) {
        WeStrategicCrowd strategicCrowd = new WeStrategicCrowd();
        strategicCrowd.setUpdateTime(new Date());
        strategicCrowd.setStatus(2);
        update(strategicCrowd,new LambdaUpdateWrapper<WeStrategicCrowd>().in(WeStrategicCrowd::getId,ids));
        for (Long id : ids) {
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeSyncEx(),rabbitMQSettingConfig.getWeCrowdCalculateRk(), String.valueOf(id));
        }
    }

    @Override
    public WeStrategicCrowdDetailVo getDetail(Long id) {
        WeStrategicCrowd crowd = getById(id);
        WeStrategicCrowdDetailVo crowdDetailVo = new WeStrategicCrowdDetailVo();
        crowdDetailVo.setId(crowdDetailVo.getId());
        crowdDetailVo.setName(crowd.getName());
        crowdDetailVo.setGroupId(crowd.getGroupId());
        crowdDetailVo.setType(crowd.getType());
        crowdDetailVo.setStatus(crowd.getStatus());
        crowdDetailVo.setCrowdNum(crowd.getCrowdNum());
        crowdDetailVo.setStr2Swipe(crowd.getSwipe());
        return crowdDetailVo;
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        update(new LambdaUpdateWrapper<WeStrategicCrowd>()
                .set(WeStrategicCrowd::getDelFlag,1).in(WeStrategicCrowd::getId,ids));
    }

    @Override
    @DataScope(type = "2", value = @DataColumn(alias = "we_strategic_crowd", name = "create_by_id", userid = "user_id"))
    public List<WeStrategicCrowd> getList(WeStrategicCrowdListQuery query) {
//        if(query.getGroupId() == null){
//           throw new WeComException("分组ID不能为空");
//        }
        LambdaQueryWrapper<WeStrategicCrowd> wrapper = new LambdaQueryWrapper<WeStrategicCrowd>();
        wrapper.select(WeStrategicCrowd::getId,WeStrategicCrowd::getGroupId,WeStrategicCrowd::getName,
                WeStrategicCrowd::getTenantId,WeStrategicCrowd::getCrowdNum,WeStrategicCrowd::getStatus,
                WeStrategicCrowd::getType,BaseEntity::getCreateBy,WeStrategicCrowd::getCreateById,BaseEntity::getCreateTime,
                BaseEntity::getUpdateTime);
        wrapper.like(StringUtils.isNotEmpty(query.getName()),WeStrategicCrowd::getName,query.getName());
        wrapper.eq(query.getStatus() != null,WeStrategicCrowd::getStatus,query.getStatus());
        wrapper.ge(StringUtils.isNotEmpty(query.getBeginTime()),BaseEntity::getCreateTime,query.getBeginTime());
        wrapper.le(StringUtils.isNotEmpty(query.getEndTime()),BaseEntity::getCreateTime,query.getEndTime());
        wrapper.eq(WeStrategicCrowd::getDelFlag,0);
        wrapper.eq(query.getGroupId() != null,WeStrategicCrowd::getGroupId,query.getGroupId());
        if(Objects.nonNull(query.getParams()) && Objects.nonNull(query.getParams().get("dataScope"))
                && StringUtils.isNotEmpty(query.getParams().get("dataScope").toString())){
            wrapper.apply(""+query.getParams().get("dataScope").toString()+"");
        }
        return list(wrapper);
    }

    @Override
    public Map<String, Object> getDownOptions(List<String> enumNames) {
        Map<String, Object> resMap = new HashMap<>(16);
        if (CollectionUtil.isNotEmpty(enumNames)) {
            enumNames.forEach(enumName -> {
                resMap.put(enumName, new ArrayList<>());
                if(enumName.equals("WeCorpTagEnum")){
                    WeTagGroup tagGroup = new WeTagGroup();
                    tagGroup.setGroupTagType(1);
                    List<WeTagGroup> list = weTagGroupService.getTagGroupList(tagGroup);
                    if(CollectionUtil.isNotEmpty(list)){
                        List<Map<String, Object>> map = list.stream().map(item -> {
                            Map<String, Object> tagGroupMap = new HashMap<>(32);
                            tagGroupMap.put("code", item.getId());
                            tagGroupMap.put("value", item.getGroupName());
                            if(CollectionUtil.isNotEmpty(item.getWeTags())){
                                List<Map<String, Object>> tagCodeList = item.getWeTags().stream().map(tagItem -> {
                                    Map<String, Object> tagMap = new HashMap<>(32);
                                    tagMap.put("code", tagItem.getTagId());
                                    tagMap.put("value", tagItem.getName());
                                    return tagMap;
                                }).collect(Collectors.toList());
                                tagGroupMap.put("child",tagCodeList);
                            }
                            return tagGroupMap;
                        }).collect(Collectors.toList());
                        resMap.put(enumName, map);
                    }
                }else if(enumName.equals("StrategicCrowdEnum")){
                    List<WeStrategicCrowd> list = list(new LambdaQueryWrapper<WeStrategicCrowd>().eq(WeStrategicCrowd::getDelFlag, 0));
                    if(CollectionUtil.isNotEmpty(list)){
                        List<Object> map = list.parallelStream().map(item -> {
                            Map<String, Object> crowdMap = new HashMap<>(32);
                            crowdMap.put("code", item.getId());
                            crowdMap.put("value", item.getName());
                            return crowdMap;
                        }).collect(Collectors.toList());
                        resMap.put(enumName, map);
                    }
                }else if(enumName.equals("GroupChatEnum")){

                    List<WeGroup> weGroups = iWeGroupService.list();
                    if(CollectionUtil.isNotEmpty(weGroups)){
                        List<Object> map = weGroups.parallelStream().map(item -> {
                            Map<String, Object> crowdMap = new HashMap<>(32);
                            crowdMap.put("code", item.getChatId());
                            crowdMap.put("value", item.getGroupName());
                            return crowdMap;
                        }).collect(Collectors.toList());
                        resMap.put(enumName, map);
                    }




                }else {
                    Object map = Optional.ofNullable(commonEnumMap.getValue().get(enumName)).orElseGet(ArrayList::new);
                    resMap.put(enumName, map);
                }
            });
        }else {
            throw new WeComException("参数不能为空");
        }
        return resMap;
    }

    @Override
    public List<WeStrategicCrowd> getListByIds(List<Long> ids) {

        return  this.listByIds(ids);
    }

    @Override
    public void updateStatusByIds(Integer status, List<Long> idList) {
        WeStrategicCrowd strategicCrowd = new WeStrategicCrowd();
        strategicCrowd.setStatus(status);
        this.update(strategicCrowd,new LambdaQueryWrapper<WeStrategicCrowd>().in(WeStrategicCrowd::getId,idList));
    }

    @Override
    public WeStrategicCrowdAnalyzelVo getAnalyze(WeStrategicCrowdQuery query) {
        WeStrategicCrowdAnalyzelVo weStrategicCrowdAnalyzelVo = new WeStrategicCrowdAnalyzelVo();
        if(query.getId() == null){
            throw new WeComException("ID不能为空");
        }
        if(query.getBeginTime() == null){
            query.setBeginTime(DateUtil.offsetDay(new Date(),-7));
        }

        if(query.getEndTime() == null){
            query.setEndTime(DateUtil.date());
        }

        Long addNum = 0L;
        Long reduceNum = 0L;
//        String beginTime = DateUtils.initSqlBeginTime(DateUtil.yesterday().toDateStr());
//        String endTime = DateUtils.initSqlEndTime(DateUtil.today());
        String beginTime =  DateUtils.dateTime(DateUtil.yesterday());
        String endTime = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        List<WeStrategicCrowdCustomerRel> crowdCustomerRelList = weStrategicCrowdCustomerRelService.getListByCrowdIdAndTime(query.getId(), beginTime, endTime);
        if(CollectionUtil.isNotEmpty(crowdCustomerRelList)){
            List<WeStrategicCrowdCustomerRel> list = crowdCustomerRelList.stream().filter(bean -> Objects.equals(0, bean.getDelFlag())).collect(Collectors.toList());
            List<WeStrategicCrowdCustomerRel> listNew = crowdCustomerRelList.stream().filter(bean -> bean.getDelFlag() != 0).collect(Collectors.toList());


            //获取今日人群数据
            Set<Long> today = list.stream()
                    .filter(bean -> DateUtil.today().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, bean.getCreateTime())))
                    .map(WeStrategicCrowdCustomerRel::getCustomerId)
                    .collect(Collectors.toSet());
            //获取今日之前的数据
            Set<Long> todayNo = listNew.stream()
                    .filter(bean -> !DateUtil.today().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, bean.getCreateTime())))
                    .map(WeStrategicCrowdCustomerRel::getCustomerId)
                    .collect(Collectors.toSet());

            addNum = today.stream()
                    .filter(id ->!todayNo.contains(id))
                    .count();
            reduceNum = listNew.stream()
                    .filter(bean -> DateUtil.today().equals(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, bean.getUpdateTime())) && !today.contains(bean.getCustomerId()))
                    .map(WeStrategicCrowdCustomerRel::getCustomerId)
                    .distinct()
                    .count();
        }

        List<WeStrategicCrowdAnalyzelDataVo> analyze = this.baseMapper.getAnalyze(query);

        List<WeStrategicCrowdAnalyzelDataVo> analyzelDataVos = DateUtils.findDates(query.getBeginTime(), query.getEndTime()).stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
                .map(date -> {
                    List<WeStrategicCrowdAnalyzelDataVo> crowdAnalyzelDataVoList = Optional.ofNullable(analyze).orElseGet(ArrayList::new).stream().filter(item -> ObjectUtil.equal(date, item.getDateTime())).collect(Collectors.toList());
                    WeStrategicCrowdAnalyzelDataVo tempAnalyzeData = new WeStrategicCrowdAnalyzelDataVo();
                    tempAnalyzeData.setDateTime(date);
                    if (CollectionUtil.isNotEmpty(crowdAnalyzelDataVoList)) {
                        tempAnalyzeData.setTotal(crowdAnalyzelDataVoList.get(0).getTotal());
                    } else {
                        tempAnalyzeData.setTotal(0);
                    }
                    return tempAnalyzeData;
                }).collect(Collectors.toList());
        weStrategicCrowdAnalyzelVo.setCrowdAnalyzels(analyzelDataVos);
        weStrategicCrowdAnalyzelVo.setAddNum(addNum.intValue());
        weStrategicCrowdAnalyzelVo.setReduceNum(reduceNum.intValue());
        weStrategicCrowdAnalyzelVo.setNetAddNum((int) (addNum <= reduceNum ? 0L : addNum-reduceNum));
        return weStrategicCrowdAnalyzelVo;
    }

    @Override
    public List<WeCustomersVo> getCustomerList(WeStrategicCrowdQuery query) {
        if(query.getId() == null){
            throw new WeComException("ID不能为空");
        }

        List<WeStrategicCrowdCustomerRel> crowdCustomerRelList = weStrategicCrowdCustomerRelService.getListByCrowdId(query.getId());
        if(CollectionUtil.isNotEmpty(crowdCustomerRelList)){
            List<String> customerIds = crowdCustomerRelList.parallelStream().map(WeStrategicCrowdCustomerRel::getCustomerId).map(String::valueOf).collect(Collectors.toList());
            PageHelper.startPage(query.getPageNum(),query.getPageSize());
            return weCustomerService.findWeCustomerList(customerIds);
        }
        return new ArrayList<>();
    }

    @Override
    public List<WeCustomer> getCustomerListByCrowdIds(List<String> crowdIds){
        List<WeStrategicCrowdCustomerRel> crowdCustomerRelList = weStrategicCrowdCustomerRelService.list(new LambdaQueryWrapper<WeStrategicCrowdCustomerRel>()
                .in(WeStrategicCrowdCustomerRel::getCrowdId, crowdIds));
        if(CollectionUtil.isNotEmpty(crowdCustomerRelList)){
            List<String> customerIds = crowdCustomerRelList.parallelStream().map(WeStrategicCrowdCustomerRel::getCustomerId).map(String::valueOf).collect(Collectors.toList());
            return weCustomerService.listByIds(customerIds);
        }
        return new ArrayList<>();
    }


    @Override
    public List<WeStrategicCrowd> getListIgnoreTenant(WeStrategicCrowdQuery query) {
        return this.list(new LambdaQueryWrapper<WeStrategicCrowd>()
                .eq(WeStrategicCrowd::getDelFlag, Constants.COMMON_STATE)
                .eq(WeStrategicCrowd::getStatus,query.getStatus()));
    }


    @Override
    @Transactional
    public void categoryDel(Long[] ids) {
        weCategoryService.deleteWeCategoryById(ids);
        update(Wrappers.lambdaUpdate(WeStrategicCrowd.class).set(WeStrategicCrowd::getGroupId,1).in(WeStrategicCrowd::getGroupId,ids));
    }


}
