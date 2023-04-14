package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.fission.WeFissionNotice;
import com.linkwechat.mapper.WeFissionNoticeMapper;
import com.linkwechat.service.IWeFissionNoticeService;
import org.springframework.stereotype.Service;

/**
* @author robin
* @description 针对表【we_fission_detail(裂变明细表)】的数据库操作Service实现
* @createDate 2023-03-14 14:07:21
*/
@Service
public class WeFissionNoticeServiceImpl extends ServiceImpl<WeFissionNoticeMapper, WeFissionNotice>
    implements IWeFissionNoticeService {

    @Override
    public void physicalDelete(Long fissionId) {
        this.baseMapper.physicalDelete(fissionId);
    }
}




