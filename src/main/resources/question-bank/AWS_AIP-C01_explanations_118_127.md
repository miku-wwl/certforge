# AIP-C01 Questions 118-127 Explanations

## Question 118

### 中文

#### 考点背景

本题考查生成内容中的 PII 处理，而不是只处理输入文件。题干明确指出客户数据来自 S3，最终要求是对基础模型响应中的 PII 做掩码，并且强调最低运维量。Amazon Bedrock Guardrails 的敏感信息过滤器可以直接挂在模型调用路径上，对识别出的 PII 执行 `MASK` 操作。这样输入进入模型、模型生成响应以及响应返回调用方之间都有一个托管的安全边界，应用不必自行维护实体词典、正则表达式、扫描任务和回写流程。

Amazon Comprehend 的 PII 检测适合对输入文档做预处理，但它不能保证模型不会在输出中重新生成一个输入里没有出现的姓名或编号。Macie 的职责是发现和分类 S3 中的敏感数据，不是实时改写 Bedrock 响应。自定义 Lambda 虽然可以实现业务规则，却会增加代码、测试、扩展、重试和审计维护成本。因此 A 同时满足输出掩码和最低运维量。

#### 场景比喻

像医院在出口安装一个自动安检门：无论资料从哪个科室送来，最终交给访客前都要经过同一个门；发现身份证号就用星号遮住。只在仓库入口检查一次，不能防止工作人员后来又把身份证号写回报告。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 出口安检门 | 在模型调用链路中识别并掩码响应中的 PII |
| Sensitive information filter | 隐私识别器 | 匹配姓名、身份证号等敏感信息类型 |
| Amazon S3 | 原始资料仓库 | 保存客户输入数据，不承担实时响应治理 |
| Amazon Comprehend | 入库扫描员 | 可处理输入实体，但不能替代输出侧防护 |
| AWS Lambda | 定制加工车间 | 能编写规则，但运维量高于托管 Guardrail |

#### 正确答案与推理

1. 先定位数据面：要求针对 FM response 做 PII mask，不是只扫描 S3 文件。
2. 再看动作：题目要求 mask，而不是发现、告警或把文件搬到另一个桶。
3. 最后看运维量：Guardrails 是 Bedrock 原生的托管控制，能够直接接入推理路径。
4. A 用敏感信息过滤器定义 PII 类型并设置 `MASK`，不需要另建自定义扫描和回写链路。

#### 逐项排除

- **A.** 正确；Guardrails 在 Bedrock 响应侧过滤 PII，并以 `MASK` 处理，最贴合“最低运维量”。
- **B.** 不合适；Comprehend 只处理调用前的 S3 输入，模型仍可能在输出中重新生成 PII。
- **C.** 不合适；Macie 负责 S3 数据发现和分类，不能实时掩码每个 FM 响应。
- **D.** 不合适；自定义 Lambda 要自行维护识别、替换、错误处理和扩展，运维成本最高。

#### 解题方法

看到“模型输出中的 PII”“mask”“最低运维量”，优先检查 Bedrock Guardrails 的敏感信息过滤器。把输入清洗、静态数据发现和输出治理分开：Comprehend 或 Macie 的能力并不会自动覆盖模型生成的新内容。题干若要求阻止或掩码响应，服务必须位于推理响应路径上。

### English

#### Exam focus and background

This question is about protecting personally identifiable information in generated content, not merely scanning the input files. The requirement explicitly targets foundation model responses and asks for masking with the least operational effort. Amazon Bedrock Guardrails can be attached to the inference path and can use sensitive information filters with a `MASK` action. The application therefore does not need to maintain its own entity dictionary, regular expressions, scanning jobs, rewrite jobs, retries, and audit logic.

Amazon Comprehend can detect PII in source text before inference, but it cannot guarantee that the model will not generate a new patient name or record number in its response. Amazon Macie discovers and classifies sensitive data in S3; it is not a real-time response redaction service. A custom Lambda pipeline could work, but it would add code, scaling, testing, failure handling, and operational ownership. A places a managed control at the correct boundary and directly satisfies the masking requirement.

#### Analogy

Imagine a hospital that installs an automated privacy gate at the exit of the report system. Every report passes through the gate before it reaches a visitor, and an identified medical record number is replaced with asterisks. Checking only at the warehouse entrance cannot stop a new identifier from being written later in the generated report.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Privacy exit gate | Filters and masks PII in model responses on the inference path |
| Sensitive information filter | Privacy detector | Identifies configured PII categories such as names or record numbers |
| Amazon S3 | Source archive | Holds customer data but does not govern each generated response |
| Amazon Comprehend | Input scanner | Can detect source entities but does not protect newly generated output |
| AWS Lambda | Custom processing shop | Can implement rules, but creates more operational work |

#### Correct answer and reasoning

1. The data boundary is the FM response, so the control must run on the model response path.
2. The required action is masking, not discovery, alerting, or copying objects to another bucket.
3. Bedrock Guardrails is a managed Bedrock-native control that can be attached to inference.
4. Answer A defines the PII type and applies the `MASK` action, covering generated output with minimal custom code.

#### Option-by-option elimination

- **A.** Correct. A managed Bedrock Guardrail filters PII in every configured response and masks it directly.
- **B.** Incorrect. Comprehend scans the S3 input before inference, so it cannot guarantee that the model will not generate new PII.
- **C.** Incorrect. Macie classifies sensitive S3 data; it does not perform real-time redaction of FM responses.
- **D.** Incorrect. A custom Lambda search and multi-bucket rewrite pipeline requires substantially more code and operations.

#### Exam strategy

When a question says “PII in model output,” “mask,” and “least operational effort,” look for Bedrock Guardrails sensitive-information filters. Separate input sanitization, static data discovery, and output governance. Comprehend or Macie may be useful in their own boundaries, but they do not automatically protect newly generated content. The correct service must sit on the inference response path.

---

## Question 119

### 中文

#### 考点背景

本题考查生成式 AI 幻觉检测和评估数据集的使用。题干要求将模型响应与经过验证的医疗信息比较，并且识别相似问题换一种问法后产生的语义不一致。这不是简单的关键词告警，也不是把一个已知答案表拿出来做字符串相等比较；它需要有参考数据集、生成结果评估和语义层面的判断。Amazon Bedrock Evaluations 可以使用自定义提示数据集以及模型评估指标，对响应的事实性、相关性、完整性或一致性进行系统比较。

Guardrails 主要用于内容安全策略，例如敏感信息、主题和词语过滤；它不是通用的事实核验引擎。CloudWatch Logs Insights 能发现日志模式，但不能理解医疗回答是否与参考指南语义一致。SageMaker Feature Store 也不是经过验证临床知识的自然评估存储。B 把参考问答、输出比较和 Bedrock 评估组合起来，最直接覆盖“与验证信息比较”和“相似问题语义不一致”两个要求。

#### 场景比喻

像让多位医生拿同一套经过委员会审核的病例答案作为标准答案，再用不同说法提问模型，检查模型是否仍给出同样的医学结论。只盯着日志中的关键词，无法判断一句医学建议是否偷换了含义。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Evaluations | 医疗质量评审组 | 根据参考数据集评估回答质量和事实性 |
| Custom prompt dataset | 审核过的病例卷 | 提供验证问答和多种相似提问 |
| Amazon Bedrock Guardrails | 内容安全门 | 做安全过滤，不承担完整事实核验 |
| CloudWatch Logs Insights | 日志检索员 | 能找模式和关键词，不能做语义医学比较 |
| Amazon SageMaker Feature Store | 特征仓库 | 不是临床指南评估集的首选载体 |

