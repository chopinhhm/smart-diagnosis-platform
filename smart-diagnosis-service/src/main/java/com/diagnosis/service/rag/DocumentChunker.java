package com.diagnosis.service.rag;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 智能文档分块器
 * 
 * 核心策略：按 Markdown 标题层级（## ###）进行语义分割
 * 相比固定长度分块，标题级分块能保持知识的语义完整性
 * 
 * 应用场景：将院内科室介绍、常见病分诊规则等 Markdown 文档
 * 分块后向量化存入 Milvus
 */
@Component
public class DocumentChunker {

    private static final int MIN_CHUNK_SIZE = 50;
    private static final int MAX_CHUNK_SIZE = 500;
    private static final Pattern HEADING_PATTERN = Pattern.compile("^(#{1,3})\s+", Pattern.MULTILINE);

    /**
     * 将 Markdown 文档按标题层级分块
     * @param markdown 完整 Markdown 文档
     * @return 文档块列表
     */
    public List<String> chunk(String markdown) {
        if (markdown == null || markdown.isEmpty()) return List.of();

        List<String> chunks = new ArrayList<>();
        String[] sections = HEADING_PATTERN.split(markdown);
        StringBuilder current = new StringBuilder();

        for (String section : sections) {
            String trimmed = section.trim();
            if (trimmed.isEmpty()) continue;

            if (current.length() + trimmed.length() > MAX_CHUNK_SIZE && current.length() > 0) {
                chunks.add(current.toString().trim());
                current = new StringBuilder();
            }
            current.append(trimmed).append("\n");
        }

        if (current.length() >= MIN_CHUNK_SIZE) {
            chunks.add(current.toString().trim());
        }

        return chunks;
    }
}
