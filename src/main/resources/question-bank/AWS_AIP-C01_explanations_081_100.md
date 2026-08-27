## Question 81

### 中文

#### 考点背景

这题考查**多账户环境中的组织级模型限制与统一 Guardrail 强制**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B、D。SCP 可以在组织层面限制可调用的模型，并要求调用时提供批准的 Guardrail Identifier；SCP 适用于各账户中的主体，适合集中执行这类护栏要求。使用 Block Filtering Policy 的 Guardrail 并通过 StackSets 部署到各账户，可以阻止特定主题和专有信息进入模型。A、C 把关键控制放在 Permissions Boundary 上；Boundary 只能限制权限上限，不能代替身份策略授予调用权限，也不如 SCP 适合统一约束所有账户。E 的 Mask Filtering Policy 会脱敏而不是阻止内容，不能满足“不得包含在 Prompt 中”的要求。

#### 场景比喻

Organizations 像集团总部：SCP 是所有分公司的硬性章程，任何账户都不能越界；StackSets 像总部统一下发的安全门，把同一套阻断型 Guardrail 安装到每个分公司。Permissions Boundary 只是员工权限的天花板，不是集团章程。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【多账户环境中的组织级模型限制与统一 Guardrail 强制】。
2. **再核对正确组合：** 正确答案是 **BD**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B、D。SCP 可以在组织层面限制可调用的模型，并要求调用时提供批准的 Guardrail Identifier；SCP 适用于各账户中的主体，适合集中执行这类护栏要求。使用 Block Filtering Policy 的 Guardrail 并通过 StackSets 部署到各账户，可以阻止特定主题和专有信息进入模型。A、C 把关键控制放在 Permissions Boundary 上；Boundary 只能限制权限上限，不能代替身份策略授予调用权限，也不如 SCP 适合统一约束所有账户。E 的 Mask Filtering Policy 会脱敏而不是阻止内容，不能满足“不得包含在 Prompt 中”的要求。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项把关键控制放在 Permissions Boundary 上。
- **B：** 正确。该项是答案 **BD** 的组成部分，直接落实【多账户环境中的组织级模型限制与统一 Guardrail 强制】的关键机制。
- **C：** 该项把关键控制放在 Permissions Boundary 上。
- **D：** 正确。该项是答案 **BD** 的组成部分，直接落实【多账户环境中的组织级模型限制与统一 Guardrail 强制】的关键机制。
- **E：** 该项的 Mask Filtering Policy 会脱敏而不是阻止内容，不能满足“不得包含在 Prompt 中”的要求。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【多账户环境中的组织级模型限制与统一 Guardrail 强制】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **organization-wide model restrictions and guardrail enforcement across accounts**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answers are B and D. An SCP can restrict which models may be invoked at the organization level and require an approved guardrail identifier on the invocation, making the control consistent across accounts. A guardrail with a block filtering policy, deployed to every account through StackSets, can stop specified topics and proprietary information from reaching the model. A and C place key controls in permissions boundaries; a boundary only limits the maximum permissions and does not grant invocation permissions, so it is not the right organization-wide enforcement mechanism. E masks content instead of blocking it and therefore does not satisfy the requirement that it must not be included in the prompt.

#### Analogy

AWS Organizations is corporate headquarters. SCPs are mandatory bylaws for every account, while StackSets installs the same blocking guardrail everywhere. A permissions boundary is merely an employee permission ceiling, not the headquarters-wide rulebook.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver organization-wide model restrictions and guardrail enforcement across accounts, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **BD**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answers are B and D. An SCP can restrict which models may be invoked at the organization level and require an approved guardrail identifier on the invocation, making the control consistent across accounts. A guardrail with a block filtering policy, deployed to every account through StackSets, can stop specified topics and proprietary information from reaching the model. A and C place key controls in permissions boundaries; a boundary only limits the maximum permissions and does not grant invocation permissions, so it is not the right organization-wide enforcement mechanism. E masks content instead of blocking it and therefore does not satisfy the requirement that it must not be included in the prompt.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option place key controls in permissions boundaries; a boundary only limits the maximum permissions and does not grant invocation permissions, so it is not the right organization-wide enforcement mechanism.
- **B:** Correct. This choice is part of answer **BD** and directly implements the decisive mechanism for organization-wide model restrictions and guardrail enforcement across accounts.
- **C:** This option place key controls in permissions boundaries; a boundary only limits the maximum permissions and does not grant invocation permissions, so it is not the right organization-wide enforcement mechanism.
- **D:** Correct. This choice is part of answer **BD** and directly implements the decisive mechanism for organization-wide model restrictions and guardrail enforcement across accounts.
- **E:** This option masks content instead of blocking it and therefore does not satisfy the requirement that it must not be included in the prompt.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is organization-wide model restrictions and guardrail enforcement across accounts; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---
## Question 82

### 中文

#### 考点背景

这题考查**生成式响应的流式交付、令牌边界与有界重试**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。REST API 加 Lambda 提供了一个集中控制点：Lambda 可以转发 Bedrock 的流式响应、执行 Token Limit，并在检测到 Timeout 或 Partial Response 时实施有界重试；API Gateway 和 Lambda 的超时设置负责限制单次请求和重试的边界。B 把 Token Limit 放在前端，既不可靠也不安全，轮询也不是真正的模型流式输出；C 需要自行运维 ECS、WebSocket 和推理服务器，开销更高；A 的 HTTP API 方案不是记录答案所要求的 REST API 组合。

#### 场景比喻

把模型响应看成直播信号：Lambda 是导播台，一边把片段转发给观众，一边控制最长节目时长；REST API 是统一入口。遇到断流只做有限次数补播，避免无限重试拖垮后台。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon API Gateway | 统一服务入口 | 暴露受控 API 并管理请求边界、认证和超时 |
| AWS Lambda | 按需执行的工位 | 以事件或请求驱动方式运行轻量集成与校验逻辑 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【生成式响应的流式交付、令牌边界与有界重试】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。REST API 加 Lambda 提供了一个集中控制点：Lambda 可以转发 Bedrock 的流式响应、执行 Token Limit，并在检测到 Timeout 或 Partial Response 时实施有界重试；API Gateway 和 Lambda 的超时设置负责限制单次请求和重试的边界。B 把 Token Limit 放在前端，既不可靠也不安全，轮询也不是真正的模型流式输出；C 需要自行运维 ECS、WebSocket 和推理服务器，开销更高；A 的 HTTP API 方案不是记录答案所要求的 REST API 组合。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 HTTP API 方案不是记录答案所要求的 REST API 组合。
- **B：** 该项把 Token Limit 放在前端，既不可靠也不安全，轮询也不是真正的模型流式输出。
- **C：** 该项需要自行运维 ECS、WebSocket 和推理服务器，开销更高。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【生成式响应的流式交付、令牌边界与有界重试】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【生成式响应的流式交付、令牌边界与有界重试】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **streaming delivery, token boundaries, and bounded retries for generated responses**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. The REST API and Lambda function provide one control point where the application can forward Bedrock streaming chunks, enforce a token limit, and perform bounded retries when a timeout or partial response is detected. Lambda and API Gateway timeout settings bound each attempt and the overall request. B makes the frontend enforce the token limit and uses polling rather than true model streaming. C requires operating an ECS service, WebSocket handling, and an inference server, which adds substantial overhead. A does not use the REST API combination specified by the recorded answer.

#### Analogy

Think of the model response as a live broadcast. Lambda is the control room that forwards chunks and enforces the maximum program length, while the REST API is the entrance. A partial broadcast receives only bounded retries, not an endless replay loop.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon API Gateway | Managed service entrance | Exposes controlled APIs and manages request, auth, and timeout boundaries |
| AWS Lambda | On-demand workbench | Runs event- or request-driven integration and validation logic |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver streaming delivery, token boundaries, and bounded retries for generated responses, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. The REST API and Lambda function provide one control point where the application can forward Bedrock streaming chunks, enforce a token limit, and perform bounded retries when a timeout or partial response is detected. Lambda and API Gateway timeout settings bound each attempt and the overall request. B makes the frontend enforce the token limit and uses polling rather than true model streaming. C requires operating an ECS service, WebSocket handling, and an inference server, which adds substantial overhead. A does not use the REST API combination specified by the recorded answer.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option does not use the REST API combination specified by the recorded answer.
- **B:** This option makes the frontend enforce the token limit and uses polling rather than true model streaming.
- **C:** This option requires operating an ECS service, WebSocket handling, and an inference server, which adds substantial overhead.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for streaming delivery, token boundaries, and bounded retries for generated responses.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is streaming delivery, token boundaries, and bounded retries for generated responses; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 83

### 中文

#### 考点背景

这题考查**按请求复杂度进行托管模型路由和成本优化**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。Bedrock Intelligent Prompt Routing 是托管的模型选择能力，可以根据请求特征把简单问题发送给成本较低的小模型，把复杂问题发送给更强的模型，因此无需自行部署分类器和路由服务。A 需要额外的分类模型、Lambda 和一次前置推理，增加延迟与维护量；C 使用单一模型，无法按复杂度优化成本；D 的关键词规则容易漏判语义，且多个 Endpoint 与 Provisioned Throughput 带来更多配置和运维工作。

#### 场景比喻

像医院分诊台：简单问诊送普通门诊，复杂病例送专家。Intelligent Prompt Routing 是托管分诊护士，不需要先雇另一位医生做分类，也不靠几个关键词猜病情。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Intelligent Prompt Routing | 智能分诊台 | 按请求复杂度在模型之间托管路由以平衡质量与成本 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【按请求复杂度进行托管模型路由和成本优化】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。Bedrock Intelligent Prompt Routing 是托管的模型选择能力，可以根据请求特征把简单问题发送给成本较低的小模型，把复杂问题发送给更强的模型，因此无需自行部署分类器和路由服务。A 需要额外的分类模型、Lambda 和一次前置推理，增加延迟与维护量；C 使用单一模型，无法按复杂度优化成本；D 的关键词规则容易漏判语义，且多个 Endpoint 与 Provisioned Throughput 带来更多配置和运维工作。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项需要额外的分类模型、Lambda 和一次前置推理，增加延迟与维护量。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【按请求复杂度进行托管模型路由和成本优化】的关键机制。
- **C：** 该项使用单一模型，无法按复杂度优化成本。
- **D：** 该项的关键词规则容易漏判语义，且多个 Endpoint 与 Provisioned Throughput 带来更多配置和运维工作。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【按请求复杂度进行托管模型路由和成本优化】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **managed model routing and cost optimization by request complexity**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. Bedrock Intelligent Prompt Routing is a managed model-selection capability that can route simple requests to a less expensive model and complex requests to a more capable model without building a separate classifier and routing service. A adds a classification inference, Lambda logic, and extra latency. C cannot optimize cost by complexity because it uses one model for every request. D relies on brittle keyword rules and adds multiple endpoints and provisioned-throughput operations.

#### Analogy

