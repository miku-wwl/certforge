## Question 61

### 中文

#### 考点背景

这题考的是把安全控制放在正确的边界上。Bedrock Guardrails 的 Sensitive Information Policy 可以在请求进入模型前、响应返回用户前检查 PII，并按策略过滤或脱敏；Topic Policy 则是针对“投资建议”这类不允许讨论的主题做阻断。两者分别处理“数据不能出现”和“话题不能回答”，不是靠模型自觉。

审计要求又覆盖文字、图片和文档，所以必须启用 Bedrock 的调用与交付日志，把交互材料集中送入 S3。关键线索是“输入和输出”“主题阻断”“图像/文档日志”“最低运维”：同一托管服务中的策略和日志组合，明显优于拼装 Macie、Lambda、Comprehend 或只写 Prompt。

#### 场景比喻

把银行助手想成柜台：Guardrails 是进门和出门各有一台身份证扫描器，Topic Policy 是柜员面前的“投资建议禁答”红线；S3 日志像全程录像柜，连递进来的文件和图片也留档。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 闸门与禁答清单 | 输入/输出过滤 PII，并阻断投资建议主题 |
| Sensitive Information Policy | 隐私检查员 | 识别并过滤敏感信息，避免 PII 进入模型或回到客户 |
| Topic Policy | 话题红线 | 对投资建议请求执行主题级拒答 |
| Bedrock Invocation/Delivery/Image Logging | 全程录像柜 | 记录模型调用及传输的文字、文档和图片到 S3 |

#### 正确答案与推理

1. 先处理 PII 的输入约束：Sensitive Information Policy 在模型调用边界提供托管过滤，满足“Prompt 不含 PII”。
2. 再处理输出约束：同一策略检查模型返回，避免助手把 PII 泄露给客户。
3. Topic Policy 直接表达“不得回应投资建议”，比软性的 Prompt 指示更可控。
4. Invocation、Delivery 和 Image Logging 覆盖交互中的调用内容及多模态传输，并集中交付到 S3，形成审计材料。
5. 所有核心控制由 Bedrock 托管，因而在最低运维量下同时满足安全、话题和审计要求。

#### 逐项排除

- **A：** Macie 擅长发现和分类 S3 中的数据，不是实时的模型输入/输出闸门；CloudTrail 记录 API 活动，也不等于完整对话内容日志。
- **B：** Lambda 加 Comprehend 需要自建处理链；Topic Modeling 用于分析主题，不能替代可靠的主题阻断策略，CloudWatch 自定义指标也不是完整会话存档。
- **C：** 正确。Guardrails 分别覆盖 PII 和禁谈主题，Bedrock 日志覆盖调用、交付与图片，组合最贴合全部要求。
- **D：** Regex 只能覆盖已知模式，Prompt Engineering 不是强制安全边界；它还缺少 Topic Policy，无法稳定阻断投资建议。

#### 解题方法

看到“输入也不能有敏感数据”要想到 Guardrails 的 Sensitive Information Policy；看到“不能讨论某主题”要想到 Topic Policy；看到图片/文档审计要核对 Invocation、Delivery、Image Logging。最后用“托管内建控制 vs 自定义拼装”判断最低运维。

### English

#### Exam focus and background

This question tests whether each control is placed at the model boundary where it is enforceable. A Bedrock Guardrail sensitive-information policy can inspect the request before it reaches the foundation model and inspect the response before it reaches the customer. That addresses PII in prompts and PII in returned content. A topic policy expresses a hard business boundary for a prohibited subject such as investment advice; it is stronger and more auditable than asking a model to follow a prompt instruction.

The logging requirement is broader than API auditing. The bank wants evidence of the interaction, including transmitted documents and images. Bedrock invocation, delivery, and image logging can be configured as a managed logging path to S3. The “least operational effort” clue favors these native controls over a custom Lambda and Comprehend pipeline, or over trying to make Macie and CloudTrail serve as conversation-content controls.

#### Analogy

Imagine the assistant as a bank counter. The sensitive-information policy is an identity scanner at both the entrance and exit, the topic policy is a red line on the teller’s desk, and Bedrock logging is a vault camera that records the conversation and every document or image handed across the counter.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Gate and prohibited-topic list | Enforces PII filtering and investment-advice blocking |
| Sensitive information policy | Privacy inspector | Detects and filters sensitive information on the request and response path |
| Topic policy | Business red line | Blocks responses about the prohibited investment-advice topic |
| Bedrock invocation, delivery, and image logging | Audit camera vault | Retains model calls and transmitted text, documents, and images in S3 |

#### Correct answer and reasoning

1. Apply the sensitive-information policy before inference so PII is filtered before the prompt is sent to the FM.
2. Apply the same boundary to the response so the assistant does not disclose PII to the customer.
3. Use the topic policy to enforce a direct refusal for investment-advice requests.
4. Enable the Bedrock logging paths that retain model invocation and multimodal delivery evidence in S3.
5. Because these controls are managed Bedrock features, the combination satisfies security, content, and audit requirements with the least operational work.

#### Option-by-option elimination

- **A:** Macie is primarily an S3 data-discovery and classification service, not an inline model-input/output control; CloudTrail API events are not a complete conversation transcript.
- **B:** Lambda and Comprehend create a custom processing chain. Topic modeling analyzes topics but does not itself enforce a dependable block, and custom CloudWatch metrics do not preserve the full interaction.
- **C:** Correct. It maps each requirement to a native Bedrock policy or logging capability.
- **D:** Regex has limited pattern coverage and prompt instructions are not an enforcement boundary; the option also lacks a topic policy.

#### Exam strategy

Map every noun in the stem to a control: “PII in and out” means a sensitive-information guardrail, “forbidden subject” means a topic policy, and “images/documents in the audit” means the relevant Bedrock logging paths. For least effort, prefer one managed control plane over several custom services.

---
## Question 62

### 中文

#### 考点背景

这题把透明性、审计、规模和延迟放在同一张架构图里。Agent Tracing 记录代理如何编排步骤、检索知识和调用模型；它提供可审计的执行轨迹，并不意味着应该把模型的隐藏思维链原样展示给用户。Structured Prompt 可以要求回答给出证据或引用，Knowledge Bases 则以托管 RAG 提供检索和来源关联。

10,000 个并发用户与 2 秒响应要求考的是托管扩展能力和路径长度。API Gateway 与 Lambda 能按需扩缩，Multi-AZ 提高应用层可用性，CloudFront 把静态或可缓存的前端交付推近用户。选项 A 用较少自建组件覆盖透明回答、RAG、审计和规模，因此是最低运维的组合。

#### 场景比喻

像一家大型客服中心：Tracing 是每张工单的流程追踪，Knowledge Bases 是带页码的共享档案柜，Structured Prompt 要求客服把依据写在答复旁；API Gateway/Lambda 是可弹性增开的窗口，CloudFront 是各城市的前台。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Bedrock Agent Tracing | 工单流程追踪器 | 记录代理编排、检索和模型调用步骤 |
| Bedrock Knowledge Bases | 带索引和出处的档案柜 | 托管 RAG，帮助回答引用权威来源 |
| Structured Prompt | 答复格式模板 | 要求模型展示证据，而不是只给结论 |
| API Gateway + Lambda + Multi-AZ | 可伸缩客服窗口 | 承载并发请求并提高应用可用性 |
| CloudFront | 城市前台 | 降低面向用户的交付延迟 |

#### 正确答案与推理

1. 用 Agent Tracing 留下代理执行、检索和调用的完整轨迹，满足透明与审计的运行证据要求。
2. 用 Structured Prompt 指示输出证据，并用 Knowledge Bases 提供 RAG 来源，避免只靠模型记忆。
3. 用 Multi-AZ、API Gateway 和 Lambda 承接大规模并发，减少自管服务器。
4. 用 CloudFront 缩短用户到应用边缘的交付路径，辅助 2 秒目标。
5. 该方案把核心能力交给 Bedrock 和无服务器托管服务，满足最低运维开销。

#### 逐项排除

- **A：** 正确。Tracing、结构化证据、托管 Knowledge Bases、弹性 API 与边缘交付分别命中题干要求。
- **B：** 自建 OpenSearch RAG、日志和引用链路需要更多维护；CloudTrail 主要是 API 审计，不是完整回答内容审计。
- **C：** 监控延迟不等于实现 RAG 或引用；把 Prompt 硬编码也不能保证来源，RDS 交互存储还增加应用责任。
- **D：** S3 版本和离线合规报告适合留存分析，不适合 2 秒内的在线检索和回答。

#### 解题方法

把需求拆成四列：执行透明度、知识来源、在线规模、审计。看到 Agent、检索和调用链选 Tracing；看到来源选 Knowledge Bases；看到并发和 2 秒选托管弹性路径。注意“记录推理”在考试语境中通常指可观察的执行轨迹和证据，不是暴露隐藏思维链。

### English

#### Exam focus and background

This question combines transparency, auditability, retrieval, scale, and latency. Bedrock agent tracing records how an agent orchestrates steps, retrieves knowledge, and invokes models. That is an observable execution record; it should not be interpreted as a requirement to expose a model’s private chain of thought. Structured prompts can require evidence or citations in the answer, while Knowledge Bases supplies managed retrieval-augmented generation and source associations.

The concurrency and two-second clues favor managed, elastic components. API Gateway and Lambda can absorb changing request volume without server management, Multi-AZ improves application availability, and CloudFront can move user-facing delivery closer to customers. Option A covers the full path with native retrieval and tracing, so it provides the requested behavior with less custom search and logging maintenance.

#### Analogy

Picture a global support center. Tracing is the ticket’s process history, Knowledge Bases is a shared filing cabinet with page references, and the structured prompt is a response template that makes the agent write its evidence beside the answer. API Gateway and Lambda open more service windows when the line grows; CloudFront is a local reception desk in each city.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock agent tracing | Ticket process history | Records orchestration, retrieval, and model-invocation steps |
| Bedrock Knowledge Bases | Indexed source cabinet | Provides managed RAG and authoritative source references |
| Structured prompt | Response template | Directs the FM to present supporting evidence |
| API Gateway, Lambda, and Multi-AZ | Elastic service windows | Handles concurrency and improves application availability |
| CloudFront | Local reception desk | Reduces the user-facing delivery path |

#### Correct answer and reasoning

1. Enable agent tracing to retain observable evidence of orchestration, retrieval, and invocation.
2. Use structured prompts to ask for evidence and Knowledge Bases to supply grounded sources.
3. Use Multi-AZ, API Gateway, and Lambda to serve a large concurrent population with managed scaling.
4. Use CloudFront to reduce delivery latency for the user-facing application.
5. This managed combination meets transparency, audit, RAG, scale, and latency requirements without operating a custom search and logging stack.

#### Option-by-option elimination

- **A:** Correct. Each native component maps directly to a stated requirement.
- **B:** A custom OpenSearch RAG and audit pipeline increases operations; CloudTrail records API activity, not a complete response audit.
- **C:** Monitoring latency does not create RAG or citations, and hard-coded prompts cannot guarantee authoritative sources; RDS adds application-managed audit work.
- **D:** Versioned S3 files and periodic Athena reports are useful for offline compliance, not a two-second interactive answer path.

#### Exam strategy

Translate the stem into four buckets: execution evidence, source grounding, online scale, and audit. “Agent steps” points to tracing; “citations” points to Knowledge Bases; high concurrency and low latency point to managed elastic services. In exam wording, “document reasoning” generally means traceable execution and evidence, not disclosure of hidden model reasoning.

---

## Question 63

### 中文

#### 考点背景

S3 元数据不是一个单一抽屉，而是三种用途不同的对象级信息。System Metadata 由 S3 维护，适合时间戳、Content-Type 等系统属性；Object Tags 是可单独管理的键值标签，适合研究领域这类分类；User-Defined Metadata 是上传者通过自定义元数据键写入的键值，适合作者等业务字段。题目要求的三类字段正好一一映射。

“统一结构”和“FM 不读完整正文”是架构线索：上传流程要强制固定 key 名、类型和允许值，检索时先把这些紧凑字段作为上下文或过滤条件交给应用/检索层。元数据提升召回精度并减少无谓读取，但它不是正文内容，也不能代替真正的文档索引。

