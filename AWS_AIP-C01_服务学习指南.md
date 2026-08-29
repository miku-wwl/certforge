# AWS AIP-C01：117 题涉及的 AWS 服务学习指南

> 数据源：`src/main/resources/question-bank/AWS_AIP-C01_bilingual.md`
> 提取范围：117 道题的 **English 选项 A–F**，不把只出现在题干中的服务计入主清单。
> 统计口径：同一服务在同一道题的多个选项中出现多次，只计为 1 道关联题；名称相近的子功能合并到所属服务族。

## 1. 怎么使用这份指南

不要按 AWS 服务名字的字母顺序学习。AIP-C01 的题目通常是一条完整链路：

```text
身份与权限
  → 计算、网络与存储
  → API、消息与工作流
  → 日志、审计与部署
  → 数据处理与传统 AI/ML
  → Bedrock 推理、RAG、Agent、安全和评估
```

建议先理解每个阶段的“服务边界”，再回到对应题号做选择题。题库中出现次数很多，不代表考试只考服务定义；更常考的是“为什么选它，而不是旁边那个看起来也能做的服务”。

## 2. 总体学习顺序

| 阶段 | 先学什么 | 学习结果 |
|---|---|---|
| 1 | IAM、STS、KMS、CloudTrail、Config | 能判断谁能访问、数据如何加密、操作如何审计 |
| 2 | VPC、PrivateLink、EC2、ECS、Lambda、S3 | 能画出 GenAI 应用的基本运行与网络边界 |
| 3 | RDS、DynamoDB、缓存、图数据库、OpenSearch | 能按数据模型、延迟和检索方式选择存储 |
| 4 | SQS、SNS、EventBridge、Step Functions、API Gateway | 能设计同步、异步、事件驱动和长流程 |
| 5 | CloudWatch、X-Ray、IaC、CI/CD | 能部署、监控、追踪和回滚生产系统 |
| 6 | Glue、Athena、Kinesis、Firehose、Lake Formation | 能构建可治理的数据摄取与分析链路 |
| 7 | SageMaker AI 与传统 AI 服务 | 能区分自定义 ML、文本/图像/语音 AI 和生成式 AI |
| 8 | Amazon Bedrock 全家桶 | 能处理模型调用、RAG、Agent、Guardrails 和评估题 |
| 9 | 跨服务架构题 | 能从实时性、成本、安全、开发量和运维复杂度做最终选择 |

---

## 3. 阶段一：身份、安全与治理

这一阶段是所有后续服务的地基。先掌握“身份策略 + 资源策略 + 临时凭证 + 加密密钥 + 审计事件”这条主线。

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| AWS Identity and Access Management（IAM） | 20 | 8, 9, 10, 13, 34, 36, 37, 56, 58, 73, 75, 77, 80, 81, 85, 87, 100, 104, 105, 117 | 用户、角色和策略。重点是最小权限、Bedrock 模型调用权限、服务角色、跨账户访问以及资源策略。 | 公司的门禁系统：不同员工的工牌只能打开获准进入的房间。 |
| AWS IAM Identity Center | 2 | 37, 58 | 员工统一登录和多账户权限分配；不要把它当作面向 App 用户的 Cognito。 | 集团统一员工入口：登录一次，就能按岗位进入不同分公司和系统。 |
| AWS Security Token Service（STS） | 1 | 87 | 通过 AssumeRole 产生临时凭证；跨账户访问和短期授权的基础。 | 前台签发的限时访客证：到期自动失效，只能访问指定区域。 |
| AWS Organizations | 1 | 85 | 多账户组织与 SCP。SCP 是权限上限，不直接授予权限。 | 集团总部管理各子公司，并规定所有子公司都不能突破的红线。 |
| AWS Service Catalog | 1 | 68 | 向组织发布经过批准的标准化产品；适合受控自助部署。 | 公司内部采购目录：员工只能自助领取审核过的标准设备套餐。 |
| AWS Config | 3 | 9, 77, 110 | 记录资源配置状态并检查合规；与记录 API 操作的 CloudTrail 不同。 | 定期巡检员：持续核对房间布置是否符合消防和公司规范。 |
| AWS CloudTrail | 17 | 8, 9, 18, 19, 21, 31, 58, 61, 62, 76, 77, 80, 85, 87, 100, 105, 110 | 记录“谁在何时调用了什么 API”。CloudTrail Lake 用于事件查询，CloudTrail Insights 用于异常 API 活动。 | 门禁流水和监控记录：能追查谁在什么时间做了什么操作。 |
| AWS Key Management Service（KMS） | 1 | 77 | 管理加密密钥和密钥策略；重点区分 AWS 托管密钥与客户管理密钥。 | 集中钥匙管理室：保管主钥匙，并规定谁可以使用哪把钥匙。 |
| AWS Secrets Manager | 1 | 104 | 存储并轮换数据库密码、API 密钥等秘密；不要用普通环境变量代替秘密管理。 | 会自动换密码的保险柜：应用来取秘密，但不用把密码贴在桌上。 |
| AWS Systems Manager | 7 | 22, 49, 59, 80, 98, 103, 105 | Parameter Store 管理配置/参数，Automation 执行运维流程；题目常拿它与 Secrets Manager、AppConfig 比较。 | 物业运维控制中心：保存设备参数，并按标准流程远程执行维护。 |
| AWS WAF | 3 | 21, 86, 105 | 在 HTTP 层按规则过滤恶意请求；它不负责模型输出安全，模型内容安全看 Bedrock Guardrails。 | 网站大门的安检员：按规则拦截可疑访客和恶意包裹。 |
| Amazon GuardDuty | 1 | 105 | 基于日志和威胁情报进行威胁检测；不是通用应用日志分析工具。 | 全天巡逻的保安：从异常脚印和行为模式中发现潜在入侵者。 |
| Amazon Macie | 5 | 61, 68, 77, 85, 94 | 发现和分类 S3 中的敏感数据/PII；与 Comprehend 的文本 PII 检测场景不同。 | 隐私检查员：翻查仓库文件，找出身份证号、银行卡号等敏感材料。 |
| Amazon Cognito | 4 | 40, 52, 75, 94 | 面向 Web/Mobile 应用用户的注册、登录和令牌；员工多账户登录优先考虑 IAM Identity Center。 | 商场会员系统：负责顾客注册、登录、找回密码和会员身份。 |
| AWS Lake Formation | 3 | 8, 19, 87 | 数据湖权限和治理，支持 LF-Tag；建立在 S3、Glue Data Catalog 等组件之上。 | 水库管理处：为不同人员划定可查看、取用的数据区域。 |

