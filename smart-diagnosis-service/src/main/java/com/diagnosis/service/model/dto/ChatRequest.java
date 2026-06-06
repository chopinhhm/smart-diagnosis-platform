package com.diagnosis.service.model.dto;

import lombok.Data;

/**
 * 问诊请求 DTO
 */
@Data
public class ChatRequest {

    /** 会话ID */
    private String sessionId;

    /** 用户消息（症状描述） */
    private String message;

    /** 患者ID（可选） */
    private String patientId;
}
