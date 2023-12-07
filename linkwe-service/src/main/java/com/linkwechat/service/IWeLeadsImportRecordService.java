package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.leads.leads.entity.WeLeadsImportRecord;
import com.linkwechat.domain.leads.leads.vo.WeLeadsImportRecordVO;

import java.util.List;

/**
 * 线索导入记录 服务类
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/12 9:54
 */
public interface IWeLeadsImportRecordService extends IService<WeLeadsImportRecord> {

    /**
     * 线索导入记录
     *
     * @param name 导入表格名称
     * @return {@link List< WeLeadsImportRecordVO>}
     * @author WangYX
     * @date 2023/07/19 16:57
     */
    List<WeLeadsImportRecordVO> importRecord(String name);


}