#### 正确答案与推理

1. 题目要求的是发布前评估，因此应建立参考数据集，而不是仅在生产日志中观察。
2. 参考数据应来自验证过的临床指南，并覆盖相似问题的不同表达。
3. Bedrock Evaluations 能执行基于评估任务的质量判断，识别事实错误与语义差异。
4. B 使用参考数据集、输出比较和 Bedrock 评估，形成可重复的幻觉检测流程。

#### 逐项排除

- **A.** 不合适；关键词和已知答案比较无法可靠识别语义等价、上下文差异和医学事实错误。
- **B.** 正确；验证数据集加 Bedrock Evaluations 覆盖参考对照和语义质量评估。
- **C.** 不合适；Guardrails 的自定义规则不是通用事实核验，Feature Store 也不是评估数据集的核心。
- **D.** 不完整；自动评估和告警方向合理，但没有明确将结果与验证过的医疗信息进行参考比较。

#### 解题方法

遇到 hallucination、validated reference、similar questions 或 semantic inconsistency，先找评估数据集和语义指标，再看是否能自动运行。日志、告警和安全过滤器可以辅助监控，但不能替代带参考答案的模型评估。把“发现异常”与“证明事实不一致”区分开。

### English

#### Exam focus and background

This question tests how to detect hallucinations by comparing generated answers with verified medical information. The requirement also asks for semantic inconsistency when users express similar questions in different ways. That is more than a keyword alarm or a string comparison against one known answer. It requires a reference dataset, repeatable evaluation jobs, and a semantic quality assessment. Amazon Bedrock Evaluations can use a custom prompt dataset and evaluation metrics to compare generated responses for factuality, relevance, completeness, and consistency.

Guardrails primarily enforce content safety policies such as sensitive-information, topic, or word filtering; they are not a general medical fact-checking engine. CloudWatch Logs Insights can find log patterns but cannot determine whether a treatment recommendation is semantically consistent with a clinical guideline. A SageMaker Feature Store is also not the natural evaluation repository for verified clinical answers. B combines a validated reference set, response comparison, and Bedrock evaluation, matching both major requirements.

#### Analogy

Imagine a clinical review board that publishes a set of approved case answers. The model receives the same clinical question in several different phrasings, and doctors compare the generated conclusions with the approved answers. Searching logs for a few keywords cannot prove that a medical recommendation preserved the intended meaning.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock Evaluations | Clinical quality review board | Evaluates generated answers against a reference dataset |
| Custom prompt dataset | Approved case workbook | Provides validated answers and alternate phrasings |
| Amazon Bedrock Guardrails | Safety gate | Enforces content policy but is not a general factuality evaluator |
| CloudWatch Logs Insights | Log investigator | Finds patterns, but does not perform semantic medical comparison |
| Amazon SageMaker Feature Store | Feature warehouse | Is not the central reference set required by this evaluation |

#### Correct answer and reasoning

1. This is a pre-release evaluation requirement, so a reference dataset is needed instead of only production log observation.
2. The dataset should contain validated clinical information and equivalent questions with different wording.
3. Bedrock Evaluations provides a repeatable evaluation task for factual and semantic quality.
4. B combines reference answers, response comparison, and Bedrock evaluation, producing a suitable hallucination-detection workflow.

#### Option-by-option elimination

- **A.** Incorrect. Keywords and exact comparisons cannot reliably detect semantic equivalence or clinical factual errors.
- **B.** Correct. A validated reference set plus Bedrock evaluation directly supports factual and semantic comparison.
- **C.** Incorrect. Guardrail rules are safety controls, not a complete factuality evaluator, and Feature Store is an unrelated primary repository.
- **D.** Incomplete. It mentions an evaluation job and alerts but does not clearly compare outputs with verified medical information.

#### Exam strategy

For hallucination, validated references, alternate phrasings, and semantic inconsistency, look for an evaluation dataset and semantic metrics. Logs and alerts can support monitoring, while Guardrails can support safety, but neither replaces a reference-based model evaluation. Distinguish detecting an operational anomaly from proving that a generated fact conflicts with an approved source.

---

## Question 120

### 中文

#### 考点背景

本题描述的是一个持续运行的生成式 AI 回归测试系统：超过 500 个黄金样例、语义相似度评分、定期调度、未来扩展到 2,000 个样例、指标可视化以及质量下降告警。关键不是选择一个能偶尔运行测试的组件，而是组合出一个可自动扩展、可定时执行并把结果发布成监控指标的流水线。Lambda 负责按测试样例调用 FM 并比较预期输出，EventBridge Scheduler/规则负责定期触发，CloudWatch 负责保存自定义指标、图表和告警。

SageMaker Model Monitor 的核心是模型输入或输出的监控和漂移检测，并不天然等于黄金样例语义回归测试。Step Functions 可以编排复杂流程，但 C 没有明确的语义相似度评分、计划规则和告警闭环。CloudWatch Synthetics Canary 更适合少量端到端可用性探测，不适合承载持续增长的 2,000 个黄金样例。B 的服务边界最清晰，也能通过无状态 Lambda 横向扩展测试量。

#### 场景比喻

像每天晚上自动把 500 多道标准题交给机器人批改，把得分写到成绩看板；以后增加到 2,000 道只需增加工位。Synthetics Canary 更像每隔一段时间做一次“网站能不能打开”的抽查，不是完整题库回归测试。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | 自动批改员 | 调用 FM、计算语义相似度并逐样例输出结果 |
| Amazon EventBridge | 定时器 | 按计划触发回归测试任务 |
| Amazon CloudWatch | 成绩看板与报警器 | 保存自定义质量指标、展示趋势和触发告警 |
| Amazon SageMaker Model Monitor | 漂移巡检员 | 监控数据/模型漂移，不等同黄金样例回归测试 |
| CloudWatch Synthetics | 可用性探针 | 适合少量路径探测，不适合大规模语义测试集 |

#### 正确答案与推理

1. 每个黄金样例需要一次 FM 调用和一次预期输出比较，Lambda 可以无状态并行扩展。
2. EventBridge 计划规则把测试从人工动作变成周期任务。
3. 将相似度结果发布到 CloudWatch，就能生成可视化指标和质量下降告警。
4. 这条链路不依赖固定题目数量，增加测试样例主要是数据量和并行度变化，不需改架构。

#### 逐项排除

- **A.** 不合适；Model Monitor 的漂移监控不是黄金响应的语义回归验证。
- **B.** 正确；Lambda、EventBridge 和 CloudWatch 分别覆盖测试执行、调度、指标与告警。
- **C.** 不完整；Step Functions 能编排，但选项没有明确语义相似度评分和定期规则闭环。
- **D.** 不合适；Synthetics Canary 偏向 API 可用性和简单成功率，不适合大规模黄金样例比较。

#### 解题方法

看到 golden examples、scheduled tests、semantic similarity、metrics 和 alarms，把问题拆成执行、调度、指标三层。不要把漂移监控等同于回归测试，也不要把单条合成探针等同于批量质量评估。规模增长若只增加无状态测试任务，优先考虑 Lambda 弹性并行。

### English

#### Exam focus and background

