package com.diagnosis.service.service;

import java.util.List;

/**
 * RAG 检索服务
 * 基于向量数据库实现医疗知识检索
 * 
 * 核心能力：
 * - 将用户症状描述向量化
 * - 在 Milvus 向量库中检索最相关的医疗知识
 * - 返回 Top-K 检索结果用于增强 Prompt
 */
public interface RAGRetrievalService {

    /**
     * 检索与查询相关的医疗知识
     * @param query 用户输入的症状描述
     * @param topK 返回最相关的 K 条结果
     * @return 医疗知识文本列表
     */
    List<String> retrieve(String query, int topK);
}
