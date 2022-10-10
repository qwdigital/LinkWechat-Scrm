package com.linkwechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.mapper.WeGroupMessageListMapper;
import com.linkwechat.domain.WeGroupMessageList;
import com.linkwechat.service.IWeGroupMessageListService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 群发消息列表(WeGroupMessageList)
 *
 * @author danmo
 * @since 2022-04-06 22:29:04
 */
@Slf4j
@Service
public class WeGroupMessageListServiceImpl extends ServiceImpl<WeGroupMessageListMapper, WeGroupMessageList> implements IWeGroupMessageListService {

}