### 本阶段必须会区分

- **CloudTrail vs CloudWatch**：CloudTrail 回答“谁调用了 API”；CloudWatch 回答“系统现在运行得怎么样”。
- **Config vs CloudTrail**：Config 看资源配置及合规状态；CloudTrail 看操作事件。
- **IAM Identity Center vs Cognito**：前者服务员工和 AWS 账户访问，后者服务应用终端用户。
- **Parameter Store vs Secrets Manager vs AppConfig**：普通参数、可轮换秘密、动态应用配置是三种不同问题。
- **Macie vs Comprehend PII vs Guardrails Sensitive Information Filter**：分别偏向 S3 数据发现、文本分析、生成式 AI 输入输出保护。

---

## 4. 阶段二：网络、边缘与计算

先理解请求从用户到 API，再到 Lambda/容器/虚拟机，最后访问 Bedrock 与数据层的路径。

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| Amazon VPC / AWS PrivateLink | 8 | 8, 34, 77, 86, 96, 100, 104, 105 | VPC 提供隔离网络；PrivateLink/VPC Endpoint 让流量私网访问 AWS 服务，常用于 Bedrock、S3 和合规场景。 | VPC 是有围墙的园区；PrivateLink 是园区通往合作方的专用地下通道。 |
| Amazon Route 53 | 1 | 69 | DNS、健康检查和流量路由；不要与直接加速网络路径的 Global Accelerator 混淆。 | 电话号码簿和导航台：把域名翻译成正确地址，并避开故障门店。 |
| Amazon CloudFront | 2 | 43, 62 | CDN 和边缘缓存，适合静态内容与全球分发。 | 在各城市设置前置仓，让顾客从最近的仓库拿到常用商品。 |
| AWS Global Accelerator | 2 | 69, 71 | 使用 AWS 全球网络和静态 Anycast IP 加速 TCP/UDP 应用；不等同于 CDN 缓存。 | 给全球车辆一个固定入口，再送上 AWS 私有高速公路前往目的地。 |
| AWS Outposts | 1 | 72 | 把 AWS 基础设施延伸到本地数据中心，适合严格本地部署和低延迟需求。 | 把云厂商的一整套标准设备搬进自己的工厂机房。 |
| AWS Local Zones | 1 | 72 | 把部分 AWS 服务放到靠近大城市/用户的位置；仍属于 AWS 区域扩展。 | 总仓之外在市中心设小型服务站，缩短最后一段路程。 |
| Amazon EC2 / EC2 Auto Scaling | 5 | 17, 45, 72, 96, 116 | 自管虚拟机和弹性扩缩。自由度高，但补丁、容量与运行时管理更多。 | 租用可自由装修的房子；客流增加时自动多租几套。 |
| Amazon ECS | 3 | 16, 20, 82 | AWS 原生容器编排；题目常与 Lambda、EC2、Fargate组合比较。 | 集装箱调度中心：决定每个标准箱放到哪辆车、何时补充数量。 |
| AWS Fargate | 1 | 116 | ECS/EKS 的无服务器计算引擎，运行容器但不管理底层节点。 | 只交付集装箱，物流公司负责车辆、司机和调度基础设施。 |
| AWS Lambda | 70 | 1, 3, 4, 5, 7, 8, 9, 10, 12, 16, 17, 18, 20, 22, 24, 25, 26, 27, 28, 32, 34, 35, 36, 39, 40, 42, 44, 45, 48, 49, 50, 52, 53, 54, 55, 58, 59, 61, 62, 65, 67, 70, 71, 72, 73, 75, 78, 80, 82, 83, 84, 86, 90, 93, 94, 95, 96, 97, 98, 100, 103, 104, 105, 106, 109, 111, 112, 113, 114, 117 | 事件驱动函数。重点是超时、并发、异步调用、流式响应限制、重试、幂等性和与 Step Functions/SQS 的组合。 | 自动售货机：有请求才启动完成一次工作，无请求时不用安排店员值守。 |