It works like hospital triage: simple cases go to general practice and complex cases to a specialist. Intelligent Prompt Routing is the managed triage nurse, avoiding a separate classifier or brittle keyword rules.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Intelligent Prompt Routing | Managed triage desk | Routes by request complexity to balance model quality and cost |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver managed model routing and cost optimization by request complexity, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. Bedrock Intelligent Prompt Routing is a managed model-selection capability that can route simple requests to a less expensive model and complex requests to a more capable model without building a separate classifier and routing service. A adds a classification inference, Lambda logic, and extra latency. C cannot optimize cost by complexity because it uses one model for every request. D relies on brittle keyword rules and adds multiple endpoints and provisioned-throughput operations.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option adds a classification inference, Lambda logic, and extra latency.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for managed model routing and cost optimization by request complexity.
- **C:** This option cannot optimize cost by complexity because it uses one model for every request.
- **D:** This option relies on brittle keyword rules and adds multiple endpoints and provisioned-throughput operations.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is managed model routing and cost optimization by request complexity; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 84

### 中文

#### 考点背景

这题考查**可预测性能、受控 Prompt 版本与危险输出阻断**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 A。Provisioned Throughput 提供更可预测的推理容量和延迟；Prompt Management 的版本与审批流程使相同输入使用受控的 Prompt 版本，从而提高重复输出的稳定性；Guardrails 的语义拒绝规则可以拦截不安全建议。B 的 Agent、日志和 A/B 测试不能保证 99.5% 的一致性，也不能直接阻止危险输出；C 的缓存只覆盖命中的重复请求，X-Ray 只能观测瓶颈；D 增加检索和编排组件，却没有提供同等明确的安全阻断控制，并可能增加延迟。

#### 场景比喻

把应用当成自动配药机：Provisioned Throughput 保证窗口容量，Prompt 版本审批保证每次使用同一张处方，Guardrails 则在出药口拦下危险建议。监控摄像头或缓存都不能替代这三项控制。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Provisioned Throughput | 预留专用车道 | 提供可预测的模型容量与延迟，但带来固定容量成本 |
| Amazon Bedrock Guardrails | 模型进出口安检 | 在请求和响应边界执行内容、主题和敏感信息策略 |
| Prompt Management | 受控处方版本库 | 管理、版本化和审批 Prompt，减少未经控制的变化 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【可预测性能、受控 Prompt 版本与危险输出阻断】。
2. **再核对正确组合：** 正确答案是 **A**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 A。Provisioned Throughput 提供更可预测的推理容量和延迟；Prompt Management 的版本与审批流程使相同输入使用受控的 Prompt 版本，从而提高重复输出的稳定性；Guardrails 的语义拒绝规则可以拦截不安全建议。B 的 Agent、日志和 A/B 测试不能保证 99.5% 的一致性，也不能直接阻止危险输出；C 的缓存只覆盖命中的重复请求，X-Ray 只能观测瓶颈；D 增加检索和编排组件，却没有提供同等明确的安全阻断控制，并可能增加延迟。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 正确。该项是答案 **A** 的组成部分，直接落实【可预测性能、受控 Prompt 版本与危险输出阻断】的关键机制。
- **B：** 该项的 Agent、日志和 A/B 测试不能保证 99.5% 的一致性，也不能直接阻止危险输出。
- **C：** 该项的缓存只覆盖命中的重复请求，X-Ray 只能观测瓶颈。
- **D：** 该项增加检索和编排组件，却没有提供同等明确的安全阻断控制，并可能增加延迟。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【可预测性能、受控 Prompt 版本与危险输出阻断】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **predictable performance, controlled prompt versions, and unsafe-output blocking**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is A. Provisioned Throughput provides more predictable inference capacity and latency. Prompt Management versions and approval workflows ensure that repeated inputs use a controlled prompt version, improving output stability, while Guardrails semantic denial rules can block unsafe recommendations. B's agents, logs, and A/B testing do not guarantee 99.5% consistency or directly block unsafe output. C's cache helps only on cache hits and X-Ray observes bottlenecks rather than fixing safety. D adds retrieval and orchestration components without an equivalent explicit blocking control and can increase latency.

#### Analogy

The application is an automated pharmacy. Provisioned Throughput reserves counter capacity, approved prompt versions keep the prescription consistent, and Guardrails stops unsafe advice at the output. Monitoring or caching cannot replace those controls.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Provisioned Throughput | Reserved express lane | Provides predictable model capacity and latency with committed capacity cost |
| Amazon Bedrock Guardrails | Model boundary checkpoint | Enforces content, topic, and sensitive-information policies on input and output |
| Prompt Management | Controlled prescription library | Versions and approves prompts to reduce uncontrolled variation |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver predictable performance, controlled prompt versions, and unsafe-output blocking, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **A**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is A. Provisioned Throughput provides more predictable inference capacity and latency. Prompt Management versions and approval workflows ensure that repeated inputs use a controlled prompt version, improving output stability, while Guardrails semantic denial rules can block unsafe recommendations. B's agents, logs, and A/B testing do not guarantee 99.5% consistency or directly block unsafe output. C's cache helps only on cache hits and X-Ray observes bottlenecks rather than fixing safety. D adds retrieval and orchestration components without an equivalent explicit blocking control and can increase latency.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** Correct. This choice is part of answer **A** and directly implements the decisive mechanism for predictable performance, controlled prompt versions, and unsafe-output blocking.
- **B:** This option proposes [Use Amazon Bedrock Agents to manage chaining. Log model inputs and outputs to Amazon CloudWatch Logs. Use logs from Amazon CloudWatch to perform A/B t…], but it does not close every constraint in the stem. Compared with **A**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **C:** This option proposes [Cache prompt results in Amazon ElastiCache. Use AWS Lambda functions to pre-process metadata and to trace end-to-end latency. Use AWS X-Ray to identif…], but it does not close every constraint in the stem. Compared with **A**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **D:** This option adds retrieval and orchestration components without an equivalent explicit blocking control and can increase latency.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is predictable performance, controlled prompt versions, and unsafe-output blocking; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 85

### 中文

#### 考点背景

这题考查**区域数据驻留、分类、不可篡改保留与访问审计**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 C。按 Region 隔离的 S3 Bucket Policy 和在数据来源 Region 内预处理，有助于把数据驻留与处理限定在同一大洲；S3 Object Lock 可保护数据和审计工件不被随意修改，Macie 提供数据分类，CloudTrail 记录 API 和数据访问活动。A 的 Cross-Region Inference 可能把数据路由到不符合指定驻留边界的 Region，CloudWatch 也不是不可篡改的审计存储；B 缺少数据分类和对象保留控制；D 依赖自定义监控和人工报告，不能可靠执行驻留、分类和审计要求。

#### 场景比喻

把各洲数据看成各自司法辖区的证物室：区域 Bucket Policy 是国境线，本地预处理是不把证物带出辖区，Macie 是分类员，Object Lock 是封条，CloudTrail 是出入登记簿。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS CloudTrail | API 与数据访问登记簿 | 记录主体、时间、操作和受访问资源以供审计 |
| Amazon S3 Object Lock | 不可撕毁的封条 | 在保留期内防止对象被覆盖或删除 |
| Amazon S3 | 耐久对象仓库 | 保存区域数据源、日志和审计工件 |
| Amazon Macie | 敏感数据分类员 | 发现并分类 S3 中的敏感数据与 PII |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【区域数据驻留、分类、不可篡改保留与访问审计】。
2. **再核对正确组合：** 正确答案是 **C**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 C。按 Region 隔离的 S3 Bucket Policy 和在数据来源 Region 内预处理，有助于把数据驻留与处理限定在同一大洲；S3 Object Lock 可保护数据和审计工件不被随意修改，Macie 提供数据分类，CloudTrail 记录 API 和数据访问活动。A 的 Cross-Region Inference 可能把数据路由到不符合指定驻留边界的 Region，CloudWatch 也不是不可篡改的审计存储；B 缺少数据分类和对象保留控制；D 依赖自定义监控和人工报告，不能可靠执行驻留、分类和审计要求。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 Cross-Region Inference 可能把数据路由到不符合指定驻留边界的 Region，CloudWatch 也不是不可篡改的审计存储。
- **B：** 该项缺少数据分类和对象保留控制。
- **C：** 正确。该项是答案 **C** 的组成部分，直接落实【区域数据驻留、分类、不可篡改保留与访问审计】的关键机制。
- **D：** 该项依赖自定义监控和人工报告，不能可靠执行驻留、分类和审计要求。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【区域数据驻留、分类、不可篡改保留与访问审计】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **regional data residency, classification, immutable retention, and access auditing**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is C. Region-specific S3 policies and preprocessing in the data's source Region help keep residency and processing within the required continental boundary. S3 Object Lock protects data and audit artifacts from unauthorized alteration, Macie provides classification, and CloudTrail records API and data-access activity. A's cross-Region inference can route data outside the required residency boundary, and CloudWatch is not immutable audit storage. B lacks the required classification and object-retention controls. D relies on custom monitoring and manual reports rather than enforcing residency, classification, and auditable retention.

#### Analogy

Treat continental data as evidence in its own jurisdiction. Regional bucket policies are the border, local preprocessing keeps evidence inside, Macie classifies it, Object Lock seals it, and CloudTrail records access.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS CloudTrail | API and data-access ledger | Records actor, time, action, and affected resource for audits |
| Amazon S3 Object Lock | Tamper-resistant seal | Prevents object overwrite or deletion during retention |
| Amazon S3 | Durable object warehouse | Stores regional source data, logs, and audit artifacts |
| Amazon Macie | Sensitive-data classifier | Discovers and classifies sensitive data and PII in S3 |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver regional data residency, classification, immutable retention, and access auditing, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **C**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is C. Region-specific S3 policies and preprocessing in the data's source Region help keep residency and processing within the required continental boundary. S3 Object Lock protects data and audit artifacts from unauthorized alteration, Macie provides classification, and CloudTrail records API and data-access activity. A's cross-Region inference can route data outside the required residency boundary, and CloudWatch is not immutable audit storage. B lacks the required classification and object-retention controls. D relies on custom monitoring and manual reports rather than enforcing residency, classification, and auditable retention.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option proposes [Deploy the application in each Region with local IAM policies. Use Amazon Bedrock cross-Region inference to distribute the workload. Use Amazon CloudW…], but it does not close every constraint in the stem. Compared with **C**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **B:** This option lacks the required classification and object-retention controls.
- **C:** Correct. This choice is part of answer **C** and directly implements the decisive mechanism for regional data residency, classification, immutable retention, and access auditing.
- **D:** This option relies on custom monitoring and manual reports rather than enforcing residency, classification, and auditable retention.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is regional data residency, classification, immutable retention, and access auditing; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 86

### 中文

#### 考点背景

这题考查**低延迟生成式应用的毒性、幻觉、留存和合规指标**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 A。Guardrails 提供托管的内容过滤和毒性检测，可直接保护客户交互；Model Evaluation 可用于识别和量化幻觉风险；DynamoDB 适合低延迟保存 Prompt-Response Pair，并可通过自定义 CloudWatch 指标接入现有合规 Dashboard。该组合服务较少，适合 60 天交付和低延迟要求。TTL 必须按监管保留期设置，不能在审计期结束前删除必需记录。B、D 需要更多自定义验证、监控或数据管道，且 WAF 不是模型输出安全控制；C 虽然能提供 Grounding 和搜索，但 OpenSearch、QuickSight 等组件增加运维与响应开销，也不会自动形成实时漂移检测。

#### 场景比喻

