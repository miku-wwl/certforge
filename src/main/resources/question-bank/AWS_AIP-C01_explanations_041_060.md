# AWS AIP-C01 Questions 41–60：深度双语解析

## Question 41

### 中文

#### 考点背景

本题考查 Amazon Q Developer 如何把企业私有知识纳入代码建议，以及“集中治理”与“项目本地规则”的边界。内部 Library、专有算法和已批准的示例代码属于组织知识资产；目标是让新团队持续得到基于这些资料的建议，而不是把一份规则文件复制到每个 Java 项目。Amazon Q Developer Customization 以批准的数据源创建可供 Q Developer 使用的定制化知识，让团队在开发时选择并使用同一套企业标准。

关键线索是“不希望进行 Project-Level 修改”。只要方案要求把仓库、规则目录或指导文件放进应用工作区，它就把治理责任重新压到每个项目，难以保证版本一致。Customization 才把数据源和开发体验解耦：企业可以集中维护批准资料，团队按要求使用该 customization，而不必改动每个项目的结构。

#### 场景比喻

把公司想成连锁餐厅：批准的 Library 和算法是总部认证的配方。`@workspace` 像厨师把某一本配方册放进当前厨房；每换一家门店都要再放一次。Customization 像总部发布一套“认证厨房系统”，所有指定门店直接使用同一套配方和摆盘标准，项目本身不用加隐藏抽屉。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Q Developer Customization | 总部认证的知识系统 | 纳入批准数据源，为代码建议提供企业私有上下文 |
| 批准的数据源/代码仓库 | 认证配方库 | 保存内部库、算法和规范示例，作为 customization 的知识来源 |
| `@workspace` Context | 当前厨房的手册 | 读取本地工作区上下文，但需要项目级纳入，不能替代集中 customization |

#### 正确答案与推理

1. 需求核心不是普通代码补全，而是让建议遵循组织批准的私有资源和风格。
2. 资源需要跨项目复用，并且不能通过每个项目的目录或配置来落实。
3. Amazon Q Developer Customization 正好把批准数据源用于定制开发体验；团队使用该 customization 即可获得相关建议。
4. 因此记录答案 `D` 同时满足知识来源、集中管理和无项目级修改三个约束。

#### 逐项排除

- **A.** 让仓库进入本地 Workspace 后确实能提供上下文，但每个应用都要修改工作区，并且依赖开发者主动使用 `@workspace`，不符合集中、无项目改动的要求。
- **B.** `.amazonq/rules` 放在项目根目录，治理内容仍随项目复制和维护；它不是本题所需的集中批准数据源 customization。
- **C.** 普通 `rules` 文件夹只是约定俗成的项目资料，不能自动成为 Amazon Q Developer 的组织级定制知识。
- **D.** 将批准数据源纳入 Amazon Q Developer Customization，再要求团队使用它，能把组织知识与具体项目分离，正确。

#### 解题方法

看到“内部代码/专有算法/统一风格 + 多项目复用 + 最少项目改动”，优先找 Q Developer Customization。看到 `@workspace`、规则文件夹或项目内目录，要问：它是否仍需改每个项目？若答案是“是”，就不是集中治理方案。

### English

#### Exam focus and background

This question tests the boundary between Amazon Q Developer’s project context and centrally managed private development knowledge. Internal libraries, proprietary algorithms, and approved examples are enterprise assets. The requirement is not merely to make one Java workspace aware of a repository; it is to make a new team use an approved source without changing every application project.

Amazon Q Developer Customization is the matching mechanism. The approved data sources can be incorporated into a customization that the team uses while developing. That separates the knowledge source from the project layout and gives the organization one governed place to maintain its internal coding guidance. The decisive phrase is “no project-level modifications.” A repository mounted in a workspace, a `.amazonq/rules` directory, or a normal `rules` directory all require project-local adoption and maintenance.

#### Analogy

Imagine a restaurant chain. The internal libraries and algorithms are headquarters’ certified recipes. `@workspace` is a cookbook a chef places in one kitchen; every new restaurant must receive its own copy. A Q Developer Customization is the chain-wide certified kitchen system: designated restaurants use the same approved recipes without remodeling each kitchen.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Q Developer Customization | Certified kitchen system | Uses approved sources to guide code suggestions |
| Approved repositories/data sources | Headquarters recipe library | Supplies internal libraries, algorithms, and style examples |
| `@workspace` context | Local cookbook | Supplies workspace context but requires project-level inclusion |

#### Correct answer and reasoning

1. The approved private resources must influence Q Developer suggestions.
2. The approach must be reusable across projects and must not require project-specific files.
3. A Q Developer Customization incorporates the approved data sources and is used by the development team.
4. Therefore, `D` satisfies the governance, reuse, and project-change constraints.

#### Option-by-option elimination

- **A.** A local workspace repository can provide context, but adding it to each application is still a project-level change and depends on developers invoking `@workspace`.
- **B.** A `.amazonq/rules` directory is maintained inside each project, so it does not provide the requested centrally managed customization.
- **C.** A normal `rules` directory is only project-local material; it is not an Amazon Q Developer customization backed by approved enterprise sources.
- **D.** A Q Developer Customization with the approved data sources is the intended reusable, centrally managed solution.

#### Exam strategy

For “private code knowledge + multiple projects + no project changes,” map the clue to Q Developer Customization. Treat workspace context and local rule folders as scoped developer aids. The question is testing the management boundary, not whether a folder can contain useful code.

---

## Question 42

### 中文

#### 考点背景

本题把两个性能问题叠在一起：高峰期要低延迟，且 40% 的咨询共享相似上下文。Latency-Optimized Inference 针对调用路径的响应速度；Prompt Caching 则让可复用的提示上下文不必在每次请求中从头处理。二者分别打击“单次调用慢”和“重复上下文浪费”，因此比为全天峰值预购容量更贴合“最具成本效益”。

这里的判断重点是容量采购和缓存层的成本边界。Provisioned Throughput 可以提供预留吞吐，但若峰值只是偶发，按 150,000 条/天长期购买会产生闲置成本。一个外部 Redis 或 DynamoDB 缓存还需要设计键、失效和相似请求处理；题干明确的是共享 prompt context，原生 Prompt Caching 更直接。Agents、Knowledge Base 或自定义路由会增加组件，却没有解决核心重复上下文问题。

#### 场景比喻

像促销日的客服柜台：Latency-Optimized Inference 是给窗口开一条更快的通道；Prompt Caching 是把“活动规则和退换货说明”放在柜台前，连续遇到相似问题时直接复用，不必每位顾客都重新搬出同一本厚手册。为偶尔的周末高峰永久租下整座大厅，反而浪费。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock latency-optimized inference | 快速服务窗口 | 选择低延迟的推理路径，帮助满足峰值响应目标 |
| Amazon Bedrock Prompt Caching | 柜台上的共享手册 | 复用重复咨询中的共同 prompt context，减少重复处理 |
| Provisioned Throughput/MU | 预租的固定柜台 | 能提供容量，但按偶发峰值配置会带来较高持续成本 |
| Agents/Knowledge Bases | 额外的咨询部门 | 可做编排或检索，但不是本题重复上下文的最短路径 |

#### 正确答案与推理

1. 每天 50,000 条、促销峰值 150,000 条说明负载有明显波动，不能只按峰值购买固定容量。
2. 40% 请求共享上下文，说明存在高复用率的 prompt 部分。
3. 对调用启用 latency-optimized inference，直接针对响应时间；对共享上下文启用 Prompt Caching，减少重复处理。
4. 这两个托管能力覆盖低延迟与成本控制，新增自定义编排最少，所以记录答案为 `A`。

#### 逐项排除

- **A.** 同时处理延迟路径和重复上下文，是现有信息下最直接、最少运维且成本效益最佳的组合，正确。
- **B.** Provisioned Throughput 按峰值长期预留，偶发高峰下成本高；Redis 还要维护缓存键和失效逻辑。
- **C.** Agents、Knowledge Base 和跨区域推理会引入不必要的编排与数据准备，题干没有知识检索需求。
- **D.** 自定义 prompt routing、Lambda 和 DynamoDB 缓存都需自行设计和维护，运维成本高于原生能力。

#### 解题方法

先把需求拆成“延迟”与“重复工作”两列：Latency-Optimized Inference 对前者，Prompt Caching 对后者。再看“偶发峰值 + 最具成本效益”，通常排除按峰值长期购买 Provisioned Throughput；若没有检索需求，也不要被 Agents/Knowledge Base 带偏。

### English

#### Exam focus and background

Two different bottlenecks are embedded in this scenario. Latency-optimized inference addresses the response-time path for Bedrock requests. Prompt caching addresses the fact that 40% of the inquiries share context, so the reusable prompt portion does not have to be processed from scratch for every inquiry. Together they target latency and repeated work with managed capabilities.

The cost clue matters. Provisioned Throughput can reserve capacity, but sizing it for an occasional promotional peak can leave expensive capacity idle on normal days. An external Redis or DynamoDB cache would also require cache keys, expiration, and matching logic. The scenario already describes reusable prompt context, which is the direct use case for prompt caching. Agents, knowledge bases, and custom routing add components without addressing the stated bottleneck.

#### Analogy

Think of a busy help desk. Latency-optimized inference is a faster service window. Prompt caching is keeping the promotion rules and return policy on the counter so repeated questions reuse the same handbook instead of fetching it again. Renting a whole extra building permanently for an occasional weekend rush is less economical.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock latency-optimized inference | Fast service window | Reduces the inference path’s latency |
| Bedrock Prompt Caching | Shared counter handbook | Reuses common prompt context for repetitive inquiries |
| Provisioned Throughput/MUs | Permanently rented counters | Provides reserved capacity but can be costly for occasional peaks |
| Agents/Knowledge Bases | Extra service departments | Useful for orchestration or retrieval, but not needed for this bottleneck |

#### Correct answer and reasoning

1. The workload has a normal level and a much higher, occasional peak.
2. A large portion of requests shares context, creating a clear caching opportunity.
3. Latency-optimized inference targets response time, while prompt caching reduces repeated context processing.
4. This combination meets the performance goal with the least additional operational machinery, so the recorded answer is `A`.

#### Option-by-option elimination

- **A.** It directly matches both clues—latency and reusable prompt context—and is the most cost-effective managed combination.
- **B.** Capacity sized for the peak can be expensive when the peak is occasional, and Redis introduces cache-management work.
- **C.** Agents, a knowledge base, and cross-Region routing add orchestration that the scenario does not require.
- **D.** Lambda routing plus a DynamoDB cache requires custom matching, invalidation, and maintenance, so overhead is higher.

#### Exam strategy

Separate every performance question into latency, capacity, and repeated-work clues. Map latency to latency-optimized inference and repeated prompt context to prompt caching. Then use “occasional peak” and “cost-effective” to challenge any fixed capacity purchase. Do not add retrieval services when no retrieval requirement exists.

---

## Question 43

### 中文

#### 考点背景

本题考查 RAG 中 Chunking 与 Embedding 的因果链。固定 500 Token 切分把一条完整法律论证撕成两半，向量即使数学上相似，也可能只召回其中一片，生成模型便缺少前提、结论或引用关系。按完整论证、条款、章节等 Semantic Boundary 切分，目标是让每个可检索单元保留自然语义；切分改变后必须重新生成 Embedding，否则向量仍代表旧内容。

规模线索也很关键：1,500 万份文档、维度提升后的存储增长和 P95 超时，说明不能用“更大向量”掩盖数据分块问题。更高维度会增加向量存储、索引和相似度计算成本，未必改善语义边界。静态摘要会牺牲细节和新鲜度，而 DynamoDB 关键词查找不能替代 OpenSearch 的大规模向量相似度检索。

#### 场景比喻

像把一份判决书剪成纸条：若把“法院理由”和“因此判决”剪到不同信封，律师拿到其中一张就难以理解结论。语义切分是沿章节和论证自然折页；重新 Embedding 则给每个新信封贴上与内容匹配的新索引卡。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Embeddings | 内容索引卡制作机 | 为新的语义 Chunk 生成匹配的向量表示 |
| Amazon OpenSearch Service | 大型法律索引室 | 存储向量并执行规模化相似度检索 |
| Semantic Chunking | 按论证折页 | 让完整条款、观点或章节尽量留在同一检索单元 |
| S3/静态摘要 | 复印件仓库 | 可存对象或摘要，但不能代替动态、上下文完整的检索 |

#### 正确答案与推理

1. 结果遗漏上下文的直接原因是固定边界破坏了法律语义单元。
2. 先改为按完整论证、条款或章节的语义边界切分，提升召回片段的上下文完整性。
3. 新 Chunk 的内容已经变化，必须重新生成并重建对应向量索引。
4. 这个方案同时修复相关性根因，并避免无意义地扩大维度，因此答案为 `C`。

#### 逐项排除

- **A.** 只把 768 维提高到 4,096，既没有修复错误切分，还会显著增加存储与检索计算，不能解决过时或缺失上下文。
- **B.** 静态摘要可能遗漏法律细节，也会随着法规和判决更新而过时，不满足动态法律研究。
- **C.** 以语义边界切分并重新 Embedding，使检索单元和向量一致，直接命中题干根因，正确。
- **D.** DynamoDB 的关键词索引适合键值访问，不是替代 OpenSearch 的大规模向量相似度搜索。

