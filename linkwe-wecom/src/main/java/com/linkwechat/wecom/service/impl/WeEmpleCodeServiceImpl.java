package com.linkwechat.wecom.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.mapper.WeEmpleCodeMapper;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import com.linkwechat.wecom.service.IWeEmpleCodeTagService;
import com.linkwechat.wecom.service.IWeEmpleCodeUseScopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工活码Service业务层处理
 *
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeEmpleCodeServiceImpl extends ServiceImpl<WeEmpleCodeMapper, WeEmpleCode> implements IWeEmpleCodeService {

    @Autowired
    private IWeEmpleCodeTagService weEmpleCodeTagService;


    @Autowired
    private IWeEmpleCodeUseScopService iWeEmpleCodeUseScopService;


    @Autowired
    private WeExternalContactClient weExternalContactClient;

    /**
     * 查询员工活码
     *
     * @param id 员工活码ID
     * @return 员工活码
     */
    @Override
    public WeEmpleCode selectWeEmpleCodeById(Long id) {
        return this.baseMapper.selectWeEmpleCodeById(id);
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
        if (weEmpleCodeList !=null){
            weEmpleCodeList.forEach(empleCode ->{
                List<WeEmpleCodeUseScop> weEmpleCodeUseScopList = empleCode.getWeEmpleCodeUseScops();
                if (CollectionUtil.isNotEmpty(weEmpleCodeUseScopList)){
                    String useUserName = weEmpleCodeUseScopList.stream().map(WeEmpleCodeUseScop::getBusinessName)
                            .collect(Collectors.joining(","));
                    empleCode.setUseUserName(useUserName);
                    String mobile = weEmpleCodeUseScopList.stream().map(WeEmpleCodeUseScop::getMobile)
                            .collect(Collectors.joining(","));
                    empleCode.setMobile(mobile);
                }
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
        WeExternalContactDto.WeContactWay weContactWay = getWeContactWay(weEmpleCode);
        try {
            WeExternalContactDto weExternalContactDto = weExternalContactClient.addContactWay(weContactWay);
            //新增联系方式的配置id
            String configId = weExternalContactDto.getConfig_id();
            //联系我二维码链接
            String qrCode = weExternalContactDto.getQr_code();
            weEmpleCode.setConfigId(configId);
            weEmpleCode.setQrCode(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.baseMapper.insertWeEmpleCode(weEmpleCode) == 1) {
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
     * 修改员工活码
     *
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    public void updateWeEmpleCode(WeEmpleCode weEmpleCode) {
        WeExternalContactDto.WeContactWay weContactWay = getWeContactWay(weEmpleCode);
        try {
            weExternalContactClient.updateContactWay(weContactWay);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.baseMapper.updateWeEmpleCode(weEmpleCode) == 1) {
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeUseScops())) {
                weEmpleCode.getWeEmpleCodeUseScops().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
                iWeEmpleCodeUseScopService.updateBatchById(weEmpleCode.getWeEmpleCodeUseScops());
            }
            if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
                weEmpleCode.getWeEmpleCodeTags().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
                weEmpleCodeTagService.updateBatchById(weEmpleCode.getWeEmpleCodeTags());
            }
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
    public int deleteWeEmpleCodeById(Long id) {
        WeEmpleCode weEmpleCode = getById(id);
        if (weEmpleCode != null && weEmpleCode.getConfigId() != null) {
            weExternalContactClient.delContactWay(weEmpleCode.getConfigId());
        }
        return this.baseMapper.deleteWeEmpleCodeById(id);
    }


    /**
     * 批量逻辑删除员工活码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int batchRemoveWeEmpleCodeIds(List<String> ids) {
        return this.baseMapper.batchRemoveWeEmpleCodeIds(ids);
    }

    private WeExternalContactDto.WeContactWay getWeContactWay(WeEmpleCode weEmpleCode) {
        WeExternalContactDto.WeContactWay weContactWay = new WeExternalContactDto.WeContactWay();
        List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
        //根据类型生成相应的活码农
        weContactWay.setConfig_id(weEmpleCode.getConfigId());
        weContactWay.setType(weEmpleCode.getCodeType());
        weContactWay.setScene(2);
        weContactWay.setSkip_verify(weEmpleCode.getIsJoinConfirmFriends());
        weContactWay.setState(weEmpleCode.getActivityScene());
        if (CollectionUtil.isNotEmpty(weEmpleCodeUseScops)) {
            String[] userIdArr = weEmpleCodeUseScops.stream().filter(itme -> 2 == itme.getBusinessIdType())
                    .map(WeEmpleCodeUseScop::getBusinessId).toArray(String[]::new);
            weContactWay.setUser(userIdArr);
            Long[] partyArr = weEmpleCodeUseScops.stream().filter(itme -> 1 == itme.getBusinessIdType())
                    .map(WeEmpleCodeUseScop::getBusinessId).toArray(Long[]::new);
            weContactWay.setParty(partyArr);
        }
        return weContactWay;
    }
}
