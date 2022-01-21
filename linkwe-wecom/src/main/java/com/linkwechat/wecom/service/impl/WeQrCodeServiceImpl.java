package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.sql.SqlUtil;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.domain.query.qr.WeQrAddQuery;
import com.linkwechat.wecom.domain.query.qr.WeQrCodeListQuery;
import com.linkwechat.wecom.domain.query.qr.WeQrUserInfoQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeScanCountVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrScopeVo;
import com.linkwechat.wecom.mapper.WeQrCodeMapper;
import com.linkwechat.wecom.service.*;
import com.linkwechat.wecom.service.event.WeEventPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 活码信息表(WeQrCode)$desc
 *
 * @author danmo
 * @since 2021-11-07 02:17:43
 */
@Service
public class WeQrCodeServiceImpl extends ServiceImpl<WeQrCodeMapper, WeQrCode> implements IWeQrCodeService {

    @Autowired
    private WeExternalContactClient externalContactClient;

    @Autowired
    private IWeQrTagRelService tagRelService;

    @Autowired
    private IWeQrScopeService scopeService;

    @Autowired
    private IWeQrAttachmentsService attachmentsService;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private WeEventPublisherService weEventPublisherService;
    /**
     * 新增员工活码
     *
     * @param weQrAddQuery 入参
     */
    @Override
    public void addQrCode(WeQrAddQuery weQrAddQuery) {
        //校验排期是否存在冲突
        checkScope(weQrAddQuery.getQrUserInfos());
        WeExternalContactDto.WeContactWay weContactWay = weQrAddQuery.getWeContactWay();
        WeExternalContactDto resultDto = externalContactClient.addContactWay(weContactWay);
        if (resultDto != null && ObjectUtil.equal(0, resultDto.getErrcode())) {
            WeQrCode weQrCode = weQrAddQuery.getWeQrCodeEntity(resultDto.getConfig_id(), resultDto.getQr_code());
            if (save(weQrCode)) {
                //保存标签数据
                tagRelService.saveBatchByQrId(weQrCode.getId(), weQrAddQuery.getQrTags());
                //保存活码范围数据
                scopeService.saveBatchByQrId(weQrCode.getId(), weQrAddQuery.getQrUserInfos());
                //保存活码素材
                attachmentsService.saveBatchByQrId(weQrCode.getId(), weQrAddQuery.getAttachments());
            }
            weEventPublisherService.refreshQrCode(String.valueOf(weQrCode.getId()));
        }else {
            throw new WeComException(resultDto.getErrcode(), WeErrorCodeEnum.parseEnum(resultDto.getErrcode()).getErrorMsg());
        }
    }

    @Override
    public void updateQrCode(WeQrAddQuery weQrAddQuery) {
        //校验排期是否存在冲突
        checkScope(weQrAddQuery.getQrUserInfos());
        WeExternalContactDto.WeContactWay weContactWay = weQrAddQuery.getWeContactWay();
        WeExternalContactDto resultDto = externalContactClient.updateContactWay(weContactWay);
        if (resultDto != null && ObjectUtil.equal(0, resultDto.getErrcode())) {
            WeQrCode weQrCode = weQrAddQuery.getWeQrCodeEntity(null, null);
            if (updateById(weQrCode)) {
                //修改标签数据
                tagRelService.updateBatchByQrId(weQrCode.getId(), weQrAddQuery.getQrTags());
                //修改活码范围数据
                scopeService.updateBatchByQrId(weQrCode.getId(), weQrAddQuery.getQrUserInfos());
                //修改活码素材
                attachmentsService.updateBatchByQrId(weQrCode.getId(), weQrAddQuery.getAttachments());
            }
            weEventPublisherService.refreshQrCode(String.valueOf(weQrCode.getId()));
        }else {
            throw new WeComException(resultDto.getErrcode(), WeErrorCodeEnum.parseEnum(resultDto.getErrcode()).getErrorMsg());
        }
    }

