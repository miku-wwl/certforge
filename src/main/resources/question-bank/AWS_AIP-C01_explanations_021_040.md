## Question 21

### 中文

#### 考点背景

本题考查 Amazon Bedrock Guardrails 如何把提示词攻击防护、跨 Region 推理和可观测性组合成一条安全边界。对 Prompt Injection，重点不是在应用外面猜测几种危险字符串，而是启用 Guardrails 的 Prompt Attack/内容过滤能力，并在调用路径上让它评估用户输入和模型响应；较高强度代表更严格的检测取舍。审计要求则意味着必须记录每次拦截或检测的细节，而不只是记录“某个 API 被调用过”。

题目的“跨 Region 故障转移”是第二个硬条件。Guardrail Profile 是题目要识别的托管路由机制；它与 Guardrail 的策略配置相配合，让策略评估可以在支持的地理边界内转发。CloudTrail 适合 API 活动审计，但不能替代应用对 Guardrail trace、干预原因和请求关联信息的详细日志。

#### 场景比喻

把客服助手想成银行大厅：Guardrails 是入口安检和出口复核，Guardrail Profile 是另一座同规格的备用大厅，CloudWatch Logs 是保安逐笔填写的事件簿。CloudTrail 只像门禁系统的刷卡记录——知道谁进过门，却不一定写下保安为何拦下某句话。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 安检与出口复核 | 检测并阻止 Prompt Attack 及不当输入/输出，使用 High 强度。 |
| Guardrail Profile | 备用安检大厅 | 按题目要求承载跨 Region Guardrail inference/failover。 |
| Amazon CloudWatch Logs | 安全事件簿 | 保存详细干预事件，并以自定义指标触发告警。 |

#### 正确答案与推理

**正确答案：`A`**

1. 先找“阻止复杂 Prompt Injection”的直接托管控制：A 选择 Guardrails 的过滤器并设为 High，覆盖安全干预的核心入口；实际 SDK 调用还应正确标记用户输入，使 Prompt Attack 过滤器评估用户可控部分。
2. 再找“跨 Region 故障转移”的专门机制：A 使用 Guardrail Profile，而不是只复制配置文件或依赖网络层 WAF。
3. 最后核对“所有干预行为的审计日志”：A 把 Guardrails 事件写入 CloudWatch Logs，并可按事件类型、请求 ID 等维度发布指标，满足细粒度追踪和告警要求。
4. 因此按题库记录答案为 A。它把检测、跨 Region 路由和干预观测放在同一条托管调用链上；“Defense-in-Depth”仍可在生产中叠加身份、输入校验和权限边界，但这些不能替换 A 的三个关键能力。

#### 逐项排除

- **A：正确。** Guardrails 的高强度攻击过滤、Guardrail Profile 的跨 Region inference，以及 CloudWatch Logs 的详细事件记录共同覆盖题干三项要求。
- **B：不选。** WAF 可以在边缘或 API 层拦截可疑请求，但没有给出 Guardrail 的跨 Region inference；CloudTrail 的 API 活动记录也不等于每次安全干预的完整细节。
- **C：不选。** Comprehend 自定义分类器和 API Gateway 校验需要自建训练、阈值和处置链路，既增加运维，也没有题目要求的 Guardrail Profile 故障转移。
- **D：不选。** Word Filter 适合已知词条，不能等同于 Prompt Attack 检测；“复制 Guardrail”也不是题目要识别的 Guardrail Profile inference，而且 CloudTrail 仍不是详细干预日志的主存储。

#### 解题方法

看到“Prompt Injection/攻击过滤 + 跨 Region + 详细干预审计”，按三列核对：Guardrails 负责安全策略，Profile 负责跨 Region 路由，CloudWatch Logs/metrics 负责事件证据。看到 WAF、CloudTrail 或自定义分类器时，问它是否同时满足三项；只覆盖入口或 API 审计的选项通常不完整。

### English

#### Exam focus and background

This question tests how Amazon Bedrock Guardrails, cross-Region guardrail inference, and operational evidence fit together. A prompt-injection requirement points to a managed Guardrails prompt-attack/content-filter control on the model path, not merely to a hand-built list of suspicious strings in an upstream gateway. A high filter strength represents the stricter detection setting in the question. In an actual SDK design, user-controlled text should also be identified correctly so the prompt-attack filter evaluates the intended portion of the request.

The second hard requirement is failover across Regions. The exam is looking for a guardrail profile as the managed routing mechanism for cross-Region guardrail inference. The audit requirement is different from API activity auditing: CloudTrail can show that an API operation occurred, while application logging can preserve the intervention outcome, reason, trace, and correlation identifiers needed to investigate a blocked request.

#### Analogy

Imagine a bank lobby with an entrance security check and an exit inspection. Guardrails are both checks, the guardrail profile is an equivalent backup lobby in another approved Region, and CloudWatch Logs is the guard’s detailed incident notebook. CloudTrail is more like a door-access ledger: useful for proving that someone used the door, but not necessarily enough to explain why a particular sentence was stopped.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Security checkpoint | Detects and blocks prompt attacks and unsafe input/output using the required high strength. |
| Guardrail profile | Backup checkpoint | Provides the exam’s cross-Region guardrail inference/failover path. |
| Amazon CloudWatch Logs | Incident notebook | Stores detailed intervention events and supports custom metrics and alarms. |

#### Correct answer and reasoning

**Correct answer: `A`**

1. Start with the direct control for sophisticated prompt injection: A uses Bedrock Guardrails with a high-strength filter, covering the safety boundary where the model is invoked.
2. Match the failover phrase to the specialized mechanism: A uses a guardrail profile for cross-Region guardrail inference rather than relying only on WAF rules or copying configuration.
3. Match the audit phrase to the evidence layer: A sends detailed intervention events to CloudWatch Logs and can publish correlated custom metrics for alerting.
4. The recorded answer is therefore A. The option covers detection, cross-Region routing, and intervention observability in one managed design. Additional identity and application controls may strengthen production defense-in-depth, but they do not replace these three required matches.

#### Option-by-option elimination

- **A: Correct.** It combines high-strength Guardrails protection, a guardrail profile for cross-Region inference, and CloudWatch evidence for interventions.
- **B: Eliminate.** WAF can filter requests at an HTTP boundary, but the option supplies no cross-Region guardrail inference. CloudTrail API events are not the same as detailed records of every guardrail intervention.
- **C: Eliminate.** A custom Comprehend classifier and API Gateway validation add training, threshold, and response logic, while omitting the required guardrail-profile failover design.
- **D: Eliminate.** Word filters address known terms rather than general prompt attacks. “Guardrail replication” is not the profile-based inference mechanism in the recorded answer, and CloudTrail is still not the detailed intervention log.

#### Exam strategy

Translate the stem into three capability slots: Guardrails for attack/content policy, a guardrail profile for cross-Region routing, and CloudWatch Logs/metrics for evidence and alerting. When an option offers WAF, CloudTrail, or a custom classifier, ask whether it satisfies all three slots; an option that only protects the edge or records API calls is incomplete.

---
## Question 22

### 中文

#### 考点背景

本题考查可自动推进和回滚的 Canary Deployment 编排。金丝雀发布不是简单地把新模型上线，而是把流量切成阶段，在每个观察窗口比较延迟、错误率和服务健康，再决定增加、保持、减少或撤回流量。题目还强调历史流量模式和无人干预，意味着策略必须能被工作流表达并持续执行，而不是依赖工程师看 Dashboard 后手动改权重。

记录答案 A 的重点在职责组合：Provisioned Throughput 为模型版本提供可预测容量，EventBridge 负责由发布事件启动流程，Step Functions 负责阶段、等待和分支，Lambda 读取 CloudWatch 指标并执行路由/回滚动作。严格地说，Provisioned Throughput 本身不是通用的流量分配器；本题把流量切换抽象为编排工作流中的路由配置，因此应理解为“以这些组件构成自动控制平面”。

#### 场景比喻

像支付高速公路试开新车道：先放 1% 车辆，收费站观察延迟和故障；一段时间平稳就放到 10%、25%，若事故率升高，控制中心自动把车流导回旧车道。Step Functions 是控制中心的流程卡，Lambda 是读仪表和拨动闸门的操作员。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Provisioned Throughput | 稳定容量车道 | 为各模型版本提供可预测的推理容量基线。 |
| EventBridge | 发布信号灯 | 在新版本发布时启动金丝雀工作流。 |
| Step Functions | 分阶段控制中心 | 执行加权步骤、等待观察窗口、条件分支和回滚。 |
| Lambda + CloudWatch | 仪表读数与执行器 | 读取延迟/错误率，依据阈值推进或撤回流量。 |

#### 正确答案与推理

**正确答案：`A`**

1. “逐步增加流量”要求有可重复的阶段状态；A 用 Step Functions 把 1%、10% 等阶段和等待时间写成可审计流程。
2. “健康继续、退化减少”要求条件分支；A 的 Lambda 读取 CloudWatch 延迟和错误率，再让工作流增加权重或触发回滚。
3. “新版本发布自动启动”由 EventBridge 连接发布事件与状态机；历史模式可以作为阶段阈值、观察窗口和流量计划的输入。
4. “无人工停止并恢复旧版本”由失败分支自动执行回滚，完成题目记录的 A。生产实现仍需明确实际路由层和幂等回滚动作，因为 Bedrock Provisioned Throughput 不会单独替代路由控制器。

#### 逐项排除

- **A：正确。** 它给出事件触发、分阶段编排、指标判断和自动回滚的闭环。
- **B：不选。** API Gateway 的加权路由可以成为路由组件，但题目把关键调整和回滚交给“外部逻辑”，无法保证全自动、可审计的闭环；HTTP API 的具体路由能力也不能凭空补齐该逻辑。
- **C：不选。** SageMaker Endpoint Variant 是 SageMaker 端点的模型变体，不能直接把 Amazon Bedrock 模型版本当成同一 SageMaker 端点变体；Model Monitor 也不会自动管理 Bedrock 流量回滚。
- **D：不选。** OpenSearch 擅长日志检索和分析，但不是可靠的金丝雀流量编排或回滚控制器。

#### 解题方法

圈出四个关键词：“gradual/逐步”“metrics/指标”“threshold/阈值”“without manual intervention/无人干预”。优先选能表达事件触发、等待、条件分支和补偿回滚的工作流；不要把“能观察日志”误判为“能改变流量”。若答案含有 Provisioned Throughput，要另问：它提供容量，真正的权重与回滚由哪层执行？

### English

#### Exam focus and background

This question is about an automated canary-deployment control loop. A canary is not simply a second model sitting beside the old one. Traffic must move in stages, each stage needs an observation window, latency and error-rate signals must be compared with thresholds, and the workflow must either advance or compensate without waiting for an operator.

The recorded answer, A, is built as a responsibility chain: Provisioned Throughput supplies predictable capacity for the model versions, EventBridge starts the workflow when a release occurs, Step Functions expresses stages, waits, branches, and rollback, and Lambda reads CloudWatch metrics and performs the routing action. Provisioned Throughput itself is not a general-purpose traffic router. The exam abstracts the routing configuration into the orchestration layer, so the answer should be read as an automated control plane around Bedrock inference.

#### Analogy

Think of opening a new lane on a payment highway. The control center sends 1% of cars into the new lane, watches delay and accident gauges, then raises the share to 10% and 25% if the lane stays healthy. If the gauges deteriorate, it closes the new lane and sends cars back. Step Functions is the control-center procedure; Lambda reads gauges and operates the gates.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Provisioned Throughput | Stable-capacity lane | Provides a predictable inference-capacity baseline for model versions. |
| EventBridge | Release signal | Starts the canary workflow when a new version is released. |
| Step Functions | Control center | Executes staged shifts, waits, conditions, and rollback branches. |
| Lambda + CloudWatch | Gauge reader and actuator | Reads latency/error metrics and advances or reverses the traffic plan. |

#### Correct answer and reasoning

**Correct answer: `A`**

1. “Gradually increase traffic” requires explicit, repeatable stages; A uses Step Functions to represent staged percentages and observation periods.
2. “Continue when healthy, decrease when degraded” requires conditional evaluation; A has Lambda inspect CloudWatch latency and error-rate metrics and then advance or roll back.
3. “Start automatically on release” is supplied by the EventBridge rule targeting the state machine. Historical traffic patterns can inform the configured schedule, thresholds, and observation windows.
4. “No manual intervention” is satisfied by the failure branch that performs rollback. The recorded answer is A. In production, the implementation must still identify the real routing layer and make rollback idempotent, because Provisioned Throughput alone does not perform traffic weighting.

#### Option-by-option elimination

- **A: Correct.** It provides the event trigger, staged orchestration, metric decision, and automatic rollback loop.
- **B: Eliminate.** API Gateway weighted routing could be part of a routing layer, but the option leaves critical adjustment and rollback to external logic, so the complete automated, auditable loop is not defined.
- **C: Eliminate.** SageMaker endpoint variants are variants of a SageMaker endpoint, not a direct representation of Bedrock model versions, and Model Monitor does not automatically roll back Bedrock traffic.
- **D: Eliminate.** OpenSearch is useful for log analysis, but it is not a dependable canary traffic orchestrator or rollback controller.

#### Exam strategy

Mark the phrases “gradual,” “metrics,” “threshold,” and “without manual intervention.” Prefer the design that can express an event trigger, wait state, conditional branch, and compensating rollback. Do not confuse the ability to visualize logs with the ability to change traffic. Whenever Provisioned Throughput appears, separately identify the component that actually performs routing and rollback.

---

## Question 23

### 中文

#### 考点背景

本题考查 Provisioned Throughput 的调用寻址。购买预置吞吐量只是创建了一条专用容量路径；应用必须在运行时把请求发到那条路径。Amazon Bedrock 的运行时调用通过 `modelId` 选择目标，因此使用普通基础模型 ID 会继续走 On-Demand，而不会因为账户里存在预置容量就自动切换。

题干给出两个相反证据：Provisioned capacity 没有消耗，On-Demand 正在 Throttling。看到这组证据，应先检查路由/目标标识，而不是先加容量或改变响应传输方式。创建 API 返回的 `provisionedModelArn` 正是运行时要使用的模型 ID；还应确认其状态已为 `InService`。

#### 场景比喻

公司买了专属柜台，但应用仍拿着“普通排队”号码去大厅。增加柜台数量不会让顾客自动走进专属通道；必须把取号单改成专属柜台的编号。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| `CreateProvisionedModelThroughput` | 购买专属柜台 | 创建固定模型容量并返回预置模型 ARN。 |
| `provisionedModelArn` | 专属柜台编号 | 作为运行时 `modelId`，把请求路由到预置吞吐量。 |
| `InvokeModel` | 取号/办理窗口 | 使用指定 `modelId` 发起实际推理。 |
| Model Units | 柜台数量 | 决定预置容量规模，但不改变应用当前指向的目标。 |

#### 正确答案与推理

**正确答案：`B`**

1. 应用代码当前传入基础模型 ID，说明请求目标是普通模型路径。
2. 购买 API 返回的 `provisionedModelArn` 是专门代表该预置吞吐量的运行时目标；把它替换到 `modelId`，请求才会使用已购容量。
3. 这样既解释了“预置容量闲置”，也解释了“On-Demand 被限流”，并直接修复两者的因果关系。
4. 按题库记录答案为 B。部署前应检查预置资源处于 `InService`，权限也同时允许调用该 ARN。

