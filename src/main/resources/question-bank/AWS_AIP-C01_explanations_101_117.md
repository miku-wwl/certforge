## Question 101

### 中文

#### 考点背景

本题考查多语言文本检索的建模边界：输入语言不等于必须统一成英语。Titan Text Embeddings 的价值在于把语义映射到向量空间；对于英语、法语、德语等原文，保留原始术语通常比先翻译更能保护医学缩写、药名和细微上下文。医学文档还需要按完整章节或语义单元切块，避免把诊断结论、限定条件和引用拆散。

题干同时强调 50,000 份文本、跨国部署、高准确率和低维护。它在寻找的是 Knowledge Bases 管理的摄取、分块、嵌入和检索路径，而不是一条需要自建翻译、图片化或查询语言检测的流水线。A 直接针对文本语料的形态，保留多语言医学语境，并把自定义代码控制在较小范围内。

#### 场景比喻

像给三种语言写成的医学图书馆编目录：最好的馆员会保留书名、药名和章节关系，只把每章放进可跨语言查找的索引；不会先把整座图书馆机翻后再索引，也不会把文字书拍成照片。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Titan Text Embeddings | 多语种语义坐标员 | 将原文医学片段转换为用于相似度检索的向量 |
| Amazon Bedrock Knowledge Bases | 资料馆总管 | 承担数据摄取、分块、嵌入和检索集成，减少自定义维护 |
| Semantic chunking | 按章节装订的目录员 | 以医学章节保留术语与上下文，而不是机械截断 |
| Vector store | 可按含义查书的索引柜 | 保存嵌入并返回与查询语义相近的片段 |

#### 正确答案与推理

1. 先看资料形态：题目是文本为主，不需要把页面转成图像，因此应选文本嵌入。
2. 再看语言要求：英语、法语、德语术语需要保真；A 保留原始语言，避免翻译改变药名、缩写或医学限定。
3. 再看准确率：按医学章节做语义切块，让检索结果带着定义、结论和上下文。
4. 最后看运营成本：Bedrock Knowledge Bases 负责托管式知识库流程，符合“少自定义代码、少持续维护”。

#### 逐项排除

- **A.** 正确；原文多语言嵌入、医学章节级语义切块和知识库式检索共同覆盖准确率与低维护要求。
- **B.** 分块方向合理，但强制先翻译会损失专业术语语义和原文细节，并引入不必要的转换步骤。
- **C.** 不合适；文本密集型文档没有图像分析需求，转图和生成 Caption 反而增加处理链路与信息损失风险。
- **D.** 不合适；Kendra 并不是这里所需的 Bedrock Knowledge Bases 向量摄取方案，题设也没有要求每次查询自动翻译。

#### 解题方法

看到“多语言文本、术语准确、少维护”，优先保留原文并选择文本嵌入；看到“章节上下文”，选择语义切块。只有出现图像、表格视觉结构或音视频，才考虑多模态路线。把“能实现”与“最贴合题干且维护最少”分开比较。

### English

#### Exam focus and background

This question tests multilingual semantic retrieval, not whether every document must first be converted to English. Text embeddings represent meaning in a vector space, and preserving the source language can protect medical names, abbreviations, and qualifiers that a translation step might distort. Chunking at a meaningful medical-section boundary also keeps a diagnosis, limitation, or citation with the context that makes it useful.

The scale and maintenance clues matter as much as accuracy. The workload is text-heavy, spans many languages, and should be implemented with minimal custom code. A Bedrock Knowledge Bases workflow is therefore the natural managed path for ingestion, chunking, embedding, and retrieval. The winning design preserves multilingual source content and applies semantic section-level chunks rather than adding an unnecessary translation or image-caption pipeline.

#### Analogy

Imagine cataloging a medical library whose books have English, French, and German titles. A good librarian preserves the original drug names and section structure while adding a meaning-based index. The librarian does not translate every book first or photograph every page.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Titan Text Embeddings | Multilingual semantic cartographer | Converts source-language medical passages into retrieval vectors |
| Amazon Bedrock Knowledge Bases | Managed library curator | Coordinates ingestion, chunking, embeddings, and retrieval with less custom code |
| Semantic chunking | Section-aware bookbinder | Keeps medical concepts and their qualifiers together |
| Vector store | Meaning-based catalog | Stores embeddings and returns semantically related passages |

#### Correct answer and reasoning

1. The corpus is primarily text, so text embeddings fit better than a vision-oriented representation.
2. The documents contain English, French, and German medical terms. Answer A preserves those terms instead of risking translation drift.
3. Medical-section semantic chunks retain the context needed for precise retrieval.
4. A managed Bedrock Knowledge Bases flow satisfies the scale and low-maintenance requirement without inventing a separate translation service.

#### Option-by-option elimination

- **A.** Correct. It preserves multilingual source text, uses context-aware chunks, and matches the managed text-retrieval requirement.
- **B.** Its logical chunking is sensible, but mandatory translation adds a failure point and can alter specialized terminology.
- **C.** Incorrect for a text-heavy corpus; converting pages to images and captions adds work and can discard textual precision.
- **D.** Incorrect framing; it mixes services and assumes per-query language translation that the requirement does not need.

#### Exam strategy

For multilingual text plus terminology accuracy, favor source-language text embeddings and semantic chunks. Treat “minimal custom code” as a tie-breaker toward a managed Knowledge Bases workflow. Do not select multimodal processing merely because the documents are formatted files.

---
## Question 102

### 中文

#### 考点背景

本题考查实时 LLM 推理的两个不同瓶颈：首个 Token 前的冷启动/模型装载等待，以及完整响应生成时间。SageMaker AI Real-Time Endpoint 要保持可服务容量，模型应在容器启动时预加载；最小实例数大于零可以避免请求到达时才启动实例或装载权重。Dynamic Batching 则在吞吐和 GPU 利用率之间做取舍，但不能用异步推理替代交互响应。

题干明确关注“等待首个 Token 超过 2 秒”和 P95 小于 800 ms。Response Streaming 能让客户端在完整答案生成前看到首批输出，直接改善感知延迟；预热实例和预加载模型则减少真实首 Token 延迟。故 A 与 D 分别处理推理效率和响应交付时机，组合最完整。

#### 场景比喻

像一家咖啡店：A 让咖啡机开店前已预热，并把同一烘焙批次的订单合并处理；D 让店里始终有人值班，并先递出第一口咖啡，而不是等整杯完成才说“正在制作”。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| SageMaker AI Real-Time Endpoint | 常驻的出餐柜台 | 为交互请求提供在线模型推理 |
| Model preload | 开店前预热设备 | 容器启动时加载权重，减少首次请求等待 |
| Dynamic batching | 合并同批订单的厨师 | 提高 GPU 利用率和吞吐，降低排队抖动 |
| Response streaming | 分段传菜员 | 先返回已生成的 Token，降低用户等到首个 Token 的时间 |

#### 正确答案与推理

1. 这是交互式实时端点，不应把所有请求改成 S3 驱动的异步推理。
2. A 的预加载避免请求触发 Lazy Load；Dynamic Batching 让多个请求共享一次推理通道，提高高峰期效率。
3. D 的最小实例数大于零保持热实例，Response Streaming 让首个 Token 尽快抵达客户端。
4. 两项合起来既减少端点内部等待，也改善客户端获得首个输出的时间，最符合 P95 目标。

#### 逐项排除

- **A.** 正确；预加载解决模型装载延迟，Dynamic Batching 改善吞吐和 GPU 利用率。
- **B.** 更大 GPU 可能有帮助，但最小实例为零且 Lazy Load 会制造冷启动，仍逐请求处理也无法稳定 P95。
- **C.** Multi-Model Endpoint 加 Lazy Loading 与首 Token 目标相冲突，且不做 batching 不能缓解并发压力。
- **D.** 正确；常驻实例减少冷启动，Streaming 缩短用户等到首个 Token 的时间。
- **E.** 异步推理适合不要求立即返回的工作负载；S3 和零实例数不符合交互式聊天目标。

#### 解题方法

把线索拆成三类：冷启动看常驻实例和预加载，吞吐看 batching，首个 Token 看 streaming。看到 interactive、first token、P95，优先组合这三类能力；看到 asynchronous、S3、允许排队，才考虑异步推理。

### English

#### Exam focus and background

This question separates two latency problems in real-time LLM inference. Waiting before the first token can come from endpoint cold starts and lazy model loading. Waiting for the entire completion is a different problem, and response streaming can expose early tokens before generation finishes. A warm SageMaker AI endpoint with the model preloaded reduces real startup delay; dynamic batching improves accelerator utilization when requests arrive together.

The requirement is interactive traffic with a strict p95 target, not a batch job. The two selected actions therefore address different parts of the path: A improves the endpoint’s readiness and throughput, while D keeps capacity available and returns partial output as soon as it exists. Asynchronous inference, scale-to-zero, or lazy loading would make the first-token experience less predictable.

#### Analogy

Think of a coffee shop. Option A preheats the machine before opening and groups compatible orders efficiently. Option D keeps a barista and machine ready while handing the customer the first sip immediately instead of waiting for the entire cup.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| SageMaker AI real-time endpoint | Always-open service counter | Hosts online inference for interactive requests |
| Model preload | Preheated equipment | Loads model weights during container startup |
| Dynamic batching | Order-grouping barista | Improves accelerator utilization and throughput |
| Response streaming | First-sip server | Delivers generated tokens before the full response is complete |

#### Correct answer and reasoning

1. The workload is interactive, so moving every request to S3-backed asynchronous inference is a mismatch.
2. A removes lazy loading and uses dynamic batching to improve readiness and high-concurrency efficiency.
3. D keeps at least one instance available and streams the first output promptly.
4. Together, A and D reduce actual startup delay and perceived first-token delay, matching the p95 objective.

#### Option-by-option elimination

- **A.** Correct. Preloading removes request-time weight loading, and dynamic batching improves throughput.
- **B.** A larger GPU may help raw speed, but zero minimum capacity and lazy loading preserve cold-start risk.
- **C.** A multi-model endpoint with lazy loading and no batching does not solve the first-token or concurrency problem.
- **D.** Correct. Warm capacity avoids scale-from-zero delay, and streaming improves time to first output.
- **E.** Asynchronous inference is for decoupled workloads, not a chat path that must respond interactively.

#### Exam strategy

Map keywords to mechanisms: cold start means warm instances and preload; throughput means batching; first-token or time-to-first-byte means streaming. For interactive p95 questions, reject options that introduce queues, S3 polling, scale-to-zero, or lazy loading unless the stem explicitly permits asynchronous work.

---

## Question 103

### 中文

#### 考点背景

本题测试“动态模型选择”到底应该放在业务代码里，还是交给配置发布系统。应用需要依据质量、延迟和错误率切换 Bedrock 模型，同时不重新部署 Lambda；这意味着模型选择应是运行时配置或 Feature Flag，而不是写死的环境变量或函数版本。AWS AppConfig Agent Lambda Extension 可以让函数读取最新配置，避免为每一次模型切换改代码。