#### 解题方法

RAG 题先追问“召回的 Chunk 是否自洽”。看到固定长度切断论证，就选 Semantic Chunking；只要 Chunk 变了，就检查是否同步 Re-embed。看到规模和延迟，不要自动选更高维度，要评估它是否增加成本而未修根因。

### English

#### Exam focus and background

This question tests the causal relationship between chunking and embeddings in a RAG system. A fixed 500-token boundary can split a legal argument, a court rationale, or a statute reference into separate pieces. A vector search may retrieve one piece, but the generation model then lacks the premise or conclusion needed for a reliable answer. Semantic boundaries keep a complete clause, argument, or section together. Because the text chunks change, their embeddings must also be regenerated and reindexed.

The scale clues reinforce the diagnosis. With 15 million documents, increasing dimensionality from 768 to 4,096 raises storage, index, and similarity-computation costs without repairing broken context. Static summaries can lose legal detail and become stale. DynamoDB keyword access is not a replacement for large-scale vector similarity search in OpenSearch. The best fix changes the retrieval unit and keeps its vector representation consistent.

#### Analogy

Imagine cutting a court opinion into strips of paper. If the court’s reasoning is in one envelope and “therefore” plus the decision is in another, a lawyer holding one strip cannot reconstruct the ruling. Semantic chunking folds the document along its chapters and arguments; re-embedding creates a new index card for every new envelope.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock embedding model | Index-card maker | Creates vectors that represent the new semantic chunks |
| Amazon OpenSearch Service | Large legal index room | Stores vectors and performs similarity retrieval at scale |
| Semantic chunking | Natural document folding | Keeps related clauses, arguments, or sections together |
| S3/static summaries | Copy warehouse | Can store material but cannot replace fresh, context-complete retrieval |

#### Correct answer and reasoning

1. Missing context is caused by boundaries that split semantically linked legal content.
2. Change preprocessing to chunk on complete arguments, clauses, or sections.
3. Regenerate embeddings and rebuild the affected index because the chunk contents changed.
4. This addresses the relevance root cause without the unnecessary cost of much larger vectors, so `C` is correct.

#### Option-by-option elimination

- **A.** Larger vectors do not repair bad boundaries and increase storage and search cost.
- **B.** Static summaries can omit important legal detail and become outdated, which conflicts with dynamic legal research.
- **C.** Semantic chunking plus re-embedding aligns the retrieval units with their vector representations and directly fixes the stated problem.
- **D.** DynamoDB keyword indexes do not substitute for OpenSearch vector similarity search at this scale.

#### Exam strategy

In RAG questions, first ask whether each retrieved chunk is semantically self-contained. If fixed-size chunks split arguments, select semantic chunking; if the chunks change, select re-embedding as part of the same fix. Do not choose higher dimensionality merely because the dataset is large.

---

## Question 44

### 中文

#### 考点背景

本题考查交互式澄清流程中的“持久编排”和“高并发会话存储”。用户问题含糊时，工作流可能需要等待用户或外部回调；Step Functions Standard Workflow 能持久保存执行状态，并用 Wait for a Callback Pattern 暂停而不让 Lambda 一直占用执行环境。对话记录则是按用户/Session 高频读取和更新的结构化状态，DynamoDB On-Demand 能随请求量弹性扩展，Server-Side Encryption 满足静态加密要求。

“快速响应”不等于所有步骤都必须用最快的短流程模式；它更要求请求入口和会话读写不要被固定容量或低效对象拼接拖慢。Standard 适合需要可靠、可恢复、可等待的业务流程。题目中原始英文的 `Amazon DynamoDPurchase` 是排版错误，语义明显指向 DynamoDB On-Demand；按该记录理解即可。

#### 场景比喻

像医院的问诊台：护士把模糊病例挂起，等待患者补充信息，不能让一名护士站着占住窗口，这就是 callback 等待。DynamoDB 像可扩展的电子病历柜，数千名患者可同时读写；S3 更像仓库里一箱箱 JSON，不适合每次问诊都翻找并拼接。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Step Functions Standard | 可挂起的流程调度台 | 持久化编排澄清步骤并等待 callback |
| Wait for a Callback | “等患者回电”挂号牌 | 暂停流程，收到外部结果后再继续 |
| DynamoDB On-Demand | 弹性电子病历柜 | 存取会话上下文，按并发访问弹性处理容量 |
| Server-Side Encryption | 病历柜的锁 | 保护存储中的对话记录 |

#### 正确答案与推理

1. 澄清流程可能跨越一段等待时间，需要可持久化的执行状态。
2. Standard Workflow 与 callback 模式可以表达“暂停—回调—继续”，比短时 Express 更合适。
3. 数千并发用户需要弹性会话存储；DynamoDB On-Demand 避免预估吞吐，且可配置服务器端加密。
4. 所以记录答案 `B` 同时覆盖等待、并发、上下文和加密要求。

#### 逐项排除

- **A.** Express 不适合需要持久等待的澄清流程；RDS 也会带来连接和容量管理，整体不如 Standard 加 DynamoDB。
- **B.** Standard、callback、DynamoDB On-Demand 和服务器端加密分别命中流程、并发与安全要求，正确。
- **C.** S3 每次交互一个 JSON 对象，增加对象管理、读取和上下文拼接开销，不是高频会话状态的优选。
- **D.** SQS 传递消息却不自然表达交互式澄清状态；Redis 更偏缓存，作为需保存的个性化对话记录不如 DynamoDB 合适。

#### 解题方法

看到“澄清/人工或外部回调/可能等待”，优先 Standard + callback；看到“数千并发会话/按需扩展/加密记录”，优先 DynamoDB On-Demand + SSE。区分“耐久状态”与“消息队列/缓存”，不要只因 Lambda 或 SQS 看起来简单就选它们。

### English

#### Exam focus and background

This scenario combines durable orchestration with high-concurrency conversational state. An ambiguous request may require the workflow to wait for a user or an external callback. Step Functions Standard supports durable execution state and callback waiting, so a Lambda execution does not have to remain occupied while the clarification is pending. Conversation history is structured, frequently accessed session state. DynamoDB On-Demand can absorb changing request volume, and server-side encryption protects stored records.

“Respond quickly” does not mean every component must use a short-running workflow mode. The design must avoid tying up workers and avoid slow, awkward object-based session assembly. Standard is the appropriate workflow choice when waiting and recoverability matter. The malformed source text referring to `Amazon DynamoDPurchase` clearly intends DynamoDB On-Demand; the recorded answer is evaluated with that meaning.

#### Analogy

Picture a clinic desk. When a case is unclear, the nurse hangs a callback ticket and continues serving others instead of standing at the window. Standard Step Functions is that durable ticket. DynamoDB is an expandable electronic chart cabinet for thousands of simultaneous patients; S3 is a warehouse of separate JSON boxes that is awkward for every conversational read and update.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Step Functions Standard | Durable workflow desk | Orchestrates clarification and persists waiting state |
| Wait for a Callback | “Await patient call” ticket | Pauses and resumes after an external result |
| DynamoDB On-Demand | Elastic chart cabinet | Stores session context under variable concurrent load |
| Server-side encryption | Cabinet lock | Protects conversation records at rest |

#### Correct answer and reasoning

1. Clarification can take time, so the execution state must survive a wait.
2. Standard plus a callback pattern models pause, callback, and continuation.
3. Thousands of users need elastic session storage; DynamoDB On-Demand avoids pre-provisioning, and server-side encryption protects the records.
4. Therefore, the recorded answer `B` covers orchestration, concurrency, context, and encryption.

#### Option-by-option elimination

- **A.** Express is not the natural fit for a durable waiting workflow, and RDS adds connection and capacity management for this access pattern.
- **B.** Standard, callback waiting, On-Demand DynamoDB, and server-side encryption directly match the requirements.
- **C.** One S3 JSON object per interaction creates object and session-assembly overhead for frequently updated conversational state.
- **D.** SQS transports messages but does not naturally model conversational state; Redis is less suitable than DynamoDB as the durable conversation record store.

#### Exam strategy

Map “clarification, callback, or waiting” to Standard Step Functions and a callback pattern. Map “many concurrent sessions, elastic capacity, encrypted records” to DynamoDB On-Demand with server-side encryption. Distinguish durable state from a queue or cache before selecting a service.

---

## Question 45

### 中文

#### 考点背景

本题考查面向 Bedrock RAG 的数据预处理治理链，而不是单一的文本分析。公司需要从 S3 数据源建立可审计的 Metadata，执行自定义转换和 Text Chunking，还要有数据质量规则、验证结果和持续指标。AWS Glue 的组合刚好按职责分层：Crawler 发现并 Catalog 数据，ETL Job 承载脚本化转换，Glue Data Quality 负责规则、质量评估与监控。

“最少开发工作量”意味着优先采用同一托管数据集成体系里的现成功能，而不是把质量检查、编排和监控拆成多个自定义组件。处理结果可写回 S3，再供 Bedrock Knowledge Base 摄取；关键是流程有目录、有质量门、有可追踪指标，同时保留自定义 Chunking 的空间。

#### 场景比喻

像出版社准备一套金融教材：Glue Crawler 是登记员，给每份稿件编号并记录来源；ETL Job 是编辑台，按规则清洗和分章；Glue Data Quality 是校对委员会，检查缺页、乱码和必填字段，并持续公布质量分数。三者在一条流水线上，比临时找不同团队拼装工具更省维护。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Glue Crawler/Data Catalog | 登记员与目录 | 发现 S3 数据并建立可审计的元数据目录 |
| AWS Glue ETL Job | 编辑流水线 | 运行自定义转换、清洗和 Text Chunking |
| AWS Glue Data Quality | 校对委员会 | 定义质量规则、验证数据并监控质量指标 |
| Amazon S3/Bedrock Knowledge Base | 出版仓与读者 | 保存处理结果并作为 Bedrock 检索语料来源 |

#### 正确答案与推理

1. S3 中的非结构化资料需要被发现、分类并留下来源信息，Crawler/Data Catalog 覆盖 Metadata 要求。
2. 自定义 Text Chunking 和转换属于 ETL 逻辑，Glue ETL Job 可以承载脚本。
3. 数据质量验证与指标监控由 Glue Data Quality 集中处理。
4. 因而 `B` 以一套托管服务覆盖目录、转换、质量和后续加载，开发与运维开销最低。

#### 逐项排除

- **A.** Data Wrangler 可做转换，但再加自定义 Lambda、CloudWatch 指标和告警，需要自行拼装质量治理，工作量更大。
- **B.** Glue Crawler、ETL Job 和 Data Quality 分别覆盖目录、可定制处理与质量监控，正确。
- **C.** Comprehend 实体抽取不是完整质量治理；Lambda Chunking 和 Athena 查询也不能替代质量规则体系。
- **D.** Step Functions、EC2 自定义代码和 Model Monitor 组件多；Model Monitor 面向模型监控，不是原始数据质量验证。

#### 解题方法

先把需求映射为“目录—转换—质量—加载”：Glue Crawler/Data Catalog、Glue ETL、Glue Data Quality。看到“原始数据质量”不要选 SageMaker Model Monitor；看到“最少开发”要惩罚 EC2、Lambda 拼装和多套自定义监控。

### English

#### Exam focus and background

This is a data-preparation governance question, not merely a text-analysis question. The company needs auditable metadata, custom transformation and chunking, data-quality validation, and ongoing quality metrics for unstructured S3 content used by Bedrock. AWS Glue provides a coherent managed chain: a crawler discovers and catalogs sources, an ETL job runs custom transformations, and Glue Data Quality applies rules and reports quality results.

The least-development clue favors integrated managed capabilities. The transformed output can be written back to S3 for ingestion by a Bedrock knowledge base. What matters is that the workflow has a catalog, a quality gate, observable metrics, and enough ETL flexibility to customize chunking, without building separate orchestration and monitoring infrastructure.

#### Analogy

Think of preparing a financial textbook. The Glue crawler is the registrar that assigns each manuscript a source record. The ETL job is the editorial desk that cleans and divides chapters. Glue Data Quality is the proofing board that checks missing pages and required fields and publishes quality scores. One publishing line is easier to run than several improvised tools.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Glue Crawler/Data Catalog | Registrar and catalog | Discovers S3 data and creates auditable metadata |
| Glue ETL Job | Editorial line | Runs custom transforms, cleaning, and text chunking |
| Glue Data Quality | Proofing board | Defines rules, validates data, and monitors quality metrics |
| S3/Bedrock Knowledge Base | Warehouse and reader | Stores processed output for Bedrock retrieval ingestion |

#### Correct answer and reasoning

1. The S3 sources need discoverable, traceable metadata, which the crawler and Data Catalog provide.
2. Custom chunking and transformations belong in a Glue ETL job.
3. Glue Data Quality supplies rules, validation, and quality monitoring.
4. Therefore `B` covers cataloging, processing, quality, and downstream loading with the least custom infrastructure.

#### Option-by-option elimination

- **A.** Data Wrangler plus custom Lambda and CloudWatch metrics requires the company to assemble more of the quality-governance pipeline.
- **B.** Glue’s crawler, ETL job, and Data Quality components directly match the four required capabilities.
- **C.** Entity extraction is not comprehensive data-quality governance, and Lambda chunking plus Athena queries add custom work.
- **D.** EC2 orchestration is operationally heavier, and SageMaker Model Monitor is for model monitoring rather than raw-data quality validation.

