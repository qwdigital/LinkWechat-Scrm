package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeLxQrCode;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.qr.query.WeLxQrAddQuery;
import com.linkwechat.domain.qr.query.WeLxQrCodeListQuery;
import com.linkwechat.domain.qr.query.WxLxQrQuery;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeContactWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwFileClient;
import com.linkwechat.fegin.QwSysDeptClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.mapper.WeLxQrCodeMapper;
import com.linkwechat.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 拉新活码信息表(WeLxQrCode)
 *
 * @author danmo
 * @since 2023-03-07 14:59:35
 */
@Slf4j
@Service
public class WeLxQrCodeServiceImpl extends ServiceImpl<WeLxQrCodeMapper, WeLxQrCode> implements IWeLxQrCodeService {

    @Resource
    private QwCustomerClient qwCustomerClient;

    @Autowired
    private IWeQrAttachmentsService attachmentsService;

    @Autowired
    private IWeQrTagRelService tagRelService;

    @Autowired
    private IWeLxQrScopeService scopeService;

    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Resource
    private QwFileClient qwFileClient;

    @Resource
    private QwSysUserClient qwSysUserClient;

    @Resource
    private QwSysDeptClient qwSysDeptClient;

    @Autowired
    private IWeCustomerService weCustomerService;

    @Override
    public Long addQrCode(WeLxQrAddQuery weQrAddQuery) {
        WeLxQrAddVo weLxQrAddVo = new WeLxQrAddVo();
        WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();
        WeAddWayVo weAddWayResult = qwCustomerClient.addContactWay(weContactWay).getData();
        log.info("新增拉新活码结果 result:{}", JSONObject.toJSONString(weAddWayResult));

        if (Objects.isNull(weAddWayResult)) {
            throw new WeComException("新增企微活码失败");
        }
        if (ObjectUtil.notEqual(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode(), weAddWayResult.getErrCode())) {
            throw new WeComException(weAddWayResult.getErrCode(), WeErrorCodeEnum.parseEnum(weAddWayResult.getErrCode()).getErrorMsg());
        }
        WeLxQrCode weLxQrCode = weQrAddQuery.getLxQrCodeEntity(weAddWayResult.getConfigId(), weAddWayResult.getQrCode());
        if (save(weLxQrCode)) {
            //保存标签数据
            tagRelService.saveBatchByQrId(weLxQrCode.getId(), 3, weQrAddQuery.getQrTags());
            //保存活码范围数据
            scopeService.saveBatchByQrId(weLxQrCode.getId(), weQrAddQuery.getQrUserInfos());
            //保存活码素材
            attachmentsService.saveBatchByQrId(weLxQrCode.getId(), 3, weQrAddQuery.getAttachments());
            return weLxQrCode.getId();
        } else {
            throw new WeComException("数据入库失败");
        }
    }

    @Override
    public WeLxQrAddVo generateQrCode(Long id) {
        WeLxQrAddVo weLxQrAddVo = new WeLxQrAddVo();

        weLxQrAddVo.setQrId(id);
        String lxLinkUrl = StringUtils.format(linkWeChatConfig.getLxQrCodeUrl(), id);
        weLxQrAddVo.setLinkPath(lxLinkUrl);
        try {
            NetFileUtils.StreamMultipartFile streamMultipartFile = new NetFileUtils.StreamMultipartFile(IdUtil.simpleUUID() + "." + ImgUtil.IMAGE_TYPE_PNG, QrCodeUtil.generatePng(lxLinkUrl, QrConfig.create()));
            FileEntity fileInfo = qwFileClient.upload(streamMultipartFile).getData();
            weLxQrAddVo.setImageUrl(fileInfo.getUrl());
        } catch (Exception e) {
            log.error("生成二维码异常, query={}", JSONObject.toJSONString(weLxQrAddVo), e);
            throw new WeComException("生成二维码异常");
        }
        update(new LambdaUpdateWrapper<WeLxQrCode>()
                .set(WeLxQrCode::getLinkPath, weLxQrAddVo.getLinkPath())
                .set(WeLxQrCode::getImageUrl, weLxQrAddVo.getImageUrl()).eq(WeLxQrCode::getId, id));
        return weLxQrAddVo;
    }