### 选择计算服务的简化判断

- 短时、事件驱动、少运维：优先考虑 **Lambda**。
- 已有容器、需要更长运行时间或更多运行时控制：考虑 **ECS + Fargate**。
- 需要完整操作系统、特殊依赖或长期进程：考虑 **EC2**。
- 数据或延迟必须贴近本地机房：再评估 **Outposts/Local Zones**，不要一看到“低延迟”就选。

---

## 5. 阶段三：对象、关系、键值、缓存、图与向量数据

这部分最重要的不是背名字，而是先识别数据访问模式：对象、SQL、Key-Value、缓存、文档、图、全文/向量检索。

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| Amazon S3 | 38 | 6, 8, 9, 16, 18, 19, 24, 34, 36, 40, 43, 44, 50, 54, 59, 61, 62, 63, 69, 70, 72, 77, 80, 85, 86, 87, 94, 95, 98, 100, 102, 105, 106, 108, 109, 110, 115, 117 | 对象存储和数据湖基础。子能力包括 Lifecycle、Object Lock、Express One Zone 和 S3 Vectors。 | 超大型自助仓库：按唯一标签存放文件，想放多少就放多少。 |
| Amazon RDS / Amazon Aurora | 12 | 6, 16, 24, 44, 47, 48, 62, 65, 72, 86, 92, 105 | 托管关系数据库。Aurora PostgreSQL 可支持向量扩展；Aurora Serverless 和 RDS Data API 常用于无服务器应用。 | 管理规范的账本室：数据按表格和关系保存，还能执行复杂查询和事务。 |
| Amazon DynamoDB / DAX | 25 | 5, 9, 10, 15, 16, 17, 24, 25, 26, 27, 35, 39, 42, 43, 49, 51, 73, 84, 86, 103, 105, 106, 111, 112, 115 | 无服务器 Key-Value/文档数据库；DAX 是兼容 DynamoDB 的内存缓存，用于读密集型低延迟访问。 | 按编号直取的智能抽屉；DAX 是把最常拿的物品提前放到柜台。 |
| Amazon ElastiCache | 8 | 35, 42, 44, 49, 73, 84, 106, 109 | 托管 Redis/Valkey 或 Memcached 缓存；适合会话、热点数据和响应缓存。 | 收银台旁的快速取货架：常用商品不用每次都去后仓寻找。 |
| Amazon MemoryDB | 2 | 47, 51 | 兼容 Valkey/Redis 的持久内存数据库；与只作为缓存层的典型 ElastiCache 用法不同。 | 写在高速电子白板上的正式账本：读写快，同时会可靠保存。 |
| Amazon DocumentDB | 2 | 47, 106 | MongoDB 兼容的托管文档数据库；不要因为数据是 JSON 就默认选择它。 | 每位客户一个格式灵活的档案袋，不要求所有档案字段完全一致。 |
| Amazon Neptune / Neptune Analytics | 4 | 6, 17, 88, 106 | Neptune 处理关系密集的图数据；Neptune Analytics 偏向图分析。知识图谱、路径和实体关系是关键信号。 | 人际关系图：重点不是单个人的资料，而是谁与谁、通过什么关系连接。 |
| Amazon OpenSearch Service / OpenSearch Serverless | 20 | 6, 7, 15, 17, 22, 28, 31, 36, 43, 46, 48, 51, 62, 65, 66, 80, 86, 90, 92, 104 | 全文检索、日志分析和向量搜索；Serverless 减少集群容量管理，常作为 Bedrock Knowledge Bases 的向量存储。 | 图书馆搜索系统：按关键词、条件或语义相似度快速找到资料。 |

### 向量存储如何选

- 已经使用 OpenSearch，需要全文 + 过滤 + 向量混合检索：优先考虑 **OpenSearch**。
- 关系数据在 PostgreSQL，想把业务 SQL 与向量检索放在一起：考虑 **Aurora PostgreSQL**。
- 图关系本身就是问题核心：考虑 **Neptune**。
- 大规模、成本敏感、查询相对不频繁的向量数据：评估 **S3 Vectors**。AWS 当前把它定义为 S3 中面向 AI Agent、RAG 和语义搜索的专用向量存储，并可与 Bedrock Knowledge Bases 集成。