像 60 天内上线的客服质检线：Guardrails 是即时安检员，Model Evaluation 是定期抽检实验室，DynamoDB 是快速留样柜，CloudWatch 是合规大屏。TTL 必须等法定留样期结束才能清理。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 模型进出口安检 | 在请求和响应边界执行内容、主题和敏感信息策略 |
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |
| Amazon DynamoDB | 低延迟记录柜 | 以可扩展方式保存 Prompt、响应和状态记录 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【低延迟生成式应用的毒性、幻觉、留存和合规指标】。
2. **再核对正确组合：** 正确答案是 **A**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 A。Guardrails 提供托管的内容过滤和毒性检测，可直接保护客户交互；Model Evaluation 可用于识别和量化幻觉风险；DynamoDB 适合低延迟保存 Prompt-Response Pair，并可通过自定义 CloudWatch 指标接入现有合规 Dashboard。该组合服务较少，适合 60 天交付和低延迟要求。TTL 必须按监管保留期设置，不能在审计期结束前删除必需记录。B、D 需要更多自定义验证、监控或数据管道，且 WAF 不是模型输出安全控制；C 虽然能提供 Grounding 和搜索，但 OpenSearch、QuickSight 等组件增加运维与响应开销，也不会自动形成实时漂移检测。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 正确。该项是答案 **A** 的组成部分，直接落实【低延迟生成式应用的毒性、幻觉、留存和合规指标】的关键机制。
- **B：** 该项需要更多自定义验证、监控或数据管道，且 WAF 不是模型输出安全控制。
- **C：** 该项虽然能提供 Grounding 和搜索，但 OpenSearch、QuickSight 等组件增加运维与响应开销，也不会自动形成实时漂移检测。
- **D：** 该项需要更多自定义验证、监控或数据管道，且 WAF 不是模型输出安全控制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【低延迟生成式应用的毒性、幻觉、留存和合规指标】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **toxicity, hallucination, retention, and compliance metrics for a low-latency GenAI app**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is A. Guardrails provide managed content filtering and toxicity detection for customer interactions. Model Evaluation can identify and quantify hallucination risk, while DynamoDB is suitable for low-latency storage of prompt-response pairs and CloudWatch custom metrics can feed the existing compliance dashboard. The combination uses relatively few managed components, supporting the delivery and latency constraints. TTL must be aligned with the regulatory retention period and must not delete records that are still required for audit. B and D require more custom validation, monitoring, or data pipelines, and WAF is not a model-output safety control. C adds OpenSearch and QuickSight operations and does not automatically provide real-time drift detection.

#### Analogy

It is a customer-service quality line due in 60 days. Guardrails performs inline screening, Model Evaluation runs lab tests, DynamoDB stores samples quickly, and CloudWatch feeds the compliance board; TTL must respect the legal retention window.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Model boundary checkpoint | Enforces content, topic, and sensitive-information policies on input and output |
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |
| Amazon DynamoDB | Low-latency record cabinet | Scalably stores prompt, response, and state records |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver toxicity, hallucination, retention, and compliance metrics for a low-latency GenAI app, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **A**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is A. Guardrails provide managed content filtering and toxicity detection for customer interactions. Model Evaluation can identify and quantify hallucination risk, while DynamoDB is suitable for low-latency storage of prompt-response pairs and CloudWatch custom metrics can feed the existing compliance dashboard. The combination uses relatively few managed components, supporting the delivery and latency constraints. TTL must be aligned with the regulatory retention period and must not delete records that are still required for audit. B and D require more custom validation, monitoring, or data pipelines, and WAF is not a model-output safety control. C adds OpenSearch and QuickSight operations and does not automatically provide real-time drift detection.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** Correct. This choice is part of answer **A** and directly implements the decisive mechanism for toxicity, hallucination, retention, and compliance metrics for a low-latency GenAI app.
- **B:** This option require more custom validation, monitoring, or data pipelines, and WAF is not a model-output safety control.
- **C:** This option adds OpenSearch and QuickSight operations and does not automatically provide real-time drift detection.
- **D:** This option require more custom validation, monitoring, or data pipelines, and WAF is not a model-output safety control.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is toxicity, hallucination, retention, and compliance metrics for a low-latency GenAI app; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 87

### 中文

#### 考点背景

这题考查**数据湖中按部门和 Region 扩展的细粒度授权**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。用 IAM Role 认证并以 LF-Tag Expression 授权，可以按业务部门和 Region 对 Lake Formation 资源实施可扩展的细粒度访问控制，也可以限制到不含 PII 的列或受控数据子集；CloudTrail 提供统一的数据访问审计。A 需要为每个组合复制数据，S3 Bucket Policy 不能提供 Lake Formation 的表列级治理；C 的直接授权和应用层过滤难以规模化且可能被其他访问路径绕过；D 的 Presigned URL 绕过了 Lake Formation 的细粒度表列权限，过滤逻辑也完全依赖自定义 API。

#### 场景比喻

Lake Formation 像大型图书馆：LF-Tag 是贴在书架和读者证上的分类标签，IAM Role 是读者证，表达式权限让某部门只看到本地区且不含 PII 的列；CloudTrail 记录每次借阅。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| IAM Role | 可审计员工证 | 为工作负载和联合用户提供一致的临时 AWS 身份 |
| AWS CloudTrail | API 与数据访问登记簿 | 记录主体、时间、操作和受访问资源以供审计 |
| LF-Tag | 数据分类标签 | 用表达式把资源分类与角色权限解耦并规模化授权 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【数据湖中按部门和 Region 扩展的细粒度授权】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。用 IAM Role 认证并以 LF-Tag Expression 授权，可以按业务部门和 Region 对 Lake Formation 资源实施可扩展的细粒度访问控制，也可以限制到不含 PII 的列或受控数据子集；CloudTrail 提供统一的数据访问审计。A 需要为每个组合复制数据，S3 Bucket Policy 不能提供 Lake Formation 的表列级治理；C 的直接授权和应用层过滤难以规模化且可能被其他访问路径绕过；D 的 Presigned URL 绕过了 Lake Formation 的细粒度表列权限，过滤逻辑也完全依赖自定义 API。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项需要为每个组合复制数据，S3 Bucket Policy 不能提供 Lake Formation 的表列级治理。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【数据湖中按部门和 Region 扩展的细粒度授权】的关键机制。
- **C：** 该项的直接授权和应用层过滤难以规模化且可能被其他访问路径绕过。
- **D：** 该项的 Presigned URL 绕过了 Lake Formation 的细粒度表列权限，过滤逻辑也完全依赖自定义 API。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【数据湖中按部门和 Region 扩展的细粒度授权】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **scalable fine-grained authorization by business unit and Region in a data lake**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. IAM-role authentication combined with LF-Tag expression permissions provides scalable, fine-grained Lake Formation access by business unit and Region, including restricting access to approved subsets or non-PII columns. CloudTrail provides a centralized audit trail for data access. A duplicates data for every combination and S3 bucket policies do not provide Lake Formation table- and column-level governance. C is difficult to scale and relies on an application filter that other access paths might bypass. D's presigned S3 URLs bypass Lake Formation's fine-grained table and column permissions and make the filtering logic entirely dependent on a custom API.

#### Analogy

