package com.linkwechat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
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
import com.github.pagehelper.PageInfo;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.HttpStatus;
import com.linkwechat.common.core.domain.BaseEntity;
import com.linkwechat.common.core.domain.FileEntity;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.common.utils.img.NetFileUtils;
import com.linkwechat.domain.WeCorpAccount;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeLxQrCode;
import com.linkwechat.domain.WeLxQrCodeLog;
import com.linkwechat.domain.envelopes.WeRedEnvelopesRecord;
import com.linkwechat.domain.envelopes.dto.H5RedEnvelopesDetailDto;
import com.linkwechat.domain.qr.WeQrAttachments;
import com.linkwechat.domain.qr.query.*;
import com.linkwechat.domain.qr.vo.*;
import com.linkwechat.domain.system.dept.query.SysDeptQuery;
import com.linkwechat.domain.system.dept.vo.SysDeptVo;
import com.linkwechat.domain.system.user.query.SysUserQuery;
import com.linkwechat.domain.system.user.vo.SysUserVo;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.query.qr.WeContactWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.domain.wx.coupon.WxSendCouponQuery;
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
import java.math.BigDecimal;
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

    @Autowired
    private IWeRedEnvelopesService weRedEnvelopesService;

    @Autowired
    private IWeCouponService weCouponService;

    @Autowired
    private IWeCorpAccountService weCorpAccountService;

    @Autowired
    private IWeLxQrCodeLogService weLxQrCodeLogService;

    //@Autowired
    //private WechatPayConfig wechatPayConfig;

    @Override
    public Long addQrCode(WeLxQrAddQuery weQrAddQuery) {
        WeLxQrAddVo weLxQrAddVo = new WeLxQrAddVo();

        WeAddWayQuery weContactWay = weQrAddQuery.getWeContactWay();

        getWeUserIdByPosition(weQrAddQuery, weContactWay);

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
    public WeLxQrAddVo generateQrCode(Long id, Integer qrType) {
        WeLxQrAddVo weLxQrAddVo = new WeLxQrAddVo();

        weLxQrAddVo.setQrId(id);
        String lxLinkUrl = StringUtils.format(linkWeChatConfig.getLxQrCodeUrl(), id,qrType);
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

        getWeUserIdByPosition(query, weContactWay);

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
        return getQrDetail(id, true);
    }

    @Override
    public WeLxQrCodeDetailVo getQrDetail(Long id, Boolean isNeedName) {
        WeLxQrCodeDetailVo detail = this.baseMapper.getQrDetail(id);

        if (Objects.isNull(detail)) {
            throw new WeComException("无效ID");
        }

        //todo 查询红包或者卡券信息

        List<WeLxQrScopeUserVo> qrUserInfos = detail.getQrUserInfos();
        if (isNeedName && CollectionUtil.isNotEmpty(qrUserInfos)) {
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
    public WeLxQrCodeDetailVo getQrDetailByState(String state) {
        if (StringUtils.isEmpty(state)) {
            return null;
        }
        return this.baseMapper.getQrDetailByState(state);
    }

    @Override
    public PageInfo<WeLxQrCodeListVo> getQrCodeList(WeLxQrCodeListQuery query) {
        PageInfo<WeLxQrCodeListVo> pageInfo = new PageInfo<>();
        LambdaQueryWrapper<WeLxQrCode> wrapper = new LambdaQueryWrapper<WeLxQrCode>()
                .like(StringUtils.isNotEmpty(query.getQrName()), WeLxQrCode::getName, query.getQrName())
                .eq(Objects.nonNull(query.getType()), WeLxQrCode::getType, query.getType())
                .ge(StringUtils.isNotEmpty(query.getBeginTime()), WeLxQrCode::getCreateTime, query.getBeginTime())
                .le(StringUtils.isNotEmpty(query.getEndTime()), WeLxQrCode::getCreateTime, query.getEndTime())
                .eq(WeLxQrCode::getDelFlag, 0)
                .orderByDesc(BaseEntity::getUpdateTime);
        List<WeLxQrCode> lxQrCodeList = list(wrapper);
        if (CollectionUtil.isNotEmpty(lxQrCodeList)) {
            //计算领取总数
            List<String> stateList = lxQrCodeList.parallelStream().map(WeLxQrCode::getState).collect(Collectors.toList());

            List<Long> qrIds = lxQrCodeList.parallelStream().map(WeLxQrCode::getId).collect(Collectors.toList());
            //客户数
            List<WeCustomer> customerList = weCustomerService.list(new LambdaQueryWrapper<WeCustomer>().in(WeCustomer::getState, stateList));
            Map<String, Long> stateAndCountMap = new HashMap<>(64);
            Map<Long, Long> qrIdAndCountMap = new HashMap<>(64);
            if (CollectionUtil.isNotEmpty(customerList)) {
                stateAndCountMap.putAll(customerList.parallelStream().collect(Collectors.groupingBy(WeCustomer::getState, Collectors.counting())));

                List<String> unionIds = customerList.parallelStream().map(WeCustomer::getUnionid).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());

                List<WeLxQrCodeLog> codeLogList = weLxQrCodeLogService.list(new LambdaQueryWrapper<WeLxQrCodeLog>()
                        .in(WeLxQrCodeLog::getQrId, qrIds)
                        .in(WeLxQrCodeLog::getUnionId, unionIds));
                qrIdAndCountMap.putAll(codeLogList.parallelStream().collect(Collectors.groupingBy(WeLxQrCodeLog::getQrId, Collectors.counting())));
            }

            List<WeLxQrCodeListVo> list = lxQrCodeList.stream().map(shortLink -> {
                WeLxQrCodeListVo codeList = new WeLxQrCodeListVo();
                BeanUtil.copyProperties(shortLink, codeList);

                if (stateAndCountMap.containsKey(shortLink.getState())) {
                    codeList.setScanNum(stateAndCountMap.get(shortLink.getState()).intValue());
                }
                if (qrIdAndCountMap.containsKey(shortLink.getId())) {
                    codeList.setReceiveNum(qrIdAndCountMap.get(shortLink.getId()).intValue());
                }
                return codeList;
            }).collect(Collectors.toList());
            pageInfo.setList(list);
        }
        PageInfo<WeLxQrCode> pageIdInfo = new PageInfo<>(lxQrCodeList);
        pageInfo.setTotal(pageIdInfo.getTotal());
        pageInfo.setPageNum(pageIdInfo.getPageNum());
        pageInfo.setPageSize(pageIdInfo.getPageSize());
        return pageInfo;
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


    private void getWeUserIdByPosition(WeLxQrAddQuery weQrAddQuery, WeAddWayQuery weContactWay) {
        //岗位列表
        String positions = weQrAddQuery.getQrUserInfos().stream().map(WeLxQrUserInfoQuery::getPositions)
                .filter(CollectionUtil::isNotEmpty).flatMap(Collection::stream).collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(positions)) {
            List<SysUser> userList = qwSysUserClient.findAllSysUser(null, positions, null).getData();
            if (CollectionUtil.isNotEmpty(userList)) {
                List<String> weUserIds = userList.stream().map(SysUser::getWeUserId).collect(Collectors.toList());
                List<String> user = weContactWay.getUser();
                Set<String> userIdSet = CollectionUtil.unionDistinct(weUserIds, user);
                weContactWay.setUser(new ArrayList<>(userIdSet));
            }
        }
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

        WeLxQrCode lxQrCode = getById(query.getQrId());

        if (Objects.isNull(lxQrCode)) {
            throw new WeComException("无效ID");
        }
        wxLxQrCodeVo.setQrCode(lxQrCode.getQrCode());
        //判断是否为新客
        int count = weCustomerService.count(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getUnionid, SecurityUtils.getWxLoginUser().getUnionId()));

        if (count > 0) {
            throw new WeComException("您已经是企业客户啦，暂不符合该拉新活动要求。");
        }

        return wxLxQrCodeVo;
    }

    @Override
    public void receiveAward(WxLxQrQuery query) throws Exception {
        WeLxQrCodeLog codeLog = new WeLxQrCodeLog();
        WeLxQrCode lxQrCode = getById(query.getQrId());
        if (Objects.isNull(lxQrCode)) {
            throw new WeComException("无效ID");
        }

        List<WeLxQrCodeLog> list = weLxQrCodeLogService.list(new LambdaQueryWrapper<WeLxQrCodeLog>()
                .eq(WeLxQrCodeLog::getQrId, query.getQrId())
                .eq(StringUtils.isNotEmpty(query.getOrderNo()),WeLxQrCodeLog::getOrderId,query.getOrderNo())
                .eq(WeLxQrCodeLog::getDelFlag, 0));
        if(CollectionUtil.isNotEmpty(list)){
            return;
        }

        if (ObjectUtil.equal(1, lxQrCode.getType())) {
            String businessData = lxQrCode.getBusinessData();
            JSONObject redInfo = JSONObject.parseObject(businessData);
            String money = redInfo.getString("money");
            codeLog.setAmount(new BigDecimal(money).multiply(new BigDecimal(100)).intValue());
            String returnMsg = weRedEnvelopesService.customerReceiveRedEnvelopes(query.getOrderNo()
                    , SecurityUtils.getWxLoginUser().getOpenId()
                    , SecurityUtils.getWxLoginUser().getNickName()
                    , SecurityUtils.getWxLoginUser().getHeadImgUrl());
            if (StringUtils.isNotEmpty(returnMsg)) {
                throw new WeComException(HttpStatus.NOT_IMPLEMENTED, returnMsg);
            }
        } else if (ObjectUtil.equal(2, lxQrCode.getType())) {
            WeCorpAccount weCorpAccount = weCorpAccountService.getCorpAccountByCorpId(null);
            //领取卡券
            WxSendCouponQuery couponQuery = new WxSendCouponQuery();
            couponQuery.setStock_id(lxQrCode.getBusinessId());
            //couponQuery.setStock_creator_mchid(wechatPayConfig.getMchId());
            couponQuery.setAppid(weCorpAccount.getWxAppId());
            couponQuery.setOpenid(SecurityUtils.getWxLoginUser().getOpenId());
            /*JSONObject jsonObject = weCouponService.sendCoupon(couponQuery);
            String couponId = jsonObject.getString("coupon_id");
            if (StringUtils.isNotBlank(couponId)) {
                JSONObject businessData = JSONObject.parseObject(lxQrCode.getBusinessData());
                businessData.put("couponId", couponId);
                update(new LambdaUpdateWrapper<WeLxQrCode>().set(WeLxQrCode::getBusinessData, businessData.toJSONString())
                        .eq(WeLxQrCode::getId, lxQrCode.getId()));
            }*/
        }

        codeLog.setQrId(query.getQrId());
        codeLog.setType(lxQrCode.getType());
        codeLog.setOrderId(query.getOrderNo());
        codeLog.setUnionId(SecurityUtils.getWxLoginUser().getUnionId());
        weLxQrCodeLogService.save(codeLog);

    }

    @Override
    public WeLxQrCodeReceiveVo receiveStatistics(WeLxQrCodeQuery query) {
        WeLxQrCodeReceiveVo weLxQrCodeReceiveVo = new WeLxQrCodeReceiveVo();

        List<WeLxQrCodeLog> logList = weLxQrCodeLogService.list(new LambdaQueryWrapper<WeLxQrCodeLog>()
                .eq(WeLxQrCodeLog::getQrId, query.getQrId())
                .eq(WeLxQrCodeLog::getDelFlag, 0));
        if (CollectionUtil.isNotEmpty(logList)) {
            List<WeLxQrCodeLog> todayList = logList.parallelStream().filter(codeLog -> ObjectUtil.equal(DateUtil.today(), DateUtil.formatDate(codeLog.getCreateTime()))).collect(Collectors.toList());
            List<WeLxQrCodeLog> tomorrowList = logList.parallelStream().filter(codeLog -> ObjectUtil.equal(DateUtil.tomorrow().toDateStr(), DateUtil.formatDate(codeLog.getCreateTime()))).collect(Collectors.toList());

            int totalAmount = logList.parallelStream().filter(item -> Objects.nonNull(item.getAmount())).mapToInt(WeLxQrCodeLog::getAmount).sum();
            int todayAmount = todayList.parallelStream().filter(item -> Objects.nonNull(item.getAmount())).mapToInt(WeLxQrCodeLog::getAmount).sum();
            int tomorrowAmount = tomorrowList.parallelStream().mapToInt(WeLxQrCodeLog::getAmount).sum();

            String totalAmountStr = new BigDecimal(totalAmount).divide(BigDecimal.valueOf(100L)).toString();
            String todayAmountStr = new BigDecimal(todayAmount).divide(BigDecimal.valueOf(100L)).toString();
            String tomorrowAmountStr = new BigDecimal(todayAmount - tomorrowAmount).divide(BigDecimal.valueOf(100L)).toString();


            weLxQrCodeReceiveVo.setTotalNum(logList.size());
            weLxQrCodeReceiveVo.setTodayNum(todayList.size());
            weLxQrCodeReceiveVo.setTotalAmount(totalAmountStr);
            weLxQrCodeReceiveVo.setTodayAmount(todayAmountStr);
            weLxQrCodeReceiveVo.setTodayNumDiff(todayList.size() - tomorrowList.size());
            weLxQrCodeReceiveVo.setTodayAmountDiff(tomorrowAmountStr);
        }

        return weLxQrCodeReceiveVo;
    }

    @Override
    public WeLxQrCodeReceiveLineVo receiveLineNum(WeLxQrCodeQuery query) {

        WeLxQrCodeReceiveLineVo lineVo = new WeLxQrCodeReceiveLineVo();

        List<String> xAxis = new LinkedList<>();
        List<String> yAxis = new LinkedList<>();

        LambdaQueryWrapper<WeLxQrCodeLog> wrapper = new LambdaQueryWrapper<WeLxQrCodeLog>()
                .eq(WeLxQrCodeLog::getQrId, query.getQrId())
                .eq(WeLxQrCodeLog::getDelFlag, 0);

        if (query.getBeginTime() != null) {
            wrapper.apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) >= '" + DateUtil.formatDate(query.getBeginTime()) + "'");
        }
        if (query.getEndTime() != null){
            wrapper .apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) <= '" + DateUtil.formatDate(query.getEndTime()) + "'");
        }

        List<WeLxQrCodeLog> logList = weLxQrCodeLogService.list(wrapper);

        Map<String, Long> dateMap = logList.parallelStream().collect(Collectors.groupingBy(item -> DateUtil.formatDate(item.getCreateTime()), Collectors.counting()));

        List<DateTime> timeList = DateUtil.rangeToList(query.getBeginTime(), query.getEndTime(), DateField.DAY_OF_YEAR);

        for (DateTime dateTime : timeList) {
            if (dateMap.containsKey(dateTime.toDateStr())) {
                Long num = dateMap.get(dateTime.toDateStr());
                xAxis.add(dateTime.toDateStr());
                yAxis.add(String.valueOf(num));
            } else {
                xAxis.add(dateTime.toDateStr());
                yAxis.add("0");
            }
        }
        lineVo.setXAxis(xAxis);
        lineVo.setYAxis(yAxis);
        return lineVo;
    }

    @Override
    public WeLxQrCodeReceiveLineVo receiveLineAmount(WeLxQrCodeQuery query) {
        WeLxQrCodeReceiveLineVo lineVo = new WeLxQrCodeReceiveLineVo();

        List<String> xAxis = new LinkedList<>();
        List<String> yAxis = new LinkedList<>();

        LambdaQueryWrapper<WeLxQrCodeLog> wrapper = new LambdaQueryWrapper<WeLxQrCodeLog>()
                .eq(WeLxQrCodeLog::getQrId, query.getQrId())
                .eq(WeLxQrCodeLog::getDelFlag, 0);

        if (query.getBeginTime() != null) {
            wrapper.apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) >= '" + DateUtil.formatDate(query.getBeginTime()) + "'");
        }
        if (query.getEndTime() != null){
            wrapper .apply("DATE_FORMAT(CREATE_TIME, '%Y-%m-%d' ) <= '" + DateUtil.formatDate(query.getEndTime()) + "'");
        }

        List<WeLxQrCodeLog> logList = weLxQrCodeLogService.list(wrapper);

        Map<String, Integer> amountMap = logList.parallelStream().collect(Collectors.groupingBy(item -> DateUtil.formatDate(item.getCreateTime()), Collectors.summingInt(WeLxQrCodeLog::getAmount)));

        List<DateTime> timeList = DateUtil.rangeToList(query.getBeginTime(), query.getEndTime(), DateField.DAY_OF_YEAR);

        for (DateTime dateTime : timeList) {
            if (amountMap.containsKey(dateTime.toDateStr())) {
                Integer amount = amountMap.get(dateTime.toDateStr());
                xAxis.add(dateTime.toDateStr());
                String amountStr = new BigDecimal(amount).divide(BigDecimal.valueOf(100L)).toString();
                yAxis.add(amountStr);
            } else {
                xAxis.add(dateTime.toDateStr());
                yAxis.add("0");
            }
        }
        lineVo.setXAxis(xAxis);
        lineVo.setYAxis(yAxis);
        return lineVo;
    }

    @Override
    public List<WeLxQrCodeReceiveListVo> receiveListStatistics(WeLxQrCodeQuery query) {

        List<WeLxQrCodeReceiveListVo> receiveList = weLxQrCodeLogService.receiveListStatistics(query);

        WeLxQrCodeReceiveVo totalData = weLxQrCodeLogService.receiveTotalStatistics(query);

        int totalNum = totalData.getTotalNum();
        int totalAmount = Integer.parseInt(totalData.getTotalAmount());

        for (WeLxQrCodeReceiveListVo receiveData : receiveList) {
            String todayAmount = receiveData.getTodayAmount();
            String todayAmountStr = new BigDecimal(todayAmount).divide(BigDecimal.valueOf(100L)).toString();
            receiveData.setTodayAmount(todayAmountStr);

            receiveData.setTotalNum(totalNum);
            totalNum = totalNum - receiveData.getTodayNum();

            String totalAmountStr = new BigDecimal(totalAmount).divide(BigDecimal.valueOf(100L)).toString();
            receiveData.setTotalAmount(totalAmountStr);

            totalAmount = totalAmount - Integer.parseInt(receiveData.getTotalAmount());
        }

        return receiveList;
    }

    @Override
    public Boolean checkIsReceive(WxLxQrQuery query) {
        List<WeLxQrCodeLog> list = weLxQrCodeLogService.list(new LambdaQueryWrapper<WeLxQrCodeLog>()
                .eq(WeLxQrCodeLog::getQrId, query.getQrId())
                .eq(StringUtils.isNotEmpty(query.getOrderNo()),WeLxQrCodeLog::getOrderId,query.getOrderNo())
                .eq(WeLxQrCodeLog::getUnionId, SecurityUtils.getWxLoginUser().getUnionId())
                .eq(WeLxQrCodeLog::getDelFlag, 0));
        if(CollectionUtil.isNotEmpty(list)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public H5RedEnvelopesDetailDto getReceiveList(WxLxQrQuery query) {
        return weRedEnvelopesService.detailDto(query.getOrderNo(), SecurityUtils.getWxLoginUser().getOpenId(), null);
    }


}