---

## 6. 阶段四：消息、事件、工作流、API 与动态配置

这组服务决定系统是同步还是异步、是广播还是排队、是单步函数还是有状态工作流。

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| Amazon SQS | 11 | 3, 16, 20, 25, 35, 44, 50, 109, 114, 116, 117 | 队列、削峰、解耦和重试；一个消息通常由一个消费者处理，适合异步任务。 | 银行取号机：任务排队等待，柜员按能力逐个处理，不怕瞬间人多。 |
| Amazon SNS | 4 | 39, 71, 95, 106 | Pub/Sub 广播；一个事件可推送给多个订阅者。 | 小区广播：一条通知同时传给所有已订阅的住户和部门。 |
| Amazon EventBridge / EventBridge Pipes | 21 | 1, 9, 16, 22, 25, 27, 35, 49, 58, 66, 77, 78, 80, 89, 93, 95, 98, 103, 105, 111, 114 | 事件总线、规则路由和 SaaS/AWS 事件集成；Pipes 用于点到点连接源、过滤、增强和目标。 | 智能邮件分拣中心：按事件内容和规则把消息送到正确部门。 |
| AWS Step Functions | 27 | 5, 9, 12, 14, 16, 20, 22, 26, 27, 35, 36, 40, 44, 45, 48, 53, 54, 78, 84, 90, 93, 110, 111, 114, 115, 116, 117 | 有状态编排、重试、并行、人工审批和任务令牌。Standard 适合长时、可审计工作流；Express 适合高吞吐短流程。 | 流程经理拿着 SOP：安排先后步骤、分支、重试，并等待人工签字。 |
| Amazon API Gateway | 27 | 3, 4, 10, 16, 21, 22, 25, 26, 28, 39, 40, 44, 52, 53, 59, 62, 68, 71, 73, 75, 82, 86, 88, 96, 97, 109, 116 | 托管 API 入口。REST 功能最完整，HTTP API 更轻量，WebSocket 处理双向长连接和流式交互。 | 酒店前台：统一接待、验明身份、限流，再把请求转给内部部门。 |
| AWS AppSync | 2 | 3, 106 | 托管 GraphQL 和实时数据；题 3 与 Amplify AI Kit 的流式响应相关。 | 点餐服务员：顾客只点需要的字段，菜品状态变化时还能主动通知。 |
| AWS Amplify | 1 | 3 | Web/Mobile 全栈开发和托管；题库具体涉及 Amplify AI Kit 与 AppSync 的流式体验。 | 配套齐全的开店工具包：前台、托管和常用后端连接都准备好了。 |
| AWS AppConfig | 6 | 4, 49, 59, 80, 103, 111 | 动态配置与安全发布。模型路由、Prompt 配置和 Feature Flag 变化时，不必重新部署代码。 | 总部遥控菜单和营业规则，不用重新装修门店就能安全分批生效。 |

### 本阶段必须会区分

- **SQS vs SNS**：排队缓冲 vs 广播通知。
- **EventBridge vs Step Functions**：事件路由 vs 有状态流程编排。
- **Lambda 编排 vs Step Functions**：复杂重试、等待、分支和人工审批不要塞进一个“总控 Lambda”。
- **REST API vs HTTP API vs WebSocket API**：完整 API 管理、低成本轻量 HTTP、双向长连接。
- **AppConfig vs Parameter Store**：前者强调动态配置发布和验证，后者更像参数存储。

---

## 7. 阶段五：部署、可观测性与运维

生产题常要求同时考虑指标、日志、追踪和审计。它们不是同一件事。

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| AWS CloudFormation | 3 | 9, 68, 81 | 声明式基础设施即代码；用于可重复部署和漂移控制。 | 建筑蓝图：按同一份图纸反复施工，最终建筑应保持一致。 |
| AWS Cloud Development Kit（CDK） | 1 | 57 | 用编程语言定义并合成为 CloudFormation；它是开发框架，不是运行时服务。 | 用熟悉语言描述建筑需求，再自动翻译成标准施工图纸。 |
| AWS CodePipeline | 2 | 57, 98 | 持续交付流水线，串联源代码、构建、测试和部署阶段。 | 工厂总装流水线：依次把原料送入构建、质检和交付工位。 |
| AWS CodeBuild | 1 | 57 | 托管构建与测试执行环境。 | 自动化加工车间：拿到原料后完成编译、打包和测试。 |
| AWS CodeDeploy | 1 | 103 | 自动化部署，以及蓝绿/滚动等发布策略。 | 配送安装团队：把合格产品分批或整套换新送到生产现场。 |
| Amazon CloudWatch | 39 | 1, 8, 9, 12, 18, 19, 20, 21, 22, 24, 25, 29, 31, 34, 40, 45, 49, 55, 61, 62, 66, 69, 70, 71, 74, 76, 77, 84, 85, 86, 89, 95, 98, 100, 103, 109, 111, 112, 115 | 指标、告警、日志和 Dashboard。子能力包括 Logs Insights、Synthetics、Container Insights、Application Insights 和异常检测。 | 医院监护仪：持续显示生命体征，超过阈值立即报警并保留记录。 |
| AWS X-Ray | 5 | 31, 76, 84, 109, 112 | 分布式追踪，定位跨 API、Lambda 和下游服务的延迟与错误。 | 快递全链路追踪：查看包裹在哪一站停留太久或发生错误。 |
| Amazon Managed Grafana | 3 | 66, 95, 112 | 托管 Grafana Dashboard，统一展示多数据源指标；它不替代指标采集本身。 | 运营指挥大屏：把多个部门的数据统一展示，但数据仍由各部门采集。 |

