package com.linkwechat.wecom.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeQrCode;
import com.linkwechat.wecom.domain.vo.qr.WeQrCodeDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 活码信息表(WeQrCode)
 *
 * @author danmo
 * @since 2021-11-07 02:30:11
 */
@Repository()
@Mapper
public interface WeQrCodeMapper extends BaseMapper<WeQrCode> {

    /**
     * 根据活码id查询详情
     * @param qrId 活码id
     * @return
     */
    WeQrCodeDetailVo getQrDetailByQrId(Long qrId);

    /**
     * 根据活码id批量查询详情
     * @param qrIds 活码id
     * @return
     */
    List<WeQrCodeDetailVo> getQrDetailByQrIds(List<Long> qrIds);
}

