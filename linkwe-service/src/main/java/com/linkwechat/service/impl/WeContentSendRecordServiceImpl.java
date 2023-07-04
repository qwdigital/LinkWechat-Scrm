package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.material.dto.WeContentSendViewDto;
import com.linkwechat.domain.material.entity.WeContentSendRecord;
import com.linkwechat.domain.material.entity.WeContentViewRecord;
import com.linkwechat.domain.material.entity.WeTalkMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.WeContentSendRecordQuery;
import com.linkwechat.domain.material.vo.ContentAxisVo;
import com.linkwechat.domain.material.vo.ContentDataDetailVo;
import com.linkwechat.domain.material.vo.WeContentCountVo;
import com.linkwechat.mapper.WeContentSendRecordMapper;
import com.linkwechat.mapper.WeContentViewRecordMapper;
import com.linkwechat.mapper.WeTalkMaterialMapper;
import com.linkwechat.service.IWeContentSendRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeContentSendRecordServiceImpl extends ServiceImpl<WeContentSendRecordMapper, WeContentSendRecord> implements IWeContentSendRecordService {

    @Resource
    private WeContentSendRecordMapper weContentSendRecordMapper;

    @Resource
    private WeContentViewRecordMapper weContentViewRecordMapper;

    @Resource
    private WeTalkMaterialMapper weTalkMaterialMapper;

    @Override
    public List<WeContentSendRecord> getSendTotal(Long id) {
        return list(
                new LambdaQueryWrapper<WeContentSendRecord>()
                        .eq(id != null, WeContentSendRecord::getContentId, id)
        );
    }

    @Override
    public List<WeContentSendRecord> getSendTotal(List<Long> ids) {
        return list(
                new LambdaQueryWrapper<WeContentSendRecord>()
                        .in(ObjectUtil.isNotEmpty(ids), WeContentSendRecord::getContentId, ids)
        );
    }

    @Override
    public List<WeContentSendRecord> getSendTotal(List<Long> ids, Integer resourceType, Long talkId) {
        return list(new LambdaQueryWrapper<WeContentSendRecord>()
                .eq(ObjectUtil.isNotEmpty(talkId), WeContentSendRecord::getTalkId, talkId)
                .eq(ObjectUtil.isNotEmpty(resourceType), WeContentSendRecord::getResourceType, resourceType)
                .in(ObjectUtil.isNotEmpty(ids), WeContentSendRecord::getContentId, ids)
        );
    }

    @Override
    public List<WeContentSendRecord> getSendTotal(Long id, Integer resourceType) {
        return list(
                new LambdaQueryWrapper<WeContentSendRecord>()
                        .eq(ObjectUtil.isNotEmpty(id), WeContentSendRecord::getContentId, id)
                        .eq(ObjectUtil.isNotEmpty(resourceType), WeContentSendRecord::getResourceType, resourceType)
        );
    }

    @Override
    public List<ContentDataDetailVo> getSendDetail(ContentDetailQuery contentDetailQuery) {
//        Long contentId = contentDetailQuery.getContentId();
//
//        String beginTime = contentDetailQuery.getBeginTime();
//        String endTime = contentDetailQuery.getEndTime();
////        if (StringUtils.isNotBlank(beginTime)) {
////            beginTime = DateUtils.initSqlBeginTime(beginTime);
////        }
////        if (StringUtils.isNotBlank(endTime)) {
////            endTime = DateUtils.initSqlEndTime(endTime);
////        }
//        QueryWrapper<WeContentSendRecord> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("id,talk_id, content_id, send_by, send_by_id, send_time, resource_type,count(id) as sendCount");
//        queryWrapper.lambda().eq(ObjectUtil.isNotEmpty(contentDetailQuery.getTalkId()), WeContentSendRecord::getTalkId, contentDetailQuery.getTalkId());
//        queryWrapper.lambda().eq(ObjectUtil.isNotEmpty(contentId), WeContentSendRecord::getContentId, contentId);
//        queryWrapper.lambda().eq(ObjectUtil.isNotEmpty(contentDetailQuery.getResourceType()), WeContentSendRecord::getResourceType, contentDetailQuery.getResourceType());
//
//        queryWrapper.lambda().apply(StringUtils.isNotEmpty(beginTime) ,"date_format(send_time, '%Y-%m-%d' ) >= '" + beginTime + "'");
//        queryWrapper.lambda().apply(StringUtils.isNotEmpty(endTime),"date_format(send_time, '%Y-%m-%d' ) <= '" + endTime + "'");
//
//        queryWrapper.lambda().groupBy(WeContentSendRecord::getSendById);
//
//        //取出所有的数据
//        List<WeContentSendRecord> weContentSendRecordList = list(queryWrapper);
//
//        List<ContentDataDetailVo> result = new ArrayList<>();
//        weContentSendRecordList.forEach(o -> {
//            ContentDataDetailVo contentDataDetailVo = new ContentDataDetailVo();
//            contentDataDetailVo.setSendTotalNum(o.getSendCount());
//            contentDataDetailVo.setSendBy(o.getSendBy());
//            result.add(contentDataDetailVo);
//        });
//
//        result.sort(Comparator.comparing(ContentDataDetailVo::getSendTotalNum).reversed());
        return this.baseMapper.findContentDataDetailVo(contentDetailQuery.getContentId(),contentDetailQuery.getTalkId(), contentDetailQuery.getBeginTime(), contentDetailQuery.getEndTime());
    }

    @Override
    public int getSendTotalToday(List<WeContentSendRecord> weContentSendRecordList) {
        if (ObjectUtil.isEmpty(weContentSendRecordList)) {
            return 0;
        }
        long count = weContentSendRecordList.stream().filter(item -> ObjectUtil.equal(DateUtils.getDate(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getSendTime()))).count();
        return (int) count;
    }

    @Override
    public List<ContentAxisVo> getContentAxis(Date beginTime, Date endTime, List<WeContentSendRecord> weContentSendRecordList, List<ContentAxisVo> contentAxisVoList) {
        Map<String, List<WeContentSendRecord>> listMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(weContentSendRecordList)) {
            Map<String, List<WeContentSendRecord>> map = weContentSendRecordList.stream().collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getSendTime())));
            listMap.putAll(map);
            List<Date> dateList = DateUtils.findDates(beginTime, endTime);
            dateList.stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
                    .forEach(dateStr -> {
                        if (CollectionUtil.isNotEmpty(listMap.get(dateStr))) {
                            for (ContentAxisVo contentAxisVo : contentAxisVoList) {
                                if (ObjectUtil.equal(contentAxisVo.getDateStr(), dateStr)) {
                                    contentAxisVo.setSendNum(listMap.get(dateStr).size());
                                }
                            }
                        }
                    });
        }
        return contentAxisVoList;
    }

    @Override
    public List<ContentAxisVo> initContentAxisDate(Date beginTime, Date endTime) {
        List<ContentAxisVo> contentAxisVoList = new ArrayList<>();
        List<Date> dateList = DateUtils.findDates(beginTime, endTime);
        List<String> list = dateList.stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d)).collect(Collectors.toList());
        list.forEach(s -> {
            ContentAxisVo contentAxisVo = new ContentAxisVo();
            contentAxisVo.setDateStr(s);
            contentAxisVoList.add(contentAxisVo);
        });
        return contentAxisVoList;
    }

    @Override
    public void setWeContentCountVoForSendRecord(ContentDetailQuery contentDetailQuery, WeContentCountVo weContentCountVo) {
        Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, contentDetailQuery.getBeginTime());
        Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, contentDetailQuery.getEndTime());
        Integer moduleType = contentDetailQuery.getModuleType();
        List<ContentAxisVo> contentAxisVoList = initContentAxisDate(beginTime, endTime);
        List<WeContentSendRecord> weContentSendRecordList = new ArrayList<>();
        switch (moduleType) {
            case 1:
                //获取素材中心发送数据
                //ContentId不传则统计所有素材中心的数据，传contentId则获取单个素材的数据
                weContentSendRecordList = getSendTotal(contentDetailQuery.getContentId(), contentDetailQuery.getResourceType());
                break;
            case 2:
                //talkId不传获取所有的话术中心的统计数据，传则是获取单个话术的统计数据
                Long talkId = contentDetailQuery.getTalkId();
                List<WeTalkMaterial> weTalkMaterialList = weTalkMaterialMapper.selectList(new LambdaQueryWrapper<WeTalkMaterial>().eq(talkId != null, WeTalkMaterial::getTalkId, talkId));
                List<Long> list = weTalkMaterialList.stream().map(WeTalkMaterial::getMaterialId).distinct().collect(Collectors.toList());
                weContentSendRecordList = getSendTotal(list, contentDetailQuery.getResourceType(), talkId);
                break;
        }

        //当天发送总次数
        int sendTotalToday = getSendTotalToday(weContentSendRecordList);

        getContentAxis(beginTime, endTime, weContentSendRecordList, contentAxisVoList);
        //当天发送总次数
        weContentCountVo.setSendTodayNum(sendTotalToday);
        //发送总次数
        weContentCountVo.setSendTotalNum(weContentSendRecordList.size());
        //
        weContentCountVo.setContentAxisVoList(contentAxisVoList);
    }

    @Override
    public List<WeContentSendViewDto> getSendViewDataTotal(Integer resourceType) {
        //List<WeContentSendViewDto> weSendRecordList = weContentSendRecordMapper.selectSendTotalNumGroupByContentId();

        //查询所有素材的发送次数
        QueryWrapper<WeContentSendRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(WeContentSendRecord::getResourceType, resourceType);
        List<WeContentSendRecord> weContentSendRecords = weContentSendRecordMapper.selectList(queryWrapper);
        Map<Long, List<WeContentSendRecord>> collect = weContentSendRecords.stream().collect(Collectors.groupingBy(o -> o.getTalkId()));
        List<WeContentSendViewDto> weSendRecordList = new ArrayList<>();
        collect.forEach((k, v) -> {
            WeContentSendViewDto weContentSendViewDto = new WeContentSendViewDto();
            weContentSendViewDto.setTalkId(k);
            weContentSendViewDto.setSendTotalNum(Long.valueOf(v.size()));
            weSendRecordList.add(weContentSendViewDto);
        });

        //查询所有的素材查看记录
        QueryWrapper<WeContentViewRecord> viewRecordQueryWrapper = new QueryWrapper<>();
        viewRecordQueryWrapper.lambda().eq(WeContentViewRecord::getResourceType, resourceType);
        List<WeContentViewRecord> weContentViewRecordList = weContentViewRecordMapper.selectList(viewRecordQueryWrapper);

        weSendRecordList.forEach(
                weSendRecord -> {
                    Long talkId = weSendRecord.getTalkId();
                    long count = weContentViewRecordList.stream().filter(weViewRecord -> weViewRecord.getTalkId().equals(talkId)).count();
                    List<WeContentViewRecord> sendRecords = weContentViewRecordList.stream().filter(weViewRecord -> weViewRecord.getTalkId().equals(talkId)).collect(Collectors.toList());
                    List<String> sendRecords1 = sendRecords.stream().map(WeContentViewRecord::getViewOpenid).collect(Collectors.toList());
                    long count1 = sendRecords1.stream().distinct().count();
                    weSendRecord.setViewTotalNum(count);
                    weSendRecord.setViewByTotalNum(count1);
                }
        );
        return weSendRecordList;
    }

    @Override
    public void singleSend(WeContentSendRecordQuery weContentSendRecordQuery) {
        WeContentSendRecord weContentSendRecord = new WeContentSendRecord();
        if (weContentSendRecordQuery.getResourceType() != 1 && weContentSendRecordQuery.getTalkId() != null) {
            weContentSendRecord.setTalkId(weContentSendRecordQuery.getTalkId());
        }
        weContentSendRecord.setContentId(weContentSendRecordQuery.getContentId());
        weContentSendRecord.setSendBy(weContentSendRecordQuery.getSendBy());
        weContentSendRecord.setSendById(weContentSendRecordQuery.getSendById());
        weContentSendRecord.setSendTime(weContentSendRecordQuery.getSendTime() == null ? new Date() : weContentSendRecordQuery.getSendTime());
        weContentSendRecord.setResourceType(weContentSendRecordQuery.getResourceType());
        weContentSendRecordMapper.insert(weContentSendRecord);
    }

    @Override
    public void withOneTouchSend(WeContentSendRecordQuery weContentSendRecordQuery) {
        //话术中心Id
        Long talkId = weContentSendRecordQuery.getTalkId();
        //获取话术中心所关联的所有素材中心的数据
        LambdaQueryWrapper<WeTalkMaterial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WeTalkMaterial::getTalkId, talkId);
        List<WeTalkMaterial> weTalkMaterials = weTalkMaterialMapper.selectList(queryWrapper);

        if (weTalkMaterials != null && weTalkMaterials.size() > 0) {
            for (WeTalkMaterial weTalkMaterial : weTalkMaterials) {
                WeContentSendRecord weContentSendRecord = new WeContentSendRecord();
                weContentSendRecord.setTalkId(talkId);
                weContentSendRecord.setContentId(weTalkMaterial.getMaterialId());
                weContentSendRecord.setSendBy(weContentSendRecordQuery.getSendBy());
                weContentSendRecord.setSendById(weContentSendRecordQuery.getSendById());
                weContentSendRecord.setSendTime(weContentSendRecordQuery.getSendTime() == null ? new Date() : weContentSendRecordQuery.getSendTime());
                weContentSendRecord.setResourceType(weContentSendRecordQuery.getResourceType());
                weContentSendRecordMapper.insert(weContentSendRecord);
            }
        }
    }
}