第二层是安全发布：新模型从 20% 流量开始，错误率超过 1% 自动回滚。AppConfig 的部署策略、验证规则和可回滚发布正好覆盖逐步推出与门禁。三个 Region 的存在强化了配置分发和运行时读取的重要性，但不改变“托管配置发布”这个核心判断。

#### 场景比喻

像机场给新登机系统做灰度上线：先让 20% 的旅客经过新柜台，监控延误率，超过红线就关回旧柜台；值班人员改的是运营控制台，不是重写机场软件。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS AppConfig | 灰度发布控制塔 | 管理模型选择 Feature Flag、验证规则和发布策略 |
| AppConfig Agent Lambda Extension | 函数旁的配置收音机 | 让 Lambda 运行时获取配置而不重新部署代码 |
| Linear deployment strategy | 分阶段放行闸门 | 从 20% 流量起逐步扩大新模型覆盖 |
| Amazon Bedrock | 可替换的模型柜台 | 承接被配置选中的 Foundation Model 调用 |

#### 正确答案与推理

1. “不修改、不重新部署代码”排除了把模型选择写进 Lambda 逻辑或函数版本。
2. AppConfig Feature Flag 可把模型标识和选择条件外置，运行时由 Extension 读取。
3. Validation Rule 用错误率等指标做发布门槛，Linear Strategy 负责从 20% 逐步放量。
4. 当错误率超过 1% 时，托管发布流程可以停止或回滚；因此 A 同时覆盖动态选择、灰度和故障保护。

#### 逐项排除

- **A.** 正确；AppConfig 的配置、Agent、验证和渐进部署与全部要求一一对应。
- **B.** DynamoDB 能存配置，却需要 EventBridge、Lambda 更新和每次调用查询的自定义逻辑，回滚链路也需自行维护。
- **C.** Lambda Alias/Version 控制的是函数版本流量，不是运行时切换 Bedrock 模型配置。
- **D.** Dashboard 只能观察，Lambda 自定义阈值逻辑会重新引入代码部署和回滚责任；Parameter Store 也不是完整的渐进发布控制器。

#### 解题方法

看到“配置与代码分离、Feature Flag、灰度、验证、回滚”，优先 AppConfig。不要把 Lambda Alias 的流量切换误认为模型流量切换；也不要把监控 Dashboard 当作自动发布门禁。

### English

#### Exam focus and background

The key distinction is runtime configuration versus application deployment. The application must switch among Bedrock models based on quality and latency without changing or redeploying Lambda code. That points to an external feature-flag and configuration service, read at runtime through the AppConfig Agent Lambda Extension, rather than model IDs embedded in application logic or Lambda versions.

The rollout requirements are equally decisive. A new model starts with 20% of traffic and must be rolled back when errors exceed 1%. AppConfig deployment strategies and validation rules provide a managed way to gate and gradually release configuration. The three-Region detail reinforces the value of centrally managed runtime configuration; it does not turn Lambda alias routing into model-selection control.

#### Analogy

Imagine replacing an airport check-in system. Operations sends 20% of travelers to the new counter, watches delay rates, and closes that counter if the error line is crossed. The operator changes the control panel, not the airport software.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS AppConfig | Canary-release control tower | Holds feature flags, validation rules, and deployment strategies |
| AppConfig Agent Lambda Extension | Radio beside the application | Delivers current configuration at runtime without code redeployment |
| Linear deployment strategy | Staged access gate | Gradually increases the new model from 20% traffic |
| Amazon Bedrock | Replaceable model counter | Invokes the FM selected by the external configuration |

#### Correct answer and reasoning

1. No code change or redeployment means the model choice must be externalized.
2. AppConfig feature flags can store the selection criteria or model mapping, and the Lambda extension reads it at runtime.
3. Validation rules can check error-rate conditions, while a linear strategy starts at 20% and expands gradually.
4. The managed deployment can stop or roll back when the 1% error threshold is exceeded. A therefore covers dynamic selection, rollout, and protection.

#### Option-by-option elimination

- **A.** Correct. It maps external flags, runtime retrieval, progressive rollout, validation, and rollback to the requirements.
- **B.** DynamoDB can store data, but the EventBridge, update, query, and rollback behavior becomes custom operational code.
- **C.** Lambda aliases shift traffic between function versions, not between Bedrock model configurations at runtime.
- **D.** A dashboard observes metrics, while custom Lambda thresholds and Parameter Store do not provide the full managed progressive deployment and rollback flow.

#### Exam strategy

When the stem says feature flags, no redeployment, gradual rollout, validation, and rollback, think AppConfig. Distinguish function-version traffic shifting from model-selection configuration, and distinguish monitoring from an automated deployment gate.

---

## Question 104

### 中文

#### 考点背景

本题把两层权限故障放在一起：Bedrock Agent Runtime 的 400 Bad Authorization 反映调用 Agent 的身份权限，OpenSearch Serverless 的 403 则反映数据平面访问授权。Lambda 的执行角色必须能调用 Agent，并且 AOSS API 访问还需要相应的 aoss 权限；只有身份策略允许仍不够，OpenSearch Serverless 还要有 Data Access Policy 授权具体 Collection 和 Index。

题干特别说“按 Pattern 匹配 Collection 和 Index 自动分配权限”。这是 OpenSearch Serverless Data Access Policy 的典型线索。VPC Endpoint Policy 解决的是网络入口条件，Secrets Manager 解决的是秘密存储，OpenSearch Service Domain 的 es 权限也不是 AOSS 的授权面，因此 A、B 正好分别补齐调用层和数据层。

#### 场景比喻

像研究员进入资料馆：前台先检查他是否有“进入智能检索室”的门票，馆内管理员再检查他是否能打开以某个前缀命名的书架。网络通道通了，不代表他获得了书架内容权限。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda execution role | 研究员证件 | 允许 Lambda 调用 Bedrock Agent 并访问 AOSS API |
| Amazon Bedrock Agent Runtime | 智能检索前台 | 接收测试请求并运行 Agent |
| Amazon OpenSearch Serverless | 可扩展资料馆 | 承载 Knowledge Base 的 Collection/Index 数据 |
| AOSS Data Access Policy | 书架授权清单 | 按资源 Pattern 授予 Collection 和 Index 的数据访问 |

#### 正确答案与推理

1. 先定位 400：测试工具调用 Agent，需要 Lambda 执行角色具备 bedrock:InvokeAgent。
2. 再定位 403：请求到达 AOSS 后还要允许 API 访问，因此角色需要 aoss:APIAccessAll。
3. AOSS 数据平面采用 Data Access Policy 管理 Collection 和 Index 的资源权限；Pattern 规则满足规模化匹配。
4. A 与 B 联合后，身份能调用 Agent、能触达 AOSS API，且数据资源策略允许实际读写，覆盖两类错误。

#### 逐项排除

- **A.** 正确；同时修复 Agent 调用权限和 AOSS API 访问权限，但仍需与 B 的数据策略配合。
- **B.** 正确；Pattern-based Data Access Policy 授予匹配 Collection/Index 的数据权限，解决 403 的关键一层。
- **C.** 不足；VPC Endpoint Policy 和 Lambda VPC 配置属于网络路径控制，不能授予 AOSS 数据访问权限。
- **D.** 不合适；AOSS 使用 IAM 与数据访问策略，不需要把不存在的用户名密码路径作为主要修复。
- **E.** 不合适；es:ESHttp* 面向 OpenSearch Service Domain，不能替代 OpenSearch Serverless 的 aoss 授权。

#### 解题方法

遇到 Bedrock Agent + AOSS，分层排查：先看 bedrock:InvokeAgent，再看 aoss API 权限，最后看 Data Access Policy。看到 Collection/Index Pattern 就锁定 AOSS 数据访问策略；不要用 VPC 或 Secrets Manager 代替授权。

### English

#### Exam focus and background

This scenario contains two authorization planes. The Bedrock Agent Runtime error concerns whether the Lambda identity may invoke the agent. The OpenSearch Serverless 403 concerns access to the AOSS data plane. The execution role therefore needs the agent invocation permission and the AOSS API permission, while OpenSearch Serverless also needs a data access policy that grants access to the relevant collections and indexes.

The pattern requirement is the strongest clue. A pattern-based AOSS data access policy is designed to apply permissions to matching collection and index resources at scale. Network endpoint policy, stored credentials, and OpenSearch Service domain permissions address different concerns. Answers A and B jointly repair the caller authorization and the resource authorization.

#### Analogy

Picture a researcher entering a library. The front desk checks whether the researcher has a pass for the intelligent-search room. Inside, a librarian checks whether the pass allows opening shelves with a particular name pattern. A working network door does not grant shelf permissions.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda execution role | Researcher identity card | Authorizes agent invocation and AOSS API access |
| Amazon Bedrock Agent Runtime | Intelligent-search desk | Receives the test request and runs the agent |
| Amazon OpenSearch Serverless | Scalable library | Hosts the Knowledge Base collections and indexes |
| AOSS data access policy | Shelf-permission register | Grants data access to matching collection and index resources |

#### Correct answer and reasoning

1. The 400 Bad Authorization points to the caller’s ability to invoke the agent, so the role needs bedrock:InvokeAgent.
2. The AOSS request also needs aoss:APIAccessAll on the execution role.
3. AOSS data-plane access is separately granted by a data access policy, and its resource rules can match collection and index patterns.
4. A and B together cover the agent identity, AOSS API access, and actual data-resource authorization.

#### Option-by-option elimination

- **A.** Correct. It supplies the agent invocation and AOSS API permissions, although the data policy in B is also needed.
- **B.** Correct. Pattern-based rules authorize the matching collections and indexes in the AOSS data plane.
- **C.** Insufficient. Endpoint policy and VPC placement govern network access, not the data permissions that produce the 403.
- **D.** Incorrect direction. AOSS authorization is not fixed by introducing stored username/password credentials.
- **E.** Incorrect service model. es:ESHttp* is for an OpenSearch Service domain, not the AOSS aoss permission model.

#### Exam strategy

For Bedrock Agent plus AOSS, separate agent invocation, AOSS API access, and collection/index data access. A collection or index pattern is a direct signal for an AOSS data access policy. Do not substitute VPC controls or Secrets Manager for authorization policies.

---

## Question 105

### 中文

#### 考点背景

本题考查 Text-to-SQL 的纵深防御，而不是单纯“让模型生成得更好”。自然语言输入可能携带 SQL Injection 意图，模型生成的 SQL 还可能语法错误、越权访问或引用不在允许范围内的表列。因此安全流程必须在执行前串起输入过滤、语法解析、Schema 校验和审计记录；任何一层都不能由“模型看起来合理”替代。

B 的价值是职责清晰且顺序正确：Guardrails 先处理有害输入，Lambda 中的 SQL parser 检查结构，RDS Data API 配合预定义 Schema 做数据库语义验证，真正执行前记录审计。它把安全决策放在后端可控制的流水线中，而不是依赖客户端隐藏结果或只记录已经发生的 API 调用。

