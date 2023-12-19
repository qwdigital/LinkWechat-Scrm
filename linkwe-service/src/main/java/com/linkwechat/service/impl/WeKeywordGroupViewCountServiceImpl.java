package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeKeywordGroupViewCount;
import com.linkwechat.service.IWeKeywordGroupViewCountService;
import com.linkwechat.mapper.WeKeywordGroupViewCountMapper;
import org.springframework.stereotype.Service;

/**
* @author robin
* @description 针对表【we_keyword_group_view_count(关键词群访问统计)】的数据库操作Service实现
* @createDate 2023-12-19 12:33:29
*/
@Service
public class WeKeywordGroupViewCountServiceImpl extends ServiceImpl<WeKeywordGroupViewCountMapper, WeKeywordGroupViewCount>
    implements IWeKeywordGroupViewCountService {

}




