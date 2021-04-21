package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 员工活码标签Mapper接口
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Repository
public interface WeEmpleCodeTagMapper extends BaseMapper<WeEmpleCodeTag>
{
    /**
     * 查询员工活码标签
     * 
     * @param id 员工活码标签ID
     * @return 员工活码标签
     */
    public WeEmpleCodeTag selectWeEmpleCodeTagById(Long id);

    /**
     * 查询员工活码标签列表
     * 
     * @param empleCodeId 员工活码Id
     * @return 员工活码标签集合
     */
    public List<WeEmpleCodeTag> selectWeEmpleCodeTagListById(@Param("empleCodeId") Long empleCodeId);

    /**
     * 查询员工活码标签列表
     *
     * @param empleCodeIdList 员工活码Ids
     * @return 员工活码标签集合
     */
    List<WeEmpleCodeTag> selectWeEmpleCodeTagListByIds(@Param("empleCodeIdList") List<Long> empleCodeIdList);

    /**
     * 新增员工活码标签
     * 
     * @param weEmpleCodeTag 员工活码标签
     * @return 结果
     */
    public int insertWeEmpleCodeTag(WeEmpleCodeTag weEmpleCodeTag);

    /**
     * 修改员工活码标签
     * 
     * @param weEmpleCodeTag 员工活码标签
     * @return 结果
     */
    public int updateWeEmpleCodeTag(WeEmpleCodeTag weEmpleCodeTag);

    /**
     * 删除员工活码标签
     * 
     * @param id 员工活码标签ID
     * @return 结果
     */
    public int deleteWeEmpleCodeTagById(Long id);

    /**
     * 批量删除员工活码标签
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeEmpleCodeTagByIds(Long[] ids);


    /**
     * 批量保存标签
     * @param weEmpleCodeTags
     * @return
     */
    public int batchInsetWeEmpleCodeTag(@Param("weEmpleCodeTags") List<WeEmpleCodeTag> weEmpleCodeTags);


    /**
     * 批量逻辑删除
     * @param ids 员工活码id列表
     * @return
     */
    public int batchRemoveWeEmpleCodeTagIds(@Param("ids") List<Long> ids);


}