    @Override
    public void updateQrCode(WeLxQrAddQuery query) {
        WeLxQrCode qrCode = getById(query.getQrId());

        if (Objects.isNull(qrCode)) {
            throw new WeComException("无效ID");
        }
        WeAddWayQuery weContactWay = query.getWeContactWay();
        weContactWay.setConfig_id(qrCode.getConfigId());
        WeResultVo weResult = qwCustomerClient.updateContactWay(weContactWay).getData();
        log.info("修改拉新活码结果 result:{}", JSONObject.toJSONString(weResult));

        if (Objects.isNull(weResult)) {
            throw new WeComException("修改企微活码失败");
        }
        if (ObjectUtil.notEqual(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode(), weResult.getErrCode())) {
            throw new WeComException(weResult.getErrCode(), WeErrorCodeEnum.parseEnum(weResult.getErrCode()).getErrorMsg());
        }

        WeLxQrCode lxQrCode = query.getLxQrCodeEntity(null, null);

        if (updateById(lxQrCode)) {
            //修改标签数据
            tagRelService.updateBatchByQrId(qrCode.getId(), 3, query.getQrTags());
            //修改活码范围数据
            scopeService.updateBatchByQrId(qrCode.getId(), query.getQrUserInfos());
            //修改活码素材
            attachmentsService.updateBatchByQrId(qrCode.getId(), 3, query.getAttachments());
        }
    }

    @Override
    public void delQrCode(List<Long> qrIds) {
        List<WeLxQrCode> weQrCodes = this.listByIds(qrIds);
        if (CollectionUtil.isNotEmpty(weQrCodes)) {
            LambdaUpdateWrapper<WeLxQrCode> wrapper = new LambdaUpdateWrapper<WeLxQrCode>().set(WeLxQrCode::getDelFlag, 1).in(WeLxQrCode::getId, qrIds);
            if (this.update(wrapper)) {
                //删除标签数据
                tagRelService.delBatchByQrIds(qrIds, 3);
                //删除员工数据
                scopeService.delBatchByQrIds(qrIds);
                //删除活码素材
                attachmentsService.remove(new LambdaQueryWrapper<WeQrAttachments>()
                        .in(WeQrAttachments::getQrId, ListUtil.toList(qrIds))
                        .eq(WeQrAttachments::getBusinessType, 3));
                ;
            }
            //异步删除企微活码---最好使用mq
            weQrCodes.forEach(item -> {
                if (StringUtils.isNotEmpty(item.getConfigId())) {
                    WeContactWayQuery weContactWayQuery = new WeContactWayQuery();
                    weContactWayQuery.setConfig_id(item.getConfigId());
                    qwCustomerClient.delContactWay(weContactWayQuery);
                }
            });
        }
    }