#### Exam strategy

Translate the stem into catalog, transform, quality, and load. For this sequence, look for Glue Crawler/Data Catalog, Glue ETL, and Glue Data Quality. Do not confuse raw-data quality with SageMaker model monitoring, and penalize EC2 plus custom Lambda assembly when the question asks for least development.

---

## Question 46

### 中文

#### 考点背景

本题考查 RAG 检索范围控制，也就是在向量相似度之前先用业务 Metadata 缩小候选集合。突发新闻语料同时有转录、历史报告和相关文档，且时间跨度很长；如果所有内容混在一次 Vector Search 中，系统会比较大量无关向量，既拉高延迟，也让相似度结果被错误类型或过时资料稀释。Metadata-Aware Filtering 允许 Knowledge Base 按内容类型、时间、事件等属性约束检索。

题干已经明确高级 Embedding 和底层 Fine-Tuning 不是收益点，所以不要继续“训练更聪明的模型”。最小架构改动意味着保留现有 Bedrock Knowledge Base，只为 S3 对象提供可检索的元数据并建立索引。过滤不是删除向量，而是把搜索问题从“整个仓库找相似”变成“在正确货架中找相似”。

#### 场景比喻

像新闻编辑室要找“本周地震视频的文字稿”：不加过滤，检索员要在文字稿、三年前报告和体育新闻的所有文件中翻找；加上 `type=transcript`、`date=this week` 后，先关掉错误货架，再做相似度比较，答案更快也更准。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Base | 新闻检索台 | 管理数据源、Embedding 与 RAG 检索 |
| S3 Object Metadata | 文件标签 | 描述内容类型、时间范围等业务筛选属性 |
| Metadata-Aware Filtering | 货架门禁 | 在向量搜索前限制候选文档范围 |
| Embedding Model | 相似度探测器 | 在已经缩小的候选集合内比较语义相似度 |

#### 正确答案与推理

1. 不相关和缓慢的根因是候选集合太大、内容类型和时间范围没有约束。
2. 现有 Knowledge Base 已能做向量检索，不需要迁移平台。
3. 为 S3 对象建立可用元数据并启用 Metadata-Aware Filtering，可先按业务条件缩小范围。
4. 因此 `C` 用最小改动同时改善相关性和延迟，符合题目记录答案。

#### 逐项排除

- **A.** 更换或增强 Embedding 可能改变相似度质量，却不会阻止搜索跨越错误内容类型和时间范围。
- **B.** OpenSearch 支持过滤，但把现有 Knowledge Base 迁移出去是额外架构改动，不符合最小变更。
- **C.** 在现有 Knowledge Base 内索引 S3 元数据并启用过滤，直接约束检索集合，正确。
- **D.** 迁移到 Q Business 改变平台，且不是解决当前 Knowledge Base 检索范围的最小路径。

#### 解题方法

看到“向量搜索太宽/文档类型和时间未限制/想少改架构”，关键词就是 Metadata Filtering。先做结构化候选集过滤，再做语义相似度；不要用 Fine-Tuning 或平台迁移解决一个范围约束问题。

### English

#### Exam focus and background

This question is about constraining the retrieval set before vector similarity search. The news corpus contains transcripts, archived reports, and related documents across long time ranges. If every vector participates in every search, irrelevant content increases both latency and the chance that stale or wrong-type material influences the answer. Metadata-aware filtering lets the Bedrock knowledge base narrow candidates by attributes such as content type, date, or event.

The scenario explicitly says that more fine-tuning will not help, and it asks for minimal architectural change. Keep the existing knowledge base, index useful S3 object metadata, and apply that metadata during retrieval. Filtering does not remove embeddings; it changes the search from “find a similar item anywhere” to “find a similar item on the correct shelf.”

#### Analogy

Suppose an editor needs the transcript of this week’s earthquake video. Without filters, the clerk searches transcripts, three-year-old reports, and unrelated sports files together. With `type=transcript` and `date=this week`, the wrong shelves are closed before similarity search begins, making the answer faster and more relevant.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock Knowledge Base | News retrieval desk | Owns the data source, embeddings, and RAG retrieval |
| S3 object metadata | File labels | Supplies content type, date, and other filter attributes |
| Metadata-aware filtering | Shelf gate | Restricts candidates before vector comparison |
| Embedding model | Similarity detector | Compares meaning within the narrowed candidate set |

#### Correct answer and reasoning

1. The root problem is an unbounded candidate set spanning unrelated types and periods.
2. The current Bedrock knowledge base already provides the retrieval architecture.
3. Index S3 metadata and enable metadata-aware filtering to narrow candidates by business constraints.
4. This improves relevance and latency with minimal change, so `C` is correct.

#### Option-by-option elimination

- **A.** A domain-adapted embedding may change similarity quality, but it does not restrict the content types or dates searched.
- **B.** OpenSearch can support filtering, but migrating away from the current knowledge base adds unnecessary architectural work.
- **C.** Metadata-aware filtering inside the existing knowledge base directly constrains the retrieval scope.
- **D.** Moving to Q Business changes platforms and is not the minimal fix for a filtering problem in the current knowledge base.

#### Exam strategy

When vector search is too broad and the stem names types, dates, tenants, or regions, look for metadata filtering. Filter the candidate set first and perform semantic similarity second. Avoid model fine-tuning or service migration when the requested fix is a retrieval-scope constraint.

---

## Question 47

### 中文

#### 考点背景

本题考查向量索引算法与数据规模的匹配。数据集小、索引数量低，同时要求尽可能高的准确率，意味着可以用 MemoryDB 的 Flat Algorithm 做精确相似度搜索，不必承担 HNSW 或 IVFFlat 的近似索引调参和召回误差。Flat 通常需要比较更多向量，但在小数据集上这一代价可控，换来直接、稳定的准确性。

性能要求并不自动等于垂直扩容。随着访问量上升，可根据指标做 Horizontal Scaling，让容量随节点/分片扩展；这与“数据库规模小但请求可能增长”并不矛盾。考试要同时看数据规模、准确率和扩展方式，而不是看到向量就盲选复杂 ANN 索引。

#### 场景比喻

像在一间只有几百本书的小型档案室找最相近的段落：管理员逐本比较，反而能保证没有漏掉最佳匹配。为这个小房间建一套复杂的分区地图和近似路线，可能还要不断调参数；读者变多时，开第二间同样的档案室比把一间房越加高更灵活。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon MemoryDB | 快速向量档案室 | 承载应用使用的向量数据和索引 |
| Flat Algorithm | 逐本核对员 | 对小数据集执行精确相似度搜索，优先准确率 |
| HNSW/IVFFlat | 近似导航地图 | 适合更大规模时降低搜索量，但需要索引取舍与调优 |
| Horizontal Scaling Policy | 增开档案室 | 依据性能指标扩展处理容量，保持高性能 |

#### 正确答案与推理

1. 数据集小、索引少，Flat 的精确搜索成本在可接受范围内。
2. 题目强调最大化准确率，因此优先精确搜索而不是近似召回。
3. 请求负载增长时按指标水平扩展，避免把“性能”误解成只能升级单机规格。
4. 组合起来，`A` 最符合规模、准确率与高性能要求。

#### 逐项排除

- **A.** MemoryDB + Flat 提供精确搜索，水平扩展可随负载增长，是小数据集的直接方案，正确。
- **B.** HNSW 是近似搜索，且题目给的是小数据集和准确率优先；垂直扩展也限制了横向弹性。
- **C.** Aurora 加 IVFFlat 仍是近似算法，需要维护关系数据库与索引调优；垂直扩展不如所需的弹性路径。
- **D.** DocumentDB、IVFFlat 和较高 Probe Value 增加参数与连接复杂度，不能像 Flat 一样直接保证精确搜索。

#### 解题方法

用三步判断：数据小不小？准确率还是极低延迟优先？扩容应横向还是纵向？“小数据 + 高准确率”通常指 Flat；“大数据 + 近似可接受”才考虑 HNSW/IVF。不要忽略题目给出的 scaling policy 线索。

### English

#### Exam focus and background

This question tests choosing a vector index algorithm according to data size and accuracy priorities. The dataset is small and has few indexes, while accuracy should be maximized. A Flat algorithm can perform exact similarity search. It may compare more vectors, but that cost is manageable at small scale and avoids the recall trade-offs and tuning associated with approximate HNSW or IVFFlat indexes.

Performance does not automatically mean vertical scaling. If request load grows, horizontal scaling based on performance metrics can add capacity without turning the solution into a larger single node. The exam is asking you to combine dataset size, accuracy, and scaling direction rather than selecting the most sophisticated approximate index whenever vectors appear.

#### Analogy

Imagine finding the closest passage in a small archive room with a few hundred books. Comparing each book can guarantee the best match. Building a complex approximate map for that room adds tuning work. If more readers arrive, opening another archive room is more flexible than continually making one room taller.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon MemoryDB | Fast vector archive | Stores the application’s vector data and index |
| Flat algorithm | Careful book-by-book clerk | Performs exact similarity search for high accuracy |
| HNSW/IVFFlat | Approximate navigation maps | Reduce search work at larger scale but introduce trade-offs |
| Horizontal scaling policy | Opening more archive rooms | Adds capacity according to performance metrics |

#### Correct answer and reasoning

1. A small dataset makes the cost of exact Flat search manageable.
2. The explicit accuracy priority favors exact search over approximate recall.
3. Horizontal scaling can address growing request load while preserving a simple index choice.
4. Therefore `A` best matches the dataset, accuracy, and performance requirements.

#### Option-by-option elimination

- **A.** MemoryDB with Flat search provides exact similarity, and metric-based horizontal scaling handles increased load.
- **B.** HNSW is approximate and less aligned with a small, accuracy-first dataset; vertical scaling is also less elastic.
- **C.** Aurora with IVFFlat introduces approximate-index tuning and a relational cluster; vertical expansion is not the stated best fit.
- **D.** DocumentDB, IVFFlat, and probe tuning add complexity and do not provide the direct exact-search behavior of Flat.

#### Exam strategy

Ask three questions: Is the dataset small? Is exact accuracy more important than approximate speed? Should capacity scale out or only up? “Small plus accuracy-first” points to Flat; “large plus acceptable approximation” points toward HNSW or IVF. Use the scaling-policy clue as part of the answer.

---

## Question 48

### 中文

#### 考点背景

本题考查大批量 RAG 语料处理中的托管编排与服务组合。50 GB 的 S3 JSON 需要抽取有效字段、删除 PII、生成 Embedding，并在四小时内成为可检索语料；最重要的不是把所有工作塞进一段自定义代码，而是用 Step Functions 把步骤连接起来，让 Comprehend 负责 PII 检测、Bedrock 负责向量生成、OpenSearch Serverless 负责托管向量存储和相似度搜索。

“最低运维开销”使集群型选项处于劣势。Lambda 并行处理大文件会遇到执行时长、并发和吞吐调节；Glue 加 SageMaker Processing 需要维护更多作业边界和模型处理配置；EMR、Spark、UDF、Aurora 则引入集群、数据库和自定义代码。托管服务并不取消批量切分和错误处理，但把基础设施运维从方案中移开，更有机会在时限内完成。

#### 场景比喻

像整理五十吨客服档案：Step Functions 是传送带总控，先让安检员 Comprehend 圈出身份证号码，再让 Bedrock 把干净文本压成语义条码，最后放入 OpenSearch Serverless 的自动化仓库。租一整座 EMR 工厂来做一次性整理，会把机器维护变成主任务。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Step Functions | 传送带总控 | 编排抽取、PII 检测、Embedding 和写入步骤，可处理批量分支 |
| Amazon Comprehend | 隐私安检员 | 检测客户对话中的 PII，供清理流程使用 |
| Amazon Bedrock | 语义条码机 | 为脱敏后的文本生成向量 Embedding |
| OpenSearch Serverless | 托管向量仓库 | 存储向量并提供相似度检索，减少集群运维 |

#### 正确答案与推理

1. 先从 S3 JSON 提取相关字段，再把含 PII 的内容送入检测和清理步骤，保证语料安全。
2. 对清理后的文本调用 Bedrock 生成 Embedding，不能在脱敏前把个人信息写入向量库。
3. 用 Step Functions 明确编排依赖、批量分支和失败处理，再写入 OpenSearch Serverless。
4. 该组合覆盖 50 GB、四小时、向量检索和低运维目标，所以答案是 `D`。

#### 逐项排除

- **A.** Lambda 并发和内存调优仍需自行解决长任务、大文件和吞吐上限，运维与可靠性负担较大。
- **B.** Glue 与 SageMaker Processing 都可处理数据，但两套作业加自管预训练处理链比托管 Embedding 组合更复杂。
- **C.** EMR、Spark UDF、Comprehend 调用和 Aurora pgvector 引入集群及数据库维护，对该任务过重。
- **D.** Step Functions 编排 Comprehend 与 Bedrock，并接入 OpenSearch Serverless，职责清晰且托管程度最高，正确。

#### 解题方法

批量 RAG 题按“抽取—脱敏—向量化—存储”排序，再看“时间限制 + 最低运维”。优先选择托管服务的直接集成；看到 Lambda 长任务、EMR 集群、Aurora 或多层自定义处理，要问它们是否只是增加运维而非满足新需求。

