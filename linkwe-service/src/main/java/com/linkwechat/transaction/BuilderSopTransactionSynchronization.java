package com.linkwechat.transaction;

import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.sop.dto.WeSopBaseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

@Component
public class BuilderSopTransactionSynchronization extends TransactionSynchronizationAdapter {
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQSettingConfig rabbitMQSettingConfig;
    private final WeSopBaseDto weSopBaseDto;

    public BuilderSopTransactionSynchronization(RabbitTemplate rabbitTemplate, RabbitMQSettingConfig rabbitMQSettingConfig, WeSopBaseDto weSopBaseDto) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQSettingConfig = rabbitMQSettingConfig;
        this.weSopBaseDto = weSopBaseDto;
    }

    @Override
    public void afterCommit() {
        super.afterCommit();
        rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getSopEx(), rabbitMQSettingConfig.getSopRk(), weSopBaseDto);
    }
}
