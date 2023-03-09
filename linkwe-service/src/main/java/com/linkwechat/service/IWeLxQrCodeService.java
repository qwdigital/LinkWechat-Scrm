package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.WeLxQrCode;
import com.linkwechat.domain.qr.query.WeLxQrAddQuery;
import com.linkwechat.domain.qr.query.WeLxQrCodeListQuery;
import com.linkwechat.domain.qr.vo.WeLxQrAddVo;
import com.linkwechat.domain.qr.vo.WeLxQrCodeDetailVo;
import com.linkwechat.domain.qr.vo.WeQrCodeDetailVo;
import com.linkwechat.domain.qr.vo.WeQrCodeScanCountVo;

import java.util.List;

/**
 * 拉新活码信息表(WeLxQrCode)
 *
 * @author danmo
 * @since 2023-03-07 14:59:35
 */
public interface IWeLxQrCodeService extends IService<WeLxQrCode> {

    /**
     * 新增拉新活码
     * @param weQrAddQuery 入参
     */
    Long addQrCode(WeLxQrAddQuery weQrAddQuery);

    /**
     * 生成拉新二维码
     * @param id
     * @return
     */
    WeLxQrAddVo generateQrCode(Long id);

    /**
     * 修改拉新活码
     * @param query
     */
    void updateQrCode(WeLxQrAddQuery query);

    /**
     * 删除拉新活码
     * @param ids 活码ID
     */
    void delQrCode(List<Long> ids);

    /**
     * 查询拉新活码详情
     * @param id 活码ID
     * @return
     */
    WeLxQrCodeDetailVo getQrDetail(Long id);

    /**
     * 获取拉新活码列表
     * @param query
     * @return
     */
    List<WeQrCodeDetailVo> getQrCodeList(WeLxQrCodeListQuery query);

    /**
     * 统计拉新活码
     * @param query
     * @return
     */
    WeQrCodeScanCountVo getLxQrCodeScanCount(WeLxQrCodeListQuery query);
}