#### 场景比喻

每篇论文像图书馆的一本书：S3 System Metadata 是图书馆盖的入馆日期章，Object Tag 是书架上的“免疫学/神经科学”标签，User-Defined Metadata 是作者卡片；检索员先看卡片和标签，再决定是否打开整本书。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| S3 System Metadata | 馆方登记章 | 保存对象时间戳等 S3 管理的系统信息 |
| S3 Object Tags | 书架分类标签 | 表示研究领域，便于筛选与治理 |
| S3 User-Defined Metadata | 作者卡片 | 保存作者等上传方定义的字段 |
| 上传约定/检索层 | 编目规则与索引员 | 统一字段结构，并先用元数据提供上下文 |

#### 正确答案与推理

1. 文档时间戳属于对象系统属性，放在 S3 System Metadata 最自然。
2. 研究领域是分类维度，使用 Object Tags 可独立读取、筛选和管理。
3. 作者信息不是 S3 固有属性，用 User-Defined Metadata 表达自定义字段。
4. 对所有上传建立同样的 key、值格式和校验规则，才真正保证结构一致。
5. 检索流程先读取元数据，FM 可获得文档背景线索而无需先载入完整正文，故 A 完整命中要求。

#### 逐项排除

- **A：** 正确。三个字段分别落在合适的 S3 对象级元数据机制，且能配合统一上传契约。
- **B：** Object Lock 的 Legal Hold 是防删除保留控制，不是时间戳字段；Access Point 是访问入口，不是领域分类器。
- **C：** Inventory 是批量清单，Storage Lens 是存储分析；二者和 Access Point 都不是每个对象的统一作者/领域元数据框架。
- **D：** Object Lock Retention Period 规定保留期限而非记录时间戳，Event Notification 传递事件也不会写入领域分类。

#### 解题方法

先按“系统属性、分类标签、自定义业务字段”三类分桶，再检查是否为对象级、可统一、可在正文外读取。看到时间戳优先 System Metadata，分类优先 Tags，业务字段优先 User-Defined Metadata；Lock、Inventory、Access Point、Notification 多半是干扰的控制面能力。

### English

#### Exam focus and background

S3 metadata is not one generic drawer. S3 system metadata is maintained by S3 and is appropriate for system properties such as object timestamps. Object tags are independently manageable key-value labels, which fit a research-domain classification. User-defined metadata is supplied by the uploader through custom metadata keys and fits business fields such as author information. The three requested fields therefore map cleanly to three different object-level mechanisms.

The consistency and context clues concern the upload and retrieval contract. Every uploader should use the same key names, value formats, and validation rules. The retrieval layer can read compact metadata first and use it as a filter or context signal before deciding whether to fetch the document body. Metadata improves precision and avoids unnecessary reads, but it is not a replacement for a full document index or the document’s content.

#### Analogy

Treat each paper as a library book. System metadata is the library’s arrival-date stamp, object tags are the “neurology” or “immunology” shelf labels, and user-defined metadata is the author card supplied with the book. A researcher checks the card and labels before opening the whole book.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| S3 system metadata | Library registration stamp | Holds S3-managed object properties such as timestamps |
| S3 object tags | Shelf classification labels | Represents research domains for filtering and governance |
| S3 user-defined metadata | Author card | Stores uploader-defined fields such as author information |
| Upload contract and retrieval layer | Cataloging rule and librarian | Enforces a common schema and supplies context before full-content access |

#### Correct answer and reasoning

1. Map the document timestamp to S3 system metadata because it is a system-level object property.
2. Map research domain to object tags because it is a classification dimension that can be managed separately.
3. Map author information to user-defined metadata because it is a custom business field.
4. Enforce the same keys, formats, and validation during every upload to preserve a consistent structure.
5. Read the compact object metadata as retrieval context so the FM can understand useful document signals without first reading the full body.

#### Option-by-option elimination

- **A:** Correct. It assigns each field to the appropriate S3 object-level metadata facility.
- **B:** Object Lock legal holds protect against deletion; they do not store timestamps as a metadata framework. Access Points are access entry points, not domain classifiers.
- **C:** Inventory is a batch listing and Storage Lens is storage analytics; neither supplies per-object author metadata, and an Access Point is not a classification field.
- **D:** Object Lock retention is a retention rule rather than a timestamp field, and event notifications deliver events rather than implement domain classification.

#### Exam strategy

Sort the requested fields into system property, classification, and custom business data. Then ask whether the mechanism is object-level, consistently writable, and readable without opening the body. Timestamp usually signals system metadata, a category signals tags, and an organization-specific field signals user-defined metadata.

---

## Question 64

### 中文

#### 考点背景

本题的核心是多租户数据所有权与托管检索的边界。客户自己建立并控制 Amazon Q Business Index，数据仍由客户掌握；客户再把 Example Corp 指定为受控的 Data Accessor，服务方只通过授权 API 获取相关上下文。这样把“谁拥有索引”和“谁能查询”分开，既满足治理，又避免 Example Corp 在数百万客户环境里部署一套服务。

题干明确“不要求实时访问”，因此不必为每次 Prompt 去客户源系统做联邦检索。Q Business 的托管索引适合语义检索，运行时延迟低；Data Accessor 则提供受权限约束的共享方式。考试中的最小复杂度信号是：客户侧一次配置授权，服务侧使用标准托管能力，而不是按客户管理服务器、爬虫或跨账户数据管道。

#### 场景比喻

像连锁企业把每家门店的档案柜留在店内：店主拥有钥匙和档案，Example Corp 不是搬走档案，而是拿到一张可撤销的“代查证”。需要做视频时，只取被授权的几页资料放进 Prompt。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Q Business Index | 客户自有档案柜 | 客户建立并控制索引，保留数据所有权 |
| Q Business Data Accessor | 可撤销的代查证 | 授权 Example Corp 通过安全 API 检索相关内容 |
| Q Business 语义检索 | 训练有素的档案员 | 以高语义准确率和低延迟找出客户上下文 |
| Example Corp GenAI 应用 | 获准的调阅人 | 在运行时把授权检索结果用于 Prompt 增强 |

#### 正确答案与推理

1. 先满足所有权：让每个客户自己创建和控制 Q Business Index，Example Corp 不成为数据所有者。
2. 再满足访问控制：客户明确指定 Data Accessor，服务方只能在授权边界内调用安全 API。
3. 用托管索引完成语义检索，满足高准确率和低延迟，而无需实时连接原始数据源。
4. 运行时只取与当前视频 Prompt 相关的内容，减少跨租户数据暴露和集成负担。
5. 该模式避免每个客户部署 MCP、Knowledge Base 或 Kendra 管理面，因而最简单。

#### 逐项排除

- **A：** 正确。客户控制索引和授权，Example Corp 仅作为 Data Accessor，正好平衡治理、检索和规模。
- **B：** 每个客户一个实时 MCP Server 会产生部署、版本、可用性和安全运维；题目又没有实时数据需求。
- **C：** 每户 Knowledge Base 及跨账户查询会增加连接器、权限和生命周期管理，且不是最小集成。
- **D：** Kendra 爬取和跨账户索引共享带来爬虫、索引、权限及更新运维，明显重于托管访问者模式。

#### 解题方法

先圈出“客户保留完整所有权”“不需实时”“高语义准确率”“低延迟”“数百万租户”。所有权看客户控制的索引，语义和延迟看托管搜索，最小运维看一次授权而不是逐租户部署。不要被“实时”方案的先进感带偏。

### English

#### Exam focus and background

The key issue is the boundary between tenant ownership and controlled retrieval. Each customer creates and controls an Amazon Q Business index, so the customer remains the data owner. The customer then designates Example Corp as a restricted Data Accessor. Example Corp can retrieve only the content exposed through the authorized API and use that content to enrich a prompt.

The requirement explicitly says that real-time access to the customer’s source systems is unnecessary. That removes the need to federate every prompt through a customer-side server. A managed semantic index can provide accurate, low-latency retrieval, while the accessor relationship supplies the governance boundary. At the scale of millions of customers, a customer-managed index plus delegated access is much lighter than operating a crawler, MCP server, or cross-account knowledge-base pattern for every tenant.

#### Analogy

Imagine every store in a chain keeps its own records cabinet. The store owns the papers and controls the key; Example Corp receives a revocable “researcher badge” that permits selected lookups. When generating a video, the service retrieves only the authorized pages relevant to that prompt.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Q Business index | Customer-owned records cabinet | Lets each customer create and control its indexed data |
| Q Business Data Accessor | Revocable researcher badge | Grants Example Corp controlled API access to relevant content |
| Q Business semantic retrieval | Skilled records clerk | Supplies accurate, low-latency customer context |
| Example Corp GenAI application | Authorized reader | Uses permitted retrieval results for runtime prompt augmentation |

#### Correct answer and reasoning

1. Preserve ownership by having each customer create and control its own Q Business index.
2. Preserve least privilege by having the customer explicitly designate Example Corp as a Data Accessor.
3. Use the managed semantic index to meet accuracy and latency requirements without a live source-system connection.
4. Retrieve only relevant, authorized context at runtime and add it to the video-generation prompt.
5. This avoids a per-customer MCP server, Knowledge Base, or Kendra lifecycle, giving the lowest integration complexity.

#### Option-by-option elimination

- **A:** Correct. It separates customer control from delegated, secure retrieval and matches the non-real-time requirement.
- **B:** A real-time MCP server per customer adds deployment, versioning, availability, and security operations that the scenario does not need.
- **C:** Per-customer Knowledge Bases and cross-account queries create more connectors, permissions, and lifecycle management; they are not the simplest pattern.
- **D:** Kendra crawling and cross-account index sharing add crawler, index-refresh, permission, and tenancy operations.

#### Exam strategy

Highlight “customer owns and controls,” “no real-time requirement,” “high semantic accuracy,” and “low latency.” Ownership points to a customer-controlled index; semantic retrieval points to a managed search index; least effort points to delegated access rather than a server or crawler per tenant.

---

## Question 65

### 中文

#### 考点背景

法律搜索同时有两种看似相反的需求：理解“过失”和“注意义务”之间的语义关系，需要 Vector Search；又必须精确命中法条编号、案号、拉丁术语和引文，需要 Keyword Search。只用向量可能把概念相近但引用错误的案件排在前面，只用关键词又会错过不同措辞表达的同一法律概念。

Hybrid Search 先融合两类候选，Bedrock Reranker Model 再以查询与文档的整体相关性重排。考试要看的是能力链，而不只是“有一个向量数据库”：语义召回解决同义和概念关系，词法召回守住精确术语，重排把最终结果变得更准，同时使用 OpenSearch 的托管能力降低自定义维护。

#### 场景比喻

像法律图书馆有两位检索员：一位按意思理解“相似判例”，另一位逐字核对案号和条文；Reranker 是资深法官，把两份名单合并后按证据强弱重新排队。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| OpenSearch Vector Search | 按含义找书的检索员 | 识别法律概念和语义关系 |
| OpenSearch Keyword Search | 逐字核对员 | 命中术语、案号、引用和精确词项 |
| Bedrock Reranker Model | 资深复核法官 | 对混合候选按查询相关性重新排序 |
| OpenSearch Service | 法律馆目录系统 | 托管索引与快速检索基础设施 |

#### 正确答案与推理

1. 对“概念之间的语义关系”使用向量检索，避免只因措辞不同而漏掉相关案例。
2. 对“特定法律术语和引用”加入关键词检索，确保精确字面匹配。
3. 将两路候选交给 Bedrock Reranker，进一步判断查询与文档的整体相关性。
4. 用 OpenSearch 承载混合检索，保持查询快速，并减少自建索引和合并逻辑。
5. 该链路同时满足相关性、精确性和速度，所以 B 是完整方案。

#### 逐项排除

