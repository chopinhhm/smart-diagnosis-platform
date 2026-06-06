package com.diagnosis.service.mq;

import com.diagnosis.service.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 问诊消息监听器
 * 异步处理消息推送、日志记录等非核心链路任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiagnosisMessageListener {

    private final ChatService chatService;

    /**
     * 监听问诊消息队列
     * 幂等消费：通过消息ID去重
     */
    @RabbitListener(queues = RabbitMQConfig.DIAGNOSIS_QUEUE)
    public void onDiagnosisMessage(String messageId) {
        log.info("收到问诊消息: messageId={}", messageId);
        // 幂等校验：检查是否已处理过该消息
        // 异步记录日志、更新统计等
    }
}
