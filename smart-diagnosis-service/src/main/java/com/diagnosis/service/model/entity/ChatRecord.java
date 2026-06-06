package com.diagnosis.service.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 问诊对话记录实体
 */
@Data
@TableName("chat_record")
public class ChatRecord {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /** 问诊会话ID */
    private String sessionId;

    /** 角色：user / assistant */
    private String role;

    /** 消息内容 */
    private String content;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
