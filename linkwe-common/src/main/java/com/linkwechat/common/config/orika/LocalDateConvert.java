package com.linkwechat.common.config.orika;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * LocalDateTime, Date之间转换器
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/07/07 11:40
 */
public class LocalDateConvert extends BidirectionalConverter<LocalDateTime, Date> {

    @Override
    public Date convertTo(LocalDateTime source, Type<Date> destinationType, MappingContext mappingContext) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = source.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    @Override
    public LocalDateTime convertFrom(Date source, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = source.toInstant();
        return instant.atZone(zoneId).toLocalDateTime();
    }
}
