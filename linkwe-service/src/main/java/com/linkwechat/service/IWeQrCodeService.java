package com.linkwechat.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.linkwechat.domain.qr.WeQrCode;
import com.linkwechat.domain.qr.query.WeQrAddQuery;
import com.linkwechat.domain.qr.query.WeQrCodeListQuery;
import com.linkwechat.domain.qr.vo.WeQrCodeDetailVo;
import com.linkwechat.domain.qr.vo.WeQrCodeScanCountVo;
import com.linkwechat.domain.qr.vo.WeQrCodeScanLineCountVo;
import com.linkwechat.domain.qr.vo.WeQrScopeVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;

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

    /**
     * 活码变更任务
     * @param qrCodeId
     */
    public void qrCodeUpdateTask(Long qrCodeId);

    /**
     * 查询当前时间使用范围活码数据
     * @return
     */
    List<WeQrScopeVo> getWeQrScopeByTime(String formatTime, Long qrId);

    /**
     * 删除活码分组
     * @param groupId 活码分组ID
     */
    void deleteQrGroup(Long groupId);


    /**
     * 根据员工id创建活码
     * @param weUserIds 员工id
     * @param state 活码标识
     * @return
     */
    WeAddWayVo createQrbyWeUserIds(List<String> weUserIds, String state);



    /**
     * 更新员工活码
     * @param weUserIds 员工id
     * @param  configId 活码配置id如果生成了下次更新需要
     * @return
     */
    void updateQrbyWeUserIds(List<String> weUserIds, String configId);

    /**
     * 更新多人员工活码
     * @param state  渠道,当前用户通过哪个活码添加
     */
    void updateQrMultiplePeople(String state);

    JSONObject getShort2LongUrl(String shortUrl);


    WeQrCodeScanCountVo getWeQrCodeScanTotalCount(WeQrCodeListQuery qrCodeListQuery);

    List<WeQrCodeScanLineCountVo> getWeQrCodeScanLineCount(WeQrCodeListQuery qrCodeListQuery);

    List<WeQrCodeScanLineCountVo> getWeQrCodeScanSheetCount(WeQrCodeListQuery qrCodeListQuery);
}
