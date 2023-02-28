package com.linkwechat.typeHandler;

import com.alibaba.fastjson.TypeReference;
import com.linkwechat.common.typeHandler.ListTypeHandler;
import com.linkwechat.domain.WeSysFieldTemplate;
import java.util.List;

public class WeCustomerInfoExpandListTypeHandler extends ListTypeHandler<WeSysFieldTemplate.OtherContent> {


    @Override
    protected TypeReference<List<WeSysFieldTemplate.OtherContent>> specificType() {
        return new TypeReference<List<WeSysFieldTemplate.OtherContent>>() {
        };
    }

}