- **A：** 默认向量搜索和查询扩展能帮助语义召回，却不能稳定保证案号、引文和术语的精确命中，也没有混合重排。
- **B：** 正确。Vector、Keyword 和 Reranker 恰好对应三项核心检索要求。
- **C：** Query Suggestion 只改善用户输入，后处理并不能替代并行的词法/向量召回与专门重排。
- **D：** 自定义 Lambda 加 RDS 关键词合并可以拼出功能，但运维复杂、延迟路径更长，且缺少托管 Reranker。

#### 解题方法

把关键词“语义关系、精确术语/引用、快速、相关性优化”依次映射为 Vector、Keyword、混合索引/检索、Reranker。若选项只有向量或把精确过滤塞进自定义数据库，通常不如原生 Hybrid Search 完整。

### English

#### Exam focus and background

Legal retrieval has two different notions of relevance. Understanding that “duty of care” and a related phrase describe a similar concept calls for vector search. Matching a statute number, case identifier, Latin term, or citation calls for keyword search. Vector-only retrieval can return conceptually similar cases with the wrong citation, while keyword-only retrieval can miss a relevant case written with different wording.

Hybrid search combines both candidate sets, and a Bedrock reranker model can reorder those candidates using the query-document relationship as a whole. The exam is testing the complete retrieval chain: semantic recall, exact lexical recall, and final relevance ordering. Using OpenSearch Service for the index and query path also avoids unnecessary custom merging infrastructure.

#### Analogy

Picture two legal librarians. One searches by meaning and finds related precedents; the other checks every case number and citation character by character. The reranker is a senior judge who merges their lists and puts the strongest evidence first.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| OpenSearch vector search | Meaning-based librarian | Finds legal concepts and semantic relationships |
| OpenSearch keyword search | Exact-match clerk | Preserves terms, case numbers, and citations |
| Bedrock reranker model | Senior reviewing judge | Reorders mixed candidates by query relevance |
| OpenSearch Service | Managed law-library catalog | Hosts the index and fast retrieval path |

#### Correct answer and reasoning

1. Use vector search for semantic relationships so different wording does not hide relevant case law.
2. Add keyword search for exact terminology, case numbers, and citations.
3. Pass both candidate sets to a Bedrock reranker to improve the final relevance order.
4. Keep the hybrid index and query path in OpenSearch to support fast retrieval with less custom infrastructure.
5. This chain satisfies semantic coverage, exactness, speed, and precision, making B the complete design.

#### Option-by-option elimination

- **A:** Default vector search and query expansion help semantic recall but cannot reliably guarantee exact citation and terminology matches; it lacks hybrid reranking.
- **B:** Correct. Vector search, keyword search, and the reranker map directly to the three retrieval needs.
- **C:** Query suggestion improves query formulation; post-processing does not replace parallel lexical and semantic retrieval plus dedicated reranking.
- **D:** Custom Lambda and an RDS keyword store could assemble a solution, but they add operations and latency and omit a managed reranker.

#### Exam strategy

Map “semantic relationship” to vector search, “exact term or citation” to keyword search, and “optimize relevance” to reranking. When speed and precision are both present, prefer a managed hybrid path over a custom database-and-Lambda merge.

---

## Question 66

### 中文

#### 考点背景

这是一道“日志回答归因、指标回答告警”的可观测性题。Bedrock Invocation Log 含有调用细节，可用 CloudWatch Logs Insights 按应用、模型或 Tool 维度查询 Token 消耗，识别哪个业务单元在异常使用，并把查询结果放入自定义 Dashboard。它适合回答“发生了什么、谁用了多少”。

CloudWatch 原生 Token 和 Invocation Metrics 则适合持续的时间序列监控和阈值告警，能够横跨多个 FM 设置 Dashboard 与 Alarm。题目强调实时可见性、多个 Stakeholder 自定义视图、所有模型告警和最低运维；B 提供细粒度分析，C 提供低维护的监控告警，两者互补。

#### 场景比喻

像电网控制室：Logs Insights 是工程师翻查每台机器的详细电表，能追到部门和应用；CloudWatch Metrics 是墙上的总表，Alarm 是超过红线就响的警铃。两者一起才既能定位又能自动报警。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Bedrock Invocation Logging | 逐笔用量账本 | 保存模型调用和 Token 细节 |
| CloudWatch Logs Insights | 账本分析员 | 按应用/模型查询模式和用量归因 |
| CloudWatch Dashboard | 多角色控制屏 | 为不同 Stakeholder 展示自定义视图 |
| CloudWatch Metrics and Alarms | 总表与警铃 | 汇总 Token/Invocation 指标并触发阈值告警 |

#### 正确答案与推理

1. 开启 Invocation Logging，使 Token 细节有可查询的原始记录。
2. 用 Logs Insights 按应用分析 Token 模式，找出高使用量场景并加入日志 Widget，满足归因和自定义视图。
3. 用原生 Token 与 Invocation Metrics 构建跨 FM 的 CloudWatch Dashboard，避免额外 ETL。
4. 在指标上设置 CloudWatch Alarm，持续检测所有应用的 Token 阈值。
5. B 负责深挖，C 负责低开销实时监控和告警，组合最合适。

#### 逐项排除

- **A：** QuickSight 能做可视化，但刷新、数据治理和接入开销更高；该方案也没有同样直接的 CloudWatch Alarm 路径。
- **B：** 正确。Logs Insights 能从调用日志做按应用的 Token 分析与 Dashboard 监控。
- **C：** 正确。原生指标、Dashboard 和 Alarm 直接覆盖跨模型可见性与告警。
- **D：** 题干要求的低开销标准路径不是 Bedrock 到 Grafana 的所谓 Zero-ETL 组合，且未给出相应告警链。
- **E：** EventBridge、Firehose 和 OpenSearch Serverless 组成额外数据管道，运维组件多于直接使用 CloudWatch。

#### 解题方法

先分辨“要解释细节”还是“要连续告警”：日志+Logs Insights 用于归因和模式，Metrics+Alarm 用于趋势和阈值。多选题中，选能分别覆盖分析与告警、且不引入 ETL 的两个原生方案。

### English

#### Exam focus and background

This is an observability question where logs provide attribution and metrics provide alerting. Bedrock invocation logs contain call details that CloudWatch Logs Insights can query by application, model, or tool dimension. That supports questions such as “which business unit used how many tokens?” and allows query results to appear in custom dashboards.

Native CloudWatch token and invocation metrics are better for continuous time-series monitoring and alarms across the company’s models. The clues are real-time visibility, stakeholder-specific dashboards, alarms for all FMs, and low operations. B supplies detailed usage analysis; C supplies a direct, managed monitoring and alarm path. Together they cover both diagnosis and automated detection without building an ingestion pipeline.

#### Analogy

Think of a power-control room. Logs Insights is the engineer reading each machine’s detailed meter and tracing consumption to a department. CloudWatch metrics are the large wall gauges, and alarms are the sirens that sound when a gauge crosses its limit. You need both the investigation ledger and the automatic warning system.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock invocation logging | Detailed usage ledger | Records invocation and token details |
| CloudWatch Logs Insights | Ledger analyst | Queries patterns and attributes usage by application |
| CloudWatch Dashboard | Role-specific control screen | Presents custom views for stakeholders |
| CloudWatch metrics and alarms | Gauges and sirens | Monitors token/invocation series and raises threshold alarms |

#### Correct answer and reasoning

1. Enable invocation logging so detailed token information exists in a queryable record.
2. Use Logs Insights to analyze token patterns by application, identify high-use scenarios, and place useful log widgets on dashboards.
3. Combine native token and invocation metrics in CloudWatch dashboards for cross-model visibility without ETL.
4. Set CloudWatch alarms on those metrics to alert on token thresholds across applications.
5. B handles attribution and investigation, while C handles low-overhead continuous monitoring and alarms; the pair is complementary.

#### Option-by-option elimination

- **A:** QuickSight can visualize data, but refresh and governance add overhead and the option lacks the same direct CloudWatch alarm path.
- **B:** Correct. Logs Insights provides detailed, application-level token analysis and dashboard monitoring.
- **C:** Correct. Native metrics, dashboards, and alarms directly satisfy cross-model visibility and alerting.
- **D:** The described Bedrock-to-Grafana zero-ETL path is not the standard low-overhead combination for this requirement and provides no clear alarm design.
- **E:** EventBridge, Firehose, and OpenSearch Serverless create an additional data pipeline when CloudWatch can do the job directly.

#### Exam strategy

Separate “explain the detail” from “alarm continuously.” Invocation logs plus Logs Insights support attribution and pattern analysis; native metrics plus alarms support trends and thresholds. For a least-operations multiple-choice question, favor the two native CloudWatch paths over ETL and a separate analytics stack.

---

## Question 67

### 中文

#### 考点背景

题目把三种维度混在一起：按需实时调用、批处理的稳定吞吐，以及跨云端和本地/边缘的混合部署。Bedrock Provisioned Throughput 为指定模型预留稳定的推理容量，适合高流量或批处理；它与“实时按需”并不矛盾，因为公司可以为不同工作负载选择不同调用模式。

Hybrid Deployment 要求模型或推理工作负载能落在边缘/本地侧。SageMaker Endpoint 配合 SageMaker Neo 可以把模型优化为适合边缘部署的形式，Lambda 则可编排云端与边缘路径。正确组合 B、C 正好分别解决稳定吞吐和混合部署；不要把 JumpStart 的模型发现/部署辅助误认为完整的容量与混合运行方案。

#### 场景比喻

像工厂有两条生产线：大批订单送进预留的高速传送带（Provisioned Throughput）；需要数据留在厂区的工序把优化后的设备搬到现场边缘，Lambda 像调度员安排云端和现场机器。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Bedrock Provisioned Throughput | 预订的高速车道 | 为批处理和高流量提供稳定模型吞吐 |
| SageMaker AI Endpoint | 可部署的推理机器 | 承载需要独立部署的 FM |
| SageMaker Neo | 边缘设备优化器 | 优化模型以支持 Edge Deployment |
| AWS Lambda | 云边调度员 | 编排云端与本地/边缘的混合推理路径 |

#### 正确答案与推理

1. 对需要稳定高吞吐的 Batch Processing，配置 Bedrock Provisioned Throughput，避免完全依赖共享按需容量。
2. 对需要混合部署的 FM，把模型部署到 SageMaker Endpoint，并用 Neo 支持边缘运行。
3. 用 Lambda 选择或编排云端与边缘工作负载，使合规数据可留在适当位置。
4. A 的异步 Endpoint 不满足题干的按需低延迟部分；D、E 不能同时给出稳定容量与混合落点。
5. 因而 B、C 的组合覆盖所有工作负载类型和部署边界。

#### 逐项排除

- **A：** 异步 Endpoint 面向排队式处理，不适合题干要求的低延迟按需推理。
- **B：** 正确。Provisioned Throughput 对批处理的稳定、高吞吐访问提供明确能力。
- **C：** 正确。SageMaker/Neo 提供边缘落点，Lambda 可编排云端与边缘，满足 Hybrid Deployment。
- **D：** 题干给出的 Bedrock Auto Scaling 不是解决混合部署的完整机制，也不能替代为批处理预留稳定吞吐的选择。
- **E：** JumpStart 主要帮助发现、获取或部署模型，本身不保证 Provisioned Throughput 或云边混合运行。

#### 解题方法

把题干拆成“容量稳定性”和“部署位置”两条轴：稳定批量吞吐找 Provisioned Throughput；本地/边缘合规找 SageMaker Endpoint+Neo，并看是否有编排。多选题要选能覆盖不同轴的选项，而不是两个都只解决流量。

### English

#### Exam focus and background

The question combines three dimensions: on-demand real-time invocation, consistent throughput for batch processing, and hybrid execution across cloud and local or edge infrastructure. Bedrock Provisioned Throughput reserves model capacity for predictable, high-volume access, which fits the batch requirement. It can coexist with an on-demand path for a different workload; the exam is not asking one mode to serve every use case.

Hybrid deployment requires an execution location outside the ordinary managed inference path. SageMaker endpoints with SageMaker Neo can support an edge-deployment pattern by optimizing a model for the target environment, while Lambda can orchestrate cloud and edge flows. B and C therefore cover different requirements. JumpStart is a model discovery and deployment aid, not a guarantee of reserved capacity or hybrid execution.