#### 逐项排除

- **A：不选。** 增加 MU 只会扩大专属容量；代码仍指向基础模型时，这些容量依旧可能闲置。
- **B：正确。** 使用创建 API 返回的 Provisioned Model ARN 作为 `modelId`，修正了请求路由。
- **C：不选。** 指数退避能缓和暂时性限流，却不会把请求从 On-Demand 改到 Provisioned Throughput。
- **D：不选。** `InvokeModelWithResponseStream` 只改变响应传输形态，不改变 `modelId` 指向的容量路径。

#### 解题方法

看到“已购买但未使用 + On-Demand throttling”，先做目标三问：调用的 `modelId` 是基础模型、Inference Profile，还是 provisioned ARN？资源是否 `InService`？IAM 是否允许该目标？只有在确认已经命中预置路径后，才讨论是否需要增加 MU 或重试。

### English

#### Exam focus and background

This question tests addressing for Amazon Bedrock Provisioned Throughput. Purchasing provisioned capacity creates a dedicated invocation path, but the application does not automatically switch to it. The runtime request selects its target through `modelId`; if the code continues to use the base foundation-model ID, the request remains on the on-demand path.

The evidence is deliberately diagnostic: provisioned capacity is idle while on-demand calls are throttled. That combination points first to incorrect routing, not to insufficient model units or a streaming choice. The provisioned-model ARN returned by the creation operation is the runtime model identifier for that dedicated capacity. The application should also confirm that the provisioned resource is `InService` before invoking it.

#### Analogy

The company bought a private service counter, but the application still takes a normal queue ticket. Buying more private counters will not move those customers. The ticket must contain the private counter’s identifier.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| `CreateProvisionedModelThroughput` | Private-counter purchase | Creates dedicated capacity and returns its provisioned-model ARN. |
| `provisionedModelArn` | Private-counter identifier | Becomes the runtime `modelId` that selects provisioned capacity. |
| `InvokeModel` | Service window | Performs inference against the selected `modelId`. |
| Model Units | Number of counters | Controls provisioned capacity size, not the application’s current target. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. The code passes a base model ID, so it is targeting the ordinary on-demand model path.
2. The ARN returned by `CreateProvisionedModelThroughput` identifies the provisioned model resource. Replacing the base ID with that ARN routes inference to the purchased capacity.
3. This single change explains both observations: unused provisioned capacity and throttled on-demand requests.
4. The recorded answer is B. Before rollout, verify that the provisioned resource is `InService` and that the caller is authorized to invoke the ARN.

#### Option-by-option elimination

- **A: Eliminate.** More model units increase dedicated capacity, but the code would still target the base model and could leave the new capacity unused.
- **B: Correct.** The provisioned-model ARN is used as `modelId`, fixing the invocation route.
- **C: Eliminate.** Exponential backoff can reduce pressure from transient throttling, but it does not redirect calls from on-demand to provisioned capacity.
- **D: Eliminate.** `InvokeModelWithResponseStream` changes response delivery, not the target selected by `modelId`.

#### Exam strategy

When the stem says “purchased but unused” and “on-demand throttled,” inspect the target identifier first. Ask whether `modelId` is a base model, an inference profile, or the provisioned ARN; then check `InService` status and IAM permission. Only after the request is reaching provisioned capacity should you consider more model units or retry behavior.

---

## Question 24

### 中文

#### 考点背景

本题把四个要求放在一起：从公司数据源检索、回答中给出可核查引用、提供对推荐的可理解解释，以及低于 3 秒的延迟和最低运维开销。Amazon Bedrock Knowledge Bases 的 RAG 将“查资料”和“生成回答”连接起来，Source Attribution 则让结果携带来源信息。考试语境中的“解释推理”应理解为展示基于证据的简短理由，而不是暴露模型的隐藏 Chain-of-Thought。

关键区分是“有思考文本”不等于“有事实依据”。Extended Thinking 或 Chain-of-Thought 可能增加延迟，也不会自动把每个事实绑定到公司文档；自行托管模型、检索库和引用拼装则引入更多组件。记录答案 A 的核心是使用托管 Knowledge Bases/RAG/source attribution，把检索与引用交给集成能力。

#### 场景比喻

像理财顾问递交一页建议：每个数字旁边贴着公司档案的页码，顾问只写“基于这三条资料所以建议 X”，而不是把脑中的草稿本全部摊开。Knowledge Base 是档案室，Source Attribution 是页码贴纸。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Bases | 公司档案室 | 索引并检索公司数据源，为生成提供相关上下文。 |
| RAG + Source Attribution | 证据夹与页码贴纸 | 让推荐以检索内容为依据，并返回可链接的来源。 |
| Anthropic Claude / Bedrock API | 顾问写作员 | 根据检索上下文生成建议和简短、可展示的理由。 |
| Amazon S3 | 低运维档案柜 | 可保存审计所需的回答与引用记录。 |

#### 正确答案与推理

**正确答案：`A`**

1. “检索公司数据源”匹配 Knowledge Bases + RAG；相关内容先进入生成上下文，不要求每次手写检索器。
2. “引用具体来源”匹配 Source Attribution，回答可以把数据性陈述链接到检索到的文档片段。
3. “解释推荐”应输出基于这些来源的摘要理由，而不是把隐藏详细思维当作产品接口。
4. “低于 3 秒、最低运维”使托管检索/引用方案优于自建 SageMaker、RDS、Lambda 管道；按题库记录答案为 A。实际仍需用目标数据和模型测量延迟，不能由服务名称自动保证 3 秒。

#### 逐项排除

- **A：正确。** Knowledge Bases、RAG 和 Source Attribution 覆盖检索、来源链接和较低运维；S3 可承载审计记录。
- **B：不选。** Extended Thinking 主要改变模型推理预算，不能自动产生公司文档引用，4,000 token 还可能损害 3 秒延迟。
- **C：不选。** 自定义托管模型、RDS 引用和 Lambda 拼装增加运营面，且引用链路需要自行保证与生成事实一致。
- **D：不选。** Chain-of-Thought 不保证 grounding 或 citation；自定义检索跟踪又增加实现负担。

#### 解题方法

看到“公司资料 + 引用 + 最低运维”，优先寻找 Knowledge Bases、RAG、source attribution 或 `RetrieveAndGenerate` 的组合。看到“解释推理”时先翻译成“证据支持的理由”，不要被 Extended Thinking/CoT 带偏；再检查延迟和自建组件数量。

### English

#### Exam focus and background

The stem combines four requirements: retrieve from company sources, attach verifiable citations, explain a recommendation, and stay under a latency target with the least operational overhead. Amazon Bedrock Knowledge Bases with RAG connects retrieval to generation, while source attribution supplies the provenance needed to link factual claims to source passages. In an exam and product-design context, “explain the reasoning” should mean a concise, evidence-grounded rationale—not exposing hidden chain-of-thought.

The important distinction is that visible thinking is not the same as factual grounding. Extended thinking or chain-of-thought does not automatically bind every claim to a company document and can consume latency. Hosting a model and assembling retrieval, database, citation, and audit code yourself also increases operations. The recorded answer, A, is centered on the managed Knowledge Bases/RAG/source-attribution path.

#### Analogy

Imagine an adviser handing over a one-page recommendation. Every number has a page reference to the company archive, and the adviser writes a short evidence-based rationale rather than exposing the entire private draft notebook. The knowledge base is the archive; source attribution is the page sticker.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Knowledge Bases | Company archive | Indexes and retrieves relevant company data for generation. |
| RAG + source attribution | Evidence folder and page labels | Grounds the answer and returns linkable source information. |
| Anthropic Claude / Bedrock API | Adviser-writer | Generates the recommendation and a concise rationale from retrieved context. |
| Amazon S3 | Low-operations filing cabinet | Can retain answer and citation records for audit purposes. |

#### Correct answer and reasoning

**Correct answer: `A`**

1. “Retrieve company data” maps to Knowledge Bases and RAG, so relevant passages enter the generation context without a custom retrieval service.
2. “Cite specific sources” maps to source attribution, allowing claims to point back to retrieved document passages.
3. “Explain the recommendation” is satisfied by presenting a concise rationale tied to those sources, not by exposing hidden internal reasoning.
4. “Under three seconds with least operations” favors managed retrieval and attribution over a self-hosted model, database, and Lambda pipeline. The recorded answer is A. The latency target must still be measured with the real corpus and model; a service label does not guarantee three seconds.

#### Option-by-option elimination

- **A: Correct.** Knowledge Bases, RAG, and source attribution cover retrieval, provenance, and lower operational burden; S3 can retain audit records.
- **B: Eliminate.** Extended thinking changes the model’s reasoning budget, but it does not automatically create company-document citations, and 4,000 extra tokens may threaten the latency target.
- **C: Eliminate.** A custom hosted model, RDS citation store, and Lambda assembly add operational components and require the application to keep citations aligned with generated claims.
- **D: Eliminate.** Chain-of-thought does not guarantee grounding or citations, and custom retrieval tracking increases implementation work.

#### Exam strategy

For “company sources + citations + least operations,” look for Knowledge Bases, RAG, source attribution, or `RetrieveAndGenerate` together. Translate “explain reasoning” into “show an evidence-based rationale,” then reject options that only add thinking tokens. Finally compare latency risk and the number of custom components.

---

## Question 25

### 中文

#### 考点背景

本题考查多模型 Token 管理的三个层面：模型特定的上限预警、高并发处理，以及可用于成本分摊的持续用量记录。不同 Foundation Model 的 tokenizer 和上下文规则可能不同，所以不能拿一个通用字符数估计器代替模型级估算。预警必须发生在调用前或接近上限时，而不是等 API 失败后才统计。

Lambda 适合把估算、指标发布和请求处理封装成可并发扩展的无服务器入口；CloudWatch 负责按模型设置阈值与告警；DynamoDB 保存带业务部门、模型、请求时间和 token 估算/实际值的明细。记录答案 A 的关键是“主动、持续、按模型、可归属”，而不是只观察被 Guardrails 拒绝的少数请求。

#### 场景比喻

把每个模型想成不同容量的货运集装箱：装货前要用对应尺寸的量尺估算还剩多少空间，仓库仪表盘要在快满时报警，账本还要写明这箱货属于哪个部门。不能等集装箱爆仓后才从事故单反推装了多少。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda + model-specific tokenizer | 入仓前量尺 | 按模型估算输入/输出 token，接近阈值时提前处理。 |
| Amazon CloudWatch | 仓库仪表盘 | 接收 token 指标，按模型和阈值告警。 |
| Amazon DynamoDB | 成本账本 | 保存高吞吐明细，按业务部门汇总成本。 |
| Amazon Bedrock | 多种集装箱 | 提供实际使用的不同 Foundation Model。 |

#### 正确答案与推理

**正确答案：`A`**

1. “不同模型各自的 Token 上限”要求每个模型使用匹配 tokenizer；A 在 Lambda 中按模型估算，而不是依赖统一配额。
2. “主动告警”要求在发送请求前或达到预设余量时发布指标并触发 CloudWatch Alarm；失败日志不够早。
3. “每分钟超过 5,000 请求”要求无服务器处理路径可并发扩展，Lambda 可以通过并发、批量和配额配置承担该吞吐目标，仍需压测和配额核验。
4. “按业务部门分摊成本”要求持久化带标签的明细，DynamoDB 补上 CloudWatch 聚合指标缺少的逐请求维度；所以记录答案为 A。

#### 逐项排除

- **A：正确。** 模型特定估算、CloudWatch 主动告警、Lambda 扩展和 DynamoDB 成本明细正好覆盖全部条件。
- **B：不选。** Guardrails 指标只围绕过滤/拒绝事件，不能代表所有模型 token 用量，也不能可靠地在每个模型接近上限前预警。
- **C：不选。** DLQ 和错误日志是失败后的补偿与分析，既不主动，也无法完整统计成功请求的 token 消耗。
- **D：不选。** API Gateway Usage Plan 通常按请求/配额限流，并不理解不同模型实际消耗的 token，不能准确预估上限。

#### 解题方法

把要求拆成“估算、告警、扩展、归属”四格：model-specific tokenizer、CloudWatch Alarm、可并发 Lambda、明细存储。凡是只统计拒绝或失败、只做请求级配额、或只画趋势没有逐请求归属的选项，都缺一格。

### English

#### Exam focus and background

This question tests three layers of token management across multiple models: proactive warnings against model-specific limits, high request concurrency, and durable usage records for cost allocation. Foundation models can use different tokenizers and context rules, so a single character-count heuristic is not a reliable substitute for model-specific estimation. A warning must happen before invocation or while there is still headroom, not only after an API failure.

Lambda can package estimation, metric publication, and request handling in a horizontally concurrent serverless path. CloudWatch supplies dimensions, thresholds, and alarms. DynamoDB retains detail such as business unit, model, timestamp, and estimated or returned token counts. The recorded answer, A, is about being proactive, continuous, model-aware, and attributable—not merely observing the small subset of calls rejected by a guardrail.

#### Analogy

Imagine different models as shipping containers with different capacities. Before loading, use the correct measuring tool, make the warehouse dashboard warn when a container is nearly full, and record which department owns each shipment. Waiting for an overflow incident would be too late and would miss successful loads.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda + model-specific tokenizer | Pre-loading measuring tool | Estimates tokens per model and acts before a request crosses a threshold. |
| Amazon CloudWatch | Warehouse dashboard | Receives token metrics and raises model-specific alarms. |
| Amazon DynamoDB | Cost ledger | Stores high-volume detail for allocation by business unit. |
| Amazon Bedrock | Container fleet | Hosts the different foundation models being measured. |

#### Correct answer and reasoning

**Correct answer: `A`**

1. “Different model-specific limits” requires a tokenizer or estimator matched to each model; A does this in Lambda instead of applying one universal quota.
2. “Proactive alerting” requires publishing measurements before invocation or when remaining headroom reaches a threshold and alarming through CloudWatch; failure logs are too late.
3. “More than 5,000 requests per minute” calls for a concurrently scalable request path. Lambda can be configured and load-tested for that target, subject to concurrency and service quotas.
4. “Allocate costs by business unit” requires durable, attributed detail. DynamoDB supplies the per-request dimensions that aggregate CloudWatch metrics alone may not retain. The recorded answer is therefore A.

#### Option-by-option elimination

- **A: Correct.** It combines model-specific estimation, proactive CloudWatch alarms, scalable Lambda processing, and DynamoDB cost detail.
- **B: Eliminate.** Guardrail metrics describe filtering or rejected calls, not complete token consumption, and cannot reliably warn before every model reaches its limit.
- **C: Eliminate.** A dead-letter queue and error-log analysis are post-failure mechanisms and miss successful requests and advance warnings.
- **D: Eliminate.** API Gateway usage plans throttle requests or quotas; they do not understand each model’s actual token consumption or predict context-limit breaches accurately.

#### Exam strategy

Map the stem to four slots: estimate, alert, scale, and attribute. Look for a model-specific tokenizer, CloudWatch alarms, a concurrent Lambda path, and durable detailed storage. Reject designs that observe only failures, use request quotas as token quotas, or provide a dashboard without per-request ownership data.

