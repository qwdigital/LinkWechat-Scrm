package com.linkwechat.wecom.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.utils.DateUtils;
import com.linkwechat.wecom.client.WeExternalContactClient;
import com.linkwechat.wecom.domain.WeEmpleCodeTag;
import com.linkwechat.wecom.domain.WeEmpleCodeUseScop;
import com.linkwechat.wecom.domain.dto.WeExternalContactDto;
import com.linkwechat.wecom.service.IWeEmpleCodeTagService;
import com.linkwechat.wecom.service.IWeEmpleCodeUseScopService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeEmpleCodeMapper;
import com.linkwechat.wecom.domain.WeEmpleCode;
import com.linkwechat.wecom.service.IWeEmpleCodeService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 员工活码Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-10-04
 */
@Service
public class WeEmpleCodeServiceImpl implements IWeEmpleCodeService 
{
    @Autowired
    private WeEmpleCodeMapper weEmpleCodeMapper;


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
    public WeEmpleCode selectWeEmpleCodeById(Long id)
    {
        WeEmpleCode weEmpleCode = weEmpleCodeMapper.selectWeEmpleCodeById(id);
        if(null != weEmpleCode){
            weEmpleCode.setWeEmpleCodeTags(
                    weEmpleCodeTagService.selectWeEmpleCodeTagListById(id)
            );
            weEmpleCode.setWeEmpleCodeUseScops(
                    iWeEmpleCodeUseScopService.selectWeEmpleCodeUseScopListById(id)
            );

        }
        return weEmpleCode;
    }

    /**
     * 查询员工活码列表
     * 
     * @param weEmpleCode 员工活码
     * @return 员工活码
     */
    @Override
    public List<WeEmpleCode> selectWeEmpleCodeList(WeEmpleCode weEmpleCode)
    {
        return weEmpleCodeMapper.selectWeEmpleCodeList(weEmpleCode);
    }

    /**
     * 新增员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    @Transactional
    public int insertWeEmpleCode(WeEmpleCode weEmpleCode)
    {
        int returnCode = weEmpleCodeMapper.insertWeEmpleCode(weEmpleCode);

        if(returnCode>0){
            List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
            if(CollectionUtil.isNotEmpty(weEmpleCodeUseScops)){
                weEmpleCodeUseScops.stream().forEach(v->v.setEmpleCodeId(weEmpleCode.getId()));
                //批量保存使用员工
                iWeEmpleCodeUseScopService.batchInsetWeEmpleCodeUseScop(weEmpleCodeUseScops);


//                List<WeEmpleCodeTag> weEmpleCodeTags = weEmpleCode.getWeEmpleCodeTags().stream().filter(v->v.getTagId()!=null).collect(Collectors.toList());
//                if(CollectionUtil.isNotEmpty(weEmpleCodeTags)){
//                    //批量保存标签
//                    weEmpleCodeTags.stream().forEach(v->v.setEmpleCodeId(weEmpleCode.getId()));
//                    weEmpleCodeTagService.batchInsetWeEmpleCodeTag(weEmpleCodeTags);
//                }



                if(!weEmpleCode.getCodeType().equals(WeConstans.BATCH_SINGLE_EMPLE_CODE_TYPE)){

//
                    WeExternalContactDto.WeContactWay weContactWay = new WeExternalContactDto.WeContactWay( weEmpleCode.getCodeType(),
                            WeConstans.QR_CODE_EMPLE_CODE_SCENE,weEmpleCode.getIsJoinConfirmFriends());



                    if(weEmpleCode.getCodeType().equals(WeConstans.SINGLE_EMPLE_CODE_TYPE)){
                              weContactWay.setUser(
                                      ArrayUtil.toArray(weEmpleCodeUseScops.stream().map(WeEmpleCodeUseScop::getUseUserId)
                                              .map(x -> String.valueOf(x))
                                              .collect(Collectors.toList()), String.class)
                              );//设置员工id
                    }else if(weEmpleCode.getCodeType().equals(WeConstans.SINGLE_EMPLE_CODE_TYPE)){
                        weContactWay.setParty(ArrayUtil.toArray(weEmpleCodeUseScops.stream().map(WeEmpleCodeUseScop::getUseUserId)
                                .collect(Collectors.toList()), Long.class)); //设置部门id
                    }

                    //生成员工活码
                    WeExternalContactDto weExternalContactDto = weExternalContactClient.addContactWay(
                            weContactWay
                    );

                    System.out.println(weExternalContactDto.toString());

//                    if(weExternalContactDto.getErrcode().equals(WeConstans.WE_SUCCESS_CODE)){
//                        //二维码等数据入库
//
//                    }

                }




            }





        }
        return returnCode;
    }

    /**
     * 修改员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    public int updateWeEmpleCode(WeEmpleCode weEmpleCode)
    {
        int returnCode = weEmpleCodeMapper.updateWeEmpleCode(weEmpleCode);

        if(returnCode>0){
            List<WeEmpleCodeTag> weEmpleCodeTags = weEmpleCode.getWeEmpleCodeTags();
            if(CollectionUtil.isNotEmpty(weEmpleCodeTags)){
                //删除原有的
                weEmpleCodeTagService.batchRemoveWeEmpleCodeTagIds(ListUtil.toList(weEmpleCode.getId()));
                //保存新的
                weEmpleCodeTagService.batchInsetWeEmpleCodeTag(weEmpleCodeTags);


            }
            List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
            if(CollectionUtil.isNotEmpty(weEmpleCodeUseScops)){
                //删除原有的
                iWeEmpleCodeUseScopService.batchRemoveWeEmpleCodeUseScopIds(ListUtil.toList(weEmpleCode.getId()));
                //保存新的
                iWeEmpleCodeUseScopService.batchInsetWeEmpleCodeUseScop(weEmpleCodeUseScops);

            }


        }

        return returnCode;
    }

    /**
     * 批量删除员工活码
     * 
     * @param ids 需要删除的员工活码ID
     * @return 结果
     */
    @Override
    public int deleteWeEmpleCodeByIds(Long[] ids)
    {
        return weEmpleCodeMapper.deleteWeEmpleCodeByIds(ids);
    }

    /**
     * 删除员工活码信息
     * 
     * @param id 员工活码ID
     * @return 结果
     */
    @Override
    public int deleteWeEmpleCodeById(Long id)
    {
        return weEmpleCodeMapper.deleteWeEmpleCodeById(id);
    }


    /**
     * 批量逻辑删除员工活码
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int batchRemoveWeEmpleCodeIds(List<Long> ids) {

        return weEmpleCodeMapper.batchRemoveWeEmpleCodeIds(ids);
    }
}