### English

#### Exam focus and background

This question tests a managed pipeline for preparing a large RAG corpus. Fifty gigabytes of JSON in S3 must be extracted, stripped of PII, embedded, and made searchable within four hours. The intended design is to orchestrate clear stages: Comprehend detects PII, Bedrock generates embeddings from the cleaned text, and OpenSearch Serverless stores vectors and serves similarity search.

The least-operational-overhead clue disfavors clusters and a large custom processing stack. Lambda parallelism still requires careful handling of execution duration, concurrency, and throughput for large files. Glue plus SageMaker Processing introduces more job boundaries and model-processing configuration. EMR, Spark UDFs, and Aurora add cluster and database operations. Managed services do not eliminate partitioning or error handling, but they remove much of the infrastructure burden and make the four-hour target more practical.

#### Analogy

Imagine processing fifty tons of customer records. Step Functions is the conveyor controller. Comprehend is the privacy inspector that marks identity numbers, Bedrock turns sanitized text into semantic barcodes, and OpenSearch Serverless is the automated warehouse. Renting and maintaining an entire EMR factory would distract from the actual cleanup job.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Step Functions | Conveyor controller | Orchestrates extraction, PII handling, embedding, and storage |
| Amazon Comprehend | Privacy inspector | Detects PII so the data can be sanitized |
| Amazon Bedrock | Semantic barcode machine | Generates embeddings from sanitized text |
| OpenSearch Serverless | Managed vector warehouse | Stores vectors and provides similarity search without cluster management |

#### Correct answer and reasoning

1. Extract the useful fields from the S3 JSON and detect and remove PII before indexing.
2. Generate embeddings from the sanitized text rather than sending personal data into the vector corpus.
3. Use Step Functions to coordinate batch branches, dependencies, and failure handling, then write vectors to OpenSearch Serverless.
4. This covers the data size, time limit, retrieval need, and low-operations requirement, so `D` is correct.

#### Option-by-option elimination

- **A.** Lambda concurrency and memory tuning still leave long-running, large-file, and throughput management to the team.
- **B.** Glue and SageMaker Processing can work, but operating two processing layers and a custom embedding path adds more overhead.
- **C.** EMR, Spark UDFs, Comprehend calls, and Aurora pgvector create unnecessary cluster and database operations.
- **D.** Step Functions plus Comprehend, Bedrock, and OpenSearch Serverless gives the clearest managed pipeline and is the intended choice.

#### Exam strategy

Translate batch RAG preprocessing into extract, sanitize, embed, and store. Then apply the time and operational-overhead constraints. Prefer direct managed integrations; challenge Lambda for long bulk work and challenge EMR, Aurora, or multi-layer custom stacks when they add infrastructure without a stated capability.

---

## Question 49

### 中文

#### 考点背景

本题考查“模型配置是运行时数据，不是代码”的发布治理。Premium 与 Standard 需要不同模型，A/B Testing 需要按客户等级或流量分组切换，而 Temperature、Maximum Token Limit 等参数必须在生效前经过格式和范围验证。AWS AppConfig 同时提供配置管理、Feature Flag、JSON Schema Validation，以及面向运行时读取的 AppConfig Agent，因此 Lambda 可以保持不变。

低运维的关键不只是“把值放到某个数据库”，还包括变更安全。AppConfig 让配置发布成为有验证和部署控制的流程；Agent 让函数读取当前配置，而不是每次都自己轮询、解析和实现一致性。Parameter Store、DynamoDB 或 Redis 可以存值，但题干要求的 A/B、验证和安全配置生命周期会落回自定义代码。

#### 场景比喻

像机场要给商务舱和经济舱分配不同登机通道，并试验新流程：AppConfig 是带审批、演练和回滚按钮的航班控制台；Feature Flag 决定哪批乘客走新通道，Schema Validation 先检查登机规则是否完整。Lambda 只是每次看控制台，不必重新装修机场。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS AppConfig | 配置发布控制台 | 管理模型配置，支持验证、安全部署和回滚 |
| Feature Flag | 分流开关 | 按客户等级或实验分组进行 A/B Testing |
| JSON Schema Validation | 配置质检员 | 在发布前校验 Temperature、Token Limit 等字段 |
| AppConfig Agent | 现场读卡器 | 让 Lambda 在运行时读取配置而不重新部署 |

#### 正确答案与推理

1. 模型 Provider 和参数必须能运行时切换，不能硬编码在 Lambda 集成中。
2. Premium/Standard 和 A/B Testing 需要可控分流，Feature Flag 正好表达目标组。
3. 配置发布前要验证结构和字段，JSON Schema Validation 覆盖参数检查。
4. AppConfig Agent 提供运行时读取，且 AppConfig 提供安全发布与回滚能力，因此答案为 `C`。

#### 逐项排除

- **A.** Parameter Store 可保存参数，但轮询、EventBridge 触发重新部署把运行时配置变成自定义发布流程，不能以最低运维满足 A/B 与验证。
- **B.** DynamoDB 能按客户等级查询，却要自行实现 Feature Flag、Schema Validation、发布和回滚。
- **C.** AppConfig、Feature Flag、JSON Schema 和 Agent 分别命中配置治理、实验、验证和运行时读取，正确。
- **D.** Redis TTL 与 Lambda 自定义验证只解决临时存取，不能自然提供安全配置发布和回滚。

#### 解题方法

看到“无需重新部署 + A/B + 参数验证 + 回滚”，直接联想 AppConfig。区分“能存配置”的 Parameter Store/DynamoDB/Redis 与“能治理配置生命周期”的 AppConfig；Agent 是 Lambda 运行时读取的关键词。

### English

#### Exam focus and background

This question treats model configuration as runtime data rather than application code. Premium and standard customers need different providers or models, feature experiments need controlled traffic splits, and parameters such as temperature and maximum tokens must be validated before release. AWS AppConfig combines configuration management, feature flags, JSON Schema validation, and runtime retrieval through the AppConfig Agent, allowing Lambda code to remain unchanged.

The operational clue goes beyond storing a value somewhere. AppConfig gives configuration changes a governed lifecycle with validation, controlled deployment, and rollback. The Agent lets the function read the active configuration without implementing its own polling and consistency logic. Parameter Store, DynamoDB, or Redis can hold values, but the team would have to build the experiment, validation, and safe-release behavior around them.

#### Analogy

Imagine an airport assigning different boarding lanes to business and economy passengers while testing a new process. AppConfig is a flight-control console with approval, rollout, and rollback controls. A feature flag decides which passengers use the new lane, and schema validation checks the rules before publication. Lambda simply reads the console instead of rebuilding the airport.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS AppConfig | Configuration control console | Manages model settings with validation and controlled release |
| Feature flag | Traffic-split switch | Supports customer-tier or experiment-based A/B testing |
| JSON Schema validation | Configuration inspector | Checks temperature, token limits, and required fields |
| AppConfig Agent | On-site card reader | Lets Lambda obtain configuration at runtime without redeployment |

#### Correct answer and reasoning

1. The active model and parameters must change at runtime rather than be hardcoded in Lambda.
2. Feature flags provide controlled segmentation for premium/standard tiers and A/B testing.
3. JSON Schema validation checks configuration structure and parameter fields before release.
4. The AppConfig Agent supplies runtime access, while AppConfig supplies safe deployment and rollback, so `C` is correct.

#### Option-by-option elimination

- **A.** Parameter Store can hold values, but polling and redeployment through EventBridge create custom lifecycle work and do not directly satisfy the full experiment-and-validation requirement.
- **B.** DynamoDB supports tier-based lookups, but the team must build feature flags, schema validation, rollout, and rollback.
- **C.** AppConfig, feature flags, JSON Schema validation, and the Agent map directly to all requirements.
- **D.** Redis with TTL and custom Lambda validation handles temporary storage, not a complete safe configuration-release lifecycle.

#### Exam strategy

For “no redeployment + A/B testing + validation + rollback,” choose AppConfig. Distinguish a service that stores configuration from one that governs configuration changes. The AppConfig Agent is the strong clue for runtime Lambda access.

---

## Question 50

### 中文

#### 考点背景

本题考查 Bedrock 推理吞吐瓶颈的两种互补处理：减少单请求开销，以及跨区域利用可用容量。Token Batching 把许多小请求的 token 工作更有效地组织起来，降低大量 API 往返带来的开销；Cross-Region Inference Profile 则让 Bedrock 在多个可用 Region 间分配推理流量，避免单个 Region 在峰值时成为瓶颈。二者共同针对 30,000 请求/小时和 2 秒响应目标。

不要把跨 Region 的意思误解为客户端自己轮询几个端点。Inference Profile 由服务侧按可用性和容量进行路由，通常比固定 Round-Robin 更能应对实际负载。Batch Inference 虽然适合离线吞吐，却是异步模式；Provisioned Throughput 若只在一个 Region 配置，仍然留下区域故障和峰值上限。

#### 场景比喻

像多城快递中心处理促销订单：Token Batching 把零散的小包合并装车，减少每单启动卡车的成本；Cross-Region Profile 是总调度台把订单送到有空位的城市仓。只在一座城租满仓库，或让顾客自己按固定顺序选仓，都不如按实时容量调度。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Token Batching | 合并小包的装车台 | 降低大量小请求的 API 开销 |
| Cross-Region Inference Profile | 跨城总调度台 | 把请求分散到可用 Region，缓解单区吞吐瓶颈 |
| Provisioned Throughput/MU | 单城预租仓库 | 可保容量，但单 Region 仍可能成为峰值瓶颈 |
| Batch Inference | 夜间批处理线 | 适合异步大批量，不符合 2 秒交互响应 |

#### 正确答案与推理

1. 负载从 10,000 增到 30,000 请求/小时，说明单区的峰值容量和延迟都需要缓解。
2. Token Batching 减少许多小调用的固定开销，提高吞吐效率。
3. Cross-Region Inference Profile 利用多个 Region 的可用推理容量，降低单区拥塞。
4. 组合满足跨区域、低延迟和峰值吞吐要求，因此答案为 `B`。

#### 逐项排除

- **A.** 单 Region Provisioned Throughput 把瓶颈和容量风险留在一个区域；重试只能缓解失败，不能增加跨区吞吐。
- **B.** Token Batching 与跨区域推理分别降低调用开销、分散容量压力，正确。
- **C.** 客户端 Round-Robin 不知道各 Region 的真实容量和健康状态；一个 MU 也不是可靠峰值备用。
- **D.** Batch Inference 是异步流程，不能保证交互请求在 2 秒内返回。

#### 解题方法

把吞吐题拆成“每个请求的开销”和“总容量分布”。前者看 batching，后者看 Cross-Region Inference Profile；看到 2 秒就排除异步 Batch Inference。客户端轮询不是服务侧容量感知，单区预留也不等于跨区弹性。

### English

#### Exam focus and background

This question combines two ways to relieve a Bedrock throughput bottleneck: reduce per-request overhead and use capacity across Regions. Token batching organizes the token work from many small requests more efficiently. A cross-Region inference profile distributes inference traffic across available Regions, preventing one Region from becoming the peak bottleneck. Together they address the jump from 10,000 to 30,000 requests per hour and the two-second response target.

Do not confuse cross-Region inference with a client blindly rotating through endpoints. A service-side inference profile can make routing decisions based on available capacity and health more effectively than fixed round robin. Batch inference can process large volumes, but it is asynchronous and therefore unsuitable for interactive two-second responses. Provisioned Throughput confined to one Region still leaves a regional capacity boundary.

#### Analogy

Think of several delivery hubs handling a promotion. Token batching loads small parcels together, reducing the cost of starting a truck for every parcel. A cross-Region inference profile is the central dispatcher that sends orders to a city with available warehouse space. Filling one city’s warehouse or making customers choose hubs in a fixed rotation is less resilient.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock token batching | Small-parcel loading station | Reduces API overhead from many small requests |
| Cross-Region Inference Profile | Multi-city dispatcher | Spreads requests across available Regions |
| Provisioned Throughput/MUs | Rented single-city warehouse | Reserves capacity but leaves a single-Region boundary |
| Batch Inference | Overnight processing line | Suits asynchronous work, not a two-second interaction |

#### Correct answer and reasoning

1. The workload can triple during peaks, so both request overhead and regional capacity need attention.
2. Token batching improves efficiency for many small requests.
3. A cross-Region inference profile uses available capacity across Regions and reduces local congestion.
4. This combination meets the latency, peak-throughput, and multi-Region requirements, so `B` is correct.

#### Option-by-option elimination

- **A.** Single-Region Provisioned Throughput leaves the regional bottleneck in place; retries do not add cross-Region capacity.
- **B.** Batching reduces per-call overhead and cross-Region inference distributes capacity, directly matching the problem.
- **C.** Client-side round robin is not capacity-aware, and one MU is not a dependable peak backup.
- **D.** Batch inference is asynchronous and cannot guarantee a two-second interactive response.

#### Exam strategy

Separate throughput questions into per-call overhead and aggregate capacity. Think batching for the first and cross-Region inference profiles for the second. The two-second clue eliminates asynchronous batch inference; fixed client routing and single-Region reservation do not equal capacity-aware resilience.

---

## Question 51

### 中文

#### 考点背景