---

## Question 26

### 中文

#### 考点背景

本题考查 Amazon Bedrock Agent 在“知识问答 + 可执行动作 + 多轮上下文 + 追踪”场景中的分工。50,000 份每天更新的商品文档适合放入 Knowledge Base，通过 RAG 在查询时取最新相关内容；订单状态和退货则不是静态知识，而是需要调用外部订单管理 API 的 Action Group。Agent 负责决定何时查知识库、何时调用动作，并在会话中继续使用上下文。

“最低运维开销”是排除微调、持续预训练、自托管容器和手工编排的关键。文档变化不应触发多模型重新训练；审计也应利用 Agent 的 Trace Event，记录知识库检索、动作调用、输入输出和失败位置。题目记录答案 D，核心是把变化频繁的知识与实时操作分别接到 Agent 的原生扩展点。

#### 场景比喻

把客服 Agent 想成商场服务台：Knowledge Base 是每天换新的商品目录，Action Group 是能查询订单系统、办理退货的电话分机，Session 是服务员手里的连续对话便签，Trace 是摄像机回放。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Agent | 商场服务台主管 | 维持会话上下文，决定检索资料或执行动作。 |
| Knowledge Base + RAG | 最新商品目录 | 检索每日更新的商品、保修文档，避免频繁重训。 |
| Action Group | 订单系统分机 | 以定义好的 API/函数动作查询订单并协助退货。 |
| Trace Event | 摄像机回放 | 记录 Agent 的检索、调用、观察和响应链路以便审计。 |

#### 正确答案与推理

**正确答案：`D`**

1. 50,000 份且每天更新的文档需要检索增强，而非把内容固化进每个模型；D 关联 Knowledge Base，用 RAG 取相关资料。
2. 查询订单和处理退货需要真实系统动作；D 通过 Action Group 暴露订单管理 API，Agent 可在收集必要参数后调用。
3. 多轮对话需要会话状态；Bedrock Agent 的 session 机制让后续问题可以承接前文。
4. 完整响应审计需要可解释的执行路径；D 启用 Trace Event，保留知识库查询、动作输入输出和失败原因。所有这些由托管 Agent 组合提供，故记录答案为 D。

#### 逐项排除

- **A：不选。** 每个类别微调模型会让每日文档更新变成重复训练和发布工作；Lambda 与 DynamoDB 还需自行实现动作路由和会话审计。
- **B：不选。** Continued Pre-Training 不适合高频变化的商品资料；API Gateway/Lambda 组合也没有 Agent 原生的知识检索、会话和 Trace 组合。
- **C：不选。** SageMaker 容器、Kendra、Step Functions 都能各自承担部分工作，但需要管理更多部署、搜索和编排组件，且题干没有给出 Agent 的原生上下文与追踪能力。
- **D：正确。** Action Group、Knowledge Base/RAG、会话上下文和 Trace Event 一一对应要求，运维面最小。

#### 解题方法

遇到“频繁更新文档”选 RAG/Knowledge Base，遇到“查询或办理外部系统”选 Action Group，遇到“多轮上下文”查 Agent session，遇到“审计执行链路”查 Trace。再用“最低运维”淘汰微调、预训练、自托管和多套手写编排。

### English

#### Exam focus and background

This question tests the division of responsibility inside an Amazon Bedrock agent solution: knowledge retrieval, executable actions, multi-turn context, and traceability. Fifty thousand product documents that change every day belong in a knowledge base and should be retrieved with RAG at query time. Order status and returns are not static facts; they require an action group that exposes the order-management API. The agent decides whether to retrieve information or invoke an action while maintaining the conversation.

“Least operational overhead” is the clue that rules out repeated fine-tuning, continued pre-training, self-managed containers, and hand-built orchestration. Document changes should not trigger a fleet of model retraining jobs. Trace events provide the execution evidence for knowledge-base lookups, action calls, observations, and failures. The recorded answer, D, connects frequently changing knowledge and live operations through the agent’s native extension points.

#### Analogy

Picture an agent as a shopping-mall service desk. The knowledge base is a product catalog replaced every day, the action group is a phone extension into the order system, the session is the clerk’s continuing conversation note, and the trace is the security-camera replay of what the clerk checked and called.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Agent | Service-desk supervisor | Maintains session context and chooses retrieval or an action. |
| Knowledge Base + RAG | Current product catalog | Retrieves updated product and warranty documents without frequent retraining. |
| Action Group | Order-system extension | Calls defined APIs or functions to check orders and support returns. |
| Trace events | Camera replay | Records retrievals, calls, observations, and response paths for audit. |

#### Correct answer and reasoning

**Correct answer: `D`**

1. A large, daily-changing document set calls for retrieval rather than embedding every fact in a model. D associates a knowledge base and uses RAG.
2. Checking an order or processing a return requires a live system action. D uses an action group to expose the order-management API after required parameters are available.
3. Multi-turn conversations require session context. The Bedrock agent session lets later questions build on earlier exchanges.
4. Complete response auditing requires an execution path, not only the final text. D enables trace events for knowledge-base queries, action inputs and outputs, and failures. The recorded answer is therefore D because the managed components match all requirements.

#### Option-by-option elimination

- **A: Eliminate.** A fine-tuned model per category turns daily document changes into repeated retraining and release work; Lambda and DynamoDB also require custom action routing and conversation auditing.
- **B: Eliminate.** Continued pre-training is a poor fit for frequently changing product material, and the API Gateway/Lambda design lacks the agent’s native combination of retrieval, session, and trace behavior.
- **C: Eliminate.** SageMaker containers, Kendra, and Step Functions can cover separate pieces, but they add deployment, search, and orchestration operations and do not provide the stated agent-native combination.
- **D: Correct.** Action groups, Knowledge Base/RAG, session context, and trace events map directly to the requirements with the least custom infrastructure.

#### Exam strategy

For “frequently changing documents,” choose RAG/Knowledge Base. For “query or change an external system,” choose an action group. For “multi-turn context,” look for an agent session; for “audit the execution path,” look for trace. Then use “least operations” to reject fine-tuning, pre-training, self-hosted containers, and hand-written orchestration.

---

## Question 27

### 中文

#### 考点背景

本题考查 Step Functions 的状态输入输出配额，以及如何让大模型工作流传递“指针”而不是传递大对象。每个 Agent 仍要按描述生成、规格验证、品牌检查的顺序运行，且 Trace/输出必须可追踪；问题只在于把大结果塞进下一个 State 会超过 256 KB。正确思路是把大内容放在已有的 S3 工作流存储中，状态数据只携带 URI、版本和必要元数据。

Amazon Bedrock 的 Step Functions 集成支持从 S3 读取大输入；`ResultSelector` 可把返回结果裁剪成小型对象，`ResultPath` 可把这个对象放到状态 JSON 的指定位置。这样不破坏串行 ReAct 链，也减少自定义数据搬运。题目答案 B 是一种引用传递设计；实际还应限制结果字段、校验 S3 权限和同 Region 约束。

#### 场景比喻

三个编辑坐在流水线旁，不把整箱稿纸递来递去，而是在交接单上写“稿件在仓库 A 的货架 7、版本 12”。下一个编辑按单取稿；交接单小而稳定，完整稿件仍可审计地留在仓库。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon S3 | 大件仓库 | 保存中间输出、Trace 和最终结果。 |
| Step Functions Bedrock Integration | 传送带接口 | 从 S3 取大输入并调用 Bedrock，不把大对象塞进状态。 |
| `ResultSelector` | 交接单裁剪器 | 只保留 S3 引用、状态和必要 Trace 元数据。 |
| `ResultPath` | 交接单归档位 | 将小引用写入状态 JSON，供下一个串行 Agent 使用。 |

#### 正确答案与推理

**正确答案：`B`**

1. 先识别失败原因：Step Functions 的状态输入/输出不能反复携带超过 256 KB 的中间 Trace 和生成结果。
2. 题干已有 S3，B 让 Bedrock 集成从 S3 URI 获取大输入；完整内容留在对象存储，状态只传引用。
3. 用 `ResultSelector` 选择 URI、对象版本和必要的 Agent/Trace 信息，再用 `ResultPath` 放进下一个 State 需要的字段，避免默认传递整个结果。
4. State 仍按三个 Agent 串行执行，ReAct 的决策—动作—观察模式不变；B 因此同时解决配额、可观测性、流程连续性和运维开销。

#### 逐项排除

- **A：不选。** DynamoDB 也能存引用，但题目已有 S3；额外的 Map 和读取逻辑扩大组件数，并没有比原生 S3 集成更省运维。
- **B：正确。** S3 承载大对象，`ResultSelector`/`ResultPath` 传递小引用，且保持串行验证链。
- **C：不选。** 压缩不能保证任意文本和 Trace 都低于配额；每个 State 解压再压缩增加延迟、代码和失败点。
- **D：不选。** 拆成多个状态机并用 EventBridge 协调会增加跨执行状态、重试、追踪和幂等管理，复杂度高于在一个状态机内传引用。

#### 解题方法

看到 Step Functions `256 KB`，立刻想到“外置大对象，状态传 URI”。若题目已有 S3，优先使用服务集成的 S3 input/output；再检查 `ResultSelector` 是否裁剪结果、`ResultPath` 是否只保留指针。不要用压缩或拆状态机掩盖配额问题。

### English

#### Exam focus and background

This question tests the Step Functions state-payload quota and the pointer-passing pattern for large generative-AI outputs. The three agents must still run in order—generation, specification validation, then brand-voice checking—and their traces and outputs must remain observable. The failure is caused by putting large intermediate objects into the next state, not by the ReAct sequence itself.

The efficient design stores large content in the existing S3 workflow bucket and passes a URI, version, status, and selected trace metadata through the state input. The Step Functions Bedrock integration can read large input from S3. `ResultSelector` can shape the returned result into a small object, while `ResultPath` places that object at a controlled location in the state JSON. The recorded answer, B, preserves the serial workflow while avoiding repeated payload inflation.

#### Analogy

Three editors work on an assembly line. Instead of handing over the entire box of paper, each editor writes “warehouse A, shelf 7, revision 12” on a small handoff slip. The next editor retrieves the box using the slip; the slip stays small while the full manuscript remains auditable in storage.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon S3 | Large-item warehouse | Stores intermediate outputs, traces, and final artifacts. |
| Step Functions Bedrock integration | Conveyor-belt interface | Reads large input from S3 and invokes Bedrock without bloating state. |
| `ResultSelector` | Handoff-slip editor | Keeps only the S3 reference, status, and required trace metadata. |
| `ResultPath` | Handoff-slip location | Inserts the small reference into the state JSON for the next agent. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. Identify the failure: Step Functions states cannot repeatedly carry intermediate traces and outputs larger than 256 KB.
2. The question already has S3. B uses the Bedrock integration to read the large input from an S3 URI while leaving the full artifact in object storage.
3. `ResultSelector` shapes the response into an S3 URI, object version, and necessary agent/trace fields; `ResultPath` places only that compact object where the next state expects it.
4. The three agent states remain sequential, so the ReAct reasoning-and-acting pattern is preserved. B therefore addresses the quota, observability, workflow continuity, and operational-overhead requirements together.

#### Option-by-option elimination

- **A: Eliminate.** DynamoDB could store references, but the question already has S3. Adding a Map and explicit reads increases components and operations without improving the native S3 handoff.
- **B: Correct.** S3 holds large artifacts and `ResultSelector`/`ResultPath` pass compact references while preserving the sequential chain.
- **C: Eliminate.** Compression cannot guarantee that arbitrary text and traces fit below the quota, and repeated compression/decompression adds latency, code, and failure modes.
- **D: Eliminate.** Multiple state machines coordinated by EventBridge add cross-execution state, retry, tracing, and idempotency complexity compared with one state machine carrying pointers.

#### Exam strategy

When you see the Step Functions `256 KB` limit, translate it immediately into “externalize large data; pass a URI.” If S3 is already present, prefer the managed S3 integration and check that `ResultSelector` trims the result and `ResultPath` retains only the pointer. Compression and state-machine fragmentation are usually complexity traps.

---

## Question 28

### 中文

#### 考点背景

本题考查大规模 Semantic Search 的向量化架构。复杂自然语言查询不能只靠关键词 analyzer；应把餐厅描述、菜单和评论编码为 embedding，把用户问题用同一模型编码，再在向量索引中做 k-NN 相似度检索。OpenSearch Service 适合承载可扩展的索引、过滤和向量查询，并能通过增量 ingestion 处理每小时更新。

题目同时给出 2,000 万餐厅、2 亿评论、500 ms P95 目标和高峰成本约束，暗示要把在线查询与离线 embedding 生成分开：数据更新时更新受影响的向量，查询时只做一次 query embedding 和索引搜索。记录答案 B 并不是说 OpenSearch 自动保证 95% 延迟，而是它在给定选项中最直接匹配语义检索和规模目标，且比自建 pgvector 运营面更清晰。

#### 场景比喻

把每家餐厅放进一张“口味地图”：川菜辣、适合约会、靠海等不是固定关键词，而是地图上的位置。用户说“想找安静又有海景的晚餐”，同一个向量化尺子把这句话放到地图上，k-NN 找附近的餐厅。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock FM | 统一量尺 | 为餐厅内容和自然语言查询生成同空间 embedding。 |
| Amazon OpenSearch Service | 向量地图 | 保存大规模向量与属性，执行 k-NN 和过滤检索。 |
| Ingestion/更新管道 | 地图修订队 | 每小时同步变化的餐厅详情和受影响 embedding。 |
| API 查询层 | 导航员 | 接收自然语言、生成 query embedding 并返回结果。 |

#### 正确答案与推理

**正确答案：`B`**

1. “复杂自然语言”要求语义相似度，不是把句子硬转换为菜系、地点等有限字段；B 用同一 FM 生成内容和查询 embedding。
2. “2 亿评论、500 ms”要求面向向量搜索的可扩展索引；B 使用 OpenSearch k-NN，而不是在应用层逐条比较。
3. “每小时更新”要求 ingestion 能增量刷新文档、属性和向量；B 的 OpenSearch 索引架构可配合更新管道保持新鲜。
4. “高峰成本有效”要求查询层与 embedding 生成解耦并按需扩展；B 的服务化搜索路径比自管数据库向量扩展更贴题。故记录答案为 B，但 95% 延迟仍需基准测试和容量设计。

#### 逐项排除

- **A：不选。** Custom Analyzer 和 relevance tuning 主要解决词法匹配；API Gateway 把自然语言转结构化参数无法可靠表达复杂语义。
- **B：正确。** 同一 embedding 模型加 OpenSearch k-NN 直接实现语义检索，并可配合增量索引。
- **C：不选。** pgvector 能做原型，但在题目规模、并发和高峰扩展下需自行承担 PostgreSQL/索引容量与运维；开发和运营工作更多。
- **D：不选。** Knowledge Base 更适合托管文档 RAG；将海量结构化餐厅/评论迁移到自定义 ingestion，并依赖通用 Retrieve，控制和性能验证路径不如 B 直接。

#### 解题方法

