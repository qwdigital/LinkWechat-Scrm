package com.linkwechat.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.WeTagGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeTagGroupMapper  extends BaseMapper<WeTagGroup> {
    /**
     * 查询标签组列表
     *
     * @param weTagGroup 标签组
     * @return 标签组集合
     */
//    @DataScope(type = "2", value = @DataColumn(alias = "wtg", name = "create_by_id", userid = "user_id"))
    List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup);


    /**
     * 标签批量新增或更新
     * @param weTagGroups
     */
    void batchAddOrUpdate(@Param("weTagGroups") List<WeTagGroup> weTagGroups);

    List<Long> getTagGroupIds(WeTagGroup tagGroup);

    List<WeTagGroup> getTagGroupList(WeTagGroup tagGroup);
}
