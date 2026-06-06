package com.diagnosis.service.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 问诊会话实体
 */
@Data
@TableName("diagnosis_session")
public class DiagnosisSession {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /** 患者ID */
    private String patientId;

    /** 会话状态：active / ended */
    private String status;

    /** 初步分诊科室 */
    private String department;

    /** 主诉症状 */
    private String chiefComplaint;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 结束时间 */
    private LocalDateTime endTime;
}