本题考查语义缓存，而不是文本精确缓存。40% 的问题措辞不同却语义等价，所以不能把原始 Query Text 当唯一键，也不能只靠 stemming 处理词形变化。正确思路是先用 Bedrock Embedding 把查询映射为向量，再在支持 k-NN 的 OpenSearch 中做 Approximate k-NN，找到语义相近的已缓存问题，并复用对应响应。这样既减少模型调用，又能让同一意图稳定落到相同答案。

低延迟来自向量索引的近似邻居搜索；准确性来自相似度阈值、结果校验和缓存响应的绑定，而不是把任意近邻都直接返回。题目选项的核心比较是：OpenSearch 原生面向大规模向量检索，DAX/DynamoDB 的键值访问和字符串过滤无法理解同义改写，MemoryDB 的 Hash Set 与 RANGE 也不是向量相似度查询表达。

#### 场景比喻

像客服柜台遇到“怎么退货？”、“我想把买的东西寄回去”两种说法。精确缓存只认同一句话；语义缓存先把两句话翻译成地图上的位置，再找附近的“退货”标记，直接取出经过审核的统一答复。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Embedding | 意图地图制图员 | 把不同措辞的查询转为可比较的向量 |
| Amazon OpenSearch Service | 语义索引柜 | 存储 Query-Response 与向量并执行 k-NN |
| Approximate k-NN | 附近意图雷达 | 低延迟找到相似问题，支持语义缓存 |
| DynamoDB DAX/MemoryDB | 快速抽屉 | 可做键值或内存访问，但不是本题的语义邻居索引 |

#### 正确答案与推理

1. 同一问题有不同措辞，缓存键必须表达语义而非原文字面。
2. 用 Bedrock 为入站查询生成 Embedding，并与历史 Query-Response 一起索引。
3. OpenSearch k-NN 以低延迟寻找相近向量，再按相似度门槛复用答案。
4. 所以 `C` 同时满足减少重复调用、一致答案和低延迟。

#### 逐项排除

- **A.** DAX 仍是键值缓存；Partition Key 加 `LIKE` 不能可靠做同义查询，也不是 DynamoDB 的向量检索方案。
- **B.** MemoryDB 的 Hash Set 和 RANGE 查询没有表达向量距离的 k-NN 索引能力，不能完成语义邻居搜索。
- **C.** OpenSearch k-NN 配合查询 Embedding 可按语义命中相似 Query-Response，正确。
- **D.** 标准化和 stemming 只能覆盖有限词形变化，无法稳定识别同义改写或不同句式。

#### 解题方法

看到“措辞不同但同一问题/一致答案/减少模型调用”，立即想到 Semantic Cache：Embedding → 向量索引 → k-NN → 相似度阈值。精确键、LIKE、GSI 和 stemming 都属于词面技巧，不能代替向量相似度。

### English

#### Exam focus and background

This is a semantic-cache question, not an exact-text cache question. Forty percent of requests ask the same thing with different wording. The original query cannot be the only key, and stemming handles only limited word-form variation. The intended pattern is to generate an embedding for each incoming query, store query-response pairs with vectors, and use approximate k-nearest-neighbor search in OpenSearch to find a semantically equivalent cached request.

The low-latency benefit comes from a vector index, while answer consistency comes from binding the retrieved neighbor to an approved cached response and applying an appropriate similarity threshold. DAX and DynamoDB provide key-value access, not semantic understanding. A MemoryDB hash set with a range query is also not a vector-distance index. OpenSearch k-NN is the service capability that matches the retrieval problem.

#### Analogy

At a help desk, “How do I return this?” and “I want to send back my purchase” are different sentences with the same intent. Exact caching recognizes only identical wording. A semantic cache places both sentences on an intent map, finds the nearby “returns” marker, and reuses the reviewed answer.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock embedding model | Intent mapmaker | Converts varied wording into comparable vectors |
| Amazon OpenSearch Service | Semantic index cabinet | Stores query-response vectors and performs k-NN search |
| Approximate k-NN | Nearby-intent radar | Finds similar queries with low latency |
| DAX/MemoryDB | Fast drawers | Can support key-value access but not the required semantic-neighbor index |

#### Correct answer and reasoning

1. Different wording requires a semantic representation rather than an exact string key.
2. Generate a query embedding and index it with the corresponding query-response pair.
3. Use OpenSearch approximate k-NN to retrieve a nearby query, then reuse its response when the similarity threshold is acceptable.
4. This reduces redundant model calls while preserving consistent answers and low latency, so `C` is correct.

#### Option-by-option elimination

- **A.** DAX is a key-value cache; a `LIKE` filter does not reliably find paraphrases and is not vector retrieval.
- **B.** A MemoryDB hash set and range query do not provide a k-NN vector-distance search structure.
- **C.** OpenSearch k-NN with query embeddings directly supports semantic query-response reuse.
- **D.** Normalization and stemming cover limited morphological variation, not reliable paraphrase equivalence.

#### Exam strategy

For “different wording, same intent, consistent answer, fewer calls,” recall semantic caching: embedding, vector index, k-NN, and a similarity threshold. Exact keys, `LIKE`, GSIs, and stemming are lexical techniques; they do not replace semantic similarity search.

---

## Question 52

### 中文

#### 考点背景

本题考查 MCP Server 的部署边界、传输协议和认证。Lambda 适合承载无状态的 MCP Server 逻辑，API Gateway HTTP API 为远程 Agent 提供可访问的 HTTP 入口；Streamable HTTP Transport 才能把 MCP 请求作为远程 HTTP 通信送到该入口。Cognito 的 OAuth 2.1 则在入口建立用户认证与授权边界，确保不是任何拿到 Endpoint 的人都能读取用户信息。

最容易混淆的是“Lambda invoke 权限”和“最终用户授权”。给 Agent Lambda 调用另一个 Lambda 的权限，只描述 AWS 资源之间能否调用，不等于 MCP Client 使用了正确的远程 Transport，也不自动完成终端用户身份验证。STDIO 适合本地进程通过标准输入输出通信；Layer 是代码打包载体，不是可执行的 MCP 远程命令。

#### 场景比喻

像把档案管理员搬到云端：Lambda 是管理员办公室，API Gateway 是对外服务窗口，Streamable HTTP 是窗口的通话线路，Cognito 是门口核验身份证和通行证的人。给另一名员工“可以按门铃”的权限，不代表每位来访者都通过了身份核验；把仓库地址写在纸箱上也不会让纸箱变成管理员。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | MCP 管理员办公室 | 承载 MCP Server 的业务逻辑 |
| API Gateway HTTP API | 对外服务窗口 | 提供远程 HTTP 入口并代理到 Lambda |
| Streamable HTTP Transport | 远程通话线路 | 让 Agent MCP Client 通过 HTTP 与 Server 通信 |
| Amazon Cognito/OAuth 2.1 | 门卫与通行证 | 强制用户认证、授权后才能访问 |

#### 正确答案与推理

1. MCP Server 需要被远程 Agent 访问，不能只依赖本地进程通信。
2. Lambda 承载 Server，API Gateway HTTP API 提供远程入口。
3. Agent 使用 Streamable HTTP Transport 连接入口，协议和部署边界匹配。
4. Cognito OAuth 2.1 保护用户信息入口，覆盖授权要求，所以答案为 `C`。

#### 逐项排除

- **A.** 异步调用不是这里所需的 MCP 远程 Transport，也没有为最终用户提供明确的认证授权层。
- **B.** STDIO 面向本地进程标准输入输出，不适合通过 HTTP 访问部署在 Lambda 后面的远程 Server。
- **C.** Lambda、HTTP API、Streamable HTTP 与 Cognito OAuth 2.1 依次覆盖承载、传输和授权，正确。
- **D.** Layer ARN 只是代码层标识，不是 MCP Server 命令；把用户凭证放环境变量也没有建立安全的用户授权入口。

#### 解题方法

把 MCP 题拆成三问：Server 放哪里？远程还是本地用什么 Transport？用户如何认证？远程 Lambda 通常对应 HTTP API + Streamable HTTP；用户访问控制看 Cognito/OAuth。STDIO、Layer ARN 和 Lambda-to-Lambda 权限不要混同。

### English

#### Exam focus and background

This question tests three separate MCP concerns: where the server runs, how a remote client transports requests, and how end users are authorized. Lambda can host the server logic, an API Gateway HTTP API can expose a remote HTTP endpoint, and Streamable HTTP is the appropriate transport for an agent MCP client calling that endpoint. Cognito with OAuth 2.1 establishes the user authentication and authorization boundary before access to user information.

Do not confuse AWS resource invocation permission with end-user authorization. Allowing one Lambda to invoke another says something about AWS principals, but it does not select a suitable remote MCP transport or authenticate the person using the agent. STDIO is intended for local process communication. A Lambda layer packages code; its ARN is not a runnable MCP command.

#### Analogy

Move an archivist to the cloud: Lambda is the office, API Gateway is the public service window, Streamable HTTP is the conversation line, and Cognito is the guard checking identity and access passes. Giving another employee permission to ring the office does not authenticate every visitor, and writing a warehouse address on a box does not turn the box into an archivist.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda | MCP office | Hosts the MCP server logic |
| API Gateway HTTP API | Public service window | Provides and proxies the remote HTTP endpoint |
| Streamable HTTP transport | Remote conversation line | Connects the agent’s MCP client over HTTP |
| Cognito/OAuth 2.1 | Guard and access pass | Authenticates and authorizes users |

#### Correct answer and reasoning

1. The server must be reachable by a remote agent, so local-only process communication is insufficient.
2. Host the server in Lambda and expose it through an API Gateway HTTP API.
3. Configure the MCP client to use Streamable HTTP through that endpoint.
4. Protect the endpoint with Cognito OAuth 2.1 so only authorized users reach the user-information tools. Thus `C` is correct.

#### Option-by-option elimination

- **A.** Asynchronous invocation is not the required MCP remote transport, and the option lacks a clear end-user authentication boundary.
- **B.** STDIO is for local process input/output, not for an HTTP-accessed server behind Lambda.
- **C.** Lambda, an HTTP API, Streamable HTTP, and Cognito OAuth 2.1 map to hosting, transport, and authorization.
- **D.** A layer ARN is a packaging reference, not an executable MCP command; environment-variable credentials do not provide proper user authorization.

#### Exam strategy

Break MCP questions into server placement, transport, and identity. A remote Lambda-hosted server points to an HTTP API and Streamable HTTP; user access control points to Cognito/OAuth. Do not treat Lambda-to-Lambda permission, STDIO, or a layer ARN as substitutes for an authenticated remote endpoint.

---

## Question 53

### 中文

#### 考点背景

本题考查多 Agent 顺序业务流程中的状态编排和故障隔离。搜索、推荐、奖励计算、下单是四个有先后依赖的步骤：没有搜索结果就不能推荐，推荐结果又是奖励计算和下单的输入。Step Functions State Machine 可以把它们显式建模成四个 Task，并为每个 Task 单独定义 Retry 与 Catch；某一步暂时失败时重试，持续失败时转入降级分支或保留部分结果。

“Graceful Degradation”要求失败被局部化，而不是让一段 Lambda 代码把所有异常吞在一起。独立 Task 让执行历史、重试次数和失败原因可观察，也能只跳过奖励计算或暂缓下单，而不必重做成功的搜索。API Gateway 是否存在不是核心；核心是顺序、每步重试和每步降级。

#### 场景比喻

像机场四道检查：查航班、推荐座位、计算里程、出票。Step Functions 是有四个闸门的调度员，每个闸门都有自己的备用路线；推荐服务短暂故障只重开第二道门，不能把已经完成的第一道门全部重跑，更不能把四道检查藏在一个黑箱里。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Step Functions State Machine | 四闸门调度员 | 表达三个 Agent 与奖励 Lambda 的顺序依赖 |
| Step Functions Task | 单道检查闸 | 独立调用一个 Agent 或奖励计算 Lambda |
| Retry | 闸门重试按钮 | 对可恢复的瞬时错误按步骤重试 |
| Catch/Fallback | 备用通道 | 将不可恢复失败转为优雅降级或补偿路径 |

#### 正确答案与推理

1. 三个 Agent 和奖励计算必须按顺序执行，不能由并行或隐式调用承担依赖。
2. 四个逻辑动作应成为四个可观察 Task，状态机负责传递上一步输出。
3. 每个 Task 都有独立 Retry/Catch，故障只影响对应步骤，并可实现降级。
4. 因此 `B` 的维护边界最清晰，运维效率最高。

#### 逐项排除

- **A.** 把重试和 fallback 写在一个编排 Lambda 中，仍需自行维护状态、异常和可观测性，且 API Gateway 不是核心收益。
- **B.** 四个 Task 加逐步 Retry/Catch 同时满足顺序执行、故障隔离与优雅降级，正确。
- **C.** 每个 Agent 有机制但总编排仍在自定义 Lambda 中，重试策略分散且维护复杂。
- **D.** 只有一个 Task 包住整个 Lambda，状态机无法独立重试或捕获某一个 Agent 的失败，降级粒度太粗。

#### 解题方法

看到“多个步骤必须顺序 + 每步失败可重试/降级”，优先 Step Functions 多 Task。检查 Retry/Catch 是否贴在每个步骤，而不是只包住总 Lambda；能否保留已成功步骤，是判断运维效率的关键。

### English

#### Exam focus and background

