package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeErrorMsg;
import com.linkwechat.service.IWeErrorMsgService;
import com.linkwechat.mapper.WeErrorMsgMapper;
import org.springframework.stereotype.Service;

/**
* @author robin
* @description 针对表【we_error_msg(企业微信响应错误参数记录表)】的数据库操作Service实现
* @createDate 2023-08-23 17:36:43
*/
@Service
public class WeErrorMsgServiceImpl extends ServiceImpl<WeErrorMsgMapper, WeErrorMsg>
    implements IWeErrorMsgService {

}




