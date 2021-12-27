package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.WePoster;
import com.linkwechat.wecom.domain.WePosterFont;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 海报
 * @author ws
 */
public interface WePosterMapper extends BaseMapper<WePoster> {
    List<WeMaterial> findWePosterToWeMaterial(@Param("categoryId") String categoryId,@Param("name") String name,@Param("status") Integer status);
}
