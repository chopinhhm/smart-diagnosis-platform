package com.diagnosis.service.controller;

import com.diagnosis.service.model.dto.ChatRequest;
import com.diagnosis.service.service.ChatService;
import com.diagnosis.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * 问诊对话控制器
 * 支持 SSE 流式输出和普通对话两种模式
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * SSE 流式问诊接口
     * 客户端通过 EventSource 建立长连接，服务端实时推送问诊回复
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        return chatService.streamDiagnosis(request);
    }

    /**
     * 普通问诊接口（非流式）
     */
    @PostMapping("/send")
    public Result<String> sendChat(@RequestBody ChatRequest request) {
        return Result.success(chatService.diagnosis(request));
    }

    /**
     * 获取问诊历史记录
     */
    @GetMapping("/history/{sessionId}")
    public Result<?> getHistory(@PathVariable String sessionId) {
        return Result.success(chatService.getChatHistory(sessionId));
    }

    /**
     * 结束问诊会话
     */
    @PostMapping("/end/{sessionId}")
    public Result<Void> endSession(@PathVariable String sessionId) {
        chatService.endSession(sessionId);
        return Result.success();
    }
}
