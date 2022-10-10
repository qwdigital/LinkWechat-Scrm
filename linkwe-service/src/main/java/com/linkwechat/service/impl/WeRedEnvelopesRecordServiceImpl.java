package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.envelopes.WeRedEnvelopesRecord;
import com.linkwechat.mapper.WeRedEnvelopesRecordMapper;
import com.linkwechat.service.IWeRedEnvelopesRecordService;
import org.springframework.stereotype.Service;

@Service
public class WeRedEnvelopesRecordServiceImpl extends ServiceImpl<WeRedEnvelopesRecordMapper,WeRedEnvelopesRecord>
        implements IWeRedEnvelopesRecordService {

}
