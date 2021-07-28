package com.linkwechat.framework.config;

import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.framework.listener.ChatMsgCheckListener;
import com.linkwechat.framework.listener.ChatMsgListener;
import com.linkwechat.framework.listener.EmpleCodeExpiredListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * redis配置
 * 
 * @author ruoyi
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport
{

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }



    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(MessageListenerAdapter chatMsgAdapter,
                                                                       MessageListenerAdapter chatMsgCheckAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        //定义监听渠道
        Topic msgChannel = new ChannelTopic(WeConstans.CONVERSATION_MSG_CHANNEL);
        //定义监听器监听的Redis的消息
        redisMessageListenerContainer.addMessageListener(chatMsgAdapter,msgChannel);
        redisMessageListenerContainer.addMessageListener(chatMsgCheckAdapter,msgChannel);
        return redisMessageListenerContainer;
    }

    @Bean
    public EmpleCodeExpiredListener codeExpiredListener() {
        return new EmpleCodeExpiredListener(this.redisMessageListenerContainer(null,null));
    }

    @Bean
    MessageListenerAdapter chatMsgAdapter(ChatMsgListener receiver){
        return new MessageListenerAdapter(receiver);
    }

    @Bean
    MessageListenerAdapter chatMsgCheckAdapter(ChatMsgCheckListener receiver){
        return new MessageListenerAdapter(receiver);
    }
}
