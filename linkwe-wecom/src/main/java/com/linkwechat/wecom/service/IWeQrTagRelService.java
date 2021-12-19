package com.linkwechat.wecom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.domain.WeQrTagRel;

import java.util.List;

/**
 * 活码标签关联表(WeQrTagRel)表服务接口
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
public interface IWeQrTagRelService extends IService<WeQrTagRel> {

    /**
     * 通过活码id批量保存活码标签
     * @param qrId 活码id
     * @param qrTags 标签id
     */
    void saveBatchByQrId(Long qrId, List<String> qrTags);

    /**
     * 通过活码id批量删除活码标签
     * @param qrIds
     */
    Boolean delBatchByQrIds(List<Long> qrIds);

    /**
     * 通过活码id批量修改活码标签
     * @param qrId 活码id
     * @param qrTags 标签id
     */
    void updateBatchByQrId(Long qrId, List<String> qrTags);
}
