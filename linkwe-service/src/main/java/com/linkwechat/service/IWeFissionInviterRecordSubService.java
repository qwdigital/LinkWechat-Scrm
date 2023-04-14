package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.fission.WeFissionInviterRecordSub;
import com.linkwechat.domain.fission.vo.WeFissionProgressVo;

import java.util.List;

/**
* @author robin
* @description 针对表【we_fission_detail_sub(裂变明细子表)】的数据库操作Service
* @createDate 2023-03-14 14:07:21
*/
public interface IWeFissionInviterRecordSubService extends IService<WeFissionInviterRecordSub> {

    /**
     * 获取任务裂变进度
     * @param unionid
     * @param fissionId
     * @return
     */
    WeFissionProgressVo findWeFissionProgress(String unionid,String fissionId);


    /**
     * 批量报错或更新
     * @param weFissionInviterRecordSubList
     */
    void batchSaveOrUpdate(List<WeFissionInviterRecordSub> weFissionInviterRecordSubList);

}
