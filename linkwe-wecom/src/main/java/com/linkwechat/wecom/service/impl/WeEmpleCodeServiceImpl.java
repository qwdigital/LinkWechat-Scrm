package com.linkwechat.wecom.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class WeEmpleCodeServiceImpl extends ServiceImpl<WeEmpleCodeMapper,WeEmpleCode> implements IWeEmpleCodeService
{
//    @Autowired
//    private WeEmpleCodeMapper weEmpleCodeMapper;


    @Autowired
    private IWeEmpleCodeTagService weEmpleCodeTagService;


    @Autowired
    private IWeEmpleCodeUseScopService iWeEmpleCodeUseScopService;


//    @Autowired
//    private WeExternalContactClient weExternalContactClient;

//    /**
//     * 查询员工活码
//     *
//     * @param id 员工活码ID
//     * @return 员工活码
//     */
//    @Override
//    public WeEmpleCode selectWeEmpleCodeById(Long id)
//    {
////        WeEmpleCode weEmpleCode = weEmpleCodeMapper.selectWeEmpleCodeById(id);
////        if(null != weEmpleCode){
////            weEmpleCode.setWeEmpleCodeTags(
////                    weEmpleCodeTagService.selectWeEmpleCodeTagListById(id)
////            );
////            weEmpleCode.setWeEmpleCodeUseScops(
////                    iWeEmpleCodeUseScopService.selectWeEmpleCodeUseScopListById(id)
////            );
//
////        }
////        return weEmpleCode;
//    }

    /**
     * 查询员工活码列表
     *
     * @param weEmpleCode 员工活码
     * @return 员工活码
     */
    @Override
    public List<WeEmpleCode> selectWeEmpleCodeList(WeEmpleCode weEmpleCode)
    {
        return this.baseMapper.selectWeEmpleCodeList(weEmpleCode);
    }

    /**
     * 新增员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWeEmpleCode(WeEmpleCode weEmpleCode)
    {

         if(this.save(weEmpleCode)){
             List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
             if(CollectionUtil.isNotEmpty(weEmpleCodeUseScops)){
                 iWeEmpleCodeUseScopService.saveBatch(weEmpleCodeUseScops);

                 //根据类型生成相应的活码农
             }
             weEmpleCodeTagService.saveBatch(weEmpleCode.getWeEmpleCodeTags());

         }


    }

    /**
     * 修改员工活码
     * 
     * @param weEmpleCode 员工活码
     * @return 结果
     */
    @Override
    public void updateWeEmpleCode(WeEmpleCode weEmpleCode)
    {

        if(this.updateById(weEmpleCode)){
            List<WeEmpleCodeUseScop> weEmpleCodeUseScops = weEmpleCode.getWeEmpleCodeUseScops();
            if(CollectionUtil.isNotEmpty(weEmpleCodeUseScops)){
                iWeEmpleCodeUseScopService.updateBatchById(weEmpleCodeUseScops);

                //根据类型生成相应的活码农
            }
            weEmpleCodeTagService.updateBatchById(weEmpleCode.getWeEmpleCodeTags());

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
//    /**
//     * 删除员工活码信息
//     *
//     * @param id 员工活码ID
//     * @return 结果
//     */
//    @Override
//    public int deleteWeEmpleCodeById(Long id)
//    {
//        return weEmpleCodeMapper.deleteWeEmpleCodeById(id);
//    }
//
//
//    /**
//     * 批量逻辑删除员工活码
//     *
//     * @param ids 需要删除的数据ID
//     * @return 结果
//     */
//    @Override
//    public int batchRemoveWeEmpleCodeIds(List<String> ids) {
//
//        return weEmpleCodeMapper.batchRemoveWeEmpleCodeIds(ids);
//    }
}
