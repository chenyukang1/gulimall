package com.cyk.gulimall.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The class RabbitMQConfig.
 *
 * @author chenyukang
 * @email chen.yukang@qq.com
 * @date 2024/6/17
 **/
@Configuration
@Slf4j
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        // 1、只要消息抵达Broker就ack=true
        // correlationData：当前消息的唯一关联数据(这个是消息的唯一id)
        // ack：消息是否成功收到
        // cause：失败的原因
        rabbitTemplate.setConfirmCallback((correlationData,ack,cause) -> log.info("confirm...correlationData["+correlationData+"]==>ack:["+ack+"]==>cause:["+cause+"]"));

        // 只要消息没有投递给指定的队列，就触发这个失败回调
        // message：投递失败的消息详细信息
        // replyCode：回复的状态码
        // replyText：回复的文本内容
        // exchange：当时这个消息发给哪个交换机
        // routingKey：当时这个消息用哪个路邮键
        rabbitTemplate.setReturnCallback((message,replyCode,replyText,exchange,routingKey) ->
                log.info("Fail Message["+message+"]==>replyCode["+replyCode+"]" +
                "==>replyText["+replyText+"]==>exchange["+exchange+"]==>routingKey["+routingKey+"]"));
        return rabbitTemplate;
    }

}
