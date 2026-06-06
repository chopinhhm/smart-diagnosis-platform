package com.diagnosis.service.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 业务流程记忆管理器
 * 
 * 跟踪问诊流程状态，记录关键业务信息：
 * - 已收集的症状信息
 * - 初步判断的科室方向
 * - 问诊阶段（症状描述 → 详细问诊 → 分诊建议）
 * 
 * 与 ConversationMemory 的区别：
 * - ConversationMemory：原始对话文本（给 AI 做上下文）
 * - BusinessMemory：结构化的业务状态（给流程控制用）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessMemory {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "diagnosis:memory:business:";
    private static final int EXPIRE_MINUTES = 30;

    /**
     * 获取业务上下文
     */
    public String getContext(String sessionId) {
        String key = KEY_PREFIX + sessionId;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 更新业务上下文
     */
    public void updateContext(String sessionId, String context) {
        String key = KEY_PREFIX + sessionId;
        redisTemplate.opsForValue().set(key, context, EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 根据 AI 回复更新业务状态（解析提取关键信息）
     */
    public void updateFromResponse(String sessionId, String userMessage) {
        String context = getContext(sessionId);
        // 实际项目中通过 AI 函数调用或正则提取结构化信息
        StringBuilder updated = new StringBuilder(context != null ? context : "");
        updated.append("患者提及：").append(userMessage).append("\n");
        updateContext(sessionId, updated.toString());
    }

    /**
     * 清除业务记忆
     */
    public void clearSession(String sessionId) {
        redisTemplate.delete(KEY_PREFIX + sessionId);
    }
}
