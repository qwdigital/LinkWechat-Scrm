package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.domain.WeQrTagRel;
import com.linkwechat.wecom.mapper.WeQrTagRelMapper;
import com.linkwechat.wecom.service.IWeQrTagRelService;
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
    public void saveBatchByQrId(Long qrId, List<String> qrTags) {
        List<WeQrTagRel> tagRels = Optional.ofNullable(qrTags).orElseGet(ArrayList::new).stream().map(tagId -> {
            WeQrTagRel weQrTagRel = new WeQrTagRel();
            weQrTagRel.setQrId(qrId);
            weQrTagRel.setTagId(tagId);
            return weQrTagRel;
        }).collect(Collectors.toList());
        saveBatch(tagRels);
    }

    @Override
    public Boolean delBatchByQrIds(List<Long> qrIds) {
        WeQrTagRel weQrTagRel = new WeQrTagRel();
        weQrTagRel.setDelFlag(1);
        return this.update(weQrTagRel, new LambdaQueryWrapper<WeQrTagRel>().in(WeQrTagRel::getQrId,qrIds));
    }

    @Override
    public void updateBatchByQrId(Long qrId, List<String> qrTags) {
        this.delBatchByQrIds(ListUtil.toList(qrId));
        this.saveBatchByQrId(qrId,qrTags);
    }
}