### 一条最实用的可观测性口诀

- **Metrics**：有没有异常、何时告警。
- **Logs**：具体发生了什么。
- **Traces**：一次请求慢在了哪一段。
- **CloudTrail**：是谁进行了什么 AWS API 操作。

---

## 8. 阶段六：数据工程、流处理与 BI

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| AWS Glue | 9 | 18, 19, 35, 36, 45, 48, 62, 70, 78 | Serverless 数据集成/ETL；Data Catalog 管元数据，Glue Data Quality 检查质量。 | 仓库整理队：清洗、转换货物，并编写目录说明每样东西放在哪里。 |
| Amazon Athena | 4 | 18, 45, 62, 70 | 使用 SQL 直接查询 S3 数据；适合按需分析，不是低延迟事务数据库。 | 向仓库管理员直接提 SQL 问题，不必先把所有货物搬进新数据库。 |
| Amazon EMR | 1 | 48 | 托管 Spark/Hadoop 等大数据框架；适合需要框架级控制的大规模处理。 | 临时组织大型加工车队，使用 Spark/Hadoop 批量处理海量原料。 |
| Amazon Kinesis | 2 | 31, 80 | 实时流数据服务族；选项中用于摄取指标/日志或联合实时数据。 | 不停运转的传送带：事件一产生就连续送往下游处理。 |
| Amazon Data Firehose | 2 | 66, 80 | 托管式将流数据投递到 S3、OpenSearch 等目标；重点是“投递”，不是复杂流式应用逻辑。 | 自动消防水管：持续把数据流可靠输送到指定蓄水池或目的地。 |
| AWS Lake Formation | 3 | 8, 19, 87 | 数据湖权限与治理；应在理解 S3 和 Glue Data Catalog 后学习。 | 数据水库管理局：建水库、登记水源，并规定谁能取哪部分水。 |
| Amazon QuickSight | 6 | 18, 31, 66, 86, 95, 98 | 托管 BI、报表和 Dashboard；适合业务分析，不替代 CloudWatch 的运维告警。 | 经营分析报表墙：把销售和业务数据变成管理者能看懂的图表。 |

### 典型链路

```text
实时事件 → Kinesis / Firehose → S3 数据湖
                              → Glue Catalog / ETL
                              → Athena 查询
                              → QuickSight 报表
                              → Lake Formation 权限治理
```

---

## 9. 阶段七：SageMaker AI 与传统 AI 服务

先建立一个边界：**Bedrock 偏向使用 Foundation Model 构建生成式 AI；SageMaker AI 偏向完整 ML 生命周期和深度自定义。** 预训练的文本、图像、文档和语音任务，则优先检查专用 AI 服务。

