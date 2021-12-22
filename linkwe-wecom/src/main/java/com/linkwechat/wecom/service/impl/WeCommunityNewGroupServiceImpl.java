package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeEmpleCodeType;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.domain.*;
import com.linkwechat.wecom.domain.dto.WeCommunityNewGroupDto;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.domain.vo.WeCommunityNewGroupVo;
import com.linkwechat.wecom.domain.vo.WeCommunityWeComeMsgVo;
import com.linkwechat.wecom.domain.vo.WeGroupCodeVo;
import com.linkwechat.wecom.mapper.*;
import com.linkwechat.wecom.service.IWeCommunityNewGroupService;
import com.linkwechat.wecom.service.IWeGroupCodeActualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 社群运营 新客自动拉群
 *
 * @author kewen
 * @date 2021-02-19
 */
@Service
@Slf4j
public class WeCommunityNewGroupServiceImpl extends ServiceImpl<WeCommunityNewGroupMapper, WeCommunityNewGroup> implements IWeCommunityNewGroupService {

    @Autowired
    private WeEmpleCodeServiceImpl weEmpleCodeService;

    @Autowired
    private WeGroupCodeMapper weGroupCodeMapper;

    @Autowired
    private WeCommunityNewGroupMapper weCommunityNewGroupMapper;

    @Autowired
    private WeEmpleCodeTagMapper empleCodeTagMapper;

    @Autowired
    private WeEmpleCodeUseScopMapper empleCodeUseScopMapper;

    @Autowired
    private WeTagMapper weTagMapper;

    @Autowired
    private WeEmpleCodeMapper weEmpleCodeMapper;

    @Autowired
    private WeUserMapper weUserMapper;

    @Autowired
    private IWeGroupCodeActualService weGroupCodeActualService;


