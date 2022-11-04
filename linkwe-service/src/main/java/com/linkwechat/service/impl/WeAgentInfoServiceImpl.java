package com.linkwechat.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.enums.MessageType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeAgentInfo;
import com.linkwechat.domain.agent.query.WeAgentAddQuery;
import com.linkwechat.domain.agent.query.WeAgentEditQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.agent.query.WeAgentQuery;
import com.linkwechat.domain.wecom.vo.agent.vo.WeAgentDetailVo;
import com.linkwechat.domain.wecom.vo.media.WeMediaVo;
import com.linkwechat.fegin.QwAgentClient;
import com.linkwechat.mapper.WeAgentInfoMapper;
import com.linkwechat.service.IWeAgentInfoService;
import com.linkwechat.service.IWeMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应用信息表(WeAgentInfo)
 *
 * @author danmo
 * @since 2022-11-04 17:08:08
 */
@Service
public class WeAgentInfoServiceImpl extends ServiceImpl<WeAgentInfoMapper, WeAgentInfo> implements IWeAgentInfoService {

    @Resource
    private QwAgentClient qwAgentClient;

    @Autowired
    private IWeMaterialService weMaterialService;

    @Override
    public void pullAgent(WeAgentAddQuery query) {
        WeAgentInfo weAgentInfo = new WeAgentInfo();
        weAgentInfo.setAgentId(query.getAgentId());
        weAgentInfo.setSecret(query.getSecret());
        if (save(weAgentInfo)) {
            WeAgentQuery weAgentQuery = new WeAgentQuery();
            weAgentQuery.setAgentid(String.valueOf(query.getAgentId()));
            weAgentQuery.setCorpid(SecurityUtils.getCorpId());
            WeAgentDetailVo weAgentDetail = qwAgentClient.getAgentDetail(weAgentQuery).getData();
            if (Objects.nonNull(weAgentDetail) && Objects.nonNull(weAgentDetail.getAgentId())) {
                String userId = weAgentDetail.getAllowUserinfos().getUser().stream().map(WeAgentDetailVo.AllowUser::getUserId).collect(Collectors.joining(","));
                weAgentInfo.setAllowUserinfoId(userId);
                String partyIds = String.join(",", weAgentDetail.getAllowPartys().getPartyId());
                weAgentInfo.setAllowPartyId(partyIds);
                String tagIds = String.join(",", weAgentDetail.getAllowTags().getTagId());
                weAgentInfo.setAllowTagId(tagIds);
                weAgentInfo.setClose(weAgentDetail.getClose());
                weAgentInfo.setCustomizedPublishStatus(weAgentDetail.getCustomizedPublishStatus());
                weAgentInfo.setDescription(weAgentDetail.getDescription());
                weAgentInfo.setHomeUrl(weAgentDetail.getHomeUrl());
                weAgentInfo.setIsReporter(weAgentDetail.getIsreportenter());
                weAgentInfo.setLogoUrl(weAgentDetail.getSquareLogoUrl());
                weAgentInfo.setName(weAgentDetail.getName());
                weAgentInfo.setRedirectDomain(weAgentDetail.getRedirectDomain());
                weAgentInfo.setReportLocationFlag(weAgentDetail.getReportLocationFlag());
                updateById(weAgentInfo);
            }
        }
    }

    @Override
    public void update(WeAgentEditQuery query) {
        WeAgentInfo agentInfo = getById(query.getId());
        if (Objects.isNull(agentInfo)) {
            throw new WeComException("应用不存在");
        }
        WeAgentQuery weAgentQuery = new WeAgentQuery();
        weAgentQuery.setAgentid(String.valueOf(agentInfo.getAgentId()));
        weAgentQuery.setCorpid(SecurityUtils.getCorpId());
        WeAgentInfo weAgentInfo = new WeAgentInfo();
        weAgentInfo.setId(query.getId());
        if (StringUtils.isNotEmpty(query.getName())) {
            weAgentInfo.setName(query.getName());
            weAgentQuery.setName(query.getName());
        }
        if (StringUtils.isNotEmpty(query.getDescription())) {
            weAgentInfo.setDescription(query.getDescription());
            weAgentQuery.setDescription(query.getDescription());
        }
        if (StringUtils.isNotEmpty(query.getHomeUrl())) {
            weAgentInfo.setHomeUrl(query.getHomeUrl());
            weAgentQuery.setHome_url(query.getHomeUrl());
        }
        if (StringUtils.isNotEmpty(query.getRedirectDomain())) {
            weAgentInfo.setRedirectDomain(query.getRedirectDomain());
            weAgentQuery.setRedirect_domain(query.getRedirectDomain());
        }
        if (StringUtils.isNotEmpty(query.getLogoUrl())) {
            WeMediaVo weMediaVo = weMaterialService.uploadTemporaryMaterial(query.getLogoUrl(),
                    MessageType.IMAGE.getMessageType(),
                    FileUtil.getName(query.getLogoUrl()));
            if (Objects.nonNull(weMediaVo) && StringUtils.isNotEmpty(weMediaVo.getMediaId())) {
                weAgentInfo.setLogoUrl(query.getLogoUrl());
                weAgentQuery.setLogo_mediaid(query.getLogoUrl());
            }
        }
        WeResultVo resultVo = qwAgentClient.updateAgent(weAgentQuery).getData();
        if (Objects.nonNull(resultVo) && Objects.equals(0, resultVo.getErrCode())) {
            updateById(weAgentInfo);
        } else {
            throw new WeComException(resultVo.getErrCode(), "更新失败");
        }
    }

    @Override
    public WeAgentInfo getAgentInfoByAgentId(Integer agentId) {
        return getOne(new LambdaQueryWrapper<WeAgentInfo>().eq(WeAgentInfo::getAgentId, agentId).eq(WeAgentInfo::getDelFlag, 0).last("limit 1"));
    }

    @Override
    public void deleteAgent(Integer id) {
        WeAgentInfo weAgentInfo = new WeAgentInfo();
        weAgentInfo.setId(id);
        weAgentInfo.setDelFlag(1);
        updateById(weAgentInfo);
    }
}
