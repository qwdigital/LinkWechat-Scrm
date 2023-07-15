package com.linkwechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.moments.entity.WeMomentsTask;
import com.linkwechat.domain.moments.query.WeMomentsTaskListRequest;
import com.linkwechat.domain.moments.vo.WeMomentsTaskVO;

import java.util.List;

/**
 * 朋友圈
 *
 * @author WangYX
 * @version 2.0.0
 * @date 2023/06/06 17:48
 */
public interface WeMomentsTaskMapper extends BaseMapper<WeMomentsTask> {

    /**
     * 朋友圈列表
     *
     * @param request
     * @return {@link List< WeMomentsTaskVO>}
     * @author WangYX
     * @date 2023/06/06 18:10
     */
    List<WeMomentsTaskVO> list(WeMomentsTaskListRequest request);

}
