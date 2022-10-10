package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.envelopes.WeRedEnvelopesRecord;
import com.linkwechat.domain.envelopes.dto.H5RedEnvelopesDetailDto;
import com.linkwechat.domain.envelopes.vo.WeCutomerRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeGroupRedEnvelopesVo;
import com.linkwechat.domain.envelopes.vo.WeRedEnvelopesCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeRedEnvelopesRecordMapper extends BaseMapper<WeRedEnvelopesRecord> {

    List<WeCutomerRedEnvelopesVo> findRedEnveForUser(WeRedEnvelopesRecord redEnvelopesRecord);


    List<WeGroupRedEnvelopesVo> findRedEnveForGroup(WeRedEnvelopesRecord redEnvelopesRecord);

    List<WeCutomerRedEnvelopesVo> findRedEnveForGroupUser(@Param("chatId") String chatId, @Param("orderNo") String orderNo);

    WeRedEnvelopesCountVo countTab();

    List<WeRedEnvelopesCountVo> countLineChart(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("dates") List<String> dates);


    List<WeRedEnvelopesCountVo> findRecordGroupByUserId(@Param("startTime") String startTime,@Param("endTime") String endTime);


    int findAccpectMoney(@Param("orderNo")String orderNo);

    List<H5RedEnvelopesDetailDto.AccpestCustomer> findAccpectCustomers(@Param("orderNo") String orderNo);

}
