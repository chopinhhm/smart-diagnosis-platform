# 智慧问诊服务平台 (Smart Diagnosis Platform)

> 基于 Spring Cloud Alibaba + Spring AI 构建的智慧医疗问诊平台，支持多轮对话问诊、RAG 知识库检索、SSE 流式输出

## 项目简介

本项目为重医附院打造的智慧问诊服务平台，基于 Spring Cloud Alibaba 微服务架构与 Spring AI 大模型集成能力，实现智能分诊导诊、多轮对话问诊、RAG 知识库检索等核心功能。

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Cloud Alibaba | Nacos 注册/配置中心、Gateway 网关、Sentinel 熔断降级 |
| Spring AI | 大模型集成、函数调用、RAG 检索增强 |
| Milvus | 向量数据库，存储医疗知识库向量 |
| DeepSeek-Embedding-V1 | 文本向量化模型 |
| Redis | 三级缓存架构（本地缓存 → Redis → 向量库） |
| RabbitMQ | 异步消息解耦、削峰填谷 |
| MySQL | 业务数据持久化 |
| SSE | 服务端推送，实现流式问诊对话 |

## 核心亮点

- **SSE 流式问诊**：基于 Spring AI + SSE 协议实现实时流式对话输出
- **双记忆体系**：对话记忆 + 业务流程记忆，解决多轮对话上下文丢失问题
- **医疗 RAG 知识库**：基于 Milvus 向量库 + Markdown 智能分块，导入院内科室介绍、常见病分诊规则
- **三级缓存架构**：本地缓存 → Redis → 向量库，向量库请求量下降 62%
- **Sentinel 熔断降级**：接口级流控与熔断，保障系统高可用
- **医疗合规**：敏感词过滤、数据脱敏、独立数据闭环

## 项目架构

```
┌──────────────┐     ┌──────────────┐     ┌──────────────────┐
│   Gateway    │────▶│    Auth      │────▶│  Diagnosis       │
│  (Spring     │     │  (JWT认证)    │     │  Service         │
│   Cloud      │     └──────────────┘     │  (核心问诊服务)    │
│   Gateway)   │                          │                  │
└──────────────┘                          │  ┌─ ChatController│
                                          │  ├─ ChatService   │
                                          │  ├─ RAGRetrieval  │
                                          │  ├─ Memory Mgmt   │
                                          │  └─ AI Functions  │
                                          └────────┬─────────┘
                                                   │
                    ┌──────────────┬─────────────────┼──────────┐
                    ▼              ▼                 ▼          ▼
              ┌──────────┐  ┌──────────┐   ┌────────────┐  ┌────────┐
              │  Redis   │  │  Milvus  │   │   MySQL    │  │RabbitMQ│
              │ 三级缓存  │  │ 向量知识库│   │ 业务数据   │  │ 异步消息│
              └──────────┘  └──────────┘   └────────────┘  └────────┘
```

## 模块说明

| 模块 | 说明 |
|------|------|
| smart-diagnosis-gateway | API 网关，路由转发、限流过滤、鉴权 |
| smart-diagnosis-auth | 用户认证服务，JWT Token 签发与校验 |
| smart-diagnosis-service | 核心问诊服务，包含对话、RAG、记忆管理等 |
| smart-diagnosis-common | 公共模块，统一响应、异常处理、工具类 |

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0
- Redis 6.0+
- Nacos 2.x
- Milvus 2.x
- RabbitMQ 3.x

### 启动步骤
1. 执行 `sql/init.sql` 初始化数据库
2. 修改各模块 `application.yml` 中的数据库、Redis、Nacos 地址
3. 启动 Nacos → MySQL → Redis → Milvus → RabbitMQ
4. 按顺序启动：gateway → auth → service

## 许可证

MIT License
