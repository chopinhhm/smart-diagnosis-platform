package com.diagnosis.service.service;

import com.diagnosis.service.model.dto.ChatRequest;
import reactor.core.publisher.Flux;
import java.util.List;

/**
 * 问诊服务接口
 * 定义问诊核心能力：流式问诊、普通问诊、历史查询、会话管理
 */
public interface ChatService {

    /**
     * SSE 流式问诊
     * @param request 问诊请求（含 sessionId、用户消息、患者信息）
     * @return 流式输出的问诊回复
     */
    Flux<String> streamDiagnosis(ChatRequest request);

    /**
     * 普通问诊（非流式）
     * @param request 问诊请求
     * @return 完整的问诊回复文本
     */
    String diagnosis(ChatRequest request);

    /**
     * 获取问诊历史记录
     * @param sessionId 会话ID
     * @return 对话记录列表
     */
    List<?> getChatHistory(String sessionId);

    /**
     * 结束问诊会话
     * @param sessionId 会话ID
     */
    void endSession(String sessionId);
}