This is a continuously operated generative-AI regression test system. It has more than 500 golden examples, semantic-similarity scoring, scheduled executions, growth to more than 2,000 cases, visualized quality metrics, and alarms for degradation. The solution needs an execution layer, a scheduler, and an observability layer. Lambda can invoke the FM for each case and compare the generated response with the expected response. EventBridge scheduled rules can trigger the test run, and CloudWatch can store custom metrics, show trends, and alarm on quality thresholds.

SageMaker Model Monitor focuses on monitoring data or model behavior and detecting drift; it is not automatically a golden-response semantic regression system. Step Functions could orchestrate a more complex workflow, but option C does not explicitly provide semantic scoring, scheduled rules, or an alarm loop. CloudWatch Synthetics is better for a small end-to-end availability probe than for a growing 2,000-case semantic test corpus. B has the clearest service boundaries and scales the stateless test workers.

#### Analogy

Imagine an automated grader that checks 500 standard answers every night, writes the scores to a dashboard, and adds more grading stations when the workbook grows to 2,000 answers. A synthetic canary is more like periodically checking whether one website journey opens successfully; it is not a full semantic regression suite.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| AWS Lambda | Automated grader | Invokes the FM, computes similarity, and processes test cases |
| Amazon EventBridge | Scheduled clock | Starts the regression run on a recurring schedule |
| Amazon CloudWatch | Scoreboard and alarm | Stores custom quality metrics, visualizes trends, and alerts |
| Amazon SageMaker Model Monitor | Drift inspector | Monitors drift, not the required golden-answer regression directly |
| CloudWatch Synthetics | Availability probe | Tests a small path, not thousands of semantic cases |

#### Correct answer and reasoning

1. Each golden example needs an FM invocation and an expected-output comparison; stateless Lambda workers can scale horizontally.
2. EventBridge scheduled rules turn the test into an automated recurring process.
3. Publishing similarity results as CloudWatch custom metrics supplies dashboards and degradation alarms.
4. Adding cases changes the workload size and concurrency, not the architecture, so B fits the future growth requirement.

#### Option-by-option elimination

- **A.** Incorrect. Model Monitor drift tracking is not the same as semantic regression against golden responses.
- **B.** Correct. Lambda, EventBridge, and CloudWatch cover execution, scheduling, metrics, visualization, and alarms.
- **C.** Incomplete. Step Functions can orchestrate steps, but the option does not clearly define semantic scoring and scheduled alarm delivery.
- **D.** Incorrect. Synthetics is mainly an availability probe and is not a scalable golden-example comparison engine.

#### Exam strategy

Map “golden examples,” “scheduled,” “semantic similarity,” “visualization,” and “alarm” to execution, scheduling, and observability. Do not equate drift monitoring with response regression testing, and do not equate a single synthetic probe with a large evaluation corpus. If growth only adds stateless test work, favor an elastic worker pattern.

---

## Question 121

### 中文

#### 考点背景

本题考查提示词版本治理、A/B 比较和零接触发布门禁。题干要求同时比较五种语言的准确率、相似查询的方差、投资风险与费用披露的合规结果，并把结果接入现有 CI/CD，形成可审计报告。这里需要的是提示版本作为受治理制品、自动化评估作为质量门禁，以及可以被流水线调用的编排能力。Amazon Bedrock Prompt Management 负责提示版本和变体管理；Amazon Bedrock Evaluations 负责使用参考数据与 LLM judge 评估准确率和一致性；Bedrock Flows 可以把跨语言测试串成可重复流程。

S3 Object Versioning 只能保存文件版本，不能天然提供提示变体评估、语义质量评分或合规门禁。人工开会和人工导出不符合 zero-touch，也无法稳定复现五语言结果。Comprehend 情感分析并不等于提示版本的准确率、方差和监管检查。C 的组合将版本、评估、跨语言工作流和 CI/CD 集成放在正确层次上。

#### 场景比喻

像软件公司为同一份客服话术建立版本库，每次提交都自动让五个语言团队做回归测试，只有所有成绩和合规检查通过，流水线才把新话术推到生产。人工把结果复制到文档里再开会，不能称为零接触发布。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Prompt Management | 话术版本库 | 管理提示版本、变体和比较对象 |
| Amazon Bedrock Evaluations | 自动评审委员会 | 评估准确率、一致性和语言维度结果 |
| LLM-based judge | 语义评分员 | 对开放式模型输出进行质量判断 |
| Amazon Bedrock Flows | 测试流水线 | 编排跨语言测试和条件步骤 |
| CI/CD pipeline | 发布闸门 | 只有评估和合规标准通过才推广版本 |

#### 正确答案与推理

1. 题目首先要求提示版本比较，Prompt Management 比单纯 S3 文件版本更贴合。
2. 准确率和相似查询方差需要自动评估，不能靠情感分析或人工投票。
3. 五种语言应作为同一评估流程的维度，生成可重复、可审计的结果。
4. 将 Flow 接入 CI/CD 后，评估不通过即可阻止推广，C 满足 zero-touch 和长期成本效益。

#### 逐项排除

- **A.** 不合适；Canvas 可视化和人工会议不能形成自动发布门禁或稳定审计链路。
- **B.** 不完整；S3、Lambda 和 QuickSight 需要自建评分、比较和合规控制，运维量大。
- **C.** 正确；Prompt Management、Evaluations、Flows 和 CI/CD 覆盖全流程。
- **D.** 不合适；众包投票和情感分析无法证明准确率、方差或监管披露合规。

#### 解题方法

看到 prompt version、A/B test、all languages、variance、automated evaluation 和 CI/CD gate，优先组合 Prompt Management、Bedrock Evaluations、Flows。把可视化看成结果消费端，把评估和门禁看成发布控制面；不要被“能存文件”或“能分析情感”误导。

### English

#### Exam focus and background

This question tests prompt version governance, A/B comparison, and an automated promotion gate. The workflow must compare accuracy across five languages, measure variance across similar queries, enforce compliance checks for investment-risk and fee disclosures, run inside an existing CI/CD pipeline, produce auditable reports, and remain cost effective. Prompt Management supplies governed prompt versions and variants. Bedrock Evaluations supplies repeatable evaluation with reference data and LLM-based judges. Bedrock Flows can compose the multilingual test path into a reusable workflow that the delivery pipeline can invoke.

S3 Object Versioning only stores file revisions; it does not inherently provide prompt-variant evaluation, semantic scoring, or a compliance gate. Meetings and manual exports are not zero-touch and are difficult to reproduce consistently across five languages. Comprehend sentiment analysis is not a measure of accuracy, variance, or regulatory disclosure compliance. C places each responsibility in the appropriate managed control plane.

#### Analogy

Imagine a software company that keeps customer-service scripts in a governed version library. Every submitted version is automatically tested by five language teams, and the release pipeline promotes it only when all quality and compliance checks pass. Copying results into a document and scheduling a meeting is not a zero-touch promotion workflow.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock Prompt Management | Script version library | Manages prompt versions, variants, and comparison candidates |
| Amazon Bedrock Evaluations | Automated review board | Evaluates accuracy and consistency across the test dimensions |
| LLM-based judge | Semantic grader | Judges open-ended generated responses against evaluation criteria |
| Amazon Bedrock Flows | Test pipeline | Composes multilingual evaluation steps into a repeatable flow |
| CI/CD pipeline | Promotion gate | Promotes a prompt only after evaluation and compliance pass |

#### Correct answer and reasoning