#### 场景比喻

像银行柜员处理一张自然语言填单：保安先拦截恶意指令，格式审核员检查表格语法，账户管理员核对可访问的账户清单，最后在放行前盖章留痕。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 输入安检门 | 过滤有害内容并阻止危险 Prompt 继续流转 |
| AWS Lambda + SQL parser | SQL 语法审稿员 | 检查模型生成语句的结构与语法 |
| Amazon RDS Data API | 数据库规则核验员 | 按预定义 Schema 验证表、列和查询可执行性 |
| Audit log pipeline | 银行盖章簿 | 在真正执行 Query 前记录转换与校验结果 |

#### 正确答案与推理

1. 先对自然语言 Prompt 做 Guardrails 过滤，阻止 SQL Injection 企图或有害输入进入生成/执行链。
2. 对生成 SQL 做解析，确认语法完整，而不是直接拼接到数据库请求。
3. 再按预定义 Schema 检查允许的表列和结构，避免语法正确但语义越权。
4. 在执行前写入审计事件，记录输入转换、校验和最终放行决定。B 依次满足全部要求。

#### 逐项排除

- **A.** 不正确；GuardDuty 并非题设所需的自然语言 SQL 转换防线，事件存储也不能替代执行前 Schema 校验。
- **B.** 正确；多阶段流程覆盖输入拦截、语法验证、Schema 验证和执行前审计。
- **C.** CloudTrail 适合记录 API 活动，WAF 偏向边界请求过滤，但二者不能验证模型输出的 SQL 结构和 Schema。
- **D.** 自定义模型与网络隔离可能有价值，却没有形成题设要求的完整、可审计的逐层验证。

#### 解题方法

看到“输入注入 + 生成 SQL + Schema + audit trail”，寻找顺序明确的 pre-execution pipeline。Guardrails 负责内容安全，解析器负责语法，数据库/Schema 规则负责语义授权，审计必须发生在执行前；不要把 WAF、CloudTrail 或自定义模型当成全部答案。

### English

#### Exam focus and background

Text-to-SQL security requires defense in depth, not merely a better prompt. A natural-language request can contain an injection attempt, and a generated query can be syntactically valid while referencing forbidden tables or columns. The control path must therefore filter the input, parse the generated SQL, validate it against an approved schema, and record the decision before execution.

Answer B expresses that sequence as a multi-stage pipeline. Bedrock Guardrails handles the harmful-input boundary, Lambda can run a SQL parser, the RDS Data API participates in schema-aware validation, and the audit record is created before the query is executed. This puts the safety decision in a controllable backend path instead of relying on UI masking or logging only after an API call has happened.

#### Analogy

It is like processing a banking form written in plain language: a guard stops malicious instructions, a format reviewer checks syntax, an account clerk checks the approved account list, and the bank stamps the decision before releasing the transaction.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Input security gate | Filters harmful content and blocks dangerous prompts |
| AWS Lambda with a SQL parser | Query grammar reviewer | Checks the structure and syntax of generated SQL |
| Amazon RDS Data API | Database-rule verifier | Validates the query against the approved schema |
| Audit log pipeline | Bank stamp book | Records the transformation and decision before execution |

#### Correct answer and reasoning

1. Filter the natural-language prompt first so an injection attempt is stopped before it reaches generation or execution.
2. Parse the generated SQL to ensure it is structurally valid rather than sending an unchecked string to the database.
3. Validate tables, columns, and query shape against the predefined schema so valid syntax cannot become unauthorized access.
4. Record the transformation and validation outcome before execution. B covers every required control in the correct order.

#### Option-by-option elimination

- **A.** Incorrect. GuardDuty is not the specified natural-language SQL transformation control, and event storage cannot replace pre-execution schema validation.
- **B.** Correct. It provides input filtering, parser validation, schema validation, and an audit point before execution.
- **C.** CloudTrail and WAF help with API auditing and perimeter filtering, but they do not validate generated SQL against the schema.
- **D.** A custom model and network isolation may help elsewhere, but this design omits the required layered validation pipeline.

#### Exam strategy

For injection plus generated SQL plus schema plus audit requirements, look for an ordered pre-execution pipeline. Map Guardrails to harmful input, a parser to syntax, schema/database checks to authorization, and audit logging to traceability. A perimeter filter or API log alone is never the complete Text-to-SQL control.

---

## Question 106

### 中文

#### 考点背景

本题是多模态内容处理与协作存储的架构配对题。PDF、PowerPoint、Word 与录制视频需要不同的提取器：BDA/FM 负责自动理解和生成学习材料，Textract 适合 PDF 中的文字与布局提取，Transcribe 把视频中的语音变成可分析文本。500 个并发上传和每日万级输入要求组件可托管扩展，而不是把所有工作塞进单个自定义端点。

生成后的内容还必须能追溯版本并支持实时协作。S3 Versioning 是内容对象的历史底座，DynamoDB 保存状态和元数据，AppSync GraphQL Subscription 把变更实时推送给协作者。B 是唯一把提取、总结、持久化、版本和协作各层按职责接起来的方案。

#### 场景比喻

像大型出版社的数字化工厂：扫描员读 PDF，听写员处理视频，编辑部生成摘要，档案库保留每一版原稿，实时编辑台把变更同步给所有作者。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Data Automation / FMs | 内容编辑总线 | 批量理解文档并生成关键概念与摘要 |
| Amazon Textract / Transcribe | 文字扫描员与听写员 | 分别提取 PDF 内容与视频语音文本 |
| Amazon S3 Versioning | 版本档案库 | 持久保存处理后材料及其历史版本 |
| Amazon DynamoDB | 目录与状态账本 | 保存元数据、协作状态和索引信息 |
| AWS AppSync GraphQL subscriptions | 实时编辑广播台 | 向协作者推送材料更新 |

#### 正确答案与推理

1. 文档和视频是不同输入模态，BDA/FM 加 Textract 与 Transcribe 能分别提取可供总结的内容。
2. 托管服务组合能吸收万级日处理和 500 并发上传，不必自建一整套专用提取模型。
3. S3 Versioning 满足生成材料的历史版本保留；DynamoDB 适合管理可快速访问的元数据。
4. AppSync Subscription 提供实时变更传播，因此 B 同时满足内容准确生成、扩展、版本控制和协作。

#### 逐项排除

- **A.** 不正确；Knowledge Bases 不是“处理所有多媒体”的提取器，DocumentDB、SNS 和 Agents 也没有形成版本化实时编辑模型。
- **B.** 正确；各服务分别覆盖多模态提取、摘要、持久化、元数据、版本和实时协作。
- **C.** 不正确；Guardrails 是安全控制而非通用文件提取器，Neptune 的时间序列用途和 Bedrock Chat 协作也不匹配。
- **D.** 不正确；ElastiCache 是缓存而非长期权威存储，微调和 Prompt Management 不能替代多模态提取与实时协作。

#### 解题方法

先按输入模态配服务，再按数据生命周期配存储。PDF 看 Textract，视频语音看 Transcribe；长期版本看 S3 Versioning，实时订阅看 AppSync。遇到万级处理和并发上传，优先托管、可扩展组件，警惕缓存冒充持久库。

### English

#### Exam focus and background

This is a multimodal processing and collaboration architecture question. PDFs, presentations, Word files, and recorded video do not share the same extraction path. Bedrock Data Automation and foundation models can organize and summarize content; Textract is suited to extracting text and layout from PDFs, while Transcribe converts spoken video content into analyzable text. The daily volume and upload concurrency favor managed, scalable components.

The second half is a data-lifecycle test. Generated materials need durable version history and real-time collaboration. S3 Versioning preserves object revisions, DynamoDB can hold metadata and collaboration state, and AppSync GraphQL subscriptions can push changes to connected collaborators. Answer B is the only option that assigns each responsibility to a fitting service.

#### Analogy

Think of a publishing factory: a scanner reads PDFs, a stenographer processes video speech, editors create summaries, an archive keeps every manuscript version, and a live editing desk broadcasts changes to all authors.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Data Automation / FMs | Content-editing conveyor | Understands source material and produces concepts and summaries |
| Amazon Textract / Transcribe | Scanner and stenographer | Extracts PDF content and spoken video text |
| Amazon S3 Versioning | Versioned archive | Durably stores processed materials and their revisions |
| Amazon DynamoDB | Catalog and state ledger | Stores metadata and collaboration state |
| AWS AppSync GraphQL subscriptions | Live editorial broadcast | Pushes updates to collaborators in real time |

#### Correct answer and reasoning

1. The sources use different modalities, so B pairs BDA/FMs with Textract and Transcribe for appropriate extraction.
2. Managed services can scale across ten thousand daily sources and 500 concurrent uploads without a custom extraction platform.
3. S3 Versioning preserves historical material, while DynamoDB provides a fast metadata and state store.
4. AppSync subscriptions deliver live updates, so B covers extraction, summarization, scale, versioning, and collaboration together.

#### Option-by-option elimination

- **A.** Incorrect. A knowledge base is not a general multimedia extraction service, and DocumentDB, SNS, and agents do not create the required versioned collaboration model.
- **B.** Correct. It aligns modality-specific extraction, summarization, durable versioning, metadata, and live updates.
- **C.** Incorrect. Guardrails are safety controls, not universal file extractors, and Neptune plus Bedrock Chat do not fit the stated storage and collaboration needs.
- **D.** Incorrect. ElastiCache is a cache rather than authoritative durable storage, and fine-tuning or prompt management does not replace extraction and collaboration.

#### Exam strategy

Match services to modality and lifecycle. PDFs suggest Textract; spoken video suggests Transcribe; durable revisions suggest S3 Versioning; live collaboration suggests AppSync subscriptions. For high volume, prefer managed scalable building blocks and reject a cache presented as the system of record.

---

## Question 107

### 中文

#### 考点背景

本题考查医疗场景 PoC 的“时间、隐私、评估”三重约束。三周验证期意味着先做小而有代表性的样本，不应先微调、部署多个 Agent 或组织大规模用户测试。50～100 份匿名化记录既能减少暴露真实身份的风险，又足以建立可重复的基线；安全 Knowledge Base 加 RAG 可以快速把医疗文档作为受控上下文提供给模型。

题目还要求摘要准确率和处理时间指标，而不是泛泛的“用户觉得不错”。Judge Model 可用一致标准比较三个 FM 的输出，配合样本集与处理时长记录，就能形成可量化的 PoC 评估。A 以最少的建设工作同时回应隐私、速度和客观指标。

#### 场景比喻

像医院试用三台摘要打印机：先用去掉姓名的少量病例做封闭测试，让同一位评审按同一把尺子打分，并记录每张报告用时，而不是把真实病历交给全院五百人试用。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Bases | 受控病例资料室 | 存放可检索的医疗上下文并支持 RAG |
| RAG | 摘要员的参考夹 | 只把相关原文证据提供给生成过程 |
| Judge Model | 统一评分裁判 | 对多个 FM 的准确率进行一致比较 |
| Anonymized evaluation dataset | 去标识化试卷 | 在隐私边界内提供 50～100 份可重复测试样本 |

