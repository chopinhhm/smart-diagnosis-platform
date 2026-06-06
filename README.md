<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=0D1117&height=150&section=header&text=%E6%99%BA%E6%85%A7%E9%97%AE%E8%AF%8A%E6%9C%8D%E5%8A%A1%E5%B9%B3%E5%8F%B0&fontSize=36&fontColor=58A6FF&animation=fadeIn" />

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud_Alibaba-0079BE?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud)
[![Spring AI](https://img.shields.io/badge/Spring_AI-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://spring.io/projects/spring-ai)
[![Milvus](https://img.shields.io/badge/Milvus-00A1EA?style=flat-square)](https://milvus.io/)
[![Redis](https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=redis&logoColor=white)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)

</div>

---

## ✨ Features

<div align="center">

<table>
<tr>
<td width="50%" align="center">
<h3>🧠 双记忆体系</h3>
<p>对话记忆 + 业务流程记忆<br/>解决多轮对话上下文丢失问题</p>
</td>
<td width="50%" align="center">
<h3>🔍 RAG 知识库检索</h3>
<p>Milvus 向量库 + Markdown 智能分块<br/>62% 缓存命中率提升</p>
</td>
</tr>
<tr>
<td width="50%" align="center">
<h3>⚡ SSE 流式输出</h3>
<p>Spring AI + Server-Sent Events<br/>实时流式问诊对话体验</p>
</td>
<td width="50%" align="center">
<h3>🛡️ 高可用保障</h3>
<p>Sentinel 熔断降级 + Redis 三级缓存<br/>RabbitMQ 异步解耦</p>
</td>
</tr>
</table>

</div>

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────┐
│                    Spring Cloud Gateway              │
│            (路由转发 / 限流过滤 / 鉴权)               │
└──────────────────┬──────────────────────────────────┘
                   │
     ┌─────────────┼─────────────┐
     ▼             ▼             ▼
┌─────────┐  ┌──────────┐  ┌──────────────┐
│  Auth   │  │Diagnosis │  │   Admin      │
│ Service │  │ Service  │  │   Service    │
└────┬────┘  └────┬─────┘  └──────────────┘
     │           │
     │    ┌──────┴──────┬──────────────┐
     │    ▼             ▼              ▼
     │  ┌────────┐ ┌────────┐  ┌────────────┐
     │  │Conversation│ │Business│  │ RAG Service │
     │  │ Memory  │ │ Memory │  │ (Milvus)    │
     │  └────────┘ └────────┘  └────────────┘
     │                    │
     ▼                    ▼
  ┌────────┐        ┌──────────┐
  │  Redis │        │  MySQL   │
  │ 三级缓存│        │ 持久化   │
  └────────┘        └──────────┘
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Spring Boot 3.2 / Spring Cloud Alibaba |
| **AI Engine** | Spring AI / DeepSeek / Milvus Vector Store |
| **Cache** | Redis (三级: Local → Redis → Vector) |
| **Message** | RabbitMQ (死信队列 / 幂等消费) |
| **Database** | MySQL 8.0 + MyBatis-Plus |
| **Protocol** | SSE (Server-Sent Events) / REST API |
| **Resilience** | Sentinel (熔断 / 降级 / 流控) |

---

## 📦 Modules

```
smart-diagnosis-platform/
├── smart-diagnosis-gateway/       # API Gateway
├── smart-diagnosis-auth/          # JWT Auth Service
├── smart-diagnosis-service/       # Core Diagnosis Service
│   ├── controller/                # SSE Stream Controller
│   ├── service/                   # Chat / RAG / Memory Services
│   ├── memory/                    # Dual Memory System
│   ├── rag/                       # Document Chunker / Vector Store
│   ├── mq/                        # RabbitMQ Config & Listener
│   └── config/                    # Redis / Sentinel / MyBatis-Plus
├── smart-diagnosis-common/        # Shared Utils & Exceptions
├── sql/                           # Database Init Scripts
└── docs/                          # Architecture Docs
```

---

## 🚀 Quick Start

\`\`\`bash
# Clone the repo
git clone https://github.com/chopinhhm/smart-diagnosis-platform.git
cd smart-diagnosis-platform

# Start with Docker Compose
docker-compose up -d

# Or build manually
mvn clean package -DskipTests
java -jar smart-diagnosis-service/target/*.jar
\`\`\`

### Prerequisites
- JDK 17+
- MySQL 8.0
- Redis 6.0+
- Nacos 2.x
- Milvus 2.x
- RabbitMQ 3.x

---

## 📊 Performance Metrics

| Metric | Value |
|--------|-------|
| 向量库请求量下降 | **62%** (三级缓存) |
| SSE 首字延迟 | < 200ms |
| 并发支持 | 500+ QPS |
| 可用性 | 99.9% (Sentinel 熔断) |

---

## 📄 License

MIT License © [chopinhhm](https://github.com/chopinhhm)

