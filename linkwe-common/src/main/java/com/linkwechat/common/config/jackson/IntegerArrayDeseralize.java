package com.linkwechat.common.config.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * @Author: Robin
 * @Description:
 * @Date: create in 2020/9/15 0015 1:09
 */
public class IntegerArrayDeseralize extends JsonDeserializer<Integer[]> {
    @Override
    public Integer[] deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        System.out.println(jsonParser.getText());
        return new Integer[0];
    }
}
