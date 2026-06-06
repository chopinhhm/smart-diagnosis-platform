package com.diagnosis.service.service;

import com.diagnosis.service.model.dto.ChatRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 问诊服务单元测试
 */
@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Test
    void testStreamDiagnosisNotNull() {
        ChatRequest request = new ChatRequest();
        request.setSessionId("test-session-001");
        request.setMessage("我最近头疼，持续三天了");

        var flux = chatService.streamDiagnosis(request);
        assertNotNull(flux);
    }

    @Test
    void testGetHistory() {
        var history = chatService.getChatHistory("non-existent");
        assertNotNull(history);
    }

    @Test
    void testEndSession() {
        assertDoesNotThrow(() -> chatService.endSession("test-session-001"));
    }
}