#### Analogy

Imagine a factory with two lines. Large batch orders use a reserved high-speed conveyor, representing Provisioned Throughput. Work that must stay on the factory floor uses an optimized local machine at the edge, while Lambda acts as the dispatcher choosing between the cloud line and the local line.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock Provisioned Throughput | Reserved high-speed lane | Provides consistent capacity for batch and high-volume workloads |
| SageMaker AI endpoint | Deployable inference machine | Hosts a model that needs an independently managed placement |
| SageMaker Neo | Edge equipment optimizer | Optimizes the model for edge deployment |
| AWS Lambda | Cloud-edge dispatcher | Orchestrates workloads across cloud and local/edge paths |

#### Correct answer and reasoning

1. Use Bedrock Provisioned Throughput for batch processing that needs stable, high-throughput model access.
2. Deploy the hybrid-capable model on a SageMaker endpoint and use Neo for the edge placement.
3. Use Lambda to orchestrate cloud and edge execution so residency-sensitive work can remain in the appropriate location.
4. An asynchronous endpoint does not meet the real-time on-demand clue, while the other choices do not provide both stable capacity and hybrid placement.
5. B and C together cover the workload and deployment axes in the stem.

#### Option-by-option elimination

- **A:** An asynchronous endpoint is designed for queued processing, not the required low-latency, on-demand inference path.
- **B:** Correct. Provisioned Throughput directly addresses consistent high-throughput batch access.
- **C:** Correct. SageMaker and Neo supply an edge deployment pattern, and Lambda can orchestrate cloud-edge execution.
- **D:** The stated Bedrock auto-scaling choice does not supply a complete hybrid-deployment mechanism or the explicit reserved capacity needed for batch.
- **E:** JumpStart helps discover or deploy models; it does not by itself guarantee reserved throughput or hybrid operation.

#### Exam strategy

Split the stem into “capacity stability” and “placement.” Stable batch throughput points to Provisioned Throughput. Local or edge compliance points to a SageMaker endpoint with Neo plus orchestration. In a multi-select question, choose options that cover separate axes rather than two variants of traffic scaling.

---

## Question 68

### 中文

#### 考点背景

这题考平台工程的“黄金模板”思路。AWS Well-Architected Generative AI Lens 提供安全、可观测性和 RAG 的设计检查点；把这些模式编码进 CloudFormation Template，就能把架构意图变成可重复部署的资源定义。集中 Repository 再为模板建立版本历史，所有业务部门复用同一基线，同时保留参数化差异。

CloudFormation Guard 放在 CI/CD 中进行策略检查，发布前就能阻止不合规模板进入各 Region/业务单元。注意题干的关键词是 reusable、versioned、governed、consistent；文档只能告诉人怎么做，部署后检查又太晚，而为每个部门分裂 Service Catalog Portfolio 会增加治理面。

#### 场景比喻

像连锁餐厅使用中央厨房的标准菜谱：Repository 是菜谱版本库，CloudFormation 是可重复的备料单，Guard 是出餐前的食品检查；各门店只能在标准框架内替换允许的食材参数。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Well-Architected Generative AI Lens | 总厨标准 | 定义安全、可观测性和 RAG 的设计基线 |
| AWS CloudFormation | 标准施工图 | 把组件编码成可重复、可复用的部署模板 |
| 集中式 Repository | 菜谱版本库 | 审核、版本化并分发统一模板 |
| CloudFormation Guard | 出餐前检查员 | 在 CI/CD 中阻止不符合规则的部署 |

#### 正确答案与推理

1. 从 Generative AI Lens 提取跨应用共通的安全、观测和 RAG 模式。
2. 用 CloudFormation 模板把这些模式声明化，支持参数化复用而不复制手工步骤。
3. 把模板放入集中仓库，形成版本、审查和回滚依据。
4. 在 CI/CD 阶段运行 CloudFormation Guard，部署前强制一致的 policy compliance。
5. 这条链路同时提供复用、版本和治理，且避免每个部门自行解释标准。

#### 逐项排除

- **A：** 模板和 Guard 方向正确，但把 Guard 放在部署后才验证，且额外加入不必要的 API Gateway，不能体现最完整的统一模式。
- **B：** 正确。集中版本库、标准模板和 CI/CD Guard 完整实现可重复治理。
- **C：** Service Catalog 可提供产品目录，但按部门分别维护 Portfolio 和要求手工经 Console 部署，治理分散、自动化较弱。
- **D：** 设计文档不能强制实际资源状态；Macie 用于数据发现和分类，不是部署策略执行器。

#### 解题方法

看到“标准化、可复用、版本化、统一治理”就寻找 IaC + 版本库 + pipeline policy gate。考试中 Guard 应在部署前进入 CI/CD；文档、数据分类服务和分散的产品目录通常不是最强一致性答案。

### English

#### Exam focus and background

This question tests the platform-engineering pattern of a governed golden template. The Well-Architected Generative AI Lens supplies design guidance for security, observability, and RAG. Encoding those patterns in CloudFormation turns architectural intent into repeatable resource definitions. A centralized repository provides version history, review, and a common baseline that can still be parameterized for each business unit.

CloudFormation Guard belongs in the CI/CD path so noncompliant templates are rejected before deployment. The keywords are reusable, versioned, governed, and consistent. A shared document depends on human interpretation, and post-deployment checking is too late to prevent drift. Service Catalog can package products, but separate portfolios and console-led administration add fragmentation when the question asks for one consistently governed deployment pattern.

#### Analogy

Imagine a restaurant chain supplied by one central kitchen. The repository is the versioned recipe book, CloudFormation is the repeatable preparation sheet, and CloudFormation Guard is the food-safety inspection before a dish leaves the kitchen. Stores can change approved parameters, but not silently rewrite the recipe.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Well-Architected Generative AI Lens | Head-chef standard | Defines the security, observability, and RAG baseline |
| AWS CloudFormation | Standard construction plan | Encodes reusable, repeatable infrastructure patterns |
| Central repository | Versioned recipe book | Reviews, versions, and distributes the common templates |
| CloudFormation Guard | Pre-service inspector | Enforces policy rules in CI/CD before deployment |

#### Correct answer and reasoning

1. Derive common security, observability, and RAG patterns from the Generative AI Lens.
2. Encode those patterns in parameterized CloudFormation templates so teams reuse the same implementation.
3. Store the templates in a centralized version-controlled repository for review and rollback.
4. Run CloudFormation Guard in CI/CD to reject policy violations before resources are deployed.
5. This directly supplies reuse, versioning, repeatability, and centralized governance across business units.

#### Option-by-option elimination

- **A:** The template approach is useful, but validating only after deployment is too late, and the extra API Gateway requirement is not needed for the stated standard.
- **B:** Correct. It combines centralized versioned templates with a pre-deployment CI/CD policy gate.
- **C:** Service Catalog can package products, but per-unit portfolios and console-driven deployment fragment administration and weaken the single governed pattern.
- **D:** Documentation cannot enforce deployed state, and Macie discovers and classifies data rather than enforcing deployment policy.

#### Exam strategy

For “standardized, reusable, versioned, consistently governed,” look for IaC plus a version-control repository plus a pre-deployment policy gate. CloudFormation Guard in CI/CD is stronger than documentation or after-the-fact checking; data-classification services are not deployment-policy engines.

---

## Question 69

### 中文

#### 考点背景

这题不是负载测试，而是多语言生成质量的回归测试。语义等价的测试对话必须保留各语言真实表达，才能暴露“问题意思相同但回答不同”的模型偏差；把所有语言先翻成一种语言会把语言特有的行为藏起来。Similarity 用于比较答案的一致程度，Hallucination 用于捕捉无依据生成。

题干又给出 45 分钟、15,000 条并行、全自动和失败阻断，这些是发布门的工程约束。Bedrock Model Evaluation Job 提供托管评估执行，结果阈值接入 CI/CD 后，质量不达标即可停止发布。重点是“内容质量门”，不是只测延迟、路由或基础设施吞吐。

#### 场景比喻

像同一位客服被 15,000 个来自不同国家的考官同时问等义问题：评分员比较答案是否同样可靠，并检查是否编造；如果平均分跌破红线，发布列车就在站台前停下。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Bedrock Model Evaluation Job | 批量考场 | 并行运行多语言测试对话 |
| Similarity Threshold | 一致性尺 | 判断等义问题的回答是否保持相近 |
| Hallucination Threshold | 事实红线 | 检查回答是否出现无依据内容 |
| CI/CD Pipeline | 发布闸机 | 将评估结果转为自动阻断条件 |

#### 正确答案与推理

1. 构造语义完全相同、但保留原始语言的标准测试集，直接测跨语言一致性。
2. 用 Model Evaluation Job 并行处理大规模对话，针对 45 分钟和 15,000 条约束配置执行规模。
3. 用 Similarity 阈值发现跨语言答案偏离，用 Hallucination 阈值发现生成不可信。
4. 把评估 Job 接入 CI/CD，设置质量门；任一阈值失败就拒绝模型更新。
5. 该方案同时覆盖输入真实性、并行规模、自动化和部署阻断。

#### 逐项排除

- **A：** CloudWatch 只能说明延迟、并发和吞吐，不能评价等义问题的回答是否一致或是否幻觉。
- **B：** 多 Region 路由改善性能，但每周人工审计既不自动，也不能在发布前阻断质量问题。
- **C：** 统一翻译会抹掉语言差异；规则检查也不能替代跨语言生成质量和幻觉评估。
- **D：** 正确。标准化多语言对话、并行 Evaluation Job、两类阈值和 CI/CD gate 完整对应要求。

#### 解题方法

看到“同义多语言表现不一致”，保留原语言做等义对照；看到“自动、并行、阻止发布”，寻找 Model Evaluation Job + Similarity/Hallucination thresholds + CI/CD。不要把性能测试指标当作答案质量指标。

### English

#### Exam focus and background

This is a multilingual generation-quality regression test, not a load test. Semantically equivalent conversations must preserve their real languages so the evaluation can expose language-specific behavior. Translating every input into one common language can hide exactly the inconsistency the company wants to detect. A similarity measure compares response consistency, while a hallucination threshold catches unsupported generation.

The 45-minute, 15,000-conversation, fully automated, and release-blocking clues describe a quality gate. Bedrock model evaluation jobs provide a managed execution path for parallel evaluation, and their results can feed CI/CD criteria. The objective is to stop a model update whose content quality fails; latency, routing, and infrastructure throughput alone cannot prove that equivalent customer questions receive equivalent, grounded answers.

#### Analogy

Imagine 15,000 examiners from different countries asking equivalent questions at once. They compare whether the answers are equally reliable and mark invented claims. If the score crosses a red line, the release train stops before leaving the station.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock model evaluation job | Large examination hall | Runs multilingual conversations in parallel |
| Similarity threshold | Consistency ruler | Checks whether equivalent prompts receive comparable answers |
| Hallucination threshold | Factual red line | Detects unsupported generated content |
| CI/CD pipeline | Release gate | Blocks deployment when evaluation quality fails |

#### Correct answer and reasoning

1. Build a standardized dataset of semantically equivalent conversations while retaining each original language.
2. Run the dataset through Bedrock model evaluation jobs with enough parallelism to meet the time and volume constraints.
3. Apply similarity thresholds to detect cross-language divergence and hallucination thresholds to detect unsupported answers.
4. Integrate the evaluation result with CI/CD and make threshold failure a deployment-blocking condition.
5. This covers authentic multilingual inputs, scale, automation, quality measurement, and release control.

#### Option-by-option elimination

- **A:** CloudWatch latency, concurrency, and throughput show infrastructure behavior, not semantic consistency or hallucination quality.
- **B:** Multi-Region routing may improve performance, but a weekly manual audit is neither automatic nor a pre-release quality gate.
- **C:** Normalizing all languages can erase language-specific failures, and rules cannot replace multilingual generation-quality evaluation.
- **D:** Correct. It combines equivalent multilingual tests, parallel evaluation, similarity and hallucination thresholds, and a CI/CD gate.

#### Exam strategy

