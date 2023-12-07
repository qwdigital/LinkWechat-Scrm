package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.common.config.mybatis.LwBaseMapper;
import com.linkwechat.domain.moments.entity.WeMomentsUser;
import com.linkwechat.domain.moments.query.WeMomentsTaskMobileRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskMobileVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 朋友圈员工 Mapper 接口
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/07 9:58
 */
public interface WeMomentsUserMapper extends LwBaseMapper<WeMomentsUser> {


    /**
     * 移动端列表
     *
     * @param request 移动端列表查询参数
     * @return {@link String}
     * @author WangYX
     * @date 2023/06/20 18:44
     */
    List<WeMomentsTaskMobileVO> mobileList(WeMomentsTaskMobileRequest request);

    /**
     * 数量
     *
     * @param request 移动端列表查询参数
     * @return {@link int}
     * @author WangYX
     * @date 2023/06/26 18:53
     */
    int count(WeMomentsTaskMobileRequest request);


    /**
     * 移动端详情
     *
     * @param weUserId        企微员工Id
     * @param weMomentsTaskId 朋友圈任务Id
     * @return {@link WeMomentsTaskMobileVO}
     * @author WangYX
     * @date 2023/06/21 9:59
     */
    WeMomentsTaskMobileVO mobileGet(@Param("weUserId") String weUserId, @Param("weMomentsTaskId") Long weMomentsTaskId);

}