This question tests explicit orchestration and failure isolation for a sequential multi-agent workflow. Search, recommendation, reward calculation, and order placement have dependencies: recommendation needs search output, and rewards and ordering depend on the recommendation. A Step Functions state machine can model four separate tasks and give each task its own Retry and Catch behavior. A transient failure can be retried; a persistent failure can move to a fallback or degraded path.

Graceful degradation is easier when failures are localized. Separate tasks provide execution history, step-level retry counts, and clear failure causes. The workflow can skip or defer reward calculation without repeating a successful search. Whether an API Gateway sits in front is secondary; the central requirements are ordering, per-step retries, and per-step degradation.

#### Analogy

Imagine four airport gates: find a flight, recommend a seat, calculate miles, and issue the ticket. Step Functions is the dispatcher with a retry and alternate lane at each gate. If the recommendation gate is briefly unavailable, reopen that gate rather than repeating the completed search, and do not hide all four gates inside one black box.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Step Functions state machine | Four-gate dispatcher | Expresses dependencies among agents and reward calculation |
| Step Functions Task | One inspection gate | Invokes one agent or the rewards Lambda independently |
| Retry | Gate retry button | Retries recoverable failures at the relevant step |
| Catch/fallback | Alternate lane | Converts persistent failure into graceful degradation |

#### Correct answer and reasoning

1. The three agents and the rewards calculation must run sequentially.
2. Model the four logical actions as four observable tasks and pass outputs between them.
3. Configure Retry and Catch for each task so failures remain local and can degrade gracefully.
4. Therefore `B` provides the clearest and most operationally efficient design.

#### Option-by-option elimination

- **A.** A single orchestration Lambda still requires custom state, error, and observability logic; API Gateway does not solve the core workflow requirement.
- **B.** Four tasks with step-specific Retry and Catch directly provide sequencing, isolation, and graceful degradation.
- **C.** Retry/fallback is scattered across agents and a custom orchestrator, increasing maintenance and making policy inconsistent.
- **D.** One task wrapping the entire Lambda prevents independent retry or catch handling for each agent and makes degradation too coarse.

#### Exam strategy

For “sequential steps + retry and graceful degradation per step,” prefer a multi-task Step Functions state machine. Check whether Retry/Catch is attached to every task rather than only to one wrapper Lambda. Preserving successful prior steps is the operational-efficiency clue.

---

## Question 54

### 中文

#### 考点背景

本题考查确定性文档审核流水线与 Agent/Flow 的边界。模板在 S3 中，流程只需读取对象、取出指定部分、调用 Bedrock 模型审核，再把结果写回同一 Bucket；这是固定的 Read → Parse → Invoke → Write 链路，不需要 Agent 根据自然语言决定工具，也不需要额外的 Action Group。Step Functions Express Workflow 能以较轻量的方式承载短时、高吞吐、可预测的步骤。

Intrinsic Function 配合 Pass Step 可在状态数据中完成题目所需的字段提取/转换，S3 `GetObject` 和 `PutObject`、Bedrock `InvokeModel` 则对应输入、中间处理和输出。考试的关键词是“预定义模板、特定部分、固定工作流、最少额外推理”；一旦流程确定，就不要为它引入 Agent 的规划和 Lambda 维护层。

#### 场景比喻

像流水线审核印刷稿：先从文件柜取出稿件，沿固定标记剪出“免责声明”段落，送入审稿机，最后把盖章结果放回原柜。用 Agent 就像让一位自由职业编辑先猜要做什么、再找工具，固定模板反而多了不必要的决策。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Step Functions Express | 轻量流水线控制器 | 编排固定的短流程并减少自定义服务代码 |
| Amazon S3 `GetObject`/`PutObject` | 文件柜取件/归档 | 读取模板并把审核响应写回原 Bucket |
| `Pass` + Intrinsic Functions | 固定裁纸刀 | 从状态中的模板数据提取指定部分 |
| Bedrock `InvokeModel` | 审稿机 | 对提取出的通信内容执行模型审核 |

#### 正确答案与推理

1. 原始通信位于 S3，第一步应使用 `GetObject` 读取。
2. 指定部分的提取是确定性数据变换，放在带 Intrinsic Function 的 `Pass` 中。
3. 将解析结果传给 Bedrock `InvokeModel`，执行审核。
4. 用 `PutObject` 把模型响应写回原 Bucket；整个固定链路正是 `B`，所以答案为 `B`。

#### 逐项排除

- **A.** Bedrock Flows 和 Agent Step 能编排更复杂的生成流程，但本题只需确定性解析和调用，组件与推理层多余。
- **B.** GetObject、Pass/Intrinsic、InvokeModel、PutObject 一一对应四个步骤，正确。
- **C.** Agent 让模型规划解析和 Action Group，增加非必要推理；固定模板不需要 Agent 决策。
- **D.** 三个 Lambda 会把简单解析、调用和写回变成自定义维护点，且仍不如状态机直接。

#### 解题方法

看到预定义模板和固定的读取—提取—调用—写回，选择 Step Functions 的服务集成与 Intrinsic Functions。Agent 适合需要规划和工具选择的任务；确定性字段处理不应被 Agent 或多个 Lambda 放大。

### English

#### Exam focus and background

This is a boundary question between a deterministic document workflow and an agentic workflow. The object is in S3, a specific portion must be extracted, Bedrock must review it, and the response must be written back. That is a fixed Read → Parse → Invoke → Write sequence. It does not require an agent to plan, select tools, or decide what to do next. Step Functions Express provides a lightweight orchestration mode for a predictable short workflow.

An intrinsic function in a Pass state can perform the required state-data extraction or transformation. S3 `GetObject` and `PutObject` and Bedrock `InvokeModel` map directly to the input, model, and output steps. The exam clues are “pre-defined template,” “specific portion,” and a fixed workflow. Introducing agents or several Lambda functions would add decision and maintenance layers without adding a required capability.

#### Analogy

Imagine reviewing printed material on a production line. Retrieve the document from a cabinet, cut out the disclaimer section at a known marker, send it to a review machine, and file the stamped result back in the cabinet. Hiring a free-form editor to guess the job and find tools would add complexity to a fixed template process.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Step Functions Express | Lightweight line controller | Orchestrates the fixed short workflow |
| S3 `GetObject`/`PutObject` | Retrieve/file cabinet | Reads the template and writes the review result back |
| `Pass` plus intrinsic functions | Fixed paper cutter | Extracts the required portion from state data |
| Bedrock `InvokeModel` | Review machine | Reviews the extracted communication |

#### Correct answer and reasoning

1. Use S3 `GetObject` to retrieve the original communication.
2. Use a Pass state with intrinsic functions to extract the specified portion deterministically.
3. Send that value to Bedrock `InvokeModel` for review.
4. Use S3 `PutObject` to write the response back to the original bucket. This exact chain is `B`.

#### Option-by-option elimination

- **A.** Bedrock Flows and an agent step can orchestrate richer generative behavior, but they are unnecessary for fixed parsing and invocation.
- **B.** The GetObject, Pass/intrinsic, InvokeModel, and PutObject steps map directly to the required workflow.
- **C.** An agent and action group introduce planning and tool selection where the template process is deterministic.
- **D.** Three Lambda functions create custom maintenance points for retrieval, parsing, invocation, and storage that the state machine can handle directly.

#### Exam strategy

For a predefined template and fixed retrieve-extract-invoke-write chain, choose Step Functions service integrations and intrinsic functions. Use agents when planning or tool selection is required; do not turn deterministic field extraction into an agent or a set of wrapper Lambdas.

---

## Question 55

### 中文

#### 考点背景

本题考查生成式 AI 质量评估的“自动化规模 + 定向人工复核”组合。准确性和对话适当性属于语义质量，适合用 Amazon Bedrock Evaluations 配合 Judge Model 在大量响应上统一打分；金融政策合规属于运行时安全约束，可由自定义 Bedrock Guardrails 检查；只有关键或被标记的交互才送给 Amazon A2I，让人工把时间花在高风险样本上。

不要把 CloudWatch 告警当成语义评估器。指标和日志能告诉你错误率、延迟或某类事件上升，却不能独立判断回答是否准确、是否合适。相反，若让专家审核每一条响应，质量可能高但无法经济扩展。题目的理想闭环是模型评估发现质量、Guardrails 拦截政策问题、A2I 对边界案例进行人工反馈。

#### 场景比喻

像金融客服质检中心：Judge Model 是自动抽检员，快速给十万张答卷打语义分；Guardrails 是合规闸门，看到违规表述就拦下；A2I 是资深审计员，只接收机器标红的高风险答卷，而不是从头读完每一张。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Evaluations | 自动质检线 | 大规模评估准确性、适当性等生成质量 |
| Claude Sonnet Judge Model | 资深自动评分员 | 作为评审模型比较和评分响应 |
| Amazon Bedrock Guardrails | 合规闸门 | 检查金融政策并阻断不合规内容 |
| Amazon A2I | 人工复核台 | 只把关键/标记交互路由给人工 |

#### 正确答案与推理

1. 大规模准确性与对话质量需要可重复的自动评估，Bedrock Evaluations + Claude Sonnet Judge 覆盖它。
2. 金融政策属于安全与合规控制，用自定义 Guardrails 施加规则。
3. 对高风险、低置信或被标记样本使用 A2I，保留人工判断但控制成本。
4. 这正是 `B` 的自动评估、合规护栏和定向人工三层组合。

#### 逐项排除

- **A.** 全量人工评分难以随响应量扩展，SageMaker Notebook 也不能替代实时/系统化评估闭环。
- **B.** Judge Model 评估质量、Guardrails 检查政策、A2I 审核关键交互，完整且可扩展，正确。
- **C.** Lex 和静态数据库适合确定性对话逻辑，不是通用生成质量评估或目标式人工复核方案。
- **D.** CloudWatch 能告警模式和指标，但不能判断语义准确性，也没有完整的 A2I 定向审核流程。

#### 解题方法

将要求分成三类：生成质量看 Bedrock Evaluations/Judge，安全合规看 Guardrails，关键案例人工看 A2I。看到“全部人工”先考虑扩展性；看到 CloudWatch 先问它观测的是指标还是语义内容。

### English

#### Exam focus and background

This question tests the combination of automated evaluation at scale and targeted human review. Accuracy and conversational appropriateness are semantic quality dimensions, so Bedrock Evaluations with a judge model can score many responses consistently. Financial-policy compliance is a safety control, so custom Bedrock Guardrails can inspect and block policy violations. Amazon A2I should receive only critical or flagged interactions so human effort is focused on high-risk cases.

CloudWatch metrics and alarms are not semantic judges. They can show that latency, errors, or an event pattern increased, but they cannot independently determine whether a response is accurate or appropriate. Conversely, having experts score every response may be thorough but does not scale economically. The desired loop is automated quality measurement, policy enforcement, and selective human review.

#### Analogy

Picture a financial-service quality center. The judge model is an automated reviewer that scores a huge stack of answers. Guardrails are the compliance gate that stops prohibited language. A2I is the senior auditor who receives only machine-flagged or high-risk cases instead of reading every answer.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock Evaluations | Automated quality line | Measures accuracy and appropriateness at scale |
| Claude Sonnet judge model | Senior automated scorer | Judges and scores generated responses |
| Bedrock Guardrails | Compliance gate | Checks responses against financial policies |
| Amazon A2I | Human review desk | Reviews critical or flagged interactions |

#### Correct answer and reasoning

1. Use Bedrock Evaluations and a Claude Sonnet judge model for scalable accuracy and appropriateness assessment.
2. Use custom Guardrails for financial-policy compliance controls.
3. Route only critical or flagged interactions to A2I for human judgment.
4. This three-layer design is exactly the combination in the recorded answer `B`.

#### Option-by-option elimination

- **A.** Having experts score every response is expensive and difficult to scale; notebooks do not create the complete operational evaluation loop.
- **B.** Judge-model evaluation, Guardrails, and targeted A2I review cover quality, compliance, and human oversight.
- **C.** Lex and a static compliance database provide deterministic bot logic, not general generative-quality evaluation with targeted review.
- **D.** CloudWatch can alert on metrics or patterns but cannot semantically assess answers or supply the described human-review workflow.

#### Exam strategy

Classify each requirement: generation quality maps to Bedrock Evaluations and a judge, safety/compliance maps to Guardrails, and exceptional cases map to A2I. Treat “human reviews all responses” as a scalability warning. Treat CloudWatch as observability, not a semantic evaluator.

---

## Question 56

### 中文

#### 考点背景

本题考查面向多个医疗领域的 Supervisor–Collaborator Multi-Agent Architecture。临床、保险资格、预约和理赔既有不同术语、流程和数据权限，又需要随着新功能加入而扩展。一个 Supervisor 负责自然语言意图分类和路由；各个 Collaborator 专注自己的领域并连接专属 Knowledge Base，IAM Filtering 再把检索权限限制在适当数据范围内。这样路由边界、知识边界和团队 ownership 都清晰。

数千个并行交互要求组件可独立扩展，而不是把所有领域规则塞进一个大 Agent Instruction。独立专业 Agent 可以分别更新知识和动作；Supervisor 只需识别意图。共享 Knowledge Base 或并行询问多个 Supervisor 会把不同领域混在一起，既浪费调用，也增加患者拿到跨域、错误答案的风险。

#### 场景比喻