看到“语义/自然语言/相似度”就找 embedding + vector index + k-NN；看到“同一模型”要确认文档与查询在同一向量空间。再用规模、P95、更新频率和成本检查索引与 ingestion 是否可扩展，不要把关键词搜索或普通 RAG 误当作同类方案。

### English

#### Exam focus and background

This question tests a vector architecture for large-scale semantic search. Complex natural-language queries are not reliably solved by keyword analyzers. Restaurant descriptions, menus, and reviews should be encoded into embeddings; the user query should be encoded with the same model; and a vector index should perform a k-nearest-neighbor search. OpenSearch Service is the option that directly provides a scalable search index with vector retrieval, attribute filtering, and an operational path for hourly updates.

The scale and latency clues suggest separating offline embedding generation from online search. When a restaurant changes, update the affected document and vector; when a user searches, create one query embedding and search the index. The recorded answer, B, does not magically guarantee the 500 ms target. It is the best match among the choices and still requires benchmark testing, shard/index design, and capacity planning.

#### Analogy

Imagine a taste map where “spicy,” “quiet,” “date-night,” and “ocean view” place a restaurant at a location rather than in a single keyword box. The same measuring tool places “a quiet dinner with an ocean view” on that map, and k-NN finds nearby restaurants.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock FM | Shared measuring tool | Generates embeddings for restaurant content and user queries in one space. |
| Amazon OpenSearch Service | Vector map | Stores large-scale vectors and attributes and runs k-NN/filter searches. |
| Ingestion/update pipeline | Map revision crew | Refreshes changed restaurant details and affected embeddings hourly. |
| Query API layer | Navigator | Accepts natural language, creates a query embedding, and returns matches. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. “Complex natural language” requires semantic similarity, not a fragile conversion into a few cuisine or location fields. B uses the same Bedrock FM for content and query embeddings.
2. “200 million reviews and 500 ms” calls for a vector-oriented scalable index. B uses OpenSearch k-NN rather than comparing every record in application code.
3. “Hourly updates” requires incremental ingestion of changed attributes and vectors. B can pair the OpenSearch index with an update pipeline to maintain freshness.
4. “Cost-effective peak scaling” favors separating online search from embedding generation and scaling the managed search path. The recorded answer is B, with the reminder that the 95th-percentile latency still requires real benchmarking and capacity design.

#### Option-by-option elimination

- **A: Eliminate.** Custom analyzers and relevance tuning are primarily lexical techniques. An API Gateway transformation into structured fields cannot reliably represent complex semantic intent.
- **B: Correct.** A shared embedding model plus OpenSearch k-NN directly implements semantic search and supports an incremental index.
- **C: Eliminate.** pgvector may work for a prototype, but at this scale and concurrency the company owns PostgreSQL, vector-index, and peak-capacity operations, increasing the work.
- **D: Eliminate.** Knowledge Bases are a managed RAG path, but moving this huge structured restaurant/review corpus through a custom ingestion design and relying on generic retrieval is less direct for the stated search and latency goals.

#### Exam strategy

For “semantic,” “natural language,” and “similarity,” look for embeddings, a vector index, and k-NN. “Same model” is a clue that document and query vectors must share a space. Then test the choice against corpus size, percentile latency, update frequency, and peak scaling; do not confuse keyword search or ordinary document RAG with general vector search.

---

## Question 29

### 中文

#### 考点背景

本题考查 Prompt Engineering 的实验方法，而不是把一个新 Prompt 直接推到生产。系统在简单文档上表现好、复杂文档上不稳定，说明测试集必须覆盖失败分布：长文档、嵌套事实、歧义段落、数字和临床术语。只有固定输入、量化指标和可重复的版本比较，才能判断是 Prompt 规则、上下文组织还是模型本身造成退化。

代码仓库中的 Prompt 版本控制提供历史、评审和回滚；自动化测试套件则能在每次变更后比较一致性、事实准确率、完整性或格式合规。题目答案 B 的“诊断”来自复杂样例和性能模式记录，而不是来自把多个版本分流到生产后猜测用户反馈。Prompt Management 可辅助存储，但不是完整的评估实验框架。

#### 场景比喻

像给医生测试不同的病历摘要模板：不能只拿两份简单病例让医生凭印象选模板，而要用同一批疑难病例、同一张评分表和带日期的版本柜，才能知道哪个模板在哪类病例上失误。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Git 代码仓库 | 实验版本柜 | 保存 Prompt 历史、评审记录和可回滚版本。 |
| 自动化测试框架 | 标准化考场 | 对复杂临床文档重复运行每个 Prompt 变体。 |
| 量化评估指标 | 统一评分表 | 比较事实性、一致性、完整性和格式等表现。 |
| Amazon Bedrock | 被测引擎 | 对相同测试输入生成可比较的响应。 |

#### 正确答案与推理

**正确答案：`B`**

1. “复杂文档不一致”要求测试输入代表复杂失败场景，而 B 明确建立包含复杂文档的测试套件。
2. “按既定指标比较”要求固定评分定义并自动计算，B 使用可量化评估指标比较 Prompt 版本。
3. “保留历史记录”要求可追溯版本而非只保留当前控制台草稿，B 用代码仓库做版本控制。
4. 自动化执行还可以记录每个版本的错误模式，帮助定位 Prompt 变化导致的回归；因此题库记录答案为 B。

#### 逐项排除

- **A：不选。** Prompt 变体和手动简单样例不能诊断复杂文档问题，也缺少可重复的量化历史比较。
- **B：正确。** 复杂测试集、版本控制、自动运行和性能模式记录覆盖三个要求。
- **C：不选。** 为 Prompt 建独立 Endpoint 并分流生产会增加成本和风险；用户反馈也不能替代受控测试，更难解释复杂输入上的原因。
- **D：不选。** 自定义 Flow 和 Comprehend Medical 增加运维；医学实体分析也不是通用 Prompt 质量或摘要一致性的完整评分器。

#### 解题方法

看到“诊断不一致 + 比较版本 + 历史”，按“代表性测试集、固定指标、版本控制、自动运行”四项检查。生产 A/B 分流是后续验证手段，不是替代离线回归测试的第一步；工具越专用不代表越能解释 Prompt 退化。

### English

#### Exam focus and background

This question is about a disciplined prompt experiment, not immediately sending a new prompt to production. Good performance on simple documents and instability on complex ones means the test corpus must represent the failure distribution: long structure, nested facts, ambiguity, numbers, and clinical terminology. With fixed inputs, quantitative metrics, and repeatable comparisons, the team can tell whether a regression comes from prompt instructions, context organization, or the model.

Version control in a code repository supplies history, review, and rollback. An automated suite reruns the same difficult cases after every prompt change and records consistency, factuality, completeness, or format results. The recorded answer, B, diagnoses patterns through controlled tests rather than splitting production traffic and guessing from aggregate feedback. Prompt Management may help store prompts, but it is not the complete evaluation experiment by itself.

#### Analogy

Imagine testing clinical-summary templates. You would not give a doctor two easy cases and ask for a memory-based preference. You would use the same difficult case set, the same scorecard, and a dated version cabinet so you know which template fails on which kind of case.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Git repository | Version cabinet | Stores prompt history, reviews, and rollback points. |
| Automated test framework | Standardized exam room | Runs every prompt variant against complex clinical documents. |
| Quantitative metrics | Shared scorecard | Measures factuality, consistency, completeness, and format. |
| Amazon Bedrock | System under test | Generates comparable responses for the same test inputs. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. “Inconsistent on complex documents” requires representative difficult inputs; B explicitly creates a test suite containing complex clinical documents.
2. “Compare against established metrics” requires fixed scoring definitions and automated calculation; B compares prompt versions using quantifiable evaluation metrics.
3. “Maintain prompt history” requires traceable versions, not only the current console draft; B uses a repository for version control.
4. Automated runs can record error patterns by prompt version, making regressions diagnosable. The recorded answer is therefore B.

#### Option-by-option elimination

- **A: Eliminate.** Prompt variants and manual tests on simple documents do not diagnose the complex-document failure and do not provide a repeatable quantitative history.
- **B: Correct.** It combines a representative test corpus, version control, automated execution, and performance-pattern records.
- **C: Eliminate.** Separate production endpoints and traffic splitting add cost and risk. User feedback cannot replace controlled regression tests or explain why complex inputs fail.
- **D: Eliminate.** A custom Flow and Comprehend Medical add operations, while medical-entity extraction is not a complete evaluator for prompt quality or summary consistency.

#### Exam strategy

For “diagnose inconsistency + compare versions + preserve history,” check for a representative test set, fixed metrics, version control, and automated runs. Production A/B traffic can be a later validation step, not a substitute for offline regression testing. A specialized service is not automatically the right quality evaluator.

---

## Question 30

### 中文

#### 考点背景

本题考查降低长文档摘要幻觉的两条互补路径：RAG 负责把生成限制在可检索的源内容内，Prompt 负责要求模型按步骤核对事实。当前“一次性塞入完整长文档”的方式同时制造上下文压力和无依据补全；提高 Temperature 只会扩大随机性，Guardrails 也不是通用事实核验器。

记录答案 AB，但要正确理解它的考试边界。Zero-Shot CoT 的价值是把“先分析、再核对、后输出”写成明确指令；它不能单独保证真实，也可能消耗延迟预算，所以生产上应限制输出、测量吞吐。Semantic Chunking 与调优 embedding 让 Knowledge Base 找到语义完整、相关的片段，RAG 再把摘要锚定到源内容；这是解决幻觉根因的主力。

#### 场景比喻

像编辑技术手册：不能让编辑凭记忆读完一整箱纸后自由发挥。先把手册按完整主题放入索引，按问题取出相关页，再让编辑逐条对照页码核实后写摘要；编辑的核对清单和资料索引必须同时存在。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Base | 技术手册索引 | 存储/检索语义完整的文档片段。 |
| Semantic Chunking + Embedding | 按主题分册与索引 | 减少跨主题切断，提升相关片段召回。 |
| RAG | 编辑的参考资料 | 将检索到的源内容放入摘要上下文，降低无依据生成。 |
| Zero-Shot CoT prompt | 事实核对清单 | 要求生成前分解任务并检查证据，再输出摘要。 |

#### 正确答案与推理

**正确答案：`AB`**

1. 长文档一次性输入造成上下文负担和遗漏；B 用语义切分和 embedding，让检索围绕相关源片段进行。
2. 事实准确率要求 grounding；B 的 RAG 把源内容带入生成上下文，比只改采样参数更直接降低幻觉。
3. A 用明确的逐步核对指令补上生成过程的检查步骤，帮助模型区分事实、证据和待确认内容。
4. 题目还要求高吞吐和每份 3 秒；AB 是记录答案，但 CoT 的额外 token 可能影响延迟，必须用限长输出、并发和真实文档压测验证，不能把答案当作性能保证。

#### 逐项排除

- **A：正确。** Zero-Shot CoT 加事实验证指令提供生成前的核对框架；按题目记录，它是降低幻觉的 Prompt 层措施。
- **B：正确。** Knowledge Base、语义切分和 embedding 让摘要依据相关源片段，直接处理长文档 grounding。
- **C：不选。** Guardrails 擅长不当内容和敏感信息策略，并没有可靠的通用“幻觉模式”事实判定器。
- **D：不选。** 提高 Temperature 增加输出随机性，通常与降低虚构细节的目标相反。
- **E：不选。** 继续一次性总结保留了上下文溢出、遗漏和无依据补全的原问题。

#### 解题方法

把“减少幻觉”拆为 grounding 和生成纪律：先找 RAG/Knowledge Base/语义检索，再找要求核验的 Prompt。不要用 Guardrails 代替事实评估，也不要把更高 Temperature 当作质量提升；若有严格延迟，额外 CoT token 必须列为风险并压测。

### English

#### Exam focus and background

This question tests two complementary ways to reduce hallucinations in long-document summarization. RAG grounds generation in retrievable source content; a prompt can require a deliberate fact-checking sequence before the final summary. The current design—putting an entire long document into one request—creates context pressure, omissions, and unsupported completion. Raising temperature increases randomness, and Guardrails are not a general factuality verifier.

The recorded answer is AB, but its exam boundary matters. Zero-shot chain-of-thought instructions provide a “analyze, verify, then answer” discipline; they do not prove truth and may consume latency. Semantic chunking and tuned embeddings help the knowledge base retrieve coherent, relevant passages, so RAG addresses the root grounding problem. Production must still cap output, measure token cost, and benchmark the three-second target rather than treating AB as a performance guarantee.

#### Analogy

Think of editing a technical manual. Do not ask an editor to remember an entire box of pages and improvise. Index the manual by complete topics, retrieve the relevant pages, and require the editor to check each claim against those pages before writing. The source index and the checking list work together.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Knowledge Base | Manual index | Stores and retrieves semantically coherent document passages. |
| Semantic chunking + embeddings | Topic binders and index | Avoids arbitrary topic breaks and improves relevant retrieval. |
| RAG | Editor’s reference pages | Places source content in the summary context to reduce unsupported generation. |
| Zero-shot CoT prompt | Fact-check checklist | Instructs the model to decompose and verify before producing the summary. |

#### Correct answer and reasoning

**Correct answer: `AB`**

1. The one-shot full-document design creates context pressure and missed evidence. B uses semantic chunks and embeddings so retrieval can focus on relevant source passages.
2. Factual accuracy requires grounding. B’s RAG supplies source content to generation, a more direct control than changing sampling parameters.
3. A adds an explicit prompt discipline that asks the model to reason through and verify facts before output, complementing grounding.
4. The recorded answer is AB. The throughput and three-second requirements still need testing: extra CoT tokens can increase latency, so output bounds, concurrency, and real documents must be measured.

#### Option-by-option elimination

- **A: Correct.** Zero-shot CoT with explicit fact verification provides the prompt-level checking structure intended by the question.
- **B: Correct.** A knowledge base, semantic chunking, and embeddings ground summaries in relevant source material.
- **C: Eliminate.** Guardrails handle safety and sensitive-information policies; they do not provide a reliable general-purpose hallucination-pattern fact checker.
- **D: Eliminate.** Increasing temperature increases randomness and normally conflicts with reducing fabricated details.
- **E: Eliminate.** Summarizing the entire document in one pass preserves the original context, omission, and unsupported-completion problems.

#### Exam strategy

Split “reduce hallucinations” into grounding and generation discipline. Look for RAG/Knowledge Base/semantic retrieval first, then a prompt that requires verification. Do not use Guardrails as a factuality evaluator or higher temperature as a quality control. If latency is strict, treat extra reasoning tokens as a risk to benchmark.

---

## Question 31

### 中文

#### 考点背景

本题考查“运行指标 + 相对基线的异常检测 + 关联证据”的完整可观测性。推荐质量、token 用量和延迟是不同维度，不能只看 EC2 CPU 或一个延迟阈值；还应按请求类型、用户群体等 Dimension 切开，避免把正常的流量差异误判为模型退化。CloudWatch Anomaly Detection 用历史数据建立动态基线，适合发现间歇性偏离。

Application Insights 负责应用资源层的发现和监控，EMF 让应用把结构化日志转成 CloudWatch 自定义指标，Logs Insights 再提供错误模式和上下文。题目要求十分钟内生成带相关性数据的告警，因此必须让指标、维度、日志和请求关联 ID 能互相回溯。记录答案 C 的优势是覆盖信号、基线、模式和关联，而不是只画一个 Dashboard。

