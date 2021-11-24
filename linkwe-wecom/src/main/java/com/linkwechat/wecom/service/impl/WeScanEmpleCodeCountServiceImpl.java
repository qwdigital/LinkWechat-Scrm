package com.linkwechat.wecom.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeScanEmpleCodeCount;
import com.linkwechat.wecom.mapper.WeScanEmpleCodeCountMapper;
import com.linkwechat.wecom.service.IWeScanEmpleCodeCountService;
import org.springframework.stereotype.Service;

@Service
public class WeScanEmpleCodeCountServiceImpl extends ServiceImpl<WeScanEmpleCodeCountMapper, WeScanEmpleCodeCount>
implements IWeScanEmpleCodeCountService {
}