| 服务/服务族 | 选项涉及题数 | 关联题号 | 核心定位与 AIP-C01 重点 | 生活中场景类比 |
|---|---:|---|---|---|
| Amazon SageMaker AI | 29 | 1, 7, 9, 12, 17, 18, 22, 24, 26, 33, 34, 36, 40, 45, 48, 55, 67, 78, 80, 85, 86, 88, 92, 96, 97, 102, 105, 106, 115 | 自定义 ML 的准备、训练、部署、监控和 MLOps。AIP-C01 常考 Clarify、Model Monitor、Feature Store、Processing 和推理模式。 | 完整的机器学习工厂：从备料、训练、质检到上线和持续监控都有工位。 |
| Amazon Comprehend / Comprehend Medical | 15 | 5, 12, 21, 29, 38, 39, 40, 45, 48, 61, 80, 88, 92, 94, 98 | NLP：实体、情感、主题、PII；Medical 面向医疗文本。适合结构化分析，不是通用生成模型。 | 阅读分析员：从文章中标出人名、情绪、主题和敏感信息。 |
| Amazon Rekognition | 3 | 40, 77, 80 | 图像/视频分析，如对象、人脸、内容审核和标签检测。 | 视觉检查员：观看照片和视频，识别人脸、物体及不适当内容。 |
| Amazon Textract | 4 | 36, 40, 88, 106 | 从扫描文档提取文字、表格和表单结构；比普通 OCR 更关注文档结构。 | 录入员：把纸质表格和发票中的文字、栏目及表格准确录入系统。 |
| Amazon Transcribe | 4 | 39, 80, 93, 106 | 语音转文字；若题目要“理解/摘要生成”，通常还要接 Comprehend 或 Bedrock。 | 会议速记员：把录音实时或批量整理成文字稿。 |
| Amazon Lex | 2 | 39, 55 | 构建语音/文本对话接口，处理 Intent 和 Slot；不要与负责自由文本生成的 Bedrock 混为一谈。 | 呼叫中心接线员：识别来电目的，并逐项询问办理业务所需信息。 |
| Amazon Kendra | 10 | 7, 16, 26, 36, 38, 64, 65, 72, 84, 101 | 企业搜索和连接器；题目常与 Bedrock Knowledge Bases、OpenSearch 比较。 | 熟悉公司所有资料库的企业图书管理员，能跨系统找答案。 |
| Amazon Q Business | 3 | 46, 64, 80 | 面向企业数据的生成式 AI 助手；偏成品化企业问答和连接器。 | 熟悉公司制度和资料的现成数字同事，员工可以直接向它提问。 |
| Amazon Q Developer | 2 | 41, 111 | 面向开发、理解、构建和运维 AWS 应用的助手；不是企业知识库产品。 | 坐在开发者旁边的结对工程师，协助写代码、理解系统和排障。 |
| Amazon Augmented AI（A2I） | 2 | 36, 55 | 为 ML 预测建立人工复核工作流；题目中也可能直接写 Amazon A2I。 | 自动质检不确定时，把产品送到人工质检员手中复核。 |

### SageMaker 子能力清单

| 子能力 | 关联题号 | 应掌握的边界 |
|---|---|---|
| SageMaker Clarify | 1, 34, 36 | 偏差检测、可解释性和公平性分析。 |
| SageMaker Model Monitor | 18, 22, 45, 86 | 监控数据质量、模型质量、偏差漂移和特征归因漂移。 |
| SageMaker Feature Store | 12, 80 | 统一保存训练与推理使用的特征，减少 training-serving skew。 |
| SageMaker Processing | 48, 80 | 托管数据预处理、后处理和评估作业。 |
| SageMaker Data Wrangler | 45, 78 | 可视化准备和转换数据。 |
| SageMaker Canvas | 9 | 面向低代码/无代码 ML 使用者。 |
| SageMaker JumpStart | 67 | 预训练模型、解决方案和示例的快速起点。 |
| SageMaker Neo | 67 | 针对目标硬件优化模型。 |
| SageMaker Asynchronous Inference | 102 | 处理较大 Payload、较长处理时间和异步响应。 |

> **时效提醒（2026-08-29）**：题库仍会按题目给定的产品语境考 SageMaker Model Monitor；但 AWS 当前官方文档注明，该能力已不再向新客户开放。做题时按题干判断，做真实架构时必须重新查看最新服务可用性。

---

## 10. 阶段八：Amazon Bedrock 专项

Amazon Bedrock 在 85 道题的选项中出现，是整套题库的核心。建议按下面顺序学习，而不是一次把所有功能混在一起。

### 10.1 基础模型调用

先掌握三件事：选择模型、构造 Prompt、调用推理 API。

- `InvokeModel` / `InvokeModelWithResponseStream`：模型原生请求格式，流式版本逐步返回内容。
- `Converse` / `ConverseStream`：统一的多轮对话接口，便于在支持的模型之间使用一致消息结构。
- 题库模型族：**Amazon Nova**（题 11、13、79）与 **Amazon Titan / Titan Embeddings**（题 39、65、92、97、101）。模型不是 AWS 服务，应在 Bedrock 下学习。

关联题：3, 10, 15, 23, 34, 38, 39, 54, 71, 73, 88, 114, 117。

### 10.2 Prompt Management

集中保存、版本化和复用 Prompt，可管理 Prompt 变体。它解决的是 Prompt 生命周期，不负责复杂业务编排。

关联题：1, 5, 9, 11, 84, 106, 110。

### 10.3 Guardrails

对模型输入和输出应用安全策略。重点包括：

- Content Filters：有害内容类别和强度。
- Denied Topics：按业务语义禁止话题。
- Word Filters：屏蔽具体词语或名称。
- Sensitive Information Filters：检测、遮盖或阻止 PII。
- Contextual Grounding：检查响应是否有依据、是否与问题相关。
- Automated Reasoning：用形式化规则验证响应中的逻辑结论。

Guardrails 可以用于模型推理，也能与 Agent、Knowledge Base 和 Flow 结合；它不是公平性监控、基础设施 WAF 或完整事实核查系统的替代品。

关联题：1, 2, 5, 11, 15, 18, 21, 25, 30, 36, 40, 55, 61, 77, 80, 81, 84, 86, 90, 91, 99, 105, 106, 111。

### 10.4 Knowledge Bases 与 RAG

标准 RAG 流程：