#### 场景比喻

像连锁商店的运营室：不只看总销售额，还按门店和时段建立正常曲线；某门店推荐商品突然不合顾客口味时，警报同时附上订单类型、顾客群和错误日志，值班员能沿着同一张小票追查。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| CloudWatch Application Insights | 运营地图 | 发现并监控应用资源及相关组件。 |
| CloudWatch EMF | 结构化仪表读数 | 发布推荐质量、token 和延迟等带 Dimension 的自定义指标。 |
| CloudWatch Anomaly Detection | 动态基线 | 识别模型指标相对历史正常模式的偏离。 |
| CloudWatch Logs Insights | 监控录像检索 | 分析错误/日志模式，补充告警的关联上下文。 |

#### 正确答案与推理

**正确答案：`C`**

1. “运行指标”要求覆盖推荐质量、token 和响应延迟；C 用 EMF 发布三类自定义指标，而非只看容器资源。
2. “相对 Baseline 检测退化”匹配 CloudWatch Anomaly Detection，它能按时间序列判断异常，不必为每个时段手写固定阈值。
3. “按请求类型和用户群体分析”匹配 Dimensions，可定位是某类流量还是全局 FM 行为发生变化。
4. “十分钟内带相关性数据告警”由 CloudWatch 指标告警及时触发，Logs Insights 用请求关联信息和错误模式补全证据；因此题库记录答案为 C。

#### 逐项排除

- **A：不选。** Container Insights 偏基础设施/容器，题目应用在 EC2；固定延迟 Alarm 不能检测推荐质量相对基线的异常。
- **B：不选。** X-Ray 有链路追踪价值，但 CloudTrail 记录 API 活动、QuickSight 适合分析展示，组合没有模型指标异常基线与及时关联告警的闭环。
- **C：正确。** Application Insights、EMF、Dimensions、Anomaly Detection 和 Logs Insights 分别对应发现、指标、切片、基线和证据。
- **D：不选。** OpenSearch 能分析日志，但需额外摄取和查询运维；题目所需的 CloudWatch 原生异常检测与十分钟告警匹配不如 C 直接。

#### 解题方法

先把可观测性拆成四层：业务指标、维度切片、动态基线、关联日志。看到“deviation from baseline/间歇性退化”优先找 Anomaly Detection；看到“within 10 minutes/相关性数据”再确认 Alarm、EMF 和 Logs Insights 是否连成证据链。

### English

#### Exam focus and background

This question asks for complete observability: operational and business metrics, baseline-relative anomaly detection, and correlated evidence. Recommendation quality, token usage, and latency are different signals; EC2 CPU or one fixed latency threshold cannot explain an intermittent quality regression. Dimensions such as request type and user segment help distinguish a traffic-mix change from a model-wide problem. CloudWatch Anomaly Detection is the intended dynamic-baseline mechanism.

Application Insights helps discover and monitor application resources. Embedded Metric Format lets the application emit structured logs that become CloudWatch custom metrics, while Logs Insights supplies searchable error patterns and context. The ten-minute alert must point back to correlated request data, so metrics, dimensions, logs, and request identifiers need to work together. The recorded answer, C, covers that chain instead of merely drawing a dashboard.

#### Analogy

Imagine a retail operations room. It tracks sales by store and hour against a normal curve. If recommendations suddenly miss the preferences of one customer segment, the alert includes the store, segment, order type, and the matching receipt/log trail so the operator can investigate the same event.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| CloudWatch Application Insights | Operations map | Discovers and monitors application resources and components. |
| CloudWatch EMF | Structured gauge reading | Publishes recommendation quality, token, and latency metrics with dimensions. |
| CloudWatch Anomaly Detection | Dynamic baseline | Detects deviations from expected historical model behavior. |
| CloudWatch Logs Insights | Recording search | Finds error/log patterns and supplies correlated alert context. |

#### Correct answer and reasoning

**Correct answer: `C`**

1. “Operational metrics” requires recommendation quality, token usage, and latency. C emits all three as custom metrics through EMF rather than monitoring only infrastructure.
2. “Degradation compared with a baseline” maps to CloudWatch Anomaly Detection, which evaluates time-series deviations instead of requiring only static thresholds.
3. “Request types and user segments” maps to metric dimensions, allowing the team to isolate a traffic slice or identify a global FM behavior change.
4. “Correlation data within ten minutes” is addressed by timely CloudWatch alarms plus Logs Insights queries that connect request identifiers to error patterns. The recorded answer is C.

#### Option-by-option elimination

- **A: Eliminate.** Container Insights is aimed at infrastructure/container visibility, while the application runs on EC2. A fixed latency alarm cannot detect recommendation-quality drift against a baseline.
- **B: Eliminate.** X-Ray helps trace requests, and CloudTrail records API activity, but the combination lacks model metrics, anomaly baselines, and a direct timely correlation loop.
- **C: Correct.** Application Insights, EMF, dimensions, Anomaly Detection, and Logs Insights map to discovery, metrics, segmentation, baselines, and evidence.
- **D: Eliminate.** OpenSearch can analyze logs, but it adds ingestion/query operations and is less direct than the stated CloudWatch anomaly and alerting path.

#### Exam strategy

Decompose observability into business signals, dimensions, a dynamic baseline, and correlated logs. For “deviation from baseline” look for Anomaly Detection; for “within ten minutes with correlation data,” verify that alarms, EMF metrics, Logs Insights, and request correlation can form one evidence chain.

---

## Question 32

### 中文

#### 考点背景

本题考查长文档超过 FM Context Window 时的切分与检索。把 50～200 页平均切开或顺序拼到窗口上限，会把不相关内容带入请求，仍可能截断输出；更糟的是，句子、表格和定义可能在边界被拆散。Semantic Chunking 根据语义变化点切块，能让一个 Chunk 更可能保持完整主题。

但切分只是索引阶段，运行时还要检索。`RetrieveAndGenerate` 依据 embedding 相似度挑出与问题最相关的 Chunk，把有限上下文留给真正需要的证据。记录答案 C 中的 95% breakpoint 和 3 句 buffer 是调参示例，不是普适魔法值；正确考点是语义边界 + 动态相关性检索，而不是盲目填满窗口。

#### 场景比喻

像把厚技术手册整理进急救箱：不按固定页数撕成纸条，而按“故障现象—原因—修复步骤”分包；遇到具体故障只取相关几包，避免把整本手册塞到医生手里导致找不到重点。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Semantic Chunking | 按主题分包员 | 在语义断点切出较完整的文档单元。 |
| Embedding | 内容索引坐标 | 把文档块和查询放到可比较的语义空间。 |
| Amazon Bedrock Knowledge Base | 分包仓库 | 管理文档、向量索引和检索配置。 |
| `RetrieveAndGenerate` | 急救取包员 | 动态取相关 Chunk，再用 FM 生成回答。 |

#### 正确答案与推理

**正确答案：`C`**

1. 先处理 Context Window：不把多个 Chunk 机械拼到最大值，而是减少单次请求中的无关 token。
2. Semantic Chunking 保留技术主题、定义和步骤的边界；95% breakpoint、3 句 buffer 是题目给出的可调参数。
3. Embedding Similarity Score 让 `RetrieveAndGenerate` 针对查询动态选择最相关片段，从而在窗口有限时提高证据密度。
4. 这同时改善截断和不一致问题，且使用 Knowledge Base/托管检索的运维少于自写预处理聚合；故记录答案为 C。参数仍应以真实文档评测调优。

#### 逐项排除

- **A：不选。** Fixed-size + 顺序拼接仍可能把无关内容塞满窗口，并不能保证跨段语义完整或避免输出截断。
- **B：不选。** Hierarchical Chunking 有价值，但本题记录答案强调语义断点和基于相似度的动态选择；父块自动选择也可能携带过多上下文。
- **C：正确。** Semantic Chunking、embedding 相似度和 `RetrieveAndGenerate` 直接针对语义完整性、相关性和窗口限制。
- **D：不选。** 把文档平均切片独立摘要再汇总会丢跨片段关系，且需要自建 tokenizer、聚合和错误处理逻辑。

#### 解题方法

看到“超过 Context Window + 长技术文档”，先找 semantic chunking 或层次化切分，再确认是否有 retrieval 只取相关块。若选项只是“切小后顺序拼接”或“独立总结再汇总”，要警惕它没有解决相关性、跨段一致性和运维问题。

### English

#### Exam focus and background

This question tests chunking and retrieval when documents exceed a foundation model’s context window. Splitting a 50–200 page manual by an arbitrary page or token count and then concatenating chunks up to the limit still sends irrelevant material and can truncate the answer. It can also cut a sentence, table, or definition at an unsafe boundary. Semantic chunking uses meaning changes to keep a chunk closer to a complete topic.

Chunking is only the indexing side; runtime retrieval is equally important. `RetrieveAndGenerate` uses embedding similarity to select the passages most relevant to the query, preserving scarce context for useful evidence. The 95-percentile breakpoint and three-sentence buffer in C are tuning examples, not universal magic values. The tested idea is semantic boundaries plus dynamic relevance retrieval, rather than filling the context window blindly.

#### Analogy

Organize a thick technical manual into emergency kits. Do not tear it into equal scraps; package “symptom, cause, and repair steps” together. When a specific fault appears, take only the relevant kits instead of handing the whole manual to the technician and burying the answer.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Semantic chunking | Topic packager | Creates document units around semantic boundaries. |
| Embeddings | Content coordinates | Put document chunks and queries into a comparable semantic space. |
| Amazon Bedrock Knowledge Base | Package warehouse | Manages documents, vector index, and retrieval configuration. |
| `RetrieveAndGenerate` | Emergency-kit picker | Retrieves relevant chunks and generates the answer with the FM. |

#### Correct answer and reasoning

**Correct answer: `C`**

1. Address the context limit by avoiding a request filled with unrelated tokens rather than concatenating chunks until the maximum.
2. Semantic chunking preserves boundaries around technical topics, definitions, and procedures. The threshold and buffer are tunable values supplied by the option.
3. Embedding similarity lets `RetrieveAndGenerate` select relevant passages dynamically, increasing evidence density within the limited context.
4. This addresses truncation and inconsistency while using a managed knowledge-base retrieval path instead of custom preprocessing and aggregation. The recorded answer is C; the actual parameter values still require evaluation on the real corpus.

#### Option-by-option elimination

- **A: Eliminate.** Fixed-size chunks followed by sequential concatenation can fill the context with irrelevant material and does not ensure semantic completeness or prevent truncation.
- **B: Eliminate.** Hierarchical chunking can be useful, but the recorded answer focuses on semantic boundaries and similarity-based dynamic selection; a selected parent can still bring too much context.
- **C: Correct.** Semantic chunking, embedding similarity, and `RetrieveAndGenerate` directly target coherence, relevance, and the context-window limit.
- **D: Eliminate.** Independently summarizing average slices and aggregating them can lose cross-chunk relationships and requires custom tokenizer, aggregation, and failure handling.

#### Exam strategy

For “document exceeds context window,” look for semantic or hierarchical chunking plus retrieval of only relevant chunks. Be suspicious of “split and concatenate” or “summarize each slice” when the option does not address relevance, cross-section consistency, and custom operational work.

---

## Question 33

### 中文

#### 考点背景

本题是实时并发和成本的模型选型。已有转录架构，新的 AI 层只需接收文本并在 200 ms 内生成建议；因此“大而会推理”不是优势，低延迟模型和足够的稳定吞吐才是核心。500,000 路并发意味着必须预先规划容量、限制每次输出长度，并让容量随需求受控扩展。

记录答案 B 的考试逻辑是：Bedrock 的低延迟实时模型配合 Provisioned Throughput，减少按需高峰抖动，再配置自动扩缩容策略以遵守预算和保持弹性。技术上要保持边界意识：Provisioned Throughput 是固定购买的容量，不能被描述成自动无限扩缩；实际是否能达到 200 ms/500,000 并发必须用目标模型、配额和负载测试验证。

#### 场景比喻

像呼叫中心在暴雨前开足够多的快速服务台：每张桌子都用短流程，先保证首句建议迅速出现；客流下降时回收多余桌位。买一辆功能复杂但慢的卡车，或用临时帐篷等车流，都会错过 200 ms 的窗口。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Low-latency Bedrock model | 快速服务台 | 针对实时建议优化首字节/响应延迟。 |
| Provisioned Throughput | 预留服务席位 | 为持续高并发提供可预测吞吐容量。 |
| Auto scaling policy | 客流调度表 | 按负载调整容量或路由策略，控制预算。 |
| Existing Transcribe stream | 前端速记员 | 已提供实时文本，避免本题重复设计转录。 |

#### 正确答案与推理

**正确答案：`B`**

1. “已有转录”说明瓶颈在文本到建议的模型推理，不需要再增加离线处理链。
2. “500,000 并发、200 ms”要求低延迟实时模型，A 的复杂推理/批处理方向与目标相反。
3. “稳定高吞吐”匹配 Provisioned Throughput；B 再要求自动扩缩容/容量调度，使方案能根据高峰而不是完全依赖按需限流。
4. “预算不超且保留弹性”要求输出限制、并发配额和容量策略一起压测；在题库记录框架下答案为 B，但服务不会自动保证该数量级或延迟。

#### 逐项排除

- **A：不选。** 大型复杂推理模型和批处理优化增加计算与响应时间，不适合实时 200 ms 建议。
- **B：正确。** 低延迟模型、预置吞吐和容量扩展方向同时回应实时性、并发、预算和弹性；实际需验证自动扩缩容的实现方式。
- **C：不选。** 专用 GPU SageMaker Endpoint 可控但成本和基础设施运维高，且大型 LLM 不利于预算与 200 ms 目标。
- **D：不选。** Serverless Endpoint 和批处理优化可能有冷启动/排队延迟，不能作为 500,000 路实时响应的首选。

#### 解题方法

先按“实时还是批处理”筛掉大模型批处理和 Serverless；再按“稳定吞吐”找 Provisioned Throughput，按“成本/弹性”检查扩展和配额。遇到极端并发与毫秒级目标，答案只能是候选方向，仍要警惕服务能力与真实压测不能靠关键词保证。

### English

#### Exam focus and background

This is a model-selection question driven by real-time concurrency, latency, and cost. The transcription architecture already exists, so the new AI layer receives text and must produce a suggestion in under 200 ms. A large reasoning model is not automatically better here; a low-latency model and predictable capacity are the primary concerns. Five hundred thousand concurrent calls also require capacity planning, bounded outputs, service quotas, and load testing.

The recorded exam logic for B is a low-latency Bedrock model with Provisioned Throughput and an automatic scaling policy. Provisioned capacity reduces peak variability, while the policy represents controlled elasticity and budget management. Keep the technical boundary clear: Provisioned Throughput is purchased capacity and does not mean unlimited automatic scaling by itself. Whether the target model and quotas can meet 500,000 concurrent calls and 200 ms must be verified with a real benchmark.

#### Analogy