1. The requirement explicitly asks for prompt version comparison, so Prompt Management fits better than generic S3 versioning.
2. Accuracy and variance across similar queries require automated evaluation, not sentiment scoring or meetings.
3. The five languages can be represented as dimensions of one repeatable and auditable evaluation flow.
4. Integrating the flow into CI/CD allows failed quality or compliance thresholds to block promotion. C satisfies zero-touch operation and controlled cost.

#### Option-by-option elimination

- **A.** Incorrect. Visual comparison and weekly meetings do not create an automated promotion gate or repeatable audit trail.
- **B.** Incomplete. S3, custom Lambda scoring, DynamoDB, and QuickSight require the team to build and operate the comparison and compliance controls.
- **C.** Correct. Prompt Management, Evaluations, Flows, and CI/CD cover versioning, scoring, multilingual orchestration, and gating.
- **D.** Incorrect. Crowdsourcing and sentiment analysis cannot prove accuracy, variance, or regulatory disclosure compliance.

#### Exam strategy

For prompt versioning, A/B testing, multilingual quality, variance, automated evaluation, and a CI/CD gate, look for Prompt Management plus Bedrock Evaluations and a reusable Flow. Treat visualization as an output consumer, while evaluation and gating are release controls. Do not select a generic file store or sentiment service merely because it can hold or inspect data.

---

## Question 122

### 中文

#### 考点背景

本题是一个同时包含 Agent 编排、实时反馈、持续评估、A/B 测试和合规审计保留的综合架构。最重要的硬约束是所有用户反馈、标记事件和审计日志必须进入一次写入、防篡改、具有篡改证据的存储库。Amazon S3 Object Lock 提供 WORM 保留边界，适合监管审计留存；CloudTrail 负责记录 AWS API 活动并把日志交付到 S3；DynamoDB 可以承载结构化用户反馈；Model Monitor 可继续观察漂移和异常。AgentCore、Lambda 分别处理工作流编排、输入清理、事实检查和 A/B 流量拆分。

本题的关键是不要把“能加密”当成“不可变”。DynamoDB、RDS 和普通 CloudWatch Logs 都不能单独提供题设要求的 WORM 审计库。Macie 能发现敏感内容，不会替代审计保存。A 虽然启用了 Object Lock，却没有把用户反馈和标记事件统一送入不可变库；B 缺少实时评估、A/B 测试和多步骤编排。C 的职责覆盖最完整。

#### 场景比喻

像金融机构有一个受监管的档案室：业务人员可以把审计材料投进去，但任何人都不能在规定期限内撕掉或改写。旁边还有一个普通工作台记录用户评分，最终审计记录由专门的审计系统投递到档案室。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock AgentCore | 推荐流程总调度 | 编排检索、验证和生成等多步骤工作流 |
| Amazon Kendra | 精选资料索引 | 提供可信内部文档检索 |
| AWS Lambda | 校验与分流工位 | 清理输入、检查事实一致性、拆分 A/B 流量 |
| AWS CloudTrail | 审计记录员 | 记录 AWS API 活动并交付日志 |
| Amazon S3 Object Lock | 防篡改档案室 | 以 WORM 方式保存审计日志和标记事件 |
| Amazon SageMaker Model Monitor | 长期巡检员 | 监控数据漂移和输出异常 |

#### 正确答案与推理

1. AgentCore 编排检索、输入清理、事实检查和推荐生成，覆盖多步骤工作流。
2. DynamoDB 适合保存显式用户评分，CloudTrail 负责 API 审计，二者职责不同。
3. CloudTrail 日志及相关标记事件最终进入启用 Object Lock 的 S3，满足一次写入和监管留存。
4. Model Monitor 提供持续的漂移/异常观测，Lambda 可实现输入清理和 A/B 分流，C 最完整。

#### 逐项排除

- **A.** 不完整；虽然有 Object Lock，但用户反馈和标记查询仍留在 DynamoDB，未统一进入不可变审计库。
- **B.** 不完整；Kendra 和 S3 Object Lock 解决检索与保留，却没有实时评估、A/B 测试和 Agent 编排。
- **C.** 正确；同时覆盖编排、检索、评估、反馈、CloudTrail 审计、Object Lock 和持续监控。
- **D.** 不合适；季度人工审核不满足实时检测，RDS 也不是题设要求的 WORM 审计仓库。

#### 解题方法

先圈出 immutable、write-once、tamper-evident 和 regulated retention，这些词优先指向 S3 Object Lock。再分别匹配编排、反馈、审计、监控和 A/B 测试的职责。加密只解决机密性，不能自动提供不可变性；“能存日志”也不等于“满足合规保留”。

### English

#### Exam focus and background

This is a composite architecture involving agent orchestration, explicit user feedback, continuous evaluation, A/B testing, and regulated audit retention. The strongest constraint is that feedback, flagged events, and audit logs must end up in write-once, tamper-evident storage. Amazon S3 Object Lock provides a WORM retention boundary suitable for regulated records. AWS CloudTrail records AWS API activity and can deliver logs to S3. DynamoDB can store structured user ratings, while SageMaker Model Monitor can observe drift and output anomalies over time. AgentCore and Lambda handle orchestration, input sanitization, fact checks, and traffic splitting.

Do not confuse encryption with immutability. DynamoDB, RDS, and ordinary CloudWatch Logs do not independently provide the required WORM audit repository. Macie can discover sensitive data but does not replace audit retention. A enables Object Lock but leaves feedback and flagged queries outside the immutable repository. B lacks the full evaluation, A/B, and orchestration path. C covers the combined responsibilities most completely.

#### Analogy

Imagine a regulated financial archive. Staff may deposit audit material, but nobody can tear out or rewrite the record during the retention period. A normal desk can collect user ratings, while a dedicated audit system writes the authoritative record into the locked archive.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock AgentCore | Recommendation dispatcher | Orchestrates retrieval, validation, and generation steps |
| Amazon Kendra | Curated document index | Retrieves trusted internal documents |
| AWS Lambda | Validation and routing stations | Sanitizes input, checks facts, and splits A/B traffic |
| AWS CloudTrail | Audit clerk | Records AWS API activity and delivers audit logs |
| Amazon S3 Object Lock | Tamper-resistant archive | Provides WORM retention for audit material |
| Amazon SageMaker Model Monitor | Long-term inspector | Tracks drift and output anomalies continuously |

#### Correct answer and reasoning

1. AgentCore orchestrates retrieval, input cleanup, fact checking, and recommendation generation.
2. DynamoDB is appropriate for explicit user ratings, while CloudTrail provides AWS API audit records.
3. Audit logs and flagged-event records are delivered into an Object Lock-enabled S3 repository for WORM retention.
4. Model Monitor supplies ongoing drift and anomaly observation, and Lambda implements sanitization and A/B routing. C covers the complete architecture.

#### Option-by-option elimination

- **A.** Incomplete. Object Lock is present, but feedback and flagged queries remain in DynamoDB rather than being covered by the immutable repository.
- **B.** Incomplete. Kendra and Object Lock address retrieval and retention, but real-time evaluation, A/B testing, and multi-step orchestration are missing.
- **C.** Correct. It combines orchestration, retrieval, evaluation, feedback, CloudTrail, Object Lock, and continuous monitoring.
- **D.** Incorrect. Quarterly manual review does not provide real-time detection, and encrypted RDS is not the required WORM audit repository.

#### Exam strategy

