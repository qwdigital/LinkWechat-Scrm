package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.fission.WeFission;
import com.linkwechat.domain.fission.vo.*;
import com.linkwechat.service.IWeFissionService;
import com.linkwechat.mapper.WeFissionMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author robin
* @description 针对表【we_fission(裂变（任务宝,群裂变）)】的数据库操作Service实现
* @createDate 2023-03-14 14:07:21
*/
@Service
public class WeFissionServiceImpl extends ServiceImpl<WeFissionMapper, WeFission>
    implements IWeFissionService {

    @Override
    public List<WeFission> findWeFissions(WeFission weFission) {
        List<WeFission> weFissions = this.baseMapper.findWeFissions(new LambdaQueryWrapper<WeFission>()
                .like(StringUtils.isNotEmpty(weFission.getFassionName()),WeFission::getFassionName,weFission.getFassionName())
                .eq(weFission.getFassionType() != null,WeFission::getFassionType,weFission.getFassionType())
                .eq(weFission.getFassionState() != null,WeFission::getFassionState,weFission.getFassionState()));
        return weFissions;
    }

    @Override
    public WeFissionTabVo findWeFissionTab(Long fissionId) {
        return this.baseMapper.findWeFissionTab(fissionId);
    }

    @Override
    public List<WeFissionTrendVo> findWeFissionTrend(WeFission weFission) {
        return this.baseMapper.findWeFissionTrend(weFission);
    }

    @Override
    public List<WeFissionDataReportVo> findWeFissionDataReport(WeFission weFission) {
        return this.baseMapper.findWeFissionDataReport(weFission);
    }

    @Override
    public List<WeFissionDetailVo> findWeFissionDetail(String customerName,String weUserId) {
        return this.baseMapper.findWeFissionDetail(customerName, weUserId);
    }

    @Override
    public List<WeFissionDetailSubVo> findWeFissionDetailSub(Long fissionDetailId) {
        return this.baseMapper.findWeFissionDetailSub(fissionDetailId);
    }
}