```text
文档进入数据源
  → 切分 Chunk
  → Embedding 模型生成向量
  → 向量存储建立索引
  → Retrieve 检索相关片段
  → Generate 基于片段生成回答
```

要重点理解：Chunking、Embedding、向量距离、Metadata Filter、Hybrid Search、Rerank、引用、同步数据源和 `Retrieve`/`RetrieveAndGenerate` 的差别。

可能的底层检索选择包括 OpenSearch Serverless、Aurora PostgreSQL、Neptune Analytics 和 S3 Vectors。Knowledge Bases 是托管 RAG 能力，不等于底层向量数据库。

关联题：3, 6, 15, 17, 24, 26, 28, 30, 32, 38, 46, 56, 58, 62, 64, 65, 72, 80, 86, 88, 90, 97, 101, 106, 107, 108；Rerank API 见题 88。

### 10.5 Agents 与 AgentCore

- **Bedrock Agents**：围绕 FM 进行任务规划，调用 Action Group/API，并使用 Knowledge Base。
- **Bedrock AgentCore**：更偏向把不同框架、模型构建的 Agent 安全地部署和运营，包括运行时、身份、记忆、网关与可观测性等基础能力。

Agents 关联题：16, 26, 42, 54, 58, 62, 84, 86, 97, 99, 106, 107, 111。
AgentCore 关联题：16, 107, 111。

### 10.6 Flows 与 Step Functions

- **Bedrock Flows**：把 Prompt、模型、Knowledge Base、Agent 和条件节点组成生成式 AI 流程。
- **Step Functions**：通用 AWS 服务编排，适合跨服务、长时间等待、人工审批、重试和审计。

看到纯 GenAI 节点连接时考虑 Flows；看到跨多个 AWS 服务的业务状态机时优先评估 Step Functions。

Flows 关联题：1, 29, 54, 97, 110。

### 10.7 Model Evaluation、Clarify 与生产监控

- **Bedrock Model Evaluation**：比较模型/Prompt 在质量、安全等维度上的表现，可使用自动或人工评估方式。
- **SageMaker Clarify**：偏差、公平性和可解释性分析。
- **CloudWatch**：在线指标、日志、告警与 Dashboard。
- **人工评审/A2I/Step Functions**：监管或高风险场景中的 Human-in-the-loop。

Model Evaluation 关联题：1, 69, 86, 115。

### 10.8 Bedrock Data Automation

用于处理文档、图像、音频和视频等非结构化内容。标准输出覆盖这些模态；基于 Blueprint 的自定义输出适用于文档、图像和音频。它把多模态内容理解和抽取整合成托管流程，但不能简单理解为 Textract、Transcribe、Rekognition 的同义替代品。

关联题：106。

### 10.9 Bedrock 其他题库能力

| 能力 | 关联题号 | 一句话定位 |
|---|---|---|
| Bedrock Studio | 9 | 团队构建和试验生成式 AI 应用的环境。 |
| Bedrock Rerank API | 88 | 对初步检索结果重新排序，提高最终上下文相关性。 |
| Bedrock Converse API | 73 | 统一多轮消息接口。 |
| Bedrock Custom Model Import | 题干出现，选项未直接命名 | 把支持的自定义模型导入 Bedrock 托管调用。 |

---

## 11. 所有 AWS 名称的归类校正

下面这些名称确实出现在选项中，但不应重复统计为独立顶层服务：

| 所属服务 | 选项中的子功能/产品名称 |
|---|---|
| Amazon Bedrock | Guardrails、Knowledge Bases、Agents、AgentCore、Flows、Prompt Management、Model Evaluation、Data Automation、Studio、Rerank API、Converse API、InvokeModel API |
| Amazon S3 | S3 Express One Zone、S3 Lifecycle、S3 Object Lock、S3 Vectors |
| Amazon SageMaker AI | Clarify、Model Monitor、Feature Store、Processing、Canvas、Data Wrangler、JumpStart、Neo、Asynchronous Inference |
| Amazon CloudWatch | Logs、Logs Insights、Synthetics、Container Insights、Application Insights、Anomaly Detection |
| AWS CloudTrail | CloudTrail Lake、CloudTrail Insights |
| Amazon API Gateway | REST API、HTTP API、WebSocket API |
| AWS Step Functions | Standard Workflows、Express Workflows |
| Amazon OpenSearch Service | OpenSearch Serverless |
| Amazon DynamoDB | DynamoDB Accelerator（DAX） |
| Amazon RDS / Aurora | Aurora PostgreSQL Serverless、RDS Data API |
| AWS Glue | Glue ETL、Glue Data Catalog、Glue Data Quality |
| Amazon Comprehend | Comprehend PII、Comprehend Medical |
| AWS Systems Manager | Parameter Store、Automation |
| Amazon EC2 | EC2 Auto Scaling |
| Amazon EventBridge | EventBridge Pipes |

另外，以下名称是工具、框架、模型或方法，也不要当成独立 AWS 服务背诵：

