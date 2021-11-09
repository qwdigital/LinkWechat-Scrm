package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.query.qr.WeQrAddQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;

import java.util.List;

/**
 * 活码信息表(WeQrCode)$desc
 *
 * @author danmo
 * @since 2021-11-07 02:10:51
 */
public interface IWeQrCodeService extends IService<WeQrCode> {

    /**
     * 新增员工活码
     * @param weQrAddQuery 入参
     */
    void addQrCode(WeQrAddQuery weQrAddQuery);

    /**
     * 获取活码详情
     * @param qrId
     * @return
     */
    WeQrCodeDetailVo getQrDetail(Long qrId);

    /**
     * 删除活码
     */
    void delQrCode(List<Long> qrIds);
} 
