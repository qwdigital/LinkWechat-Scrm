package com.linkwechat.wecom.factory.impl.tag;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.wecom.domain.WeFlowerCustomerRel;
import com.linkwechat.wecom.domain.WeFlowerCustomerTagRel;
import com.linkwechat.wecom.domain.vo.WxCpXmlMessageVO;
import com.linkwechat.wecom.factory.WeEventStrategy;
import com.linkwechat.wecom.service.IWeFlowerCustomerRelService;
import com.linkwechat.wecom.service.IWeFlowerCustomerTagRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private IWeFlowerCustomerRelService weFlowerCustomerRelService;

    @Override
    public void eventHandle(WxCpXmlMessageVO message) {
        try {
            String tagId = message.getTagId();
            //标签中新增的成员userid列表，用逗号分隔
            List<String> addUserItemsList = Arrays.stream(Optional.ofNullable(message.getAddUserItems())
                    .orElse("").split(",")).collect(Collectors.toList());
            //标签中删除的成员userid列表，用逗号分隔
            List<String> delUserItemsList = Arrays.stream(Optional.ofNullable(message.getDelUserItems())
                    .orElse("").split(",")).collect(Collectors.toList());

            //标签中新增的成员userid列表，建立关联
            List<WeFlowerCustomerTagRel> weFlowerCustomerTagRels = new ArrayList<>();
            LambdaQueryWrapper<WeFlowerCustomerRel> relLambdaQueryWrapper = new LambdaQueryWrapper<>();
            relLambdaQueryWrapper.in(WeFlowerCustomerRel::getUserId, addUserItemsList);
            List<WeFlowerCustomerRel> flowerCustomerRelList = weFlowerCustomerRelService.list(relLambdaQueryWrapper);
            List<Long> idList = Optional.ofNullable(flowerCustomerRelList).orElseGet(ArrayList::new)
                    .stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
            idList.forEach(id -> {
                weFlowerCustomerTagRels.add(WeFlowerCustomerTagRel.builder().flowerCustomerRelId(id)
                        .tagId(tagId)
                        .build());
            });
            weFlowerCustomerTagRelService.batchInsetWeFlowerCustomerTagRel(weFlowerCustomerTagRels);

            //当前标签对应成员列表
            LambdaQueryWrapper<WeFlowerCustomerTagRel> tagRelLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tagRelLambdaQueryWrapper.eq(WeFlowerCustomerTagRel::getTagId, tagId);
            List<WeFlowerCustomerTagRel> tagRelList = weFlowerCustomerTagRelService.list(tagRelLambdaQueryWrapper);

            List<Long> flowerCustomerRelIdList = Optional.ofNullable(tagRelList).orElseGet(ArrayList::new)
                    .stream().map(WeFlowerCustomerTagRel::getFlowerCustomerRelId).collect(Collectors.toList());

            if (!flowerCustomerRelIdList.isEmpty()) {
                LambdaQueryWrapper<WeFlowerCustomerRel> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(WeFlowerCustomerRel::getId, flowerCustomerRelIdList)
                        .in(WeFlowerCustomerRel::getUserId, delUserItemsList);
                List<WeFlowerCustomerRel> relList = weFlowerCustomerRelService.list(queryWrapper);
                List<Long> relIdList = Optional.ofNullable(relList).orElseGet(ArrayList::new)
                        .stream().map(WeFlowerCustomerRel::getId).collect(Collectors.toList());
                //标签中删除的成员userid列表
                LambdaQueryWrapper<WeFlowerCustomerTagRel> tagRelQueryWrapper = new LambdaQueryWrapper<>();
                tagRelQueryWrapper.in(WeFlowerCustomerTagRel::getFlowerCustomerRelId, relIdList);
                weFlowerCustomerTagRelService.remove(tagRelQueryWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
