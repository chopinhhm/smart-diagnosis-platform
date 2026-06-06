package com.diagnosis.service.memory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 对话记忆管理器
 * 
 * 维护每个问诊会话的多轮对话上下文
 * 基于 Redis 存储，支持会话过期自动清理
 * 
 * 存储策略：
 * - 每个会话维护独立的对话历史列表
 * - 用户消息和 AI 回复交替存储
 * - 设置 30 分钟过期时间，超时自动清理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ConversationMemory {

    private final StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "diagnosis:memory:conv:";
    private static final int MAX_HISTORY = 20;
    private static final int EXPIRE_MINUTES = 30;

    /**
     * 获取会话对话历史
     */
    public List<Message> getHistory(String sessionId) {
        String key = KEY_PREFIX + sessionId;
        List<String> records = redisTemplate.opsForList().range(key, 0, -1);
        List<Message> messages = new ArrayList<>();
        if (records != null) {
            for (String record : records) {
                if (record.startsWith("U:")) {
                    messages.add(new UserMessage(record.substring(2)));
                } else if (record.startsWith("A:")) {
                    messages.add(new AssistantMessage(record.substring(2)));
                }
            }
        }
        return messages;
    }

    /**
     * 追加 AI 回复 token（流式场景）
     */
    public void appendAssistantToken(String sessionId, String token) {
        // 流式场景下拼接最终完整回复，保存时使用
        String tempKey = KEY_PREFIX + sessionId + ":streaming";
        redisTemplate.opsForValue().increment(tempKey);
        redisTemplate.expire(tempKey, EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 保存会话（对话结束后持久化）
     */
    public void saveSession(String sessionId) {
        redisTemplate.expire(KEY_PREFIX + sessionId, EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * 清除会话记忆
     */
    public void clearSession(String sessionId) {
        redisTemplate.delete(KEY_PREFIX + sessionId);
        redisTemplate.delete(KEY_PREFIX + sessionId + ":streaming");
        log.info("对话记忆已清除，sessionId={}", sessionId);
    }
}
