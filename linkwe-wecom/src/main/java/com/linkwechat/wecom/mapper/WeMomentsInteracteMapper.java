package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeMomentsInteracte;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeMomentsInteracteMapper extends BaseMapper<WeMomentsInteracte> {

    void batchAddOrUpdate(@Param("weMomentsInteracte") List<WeMomentsInteracte> weMomentsInteracteList);

}
