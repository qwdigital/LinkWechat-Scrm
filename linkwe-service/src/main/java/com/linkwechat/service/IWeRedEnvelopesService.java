package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.envelopes.WeRedEnvelopes;
import com.linkwechat.domain.envelopes.WeRedEnvelopesLimit;
import com.linkwechat.domain.envelopes.WeRedEnvelopesRecord;
import com.linkwechat.domain.envelopes.dto.H5RedEnvelopesDetailDto;
import com.linkwechat.domain.envelopes.query.H5RedEnvelopesParmQuery;
import com.linkwechat.domain.envelopes.query.WeRedEnvelopeListQuery;
import com.linkwechat.domain.envelopes.vo.WeCutomerRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeGroupRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeRedEnvelopesCountVo;

import java.util.List;

public interface IWeRedEnvelopesService extends IService<WeRedEnvelopes> {

    /**
     * 获取企业红包限额
     * @return
     */
    WeRedEnvelopesLimit findLimitRedEnvelopes();

    /**
     * 设置企业红包限额
     * @param envelopesLimit
     */
    void limitRedEnvelopes(WeRedEnvelopesLimit envelopesLimit);


    /**
     * 发放记录-发放客户
     * @param redEnvelopesRecord
     * @return
     */
    List<WeCutomerRedEnvelopesVo> findRedEnveForUser(WeRedEnvelopesRecord redEnvelopesRecord);


    /**
     *  发放记录-发放群
     * @param redEnvelopesRecord
     * @return
     */
    List<WeGroupRedEnvelopesVo> findRedEnveForGroup(WeRedEnvelopesRecord redEnvelopesRecord);



    /**
     * 支出统计-折线图
     * @param startTime
     * @param endTime
     * @return
     */
    List<WeRedEnvelopesCountVo> countLineChart(String startTime,String endTime);



    /**
     * 群红包详情列表
     * @param chatId
     * @return
     */
    List<WeCutomerRedEnvelopesVo> findRedEnveForGroupUser(String chatId,String orderNo);


    /**
     * 支出统计头部统计
     * @return
     */
    WeRedEnvelopesCountVo countTab();




    /**
     * 支出统计-支出记录
     * @return
     */
    List<WeRedEnvelopesCountVo> findRecordGroupByUserId(String startTime,String endTime);





    /**
     * 生成客户红包订单
     * @param redEnvelopeAmount
     * @param redEnvelopeName
     * @param  redEnvelopeNum
     * @param sendUserId
     * @return 订单号
     */
    String createCustomerRedEnvelopesOrder(String redenvelopesId,int redEnvelopeAmount,String redEnvelopeName,Integer redEnvelopeNum,String sendUserId,Integer fromType,String externalUserId);


    /**
     *  校验个人红包限额
     * @param sendUserId
     * @param redEnvelopeAmount
     * @return
     */
    String checkWeUserQuotaLimit(String sendUserId, int redEnvelopeAmount);


    /**
     * 检查红包总体限额
     * @return
     */
    String checkRedEnvelopesTplLimit();



    /**
     *  生成群红包订单
     * @param redEnvelopeAmount
     * @param redEnvelopeName
     * @param redEnvelopeNum
     * @param chatId
     * @param sendUserId
     * @param redEnvelopesType
     * @return
     */
    String createGroupRedEnvelopesOrder(String redenvelopesId,int redEnvelopeAmount,String redEnvelopeName,Integer redEnvelopeNum,String chatId,String sendUserId,Integer redEnvelopesType,Integer fromType);



    /**
     * 校验客户是否限额
     * @param officialAccountOpenId
     * @return
     */
    String checkCustomerRedEnvelopesLimit(String officialAccountOpenId);



    /**
     * 客户领取红包
     * @param orderNo
     * @param officialAccountOpenId 公众号的openid
     */
    String customerReceiveRedEnvelopes(String orderNo,String officialAccountOpenId,String receiveName,String avatar) throws Exception;


    /**
     * 红包领取列表
     * @param orderNo
     * @param openid
     * @param chatId
     * @return
     */
    H5RedEnvelopesDetailDto detailDto(String orderNo, String openid, String chatId);


    /**
     * 校验客户是否领取过红包
     * @param orderNo
     * @param openid
     * @return
     */
    Boolean checkRecevieRedEnvelopes(String orderNo, String openid);

    /**
     * 查询红包列表
     * @param query
     * @return
     */
    List<WeRedEnvelopes> getList(WeRedEnvelopeListQuery query);
}
