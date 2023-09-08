package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeFlowUserList;
import com.linkwechat.service.IWeFlowUserListService;
import com.linkwechat.mapper.WeFlowUserListMapper;
import org.springframework.stereotype.Service;

/**
* @author robin
* @description 针对表【we_flow_user_list(具有外部联系人功能员工记录表)】的数据库操作Service实现
* @createDate 2023-09-08 13:05:07
*/
@Service
public class WeFlowUserListServiceImpl extends ServiceImpl<WeFlowUserListMapper, WeFlowUserList>
    implements IWeFlowUserListService {

}