- AWS SDK、AWS CDK：开发工具/框架。
- AWS Well-Architected Generative AI Lens：架构评审指导；在题 68 的选项中出现。
- Amazon Nova、Amazon Titan Embeddings、Titan Multimodal Embeddings：Bedrock 中的模型家族。
- RAG、Embedding、Semantic Search、Hybrid Search、Chunking、Rerank：架构方法或检索概念。
- IAM Policy、SCP、LF-Tag：权限控制机制，不是独立服务。

### 严格范围说明

AWS Control Tower、Amazon EKS、Amazon Kinesis Data Streams 等名称在部分**题干**中出现，但未作为 English 选项中的服务名称出现，所以没有加入上面的主统计；Amazon Kinesis 的通用名称在题 31、80 的选项中出现，因此保留为学习单元。

---

## 12. 14 天学习安排

| 天数 | 学习主题 | 回题库练习 |
|---|---|---|
| Day 1 | IAM、STS、Identity Center、Organizations | 8, 13, 37, 58, 85, 87 |
| Day 2 | KMS、Secrets Manager、CloudTrail、Config、Macie | 9, 61, 77, 85, 100, 104, 105 |
| Day 3 | VPC、PrivateLink、边缘与计算选择 | 34, 69, 71, 72, 96, 100, 116 |
| Day 4 | S3、RDS/Aurora、DynamoDB、缓存 | 6, 24, 47, 51, 63, 92, 105 |
| Day 5 | OpenSearch、向量数据库、Neptune | 6, 17, 48, 65, 88, 104, 106 |
| Day 6 | SQS、SNS、EventBridge、Step Functions | 16, 35, 44, 54, 78, 111, 114, 117 |
| Day 7 | API Gateway、AppSync、Amplify、AppConfig | 3, 4, 52, 59, 73, 75, 103 |
| Day 8 | CloudWatch、X-Ray、CloudTrail 综合 | 1, 31, 66, 76, 84, 109, 112 |
| Day 9 | Glue、Athena、Kinesis、Firehose、QuickSight | 18, 19, 45, 62, 70, 80 |
| Day 10 | Comprehend、Textract、Transcribe、Rekognition、Lex | 29, 36, 39, 40, 55, 88, 93 |
| Day 11 | SageMaker AI 生命周期与监控 | 1, 12, 22, 34, 45, 67, 102 |
| Day 12 | Bedrock 推理、Prompt、Guardrails | 2, 5, 11, 21, 73, 91, 99 |
| Day 13 | Knowledge Bases、RAG、向量检索、Rerank | 6, 15, 17, 28, 65, 88, 101, 108 |
| Day 14 | Agents、AgentCore、Flows、Model Evaluation | 1, 16, 54, 69, 86, 97, 107, 111, 115 |

每天建议执行同一个闭环：

1. 用 30–45 分钟理解服务边界。
2. 画一张不超过 8 个方框的架构图。
3. 做表中对应题目，并说明每个错误选项为什么错。
4. 把错因归到：服务边界、实时性、成本、权限、安全、开发量、运维复杂度之一。

---

## 13. 官方资料入口

- [Amazon Bedrock User Guide](https://docs.aws.amazon.com/bedrock/latest/userguide/what-is-bedrock.html)
- [Amazon Bedrock Knowledge Bases](https://docs.aws.amazon.com/bedrock/latest/userguide/knowledge-base.html)
- [Amazon Bedrock Guardrails](https://docs.aws.amazon.com/bedrock/latest/userguide/guardrails.html)
- [Amazon Bedrock AgentCore Developer Guide](https://docs.aws.amazon.com/bedrock-agentcore/latest/devguide/what-is-bedrock-agentcore.html)
- [Amazon Bedrock Data Automation](https://docs.aws.amazon.com/bedrock/latest/userguide/bda-how-it-works.html)
- [Amazon S3 Vectors](https://docs.aws.amazon.com/AmazonS3/latest/userguide/s3-vectors.html)
- [Amazon SageMaker AI Developer Guide](https://docs.aws.amazon.com/sagemaker/latest/dg/whatis.html)
- [Amazon SageMaker Model Monitor](https://docs.aws.amazon.com/sagemaker/latest/dg/model-monitor.html)
- [Amazon Q Documentation](https://docs.aws.amazon.com/amazonq/)
- [AWS Architecture Center](https://aws.amazon.com/architecture/)

## 14. 最后的做题判断顺序

遇到架构选择题时，按下面顺序排除：

1. **先看硬约束**：Region、私网、数据驻留、延迟、吞吐、人工审批、保留时间。
2. **再看服务边界**：监控不是审计，事件路由不是流程编排，内容安全不是网络 WAF。
3. **再看托管程度**：题目要求 least operational overhead / least custom development 时，优先评估托管能力。
4. **再看时间模型**：同步、异步、流式、批处理、长时间等待分别对应不同方案。
5. **最后看成本**：只有满足前四项的方案，成本优化才有意义。
