package com.linkwechat.wecom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeGroupSopPic;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 图片语sop规则绑定信息 mapper
 */
@Mapper
@Repository
public interface WeGroupSopPicMapper extends BaseMapper<WeGroupSopPic> {

    /**
     * 批量保存群SOP和图片
     *
     * @param sopPicList WeGroupSopPic对象列表
     * @return 结果
     */
    int batchSopPic(List<WeGroupSopPic> sopPicList);
}