#### 正确答案与推理

1. 三周时间排除了重微调、复杂多 Agent 组装和大规模用户研究。
2. 匿名化样本降低隐私风险，安全 Knowledge Base 使材料有受控的检索边界。
3. RAG 提供摘要所需的原始依据，避免只凭模型记忆生成。
4. Judge Model 可跨三个 FM 采用一致评价，并记录准确率等指标；同时记录处理时间即可满足性能指标。A 最完整。

#### 逐项排除

- **A.** 正确；样本规模、匿名化、安全 RAG、跨模型 Judge 和指标评估共同符合全部约束。
- **B.** 微调、部署和 500 人测试周期长、暴露面大，而且只选一个 FM，难以完成横向比较。
- **C.** 使用真实患者记录违反隐私要求，定性反馈也不能替代可重复的准确率指标。
- **D.** 多 Agent 与多个知识库增加复杂度和周期，没有明确的统一准确率、处理时长评估设计。

#### 解题方法

PoC 题先圈出硬约束：短周期选托管能力和小样本，医疗隐私选匿名化，比较质量选 Judge/可量化指标。看到“真实数据、定性反馈、先微调、多人测试”通常是成本或合规陷阱。

### English

#### Exam focus and background

This question combines three constraints for a healthcare proof of concept: a three-week deadline, privacy protection, and measurable evaluation. A small but representative set of 50–100 anonymized records gives a repeatable baseline with less exposure than real identifiable records. A secure Bedrock knowledge base with RAG can provide controlled medical context quickly, without first building a fine-tuning and multi-agent platform.

The evaluation must be objective. A judge model can apply a consistent standard across three candidate FMs, while the test harness records processing time. That is materially different from collecting only stakeholder impressions. Answer A is the smallest coherent design that addresses privacy, rapid validation, comparative accuracy, and performance measurement.

#### Analogy

Imagine testing three hospital summarization machines in a closed lab. Use de-identified cases, have one impartial scorer use the same rubric, and record the time for each report. Do not hand real records to hundreds of users as the first experiment.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Knowledge Bases | Controlled medical reading room | Provides governed, searchable medical context |
| RAG | Reference folder for the summarizer | Supplies relevant source evidence to generation |
| Judge model | Consistent evaluation referee | Compares candidate FM accuracy using a common standard |
| Anonymized evaluation dataset | De-identified exam paper | Enables repeatable testing within the privacy boundary |

#### Correct answer and reasoning

1. Three weeks argues against fine-tuning, complex agent composition, and a large user study.
2. Anonymization reduces privacy exposure, and a secure knowledge base provides a governed context source.
3. RAG grounds summaries in relevant medical documents instead of relying only on model memory.
4. A judge model can compare all three FMs consistently; recording execution duration supplies the processing-time metric. A meets the full constraint set.

#### Option-by-option elimination

- **A.** Correct. It combines a small anonymized dataset, secure RAG, cross-model judging, and measurable evaluation.
- **B.** Fine-tuning, deployment, and testing 500 staff take longer, expose more data, and evaluate only one model.
- **C.** Real patient records violate the privacy constraint, and qualitative feedback is not a reliable accuracy metric.
- **D.** Multiple agents and knowledge bases add unnecessary build time and do not define a comparable accuracy and timing evaluation.

#### Exam strategy

For a short PoC, prioritize managed services and a small representative dataset. Map healthcare privacy to anonymization, comparative quality to a judge or quantitative rubric, and performance to recorded latency. Treat real-data testing, qualitative-only feedback, and premature fine-tuning as warning signs.

---

## Question 108

### 中文

#### 考点背景

本题测试 Amazon Bedrock Model Evaluation Job 的输入格式、角色分工和输出位置。对多个 FM 做统一质量与安全评估时，Sample Prompt 应组织为 JSONL 并放在 Amazon S3；每个待测 FM 是 Generator，负责产生响应，Judge Model 是 Evaluator，负责按评估标准判断。评估任务完成后把结果写回另一个 S3 位置，便于保存报告和历史比较。

容易混淆的地方是把 Knowledge Base 的 RAG 评估、模型自动评估和可视化工具混在一起。题目给的是一组 Prompt，要直接比较各 FM 的生成质量，不是先构建知识库；QuickSight 也不是 Evaluation Job 的原生输出位置。B 正确利用了托管评估作业的批处理与报告能力。

#### 场景比喻

像给三位候选厨师同一叠订单：每位厨师各自出菜，裁判按同一张评分表打分，工作人员把订单和评分表存入档案柜；不会把订单先当成厨房食材库，也不会让厨师自己给自己当裁判。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon S3 JSONL dataset | 统一考试卷 | 逐行保存可批量运行的 Sample Prompt |
| Bedrock Evaluation Job | 自动化评测考场 | 对每个候选 FM 执行标准化评估 |
| Generator FM | 应试厨师 | 根据 Prompt 生成待评价答案 |
| Judge Model | 盲评裁判 | 评估输出质量与安全性 |
| Amazon S3 output location | 成绩档案柜 | 保存评估报告和历史结果 |

#### 正确答案与推理

1. 将 Prompt 做成 JSONL，适合评估任务逐条读取，并存放在 S3 作为输入。
2. 创建使用 Judge Model 的 Model Evaluation Job，交给托管服务自动运行。
3. 对每个 FM 单独运行作业，并把该 FM 指定为 Generator；Judge Model 才是评估者。
4. 指定不同的 S3 输出位置，保留结果供 Data Scientist 查看和比较。B 的每个角色都正确。

#### 逐项排除

- **A.** 不正确；Knowledge Base 与 RetrieveAndGenerate 是检索生成链路，不是针对多个 FM 的直接模型评估作业。
- **B.** 正确；JSONL、S3 输入输出、Judge Model 和 Generator 角色完全匹配。
- **C.** 不正确；QuickSight 不是该评估作业的输出位置，且把待测 FM 设置成 Evaluator 颠倒了职责。
- **D.** 不正确；Retrieval and Response Generation Evaluation Type 面向 RAG 链路，JSON 文档和知识库也不符合直接比较。

#### 解题方法

看到“多模型、样本 Prompt、自动质量/安全报告”，优先 Model Evaluation Job。记住三点：JSONL 在 S3，待测 FM 是 Generator，Judge Model 是 Evaluator，结果也回到 S3；RAG 评估要有检索链路才成立。

### English

#### Exam focus and background

This question tests the mechanics of a Bedrock model evaluation job: dataset format, generator versus evaluator roles, and output storage. A set of sample prompts should be represented as JSONL in Amazon S3. Each candidate FM is the generator that produces responses, while a judge model evaluates quality and safety using the configured criteria. The job output belongs in an S3 location so reports can be retained and compared later.

The traps are mixing model evaluation with knowledge-base evaluation and with visualization. The prompt set is meant to compare direct FM generation, not a retrieval-and-generation application. QuickSight is not the evaluation job’s native result destination. Answer B uses the managed batch evaluation workflow and keeps every role in the correct place.

#### Analogy

Give three chefs the same stack of orders. Each chef cooks independently, a judge scores every dish with the same rubric, and the staff store the orders and scorecards in an archive. The chefs do not grade themselves.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon S3 JSONL dataset | Common examination paper | Stores one evaluation prompt per line |
| Bedrock evaluation job | Automated testing room | Runs standardized evaluations for candidate FMs |
| Generator FM | Contesting chef | Produces the response to each prompt |
| Judge model | Blind referee | Scores quality and safety |
| Amazon S3 output location | Results archive | Stores reports for review and historical comparison |

#### Correct answer and reasoning

1. Encode the sample prompts as JSONL and place the file in S3 for batch consumption.
2. Create a Bedrock model evaluation job with a judge model.
3. Run the job for each candidate FM and designate that candidate as the generator; the judge model remains the evaluator.
4. Use a separate S3 output location for the generated reports. B matches the required format, roles, automation, and persistence.

#### Option-by-option elimination

- **A.** Incorrect. A knowledge base and RetrieveAndGenerate describe a retrieval workflow, not direct comparative model evaluation.
- **B.** Correct. It has the proper JSONL/S3 input, judge model, generator role, and S3 output.
- **C.** Incorrect. QuickSight is not the evaluation job output location, and the candidate FM is incorrectly assigned as evaluator.
- **D.** Incorrect. Retrieval-and-response evaluation is for a RAG path and does not match direct prompt comparison with the described JSON input.

#### Exam strategy

For automated multi-FM quality and safety reports, recall the evaluation-job pattern: JSONL in S3, candidate FM as generator, judge model as evaluator, and results in S3. Choose a retrieval evaluation only when the stem explicitly evaluates retrieval plus response generation.

---

## Question 109

### 中文

#### 考点背景

本题要同时满足三个时间与数据边界：完整分析需要 15～20 秒，客户必须在 10 秒内看到响应开始，完整记录要留存 10 年，而且响应通常超过 4 MB。Response Streaming 把“开始有内容”与“全部生成完”分离，能在模型仍生成时向客户端发送片段；Lambda Timeout 必须覆盖最长的 20 秒，因此 30 秒比 15 秒安全。

完整响应的监管留存不能依靠 CloudWatch 日志或缓存。S3 适合持久化大对象并结合生命周期、保留和访问控制策略管理长期记录。D 把即时交互、执行时长和合规归档放在同一条可解释的路径上；其余选项至少缺少一个硬约束。

#### 场景比喻

像交易分析师写一份很长的报告：先把已经核实的段落逐段交给客户，后台继续写完全文；最终全文装入十年档案柜，而不是只保留聊天窗口里的一小段。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | 分析员工作台 | 运行 15～20 秒的生成任务 |
| Response streaming | 逐段交付窗口 | 在完整响应完成前开始返回内容 |
| Amazon S3 | 监管档案库 | 持久保存超过 4 MB 的完整响应并支持长期保留策略 |
| Lambda timeout | 工作台计时器 | 以 30 秒覆盖最长处理时间及余量 |

#### 正确答案与推理

1. 首字节要求是 10 秒内开始响应，不是 10 秒内完成全文，所以需要 Response Streaming。
2. 最长生成时间为 20 秒，15 秒 Timeout 会在最长请求完成前终止；30 秒能覆盖它。
3. 4 MB 以上的完整结果不应只放在短期日志或缓存中，应存入 S3。
4. S3 对象及相应保留策略能承接十年记录要求，因此 D 同时满足时延、执行和合规留存。

#### 逐项排除

- **A.** Streaming 方向正确，但 15 秒超时小于最长 20 秒，且没有十年完整记录的持久化方案。
- **B.** SQS 解耦适合后台任务，却不能直接向交互客户在 10 秒内开始返回完整分析，也没有合规归档。
- **C.** 轮询、缓存和日志增加复杂度，不能解决大响应的持久留存与流式首响应组合要求。
- **D.** 正确；Streaming、30 秒 Timeout 和 S3 归档分别覆盖三项硬约束。

