package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工活码标签Service接口
 *
 * @author ruoyi
 * @date 2020-10-04
 */
public interface IWeEmpleCodeTagService extends IService<WeEmpleCodeTag>
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
     * @param empleCodeId 员工活码id
     * @return 员工活码标签集合
     */
    public List<WeEmpleCodeTag> selectWeEmpleCodeTagListById(Long empleCodeId);

    /**
     * 查询员工活码标签列表
     *
     * @param empleCodeIdList 员工活码ids
     * @return 员工活码标签集合
     */
    List<WeEmpleCodeTag> selectWeEmpleCodeTagListByIds(List<Long> empleCodeIdList);

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
     * 批量删除员工活码标签
     *
     * @param ids 需要删除的员工活码标签ID
     * @return 结果
     */
    public int deleteWeEmpleCodeTagByIds(Long[] ids);

    /**
     * 删除员工活码标签信息
     *
     * @param id 员工活码标签ID
     * @return 结果
     */
    public int deleteWeEmpleCodeTagById(Long id);

    /**
     * 批量保存标签
     * @param weEmpleCodeTags
     * @return
     */
    public int batchInsetWeEmpleCodeTag(List<WeEmpleCodeTag> weEmpleCodeTags);


    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    public int batchRemoveWeEmpleCodeTagIds(List<Long> ids);


}
