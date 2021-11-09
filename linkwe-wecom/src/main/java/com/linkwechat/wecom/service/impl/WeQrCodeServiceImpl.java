package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.WeQrScope;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.domain.query.qr.WeQrAddQuery;
import com.linkwechat.wecom.domain.query.qr.WeQrUserInfoQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrScopeVo;
import com.linkwechat.wecom.domain.vo.tag.WeTagVo;
import com.linkwechat.wecom.mapper.WeQrCodeMapper;
import com.linkwechat.wecom.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    private IWeTagService weTagService;

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
        }
    }

    @Override
    public WeQrCodeDetailVo getQrDetail(Long qrId) {
        return this.baseMapper.getQrDetailByQrId(qrId);
    }


    @Override
    public void delQrCode(List<Long> qrIds) {
        List<WeQrCode> weQrCodes = this.listByIds(qrIds);
        if(CollectionUtil.isNotEmpty(weQrCodes)){
            weQrCodes.forEach(item -> item.setDelFlag(1));
            if(this.updateBatchById(weQrCodes)){
                //删除标签数据
                tagRelService.delBatchByQrIds(qrIds);
                //删除活码范围数据
                scopeService.delBatchByQrIds(qrIds);
                //删除活码素材
                attachmentsService.delBatchByQrIds(qrIds);
            }
            //异步删除企微活码---最好使用mq
            ThreadUtil.execute(() -> weQrCodes.forEach(item ->{
                if(StringUtils.isNotEmpty(item.getConfigId())){
                    externalContactClient.delContactWay(item.getConfigId());
                }
            }));
        }
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
                    long partySum = qrUserInfoList.get(i).getPartys().stream()
                            .filter(one -> qrUserInfoList.get(finalJ).getPartys().stream()
                                    .anyMatch(two -> ObjectUtil.equal(two, one))).count();
                    if (userSum > 0 || partySum > 0) {
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
