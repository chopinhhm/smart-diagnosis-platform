-- 智慧问诊平台 数据库初始化脚本

CREATE DATABASE IF NOT EXISTS smart_diagnosis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smart_diagnosis;

-- 问诊会话表
CREATE TABLE diagnosis_session (
    id VARCHAR(36) PRIMARY KEY COMMENT '会话ID',
    patient_id VARCHAR(36) NOT NULL COMMENT '患者ID',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态: active/ended',
    department VARCHAR(50) COMMENT '建议科室',
    chief_complaint VARCHAR(500) COMMENT '主诉症状',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    end_time DATETIME COMMENT '结束时间',
    INDEX idx_patient (patient_id),
    INDEX idx_status (status)
) ENGINE=InnoDB COMMENT='问诊会话表';

-- 对话记录表
CREATE TABLE chat_record (
    id VARCHAR(36) PRIMARY KEY COMMENT '记录ID',
    session_id VARCHAR(36) NOT NULL COMMENT '会话ID',
    role VARCHAR(20) NOT NULL COMMENT '角色: user/assistant',
    content TEXT NOT NULL COMMENT '消息内容',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_session (session_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB COMMENT='对话记录表';
