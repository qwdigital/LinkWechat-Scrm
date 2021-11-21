package com.linkwechat.wecom.factory.impl.tag;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.wecom.domain.callback.WeBackBaseVo;
import com.linkwechat.wecom.domain.callback.WeBackUserTagVo;
import com.linkwechat.wecom.domain.WeCustomer;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeFlowerCustomerTagRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author danmo
 * @description 标签变更事件
 * @date 2021/1/20 23:10
 **/
@Slf4j
@Component("update_tag")
public class WeCallBackUpdateUserTagImpl extends WeEventStrategy {
    @Autowired
    private IWeFlowerCustomerTagRelService weFlowerCustomerTagRelService;

    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackUserTagVo userTagInfo = (WeBackUserTagVo) message;
        try {
            String tagId = userTagInfo.getTagId();
            //标签中新增的成员userid列表，用逗号分隔
            List<String> addUserItemsList = Arrays.stream(Optional.ofNullable(userTagInfo.getAddUserItems())
                    .orElse("").split(",")).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(addUserItemsList)){

                //获取员工对应的所有客户
                List<WeCustomer> weCustomers = iWeCustomerService.list(new LambdaQueryWrapper<WeCustomer>()
                        .in(WeCustomer::getFirstUserId, addUserItemsList));

                if(CollectionUtil.isNotEmpty(weCustomers)){
                    List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
                    weCustomers.stream().forEach(k->{
                        weFlowerCustomerTagRels.add(
                                WeFlowerCustomerTagRel.builder()
                                        .userId(k.getFirstUserId())
                                        .externalUserid(k.getExternalUserid())
                                        .tagId(tagId)
                                        .createTime(new Date())
                                        .build()
                        );
                    });
                    weFlowerCustomerTagRelService.batchAddOrUpdate(weFlowerCustomerTagRels);
                }
            }


            //标签中删除的成员userid列表，用逗号分隔
            List<String> delUserItemsList = Arrays.stream(Optional.ofNullable(userTagInfo.getDelUserItems())
                    .orElse("").split(",")).collect(Collectors.toList());

            if(CollectionUtil.isNotEmpty(delUserItemsList)){
                weFlowerCustomerTagRelService.remove(new LambdaQueryWrapper<WeFlowerCustomerTagRel>()
                        .in(WeFlowerCustomerTagRel::getTagId,delUserItemsList));
            }


        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
