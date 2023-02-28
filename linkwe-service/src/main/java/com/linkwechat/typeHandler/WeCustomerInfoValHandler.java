package com.linkwechat.typeHandler;


import com.alibaba.fastjson.TypeReference;
import com.linkwechat.common.typeHandler.ListTypeHandler;
import com.linkwechat.domain.WeCustomerInfoExpand;
import java.util.List;

public class WeCustomerInfoValHandler extends ListTypeHandler< WeCustomerInfoExpand.CustomerInfoExpand> {
    @Override
    protected TypeReference<List<WeCustomerInfoExpand.CustomerInfoExpand>> specificType() {
        return new TypeReference<List<WeCustomerInfoExpand.CustomerInfoExpand>>() {
        };
    }
}