    @Override
    public WeQrCodeDetailVo getQrDetail(Long qrId) {
        WeQrCodeDetailVo weQrCodeDetailVo = this.baseMapper.getQrDetailByQrId(qrId);
        List<WeQrScopeVo> weQrScopeVoList = scopeService.getWeQrScopeByQrIds(ListUtil.toList(qrId));
        weQrCodeDetailVo.setQrUserInfos(weQrScopeVoList);
        return weQrCodeDetailVo;
    }

    @Override
    public List<WeQrCodeDetailVo> getQrDetailByQrIds(List<Long> qrIds) {
        List<WeQrCodeDetailVo> qrDetailByQrIds = this.baseMapper.getQrDetailByQrIds(qrIds);
        List<WeQrScopeVo> weQrScopeVoList = scopeService.getWeQrScopeByQrIds(qrIds);
        Map<Long, List<WeQrScopeVo>> weQrScopeMap = Optional.ofNullable(weQrScopeVoList).orElseGet(ArrayList::new).stream().collect(Collectors.groupingBy(WeQrScopeVo::getQrId));
        for (WeQrCodeDetailVo qrCodeDetail: qrDetailByQrIds) {
            if(weQrScopeMap.get(qrCodeDetail.getId()) != null){
                qrCodeDetail.setQrUserInfos(weQrScopeMap.get(qrCodeDetail.getId()));
            }
        }
        return qrDetailByQrIds ;
    }

    @Override
    public PageInfo<WeQrCodeDetailVo> getQrCodeList(WeQrCodeListQuery qrCodeListQuery) {
        List<WeQrCodeDetailVo> weQrCodeList = new ArrayList<>();
        List<Long> qrCodeIdList = this.baseMapper.getQrCodeList(qrCodeListQuery);
        if (CollectionUtil.isNotEmpty(qrCodeIdList)) {
            PageDomain pageDomain = TableSupport.buildPageRequest();
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
            List<WeQrCodeDetailVo> qrDetailByQrIds = this.baseMapper.getQrDetailByQrIds(qrCodeIdList);
            List<WeQrScopeVo> weQrScopeVoList = scopeService.getWeQrScopeByQrIds(qrCodeIdList);
            Map<Long, List<WeQrScopeVo>> weQrScopeMap = Optional.ofNullable(weQrScopeVoList).orElseGet(ArrayList::new).stream().collect(Collectors.groupingBy(WeQrScopeVo::getQrId));
            for (WeQrCodeDetailVo qrCodeDetail: qrDetailByQrIds) {
                if(weQrScopeMap.get(qrCodeDetail.getId()) != null){
                    qrCodeDetail.setQrUserInfos(weQrScopeMap.get(qrCodeDetail.getId()));
                }
            }
            weQrCodeList.addAll(qrDetailByQrIds);
        }
        PageInfo<Long> pageIdInfo = new PageInfo<>(qrCodeIdList);
        PageInfo<WeQrCodeDetailVo> pageInfo = new PageInfo<>(weQrCodeList);
        pageInfo.setTotal(pageIdInfo.getTotal());
        pageInfo.setPageNum(pageIdInfo.getPageNum());
        pageInfo.setPageSize(pageIdInfo.getPageSize());
        return pageInfo;
    }


