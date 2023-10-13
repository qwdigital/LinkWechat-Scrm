package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.enums.WeErrorCodeEnum;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.community.WeEmpleCode;
import com.linkwechat.domain.community.WeEmpleCodeTag;
import com.linkwechat.domain.community.WeEmpleCodeUseScop;
import com.linkwechat.domain.wecom.query.qr.WeAddWayQuery;
import com.linkwechat.domain.wecom.vo.WeResultVo;
import com.linkwechat.domain.wecom.vo.qr.WeAddWayVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.mapper.WeEmpleCodeMapper;
import com.linkwechat.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeEmpleCodeServiceImpl extends ServiceImpl<WeEmpleCodeMapper, WeEmpleCode> implements IWeEmpleCodeService {


    @Resource
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private IWeEmpleCodeTagService iWeEmpleCodeTagService;


    @Autowired
    private IWeMaterialService materialService;

    @Autowired
    private  IWeEmpleCodeUseScopService iWeEmpleCodeUseScopService;

    @Override
    public WeAddWayVo getWeContactWay(WeEmpleCode weEmpleCode) {

        return qwCustomerClient.addContactWay(this.getWeAddWayQuery(weEmpleCode)).getData();
    }

    private WeAddWayQuery getWeAddWayQuery(WeEmpleCode weEmpleCode){
        List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
        //根据类型生成相应的活码

        WeAddWayQuery weAddWayQuery = WeAddWayQuery.builder()
                .config_id(weEmpleCode.getConfigId())
                .type(weEmpleCode.getCodeType())
                .scene(WeConstans.QR_CODE_EMPLE_CODE_SCENE)
                .skip_verify(weEmpleCode.getIsJoinConfirmFriends().equals(new Integer(0)) ? false : true)
                .state(weEmpleCode.getState() == null ? String.valueOf(weEmpleCode.getId()) : weEmpleCode.getState())
                .build();

        if (CollectionUtil.isNotEmpty(weEmpleCodeUseScops)) {
            //员工列表
            List<String> userIdArr = weEmpleCodeUseScops.stream().filter(itme ->
                            WeConstans.USE_SCOP_BUSINESSID_TYPE_USER.equals(itme.getBusinessIdType())
                                    && StringUtils.isNotEmpty(itme.getBusinessId()))
                    .map(WeEmpleCodeUseScop::getBusinessId).collect(Collectors.toList());
            weAddWayQuery.setUser(userIdArr);
            //部门列表
            if (!WeConstans.SINGLE_EMPLE_CODE_TYPE.equals(weEmpleCode.getCodeType())) {
                List<Long> partyArr = weEmpleCodeUseScops.stream().filter(itme ->
                                WeConstans.USE_SCOP_BUSINESSID_TYPE_ORG.equals(itme.getBusinessIdType())
                                        && StringUtils.isNotEmpty(itme.getBusinessId()))
                        .map(item -> Long.valueOf(item.getBusinessId())).collect(Collectors.toList());
                weAddWayQuery.setParty(partyArr);
            }

        }
        return weAddWayQuery;
    }


    @Override
    public WeEmpleCode selectWeEmpleCodeById(Long id) {

        WeEmpleCode weEmpleCode = this.baseMapper.selectWeEmpleCodeById(id);



        if(null != weEmpleCode){

            if(null != weEmpleCode.getMediaId()){
                weEmpleCode.setWeMaterial(
                        materialService.getById(weEmpleCode.getMediaId())
                );
            }

            weEmpleCode.setWeEmpleCodeTags(
                    iWeEmpleCodeTagService.list(new LambdaQueryWrapper<WeEmpleCodeTag>()
                            .eq(WeEmpleCodeTag::getEmpleCodeId,weEmpleCode.getId()))
            );

        }

        return weEmpleCode;
    }

    @Override
    public void updateWeContactWay(WeEmpleCode weEmpleCode) {
        qwCustomerClient.updateContactWay(this.getWeAddWayQuery(weEmpleCode));
    }


//    /**
//     * 修改员工活码
//     *
//     * @param weEmpleCode 员工活码
//     * @return 结果
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateWeEmpleCode(WeEmpleCode weEmpleCode) {
//
//        if(StringUtils.isEmpty(weEmpleCode.getState())){
//            weEmpleCode.setState(String.valueOf(weEmpleCode.getId()));
//        }
//
//
//
//        AjaxResult<WeResultVo> weResultVoAjaxResult = qwCustomerClient.updateContactWay(this.getWeAddWayQuery(weEmpleCode));
//        if(null != weResultVoAjaxResult){
//            WeResultVo weResultVo = weResultVoAjaxResult.getData();
//
//            if(weResultVo !=null &&  weResultVo.getErrCode().equals(WeErrorCodeEnum.ERROR_CODE_0.getErrorCode())){
//                if (this.updateById(weEmpleCode)) {
//                    if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeUseScops())) {
//
//                        //移除原有的记录
//                        if(iWeEmpleCodeUseScopService.remove(new LambdaQueryWrapper<WeEmpleCodeUseScop>()
//                                .eq(WeEmpleCodeUseScop::getEmpleCodeId,weEmpleCode.getId()))){
//
//                            weEmpleCode.getWeEmpleCodeUseScops().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
//                            iWeEmpleCodeUseScopService.saveOrUpdateBatch(weEmpleCode.getWeEmpleCodeUseScops());
//
//                        }
//
//                    }
//                    if (CollectionUtil.isNotEmpty(weEmpleCode.getWeEmpleCodeTags())) {
//
//                        //有id的更新
//                        iWeEmpleCodeTagService.remove(new LambdaQueryWrapper<WeEmpleCodeTag>()
//                                .eq(WeEmpleCodeTag::getEmpleCodeId,weEmpleCode.getId()));
//
//
//                        weEmpleCode.getWeEmpleCodeTags().forEach(item -> item.setEmpleCodeId(weEmpleCode.getId()));
//                        weEmpleCode.getWeEmpleCodeTags().stream().forEach(k->{
//                            k.setEmpleCodeId(weEmpleCode.getId());
//                            k.setId(SnowFlakeUtil.nextId());
//                        });
//
//                        iWeEmpleCodeTagService.saveOrUpdateBatch(weEmpleCode.getWeEmpleCodeTags());
//
//
//                    }
//                }
//            }
//
//        }
//
//
//
//    }
//

}