Imagine opening fast service desks before a storm. Each desk follows a short script so the first suggestion appears quickly, and extra desks are scheduled as the crowd changes. A feature-heavy slow truck or a temporary tent that waits for each customer would miss the 200 ms window.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Low-latency Bedrock model | Fast service desk | Optimizes time to the initial real-time suggestion. |
| Provisioned Throughput | Reserved desks | Provides predictable inference throughput for sustained concurrency. |
| Auto scaling policy | Crowd schedule | Adjusts capacity or routing policy under load and budget limits. |
| Existing Transcribe stream | Front-desk stenographer | Already supplies live text, so transcription is not redesigned here. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. Existing transcription means the bottleneck is text-to-suggestion inference, not a new batch transcription pipeline.
2. “500,000 concurrent calls and 200 ms” points to a low-latency real-time model; A’s complex reasoning and batch orientation conflict with that target.
3. “Consistent high throughput” points to Provisioned Throughput. B also includes a scaling/capacity policy so the design is not entirely dependent on on-demand throttling during peaks.
4. “Stay within budget and retain elasticity” requires output bounds, quota planning, and load tests. Under the recorded framing, B is correct, but the services do not automatically guarantee this scale or latency.

#### Option-by-option elimination

- **A: Eliminate.** A large complex reasoning model optimized for batch processing increases compute and response time and conflicts with a 200 ms real-time target.
- **B: Correct.** A low-latency model, provisioned capacity, and controlled scaling address real-time behavior, throughput, budget, and elasticity; the exact scaling mechanism still needs validation.
- **C: Eliminate.** A dedicated GPU SageMaker endpoint can be controlled, but it carries greater infrastructure and cost overhead, while a large LLM works against the stated constraints.
- **D: Eliminate.** A serverless endpoint optimized for batch workloads can introduce cold-start or queueing latency and is not the natural first choice for massive real-time concurrency.

#### Exam strategy

First separate real-time from batch options. Then map “stable throughput” to Provisioned Throughput and “budget plus elasticity” to a capacity/routing policy. For extreme concurrency and millisecond targets, treat the answer as a design direction, not proof: validate model support, quotas, scaling behavior, and p95 latency with load tests.

---

## Question 34

### 中文

#### 考点背景

本题考查 Amazon Bedrock Model Evaluation 的数据入口、指标选择和 Dashboard 生产。公司已有电商自定义数据集，最省运维的路径是把数据放在 S3，授予评估任务需要的 IAM 权限，并满足该评估流程访问 S3 所需的 CORS 配置。然后按任务选择有意义的指标：文本生成可看 RWK，摘要可看 BERTScore，而不是拿毒性指标冒充准确率。

评估任务本身可以由 Lambda 按计划创建/查询，结果和状态写入 CloudWatch，再用 Logs Insights 展示趋势。记录答案 AC 的含义是“托管 Bedrock Evaluation + 标准数据/指标 + 轻量自动化”，不是用 Notebook 逐条直接调用 FM。注意 CORS 只解决浏览器/评估访问的跨域授权问题，IAM 仍是服务角色读取 S3 的权限边界。

#### 场景比喻

像电商公司每周给多个模型做同一套盲测：S3 是题库，IAM 是监考证，Bedrock Evaluation 是统一阅卷机，Lambda 是定时发卷和查成绩的人，CloudWatch Dashboard 是成绩墙。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon S3 | 标准化题库 | 存放自定义电商 Prompt/验证数据，供评估任务读取。 |
| Amazon Bedrock Model Evaluation | 统一阅卷机 | 按任务运行模型评估并计算 RWK、BERTScore 等合适指标。 |
| AWS Lambda | 定时监考员 | 创建任务、查询状态并把结果转为可观测日志。 |
| Amazon CloudWatch | 成绩墙 | 保存自定义状态/结果并用 Logs Insights 构建 Dashboard。 |

#### 正确答案与推理

**正确答案：`AC`**

1. “自定义标准化输入”匹配 S3 数据集；A 同时配置 IAM 和题目要求的 CORS 访问条件。
2. “文本生成和摘要准确率”需要与任务匹配的指标；C 选择文本 RWK、摘要 BERTScore，而不是泛化的安全或语音指标。
3. “Dashboard”要求把异步任务状态和结果导出到 CloudWatch；C 用 Lambda 创建/轮询任务，再由 Logs Insights 组织展示。
4. A+C 复用 Bedrock 原生评估能力，避免 SageMaker Notebook、开源框架和逐条 InvokeModel 的自建执行面，因此按题库记录答案为 AC。

#### 逐项排除

- **A：正确。** S3、IAM 和评估访问所需 CORS 让 Bedrock Evaluation 能读取自定义数据集。
- **B：不选。** VPC Endpoint 解决网络私有访问方向，但题目需要的评估数据访问配置还缺少 CORS；它不是 A 的等价替代。
- **C：正确。** 计划创建/查询 Bedrock Evaluation，使用 RWK/BERTScore，并将结果带入 CloudWatch Dashboard。
- **D：不选。** SageMaker Clarify/开源评估扩大运维；WER 和 Toxicity 也不是题目所述文本生成/摘要准确率的匹配指标。
- **E：不选。** Notebook、直接 InvokeModel 和 `fmevals`/`ragas` 需要自行维护运行环境与评估管道，运维高于原生 Bedrock Evaluation。

#### 解题方法

遇到“自定义数据集 + 模型评估 + 最低运维”，先锁定 S3 + Bedrock Evaluation，再核对 IAM/CORS、任务类型和指标语义。看到 WER、Toxicity 或 Notebook 时，问它是否真的测“准确率”以及是否绕开了托管评估。

### English

#### Exam focus and background

This question tests the data path, metric choice, and dashboarding pattern for Amazon Bedrock Model Evaluation. The company already owns custom ecommerce datasets, so the low-operations route is to place the datasets in S3, grant the evaluation job the required IAM access, and configure the S3 access conditions required by the evaluation workflow, including CORS in the exam framing. Metrics must match the task: RWK is appropriate for text generation, while BERTScore is appropriate for summarization quality; toxicity is not an accuracy metric.

Lambda can schedule evaluation-job creation and status checks, then publish state and results to CloudWatch for a Logs Insights dashboard. The recorded answer AC represents managed Bedrock Evaluation plus lightweight automation, not a notebook that directly invokes every model and maintains an open-source evaluation stack. CORS addresses the required cross-origin access path; IAM remains the authorization boundary for the evaluation service role.

#### Analogy

Imagine an ecommerce company running the same blind test for several models. S3 is the question bank, IAM is the examiner’s credential, Bedrock Evaluation is the standardized grader, Lambda schedules the exam and checks results, and CloudWatch is the score wall.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon S3 | Standardized question bank | Stores custom ecommerce prompts/validation data for evaluation jobs. |
| Amazon Bedrock Model Evaluation | Standardized grader | Runs task-specific evaluations and computes suitable metrics. |
| AWS Lambda | Scheduled examiner | Creates jobs, checks status, and turns results into observable logs. |
| Amazon CloudWatch | Score wall | Stores status/results and supports a Logs Insights dashboard. |

#### Correct answer and reasoning

**Correct answer: `AC`**

1. “Custom standardized inputs” maps to an S3 dataset. A supplies the IAM permissions and the CORS configuration required for the evaluation job’s access path.
2. “Accuracy for text generation and summarization” requires task-aligned metrics. C selects RWK for generation and BERTScore for summarization rather than a safety or speech metric.
3. “Display in dashboards” requires exporting asynchronous job status and results to CloudWatch. C uses Lambda to create and poll jobs and Logs Insights to organize the view.
4. A plus C reuse native Bedrock evaluation capabilities and avoid a self-managed notebook, open-source runtime, and per-request InvokeModel pipeline. The recorded answer is AC.

#### Option-by-option elimination

- **A: Correct.** S3, IAM, and the required CORS access configuration allow Bedrock Evaluation to read the custom dataset.
- **B: Eliminate.** A VPC endpoint addresses private network access, but the exam’s evaluation data path still requires the stated CORS configuration; it is not an equivalent replacement for A.
- **C: Correct.** It schedules and polls Bedrock Evaluation, selects RWK/BERTScore, and publishes results for a CloudWatch dashboard.
- **D: Eliminate.** SageMaker Clarify and open-source execution add operations, while WER and toxicity do not match the requested text-generation and summarization accuracy measures.
- **E: Eliminate.** A notebook, direct InvokeModel calls, and `fmevals`/`ragas` require the team to maintain the runtime and evaluation pipeline, increasing overhead over native Bedrock Evaluation.

#### Exam strategy

For “custom dataset + model evaluation + least operations,” start with S3 and Bedrock Evaluation, then verify IAM/CORS, task type, and metric meaning. When an option offers WER, toxicity, or a notebook, ask whether it truly measures the requested accuracy and whether it bypasses a managed evaluation service.

---

## Question 35

### 中文

#### 考点背景

本题考查强制人工监督的长暂停工作流。法规要求“所有 AI 建议都必须由技术人员审核”，所以不能把人工审核当作低置信度时才触发的可选分支；每一条建议都要进入人工任务，工作流在等待期间必须保持状态，并在审核完成后继续或结束。`waitForTaskToken` 是 Step Functions 把执行暂停、交出任务令牌、等待外部人工结果的回调模式。

审核决定还必须可审计，因此回调不仅要调用 `SendTaskSuccess` 传递批准/拒绝，还要把建议 ID、审核人、时间、决定和理由写入持久存储。记录答案 B 用 Step Functions 管理等待和恢复，用 Lambda 接收人工系统回调，用 DynamoDB 保存审计记录；它比自建 SQS 状态机少一层核心编排工作。

#### 场景比喻

像维修工提交电梯检修单后，调度台把单子放到技术主管的签字篮，整条流水线暂停并保留一张取件牌。主管签字后，调度台用这张牌准确唤醒原任务；签字原件进入审计档案，不会只留在缓存里。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Step Functions | 暂停的调度台 | 用 `waitForTaskToken` 保留执行状态并等待人工回调。 |
| `SendTaskSuccess` | 取件牌回执 | 将审核结果送回暂停的任务，使工作流继续。 |
| AWS Lambda | 回调接线员 | 接收人工系统结果并调用 Step Functions 回调 API。 |
| Amazon DynamoDB | 审计档案柜 | 持久化每次人工决定及其上下文。 |

#### 正确答案与推理

**正确答案：`B`**

1. “所有建议必须审核”要求每条建议都进入人工审批 Task，而不是只对异常建议抽样。
2. “等待人工、之后继续”要求可恢复的 callback pattern；B 用 `waitForTaskToken` 暂停并保留执行上下文。
3. “批准 AI 建议”要求人工完成后传回决定；B 通过 Lambda 调用 `SendTaskSuccess`，把批准/拒绝结果交给状态机。
4. “所有决定可审计”要求持久、可查询记录；B 将审核人、结果和时间等写入 DynamoDB。四项闭环对应题干，故答案为 B。

#### 逐项排除

- **A：不选。** Lambda+SQS 可以自建队列，但需要自行维护任务状态、超时、回调和恢复逻辑；题目要求的人工暂停编排不如 B 直接。
- **B：正确。** Step Functions callback、Lambda 回调和 DynamoDB 审计完整覆盖强制人工监督。
- **C：不选。** Glue Workflow 面向数据处理，不是该人审场景的自然编排服务；`SendTaskSuccess` 也属于 Step Functions 任务语义。
- **D：不选。** EventBridge 可路由事件，Glue Job 不是人工任务状态机，ElastiCache 是缓存而非可靠审计存储。

#### 解题方法

看到“人工批准后继续/暂停执行”立即联想到 Step Functions `waitForTaskToken` + `SendTaskSuccess/Failure`。再检查人工是否覆盖全部项目，以及决定是否写入持久审计库；SQS、EventBridge 只能传递事件，不能自动替代可恢复的人审状态。

### English

#### Exam focus and background

This question tests a durable human-oversight workflow with a mandatory pause. Because every AI recommendation must be reviewed, human review cannot be an optional low-confidence branch. Each recommendation must create a human task, the workflow must retain its state while waiting, and it must resume or finish when the reviewer responds. Step Functions’ `waitForTaskToken` is the callback pattern for pausing an execution and waiting for an external result.

The review decision must also be auditable. The callback should return approval or rejection with `SendTaskSuccess`, while a durable store records the recommendation ID, reviewer, timestamp, decision, and reason. The recorded answer, B, uses Step Functions for waiting and resumption, Lambda for the callback, and DynamoDB for audit records. This avoids building the central state machine from raw queues.

#### Analogy

An elevator technician submits a maintenance sheet. The dispatch desk places it in a supervisor’s signature tray, pauses the production line, and keeps a claim ticket. Once signed, the desk uses that ticket to wake the exact waiting task; the signed decision goes into an audit archive rather than temporary memory.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Step Functions | Paused dispatch desk | Uses `waitForTaskToken` to preserve execution state while waiting. |
| `SendTaskSuccess` | Claim-ticket return | Sends the human decision back to the waiting task. |
| AWS Lambda | Callback operator | Receives the human-system result and calls the Step Functions API. |
| Amazon DynamoDB | Audit archive | Persists every human decision and its context. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. “Every recommendation must be reviewed” means every item enters a human-approval task, not only low-confidence cases.
2. “Wait for a person and then continue” requires a resumable callback pattern. B pauses the state machine with `waitForTaskToken` and retains execution context.
3. “Review and approve” requires the decision to return to the workflow. B uses Lambda to call `SendTaskSuccess` with the approval or rejection.
4. “All decisions for audit” requires durable, queryable storage. B writes reviewer, decision, timestamp, and related context to DynamoDB. The recorded answer is B because the complete oversight loop is explicit.

#### Option-by-option elimination

- **A: Eliminate.** Lambda and SQS can form a custom queue, but the team must build task state, timeout, callback, and recovery behavior; B maps more directly to the required orchestration.
- **B: Correct.** The Step Functions callback pattern, Lambda callback, and DynamoDB audit store cover mandatory human oversight.
- **C: Eliminate.** Glue Workflows are designed for data processing, not this human-approval state machine; `SendTaskSuccess` is a Step Functions task concept.
- **D: Eliminate.** EventBridge can route events, but a Glue job is not the human task state machine, and ElastiCache is not durable audit storage.

#### Exam strategy

For “pause until a person approves, then continue,” immediately look for Step Functions `waitForTaskToken` plus `SendTaskSuccess` or `SendTaskFailure`. Check that review covers every required item and that decisions are written to durable audit storage. SQS and EventBridge transport events; they do not automatically provide resumable human-task state.

---

## Question 36

### 中文

#### 考点背景

本题是多选题，要求把扫描文档理解、PII 预处理、Region 数据驻留、人工复核、审计和规模化串成端到端链路。Textract 适合从扫描件提取结构化字段，A2I 适合按置信度把结果送给人工；“同一 Region”不是在事后加标签，而是从上传、处理、人工工作流到存储都选择相应 Region 的资源。Bedrock Guardrails 继续作为模型输入/输出的安全边界。

记录答案为 ABD。B 解决模型前的 PII 删除、输出保护和 Region-specific IAM；D 通过 Region 对应的 S3 存储、元数据/标签和权限保留审计线索；A 解决低置信度页面的人审。三者合起来覆盖功能和治理。严格说，单一架构描述不能自动保证 99.9% 可用性或 25,000/天，仍需服务配额、重试、幂等、监控和跨可用区托管服务验证。

