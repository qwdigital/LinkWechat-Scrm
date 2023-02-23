package com.linkwechat.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.enums.fieldtempl.CustomerPortraitFieldTempl;
import com.linkwechat.common.utils.InviteCodeUtils;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeSysFieldTemplate;
import com.linkwechat.mapper.WeSysFieldTemplateMapper;
import com.linkwechat.service.IWeSysFieldTemplateService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author robin
* @description 针对表【we_sys_field_template(自定义表单字段模板表)】的数据库操作Service实现
* @createDate 2023-02-01 14:16:43
*/
@Service
public class WeSysFieldTemplateServiceImpl extends ServiceImpl<WeSysFieldTemplateMapper, WeSysFieldTemplate> implements IWeSysFieldTemplateService {


    @Override
    public List<WeSysFieldTemplate> findLists(WeSysFieldTemplate sysFieldTemplate) {



        return this.baseMapper.findLists(
                new LambdaQueryWrapper<WeSysFieldTemplate>()
                        .like(StringUtils.isNotEmpty(sysFieldTemplate.getLabelName())
                                ,WeSysFieldTemplate::getLabelName,sysFieldTemplate.getLabelName())
                        .eq(StringUtils.isNotEmpty(sysFieldTemplate.getVisualTagIds()),WeSysFieldTemplate::getVisualTagIds,sysFieldTemplate.getVisualTagIds())
                        .eq(sysFieldTemplate.getType() != null ,WeSysFieldTemplate::getType,sysFieldTemplate.getType())
                        .eq(WeSysFieldTemplate::getDelFlag, Constants.COMMON_STATE)
                        .orderByDesc(WeSysFieldTemplate::getLabelSort)
        );
    }

    @Override
    public void initBaseSysField() {

        //校验当前租户下是否存在不存在则初始化
        if(this.count(new LambdaQueryWrapper<WeSysFieldTemplate>()
                .eq(WeSysFieldTemplate::getIsDefault,1))<=0){

            List<WeSysFieldTemplate> templates=new ArrayList<>();

            CustomerPortraitFieldTempl[] values = CustomerPortraitFieldTempl.values();
            for(CustomerPortraitFieldTempl value:values){
                WeSysFieldTemplate fieldTemplate = WeSysFieldTemplate.builder()
                        .labelName(value.getLabelName())
                        .labelVal(value.getLabelVal())
                        .labelSort(value.getSort())
                        .type(value.getLabelType())
                        .isDefault(1)
                        .build();
                templates.add(fieldTemplate);
            }
            this.saveBatch(templates);


        }


    }

    @Override
    public void addSysField(WeSysFieldTemplate sysFieldTemplate) {
        //设置对应的val
        sysFieldTemplate.setLabelVal(InviteCodeUtils.generateInviteCode());
        List<WeSysFieldTemplate.OtherContent> otherContents = sysFieldTemplate.getOtherContent();
        if(CollectionUtil.isNotEmpty(otherContents)){
            otherContents.stream().forEach(otherContent -> {
                otherContent.setLabelVal(InviteCodeUtils.generateInviteCode());
            });
        }
        this.save(sysFieldTemplate);
    }

    @Override
    public void updateSysField(WeSysFieldTemplate sysFieldTemplate) {

        List<WeSysFieldTemplate.OtherContent> otherContents
                = sysFieldTemplate.getOtherContent();
        if(CollectionUtil.isNotEmpty(otherContents)){

            otherContents.stream().forEach(otherContent -> {

                if(StringUtils.isEmpty(otherContent.getLabelVal())){
                    otherContent.setLabelVal(InviteCodeUtils.generateInviteCode());
                }

            });

        }


        this.updateById(sysFieldTemplate);


    }


}
