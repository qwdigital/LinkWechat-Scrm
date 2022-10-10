package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.WeCustomerTrackRecord;
import com.linkwechat.mapper.WeCustomerTrackRecordMapper;
import com.linkwechat.service.IWeCustomerTrackRecordService;
import org.springframework.stereotype.Service;

@Service
public class WeCustomerTrackRecordServiceImpl extends ServiceImpl<WeCustomerTrackRecordMapper, WeCustomerTrackRecord> implements IWeCustomerTrackRecordService {
}
