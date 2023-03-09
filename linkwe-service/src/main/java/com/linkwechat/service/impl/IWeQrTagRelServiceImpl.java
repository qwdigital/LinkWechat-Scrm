package com.linkwechat.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.domain.qr.WeQrTagRel;
import com.linkwechat.mapper.WeQrTagRelMapper;
import com.linkwechat.service.IWeQrTagRelService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 活码标签关联表(WeQrTagRel)表服务实现类
 *
 * @author makejava
 * @since 2021-11-07 01:29:13
 */
//@DS("db1")
@Service
public class IWeQrTagRelServiceImpl extends ServiceImpl<WeQrTagRelMapper, WeQrTagRel> implements IWeQrTagRelService {

    @Override
    public void saveBatchByQrId(Long qrId, Integer businessType, List<String> qrTags) {
        List<WeQrTagRel> tagRels = Optional.ofNullable(qrTags).orElseGet(ArrayList::new).stream().map(tagId -> {
            WeQrTagRel weQrTagRel = new WeQrTagRel();
            weQrTagRel.setQrId(qrId);
            weQrTagRel.setTagId(tagId);
            weQrTagRel.setBusinessType(businessType);
            return weQrTagRel;
        }).collect(Collectors.toList());
        saveBatch(tagRels);
    }

    @Override
    public Boolean delBatchByQrIds(List<Long> qrIds, Integer businessType) {
        WeQrTagRel weQrTagRel = new WeQrTagRel();
        weQrTagRel.setDelFlag(1);
        return this.update(weQrTagRel, new LambdaQueryWrapper<WeQrTagRel>().in(WeQrTagRel::getQrId, qrIds).eq(WeQrTagRel::getBusinessType,businessType));
    }

    @Override
    public void updateBatchByQrId(Long qrId, Integer businessType, List<String> qrTags) {
        this.delBatchByQrIds(ListUtil.toList(qrId), businessType);
        this.saveBatchByQrId(qrId, businessType, qrTags);
    }
}
