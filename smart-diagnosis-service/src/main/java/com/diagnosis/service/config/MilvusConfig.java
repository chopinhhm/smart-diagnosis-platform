package com.diagnosis.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.ai.vectorstore.MilvusVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;

/**
 * Milvus 向量数据库配置
 * 连接 Milvus 实例，配置 Collection 和索引参数
 */
@Configuration
public class MilvusConfig {

    // Milvus 连接配置通过 application.yml 管理
    // spring.ai.vectorstore.milvus.host=localhost
    // spring.ai.vectorstore.milvus.port=19530
    // spring.ai.vectorstore.milvus.collectionName=medical_knowledge
}
