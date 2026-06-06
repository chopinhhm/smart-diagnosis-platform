package com.diagnosis.service.service.impl;

import com.diagnosis.service.memory.ConversationMemory;
import com.diagnosis.service.memory.BusinessMemory;
import com.diagnosis.service.model.dto.ChatRequest;
import com.diagnosis.service.rag.VectorStoreManager;
import com.diagnosis.service.service.ChatService;
import com.diagnosis.service.service.RAGRetrievalService;
import com.diagnosis.service.service.SensitiveWordFilter;
import com.diagnosis.service.model.entity.ChatRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * 问诊服务核心实现
 * 
 * 核心设计：双记忆体系
 * - 对话记忆（ConversationMemory）：维护多轮对话上下文，防止上下文丢失
 * - 业务记忆（BusinessMemory）：记录问诊流程状态（已问症状、疑似科室等）
 * 
 * 处理流程：
 * 1. 敏感词过滤 → 2. RAG 知识库检索 → 3. 构建 Prompt（记忆 + 知识 + 用户消息）→ 4. AI 生成回复
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient.Builder chatClientBuilder;
    private final ConversationMemory conversationMemory;
    private final BusinessMemory businessMemory;
    private final RAGRetrievalService ragService;
    private final SensitiveWordFilter sensitiveWordFilter;
    private final VectorStoreManager vectorStoreManager;

    @Override
    public Flux<String> streamDiagnosis(ChatRequest request) {
        String sessionId = request.getSessionId();
        String userMessage = request.getMessage();

        // Step 1: 敏感词过滤
        if (sensitiveWordFilter.containsSensitive(userMessage)) {
            log.warn("检测到敏感词，sessionId={}", sessionId);
            return Flux.just("抱歉，您的提问包含不适当内容，请重新描述您的症状。");
        }

        // Step 2: RAG 知识库检索相关医疗知识
        List<String> knowledge = ragService.retrieve(userMessage, 3);
        log.info("RAG 检索到 {} 条相关知识，sessionId={}", knowledge.size(), sessionId);

        // Step 3: 获取对话记忆和业务记忆
        List<Message> conversationHistory = conversationMemory.getHistory(sessionId);
        String businessContext = businessMemory.getContext(sessionId);

        // Step 4: 构建系统 Prompt
        String systemPrompt = buildSystemPrompt(knowledge, businessContext);
        conversationHistory.add(0, new UserMessage(systemPrompt));
        conversationHistory.add(new UserMessage(userMessage));

        // Step 5: 流式调用 AI
        ChatClient chatClient = chatClientBuilder.build();
        return chatClient.prompt(new Prompt(conversationHistory))
                .stream()
                .content()
                .doOnNext(token -> conversationMemory.appendAssistantToken(sessionId, token))
                .doOnComplete(() -> {
                    conversationMemory.saveSession(sessionId);
                    businessMemory.updateFromResponse(sessionId, userMessage);
                });
    }

    @Override
    public String diagnosis(ChatRequest request) {
        // 非流式：收集所有 token 后返回
        StringBuilder sb = new StringBuilder();
        streamDiagnosis(request).toStream().forEach(sb::append);
        return sb.toString();
    }

    @Override
    public List<?> getChatHistory(String sessionId) {
        return conversationMemory.getHistory(sessionId);
    }

    @Override
    public void endSession(String sessionId) {
        conversationMemory.clearSession(sessionId);
        businessMemory.clearSession(sessionId);
        log.info("问诊会话已结束，sessionId={}", sessionId);
    }

    /**
     * 构建系统级 Prompt
     * 注入医疗知识库检索结果和业务上下文
     */
    private String buildSystemPrompt(List<String> knowledge, String businessContext) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位专业的医疗问诊助手，请根据患者描述的症状进行初步分诊。\n\n");
        
        if (businessContext != null && !businessContext.isEmpty()) {
            prompt.append("【当前问诊进度】\n").append(businessContext).append("\n\n");
        }
        
        if (!knowledge.isEmpty()) {
            prompt.append("【相关知识库检索结果】\n");
            for (String k : knowledge) {
                prompt.append("- ").append(k).append("\n");
            }
            prompt.append("\n");
        }
        
        prompt.append("请基于以上信息，引导患者描述更多症状细节，并给出初步分诊建议。");
        return prompt.toString();
    }
}
