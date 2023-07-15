package com.linkwechat.mapper;

import com.linkwechat.domain.WeShortLinkPromotion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionVo;

import java.util.List;

/**
 * <p>
 * 短链推广 Mapper 接口
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
public interface WeShortLinkPromotionMapper extends BaseMapper<WeShortLinkPromotion> {

    /**
     * 短链推广列表
     *
     * @param query 查询条件
     * @return {@link List <  WeShortLinkPromotionVo >}
     * @author WangYX
     * @date 2023/03/07 15:41
     */
    List<WeShortLinkPromotionVo> selectList(WeShortLinkPromotionQuery query);

}