Lake Formation is a large library. LF-Tags label shelves and reader cards, IAM roles identify readers, expression permissions expose only approved regional and non-PII columns, and CloudTrail records each checkout.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| IAM Role | Auditable employee badge | Provides consistent temporary AWS identity to workloads and federated users |
| AWS CloudTrail | API and data-access ledger | Records actor, time, action, and affected resource for audits |
| LF-Tag | Data classification label | Scales access by decoupling resource classification from role permissions |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver scalable fine-grained authorization by business unit and Region in a data lake, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. IAM-role authentication combined with LF-Tag expression permissions provides scalable, fine-grained Lake Formation access by business unit and Region, including restricting access to approved subsets or non-PII columns. CloudTrail provides a centralized audit trail for data access. A duplicates data for every combination and S3 bucket policies do not provide Lake Formation table- and column-level governance. C is difficult to scale and relies on an application filter that other access paths might bypass. D's presigned S3 URLs bypass Lake Formation's fine-grained table and column permissions and make the filtering logic entirely dependent on a custom API.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option duplicates data for every combination and S3 bucket policies do not provide Lake Formation table- and column-level governance.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for scalable fine-grained authorization by business unit and Region in a data lake.
- **C:** This option is difficult to scale and relies on an application filter that other access paths might bypass.
- **D:** This option proposes [Configure the FM to request temporary credentials from AWS STS. Access the data by using presigned S3 URLs that are generated by an API that applies b…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is scalable fine-grained authorization by business unit and Region in a data lake; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 88

### 中文

#### 考点背景

这题考查**RAG 初始召回后的托管上下文重排**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。Knowledge Bases 内置的 Reranking Configuration 可以在初始检索后使用托管 Reranker Model，按查询上下文重新评估并排序候选文档，直接改善语义相似但上下文不相关的问题，同时运维最少。A 需要自行部署和维护 SageMaker 排序 Endpoint 及 API；B 拼接 Comprehend、Textract 和 Neptune，既复杂又不是针对 RAG 候选结果的标准重排路径；C 虽可构建自定义检索流水线，但会增加 API 编排、错误处理和延迟，而内置配置已覆盖该需求。

#### 场景比喻

初始向量检索像书店按书名相似度拿来一摞书，Reranker 是懂当前问题的店员，会把真正相关的书放到最上面。无需另开一家 SageMaker 排序工厂。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Knowledge Bases | 可检索证据档案柜 | 托管 RAG 数据源、检索与生成上下文连接 |
| Reranking Configuration | 检索结果复审员 | 按查询上下文对初始候选重新评分和排序 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【RAG 初始召回后的托管上下文重排】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。Knowledge Bases 内置的 Reranking Configuration 可以在初始检索后使用托管 Reranker Model，按查询上下文重新评估并排序候选文档，直接改善语义相似但上下文不相关的问题，同时运维最少。A 需要自行部署和维护 SageMaker 排序 Endpoint 及 API；B 拼接 Comprehend、Textract 和 Neptune，既复杂又不是针对 RAG 候选结果的标准重排路径；C 虽可构建自定义检索流水线，但会增加 API 编排、错误处理和延迟，而内置配置已覆盖该需求。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项需要自行部署和维护 SageMaker 排序 Endpoint 及 API。
- **B：** 该项拼接 Comprehend、Textract 和 Neptune，既复杂又不是针对 RAG 候选结果的标准重排路径。
- **C：** 该项虽可构建自定义检索流水线，但会增加 API 编排、错误处理和延迟，而内置配置已覆盖该需求。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【RAG 初始召回后的托管上下文重排】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【RAG 初始召回后的托管上下文重排】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **managed contextual reranking after initial RAG retrieval**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. The Knowledge Bases reranking configuration can use a managed reranker after initial retrieval to reassess and reorder candidate documents using the query context. This directly addresses semantically similar but contextually irrelevant results with the least operational work. A requires operating a SageMaker ranking endpoint and an API layer. B combines Comprehend, Textract, and Neptune in a complex pipeline that is not the standard way to rerank RAG candidates. C is possible as a custom retrieval pipeline but adds orchestration, failure handling, and latency that the native configuration avoids.

#### Analogy

Initial vector retrieval brings a stack of similarly titled books. The managed reranker is a clerk who understands the current question and moves the truly relevant books to the top without a separate SageMaker ranking factory.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Knowledge Bases | Searchable evidence archive | Connects managed RAG data, retrieval, and generation context |
| Reranking Configuration | Retrieval result reviewer | Rescores and reorders initial candidates using query context |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver managed contextual reranking after initial RAG retrieval, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. The Knowledge Bases reranking configuration can use a managed reranker after initial retrieval to reassess and reorder candidate documents using the query context. This directly addresses semantically similar but contextually irrelevant results with the least operational work. A requires operating a SageMaker ranking endpoint and an API layer. B combines Comprehend, Textract, and Neptune in a complex pipeline that is not the standard way to rerank RAG candidates. C is possible as a custom retrieval pipeline but adds orchestration, failure handling, and latency that the native configuration avoids.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option requires operating a SageMaker ranking endpoint and an API layer.
- **B:** This option combines Comprehend, Textract, and Neptune in a complex pipeline that is not the standard way to rerank RAG candidates.
- **C:** This option is possible as a custom retrieval pipeline but adds orchestration, failure handling, and latency that the native configuration avoids.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for managed contextual reranking after initial RAG retrieval.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is managed contextual reranking after initial RAG retrieval; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 89

### 中文

#### 考点背景

这题考查**模型调用前并行执行毒性、Prompt 安全和 PII 脱敏**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。Toxicity Detection 可以过滤冒犯性内容；Prompt Safety Classification 和 PII Detection 可以并行完成，并通过 Entity Redaction 在请求到达 FM 前保护隐私，较低的过滤延迟也有利于整体响应时间；CloudWatch Alarm 可监控过滤指标。A 只检测 PII 而不 Redaction，隐私仍可能泄露；B 用自定义分类器承担通用安全能力，且只有通过第一层才检测 PII，会造成覆盖不足；C 串行执行多个阶段并在最后使用 Streaming，不能保证所有过滤在 FM 调用前完成。

#### 场景比喻

进入模型前像机场安检：毒性检测查危险品，Prompt Safety 检查异常意图，PII Detection 找证件号码，Entity Redaction 当场遮住它。并行安检既守住登机口，又不会让旅客排成长队。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【模型调用前并行执行毒性、Prompt 安全和 PII 脱敏】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。Toxicity Detection 可以过滤冒犯性内容；Prompt Safety Classification 和 PII Detection 可以并行完成，并通过 Entity Redaction 在请求到达 FM 前保护隐私，较低的过滤延迟也有利于整体响应时间；CloudWatch Alarm 可监控过滤指标。A 只检测 PII 而不 Redaction，隐私仍可能泄露；B 用自定义分类器承担通用安全能力，且只有通过第一层才检测 PII，会造成覆盖不足；C 串行执行多个阶段并在最后使用 Streaming，不能保证所有过滤在 FM 调用前完成。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项只检测 PII 而不 Redaction，隐私仍可能泄露。
- **B：** 该项用自定义分类器承担通用安全能力，且只有通过第一层才检测 PII，会造成覆盖不足。
- **C：** 该项串行执行多个阶段并在最后使用 Streaming，不能保证所有过滤在 FM 调用前完成。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【模型调用前并行执行毒性、Prompt 安全和 PII 脱敏】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【模型调用前并行执行毒性、Prompt 安全和 PII 脱敏】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **parallel toxicity, prompt-safety, and PII redaction before model invocation**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. Toxicity Detection filters offensive content. Prompt Safety Classification and PII Detection can run in parallel, and entity redaction protects privacy before the request reaches the FM while keeping preprocessing latency lower; CloudWatch alarms monitor the filter metrics. A detects PII but does not redact it, so privacy is not protected. B replaces managed safety capabilities with a custom classifier and conditionally performs PII detection, leaving coverage gaps. C serializes several stages and performs PII filtering in streaming mode, so it cannot ensure that all preprocessing is complete before the FM call.

#### Analogy

The pre-model path is airport security. Toxicity detection finds dangerous content, prompt safety checks intent, PII detection finds identifiers, and redaction covers them before boarding; parallel lanes protect the gate without a long queue.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver parallel toxicity, prompt-safety, and PII redaction before model invocation, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. Toxicity Detection filters offensive content. Prompt Safety Classification and PII Detection can run in parallel, and entity redaction protects privacy before the request reaches the FM while keeping preprocessing latency lower; CloudWatch alarms monitor the filter metrics. A detects PII but does not redact it, so privacy is not protected. B replaces managed safety capabilities with a custom classifier and conditionally performs PII detection, leaving coverage gaps. C serializes several stages and performs PII filtering in streaming mode, so it cannot ensure that all preprocessing is complete before the FM call.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option detects PII but does not redact it, so privacy is not protected.
- **B:** This option replaces managed safety capabilities with a custom classifier and conditionally performs PII detection, leaving coverage gaps.
- **C:** This option serializes several stages and performs PII filtering in streaming mode, so it cannot ensure that all preprocessing is complete before the FM call.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for parallel toxicity, prompt-safety, and PII redaction before model invocation.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is parallel toxicity, prompt-safety, and PII redaction before model invocation; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 90

### 中文

#### 考点背景

这题考查**关系型事实的 Text-to-SQL 提取与多层结果校验**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。旅行历史存储在关系型 RDS 中，Text-to-SQL 配合 SQL Validation 能直接、可验证地提取预订模式、偏好和忠诚度等结构化事实；Guardrails 和 Step Functions/Lambda 验证流程进一步控制输出并降低幻觉。A、C 以语义检索或 Embedding 相似度处理结构化事实，可能遗漏精确条件，结果一致性较差；D 虽然也使用 Text-to-SQL，但依赖 Confidence Scoring 和 Semantic Similarity，不能替代 SQL 校验与工作流级验证，且会增加不必要的复杂度。

#### 场景比喻

RDS 像旅行社账本：Text-to-SQL 把自然语言问题翻成可核对的查账指令，SQL Validation 检查指令是否合法，Step Functions 和 Lambda 再复核结果，Guardrails 防止客服把猜测说成事实。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Guardrails | 模型进出口安检 | 在请求和响应边界执行内容、主题和敏感信息策略 |
| AWS Lambda | 按需执行的工位 | 以事件或请求驱动方式运行轻量集成与校验逻辑 |
| AWS Step Functions | 有记忆的流程主管 | 持久化编排长时、异步、重试和分支工作流 |
| Text-to-SQL | 自然语言查账翻译器 | 把问题转换为可验证的关系型查询 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【关系型事实的 Text-to-SQL 提取与多层结果校验】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。旅行历史存储在关系型 RDS 中，Text-to-SQL 配合 SQL Validation 能直接、可验证地提取预订模式、偏好和忠诚度等结构化事实；Guardrails 和 Step Functions/Lambda 验证流程进一步控制输出并降低幻觉。A、C 以语义检索或 Embedding 相似度处理结构化事实，可能遗漏精确条件，结果一致性较差；D 虽然也使用 Text-to-SQL，但依赖 Confidence Scoring 和 Semantic Similarity，不能替代 SQL 校验与工作流级验证，且会增加不必要的复杂度。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项以语义检索或 Embedding 相似度处理结构化事实，可能遗漏精确条件，结果一致性较差。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【关系型事实的 Text-to-SQL 提取与多层结果校验】的关键机制。
- **C：** 该项以语义检索或 Embedding 相似度处理结构化事实，可能遗漏精确条件，结果一致性较差。
- **D：** 该项虽然也使用 Text-to-SQL，但依赖 Confidence Scoring 和 Semantic Similarity，不能替代 SQL 校验与工作流级验证，且会增加不必要的复杂度。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【关系型事实的 Text-to-SQL 提取与多层结果校验】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **text-to-SQL extraction of relational facts with layered validation**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. The travel history is in relational RDS data, so text-to-SQL with SQL validation can retrieve structured facts such as booking patterns, preferences, and loyalty information directly and verifiably. Guardrails and a Step Functions/Lambda validation workflow further constrain the output and reduce hallucinations. A and C use semantic or embedding similarity for structured facts, which can miss exact conditions and reduce consistency. D also uses text-to-SQL, but confidence scoring and semantic similarity do not replace SQL validation and workflow-level checks and add unnecessary complexity.

#### Analogy

RDS is a travel-agency ledger. Text-to-SQL converts a question into an auditable lookup, SQL validation checks the query, Step Functions and Lambda verify the result, and Guardrails keeps guesses out of the final response.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Guardrails | Model boundary checkpoint | Enforces content, topic, and sensitive-information policies on input and output |
| AWS Lambda | On-demand workbench | Runs event- or request-driven integration and validation logic |
| AWS Step Functions | Stateful workflow supervisor | Durably orchestrates long-running, asynchronous, retry, and branching work |
| Text-to-SQL | Natural-language ledger translator | Converts questions into verifiable relational queries |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver text-to-SQL extraction of relational facts with layered validation, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. The travel history is in relational RDS data, so text-to-SQL with SQL validation can retrieve structured facts such as booking patterns, preferences, and loyalty information directly and verifiably. Guardrails and a Step Functions/Lambda validation workflow further constrain the output and reduce hallucinations. A and C use semantic or embedding similarity for structured facts, which can miss exact conditions and reduce consistency. D also uses text-to-SQL, but confidence scoring and semantic similarity do not replace SQL validation and workflow-level checks and add unnecessary complexity.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option use semantic or embedding similarity for structured facts, which can miss exact conditions and reduce consistency.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for text-to-SQL extraction of relational facts with layered validation.
- **C:** This option use semantic or embedding similarity for structured facts, which can miss exact conditions and reduce consistency.
- **D:** This option also uses text-to-SQL, but confidence scoring and semantic similarity do not replace SQL validation and workflow-level checks and add unnecessary complexity.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is text-to-SQL extraction of relational facts with layered validation; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 91

### 中文

#### 考点背景

这题考查**Guardrail 在开发到生产阶段的 PII 动作和区域驻留**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。Sensitive Information Filter 能识别个人信息；开发和测试阶段使用 Mask Mode，既能观察检测效果又避免暴露原始 PII，生产阶段切换为 Block Mode 可阻止不合规响应。每个运营 Region 部署本地 Guardrail，避免 Cross-Region 处理违反数据驻留要求。A、D 使用 Cross-Region Guardrail，不满足同 Region 处理；C 在所有阶段使用 Detect Mode，生产环境仍可能泄露个人信息，而且关闭 Invocation Logging 会削弱审计。

#### 场景比喻

像新安检系统分阶段上线：开发时用 Mask 模式，把敏感号码遮住但保留测试证据；生产时切到 Block，违规包裹直接不放行。每个 Region 都有本地安检口，数据不跨境送检。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【Guardrail 在开发到生产阶段的 PII 动作和区域驻留】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。Sensitive Information Filter 能识别个人信息；开发和测试阶段使用 Mask Mode，既能观察检测效果又避免暴露原始 PII，生产阶段切换为 Block Mode 可阻止不合规响应。每个运营 Region 部署本地 Guardrail，避免 Cross-Region 处理违反数据驻留要求。A、D 使用 Cross-Region Guardrail，不满足同 Region 处理；C 在所有阶段使用 Detect Mode，生产环境仍可能泄露个人信息，而且关闭 Invocation Logging 会削弱审计。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项使用 Cross-Region Guardrail，不满足同 Region 处理。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【Guardrail 在开发到生产阶段的 PII 动作和区域驻留】的关键机制。
- **C：** 该项在所有阶段使用 Detect Mode，生产环境仍可能泄露个人信息，而且关闭 Invocation Logging 会削弱审计。
- **D：** 该项使用 Cross-Region Guardrail，不满足同 Region 处理。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【Guardrail 在开发到生产阶段的 PII 动作和区域驻留】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **PII guardrail actions across lifecycle stages and regional residency**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. A sensitive-information filter detects personal data. Mask mode during development and testing allows the team to measure detection while hiding the original PII; switching to block mode in production prevents noncompliant responses. Deploying a local guardrail in each operating Region avoids cross-Region processing that would violate residency requirements. A and D use cross-Region guardrails, and C leaves production in detect mode, which can still expose PII, while disabling invocation logging weakens the audit trail.

#### Analogy

A new screening system rolls out in stages. Mask mode hides identifiers while preserving test evidence; production switches to Block so noncompliant payloads do not pass. Every Region has a local checkpoint, avoiding cross-border inspection.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver PII guardrail actions across lifecycle stages and regional residency, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. A sensitive-information filter detects personal data. Mask mode during development and testing allows the team to measure detection while hiding the original PII; switching to block mode in production prevents noncompliant responses. Deploying a local guardrail in each operating Region avoids cross-Region processing that would violate residency requirements. A and D use cross-Region guardrails, and C leaves production in detect mode, which can still expose PII, while disabling invocation logging weakens the audit trail.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option use cross-Region guardrails, and C leaves production in detect mode, which can still expose PII, while disabling invocation logging weakens the audit trail.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for PII guardrail actions across lifecycle stages and regional residency.
- **C:** This option proposes [Configure an Amazon Bedrock guardrail to apply content and topic filters. Set the guardrail to detect mode during development, testing, and production…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **D:** This option use cross-Region guardrails, and C leaves production in detect mode, which can still expose PII, while disabling invocation logging weakens the audit trail.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is PII guardrail actions across lifecycle stages and regional residency; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 92

### 中文

#### 考点背景

这题考查**无关键词文档的托管语义向量化与 Serverless 向量存储**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。Titan Embeddings 是托管能力，可把没有关键词的文档转换为可比较的语义向量；Aurora PostgreSQL Serverless 的 `pgvector` 可在少于 100 万个文件的规模下存储向量、文本和 Metadata，并支持相似度与 Metadata 查询，运维较少。A 的 OpenSearch 需要更多搜索基础设施配置；B 只抽取 Topic，不能充分表达摘要之间的语义相似性；C 需要自行部署和维护 Sentence-Transformer Endpoint，运维开销高于托管 Embeddings 和 Serverless 数据库。

#### 场景比喻

研究摘要像没有索引词的书：Titan Embeddings 把每本书变成语义坐标，Aurora PostgreSQL Serverless 加 pgvector 是可自动伸缩的地图柜，既按距离找相似内容，也能按 Metadata 筛选。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Titan Embeddings | 语义坐标生成器 | 把文档转换为可做相似度比较的向量 |
| Aurora PostgreSQL Serverless | 弹性向量与元数据仓库 | 用 pgvector 保存向量并支持关系型 Metadata 查询 |
| pgvector | 向量抽屉插件 | 为 PostgreSQL 增加向量存储和相似度检索 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【无关键词文档的托管语义向量化与 Serverless 向量存储】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。Titan Embeddings 是托管能力，可把没有关键词的文档转换为可比较的语义向量；Aurora PostgreSQL Serverless 的 `pgvector` 可在少于 100 万个文件的规模下存储向量、文本和 Metadata，并支持相似度与 Metadata 查询，运维较少。A 的 OpenSearch 需要更多搜索基础设施配置；B 只抽取 Topic，不能充分表达摘要之间的语义相似性；C 需要自行部署和维护 Sentence-Transformer Endpoint，运维开销高于托管 Embeddings 和 Serverless 数据库。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 OpenSearch 需要更多搜索基础设施配置。
- **B：** 该项只抽取 Topic，不能充分表达摘要之间的语义相似性。
- **C：** 该项需要自行部署和维护 Sentence-Transformer Endpoint，运维开销高于托管 Embeddings 和 Serverless 数据库。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【无关键词文档的托管语义向量化与 Serverless 向量存储】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【无关键词文档的托管语义向量化与 Serverless 向量存储】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **managed semantic embeddings and serverless vector storage for documents without keywords**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. The managed Titan Embeddings capability converts documents without keywords into comparable semantic vectors. Aurora PostgreSQL Serverless with `pgvector` can store vectors, text, and metadata and support similarity and metadata queries at a scale below one million files with relatively little operations work. A requires more search-infrastructure configuration in OpenSearch. B's topic extraction does not adequately represent semantic similarity between abstracts. C requires deploying and maintaining a Sentence-Transformer endpoint, which has more operational overhead than managed embeddings and a serverless database.

#### Analogy

Research abstracts are books without index terms. Titan Embeddings turns each into semantic coordinates, and Aurora PostgreSQL Serverless with pgvector is an elastic map cabinet that supports both vector distance and metadata filters.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Titan Embeddings | Semantic coordinate generator | Converts documents into vectors for similarity comparison |
| Aurora PostgreSQL Serverless | Elastic vector and metadata store | Stores pgvector embeddings and supports relational metadata queries |
| pgvector | Vector drawer extension | Adds vector storage and similarity search to PostgreSQL |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver managed semantic embeddings and serverless vector storage for documents without keywords, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. The managed Titan Embeddings capability converts documents without keywords into comparable semantic vectors. Aurora PostgreSQL Serverless with `pgvector` can store vectors, text, and metadata and support similarity and metadata queries at a scale below one million files with relatively little operations work. A requires more search-infrastructure configuration in OpenSearch. B's topic extraction does not adequately represent semantic similarity between abstracts. C requires deploying and maintaining a Sentence-Transformer endpoint, which has more operational overhead than managed embeddings and a serverless database.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option requires more search-infrastructure configuration in OpenSearch.
- **B:** This option proposes [Use Amazon Comprehend to extract topics from the digitized files. Store the topics and file metadata in an Amazon Aurora PostgreSQL database. Query th…], but it does not close every constraint in the stem. Compared with **D**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **C:** This option requires deploying and maintaining a Sentence-Transformer endpoint, which has more operational overhead than managed embeddings and a serverless database.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for managed semantic embeddings and serverless vector storage for documents without keywords.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is managed semantic embeddings and serverless vector storage for documents without keywords; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 93

### 中文

#### 考点背景

这题考查**S3 事件触发、异步转录等待和结构化生成编排**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 C、D。S3 通过 EventBridge 发出 Object Created 事件后，规则可以启动 Step Functions；工作流再等待 Transcribe 异步任务完成，并由 Lambda 构造 Prompt、调用 FM 和整理结构化输出。A 没有明确的 Prompt 构造与结构化响应控制，B 把 FM 调用直接放进工作流，缺少该控制点，不利于稳定生成和校验 JSON；E 不是 S3 可直接发送到 Step Functions 的标准通知目标，通常需要 EventBridge、SQS、SNS 或 Lambda 作为中间目标。

#### 场景比喻

上传录音像病历进入流水线：EventBridge 是分诊铃，Step Functions 保存每个异步工序状态，Transcribe 把语音转成文字，Lambda 像主治医生整理 Prompt 并校验最终 JSON 病历。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | 按需执行的工位 | 以事件或请求驱动方式运行轻量集成与校验逻辑 |
| AWS Step Functions | 有记忆的流程主管 | 持久化编排长时、异步、重试和分支工作流 |
| Amazon Transcribe | 语音速记员 | 把音频转换为后续可检索和分析的文字 |
| Amazon EventBridge | 事件分诊总线 | 把 S3 等服务事件可靠路由到工作流目标 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【S3 事件触发、异步转录等待和结构化生成编排】。
2. **再核对正确组合：** 正确答案是 **CD**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 C、D。S3 通过 EventBridge 发出 Object Created 事件后，规则可以启动 Step Functions；工作流再等待 Transcribe 异步任务完成，并由 Lambda 构造 Prompt、调用 FM 和整理结构化输出。A 没有明确的 Prompt 构造与结构化响应控制，B 把 FM 调用直接放进工作流，缺少该控制点，不利于稳定生成和校验 JSON；E 不是 S3 可直接发送到 Step Functions 的标准通知目标，通常需要 EventBridge、SQS、SNS 或 Lambda 作为中间目标。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项没有明确的 Prompt 构造与结构化响应控制，B 把 FM 调用直接放进工作流，缺少该控制点，不利于稳定生成和校验 JSON。
- **B：** 该项提出【使用 AWS Step Functions 编排工作流。调用 Amazon Transcribe 将音频转成文本并验证任务完成状态，然后直接调用 Amazon Bedrock FM，以 JSON 格式生成摘要和情感分析。】，但没有同时覆盖题干的全部约束。与答案 **CD** 对照，它缺少上文说明的关键托管边界，或增加了不必要的自定义运维与风险。
- **C：** 正确。该项是答案 **CD** 的组成部分，直接落实【S3 事件触发、异步转录等待和结构化生成编排】的关键机制。
- **D：** 正确。该项是答案 **CD** 的组成部分，直接落实【S3 事件触发、异步转录等待和结构化生成编排】的关键机制。
- **E：** 该项不是 S3 可直接发送到 Step Functions 的标准通知目标，通常需要 EventBridge、SQS、SNS 或 Lambda 作为中间目标。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【S3 事件触发、异步转录等待和结构化生成编排】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **S3 event triggering, asynchronous transcription waiting, and structured-generation orchestration**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answers are C and D. S3 can emit an Object Created event to EventBridge, whose rule starts Step Functions. The workflow waits for the asynchronous Transcribe job, then Lambda builds the prompt, invokes the FM, and normalizes the structured output. A does not explicitly provide the prompt-construction and structured-response control point, while B places the FM call directly in the workflow without that control, making reliable JSON generation and validation less explicit. E is not a standard direct S3 notification target for Step Functions; EventBridge, SQS, SNS, or Lambda is normally used as the intermediary.

#### Analogy

An uploaded recording enters a clinical workflow. EventBridge rings the intake bell, Step Functions remembers asynchronous state, Transcribe creates text, and Lambda acts as the clinician who builds the prompt and validates the final JSON record.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda | On-demand workbench | Runs event- or request-driven integration and validation logic |
| AWS Step Functions | Stateful workflow supervisor | Durably orchestrates long-running, asynchronous, retry, and branching work |
| Amazon Transcribe | Speech stenographer | Turns audio into text that can be retrieved and analyzed |
| Amazon EventBridge | Event dispatch bus | Routes service events such as S3 notifications to workflow targets |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver S3 event triggering, asynchronous transcription waiting, and structured-generation orchestration, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **CD**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answers are C and D. S3 can emit an Object Created event to EventBridge, whose rule starts Step Functions. The workflow waits for the asynchronous Transcribe job, then Lambda builds the prompt, invokes the FM, and normalizes the structured output. A does not explicitly provide the prompt-construction and structured-response control point, while B places the FM call directly in the workflow without that control, making reliable JSON generation and validation less explicit. E is not a standard direct S3 notification target for Step Functions; EventBridge, SQS, SNS, or Lambda is normally used as the intermediary.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option does not explicitly provide the prompt-construction and structured-response control point, while B places the FM call directly in the workflow without that control, making reliable JSON generation and validation less explicit.
- **B:** This option proposes [Use AWS Step Functions to orchestrate a workflow to process the recordings. Configure steps to invoke Amazon Transcribe to convert audio to text, vali…], but it does not close every constraint in the stem. Compared with **CD**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **C:** Correct. This choice is part of answer **CD** and directly implements the decisive mechanism for S3 event triggering, asynchronous transcription waiting, and structured-generation orchestration.
- **D:** Correct. This choice is part of answer **CD** and directly implements the decisive mechanism for S3 event triggering, asynchronous transcription waiting, and structured-generation orchestration.
- **E:** This option is not a standard direct S3 notification target for Step Functions; EventBridge, SQS, SNS, or Lambda is normally used as the intermediary.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is S3 event triggering, asynchronous transcription waiting, and structured-generation orchestration; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 94

### 中文

#### 考点背景

这题考查**知识库保留期、同步以及按用户组动态脱敏**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 C。S3 Lifecycle 自动删除超过 3 年的源文档，定期同步使 Knowledge Base 的索引与源数据保持一致；根据 Cognito User Group 选择对应的 `ApplyGuardrail` 配置，可以只对 Engineer 的响应 Redact PII，而不必把原始报告复制成多套知识库。A 会把源文档对所有用户都脱敏，且 Macie 本身不是写回式 Redaction 工具；B 在用户请求时才处理隐私，不能可靠保护生成响应；D 复制知识库和预处理数据增加同步、权限和运维复杂度，也可能让原始 PII 通过错误的路由暴露。

#### 场景比喻

同一医学档案室服务外科医生和工程师：S3 Lifecycle 到期销毁旧档，Knowledge Base 定期更新目录；Cognito 像不同颜色的胸牌，应用据此为工程师套上 PII 遮罩，而医生仍可按授权查看原始信息。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| AWS Lambda | 按需执行的工位 | 以事件或请求驱动方式运行轻量集成与校验逻辑 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【知识库保留期、同步以及按用户组动态脱敏】。
2. **再核对正确组合：** 正确答案是 **C**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 C。S3 Lifecycle 自动删除超过 3 年的源文档，定期同步使 Knowledge Base 的索引与源数据保持一致；根据 Cognito User Group 选择对应的 `ApplyGuardrail` 配置，可以只对 Engineer 的响应 Redact PII，而不必把原始报告复制成多套知识库。A 会把源文档对所有用户都脱敏，且 Macie 本身不是写回式 Redaction 工具；B 在用户请求时才处理隐私，不能可靠保护生成响应；D 复制知识库和预处理数据增加同步、权限和运维复杂度，也可能让原始 PII 通过错误的路由暴露。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项会把源文档对所有用户都脱敏，且 Macie 本身不是写回式 Redaction 工具。
- **B：** 该项在用户请求时才处理隐私，不能可靠保护生成响应。
- **C：** 正确。该项是答案 **C** 的组成部分，直接落实【知识库保留期、同步以及按用户组动态脱敏】的关键机制。
- **D：** 该项复制知识库和预处理数据增加同步、权限和运维复杂度，也可能让原始 PII 通过错误的路由暴露。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【知识库保留期、同步以及按用户组动态脱敏】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **knowledge-base retention, synchronization, and user-group-specific redaction**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is C. An S3 Lifecycle rule removes source reports older than three years, and scheduled synchronization keeps the Knowledge Base index aligned with the source. Selecting an `ApplyGuardrail` configuration based on the Cognito user group lets the application redact PII for engineers while preserving the original information for surgeons, without duplicating the knowledge base. A would redact the source for every user, and Macie is not itself a write-back redaction tool. B handles privacy only during a user request and does not reliably protect generated responses. D duplicates knowledge bases and adds synchronization and authorization complexity, with more opportunities for the original PII to be routed incorrectly.

#### Analogy

One medical archive serves surgeons and engineers. S3 Lifecycle disposes of expired reports, Knowledge Base sync updates the catalog, and Cognito group badges select a guardrail that redacts PII only for engineers.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| AWS Lambda | On-demand workbench | Runs event- or request-driven integration and validation logic |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver knowledge-base retention, synchronization, and user-group-specific redaction, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **C**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is C. An S3 Lifecycle rule removes source reports older than three years, and scheduled synchronization keeps the Knowledge Base index aligned with the source. Selecting an `ApplyGuardrail` configuration based on the Cognito user group lets the application redact PII for engineers while preserving the original information for surgeons, without duplicating the knowledge base. A would redact the source for every user, and Macie is not itself a write-back redaction tool. B handles privacy only during a user request and does not reliably protect generated responses. D duplicates knowledge bases and adds synchronization and authorization complexity, with more opportunities for the original PII to be routed incorrectly.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option would redact the source for every user, and Macie is not itself a write-back redaction tool.
- **B:** This option handles privacy only during a user request and does not reliably protect generated responses.
- **C:** Correct. This choice is part of answer **C** and directly implements the decisive mechanism for knowledge-base retention, synchronization, and user-group-specific redaction.
- **D:** This option duplicates knowledge bases and adds synchronization and authorization complexity, with more opportunities for the original PII to be routed incorrectly.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is knowledge-base retention, synchronization, and user-group-specific redaction; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 95

### 中文

#### 考点背景

这题考查**技术与业务信号的统一可视化、关联告警和异常检测**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。CloudWatch Custom Dashboard 可以集中展示技术指标和导入的业务指标；Composite Alarm 能关联多个信号，Anomaly Detection 能识别偏离正常模式的退化，SNS 则负责自动通知 Stakeholder。A 缺少异常检测，难以识别非固定阈值的退化；B 增加自动修复并非要求，且引入 Grafana 和 Lambda 运维；C 经过 S3 和 QuickSight 的链路更偏离实时告警，EventBridge 阈值规则也不能替代 CloudWatch 的关联告警能力。

#### 场景比喻

运维中心像医院监护站：Dashboard 把延迟、错误和业务质量放在一屏，Composite Alarm 只有在多个生命体征共同异常时升级，Anomaly Detection 能发现偏离患者日常基线的变化，SNS 负责呼叫值班人员。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon SNS | 自动呼叫器 | 把告警及时通知给 Stakeholder 和值班团队 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【技术与业务信号的统一可视化、关联告警和异常检测】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。CloudWatch Custom Dashboard 可以集中展示技术指标和导入的业务指标；Composite Alarm 能关联多个信号，Anomaly Detection 能识别偏离正常模式的退化，SNS 则负责自动通知 Stakeholder。A 缺少异常检测，难以识别非固定阈值的退化；B 增加自动修复并非要求，且引入 Grafana 和 Lambda 运维；C 经过 S3 和 QuickSight 的链路更偏离实时告警，EventBridge 阈值规则也不能替代 CloudWatch 的关联告警能力。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项缺少异常检测，难以识别非固定阈值的退化。
- **B：** 该项增加自动修复并非要求，且引入 Grafana 和 Lambda 运维。
- **C：** 该项经过 S3 和 QuickSight 的链路更偏离实时告警，EventBridge 阈值规则也不能替代 CloudWatch 的关联告警能力。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【技术与业务信号的统一可视化、关联告警和异常检测】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【技术与业务信号的统一可视化、关联告警和异常检测】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **unified technical-business visibility, correlated alarms, and anomaly detection**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. A CloudWatch custom dashboard can present technical and imported business metrics together. Composite alarms correlate multiple signals, anomaly detection identifies degradation that is not limited to a fixed threshold, and SNS automatically notifies stakeholders. A lacks anomaly detection and is less able to identify non-fixed-pattern degradation. B adds unrequested automated remediation and the operational burden of Grafana and Lambda. C routes metrics through S3 and QuickSight, which is less suitable for timely operational alerting, and its EventBridge threshold rule does not replace CloudWatch correlated alarms.

#### Analogy

The operations center is a hospital monitor. A dashboard combines latency, errors, and business quality; composite alarms correlate vital signs, anomaly detection notices deviation from baseline, and SNS calls the on-duty team.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon SNS | Automated pager | Notifies stakeholders and on-call teams when alarms fire |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver unified technical-business visibility, correlated alarms, and anomaly detection, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. A CloudWatch custom dashboard can present technical and imported business metrics together. Composite alarms correlate multiple signals, anomaly detection identifies degradation that is not limited to a fixed threshold, and SNS automatically notifies stakeholders. A lacks anomaly detection and is less able to identify non-fixed-pattern degradation. B adds unrequested automated remediation and the operational burden of Grafana and Lambda. C routes metrics through S3 and QuickSight, which is less suitable for timely operational alerting, and its EventBridge threshold rule does not replace CloudWatch correlated alarms.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option lacks anomaly detection and is less able to identify non-fixed-pattern degradation.
- **B:** This option adds unrequested automated remediation and the operational burden of Grafana and Lambda.
- **C:** This option routes metrics through S3 and QuickSight, which is less suitable for timely operational alerting, and its EventBridge threshold rule does not replace CloudWatch correlated alarms.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for unified technical-business visibility, correlated alarms, and anomaly detection.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is unified technical-business visibility, correlated alarms, and anomaly detection; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 96

### 中文

#### 考点背景

这题考查**SCP 约束下的欧洲跨区域推理与 PrivateLink 私网访问**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 C。欧洲 Cross-Region Inference Profile 可以把请求路由到该 FM 实际可用的欧洲 Region；配合 Bedrock VPC Endpoint 和更新后的 SCP，应用可以通过私有网络调用，并只允许所需的欧洲 Inference Profile 与目标 Region。A 把 Lambda 放在一个批准 Region，却试图跨 Region 使用模型 Endpoint，不能保证符合 SCP 和模型所在 Region 的私有访问要求；B 改为在 EC2 自托管模型，已不是访问选定的 Bedrock FM；D 把模型迁移到 SageMaker，改变了服务和模型部署方案，也不能满足使用该 Bedrock FM 的要求。

#### 场景比喻

企业网络像只开放欧洲内部航线的机场：SCP 是航线禁飞表，欧洲 Inference Profile 选择有该模型的获准机场，VPC Endpoint 是不经过公共候机厅的专用通道。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Cross-Region Inference Profile | 区域容量调度中心 | 在批准的地理边界内分散推理请求与配额压力 |
| VPC Endpoint | AWS 私有走廊 | 让服务调用留在 AWS 私网并配合 Endpoint Policy 控制访问 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【SCP 约束下的欧洲跨区域推理与 PrivateLink 私网访问】。
2. **再核对正确组合：** 正确答案是 **C**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 C。欧洲 Cross-Region Inference Profile 可以把请求路由到该 FM 实际可用的欧洲 Region；配合 Bedrock VPC Endpoint 和更新后的 SCP，应用可以通过私有网络调用，并只允许所需的欧洲 Inference Profile 与目标 Region。A 把 Lambda 放在一个批准 Region，却试图跨 Region 使用模型 Endpoint，不能保证符合 SCP 和模型所在 Region 的私有访问要求；B 改为在 EC2 自托管模型，已不是访问选定的 Bedrock FM；D 把模型迁移到 SageMaker，改变了服务和模型部署方案，也不能满足使用该 Bedrock FM 的要求。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项把 Lambda 放在一个批准 Region，却试图跨 Region 使用模型 Endpoint，不能保证符合 SCP 和模型所在 Region 的私有访问要求。
- **B：** 该项改为在 EC2 自托管模型，已不是访问选定的 Bedrock FM。
- **C：** 正确。该项是答案 **C** 的组成部分，直接落实【SCP 约束下的欧洲跨区域推理与 PrivateLink 私网访问】的关键机制。
- **D：** 该项把模型迁移到 SageMaker，改变了服务和模型部署方案，也不能满足使用该 Bedrock FM 的要求。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【SCP 约束下的欧洲跨区域推理与 PrivateLink 私网访问】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **European cross-Region inference and PrivateLink access under SCP constraints**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is C. A Europe cross-Region inference profile can route requests to European Regions where the selected FM is actually available. Combined with a Bedrock VPC endpoint and updated SCPs, the application can use private connectivity while allowing only the required European inference profile and destination Regions. A places Lambda in an allowed Region but attempts to reach model endpoints in other Regions, so it does not reliably satisfy the SCP and private-access constraints. B self-hosts a different deployment on EC2 instead of using the selected Bedrock FM. D moves the model to SageMaker and changes the required service and deployment architecture.

#### Analogy

The enterprise network is an airport limited to European routes. The SCP is the no-fly list, the European inference profile selects an approved airport where the model exists, and the VPC endpoint is a private corridor that avoids the public terminal.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Cross-Region Inference Profile | Regional capacity dispatcher | Spreads inference demand and quota pressure within an approved geography |
| VPC Endpoint | Private AWS corridor | Keeps service calls on private AWS networking with endpoint policy controls |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver European cross-Region inference and PrivateLink access under SCP constraints, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **C**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is C. A Europe cross-Region inference profile can route requests to European Regions where the selected FM is actually available. Combined with a Bedrock VPC endpoint and updated SCPs, the application can use private connectivity while allowing only the required European inference profile and destination Regions. A places Lambda in an allowed Region but attempts to reach model endpoints in other Regions, so it does not reliably satisfy the SCP and private-access constraints. B self-hosts a different deployment on EC2 instead of using the selected Bedrock FM. D moves the model to SageMaker and changes the required service and deployment architecture.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option places Lambda in an allowed Region but attempts to reach model endpoints in other Regions, so it does not reliably satisfy the SCP and private-access constraints.
- **B:** This option self-hosts a different deployment on EC2 instead of using the selected Bedrock FM.
- **C:** Correct. This choice is part of answer **C** and directly implements the decisive mechanism for European cross-Region inference and PrivateLink access under SCP constraints.
- **D:** This option moves the model to SageMaker and changes the required service and deployment architecture.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is European cross-Region inference and PrivateLink access under SCP constraints; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 97

### 中文

#### 考点背景

这题考查**复杂问题的查询分解、降低语义稀释与托管 RAG 编排**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。Knowledge Base 的 Query Decomposition 能把复杂问题拆成更聚焦的检索子问题，降低 Semantic Dilution；结合领域文档和 Bedrock Flow，可以在托管服务中完成检索与生成编排，并以较低运维开销扩展到所需吞吐。A、C、D 分别引入自定义多模型、SageMaker Endpoint、多个 Lambda 或 Agent 高级能力，增加延迟、成本和维护工作；其中 D 的 Deep Research/Reasoning 并不是解决该检索要求的必要控制。

#### 场景比喻

复杂问题像一张写了五种商品的采购单：一次拿整张单去仓库容易什么都找不准；Query Decomposition 把它拆成小清单分别检索，Bedrock Flow 再把结果装回一个包裹。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【复杂问题的查询分解、降低语义稀释与托管 RAG 编排】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。Knowledge Base 的 Query Decomposition 能把复杂问题拆成更聚焦的检索子问题，降低 Semantic Dilution；结合领域文档和 Bedrock Flow，可以在托管服务中完成检索与生成编排，并以较低运维开销扩展到所需吞吐。A、C、D 分别引入自定义多模型、SageMaker Endpoint、多个 Lambda 或 Agent 高级能力，增加延迟、成本和维护工作；其中 D 的 Deep Research/Reasoning 并不是解决该检索要求的必要控制。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项分别引入自定义多模型、SageMaker Endpoint、多个 Lambda 或 Agent 高级能力，增加延迟、成本和维护工作。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【复杂问题的查询分解、降低语义稀释与托管 RAG 编排】的关键机制。
- **C：** 该项分别引入自定义多模型、SageMaker Endpoint、多个 Lambda 或 Agent 高级能力，增加延迟、成本和维护工作。
- **D：** 该项分别引入自定义多模型、SageMaker Endpoint、多个 Lambda 或 Agent 高级能力，增加延迟、成本和维护工作。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【复杂问题的查询分解、降低语义稀释与托管 RAG 编排】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **query decomposition, reduced semantic dilution, and managed RAG orchestration**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. Knowledge Base query decomposition breaks a complex question into focused retrieval subqueries, reducing semantic dilution. Combined with domain documents and a Bedrock Flow, it provides managed retrieval-and-generation orchestration that can scale with less operational work. A, C, and D introduce custom multi-model logic, a SageMaker endpoint, multiple Lambda functions, or extra agent capabilities, increasing latency, cost, and maintenance. Deep research and reasoning in D are not necessary controls for this retrieval requirement.

#### Analogy

A complex question is a shopping list containing five unrelated items. Searching with the whole list dilutes intent; query decomposition creates focused sublists, and Bedrock Flow assembles the retrieved results into one answer.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver query decomposition, reduced semantic dilution, and managed RAG orchestration, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. Knowledge Base query decomposition breaks a complex question into focused retrieval subqueries, reducing semantic dilution. Combined with domain documents and a Bedrock Flow, it provides managed retrieval-and-generation orchestration that can scale with less operational work. A, C, and D introduce custom multi-model logic, a SageMaker endpoint, multiple Lambda functions, or extra agent capabilities, increasing latency, cost, and maintenance. Deep research and reasoning in D are not necessary controls for this retrieval requirement.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option proposes [Use Amazon API Gateway to route incoming queries to an Amazon Bedrock agent. Configure the agent to use an Anthropic Claude model to decompose queries…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for query decomposition, reduced semantic dilution, and managed RAG orchestration.
- **C:** This option proposes [Use Amazon SageMaker Al to host custom ML models for both query decomposition and query expansion. Configure Amazon Bedrock knowledge bases to store t…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.
- **D:** This option proposes [Create an Amazon Bedrock agent to orchestrate multiple AWS Lambda functions to decompose queries. Create an Amazon Bedrock knowledge base to store the…], but it does not close every constraint in the stem. Compared with **B**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is query decomposition, reduced semantic dilution, and managed RAG orchestration; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 98

### 中文

#### 考点背景

这题考查**Prompt 与模型质量的自动评估以及 CI/CD 发布门禁**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 B。Bedrock Evaluation Job 可以用自定义 Prompt Dataset 比较多个 Prompt Template 或模型配置，并生成量化质量指标；在需要时可配置人工评估，让审核员反馈响应。把 Evaluation Job 接入 CodePipeline，并以质量阈值作为部署门禁，可以自动阻止不合格配置发布。A 的人工每日审核不是每次更新的自动门禁；C 只监控延迟和错误率；D 只比较情感分数，不能覆盖相关性、事实性和整体响应质量，也不能替代系统化人工评估。

#### 场景比喻

每次改 Prompt 都像发布新版教材：Evaluation Job 用固定试卷量化测试，必要时让人工阅卷；CodePipeline 是校门，分数没达到阈值就不允许新版进入生产课堂。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock Evaluation | 标准化阅卷室 | 用数据集、指标或人工评审比较模型和 Prompt 质量 |
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【Prompt 与模型质量的自动评估以及 CI/CD 发布门禁】。
2. **再核对正确组合：** 正确答案是 **B**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 B。Bedrock Evaluation Job 可以用自定义 Prompt Dataset 比较多个 Prompt Template 或模型配置，并生成量化质量指标；在需要时可配置人工评估，让审核员反馈响应。把 Evaluation Job 接入 CodePipeline，并以质量阈值作为部署门禁，可以自动阻止不合格配置发布。A 的人工每日审核不是每次更新的自动门禁；C 只监控延迟和错误率；D 只比较情感分数，不能覆盖相关性、事实性和整体响应质量，也不能替代系统化人工评估。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的人工每日审核不是每次更新的自动门禁。
- **B：** 正确。该项是答案 **B** 的组成部分，直接落实【Prompt 与模型质量的自动评估以及 CI/CD 发布门禁】的关键机制。
- **C：** 该项只监控延迟和错误率。
- **D：** 该项只比较情感分数，不能覆盖相关性、事实性和整体响应质量，也不能替代系统化人工评估。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【Prompt 与模型质量的自动评估以及 CI/CD 发布门禁】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **automated prompt/model quality evaluation and CI/CD release gates**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is B. A Bedrock Evaluation Job can compare prompt templates or model configurations with a custom prompt dataset and produce quantitative quality metrics; human evaluation can be configured when reviewers need to provide feedback. Connecting the evaluation job to CodePipeline and using the quality threshold as a deployment gate automatically blocks configurations that do not qualify. A's daily manual review is not an update-by-update automated gate. C measures latency and errors rather than response quality. D uses sentiment as a narrow proxy and cannot cover relevance, factuality, and overall quality or replace systematic human evaluation.

#### Analogy

Every prompt change is a new textbook edition. An Evaluation Job runs a fixed exam and can add human graders; CodePipeline is the school gate that blocks an edition whose score misses the threshold.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock Evaluation | Standard grading room | Compares model and prompt quality with datasets, metrics, or human review |
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver automated prompt/model quality evaluation and CI/CD release gates, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **B**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is B. A Bedrock Evaluation Job can compare prompt templates or model configurations with a custom prompt dataset and produce quantitative quality metrics; human evaluation can be configured when reviewers need to provide feedback. Connecting the evaluation job to CodePipeline and using the quality threshold as a deployment gate automatically blocks configurations that do not qualify. A's daily manual review is not an update-by-update automated gate. C measures latency and errors rather than response quality. D uses sentiment as a narrow proxy and cannot cover relevance, factuality, and overall quality or replace systematic human evaluation.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option Bedrock Evaluation Job can compare prompt templates or model configurations with a custom prompt dataset and produce quantitative quality metrics; human evaluation can be configured when reviewers need to provide feedback.
- **B:** Correct. This choice is part of answer **B** and directly implements the decisive mechanism for automated prompt/model quality evaluation and CI/CD release gates.
- **C:** This option measures latency and errors rather than response quality.
- **D:** This option uses sentiment as a narrow proxy and cannot cover relevance, factuality, and overall quality or replace systematic human evaluation.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is automated prompt/model quality evaluation and CI/CD release gates; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 99

### 中文

#### 考点背景

这题考查**用 Prompt 输出指示约束必需文案及其保证边界**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 D。Output Indicator 可以在 Prompt 中明确输出格式和必需内容，为模型提供清晰的结构约束，使促销信息更稳定、自然地出现在推荐文本中。在给定选项中这是最直接的 Prompt 控制；如果业务要求真正的绝对保证，仍应在应用层做模板化插入或生成后校验重试。A 的 Guardrails 主要负责检测、阻止或处理内容，不负责自然地插入指定文案；B 只能从候选中选择较相关的文本，不能保证每个候选都包含文案；C 增加 Agent 和重排流程，仍无法提供硬保证。

#### 场景比喻

模型像广告文案写手：Output Indicator 是明确的版式清单，要求每份推荐都包含促销语并放在指定位置。Guardrail 像审核员，只会拦违规内容，不会替写手自然补上一句广告。真正零遗漏还要应用层复核。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Amazon Bedrock | 托管模型厨房 | 提供 Foundation Model 推理及其治理能力 |
| AWS Identity and Access Management | 身份与权限门禁 | 用 Role、Policy 和条件键执行最小权限 |
| Amazon CloudWatch | 实时监控室 | 汇总指标、Dashboard、异常检测和告警 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【用 Prompt 输出指示约束必需文案及其保证边界】。
2. **再核对正确组合：** 正确答案是 **D**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 D。Output Indicator 可以在 Prompt 中明确输出格式和必需内容，为模型提供清晰的结构约束，使促销信息更稳定、自然地出现在推荐文本中。在给定选项中这是最直接的 Prompt 控制；如果业务要求真正的绝对保证，仍应在应用层做模板化插入或生成后校验重试。A 的 Guardrails 主要负责检测、阻止或处理内容，不负责自然地插入指定文案；B 只能从候选中选择较相关的文本，不能保证每个候选都包含文案；C 增加 Agent 和重排流程，仍无法提供硬保证。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 该项的 Guardrails 主要负责检测、阻止或处理内容，不负责自然地插入指定文案。
- **B：** 该项只能从候选中选择较相关的文本，不能保证每个候选都包含文案。
- **C：** 该项增加 Agent 和重排流程，仍无法提供硬保证。
- **D：** 正确。该项是答案 **D** 的组成部分，直接落实【用 Prompt 输出指示约束必需文案及其保证边界】的关键机制。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【用 Prompt 输出指示约束必需文案及其保证边界】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **using prompt output indicators for required copy and understanding guarantee boundaries**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is D. An output indicator makes the required format and content explicit in the prompt, giving the model a clearer structural constraint so the promotional message appears more consistently and naturally. Among the choices, this is the most direct prompt-level control; a true absolute guarantee would still require deterministic application-level insertion or post-generation validation and retry. A Guardrails filter primarily detects, blocks, or handles content and does not naturally insert the required copy. B only selects among candidates and cannot ensure that every candidate contains the message. C adds agent and reranking steps without providing a hard guarantee.

#### Analogy

The model is an advertising copywriter. An output indicator is the layout checklist requiring the promotion in every response. A guardrail is a reviewer that blocks violations, not a writer that inserts copy; an absolute guarantee still needs application validation.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Amazon Bedrock | Managed model kitchen | Provides foundation-model inference and its governance capabilities |
| AWS Identity and Access Management | Identity checkpoint | Enforces least privilege through roles, policies, and condition keys |
| Amazon CloudWatch | Operations control room | Combines metrics, dashboards, anomaly detection, and alarms |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver using prompt output indicators for required copy and understanding guarantee boundaries, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **D**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is D. An output indicator makes the required format and content explicit in the prompt, giving the model a clearer structural constraint so the promotional message appears more consistently and naturally. Among the choices, this is the most direct prompt-level control; a true absolute guarantee would still require deterministic application-level insertion or post-generation validation and retry. A Guardrails filter primarily detects, blocks, or handles content and does not naturally insert the required copy. B only selects among candidates and cannot ensure that every candidate contains the message. C adds agent and reranking steps without providing a hard guarantee.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** This option Guardrails filter primarily detects, blocks, or handles content and does not naturally insert the required copy.
- **B:** This option only selects among candidates and cannot ensure that every candidate contains the message.
- **C:** This option adds agent and reranking steps without providing a hard guarantee.
- **D:** Correct. This choice is part of answer **D** and directly implements the decisive mechanism for using prompt output indicators for required copy and understanding guarantee boundaries.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is using prompt output indicators for required copy and understanding guarantee boundaries; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---

## Question 100

### 中文

#### 考点背景

这题考查**企业联合身份、按部门模型授权、私网调用与完整模型审计**。专业级考试不会只问某个服务【能不能做】，而是要求同时核对功能边界、数据或安全约束、故障行为，以及题干强调的成本或运维目标。正确方案必须逐条闭环，不能只命中一个熟悉的服务名。

正确答案为 A。SAML Federation 让 Microsoft Entra ID 用户通过 IAM Role 访问 AWS；部门专属 Role 可用 `ModelId` 条件限制模型家族。Bedrock Runtime 的 PrivateLink Interface VPC Endpoint 保证 API 调用走私有路径，CloudTrail 记录 API 活动，Model Invocation Logging 保存详细的 Prompt-Response 交互，形成完整审计。B 通过 NAT 访问 Public Endpoint，违反私有路径要求；C 明确使用 Public Endpoint，VPC Routing 不能把它变成 PrivateLink；D 的 SCP 按账户或组织层级控制，不能可靠按部门用户属性实施细粒度授权，Entra 日志也不包含完整模型交互。

#### 场景比喻

把 Bedrock 看成企业实验室：Entra SAML 是公司门禁，部门 IAM Role 决定可进入哪间模型实验室，PrivateLink 是内部走廊，CloudTrail 记录开门动作，Invocation Logging 保存实验输入和输出。

#### AWS 服务角色

| AWS 服务 | 角色比喻 | 在本题中的作用 |
|---|---|---|
| Model Invocation Logging | 对话黑匣子 | 保存模型请求与响应细节用于调查和审计 |
| IAM Role | 可审计员工证 | 为工作负载和联合用户提供一致的临时 AWS 身份 |
| AWS CloudTrail | API 与数据访问登记簿 | 记录主体、时间、操作和受访问资源以供审计 |
| VPC Endpoint | AWS 私有走廊 | 让服务调用留在 AWS 私网并配合 Endpoint Policy 控制访问 |

#### 正确答案与推理

1. **先锁定核心矛盾：** 本题不是单纯寻找可用服务，而是要解决【企业联合身份、按部门模型授权、私网调用与完整模型审计】。
2. **再核对正确组合：** 正确答案是 **A**。逐项把正确选项映射到题干的功能、边界与非功能要求，不能遗漏数据驻留、安全、延迟、审计或运维条件。
3. **验证 AWS 机制：** 正确答案为 A。SAML Federation 让 Microsoft Entra ID 用户通过 IAM Role 访问 AWS；部门专属 Role 可用 `ModelId` 条件限制模型家族。Bedrock Runtime 的 PrivateLink Interface VPC Endpoint 保证 API 调用走私有路径，CloudTrail 记录 API 活动，Model Invocation Logging 保存详细的 Prompt-Response 交互，形成完整审计。B 通过 NAT 访问 Public Endpoint，违反私有路径要求；C 明确使用 Public Endpoint，VPC Routing 不能把它变成 PrivateLink；D 的 SCP 按账户或组织层级控制，不能可靠按部门用户属性实施细粒度授权，Entra 日志也不包含完整模型交互。
4. **做反向检查：** 如果删掉正确方案中的关键托管能力，题目至少会留下一个未闭环条件；而错误项往往是能完成局部工作，却引入自定义组件、错误服务边界或新的合规风险。

#### 逐项排除

- **A：** 正确。该项是答案 **A** 的组成部分，直接落实【企业联合身份、按部门模型授权、私网调用与完整模型审计】的关键机制。
- **B：** 该项通过 NAT 访问 Public Endpoint，违反私有路径要求。
- **C：** 该项明确使用 Public Endpoint，VPC Routing 不能把它变成 PrivateLink。
- **D：** 该项的 SCP 按账户或组织层级控制，不能可靠按部门用户属性实施细粒度授权，Entra 日志也不包含完整模型交互。

#### 解题方法

先把题干拆成三栏：**要实现什么**、**绝不能违反什么**、**要优化什么**。本题第一栏是【企业联合身份、按部门模型授权、私网调用与完整模型审计】，第二栏通常是 Region、身份、安全、审计或质量边界，第三栏则是延迟、成本和运维量。逐项检查是否三栏全部打勾；看到【最低运维】优先考虑 AWS 原生托管集成，看到【必须、始终、不得】则优先验证可强制执行的策略边界，而不是 Prompt 约定或人工流程。

### English

#### Exam focus and background

This question tests **enterprise federation, department-based model authorization, private invocation, and complete model auditing**. A professional-level AWS item is rarely asking only whether a service can perform one action. The winning architecture must close every functional, security or residency, failure-behavior, and cost or operational requirement in the stem. A familiar service name is not enough when another stated constraint remains open.

The correct answer is A. SAML federation lets Microsoft Entra ID users access AWS through IAM roles, and department-specific roles can restrict model families with `ModelId` conditions. A Bedrock Runtime PrivateLink interface VPC endpoint keeps API calls on private paths, while CloudTrail records API activity and model invocation logging retains detailed prompt-response interactions for a complete audit trail. B reaches a public endpoint through NAT and violates the private-path requirement. C explicitly uses a public endpoint; VPC routing does not turn it into PrivateLink. D's SCPs operate at account or organization scope rather than reliably enforcing user-department authorization, and Entra logs do not contain the full model interaction.

#### Analogy

Bedrock is an enterprise lab. Entra SAML is the corporate badge, department IAM roles select the permitted model room, PrivateLink is the internal corridor, CloudTrail records door operations, and invocation logging preserves experiment inputs and outputs.

#### AWS service roles

| AWS service | Analogy role | Role in this question |
|---|---|---|
| Model Invocation Logging | Conversation black box | Retains detailed model requests and responses for investigation and audit |
| IAM Role | Auditable employee badge | Provides consistent temporary AWS identity to workloads and federated users |
| AWS CloudTrail | API and data-access ledger | Records actor, time, action, and affected resource for audits |
| VPC Endpoint | Private AWS corridor | Keeps service calls on private AWS networking with endpoint policy controls |

#### Correct answer and reasoning

1. **Identify the central conflict:** the design must deliver enterprise federation, department-based model authorization, private invocation, and complete model auditing, not merely invoke a model successfully.
2. **Map the selected choice:** the correct answer is **A**. Each selected component must satisfy a named functional, boundary, and non-functional requirement from the stem.
3. **Verify the AWS mechanism:** The correct answer is A. SAML federation lets Microsoft Entra ID users access AWS through IAM roles, and department-specific roles can restrict model families with `ModelId` conditions. A Bedrock Runtime PrivateLink interface VPC endpoint keeps API calls on private paths, while CloudTrail records API activity and model invocation logging retains detailed prompt-response interactions for a complete audit trail. B reaches a public endpoint through NAT and violates the private-path requirement. C explicitly uses a public endpoint; VPC routing does not turn it into PrivateLink. D's SCPs operate at account or organization scope rather than reliably enforcing user-department authorization, and Entra logs do not contain the full model interaction.
4. **Run a negative check:** removing the decisive managed capability leaves at least one requirement unsatisfied. Distractors usually solve only a local symptom, place control at the wrong boundary, or add custom operations and new failure modes.

#### Option-by-option elimination

- **A:** Correct. This choice is part of answer **A** and directly implements the decisive mechanism for enterprise federation, department-based model authorization, private invocation, and complete model auditing.
- **B:** This option reaches a public endpoint through NAT and violates the private-path requirement.
- **C:** This option explicitly uses a public endpoint; VPC routing does not turn it into PrivateLink.
- **D:** This option proposes [Configure OpenID Connect (OIDC) federation between Microsoft Entra ID and IAM. Use attribute-based access control to map department attributes to spec…], but it does not close every constraint in the stem. Compared with **A**, it omits the enforceable managed boundary described above or introduces unnecessary custom operations and risk.

#### Exam strategy

Divide the stem into three columns: **required outcome**, **hard boundary**, and **optimization target**. Here the first column is enterprise federation, department-based model authorization, private invocation, and complete model auditing; the second is usually a Region, identity, safety, audit, or quality boundary; the third is latency, cost, or operational effort. Reject any option that leaves one column unchecked. For [least operations], prefer a native managed integration. For words such as [must], [always], or [must not], look for an enforceable policy or service boundary instead of a prompt convention or manual process.

---