#### 解题方法

把“开始响应”和“完成响应”分开读；前者看 streaming，后者看 timeout。看到大于 MB 的响应和多年留存，选择 S3 等持久对象存储，不要把 CloudWatch 或 ElastiCache 当监管档案库。

### English

#### Exam focus and background

This question has three separate constraints: generation takes 15–20 seconds, the customer must see a response start within 10 seconds, and the complete response must be retained for ten years. The response is also commonly larger than 4 MB. Streaming separates “the first content is available” from “the full answer is finished,” while the Lambda timeout must cover the maximum 20-second generation time. A 30-second timeout provides the required headroom.

Regulatory retention should not depend on a transient cache or ordinary application logs. Amazon S3 is appropriate for durable large-object storage and can be governed with retention and lifecycle controls. Option D is the only choice that combines early interactive delivery, sufficient execution time, and durable storage of the complete record.

#### Analogy

Imagine an analyst writing a long trading report. The analyst sends verified paragraphs as they are ready, continues writing in the background, and places the final report in a ten-year archive. The chat window is not the archive.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda | Analyst’s workbench | Runs the 15–20 second generation task |
| Response streaming | Paragraph delivery window | Sends content before the complete response is finished |
| Amazon S3 | Regulatory archive | Durably stores the large complete response for long retention |
| Lambda timeout | Workbench timer | Allows 30 seconds for the longest request and headroom |

#### Correct answer and reasoning

1. The ten-second requirement is time to begin responding, not time to finish the entire analysis, so streaming is needed.
2. The maximum generation time is 20 seconds; a 15-second timeout can terminate a valid request, while 30 seconds covers it.
3. A response larger than 4 MB should be persisted as a durable object rather than left only in logs or a cache.
4. S3 can retain the complete record under an appropriate retention policy. D meets latency, execution, and compliance requirements together.

#### Option-by-option elimination

- **A.** Streaming helps, but a 15-second timeout is shorter than the maximum generation time and there is no ten-year archive.
- **B.** SQS is useful for decoupled background work, but it does not provide the required immediate interactive start or compliant record storage.
- **C.** Polling, caching, and logs add moving parts without solving durable retention of the complete large response.
- **D.** Correct. Streaming, a 30-second timeout, and S3 storage map directly to the three hard constraints.

#### Exam strategy

Separate time to first output from time to completion: use streaming for the former and a sufficient timeout for the latter. For multi-year retention and multi-megabyte records, choose durable object storage with retention controls, not a cache or monitoring log.

---
## Question 110

### 中文

#### 考点背景

本题考查 Prompt 生命周期管理，而不是单纯把字符串放在某个存储桶里。大学有多个应用、多个 FM 和多个 Prompt Variant，需要可追溯版本、可复用变量、可测试差异以及低运维。Amazon Bedrock Prompt Management 把 Prompt 作为受管理资源，适合保存版本和参数化模板，让不同场景使用同一套可控定义。

Prompt 还要接入主应用并与模型和 AWS 服务组合。Bedrock Flows 提供可编排的工作流表达，能把 Prompt、FM 和服务连接起来。A 解决“Prompt 本身怎么管理”，C 解决“Prompt 如何一致地嵌入应用流程”；两者联合比自建 S3/Step Functions 维护链路更贴合最低运维目标。

#### 场景比喻

像大学出版社管理课程讲义：Prompt Management 是带版本号的模板库，Flows 是把讲义交给不同老师、评分器和资料系统的课程流程；模板改坏时可以定位版本，而不是在每个应用里寻找散落的复制品。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Prompt Management | 模板档案编辑部 | 管理 Prompt 版本、变量和可复用定义 |
| Prompt variants | 同一讲义的试验稿 | 比较不同写法在指定 FM/场景中的效果 |
| Amazon Bedrock Flows | 课程流程编排员 | 组合 Prompt、FM 与 AWS 服务并接入应用 |
| AWS Config / CloudTrail | 旁观记录员 | 可记录部分配置或 API 活动，但不是 Prompt 生命周期主系统 |

#### 正确答案与推理

1. “版本控制、参数化变量、跨应用一致复用”直接对应 Prompt Management，选择 A。
2. “Prompt 需要集成到主应用，并组合 FM 与服务”对应 Flows，选择 C。
3. 两者都属于 Bedrock 的托管能力，减少自建版本协议、调用编排和运维脚本。
4. 题干要的是 Variant 测试和可审计的一致使用路径；A 管模板，C 管执行组合，AC 覆盖面最完整。

#### 逐项排除

- **A.** 正确；版本化 Prompt、参数化变量和按 Use Case 复用正中核心要求。
- **B.** S3 可存文本、Step Functions 可编排，但版本语义、Variant 试验和 Prompt 复用都需要自行维护。
- **C.** 正确；Flows 将 Prompt、FM 和 AWS 服务组成可集成工作流，降低应用侧编排负担。
- **D.** Config 与 CloudTrail 更偏配置变更和 API 活动记录，不能原生管理 Prompt Variant 的测试与版本。
- **E.** Intelligent Prompt Routing 主要解决请求在模型间路由，不负责 Prompt 内容的版本、变体和一致复用。

#### 解题方法

看到 Prompt version、variables、variants、reuse，先选 Prompt Management；看到 Prompt + FM + AWS service workflow，再选 Flows。审计服务能记录活动不等于拥有 Prompt 生命周期能力，模型路由也不等于 Prompt 版本管理。

### English

#### Exam focus and background

This question is about prompt lifecycle management rather than simply storing prompt strings. The university has multiple applications, models, prompt variants, and use cases. It needs version control, reusable parameters, variant testing, consistency, and low operational effort. Amazon Bedrock Prompt Management treats prompts as managed resources with versions and variables, which is a better fit than a bucket full of files and custom conventions.

The prompts must also be integrated with the application and combined with models and AWS services. Bedrock Flows supplies a managed workflow representation for that composition. A manages the prompt definition; C manages how the prompt participates in a repeatable application flow. Together they address the content lifecycle and execution integration without requiring the university to build and maintain both concerns itself.

#### Analogy

Imagine a university publisher. Prompt Management is the numbered template library for lecture notes, while Flows is the course process that sends a selected note to teachers, graders, models, and supporting services. A broken template can be traced to a version instead of hunted through copied files.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Prompt Management | Template editorial office | Manages prompt versions, variables, and reusable definitions |
| Prompt variants | Experimental editions | Lets teams compare prompt forms for a use case |
| Amazon Bedrock Flows | Course-process coordinator | Combines prompts, FMs, and AWS services for application integration |
| AWS Config / CloudTrail | Activity recorder | May record configuration or API activity but is not the prompt lifecycle system |

#### Correct answer and reasoning

1. Version control, parameterized variables, reuse, and prompt variants point directly to Prompt Management, so A is required.
2. Integration of prompts with FMs and AWS services points to Bedrock Flows, so C supplies the workflow layer.
3. Both are managed Bedrock capabilities and reduce custom versioning, orchestration, and maintenance code.
4. A owns the prompt definition and C owns its composed execution path; AC covers the requirements with the least operational overhead.

#### Option-by-option elimination

- **A.** Correct. It provides versioned prompts, variables, and use-case reuse.
- **B.** S3 and Step Functions can be assembled into a solution, but variant testing, prompt semantics, and version conventions become custom maintenance.
- **C.** Correct. Flows combine prompts, models, and AWS services into an application-integrable workflow.
- **D.** Config and CloudTrail record configuration or API activity; they do not manage and test prompt variants as the primary capability.
- **E.** Intelligent prompt routing chooses among models; it does not manage prompt versions, variants, or consistent reuse.

#### Exam strategy

Map prompt versions, variables, variants, and reuse to Prompt Management. Map prompt-plus-model-plus-service composition to Flows. Do not confuse audit evidence with lifecycle management, or model routing with prompt version control.

---

## Question 111

### 中文

#### 考点背景

本题把短生命周期 Secret、Secret Chaining、Drift 和 Prompt Flow 版本放进同一个多 Agent 流程，考的是状态传播与可回滚编排。多个 Agent 可以并行处理彼此独立的解析工作，但后续验证必须看到最新的 Secret Metadata；如果 Lambda 只发告警而不把更新后的状态传下去，后续 Agent 仍可能沿用旧值。

Prompt Flow 的过时模板会造成级联故障，所以模板本身也要有版本和发布门禁。Step Functions Map State 适合并行分支，Lambda 输出可把刷新后的元数据交给下一状态，AppConfig 则提供版本化、Feature Gating 和 Rollback。A 正好同时修复并发执行、状态新鲜度和模板回退三个问题。

#### 场景比喻

像应急响应中心同时派三支小队查找钥匙：每队完成后把新钥匙编号写回交接单，下一队只认最新编号；新的行动手册先灰度启用，发现错误就切回上一版，而不是继续传阅旧手册。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Step Functions Map State | 并行调度台 | 并行运行独立 Agent Workflow 并保留编排边界 |
| AWS Lambda | 状态刷新交接员 | 获取/处理 Secret 后输出更新的 Metadata |
| AWS AppConfig | 手册发布控制室 | 为 Prompt Flow 提供版本、Feature Gating 和 Rollback |
| Key Store / HSM | 受保护钥匙柜 | 作为 Secret 的外部来源，需由流程解析和验证 |

#### 正确答案与推理

1. 多个独立 Agent 可并行，Map State 减少串行等待并让每个分支有清晰执行记录。
2. Secret 过期或漂移后，Lambda 必须把新版本/时间等 Metadata 作为输出传递给后续状态，避免只告警不更新。
3. Prompt Flow 需要版本化发布；AppConfig 的 Feature Gating 可控制启用范围，Rollback 可撤回坏模板。
4. A 同时解决 Agent 执行效率、最新 Secret 状态传播和 Prompt 级联故障，其他方案没有闭合这条链。

#### 逐项排除

- **A.** 正确；Map、Lambda 输出和 AppConfig 版本发布分别对应并行、状态新鲜度和模板回滚。
- **B.** 只依赖 AgentCore 和 Guardrails 没有解决过期 Secret 刷新、跨状态传递或 Prompt Flow 回滚。
- **C.** EventBridge 与 TTL 重试不能保证 Secret 已刷新，也不能把最新状态可靠地绑定到 Agent 后续步骤。
- **D.** 依赖日志触发和动态生成 Fallback Prompt，增加不可预测性；TTL/Versioned Write 也不等于模板发布回滚。

#### 解题方法

看到“并行 Agent + 后续依赖最新状态”，找 Map 与显式输出传递；看到“过期/漂移”要问状态是否真正更新，而非只发告警；看到“旧模板/级联故障”找版本、门禁和回滚能力。

### English

#### Exam focus and background

