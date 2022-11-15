package com.linkwechat.config.common;

import com.linkwechat.common.enums.CustomerAddWay;
import com.linkwechat.common.enums.strategiccrowd.*;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author danmo
 * @description
 * @date 2022/07/10 14:33
 **/
@Configuration
@Component
public class EnumConfig {

    @Bean(name = "commonEnumMap")
    public CommonEnumMap commonEnumMap() {
        CommonEnumMap commonEnumMap = new CommonEnumMap();
        Map<String, Object> resMap = new HashMap<>(16);
        resMap.put("CorpAddStateEnum", CorpAddStateEnum.getType());
        resMap.put("CustomerAttributesEnum", CustomerAttributesEnum.getType());
        resMap.put("CustomerBehaviorTimeEnum", CustomerBehaviorTimeEnum.getType());
        resMap.put("CustomerBehaviorEnum", CustomerBehaviorEnum.getType());
        resMap.put("CustomerBehaviorTypeEnum", CustomerBehaviorTypeEnum.getType());
        resMap.put("RelationEnum", RelationEnum.getType());
        resMap.put("CustomerAddWay", CustomerAddWay.getType());
        commonEnumMap.setValue(resMap);
        return commonEnumMap;
    }

    @Data
    public class CommonEnumMap {
        private Map<String, Object> value;
    }
}
