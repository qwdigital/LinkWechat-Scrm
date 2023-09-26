package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.linkwechat.common.constant.MessageConstants;
import com.linkwechat.common.context.SecurityContextHolder;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.TrajectorySceneType;
import com.linkwechat.common.enums.TrajectoryType;
import com.linkwechat.common.enums.message.MessageTypeEnum;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.*;
import com.linkwechat.domain.customer.WeMakeCustomerTag;
import com.linkwechat.domain.material.entity.WeContentTalk;
import com.linkwechat.domain.material.entity.WeContentViewRecord;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.entity.WeTalkMaterial;
import com.linkwechat.domain.material.query.ContentDetailQuery;
import com.linkwechat.domain.material.query.WeContentViewRecordQuery;
import com.linkwechat.domain.material.vo.ContentAxisVo;
import com.linkwechat.domain.material.vo.ContentDataDetailVo;
import com.linkwechat.domain.material.vo.WeContentCountVo;
import com.linkwechat.domain.media.WeMessageTemplate;
import com.linkwechat.domain.msg.QwAppMsgBody;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.*;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WeContentViewRecordServiceImpl extends ServiceImpl<WeContentViewRecordMapper, WeContentViewRecord> implements IWeContentViewRecordService {

    @Resource
    private WeTalkMaterialMapper weTalkMaterialMapper;
    @Resource
    private WeCustomerMapper weCustomerMapper;
    @Resource
    private WeContentViewRecordMapper weContentViewRecordMapper;

    @Resource
    private QwAppSendMsgService qwAppSendMsgService;
    @Resource
    private QwSysUserClient qwSysUserClient;
    @Resource
    private WeMaterialMapper weMaterialMapper;
    @Resource
    private IWeMessageNotificationService weMessageNotificationService;


    @Resource
    private IWeContentTalkService weContentTalkService;
    /**
     * 客户轨迹
     */
    @Resource
    private WeCustomerTrajectoryMapper weCustomerTrajectoryMapper;


    @Resource
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeTagService iWeTagService;


    @Override
    public List<WeContentViewRecord> getViewTotal(Long id) {
        return list(
                new LambdaQueryWrapper<WeContentViewRecord>()
                        .eq(id != null, WeContentViewRecord::getContentId, id)
        );
    }

    @Override
    public List<WeContentViewRecord> getViewTotal(List<Long> ids) {
        return list(
                new LambdaQueryWrapper<WeContentViewRecord>()
                        .in(ObjectUtil.isNotEmpty(ids), WeContentViewRecord::getContentId, ids)
        );
    }

    @Override
    public List<WeContentViewRecord> getViewTotal(List<Long> ids, Integer resourceType, Long talkId) {
        return list(new LambdaQueryWrapper<WeContentViewRecord>()
                .eq(ObjectUtil.isNotEmpty(talkId), WeContentViewRecord::getTalkId, talkId)
                .eq(ObjectUtil.isNotEmpty(resourceType), WeContentViewRecord::getResourceType, resourceType)
                .in(ObjectUtil.isNotEmpty(ids), WeContentViewRecord::getContentId, ids)

        );
    }

    @Override
    public List<WeContentViewRecord> getViewTotal(Long id, Integer resourceType) {
        return list(
                new LambdaQueryWrapper<WeContentViewRecord>()
                        .eq(ObjectUtil.isNotEmpty(id), WeContentViewRecord::getContentId, id)
                        .eq(ObjectUtil.isNotEmpty(resourceType), WeContentViewRecord::getResourceType, resourceType)
        );
    }

    @Override
    public List<ContentDataDetailVo> getViewDetail(ContentDetailQuery contentDetailQuery) {
//        Long contentId = contentDetailQuery.getContentId();

//        String beginTime = contentDetailQuery.getBeginTime();
//        String endTime = contentDetailQuery.getEndTime();
//
//        if (StringUtils.isNotBlank(beginTime)) {
//            beginTime = DateUtils.initSqlBeginTime(beginTime);
//            contentDetailQuery.setBeginTime(beginTime);
//        }
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtils.initSqlEndTime(endTime);
//            contentDetailQuery.setEndTime(endTime);
//        }

        //查询数据
//        List<WeContentViewRecord> weContentViewRecordList = weContentViewRecordMapper.getList(contentDetailQuery);
//        Map<String, List<WeContentViewRecord>> collect = weContentViewRecordList.stream().collect(Collectors.groupingBy(WeContentViewRecord::getViewOpenid));
//
//        List<ContentDataDetailVo> result = new ArrayList<>();
//        collect.forEach((k, v) -> {
//            WeContentViewRecord item = v.get(0);
//            ContentDataDetailVo contentDataDetailVo = new ContentDataDetailVo();
//            contentDataDetailVo.setViewTime(item.getViewTime());
//            contentDataDetailVo.setViewTotalNum(v.size());
//            Long sum = v.stream().mapToLong(o -> o.getViewWatchTime()).sum();
//            contentDataDetailVo.setViewDuration(sum.intValue());
//            contentDataDetailVo.setViewByOpenid(item.getViewOpenid());
//            contentDataDetailVo.setViewByUnionid(item.getViewUnionid());
//            contentDataDetailVo.setViewBy(item.getExternalUserName());
//            contentDataDetailVo.setViewAvatar(item.getExternalAvatar());
//            result.add(contentDataDetailVo);
//        });
//        result.sort(Comparator.comparing(ContentDataDetailVo::getViewTime).reversed());
        List<ContentDataDetailVo> contentDataDetailVos = this.baseMapper.findContentDataDetailVos(String.valueOf(contentDetailQuery.getContentId())
                , contentDetailQuery.getBeginTime(), contentDetailQuery.getEndTime());

        if (CollectionUtil.isNotEmpty(contentDataDetailVos)) {
            contentDataDetailVos.stream().forEach(contentDataDetailVo -> {
                if (contentDataDetailVo.getViewDuration() != null) {
                    contentDataDetailVo.setViewDurationCpt(DateUtils.formatTime(
                            contentDataDetailVo.getViewDuration().longValue() * 1000
                    ));
                }

            });
        }

        return contentDataDetailVos;
    }

    @Override
    public int getViewTotalToday(List<WeContentViewRecord> weContentViewRecordList) {
        if (ObjectUtil.isEmpty(weContentViewRecordList)) {
            return 0;
        }
        long count = weContentViewRecordList.stream().
                filter(item -> ObjectUtil.equal(DateUtils.getDate(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getViewTime()))).count();
        return (int) count;
    }

    @Override
    public List<ContentAxisVo> getContentAxis(Date beginTime, Date
            endTime, List<WeContentViewRecord> weContentViewRecordList, List<ContentAxisVo> contentAxisVoList) {
        Map<String, List<WeContentViewRecord>> listMap = new HashMap<>();
        if (ObjectUtil.isNotEmpty(weContentViewRecordList)) {
            Map<String, List<WeContentViewRecord>> map = weContentViewRecordList.stream().collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getViewTime())));
            listMap.putAll(map);
            List<Date> dateList = DateUtils.findDates(beginTime, endTime);
            dateList.stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
                    .forEach(dateStr -> {
                        List<WeContentViewRecord> viewRecordList = listMap.get(dateStr);
                        if (CollectionUtil.isNotEmpty(viewRecordList)) {
                            long count = viewRecordList.stream().filter(o -> o.getIsAuth().equals(1)).map(WeContentViewRecord::getViewOpenid).distinct().count();
                            for (ContentAxisVo contentAxisVo : contentAxisVoList) {
                                if (ObjectUtil.equal(contentAxisVo.getDateStr(), dateStr)) {
                                    contentAxisVo.setViewNum(viewRecordList.size());
                                    contentAxisVo.setViewByNum(count);
                                }
                            }
                        }
                    });
        }
        return contentAxisVoList;
    }

    @Override
    public int getViewByToday(List<WeContentViewRecord> weContentViewRecordList) {
        long count = weContentViewRecordList.stream().
                filter(item -> ObjectUtil.equal(DateUtils.getDate(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getViewTime())) && item.getIsAuth().equals(1))
                .map(WeContentViewRecord::getViewOpenid).distinct().count();
        return (int) count;
    }

    @Override
    public int getViewByTotal(List<WeContentViewRecord> weContentViewRecordList) {
        long count = weContentViewRecordList.stream().filter(o -> o.getIsAuth().equals(1))
                .map(WeContentViewRecord::getViewOpenid)
                .distinct().count();
        return (int) count;
    }

    @Override
    public void setWeContentCountVoForViewRecord(ContentDetailQuery contentDetailQuery, WeContentCountVo
            weContentCountVo) {
        Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, contentDetailQuery.getBeginTime());
        Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, contentDetailQuery.getEndTime());
        Integer moduleType = contentDetailQuery.getModuleType();
        List<ContentAxisVo> contentAxisVoList = weContentCountVo.getContentAxisVoList();
        List<WeContentViewRecord> weContentViewRecordList = new ArrayList<>();

        switch (moduleType) {
            case 1:
                //素材中心
                weContentViewRecordList = getViewTotal(contentDetailQuery.getContentId(), contentDetailQuery.getResourceType());
                break;
            case 2:
                //话术中心
                Long talkId = contentDetailQuery.getTalkId();
                List<WeTalkMaterial> weTalkMaterialList = weTalkMaterialMapper.selectList(new LambdaQueryWrapper<WeTalkMaterial>().eq(talkId != null, WeTalkMaterial::getTalkId, talkId));
                List<Long> list = weTalkMaterialList.stream().map(WeTalkMaterial::getMaterialId).distinct().collect(Collectors.toList());
                weContentViewRecordList = getViewTotal(list, contentDetailQuery.getResourceType(), talkId);
                break;
        }

        //数据过滤
        //当天查看总次数
        int viewTotalToday = getViewTotalToday(weContentViewRecordList);
        //当天查看总人数
        int viewByToday = getViewByToday(weContentViewRecordList);
        //查看总人数
        int viewByTotal = getViewByTotal(weContentViewRecordList);

        List<ContentAxisVo> contentAxis = getContentAxis(beginTime, endTime, weContentViewRecordList, contentAxisVoList);
        weContentCountVo.setContentAxisVoList(contentAxis);
        //查看总人数
        weContentCountVo.setViewByTotalNum(viewByTotal);
        //今日查看总人数
        weContentCountVo.setViewByTotalTodayNum(viewByToday);
        //今日查看总次数
        weContentCountVo.setViewTotalTodayNum(viewTotalToday);
        //查看总次数
        weContentCountVo.setViewTotalNum(weContentViewRecordList.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addView(WeContentViewRecordQuery weContentViewRecordQuery) {

        //获取员工信息
        AjaxResult result = qwSysUserClient.getUserInfoById(weContentViewRecordQuery.getSendUserId());

        //如果发送员工不存在，则跳过此次的记录
        if (result == null || result.getCode() != 200) {
            return;
        }
        Object object = result.getData();
        String s = JSONObject.toJSONString(object);
        SysUser data = JSONObject.parseObject(s, SysUser.class);



        //根据unionId获取客户
        WeCustomer weCustomer = null;
        if (StringUtils
                .isNotEmpty(weContentViewRecordQuery.getUnionid())) {


            List<WeCustomer> weCustomers = weCustomerMapper.selectList(new LambdaQueryWrapper<WeCustomer>()
                    .eq(WeCustomer::getAddUserId,data.getWeUserId())
                    .eq(WeCustomer::getUnionid, weContentViewRecordQuery.getUnionid()));

            if (CollectionUtil.isNotEmpty(weCustomers)) {
                weCustomer = weCustomers.stream().findFirst().get();
            }
        }


        //3.添加查看数据
        WeContentViewRecord weContentViewRecord = new WeContentViewRecord();
        if (weContentViewRecordQuery.getTalkId() != null) {
            WeContentTalk weContentTalk = weContentTalkService.getById(weContentViewRecordQuery.getTalkId());
            weContentViewRecord.setTalkId(weContentViewRecordQuery.getTalkId());
            //TalkType 0企业话术，1客服话术
            Integer resourceType = weContentTalk.getTalkType() == 0 ? 2 : 3;
            weContentViewRecord.setResourceType(resourceType);
        } else {
            weContentViewRecord.setResourceType(1);
        }
        weContentViewRecord.setContentId(weContentViewRecordQuery.getContentId());
        weContentViewRecord.setViewOpenid(weContentViewRecordQuery.getOpenid());
        weContentViewRecord.setViewUnionid(weContentViewRecordQuery.getUnionid());
        weContentViewRecord.setViewTime(weContentViewRecordQuery.getViewTime());
        weContentViewRecord.setViewWatchTime(weContentViewRecordQuery.getViewWatchTime());
        weContentViewRecord.setIsAuth(1);
        if (ObjectUtil.isNotNull(weCustomer)) {
            //TODO
            weContentViewRecord.setExternalUserId(weCustomer.getExternalUserid());
            weContentViewRecord.setExternalUserName(weCustomer.getCustomerName());
            weContentViewRecord.setIsCustomer(weCustomer.getCustomerType().equals(1) ? 0 : 1);
            if (StringUtils.isNotEmpty(weCustomer.getAvatar())) {
                weContentViewRecord.setExternalAvatar(weCustomer.getAvatar());
            }
        } else {
            //非企业客户
            weContentViewRecord.setIsCustomer(0);
        }
         weContentViewRecordMapper.insert(weContentViewRecord);


        //素材信息
        WeMaterial weMaterial = weMaterialMapper.selectById(weContentViewRecordQuery.getContentId());

        //时间转换
        Long viewWatchTime = weContentViewRecordQuery.getViewWatchTime();
        //秒数
        long second = viewWatchTime / 1000;
        //小时
        long hour = second / 3600;
        //剩余秒数
        second = second % 3600;

        //分钟
        long minutes = second / 60;
        //剩余秒数
        second = second % 60;

        StringBuffer sb = new StringBuffer();
        sb.append(hour != 0L ? hour + "时" : "");
        sb.append(minutes != 0L ? minutes + "分" : "");
        sb.append(second != 0L ? second + "秒" : "");


        //给客户打标签
        if(ObjectUtil.isNotEmpty(weCustomer)){
            if(StringUtils.isNotEmpty(weMaterial.getTagIds())){
                List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId, weMaterial.getTagIds().split(",")));
                if(CollectionUtil.isNotEmpty(weTags)){
                    iWeCustomerService.makeLabel(WeMakeCustomerTag.builder()
                            .addTag(weTags)
                            .userId(weCustomer.getAddUserId())
                            .source(false)
                            .externalUserid(weCustomer.getExternalUserid())
                            .build());
                }
            }

        }

        //4.发送应用通知消息。由RabbitMQ进行解耦
        if (ObjectUtil.isNotEmpty(weCustomer)) {

            log.info("发送应用通知消息");

            //发送消息
            QwAppMsgBody qwAppMsgBody = new QwAppMsgBody();
            qwAppMsgBody.setCorpId(SecurityUtils.getCorpId());
            qwAppMsgBody.setCorpUserIds(Lists.newArrayList(data.getWeUserId()));

            //发送模板
            WeMessageTemplate weMessageTemplate = new WeMessageTemplate();
            weMessageTemplate.setMsgType("text");
            weMessageTemplate.setContent("【客户动态】\r\n\r\n 客户 " + weCustomer.getCustomerName() + " 查看了您发送的素材 「" + weMaterial.getMaterialName() + "」" + sb.toString());
            qwAppMsgBody.setMessageTemplates(weMessageTemplate);
            qwAppSendMsgService.appMsgSend(qwAppMsgBody);

            //添加消息通知
            weMessageNotificationService.save(MessageTypeEnum.MATERIAL.getType(), data.getWeUserId(), MessageConstants.MATERIAL_LOOK, weCustomer.getCustomerName(), weMaterial.getMaterialName(), sb.toString());
        }

        //5.添加客户轨迹
        if (ObjectUtil.isNotEmpty(weCustomer) && ObjectUtil.isNotEmpty(data)) {

            log.info("添加客户轨迹");

            WeCustomerTrajectory weCustomerTrajectory = new WeCustomerTrajectory();
            //轨迹类型:(1:客户动态;2:员工动态;3:互动动态4:跟进动态5:客群动态)
            weCustomerTrajectory.setTrajectoryType(TrajectoryType.TRAJECTORY_TYPE_HDGZ.getType());
            //轨迹场景类型，详细描述，见TrajectorySceneType
            weCustomerTrajectory.setTrajectorySceneType(TrajectorySceneType.TRAJECTORY_TITLE_LOOK_MATERIAL.getType());
            //操作人类型:1:客户;2:员工;
            weCustomerTrajectory.setOperatorType(1);
            //操作人id
            weCustomerTrajectory.setOperatorId(weCustomer.getExternalUserid());
            //操作人姓名
            weCustomerTrajectory.setOperatorName(weCustomer.getCustomerName());
            //被操作对象类型:1:客户;2:员工:3:客群
            weCustomerTrajectory.setOperatoredObjectType(2);
            //被操作对象的id
            weCustomerTrajectory.setOperatoredObjectId(data.getWeUserId());
            //被操作对象名称
            weCustomerTrajectory.setOperatoredObjectName(data.getUserName());
            //客户id或群id，查询字段冗余,档该id不存在的时候代表
            weCustomerTrajectory.setExternalUseridOrChatid(weCustomer.getExternalUserid());
            //员工id，查询字段冗余
            weCustomerTrajectory.setWeUserId(data.getWeUserId());
            //动作
            weCustomerTrajectory.setAction(TrajectorySceneType.TRAJECTORY_TITLE_LOOK_MATERIAL.getName());
            //标题
            weCustomerTrajectory.setTitle(TrajectoryType.TRAJECTORY_TYPE_HDGZ.getName());
            //文案内容,整体内容

            weCustomerTrajectory.setContent(
                    String.format(TrajectorySceneType.TRAJECTORY_TITLE_LOOK_MATERIAL.getMsgTpl(), weCustomer.getCustomerName(), data.getUserName(), sb.toString())
            );
            //
            weCustomerTrajectory.setMaterialId(weMaterial.getId());
            weCustomerTrajectoryMapper.insert(weCustomerTrajectory);
        }
    }
}