This scenario combines short-lived secrets, secret chaining, drift detection, multi-agent orchestration, and prompt-flow failures. The central issue is state freshness. Independent agent work can run in parallel, but downstream validation must receive the refreshed secret metadata. An alert by itself does not change the state consumed by later agents.

The prompt flow is a second versioned artifact. Reusing an old template can amplify one defect across several agents, so the flow needs controlled release and rollback. A Step Functions Map state supplies parallel orchestration, Lambda outputs can carry the refreshed metadata into later states, and AppConfig can version prompt flows and gate or roll back a faulty release. A is the only design that closes all three gaps.

#### Analogy

Picture an incident center sending three teams to find a key. Each team writes the newest key number on the handoff sheet, and the next team trusts that current sheet. A new response manual is released gradually and reverted when it causes errors; an alert alone does not replace the handoff.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Step Functions Map state | Parallel dispatch desk | Runs independent agent workflows concurrently with orchestration state |
| AWS Lambda | State-refresh handoff clerk | Processes secret results and outputs updated metadata |
| AWS AppConfig | Manual-release control room | Versions prompt flows and supports feature gating and rollback |
| Key stores / HSMs | Protected key cabinets | Supply secrets that the workflow must resolve and validate |

#### Correct answer and reasoning

1. Independent agent work can run in parallel, so a Map state reduces unnecessary serial waiting while keeping branch execution visible.
2. After expiry or drift, Lambda must pass the refreshed version or timing metadata to downstream states; an alert without state propagation leaves stale consumers.
3. Prompt flows need versioned release control. AppConfig feature gating limits exposure, and rollback removes a faulty template.
4. A addresses parallel execution, current secret state, and prompt rollback in one coherent workflow.

#### Option-by-option elimination

- **A.** Correct. Map, Lambda outputs, and AppConfig release controls map to parallelism, freshness, and prompt rollback.
- **B.** AgentCore and guardrails do not solve expired-secret refresh, cross-state propagation, or prompt-flow rollback.
- **C.** EventBridge, TTL, and retries do not guarantee that a refreshed secret is available to the next agent or that execution ordering is correct.
- **D.** Log-triggered resolvers and dynamically generated fallback prompts add unpredictability; TTL and versioned writes are not prompt release rollback.

#### Exam strategy

For parallel agents with dependent state, look for a Map state plus explicit output propagation. For expiry or drift, verify that state is refreshed, not merely observed. For stale templates, choose managed versioning, feature gating, and rollback rather than ad hoc retries or generated fallbacks.

---

## Question 112

### 中文

#### 考点背景

本题的关键词是 end-to-end visibility、Agent 与 Tool 行为、内置能力、无自定义 Instrumentation 和低开销。要定位 40% 的 Tool Failure，不能只看容器资源或单一 API 调用；需要把 Agent 何时决定调用工具、工具调用耗时/错误、节流以及不同团队的差异串成可追踪的执行链。

AgentCore Observability 与 Trace Collection 提供面向 Agent 的内置可观测性，X-Ray 可承接分布式 Trace，CloudWatch Dashboard 适合把 Error、Throttling 和 Latency 汇总成趋势。A 使用现成观测路径；C、D 额外处理日志，B 轮询指标，都会引入题目明确不希望承担的自定义工作或性能开销。

#### 场景比喻

像机场调查行李丢失：不是只看传送带服务器温度，而是给旅客、分拣节点和交接柜台串上同一张追踪单，再用控制室图表比较错误、拥堵和耗时。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AgentCore Observability / Trace Collection | Agent 运行记录仪 | 捕获 Agent 交互和工具调用的内置追踪 |
| AWS X-Ray | 跨站追踪单 | 串联自定义 Tool 的分布式调用路径 |
| Amazon CloudWatch Dashboards | 运控大屏 | 展示 Error、Throttling、Latency 等高峰指标 |

#### 正确答案与推理

1. 需要端到端链路，所以先启用 AgentCore Observability 和 Trace Collection，而不是只监控容器。
2. 自定义 Tool 的失败必须能在分布式路径中定位，X-Ray 负责把相关调用串起来。
3. 错误率、节流和延迟是题干要求的行为指标，CloudWatch Dashboard 提供集中趋势视图。
4. 这条方案使用内置采集与托管展示，不需要自行埋点、轮询或 ETL，因此性能和运维负担最低。答案为 A。

#### 逐项排除

- **A.** 正确；Agent 追踪、Tool 分布式追踪和 CloudWatch 指标正好覆盖端到端诊断。
- **B.** Container Insights 偏容器层，轮询 Bedrock API 不能替代 Agent/Tool 追踪，还会增加延迟和自定义代码。
- **C.** 自建 ETL、DynamoDB 和 QuickSight 明确增加数据处理运维，违反“内置、低开销”约束。
- **D.** 虽启用 Observability，但还需 Lambda 从日志提取指标和 Grafana 处理，仍有自定义处理开销。

#### 解题方法

看到 AgentCore、end-to-end、custom tools、built-in、no instrumentation，优先原生 Observability + Trace。区分“资源监控”“日志后处理”和“调用链追踪”；题目问工具行为时，单看容器或 API 轮询通常不够。

### English

#### Exam focus and background

The decisive clues are end-to-end visibility, agent interactions, tool behavior, built-in Bedrock capability, no custom instrumentation, and low overhead. A 40% tool failure rate cannot be diagnosed by looking only at container health or by polling one API. The team needs a trace that shows the agent decision, the tool invocation, latency, throttling, and error path so behavior can be compared across teams.

AgentCore Observability and trace collection provide the agent-focused built-in path. X-Ray can connect distributed calls involving custom tools, and CloudWatch dashboards can present errors, throttling, and latency during peaks. A uses those managed observation layers directly. B, C, and D add polling, ETL, log extraction, or alternate monitoring that increases custom operational work.

#### Analogy

Imagine an airport investigating lost luggage. The team does not inspect only the conveyor motor temperature; it follows one tracking record across the passenger desk, sorting nodes, and handoff counter, then compares errors, congestion, and elapsed time on a control-room screen.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AgentCore Observability / trace collection | Agent flight recorder | Captures agent interactions and tool-call traces |
| AWS X-Ray | Cross-station tracking sheet | Connects distributed calls through custom tools |
| Amazon CloudWatch dashboards | Operations wallboard | Visualizes errors, throttling, and latency at peak load |

#### Correct answer and reasoning

1. End-to-end visibility starts with AgentCore Observability and trace collection, not container-only monitoring.
2. Tool failures must be located within the distributed path, so X-Ray connects the relevant tool calls.
3. CloudWatch dashboards expose the required error, throttling, and latency trends.
4. The design uses built-in collection and managed visualization, avoiding custom instrumentation, polling, and ETL. A has the lowest added overhead.

#### Option-by-option elimination

- **A.** Correct. It combines agent traces, distributed tool traces, and the requested CloudWatch metrics.
- **B.** Container Insights is infrastructure-oriented, and API polling does not replace agent/tool traces while adding custom work.
- **C.** A custom ETL pipeline, DynamoDB, and QuickSight directly violate the built-in and low-overhead constraints.
- **D.** It enables observability but still requires a custom Lambda to parse logs and extract metrics, adding processing overhead.

#### Exam strategy

When the stem names AgentCore, end-to-end tool behavior, built-in capability, and no instrumentation, choose native observability and trace collection. Separate infrastructure monitoring, log post-processing, and distributed tracing; tool behavior usually requires the last of these.

---

## Question 113

### 中文

#### 考点背景

本题的根因是把 DynamoDB、S3 和 RDS 取出的全部原始资料一次塞进 Claude Sonnet：输入可能超过 Context Window，也会让模型在长上下文中遗漏重点，Lambda 还因等待过久而超时。解决方案必须既处理“数据总量很大”，又保留对全部客户资料的可用覆盖，而不是简单删掉不重要内容。

A 是分块与分阶段汇总的 Map-Reduce 思路：每个 Segment 独立处理，最后由一次综合调用整合结果。E 用 Knowledge Base 建立向量索引，把资料组织为可检索的语义片段，在当前客户上下文下只取最相关内容，避免无关 Token 进入窗口。两者分别解决超长输入和检索组织，正是 AE。

#### 场景比喻

像给顾问准备一份客户画像：先把厚厚的购买档案按年份分册并分别做摘要，再把与本次推荐最相关的分册从索引柜取出，最后写成一份完整建议，而不是硬塞整箱文件给顾问。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Bases | 客户资料索引库 | 组织客户数据并支持语义检索 |
| Embedding model / vector index | 语义目录 | 把资料片段映射为可按含义召回的索引 |
| Claude Sonnet | 推荐撰稿人 | 分段总结并最终综合生成推荐 |
| AWS Lambda | 编排工作台 | 管理分段调用与最终汇总，需避免超时 |

#### 正确答案与推理

1. Context length exceeded 说明不能继续把所有原始记录放在一次 Prompt 中。
2. A 先把数据分成 Segment，让每次调用在上下文上限内完成，再用最终调用综合各段，保留全部数据的处理覆盖。
3. E 通过 Knowledge Base 和 Embeddings 建索引，在每次请求只检索当前客户上下文相关内容，改善组织并减少无关 Token。
4. A 解决“如何完整处理长数据”，E 解决“如何高效找到相关数据”；合并后降低错误和超时，符合 AE。

#### 逐项排除

- **A.** 正确；分块、逐段处理和最终综合能在窗口限制内覆盖长购买历史。
- **B.** 把不重要资料截断会丢失客户数据，且不能满足“考虑所有数据”的目标。
- **C.** 更大窗口和更长 Timeout 可能缓解表面症状，却增加成本/延迟，也没有用 Knowledge Base 改善检索组织。
- **D.** Converse API 不会突破模型硬性 Context Window，参数也不能把最大窗口提高到不支持的范围。
- **E.** 正确；RAG 通过向量索引按当前上下文检索相关资料，减少直接注入的无关内容。

#### 解题方法

先识别 Context Window 溢出，再区分两种解法：所有内容都要处理，用 chunk + summarize；资料要组织和按需取回，用 Knowledge Base + RAG。不要把“换大模型”或“截断”误当作完整数据策略。

### English

#### Exam focus and background

The failure comes from placing all raw DynamoDB, S3, and RDS data into one Claude Sonnet request. Large purchase histories can exceed the context window, cause the model to miss important material in a long prompt, and keep Lambda waiting until it times out. The solution must preserve usable coverage of the customer data without simply deleting records.

A uses a chunk-and-synthesize pattern: divide the data into segments, process each segment within the context limit, and make a final call to combine the partial results. E uses a Bedrock knowledge base and vector embeddings to organize the data and retrieve the passages most relevant to the current recommendation context. A solves complete processing of oversized input; E solves organized, selective retrieval. Together they are the recorded answer AE.

#### Analogy

