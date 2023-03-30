package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.live.WeLiveWatchUser;
import com.linkwechat.mapper.WeLiveWatchUserMapper;
import com.linkwechat.service.IWeLiveWatchUserService;
import org.springframework.stereotype.Service;

/**
* @author robin
* @description 针对表【we_live_watch_user(观看成员)】的数据库操作Service实现
* @createDate 2022-11-07 13:49:15
*/
@Service
public class WeLiveWatchUserServiceImpl extends ServiceImpl<WeLiveWatchUserMapper, WeLiveWatchUser>
implements IWeLiveWatchUserService {

}
