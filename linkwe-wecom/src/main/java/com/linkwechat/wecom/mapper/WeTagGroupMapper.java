package com.linkwechat.wecom.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.wecom.domain.WeTagGroup;
import org.apache.ibatis.annotations.Param;

/**
 * 标签组Mapper接口
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public interface WeTagGroupMapper  extends BaseMapper<WeTagGroup>
{
    /**
     * 查询标签组ID集合查询标签
     * 
     * @param ids 标签组ID集合
     * @return 标签组
     */
    public List<WeTagGroup> getWeTagGroupByIds(Long[] ids);

    /**
     * 查询标签组列表
     * 
     * @param weTagGroup 标签组
     * @return 标签组集合
     */
    public List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup);

    /**
     * 新增标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    public int insertWeTagGroup(WeTagGroup weTagGroup);

    /**
     * 修改标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    public int updateWeTagGroup(WeTagGroup weTagGroup);


    /**
     * 批量逻辑删除标签组
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteWeTagGroupByIds(String[] ids);


    /**
     * 批量保存标签组
     * @param weTagGroups
     * @return
     */
    public int batchInsetWeTagGroup(@Param("weTagGroups") List<WeTagGroup> weTagGroups);

//
//    /**
//     * 根据企业员工与添加客户关系id给客户打标签
//     * @param externalUserid
//     * @param userid
//     * @return
//     */
//    public List<WeTagGroup> findCustomerTagByFlowerCustomerRelId(@Param("externalUserid") String externalUserid, @Param("userid") String userid);
//



}