Imagine preparing a customer profile for an advisor. Divide a huge purchase archive into yearly binders and summarize each one, then pull the relevant binders from an indexed cabinet before writing the final recommendation. Do not drop the entire archive on the advisor’s desk.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Knowledge Bases | Customer-record index | Organizes customer data for semantic retrieval |
| Embedding model / vector index | Meaning-based catalog | Maps chunks to retrievable semantic representations |
| Claude Sonnet | Recommendation writer | Summarizes segments and synthesizes the final recommendation |
| AWS Lambda | Orchestration desk | Coordinates segmented calls and final synthesis without timing out |

#### Correct answer and reasoning

1. A context-length error means the raw corpus cannot continue to be placed in one prompt.
2. A keeps each segment within the limit and uses a final synthesis call, preserving processing coverage across the full history.
3. E indexes the customer data and retrieves only contextually relevant information, reducing irrelevant tokens and improving organization.
4. A handles complete processing of long input while E handles efficient retrieval. Their combination addresses the observed errors and the Knowledge Base requirement.

#### Option-by-option elimination

- **A.** Correct. Chunking, per-segment processing, and final synthesis handle an oversized history.
- **B.** Truncating supposedly less important information loses customer data and conflicts with the requirement to consider all data.
- **C.** A larger context and longer timeout may mask symptoms but add cost and latency without improving knowledge-base retrieval.
- **D.** Converse does not raise a model’s hard context limit beyond its supported capacity through request fields.
- **E.** Correct. RAG uses a vector index to retrieve relevant customer information for the current context.

#### Exam strategy

First identify context overflow. If the whole corpus must be processed, use chunking plus synthesis; if the corpus must be organized and selectively supplied, use a knowledge base with RAG. Treat truncation and “just choose a bigger model” as incomplete fixes when the stem requires full-data coverage.

---

## Question 114

### 中文

#### 考点背景

本题要求同一系统处理两种路由：文件扩展名已经足以决定模型时，额外分析只会增加延迟；内容语义不明显时，必须先分析再选专用 FM。因此最优架构不是把所有请求都送进 Lambda 或 SQS，而是把低成本、低延迟的确定性路径留在应用代码，把复杂路径交给可观察、可重试、可分支的工作流。

B 的混合设计正好做到这一点：应用代码处理简单扩展名，Step Functions 处理复杂内容路由，JSONata 进行数据分析，InvokeModel 调用选中的模型。Step Functions 执行历史可以保留决策过程，并可用 Retry/Catch 或分支实现主 FM 失败后的 Fallback，满足可追溯与韧性要求。

#### 场景比喻

像快递分拣：看到清晰的颜色标签就走最短传送带；标签模糊的包裹送到质检台分析内容，再交给专门线路。每个复杂包裹都有扫描记录，主线路堵塞时按预案转备用线路。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Application code | 快速标签分拣员 | 依据文件扩展名做最低延迟路由 |
| AWS Step Functions | 复杂包裹调度台 | 编排语义分析、模型选择、重试和 Fallback |
| JSONata | 内容检查员 | 在工作流中分析输入并产生路由所需数据 |
| Amazon Bedrock InvokeModel | 专线出站口 | 调用最终选中的专用 Foundation Model |

#### 正确答案与推理

1. 扩展名足够时，应用代码直接决定目标 FM，避免为简单请求启动额外工作流。
2. 语义路由需要先分析内容，Step Functions 以 JSONata 完成数据处理，再进入模型选择。
3. InvokeModel 负责调用专用 FM，工作流状态保留每一步输入、输出和决定，提供详细历史。
4. Step Functions 的分支、Retry/Catch 可在主模型失败时转备用模型；B 同时满足延迟、语义分析、历史和 Fallback。

#### 逐项排除

- **A.** 全部逻辑塞进 Lambda，增加自定义代码和维护责任，也没有自然的托管执行历史与 Fallback 编排。
- **B.** 正确；简单路径快，复杂路径有 JSONata 分析、InvokeModel、历史和可编排回退。
- **C.** 每种内容拆一个 Workflow 并用 EventBridge 协调，重复且复杂；Lambda 仍承担主要路由逻辑。
- **D.** SQS 引入异步排队延迟，不能自然满足复杂语义路由的同步分析、详细执行历史和工作流回退。

#### 解题方法

先按路由复杂度分层：确定性低延迟逻辑留在应用端，语义分析和 Fallback 交给 Step Functions。看到 execution history、Retry/Catch、branch、content analysis 等词，优先工作流；不要为简单扩展名路由强行引入队列。

### English

#### Exam focus and background

The application has two routing paths. If a file extension is sufficient to choose the model, extra analysis only adds latency. If the content is semantically ambiguous, analysis must happen before selecting a specialized FM. The best design therefore separates a fast deterministic path from a managed workflow path that can record decisions, retry failures, and fall back.

Answer B does exactly that. Application code handles extension-based routing directly. Step Functions handles complex content-based routing, JSONata analyzes the workflow data, and InvokeModel calls the selected FM. Step Functions execution history records the decisions and results. Its branching and Retry/Catch controls can send a failed primary invocation to an alternate model, satisfying the history and fallback requirements without splitting the system into many loosely coordinated workflows.

#### Analogy

Think of parcel sorting. A clearly labeled package takes the shortest belt. An ambiguous package goes to an inspection desk, is routed to a specialist belt, and keeps a scan history. If the main belt fails, the operating plan diverts it to a backup belt.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Application code | Fast label sorter | Routes unambiguous extensions with minimal latency |
| AWS Step Functions | Complex-parcel control desk | Orchestrates analysis, choice, retry, and fallback |
| JSONata | Content inspection clerk | Transforms and analyzes workflow data for routing |
| Amazon Bedrock InvokeModel | Specialist dispatch gate | Invokes the selected specialized foundation model |

#### Correct answer and reasoning

1. When the extension is enough, application code chooses the FM directly and avoids workflow overhead.
2. Semantic routing needs analysis first, so Step Functions runs JSONata processing before the model decision.
3. InvokeModel calls the specialized FM, while the state machine records inputs, outputs, and decisions.
4. Branching and Retry/Catch can route a failed primary model to a fallback. B meets latency, analysis, history, and resilience together.

#### Option-by-option elimination

- **A.** Putting all logic in Lambda increases custom code and does not naturally supply managed execution history and fallback orchestration.
- **B.** Correct. It preserves the fast path and uses Step Functions for semantic analysis, model invocation, history, and fallback.
- **C.** Separate workflows per content type plus EventBridge coordination duplicate logic and add unnecessary complexity.
- **D.** SQS introduces asynchronous queueing and does not naturally provide synchronous semantic analysis, detailed execution history, and workflow fallback.

#### Exam strategy

Partition by routing complexity: keep deterministic, latency-sensitive decisions in application code and use Step Functions for semantic analysis and fallback. Keywords such as execution history, branches, Retry/Catch, and content analysis favor a workflow. Do not add a queue to a path whose key requirement is immediate semantic routing.

---

## Question 115

### 中文

#### 考点背景

本题问的是“最低运维复杂度”的模型升级验证。500 个固定 Test Case 需要对比新旧 FM 的响应含义，输出量化 Similarity Score，自动完成验证，并把详细结果留作历史基线。这不是普通的健康检查，也不是训练一个异常分类器；核心是批量、可重复、由托管评估能力执行的模型比较。

Amazon Bedrock Model Evaluation Job 将测试集、Generator 和评估指标纳入作业，题设把 Semantic Similarity Metric 作为检测 Semantic Drift 的评估项，并把结果写入 S3。这样不需要自行编排两次调用、实现嵌入相似度、维护结果表或人工逐条审阅。C 以最少自建组件覆盖规模化比较和历史留存。

#### 场景比喻

像汽车厂换发动机：把同一批 500 条测试路线交给旧车和新车，自动测量路线表现并把成绩单归档；不需要工程师手写每次试驾脚本，也不用另训一位“异常猜测员”。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Model Evaluation Job | 自动试车台 | 批量运行新旧 FM 对比评估 |
| Semantic similarity metric | 语义测量仪 | 量化两版输出含义变化 |
| Judge/evaluation capability | 统一评审员 | 按配置好的标准自动完成验证 |
| Amazon S3 | 历史成绩档案 | 保存详细结果，供后续基线比较 |

#### 正确答案与推理

1. 500 个 Test Case 需要批量作业，不适合人工逐条审阅。
2. 目标是语义漂移，因此评估必须产出语义相似度等量化指标，而不是只看可用性。
3. Model Evaluation Job 托管测试执行、模型比较和评估结果生成，降低自定义编排。
4. S3 保存详细结果形成可追溯历史；因此 C 同时满足自动化、评分、规模和最低运维。

#### 逐项排除

- **A.** Canary 与自定义 JavaScript 需要维护，人工审核不能自动生成量化语义分数，且不符合最低运维。
- **B.** 可实现比较，但要自行编排调用、计算 Embedding 相似度和维护 DynamoDB，运维面更大。
- **C.** 正确；托管 Evaluation Job、Semantic Similarity 和 S3 历史结果一一对应题干。
- **D.** 训练分类器需要数据、训练和维护，异常检测也不等同于直接比较新旧响应语义。

#### 解题方法

看到固定测试集、模型版本对比、语义漂移、自动分数、历史报告，优先 Bedrock Model Evaluation Job。题目强调 least operational complexity 时，优先原生批处理和 S3 输出，淘汰自定义编排、人工审阅和另训模型。

### English

#### Exam focus and background

The question asks for the least operationally complex validation system for a model upgrade. There are 500 fixed test cases, and the team needs to compare the meaning of old and new responses, produce quantitative similarity scores, automate the validation, and retain detailed results as a historical baseline. This is a managed batch model-comparison problem, not a simple health check or a classifier-training project.

An Amazon Bedrock model evaluation job brings the test set, candidate generator, judge/evaluation criteria, and result generation into one managed job. In the question’s framing, a semantic similarity metric detects drift, and S3 stores the detailed output. That avoids custom dual invocation orchestration, hand-built similarity computation, result-table maintenance, and manual review. C covers the requirements with the fewest operational components.

#### Analogy

Imagine an automobile factory replacing an engine. Give the old and new cars the same 500 test routes, automatically measure the results, and archive the scorecards. Engineers do not hand-script every drive or train a separate anomaly guesser.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock model evaluation job | Automated test track | Runs batch comparisons between FM versions |
| Semantic similarity metric | Meaning gauge | Quantifies changes in response meaning |
| Judge/evaluation capability | Standardized inspector | Applies the configured validation criteria automatically |
| Amazon S3 | Historical score archive | Stores detailed results for later baseline comparison |

#### Correct answer and reasoning

1. Five hundred test cases call for a batch job rather than manual inspection.
2. The target is semantic drift, so the evaluation must produce a quantitative semantic-similarity result, not only availability data.
3. A model evaluation job manages test execution, model comparison, and evaluation output with less custom orchestration.
4. S3 preserves detailed results for history. C therefore covers automation, scoring, scale, and low operations.

#### Option-by-option elimination