For cross-language inconsistency, preserve the languages and compare equivalent meaning. For “automated, parallel, block release,” look for model evaluation jobs plus quality thresholds wired into CI/CD. Do not substitute infrastructure performance metrics for response-quality metrics.

---

## Question 70

### 中文

#### 考点背景

题目要求的不只是知道 Token 多了，而是把异常归因到某一个 Tool Integration，并让阈值随流量变化。Bedrock Model Invocation Logging 把调用信息送到 CloudWatch Logs；Metric Filter 可以从包含 Tool 标识和 Token 字段的日志中提取可监控指标。这样每个 Tool 都能形成独立时间序列，而不是只有应用总量。

固定阈值适合稳定基线，面对时区、活动或调用模式变化容易误报或漏报。CloudWatch Anomaly Detection 用历史数据学习基线，让告警围绕“异常偏离”而不是死数字判断。考试关键词是“日志字段提取 + Tool-specific + automatically adjust”，对应 C 的组合。

#### 场景比喻

像厨房有多个自动化厨具：日志是每台机器的工作记录，Metric Filter 把“哪台机器耗了多少原料”记成独立仪表；Anomaly Detection 会随午餐高峰调整正常线，突然暴涨才鸣笛。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Bedrock Model Invocation Logging | 每次操作的黑匣子 | 捕获 InputTokenCount、OutputTokenCount 和调用上下文 |
| CloudWatch Logs | 集中工作日志 | 承载可查询的模型调用记录 |
| CloudWatch Metric Filter | 计数器制造机 | 从日志提取每个 Tool 的调用/Token 模式 |
| CloudWatch Anomaly Detection Alarm | 自适应警铃 | 根据历史基线检测异常并自动调整判断范围 |

#### 正确答案与推理

1. 启用 Model Invocation Logging，把输入和输出 Token 作为日志事实保存。
2. 按 Tool 标识建立 Metric Filter，将不同 Integration 的模式拆成可告警指标。
3. 为每个 Tool 的指标使用 Anomaly Detection，基于历史行为形成动态 baseline。
4. 当稳定流量下某 Tool 的 Token 消耗异常偏离时，Alarm 能指出对应时间序列。
5. 因而 C 同时提供数据来源、细粒度归因和自动阈值调整。

#### 逐项排除

- **A：** 有日志和 Dashboard，却使用固定阈值；流量基线变化时不能自动适应，也没有明确的异常检测。
- **B：** S3、Glue 和定时 Athena 适合离线趋势分析，无法及时检测并定位实时 Token 激增。
- **C：** 正确。Metric Filter 细分 Tool，Anomaly Detection 根据历史基线动态识别异常。
- **D：** Lambda 可做实时处理，但仍要求人工更新 Alarm 阈值，违背自动调整要求，且组件更多。

#### 解题方法

先找“原始证据”——Invocation Logging；再找“按 Tool 拆分”——Metric Filter；最后找“动态阈值”——Anomaly Detection。固定 Alarm、定时 Athena 或人工调阈值都错在无法适应变化或延迟太高。

### English

#### Exam focus and background

The requirement is not merely to notice that token use increased. It is to attribute the increase to a particular tool integration and adjust the detection baseline as traffic changes. Bedrock model invocation logging places call details in CloudWatch Logs. Metric filters can extract token fields and tool identifiers into separate, monitorable time series, rather than leaving only an application-wide total.

Static thresholds work when behavior is stable, but they create false alarms during legitimate peaks and miss anomalies after the normal level changes. CloudWatch anomaly detection learns a historical baseline and evaluates deviation from that baseline. The exam keywords are “invocation logs,” “tool-specific patterns,” and “automatically adjust,” which point directly to the combination in C.

#### Analogy

Imagine a kitchen with several automated appliances. The logs are each appliance’s work diary; metric filters turn “which appliance consumed how much” into separate gauges. Anomaly detection moves the normal line during a lunch rush and sounds the alarm only when one machine behaves unusually.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock model invocation logging | Operation black box | Captures token counts and invocation context |
| CloudWatch Logs | Central work diary | Stores the queryable invocation records |
| CloudWatch metric filter | Counter builder | Extracts tool-specific invocation and token patterns |
| CloudWatch anomaly detection alarm | Adaptive siren | Detects deviation from a learned baseline |

#### Correct answer and reasoning

1. Enable model invocation logging so input and output token counts are recorded as evidence.
2. Use tool-aware metric filters to split the patterns into separate metrics for each integration.
3. Apply anomaly detection to each tool metric so the baseline follows historical traffic behavior.
4. When one tool’s consumption deviates unexpectedly, its time series and alarm identify the responsible integration.
5. C therefore supplies the evidence source, attribution, anomaly detection, and automatic baseline adjustment.

#### Option-by-option elimination

- **A:** It has logs and dashboards but relies on fixed thresholds, so it cannot adapt automatically as traffic changes.
- **B:** S3, Glue, and scheduled Athena queries support offline trends, not timely detection and attribution of a live surge.
- **C:** Correct. Metric filters isolate tools and anomaly detection evaluates dynamic deviation from history.
- **D:** Lambda can process logs in real time, but manual alarm-threshold updates violate the automatic-adjustment requirement and add components.

#### Exam strategy

Find the three-stage pattern: invocation logging for evidence, metric filters for tool-level dimensions, and anomaly detection for changing baselines. Fixed alarms, scheduled analytics, and manual threshold edits fail either the attribution or the adaptive-detection clue.

---
## Question 71

### 中文

#### 考点背景

这题考查**地理范围内的跨区域推理、数据驻留与峰值韧性**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。按地理范围配置的 Bedrock Cross-Region Inference Profile 会在批准的欧洲或北美 Region 内分配推理请求，从而提高超出单一区域配额时的韧性，同时满足欧洲数据驻留要求；分别使用区域 API Gateway 也能把用户送到相应地理范围。A 需要自建路由和告警，却不能自动吸收流量峰值；C 的 Lambda 重试和自定义 Intelligent Routing 运维复杂，且次级 Region 可能违反驻留边界；D 依赖多区域 Provisioned Throughput 和应用级故障转移，成本与实现复杂度都更高。

#### 场景比喻

把欧洲和北美想成两个有海关边界的铁路网：API Gateway 是分站入口，地理型 Inference Profile 是网内调度中心。欧洲旅客只能在欧洲铁路网换车，但某一站拥挤时，调度中心可把车送到同一欧洲网络的其他站。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Cross-Region Inference Profile | 区域容量调度中心 | 在批准的地理边界内分散推理请求与配额压力 |
| Provisioned Throughput | 预留专用车道 | 提供可预测的模型容量与延迟，但带来固定容量成本 |
| Amazon API Gateway | 统一服务入口 | 暴露受控 API 并管理请求边界、认证和超时 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【地理范围内的跨区域推理、数据驻留与峰值韧性】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。按地理范围配置的 Bedrock Cross-Region Inference Profile 会在批准的欧洲或北美 Region 内分配推理请求，从而提高超出单一区域配额时的韧性，同时满足欧洲数据驻留要求；分别使用区域 API Gateway 也能把用户送到相应地理范围。A 需要自建路由和告警，却不能自动吸收流量峰值；C 的 Lambda 重试和自定义 Intelligent Routing 运维复杂，且次级 Region 可能违反驻留边界；D 依赖多区域 Provisioned Throughput 和应用级故障转移，成本与实现复杂度都更高。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项需要自建路由和告警，却不能自动吸收流量峰值。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【地理范围内的跨区域推理、数据驻留与峰值韧性】的关键机制。
- **C：** 该项的 Lambda 重试和自定义 Intelligent Routing 运维复杂，且次级 Region 可能违反驻留边界。
- **D：** 该项依赖多区域 Provisioned Throughput 和应用级故障转移，成本与实现复杂度都更高。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【地理范围内的跨区域推理、数据驻留与峰值韧性】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **geography-scoped cross-Region inference, data residency, and peak-load resilience**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. A geographic Bedrock Cross-Region Inference Profile distributes inference requests within the approved European or North American Regions. This improves resilience when a single Region reaches its quota while preserving European data-residency boundaries; separate regional API Gateway endpoints direct users to the appropriate geography. A requires custom routing and alarms but does not automatically absorb traffic spikes. C adds complex Lambda retry and intelligent-routing logic, and a secondary Region could violate residency boundaries. D requires multi-Region Provisioned Throughput and application-level failover, increasing cost and implementation complexity.

#### Analogy

Think of Europe and North America as two rail networks separated by customs. API Gateway is the local station entrance, and the geographic inference profile is the dispatcher that can move a train to another station inside the same approved network when one station is full.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Cross-Region Inference Profile | Regional capacity dispatcher | Spreads inference demand and quota pressure within an approved geography |
| Provisioned Throughput | Reserved express lane | Provides predictable model capacity and latency with committed capacity cost |
| Amazon API Gateway | Managed service entrance | Exposes controlled APIs and manages request, auth, and timeout boundaries |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver geography-scoped cross-Region inference, data residency, and peak-load resilience, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. A geographic Bedrock Cross-Region Inference Profile distributes inference requests within the approved European or North American Regions. This improves resilience when a single Region reaches its quota while preserving European data-residency boundaries; separate regional API Gateway endpoints direct users to the appropriate geography. A requires custom routing and alarms but does not automatically absorb traffic spikes. C adds complex Lambda retry and intelligent-routing logic, and a secondary Region could violate residency boundaries. D requires multi-Region Provisioned Throughput and application-level failover, increasing cost and implementation complexity.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option requires custom routing and alarms but does not automatically absorb traffic spikes.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for geography-scoped cross-Region inference, data residency, and peak-load resilience.
- **C:** This option adds complex Lambda retry and intelligent-routing logic, and a secondary Region could violate residency boundaries.
- **D:** This option requires multi-Region Provisioned Throughput and application-level failover, increasing cost and implementation complexity.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is geography-scoped cross-Region inference, data residency, and peak-load resilience; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 72

### 中文

#### 考点背景

这题考查**多地域 RAG 的本地数据边界与托管推理容量**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。在每个运营 Region 部署模型，并把该区域的 S3 数据和 Bedrock Knowledge Base 保持在本地，可以让专有数据就近检索；地理范围的 Cross-Region Inference Profile 还能在允许的地理边界内提供更高吞吐和较低延迟。A 额外引入 Kendra、Lambda 和重复集成；C 把推理集中到中央 Region，不能保证数据不离开客户区域；D 需要在 Local Zone 自建 RDS、EC2 和 LLM，运维负担高且没有使用 Bedrock 的托管能力。

#### 场景比喻

像跨国连锁图书馆：每个国家保留自己的机密书库和目录，读者先在本地图书馆检索；地理型推理配置只允许馆员在获准的大区内互相支援，不能把原书运到另一个大洲。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Cross-Region Inference Profile | 区域容量调度中心 | 在批准的地理边界内分散推理请求与配额压力 |
| Amazon Bedrock Knowledge Bases | 可检索证据档案柜 | 托管 RAG 数据源、检索与生成上下文连接 |
| Amazon S3 | 耐久对象仓库 | 保存区域数据源、日志和审计工件 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【多地域 RAG 的本地数据边界与托管推理容量】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。在每个运营 Region 部署模型，并把该区域的 S3 数据和 Bedrock Knowledge Base 保持在本地，可以让专有数据就近检索；地理范围的 Cross-Region Inference Profile 还能在允许的地理边界内提供更高吞吐和较低延迟。A 额外引入 Kendra、Lambda 和重复集成；C 把推理集中到中央 Region，不能保证数据不离开客户区域；D 需要在 Local Zone 自建 RDS、EC2 和 LLM，运维负担高且没有使用 Bedrock 的托管能力。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项额外引入 Kendra、Lambda 和重复集成。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【多地域 RAG 的本地数据边界与托管推理容量】的关键机制。
- **C：** 该项把推理集中到中央 Region，不能保证数据不离开客户区域。
- **D：** 该项需要在 Local Zone 自建 RDS、EC2 和 LLM，运维负担高且没有使用 Bedrock 的托管能力。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【多地域 RAG 的本地数据边界与托管推理容量】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **regional RAG data boundaries and managed inference capacity**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. Deploying the model in each operating Region and keeping that Region's S3 data and Bedrock Knowledge Base local allows proprietary data to be retrieved close to its source. A geographic cross-Region inference profile can also provide higher throughput and lower latency within the permitted geographic boundary. A adds Kendra, Lambda, and duplicate integrations. C centralizes inference in one Region and cannot ensure that data stays in the customer's area. D requires self-managing RDS, EC2, and an LLM in Local Zones, creating high operational overhead instead of using managed Bedrock capabilities.

