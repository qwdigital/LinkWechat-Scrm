package com.linkwechat.wecom.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WeMsgTlpScope;
import com.linkwechat.wecom.service.IWeMsgTlpScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeMsgTlpMapper;
import com.linkwechat.wecom.domain.WeMsgTlp;
import com.linkwechat.wecom.service.IWeMsgTlpService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 欢迎语模板Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeMsgTlpServiceImpl implements IWeMsgTlpService 
{
    @Autowired
    private WeMsgTlpMapper weMsgTlpMapper;


    /**
     * 查询欢迎语模板
     * 
     * @param id 欢迎语模板ID
     * @return 欢迎语模板
     */
    @Override
    public WeMsgTlp selectWeMsgTlpById(Long id)
    {
        return weMsgTlpMapper.selectWeMsgTlpById(id);
    }

    /**
     * 查询欢迎语模板列表
     * 
     * @param weMsgTlp 欢迎语模板
     * @return 欢迎语模板
     */
    @Override
    public List<WeMsgTlp> selectWeMsgTlpList(WeMsgTlp weMsgTlp)
    {
        return weMsgTlpMapper.selectWeMsgTlpList(weMsgTlp);
    }

    /**
     * 新增欢迎语模板
     * 
     * @param weMsgTlp 欢迎语模板
     * @return 结果
     */
    @Override
    public int insertWeMsgTlp(WeMsgTlp weMsgTlp)
    {
        weMsgTlp.setCreateBy(SecurityUtils.getUsername());
        weMsgTlp.setCreateTime(new Date());

        return weMsgTlpMapper.insertWeMsgTlp(weMsgTlp);

//        if(returnCode>0){
//            List<WeMsgTlpScope> weMsgTlpScopess = weMsgTlp.getWeMsgTlpScopes();
//            if(CollectionUtil.isNotEmpty(weMsgTlpScopess)){
//
//                List<WeMsgTlpScope> weMsgTlpScopes=weMsgTlp.getWeMsgTlpScopes().stream().filter(c -> c.getUseUserId() != null).collect(Collectors.toList());
//                if(CollectionUtil.isNotEmpty(weMsgTlpScopes)){
//
//                    weMsgTlpScopes.forEach(v->{
//                        v.setMsgTlpId(weMsgTlp.getId());
//                        v.setId(SnowFlakeUtil.nextId());
//                    });
//
//                    iWeMsgTlpScopeService.batchInsetWeMsgTlpScope(weMsgTlpScopes);
//                }
//
//            }
//        }

//        return returnCode;
    }

    /**
     * 修改欢迎语模板
     * 
     * @param weMsgTlp 欢迎语模板
     * @return 结果
     */
    @Override
    public int updateWeMsgTlp(WeMsgTlp weMsgTlp)
    {

//        int returnCode = weMsgTlpMapper.updateWeMsgTlp(weMsgTlp);
//
//        if(returnCode>0){
//            iWeMsgTlpScopeService.batchRemoveWeMsgTlpScopesByMsgTlpIds(ListUtil.toList(weMsgTlp.getId()));
//            List<WeMsgTlpScope> weMsgTlpScopes = weMsgTlp.getWeMsgTlpScopes().stream().filter(c -> c.getUseUserId() != null).collect(Collectors.toList());
//
//            if(CollectionUtil.isNotEmpty(weMsgTlpScopes)){
//                weMsgTlpScopes.stream().forEach(v->v.setMsgTlpId(weMsgTlp.getId()));
//                iWeMsgTlpScopeService.batchInsetWeMsgTlpScope(weMsgTlpScopes);
//            }
//
//        }
        return weMsgTlpMapper.updateWeMsgTlp(weMsgTlp);
    }

    /**
     * 批量删除欢迎语模板
     * 
     * @param ids 需要删除的欢迎语模板ID
     * @return 结果
     */
    @Override
    public int deleteWeMsgTlpByIds(Long[] ids)
    {
        return weMsgTlpMapper.deleteWeMsgTlpByIds(ids);
    }

    /**
     * 删除欢迎语模板信息
     * 
     * @param id 欢迎语模板ID
     * @return 结果
     */
    @Override
    public int deleteWeMsgTlpById(Long id)
    {
        return weMsgTlpMapper.deleteWeMsgTlpById(id);
    }


    /**
     * 批量逻辑删除
     * @param msgTlpIds
     * @return
     */
    @Override
    public int batchRemoveByids(List<Long> msgTlpIds) {
        return weMsgTlpMapper.batchRemoveByids(msgTlpIds);
    }
}
