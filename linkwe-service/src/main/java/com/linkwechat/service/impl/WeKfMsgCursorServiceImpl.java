package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeKfMsgCursor;
import com.linkwechat.mapper.WeKfMsgCursorMapper;
import com.linkwechat.service.IWeKfMsgCursorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 客服消息偏移量表(WeKfMsgCursor)
 *
 * @author danmo
 * @since 2022-04-15 15:53:37
 */
@Service
public class WeKfMsgCursorServiceImpl extends ServiceImpl<WeKfMsgCursorMapper, WeKfMsgCursor> implements IWeKfMsgCursorService {

    @Autowired
    private RedisService redisService;

    private final String kfMsgCursorKey = "we:kf:session:cursor:{}";

    @Override
    public String getKfMsgCursor(String corpId) {
        String key = StringUtils.format(kfMsgCursorKey, corpId);
        String nextCursor = (String) redisService.getCacheObject(key);
        if(StringUtils.isEmpty(nextCursor)){
            WeKfMsgCursor weKfMsgCursor = getOne(new LambdaQueryWrapper<WeKfMsgCursor>().eq(WeKfMsgCursor::getCorpId,corpId).last("limit 1"));
            if(weKfMsgCursor != null){
                nextCursor = weKfMsgCursor.getNextCursor();
                redisService.setCacheObject(key,nextCursor);
            }
        }
        return nextCursor;
    }

    @Override
    public void saveKfMsgCursor(String corpId, String nextCursor) {
        String key = StringUtils.format(kfMsgCursorKey, corpId);
        redisService.setCacheObject(key,nextCursor);
        WeKfMsgCursor weKfMsgCursor = new WeKfMsgCursor();
        weKfMsgCursor.setNextCursor(nextCursor);
        saveOrUpdate(weKfMsgCursor,new LambdaQueryWrapper<WeKfMsgCursor>().eq(WeKfMsgCursor::getCorpId,corpId).eq(WeKfMsgCursor::getDelFlag,0));
    }
}