#### Analogy

Picture an international library chain. Each country keeps its confidential shelves and catalog locally; a geographic inference profile lets librarians help within the approved area without shipping the original books to another continent.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Cross-Region Inference Profile | Regional capacity dispatcher | Spreads inference demand and quota pressure within an approved geography |
| Amazon Bedrock Knowledge Bases | Searchable evidence archive | Connects managed RAG data, retrieval, and generation context |
| Amazon S3 | Durable object warehouse | Stores regional source data, logs, and audit artifacts |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver regional RAG data boundaries and managed inference capacity, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. Deploying the model in each operating Region and keeping that Region's S3 data and Bedrock Knowledge Base local allows proprietary data to be retrieved close to its source. A geographic cross-Region inference profile can also provide higher throughput and lower latency within the permitted geographic boundary. A adds Kendra, Lambda, and duplicate integrations. C centralizes inference in one Region and cannot ensure that data stays in the customer's area. D requires self-managing RDS, EC2, and an LLM in Local Zones, creating high operational overhead instead of using managed Bedrock capabilities.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option adds Kendra, Lambda, and duplicate integrations.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for regional RAG data boundaries and managed inference capacity.
- **C:** This option centralizes inference in one Region and cannot ensure that data stays in the customer's area.
- **D:** This option requires self-managing RDS, EC2, and an LLM in Local Zones, creating high operational overhead instead of using managed Bedrock capabilities.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is regional RAG data boundaries and managed inference capacity; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 73

### 中文

#### 考点背景

这题考查**跨语言 SDK 的统一会话接口、认证和多轮上下文**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。Bedrock Converse API 在 Python 和 JavaScript SDK 中提供一致的消息接口，IAM Role 可以统一认证；把历史消息放入请求的 messages Array，就能在多轮请求中显式传递上下文。A 使用不同认证和自定义 I/O，难以保持一致；B 额外引入 ElastiCache 和语言包装器，且不是必要的上下文机制；C 的集中 API 和进程内存会增加瓶颈，并且 Lambda 或容器重启后会丢失历史。

#### 场景比喻

把 Python Lambda 和 JavaScript 容器看成两位说不同语言的客服。Converse API 是统一工单格式，IAM Role 是同一员工证，messages 数组则是随工单附上的完整聊天记录，换客服也不会失忆。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| IAM Role | 可审计员工证 | 为工作负载和联合用户提供一致的临时 AWS 身份 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【跨语言 SDK 的统一会话接口、认证和多轮上下文】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。Bedrock Converse API 在 Python 和 JavaScript SDK 中提供一致的消息接口，IAM Role 可以统一认证；把历史消息放入请求的 messages Array，就能在多轮请求中显式传递上下文。A 使用不同认证和自定义 I/O，难以保持一致；B 额外引入 ElastiCache 和语言包装器，且不是必要的上下文机制；C 的集中 API 和进程内存会增加瓶颈，并且 Lambda 或容器重启后会丢失历史。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项使用不同认证和自定义 I/O，难以保持一致。
- **B：** 该项额外引入 ElastiCache 和语言包装器，且不是必要的上下文机制。
- **C：** 该项的集中 API 和进程内存会增加瓶颈，并且 Lambda 或容器重启后会丢失历史。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【跨语言 SDK 的统一会话接口、认证和多轮上下文】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【跨语言 SDK 的统一会话接口、认证和多轮上下文】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **a consistent cross-language SDK interface, authentication, and multi-turn context**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. The Bedrock Converse API exposes a consistent message interface through the Python and JavaScript SDKs, and IAM roles provide common authentication. Passing prior turns in the request's messages array explicitly preserves context across multi-turn calls. A uses different authentication methods and custom I/O, making consistency harder. B adds ElastiCache and language-specific wrappers even though the request can carry the context directly. C introduces a centralized bottleneck and stores history in process memory, which can be lost when a Lambda function or container restarts.

#### Analogy

Treat the Python Lambda and JavaScript container as two agents who speak different programming languages. Converse is the shared ticket format, IAM roles are their employee badges, and the messages array is the conversation transcript attached to every handoff.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| IAM Role | Auditable employee badge | Provides consistent temporary AWS identity to workloads and federated users |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver a consistent cross-language SDK interface, authentication, and multi-turn context, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. The Bedrock Converse API exposes a consistent message interface through the Python and JavaScript SDKs, and IAM roles provide common authentication. Passing prior turns in the request's messages array explicitly preserves context across multi-turn calls. A uses different authentication methods and custom I/O, making consistency harder. B adds ElastiCache and language-specific wrappers even though the request can carry the context directly. C introduces a centralized bottleneck and stores history in process memory, which can be lost when a Lambda function or container restarts.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option uses different authentication methods and custom I/O, making consistency harder.
- **B:** This option adds ElastiCache and language-specific wrappers even though the request can carry the context directly.
- **C:** This option introduces a centralized bottleneck and stores history in process memory, which can be lost when a Lambda function or container restarts.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for a consistent cross-language SDK interface, authentication, and multi-turn context.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is a consistent cross-language SDK interface, authentication, and multi-turn context; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 74

### 中文

#### 考点背景

这题考查**按需推理的限流观测、跨区域容量分摊与成本模型**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 C。Bedrock 的 Invocation Logging 和 Invocations、Token Count、InvocationThrottles 等指标可以观察调用量、消耗和限流；Cross-Region Inference Endpoint 能在不同地理范围的可用容量之间分散峰值，而且按需计费，不需要低流量时段也持续支付固定小时容量。A 的 Provisioned Throughput 是固定容量和固定成本；B 需要自定义故障转移，通常要等错误超过阈值；D 把流量分到同一模型的不同版本不能增加区域配额，也没有直接监控限流指标。

#### 场景比喻

全球学生晚间同时涌入像电影院散场：调用指标是门口计数器，InvocationThrottles 是“满员”灯，Cross-Region Inference 则把人流引到仍有座位的同区域影厅，而不是全天包下一座空电影院。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Provisioned Throughput | 预留专用车道 | 提供可预测的模型容量与延迟，但带来固定容量成本 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【按需推理的限流观测、跨区域容量分摊与成本模型】。
2. **再核对正确组合：** 正确答案是 **C**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 C。Bedrock 的 Invocation Logging 和 Invocations、Token Count、InvocationThrottles 等指标可以观察调用量、消耗和限流；Cross-Region Inference Endpoint 能在不同地理范围的可用容量之间分散峰值，而且按需计费，不需要低流量时段也持续支付固定小时容量。A 的 Provisioned Throughput 是固定容量和固定成本；B 需要自定义故障转移，通常要等错误超过阈值；D 把流量分到同一模型的不同版本不能增加区域配额，也没有直接监控限流指标。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 Provisioned Throughput 是固定容量和固定成本。
- **B：** 该项需要自定义故障转移，通常要等错误超过阈值。
- **C：** 正确。该项是答案 **C** 的组成部分，直接落实【按需推理的限流观测、跨区域容量分摊与成本模型】的关键机制。
- **D：** 该项把流量分到同一模型的不同版本不能增加区域配额，也没有直接监控限流指标。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【按需推理的限流观测、跨区域容量分摊与成本模型】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **on-demand throttling visibility, cross-Region capacity sharing, and cost behavior**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is C. Bedrock invocation logging and metrics such as Invocations, token counts, and InvocationThrottles provide visibility into usage and throttling. Cross-Region inference endpoints distribute peaks across available capacity within the relevant geography and use on-demand pricing, so there is no fixed hourly capacity cost during low traffic. A uses Provisioned Throughput with fixed capacity and cost. B requires custom failover that generally waits for errors to cross a threshold. D's different versions of the same model do not add Regional quota and it omits the direct throttling metric.

#### Analogy

Global evening traffic is like several cinemas emptying at once. Metrics count the crowd, InvocationThrottles is the full-house light, and cross-Region inference redirects demand to an approved theater with spare seats instead of renting an empty theater all day.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Provisioned Throughput | Reserved express lane | Provides predictable model capacity and latency with committed capacity cost |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver on-demand throttling visibility, cross-Region capacity sharing, and cost behavior, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **C**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is C. Bedrock invocation logging and metrics such as Invocations, token counts, and InvocationThrottles provide visibility into usage and throttling. Cross-Region inference endpoints distribute peaks across available capacity within the relevant geography and use on-demand pricing, so there is no fixed hourly capacity cost during low traffic. A uses Provisioned Throughput with fixed capacity and cost. B requires custom failover that generally waits for errors to cross a threshold. D's different versions of the same model do not add Regional quota and it omits the direct throttling metric.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option uses Provisioned Throughput with fixed capacity and cost.
- **B:** This option requires custom failover that generally waits for errors to cross a threshold.
- **C:** Correct. This choice is part of answer **C** and directly implements the decisive mechanism for on-demand throttling visibility, cross-Region capacity sharing, and cost behavior.
- **D:** This option proposes [Enable invocation logging in Amazon Bedrock. Monitor InvocationLatency, InvocationClientErrors, and InvocationServerErrors metrics. Distribute traffic…], but it does not close every constraint in the stem. Compared with **C**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is on-demand throttling visibility, cross-Region capacity sharing, and cost behavior; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 75

### 中文

#### 考点背景

这题考查**无状态 MCP Server 的托管 HTTPS 传输与强身份控制**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 C。Lambda Function URL 可以提供 HTTPS 入口，函数内部实现 Streamable HTTP MCP Transport，并通过 SigV4 和 IAM 进行强认证；向授权用户或 Role 授予 lambda:InvokeFunctionUrl 后，内部应用和授权合作伙伴都能按权限调用。A 的 Lambda Invoke API 不是 MCP 的网络传输入口；B 用 API Key 且要求改用普通 HTTP，会丢失 MCP 协议和更强的 IAM 授权；D 可以实现类似访问，但额外引入 API Gateway、Cognito 和 OAuth 验证，运维开销更高。

#### 场景比喻

把 MCP Tool 想成受控仓库窗口：Lambda Function URL 是窗口，Streamable HTTP 是标准传送带，SigV4 是每个包裹的防伪签名，IAM 权限清单决定内部员工和合作伙伴谁能取货。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Lambda Function URL | 函数专属 HTTPS 窗口 | 用较少组件为 Lambda 提供直接网络入口 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【无状态 MCP Server 的托管 HTTPS 传输与强身份控制】。
2. **再核对正确组合：** 正确答案是 **C**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 C。Lambda Function URL 可以提供 HTTPS 入口，函数内部实现 Streamable HTTP MCP Transport，并通过 SigV4 和 IAM 进行强认证；向授权用户或 Role 授予 lambda:InvokeFunctionUrl 后，内部应用和授权合作伙伴都能按权限调用。A 的 Lambda Invoke API 不是 MCP 的网络传输入口；B 用 API Key 且要求改用普通 HTTP，会丢失 MCP 协议和更强的 IAM 授权；D 可以实现类似访问，但额外引入 API Gateway、Cognito 和 OAuth 验证，运维开销更高。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 Lambda Invoke API 不是 MCP 的网络传输入口。
- **B：** 该项用 API Key 且要求改用普通 HTTP，会丢失 MCP 协议和更强的 IAM 授权。
- **C：** 正确。该项是答案 **C** 的组成部分，直接落实【无状态 MCP Server 的托管 HTTPS 传输与强身份控制】的关键机制。
- **D：** 该项可以实现类似访问，但额外引入 API Gateway、Cognito 和 OAuth 验证，运维开销更高。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【无状态 MCP Server 的托管 HTTPS 传输与强身份控制】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **managed HTTPS transport and strong identity controls for stateless MCP servers**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is C. A Lambda function URL provides an HTTPS entry point; the function can implement the Streamable HTTP MCP transport and use SigV4 with IAM for strong authentication. Granting lambda:InvokeFunctionUrl to authorized users or roles allows both internal applications and approved partners to invoke it according to their permissions. A's Lambda Invoke API is not an MCP network transport. B uses API keys and requires clients to switch to ordinary HTTP, losing the MCP protocol and stronger IAM authorization. D can provide similar access but adds API Gateway, Cognito, and OAuth-token validation, increasing operational overhead.

