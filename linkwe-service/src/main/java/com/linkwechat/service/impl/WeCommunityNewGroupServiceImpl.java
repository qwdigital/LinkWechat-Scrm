package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WeEmpleCodeType;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomer;
import com.linkwechat.domain.WeGroup;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.community.WeEmpleCode;
import com.linkwechat.domain.community.WeEmpleCodeTag;
import com.linkwechat.domain.community.WeEmpleCodeUseScop;
import com.linkwechat.domain.community.query.WeCommunityNewGroupQuery;
import com.linkwechat.domain.community.vo.WeCommunityNewGroupVo;
import com.linkwechat.domain.community.vo.WeCommunityWeComeMsgVo;
import com.linkwechat.domain.community.vo.WeGroupCodeVo;
import com.linkwechat.domain.groupcode.entity.WeGroupCode;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.mapper.WeGroupCodeMapper;
import com.linkwechat.mapper.WeTagMapper;
import com.linkwechat.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.domain.community.WeCommunityNewGroup;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeCommunityNewGroupServiceImpl extends ServiceImpl<WeCommunityNewGroupMapper, WeCommunityNewGroup> implements IWeCommunityNewGroupService {

    @Autowired
    private WeGroupCodeMapper weGroupCodeMapper;

    @Autowired
    private WeTagMapper weTagMapper;


    @Autowired
    private IWeEmpleCodeTagService iWeEmpleCodeTagService;

    @Autowired
    private IWeEmpleCodeService iWeEmpleCodeService;

    @Autowired
    private IWeEmpleCodeUseScopService iWeEmpleCodeUseScopService;


    @Autowired
    private IWeGroupCodeService iWeGroupCodeService;

    @Autowired
    private IWeCustomerService iWeCustomerService;




    @Override
    @Transactional
    public void add(WeCommunityNewGroupQuery weCommunityNewGroupQuery) {

        //检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectById(weCommunityNewGroupQuery.getGroupCodeId());
        if (!Optional.ofNullable(weGroupCode).isPresent()) {
            throw new WeComException("群活码不存在！");
        }
        // 获取员工活码相关信息
        WeEmpleCode weEmpleCode = getWeEmpleCode(weCommunityNewGroupQuery);

        // 构造请求参数，请求企微接口获取二维码及config_id
        WeAddWayVo weContactWay = iWeEmpleCodeService.getWeContactWay(weEmpleCode);

        if(null != weContactWay){
            if(StringUtils.isNotEmpty(weContactWay.getConfigId())&&StringUtils.isNotEmpty(weContactWay.getQrCode())){
                weEmpleCode.setConfigId(weContactWay.getConfigId());
                weEmpleCode.setQrCode(weContactWay.getQrCode());

                // 保存员工活码信息
                if (iWeEmpleCodeService.save(weEmpleCode)) {

                    if (StringUtils.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
                        // 批量保存员工活码标签
                        iWeEmpleCodeTagService.saveOrUpdateBatch(weEmpleCode.getWeEmpleCodeTags());
                    }
                    // 批量保存活码使用员工
                    iWeEmpleCodeUseScopService.saveOrUpdateBatch(weEmpleCode.getWeEmpleCodeUseScops());

                    // 保存新客自动拉群信息
                    WeCommunityNewGroup communityNewGroup = new WeCommunityNewGroup();
                    communityNewGroup.setGroupCodeId(weGroupCode.getId());
                    communityNewGroup.setEmplCodeName(weCommunityNewGroupQuery.getCodeName());
                    communityNewGroup.setEmplCodeId(weEmpleCode.getId());

                    save(communityNewGroup);
                }
            }else{
                throw new WeComException("创建活码失败");
            }
        }





    }


    @Override
    public List<WeCommunityNewGroupVo> selectWeCommunityNewGroupList(WeCommunityNewGroup weCommunityNewGroup) {
        List<WeCommunityNewGroupVo> communityNewGroupVos = this.baseMapper.selectWeCommunityNewGroupList(weCommunityNewGroup);
        if (StringUtils.isNotEmpty(communityNewGroupVos)) {
            communityNewGroupVos.forEach(this::getCompleteEmplCodeInfo);
        }
        return communityNewGroupVos;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWeCommunityNewGroup(WeCommunityNewGroupQuery weCommunityNewGroupQuery) {

       //检查新客拉群信息是否存在
        WeCommunityNewGroup communityNewGroup = getById(weCommunityNewGroupQuery.getId());
        if (StringUtils.isNull(communityNewGroup)) {
            throw new WeComException("新客拉群信息不存在！");
        }
        // 检查群活码是否存在
        WeGroupCode weGroupCode = weGroupCodeMapper.selectById(weCommunityNewGroupQuery.getGroupCodeId());
        if (null == weGroupCode) {
            throw new WeComException("群活码不存在！");
        }

        communityNewGroup.setGroupCodeId(weCommunityNewGroupQuery.getGroupCodeId());
        try {
            // 更新员工活码以及其对应的 "联系我" 配置
            WeEmpleCode weEmplCode = iWeEmpleCodeService.getById(communityNewGroup.getEmplCodeId());
            // 更改员工活码的扫码标签、使用员工
            setScopsAndTags(weEmplCode, weCommunityNewGroupQuery);
            // 更新使用场景、欢迎语、是否验证
            weEmplCode.setScenario(weCommunityNewGroupQuery.getCodeName());
            weEmplCode.setWelcomeMsg(weCommunityNewGroupQuery.getWelcomeMsg());
            weEmplCode.setIsJoinConfirmFriends(weCommunityNewGroupQuery.getSkipVerify()?new Integer(1):new Integer(0));
            iWeEmpleCodeService.updateWeEmpleCode(weEmplCode);
        } catch (Exception e) {
            throw new WeComException("员工活码更新失败");
        }
        communityNewGroup.setEmplCodeName(weCommunityNewGroupQuery.getCodeName());
         updateById(communityNewGroup);


    }

    @Override
    public WeCommunityWeComeMsgVo getWelcomeMsgByState(String state) {
        return this.baseMapper.getWelcomeMsgByState(state);
    }


    /**
     * 获取完整的新客自动拉群相关信息
     *
     * @param vo 新客自动拉群
     */
    private void getCompleteEmplCodeInfo(WeCommunityNewGroupVo vo) {
        // 设置员工活码信息
        WeEmpleCode empleCode = iWeEmpleCodeService.selectWeEmpleCodeById(vo.getEmplCodeId());

        Optional.ofNullable(empleCode).ifPresent(e -> {
            vo.setEmplCodeUrl(e.getQrCode());
            vo.setWelcomeMsg(e.getWelcomeMsg());
            vo.setSkipVerify(e.getIsJoinConfirmFriends().equals(new Integer(1))?true:false);
        });

       //添加的人员数
        vo.setCusNumber(iWeCustomerService.count(new LambdaQueryWrapper<WeCustomer>()
                .eq(WeCustomer::getState, empleCode.getState())));

        // 设置群活码信息
        WeGroupCode weGroupCode = iWeGroupCodeService.getById(vo.getGroupCodeId());
        Optional.ofNullable(weGroupCode).ifPresent(e -> {
            WeGroupCodeVo groupCodeVo = WeGroupCodeVo
                    .builder()
                    .id(e.getId())
                    .codeUrl(e.getCodeUrl())
                    .build();
            BeanUtils.copyProperties(e, groupCodeVo);
            vo.setGroupCodeInfo(groupCodeVo);
            vo.setActualGroupName(weGroupCode.getActivityName());
        });

        // 获取员工列表信息
        List<WeEmpleCodeUseScop> empleCodeUseScopList = iWeEmpleCodeUseScopService.list(new LambdaQueryWrapper<WeEmpleCodeUseScop>()
                .eq(WeEmpleCodeUseScop::getEmpleCodeId,vo.getEmplCodeId()));

        vo.setEmplList(empleCodeUseScopList);
//
//        List<WeGroupCodeActual> codeActuals = weGroupCodeActualService.list(new LambdaQueryWrapper<WeGroupCodeActual>()
//                .eq(WeGroupCodeActual::getGroupCodeId, vo.getGroupCodeId()));
//        if(CollectionUtil.isNotEmpty(codeActuals)){
//            vo.setActualGroupName(
//                    String.join(",", codeActuals.stream().map(WeGroupCodeActual::getGroupName).collect(Collectors.toList()))
//            );
//        }
//
//
//        //  获取相关群聊信息
//        //  获取相关群聊信息
//        List<WeGroup> groupList = iWeGroupCodeService.selectWeGroupListByGroupCodeId(vo.getGroupCodeId());
//        vo.setGroupList(groupList);

        // 获取标签信息
        List<WeEmpleCodeTag> tagList = iWeEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                .eq(WeEmpleCodeTag::getEmpleCodeId,vo.getEmplCodeId()));
        vo.setTagList(tagList);
    }


    /**
     * 创建员工活码
     *
     * @param communityNewGroupDto 数据
     * @return 员工活码
     */
    private WeEmpleCode getWeEmpleCode(WeCommunityNewGroupQuery communityNewGroupDto) {
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
        weEmpleCode.setState(WelcomeMsgTypeEnum.WE_QR_XKLQ_PREFIX.getType() + snowflake.nextIdStr());

        // 活动场景，使用键入的活码名称
        weEmpleCode.setScenario(communityNewGroupDto.getCodeName());


        return weEmpleCode;
    }

    /**
     * 设置员工活码的标签列表和员工列表
     *
     * @param weEmpleCode          员工活码
     * @param communityNewGroupDto 新客拉群信息
     */
    private void setScopsAndTags(WeEmpleCode weEmpleCode, WeCommunityNewGroupQuery communityNewGroupDto) {
        // 获取活码员工列表 TODO user_id是对应business_id?
        List<Map<String, String>> userNameByUserIds = this.baseMapper.findUserNameByUserIds(communityNewGroupDto.getEmplList());
        List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = userNameByUserIds.stream().map(e -> {
            WeEmpleCodeUseScop scop = new WeEmpleCodeUseScop();
            scop.setEmpleCodeId(weEmpleCode.getId());
            scop.setBusinessIdType(2);
            scop.setBusinessId(String.valueOf(e.get("we_user_id")));
            scop.setBusinessName(e.get("user_name"));
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

}