Start with “immutable,” “write-once,” “tamper-evident,” and “regulated retention”; these point to an Object Lock-controlled S3 archive. Then assign separate services to orchestration, feedback, audit, monitoring, and A/B routing. Encryption protects confidentiality, but it does not create immutability. Storing logs is not the same as preserving them under a compliant retention boundary.

---

## Question 123

### 中文

#### 考点背景

本题考查大规模向量相似性检索的服务选择和容量设计。系统需要处理数百万个向量，每小时更新，还要在购物高峰承受突发查询。Amazon OpenSearch Service 的核心能力就是索引、近似最近邻检索和横向扩展；多节点架构可以把存储、计算和查询压力分散，向量索引设置可以针对内存和查询路径调整，断路器和队列帮助在高峰时保护节点。

DynamoDB 是键值/文档数据库，不提供题干所需的原生高性能向量索引模型；Streams 加 Lambda 只解决变更传播，不能把 DynamoDB 变成向量搜索引擎。Bedrock Knowledge Bases 可以提供托管式 RAG 摄取，但题目强调的是高峰向量查询性能和专门的索引容量控制，选项 C 提到的 custom vector partitioning 并非通用的 Knowledge Bases 配置。Neptune 的强项是图关系、路径和实体连接，产品嵌入相似度目录不是它的核心场景。A 最符合重量级向量搜索引擎的定位。

#### 场景比喻

像大型电商把数百万件商品放进专门的“按相似外观找商品”的索引中心。高峰期增加索引节点并保护内存，比把向量塞进普通通讯录，或把商品关系图数据库硬改成向量搜索更合适。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon OpenSearch Service | 向量搜索中心 | 建立索引并执行大规模相似度检索 |
| Vector index settings | 专用目录结构 | 针对向量查询调整索引和内存行为 |
| Memory circuit breaker | 过载保护器 | 高峰时阻止查询把节点内存耗尽 |
| Amazon DynamoDB | 键值仓库 | 适合业务记录，不是原生向量检索主引擎 |
| Amazon Neptune | 关系图谱库 | 适合路径和实体关系，不是本题的向量目录核心 |

#### 正确答案与推理

1. 题干的主问题是数百万向量的相似度查询性能，而不是关系遍历或普通键值读写。
2. OpenSearch 提供向量索引和查询能力，并能通过多节点扩展容量与并发。
3. 向量专用索引设置、内存断路器和操作队列针对高峰保护和调优。
4. A 直接解决查询引擎、容量和突发流量问题，且比自建向量索引代码更贴合托管服务要求。

#### 逐项排除

- **A.** 正确；OpenSearch 的多节点向量搜索、内存保护和容量设计匹配高峰性能要求。
- **B.** 不合适；DynamoDB Streams 可以维护变更，但不是高性能向量相似性搜索引擎。
- **C.** 不合适；Knowledge Bases 偏向托管 RAG 摄取与检索，选项中的自定义向量分区不是解决高峰索引性能的标准路径。
- **D.** 不合适；Neptune 面向图数据的关系和路径查询，商品嵌入目录不需要图遍历。

#### 解题方法

看到 millions of embeddings、similarity search、hourly updates、peak latency 和 multi-node，优先考虑专业向量检索引擎。看到知识库、RAG、文档摄取才优先考虑 Knowledge Bases；看到谁与谁相连、路径和实体关系才考虑 Neptune。不要仅因为服务可以保存数据，就把它当成向量查询引擎。

### English

#### Exam focus and background

This question tests service selection and capacity design for large-scale vector similarity search. The system processes millions of embeddings, updates them hourly, adds new products, and experiences bursts during shopping events. Amazon OpenSearch Service is designed for indexed search, approximate-nearest-neighbor style retrieval, and horizontal scaling. A multi-node deployment distributes query and storage pressure. Vector-specific index settings, memory circuit breakers, and dedicated queues protect and tune the search path during surges.

DynamoDB is a key-value and document database; Streams plus Lambda can propagate changes but do not turn it into a high-performance vector search engine. Bedrock Knowledge Bases provides managed RAG ingestion and retrieval, but the requirement emphasizes peak vector-query performance and index capacity controls; the proposed custom vector partitioning is not the standard answer. Neptune is strongest for graph relationships, paths, and entities, not a product-embedding similarity catalog. A best matches the workload and the managed search requirement.

#### Analogy

Imagine an e-commerce company with a dedicated index center that finds products by visual or semantic similarity across millions of items. During a sale, it adds search capacity and protects memory. It would be a poor design to store all vectors in an ordinary address book or force a relationship graph database to act as the primary vector engine.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon OpenSearch Service | Vector search center | Builds indexes and executes large-scale similarity searches |
| Vector index settings | Specialized catalog design | Tunes the index and memory behavior for vector queries |
| Memory circuit breaker | Overload guard | Prevents burst queries from exhausting node memory |
| Amazon DynamoDB | Key-value store | Holds business records, but is not the primary vector engine |
| Amazon Neptune | Relationship graph | Fits paths and entities, not this embedding catalog |

#### Correct answer and reasoning

1. The central workload is similarity search over millions of vectors, not graph traversal or ordinary key-value access.
2. OpenSearch provides vector indexing and query capabilities and can scale across nodes.
3. Vector settings, memory protection, and operational queues address the peak-load behavior described.
4. A directly addresses the search engine, capacity, and surge requirements without inventing a custom vector system.

#### Option-by-option elimination

- **A.** Correct. Multi-node OpenSearch with vector tuning and memory protection matches the high-scale search workload.
- **B.** Incorrect. DynamoDB Streams can maintain changes, but DynamoDB is not the required high-performance similarity engine.
- **C.** Incorrect. Knowledge Bases is a managed RAG ingestion and retrieval path, and the proposed custom partitioning is not the right capacity solution here.
- **D.** Incorrect. Neptune is optimized for graph relationships and paths; product embeddings do not require graph traversal.

#### Exam strategy

For millions of embeddings, similarity search, hourly updates, peak latency, and multi-node capacity, favor a dedicated vector-search engine. Use Knowledge Bases when the clues emphasize managed RAG ingestion and document retrieval. Use Neptune when the clues emphasize connected entities, paths, and relationships. Data storage alone does not make a service a vector query engine.

---

## Question 124

### 中文

#### 考点背景

本题同时考查 PHI 的输出掩码、提示-补全审计、S3 留存和合规策略。输入中包含原始患者数据，但存储记录必须去除 PHI；模型也可能在响应中重新生成患者身份信息。因此，控制点必须包含模型输出侧的敏感信息过滤，而不能只依赖输入扫描。Amazon Bedrock Guardrails 可以配置敏感信息过滤器并执行 `MASK`，S3 负责保存经过处理的审计记录，并根据医疗政策设置保留期限。

选项 C 的 Object Lock Compliance Mode 与“处理后重新删除患者信息”存在根本冲突：合规模式下对象在保留期内不能被应用覆盖或删除。选项 A 把记录先存下再扫描，意味着 PHI 已经进入存储，且“到期删除”不是“存储前脱敏”。Prompt Management 和 Flows 本身不是 PHI 检测器；B 虽然措辞把检测职责写在 Prompt/Flows 附近，但答案的核心是 Bedrock Guardrails MASK 加 S3 保留控制。

#### 场景比喻

