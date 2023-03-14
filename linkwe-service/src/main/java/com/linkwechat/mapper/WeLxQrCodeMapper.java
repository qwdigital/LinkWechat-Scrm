package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.linkwechat.domain.qr.query.WeLxQrCodeListQuery;
import com.linkwechat.domain.qr.vo.WeLxQrCodeDetailVo;
import com.linkwechat.domain.qr.vo.WeLxQrCodeSheetVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeLxQrCode;

/**
 * 拉新活码信息表(WeLxQrCode)
 *
 * @author danmo
 * @since 2023-03-07 14:59:35
 */
@Repository()
@Mapper
public interface WeLxQrCodeMapper extends BaseMapper<WeLxQrCode> {


    WeLxQrCodeDetailVo getQrDetail(@Param("id") Long id);


    List<WeLxQrCodeSheetVo> getWeQrCodeListStatistics(WeLxQrCodeListQuery query);

    WeLxQrCodeDetailVo getQrDetailByState(@Param("state") String state);
}