    /**
     * 添加新客自动拉群信息
     *
     * @param communityNewGroupDto 信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(WeCommunityNewGroupDto communityNewGroupDto) {

        //检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectById(communityNewGroupDto.getGroupCodeId());
        if (!Optional.ofNullable(weGroupCode).isPresent()) {
            throw new WeComException("群活码不存在！");
        }

        // 获取员工活码相关信息
        WeEmpleCode weEmpleCode = getWeEmpleCode(communityNewGroupDto);

        // 构造请求参数，请求企微接口获取二维码及config_id
        WeExternalContactDto.WeContactWay weContactWay = weEmpleCodeService.getWeContactWay(weEmpleCode);
        WeExternalContactDto weExternalContactDto = weEmpleCodeService.getQrCode(weContactWay);
        weEmpleCode.setConfigId(weExternalContactDto.getConfig_id());
        weEmpleCode.setQrCode(weExternalContactDto.getQr_code());

        // 保存员工活码信息
        if (weEmpleCodeMapper.insert(weEmpleCode) > 0) {

            if (StringUtils.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
                // 批量保存员工活码标签
                empleCodeTagMapper.batchInsetWeEmpleCodeTag(weEmpleCode.getWeEmpleCodeTags());
            }
            // 批量保存活码使用员工
            empleCodeUseScopMapper.batchInsetWeEmpleCodeUseScop(weEmpleCode.getWeEmpleCodeUseScops());

            // 保存新客自动拉群信息
            WeCommunityNewGroup communityNewGroup = new WeCommunityNewGroup();
            communityNewGroup.setGroupCodeId(weGroupCode.getId());
            communityNewGroup.setEmplCodeName(communityNewGroupDto.getCodeName());
            communityNewGroup.setCreateBy(SecurityUtils.getUsername());
            communityNewGroup.setEmplCodeId(weEmpleCode.getId());

            return weCommunityNewGroupMapper.insert(communityNewGroup);
        }

        return 0;

    }


    /**
     * 查询新客自动拉群列表
     *
     * @param weCommunityNewGroup 新科拉群过滤条件
     * @return {@link WeCommunityNewGroupVo}s
     */
    @Override
    public List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup) {
        List<WeCommunityNewGroupVo> communityNewGroupVos = weCommunityNewGroupMapper.selectWeCommunityNewGroupList(weCommunityNewGroup);
        if (StringUtils.isNotEmpty(communityNewGroupVos)) {
            communityNewGroupVos.forEach(this::getCompleteEmplCodeInfo);
        }
        return communityNewGroupVos;
    }

    /**
     * 获取新客自动拉群详细信息
     *
     * @param id 主键id
     * @return {@link WeCommunityNewGroupVo} 自动拉群信息
     */
    @Override
    public Optional<WeCommunityNewGroupVo> selectWeCommunityNewGroupById(Long id) {
        Optional<WeCommunityNewGroupVo> vo = weCommunityNewGroupMapper.selectWeCommunityNewGroupById(id);
        vo.ifPresent(this::getCompleteEmplCodeInfo);
        return vo;
    }

    /**
     * 修改新客自动拉群。主要是对员工活码进行更新。需要先调用企微接口更新对应config_id的"联系我"配置
     *
     * @param id                   新客拉群id
     * @param communityNewGroupDto 信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateWeCommunityNewGroup(Long id, WeCommunityNewGroupDto communityNewGroupDto) {
        // 检查新客拉群信息是否存在
        WeCommunityNewGroup communityNewGroup = weCommunityNewGroupMapper.selectById(id);
        if (StringUtils.isNull(communityNewGroup)) {
            throw new WeComException("新客拉群信息不存在！");
        }
        // 检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectById(communityNewGroupDto.getGroupCodeId());
        if (null == weGroupCode) {
            throw new WeComException("群活码不存在！");
        }

        communityNewGroup.setGroupCodeId(communityNewGroupDto.getGroupCodeId());
        try {
            // 更新员工活码以及其对应的 "联系我" 配置
            WeEmpleCode weEmplCode = weEmpleCodeMapper.selectWeEmpleCodeById(communityNewGroup.getEmplCodeId());
            // 更改员工活码的扫码标签、使用员工
            setScopsAndTags(weEmplCode, communityNewGroupDto);
            // 更新使用场景、欢迎语、是否验证
            weEmplCode.setScenario(communityNewGroupDto.getCodeName());
            weEmplCode.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
            weEmplCode.setIsJoinConfirmFriends(communityNewGroupDto.getSkipVerify()?new Integer(1):new Integer(0));
            weEmpleCodeService.updateWeEmpleCode(weEmplCode);
        } catch (Exception e) {
            throw new WeComException("员工活码更新失败");
        }
        communityNewGroup.setEmplCodeName(communityNewGroupDto.getCodeName());
        communityNewGroup.setUpdateBy(SecurityUtils.getUsername());
        return weCommunityNewGroupMapper.updateById(communityNewGroup);
    }

    /**
     * 批量删除新客自动拉群。不需要删除该拉群的员工活码信息
     *
     * @param idList 新客拉群id列表
     * @return 标签对象列表
     */
    @Override
    @Transactional
    public int batchRemoveWeCommunityNewGroupByIds(List<Long> idList) {
        // 删除新客拉群信息
        return weCommunityNewGroupMapper.batchRemoveWeCommunityNewGroupByIds(idList);
    }

    /**
     * 通过id查询新客自动拉群信息列表
     *
     * @param ids id列表
     * @return {@link WeCommunityNewGroup} 新客自动拉群信息
     */
    @Override
    public List<WeCommunityNewGroupVo> selectWeCommunityNewGroupByIds(List<Long> ids) {
        return weCommunityNewGroupMapper.selectWeCommunityNewGroupByIds(ids);
    }

    @Override
    public WeCommunityWeComeMsgVo getWelcomeMsgByState(String state) {
        return this.baseMapper.getWelcomeMsgByState(state);
    }

    /**
     * 创建员工活码
     *
     * @param communityNewGroupDto 数据
     * @return 员工活码
     */
    private WeEmpleCode getWeEmpleCode(WeCommunityNewGroupDto communityNewGroupDto) {
        Snowflake snowflake = IdUtil.getSnowflake(RandomUtil.randomLong(6), RandomUtil.randomInt(6));
        WeEmpleCode weEmpleCode = new WeEmpleCode();

        weEmpleCode.setId(SnowFlakeUtil.nextId());
        // 设置员工和扫码标签
        setScopsAndTags(weEmpleCode, communityNewGroupDto);

        // 固定为多人类型
        weEmpleCode.setCodeType(WeEmpleCodeType.MULTI.getType());

        weEmpleCode.setIsJoinConfirmFriends(communityNewGroupDto.getSkipVerify()?new Integer(1):new Integer(0));
        // 欢迎语
        weEmpleCode.setWelcomeMsg(communityNewGroupDto.getWelcomeMsg());
        // state，用于区分客户具体是通过哪个「联系我」添加，最大30个字符。使用id作为值即可。
        weEmpleCode.setState(WeConstans.WE_QR_XKLQ_PREFIX + snowflake.nextIdStr());

        // 活动场景，使用键入的活码名称
        weEmpleCode.setScenario(communityNewGroupDto.getCodeName());

        // 通用属性设置
        weEmpleCode.setCreateBy(SecurityUtils.getUsername());

        return weEmpleCode;
    }

    /**
     * 设置员工活码的标签列表和员工列表
     *
     * @param weEmpleCode          员工活码
     * @param communityNewGroupDto 新客拉群信息
     */
    private void setScopsAndTags(WeEmpleCode weEmpleCode, WeCommunityNewGroupDto communityNewGroupDto) {
        // 获取活码员工列表 TODO user_id是对应business_id?
        LambdaQueryWrapper<WeUser> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.in(WeUser::getUserId, communityNewGroupDto.getEmplList());
        List<WeUser> weUserList = weUserMapper.selectList(userQueryWrapper);
        List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = weUserList.stream().map(e -> {
            WeEmpleCodeUseScop scop = new WeEmpleCodeUseScop();
            scop.setEmpleCodeId(weEmpleCode.getId());
            scop.setBusinessId(e.getUserId());
            scop.setBusinessIdType(2);
            scop.setBusinessName(e.getName());
            return scop;
        }).collect(Collectors.toList());
        weEmpleCode.setWeEmpleCodeUseScops(weEmpleCodeUseScopList);

        // 获取活码标签
        List<String> tagIdList = communityNewGroupDto.getTagList();
        if (StringUtils.isNotEmpty(tagIdList)) {
            LambdaQueryWrapper<WeTag> tagQueryWrapper = new LambdaQueryWrapper<>();
            tagQueryWrapper.in(WeTag::getTagId, tagIdList);
            List<WeTag> weTagList = weTagMapper.selectList(tagQueryWrapper);
            List<WeEmpleCodeTag> weEmpleCodeTagList = weTagList.stream().map(e -> {
                WeEmpleCodeTag tag = new WeEmpleCodeTag();
                tag.setEmpleCodeId(weEmpleCode.getId());
                tag.setTagId(e.getTagId());
                tag.setTagName(e.getName());
                return tag;
            }).collect(Collectors.toList());
            weEmpleCode.setWeEmpleCodeTags(weEmpleCodeTagList);
        }
    }

    /**
     * 获取完整的新客自动拉群相关信息
     *
     * @param vo 新客自动拉群
     */
    private void getCompleteEmplCodeInfo(WeCommunityNewGroupVo vo) {
        // 设置员工活码信息
        WeEmpleCode empleCode = weEmpleCodeMapper.selectWeEmpleCodeById(vo.getEmplCodeId());

        Optional.ofNullable(empleCode).ifPresent(e -> {
            vo.setEmplCodeUrl(e.getQrCode());
            vo.setWelcomeMsg(e.getWelcomeMsg());
            vo.setSkipVerify(e.getIsJoinConfirmFriends().equals(new Integer(1))?true:false);
        });

        // 设置群活码信息
        WeGroupCode weGroupCode = weGroupCodeMapper.selectById(vo.getGroupCodeId());
        Optional.ofNullable(weGroupCode).ifPresent(e -> {
            WeGroupCodeVo groupCodeVo = WeGroupCodeVo
                    .builder()
                    .id(e.getId())
                    .codeUrl(e.getCodeUrl())
                    .build();
            BeanUtils.copyProperties(e, groupCodeVo);
            vo.setGroupCodeInfo(groupCodeVo);
        });

        // 获取员工列表信息
        List<WeEmpleCodeUseScop> empleCodeUseScopList = empleCodeUseScopMapper.selectWeEmpleCodeUseScopListById(vo.getEmplCodeId());
        vo.setEmplList(empleCodeUseScopList);

        List<WeGroupCodeActual> codeActuals = weGroupCodeActualService.list(new LambdaQueryWrapper<WeGroupCodeActual>()
                .eq(WeGroupCodeActual::getGroupCodeId, vo.getGroupCodeId()));
        if(CollectionUtil.isNotEmpty(codeActuals)){
            vo.setActualGroupName(
                    String.join(",", codeActuals.stream().map(WeGroupCodeActual::getGroupName).collect(Collectors.toList()))
            );
        }


        //  获取相关群聊信息
        List<WeGroup> groupList = weGroupCodeMapper.selectWeGroupListByGroupCodeId(vo.getGroupCodeId());
        vo.setGroupList(groupList);

        // 获取标签信息
        List<WeEmpleCodeTag> tagList = empleCodeTagMapper.selectWeEmpleCodeTagListById(vo.getEmplCodeId());
        vo.setTagList(tagList);
    }
}