像医院先让自动隐私审核员把病历中的姓名和病案号遮掉，再把审计副本放进有保留期限的档案柜。把未脱敏档案先锁进柜子，之后再说要删掉其中一部分，会同时违反脱敏和不可变留存逻辑。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | PHI 遮挡员 | 在模型处理链路中识别并掩码敏感信息 |
| Amazon S3 | 审计档案柜 | 保存已脱敏提示-补全记录并执行保留设置 |
| S3 Object Lock | 不可变封存柜 | 适用于需要不可修改的记录，但不适合后续脱敏删除 |
| Amazon Bedrock Prompt Management | 提示版本库 | 管理提示，不负责 PHI 识别 |
| Amazon Bedrock Flows | 流程编排器 | 可编排步骤，不是专门的 PHI 过滤器 |

#### 正确答案与推理

1. PHI 可能出现在输出，因此必须在响应侧启用敏感信息过滤器。
2. `MASK` 能保留审计记录结构，同时从记录中隐藏具体患者身份信息。
3. 将处理后的记录写入 S3，并配置符合政策的保留设置，满足审计和生命周期要求。
4. B 将输出治理与审计存储结合，避免把 PHI 先写入再删除。

#### 逐项排除

- **A.** 不合适；先写入未脱敏记录再扫描，无法满足存储记录必须去除 PHI。
- **B.** 正确；Guardrails MASK 负责敏感信息处理，S3 负责安全审计记录和保留设置。
- **C.** 不合适；Object Lock Compliance Mode 与后续删除或改写 PHI 冲突。
- **D.** 不合适；知识库不是 PHI 审计日志仓库，Comprehend Medical 加 Lambda 也增加自定义保留维护。

#### 解题方法

先确定脱敏发生的时间点：题目要求从存储记录中删除 PHI，优先选择存储前/输出侧的托管过滤。再分别判断加密、Object Lock 和保留策略的职责。不可变存储适合最终审计证据，但不适合先存敏感数据、之后再修改的流程。

### English

#### Exam focus and background

This question combines PHI masking, prompt-completion auditing, S3 retention, and healthcare policy. The original patient data is sensitive, but the stored audit record must not contain PHI. The model can also regenerate patient identifiers that were not present in the original input, so input scanning alone is insufficient. Bedrock Guardrails can use sensitive-information filters with a `MASK` action, while S3 stores the sanitized audit records and applies the required retention configuration.

Option C has a direct contradiction: S3 Object Lock in compliance mode prevents the application from deleting or overwriting an object during its retention period, yet the option asks Lambda to redact the record afterward. Option A stores an unredacted record before scanning it, so PHI has already entered the audit store. Prompt Management and Flows manage prompts and orchestration; they are not PHI detectors. The core of B is the managed Guardrail mask plus controlled S3 retention.

#### Analogy

Imagine a hospital sends every report through a privacy clerk who covers names and record numbers before placing the audit copy in a retention-controlled archive. Locking an unredacted file first and planning to erase parts of it later violates both the redaction timing and the immutability boundary.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | PHI masking clerk | Detects and masks sensitive information in the model path |
| Amazon S3 | Audit archive | Stores sanitized prompt-completion records under retention settings |
| S3 Object Lock | Immutable vault | Fits records that must not change, but conflicts with later redaction |
| Amazon Bedrock Prompt Management | Prompt library | Versions prompts but does not detect PHI |
| Amazon Bedrock Flows | Workflow conductor | Orchestrates steps but is not itself the PHI filter |

#### Correct answer and reasoning

1. PHI can be regenerated in the output, so sensitive-information filtering must cover the response boundary.
2. The `MASK` action preserves the audit record structure while hiding the patient identifier.
3. The sanitized record can then be stored in S3 with a healthcare-appropriate retention configuration.
4. B combines output governance and audit storage without first writing an unredacted record and trying to remove it later.

#### Option-by-option elimination

- **A.** Incorrect. It writes an unredacted record before scanning, violating the requirement for PHI-free stored records.
- **B.** Correct. Guardrails masks sensitive information and S3 retains the sanitized audit records.
- **C.** Incorrect. Compliance-mode Object Lock conflicts with the later Lambda deletion or rewrite of PHI.
- **D.** Incorrect. A knowledge base is not the audit repository, and a Comprehend plus Lambda retention pipeline adds custom operations.

#### Exam strategy

First identify when redaction must happen. If the stored record must not contain PHI and the model may generate new identifiers, choose a managed output-side filter. Then separate encryption, Object Lock, and retention responsibilities. Immutable storage is appropriate for final audit evidence, but not for a workflow that writes sensitive data and plans to edit it later.

---

## Question 125

### 中文

#### 考点背景

本题是文档预处理和生成式 AI 输入质量的组合题。公司既要删除 PII，又要把格式不一致的客户文件标准化，并且要求最低运维开销。最佳边界是：在调用模型前由轻量的预处理 Lambda 统一结构，在推理路径上由 Bedrock Guardrails 过滤 PII，再通过标准化示例和少样本提示帮助模型稳定处理不同文档类型。这样把数据质量、隐私和提示策略分别放在合适的位置。

Macie 主要发现 S3 中的敏感数据，并不等于可靠的在线脱敏管道；Textract 适合从扫描文档和复杂版面抽取文本，但题干并未明确需要 OCR。Comprehend 的自定义实体识别能帮助识别 PII，却要额外维护实体模型和编排流程，且没有解决模型推理侧的隐私输出。情感分析与投资文件格式标准化没有直接关系。C 用 Guardrails、Lambda 和少样本示例覆盖要求，同时避免把每个步骤都做成大型自定义平台。

#### 场景比喻

像投资顾问办公室先让助理把所有客户表格整理成统一模板，再由隐私审核员遮住不应展示的身份信息，最后给模型一组标准范例。把情感分析或整套 OCR 工厂搬进来，并不能自动解决格式和隐私问题。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | 文档预处理台 | 在模型调用前标准化格式和元数据 |
| Amazon Bedrock Guardrails | 推理隐私闸门 | 在推理期间过滤 PII |
| Few-shot prompting | 标准范例册 | 给模型稳定的输入/输出结构示例 |
| Amazon Macie | S3 隐私巡检员 | 发现静态对象中的敏感数据，不是完整在线脱敏器 |
| Amazon Textract | OCR 录入员 | 适合图像/扫描版面抽取，题干不一定需要 |

#### 正确答案与推理

1. 先用 Lambda 统一文档格式，让 FM 接收到结构稳定的输入。
2. 用 Guardrails 处理推理过程中的 PII，覆盖模型输出侧风险。
3. 用少样本提示和标准化示例减少文档类型差异造成的行为波动。
4. 这条链路只在必要位置使用自定义代码，符合最低运维开销，C 最匹配。

#### 逐项排除

- **A.** 不合适；Macie 发现 PII，Textract 处理版面，但组合过重且没有清晰的推理侧控制。
- **B.** 不合适；Comprehend 自定义实体和 Step Functions 可以实现，但运维与建模成本更高，Guardrails 也缺失。
- **C.** 正确；预处理标准化、Guardrails 脱敏和少样本示例共同解决输入质量与隐私。
- **D.** 不合适；Textract 和情感分析并不能直接处理题干中的格式一致性和 PII 输出风险。

#### 解题方法

把要求拆成“输入标准化、隐私治理、模型稳定性、运维量”四个维度。格式不一致看预处理，PII 看 Guardrails，跨文档稳定性看标准化示例；只有明确扫描图片或表格版面时才引入 Textract。不要选择功能很多但边界不贴题的服务堆叠。

