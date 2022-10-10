package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.linkwechat.common.core.page.PageDomain;
import com.linkwechat.common.core.page.TableSupport;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.sql.SqlUtil;
import com.linkwechat.domain.WeMsgTlp;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpAddQuery;
import com.linkwechat.domain.msgtlp.query.WeMsgTlpQuery;
import com.linkwechat.domain.msgtlp.vo.WeMsgTlpVo;
import com.linkwechat.mapper.WeMsgTlpMapper;
import com.linkwechat.service.IWeMsgTlpAttachmentsService;
import com.linkwechat.service.IWeMsgTlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 欢迎语模板表(WeMsgTlp)
 *
 * @author danmo
 * @since 2022-03-28 10:21:24
 */
@Service
public class WeMsgTlpServiceImpl extends ServiceImpl<WeMsgTlpMapper, WeMsgTlp> implements IWeMsgTlpService {

    @Autowired
    private IWeMsgTlpAttachmentsService weMsgTlpAttachmentsService;

    @Transactional(rollbackFor = {WeComException.class,Exception.class})
    @Override
    public void addMsgTlp(WeMsgTlpAddQuery query) {
        WeMsgTlp weMsgTlp = new WeMsgTlp();
        weMsgTlp.setTplType(query.getTplType());
        if(CollectionUtil.isNotEmpty(query.getUserIds())){
            weMsgTlp.setUserIds(String.join(",", query.getUserIds()));
        }
        if(CollectionUtil.isNotEmpty(query.getUserNames())){
            weMsgTlp.setUserNames(String.join(",", query.getUserNames()));
        }

        if(save(weMsgTlp)){
            weMsgTlpAttachmentsService.addMsgTlpAttachments(weMsgTlp.getId(),query.getAttachments());
        }
    }

    @Transactional(rollbackFor = {WeComException.class,Exception.class})
    @Override
    public void updateMsgTlp(WeMsgTlpAddQuery query) {
        WeMsgTlp weMsgTlp = new WeMsgTlp();
        weMsgTlp.setId(query.getId());
        weMsgTlp.setTplType(query.getTplType());
        if(CollectionUtil.isNotEmpty(query.getUserIds())){
            weMsgTlp.setUserIds(String.join(",", query.getUserIds()));
        }
        if(CollectionUtil.isNotEmpty(query.getUserNames())){
            weMsgTlp.setUserNames(String.join(",", query.getUserNames()));
        }
        if(updateById(weMsgTlp)){
            weMsgTlpAttachmentsService.updateMsgTlpAttachments(weMsgTlp.getId(),query.getAttachments());
        }
    }

    @Transactional(rollbackFor = {WeComException.class,Exception.class})
    @Override
    public void delMsgTlp(List<Long> ids) {
        if(CollectionUtil.isNotEmpty(ids)){
            List<WeMsgTlp> weMsgTlpList = ids.stream().map(id -> {
                WeMsgTlp weMsgTlp = new WeMsgTlp();
                weMsgTlp.setId(id);
                weMsgTlp.setDelFlag(1);
                return weMsgTlp;
            }).collect(Collectors.toList());
            updateBatchById(weMsgTlpList);
            weMsgTlpAttachmentsService.delMsgTlpAttachments(ids);
        }else {
            throw new WeComException("模板ID不能为空");
        }
    }

    @Override
    public WeMsgTlpVo getInfo(Long id) {
       return this.baseMapper.getInfo(id);
    }

    @Override
    public List<WeMsgTlpVo> getList(WeMsgTlpQuery query) {
        List<Long> weMsgTlpIds = this.baseMapper.getListIds(query);

        if (CollectionUtil.isNotEmpty(weMsgTlpIds)) {
            if(query.getFlag()){
                PageDomain pageDomain = TableSupport.buildPageRequest();
                String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
                PageHelper.orderBy(orderBy);
            }
            return this.baseMapper.getMsgTlpByIds(weMsgTlpIds);
        }
        return new ArrayList<>();
    }
}
