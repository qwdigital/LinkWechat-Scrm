package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.redis.RedisCache;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import com.linkwechat.wecom.domain.WeMaterial;
import com.linkwechat.wecom.domain.dto.WeEmpleCodeDto;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.mapper.WeCommunityNewGroupMapper;
import com.linkwechat.wecom.mapper.WeEmpleCodeMapper;
import com.linkwechat.wecom.mapper.WeEmpleCodeTagMapper;
import com.linkwechat.wecom.mapper.WeEmpleCodeUseScopMapper;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import com.linkwechat.wecom.service.IWeEmpleCodeTagService;
import com.linkwechat.wecom.service.IWeEmpleCodeUseScopService;
import com.linkwechat.wecom.service.IWeMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 员工活码Service业务层处理
 *
 * @author ruoyi
 * @date 2020-10-04
 */
@Slf4j
@Service
public class WeEmpleCodeServiceImpl extends ServiceImpl<WeEmpleCodeMapper, WeEmpleCode> implements IWeEmpleCodeService {

    @Autowired
    private IWeEmpleCodeTagService weEmpleCodeTagService;


    @Autowired
    private IWeEmpleCodeUseScopService iWeEmpleCodeUseScopService;


    @Autowired
    private WeExternalContactClient weExternalContactClient;

    @Autowired
    private IWeMaterialService materialService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WeCommunityNewGroupMapper communityNewGroupMapper;

    /**
     * 查询员工活码
     *
     * @param id 员工活码ID
     * @return 员工活码
     */
    @Override
    public WeEmpleCode selectWeEmpleCodeById(Long id) {
        WeEmpleCode weEmpleCode = this.baseMapper.selectWeEmpleCodeById(id);
        //查询活码详情中素材信息
        Optional.ofNullable(weEmpleCode).map(WeEmpleCode::getMediaId).ifPresent(mediaId -> {
            WeMaterial weMaterialInfo = materialService.findWeMaterialById(mediaId);
            if (weMaterialInfo != null) {
                weEmpleCode.setWeMaterial(weMaterialInfo);
            }
        });


        weEmpleCode.setWeEmpleCodeTags(
                weEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                        .eq(WeEmpleCodeTag::getEmpleCodeId,weEmpleCode.getId()))
        );