### English

#### Exam focus and background

This question combines document preprocessing with generative-AI input quality. The company must redact PII, standardize inconsistent document formats, and keep operational overhead low. The clean boundary is to use a small preprocessing Lambda to normalize structure before inference, use Bedrock Guardrails to filter PII on the inference path, and use few-shot prompts with standardized examples to make the model behave consistently across document types. Each concern is handled where it belongs.

Macie discovers sensitive data in S3; it is not automatically a complete online redaction pipeline. Textract is valuable for scanned images and complex layouts, but the question does not establish an OCR requirement. Comprehend custom entities can detect PII, but it adds model and workflow ownership and does not protect generated output by itself. Sentiment analysis is unrelated to formatting normalization. C addresses input quality and privacy with a relatively small amount of custom code.

#### Analogy

Imagine an investment office where an assistant converts every client form into one standard template, a privacy clerk masks identifiers, and the model receives a small book of approved examples. Adding a sentiment-analysis desk or a full OCR factory does not automatically solve inconsistent structure and PII output risk.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| AWS Lambda | Document preparation desk | Normalizes format and metadata before inference |
| Amazon Bedrock Guardrails | Inference privacy gate | Filters PII during the model interaction |
| Few-shot prompting | Standard example book | Shows the model a stable structure across document types |
| Amazon Macie | S3 privacy inspector | Finds sensitive objects but is not the full online redactor |
| Amazon Textract | OCR clerk | Extracts scanned layouts when that requirement exists |

#### Correct answer and reasoning

1. Lambda standardizes document structure so the FM receives consistent inputs.
2. Guardrails filters PII on the inference path and covers the generated-output boundary.
3. Few-shot examples and standardized demonstrations reduce variation across document schemas.
4. The design uses custom code only where necessary and therefore best fits the least-operational-overhead requirement. C is the best match.

#### Option-by-option elimination

- **A.** Incorrect. Macie discovery and Textract extraction create a heavier chain and do not define a clear inference-side privacy control.
- **B.** Incorrect. Custom Comprehend entities and Step Functions can work, but they add operating and modeling work and omit Guardrails.
- **C.** Correct. Preprocessing normalization, Guardrails, and standardized few-shot examples cover the requirements together.
- **D.** Incorrect. Textract and sentiment analysis do not directly solve formatting consistency and PII output protection.

#### Exam strategy

Split the requirements into input normalization, privacy governance, model consistency, and operations. Use preprocessing for schema variation, Guardrails for PII, and standardized examples for stable generation. Add Textract only when the question clearly requires image or scanned-layout extraction. Avoid selecting a large stack merely because each service can perform an adjacent task.

---

## Question 126

### 中文

#### 考点背景

本题考查 AgentCore 跨账户访问、代表用户执行操作和身份上下文审计。应用不能只拥有一个“万能执行角色”，因为题目要求每个 DynamoDB 操作都能追溯到具体最终用户，并且账户数量还会从 25 个扩展到 40 多个。出站凭证提供程序通过 `AssumeRole` 动态获得临时凭证；每个业务账户中的目标角色只信任中央 AgentCore 执行角色，并只授予本账户表的最小权限；会话标签把最终用户身份带入跨账户会话，便于 CloudTrail 和审计链路关联。

AWS RAM 共享表并不会自动形成“代表某个用户”的临时会话，也不能替代各账户的资源权限边界。SCP 是组织级最大权限边界，不能单独授予实际访问权限；并且跨账户 DynamoDB 访问不能简单靠资源定义直接引用 ARN 来解决。为每个业务单元部署一个助手虽然可以隔离，但会把版本、容量、监控和运营对象扩大到几十份。A 的模式集中运行、分账户授权、用户上下文可审计，扩展成本最低。

#### 场景比喻

像总部的客服代表要进入不同分行的客户档案室：每次进入都领取只在短时间有效的临时门卡，门卡上标记服务的是哪位客户；各分行只信任总部指定的工作身份，不把所有档案室钥匙交给一个永久管理员。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock AgentCore | 中央业务助手 | 发起跨账户、代表用户的工具操作 |
| AWS STS AssumeRole | 临时门卡发行处 | 为每次目标账户访问生成短期凭证 |
| IAM target role | 分账户门禁 | 在业务账户授予 DynamoDB 最小权限并建立信任 |
| Session tags | 用户工牌标签 | 保留最终用户上下文，便于审计关联 |
| AWS CloudTrail | 跨账户审计员 | 记录角色会话和 API 调用轨迹 |
| AWS Organizations/SCP | 最大边界围栏 | 限制权限上限，但不是实际授权本身 |

#### 正确答案与推理

1. 中央 AgentCore 使用出站凭证提供程序，在需要访问业务账户时执行 `AssumeRole`。
2. 每个业务账户创建目标 IAM Role，信任中央执行角色，只允许访问本账户的 DynamoDB 表。
3. `AssumeRole` 附带 session tags，把最终用户标识带入临时会话和审计日志。
4. 只需维护中央助手和各账户一份标准目标角色模板，适合从 25 个账户扩展到 40 多个账户。

#### 逐项排除

- **A.** 正确；临时凭证、分账户最小权限角色和 session tags 同时满足安全边界、身份追踪和扩展性。
- **B.** 不合适；每个业务账户一个助手会造成重复部署和运营，扩展成本高。
- **C.** 不完整；RAM 共享资源不能替代代表用户的跨账户会话与细粒度审计。
- **D.** 不合适；SCP 不能授予权限，且不能仅靠跨账户 ARN 直接让中央角色访问成员账户表。

#### 解题方法

看到 cross-account、on behalf of user、temporary credentials、session context 和 many accounts，优先考虑 STS AssumeRole 加目标账户 IAM Role。记住 SCP 是 permission boundary，不是 grant；资源共享也不等于用户委派。最小权限和可扩展性通常来自“中央工作负载 + 各账户标准角色”，而不是复制整套应用。

### English

#### Exam focus and background

This question tests AgentCore cross-account access, acting on behalf of a user, and preservation of identity context for auditing. A single broad execution role would not meet the requirement because every DynamoDB operation must be attributable to a particular end user, while the organization will grow from 25 to more than 40 accounts. An outbound credential provider can call `AssumeRole` to obtain temporary credentials. Each business account can expose a target role that trusts the central AgentCore execution role and grants only local table permissions. Session tags carry the end-user context into the assumed session and audit records.

AWS RAM sharing does not automatically create a per-user delegated session and does not replace account-level resource policies. An SCP is an organization-wide maximum-permission boundary; it does not grant the actual access. Directly placing a member-account table ARN in a central resource definition is not sufficient for cross-account DynamoDB authorization. Deploying one assistant per business unit would multiply releases, monitoring, and operations. A central assistant with standardized target roles is the scalable design.

#### Analogy

Imagine a headquarters service representative entering customer archives in many branches. Each visit receives a short-lived badge labeled with the customer being served. Every branch trusts only the approved headquarters identity and grants access to its own archive; headquarters does not receive one permanent master key for every branch.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock AgentCore | Central assistant | Initiates cross-account tool actions on behalf of users |
| AWS STS AssumeRole | Temporary badge office | Issues short-lived credentials for a target account |
| IAM target role | Branch access gate | Establishes trust and grants least-privilege local table access |
| Session tags | User badge label | Preserves end-user context for audit correlation |
| AWS CloudTrail | Cross-account auditor | Records role sessions and API activity |
| AWS Organizations/SCP | Maximum boundary fence | Limits the ceiling but is not the actual grant |