- **A.** Custom canary code and manual review require maintenance and do not automatically produce quantitative semantic scores.
- **B.** It could work, but custom orchestration, embedding calculations, and DynamoDB maintenance create more operational burden.
- **C.** Correct. The managed evaluation job, semantic-similarity metric, and S3 results align with every requirement.
- **D.** A trained classifier requires data and lifecycle maintenance, and anomaly detection is not direct semantic comparison of two FM versions.

#### Exam strategy

For a fixed test set, model-version comparison, semantic drift, automatic scores, and historical reports, recall Bedrock model evaluation jobs. When the stem says least operational complexity, favor the native batch workflow and S3 output over custom orchestration, manual review, or a newly trained model.

---

## Question 116

### 中文

#### 考点背景

本题考查“先便宜快速分类，低置信度再升级”的异步解耦模式。高置信度路径需要尽快返回，低置信度路径可以排队；突发流量不能直接压垮推理组件；大模型成本高，不能对所有请求无条件调用。因此队列必须位于组件之间，先由小模型筛选，只有低于 0.65 的消息才进入第二条处理链。

C 用 SQS 吸收峰值并解耦生产者和消费者，Fargate 作为可扩展消费者先调用小模型；分数低于阈值时把消息送入第二个 SQS Queue，再异步调用大模型。它把高成本动作变成条件分支，也使低置信度任务可以独立重试和扩容，最符合韧性与成本要求。

#### 场景比喻

像机场安检：大多数行李先过快速扫描，明确安全的立即放行；可疑行李进入第二条人工复检队列。客流暴增时队列吸收拥堵，复检员不会为每件行李都出场。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon SQS | 缓冲与交接队列 | 吸收突发流量并解耦两个处理阶段 |
| AWS Fargate | 弹性分拣班组 | 消费消息并先调用小模型 |
| Small/large Bedrock models | 初筛员与专家复核员 | 先低成本分类，低置信度才升级 |
| Second SQS queue | 复检预约台 | 保存需异步升级的大模型任务 |

#### 正确答案与推理

1. 入口 SQS 把请求与计算解耦，峰值时先缓冲，消费者可按积压扩展。
2. Fargate 取消息调用小模型，高置信度结果走快速完成路径。
3. 只有 Confidence Score < 0.65 才进入第二队列，大模型不会被每个请求调用。
4. 第二队列独立承载异步升级、重试和扩容；C 同时满足近实时、高峰韧性、成本和解耦要求。

#### 逐项排除

- **A.** 小模型后的低置信度请求仍同步等待大模型，违背异步升级和按需解耦；Provisioned Concurrency 也不能改变流程。
- **B.** 每个请求并行调用两种模型，浪费大模型成本，也没有“只有低置信度才升级”。
- **C.** 正确；两级 SQS 与 Fargate 形成按置信度升级的弹性异步流水线。
- **D.** 自定义 Phrase/Keyword Heuristic 不是题设的 Confidence Score，EC2 方案也没有所需的队列解耦。

#### 解题方法

把阈值当作路由条件：先小模型，低于阈值才进入第二队列和大模型。看到 sudden spikes、decoupled、async、cost optimization，优先 SQS + 可扩展消费者；看到“每次并行调用”或“同步升级”要警惕成本和时序错误。

### English

#### Exam focus and background

This is a confidence-based escalation pattern: use a fast, inexpensive classifier first, and send only low-confidence cases to an expensive model. High-confidence work should finish quickly, low-confidence work may be queued, and sudden spikes must not overwhelm the inference workers. The queue must sit between components so each stage can scale and retry independently.

Option C uses SQS to absorb bursts and decouple producers from consumers. Fargate consumes the first queue and invokes the small model. Only a score below 0.65 is placed on a second queue, where the larger model processes it asynchronously. That makes the expensive call a conditional branch rather than a default cost, while preserving a resilient path for backlog and retries.

#### Analogy

Think of airport security. Most bags receive a quick scan and are released; suspicious bags enter a separate manual-review line. A surge is absorbed by queues, and the expensive review team is not assigned to every bag.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon SQS | Buffer and handoff queue | Absorbs spikes and decouples processing stages |
| AWS Fargate | Elastic sorting crew | Consumes messages and invokes the small model first |
| Small/large Bedrock models | Screener and expert reviewer | Classifies cheaply, then escalates only when confidence is low |
| Second SQS queue | Review appointment line | Holds cases for asynchronous large-model processing |

#### Correct answer and reasoning

1. The first SQS queue decouples arrivals from compute and absorbs a sudden backlog.
2. Fargate consumes the messages and invokes the small model; high-confidence cases can complete on the fast path.
3. Only scores below 0.65 enter the second queue, so the expensive model is not invoked for every request.
4. The second queue supports independent asynchronous scaling and retries. C satisfies responsiveness, resilience, conditional cost, and decoupling.

#### Option-by-option elimination

- **A.** Low-confidence requests still wait synchronously for the large model, violating the asynchronous escalation requirement.
- **B.** Running both models for every request wastes the expensive model and removes conditional escalation.
- **C.** Correct. Two SQS stages and Fargate create an elastic confidence-based asynchronous pipeline.
- **D.** Phrase and keyword heuristics do not route on the required confidence score, and EC2 does not provide the requested queue decoupling.

#### Exam strategy

Treat the confidence threshold as a routing condition: small model first, second queue and large model only below the threshold. Keywords such as sudden spikes, decoupled, asynchronous, and cost optimization favor SQS plus scalable consumers. Reject parallel-every-time and synchronous escalation designs.

---

## Question 117

### 中文

#### 考点背景

本题考查 Guardrails 在推理链路中的三道安全闸门，以及如何用 IAM 把“每一次都必须执行”变成强制约束。输入要在模型调用前经过 ApplyGuardrail，才能阻止有害 Prompt；流式输出不能等完整响应结束后才治理，必须在 InvokeModelWithResponseStream 路径上附加 Guardrail；被标记案例还要离开正常回答路径，进入人工审核队列。

A 的最后一层很关键：流程编排解决“设计上有这些步骤”，bedrock:GuardrailIdentifier IAM Condition 解决“调用者不能绕过 Guardrail”。因此 A 同时覆盖 Pre-Check、In-Stream Filtering、Post-Check、人工路由和强制执行。UI 隐藏 Token 或 Code Review 都无法对每次服务调用形成可靠的后端控制。

#### 场景比喻

像直播电台：节目稿在播出前先过审，直播信号经过实时延迟审查器，违规片段被截取交给编辑组；门禁系统要求每个播音间都接入审查器，不能只靠主持人口头承诺。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock ApplyGuardrail | 播出前/事后审查台 | 检查输入并对被标记输出做后续判定 |
| InvokeModelWithResponseStream + Guardrail | 实时延迟审查器 | 在 Token 流出时进行 In-Stream Filtering |
| AWS Step Functions | 安全节目导播台 | 编排 Pre-Check、推理、Post-Check 和异常分支 |
| Amazon SQS | 人工审核收件箱 | 接收被标记案例，等待人工处理 |
| IAM bedrock:GuardrailIdentifier condition | 强制接线门禁 | 要求调用使用指定 Guardrail，防止绕过 |

#### 正确答案与推理

1. 先用 Lambda 调 ApplyGuardrail 检查输入；命中有害规则就停止，未命中才进入模型。
2. 使用附加 Guardrail 的 InvokeModelWithResponseStream，让输出在流式传输阶段就被过滤，而不是交给 UI 补救。
3. 对输出/标记案例执行 Post-Check，并把需要人工判断的项目送入 SQS。
4. 用 bedrock:GuardrailIdentifier IAM Condition 强制调用方带上 Guardrail；A 因而满足“每一次推理”而不仅是默认路径。

#### 逐项排除

- **A.** 正确；前置拦截、流式过滤、后置检查、SQS 人审和 IAM Enforcement 全部齐备。
- **B.** InvokeModel 不支持题设所需的 Streaming；把问题 Token 藏在 UI 里也不能阻止其生成或传播。
- **C.** 缺少 Pre-Inference Check，恶意 Prompt 可能已经进入模型，不能满足调用前阻止。
- **D.** 虽有检查步骤，却把强制执行交给 Code Review；开发流程不能保证每次 API 调用都附加 Guardrail。

#### 解题方法

把安全要求按时序画成 Pre → In-Stream → Post → Human Review，再检查是否有 IAM 强制。看到 every inference、streaming、block before invocation、human review，必须同时寻找 ApplyGuardrail、带 Guardrail 的流式 API、SQS 和 IAM Condition。

### English

#### Exam focus and background

This question tests three guardrail points in an inference path and the difference between a designed control and an enforced control. ApplyGuardrail must inspect the prompt before model invocation so a harmful request can be blocked. Streamed output must be filtered on the InvokeModelWithResponseStream path while tokens are being delivered. Flagged cases must leave the normal response path and go to human review.

The IAM condition is the decisive final control. A workflow can contain the right steps, but a caller might still invoke a model through another path. A bedrock:GuardrailIdentifier condition makes guardrail use a permission requirement. A therefore combines pre-check, in-stream filtering, post-check, SQS routing, and enforcement for every inference.

#### Analogy

Imagine a live radio station. The script is reviewed before broadcast, the live signal passes a real-time censor, and flagged clips go to editors. The access system requires every studio to be wired through the censor; a host’s promise is not enough.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock ApplyGuardrail | Pre/post broadcast review desk | Checks inputs and performs follow-up checks on flagged output |
| InvokeModelWithResponseStream + guardrail | Live signal censor | Filters output during streaming |
| AWS Step Functions | Security-director console | Orchestrates pre-check, inference, post-check, and exception paths |
| Amazon SQS | Human-review inbox | Holds flagged cases for reviewers |
| IAM bedrock:GuardrailIdentifier condition | Mandatory wiring lock | Enforces use of the specified guardrail |

#### Correct answer and reasoning

1. A Lambda step calls ApplyGuardrail on the input. A harmful result stops before model invocation.
2. The streaming invocation attaches the guardrail so output is filtered as tokens are emitted, not hidden later by the UI.
3. A post-check handles flagged output, and SQS routes cases requiring human review.
4. The bedrock:GuardrailIdentifier IAM condition prevents callers from bypassing the required guardrail. A satisfies the “every inference” requirement, not just the normal workflow.

#### Option-by-option elimination

- **A.** Correct. It includes pre-inference blocking, in-stream filtering, post-check handling, SQS review routing, and IAM enforcement.
- **B.** Non-streaming InvokeModel fails the real-time filtering requirement, and UI token hiding is not a backend safety control.
- **C.** It omits the pre-inference check, so a harmful prompt can reach the model before any output check occurs.
- **D.** Code review is a process safeguard, not an authorization control that guarantees every API call carries the guardrail.

#### Exam strategy

Trace the safety sequence as Pre → In-Stream → Post → Human Review, then ask whether IAM enforces it. For “every inference,” “streaming,” “block before invocation,” and “human review,” look for ApplyGuardrail, a guarded streaming API, SQS, and the GuardrailIdentifier condition together.

---