#### 场景比喻

像银行贷款档案中心：扫描员先把表格读成字段，信心不足的页交给同区域审核员；隐私遮罩员在送入评分室前涂掉身份证号；每份原件、处理版本和审核印章都放在该地区的有锁档案库里。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Textract | 扫描档案录入员 | 从扫描金融文档提取结构化字段和置信度。 |
| Amazon A2I | 同区域复核台 | 将低置信度页面交给人工审核。 |
| Lambda + Bedrock Guardrails | 隐私遮罩与模型门卫 | 推理前删除 PII，并限制不当/未授权模型输出。 |
| Regional S3 + IAM | 地区档案库与门锁 | 按 Region 存储原件/版本，控制访问并用 metadata/tag 支持审计。 |

#### 正确答案与推理

**正确答案：`ABD`**

1. 扫描件必须转结构化数据，且低置信度结果要人审；A 用同 Region 的 Textract + A2I 完成提取和路由。
2. FM 推理前不能携带 PII；B 用 Lambda 检测/删除，随后用 Guardrails 保护模型输出，并以 Region-specific IAM 限制访问路径。
3. 数据驻留和审计要求资源位置及处理轨迹可证明；D 应落实为按申请人 Region 选择 S3 bucket/前缀，保存 metadata、tags 和权限证据，而不是只在事后记录一条日志。
4. A、B、D 分别覆盖理解/人审、隐私/权限、存储/审计；托管组件为 25,000/天提供扩展基础。按题库记录答案为 ABD，99.9% 仍需独立可靠性设计。

#### 逐项排除

- **A：正确。** Textract 提取字段、A2I 按置信度人审，并保持人工工作流与申请人 Region 一致。
- **B：正确。** 推理前删除 PII、Guardrails 保护输出、Region-specific IAM 强制数据边界，正面回应隐私和治理。
- **C：不选。** Kendra 和 OpenSearch 是搜索/索引工具，不是扫描文档字段级 OCR/结构化提取的首选；组合还增加无关运维。
- **D：正确。** Region 对应 S3 存储、对象元数据/标签和 IAM 让原件位置与审计线索可管理；实际应使用 Region-specific bucket，因为单个 S3 bucket 不能跨 Region。
- **E：不选。** Glue Data Quality 和 Prompt Engineering 可做额外校验，但没有明确解决推理前 PII 删除及同 Region 人工路由，且引入不必要的处理链。
- **F：不选。** SageMaker Clarify 的公平性/偏差分析不能替代 OCR、PII 清理、Region 人审和审计存储的核心要求。

#### 解题方法

多选时按流水线逐层盖章：Extract/Confidence → Human review → PII before inference → FM safety → Regional storage/IAM → Audit. 每个硬要求都要有对应选项；“标签”只能辅助审计，不能把跨 Region 资源变成同 Region。最后把规模和 99.9% 当作容量/可靠性验证项，不要被单个分析工具带偏。

### English

#### Exam focus and background

This multiple-choice question requires an end-to-end chain for scanned-document understanding, PII handling, Regional residency, human review, auditability, and scale. Textract is the document-extraction component, and A2I is the human-review workflow that can receive low-confidence results. “Same Region” must be designed from upload through processing, human review, and storage; adding a tag afterward does not move data or reviewers.

The recorded answer is ABD. A covers extraction and confidence-based human review. B removes PII before inference, adds an output safety boundary, and uses Regional IAM controls. D provides Region-aligned S3 storage plus metadata/tags and access evidence. Together they cover the functional and governance requirements. Separately, 99.9% availability and 25,000 applications per day still require quota checks, idempotency, retries, monitoring, and a reliability design; the answer choices do not prove those targets automatically.

#### Analogy

Picture a loan-document center. A scanner turns forms into fields, uncertain pages go to a reviewer in the applicant’s Region, a privacy clerk masks identification numbers before the scoring room, and originals, processed copies, and review stamps remain in a locked regional archive.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Textract | Document intake clerk | Extracts structured fields and confidence from scanned financial documents. |
| Amazon A2I | Regional review desk | Routes low-confidence pages to a human reviewer. |
| Lambda + Bedrock Guardrails | Privacy clerk and model gate | Redacts PII before inference and constrains unsafe or unauthorized output. |
| Regional S3 + IAM | Regional archive and lock | Stores data in the proper Region and controls access with audit metadata/tags. |

#### Correct answer and reasoning

**Correct answer: `ABD`**

1. Scanned documents must become structured data and low-confidence results must receive human review. A uses regional Textract and A2I for extraction and routing.
2. PII must be removed before the FM sees the data. B uses Lambda for detection/redaction, Guardrails for output protection, and Regional IAM to restrict the data path.
3. Residency and auditability require provable resource placement and processing history. D should be implemented with Region-specific S3 buckets/prefixes, metadata, tags, and permissions—not merely a later log entry.
4. A, B, and D cover document understanding/review, privacy/access control, and regional storage/audit evidence. Managed services provide a scaling foundation. The recorded answer is ABD; the 99.9% target still needs independent reliability validation.

#### Option-by-option elimination

- **A: Correct.** Textract extracts fields, A2I routes low-confidence pages to people, and the workflow can remain in the applicant’s Region.
- **B: Correct.** Pre-inference PII redaction, Guardrails output protection, and Regional IAM directly address privacy and governance.
- **C: Eliminate.** Kendra and OpenSearch are search/indexing services, not the primary OCR and structured-field extraction path for scans; the pair adds unrelated operations.
- **D: Correct.** Region-aligned S3 storage, object metadata/tags, and IAM make placement and audit evidence manageable. In practice, use Region-specific buckets because one S3 bucket has one Region.
- **E: Eliminate.** Glue quality checks and prompt engineering can be supplemental, but the option does not explicitly solve pre-inference PII redaction or same-Region human routing and adds an unnecessary chain.
- **F: Eliminate.** Clarify fairness/bias analysis cannot replace OCR, PII removal, Regional human review, or audit storage.

#### Exam strategy

For a multi-select pipeline, map each hard requirement to a stage: extract/confidence, human review, PII before inference, FM safety, Regional storage/IAM, and audit. Metadata can support audit but cannot make cross-Region resources local. Treat scale and 99.9% as capacity and reliability validation items, not as proof supplied by one analytics service.

---

## Question 37

### 中文

#### 考点背景

本题考查 Amazon Q Business 对 S3 数据源的文档级 ACL 索引。Q Business 不是只看用户能否调用 Q API；回答时还要根据被索引文档的 Allow/Deny 信息和 IAM Identity Center 的用户/组身份过滤检索结果。题干已经把每个部门映射到一个 Group、每个员工映射到一个部门，最省维护的方式是集中维护 Prefix 到 Group 的 ACL 配置。

记录答案 B 使用 Bucket 顶层单一 `acl.json`，在其中列出每个 S3 Prefix 的 `keyPrefix` 和对应 Group 的访问条目，并在数据源设置中指定路径。这样新增/变更部门只改一份映射文件再重新同步。不要把 S3 IAM Permission Set 当作 Q Business 内容 ACL，也不要把 metadata 文件格式和 ACL 配置混用。

#### 场景比喻

像图书馆的总借阅表：每个书架前缀都写着允许进入的部门组，前台根据员工胸牌查表后才把书放到咨询台。把表格复制到每个书架会难维护；把它藏在普通 metadata 文件里，前台也不会识别成借阅规则。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Q Business S3 connector | 图书馆索引员 | 抓取 S3 文档和 ACL，并在回答时执行安全过滤。 |
| `acl.json` | 总借阅表 | 把 S3 Prefix 映射到 IAM Identity Center Group 的 ALLOW/DENY。 |
| IAM Identity Center | 员工胸牌系统 | 提供用户所属 Group Membership。 |
| S3 Prefix | 部门书架 | 让 ACL 规则按部门范围覆盖对象。 |

#### 正确答案与推理

**正确答案：`B`**

1. 要按组过滤内容，ACL 必须被 Q Business 数据源索引，而不是只存在调用者的 IAM Policy 中。
2. 题干每个部门只有一个对应 Group，因此一个集中配置文件即可枚举 Prefix 与 Group 的一对一关系。
3. B 把 `acl.json` 放在同一 Bucket 顶层，并在数据源 Access Control 配置中引用；同步后 Q Business 可用 Identity Center membership 过滤回答。
4. 集中一份文件比每个文件夹维护一份 ACL 更少运维，故题库记录答案为 B。实际还要注意未出现在 ACL 文件中的 Prefix 的默认可见性，配置时应显式覆盖敏感路径。

#### 逐项排除

- **A：不选。** 题目需要数据源级 ACL 配置；每个部门目录放一份文件增加同步和一致性风险，不是集中映射的最低运维路径。
- **B：正确。** 顶层单一 `acl.json` 集中列出 Prefix 与 Group，并在 Access Control 中引用。
- **C：不选。** Permission Set 控制 AWS 资源 API 权限，不能直接替代 Q Business 索引文档的内容级 ACL；`StringNotEquals` 也不是这里的 ACL 文件机制。
- **D：不选。** `metadata.json` 与数据源 metadata 用途不同；题目要的是 ACL 配置文件和 Access Control 设置，不是任意命名的 metadata 对象。

#### 解题方法

看到“Q Business + S3 + Group Membership + Prefix”，记住三件事：ACL 必须随数据源被索引、格式是 Prefix 到 USER/GROUP 的访问条目、路径在 Access Control 配置中引用。问“最低运维”时优先集中一份映射文件，而不是复制规则或改 IAM 权限。

### English

#### Exam focus and background

This question tests document-level ACL indexing for an Amazon Q Business S3 data source. Q Business must not only authorize a user to call the Q API; it must also filter retrieved documents using the indexed allow/deny information and the user’s IAM Identity Center identity and group membership. Because each department maps to one group and each employee maps to one department, a centralized prefix-to-group ACL file is the lowest-maintenance design.

The recorded answer, B, uses one top-level `acl.json` in the same bucket. The file lists each S3 prefix and the corresponding group access entries, and the data source’s Access Control setting points to it. A department change then updates one mapping file and triggers a resync. IAM permission sets control AWS resource permissions; they are not a substitute for Q Business document ACLs, and a generic metadata file is not automatically interpreted as the ACL configuration.

#### Analogy

Think of a library’s master lending chart. Each shelf prefix lists the department groups allowed to access it, and the front desk checks an employee badge before presenting a book. Copying a chart into every shelf is harder to keep consistent; hiding it in a generic metadata file would not make it a lending rule.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Q Business S3 connector | Library indexer | Crawls S3 documents and ACL information and filters answers securely. |
| `acl.json` | Master lending chart | Maps S3 prefixes to IAM Identity Center group ALLOW/DENY entries. |
| IAM Identity Center | Employee badge system | Supplies the user’s group membership. |
| S3 prefix | Department shelf | Defines the object scope to which an ACL entry applies. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. Group-based content filtering requires ACL data to be indexed by the Q Business data source, not only placed in the caller’s IAM policy.
2. One group per department makes a single mapping file sufficient to enumerate each prefix and its group.
3. B places `acl.json` at the bucket level and references it in the data source Access Control configuration. After synchronization, Q Business can filter responses using Identity Center membership.
4. One maintained mapping is less operational work than one ACL file per folder, so the recorded answer is B. In practice, also account for the documented behavior of prefixes omitted from the ACL file and explicitly cover sensitive paths.

#### Option-by-option elimination

- **A: Eliminate.** A file in every department folder increases synchronization and consistency work and is not the centralized data-source ACL path intended by the question.
- **B: Correct.** One top-level `acl.json` centrally maps prefixes to groups and is referenced through Access Control.
- **C: Eliminate.** Permission sets govern AWS resource/API permissions; they do not replace Q Business’s indexed document ACLs, and the condition-key design is not the S3 ACL-file mechanism.
- **D: Eliminate.** `metadata.json` serves a different metadata purpose. The requirement is an ACL configuration file referenced by the data source’s Access Control setting.

#### Exam strategy

For “Q Business + S3 + group membership + prefixes,” remember three checks: ACLs must be indexed with the data source, entries map prefixes to USER/GROUP access, and the file is referenced in Access Control. For “least operations,” prefer one centralized mapping over copied rules or IAM-policy changes.

---

## Question 38

### 中文

#### 考点背景

本题考查以批准医学文档为唯一知识边界的托管 RAG。Knowledge Base 负责连接并索引批准来源，`RetrieveAndGenerate` 在一次托管调用中完成相关内容检索和回答生成，并返回引用信息。这样应用可以把建议绑定到具体文档，而不是让模型凭训练记忆自由补全。

答案 B 的“不得产生幻觉”应理解为题目目标：grounding 和 citation 能显著降低无依据内容，却不能数学上证明模型零错误。高风险临床生产仍应有医生审核、拒答策略和持续评估。最低运维要求则排斥自建 Kendra/后处理、医学实体比对或手工拆成 Retrieve + InvokeModel + 验证的多组件管道。

#### 场景比喻

像医院药典问答台：医生提问后，工作人员只从盖章药典取出相关页，把建议和页码一起递回；没有找到可靠页码就不应假装知道。Knowledge Base 是药典库，`RetrieveAndGenerate` 是取页并起草回答的一体化台。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Base | 批准药典库 | 只接入批准的临床源文档并建立检索索引。 |
| `RetrieveAndGenerate` | 取页兼起草台 | 检索相关片段、生成建议并返回引用。 |
| Foundation Model | 医学文字起草员 | 在检索上下文约束下组织治疗建议。 |
| Citation metadata | 页码印章 | 让医生核查建议对应的源文档。 |

#### 正确答案与推理

**正确答案：`B`**

1. “仅依据批准文档”要求把知识边界放在批准源组成的 Knowledge Base，而不是把所有资料交给模型。
2. “引用具体来源”要求检索结果带 citation；B 直接使用 `RetrieveAndGenerate` 返回引用，减少自定义拼装。
3. “降低幻觉/事实错误”要求生成以检索上下文为依据，并在无相关证据时设计拒答或人工升级；B 的 grounding 最接近目标，但不是绝对保证。
4. “最低运维”使一个托管检索生成 API 胜过多服务后处理链，故记录答案为 B；临床场景仍需把 B 当作证据支持层，不当作医疗决策的唯一授权者。

#### 逐项排除

- **A：不选。** Kendra 检索后还要自建响应比对和引用后处理，组件更多；题目已有 Bedrock Knowledge Base 的托管路径。
- **B：正确。** 批准文档 + Knowledge Base + `RetrieveAndGenerate` 原生提供 grounded generation 和 citation，运维最少。
- **C：不选。** 医学实体抽取和术语比对不能保证整段治疗建议只来自批准文档，也无法天然提供引用链。
- **D：不选。** `Retrieve`、`InvokeModel` 和自定义验证可实现，但要自行编排证据对照和 citation，运维高于 B。

#### 解题方法

看到“只依据指定文档 + 引用 + 最低运维”，优先找 Knowledge Base + `RetrieveAndGenerate`。把“不得幻觉”理解为 grounding、引用、拒答和评估目标；不要选择仅做实体抽取、另建搜索库或手工后处理的方案。