#### Analogy

Imagine each MCP tool as a controlled warehouse window. The Lambda function URL is the window, Streamable HTTP is the standard conveyor, SigV4 signs each parcel, and IAM decides which employees and partners may collect it.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Lambda Function URL | Function HTTPS window | Provides a direct network endpoint for Lambda with few components |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver managed HTTPS transport and strong identity controls for stateless MCP servers, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **C**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is C. A Lambda function URL provides an HTTPS entry point; the function can implement the Streamable HTTP MCP transport and use SigV4 with IAM for strong authentication. Granting lambda:InvokeFunctionUrl to authorized users or roles allows both internal applications and approved partners to invoke it according to their permissions. A's Lambda Invoke API is not an MCP network transport. B uses API keys and requires clients to switch to ordinary HTTP, losing the MCP protocol and stronger IAM authorization. D can provide similar access but adds API Gateway, Cognito, and OAuth-token validation, increasing operational overhead.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option Lambda function URL provides an HTTPS entry point; the function can implement the Streamable HTTP MCP transport and use SigV4 with IAM for strong authentication.
- **B:** This option uses API keys and requires clients to switch to ordinary HTTP, losing the MCP protocol and stronger IAM authorization.
- **C:** Correct. This choice is part of answer **C** and directly implements the decisive mechanism for managed HTTPS transport and strong identity controls for stateless MCP servers.
- **D:** This option can provide similar access but adds API Gateway, Cognito, and OAuth-token validation, increasing operational overhead.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is managed HTTPS transport and strong identity controls for stateless MCP servers; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 76

### 中文

#### 考点背景

这题考查**瞬态错误退避、避免重试风暴和跨服务分布式追踪**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。AWS SDK Standard Retry Mode 配合带 Jitter 的 Exponential Backoff 能避免客户端同步重试，在限流或瞬态错误时逐步退让，从而降低级联故障风险；AWS X-Ray 可跨服务建立分布式 Trace，Annotation 还能按模型或组件筛选性能问题。A 的固定 1 秒重试容易形成重试风暴且没有分布式追踪；C 的缓存不能普遍适用于生成结果，也不能定位跨服务延迟；D 的 CloudTrail 是 API 审计服务，不是分布式 Trace，且没有提供 X-Ray Annotation。

#### 场景比喻

限流时所有客户端若每秒一起重试，就像所有汽车同时冲向收费站。指数退避加抖动让车辆分批返回；X-Ray 像给每辆车装定位器，Annotation 再标出它使用的模型，便于找到真正堵塞的路段。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS X-Ray | 分布式行程追踪器 | 跨服务定位调用链与延迟贡献，并用 Annotation 分类 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【瞬态错误退避、避免重试风暴和跨服务分布式追踪】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。AWS SDK Standard Retry Mode 配合带 Jitter 的 Exponential Backoff 能避免客户端同步重试，在限流或瞬态错误时逐步退让，从而降低级联故障风险；AWS X-Ray 可跨服务建立分布式 Trace，Annotation 还能按模型或组件筛选性能问题。A 的固定 1 秒重试容易形成重试风暴且没有分布式追踪；C 的缓存不能普遍适用于生成结果，也不能定位跨服务延迟；D 的 CloudTrail 是 API 审计服务，不是分布式 Trace，且没有提供 X-Ray Annotation。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的固定 1 秒重试容易形成重试风暴且没有分布式追踪。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【瞬态错误退避、避免重试风暴和跨服务分布式追踪】的关键机制。
- **C：** 该项的缓存不能普遍适用于生成结果，也不能定位跨服务延迟。
- **D：** 该项的 CloudTrail 是 API 审计服务，不是分布式 Trace，且没有提供 X-Ray Annotation。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【瞬态错误退避、避免重试风暴和跨服务分布式追踪】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **transient-error backoff, retry-storm prevention, and distributed tracing**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. AWS SDK standard retry mode with exponential backoff and jitter prevents synchronized retries and progressively backs off during throttling or transient errors, reducing the risk of cascading failure. AWS X-Ray provides distributed traces across service boundaries, and annotations allow performance issues to be filtered by model or component. A's fixed one-second delay can create retry storms and provides no distributed tracing. Caching is not generally safe for generated responses and cannot identify cross-service latency. CloudTrail in D is an API-audit service rather than distributed tracing and provides no X-Ray annotations.

#### Analogy

If every client retries once per second, all cars rush the tollbooth together. Exponential backoff with jitter staggers their return, while X-Ray tracks each trip and annotations label the model so the congested segment can be found.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS X-Ray | Distributed journey tracker | Finds cross-service latency contributors and classifies traces with annotations |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver transient-error backoff, retry-storm prevention, and distributed tracing, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. AWS SDK standard retry mode with exponential backoff and jitter prevents synchronized retries and progressively backs off during throttling or transient errors, reducing the risk of cascading failure. AWS X-Ray provides distributed traces across service boundaries, and annotations allow performance issues to be filtered by model or component. A's fixed one-second delay can create retry storms and provides no distributed tracing. Caching is not generally safe for generated responses and cannot identify cross-service latency. CloudTrail in D is an API-audit service rather than distributed tracing and provides no X-Ray annotations.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option proposes [Implement a custom retry mechanism with a fixed delay of 1 second between retries. Configure Amazon CloudWatch alarms to monitor the application's err…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for transient-error backoff, retry-storm prevention, and distributed tracing.
- **C:** This option proposes [Implement client-side caching of all FM responses. Add custom logging statements in the application code to record API call durations.], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **D:** This option proposes [Configure the AWS SDK with adaptive retry mode. Use AWS CloudTrail distributed tracing to monitor throttling events.], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is transient-error backoff, retry-storm prevention, and distributed tracing; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 77

### 中文

#### 考点背景

这题考查**模型、网络、对象和密钥的纵深访问控制与审计**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。IAM ABAC 和 VPC Endpoint 提供按属性和私有网络的访问控制，Bedrock 条件键可以强制调用指定的 ModelId 和 GuardrailIdentifier；CloudTrail 的 S3 Object Data Event 与 KMS 使用事件、S3 Server Access Logging 和 CloudTrail Lake 共同记录谁在何时访问了哪些对象，CloudWatch 再对 Bedrock、Rekognition 和 KMS 的异常活动告警。A 的 Trace 和模型 I/O 存储不能完整审计视频对象访问，且扩大敏感数据暴露；C 的 Config 和加密主要证明配置与静态保护，不足以提供完整访问审计；D 偏重异常检测和响应，没有同等明确的细粒度访问强制控制。

#### 场景比喻

视频平台像高保密档案馆：VPC Endpoint 是内部走廊，ABAC 是按员工标签开的门禁，Bedrock 条件键限定可用放映机和安全规则；CloudTrail、S3 日志与 KMS 事件共同组成“谁何时看了哪卷胶片”的登记簿。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |
| AWS CloudTrail | API 与数据访问登记簿 | 记录主体、时间、操作和受访问资源以供审计 |
| VPC Endpoint | AWS 私有走廊 | 让服务调用留在 AWS 私网并配合 Endpoint Policy 控制访问 |
| Amazon Rekognition | 视频视觉分析员 | 检测和分析图像或视频中的对象与活动 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【模型、网络、对象和密钥的纵深访问控制与审计】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。IAM ABAC 和 VPC Endpoint 提供按属性和私有网络的访问控制，Bedrock 条件键可以强制调用指定的 ModelId 和 GuardrailIdentifier；CloudTrail 的 S3 Object Data Event 与 KMS 使用事件、S3 Server Access Logging 和 CloudTrail Lake 共同记录谁在何时访问了哪些对象，CloudWatch 再对 Bedrock、Rekognition 和 KMS 的异常活动告警。A 的 Trace 和模型 I/O 存储不能完整审计视频对象访问，且扩大敏感数据暴露；C 的 Config 和加密主要证明配置与静态保护，不足以提供完整访问审计；D 偏重异常检测和响应，没有同等明确的细粒度访问强制控制。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 Trace 和模型 I/O 存储不能完整审计视频对象访问，且扩大敏感数据暴露。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【模型、网络、对象和密钥的纵深访问控制与审计】的关键机制。
- **C：** 该项的 Config 和加密主要证明配置与静态保护，不足以提供完整访问审计。
- **D：** 该项偏重异常检测和响应，没有同等明确的细粒度访问强制控制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【模型、网络、对象和密钥的纵深访问控制与审计】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **defense-in-depth access control and auditing across models, networks, objects, and keys**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. IAM ABAC and VPC endpoints provide attribute-based and private-network access control, while Bedrock condition keys can enforce specific ModelId and GuardrailIdentifier values. CloudTrail S3 object data events and KMS usage events, together with S3 server access logging and CloudTrail Lake, record who accessed which objects and when; CloudWatch can alert on anomalous activity from Bedrock, Rekognition, and KMS. A's traces and model-I/O storage do not fully audit access to video objects and can increase sensitive-data exposure. C's Config and encryption mainly prove configuration and at-rest protection, not a complete access audit. D emphasizes anomaly detection and response without equally explicit fine-grained access enforcement.

#### Analogy