像一所大型医院：前台分诊 Supervisor 先听懂“我想查理赔”还是“我要预约”；随后把患者送到理赔、预约或临床专科柜台。每个柜台只打开自己的病历柜，IAM Filtering 是钥匙管理。让所有专科同时翻同一大柜子，速度和隐私都会失控。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Bedrock Supervisor Agent | 医院分诊台 | 做自然语言意图分类并路由请求 |
| Bedrock Collaborator Agents | 专科柜台 | 分别处理临床、保险、预约和理赔能力 |
| Bedrock Knowledge Bases | 专科病历柜 | 为每个领域提供专属 RAG 资料 |
| IAM Filtering | 病历钥匙系统 | 限制 Agent 只能访问获准领域数据 |

#### 正确答案与推理

1. 需求有多个清晰领域，必须先分类，再把问题交给正确专家。
2. 各领域独立 Knowledge Base 保证检索上下文不混杂，IAM Filtering 进一步控制访问。
3. 专业 Collaborator 可以各自扩展和维护，Supervisor 作为统一入口支持并行患者交互。
4. 所以 `A` 同时满足可扩展性、领域准确性、权限隔离和规模要求。

#### 逐项排除

- **A.** 一个 Supervisor 路由到专属 Collaborator，配合独立 Knowledge Base 与 IAM Filtering，边界清楚，正确。
- **B.** 每个部门再设 Supervisor 并依赖人工 Handoff，层次和交接过多，难以扩展到数千并行交互。
- **C.** 一个通用 Agent 里堆 Action Group 和规则，会形成难维护的单体路由与权限边界。
- **D.** 多个 Supervisor 并行访问同一 Knowledge Base，可能产生无关或混合答案，并浪费模型调用。

#### 解题方法

看到“多领域 + 以后加功能 + 领域专属回答/数据隔离”，画出一层 Supervisor 加多个 Collaborator，再给每个 Collaborator 专属 Knowledge Base。人工 handoff、单体 Agent、共享知识库通常是扩展性或隔离性的警报。

### English

#### Exam focus and background

This question tests a Supervisor–Collaborator multi-agent architecture for distinct healthcare domains. Clinical care, insurance eligibility, scheduling, and claims have different terminology, workflows, and data boundaries. One supervisor performs natural-language intent classification and routes the request. Each collaborator specializes in one domain and uses a domain-specific knowledge base, while IAM filtering limits which data that agent can retrieve.

Thousands of parallel interactions favor independently scalable components rather than a single set of instructions containing every department’s rules. Specialized collaborators can evolve their knowledge and actions independently, while the supervisor remains focused on routing. A shared knowledge base or several supervisors querying it in parallel can mix domains, waste calls, and increase the chance of an inappropriate patient response.

#### Analogy

Imagine a large hospital. The front desk is the supervisor: it understands whether a patient needs claims help or an appointment and sends the patient to the correct specialty desk. Each desk opens only its own records cabinet; IAM filtering manages the keys. Asking every specialty to search one giant cabinet would harm both speed and privacy.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Bedrock Supervisor Agent | Hospital triage desk | Classifies intent and routes the request |
| Bedrock Collaborator Agents | Specialty desks | Handle clinical, insurance, scheduling, and claims tasks |
| Bedrock Knowledge Bases | Specialty record cabinets | Provide domain-specific RAG context |
| IAM filtering | Record-key system | Restricts each agent to permitted domain data |

#### Correct answer and reasoning

1. Multiple domains require intent classification before specialized handling.
2. Separate knowledge bases keep retrieval context isolated, and IAM filtering limits access.
3. Independent collaborators can be extended and maintained by domain while the supervisor remains the common entry point.
4. Thus `A` satisfies scalability, domain accuracy, and data isolation.

#### Option-by-option elimination

- **A.** One supervisor routes to specialized collaborators with separate knowledge bases and IAM filtering, giving clear extension and access boundaries.
- **B.** Multiple supervisors plus manual handoffs create unnecessary layers and do not scale cleanly to thousands of interactions.
- **C.** A monolithic agent with many action groups and rule-based instructions makes routing and permissions harder to maintain.
- **D.** Parallel supervisors sharing one knowledge base can produce mixed or irrelevant answers and waste inference calls.

#### Exam strategy

For “multiple domains + future onboarding + domain-specific answers and isolation,” picture one supervisor and specialized collaborators, each with its own knowledge base and access boundary. Manual handoffs, a monolith, and a shared knowledge base are warning signs for scale or isolation.

---

## Question 57

### 中文

#### 考点背景

本题考查以 IaC 和单一流水线保持 Development/Production 一致，同时把 FM 选择变成可配置的测试变量。一个 CDK Application 定义共同基础设施，`FoundationModel.fromFoundationModelId()` 通过模型 ID 集成 Bedrock FM；一个 CodePipeline 再用两个 Deployment Stage 分别部署开发和生产，并可在各环境传入不同模型配置。这样 Product Owner 试验模型时改环境配置，不必复制应用或手工重建资源。

这里的“一个”不是为了少建资源，而是为了减少漂移。独立 CDK 应用和独立 Pipeline 会让模板、权限和部署策略逐渐分叉；ProvisionedModel ARN 把选择绑定到已配置的吞吐资源，灵活性也低。Development 不能靠“参考生产手工重建”，否则它不再是可重复、可审计的环境。

#### 场景比喻

像同一家工厂生产两条装配线：一份蓝图规定结构，流水线先部署测试车间，再部署生产车间；每条线可插入不同型号发动机试跑。复制两份蓝图或让工程师手工照着成品搭车间，最终尺寸和螺丝都会漂移。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS CDK Application | 共享工厂蓝图 | 用一套 IaC 定义应用与 FM 集成 |
| `FoundationModel.fromFoundationModelId()` | 可替换发动机接口 | 按模型 ID 引用 Bedrock FM，便于测试切换 |
| AWS CodePipeline | 装配线总控 | 在同一 Pipeline 中编排多环境部署 |
| Development/Production Stages | 测试车间/量产车间 | 分别部署环境并注入各自模型配置 |

#### 正确答案与推理

1. 两个环境需要同样的可重复基础设施，故使用一个 CDK Application。
2. FM 需要按环境测试和切换，使用 FoundationModel 的 ID 引用比固定 Provisioned ARN 灵活。
3. 一个 CodePipeline 的多个 Deployment Stage 能按顺序/策略部署 Development 与 Production。
4. 因此 `C` 同时保持一致性、可切换性和可审计部署，答案为 `C`。

#### 逐项排除

- **A.** 多个 Pipeline 增加维护分叉，ProvisionedModel ARN 也把测试绑定到特定预配置模型资源，切换不够灵活。
- **B.** 每个环境复制 CDK 应用和 Pipeline，容易产生 IaC 漂移，运维成本高于单应用单流水线。
- **C.** 一个 CDK、FoundationModel ID 和分环境 Pipeline Stages 兼顾复用与独立配置，正确。
- **D.** 手工重建 Development 破坏 IaC 一致性；只管理 Production 也无法可靠测试环境差异。

#### 解题方法

看到“Development + Production + 试不同 FM + 易切换”，优先“一份 IaC、一个 Pipeline、多 Stage、环境参数化”。区分 FoundationModel ID（灵活引用）与 Provisioned ARN（绑定资源），并把手工重建视为环境漂移信号。

### English

#### Exam focus and background

This question tests infrastructure-as-code consistency while making foundation-model selection a configurable test variable. One CDK application defines the shared infrastructure, and `FoundationModel.fromFoundationModelId()` integrates a Bedrock foundation model by model ID. One CodePipeline can then contain separate deployment stages for development and production, with each environment receiving its own model configuration. A product owner can test a different model without duplicating the application or rebuilding resources manually.

The value of “one” is drift prevention, not simply fewer resources. Separate CDK applications and pipelines tend to diverge in templates, permissions, and deployment behavior. A provisioned-model ARN binds the design to a particular provisioned resource and is less flexible for model experiments. Manually recreating development from production is not repeatable or auditable infrastructure.

#### Analogy

Think of one factory blueprint used for two assembly lines. The pipeline deploys a test workshop and then a production workshop, and each line can try a different engine model. Copying the blueprint or rebuilding a workshop by looking at the finished product eventually creates different dimensions and missing screws.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS CDK application | Shared factory blueprint | Defines reusable infrastructure and model integration |
| `FoundationModel.fromFoundationModelId()` | Replaceable engine interface | References a Bedrock FM by ID for model switching |
| AWS CodePipeline | Assembly-line controller | Coordinates deployments across environments |
| Development/Production stages | Test and production workshops | Deploy each environment with its own model configuration |

#### Correct answer and reasoning

1. Both environments need repeatable infrastructure, so use one CDK application.
2. Model experiments require flexible model selection, so reference the FM by model ID rather than a fixed provisioned ARN.
3. One CodePipeline with a deployment stage for each environment provides controlled, consistent promotion.
4. Therefore `C` preserves reuse, model flexibility, and auditable deployment.

#### Option-by-option elimination

- **A.** Multiple pipelines increase maintenance divergence, and a provisioned-model ARN binds testing to specific provisioned resources.
- **B.** Duplicating CDK applications and pipelines makes infrastructure drift more likely and increases operational work.
- **C.** One CDK application, a model-ID reference, and environment-specific pipeline stages match the requirements.
- **D.** Manually recreating development breaks IaC consistency and cannot reliably reproduce environment differences.

#### Exam strategy

For development and production plus model experiments, look for one IaC definition, one pipeline, multiple stages, and environment parameters. Distinguish a flexible foundation-model ID from a resource-bound provisioned ARN. Manual recreation is a strong drift warning.

---

## Question 58

### 中文

#### 考点背景

本题把三个边界放在一起：酒店之间的访问隔离、房间可用性的近实时更新，以及高峰期的稳定性。Multi-Account Structure 为每家酒店建立账户边界，分别部署 Knowledge Base 后，数据、权限和运维责任天然按酒店分开；Direct Data Ingestion 适合把 PMS 的关键可用性变化快速送进对应知识库；不那么时效敏感的酒店资料则可用计划同步，避免所有数据都走高频路径。

这是“隔离优先”的架构题。CloudTrail 记录谁访问了什么，但它是审计，不是阻止跨酒店访问的授权边界；集中式 Knowledge Base 即使配 Resource-Based Policy，也会把策略和数据集中在一起，复杂度与误配风险更高。集中 Agent 和 Permission Set 可以补充身份管理，却没有直接解决实时摄取与领域数据隔离的整体组合。

#### 场景比喻

像连锁酒店给每家分店发一间独立保险库：房态变化是前台立刻递进来的急件，Direct Data Ingestion 直接更新本店保险库；宣传册和历史介绍可以夜间批量归档。把所有酒店文件放进总部一个大柜子，再靠审计录像追责，不能代替真正的锁。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Multi-Account Structure | 每店独立保险库 | 在账户层隔离酒店数据和访问边界 |
| Amazon Bedrock Knowledge Base | 酒店专属资料柜 | 为每家酒店提供独立检索语料 |
| Direct Data Ingestion | 房态急件通道 | 近实时更新房间可用性信息 |
| Scheduled Synchronization | 夜间归档车 | 定期同步时效性较低的资料，平衡成本与负载 |

#### 正确答案与推理

1. 每家酒店要彼此独立的访问控制，最强的边界是多账户加独立 Knowledge Base。
2. 房间可用性是快速变化数据，使用 Direct Data Ingestion，不能等普通批量同步。
3. 酒店资料时效性不同，对低优先数据定时同步，减少不必要的实时负载。
4. 独立资源也能在高峰期按酒店扩展，故答案为 `C`。

#### 逐项排除

- **A.** CloudTrail 只是记录访问，不能执行酒店级隔离；统一知识库还把所有数据混在一起。
- **B.** 中央 Knowledge Base 即使有资源策略也集中数据和权限，事件链复杂，隔离强度与实时设计不如按酒店拆分。
- **C.** 多账户、每店知识库、Direct Data Ingestion 和定时同步分别命中隔离、实时性与负载要求，正确。
- **D.** 集中 Agent 和 Permission Set 可做身份分配，却没有直接提供每店独立知识边界及房态近实时摄取。

#### 解题方法

遇到“租户/酒店彼此隔离 + 部分数据实时 + 峰值稳定”，先选资源边界，再按数据时效分层摄取。审计日志不等于访问控制；集中路由不等于数据隔离；Direct Data Ingestion 与计划同步应按业务时效组合。

### English

#### Exam focus and background

Three boundaries matter here: isolation between hotels, near-real-time room availability, and stable peak performance. A multi-account structure gives each hotel an account boundary. A separate Bedrock knowledge base per hotel keeps data, permissions, and operational ownership aligned. Direct data ingestion is appropriate for rapidly changing PMS availability, while less time-sensitive hotel information can be synchronized on a schedule.

This is an isolation-first architecture question. CloudTrail records access for auditing; it does not prevent cross-hotel access. A centralized knowledge base with resource policies still concentrates data and policy complexity and increases misconfiguration risk. A centralized agent and permission sets may help identity management, but they do not by themselves solve the combination of independent knowledge boundaries and real-time ingestion.

#### Analogy

