package com.diagnosis.service.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * 医疗敏感词过滤器
 * 用于过滤用户输入中的敏感内容，保障医疗 AI 合规要求
 */
@Service
public class SensitiveWordFilter {

    /** 敏感词库（实际项目中从数据库或配置文件加载） */
    private final Set<String> sensitiveWords = new HashSet<>();

    public SensitiveWordFilter() {
        // 初始化默认敏感词库
        sensitiveWords.add("自杀");
        sensitiveWords.add("杀");
        sensitiveWords.add("毒");
        sensitiveWords.add("处方药购买");
    }

    /**
     * 检测文本是否包含敏感词
     * @param text 待检测文本
     * @return true 表示包含敏感词
     */
    public boolean containsSensitive(String text) {
        if (text == null || text.isEmpty()) return false;
        return sensitiveWords.stream().anyMatch(text::contains);
    }

    /**
     * 替换文本中的敏感词为 ***
     * @param text 原始文本
     * @return 替换后的文本
     */
    public String filter(String text) {
        if (text == null) return null;
        String result = text;
        for (String word : sensitiveWords) {
            result = result.replace(word, "***");
        }
        return result;
    }

    /**
     * 添加敏感词
     * @param word 敏感词
     */
    public void addSensitiveWord(String word) {
        sensitiveWords.add(word);
    }
}