The platform is a secure film archive. VPC endpoints are private corridors, ABAC opens doors by staff attributes, Bedrock condition keys restrict the projector and guardrail, and CloudTrail, S3, and KMS logs form the who-viewed-which-reel ledger.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |
| AWS CloudTrail | API and data-access ledger | Records actor, time, action, and affected resource for audits |
| VPC Endpoint | Private AWS corridor | Keeps service calls on private AWS networking with endpoint policy controls |
| Amazon Rekognition | Visual media analyst | Detects and analyzes objects and activity in images or video |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver defense-in-depth access control and auditing across models, networks, objects, and keys, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. IAM ABAC and VPC endpoints provide attribute-based and private-network access control, while Bedrock condition keys can enforce specific ModelId and GuardrailIdentifier values. CloudTrail S3 object data events and KMS usage events, together with S3 server access logging and CloudTrail Lake, record who accessed which objects and when; CloudWatch can alert on anomalous activity from Bedrock, Rekognition, and KMS. A's traces and model-I/O storage do not fully audit access to video objects and can increase sensitive-data exposure. C's Config and encryption mainly prove configuration and at-rest protection, not a complete access audit. D emphasizes anomaly detection and response without equally explicit fine-grained access enforcement.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option proposes [Configure VPC endpoints for Amazon Bedrock model API calls. Implement Amazon Bedrock Guardrails to filter harmful or unauthorized content in prompts a…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for defense-in-depth access control and auditing across models, networks, objects, and keys.
- **C:** This option proposes [Restrict access to services by using VPC endpoint policies. Use AWS Config to track resource changes and compliance with security rules. Use server-si…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **D:** This option emphasizes anomaly detection and response without equally explicit fine-grained access enforcement.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is defense-in-depth access control and auditing across models, networks, objects, and keys; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 78

### 中文

#### 考点背景

这题考查**事件驱动、可持久化的长时训练与部署编排**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。S3 新文件事件触发 Step Functions Standard Workflow，Lambda 负责接收事件，后续状态调用 SageMaker Pipelines 完成数据处理、训练和重新部署；Standard Workflow 能持久化长时间运行的训练与部署状态，并支持重试和顺序控制。A 把实时推理 Endpoint 当作训练和部署机制，概念错误；B 依赖自定义 Webhook 和 EventBridge，缺少明确的持久化编排与等待流程；C 使用 Express、Data Wrangler 和 Autopilot，增加不必要的组件，也没有清晰保证既有模型的受控部署。

#### 场景比喻

新文件像工厂新到的一批原料：S3 触发开工，Step Functions Standard 是保存每道工序状态的生产主管，SageMaker Pipelines 负责清洗、训练和发布。即使工序持续数小时，主管也不会因为一次短连接断开而忘记进度。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | 按需执行的工位 | 以事件或请求驱动方式运行轻量集成与校验逻辑 |
| AWS Step Functions | 有记忆的流程主管 | 持久化编排长时、异步、重试和分支工作流 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【事件驱动、可持久化的长时训练与部署编排】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。S3 新文件事件触发 Step Functions Standard Workflow，Lambda 负责接收事件，后续状态调用 SageMaker Pipelines 完成数据处理、训练和重新部署；Standard Workflow 能持久化长时间运行的训练与部署状态，并支持重试和顺序控制。A 把实时推理 Endpoint 当作训练和部署机制，概念错误；B 依赖自定义 Webhook 和 EventBridge，缺少明确的持久化编排与等待流程；C 使用 Express、Data Wrangler 和 Autopilot，增加不必要的组件，也没有清晰保证既有模型的受控部署。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项把实时推理 Endpoint 当作训练和部署机制，概念错误。
- **B：** 该项依赖自定义 Webhook 和 EventBridge，缺少明确的持久化编排与等待流程。
- **C：** 该项使用 Express、Data Wrangler 和 Autopilot，增加不必要的组件，也没有清晰保证既有模型的受控部署。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【事件驱动、可持久化的长时训练与部署编排】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【事件驱动、可持久化的长时训练与部署编排】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **event-driven durable orchestration for long-running training and deployment**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. An S3 new-file event starts a Step Functions Standard Workflow; Lambda receives the event, and a later state runs SageMaker Pipelines for data processing, training, and redeployment. A Standard Workflow can persist the state of long-running training and deployment operations and provide retries and sequencing. A incorrectly treats a real-time inference endpoint as the training and deployment mechanism. B relies on custom webhooks and EventBridge without the explicit durable orchestration and waiting flow. C adds Express, Data Wrangler, and Autopilot components unnecessarily and does not clearly provide controlled deployment of the existing model.

#### Analogy

A new S3 file is a shipment of raw material. Step Functions Standard is the supervisor that remembers every long-running stage, while SageMaker Pipelines performs processing, training, and deployment without losing state when a short request ends.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda | On-demand workbench | Runs event- or request-driven integration and validation logic |
| AWS Step Functions | Stateful workflow supervisor | Durably orchestrates long-running, asynchronous, retry, and branching work |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver event-driven durable orchestration for long-running training and deployment, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. An S3 new-file event starts a Step Functions Standard Workflow; Lambda receives the event, and a later state runs SageMaker Pipelines for data processing, training, and redeployment. A Standard Workflow can persist the state of long-running training and deployment operations and provide retries and sequencing. A incorrectly treats a real-time inference endpoint as the training and deployment mechanism. B relies on custom webhooks and EventBridge without the explicit durable orchestration and waiting flow. C adds Express, Data Wrangler, and Autopilot components unnecessarily and does not clearly provide controlled deployment of the existing model.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option incorrectly treats a real-time inference endpoint as the training and deployment mechanism.
- **B:** This option relies on custom webhooks and EventBridge without the explicit durable orchestration and waiting flow.
- **C:** This option adds Express, Data Wrangler, and Autopilot components unnecessarily and does not clearly provide controlled deployment of the existing model.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for event-driven durable orchestration for long-running training and deployment.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is event-driven durable orchestration for long-running training and deployment; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 79

### 中文

#### 考点背景

这题考查**RAG 检索与生成的联合评估、模型裁判和发布门槛**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。Retrieve-and-Generate Evaluation Job 可以同时评估检索和生成；自定义 Precision@k 衡量检索命中，1～5 分的 LLM-as-a-Judge 衡量生成质量，把不同 Chunking Strategy 放在同一评估数据集中即可比较两个 FM，并用评估分数作为自动部署门槛。A 只评估检索，不能比较生成质量；C 为每种组合建立 Job 并人工审核，工作量大且不能自动阻止发布；D 拆成多个 Job，缺少同一配置中的端到端比较和所需的检索精度指标。

#### 场景比喻

把 RAG 当成开卷考试：Precision@k 检查学生找到了哪些正确资料，LLM-as-a-Judge 给最终答案打分，Retrieve-and-Generate Job 同时查看“找书”和“答题”，CI/CD 门禁则是不及格不能毕业。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【RAG 检索与生成的联合评估、模型裁判和发布门槛】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。Retrieve-and-Generate Evaluation Job 可以同时评估检索和生成；自定义 Precision@k 衡量检索命中，1～5 分的 LLM-as-a-Judge 衡量生成质量，把不同 Chunking Strategy 放在同一评估数据集中即可比较两个 FM，并用评估分数作为自动部署门槛。A 只评估检索，不能比较生成质量；C 为每种组合建立 Job 并人工审核，工作量大且不能自动阻止发布；D 拆成多个 Job，缺少同一配置中的端到端比较和所需的检索精度指标。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项只评估检索，不能比较生成质量。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【RAG 检索与生成的联合评估、模型裁判和发布门槛】的关键机制。
- **C：** 该项为每种组合建立 Job 并人工审核，工作量大且不能自动阻止发布。
- **D：** 该项拆成多个 Job，缺少同一配置中的端到端比较和所需的检索精度指标。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【RAG 检索与生成的联合评估、模型裁判和发布门槛】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **joint RAG retrieval-generation evaluation, model judging, and release gates**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. A retrieve-and-generate evaluation job assesses both retrieval and generation. Custom precision-at-k measures retrieval quality, while a 1-to-5 LLM-as-a-judge metric measures generated-response quality. Putting the chunking strategies in one evaluation dataset enables comparison of both FMs, and the scores can serve as automated deployment gates. A evaluates retrieval only and cannot compare generation quality. C creates a job for every combination and relies on manual review, increasing work and failing to block releases automatically. D splits the process into multiple jobs and lacks the requested end-to-end comparison and retrieval-precision metric in one configuration.

#### Analogy

Treat RAG as an open-book exam. Precision@k checks whether the right pages were found, an LLM judge grades the final answer, and a retrieve-and-generate job evaluates both stages before the CI/CD gate permits graduation.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver joint RAG retrieval-generation evaluation, model judging, and release gates, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. A retrieve-and-generate evaluation job assesses both retrieval and generation. Custom precision-at-k measures retrieval quality, while a 1-to-5 LLM-as-a-judge metric measures generated-response quality. Putting the chunking strategies in one evaluation dataset enables comparison of both FMs, and the scores can serve as automated deployment gates. A evaluates retrieval only and cannot compare generation quality. C creates a job for every combination and relies on manual review, increasing work and failing to block releases automatically. D splits the process into multiple jobs and lacks the requested end-to-end comparison and retrieval-precision metric in one configuration.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option evaluates retrieval only and cannot compare generation quality.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for joint RAG retrieval-generation evaluation, model judging, and release gates.
- **C:** This option creates a job for every combination and relies on manual review, increasing work and failing to block releases automatically.
- **D:** This option splits the process into multiple jobs and lacks the requested end-to-end comparison and retrieval-precision metric in one configuration.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is joint RAG retrieval-generation evaluation, model judging, and release gates; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 80

### 中文

#### 考点背景

这题考查**多模态数据预处理、RAG 事实依据、安全边界与研究审计**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。SageMaker Processing 和 Transcribe 可以把视频、音频及其他多模态输入预处理为可检索摘要，再由 Bedrock Knowledge Base 通过 RAG 支持自然语言查询；Guardrails 能限制无依据的推测性输出，AppConfig 适合集中管理 Prompt Template，CloudTrail 则保留研究活动审计。A 只有基础模板和 IAM，缺少 RAG grounding 与推测控制；C 的 OpenSearch 和实体抽取需要自定义问答链，也没有 Guardrails；D 依赖跨服务联合和自定义 Lambda 过滤，运维更重且没有同样明确的托管安全与审计组合。

#### 场景比喻

研究助手像动物园研究室：Processing 和 Transcribe 把录像、录音与传感器读数整理成实验笔记，Knowledge Base 是可检索档案柜，Guardrails 是要求研究员“只根据证据发言”的伦理委员，CloudTrail 保存查阅记录。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 模型进出口安检 | 在请求和响应边界执行内容、主题和敏感信息策略 |
| AWS CloudTrail | API 与数据访问登记簿 | 记录主体、时间、操作和受访问资源以供审计 |
| Amazon SageMaker Processing | 托管数据加工车间 | 对音视频、传感器或训练数据运行可重复预处理 |
| Amazon Transcribe | 语音速记员 | 把音频转换为后续可检索和分析的文字 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【多模态数据预处理、RAG 事实依据、安全边界与研究审计】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。SageMaker Processing 和 Transcribe 可以把视频、音频及其他多模态输入预处理为可检索摘要，再由 Bedrock Knowledge Base 通过 RAG 支持自然语言查询；Guardrails 能限制无依据的推测性输出，AppConfig 适合集中管理 Prompt Template，CloudTrail 则保留研究活动审计。A 只有基础模板和 IAM，缺少 RAG grounding 与推测控制；C 的 OpenSearch 和实体抽取需要自定义问答链，也没有 Guardrails；D 依赖跨服务联合和自定义 Lambda 过滤，运维更重且没有同样明确的托管安全与审计组合。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项只有基础模板和 IAM，缺少 RAG grounding 与推测控制。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【多模态数据预处理、RAG 事实依据、安全边界与研究审计】的关键机制。
- **C：** 该项的 OpenSearch 和实体抽取需要自定义问答链，也没有 Guardrails。
- **D：** 该项依赖跨服务联合和自定义 Lambda 过滤，运维更重且没有同样明确的托管安全与审计组合。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【多模态数据预处理、RAG 事实依据、安全边界与研究审计】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **multimodal preprocessing, RAG grounding, safety boundaries, and research auditing**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. SageMaker Processing and Transcribe can preprocess video, audio, and other multimodal inputs into searchable summaries, while a Bedrock Knowledge Base supports natural-language queries through RAG. Guardrails can restrict unsupported speculative outputs, AppConfig centrally manages prompt templates, and CloudTrail preserves research activity for audits. A provides only basic templates and IAM and lacks RAG grounding and speculation controls. C requires a custom OpenSearch and entity-extraction question-answering path and has no Guardrails. D relies on cross-service federation and custom Lambda filtering, adding operations without the same clear managed safety and audit combination.

#### Analogy

The assistant is a zoo research lab. Processing and Transcribe turn recordings and sensor feeds into notes, the Knowledge Base is the searchable archive, Guardrails is the ethics reviewer demanding evidence, and CloudTrail preserves the access record.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Model boundary checkpoint | Enforces content, topic, and sensitive-information policies on input and output |
| AWS CloudTrail | API and data-access ledger | Records actor, time, action, and affected resource for audits |
| Amazon SageMaker Processing | Managed processing shop | Runs repeatable preprocessing for media, sensor, or training data |
| Amazon Transcribe | Speech stenographer | Turns audio into text that can be retrieved and analyzed |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver multimodal preprocessing, RAG grounding, safety boundaries, and research auditing, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. SageMaker Processing and Transcribe can preprocess video, audio, and other multimodal inputs into searchable summaries, while a Bedrock Knowledge Base supports natural-language queries through RAG. Guardrails can restrict unsupported speculative outputs, AppConfig centrally manages prompt templates, and CloudTrail preserves research activity for audits. A provides only basic templates and IAM and lacks RAG grounding and speculation controls. C requires a custom OpenSearch and entity-extraction question-answering path and has no Guardrails. D relies on cross-service federation and custom Lambda filtering, adding operations without the same clear managed safety and audit combination.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option provides only basic templates and IAM and lacks RAG grounding and speculation controls.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for multimodal preprocessing, RAG grounding, safety boundaries, and research auditing.
- **C:** This option requires a custom OpenSearch and entity-extraction question-answering path and has no Guardrails.
- **D:** This option relies on cross-service federation and custom Lambda filtering, adding operations without the same clear managed safety and audit combination.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is multimodal preprocessing, RAG grounding, safety boundaries, and research auditing; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---