Imagine giving every hotel its own secure vault. A room-status change is an urgent note delivered directly to that hotel’s vault. Brochures and historical details can arrive on a nightly archive truck. Putting every hotel’s files in headquarters’ large cabinet and reviewing audit footage later does not replace an actual lock.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Multi-account structure | Separate hotel vaults | Isolates data and access at the account boundary |
| Bedrock Knowledge Base | Hotel-specific records cabinet | Provides each hotel’s retrieval corpus |
| Direct data ingestion | Room-status express lane | Updates availability near real time |
| Scheduled synchronization | Nightly archive truck | Refreshes less time-sensitive information economically |

#### Correct answer and reasoning

1. Independent hotel access controls favor separate accounts and separate knowledge bases.
2. Room availability changes quickly, so use direct data ingestion for that information.
3. Synchronize less critical content on a schedule to balance load and freshness.
4. Separately managed resources can also scale during peaks, making `C` the recorded answer.

#### Option-by-option elimination

- **A.** CloudTrail is an audit record, not an access-control boundary, and a single knowledge base combines all hotel data.
- **B.** A centralized knowledge base concentrates data and policy, and the event path is more complex than separate hotel resources.
- **C.** Multi-account isolation, per-hotel knowledge bases, direct ingestion, and scheduled synchronization match every requirement.
- **D.** A central agent and permission sets do not directly provide independent knowledge boundaries or near-real-time availability ingestion.

#### Exam strategy

For tenant or hotel isolation plus mixed freshness, choose the resource boundary first and then split ingestion by data criticality. Remember that audit logs are not authorization, centralized routing is not data isolation, and direct ingestion and scheduled sync can be combined.

---

## Question 59

### 中文

#### 考点背景

本题考查 Serverless Inference API 的运行时 Provider 切换和配置发布生命周期。Lambda 应该只包含“读取当前配置并调用对应模型”的稳定代码；当前 Provider、模型 ID 或参数属于可变配置。AWS AppConfig 能保存并发布这类配置，提供验证、受控部署和回滚，Lambda 通过 AppConfig Agent 在运行时取得新值，因此切换 Provider 不需要改代码或重新部署。

Parameter Store 或 S3 也能成为配置来源，但“能读取”不等于“能安全发布”。若只把值放在 Parameter Store，验证、渐进部署和回滚仍需额外流程；S3 再套 AppConfig 的来源链路在本题中增加不必要的间接层。最少运维的判断，应选直接表达配置生命周期的 AppConfig。

#### 场景比喻

像餐厅每天更换咖啡豆供应商：服务员的点单流程不变，只看厨房当天的“供应商牌”。AppConfig 是带试营业、验货和撤回按钮的牌子；若新供应商出问题，立刻回滚。把供应商写死在每个服务员的手册里，换一次就要重印整套手册。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS AppConfig | 可回滚的运行时控制台 | 管理 Provider 配置并提供验证、安全部署与回滚 |
| AppConfig Agent | 厨房当天牌读取器 | 让 Lambda 运行时获得当前配置 |
| AWS Lambda | 稳定点单服务员 | 根据配置选择 Bedrock 模型，不因 Provider 变化改代码 |
| Amazon Bedrock | 多家咖啡供应商 | 承载被动态选择的 AI 模型 |

#### 正确答案与推理

1. Provider 必须从 Lambda 代码中解耦，成为运行时配置。
2. Lambda 通过 AppConfig Agent 读取活动 Provider，并按值选择 Bedrock 模型。
3. AppConfig 的验证、受控发布和回滚满足配置变更安全性，而不是只满足存取。
4. 所以记录答案 `B` 是最低运维、无需重新部署的方案。

#### 逐项排除

- **A.** Parameter Store 的运行时读取可切换值，但本身不覆盖题目要求的完整验证、受控发布和回滚生命周期。
- **B.** AppConfig 直接提供运行时配置治理；Agent 让 Lambda 无需改动即可读取，正确。
- **C.** 每个 Lambda 硬编码 Provider，切换仍需修改集成或代码，违背无需重新部署。
- **D.** S3 加 AppConfig 来源可以存配置，但多一层对象和来源管理；本题没有需要 S3 的理由，运维不如直接 AppConfig 简洁。

#### 解题方法

关键词组合“runtime switch + no redeploy + validate + safe rollout + rollback”几乎直接指向 AppConfig。Parameter Store/S3 解决存储，AppConfig 解决配置生命周期；硬编码 Lambda 和手工 Integration Target 直接排除。

### English

#### Exam focus and background

This question tests runtime provider switching and configuration-release governance for a serverless inference API. Lambda should contain stable logic: read the active configuration and invoke the selected model. The provider, model ID, and related settings are changeable configuration. AppConfig can manage and publish that configuration with validation, controlled deployment, and rollback. The AppConfig Agent lets Lambda obtain the active value at runtime, so changing providers does not require code changes or redeployment.

Parameter Store or S3 can store a value, but “can be read” is not the same as “has a safe release lifecycle.” With Parameter Store alone, validation, rollout, and rollback still need extra mechanisms. Putting JSON in S3 and adding it as an AppConfig source adds indirection that the scenario does not need. The least-operations answer is the service that directly models configuration lifecycle management.

#### Analogy

Imagine a restaurant changing coffee suppliers. The waiter’s ordering process stays the same; the kitchen displays today’s supplier card. AppConfig is a card with validation, a controlled trial, and a rollback button. Hardcoding the supplier in every waiter’s manual means reprinting the manuals for every change.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS AppConfig | Rollback-capable control console | Manages provider settings with validation and safe release |
| AppConfig Agent | Current-supplier card reader | Gives Lambda the active configuration at runtime |
| AWS Lambda | Stable ordering service | Selects the Bedrock model from configuration |
| Amazon Bedrock | Model-provider marketplace | Hosts the dynamically selected AI model |

#### Correct answer and reasoning

1. Decouple the provider from Lambda code and represent it as runtime configuration.
2. Have Lambda read the active provider through the AppConfig Agent and select the Bedrock model.
3. Use AppConfig’s validation, controlled deployment, and rollback for safe changes.
4. Thus `B` meets the no-redeployment and low-operations requirements.

#### Option-by-option elimination

- **A.** Parameter Store can be read at runtime, but it does not by itself provide the complete validation, controlled rollout, and rollback lifecycle requested.
- **B.** AppConfig directly provides configuration governance, and the Agent enables unchanged Lambda code to read it.
- **C.** Hardcoded providers require integration or code changes when switching, violating the no-redeployment requirement.
- **D.** S3 plus an AppConfig source can work as storage, but it adds unnecessary object and source-management indirection for this scenario.

#### Exam strategy

The combination “runtime switch, no redeploy, validate, safe rollout, rollback” strongly points to AppConfig. Parameter Store and S3 solve storage; AppConfig solves the configuration lifecycle. Hardcoded Lambda targets and manual integration changes are immediate eliminators.

---

## Question 60

### 中文

#### 考点背景

本题考查高峰期 Bedrock 调用的自适应重试、熔断、流式恢复和 Token-Aware Request Handling。指数退避加 Jitter 让不同请求不要在同一秒再次冲向服务；Circuit Breaker 在错误率过高时暂时停止重试，避免把故障放大，并在可用性恢复后再允许流量回来。Streaming Handler 则要识别 Chunk Delivery Timeout、保存已成功接收的数据，并用应用层检查点减少断线重连时的重复处理。

Token 也必须按请求计算，而不是给所有用户一个过于僵硬的全局上限：长问题应在调用前保留输入、输出和安全余量，必要时做有意识的截断或摘要。需要注意考试语境中的“从最后位置继续”是应用层的检查点/幂等恢复设计，不应误读成 Bedrock 任意响应天然支持客户端按 Chunk 偏移续传。记录答案仍以 `B` 为准，因为它最完整地组合了自适应保护、流式处理和 token-aware 思路。

#### 场景比喻

像暴雨中的高速公路收费站：指数退避和 Jitter 让车辆错峰，Circuit Breaker 在闸机故障时暂时封闭入口；收费员把已扫描的车票编号记下，恢复后从检查点继续，而不是让所有车从头排队。每辆车还要先量尺寸，超过车道限制的货物必须在进入前重新分装。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS SDK/Bedrock retry layer | 错峰交通控制 | 以指数退避和 Jitter 处理瞬时错误 |
| Circuit Breaker | 故障闸门 | 错误率过高时停止放大重试，恢复后再放行 |
| Bedrock streaming API/handler | 分段收费车道 | 监控 Chunk 超时并处理部分响应 |
| Token-aware request handler | 车辆尺寸检查 | 按请求预算输入、输出和上下文，防止超限截断 |

#### 正确答案与推理

1. FM 瞬时失败需要重试，但固定时间会造成同步重试风暴，因此使用指数退避与 Jitter。
2. 高峰错误率上升时，Circuit Breaker 暂停重试，保护 Bedrock 和应用自身。
3. 流式响应要检测 Chunk 超时、缓存已收数据，并通过应用层检查点在恢复后避免从头重复处理。
4. 每个请求在调用前计算 token budget，给不同长度的复杂咨询留出合理空间。
5. `B` 是唯一同时覆盖自适应可用性、背压式保护、流式恢复和 token-aware 处理的记录答案。

#### 逐项排除

- **A.** 固定 1 秒会让大量请求同步重试，没有 Circuit Breaker；重启整个 Stream 会重复传输并丢失已完成进度，固定 Session 上限也不适应不同查询。
- **B.** 指数退避、Jitter、Circuit Breaker、Chunk 检查点与按请求 token 处理形成完整方案，正确。
- **C.** 虽有 Jitter 和部分恢复想法，但 Standard Retry、全局统一 Token Limit、缓存 completion 以及请求缺失 Chunk 的做法不完整；Bedrock 流并非天然支持任意缺失 Chunk 拉取。
- **D.** Client timeout、load shedding 和静态上限只能粗粒度止损；它没有像 B 那样完整地以熔断和流检查点处理错误恢复，且动态 Chunk Sizing 不能替代自适应重试。

#### 解题方法

先找四个关键词：瞬时错误看 exponential backoff + Jitter，错误率失控看 Circuit Breaker，流式看 chunk timeout + checkpoint/buffer，长度变化看 per-request token budget。固定延迟、全局静态上限和“从故障 Chunk 直接拉取”通常是陷阱；同时区分应用层恢复与服务原生续传能力。

### English

#### Exam focus and background

This question combines adaptive retry, circuit breaking, streaming recovery, and token-aware request handling. Exponential backoff with jitter prevents many clients from retrying in lockstep. A circuit breaker stops additional retries when the error rate is too high, preventing a service incident from becoming a retry storm, and permits recovery as availability improves. A streaming handler should detect chunk-delivery timeouts, preserve successfully received data, and use an application-level checkpoint to avoid reprocessing the entire response after reconnection.

Token handling must also be per request rather than one rigid global cap. A long inquiry needs a budget for input, output, and safety margin, with deliberate trimming or summarization if necessary. In exam framing, “resume from the last position” means application-level checkpointing and idempotent recovery; it should not be read as claiming that Bedrock natively supports arbitrary client requests for a missing chunk offset. `B` remains the recorded answer because it is the most complete combination.

#### Analogy

Picture a highway toll plaza in a storm. Backoff and jitter stagger arrivals. A circuit breaker closes the entrance when the gates fail, rather than creating a larger queue. The clerk records scanned ticket numbers, so service can continue from a checkpoint instead of making every car start over. Each vehicle is measured before entering; oversized cargo must be repacked first.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS SDK/Bedrock retry layer | Traffic staggering | Applies exponential backoff and jitter to transient errors |
| Circuit breaker | Failure gate | Stops retry amplification during high error rates |
| Bedrock streaming API/handler | Segmented toll lane | Watches chunk timeouts and handles partial responses |
| Token-aware request handler | Vehicle-size check | Budgets input, output, and context per request |

#### Correct answer and reasoning

1. Transient FM failures require retries, but a fixed delay can synchronize clients; use exponential backoff with jitter.
2. When the error rate becomes excessive, a circuit breaker temporarily disables retries and protects Bedrock and the application.
3. The streaming handler detects chunk timeouts, buffers received data, and uses an application checkpoint to avoid restarting all processing after reconnection.
4. The request path calculates a token budget for each inquiry before invocation, allowing for different prompt lengths and output needs.
5. `B` is the recorded choice that combines adaptive protection, streaming recovery, and token-aware handling.

#### Option-by-option elimination

- **A.** A fixed one-second delay can synchronize retry storms, there is no circuit breaker, restarting the stream repeats work, and a fixed session cap ignores variable request length.
- **B.** Exponential backoff, jitter, circuit breaking, chunk checkpointing, and per-request token handling address the full requirement set.
- **C.** It includes some useful ideas, but standard retry mode, a global token limit, cached completions, and requesting arbitrary missing chunks do not form a complete or reliable design; Bedrock streams do not inherently expose arbitrary chunk-offset retrieval.
- **D.** Client timeouts, load shedding, and a static cap are coarse protections. Dynamic chunk sizing does not replace a circuit breaker and complete checkpoint-based streaming recovery.

#### Exam strategy

Map four clues: transient errors to exponential backoff plus jitter; error-rate overload to a circuit breaker; streaming to chunk timeout plus checkpoint/buffer; variable prompt length to a per-request token budget. Fixed delays, global static caps, and assumed native missing-chunk retrieval are common traps. Separate application recovery from native service semantics.

---
