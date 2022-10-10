package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.qr.WeQrScope;
import com.linkwechat.domain.qr.vo.WeQrScopeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 活码使用范围表(WeQrScope)Mapper接口
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
@Repository()
@Mapper
public interface WeQrScopeMapper extends BaseMapper<WeQrScope> {


    List<WeQrScopeVo> getWeQrScopeByQrIds(@Param("qrIds") List<Long> qrIds);

    List<WeQrScopeVo> getWeQrScopeByTime(@Param("formatTime") String formatTime,@Param("qrId") String qrId);

    
    void updateScopeStatusByQrId(@Param("qrId") Long qrId, @Param("scopeId") String scopeId);
}

