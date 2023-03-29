package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeLxQrScope;
import com.linkwechat.domain.qr.query.WeLxQrUserInfoQuery;

import java.util.List;

/**
 * 拉新活码使用范围表(WeLxQrScope)
 *
 * @author danmo
 * @since 2023-03-07 15:06:04
 */
public interface IWeLxQrScopeService extends IService<WeLxQrScope> {

    void saveBatchByQrId(Long qrId, List<WeLxQrUserInfoQuery> qrUserInfos);

    void updateBatchByQrId(Long qrId, List<WeLxQrUserInfoQuery> qrUserInfos);

    void delBatchByQrIds(List<Long> qrIds);
}
