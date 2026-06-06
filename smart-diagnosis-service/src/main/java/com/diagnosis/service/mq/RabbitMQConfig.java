package com.diagnosis.service.mq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 * 定义问诊消息队列、死信队列、延迟队列
 */
@Configuration
public class RabbitMQConfig {

    public static final String DIAGNOSIS_QUEUE = "diagnosis.queue";
    public static final String DIAGNOSIS_EXCHANGE = "diagnosis.exchange";
    public static final String DIAGNOSIS_DLQ = "diagnosis.dlq";
    public static final String DIAGNOSIS_DLX = "diagnosis.dlx";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    /** 死信交换机 */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DIAGNOSIS_DLX);
    }

    /** 死信队列 */
    @Bean
    public Queue dlqQueue() {
        return QueueBuilder.durable(DIAGNOSIS_DLQ).build();
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue()).to(dlxExchange()).with("dlq");
    }

    /** 业务队列（绑定死信） */
    @Bean
    public Queue diagnosisQueue() {
        return QueueBuilder.durable(DIAGNOSIS_QUEUE)
                .withArgument("x-dead-letter-exchange", DIAGNOSIS_DLX)
                .withArgument("x-dead-letter-routing-key", "dlq")
                .build();
    }

    @Bean
    public DirectExchange diagnosisExchange() {
        return new DirectExchange(DIAGNOSIS_EXCHANGE);
    }

    @Bean
    public Binding diagnosisBinding() {
        return BindingBuilder.bind(diagnosisQueue()).to(diagnosisExchange()).with("diagnosis");
    }
}
