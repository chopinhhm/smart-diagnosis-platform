package com.diagnosis.service.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 向量库管理器
 * 封装 Milvus 向量库操作，提供知识入库和检索能力
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class VectorStoreManager {

    private final VectorStore vectorStore;

    /**
     * 批量导入文档到向量库
     * @param documents 文档列表
     */
    public void importDocuments(List<Document> documents) {
        vectorStore.add(documents);
        log.info("成功导入 {} 条文档到向量库", documents.size());
    }

    /**
     * 相似度检索
     * @param query 查询文本
     * @param topK 返回数量
     * @return 匹配的文档列表
     */
    public List<Document> search(String query, int topK) {
        // 实际使用 Spring AI 的 similarity search
        return vectorStore.similaritySearch(query);
    }
}