#### Correct answer and reasoning

1. The central AgentCore application obtains credentials on demand through an outbound `AssumeRole` provider.
2. Each business account owns a target role that trusts the central execution role and can access only its local DynamoDB tables.
3. Session tags identify the end user in the assumed session and make the action traceable.
4. The central application and a standard target-role pattern can scale across more accounts without cloning the whole assistant. A satisfies security, audit, and operating requirements.

#### Option-by-option elimination

- **A.** Correct. Temporary credentials, account-local least-privilege roles, and session tags preserve both security and user attribution.
- **B.** Incorrect. One assistant per business unit creates duplicated deployments, capacity, monitoring, and operational ownership.
- **C.** Incomplete. RAM sharing does not provide the required delegated user session and fine-grained identity audit trail.
- **D.** Incorrect. SCPs do not grant permissions, and a central role cannot gain member-account table access merely by referencing an ARN.

#### Exam strategy

For cross-account access “on behalf of a user,” temporary credentials, session context, and many accounts, look for STS `AssumeRole` plus target-account IAM roles. Remember that an SCP is a permission ceiling, not a grant, and resource sharing is not equivalent to delegated identity. A central workload with standardized roles normally scales better than cloning the entire application.

---

## Question 127

### 中文

#### 考点背景

本题考查输入脱敏和输出安全的边界。现有方案使用 Amazon Comprehend Medical 清理输入，准确率只有 95%；问题却出现在模型输出中，而且输出中的患者姓名和病历号有一部分并不来自原始输入。这说明模型可能产生新的 PII，输入侧预处理不可能保证输出安全。Amazon Bedrock Guardrails 的敏感信息过滤器可以直接检查模型输出，并在发现 PII 时阻止或处理响应。它是托管的推理侧控制，避免为每个医疗实体维护额外的正则和后处理服务。

题目还要求低于 500 毫秒的实时响应、PII 处理决策审计和至少 99% 的输出检测准确率。选项 A 直接针对输出中已生成的 PII，是对现有 95% 输入检测的补强；Guardrails 的检测结果和调用日志可以纳入审计。B 仍然只在调用前增加输入扫描，不能解决模型新生成的患者身份信息；C 删除临床上下文会损害诊断质量，也不能保证模型不编造 PII；D 清理会话历史解决的是跨请求记忆，不是当前响应中的 PII。A 是唯一把控制点放在故障边界上的答案。

#### 场景比喻

像医院既在病历进入系统时检查一次，也在报告交给医生前再过一道最终隐私闸门。前一道检查漏掉或模型后来新写出的身份信息，必须由最后一道闸门拦住；清空上一位病人的文件，不等于检查当前报告。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 最终隐私闸门 | 检测并阻止模型输出中的 PII |
| Sensitive information filters | 医疗身份扫描器 | 识别姓名、病历号等敏感信息 |
| Amazon Comprehend Medical | 输入预检员 | 处理输入实体，但准确率不足以覆盖输出风险 |
| Amazon Bedrock | 诊断生成器 | 可能重新组合或生成输入中不存在的身份信息 |
| CloudWatch/审计日志 | 监管记录本 | 记录过滤、阻止和响应处理事件 |

#### 正确答案与推理

1. 先定位漏洞：Comprehend Medical 只处理输入，而错误身份信息是在输出中出现。
2. 选择能检查输出的托管控制，直接在 Bedrock response boundary 过滤 PII。
3. Guardrails 可以在发现敏感实体时阻止响应，避免把信息显示给临床医生。
4. 过滤事件可进入审计日志，且比额外自建后处理链路更符合实时和低运维要求。

#### 逐项排除

- **A.** 正确；Guardrails 敏感信息过滤器位于输出侧，直接拦截模型生成的 PII。
- **B.** 不合适；第二次输入扫描仍不能检测模型新生成的姓名和病历号。
- **C.** 不合适；删除临床上下文会降低诊断价值，也没有形成可靠的输出 PII 控制。
- **D.** 不合适；会话隔离防止历史串用，但不能阻止当前模型响应生成身份信息。

#### 解题方法

先问“PII 是在哪个边界出现的”。输入扫描失败后如果模型继续生成新的身份信息，答案必须包含输出侧 Guardrails 或等价的响应过滤。实时性和最低运维量进一步排除自定义正则、额外 Lambda 后处理和破坏上下文的方案。看到“block model outputs”“sensitive information filter”时，优先选 Bedrock Guardrails。

### English

#### Exam focus and background

This question tests the boundary between input sanitization and output safety. The current design uses Amazon Comprehend Medical to clean the input with only 95% accuracy, but the observed patient names and record numbers appear in model responses and are sometimes absent from the original case history. The model can therefore generate new PII. Input preprocessing cannot guarantee safe output. Amazon Bedrock Guardrails can apply sensitive-information filters directly to the model response and block or handle a response containing PII. This is a managed inference-side control and avoids maintaining a separate regular-expression and post-processing service for every medical identifier.

The solution also needs sub-500-ms behavior, auditable PII decisions, and at least 99% output protection. A addresses the actual failure boundary and complements the existing 95% input detection. B adds another input scan but still cannot detect identifiers newly generated by the model. C removes clinical context and can reduce diagnostic quality without guaranteeing privacy. D clears cross-request history, which addresses session leakage rather than PII generated in the current response. A is the only option that places the control where the defect occurs.

#### Analogy

Imagine a hospital that checks a case when it enters the system and then sends the final report through a last privacy gate before showing it to a clinician. If the first check misses an entity or the model writes a new identifier later, the final gate must stop it. Clearing the previous patient’s folder is not a check of the current report.

#### AWS service roles

| AWS service | Analogy | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Final privacy gate | Detects and blocks PII in generated model output |
| Sensitive information filters | Medical identity scanner | Identifies names, record numbers, and configured PII categories |
| Amazon Comprehend Medical | Input pre-check | Handles source entities but cannot cover generated-output risk alone |
| Amazon Bedrock | Diagnostic generator | May recombine or generate identifiers absent from the input |
| CloudWatch and audit logs | Regulatory record book | Records filtering, blocking, and response-processing events |

#### Correct answer and reasoning

1. The defect is on the response side: Comprehend Medical only handled the input and missed the later generation problem.
2. The control must inspect the Bedrock response boundary, not merely add another input scanner.
3. Guardrails sensitive-information filters can block a response containing a detected patient identifier.
4. The decision can be captured in audit logs, and the managed path is more suitable for real-time, low-operations behavior than a custom postprocessor. A is correct.

#### Option-by-option elimination

- **A.** Correct. A response-side Guardrail directly detects and blocks generated PII.
- **B.** Incorrect. A second input scan still cannot see a name or record number invented after inference begins.
- **C.** Incorrect. Removing clinical context harms diagnostic usefulness and is not a reliable output privacy control.
- **D.** Incorrect. Session isolation prevents history reuse across requests but does not stop PII generated in the current response.

#### Exam strategy

Always locate the boundary where the sensitive data appears. If input masking is incomplete and the model can generate new PII, the answer needs an output-side Guardrail or equivalent response filter. Real-time and low-maintenance clues rule out custom regular expressions, extra Lambda postprocessing, and destructive context removal. For “block model outputs” and “sensitive information filter,” prefer Bedrock Guardrails.

---