    @Override
    public void delQrCode(List<Long> qrIds) {
        List<WeQrCode> weQrCodes = this.listByIds(qrIds);
        if (CollectionUtil.isNotEmpty(weQrCodes)) {
            weQrCodes.forEach(item -> item.setDelFlag(1));
            if (this.updateBatchById(weQrCodes)) {
                //删除标签数据
                tagRelService.delBatchByQrIds(qrIds);
                //删除活码范围数据
                scopeService.delBatchByQrIds(qrIds);
                //删除活码素材
                attachmentsService.delBatchByQrIds(qrIds);
            }
            //异步删除企微活码---最好使用mq
            ThreadUtil.execute(() -> weQrCodes.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getConfigId())) {
                    WeExternalContactDto externalContactDto = new WeExternalContactDto();
                    externalContactDto.setConfig_id(item.getConfigId());
                    externalContactClient.delContactWay(externalContactDto);
                }
            }));
        }
    }

    @Override
    public WeQrCodeScanCountVo getWeQrCodeScanCount(WeQrCodeListQuery qrCodeListQuery) {
        WeQrCodeScanCountVo scanCountVo = new WeQrCodeScanCountVo();
        List<String> xAxis = new ArrayList<>();
        List<Integer> yAxis = new ArrayList<>();
        Long qrId = qrCodeListQuery.getQrId();
        Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, qrCodeListQuery.getBeginTime());
        Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, qrCodeListQuery.getEndTime());
        WeQrCode weQrCode = getById(qrId);
        Map<String, List<WeCustomer>> customerMap = new HashMap<>();
        List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getState, weQrCode.getState()));
        if (CollectionUtil.isNotEmpty(customerList)) {
            scanCountVo.setTotal(customerList.size());
            long count = customerList.stream().filter(item -> ObjectUtil.equal(DateUtils.getDate(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getFirstAddTime()))).count();
            scanCountVo.setToday(Long.valueOf(count).intValue());
            Map<String, List<WeCustomer>> listMap = customerList.stream().collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getFirstAddTime())));
            customerMap.putAll(listMap);
        }
        DateUtils.findDates(beginTime, endTime).stream().map(d -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, d))
                .forEach(date -> {
                    xAxis.add(date);
                    if (CollectionUtil.isNotEmpty(customerMap.get(date))) {
                        yAxis.add(customerMap.get(date).size());
                    } else {
                        yAxis.add(0);
                    }
                });
        scanCountVo.setXAxis(xAxis);
        scanCountVo.setYAxis(yAxis);
        return scanCountVo;
    }

    /**
     * 校验排期是否重复
     *
     * @param qrUserInfos 范围参数
     */
    private void checkScope(List<WeQrUserInfoQuery> qrUserInfos) {
        List<WeQrUserInfoQuery> qrUserInfoList = qrUserInfos.stream().filter(item -> ObjectUtil.equal(2, item.getType())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(qrUserInfoList)) {
            for (int i = 0; i < qrUserInfoList.size() - 1; i++) {
                for (int j = i + 1; j < qrUserInfoList.size(); j++) {
                    int finalJ = j;
                    long userSum = qrUserInfoList.get(i).getUserIds().stream()
                            .filter(one -> qrUserInfoList.get(finalJ).getUserIds().stream()
                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                    /*long partySum = qrUserInfoList.get(i).getPartys().stream()
                            .filter(one -> qrUserInfoList.get(finalJ).getPartys().stream()
                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();*/
                    if (userSum > 0 /*|| partySum > 0*/) {
                        long workCycleSum = qrUserInfoList.get(i).getWorkCycle().stream()
                                .filter(one -> qrUserInfoList.get(finalJ).getWorkCycle().stream()
                                        .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                        if (workCycleSum > 0) {
                            String beginTime1 = qrUserInfoList.get(i).getBeginTime();
                            String endTime1 = qrUserInfoList.get(i).getEndTime();
                            String beginTime2 = qrUserInfoList.get(finalJ).getBeginTime();
                            String endTime2 = qrUserInfoList.get(finalJ).getEndTime();
                            if (match(beginTime1, endTime1, beginTime2, endTime2)) {
                                throw new WeComException("员工或部门时间有冲突!");
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean match(String startTime1, String endTime1, String startTime2, String endTime2) {
        DateTime parseStartTime1 = DateUtil.parse(startTime1, "HH:mm");
        DateTime parseEndTime1 = DateUtil.parse(endTime1, "HH:mm");

        DateTime parseStartTime2 = DateUtil.parse(startTime2, "HH:mm");
        DateTime parseEndTime2 = DateUtil.parse(endTime2, "HH:mm");
        return !(parseStartTime2.isAfter(parseEndTime1) || parseStartTime1.isAfter(parseEndTime2));
    }
}
