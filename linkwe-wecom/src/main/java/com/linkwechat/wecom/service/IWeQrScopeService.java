package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.query.qr.WeQrUserInfoQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.domain.WeQrScope;

import java.util.List;

/**
 * 活码使用范围表(WeQrScope)表服务接口
 *
 * @author makejava
 * @since 2021-11-07 01:29:14
 */
public interface IWeQrScopeService extends IService<WeQrScope> {

    /**
     * 通过活码id批量保存适用范围
     * @param qrId  活码id
     * @param qrUserInfos 适用范围列表
     */
    void saveBatchByQrId(Long qrId, List<WeQrUserInfoQuery> qrUserInfos);

    /**
     * 通过活码id批量删除适用范围
     * @param qrIds 活码id
     * @return
     */
    Boolean delBatchByQrIds(List<Long> qrIds);
}
