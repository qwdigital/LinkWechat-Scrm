package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.query.qr.WeQrAddQuery;
import com.linkwechat.wecom.domain.query.qr.WeQrCodeListQuery;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeScanCountVo;

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
     * 修改员工活码
     * @param weQrAddQuery 入参
     */
    void updateQrCode(WeQrAddQuery weQrAddQuery);

    /**
     * 获取活码详情
     * @param qrId 活码id
     * @return
     */
    WeQrCodeDetailVo getQrDetail(Long qrId);

    /**
     * 获取活码详情
     * @param qrIds 活码id
     * @return
     */
    List<WeQrCodeDetailVo> getQrDetailByQrIds(List<Long> qrIds);

    /**
     * 获取活码列表
     * @return list
     */
    PageInfo<WeQrCodeDetailVo> getQrCodeList(WeQrCodeListQuery qrCodeListQuery);

    /**
     * 删除活码
     */
    void delQrCode(List<Long> qrIds);

    /**
     * 获取活码统计
     * @param qrCodeListQuery 入参
     * @return WeQrCodeScanCountVo
     */
    WeQrCodeScanCountVo getWeQrCodeScanCount(WeQrCodeListQuery qrCodeListQuery);

}