### English

#### Exam focus and background

This question tests managed RAG with an approved medical corpus as the only knowledge boundary. A Knowledge Base connects and indexes the approved sources. `RetrieveAndGenerate` retrieves relevant passages, generates the answer, and returns citation information in one managed path. The physician can therefore trace a recommendation to a source document instead of relying on the model’s general training memory.

“Must not hallucinate” should be read as the architectural goal: grounding and citations reduce unsupported claims but cannot mathematically prove zero errors. A clinical production system still needs physician review, refusal behavior, and ongoing evaluation. The least-operations requirement rules out a separate search engine plus custom post-processing, entity-to-terminology checks, or a hand-built Retrieve–InvokeModel–verification pipeline. The recorded answer is B.

#### Analogy

Imagine a hospital formulary desk. A doctor asks a question, the clerk retrieves only stamped formulary pages, and returns the recommendation with page numbers. If no reliable page exists, the clerk should not pretend to know. The knowledge base is the formulary archive; `RetrieveAndGenerate` is the combined retrieval and drafting desk.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Knowledge Base | Approved formulary archive | Connects only approved clinical sources and indexes them. |
| `RetrieveAndGenerate` | Page-picker and drafter | Retrieves passages, generates guidance, and returns citations. |
| Foundation Model | Medical drafting clerk | Organizes a recommendation under retrieved-context constraints. |
| Citation metadata | Page stamp | Lets the physician verify the source document. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. “Only approved documentation” requires the knowledge boundary to be the approved-source knowledge base rather than an unrestricted model context.
2. “Specific citations” requires provenance in the retrieval result. B uses `RetrieveAndGenerate` to return citations without custom assembly.
3. “Reduce hallucinations and factual errors” requires generation to be grounded and a refusal/escalation path when evidence is missing. B is the closest managed control, though it is not an absolute guarantee.
4. “Least operations” favors one managed retrieve-and-generate path over several custom services. The recorded answer is B; in clinical use, it is an evidence-support layer, not the sole authority for medical decisions.

#### Option-by-option elimination

- **A: Eliminate.** Kendra plus custom response comparison and citation post-processing adds components and maintenance when a managed Bedrock knowledge-base path is available.
- **B: Correct.** Approved sources, a Knowledge Base, and `RetrieveAndGenerate` provide grounded generation and citations with the least custom work.
- **C: Eliminate.** Medical entity extraction and terminology matching do not ensure that an entire treatment recommendation comes only from approved documents or provide a natural citation chain.
- **D: Eliminate.** Retrieve, InvokeModel, and custom verification can work, but the application must orchestrate evidence comparison and citation itself, increasing operations over B.

#### Exam strategy

For “only these documents + citations + least operations,” look for Knowledge Base plus `RetrieveAndGenerate`. Treat “no hallucinations” as a grounding, citation, refusal, and evaluation objective—not a promise of mathematical perfection. Reject entity extraction or hand-built post-processing when the managed RAG path covers the requirement.

---

## Question 39

### 中文

#### 考点背景

本题考查从实时语音到增量建议的流式链路。低于 1 秒的首条显示要求系统在客户尚未说完时就开始工作：Transcribe Streaming 的 Partial Results 先给出中间转录，Bedrock 的 `InvokeModelWithResponseStream` 再把模型输出分块返回，前端通过 API Gateway WebSocket 持续接收更新。这里的“增量”比把整通话存库后一次处理更重要。

题干还要求双向流式和仅使用托管 AWS 服务。WebSocket 提供坐席与服务端之间的双向连接；Transcribe 的音频输入和模型响应输出共同形成端到端流。记录答案 B 的链路没有多余的 DynamoDB 往返或离线批处理，但实际 1 秒目标仍需控制片段大小、并发、模型响应长度和网络路径。

#### 场景比喻

像同声传译室：客户每说完半句，速记员先递一张临时纸条；顾问立刻写出下一条建议，电子黑板马上刷新。等整段录音结束才开始翻译，哪怕模型能流式输出，也已经错过实时服务窗口。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Transcribe Streaming + Partial Results | 实时速记员 | 在客户说完前输出部分转录。 |
| Bedrock `InvokeModelWithResponseStream` | 分段顾问 | 根据文本片段增量返回模型输出。 |
| API Gateway WebSocket API | 双向电子黑板 | 与坐席保持双向连接并推送更新。 |
| Managed AWS services | 托管通信基础设施 | 满足不自建服务的约束。 |

#### 正确答案与推理

**正确答案：`B`**

1. “实时转录、说话中更新”匹配 Transcribe Streaming + Partial Results，而非 Batch。
2. “首条建议低于 1 秒”要求文本片段一到就调用模型；B 使用 `InvokeModelWithResponseStream`，避免等待完整响应。
3. “增量建议实时显示”要求持续向坐席推送；B 用 API Gateway WebSocket 发送流式更新。
4. 这条链路由 AWS 托管服务组成，并同时处理输入音频流和输出响应流，故题库记录答案为 B；延迟目标仍必须以真实通话负载验证。

#### 逐项排除

- **A：不选。** 虽有 Transcribe Streaming 和 WebSocket，但 `InvokeModel`、Comprehend 和 DynamoDB 增加串行往返，不能保证说话中低延迟增量输出。
- **B：正确。** Partial Results、Bedrock response streaming 和 WebSocket 完整覆盖首条响应、增量和双向通信。
- **C：不选。** Batch Processing 等待通话或批次完成；完整转录再送模型不符合 1 秒和说话中更新。
- **D：不选。** Titan Embeddings 产生向量而不是客服建议，SNS 是发布/订阅通知，不是坐席交互所需的双向实时流。

#### 解题方法

看到“while speaking/说话中”“partial/incremental”“under 1 second”，逐段找 Streaming：输入是 Transcribe Partial Results，模型是 `InvokeModelWithResponseStream`，输出是 WebSocket。Batch、DynamoDB、SNS 和 Embeddings 任一出现都要检查是否破坏实时生成链。

### English

#### Exam focus and background

This question tests a streaming path from live speech to incremental suggestions. A sub-second first display requires processing to begin before the customer finishes speaking. Transcribe Streaming with partial results emits interim text; Bedrock `InvokeModelWithResponseStream` returns model output in chunks; and an API Gateway WebSocket keeps the call-center agent updated. The key is incremental work, not storing the whole call and processing it afterward.

The architecture must use managed AWS services and support bidirectional streaming. WebSocket supplies the two-way connection between the agent interface and service, while Transcribe’s audio stream and the model’s response stream form the end-to-end path. The recorded answer, B, avoids an unnecessary DynamoDB round trip and batch stage. The one-second target still requires controlling chunk size, concurrency, output length, and network path in a real benchmark.

#### Analogy

Imagine a live interpreter booth. As the customer finishes half a sentence, the stenographer hands over an interim note; the adviser immediately drafts the next suggestion and the electronic board refreshes. Waiting for the complete recording would miss the real-time service window even if the model could stream its answer.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Transcribe Streaming + partial results | Live stenographer | Emits interim transcription before speech ends. |
| Bedrock `InvokeModelWithResponseStream` | Incremental adviser | Returns model suggestions in response chunks. |
| API Gateway WebSocket API | Two-way electronic board | Maintains a bidirectional connection and pushes updates. |
| Managed AWS services | Hosted communications room | Satisfies the managed-service constraint. |

#### Correct answer and reasoning

**Correct answer: `B`**

1. “Live transcription and updates while speaking” maps to Transcribe Streaming with partial results, not batch processing.
2. “First suggestion under one second” requires invoking the model as fragments arrive and receiving output progressively. B uses `InvokeModelWithResponseStream`.
3. “Incremental suggestions to the agent” requires a persistent real-time push channel. B uses an API Gateway WebSocket API.
4. The chain uses managed AWS services and streams both input and output, so the recorded answer is B. The latency target must still be measured under realistic call concurrency.

#### Option-by-option elimination

- **A: Eliminate.** It has streaming transcription and a WebSocket, but non-streaming `InvokeModel`, Comprehend, and a DynamoDB round trip add serial latency and do not ensure incremental model output.
- **B: Correct.** Partial results, Bedrock response streaming, and WebSocket delivery match the first-response, incremental, and bidirectional requirements.
- **C: Eliminate.** Batch processing waits for a complete call or batch; sending a full transcript later cannot meet the one-second, while-speaking behavior.
- **D: Eliminate.** Titan Embeddings produces vectors, not agent suggestions, and SNS pub/sub is not the bidirectional interactive stream required here.

#### Exam strategy

For “while speaking,” “partial/incremental,” and “under one second,” match every hop to streaming: Transcribe partial results, `InvokeModelWithResponseStream`, and WebSocket output. Batch processing, database round trips, SNS notifications, and embeddings should trigger a check for broken real-time generation.

---

## Question 40

### 中文

#### 考点背景

本题考查多模态上传内容的托管审核链路：输入可能是图片和文本，模型既不能处理不当内容，也不能在生成作品中泄露 PII，还要接入现有 S3 工作流。正确设计要在进入生成前审查文本/图片，并在模型输入输出边界使用 Guardrails；Step Functions 把这些步骤串起来，便于重试、失败分支和审计。

记录答案 D 的职责分配是 Comprehend PII Detection 先处理文本，Rekognition 做图像审核，Bedrock Guardrails 在模型调用中实施内容/敏感信息策略。这里的“最低基础设施管理开销”指使用托管服务而不是训练、部署和维护自定义审核模型。实际生产还要把 PII 检查应用到输出、明确原始/净化对象的位置和拒绝路径，不能只靠前置检查声称绝对不泄露。

#### 场景比喻

像艺术展入口：文字稿先由隐私管理员遮住身份证号，图片交给图像安检，最后生成室门口还有一位总门卫检查输入和成品。Step Functions 是展厅路线图，S3 是原稿和成品的存放库。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Comprehend PII Detection | 文字隐私管理员 | 预处理文本，识别并删除/遮蔽 PII。 |
| Amazon Rekognition | 图片安检员 | 检测图像中的不当内容。 |
| Amazon Bedrock Guardrails | 生成室总门卫 | 过滤模型输入/输出，执行内容与敏感信息策略。 |
| AWS Step Functions + S3 | 路线图与展品仓库 | 编排审核步骤、失败分支，并接入既有对象工作流。 |

#### 正确答案与推理

**正确答案：`D`**

1. “图片和文本上传、每小时数千用户”要求可扩展托管审核；D 使用 Comprehend 和 Rekognition，不需自建审核模型基础设施。
2. 文本 PII 应在生成前处理；Comprehend 识别后删除/遮蔽，避免把原始敏感值直接送进 FM。
3. 图片不当内容需要图像审核，且模型输入/输出还要有统一安全策略；D 用 Rekognition + Bedrock Guardrails 覆盖两类边界。
4. “接入 S3、低运维”由 Step Functions 读取/保存对象并串联服务完成；故题库记录答案为 D。要实现真正的“不暴露”，还需配置 Guardrails 敏感信息处理和输出验证，并保留原始/净化对象的权限隔离。

#### 逐项排除

- **A：不选。** CloudWatch Alarm 是监控告警，不是内容过滤器；Enhanced Monitoring 也不会执行 PII 或图像审核，流程缺少模型输出安全边界。
- **B：不选。** API Gateway Request Validation 主要验证结构/schema，不理解语义不当内容；SageMaker 自定义审核模型带来训练、部署和扩缩容运维。
- **C：不选。** Cognito Pre-Authentication 是身份生命周期钩子，不是通用内容审核；Textract 是文字提取工具，不能替代 PII/内容政策，且审核前置到上传会改变 S3 工作流。
- **D：正确。** Step Functions、Guardrails、Comprehend 和 Rekognition 分别覆盖编排、模型边界、文本 PII 和图片审核，并使用托管组件。

#### 解题方法

多模态审核要分“文本 PII、图片安全、模型输入/输出、对象编排”四格：Comprehend、Rekognition、Guardrails、Step Functions/S3。看到 Alarm、Request Validation、Cognito 或 Textract 时，确认它们只是监控、schema、身份或 OCR，不能替代语义审核。

### English

#### Exam focus and background

This question tests a managed moderation pipeline for multimodal uploads. Inputs contain text and images, the model must not process or generate inappropriate content, compositions must not expose PII, and the flow must fit an existing S3 workflow. The design should moderate text and images before generation and apply Guardrails at the model boundary. Step Functions can connect the stages with retries, failure branches, and auditable execution.

The recorded answer, D, assigns focused responsibilities: Comprehend PII detection preprocesses text, Rekognition moderates images, and Bedrock Guardrails enforces content and sensitive-information policies around inference. “Least infrastructure management” favors managed services over training and operating custom moderation models. In production, output PII checks, raw-versus-sanitized object permissions, and explicit rejection paths still need to be designed; a pre-check alone is not an absolute no-leak guarantee.

#### Analogy

Imagine an art-exhibit entrance. A privacy clerk masks ID numbers in text, an image inspector checks pictures, and a final guard at the creation room checks both the prompt and the finished composition. Step Functions is the exhibit route map; S3 stores the originals and finished work.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Comprehend PII Detection | Text privacy clerk | Detects and redacts or masks PII during text preprocessing. |
| Amazon Rekognition | Image inspector | Detects inappropriate image content. |
| Amazon Bedrock Guardrails | Final creation-room guard | Filters model input/output and applies content and sensitive-information policy. |
| AWS Step Functions + S3 | Route map and exhibit storage | Orchestrates checks, failure paths, and the existing object workflow. |

#### Correct answer and reasoning

**Correct answer: `D`**

1. “Images and text at thousands of uploads per hour” favors scalable managed moderation. D uses Comprehend and Rekognition rather than operating a custom moderation model.
2. Text PII should be handled before generation. Comprehend identifies it so the application can remove or mask the values before sending data to the FM.
3. Image safety requires image moderation, while the model input/output needs a common policy boundary. D combines Rekognition with Bedrock Guardrails.
4. “Integrate with S3 and minimize infrastructure” is addressed by a Step Functions workflow that reads, sanitizes, checks, invokes, and stores objects. The recorded answer is D. A true no-exposure design must also configure Guardrails sensitive-information handling and isolate raw and sanitized objects.

#### Option-by-option elimination

- **A: Eliminate.** CloudWatch alarms observe events but do not moderate content. Enhanced Monitoring does not perform PII or image checks, and the model-output safety boundary is incomplete.
- **B: Eliminate.** API Gateway request validation checks shape/schema, not semantic harmfulness; a SageMaker custom moderation model adds training, deployment, and scaling operations.
- **C: Eliminate.** Cognito pre-authentication is an identity lifecycle hook, not general content moderation. Textract extracts text and cannot replace PII/content policy; gating before upload also disrupts the stated S3 workflow.
- **D: Correct.** Step Functions, Guardrails, Comprehend, and Rekognition cover orchestration, model-boundary policy, text PII, and image moderation with managed services.

#### Exam strategy

For multimodal moderation, create four slots: text PII, image safety, model input/output, and object orchestration. Match them to Comprehend, Rekognition, Guardrails, and Step Functions/S3. Treat alarms, request validation, Cognito, and Textract as monitoring, schema, identity, or OCR tools—not substitutes for semantic moderation.

---