    @Override
    public WeLxQrCodeDetailVo getQrDetail(Long id) {
        WeLxQrCodeDetailVo detail = this.baseMapper.getQrDetail(id);

        if (Objects.isNull(detail)) {
            throw new WeComException("无效ID");
        }

        //todo 查询红包或者卡券信息

        List<WeLxQrScopeUserVo> qrUserInfos = detail.getQrUserInfos();
        if (CollectionUtil.isNotEmpty(qrUserInfos)) {
            Set<String> userIdSet = qrUserInfos.stream().map(WeLxQrScopeUserVo::getUserId).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
            Set<Integer> deptIdSet = qrUserInfos.stream().map(WeLxQrScopeUserVo::getParty).filter(Objects::nonNull).collect(Collectors.toSet());
            Map<String, String> userId2NameMap = new HashMap<>();
            Map<Integer, String> deptId2NameMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(userIdSet)) {
                SysUserQuery userQuery = new SysUserQuery();
                userQuery.setWeUserIds(new ArrayList<>(userIdSet));
                try {
                    List<SysUserVo> sysUserList = qwSysUserClient.getUserListByWeUserIds(userQuery).getData();
                    if (CollectionUtil.isNotEmpty(sysUserList)) {
                        Map<String, String> userMap = sysUserList.stream().collect(Collectors.toMap(SysUserVo::getWeUserId, SysUserVo::getUserName, (key1, key2) -> key2));
                        userId2NameMap.putAll(userMap);
                    }
                } catch (Exception e) {
                    log.error("换取用户名称失败：userQuery：{}", JSONObject.toJSONString(userQuery), e);
                }
            }

            if (CollectionUtil.isNotEmpty(deptIdSet)) {
                SysDeptQuery sysDeptQuery = new SysDeptQuery();
                sysDeptQuery.setDeptIds(new ArrayList<>(deptIdSet));
                try {
                    List<SysDeptVo> sysDeptList = qwSysDeptClient.getListByDeptIds(sysDeptQuery).getData();
                    if (CollectionUtil.isNotEmpty(sysDeptList)) {
                        Map<Integer, String> deptMap = sysDeptList.stream().collect(Collectors.toMap(item -> item.getDeptId().intValue(), item -> item.getDeptName(), (key1, key2) -> key2));
                        deptId2NameMap.putAll(deptMap);
                    }
                } catch (Exception e) {
                    log.error("换取部门名称失败：sysDeptQuery：{}", JSONObject.toJSONString(sysDeptQuery), e);
                }
            }

            for (WeLxQrScopeUserVo qrUserInfo : qrUserInfos) {
                //员工
                if (ObjectUtil.equal(1, qrUserInfo.getScopeType())) {
                    if (userId2NameMap.containsKey(qrUserInfo.getUserId())) {
                        qrUserInfo.setUserName(userId2NameMap.get(qrUserInfo.getUserId()));
                    }
                } else if (ObjectUtil.equal(2, qrUserInfo.getScopeType())) {
                    if (deptId2NameMap.containsKey(qrUserInfo.getParty())) {
                        qrUserInfo.setPartyName(deptId2NameMap.get(qrUserInfo.getParty()));
                    }
                }
            }
            detail.setQrUserInfos(qrUserInfos);
        }
        return detail;
    }

    @Override
    public List<WeQrCodeDetailVo> getQrCodeList(WeLxQrCodeListQuery query) {
        return null;
    }

    @Override
    public WeLxQrCodeLineVo getWeQrCodeLineStatistics(WeLxQrCodeListQuery query) {

        statisticsParamsCheck(query);

        WeLxQrCodeLineVo scanCountVo = new WeLxQrCodeLineVo();
        List<String> xAxis = new ArrayList<>();
        List<Integer> yAxis = new ArrayList<>();
        Long qrId = query.getQrId();
        Date beginTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, query.getBeginTime());
        Date endTime = DateUtils.dateTime(DateUtils.YYYY_MM_DD, query.getEndTime());
        WeLxQrCode weQrCode = getById(qrId);
        if (Objects.isNull(weQrCode)) {
            throw new WeComException("无效ID");
        }
        Map<String, List<WeCustomer>> customerMap = new HashMap<>();
        List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getState, weQrCode.getState()));
        if (CollectionUtil.isNotEmpty(customerList)) {
            scanCountVo.setTotal(customerList.size());
            long todayNum = customerList.stream().filter(item -> ObjectUtil.equal(DateUtils.getDate(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getAddTime()))).count();
            long tomorrowNum = customerList.stream().filter(item -> ObjectUtil.equal(DateUtil.tomorrow().toDateStr(), DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getAddTime()))).count();
            scanCountVo.setToday(Long.valueOf(todayNum).intValue());
            scanCountVo.setTodayDiff(Long.valueOf(todayNum - tomorrowNum).intValue());
            Map<String, List<WeCustomer>> listMap = customerList.stream().collect(Collectors.groupingBy(item -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getAddTime())));
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

    @Override
    public List<WeLxQrCodeSheetVo> getWeQrCodeListStatistics(WeLxQrCodeListQuery query) {

        statisticsParamsCheck(query);

        WeLxQrCode weQrCode = getById(query.getQrId());
        if (Objects.isNull(weQrCode)) {
            throw new WeComException("无效ID");
        }
        query.setState(weQrCode.getState());

        List<WeLxQrCodeSheetVo> lxQrCodeSheetList = this.baseMapper.getWeQrCodeListStatistics(query);
        int totalNum = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getState, weQrCode.getState()));

        for (WeLxQrCodeSheetVo lxQrCodeSheet : lxQrCodeSheetList) {
            lxQrCodeSheet.setTotalNum(totalNum);
            totalNum = totalNum - lxQrCodeSheet.getTodayNum();
        }
        return lxQrCodeSheetList;
    }


    private void statisticsParamsCheck(WeLxQrCodeListQuery query) {
        if (Objects.isNull(query)) {
            throw new WeComException("参数不能为空");
        }

        if (Objects.isNull(query.getQrId())) {
            throw new WeComException("ID不能为空");
        }
        if (StringUtils.isEmpty(query.getBeginTime())) {
            throw new WeComException("开始时间不能为空");
        }
        if (StringUtils.isEmpty(query.getEndTime())) {
            throw new WeComException("结束时间不能为空");
        }
    }


    @Override
    public WxLxQrCodeVo getQrcode(WxLxQrQuery query) {

        WxLxQrCodeVo wxLxQrCodeVo = new WxLxQrCodeVo();

        //判断是否为新客
        int count = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>().eq(WeCustomer::getUnionid, query.getUnionId()));

        if (count > 0) {
            throw new WeComException("您已经是企业客户啦，暂不符合该拉新活动要求。");
        }

        WeLxQrCode lxQrCode = getById(query.getQrId());

        if (Objects.isNull(lxQrCode)) {
            throw new WeComException("无效ID");
        }

        wxLxQrCodeVo.setQrCode(lxQrCode.getQrCode());
        return wxLxQrCodeVo;
    }

}