        return weEmpleCode;
    }

    @Override
    public List<WeEmpleCode> selectWeEmpleCodeByIds(List<String> ids) {
        List<WeEmpleCode> weEmpleCodeList = this.baseMapper.selectWeEmpleCodeByIds(ids);
        if (weEmpleCodeList != null) {
            weEmpleCodeList.forEach(empleCode -> {
                List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = empleCode.getWeEmpleCodeUseScops();
                setUserData(empleCode, weEmpleCodeUseScopList);
            });
        }
        return weEmpleCodeList;
    }

    private void setUserData(WeEmpleCode empleCode, List<WeEmpleCodeUseScop> weEmpleCodeUseScopList) {
        if (CollectionUtil.isNotEmpty(weEmpleCodeUseScopList)) {
            String useUserName = weEmpleCodeUseScopList.stream().map(WeEmpleCodeUseScop::getBusinessName)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
            empleCode.setUseUserName(useUserName);
            String mobile = weEmpleCodeUseScopList.stream().map(WeEmpleCodeUseScop::getMobile)
                    .filter(StringUtils::isNotEmpty).collect(Collectors.joining(","));
            empleCode.setMobile(mobile);
        }
    }

    /**
     * 查询员工活码列表
     *
     * @param weEmpleCode 员工活码
     * @return 员工活码
     */
    @Override
    public List<WeEmpleCode> selectWeEmpleCodeList(WeEmpleCode weEmpleCode) {

        List<WeEmpleCode> weEmpleCodeList = this.baseMapper.selectWeEmpleCodeList(weEmpleCode);
        if (CollectionUtil.isNotEmpty(weEmpleCodeList)) {
            List<Long> empleCodeIdList = weEmpleCodeList.stream().map(WeEmpleCode::getId).collect(Collectors.toList());
            List<WeEmpleCodeUseScop> useScopList = iWeEmpleCodeUseScopService.selectWeEmpleCodeUseScopListByIds(empleCodeIdList);
            List<WeEmpleCodeTag> tagList = weEmpleCodeTagService.selectWeEmpleCodeTagListByIds(empleCodeIdList);
            weEmpleCodeList.forEach(empleCode -> {
                //活码使用人对象
                List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = useScopList.stream()
                        .filter(useScop -> useScop.getEmpleCodeId().equals(empleCode.getId())).collect(Collectors.toList());
                setUserData(empleCode, weEmpleCodeUseScopList);
                empleCode.setWeEmpleCodeUseScops(weEmpleCodeUseScopList);
                //员工活码标签对象
                empleCode.setWeEmpleCodeTags(tagList.stream()
                        .filter(tag -> tag.getEmpleCodeId().equals(empleCode.getId())).collect(Collectors.toList()));
            });
        }
        return weEmpleCodeList;
    }

    /**
     * 新增员工活码
     *
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWeEmpleCode(WeEmpleCode weEmpleCode) {
        weEmpleCode.setCreateTime(new Date());
        weEmpleCode.setCreateBy(SecurityUtils.getUsername());
        // 使用员工活码的id作为state
        weEmpleCode.setState(weEmpleCode.getId().toString());
        addWeEmplCode(weEmpleCode);
    }

    /**
     * 修改员工活码
     *
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWeEmpleCode(WeEmpleCode weEmpleCode) {
        weEmpleCode.setState(String.valueOf(weEmpleCode.getId()));
        WeExternalContactDto.WeContactWay weContactWay = getWeContactWay(weEmpleCode);
        try {
            weExternalContactClient.updateContactWay(weContactWay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            if (this.updateById(weEmpleCode)) {
                if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeUseScops())) {

                    //移除原有的记录
                    if(iWeEmpleCodeUseScopService.remove(new LambdaQueryWrapper<WeEmpleCodeUseScop>()
                            .eq(WeEmpleCodeUseScop::getEmpleCodeId,weEmpleCode.getId()))){

                        weEmpleCode.getWeEmpleCodeUseScops().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
                        iWeEmpleCodeUseScopService.saveOrUpdateBatch(weEmpleCode.getWeEmpleCodeUseScops());

                    }

                }
                if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {

                    //有id的更新
                    weEmpleCodeTagService.remove(new LambdaQueryWrapper<WeEmpleCodeTag>()
                            .eq(WeEmpleCodeTag::getEmpleCodeId,weEmpleCode.getId()));


                      weEmpleCode.getWeEmpleCodeTags().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
                    weEmpleCode.getWeEmpleCodeTags().stream().forEach(k->{
                        k.setEmpleCodeId(weEmpleCode.getId());
                        k.setId(SnowFlakeUtil.nextId());
                    });

                      weEmpleCodeTagService.saveOrUpdateBatch(weEmpleCode.getWeEmpleCodeTags());


                }
            }

        }catch (Exception e){
           log.error("活码更新失败::"+e.getMessage());

        }

    }

//    /**
//     * 批量删除员工活码
//     *
//     * @param ids 需要删除的员工活码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeEmpleCodeByIds(Long[] ids)
//    {
//        return weEmpleCodeMapper.deleteWeEmpleCodeByIds(ids);
//    }
//

    /**
     * 删除员工活码信息
     *
     * @param id 员工活码ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteWeEmpleCodeById(Long id) {
        WeEmpleCode weEmpleCode = getById(id);
        if (weEmpleCode != null && weEmpleCode.getConfigId() != null) {
            WeExternalContactDto externalContactDto = new WeExternalContactDto();
            externalContactDto.setConfig_id(weEmpleCode.getConfigId());
            weExternalContactClient.delContactWay(externalContactDto);
        }
        // 删除对应的新科拉群信息
        communityNewGroupMapper.removeWeCommunityNewGroupByEmplCodeId(id);
        // 删除员工活码
        return this.baseMapper.deleteWeEmpleCodeById(id);
    }


    /**
     * 批量逻辑删除员工活码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchRemoveWeEmpleCodeIds(List<String> ids) {
        return this.baseMapper.batchRemoveWeEmpleCodeIds(ids);
    }

    @Override
    public WeEmpleCodeDto selectWelcomeMsgByScenario(String scenario, String userId) {
        return this.baseMapper.selectWelcomeMsgByScenario(scenario, userId);
    }

    /**
     * 通过state定位员工活码
     * @param state state
     * @return 员工活码
     */
    @Override
    public WeEmpleCodeDto selectWelcomeMsgByState(String state) {


        WeEmpleCode weEmpleCode = this.getOne(new LambdaQueryWrapper<WeEmpleCode>()
                .eq(WeEmpleCode::getState, state));


        return WeEmpleCodeDto.builder()
                .empleCodeId(weEmpleCode==null?state:String.valueOf(weEmpleCode.getId()))
                .welcomeMsg(weEmpleCode==null?"你好":weEmpleCode.getWelcomeMsg())
                .build();
    }

    /**
     * 批量新增员工活码
     *
     * @param weEmpleCode 员工信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWeEmpleCodeBatch(WeEmpleCode weEmpleCode) {
        weEmpleCode.setCreateTime(new Date());
        weEmpleCode.setCreateBy(SecurityUtils.getUsername());
        weEmpleCode.setCodeType(WeConstans.SINGLE_EMPLE_CODE_TYPE);

        weEmpleCode.setScenario(Optional.ofNullable(weEmpleCode.getScenario())
                .orElse(WeConstans.ONE_PERSON_CODE_GENERATED_BATCH));

        Optional.ofNullable(weEmpleCode).map(WeEmpleCode::getWeEmpleCodeUseScops)
                .orElseGet(ArrayList::new).forEach(useScops -> {
            //机构类型数据返回不执行生成二维码业务
            if (WeConstans.USE_SCOP_BUSINESSID_TYPE_ORG.equals(useScops.getBusinessIdType())) {
                return;
            }
            weEmpleCode.setId(SnowFlakeUtil.nextId());
            weEmpleCode.setState(weEmpleCode.getId().toString());
            List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = new ArrayList<>();
            weEmpleCodeUseScopList.add(useScops);
            weEmpleCode.setWeEmpleCodeUseScops(weEmpleCodeUseScopList);
            addWeEmplCode(weEmpleCode);
        });
    }

    @Override
    public WeExternalContactDto getQrcode(String userIds, String departmentIds) {
        String[] userIdArr = Arrays.stream(userIds.split(","))
                .filter(StringUtils::isNotEmpty).toArray(String[]::new);
        Long[] departmentIdArr = Arrays.stream(departmentIds.split(","))
                .filter(StringUtils::isNotEmpty)
                .map(Long::new).toArray(Long[]::new);
        WeExternalContactDto qrcode = getQrcode(userIdArr, departmentIdArr);
        //设置24小时过期
        log.info("qrcode:>>>>>>>>>>>【{}】", JSONObject.toJSONString(qrcode));
        if(qrcode !=null && qrcode.getConfig_id() != null){
            redisCache.setCacheObject(WeConstans.WE_EMPLE_CODE_KEY+":"+qrcode.getConfig_id(),qrcode.getConfig_id(),24, TimeUnit.HOURS);
        }
        return qrcode;
    }

    @Override
    public WeExternalContactDto getQrcode(String[] userIdArr, Long[] departmentIdArr) {
        WeExternalContactDto.WeContactWay weContactWay = new WeExternalContactDto.WeContactWay();
        //当存在部门id或者用户id大于一个的情况为多人二维码
        if (departmentIdArr.length > 0 || userIdArr.length > 1) {
            weContactWay.setType(WeConstans.MANY_EMPLE_CODE_TYPE);
        } else {
            weContactWay.setType(WeConstans.SINGLE_EMPLE_CODE_TYPE);
        }
        weContactWay.setScene(WeConstans.QR_CODE_EMPLE_CODE_SCENE);
        weContactWay.setUser(userIdArr);
        weContactWay.setParty(departmentIdArr);
        return getQrCode(weContactWay);
    }

    /**
     * 通过成员id 获取去成员活码
     * @param userId 成员id
     * @return
     */
    @Override
    public WeEmpleCode getQrcodeByUserId(String userId) {
        return this.baseMapper.getQrcodeByUserId(userId);
    }

    /**
     * 新增员工活码
     *
     * @param weEmpleCode
     */
    private void addWeEmplCode(WeEmpleCode weEmpleCode) {
        /*List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
        List<String> businessIdList = Optional.ofNullable(weEmpleCodeUseScops).orElseGet(ArrayList::new)
                .stream().map(WeEmpleCodeUseScop::getBusinessId)
                .collect(Collectors.toList());

        List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = iWeEmpleCodeUseScopService.listByIds(businessIdList);
        if (CollectionUtil.isNotEmpty(weEmpleCodeUseScopList)) {
            throw new WeComException("该员工或部门已经创建，无法重复创建");
        }*/
        WeExternalContactDto.WeContactWay weContactWay = getWeContactWay(weEmpleCode);
        WeExternalContactDto qrCode = getQrCode(weContactWay);
        if (qrCode != null) {
            weEmpleCode.setConfigId(qrCode.getConfig_id());
            weEmpleCode.setQrCode(qrCode.getQr_code());
        }

        if (this.save(weEmpleCode)) {
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeUseScops())) {
                weEmpleCode.getWeEmpleCodeUseScops().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
                iWeEmpleCodeUseScopService.saveBatch(weEmpleCode.getWeEmpleCodeUseScops());
            }
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
                weEmpleCode.getWeEmpleCodeTags().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
                weEmpleCodeTagService.saveBatch(weEmpleCode.getWeEmpleCodeTags());
            }
        }
    }

    /**
     * 获取二维码
     *
     * @param weContactWay
     * @return
     */
    public WeExternalContactDto getQrCode(WeExternalContactDto.WeContactWay weContactWay) {
        try {
            return StringUtils.isNotEmpty(weContactWay.getConfig_id())?
                    weExternalContactClient.updateContactWay(weContactWay):
                    weExternalContactClient.addContactWay(weContactWay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 企微接口参数生成方法
     * @param weEmpleCode 员工活码实体类
     * @return 企微接口参数实体类
     */
    public WeExternalContactDto.WeContactWay getWeContactWay(WeEmpleCode weEmpleCode) {
        WeExternalContactDto.WeContactWay weContactWay = new WeExternalContactDto.WeContactWay();
        List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
        //根据类型生成相应的活码
        weContactWay.setConfig_id(weEmpleCode.getConfigId());
        weContactWay.setType(weEmpleCode.getCodeType());
        weContactWay.setScene(WeConstans.QR_CODE_EMPLE_CODE_SCENE);
        weContactWay.setSkip_verify(weEmpleCode.getIsJoinConfirmFriends().equals(new Integer(0))?false:true);
        weContactWay.setState(weEmpleCode.getState()==null?String.valueOf(weEmpleCode.getId()):weEmpleCode.getState());
        if (CollectionUtil.isNotEmpty(weEmpleCodeUseScops)) {
            //员工列表
            String[] userIdArr = weEmpleCodeUseScops.stream().filter(itme ->
                    WeConstans.USE_SCOP_BUSINESSID_TYPE_USER.equals(itme.getBusinessIdType())
                            && StringUtils.isNotEmpty(itme.getBusinessId()))
                    .map(WeEmpleCodeUseScop::getBusinessId).toArray(String[]::new);
            weContactWay.setUser(userIdArr);
            //部门列表
            if (!WeConstans.SINGLE_EMPLE_CODE_TYPE.equals(weEmpleCode.getCodeType())) {
                Long[] partyArr = weEmpleCodeUseScops.stream().filter(itme ->
                        WeConstans.USE_SCOP_BUSINESSID_TYPE_ORG.equals(itme.getBusinessIdType())
                                && StringUtils.isNotEmpty(itme.getBusinessId()))
                        .map(item -> Long.valueOf(item.getBusinessId())).collect(Collectors.toList()).toArray(new Long[]{});
                weContactWay.setParty(partyArr);
            }

        }
        return weContactWay;
    }

    /**
     * 递增扫码次数
     *
     * @param state state
     */
    @Override
    public void updateScanTimesByState(String state) {
        this.baseMapper.updateScanTimesByState(state);
    }
}
