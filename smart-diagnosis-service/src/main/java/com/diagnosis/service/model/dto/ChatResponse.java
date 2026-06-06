package com.diagnosis.service.model.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 问诊响应 DTO
 */
@Data
@Builder
public class ChatResponse {

    /** 会话ID */
    private String sessionId;

    /** AI 回复内容 */
    private String reply;

    /** 是否完成 */
    private boolean finished;

    /** 建议科室 */
    private String suggestedDepartment;
}
