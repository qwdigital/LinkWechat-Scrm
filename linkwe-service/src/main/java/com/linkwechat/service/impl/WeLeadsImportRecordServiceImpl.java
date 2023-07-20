package com.linkwechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.leads.leads.entity.WeLeadsImportRecord;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportRecordVO;
import com.linkwechat.mapper.WeLeadsImportRecordMapper;
import com.linkwechat.service.IWeLeadsImportRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 线索导入记录 服务实现类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:57
 */
@Slf4j
@Service
public class WeLeadsImportRecordServiceImpl extends ServiceImpl<WeLeadsImportRecordMapper, WeLeadsImportRecord> implements IWeLeadsImportRecordService {

    @Override
    public List<WeLeadsImportRecordVO> importRecord(String name) {
        return this.baseMapper.importRecord(name);
    }
}
