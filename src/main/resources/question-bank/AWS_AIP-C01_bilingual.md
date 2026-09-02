# AWS Certified Generative AI Developer - Professional（AIP-C01）中英双语版

> 中文与 English 题干、选项按题号配对；正确答案和社区投票数据沿用原始题库。
> 语言切换由 CertForge 网页控制，Markdown 本身保留两种语言，便于离线阅读和维护。

---

## Question 1 - Topic 1

### 中文

一家零售公司拥有一个使用 Amazon Bedrock 的生成式 AI（GenAI）商品推荐应用。该应用会根据客户的浏览历史和人口统计特征向客户推荐商品。公司需要针对多个不同的人口群体实施公平性评估，以检测并衡量两种 Prompt 方案在推荐结果上的偏差。公司希望实时收集并监控公平性指标。当不同人口群体之间的公平性指标差异超过 15% 时，公司必须收到告警。公司还必须每周收到报告，用于比较两种 Prompt 方案的表现。哪种解决方案能以**最少的自定义开发工作量**满足这些要求？

- **A.** 配置 Amazon CloudWatch Dashboard，展示 Amazon Bedrock API 调用的默认指标。根据模型输出创建自定义指标。设置 Amazon EventBridge 规则来调用 AWS Lambda 函数，对模型响应执行后处理分析，并发布自定义公平性指标。
- **B.** 在 Amazon Bedrock Prompt Management 中创建两个 Prompt 变体。使用 Amazon Bedrock Flows 按预先定义的流量比例部署这些 Prompt 变体。配置带有内容过滤器的 Amazon Bedrock Guardrails 来监控不同人口群体之间的公平性。针对 `GuardrailContentSource` 维度设置 Amazon CloudWatch 告警，并使用 `InvocationsIntervened` 指标来检测推荐差异是否超过阈值。
- **C.** 配置 Amazon SageMaker Clarify 来分析模型输出。将公平性指标发布到 Amazon CloudWatch。创建 CloudWatch 复合告警，将 SageMaker Clarify 的偏差指标与 Amazon Bedrock 的延迟指标结合起来，提供完整的公平性评估 Dashboard。 **（最高票）**
- **D.** 创建 Amazon Bedrock 模型评估任务，比较两个 Prompt 变体之间的公平性。启用 Amazon CloudWatch 中的模型调用日志记录。针对每个人口群体分别设置维度，并为 `InvocationsIntervened` 指标创建 CloudWatch 告警。

### English

A retail company has a generative AI (GenAI) product recommendation application that uses Amazon Bedrock. The application suggests products to customers based on browsing history and demographics. The company needs to implement fairness evaluation across multiple demographic groups to detect and measure bias in recommendations between two prompt approaches. The company wants to collect and monitor fairness metrics in real time. The company must receive an alert if the fairness metrics show a discrepancy of more than 15% between demographic groups. The company must receive weekly reports that compare the performance of the two prompt approaches. Which solution will meet these requirements with the LEAST custom development effort?

- **A.** Configure an Amazon CloudWatch dashboard to display default metrics from Amazon Bedrock API calls. Create custom metrics based on model outputs. Set up Amazon EventBridge rules to invoke AWS lambda functions that perform post-processing analysis on model responses and publish custom fairness metrics.
- **B.** Create the two prompt variants in Amazon Bedrock Prompt Management. Use Amazon Bedrock Flows to deploy the prompt variants with defined traffic allocation. Configure Amazon Bedrock guardrails that have content filters to monitor demographic fairness. Set up Amazon CloudWatch alarms on the GuardrailContentSource dimension that use InvocationsIntervened metrics to detect recommendation discrepancy threshold violations.
- **C.** Set up Amazon SageMaker Clarify to analyze model outputs. Publish fairness metrics to Amazon CloudWatch. Create CloudWatch composite alarms that combine SageMaker Clarify bias metrics with Amazon Bedrock latency metrics to provide a comprehensive fairness evaluation dashboard. **(Most Voted)**
- **D.** Create an Amazon Bedrock model evaluation job to compare fairness between the two prompt variants. Enable model invocation logging in Amazon CloudWatch. Set up CloudWatch alarms for InvocationsIntervened metrics with a dimension for each demographic group.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (58%)
- D (19%)
- A (17%)

---

## Question 2 - Topic 1

### 中文

一家金融公司正在开发一款 AI 助手，帮助客户规划投资并管理投资组合。公司识别出多种高风险对话模式，例如用户要求推荐具体股票，或者要求保证投资回报。如果公司不能实施适当的控制措施，这些高风险对话可能导致违反监管要求。公司必须确保 AI 助手不会提供不适当的金融建议，不会生成有关竞争对手的内容，也不会做出缺乏公司已批准金融指导依据的事实性声明。公司希望使用 Amazon Bedrock Guardrails 来实现解决方案。以下哪三个步骤组合能够满足要求？（选择三项。）

- **A.** 将这些高风险对话模式添加到 Guardrail 的 Denied Topics（拒绝主题）中。 **（最高票）**
- **B.** 配置内容过滤 Guardrail，对包含这些高风险对话模式的 Prompt 进行过滤。
- **C.** 配置内容过滤 Guardrail，对包含竞争对手名称的 Prompt 进行过滤。
- **D.** 将竞争对手名称添加为自定义 Word Filters（词语过滤器），并将输入和输出动作都设置为 Block。 **（最高票）**
- **E.** 设置较低的 Grounding Score（事实依据评分）阈值。
- **F.** 设置较高的 Grounding Score（事实依据评分）阈值。 **（最高票）**

### English

A finance company is developing an AI assistant to help clients plan investments and manage their portfolios. The company identifies several high-risk conversation patterns such as requests for specific stock recommendations or guaranteed returns. High-risk conversation patterns could lead to regulatory violations if the company cannot implement appropriate controls. The company must ensure that the AI assistant does not provide inappropriate financial advice, generate content about competitors, or make claims that are not factually grounded in the company's approved financial guidance. The company wants to use Amazon Bedrock Guardrails to implement a solution. Which combination of steps will meet these requirements? (Choose three.)

- **A.** Add the high-risk conversation patterns to a denied topics guardrail. **(Most Voted)**
- **B.** Configure a content filter guardrail to filter prompts that contain the high-risk conversation patterns.
- **C.** Configure a content filter guardrail to filter prompts that contain competitor names.
- **D.** Add the names of competitors as custom word filters. Set the input and output actions to block. **(Most Voted)**
- **E.** Set a low grounding score threshold.
- **F.** Set a high grounding score threshold. **(Most Voted)**

**Correct Answer / 正确答案:** `ADF`

**Community vote distribution / 社区投票分布:**

- ADF (100%)

---

## Question 3 - Topic 1

### 中文

一家公司将 AI 助手部署为 React 应用，该应用使用 AWS Amplify、AWS AppSync GraphQL API 和 Amazon Bedrock Knowledge Bases。应用通过 GraphQL API 调用 Amazon Bedrock `RetrieveAndGenerate` API 与知识库交互。公司将 AWS Lambda Resolver 配置为使用 `RequestResponse` 调用类型。应用用户经常遇到超时和响应缓慢的问题，而且当问题较复杂、需要更长处理时间时，这些问题更加频繁。公司需要解决这些性能问题并改善用户体验。哪种解决方案能够满足要求？

- **A.** 使用 AWS Amplify AI Kit，让 GraphQL API 支持流式响应，并优化客户端渲染。 **（最高票）**
- **B.** 增大 Lambda Resolver 的超时时间，并实现带指数退避的重试逻辑。
- **C.** 更新应用，将 API 请求发送到 Amazon SQS 队列。更新 AWS AppSync Resolver，使其轮询并处理该队列。
- **D.** 将 `RetrieveAndGenerate` API 改为 `InvokeModelWithResponseStream` API，并更新应用，使用 Amazon API Gateway WebSocket API 支持流式响应。

### English

A company has deployed an AI assistant as a React application that uses AWS Amplify, an AWS AppSync GraphQL API, and Amazon Bedrock Knowledge Bases. The application uses the GraphQL API to call the Amazon Bedrock RetrieveAndGenerate API for knowledge base interactions. The company configures an AWS Lambda resolver to use the RequestResponse invocation type. Application users report frequent timeouts and slow response times. Users report these problems more frequently for complex questions that require longer processing. The company needs a solution to fix these performance issues and enhance the user experience. Which solution will meet these requirements?

- **A.** Use AWS Amplify AI Kit to implement streaming responses from the GraphQL API and to optimize client-side rendering. **(Most Voted)**
- **B.** Increase the timeout value of the Lambda resolver. Implement retry logic with exponential backoff.
- **C.** Update the application to send an API request to an Amazon SQS queue. Update the AWS AppSync resolver to poll and process the queue.
- **D.** Change the RetrieveAndGenerate API to the InvokeModelWithResponseStream API. Update the application to use an Amazon API Gateway WebSocket API to support the streaming response.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (67%)
- D (33%)

---

## Question 4 - Topic 1

### 中文

一家电商公司运营全球商品推荐系统。该系统需要根据法规、成本优化和性能要求，在 Amazon Bedrock 的多个 Foundation Model（FM，基础模型）之间进行切换。公司必须根据自身业务逻辑实施自定义控制，包括动态成本阈值、特定 AWS Region 的合规规则，以及多个 FM 之间的实时 A/B 测试。系统必须能够在**无需部署新代码**的情况下切换 FM。系统还必须根据复杂规则路由用户请求，这些规则包括用户等级、交易金额、监管区域，以及每小时都会变化的实时成本指标；规则发生变化后，必须立即传播到数千个并发请求。哪种解决方案能够满足这些要求？

- **A.** 部署一个 AWS Lambda 函数，使用环境变量存储路由规则和 Amazon Bedrock FM ID。当业务需求变化时，在 Lambda 控制台中更新环境变量。配置 Amazon API Gateway REST API 读取请求参数并作出路由决策。
- **B.** 使用 Amazon API Gateway REST API 的请求转换模板，根据请求属性实现路由逻辑。将 Amazon Bedrock FM Endpoint 存储为 REST API Stage Variables，并在系统切换模型时更新这些变量。
- **C.** 配置 AWS Lambda 函数，使其针对每个用户请求从 AWS AppConfig Agent 获取路由配置。由 Lambda 中的业务逻辑为每个请求选择合适的 FM，并通过单一 Amazon API Gateway REST API Endpoint 暴露该模型调用入口。 **（最高票）**
- **D.** 使用 Amazon API Gateway REST API 的 AWS Lambda Authorizer，对存储在 AWS AppConfig 中的路由规则进行评估。根据业务逻辑返回授权上下文，再将请求路由到分别对应不同 Amazon Bedrock FM 的 Lambda 函数。

### English

An ecommerce company operates a global product recommendation system that needs to switch between multiple foundation models (FM) in Amazon Bedrock based on regulations, cost optimization, and performance requirements. The company must apply custom controls based on proprietary business logic, including dynamic cost thresholds, AWS Region-specific compliance rules, and real-time A/B testing across multiple FMs. The system must be able to switch between FMs without deploying new code. The system must route user requests based on complex rules including user tier, transaction value, regulatory zone, and real-time cost metrics that change hourly and require immediate propagation across thousands of concurrent requests. Which solution will meet these requirements?

- **A.** Deploy an AWS Lambda function that uses environment variables to store routing rules and Amazon Bedrock FM IDs. Use the Lambda console to update the environment variables when business requirements change. Configure an Amazon API Gateway REST API to read request parameters to make routing decisions.
- **B.** Deploy Amazon API Gateway REST API request transformation templates to implement routing logic based on request attributes. Store Amazon Bedrock FM endpoints as REST API stage variables. Update the variables when the system switches between models.
- **C.** Configure an AWS Lambda function to fetch routing configurations from the AWS AppConfig Agent for each user request. Run business logic in the Lambda function to select the appropriate FM for each request. Expose the FM through a single Amazon API Gateway REST API endpoint. **(Most Voted)**
- **D.** Use AWS Lambda authorizers for an Amazon API Gateway REST API to evaluate routing rules that are stored in AWS AppConfig. Return authorization contexts based on business logic. Route requests to model-specific Lambda functions for each Amazon Bedrock FM.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 5 - Topic 1

### 中文

一家公司正在开发一个内部生成式 AI（GenAI）助手，使用 Amazon Bedrock 为多个业务部门总结公司文档。该 GenAI 助手必须以统一格式生成回答，其中包含文档摘要、业务风险分类，以及需要人工复核的标记术语。助手还必须根据用户所属业务部门调整回答语气，例如法务、人力资源或财务部门。助手必须屏蔽仇恨言论、不适当主题，以及个人健康信息等敏感信息。公司需要集中管理不同业务部门和团队使用的 Prompt 变体，并希望尽量减少持续的编排工作和后处理逻辑维护成本。同时，公司还希望以后能够调整该 GenAI 助手的内容审核标准。哪种解决方案能够以**最低维护开销**满足这些要求？

- **A.** 使用 Amazon Bedrock Prompt Management 配置可复用模板，以及针对不同业务部门的 Prompt 变体。应用包含类别过滤器和敏感词列表的 Amazon Bedrock Guardrails，阻止被禁止的内容。 **（最高票）**
- **B.** 使用 Amazon Bedrock Prompt Management 定义基础模板。通过 System Prompt 变量强制使用不同业务部门所需的语气。配置 Amazon Bedrock Guardrails，根据受众调整阈值，并使用内部管理 API 管理 Guardrails。
- **C.** 使用 Amazon Bedrock，并在 API 调用中根据业务部门动态注入指令。将响应格式规则存储在 Amazon DynamoDB 中。使用 AWS Step Functions 验证响应，并在 GenAI 助手生成响应后使用 Amazon Comprehend 应用内容过滤。
- **D.** 使用 Amazon Bedrock，并将自定义 Prompt 模板存储在 Amazon DynamoDB 中。创建一个 AWS Lambda 函数选择对应业务部门的 Prompt，再创建第二个 Lambda 函数调用 Amazon Comprehend，从响应中过滤被禁止的内容。

### English

A company is developing an internal generative AI (GenAI) assistant that uses Amazon Bedrock to summarize corporate documents for multiple business units. The GenAI assistant must generate responses in a consistent format that includes a document summary, classification of business risks, and terms that are flagged for review. The GenAI assistant must adapt the tone of responses for each user's business unit, such as legal, human resources, or finance. The GenAI assistant must block hate speech, inappropriate topics, and sensitive information such as personal health information. The company needs a solution to centrally manage prompt variants across business units and teams. The company wants to minimize ongoing orchestration efforts and maintenance for post-processing logic. The company also wants to have the ability to adjust content moderation criteria for the GenAI assistant over time. Which solution will meet these requirements with the LEAST maintenance overhead?

- **A.** Use Amazon Bedrock Prompt Management to configure reusable templates and business unit-specific prompt variants. Apply Amazon Bedrock guardrails that have category filters and sensitive term lists to block prohibited content. **(Most Voted)**
- **B.** Use Amazon Bedrock Prompt Management to define base templates. Enforce business unit-specific tone by using system prompt variables. Configure Amazon Bedrock guardrails to apply audience-based threshold tuning. Manage the guardrails by using an internal administration API.
- **C.** Use Amazon Bedrock with business unit-based instruction injection in API calls. Store response formatting rules in Amazon DynamoDB. Use AWS Step functions to validate responses. Use Amazon Comprehend to apply content filters after the GenAI assistant generates responses.
- **D.** Use Amazon Bedrock with custom prompt templates that are stored in Amazon DynamoDB. Create one AWS Lambda function to select business unit-specific prompts. Create a second Lambda function to call Amazon Comprehend to filter prohibited content from responses.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 6 - Topic 1

### 中文

一家金融服务公司正在构建客户支持应用。该应用需要根据用户查询的语义相似度，从数据库中检索相关金融监管文档，并与 Amazon Bedrock 集成生成回答。应用必须能够搜索英语、西班牙语和葡萄牙语文档，还必须能够按发布日期、监管机构和文档类型等 Metadata 进行过滤。数据库大约存储了 1,000 万条文档 Embedding。为了降低运维开销，公司希望尽可能减少管理和维护工作，同时应用还必须为实时客户交互提供低延迟响应。哪种解决方案能够满足这些要求？

- **A.** 使用 Amazon OpenSearch Serverless 提供向量搜索和 Metadata 过滤能力，并连接 Amazon Bedrock Knowledge Bases，通过 Anthropic Claude Foundation Model（FM）实现 Retrieval Augmented Generation（RAG，检索增强生成）。 **（最高票）**
- **B.** 部署带有 `pgvector` 扩展的 Amazon Aurora PostgreSQL 数据库。定义表来存储 Embedding 和 Metadata，使用 SQL 查询执行相似度搜索，再将检索到的文档发送到 Amazon Bedrock 生成回答。
- **C.** 使用 Amazon S3 Vectors 配置向量索引和不可过滤的 Metadata 字段，并将 S3 Vectors 与 Amazon Bedrock 集成以实现 RAG。
- **D.** 设置 Amazon Neptune Analytics 图数据库。配置维度合适的向量索引来存储文档 Embedding，并使用 Amazon Bedrock 执行基于图的检索和响应生成。

### English

A financial services company is building a customer support application that retrieves relevant financial regulation documents from a database based on semantic similarities to user queries. The application must integrate with Amazon Bedrock to generate responses. The application must be able to search documents that are in English, Spanish, and Portuguese. The application must filter documents by metadata such as publication date, regulatory agency, and document type. The database stores approximately 10 million document embeddings. To minimize operational overhead, the company wants a solution that minimizes management and maintenance effort. The application must provide low-latency responses for real-time customer interactions. Which solution will meet these requirements?

- **A.** Use Amazon OpenSearch Serverless to provide vector search capabilities and metadata filtering. Connect to Amazon Bedrock Knowledge Bases to enable Retrieval Augmented Generation (RAG) capabilities that use an Anthropic Claude foundation model (FM). **(Most Voted)**
- **B.** Deploy an Amazon Aurora PostgreSQL database with the pgvector extension. Define tables to store embeddings and metadata. Use SQL queries to perform similarity searches. Send retrieved documents to Amazon Bedrock to generate responses.
- **C.** Use Amazon S3 Vectors to configure a vector index and non-filterable metadata fields. Integrate S3 Vectors with Amazon Bedrock to enable Retrieval Augmented Generation (RAG) capabilities.
- **D.** Set up an Amazon Neptune Analytics graph database. Configure a vector index that has appropriate dimensionality to store document embeddings. Use Amazon Bedrock to perform graph-based retrieval and to generate responses.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 7 - Topic 1

### 中文

一家医疗公司正在构建使用 RAG 的生成式 AI（GenAI）应用，用于提供有证据依据的医学信息。该应用使用 Amazon OpenSearch Service 检索向量 Embedding。用户反馈，搜索经常漏掉包含**精确医学术语和缩写**的结果，同时又会返回过多语义上相似、但实际上并不相关的文档。即使文档规模增长到数百万份，公司仍需要提升检索质量，并保持较低的终端用户延迟。哪种解决方案能够以**最低运维开销**满足要求？

- **A.** 配置 Hybrid Search（混合搜索），将向量相似度搜索与关键词匹配结合起来，同时改善语义理解能力以及对精确术语和缩写的匹配能力。 **（最高票）**
- **B.** 将向量 Embedding 的维度从 384 提高到 1536。检索完成后，再使用 AWS Lambda 后处理函数过滤不相关结果。
- **C.** 使用 Amazon Kendra 替换 OpenSearch Service，并在预处理过程中使用 Query Expansion（查询扩展）处理医学缩写和术语变体。
- **D.** 实现两阶段检索架构：先执行初始向量搜索，再使用托管在 Amazon SageMaker AI 上的机器学习模型对结果进行重新排序（Rerank）。

### English

A medical company is building a generative AI (GenAI) application that uses RAG to provide evidence-based medical information. The application uses Amazon OpenSearch Service to retrieve vector embeddings. Users report that searches frequently miss results that contain exact medical terms and acronyms and return too many semantically similar but irrelevant documents. The company needs to improve retrieval quality and maintain low end user latency, even as the document collection grows to millions of documents. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Configure hybrid search by combining vector similarity with keyword matching to improve semantic understanding and exact term and acronym matching. **(Most Voted)**
- **B.** Increase the dimensions of the vector embeddings from 384 to 1536. Use a post-processing AWS Lambda function to filter out irrelevant results after retrieval.
- **C.** Replace OpenSearch Service with Amazon Kendra. Use query expansion to handle medical acronyms and terminology variants during pre-processing.
- **D.** Implement a two-stage retrieval architecture in which initial vector search results are re-ranked by an ML model that is hosted on Amazon SageMaker AI.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 8 - Topic 1

### 中文

一家公司在一个应用 AWS 账户中运行基于 Amazon Bedrock 的生成式 AI（GenAI）摘要应用。应用架构包含一个 Amazon API Gateway REST API，请求会被转发到挂载在私有 VPC 子网中的 AWS Lambda 函数。该应用用于总结敏感客户记录，而这些记录存储在中央数据存储账户中受治理的数据湖内。数据存储账户已经启用了 Amazon S3、Amazon Athena 和 AWS Glue。公司必须确保应用对 Amazon Bedrock 的所有调用，仅通过公司应用 VPC 与 Amazon Bedrock 之间的**私有网络连接**进行。同时，公司的数据湖必须支持跨 AWS 账户的细粒度列级访问控制。哪种解决方案能够满足这些要求？

- **A.** 在应用账户中，为 Amazon Bedrock Runtime 创建 Interface VPC Endpoint，并让 Lambda 函数运行在私有子网中。针对推理和数据平面策略使用 IAM Condition，只允许通过批准的 Endpoint 和 Role 发起调用。在数据存储账户中，使用 AWS Lake Formation 的 LF-Tag-Based Access Control，为表级和列级访问创建跨账户授权。 **（最高票）**
- **B.** 让 Lambda 函数运行在私有子网中，并配置 NAT Gateway 访问 Amazon Bedrock 和数据湖。使用 S3 Bucket Policy 和 ACL 管理权限。将 AWS CloudTrail 日志导出到 Amazon S3，每周进行人工审查。
- **C.** 在应用账户中仅为 Amazon S3 创建 Gateway Endpoint，通过公网 Endpoint 调用 Amazon Bedrock。使用 AWS Lake Formation 中的数据库级授权管理数据访问。将 AWS CloudTrail 日志流式发送到 Amazon CloudWatch Logs，并且不设置 Metric Filter 或 Alarm。
- **D.** 在应用账户中使用 VPC Endpoint 访问 Amazon Bedrock 和 Amazon S3。仅使用基于 IAM Path 的策略管理数据湖访问。将 AWS CloudTrail 日志发送到 Amazon CloudWatch Logs，定期创建 Dashboard，并允许跨 Region 读取时回退到公网连接，以减少配置工作量。

### English

A company runs a generative AI (GenAI)-powered summarization application in an application AWS account that uses Amazon Bedrock. The application architecture includes an Amazon API Gateway REST API that forwards requests to AWS Lambda functions that are attached to private VPC subnets. The application summarizes sensitive customer records that the company stores in a governed data lake in a centralized data storage account. The company has enabled Amazon S3, Amazon Athena, and AWS Glue in the data storage account. The company must ensure that calls that the application makes to Amazon Bedrock use only private connectivity between the company's application VPC and Amazon Bedrock. The company's data lake must provide fine-grained column-level access across the company's AWS accounts. Which solution will meet these requirements?

- **A.** In the application account, create interface VPC endpoints for Amazon Bedrock runtimes. Run Lambda functions in private subnets. Use IAM conditions on inference and data-plane policies to allow calls only to approved endpoints and roles. In the data storage account, use AWS Lake Formation LF-tag-based access control to create table and column-level cross-account grants. **(Most Voted)**
- **B.** Run Lambda functions in private subnets. Configure a NAT gateway to provide access to Amazon Bedrock and the data lake. Use S3 bucket policies and ACLs to manage permissions. Export AWS CloudTrail logs to Amazon S3 to perform weekly reviews.
- **C.** Create a gateway endpoint only for Amazon S3 in the application account. Invoke Amazon Bedrock through public endpoints. Use database-level grants in AWS Lake Formation to manage data access. Stream AWS CloudTrail logs to Amazon CloudWatch Logs. Do not set up metric filters or alarms.
- **D.** Use VPC endpoints to provide access to Amazon Bedrock and Amazon S3 in the application account. Use only IAM path-based policies to manage data lake access. Send AWS CloudTrail logs to Amazon CloudWatch Logs. Periodically create dashboards and allow public fallback for cross-Region reads to reduce setup time.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 9 - Topic 1

### 中文

一家媒体公司必须使用 Amazon Bedrock，为 AI 生成内容实施健壮的治理流程。公司需要管理数百个 Prompt 模板，这些模板由多个团队在多个 AWS Region 中用于生成内容。解决方案必须提供版本控制和审批工作流，并且在有待审核内容时发送通知。解决方案还必须提供详细的审计追踪，记录 Prompt 相关活动，并通过统一的 Prompt 参数化方式强制执行质量标准。哪种解决方案能够满足这些要求？

- **A.** 配置 Amazon Bedrock Studio Prompt 模板。使用 Amazon CloudWatch 创建 Dashboard 展示 Prompt 使用指标。将内容审批状态存储在 Amazon DynamoDB 中，并使用 AWS Lambda 函数强制执行审批。
- **B.** 使用 Amazon Bedrock Prompt Management 实现版本控制。配置 AWS CloudTrail 记录审计日志。使用 IAM Policy 控制审批权限。通过指定变量创建参数化 Prompt 模板。 **（最高票）**
- **C.** 使用 AWS Step Functions 创建审批工作流。将 Prompt 作为文档存储在 Amazon S3 中，通过 Tag 实现版本控制，并使用 Amazon EventBridge 发送通知。
- **D.** 部署 Amazon SageMaker Canvas，并将 Prompt 模板存储在 Amazon S3 中。使用 AWS CloudFormation 实现版本控制，并使用 AWS Config 强制执行审批策略。

### English

A media company must use Amazon Bedrock to implement a robust governance process for AI-generated content. The company needs to manage hundreds of prompt templates. Multiple teams use the templates across multiple AWS Regions to generate content. The solution must provide version control with approval workflows that include notifications for pending reviews. The solution must also provide detailed audit trails that document prompt activities and consistent prompt parameterization to enforce quality standards. Which solution will meet these requirements?

- **A.** Configure Amazon Bedrock Studio prompt templates. Use Amazon CloudWatch to create dashboards that display prompt usage metrics. Store the approval status of content in Amazon DynamoDB. Use AWS Lambda functions to enforce approvals.
- **B.** Use Amazon Bedrock Prompt Management to implement version control. Configure AWS CloudTrail for audit logging. Use IAM policies to control approval permissions. Create parameterized prompt templates by specifying variables. **(Most Voted)**
- **C.** Use AWS Step Functions to create an approval workflow. Store prompts as documents in Amazon S3. Use tags to implement version control. Use Amazon EventBridge to send notifications.
- **D.** Deploy Amazon SageMaker Canvas with prompt templates that are stored in Amazon S3. Use AWS CloudFormation to implement version control. Use AWS Config to enforce approval policies.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 10 - Topic 1

### 中文

一家公司正在开发客户支持应用，使用 Amazon Bedrock Foundation Model（FM）为公司员工提供实时 AI 辅助。应用必须在模型生成响应的同时，**逐字符显示 AI 生成内容**。应用需要以最低延迟支持数千名并发用户。每次响应通常需要 15～45 秒才能生成完毕。哪种解决方案能够满足这些要求？

- **A.** 配置集成 AWS Lambda 的 Amazon API Gateway WebSocket API。让 WebSocket API 调用 Amazon Bedrock `InvokeModelWithResponseStream` API，并通过 WebSocket 连接持续流式发送部分响应。 **（最高票）**
- **B.** 配置集成 AWS Lambda 的 Amazon API Gateway REST API。让 REST API 调用 Amazon Bedrock 标准 `InvokeModel` API，并在前端客户端每 100 ms 轮询一次已完成的响应 Chunk。
- **C.** 在前端客户端中直接使用 IAM User Credential 连接 Amazon Bedrock，并直接调用 `InvokeModelWithResponseStream` API，不经过任何中间 Gateway 或 Proxy。
- **D.** 配置集成 AWS Lambda 的 Amazon API Gateway HTTP API。让 HTTP API 将完整响应缓存在 Amazon DynamoDB 表中，再通过多个分页 GET 请求向前端客户端提供响应。

### English

A company is developing a customer support application that uses Amazon Bedrock foundation models (FMs) to provide real-time AI assistance to the company's employees. The application must display AI-generated responses character by character as the responses are generated. The application needs to support thousands of concurrent users with minimal latency. The responses typically take 15 to 45 seconds to finish. Which solution will meet these requirements?

- **A.** Configure an Amazon API Gateway WebSocket API with an AWS Lambda integration. Configure the WebSocket API to invoke the Amazon Bedrock InvokeModelWithResponseStream API and stream partial responses through WebSocket connections. **(Most Voted)**
- **B.** Configure an Amazon API Gateway REST API with an AWS Lambda integration. Configure the REST API to invoke the Amazon Bedrock standard InvokeModel API and implement frontend client-side polling every 100 ms for complete response chunks.
- **C.** Implement direct frontend client connections to Amazon Bedrock by using IAM user credentials and the InvokeModelWithResponseStream API without any intermediate gateway or proxy layer.
- **D.** Configure an Amazon API Gateway HTTP API with an AWS Lambda integration. Configure the HTTP API to cache complete responses in an Amazon DynamoDB table and serve the responses through multiple paginated GET requests to frontend clients.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 11 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 设计一款帮助研究人员申请科研资助的应用。该应用基于 Amazon Nova Pro Foundation Model（FM）。应用包含四个必填输入，并且必须以统一的文本格式返回响应。如果响应中包含欺凌性语言，公司希望在 Amazon Bedrock 中收到提示，但又不希望把所有被标记的响应全部阻止。公司已经创建了一个 Amazon Bedrock Flow：接收输入 Prompt，将其发送给 Amazon Nova Pro FM，再由模型返回响应。为了满足这些要求，公司还必须执行哪两个步骤？（选择两项。）

- **A.** 使用 Amazon Bedrock Prompt Management，将四个必填输入定义为变量。选择 Amazon Nova Pro FM，并指定响应的输出格式。将该 Prompt 添加到 Flow 的 Prompts Node 中。 **（最高票）**
- **B.** 创建 Amazon Bedrock Guardrail，应用 Hate Content Filter（仇恨内容过滤器），并将过滤动作设置为 Block。把该 Guardrail 添加到 Flow 的 Prompts Node。
- **C.** 创建 Amazon Bedrock Prompt Router，指定 Amazon Nova Pro FM。将必填输入作为变量添加到 Flow 的 Input Node，将 Prompt Router 添加到 Prompts Node，并在 Output Node 中指定输出格式。
- **D.** 创建 Amazon Bedrock Guardrail，应用 Insults Content Filter（侮辱内容过滤器），并将过滤动作设置为 Detect。把该 Guardrail 添加到 Flow 的 Prompts Node。 **（最高票）**
- **E.** 创建 Amazon Bedrock Application Inference Profile，并指定 Amazon Nova Pro FM。在 Description 中指定响应输出格式，为每个输入变量添加 Tag，然后将该 Profile 添加到 Flow 的 Prompts Node。

### English

A company is using Amazon Bedrock to design an application to help researchers apply for grants. The application is based on an Amazon Nova Pro foundation model (FM). The application contains four required inputs and must provide responses in a consistent text format. The company wants to receive a notification in Amazon Bedrock if a response contains bullying language. However, the company does not want to block all flagged responses. The company creates an Amazon Bedrock flow that takes an input prompt and sends it to the Amazon Nova Pro FM. The Amazon Nova Pro FM provides a response. Which additional steps must the company take to meet these requirements? (Choose two.)

- **A.** Use Amazon Bedrock Prompt Management to specify the required inputs as variables. Select an Amazon Nova Pro FM. Specify the output format for the response. Add the prompt to the prompts node of the flow. **(Most Voted)**
- **B.** Create an Amazon Bedrock guardrail that applies the hate content filter. Set the filter response to block. Add the guardrail to the prompts node of the flow.
- **C.** Create an Amazon Bedrock prompt router. Specify an Amazon Nova Pro FM. Add the required inputs as variables to the input node of the flow. Add the prompt router to the prompts node. Add the output format to the output node.
- **D.** Create an Amazon Bedrock guardrail that applies the insults content filter. Set the filter response to detect. Add the guardrail to the prompts node of the flow. **(Most Voted)**
- **E.** Create an Amazon Bedrock application inference profile that specifies an Amazon Nova Pro FM. Specify the output format for the response in the description. Include a tag for each of the input variables. Add the profile to the prompts node of the flow.

**Correct Answer / 正确答案:** `AD`

**Community vote distribution / 社区投票分布:**

- AD (100%)

---

## Question 12 - Topic 1

### 中文

一家医疗保健公司正在使用 Amazon Bedrock 构建一个 Retrieval Augmented Generation（RAG，检索增强生成）应用，帮助医务人员作出临床决策。该应用必须在患者信息检索方面达到较高准确率，能够识别生成内容中的幻觉，同时降低人工审核成本。哪种解决方案能够满足这些要求？

- **A.** 使用 Amazon Comprehend 分析和分类 RAG 响应，并提取医学实体及其关系。使用 AWS Step Functions 编排自动评估流程。配置 Amazon CloudWatch 指标跟踪实体识别的置信度分数，并在准确率低于指定阈值时由 CloudWatch 发出告警。
- **B.** 实现基于大型语言模型（LLM）的自动评估，使用针对医学内容微调过的专用模型评估所有响应。部署 AWS Lambda 函数并行执行评估，并将相关性和事实准确率结果发布为 Amazon CloudWatch 指标。
- **C.** 配置 Amazon CloudWatch Synthetics，按固定计划生成具有已知答案的测试查询，并跟踪模型成功率。创建 Dashboard，将合成测试结果与预期答案进行比较。
- **D.** 部署混合评估系统：首先使用自动化 LLM-as-a-Judge 对响应进行初筛，再针对边缘案例进行定向人工审核。使用 Amazon SageMaker Feature Store 维护评估数据集，并使用 Amazon Bedrock 内置评估跟踪检索 Precision 和幻觉率。 **（最高票）**

### English

A healthcare company is using Amazon Bedrock to build a Retrieval Augmented Generation (RAG) application that helps practitioners make clinical decisions. The application must achieve high accuracy for patient information retrievals, identify hallucinations in generated content, and reduce human review costs. Which solution will meet these requirements?

- **A.** Use Amazon Comprehend to analyze and classify RAG responses and to extract medical entities and relationships. Use AWS Step Functions to orchestrate automated evaluations. Configure Amazon CloudWatch metrics to track entity recognition confidence scores. Configure CloudWatch to send an alert when accuracy falls below specified thresholds.
- **B.** Implement automated large language model (LLM)-based evaluations that use a specialized model that is fine-tuned for medical content to assess all responses. Deploy AWS Lambda functions to parallelize evaluations. Publish results to Amazon CloudWatch metrics that track relevance and factual accuracy.
- **C.** Configure Amazon CloudWatch Synthetics to generate test queries that have known answers on a regular schedule, and track model success rates. Set up dashboards that compare synthetic test results against expected outcomes.
- **D.** Deploy a hybrid evaluation system that uses an automated LLM-as-a-judge evaluation to initially screen responses and targeted human reviews for edge cases. Use Amazon SageMaker Feature Store to maintain evaluation datasets. Use a built-in Amazon Bedrock evaluation to track retrieval precision and hallucination rates. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 13 - Topic 1

### 中文

一家公司在 AWS Control Tower 中配置了 Landing Zone。公司处理的敏感数据必须始终保留在欧盟境内，并且公司规定只能使用 `eu-central-1` Region。公司使用 SCP（Service Control Policy）强制执行数据驻留策略。公司的 GenAI 开发人员被分配了对 Amazon Bedrock 拥有完整权限的 IAM Role。公司必须确保这些开发人员只能通过 Cross-Region Inference（CRI，跨区域推理）使用 Amazon Bedrock 中的 Amazon Nova Pro，并且入口只能位于 `eu-central-1`。公司已经在 Amazon Bedrock 中为开发人员的 IAM Role 启用了模型访问权限。但是，当开发人员通过 Amazon Bedrock Chat/Text Playground 调用模型时，收到以下错误：
`User: arn:aws:sts::123456789012:assumed-role/AssumedDevRole/DevUserName`
`Action: bedrock:InvokeModelWithResponseStream`
`On resource(s): arn:aws:bedrock:eu-west-3::foundation-model/amazon.nova-pro-v1:0`
`Context: a service control policy explicitly denies the action`
公司需要解决该错误，同时保留现有治理控制、提供精确的访问控制，并继续遵守现有数据驻留策略。以下哪两个方案组合能够满足要求？（选择两项。）

- **A.** 为 GenAI 开发人员的 IAM Role 添加 `AdministratorAccess` Policy。
- **B.** 扩展现有 SCP，允许 `eu.amazon.nova-pro-v1:0` Inference Profile 使用 CRI。 **（最高票）**
- **C.** 在 `eu-west-3` Region 中为 Amazon Nova Pro 启用 Amazon Bedrock 模型访问权限。
- **D.** 确认 GenAI 开发人员的 IAM Role 有权通过 `eu.amazon.nova-pro.v1:0` Inference Profile，在所有可能实际承载该模型的欧盟 AWS Region 中调用 Amazon Nova Pro。 **（最高票）**
- **E.** 扩展现有 SCP，对 `eu.*` Inference Profile 全部启用 CRI。

### English

Company configures a landing zone in AWS Control Tower. The company handles sensitive data that must remain within the European Union. The company must use only the eu-central-1 Region. The company uses SCPs to enforce data residency policies. GenAI developers at the company are assigned IAM roles that have full permissions for Amazon Bedrock. The company must ensure that GenAI developers can use the Amazon Nova Pro model through Amazon Bedrock only by using cross-Region inference (CRI) and only in eu-central-1. The company enables model access for the GenAI developer IAM roles in Amazon Bedrock. However, when a GenAI developer attempts to invoke the model through the Amazon Bedrock Chat/Text playground, the GenAI developer receives the following error. User: arn:aws:sts::123456789012:assumed-role/AssumedDevRole/DevUserName Action: bedrock:InvokeModelWithResponseStream On resource(s): arn:aws:bedrock:eu-west-3::foundation-model/amazon.nova-pro-v1:0 Context: a service control policy explicitly denies the action The company needs a solution to resolve the error. The solution must retain the company's existing governance controls and must provide precise access control. The solution must comply with the company's existing data residency policies. Which combination of solutions will meet these requirements? (Choose two.)

- **A.** Add an AdministratorAccess policy to the GenAI developer IAM role.
- **B.** Extend the existing SCPs to enable CRI for the eu.amazon.nova-pro-v1:0 inference profile. **(Most Voted)**
- **C.** Enable Amazon Bedrock model access for Amazon Nova Pro in the eu-west-3 Region.
- **D.** Validate that the GenAI developer IAM roles have permissions to invoke Amazon Nova Pro through the eu.amazon.nova-pro.v1:0 inference profile on all European Union AWS Regions that can serve the model. **(Most Voted)**
- **E.** Extend the existing SCP to enable CRI for the eu.* inference profile.

**Correct Answer / 正确答案:** `BD`

**Community vote distribution / 社区投票分布:**

- BD (80%)
- B (20%)

---

## Question 14 - Topic 1

### 中文

一家金融服务公司正在使用 Amazon Bedrock 开发客户服务 AI 助手。该助手不得与用户讨论投资建议；必须阻止有害内容、遮蔽 Personally Identifiable Information（PII，个人身份信息），并保留用于合规报告的审计记录。AI 助手必须根据内容敏感程度，对用户输入和模型输出同时实施内容过滤。公司需要一种 Amazon Bedrock Guardrail 配置，能够有效强制执行这些策略，同时尽量减少误报，并且能够针对不同类型的敏感内容采用不同处理方式。哪种方案能够满足这些要求？

- **A.** 只配置一个 Guardrail，并把所有类别的内容过滤强度都设为 High。针对投资建议配置 Denied Topics，并提供需要阻止的示例短语。为所有 PII 实体配置 Sensitive Information Filter，统一执行 Block。将该 Guardrail 应用于所有模型推理调用。
- **B.** 使用分层策略配置多个 Guardrail。创建第一个 Guardrail，将内容过滤强度设为 High，并在公开交互中阻止 PII。创建第二个 Guardrail，将内容过滤强度设为 Medium，并在内部使用场景中对 PII 进行 Mask。再配置多个特定主题 Guardrail 阻止投资建议，同时设置 Contextual Grounding Check。
- **C.** 配置一个 Guardrail，将有害内容过滤强度设置为 Medium。为投资建议设置 Denied Topics，并提供清晰定义和需要阻止的示例短语。配置 Sensitive Information Filter，在输出中对 PII 进行 Mask，并在输入中阻止金融敏感信息。启用输入和输出两侧的评估，并使用自定义阻止提示消息支持审计。
- **D.** 为每个使用场景分别创建一个 Guardrail：一个处理有害内容，一个处理投资建议主题，一个处理 PII。然后使用 AWS Step Functions 按顺序串联这些 Guardrail，并根据内容分类结果执行条件逻辑。

### English

A financial services company is developing a customer service AI assistant by using Amazon Bedrock. The AI assistant must not discuss investment advice with users. The AI assistant must block harmful content, mask personally identifiable information (PII), and maintain audit trails for compliance reporting. The AI assistant must apply content filtering to both user inputs and model responses based on content sensitivity. The company requires an Amazon Bedrock guardrail configuration that will effectively enforce policies with minimal false positives. The solution must provide multiple handling strategies for multiple types of sensitive content. Which solution will meet these requirements?

- **A.** Configure a single guardrail and set content filters to high for all categories. Set up denied topics for investment advice and include sample phrases to block. Set up sensitive information filters that apply the block action for all PII entities. Apply the guardrail to all model inference calls.
- **B.** Configure multiple guardrails by using tiered policies. Create one guardrail and set content filters to high. Configure the guardrail to block PII for public interactions. Configure a second guardrail and set content filters to medium. Configure the second guardrail to mask PII for internal use. Configure multiple topic-specific guardrails to block investment advice and set up contextual grounding checks.
- **C.** Configure a guardrail and set content filters to medium for harmful content. Set up denied topics for investment advice and include clear definitions and sample phrases to block. Configure sensitive information filters to mask PII in responses and to block financial information in inputs. Enable both input and output evaluations that use custom blocked messages for audits.
- **D.** Create a separate guardrail for each use case. Create one guardrail that applies a harmful content filter. Create a guardrail to apply topic filters for investment advice. Create a guardrail to apply sensitive information filters to block PII. Use AWS Step Functions to chain the guardrails together sequentially. Use conditional logic based on content classification.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 15 - Topic 1

### 中文

一家电商公司正在开发生成式 AI（GenAI）商品推荐方案，使用 Amazon Bedrock 上的 Anthropic Claude 为客户推荐商品。客户反馈，部分推荐商品在网站上根本没有销售，或者与客户并不相关；此外，有些推荐生成速度很慢。公司调查发现，大多数客户与商品推荐系统之间的交互都是独一无二的，因此重复请求较少。公司还确认，系统确实会推荐不在公司商品目录中的产品。公司必须解决这些问题。哪种解决方案能够满足要求？

- **A.** 提高 Amazon Bedrock Guardrails 中的 Grounding 强度。启用 Automated Reasoning Check，并配置 Provisioned Throughput。
- **B.** 使用 Prompt Engineering 限制模型只能返回相关商品。使用 `InvokeModelWithResponseStream` 等流式技术降低客户感知到的延迟。
- **C.** 创建 Amazon Bedrock Knowledge Base，实现 Retrieval Augmented Generation（RAG），并将 `PerformanceConfigLatency` 参数设置为 `optimized`。 **（最高票）**
- **D.** 将商品目录数据存储在 Amazon OpenSearch Service 中。根据商品目录验证模型给出的商品推荐，并使用 Amazon DynamoDB 实现响应缓存。

### English

An ecommerce company is developing a generative AI (GenAI) solution that uses Amazon Bedrock with Anthropic Claude to recommend products to customers. Customers report that some of the recommended products are not available for sale on the website or are not relevant to the customer. Customers also report that the solutions takes a long time to generate some recommendations. The company investigates the issues and finds that most interactions between customers and the product recommendation solution are unique. The company confirms that the solutions recommends products that are not in the company's product catalog. The company must resolve these issues. Which solution will meet this requirement?

- **A.** Increase grounding within Amazon Bedrock Guardrails. Enable Automated Reasoning checks. Set up provisioned throughput.
- **B.** Use prompt engineering to restrict the model responses to relevant products. Use streaming techniques such as the InvokeModelWithResponseStream action to reduce perceived latency for the customers.
- **C.** Create an Amazon Bedrock knowledge base. Implement Retrieval Augmented Generation (RAG). Set the PerformanceConfigLatency parameter to optimized. **(Most Voted)**
- **D.** Store product catalog data in Amazon OpenSearch Service. Validate the model's product recommendations against the product catalog. Use Amazon DynamoDB to implement response caching.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 16 - Topic 1

### 中文

一家公司正在使用 AWS Lambda 和 REST API 构建一个推理 Agent，用于自动化支持工作流。系统必须能够跨多次交互保留 Memory，共享相关的 Agent State，并同时支持 Event-Driven Invocation 和 Synchronous Invocation。系统还必须实施访问控制以及基于 Session 的权限。以下哪两个步骤组合能够提供**最具可扩展性**的解决方案？（选择两项。）

- **A.** 使用 Amazon Bedrock AgentCore 管理 Memory 和具备 Session 感知能力的推理。部署 Agent 时使用其内置 Identity 支持、事件处理和可观测性功能。 **（最高票）**
- **B.** 使用 Amazon API Gateway 和 Amazon EventBridge，将 Lambda 函数与 REST API 注册为 Action。允许 Amazon Bedrock AgentCore 在无需自定义编排代码的情况下调用 Lambda 函数和 REST API。 **（最高票）**
- **C.** 使用 Amazon Bedrock Agents 负责推理和对话管理，使用 AWS Step Functions 和 Amazon SQS 队列完成编排，并把 Agent State 存储在 Amazon DynamoDB 中，以便在步骤之间保持 Memory。
- **D.** 将推理逻辑作为容器部署在 Amazon ECS 上，并放置在 Amazon API Gateway 后端。使用 Amazon Aurora 存储 Memory 数据和身份数据。
- **E.** 使用 Amazon Kendra 和 Amazon Bedrock 构建自定义 RAG Pipeline。使用 AWS Lambda 编排 Tool 调用，并将 Agent State 存储在 Amazon S3 中。

### English

A company is using AWS Lambda and REST APIs to build a reasoning agent to automate support workflows. The system must preserve memory across interactions, share the relevant agent state, and support event-driven invocation and synchronous invocation. The system must also enforce access control and session-based permissions. Which combination of steps provides the MOST scalable solution? (Choose two.)

- **A.** Use Amazon Bedrock AgentCore to manage memory and session-aware reasoning. Deploy the agent with built-in identity support, event handling, and observability. **(Most Voted)**
- **B.** Register the Lambda functions and the REST APIs as actions by using Amazon API Gateway and Amazon EventBridge. Enable Amazon Bedrock AgentCore to invoke the Lambda functions and the REST APIs without custom orchestration code. **(Most Voted)**
- **C.** Use Amazon Bedrock Agents for reasoning and conversation management. Use AWS Step Functions and Amazon SQS queues for orchestration. Store the agent state in Amazon DynamoDB to maintain memory between steps.
- **D.** Deploy the reasoning logic as a container on Amazon ECS behind Amazon API Gateway. Use Amazon Aurora to store memory data and identity data.
- **E.** Build a custom RAG pipeline by using Amazon Kendra and Amazon Bedrock. Use AWS Lambda to orchestrate tool invocations. Store the agent state in Amazon S3.

**Correct Answer / 正确答案:** `AB`

**Community vote distribution / 社区投票分布:**

- AB (71%)
- A (29%)

---

## Question 17 - Topic 1

### 中文

一家金融服务公司正在开发 Retrieval Augmented Generation（RAG）应用，帮助投资分析师查询多个投资工具、市场行业和监管环境之间复杂的金融关系。数据集中包含大量高度互联的实体，并存在 Multi-Hop Relationship（多跳关系）。分析师必须能够从整体上审视这些关系，才能提供准确的投资指导。应用需要给出能够覆盖金融实体之间**间接关系**的完整答案，并且响应时间必须低于 3 秒。哪种解决方案能够以**最低运维开销**满足这些要求？

- **A.** 使用 Amazon Bedrock Knowledge Bases 的 Graph RAG，并使用 Amazon Neptune Analytics 存储金融数据。分析实体之间的多跳关系，并自动识别跨文档的相关信息。 **（最高票）**
- **B.** 使用 Amazon Bedrock Knowledge Bases 和 Amazon OpenSearch Service Vector Store，并通过 AWS Lambda 函数按顺序查询多个向量 Embedding，自定义实现关系识别逻辑。
- **C.** 使用 Amazon OpenSearch Serverless 向量数据库执行 k-Nearest Neighbor（k-NN）搜索，并在运行于 Amazon EC2 Auto Scaling Group 的应用层中手动实现关系映射。
- **D.** 使用 Amazon DynamoDB 将金融数据存入自定义索引系统。使用 AWS Lambda 根据输入问题查询相关记录，再使用 Amazon SageMaker AI 生成响应。

### English

A financial services company is developing a Retrieval Augmented Generation (RAG) application to help investment analysts query complex financial relationships across multiple investment vehicles, market sectors, and regulatory environments. The dataset contains highly interconnected entities that have multi-hop relationships. The analysts must be able to examine the relationships holistically to provide accurate investment guidance. The application must deliver comprehensive answers that capture indirect relationships between financial entities. The application must produce responses in less than 3 seconds. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Use Amazon Bedrock Knowledge Bases with Graph RAG and Amazon Neptune Analytics to store the financial data. Analyze the multi-hop relationships between entities and automatically identify related information across documents. **(Most Voted)**
- **B.** Use Amazon Bedrock Knowledge Bases and an Amazon OpenSearch Service vector store to implement custom relationship identification logic that uses AWS Lambda functions to query multiple vector embeddings in sequence.
- **C.** Use an Amazon OpenSearch Serverless vector database with k-nearest neighbor (k-NN) searches. Implement manual relationship mapping in an application layer that runs in an Amazon EC2 Auto Scaling group.
- **D.** Use Amazon DynamoDB to store financial data in a custom indexing system. Use an AWS Lambda function to query relevant records based on input questions. Use Amazon SageMaker AI to generate responses.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 18 - Topic 1

### 中文

一家医疗保健公司使用 Amazon Bedrock 部署了一个临床文档摘要应用。该应用的响应质量不稳定，偶尔会出现事实性幻觉。同时，公司每月成本比原先预算高出 40%。一名 GenAI Developer 必须实现近实时监控方案，用于检测幻觉、识别异常 Token 消耗，并对成本异常提供早期预警。解决方案必须尽量减少自定义开发和维护开销。哪种方案能够满足这些要求？

- **A.** 配置 Amazon CloudWatch Alarm，监控 `InputTokenCount` 和 `OutputTokenCount` 指标以发现异常。将模型调用日志存储到 Amazon S3，并使用 AWS Glue 和 Amazon Athena 识别潜在幻觉。
- **B.** 运行 Amazon Bedrock Evaluation Job，使用基于 LLM 的评判检测幻觉。配置 Amazon CloudWatch 跟踪 Token 使用量。创建 AWS Lambda 函数处理 CloudWatch 指标，并由 Lambda 发送使用模式通知。
- **C.** 配置 Amazon Bedrock，将模型调用日志存储到 Amazon S3，并启用文本输出日志。配置 Amazon Bedrock Guardrails 执行 Contextual Grounding Check 来检测幻觉。针对 Token 使用指标创建 Amazon CloudWatch Anomaly Detection Alarm。 **（最高票）**
- **D.** 使用 AWS CloudTrail 记录所有 Amazon Bedrock API 调用。创建 Amazon QuickSight 自定义 Dashboard 展示 Token 使用模式，并使用 Amazon SageMaker Model Monitor 检测生成摘要的质量漂移。

### English

A healthcare company uses Amazon Bedrock to deploy an application that generates summaries of clinical documents. The application experiences inconsistent response quality with occasional factual hallucinations. Monthly costs exceed the company's projections by 40%. A GenAI developer must implement a near real-time monitoring solution to detect hallucinations, identify abnormal token consumption, and provide early warnings of cost anomalies. The solution must require minimal custom development work and maintenance overhead. Which solution will meet these requirements?

- **A.** Configure Amazon CloudWatch alarms to monitor InputTokenCount and OutputTokenCount metrics to detect anomalies. Store model invocation logs in an Amazon S3 bucket. Use AWS Glue and Amazon Athena to identify potential hallucinations.
- **B.** Run Amazon Bedrock evaluation jobs that use LLM-based judgments to detect hallucinations. Configure Amazon CloudWatch to track token usage. Create an AWS Lambda function to process CloudWatch metrics. Configure the Lambda function to send usage pattern notifications.
- **C.** Configure Amazon Bedrock to store model invocation logs in an Amazon S3 bucket. Enable text output logging. Configure Amazon Bedrock guardrails to run contextual grounding checks to detect hallucinations. Create Amazon CloudWatch anomaly detection alarms for token usage metrics. **(Most Voted)**
- **D.** Use AWS CloudTrail to log all Amazon Bedrock API calls. Create a custom dashboard in Amazon QuickSight to visualize token usage patterns. Use Amazon SageMaker Model Monitor to detect quality drift in generated summaries.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (89%)
- B (11%)

---

## Question 19 - Topic 1

### 中文

一家公司正在构建生成式 AI（GenAI）应用，该应用会基于多种内部和外部数据源生成内容。公司希望确保所有生成输出都具备完整的可追溯性。应用必须支持数据源注册，并能够通过 Metadata Tag 将内容归属到其原始来源；同时还必须在整个 Pipeline 中保留有关数据访问和数据使用情况的审计日志。哪种解决方案能够满足这些要求？

- **A.** 使用 AWS Lake Formation 编目数据源并控制访问权限。直接在 Amazon S3 中应用 Metadata Tag，并使用 AWS CloudTrail 监控 API 活动。
- **B.** 使用 AWS Glue Data Catalog 注册数据源并添加 Tag。使用 Amazon CloudWatch Logs 监控访问模式和应用行为。
- **C.** 将数据存储在 Amazon S3，并通过 Object Tagging 标记来源。使用 AWS Glue Data Catalog 管理 Schema 信息，并使用 AWS CloudTrail 记录对 S3 Bucket 的访问。
- **D.** 使用 AWS Glue Data Catalog 注册所有数据源，并应用 Metadata Tag 标记数据来源。使用 AWS CloudTrail 记录跨服务的数据访问和活动。 **（最高票）**

### English

A company is building a generative AI (GenAI) application that produces content based on a variety of internal and external data sources. The company wants to ensure that the generated output is fully traceable. The application must support data source registration and enable metadata tagging to attribute content to its original source. The application must also maintain audit logs of data access and usage throughout the pipeline. Which solution will meet these requirements?

- **A.** Use AWS Lake Formation to catalog data sources and control access. Apply metadata tags directly in Amazon S3. Use AWS CloudTrail to monitor API activity.
- **B.** Use AWS Glue Data Catalog to register and tag data sources. Use Amazon CloudWatch Logs to monitor access patterns and application behavior.
- **C.** Store data in Amazon S3 and use object tagging for attribution. Use AWS Glue Data Catalog to manage schema information. Use AWS CloudTrail to log access to S3 buckets.
- **D.** Use AWS Glue Data Catalog to register all data sources. Apply metadata tags to attribute data sources. Use AWS CloudTrail to log access and activity across services. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (86%)
- C (14%)

---

## Question 20 - Topic 1

### 中文

一家金融服务公司需要构建文档分析系统，使用 Amazon Bedrock 处理季度报告。系统必须针对一批报告执行金融数据分析、情感分析和合规验证。每批包含 5 份报告，每份报告都需要多次调用 Foundation Model（FM）。系统必须在 10 秒内完成每一批报告的全部分析，而当前串行处理一批需要 45 秒。哪种解决方案能够满足这些要求？

- **A.** 使用启用了 Provisioned Concurrency 的 AWS Lambda 函数，按顺序处理不同类型的分析。将 Lambda Timeout 设置为 10 秒，并配置带指数退避的自动重试。
- **B.** 使用 AWS Step Functions 的 `Parallel` State，同时调用多个独立 AWS Lambda 函数分别执行各类分析。配置 Amazon Bedrock Client Timeout，并使用 Amazon CloudWatch 指标跟踪执行时间和模型推理延迟。 **（最高票）**
- **C.** 创建 Amazon SQS 队列缓冲分析请求。部署多个带 Reserved Concurrency 的 AWS Lambda 函数，让每个 Lambda 按顺序处理每份报告的不同方面，最后再合并结果。
- **D.** 部署 Amazon ECS Cluster，让容器按顺序处理每份报告。使用 Load Balancer 分发批处理工作负载，并配置基于 CPU 利用率的 Auto Scaling Policy 来应对负载波动。

### English

A financial services company needs to build a document analysis system that uses Amazon Bedrock to process quarterly reports. The system must analyze financial data, perform sentiment analysis, and validate compliance across batches of reports. Each batch contains 5 reports. Each report requires multiple foundation model (FM) calls. The solution must finish the analysis within 10 seconds for each batch. Current sequential processing takes 45 seconds for each batch. Which solution will meet these requirements?

- **A.** Use AWS Lambda functions with provisioned concurrency to process each analysis type sequentially. Configure the Lambda function timeouts to 10 seconds. Configure automatic retries with exponential backoff.
- **B.** Use AWS Step Functions with a Parallel state to invoke separate AWS Lambda functions for each analysis type simultaneously. Configure Amazon Bedrock client timeouts. Use Amazon CloudWatch metrics to track execution time and model inference latency. **(Most Voted)**
- **C.** Create an Amazon SQS queue to buffer analysis requests. Deploy multiple AWS Lambda functions with reserved concurrency. Configure each Lambda function to process different aspects of each report sequentially and then combine the results.
- **D.** Deploy an Amazon ECS cluster that runs containers that process each report sequentially. Use a load balancer to distribute batch workloads. Configure an auto-scaling policy based on CPU utilization to handle demand fluctuations.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (80%)

---

## Question 21 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 构建面向客户的 AI 助手，用于处理敏感的客户咨询。公司必须采用 Defense-in-Depth（纵深防御）安全控制，阻止复杂的 Prompt Injection（提示词注入）攻击；必须保留所有安全干预行为的审计日志；AI 助手还必须具备跨 Region 故障转移能力。哪种解决方案能够满足这些要求？

- **A.** 配置 Amazon Bedrock Guardrails，使用内容过滤器防护 Prompt Injection，并将过滤强度设置为 High。使用 Guardrail Profile 实现跨 Region Guardrail Inference。使用 Amazon CloudWatch Logs 配合自定义指标，记录详细的 Guardrail 干预事件。 **（最高票）**
- **B.** 配置 Amazon Bedrock Guardrails，使用内容过滤器防护 Prompt Injection，并将过滤强度设置为 High。使用 AWS WAF 阻止可疑输入，并使用 AWS CloudTrail 记录 API 调用用于审计。
- **C.** 部署 Amazon Comprehend 自定义分类模型检测 Prompt Injection。使用 Amazon API Gateway 验证请求，并使用 Amazon CloudWatch Logs 和自定义指标记录详细的干预事件。
- **D.** 配置 Amazon Bedrock Guardrails，使用自定义内容过滤器防护有害内容，并将过滤强度设置为 High。使用 Word Filter 防护已知攻击模式。配置跨 Region Guardrail 复制以实现故障转移，并将日志存储在 AWS CloudTrail 中用于合规审计。

### English

A company is using Amazon Bedrock to build a customer-facing AI assistant to handle sensitive customer inquiries. The company must use defense-in-depth safety controls to block sophisticated prompt injection attacks. The company must keep audit logs of all safety interventions. The AI assistant must have cross-Region failover capabilities. Which solution will meet these requirements?

- **A.** Configure Amazon Bedrock guardrails to use content filters to protect against prompt injection attacks. Set the content filters to high. Use a guardrail profile to implement cross-Region guardrail inference. Use Amazon CloudWatch Logs with custom metrics to capture detailed guardrail intervention events. **(Most Voted)**
- **B.** Configure Amazon Bedrock guardrails to use content filters to protect against prompt injection attacks. Set the content filters to high. Use AWS WAF to block suspicious inputs. Use AWS CloudTrail to log API calls for audits.
- **C.** Deploy Amazon Comprehend custom classification to detect prompt injection attacks. Use Amazon API Gateway to validate requests. Use Amazon CloudWatch Logs with custom metrics to capture detailed intervention events.
- **D.** Configure Amazon Bedrock guardrails to use custom content filters to protect against harmful content. Set the content filters to high. Use word filters to protect against known attack patterns. Configure cross-Region guardrail replication to provide failover capabilities. Store logs in AWS CloudTrail for compliance auditing.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (75%)
- D (12%)

---

## Question 22 - Topic 1

### 中文

一家公司正在为支付处理 API 设计 Canary Deployment（金丝雀发布）策略。系统必须能够根据实时推理指标、历史流量模式和服务健康状态，在多个 Amazon Bedrock 模型之间自动、逐步地迁移流量。系统应逐渐增加发送到新模型版本的流量；如果指标持续健康，就继续增加流量；如果性能下降到可接受阈值以下，则应减少流量。公司需要在部署阶段全面监控推理延迟和错误率，并且必须能够在**无需人工干预**的情况下停止部署并回滚到之前的模型版本。哪种解决方案能够满足这些要求？

- **A.** 使用 Amazon Bedrock Provisioned Throughput 承载各个模型版本。配置 Amazon EventBridge 规则，在新模型版本发布时启动 AWS Step Functions 工作流。让工作流分阶段迁移流量、等待指定时间，再调用 AWS Lambda 检查 Amazon CloudWatch 性能指标。如果指标达到阈值，则继续增加流量；如果性能指标低于阈值，则自动触发流量回滚。 **（最高票）**
- **B.** 使用 AWS Lambda 调用不同 Amazon Bedrock 模型版本。使用带 Stage Variable 和 Weighted Routing 的 Amazon API Gateway HTTP API，逐步将流量切换到新模型版本。使用 Amazon CloudWatch 监控性能，并通过外部逻辑调整各版本流量和执行回滚。
- **C.** 使用 Amazon SageMaker AI Endpoint Variant 表示多个 Amazon Bedrock 模型版本，通过 Variant Weight 迁移流量。使用 Amazon CloudWatch 监控性能指标，并让 SageMaker Model Monitor 在性能低于阈值时触发 AWS Lambda 回滚模型部署；同时配置 Amazon EventBridge，在检测到异常时触发回滚。
- **D.** 使用 Amazon OpenSearch Service 跟踪推理日志。根据推理日志，让 OpenSearch Service 调用 AWS Systems Manager Automation Runbook，更新 Amazon Bedrock 模型 Endpoint 并迁移流量。

### English

A company is designing a canary deployment strategy for a payment processing API. The system must support automated gradual traffic shifting between multiple Amazon Bedrock models based on real-time inference metrics, historical traffic patterns, and service health. The solution must be able to gradually increase traffic to new model versions. The system must increase traffic if metrics remain healthy and decrease traffic if the performance degrades below acceptable thresholds. The company needs to comprehensively monitor inference latency and error rates during the deployment phase. The company must also be able to halt deployments and revert to a previous model version without any manual intervention. Which solution will meet these requirements?

- **A.** Use Amazon Bedrock with provisioned throughput to host the versions of the model. Configure an Amazon EventBridge rule to invoke an AWS Step Functions workflow when a new model version is released. Configure the workflow to shift traffic in stages, wait for a specified time period, and invoke an AWS Lambda function to check Amazon CloudWatch performance metrics. Configure the workflow to increase traffic if the metrics meet thresholds and to trigger a traffic rollback if performance metrics fall below thresholds. **(Most Voted)**
- **B.** Use AWS Lambda functions to invoke various Amazon Bedrock model versions. Use an Amazon API Gateway HTTP API with stage variables and weighted routing to shift traffic gradually to new model versions. Use Amazon CloudWatch to monitor performance metrics. Use external logic to adjust traffic between model versions and to roll back if performance falls below thresholds.
- **C.** Use Amazon SageMaker AI endpoint variants to represent multiple Amazon Bedrock model versions. Use variant weights to shift traffic. Use Amazon CloudWatch to monitor performance metrics. Use SageMaker Model Monitor to trigger AWS Lambda functions to roll back a model deployment if performance drops below a specified threshold. Configure an Amazon EventBridge rule to roll back model deployments if an anomaly is detected.
- **D.** Use Amazon OpenSearch Service to track inference logs. Configure OpenSearch Service to invoke an AWS Systems Manager Automation runbook to update Amazon Bedrock model endpoints to shift traffic based on the inference logs.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (90%)
- C (10%)

---

## Question 23 - Topic 1

### 中文

一家金融服务公司使用基于 Amazon Bedrock 的 AI 应用处理金融文档。工作时间内，该应用每小时大约处理 10,000 个请求，因此需要稳定吞吐量。公司使用 `CreateProvisionedModelThroughput` API 购买了 Provisioned Throughput。Amazon CloudWatch 指标显示，已购买的预置容量没有被使用，而 On-Demand 请求却正在被 Throttling。公司在应用中发现以下代码：
```python
response = bedrock_runtime.invoke_model(
modelId="anthropic.claude-v2",
body=json.dumps(payload)
)
```
公司需要让应用实际使用 Provisioned Throughput，并解决限流问题。哪种方案能够满足这些要求？

- **A.** 增加 Provisioned Throughput 配置中的 Model Unit（MU）数量。
- **B.** 将 `modelId` 参数替换为 `CreateProvisionedModelThroughput` API 返回的 Provisioned Model ARN。 **（最高票）**
- **C.** 添加指数退避重试逻辑，在高峰时段处理 Throttling Exception。
- **D.** 将应用从 `InvokeModel` API 改为使用 `InvokeModelWithResponseStream` API。

### English

A financial services company uses an AI application to process financial documents by using Amazon Bedrock. During business hours, the application handles approximately 10,000 requests each hour, which requires consistent throughput. The company uses the CreateProvisionedModelThroughput API to purchase provisioned throughput. Amazon CloudWatch metrics show that the provisioned capacity is unused while on-demand requests are being throttled. The company finds the following code in the application: python response = bedrock_runtime.invoke_model(modelId="anthropic.claude-v2", body=json.dumps(payload)) The company needs the application to use the provisioned throughput and to resolve the throttling issues. Which solution will meet these requirements?

- **A.** Increase the number of model units (MUs) in the provisioned throughput configuration.
- **B.** Replace the model ID parameter with the ARN of the provisioned model that the CreateProvisionedModelThroughput API returns. **(Most Voted)**
- **C.** Add exponential backoff retry logic to handle throttling exceptions during peak hours.
- **D.** Modify the application to use the InvokeModelWithResponseStream API instead of the InvokeModel API.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (91%)
- C (9%)

---

## Question 24 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 构建 AI 咨询应用，为客户提供建议。公司要求应用能够解释其推理过程，并为使用的数据引用具体来源。应用必须从公司数据源检索信息，针对推荐结果展示逐步推理，还必须把数据性陈述链接到源文档，并将响应延迟维持在 3 秒以内。哪种解决方案能够以**最低运维开销**满足这些要求？

- **A.** 使用启用了 Source Attribution（来源归属/引用）的 Amazon Bedrock Knowledge Bases。通过 RAG 使用 Anthropic Claude Messages API，并为源文档设置高相关性阈值。将推理内容和引用存储在 Amazon S3 中用于审计。 **（最高票）**
- **B.** 在 Amazon Bedrock 上使用 Anthropic Claude 的 Extended Thinking，并配置 4,000 Token 的 Thinking Budget。将推理 Trace 和引用存储在 Amazon DynamoDB 中用于审计。
- **C.** 在 Amazon SageMaker AI 中配置自定义 Anthropic Claude 模型。使用模型的 Reasoning 参数和 AWS Lambda 处理响应，并从独立 Amazon RDS 数据库添加来源引用。
- **D.** 在 Amazon Bedrock 中使用 Anthropic Claude 和 Chain-of-Thought 推理。通过 Amazon Bedrock Knowledge Bases API 自定义检索跟踪，并使用 Amazon CloudWatch 监控响应延迟。

### English

A company is building an AI advisory application by using Amazon Bedrock. The application will provide recommendations to customers. The company needs the application to explain its reasoning process and cite specific sources for data. The application must retrieve information from company data sources and show step-by-step reasoning for recommendations. The application must also link data claims to source documents and maintain response latency under 3 seconds. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Use Amazon Bedrock Knowledge Bases with source attribution enabled. Use the Anthropic Claude Messages API with RAG to set high-relevance thresholds for source documents. Store reasoning and citations in Amazon S3 for auditing purposes. **(Most Voted)**
- **B.** Use Amazon Bedrock with Anthropic Claude models and extended thinking. Configure a 4,000-token thinking budget. Store reasoning traces and citations in Amazon DynamoDB for auditing purposes.
- **C.** Configure Amazon SageMaker AI with a custom Anthropic Claude model. Use the model's reasoning parameter and AWS Lambda to process responses. Add source citations from a separate Amazon RDS database.
- **D.** Use Amazon Bedrock with Anthropic Claude models and chain-of-thought reasoning. Configure custom retrieval tracking with the Amazon Bedrock Knowledge Bases API. Use Amazon CloudWatch to monitor response latency metrics.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (60%)
- D (20%)
- B (20%)

---

## Question 25 - Topic 1

### 中文

一家金融服务公司通过 Amazon Bedrock 使用多个 Foundation Model（FM）运行生成式 AI（GenAI）应用。为了遵守一项针对敏感金融数据使用 GenAI 的新法规，公司需要实现 Token 管理方案。该方案必须在应用即将接近**不同模型各自的 Token 上限**时主动告警；同时每分钟必须能够处理超过 5,000 个请求，并持续记录 Token 使用指标，以便按业务部门分摊成本。哪种解决方案能够满足这些要求？

- **A.** 在 AWS Lambda 函数中针对不同模型实现各自的 Tokenizer。Lambda 在请求发送到 Amazon Bedrock 之前先估算 Token 使用量；将指标发布到 Amazon CloudWatch，并在请求接近阈值时触发 Alarm。将详细 Token 使用数据存储在 Amazon DynamoDB 中，用于成本报告。 **（最高票）**
- **B.** 使用带 Token Quota Policy 的 Amazon Bedrock Guardrails。采集被拒绝请求的指标，根据 Guardrails 指标配置 Amazon EventBridge 规则发送通知，并使用 Amazon CloudWatch Dashboard 展示各模型的 Token 使用趋势。
- **C.** 为失败请求部署 Amazon SQS Dead-Letter Queue。配置 AWS Lambda 分析与 Token 相关的失败，并根据 Amazon Bedrock API 响应错误日志，使用 Amazon CloudWatch Logs Insights 生成 Token 使用模式报告。
- **D.** 使用 Amazon API Gateway 为所有 Amazon Bedrock API 调用创建 Proxy。根据预定义 Token 配额的自定义 Usage Plan 配置请求限流，并让 API Gateway 拒绝预计会超过 Token 上限的请求。

### English

A financial services company uses multiple foundation models (FMs) through Amazon Bedrock for its generative AI (GenAI) applications. To comply with a new regulation for GenAI use with sensitive financial data, the company needs a token management solution. The token management solution must proactively alert when applications approach model-specific token limits. The solution must also process more than 5,000 requests each minute and maintain token usage metrics to allocate costs across business units. Which solution will meet these requirements?

- **A.** Develop model-specific tokenizers in an AWS Lambda function. Configure the Lambda function to estimate token usage before sending requests to Amazon Bedrock. Configure the Lambda function to publish metrics to Amazon CloudWatch and trigger alarms when requests approach thresholds. Store detailed token usage in Amazon DynamoDB to report costs. **(Most Voted)**
- **B.** Implement Amazon Bedrock Guardrails with token quota policies. Capture metrics on rejected requests. Configure Amazon EventBridge rules to trigger notifications based on Amazon Bedrock Guardrails metrics. Use Amazon CloudWatch dashboards to visualize token usage trends across models.
- **C.** Deploy an Amazon SQS dead-letter queue for failed requests. Configure an AWS Lambda function to analyze token-related failures. Use Amazon CloudWatch Logs Insights to generate reports on token usage patterns based on error logs from Amazon Bedrock API responses.
- **D.** Use Amazon API Gateway to create a proxy for all Amazon Bedrock API calls. Configure request throttling based on custom usage plans with predefined token quotas. Configure API Gateway to reject requests that will exceed token limits.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (83%)
- D (17%)

---

## Question 26 - Topic 1

### 中文

一家零售公司正在开发客户服务应用，每天需要处理 10,000 个有关商品、订单和保修的问题。应用必须能够回答来自 50,000 份商品文档的问题，而这些文档每天都会更新。应用还必须与订单管理 API 集成，用于查询订单状态并协助处理退货；在与客户进行多轮对话时必须持续保留上下文；公司还必须收集完整的应用响应审计记录。哪种解决方案能够以**最低运维开销**满足要求？

- **A.** 针对每个商品类别分别部署一个微调后的 Amazon Bedrock Anthropic Claude 模型。创建 AWS Lambda 函数让各模型连接订单管理 API，并把对话历史存储在 Amazon DynamoDB 中。
- **B.** 在 Amazon Bedrock 中使用 Continued Pre-Training 创建自定义模型处理所有商品文档。配置 Amazon API Gateway REST API，并通过 AWS Lambda 将模型连接到订单管理 API。
- **C.** 使用 Amazon SageMaker AI Container 部署模型。使用 Amazon Kendra 搜索商品文档，并使用 AWS Step Functions 编排对订单管理 API 的调用。
- **D.** 使用带 Action Group 的 Amazon Bedrock Agent 与订单管理 API 集成。将 Amazon Bedrock Knowledge Base 关联到 Agent，通过 Retrieval Augmented Generation（RAG）搜索商品文档，并启用 Trace Event 收集审计追踪。 **（最高票）**

### English

A retail company is developing a customer service application that must process 10,000 daily queries about products, orders, and warranties. The application must be able to respond to queries about 50,000 product documents that are updated every day. The application must integrate with an order management API to check the status of orders and to help process returns. The application must maintain context throughout multi-turn interactions with customers. The company must collect complete audit trails for application responses. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Deploy a fine-tuned Amazon Bedrock Anthropic Claude model for each product category. Create AWS Lambda functions to connect each model to the order management API. Store conversation history in Amazon DynamoDB.
- **B.** Create a custom model that uses continued pre-training on Amazon Bedrock to handle all product documentation. Set up an Amazon API Gateway REST API that uses AWS Lambda functions to connect the model to the order management API.
- **C.** Use Amazon SageMaker AI with containers to deploy models. Use Amazon Kendra to search product documents. Use AWS Step Functions to orchestrate calls to the order management API.
- **D.** Use an Amazon Bedrock agent with action groups to integrate with the order management API. Associate an Amazon Bedrock knowledge base with the agent to search product documentation by using Retrieval Augmentation Generation (RAG). Enable trace events to capture audit trails. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (83%)
- A (17%)

---

## Question 27 - Topic 1

### 中文

一家电商公司正在使用 Amazon Bedrock 构建生成式 AI（GenAI）应用。应用通过 AWS Step Functions 编排 Multi-Agent 工作流，以生成详细商品描述。工作流包含三个串行 State：描述生成器、技术规格验证器和品牌语气一致性检查器。每个 State 都会产生中间推理 Trace 和输出，并传递给下一个 State。应用使用 Amazon S3 Bucket 存储流程数据和输出。测试期间，公司发现 Step Functions 各 State 之间传递的输出经常超过 **256 KB 配额**，导致工作流失败。GenAI Developer 需要修改架构，在高效处理 256 KB 限制的同时保留工作流可观测性，并继续维持现有 Multi-Agent Reasoning and Acting（ReAct）模式。哪种方案能够以**最低运维开销**满足要求？

- **A.** 将中间输出存储在 Amazon DynamoDB 中，State 之间只传递引用。创建 `Map` State，在每个 Agent 需要处理数据时从 DynamoDB 获取完整数据。
- **B.** 配置 Amazon Bedrock Integration，在大输出的输入参数中使用 S3 Bucket URI。通过 `ResultPath` 和 `ResultSelector` 字段在 Agent 步骤之间传递 S3 引用，同时继续保持串行验证工作流。 **（最高票）**
- **C.** 在进入每个 Agent State 之前，使用 AWS Lambda 将输出压缩到 256 KB 以下；每个 Agent Task 先解压输入，处理后再压缩结果，然后传递给下一个 State。
- **D.** 为每个 Agent 的处理分别创建一个 Step Functions State Machine。使用 Amazon EventBridge 协调不同 State Machine 的执行顺序，并把 S3 引用作为事件数据传递。

### English

An ecommerce company is using Amazon Bedrock to build a generative AI (GenAI) application. The application uses AWS Step Functions to orchestrate a multi-agent workflow to produce detailed product descriptions. The workflow consists of three sequential states: a description generator, a technical specifications validator, and a brand voice consistency checker. Each state produces intermediate reasoning traces and outputs that are passed to the next state. The application uses an Amazon S3 bucket for process storage and to store outputs. During testing, the company discovers that outputs between Step Functions states frequently exceed the 256 KB quota and cause workflow failures. A GenAI Developer needs to revise the application architecture to efficiently handle the Step Functions 256 KB quota and maintain workflow observability. The revised architecture must preserve the existing multi-agent reasoning and acting (ReAct) pattern. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Store intermediate outputs in Amazon DynamoDB. Pass only references between states. Create a Map state that retrieves the complete data from DynamoDB when required for each agent's processing step.
- **B.** Configure an Amazon Bedrock integration to use the S3 bucket URI in the input parameter for large outputs. Use the ResultPath field and the ResultSelector field to route S3 references between the agent steps while maintaining the sequential validation workflow. **(Most Voted)**
- **C.** Use AWS Lambda functions to compress outputs to less than 256 KB before each agent state. Configure each agent task to decompress the outputs before processing and to compress results before passing them to the next state.
- **D.** Configure a separate Step Functions state machine to handle each agent's processing. Use Amazon EventBridge to coordinate the execution flow between state machines. Use S3 references for the outputs as event data.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (88%)
- A (12%)

---

## Question 28 - Topic 1

### 中文

一家公司提供全球餐厅发现服务，月活跃用户达到 5,000 万。公司希望在一个包含 2,000 万家餐厅和 2 亿条评论的数据库上实现 Semantic Search（语义搜索）。目前数据存储在 PostgreSQL 中。解决方案必须支持复杂自然语言查询，并确保至少 95% 的查询在 500 ms 内返回结果；餐厅详情每小时更新一次，因此还必须保持数据新鲜度；高峰期还需要具备成本有效的扩展能力。哪种解决方案能够以**最少开发工作量**满足要求？

- **A.** 将餐厅数据迁移到 Amazon OpenSearch Service。使用自定义 Analyzer 和 Relevance Tuning 实现基于关键词的搜索规则，根据菜系、特色和位置等属性查找餐厅。创建 Amazon API Gateway HTTP API，将用户自然语言查询转换为结构化搜索参数。
- **B.** 将餐厅数据迁移到 Amazon OpenSearch Service。使用 Amazon Bedrock 中的 Foundation Model（FM），根据餐厅描述、评论和菜单生成向量 Embedding。用户提交自然语言查询时，使用同一个 FM 将查询转换为 Embedding，再执行 k-Nearest Neighbor（k-NN）搜索找到语义相似结果。 **（最高票）**
- **C.** 保留 PostgreSQL，并启用 `pgvector` 扩展。使用 Amazon Bedrock 中的 FM 为餐厅数据生成向量 Embedding，并直接存储在 PostgreSQL 中。创建 AWS Lambda，将自然语言查询转换为向量，再在数据库中执行相似度搜索。
- **D.** 使用自定义 Ingestion Pipeline 将餐厅数据迁移到 Amazon Bedrock Knowledge Base。让 Knowledge Base 自动从餐厅信息生成 Embedding，并直接通过 Amazon Bedrock `Retrieve` API 的内置向量搜索能力处理自然语言查询。

### English

A company provides a service that helps users from around the world discover new restaurants. The service has 50 million monthly active users. The company wants to implement a semantic search solution across a database that contains 20 million restaurants and 200 million reviews. The company currently stores the data in a PostgresQL database. The solution must support complex natural language queries and return results for at least 95% of queries within 500 ms. The solution must maintain data freshness for restaurant details that update hourly. The solution must also scale cost-effectively during peak usage periods. Which solution will meet these requirements with the LEAST development effort?

- **A.** Migrate the restaurant data to Amazon OpenSearch Service. Implement keyword-based search rules that use custom analyzers and relevance tuning to find restaurants based on attributes such as cuisine type, feature, and location. Create Amazon API Gateway HTTP API endpoints to transform user queries into structured search parameters.
- **B.** Migrate the restaurant data to Amazon OpenSearch Service. Use a foundation model (FM) in Amazon Bedrock to generate vector embeddings from restaurant descriptions, reviews, and menu items. When users submit natural language queries, convert the queries to embeddings by using the same FM. Perform k-nearest neighbors (k-NN) searches to find semantically similar results. **(Most Voted)**
- **C.** Keep the restaurant data in PostgresQL and implement a pgvector extension. Use a foundation model (FM) in Amazon Bedrock to generate vector embeddings from restaurant data. Store the vector embeddings directly in PostgreSQL. Create an AWS Lambda function to convert natural language queries to vector representations by using the same FM. Configure the Lambda function to perform similarity searches within the database.
- **D.** Migrate restaurant data to an Amazon Bedrock knowledge base by using a custom ingestion pipeline. Configure the knowledge base to automatically generate embeddings from restaurant information. Use the Amazon Bedrock Retrieve API with built-in vector search capabilities to query the knowledge base directly by using natural language input.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (67%)
- D (22%)
- A (11%)

---

## Question 29 - Topic 1

### 中文

一家医疗公司使用 Amazon Bedrock 驱动临床文档摘要系统。处理复杂临床文档时，系统生成的摘要表现不一致，但处理简单临床文档时表现良好。公司需要一种方案来诊断这些不一致问题，基于既定指标比较不同 Prompt 的表现，并保留 Prompt 版本的历史记录。哪种解决方案能够满足这些要求？

- **A.** 使用 Amazon Bedrock Prompt Management 创建多个 Prompt 变体。使用简单临床文档手动测试 Prompt，并通过 Amazon Bedrock 控制台部署表现最好的版本。
- **B.** 在代码仓库中对 Prompt 实施版本控制，并建立包含复杂临床文档和可量化评估指标的测试套件。使用自动化测试框架比较不同 Prompt 版本，并记录性能模式。 **（最高票）**
- **C.** 将每个新 Prompt 版本分别部署到独立 Amazon Bedrock API Endpoint，并在 Endpoint 之间拆分生产流量。配置 Amazon CloudWatch 收集响应指标和用户反馈，用于自动选择版本。
- **D.** 在 Amazon Bedrock Flows 中创建自定义 Prompt Evaluation Flow，把相同的临床文档输入应用到不同 Prompt 变体，并使用 Amazon Comprehend Medical 分析和评分各版本的事实准确率。

### English

A medical company uses Amazon Bedrock to power a clinical documentation summarization system. The system produces inconsistent summaries when handling complex clinical documents. The system performed well on simple clinical documents. The company needs a solution that diagnoses inconsistencies, compares prompt performance against established metrics, and maintains historical records of prompt versions. Which solution will meet these requirements?

- **A.** Create multiple prompt variants by using Prompt management in Amazon Bedrock. Manually test the prompts with simple clinical documents. Deploy the highest performing version by using the Amazon Bedrock console.
- **B.** Implement version control for prompts in a code repository with a test suite that contains complex clinical documents and quantifiable evaluation metrics. Use an automated testing framework to compare prompt versions and document performance patterns. **(Most Voted)**
- **C.** Deploy each new prompt version to separate Amazon Bedrock API endpoints. Split production traffic between the endpoints. Configure Amazon CloudWatch to capture response metrics and user feedback for automatic version selection.
- **D.** Create a custom prompt evaluation flow in Amazon Bedrock Flows that applies the same clinical document inputs to different prompt variants. Use Amazon Comprehend Medical to analyze and score the factual accuracy of each version.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (62%)
- D (31%)
- A (8%)

---

## Question 30 - Topic 1

### 中文

一家公司使用 Amazon Bedrock 为客户生成技术内容。最近，模型在总结长技术文档时出现了大量幻觉，输出中包含不准确甚至虚构的细节。当前方案使用一个大型 Foundation Model（FM），采用基础 One-Shot Prompt，并把完整文档一次性放入输入。公司需要降低幻觉并达到事实准确率目标。方案必须每小时处理超过 1,000 份文档，并且每份摘要必须在 3 秒内生成。以下哪两个方案组合能够满足要求？（选择两项。）

- **A.** 使用 Zero-Shot Chain-of-Thought（CoT）指令，要求模型在生成每份摘要之前执行逐步推理，并明确进行事实验证。 **（最高票）**
- **B.** 使用 Amazon Bedrock Knowledge Base 实现 Retrieval Augmented Generation（RAG）。采用 Semantic Chunking 和经过调优的 Embedding，让摘要严格以源内容为依据。 **（最高票）**
- **C.** 配置 Amazon Bedrock Guardrails，阻止任何符合“幻觉内容模式”的生成输出。
- **D.** 提高 Amazon Bedrock 中的 Temperature 参数。
- **E.** 继续要求 Amazon Bedrock 模型一次性总结完整文档。

### English

A company uses Amazon Bedrock to generate technical content for customers. The company has recently experienced a surge in hallucination outputs when the company's model generates summaries of long technical documents. The model outputs include inaccurate or fabricated details. The company's current solution uses a large foundation model (FM) with a basic one-shot prompt that includes the full document in a single input. The company needs a solution that will reduce hallucinations and meet factual accuracy goals. The solution must process more than 1,000 documents each hour and deliver summaries within 3 seconds for each document. Which combination of solutions will meet these requirements? (Choose two.)

- **A.** Implement zero-shot chain-of-thought (CoT) instructions that require step-by-step reasoning with explicit fact verification before the model generates each summary. **(Most Voted)**
- **B.** Use Retrieval Augmented Generation (RAG) with an Amazon Bedrock knowledge base. Apply semantic chunking and tuned embeddings to ground summaries in source content. **(Most Voted)**
- **C.** Configure Amazon Bedrock guardrails to block any generated output that matches patterns that are associated with hallucinated content.
- **D.** Increase the temperature parameter in Amazon Bedrock.
- **E.** Prompt the Amazon Bedrock model to summarize each full document in one pass.

**Correct Answer / 正确答案:** `AB`

**Community vote distribution / 社区投票分布:**

- AB (88%)

---

## Question 31 - Topic 1

### 中文

一家公司拥有一套推荐系统，应用运行在 Amazon EC2 实例上。应用会调用 Amazon Bedrock Foundation Model（FM）API，分析客户行为并生成个性化商品推荐。系统目前偶发异常：部分推荐结果与客户偏好不符。公司需要一种可观测性方案，用于监控运行指标，并相对于既定 Baseline 检测运行性能退化模式。当 FM 行为偏离预期模式时，方案还必须在 10 分钟内生成包含相关性数据的告警。哪种解决方案能够满足这些要求？

- **A.** 为应用基础设施配置 Amazon CloudWatch Container Insights。针对延迟阈值设置 CloudWatch Alarm。使用 CloudWatch Embedded Metric Format 添加 Token 数量自定义指标，并创建 CloudWatch Dashboard 展示数据。
- **B.** 实施 AWS X-Ray，跟踪请求在应用组件之间的流转。启用 CloudWatch Logs Insights 检测错误模式。设置 AWS CloudTrail 监控所有 Amazon Bedrock API 调用，并在 Amazon QuickSight 中创建自定义 Dashboard。
- **C.** 为应用资源启用 Amazon CloudWatch Application Insights。使用 CloudWatch Embedded Metric Format 创建推荐质量、Token 使用量和响应延迟自定义指标，并以请求类型和用户群体作为 Dimension。对模型指标配置 CloudWatch Anomaly Detection，并使用 CloudWatch Logs Insights 建立日志模式分析。 **（最高票）**
- **D.** 使用带 Observability Plugin 的 Amazon OpenSearch Service。通过 Amazon Kinesis 摄取模型指标和日志。创建自定义 Piped Processing Language（PPL）查询来分析模型行为模式，并建立运行 Dashboard 实时展示异常。

### English

A company has a recommendation system. The system's applications run on Amazon EC2 instances. The applications make API calls to Amazon Bedrock foundation models (FMs) to analyze customer behavior and generate personalized product recommendations. The system is experiencing intermittent issues. Some recommendations do not match customer preferences. The company needs an observability solution to monitor operational metrics and detect patterns of operational performance degradation compared to established baselines. The solution must also generate alerts with correlation data within 10 minutes when FM behavior deviates from expected patterns. Which solution will meet these requirements?

- **A.** Configure Amazon CloudWatch Container Insights for the application infrastructure. Set up CloudWatch alarms for latency thresholds. Add custom metrics for token counts by using the CloudWatch embedded metric format. Create CloudWatch dashboards to visualize the data.
- **B.** Implement AWS X-Ray to trace requests through the application components. Enable CloudWatch Logs Insights for error pattern detection. Set up AWS CloudTrail to monitor all API calls to Amazon Bedrock. Create custom dashboards in Amazon QuickSight.
- **C.** Enable Amazon CloudWatch Application Insights for the application resources. Create custom metrics for recommendation quality, token usage, and response latency by using the CloudWatch embedded metric format with dimensions for request types and user segments. Configure CloudWatch anomaly detection on the model metrics. Establish log pattern analysis by using CloudWatch Logs Insights. **(Most Voted)**
- **D.** Use Amazon OpenSearch Service with the Observability plugin. Ingest model metrics and logs by using Amazon Kinesis. Create custom Piped Processing Language (PPL) queries to analyze model behavior patterns. Establish operational dashboards to visualize anomalies in real time.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (88%)
- B (12%)

---

## Question 32 - Topic 1

### 中文

一个企业应用使用 Amazon Bedrock Foundation Model（FM）处理和分析 50～200 页的技术文档。当文档超过 FM 的 Context Window 上限时，用户会遇到回答不一致以及输出被截断的问题。哪种解决方案可以解决这个问题？

- **A.** 使用 Fixed-Size Chunking，将每个 Chunk 设置为 4,000 Token，并设置 20% Overlap。通过应用层逻辑按顺序连接多个 Chunk，直到接近 FM 最大 200,000 Token 的 Context Window，然后再发起推理调用。
- **B.** 使用 Hierarchical Chunking：Parent Chunk 为 8,000 Token，Child Chunk 为 2,000 Token。使用 Amazon Bedrock Knowledge Bases 内置检索，根据查询上下文自动选择相关 Parent Chunk，并配置 Overlap Token 维持语义连续性。
- **C.** 使用 Semantic Chunking，将 Breakpoint Percentile Threshold 设置为 95%，Buffer Size 设置为 3 个句子。使用 Amazon Bedrock `RetrieveAndGenerate` API，根据 Embedding Similarity Score 动态选择最相关的 Chunk。 **（最高票）**
- **D.** 创建预处理 AWS Lambda 函数，使用 FM 的 Tokenizer 分析文档 Token 数量。将文档平均切分成能够控制在 Context Window 80% 以内的片段，让 Lambda 分别独立处理每个片段，最后再汇总结果。

### English

An enterprise application uses an Amazon Bedrock foundation model (FM) to process and analyze 50 to 200 pages of technical documents. Users are experiencing inconsistent responses and receiving truncated outputs when processing documents that exceed the FM's context window limits. Which solution will resolve this problem?

- **A.** Configure fixed-size chunking at 4,000 tokens for each chunk with 20% overlap. Use application-level logic to link multiple chunks sequentially until the FM's maximum context window of 200,000 tokens is reached before making inference calls.
- **B.** Use hierarchical chunking with parent chunks of 8,000 tokens and child chunks of 2,000 tokens. Use Amazon Bedrock Knowledge Bases built-in retrieval to automatically select relevant parent chunks based on query context. Configure overlap tokens to maintain semantic continuity.
- **C.** Use semantic chunking with a breakpoint percentile threshold of 95% and a buffer size of 3 sentences. Use the Amazon Bedrock RetrieveAndGenerate API call to dynamically select the most relevant chunks based on embedding similarity scores. **(Most Voted)**
- **D.** Create a pre-processing AWS Lambda function that analyzes document token count by using the FM's tokenizer. Configure the lambda function to split documents into equal segments that fit within 80% of the context window. Configure the Lambda function to process each segment independently before aggregating the results.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (67%)
- B (25%)
- D (8%)

---

## Question 33 - Topic 1

### 中文

一家公司正在开发生成式 AI（GenAI）应用，实时分析客户服务通话，并为人工客服生成建议回复。在高峰期，应用必须处理 **500,000 路并发通话**，并且每条建议的端到端延迟必须低于 200 ms。公司已经有现成架构用于转录客户通话音频流。应用不得超过预定义的每月计算预算，同时还必须保留 Auto Scaling 能力。哪种解决方案能够满足这些要求？

- **A.** 在 Amazon Bedrock 上部署大型复杂推理模型，购买 Provisioned Throughput，并针对批处理进行优化。
- **B.** 在 Amazon Bedrock 上部署针对低延迟实时场景优化的模型，购买 Provisioned Throughput，并设置自动扩缩容策略。 **（最高票）**
- **C.** 在使用专用 GPU 实例的 Amazon SageMaker AI Real-Time Endpoint 上部署大型语言模型（LLM）。
- **D.** 在针对批处理优化的 Amazon SageMaker AI Serverless Endpoint 上部署中等规模语言模型。

### English

A company is developing a generative AI (GenAI) application that analyzes customer service calls in real-time and generates suggested responses for human customer service agents. The application must process 500,000 concurrent calls during peak hours with less than 200 ms end-to-end latency for each suggestion. The company uses existing architecture to transcribe customer call audio streams. The application must not exceed a pre-defined monthly compute budget and must maintain auto scaling capabilities. Which solution will meet these requirements?

- **A.** Deploy a large, complex reasoning model on Amazon Bedrock. Purchase provisioned throughput and optimize for batch processing.
- **B.** Deploy a low-latency, real-time optimized model on Amazon Bedrock. Purchase provisioned throughput and set up automatic scaling policies. **(Most Voted)**
- **C.** Deploy a large language model (LLM) on an Amazon SageMaker AI real-time endpoint that uses dedicated GPU instances.
- **D.** Deploy a mid-sized language model on an Amazon SageMaker AI serverless endpoint that is optimized for batch processing.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (75%)
- C (25%)

---

## Question 34 - Topic 1

### 中文

一家电商公司正在构建内部平台，使用 Amazon Bedrock Foundation Model（FM）开发生成式 AI 应用。开发人员需要根据与电商场景对齐的评估结果选择模型。平台必须在 Dashboard 中展示文本生成和摘要任务的准确率指标。公司已经拥有自定义电商数据集，可作为标准化评估输入。以下哪两个步骤组合能够以**最低运维开销**满足要求？（选择两项。）

- **A.** 将数据集导入 Amazon S3 Bucket。配置适当的 IAM 权限和 Cross-Origin Resource Sharing（CORS）权限，使 Evaluation Job 能够访问这些数据集。 **（最高票）**
- **B.** 将数据集导入 Amazon S3 Bucket。配置适当的 IAM 权限和 VPC Endpoint，使 Evaluation Job 能够访问这些数据集。
- **C.** 配置 AWS Lambda，按计划在 Amazon Bedrock 中创建 Model Evaluation Job，并将包含数据集的 S3 Bucket URI 作为输入。让评估任务对文本生成测量 Real World Knowledge（RWK）Score，对摘要任务测量 BERT Score。再配置第二个 Lambda 检查任务状态，并将自定义日志发布到 Amazon CloudWatch，最后创建自定义 CloudWatch Logs Insights Dashboard。 **（最高票）**
- **D.** 按计划使用 Amazon SageMaker Clarify 创建模型评估任务，并使用开源框架执行标准评估。将结果发布到 Amazon CloudWatch Namespace。文本生成使用 Word Error Rate，摘要使用 Toxicity 作为准确率指标。再使用 AWS Lambda 检查任务状态、写入 CloudWatch 日志，并建立 Logs Insights Dashboard。
- **E.** 按计划运行 Amazon SageMaker AI Notebook Job，使用 `fmevals` 或 `ragas` 框架评估 S3 中的数据集。Notebook 中编写 Python，直接调用 FM 的 `InvokeModel` API 并处理响应。将任务状态和结果发布到 Amazon CloudWatch Logs，文本生成测量 RWK Score，摘要测量 Toxicity，并建立 CloudWatch Logs Insights Dashboard。

### English

An ecommerce company is building an internal platform to develop generative AI applications by using Amazon Bedrock foundation models (FMs). Developers need to select models based on evaluations that are aligned to ecommerce use cases. The platform must display accuracy metrics for text generation and summarization in dashboards. The company has custom ecommerce datasets to use as standardized evaluation inputs. Which combination of steps will meet these requirements with the LEAST operational overhead? (Choose two.)

- **A.** Import the datasets to an Amazon S3 bucket. Provide appropriate IAM permissions and cross-origin resource sharing (CORS) permissions to give the evaluation jobs access to the datasets. **(Most Voted)**
- **B.** Import the datasets to an Amazon S3 bucket. Provide appropriate IAM permissions and a VPC endpoint configuration to give the evaluation jobs access to the datasets.
- **C.** Configure an AWS Lambda function to create model evaluation jobs on a schedule in the Amazon Bedrock console. Provide the URI of the S3 bucket that contains the datasets as an input. Configure the evaluation jobs to measure the real world knowledge (RWK) score for text generation and BERT Score for summarization. Configure a second Lambda function to check the status of the jobs and publish custom logs to Amazon CloudWatch. Create a custom Amazon CloudWatch Logs Insights dashboard. **(Most Voted)**
- **D.** Use Amazon SageMaker Clarify on a schedule to create model evaluation jobs. Use open source frameworks to create and run standardized evaluations. Publish results to Amazon CloudWatch namespaces. Use the word error rate score for text generation and toxicity for summarization as metrics for accuracy. Configure an AWS Lambda function to check the status of the jobs and publish custom logs to CloudWatch. Create a custom Amazon CloudWatch Logs Insights dashboard.
- **E.** Run an Amazon SageMaker AI notebook job on a schedule by using the fmevals or ragas framework to run evaluations that use the datasets in the S3 bucket. Write Python code in the notebook that makes direct InvokeModel API calls to the FMs and processes their responses for evaluation. Publish job status and results to Amazon CloudWatch Logs to measure the real world knowledge (RWK) score for text generation and toxicity for summarization as metrics for accuracy. Create a custom CloudWatch Logs Insights dashboard.

**Correct Answer / 正确答案:** `AC`

**Community vote distribution / 社区投票分布:**

- AC (75%)
- BC (19%)

---

## Question 35 - Topic 1

### 中文

一家电梯服务公司使用 Amazon Bedrock 开发了 AI 助手，为电梯维修技术人员生成维护建议。公司使用 Amazon Kinesis Data Streams 收集电梯传感器数据。新的监管要求规定，**所有 AI 生成的维修建议都必须由人工技术人员审核**。公司需要建立 Human Oversight（人工监督）工作流，让技术人员审核并批准 AI 建议，同时所有人工审核决定都必须保存用于审计。哪种解决方案能够满足这些要求？

- **A.** 使用 AWS Lambda 和 Amazon SQS 自定义构建人工审批工作流，并将所有审核决定存储在 Amazon DynamoDB 中用于审计。
- **B.** 创建 AWS Step Functions 工作流，在人工审批步骤使用 `waitForTaskToken` API 暂停执行。人工技术人员完成审核后，使用 AWS Lambda 调用带审批决定的 `SendTaskSuccess` API。将所有审核决定存储在 Amazon DynamoDB 中。 **（最高票）**
- **C.** 创建带人工审批步骤的 AWS Glue Workflow。技术人员审核后，让应用通过 AWS Lambda 调用 `SendTaskSuccess` API，并把所有审核决定存储在 Amazon DynamoDB 中。
- **D.** 配置带自定义 Event Pattern 的 Amazon EventBridge Rule，将 AI 建议路由给人工技术人员审核。创建 AWS Glue Job 处理技术人员审批队列，并使用 Amazon ElastiCache 缓存所有审核决定。

### English

An elevator service company has developed an AI assistant application by using Amazon Bedrock. The application generates elevator maintenance recommendations to support the company's elevator technicians. The company uses Amazon Kinesis Data Streams to collect the elevator sensor data. New regulatory rules require that a human technician must review all AI-generated recommendations. The company needs to establish human oversight workflows to review and approve AI recommendations. The company must store all human technician review decisions for audit purposes. Which solution will meet these requirements?

- **A.** Create a custom approval workflow by using AWS Lambda functions and Amazon SQS queues for human review of AI recommendations. Store all review decisions in Amazon DynamoDB for audit purposes.
- **B.** Create an AWS Step Functions workflow that has a human approval step that uses the waitForTaskToken API to pause execution. After a human technician completes a review, use an AWS Lambda function to call the SendTaskSuccess API that has the approval decision. Store all review decisions in Amazon DynamoDB. **(Most Voted)**
- **C.** Create an AWS Glue workflow that has a human approval step. After the human technician review, integrate the application with an AWS Lambda function that calls the SendTaskSuccess API. Store all human technician review decisions in Amazon DynamoDB.
- **D.** Configure Amazon EventBridge rules with custom event patterns to route AI recommendations to human technicians for review. Create AWS Glue jobs to process human technician approval queues. Use Amazon ElastiCache to cache all human technician review decisions.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (88%)
- A (12%)

---

## Question 36 - Topic 1

### 中文

一家银行正在构建生成式 AI（GenAI）贷款审批应用，使用 Amazon Bedrock 分析扫描版金融文档。应用必须从文档中提取结构化数据；必须在模型推理之前删除 Personally Identifiable Information（PII）；必须使用 Foundation Model（FM）生成审批结果；当文档提取置信度较低时，必须把结果路由给与贷款申请人位于**同一 AWS Region** 的人工审核员。公司还必须满足严格的 Region 数据驻留与可审计性要求。应用需要扩展到每天处理 25,000 份申请，并提供 99.9% 可用性。以下哪三个方案组合能够满足要求？（选择三项。）

- **A.** 在同一 Region 内部署 Amazon Textract 和 Amazon Augmented AI（Amazon A2I），从扫描文档中提取相关数据，并把低置信度页面路由给人工审核员。 **（最高票）**
- **B.** 在推理之前使用 AWS Lambda 检测并删除提交文档中的 PII。应用 Amazon Bedrock Guardrails 防止模型输出不适当或未授权内容。配置 Region-Specific IAM Role，强制执行数据驻留要求并控制对提取数据的访问。 **（最高票）**
- **C.** 在推理之前使用 Amazon Kendra 和 Amazon OpenSearch Service，从上传文档中通过语义方式提取字段级数值。
- **D.** 将上传文档存储在 Amazon S3 中并应用 Object Metadata。配置 IAM Policy，确保原始文档与每位申请人位于同一 Region，并启用 Object Tagging 以支持后续审计。
- **E.** 使用 AWS Glue Data Quality 验证结构化文档数据。使用 AWS Step Functions 编排审核工作流，其中包括 Prompt Engineering 步骤，把已验证数据转换为优化后的 Prompt，再调用 Amazon Bedrock 评估贷款申请。 **（最高票）**
- **F.** 使用 Amazon SageMaker Clarify，根据 Amazon Bedrock 作出的模型评分决策生成公平性和偏差报告。

### English

A bank is building a generative AI (GenAI) application that uses Amazon Bedrock to assess loan applications by using scanned financial documents. The application must extract structured data from the documents. The application must redact personally identifiable information (PII) before inference. The application must use foundation models (FMs) to generate approvals. The application must route low-confidence document extraction results to human reviewers who are within the same AWS Region as the loan applicant. The company must ensure that the application complies with strict Regional data residency and auditability requirements. The application must be able to scale to handle 25,000 applications each day and provide 99.9% availability. Which combination of solutions will meet these requirements? (Choose three.)

- **A.** Deploy Amazon Textract and Amazon Augmented AI (Amazon A2I) within the same Region to extract relevant data from the scanned documents. Route low-confidence pages to human reviewers. **(Most Voted)**
- **B.** Use AWS Lambda functions to detect and redact PII from submitted documents before inference. Apply Amazon Bedrock guardrails to prevent inappropriate or unauthorized content in model outputs. Configure Region-specific IAM roles to enforce data residency requirements and to control access to the extracted data. **(Most Voted)**
- **C.** Use Amazon Kendra and Amazon OpenSearch Service to extract field level values semantically from the uploaded documents before inference.
- **D.** Store uploaded documents in Amazon S3 and apply object metadata. Configure IAM policies to store original documents within the same Region as each applicant. Enable object tagging for future audits.
- **E.** Use AWS Glue Data Quality to validate the structured document data. Use AWS Step Functions to orchestrate a review workflow that includes a prompt engineering step that transforms validated data into optimized prompts before invoking Amazon Bedrock to assess loan applications. **(Most Voted)**
- **F.** Use Amazon SageMaker Clarify to generate fairness and bias reports based on model scoring decisions that Amazon Bedrock makes.

**Correct Answer / 正确答案:** `ABD`

**Community vote distribution / 社区投票分布:**

- ABE (56%)
- ADE (22%)

---

## Question 37 - Topic 1

### 中文

一家软件公司正在使用 Amazon Q Business 构建 AI 助手，让员工通过自然语言 Prompt 访问公司信息和个人信息。公司把这些信息存储在一个 Amazon S3 Bucket 中。S3 中每个部门都有独立 Prefix，每个 Object 名称都包含其所属部门的 S3 Prefix。每个部门只对应 AWS IAM Identity Center 中的一个 Group，每位员工也只属于一个部门。公司已经将该 S3 Bucket 配置为 Amazon Q Business 的数据源。公司需要确保 AI 助手能够根据用户的 IAM Identity Center Group Membership 正确执行访问控制。哪种方案能够以**最低运维开销**满足要求？

- **A.** 在每个部门文件夹中创建一个名为 `acl.json` 的 JSON 文件，并在每个文件中创建 Access Control Entry，指定哪些 IAM Identity Center Group 可以访问该部门数据。然后在数据源设置的 Access Control 部分指定 JSON 文件位置。
- **B.** 在 S3 Bucket 顶层创建一个统一的 `acl.json` 文件。添加 Access Control Entry，将每个部门的 S3 Prefix 映射到对应 IAM Identity Center Group，并在数据源设置的 Access Control 部分指定该文件位置。
- **C.** 为每个 IAM Identity Center Group 分别创建 Permission Set，默认拒绝访问 S3 Bucket 中所有 Prefix。然后为各组添加 `StringNotEquals` Condition Key，指定该组所属部门，并将 Permission Set 附加到对应 Identity Center Group。
- **D.** 在 S3 Bucket 顶层创建 `metadata.json`，在文件中添加 `AccessControlList` Object，指定每个部门 Prefix 的 S3 Path 和可访问该 Prefix 的 IAM Identity Center Group，再在数据源 Metadata 设置中引用该文件位置。

### English

A software company is using Amazon Q Business to build an AI assistant that allows employees to access company information and personal information by using natural language prompts. The company stores this information in an Amazon S3 bucket. Each department in the company has a dedicated prefix in the S3 bucket. Each object name includes the S3 prefix of the department that it belongs to. Each department can belong to only a single group in AWS IAM Identity Center. Each employee belongs to a single department. The company configures Amazon Q Business to access data stored in an S3 bucket as a data source. The company needs to ensure that the AI assistant respects access controls based on the user's IAM Identity Center group membership. Which solution will meet this requirement with the LEAST operational overhead?

- **A.** Create a JSON file named acl.json in each department folder. In each file, create access control entries that specify the IAM Identity Center group that should have access to that department's data. Indicate the location of the JSON file in the Access Control section of the data source settings.
- **B.** Create a single JSON file named acl.json at the top level of the S3 bucket. Add access control entries that map each department's S3 prefix to its corresponding IAM Identity Center group. Indicate the location of the JSON file in the Access Control section of the data source settings.
- **C.** For each IAM Identity Center group, create a separate permissions set that denies access to all prefixes in the S3 bucket. Add a StringNotEquals condition key to the permissions set for each group that specifies the department each group is associated with. Attach the permissions sets to the Identity Center groups.
- **D.** Create a metadata file named metadata.json at the top level of the S3 bucket. Add an AccessControlList object to the file that specifies the S3 path of each department's prefix. Specify the IAM Identity Center group that should have access to each department's prefix. Reference the file location in the data source metadata settings.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 38 - Topic 1

### 中文

一家医疗保健公司正在使用 Amazon Bedrock 构建帮助医务人员作出临床决策的系统。系统必须**仅依据经过批准的医学文档**向医生提供治疗建议，并且必须引用具体来源；系统不得产生幻觉或事实错误。哪种方案能够以**最低运维开销**满足要求？

- **A.** 将 Amazon Bedrock 与 Amazon Kendra 集成，检索已批准文档。自定义实现后处理逻辑，将生成响应与源文档进行比较并添加引用。
- **B.** 部署 Amazon Bedrock Knowledge Base，并连接经过批准的临床源文档。使用 Amazon Bedrock `RetrieveAndGenerate` API，让知识库直接返回引用信息。 **（最高票）**
- **C.** 使用 Amazon Bedrock 和 Amazon Comprehend Medical 提取医学实体，再实现与医学术语数据库进行比对的验证逻辑。
- **D.** 使用 Amazon Bedrock Knowledge Base，通过 `Retrieve` API 和 `InvokeModel` API 检索经过批准的临床源文档，再自定义实现验证逻辑，对照检索来源并添加引用。

### English

A healthcare company is using Amazon Bedrock to build a system to help practitioners make clinical decisions. The system must provide treatment recommendations to physicians based only on approved medical documentation and must cite specific sources. The system must not hallucinate or produce factually incorrect information. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Integrate Amazon Bedrock with Amazon Kendra to retrieve approved documents. Implement custom post-processing to compare generated responses against source documents and to include citations.
- **B.** Deploy an Amazon Bedrock knowledge base and connect it to approved clinical source documents. Use the Amazon Bedrock RetrieveAndGenerate API to return citations from the knowledge base. **(Most Voted)**
- **C.** Use Amazon Bedrock and Amazon Comprehend Medical to extract medical entities. Implement verification logic against a medical terminology database.
- **D.** Use an Amazon Bedrock knowledge base with Retrieve API calls and InvokeModel API calls to retrieve approved clinical source documents. Implement verification logic to compare against retrieved sources and to cite sources.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (83%)
- C (17%)

---

## Question 39 - Topic 1

### 中文

一家金融服务公司正在开发实时生成式 AI（GenAI）助手，为人工呼叫中心坐席提供支持。GenAI 助手必须实时转录客户语音、分析上下文，并在客户仍在说话时持续向客服坐席提供增量建议。为了保证响应速度，从语音到首条响应显示的端到端延迟必须低于 1 秒。架构只能使用 AWS 托管服务，并且必须支持 Bidirectional Streaming（双向流式通信），确保客服坐席实时收到更新。哪种解决方案能够满足这些要求？

- **A.** 使用 Amazon Transcribe Streaming API 转录通话。将文本传给 Amazon Comprehend 执行情感分析，再通过 `InvokeModel` API 将结果发送给 Amazon Bedrock 上的 Anthropic Claude。将结果存入 Amazon DynamoDB，并使用 WebSocket API 展示。
- **B.** 使用 Amazon Transcribe Streaming，并启用 Partial Results，在客户说完之前就输出部分转录文本。通过 `InvokeModelWithResponseStream` API 将文本片段转发给 Amazon Bedrock，再通过 Amazon API Gateway WebSocket API 将模型响应流式推送给客服坐席。 **（最高票）**
- **C.** 使用 Amazon Transcribe Batch Processing 将通话转换为文本。使用 `ConverseStream` API 把完整转录发送给 Amazon Bedrock 上的 Anthropic Claude，再通过 Amazon Lex 聊天机器人界面向客服坐席返回响应。
- **D.** 使用 Amazon Transcribe Streaming API 配合 AWS Lambda 转录每个音频片段。让 Lambda 通过 `InvokeModel` API 调用 Amazon Bedrock 上的 Amazon Titan Embeddings，并把结果发布到 Amazon SNS Topic，让客服坐席订阅该 Topic。

### English

A financial services company is developing a real-time generative AI (GenAI) assistant to support human call center agents. The GenAI assistant must transcribe live customer speech, analyze context, and provide incremental suggestions to call center agents while a customer is still speaking. To preserve responsiveness, the GenAI assistant must maintain end-to-end latency under 1 second from speech to initial response display. The architecture must use only managed AWS services and must support bidirectional streaming to ensure that call center agents receive updates in real time. Which solution will meet these requirements?

- **A.** Use the Amazon Transcribe streaming API to transcribe calls. Pass the text to Amazon Comprehend to perform sentiment analysis. Feed the results to Anthropic Claude on Amazon Bedrock by using the InvokeModel API. Store results in Amazon DynamoDB. Use a WebSocket API to display the results.
- **B.** Use Amazon Transcribe streaming with partial results enabled to deliver fragments of transcribed text before customers finish speaking. Forward text fragments to Amazon Bedrock by using the InvokeModelWithResponseStream API. Stream responses to call center agents through an Amazon API Gateway WebSocket API. **(Most Voted)**
- **C.** Use Amazon Transcribe batch processing to convert calls to text. Pass complete transcripts to Anthropic Claude on Amazon Bedrock by using the ConverseStream API. Return responses through an Amazon Lex chatbot interface that call center agents can access from their work computers.
- **D.** Use the Amazon Transcribe streaming API with an AWS Lambda function to transcribe each audio segment. Configure the Lambda function to call the Amazon Titan Embeddings model on Amazon Bedrock by using the InvokeModel API. Configure the Lambda function to publish results to an Amazon SNS topic. Subscribe the call center agents to the SNS topic.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 40 - Topic 1

### 中文

一家媒体公司正在推出一个平台，每小时允许数千名用户上传图片和文本内容。平台使用 Amazon Bedrock 处理这些上传内容并生成创意作品。公司需要确保平台不会处理或生成不适当内容，同时生成作品中不得暴露 Personally Identifiable Information（PII）。解决方案还必须与公司现有 Amazon S3 存储工作流集成。哪种方案能够以**最低基础设施管理开销**满足要求？

- **A.** 启用 Enhanced Monitoring 工具。使用 Amazon CloudWatch Alarm 过滤进入平台的流量。使用 Amazon Comprehend PII Detection 对数据进行预处理，并创建 CloudWatch Alarm 监控 PII Detection 事件。再创建包含 Amazon Rekognition Image Moderation 步骤的 AWS Step Functions 工作流。
- **B.** 使用带 Request Validation Template 的 Amazon API Gateway HTTP API，在上传内容存入 Amazon S3 之前进行筛查。使用 Amazon SageMaker AI 构建自定义内容审核模型，在将处理后的内容发送到 Amazon Bedrock 之前进行审核。
- **C.** 创建 Amazon Cognito User Pool，并使用 Pre-Authentication AWS Lambda 执行内容审核检查。使用 Amazon Textract 过滤文本内容，使用 Amazon Rekognition 过滤图片内容，然后才允许用户上传到平台。
- **D.** 创建 AWS Step Functions 工作流，使用内置 Amazon Bedrock Guardrails 过滤内容。使用 Amazon Comprehend PII Detection 预处理内容，并使用 Amazon Rekognition 执行图片审核。 **（最高票）**

### English

A media company is launching a platform that allows thousands of users every hour to upload images and text content. The platform uses Amazon Bedrock to process the uploaded content to generate creative compositions. The company needs a solution to ensure that the platform does not process or produce inappropriate content. The platform must not expose personally identifiable information (PII) in the compositions. The solution must integrate with the company's existing Amazon S3 storage workflow. Which solution will meet these requirements with the LEAST infrastructure management overhead?

- **A.** Enable the Enhanced Monitoring tool. Use an Amazon CloudWatch alarm to filter traffic to the platform. Use Amazon Comprehend PII detection to pre-process the data. Create a CloudWatch alarm to monitor for Amazon Comprehend PII detection events. Create an AWS Step Functions workflow that includes an Amazon Rekognition image moderation step.
- **B.** Use an Amazon API Gateway HTTP API with request validation templates to screen content before storing the uploaded content in Amazon S3. Use Amazon SageMaker AI to build custom content moderation models that process content before sending the processed content to Amazon Bedrock.
- **C.** Create an Amazon Cognito user pool that uses pre-authentication AWS Lambda functions to run content moderation checks. Use Amazon Textract to filter text content and Amazon Rekognition to filter image content before allowing users to upload content to the platform.
- **D.** Create an AWS Step Functions workflow that uses built-in Amazon Bedrock guardrails to filter content. Use Amazon Comprehend PII detection to pre-process the content. Use Amazon Rekognition image moderation. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 41 - Topic 1

### 中文

一家公司已经为所有开发人员配置了 Amazon Q Developer Pro License。公司维护了一份开发应用时必须使用的“已批准资源”清单，其中包括内部 Library、专有算法技术，以及符合公司既定代码风格的示例代码。一个新的开发团队正在使用 Amazon Q Developer 开发 Java 应用。公司必须确保该团队使用这些已批准资源，但又不希望对每个项目进行 Project-Level 修改。哪种解决方案能够满足这些要求？

- **A.** 创建 Git Repository，存放所有已批准的内部 Library、算法和代码示例。将该 Repository 作为 Workspace 的一部分放入应用项目本地，并要求开发人员使用 `@workspace` Context 从该 Repository 获取建议。
- **B.** 在项目根目录创建 `.amazonq/rules` 文件夹，并把已批准的内部 Library、算法和代码示例放入其中。
- **C.** 在应用项目中创建名为 `rules` 的文件夹，将指导原则和代码存入其中，供 Amazon Q Developer 在生成代码建议时参考。
- **D.** 创建 Amazon Q Developer Customization，并把已批准的数据源纳入该 Customization。确保开发人员使用该 Customization 开发应用。 **（最高票）**

### English

A company has set up Amazon Q Developer Pro licenses for all developers at the company. The company maintains a list of approved resources that developers must use when developing applications. The approved resources include internal libraries, proprietary algorithmic techniques, and sample code with approved styling. A new team of developers is using Amazon Q Developer to develop a new Java-based application. The company must ensure that the new developer team uses the company's approved resources. The company does not want to make project-level modifications. Which solution will meet these requirements?

- **A.** Create a Git repository that contains all of the approved internal libraries, algorithms, and code samples. Include this Git repository in the application project locally as part of the workspace. Ensure that the developers use the @workspace context to retrieve suggestions from the Git repository.
- **B.** In the project root folder, create a folder named .amazonq/rules. Add the approved internal libraries, algorithms, and code samples to the folder.
- **C.** Create a folder in the application project named rules. Store the guidelines and code in the folder for Amazon Q Developer to reference product code suggestions.
- **D.** Create an Amazon Q Developer customization that includes the approved data sources. Ensure that the developers use the customization to develop the application. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 42 - Topic 1

### 中文

一家电商公司正在使用 Amazon Bedrock 构建客户服务 AI 助手。AI 助手每天需要处理超过 50,000 条客户咨询，在促销活动期间偶尔会出现峰值，每天最高达到 150,000 条。分析发现，有 40% 的咨询具有相似模式，并共享相同上下文。一名 GenAI Developer 必须设计方案，确保 AI 助手在流量峰值期间仍保持低延迟和稳定性能。哪种解决方案**最具成本效益**？

- **A.** 在发送到 Amazon Bedrock 的请求 Performance Configuration 中，将 Latency 参数设置为 `optimized`，启用 Latency-Optimized Inference；同时使用 Prompt Caching 处理重复性咨询。 **（最高票）**
- **B.** 购买足以覆盖峰值流量的 Provisioned Throughput 和 Model Unit（MU），并使用 Amazon ElastiCache（Redis OSS）缓存重复咨询。
- **C.** 使用 Amazon Bedrock Agents 和自定义 Knowledge Base 预处理客户咨询，并配置 Cross-Region Inference 分散流量。
- **D.** 使用 AWS Lambda，通过自定义 Prompt Routing 机制预处理请求，并使用 Amazon DynamoDB 作为缓存层处理常见问题。

### English

An ecommerce company is using Amazon Bedrock to build a customer service AI assistant. The AI assistant needs to process over 50,000 customer inquiries every day. The AI assistant occasionally experiences traffic spikes of up to 150,000 inquiries every day during promotional events. Analysis shows that 40% of inquiries follow similar patterns that share the same context. A GenAI developer must design a solution that will ensure low latency and consistent performance for the AI assistant during traffic spikes. Which solution will meet these requirements MOST cost-effectively?

- **A.** Configure latency-optimized inference by setting the latency parameter to optimized in the performance configuration of the request to Amazon Bedrock. Use prompt caching to handle the repetitive inquiries. **(Most Voted)**
- **B.** Purchase provisioned throughput and model units (MUs) that are sized to handle peak traffic loads. Use Amazon ElastiCache (Redis OSS) to cache repetitive inquiries.
- **C.** Use Amazon Bedrock Agents and custom knowledge bases to pre-process customer inquiries. Configure cross-Region inference to distribute traffic.
- **D.** Use AWS Lambda functions to pre-process requests by using a custom prompt routing mechanism. Use Amazon DynamoDB as a caching layer to handle frequently asked questions.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (86%)
- B (14%)

---

## Question 43 - Topic 1

### 中文

一家法律研究公司拥有一个使用 Amazon Bedrock 和 Amazon OpenSearch Service 的 Retrieval Augmented Generation（RAG）应用。该应用为 1,500 万份法律文档存储 768 维向量 Embedding，文档包括法规、法院判决和案件摘要。当前 Chunking 策略按固定长度切分，每个 Chunk 为 500 Token。这种做法经常把上下文紧密相关的内容拆到不同 Chunk 中，例如完整的法律论证、法院意见或法规引用。研究人员反馈，生成结果经常遗漏关键上下文，或引用已经过时的法律信息。近期日志显示，响应时间增加了 40%，P95 延迟已经超过 2 秒。公司预计一年内存储需求将从 90 GB 增长到 360 GB。公司需要在大规模场景下同时提高检索相关性和系统性能。哪种解决方案能够满足要求？

- **A.** 在不改变现有 Chunking 或预处理策略的情况下，将 Embedding 向量维度从 768 提高到 4,096。
- **B.** 使用存储在 Amazon S3 中的静态预写摘要替代动态检索，并通过 Amazon CloudFront 提供摘要，以降低计算需求并提高可预测性。
- **C.** 更新 Chunking 策略，不再按固定 Token 数切分，而是按完整法律论证、条款或章节等 Semantic Boundary（语义边界）切分；随后重新生成向量 Embedding，使其与新的 Chunk 结构一致。
- **D.** 从 OpenSearch Service 迁移到 Amazon DynamoDB，并实现基于关键词的索引，以更快查询法律概念。

### English

A legal research company has a Retrieval Augmented Generation (RAG) application that uses Amazon Bedrock and Amazon OpenSearch Service. The application stores 768-dimensional vector embeddings for 15 million legal documents, including statutes, court rulings, and case summaries. The company's current chunking strategy segments text into fixed-length blocks of 500 tokens. The current chunking strategy often splits contextually linked information such as legal arguments, court opinions, or statute references across separate chunks. Researchers report that generated outputs frequently omit key context or cite outdated legal information. Recent application logs show a 40% increase in response times. The p95 latency metric exceeds 2 seconds. The company expects storage needs for the application to grow from 90 GB to 360 GB within a year. The company needs a solution to improve retrieval relevance and system performance at scale. Which solution will meet these requirements?

- **A.** Increase the embedding vector dimensionality from 768 to 4,096 without changing the existing chunking or pre-processing strategy.
- **B.** Replace dynamic retrieval with static, pre-written summaries that are stored in Amazon S3. Use Amazon CloudFront to serve the summaries to reduce compute demand and improve predictability.
- **C.** Update the chunking strategy to use semantic boundaries such as complete legal arguments, clauses, or sections rather than fixed token limits. Regenerate vector embeddings to align with the new chunk structure.
- **D.** Migrate from OpenSearch Service to Amazon DynamoDB. Implement keyword-based indexes to enable faster lookups for legal concepts.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 44 - Topic 1

### 中文

一家公司正在开发由生成式 AI（GenAI）驱动的客户支持应用，使用 Amazon Bedrock Foundation Model（FM）。应用必须在同一用户的多次交互之间保持对话上下文；对于含义模糊的用户查询，必须运行 Clarification Workflow（澄清工作流）；公司还必须保存每个用户对话的加密记录，用于个性化。应用需要同时处理数千名并发用户，并快速响应每位用户。哪种解决方案能够满足这些要求？

- **A.** 使用 AWS Step Functions Express Workflow 编排对话流程。调用 AWS Lambda 执行澄清逻辑。将对话历史存储在 Amazon RDS 中，并使用 Session ID 作为 Primary Key。
- **B.** 使用 AWS Step Functions Standard Workflow 编排澄清工作流，并采用 Wait for a Callback Pattern 管理流程。将对话历史存储在 Amazon DynamoDB 中，使用 On-Demand Capacity，并配置 Server-Side Encryption。 > 注：原始题库此处存在文本连写/排版错误（`Amazon DynamoDPurchase...`），此处按原句明显表达的服务与配置关系翻译。 **（最高票）**
- **C.** 使用 Amazon API Gateway REST API 部署应用，把用户请求路由到 AWS Lambda，由 Lambda 更新和检索对话上下文。将对话历史存储在 Amazon S3，并配置 Server-Side Encryption；每次交互分别保存为独立 JSON 文件。
- **D.** 使用 AWS Lambda 调用 Amazon Bedrock Inference API。使用 Amazon SQS 编排澄清步骤。将对话历史存储在 Amazon ElastiCache（Redis OSS）Cluster 中，并配置静态加密。

### English

A company is developing a generative AI (GenAI)-powered customer support application that uses Amazon Bedrock foundation models (FMs). The application must maintain conversational context across multiple interactions with the same user. The application must run clarification workflows to handle ambiguous user queries. The company must store encrypted records of each user conversation to use for personalization. The application must be able to handle thousands of concurrent users while responding to each user quickly. Which solution will meet these requirements?

- **A.** Use an AWS Step Functions Express workflow to orchestrate conversation flow. Invoke AWS Lambda functions to run clarification logic. Store conversation history in Amazon RDS and use session IDs as the primary key.
- **B.** Use an AWS Step Functions Standard workflow to orchestrate clarification workflows. Include Wait for a Callback patterns to manage the workflows. Store conversation history in Amazon DynamoDPurchase on-demand capacity and configure server-side encryption. **(Most Voted)**
- **C.** Deploy the application by using an Amazon API Gateway REST API to route user requests to an AWS Lambda function to update and retrieve conversation context. Store conversation history in Amazon S3 and configure server-side encryption. Save each interaction as a separate JSON file.
- **D.** Use AWS Lambda functions to call Amazon Bedrock inference APIs. Use Amazon SQS queues to orchestrate clarification steps. Store conversation history in an Amazon ElastiCache (Redis OSS) cluster. Configure encryption at rest.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (60%)
- A (30%)
- D (10%)

---

## Question 45 - Topic 1

### 中文

一家金融服务公司需要对客户对话记录、金融报告和文档等非结构化数据进行预处理。这些数据存储在 Amazon S3 中，用于支持 Amazon Bedrock 应用。公司必须验证数据质量、创建可审计的 Metadata、监控数据指标，并且能够自定义 Text Chunking 以优化 Foundation Model（FM）的表现。哪种方案能够以**最少开发工作量**满足要求？

- **A.** 使用 Amazon SageMaker Data Wrangler 创建 Data Flow。配置 Amazon CloudWatch Metric 和 Alarm 监控数据质量。使用自定义 AWS Lambda 预处理数据，然后将处理后的数据加载到 Amazon Bedrock。
- **B.** 配置 AWS Glue Crawler 对数据源进行 Catalog。创建 AWS Glue ETL Job 运行自定义转换脚本。使用 AWS Glue Data Quality 验证并监控数据质量，再将处理后的数据加载到 Amazon Bedrock。
- **C.** 使用 Amazon Comprehend 提取 Entity。创建 AWS Lambda 对文本进行 Chunking。使用 Amazon Athena 查询并验证数据质量，再将处理后的数据加载到 Amazon Bedrock。
- **D.** 创建 AWS Step Functions 工作流编排数据预处理任务。在 Amazon EC2 上运行自定义代码处理数据，使用 Amazon SageMaker Model Monitor 监控数据质量，再把数据加载到 Amazon Bedrock。

### English

A financial services company needs to pre-process unstructured data such as customer transcripts, financial reports, and documentation. The company stores the unstructured data in Amazon S3 to support an Amazon Bedrock application. The company must validate data quality, create auditable metadata, monitor data metrics, and customize text chunking to optimize foundation model (FM) performance. Which solution will meet these requirements with the LEAST development effort?

- **A.** Use Amazon SageMaker Data Wrangler to create a data flow. Configure Amazon CloudWatch metrics and alarms to monitor data quality. Use a custom AWS Lambda function to pre-process the data. Load processed data into Amazon Bedrock.
- **B.** Set up an AWS Glue crawler to catalog data sources. Create AWS Glue ETL jobs to run custom transformation scripts. Use AWS Glue Data Quality to validate and monitor data quality. Load processed data into Amazon Bedrock.
- **C.** Use Amazon Comprehend to extract entities. Create an AWS Lambda function to chunk text. Run Amazon Athena to query and validate data quality. Load processed data into Amazon Bedrock.
- **D.** Create an AWS Step Functions workflow to orchestrate data pre-processing tasks. Run custom code on Amazon EC2 instances to process the data. Use Amazon SageMaker Model Monitor to monitor data quality. Load processed data into Amazon Bedrock.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 46 - Topic 1

### 中文

一家公司使用 Amazon Bedrock 构建 Retrieval Augmented Generation（RAG）系统。该系统使用 Amazon Bedrock Knowledge Base，数据源是一个保存突发新闻视频内容的 Amazon S3 Bucket。系统会从 S3 中检索转录文本、历史归档报告以及相关文档。RAG 系统已经使用先进的 Embedding Model 和高性能检索配置，但用户仍反馈响应缓慢、结果不相关，导致满意度下降。公司发现，Vector Search 正在跨过多内容类型、过长时间范围评估过多文档。公司确认，底层模型继续 Fine-Tuning 并不会带来收益。公司必须通过更合理的约束提高检索准确率，并希望尽量少修改现有架构。哪种解决方案能够满足要求？

- **A.** 使用专门针对突发新闻内容训练的 Domain-Adapted Model 来增强 Embedding，以提高向量相似度效果。
- **B.** 迁移到 Amazon OpenSearch Service，使用 Vector Field 和 Metadata Filter 限定检索结果范围。
- **C.** 在 Amazon Bedrock Knowledge Base 中启用 Metadata-Aware Filtering，并为 S3 Object Metadata 建立索引。 **（最高票）**
- **D.** 迁移到 Amazon Q Business Index，在检索过程中执行结构化 Metadata Filtering 和文档分类。

### English

A company uses Amazon Bedrock to build a Retrieval Augmented Generation (RAG) system. The RAG system uses an Amazon Bedrock knowledge base that is based on an Amazon S3 bucket as the data source for emergency news video content. The system retrieves transcripts, archived reports, and related documents from the S3 bucket. The RAG system uses state-of-the-art embedding models and a high-performing retrieval setup. However, users report slow responses and irrelevant results, which cause decreased user satisfaction. The company notices that vector searches are evaluating too many documents across too many content types and over long periods of time. The company determines that the underlying models will not benefit from additional fine tuning. The company must improve retrieval accuracy by applying smarter constraints. The company wants a solution that requires minimal changes to the existing architecture. Which solution will meet these requirements?

- **A.** Enhance embeddings by using a domain-adapted model that is specifically trained on emergency news content for improved vector similarity.
- **B.** Migrate to Amazon OpenSearch Service. Use vector fields and metadata filters to define the scope of results retrieval.
- **C.** Enable metadata-aware filtering within the Amazon Bedrock knowledge base by indexing S3 object metadata. **(Most Voted)**
- **D.** Migrate to an Amazon Q Business index to perform structured metadata filtering and document categorization during retrieval.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (88%)
- B (12%)

---

## Question 47 - Topic 1

### 中文

一家金融服务公司正在创建 Retrieval Augmented Generation（RAG）应用，使用 Amazon Bedrock 生成市场活动摘要。应用依赖一个 Vector Database，其中存储的是规模较小的专有数据集，索引数量较低。应用必须执行 Similarity Search；Amazon Bedrock 模型的响应还必须尽可能提高准确率并保持高性能。公司需要配置向量数据库并将其与应用集成。哪种方案能够满足要求？

- **A.** 启动 Amazon MemoryDB Cluster，并使用 Flat Algorithm 配置索引。根据性能指标配置 Horizontal Scaling Policy。 **（最高票）**
- **B.** 启动 Amazon MemoryDB Cluster，并使用 Hierarchical Navigable Small World（HNSW）Algorithm 配置索引。根据性能指标配置 Vertical Scaling Policy。
- **C.** 启动 Amazon Aurora PostgreSQL Cluster，并使用 Inverted File with Flat Compression（IVFFlat）Algorithm 配置索引。当负载增加时，将 Instance Class 扩展到更大规格。
- **D.** 启动 Amazon DocumentDB Cluster，使用 IVFFlat Index 和较高 Probe Value。将到 Cluster 的连接配置为 Replica Set，并把读取分散到 Replica Instance。

### English

A financial services company is creating a Retrieval Augmented Generation (RAG) application that uses Amazon Bedrock to generate summaries of market activities. The application relies on a vector database that stores a small proprietary dataset that has a low index count. The application must perform similarity searches. The Amazon Bedrock model's responses must maximize accuracy and maintain high performance. The company needs to configure the vector database and integrate it with the application. Which solution will meet these requirements?

- **A.** Launch an Amazon MemoryDB cluster and configure the index by using the Flat algorithm. Configure a horizontal scaling policy based on performance metrics. **(Most Voted)**
- **B.** Launch an Amazon MemoryDB cluster and configure the index by using the Hierarchical Navigable Small World (HNSW) algorithm. Configure a vertical policy based on performance metrics.
- **C.** Launch an Amazon Aurora PostgresSQL cluster and configure the index by using the Inverted File with Flat Compression (IVFFlat) algorithm. Configure the instance class to scale to a larger size when the load increases.
- **D.** Launch an Amazon DocumentDB cluster that has an Inverted File with Flat Compression (IVFFlat) index and a high probe value. Configure connections to the cluster as a replica set Distribute reads to replica instances.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (73%)
- B (27%)

---

## Question 48 - Topic 1

### 中文

一名 GenAI Developer 正在构建基于 Retrieval Augmented Generation（RAG）的客户支持应用，使用 Amazon Bedrock Foundation Model（FM）。应用需要处理 50 GB 的历史客户对话，这些数据以 JSON 文件形式存储在 Amazon S3 中，并将处理后的数据作为检索语料库。数据处理工作流必须从客户支持文档中提取相关数据、删除客户 Personally Identifiable Information（PII），并生成用于 Vector Store 的 Embedding。整个处理流程必须具有成本效益，并且在 4 小时内完成。哪种方案能够以**最低运维开销**满足要求？

- **A.** 使用 AWS Lambda 和 Amazon Comprehend 并行处理文件、删除 PII，并调用 Amazon Bedrock API 生成向量。通过调整 Lambda Concurrency Limit 和 Memory 优化吞吐量。
- **B.** 创建 AWS Glue ETL Job，在数据上运行 PII Detection Script。使用 Amazon SageMaker Processing 的 `HuggingFaceProcessor`，通过预训练模型生成 Embedding，再将 Embedding 存储到 Amazon OpenSearch Service。
- **C.** 部署 Amazon EMR Cluster，运行 Apache Spark，并使用调用 Amazon Comprehend 的 UDF 检测 PII。使用 Amazon Bedrock API 生成向量，再把输出存储到带 `pgvector` 扩展的 Amazon Aurora PostgreSQL。
- **D.** 实现数据处理 Pipeline，使用 AWS Step Functions 编排 Amazon Comprehend PII Detection 和 Amazon Bedrock Embedding Generation，并将工作流直接与 Amazon OpenSearch Serverless 集成，以存储向量并提供 Similarity Search。 **（最高票）**

### English

A GenAI developer is building a Retrieval Augmented Generation (RAG)-based customer support application that uses Amazon Bedrock foundation models (FMs). The application needs to process 50 GB of historical customer conversations that are stored in an Amazon S3 bucket as JSON files. The application must use the processed data as its retrieval corpus. The application's data processing workflow must extract relevant data from customer support documents, remove customer personally identifiable information (PII), and generate embeddings for vector storage. The processing workflow must be cost-effective and must finish within 4 hours. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Use AWS Lambda and Amazon Comprehend to process files in parallel, remove PII, and call Amazon Bedrock APIs to generate vectors. Configure Lambda concurrency limits and memory settings to optimize throughput.
- **B.** Create an AWS Glue ETL job to run PII detection scripts on the data. Use Amazon SageMaker Processing to run the HuggingFaceProcessor to generate embeddings by using a pre-trained model. Store the embeddings in Amazon OpenSearch Service.
- **C.** Deploy an Amazon EMR cluster that runs Apache Spark with user-defined functions (UDFs) that call Amazon Comprehend to detect PII. Use Amazon Bedrock APIs to generate vectors. Store outputs in Amazon Aurora PostgreSQL with the pgvector extension.
- **D.** Implement a data processing pipeline that uses AWS Step Functions to orchestrate a workload that uses Amazon Comprehend to detect PII and Amazon Bedrock to generate embeddings. Directly integrate the workflow with Amazon OpenSearch Serverless to store vectors and provide similarity search capabilities. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (79%)
- B (14%)

---

## Question 49 - Topic 1

### 中文

一家金融服务公司正在开发生成式 AI（GenAI）应用，同时服务 Premium Customer 和 Standard Customer。应用使用位于 Amazon API Gateway REST API 后端的 AWS Lambda 处理请求。公司需要根据用户所属客户等级动态切换 AI 模型，同时希望在**无需重新部署代码**的情况下对新功能执行 A/B Testing。公司还需要在应用配置变更之前，验证 Temperature、Maximum Token Limit 等模型参数。哪种方案能够以**最低运维开销**满足要求？

- **A.** 在 AWS Systems Manager Parameter Store 中为每种配置创建 Parameter。让 Lambda 轮询 Parameter 更新，并使用 Amazon EventBridge Event 在配置变化时触发重新部署。
- **B.** 将模型配置存储在 Amazon DynamoDB Table 中，并针对按客户等级获取配置的 Access Pattern 优化。每次请求开始时让 Lambda 查询 DynamoDB，以确定需要使用哪个模型。
- **C.** 使用 AWS AppConfig 管理模型配置。使用 Feature Flag 执行 A/B Testing。为模型参数定义 JSON Schema Validation Rule，并让 Lambda 通过 AWS AppConfig Agent 获取配置。 **（最高票）**
- **D.** 创建 Amazon ElastiCache（Redis OSS）Cluster 存储模型配置，设置较短 TTL。在 Lambda 中运行自定义验证逻辑，并使用 Amazon CloudWatch Metric 监控配置使用情况。

### English

A financial services company is developing a generative AI (GenAI) application that serves both premium customers and standard customers. The application uses AWS Lambda functions behind an Amazon API Gateway REST API to process requests. The company needs to dynamically switch between AI models based on which customer tier each user belongs to. The company also wants to perform A/B testing for new features without redeploying code. The company needs to validate model parameters like temperature and maximum token limits before applying changes. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Create an AWS Systems Manager Parameter Store parameters for each configuration. Use Lambda functions to poll for parameter updates. Use Amazon EventBridge events to trigger redeployments when configurations change.
- **B.** Store model configurations in Amazon DynamoDB tables. Optimize access patterns to retrieve configurations according to customer tier. Configure Lambda functions to query DynamoDB at the beginning of each request to determine which model to use.
- **C.** Use AWS AppConfig to manage model configurations. Use feature flags to perform A/B testing. Define JSON schema validation rules for model parameters. Configure Lambda functions to retrieve configurations by using the AWS AppConfig Agent. **(Most Voted)**
- **D.** Create an Amazon ElastiCache (Redis OSS) cluster to store model configurations. Set short TTL values. Run custom validation logic in Lambda functions. Use Amazon CloudWatch metrics to monitor configuration usage.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 50 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 和 Anthropic Claude 3 Haiku 开发 AI 助手。正常情况下，AI 助手每小时处理 10,000 个请求；高峰期可能激增至每小时 30,000 个请求。AI 助手必须在 2 秒内响应，并跨多个 AWS Region 运行。公司观察到，高峰期会出现吞吐瓶颈，从而导致延迟增加以及偶发请求超时。公司必须解决这些性能问题。哪种方案能够满足要求？

- **A.** 在单个 Region 中购买 Provisioned Throughput 和足够数量的 Model Unit（MU），并配置应用对失败请求进行指数退避重试。
- **B.** 实施 Token Batching 以减少 API Overhead，并使用 Cross-Region Inference Profile 自动把流量分散到可用 Region。 **（最高票）**
- **C.** 在每个 Region 中设置可自动扩缩的 AWS Lambda，使用客户端 Round-Robin 分发请求，并购买 1 个 Model Unit（MU）的 Provisioned Throughput 作为备用。
- **D.** 对所有请求使用 Batch Inference，并在多个 Region 使用 Amazon S3 Bucket；通过 Amazon SQS 建立异步检索流程。

### English

A company is using Amazon Bedrock and Anthropic Claude 3 Haiku to develop an AI assistant. The AI assistant normally processes 10,000 requests each hour but experiences surges of up 30,000 requests each hour during peak usage periods. The AI assistant must respond within 2 seconds while operating across multiple AWS Regions. The company observes that during peak usage periods, the AI assistant experiences throughput bottlenecks that cause increased latency and occasional request timeouts. The company must resolve the performance issues. Which solution will meet this requirement?

- **A.** Purchase provisioned throughput and sufficient model units (MUs) in a single Region. Configure the application to retry failed requests with exponential backoff.
- **B.** Implement token batching to reduce API overhead. Use cross-Region inference profiles to automatically distribute traffic across available Regions. **(Most Voted)**
- **C.** Set up auto scaling AWS Lambda functions in each Region. Implement client-side round-robin request distribution. Purchase one model unit (MU) of provisioned throughput as a backup.
- **D.** Implement batch inference for all requests by using Amazon S3 buckets across multiple Regions. Use Amazon SQS to set up an asynchronous retrieval process.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 51 - Topic 1

### 中文

一家公司使用 Amazon Bedrock 开发客户支持 AI 助手。分析发现，有 40% 的客户问题虽然措辞不同，但实际上是在询问同一个问题。公司希望减少重复的模型调用，并确保语义等价的问题得到一致答案，同时保持低延迟。哪种解决方案能够满足这些要求？

- **A.** 部署 Amazon DynamoDB Accelerator（DAX）作为内存缓存。将 Query Text 作为 Partition Key，将模型响应文本作为 Sort Key，并使用带 `LIKE` Operator 的 Filter Expression 查询缓存。
- **B.** 使用 Amazon Bedrock 为客户查询生成 Embedding。使用 Amazon MemoryDB for Valkey 存储向量 Embedding 和模型响应的 Hash Set，并使用 `RANGE` Query 查找相似查询及其响应。
- **C.** 部署支持 k-Nearest Neighbor（k-NN）的 Amazon OpenSearch Service，存储 Query-Response 文本对。使用 Approximate k-NN 技术查找相似问题及其响应。 **（最高票）**
- **D.** 使用 Amazon DynamoDB 构建缓存，并针对标准化后的 Query Text 创建 Global Secondary Index。对传入查询执行 Stemming，再查询已缓存客户问题的索引。

### English

A company uses Amazon Bedrock to develop an AI assistant to provide customer support. Analysis shows that 40% of customer queries use varied phrasing or wording to ask the same questions. The company wants a solution to reduce redundant model calls. The solution must ensure that semantically equivalent questions receive consistent answers. The solution must ensure low latency. Which solution will meet these requirements?

- **A.** Deploy an Amazon DynamoDB Accelerator (DAX) cluster as an in-memory cache. Specify the query text as the partition key and the model response text as the sort key. Query the cache by using a filter expression with the LIKE operator.
- **B.** Use Amazon Bedrock to generate embeddings from customer queries. Use Amazon MemoryDB for Valkey to store hash sets of vector embeddings and model responses. Use a RANGE query to find similar queries and their responses.
- **C.** Deploy Amazon OpenSearch Service that has k-nearest neighbor (k-NN) capabilities to store query-response text pairs. Use an approximate k-NN technique to find similar queries and their responses. **(Most Voted)**
- **D.** Create a caching solution by using Amazon DynamoDB to create a global secondary index on the normalized query text. Apply stemming to incoming queries. Query the index of cached customer queries.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 52 - Topic 1

### 中文

一家公司使用 AWS Lambda 构建 AI Agent 解决方案。一名 GenAI Developer 必须搭建一个能够访问用户信息的 Model Context Protocol（MCP）Server，并配置 AI Agent 使用这个新的 MCP Server。开发人员还必须确保只有经过授权的用户才能访问 MCP Server。哪种解决方案能够满足这些要求？

- **A.** 使用 Lambda 函数托管 MCP Server。授予 AI Agent 的 Lambda 函数调用该 MCP Server Lambda 的权限，并配置 Agent 的 MCP Client 以异步方式调用 MCP Server。
- **B.** 使用 Lambda 函数托管 MCP Server。授予 AI Agent Lambda 函数调用该 MCP Server Lambda 的权限，并配置 AI Agent 使用 STDIO Transport 与 MCP Server 通信。
- **C.** 使用 Lambda 函数托管 MCP Server。创建 Amazon API Gateway HTTP API，将请求代理到该 Lambda。配置 AI Agent 使用 Streamable HTTP Transport 通过 HTTP API 发起请求，并使用 Amazon Cognito 强制执行 OAuth 2.1。
- **D.** 使用 Lambda Layer 托管 MCP Server，并把该 Layer 添加到 AI Agent 的 Lambda 函数中。配置 Agentic AI 方案使用 STDIO Transport 向 MCP Server 发送请求；在 Agent 的 MCP 配置中，把 Lambda Layer ARN 指定为 Command，并把用户凭证放入环境变量。

### English

A company uses AWS Lambda functions to build an AI agent solution. A GenAI developer must set up a Model Context Protocol (MCP) server that accesses user information. The GenAI developer must also configure the AI agent to use the new MCP server. The GenAI developer must ensure that only authorized users can access the MCP server. Which solution will meet these requirements?

- **A.** Use a Lambda function to host the MCP server. Grant the AI agent Lambda functions permission to invoke the Lambda function that hosts the MCP server. Configure the AI agent's MCP client to invoke the MCP server asynchronously.
- **B.** Use a Lambda function to host the MCP server. Grant the AI agent Lambda functions permission to invoke the Lambda function that hosts the MCP server. Configure the AI agent to use the STDIO transport with the MCP server.
- **C.** Use a Lambda function to host the MCP server. Create an Amazon API Gateway HTTP API that proxies requests to the Lambda function. Configure the AI agent solution to use the Streamable HTTP transport to make requests through the HTTP API. Use Amazon Cognito to enforce OAuth 2.1.
- **D.** Use a Lambda layer to host the MCP server. Add the Lambda layer to the AI agent Lambda functions. Configure the agentic AI solution to use the STDIO transport to send requests to the MCP server. In the AI agent's MCP configuration, specify the Lambda layer ARN as the command. Specify the user credentials as environment variables.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 53 - Topic 1

### 中文

一家公司希望为客户创建年度奖励计划。客户能获得的奖励会根据不同参数变化，例如所购买商品的类别和客户历史购买记录。公司需要一个 GenAI 方案，在客户在线浏览商品目录时使用三个 Amazon Bedrock Agent 提供帮助。Agent 必须使用 Knowledge Base 和 Action Group，分别处理搜索、推荐和下单模块，并且这三个模块必须**按顺序执行**。此外，还必须由一个 AWS Lambda 函数计算每个推荐商品的预计奖励。系统在服务中断时必须能够 Graceful Degradation（优雅降级）。哪种方案具有**最高运维效率**？

- **A.** 在每个 Agent 后面分别定义 Amazon API Gateway REST API。创建第二个 Lambda 函数，负责统一编排三个 Agent 和奖励计算 Lambda 的调用，并在第二个 Lambda 中实现 Retry/Fallback 机制。
- **B.** 创建 AWS Step Functions State Machine，包含四个 Task，分别运行三个 Agent 和奖励计算 Lambda。为每个 Task 配置 Retry 和 Catch 分支。 **（最高票）**
- **C.** 为每个 Agent 分别配置独立 Retry/Fallback 机制。创建第二个 Lambda 函数编排三个 Agent 和奖励 Lambda，再在第二个 Lambda 后面定义 Amazon API Gateway REST API。
- **D.** 创建第二个 Lambda 函数编排三个 Agent 和奖励 Lambda。再创建只包含一个 Task 的 AWS Step Functions State Machine，让该 Task 运行第二个 Lambda，并为该 Task 配置 Retry 和 Catch。

### English

A company wants to create an annual rewards program for its customers. The rewards that customers earn vary based on different parameters such as the categories of the items ordered and the customers' purchase history. The company needs a generative AI (GenAI) solution that uses three Amazon Bedrock agents to help customers during online catalog browsing. The agents must use knowledge bases and action groups to handle the search, recommendation, and order modules. The modules must operate sequentially. An AWS Lambda function must calculate estimated rewards for each recommended item. The solution must provide graceful degradation during service disruptions. Which solution will meet these requirements with the MOST operational efficiency?

- **A.** Define an Amazon API Gateway REST API behind each agent. Create a second Lambda function to orchestrate the calls to the agents and the rewards Lambda function. Configure the second Lambda function with a retry/fallback mechanism.
- **B.** Create an AWS Step Functions state machine with four tasks that run the agents and the rewards Lambda function. Set up retry and catch branches for each of the task steps. **(Most Voted)**
- **C.** Configure each agent with a separate retry/fallback mechanism. Create a second Lambda function to orchestrate the calls to the agents and the rewards Lambda function. Define an Amazon API Gateway REST API behind the second Lambda function.
- **D.** Create a second Lambda function to orchestrate the calls to the agents and the rewards Lambda function. Create an AWS Step Functions state machine with one task that runs the second Lambda function. Set up retry and catch branches for the task step.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (80%)
- C (20%)

---

## Question 54 - Topic 1

### 中文

一家公司正在创建工作流，用于在面向客户的通信内容发送出去之前进行审核。公司使用预定义 Message Template 生成这些通信内容，并将其存储在 Amazon S3 Bucket 中。工作流需要从模板中提取一个特定部分并发送给 Amazon Bedrock 模型，再把模型响应存回原来的 S3 Bucket。哪种解决方案能够满足要求？

- **A.** 在 Amazon Bedrock Flows 中创建 Flow。分别在 Flow 的开头和结尾配置 S3 Action Node，用于获取通信内容和保存模型响应；在中间配置 Expression 解析每条通信，再配置 Agent Step 将解析后的输入发送给模型审核。
- **B.** 创建 AWS Step Functions Express Workflow State Machine。使用 Amazon S3 Integration 的 `GetObject` Step 获取原始通信内容；使用带 Intrinsic Function 的 `Pass` Step 解析通信内容，并把结果传给 Amazon Bedrock `InvokeModel` Step；最后使用 Amazon S3 Integration 的 `PutObject` Step 将模型响应写回 S3 Bucket。 **（最高票）**
- **C.** 创建带 Action Group 的 Amazon Bedrock Agent。在 Agent Instruction 中定义如何解析通信内容，并配置 Action Group 从 S3 获取内容、调用 Amazon Bedrock 模型，再把模型响应写回 S3。
- **D.** 创建只有一个 Action Group 的 Amazon Bedrock Agent，并在 Action Group 中配置三个 AWS Lambda 函数，分别负责从 S3 获取通信内容、解析内容并调用 Amazon Bedrock 模型，以及把模型响应写回 S3。

### English

A company is creating a workflow to review customer-facing communications before the company sends the communications. The company uses a pre-defined message template to generate the communications and stores the communications in an Amazon S3 bucket. The workflow needs to capture a specific portion from the template and send it to an Amazon Bedrock model. The workflow must store model responses back to the original S3 bucket. Which solution will meet these requirements?

- **A.** Create a flow in Amazon Bedrock Flows. Configure S3 action nodes at the beginning and end of the flow to retrieve and store the communications and the model responses. In the middle of the flow, configure an expression to parse each communication. Configure an agent step to send the parsed input to the model for review.
- **B.** Create an AWS Step Functions Express workflow state machine. Use an Amazon S3 integration GetObject step to retrieve the original communications. Use an intrinsic function Pass step to parse the communications and to pass the results to an Amazon Bedrock InvokeModel step. Configure an Amazon S3 integration PutObject step to store the model responses back to the S3 bucket. **(Most Voted)**
- **C.** Create an Amazon Bedrock agent that has an action group. Configure instructions to define how the agent should parse the communications. Configure the action group to retrieve the communications from the S3 bucket, invoke the Amazon Bedrock model, and store the model responses back to the S3 bucket.
- **D.** Create an Amazon Bedrock agent that has a single action group. Configure three AWS Lambda functions in the action group. Configure the functions to retrieve the communications from the S3 bucket, parse the communications and invoke the Amazon Bedrock model, and store the model responses back to the S3 bucket.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- A (50%)
- B (50%)

---

## Question 55 - Topic 1

### 中文

一家金融科技公司正在使用 Amazon Bedrock 构建针对客户服务 AI 助手的评估系统。该 AI 助手提供的金融建议必须事实准确、符合金融法规，并且在对话表达上得体。公司需要把**大规模自动质量评估**和**针对关键交互的定向人工审核**结合起来。哪种方案能够满足这些要求？

- **A.** 建立 Pipeline，让金融专家人工为所有响应的准确性、合规性和对话质量评分，再使用 Amazon SageMaker Notebook 分析结果并找出改进方向。
- **B.** 配置 Amazon Bedrock Evaluation，使用 Anthropic Claude Sonnet 作为 Judge Model 评估响应准确性和适当性。配置自定义 Amazon Bedrock Guardrails，检查响应是否符合金融政策，并针对被标记的关键交互加入 Amazon Augmented AI（Amazon A2I）人工审核。 **（最高票）**
- **C.** 创建 Amazon Lex Bot 管理客户服务交互。配置 AWS Lambda，根据静态合规数据库检查响应；在 Bot 中配置调用这些 Lambda 的 Intent，并增加一个额外 Intent 收集终端用户评价。
- **D.** 使用 Amazon CloudWatch 监控 AI 助手响应模式，为潜在合规违规配置 CloudWatch Alert，并建立人工评估团队审核被标记的交互。

### English

A financial technology company is using Amazon Bedrock to build an assessment system for the company's customer service AI assistant. The AI assistant must provide financial recommendations that are factually accurate, compliant with financial regulations, and conversationally appropriate. The company needs to combine automated quality evaluations at scale with targeted human reviews of critical interactions. What solution will meet these requirements?

- **A.** Configure a pipeline in which financial experts manually score all responses for accuracy, compliance, and conversational quality. Use Amazon SageMaker notebooks to analyze results to identify improvement areas.
- **B.** Configure Amazon Bedrock evaluations that use Anthropic Claude Sonnet as a judge model to assess response accuracy and appropriateness. Configure custom Amazon Bedrock guardrails to check responses for compliance with financial policies. Add Amazon Augmented AI (Amazon A2I) human reviews for flagged critical interactions. **(Most Voted)**
- **C.** Create an Amazon Lex bot to manage the customer service interactions. Configure AWS Lambda functions to check responses against a static compliance database. Configure intents in the bot that call the Lambda functions to check the responses. Add an additional intent to collect end-user reviews.
- **D.** Configure Amazon CloudWatch to monitor response patterns from the AI assistant. Configure CloudWatch alerts for potential compliance violations. Establish a team of human evaluators to review flagged interactions.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 56 - Topic 1

### 中文

一家医疗保健公司正在使用 Amazon Bedrock 开发实时患者服务 AI 助手，分别处理临床咨询、保险资格核验、预约安排和保险理赔等部门的问题。公司希望采用 Multi-Agent Architecture。AI 助手必须能够扩展，并便于以后为患者接入新功能；必须能同时处理数千个并行患者交互；同时要确保患者针对不同领域的问题得到正确的 Domain-Specific Response。哪种解决方案能够满足要求？

- **A.** 为每个 Agent 使用独立 Knowledge Base 隔离数据，并使用 IAM Filtering 控制对各 Knowledge Base 的访问。部署一个 Supervisor Agent，对患者咨询执行 Natural Language Intent Classification，然后将问题路由到专业 Collaborator Agent，由它们回答对应部门的问题。每个专业 Collaborator Agent 使用其部门专属 Knowledge Base 执行 Retrieval Augmented Generation（RAG）。 **（最高票）**
- **B.** 为每个部门分别创建 Supervisor Agent。每个部门内部再由多个 Collaborator Agent 对各专业领域执行自然语言意图分类，并只连接部门专属 Knowledge Base。不同 Supervisor Agent 之间使用人工 Handoff 流程。
- **C.** 为每个部门使用独立 Knowledge Base 隔离数据，并用 IAM Filtering 控制访问。只部署一个通用 Agent，在该 Agent 中配置多个 Action Group 执行不同部门功能，并在 Agent Instruction 中实现基于规则的路由逻辑。
- **D.** 为每个部门部署多个彼此独立、并行运行的 Supervisor Agent 来响应患者咨询。为每个 Supervisor Agent 配置多个 Collaborator Agent，但所有 Agent 共用同一个 Knowledge Base，再使用外部路由逻辑合并多个 Supervisor Agent 的响应。

### English

A healthcare company is using Amazon Bedrock to develop a real-time patient care AI assistant to respond to queries for separate departments that handle clinical inquiries, insurance verification, appointment scheduling, and insurance claims. The company wants to use a multi-agent architecture. The company must ensure that the AI assistant is scalable and can onboard new features for patients. The AI assistant must be able to handle thousands of parallel patient interactions. The company must ensure that patients receive appropriate domain-specific responses to queries. Which solution will meet these requirements?

- **A.** Isolate data for each agent by using separate knowledge bases. Use IAM filtering to control access to each knowledge base. Deploy a supervisor agent to perform natural language intent classification on patient inquiries. Configure the supervisor agent to route queries to specialized collaborator agents to respond to department-specific queries. Configure each specialized collaborator agent to use Retrieval Augmented Generation (RAG) with the agent's department-specific knowledge base. **(Most Voted)**
- **B.** Create a separate supervisor agent for each department. Configure individual collaborator agents to perform natural language intent classification for each specialty domain within each department. Integrate each collaborator agent with department-specific knowledge bases only. Implement manual handoff processes between the supervisor agents.
- **C.** Isolate data for each department in separate knowledge bases. Use IAM filtering to control access to each knowledge base. Deploy a single general-purpose agent. Configure multiple action groups within the general-purpose agent to perform specific department functions. Implement rule-based routing logic within the general-purpose agent instructions.
- **D.** Implement multiple independent supervisor agents that run in parallel to respond to patient inquiries for each department. Configure multiple collaborator agents for each supervisor agent. Integrate all agents with the same knowledge base. Use external routing logic to merge responses from multiple supervisor agents.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 57 - Topic 1

### 中文

一家公司使用 AI 助手应用总结公司网站内容并向客户提供信息，计划通过 Amazon Bedrock 让应用访问 Foundation Model（FM）。公司需要把 AI 助手分别部署到 Development Environment 和 Production Environment，并让两个环境都与 FM 集成。公司希望能够在每个环境中测试不同 FM 的效果，同时让 Product Owner 可以轻松切换 FM 进行测试。哪种解决方案能够满足这些要求？

- **A.** 创建一个 AWS CDK Application，再创建多个 AWS CodePipeline Pipeline。为每个 Pipeline 配置各自的 FM 设置，并让应用通过 `aws_bedrock.ProvisionedModel.fromProvisionedModelArn()` 调用 Amazon Bedrock FM。
- **B.** 为每个环境分别创建独立 AWS CDK Application。配置应用通过 `aws_bedrock.FoundationModel.fromFoundationModelId()` 调用 Amazon Bedrock FM，并为每个环境分别创建独立 AWS CodePipeline。
- **C.** 创建一个 AWS CDK Application。配置应用通过 `aws_bedrock.FoundationModel.fromFoundationModelId()` 调用 Amazon Bedrock FM。创建一个 AWS CodePipeline，其中为每个环境配置一个 Deployment Stage，并使用 AWS CodeBuild Deploy Action。
- **D.** 只为 Production Environment 创建一个 AWS CDK Application，并配置通过 `aws_bedrock.ProvisionedModel.fromProvisionedModelArn()` 调用 Amazon Bedrock FM。创建 AWS CodePipeline，并通过 AWS CodeBuild Deploy Action 部署生产环境；Development Environment 则参考生产应用代码手工重建资源。

### English

A company uses an AI assistant application to summarize the company's website content and provide information to customers. The company plans to use Amazon Bedrock to give the application access to a foundation model (FM). The company needs to deploy the AI assistant application to a development environment and a production environment. The solution must integrate the environments with the FM. The company wants to test the effectiveness of various FMs in each environment. The solution must provide product owners with the ability to easily switch between FMs for testing purposes in each environment. Which solution will meet these requirements?

- **A.** Create one AWS CDK application. Create multiple pipelines in AWS CodePipeline. Configure each pipeline to have its own settings for each FM. Configure the application to invoke the Amazon Bedrock FMs by using the aws_bedrock.ProvisionedModel.fromProvisionedModelArn() method.
- **B.** Create a separate AWS CDK application for each environment. Configure the applications to invoke the Amazon Bedrock FMs by using the aws_bedrock.FoundationModel.fromFoundationModelId() method. Create a separate pipeline in AWS CodePipeline for each environment.
- **C.** Create one AWS CDK application. Configure the application to invoke the Amazon Bedrock FMs by using the aws_bedrock.FoundationModel.fromFoundationModelId() method. Create a pipeline in AWS CodePipeline pipeline that has a deployment stage for each environment that uses AWS CodeBuild deploy actions.
- **D.** Create one AWS CDK application for the production environment. Configure the application to invoke the Amazon Bedrock FMs by using the aws_bedrock.ProvisionedModel.fromProvisionedModelArn() method. Create a pipeline in AWS CodePipeline. Configure the pipeline to deploy to the production environment by using an AWS CodeBuild deploy action. For the development environment, manually recreate the resources by referring to the production application code.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 58 - Topic 1

### 中文

一家酒店公司希望为旧有 Java Property Management System（PMS，物业/酒店管理系统）增加 AI 能力。公司希望使用 Amazon Bedrock Knowledge Bases，为员工提供房间可用性信息以及酒店专属资料。解决方案必须为公司管理的每家酒店维持彼此独立的访问控制；房间可用性必须接近实时；高峰期还必须维持稳定性能。哪种方案能够满足要求？

- **A.** 部署一个统一 Amazon Bedrock Knowledge Base，包含所有酒店的数据。配置 AWS Lambda，通过直接 API 连接从每家酒店的 PMS 数据库同步数据，并使用带酒店专属 Filter 的 AWS CloudTrail 日志审计各酒店数据访问。
- **B.** 为每家酒店创建 Amazon EventBridge Rule，在其 PMS 数据库发生变化时触发。让 Rule 将更新发送到 Management AWS Account 中的集中式 Amazon Bedrock Knowledge Base，并通过 Resource-Based Policy 强制执行酒店级访问控制。
- **C.** 在 Multi-Account Structure 中为每家酒店分别创建一个 Amazon Bedrock Knowledge Base。使用 Direct Data Ingestion 提供实时房间可用性，对不那么关键的信息按计划定期同步。 **（最高票）**
- **D.** 构建一个集中式 Amazon Bedrock Agent，并让它使用多个 Knowledge Base。实施 AWS IAM Identity Center，并通过酒店专属 Permission Set 控制员工的数据访问权限。

### English

A hotel company wants to enhance a legacy Java-based property management system (PMS) by adding AI capabilities. The company wants to use Amazon Bedrock Knowledge Bases to provide staff with room availability information and hotel-specific details. The solution must maintain separate access controls for each hotel that the company manages. The solution must provide room availability information in near real time and must maintain consistent performance during peak usage periods. Which solution will meet these requirements?

- **A.** Deploy a single Amazon Bedrock knowledge base that contains combined data for all hotels. Configure AWS Lambda functions to synchronize data from each hotel's PMS database through direct API connections. Implement AWS CloudTrail logging with hotel-specific filters to audit access logs for each hotel's data.
- **B.** Create an Amazon EventBridge rule for each hotel that is invoked by changes to the PMS database for each hotel. Configure the rule to send updates to a centralized Amazon Bedrock knowledge base in a management AWS account. Configure resource-based policies to enforce hotel-specific access controls for hotel staff.
- **C.** Implement one Amazon Bedrock knowledge base for each hotel in a multi-account structure. Use direct data ingestion to provide real-time room availability information. Schedule regular synchronization for less critical information. **(Most Voted)**
- **D.** Build a centralized Amazon Bedrock agent that uses multiple knowledge bases. Implement AWS IAM Identity Center with hotel-specific permission sets to control hotel staff data access.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 59 - Topic 1

### 中文

一家公司正在使用 AWS Lambda 实现 Serverless Inference API。该 API 会动态调用托管在 Amazon Bedrock 上的多个 AI 模型。公司需要能够在**不修改或重新部署 Lambda 代码**的情况下实时切换模型提供商，同时配置变更必须支持安全发布、验证和回滚。哪种解决方案能够满足这些要求？

- **A.** 将当前启用的模型 Provider 存储在 AWS Systems Manager Parameter Store 中，并让 Lambda 在运行时读取该参数，决定调用哪个模型。
- **B.** 将当前启用的模型 Provider 存储在 AWS AppConfig 中，并让 Lambda 在运行时读取配置，决定调用哪个模型。 **（最高票）**
- **C.** 配置 Amazon API Gateway REST API，把请求路由到多个不同 Lambda。每个 Lambda 硬编码一个特定模型 Provider，需要切换时手工修改 Integration Target。
- **D.** 将当前启用的模型 Provider 存储在 Amazon S3 上的 JSON 文件中。使用 AWS AppConfig 将该 S3 文件作为 Hosted Configuration Source，并让 Lambda 在运行时通过 AppConfig 读取文件来确定调用哪个模型。

### English

A company is implementing a serverless inference API by using AWS Lambda. The API will dynamically invoke multiple AI models hosted on Amazon Bedrock. The company needs to design a solution that can switch between model providers without modifying or redeploying Lambda code in real time. The design must include safe rollout of configuration changes and validation and rollback capabilities. Which solution will meet these requirements?

- **A.** Store the active model provider in AWS Systems Manager Parameter Store. Configure a Lambda function to read the parameter at runtime to determine which model to invoke.
- **B.** Store the active model provider in AWS AppConfig. Configure a Lambda function to read the configuration at runtime to determine which model to invoke. **(Most Voted)**
- **C.** Configure an Amazon API Gateway REST API to route requests to separate Lambda functions. Hardcode each Lambda function to a specific model provider. Switch the integration target manually.
- **D.** Store the active model provider in a JSON file hosted on Amazon S3. Use AWS AppConfig to reference the S3 file as a hosted configuration source. Configure a Lambda function to read the file through AppConfig at runtime to determine which model to invoke.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (83%)
- D (17%)

---

## Question 60 - Topic 1

### 中文

一家公司正在构建使用 Amazon Bedrock API 处理复杂客户咨询的生成式 AI（GenAI）应用。流量高峰期间，应用会间歇性出现 API Timeout，导致响应 Chunk 中断和数据交付延迟。面对长度差异很大的复杂查询时，应用也难以保证 Prompt 始终处于 Token 上限以内，用户已经遇到输入被截断和响应不完整的问题。此外，公司还观察到 Foundation Model（FM）调用失败。公司需要一种 Retry Strategy，能够自动处理瞬时服务错误，在高峰期避免进一步压垮 Amazon Bedrock，并能适应服务可用性变化，同时支持 Response Streaming 和 Token-Aware Request Handling。哪种方案能够满足要求？

- **A.** 使用标准重试策略：所有错误固定等待 1 秒后重试，最多重试 3 次。流式响应超时时直接重新开始整个 Stream，并为每个 Session 设置固定 Token 上限。
- **B.** 使用 Adaptive Retry Strategy：指数退避 + Jitter，并引入 Circuit Breaker Pattern；当错误率超过预定义阈值时暂时停止重试。实现 Streaming Response Handler，监控 Chunk Delivery Timeout；缓存已经成功收到的 Chunk，并在连接恢复后智能地从最后收到的位置继续处理。 **（最高票）**
- **C.** 使用 AWS SDK Standard Retry Mode。用 Try-Catch 包装 Amazon Bedrock API 调用并处理 Timeout Exception。流式请求失败时返回缓存的 Completion。对所有用户强制统一 Global Token Limit；同时增加基于 Jitter 的重试逻辑和轻量 Token Trimming。Stream 中断后只请求故障点之后缺失的 Chunk，并在内存中保留少量最近 Chunk 减少重复传输。
- **D.** 将 Amazon Bedrock Client Request Timeout 设置为 30 秒。实施 Client-Side Load Shedding。缓存部分结果，并在应用性能开始下降时停止接收新请求。为所有请求设置静态 Token 上限，同时配置指数退避、Dynamic Chunk Sizing 和 Context-Aware Token Limit。

### English

A company is building a generative AI (GenAI) application that uses Amazon Bedrock APIs to process complex customer inquiries. During peak usage periods, the application experiences intermittent API timeouts that cause issues such as broken response chunks and delayed data delivery. The application struggles to ensure that prompts remain within token limits when handling complex customer inquiries of varying lengths. Users have reported truncated inputs and incomplete responses. The company has also observed foundation model (FM) invocation failures. The company needs a retry strategy that automatically handles transient service errors and prevents overwhelming Amazon Bedrock during peak usage periods. The strategy must also adapt to changing service availability and support response streaming and token-aware request handling. Which solution will meet these requirements?

- **A.** Implement a standard retry strategy that uses a 1-second fixed delay between attempts and a 3-retry maximum for all errors. Handle streaming response timeouts by restarting streams. Cap token usage for each session.
- **B.** Implement an adaptive retry strategy that uses exponential backoff with jitter and a circuit breaker pattern that temporarily disables retries when error rates exceed a predefined threshold. Implement a streaming response handler that monitors for chunk delivery timeouts. Configure the handler to buffer successfully received chunks and intelligently resume streaming from the last received chunk when connections are re-established. **(Most Voted)**
- **C.** Use the AWS SDK to configure a retry strategy in standard mode. Wrap Amazon Bedrock API calls in try-catch blocks that handle timeout exceptions. Return cached completions for failed streaming requests. Enforce a global token limit for all users. Add jitter-based retry logic and lightweight token trimming for each request. Resume broken streams by requesting only the missing chunks from the point of failure. Maintain a small in-memory buffer of the most recent chunks to minimize redundant data transfer.
- **D.** Set Amazon Bedrock client request timeouts to 30 seconds. Implement client-side load shedding. Buffer partial results and stop new requests when the application performance begins to degrade. Set static token usage caps for all requests. Configure exponential backoff retries, dynamic chunk sizing, and context-aware token limits.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (75%)
- C (25%)

---

## Question 61 - Topic 1

### 中文

一家银行正在开发由生成式 AI（GenAI）驱动的 AI 助手，使用 Amazon Bedrock 为银行网站用户处理账户咨询并提供金融指导。银行必须确保 AI 助手在客户交互中不会泄露任何 Personally Identifiable Information（PII，个人身份信息）；发送给 GenAI 模型的 Prompt 中也不得包含 PII；同时 AI 助手不得回应客户提出的投资建议请求。银行还必须收集所有客户交互的审计日志，其中包括交互过程中传输的任何图片或文档。哪种方案能够以**最低运维工作量**满足要求？

- **A.** 使用 Amazon Macie 检测并删除用户输入和模型响应中的 PII。使用 Prompt Engineering 强制模型避开投资建议主题，并使用 AWS CloudTrail 捕获对话日志。
- **B.** 使用 AWS Lambda 和 Amazon Comprehend 检测并删除 PII。使用 Amazon Comprehend Topic Modeling 防止 AI 助手讨论投资建议，并在 Amazon CloudWatch 中配置自定义指标捕获客户对话。
- **C.** 配置 Amazon Bedrock Guardrails，应用 Sensitive Information Policy 来检测并过滤 PII；设置 Topic Policy，确保 AI 助手避开投资建议主题。使用 Converse API 记录模型调用，并启用向 Amazon S3 的 Delivery Logging 和 Image Logging。
- **D.** 使用 Regex Control 匹配 PII 模式。使用 Prompt Engineering 避免向客户返回 PII 或投资建议，并启用 Model Invocation Logging、Delivery Logging 和 Image Logging 到 Amazon S3。

### English

A bank is developing a generative AI (GenAI)-powered AI assistant that uses Amazon Bedrock to assist the bank's website users with account inquiries and financial guidance. The bank must ensure that the AI assistant does not reveal any personally identifiable information (PII) in customer interactions. The AI assistant must not send PII in prompts to the GenAI model. The AI assistant must not respond to customer requests to provide investment advice. The bank must collect audit logs of all customer interactions, including any images or documents that are transmitted during customer interactions. Which solution will meet these requirements with the LEAST operational effort?

- **A.** Use Amazon Macie to detect and redact PII in user inputs and in the model responses. Apply prompt engineering techniques to force the model to avoid investment advice topics. Use AWS CloudTrail to capture conversation logs.
- **B.** Use an AWS Lambda function and Amazon Comprehend to detect and redact PII. Use Amazon Comprehend topic modeling to prevent the AI assistant from discussing investment advice topics. Set up custom metrics in Amazon CloudWatch to capture customer conversations.
- **C.** Configure Amazon Bedrock guardrails to apply a sensitive information policy to detect and filter PII. Set up a topic policy to ensure that the AI assistant avoids investment advice topics. Use the Converse API to log model invocations. Enable delivery and image logging to Amazon S3.
- **D.** Use regex controls to match patterns for PII. Apply prompt engineering techniques to avoid returning PII or investment advice topics to customers. Enable model invocation logging, delivery logging, and image logging to Amazon S3.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 62 - Topic 1

### 中文

一家金融服务公司正在开发客户服务 AI 助手，使用 Amazon Bedrock 中的 Foundation Model（FM）。应用必须提供透明回答：既要记录推理过程，也要引用 Retrieval Augmented Generation（RAG）使用的来源；同时必须为所有用户响应保留完整审计追踪。应用需要支持最多 10,000 名并发用户，并在 2 秒内响应每条客户咨询。哪种方案能够以**最低运维开销**满足要求？

- **A.** 为 Amazon Bedrock Agent 启用 Tracing。配置 Structured Prompt，引导 FM 展示证据。将 Amazon Bedrock Knowledge Bases 与数据源集成以实现 RAG，并配置应用引用权威内容。使用 Multi-AZ 架构部署应用，利用 Amazon API Gateway 和 AWS Lambda 扩展应用，并使用 Amazon CloudFront 提供低延迟交付。 **（最高票）**
- **B.** 为 Amazon Bedrock Agent 启用 Tracing。将自定义 RAG Pipeline 与 Amazon OpenSearch Service 集成以检索和引用来源。配置 Structured Prompt 展示检索证据。将应用部署在 Amazon API Gateway REST API 后端，使用 AWS Lambda 和 Amazon CloudFront 扩展并降低延迟。将日志存入 Amazon S3，并使用 AWS CloudTrail 捕获审计追踪。
- **C.** 使用 Amazon CloudWatch 监控延迟和错误率。在应用后端直接嵌入模型 Prompt 以引用来源，并将应用与用户的交互存储在 Amazon RDS 中用于审计。
- **D.** 将生成响应和支持证据存储在 Amazon S3 Bucket 中，为 Bucket 启用 Versioning 以支持审计。使用 AWS Glue 对检索到的文档进行 Catalog，再使用 Amazon Athena 处理这些文档并定期生成合规报告。

### English

A financial services company is developing a customer service AI assistant application that uses a foundation model (FM) in Amazon Bedrock. The application must provide transparent responses by documenting reasoning and by citing sources that are used for Retrieval Augmented Generation (RAG). The application must capture comprehensive audit trails for all responses to users. The application must be able to serve up to 10,000 concurrent users and must respond to each customer inquiry within 2 seconds. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Enable tracing for Amazon Bedrock agents. Configure structured prompts that direct the FM to provide evidence presentations. Integrate Amazon Bedrock knowledge bases with data sources to enable RAG. Configure the application to reference and cite authoritative content. Deploy the application in a Multi-AZ architecture. Use Amazon API Gateway and AWS Lambda functions to scale the application. Use Amazon CloudFront to provide low-latency delivery. **(Most Voted)**
- **B.** Enable tracing for Amazon Bedrock agents. Integrate a custom RAG pipeline with Amazon OpenSearch Service to retrieve and cite sources. Configure structured prompts to present retrieved evidence. Deploy the application behind an Amazon API Gateway REST API. Use AWS Lambda functions and Amazon CloudFront to scale the application and to provide low latency. Store logs in Amazon S3 and use AWS CloudTrail to capture audit trails.
- **C.** Use Amazon CloudWatch to monitor latency and error rates. Embed model prompts directly in the application backend to cite sources. Store application interactions with users in Amazon RDS for audits.
- **D.** Store generated responses and supporting evidence in an Amazon S3 bucket. Enable versioning on the bucket for audits. Use AWS Glue to catalog retrieved documents. Process the retrieved documents in Amazon Athena to generate periodic compliance reports.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 63 - Topic 1

### 中文

一家医疗保健公司正在开发文档管理系统，把医学研究论文存储在 Amazon S3 Bucket 中。公司需要构建一套完整的 Metadata Framework，以提高分析这些研究论文的生成式 AI（GenAI）应用的搜索精度。Metadata 必须包含文档时间戳、作者信息和研究领域分类；所有上传文档都必须保持统一的 Metadata 结构；同时 Foundation Model（FM）应当能够在**不访问完整文档正文**的情况下理解文档上下文。哪种解决方案能够满足这些要求？

- **A.** 使用 Amazon S3 System Metadata 存储文档时间戳；使用 S3 Object Tag 实现研究领域分类；使用自定义 User-Defined Metadata 存储作者信息。
- **B.** 使用带 Legal Hold 的 S3 Object Lock 跟踪文档时间戳；使用 S3 Object Tag 存储作者信息；使用 S3 Access Point 实现领域分类。
- **C.** 使用 S3 Inventory Report 跟踪文档时间戳；创建 S3 Access Point 实现领域分类；在 S3 Storage Lens Dashboard 中存储作者信息。
- **D.** 使用自定义 User-Defined Metadata 存储作者信息；使用 S3 Object Lock Retention Period 跟踪文档时间戳；使用 S3 Event Notification 实现领域分类。

### English

A healthcare company is developing a document management system that stores medical research papers in an Amazon S3 bucket. The company needs to build a comprehensive metadata framework that will improve search precision for a generative AI (GenAI) application that analyzes the research papers. The metadata framework must include document timestamps, author information, and research domain classifications. The solution must maintain a consistent metadata structure across all uploaded documents. The solution must give foundation models (FMs) the ability to understand document context without accessing the full content. Which solution will meet these requirements?

- **A.** Store document timestamps in Amazon S3 system metadata. Use S3 object tags to implement domain classification. Implement custom user-defined metadata to store author information.
- **B.** Set up S3 Object Lock with legal holds to track document timestamps. Use S3 object tags to store author information. Implement S3 access points for domain classification.
- **C.** Use S3 Inventory reports to track document timestamps. Create S3 access points to implement domain classification. Store author information in S3 Storage Lens dashboards.
- **D.** Use custom user-defined metadata to store author information. Use S3 Object Lock retention periods to track document timestamps. Use S3 Event Notifications to implement domain classification.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 64 - Topic 1

### 中文

Example Corp 提供个性化视频生成服务，拥有数百万企业客户。客户通过向公司自有生成式 AI（GenAI）模型提交 Prompt 来生成营销视频。为了提高输出相关性和个性化程度，Example Corp 希望使用客户专属上下文增强 Prompt，例如产品偏好、客户属性和业务历史。客户具有严格的数据治理要求，并且必须始终保留对自身数据的完整所有权和控制权。客户**不要求实时访问数据**，但语义准确率必须很高，同时检索延迟仍需较低，以支持客户体验场景。Example Corp 希望尽量降低集成架构复杂度，除非确有必要，否则不希望在每个客户环境中部署并管理额外服务。哪种解决方案能够满足这些要求？

- **A.** 确保每个客户建立包含其内部数据的 Amazon Q Business Index。让客户把 Example Corp 指定为 Data Accessor，使 Example Corp 能够通过安全 API 检索相关内容，并在运行时增强 Prompt。 **（最高票）**
- **B.** 使用 Model Context Protocol（MCP）进行 Federated Search，为每个客户部署实时 MCP Server，在 Prompt 生成期间实时检索数据。
- **C.** 确保每个客户配置 Amazon Bedrock Knowledge Base，并允许 Cross-Account Query，使 Example Corp 能够检索结构化数据用于 Prompt Augmentation。
- **D.** 配置 Amazon Kendra 抓取客户数据源。跨账户共享生成的 Index，使 Example Corp 可以查询每个客户的 Amazon Kendra Index 来获取增强数据。

### English

Example Corp provides a personalized video generation service that millions of enterprise customers use. Customers generate marketing videos by submitting prompts to the company's proprietary generative AI (GenAI) model. To improve output relevance and personalization, Example Corp wants to enhance the prompts by using customer-specific context such as product preferences, customer attributes, and business history. The customers have strict data governance requirements. The customers must retain full ownership and control over their own data. The customers do not require real-time access. However, semantic accuracy must be high and retrieval latency must remain low to support customer experience use cases. Example Corp wants to minimize architectural complexity in its integration pattern. Example Corp does not want to deploy and manage services in each customer's environment unless necessary. Which solution will meet these requirements?

- **A.** Ensure that each customer sets up an Amazon Q Business index that includes the customer's internal data. Ensure that each customer designates Example Corp as a data accessor to allow Example Corp to retrieve relevant content by using a secure API to enrich prompts at runtime. **(Most Voted)**
- **B.** Use federated search with Model Context Protocol (MCP) by deploying real-time MCP servers for each customer. Retrieve data in real time during prompt generation.
- **C.** Ensure that each customer configures an Amazon Bedrock knowledge base. Allow cross-account querying so Example Corp can retrieve structured data for prompt augmentation.
- **D.** Configure Amazon Kendra to crawl customer data sources. Share the resulting indexes across accounts so Example Corp can query each customer's Amazon Kendra index to retrieve augmentation data.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (53%)
- C (41%)

---

## Question 65 - Topic 1

### 中文

一家公司正在构建法律研究 AI 助手，使用 Amazon Bedrock 上的 Anthropic Claude Foundation Model（FM）。AI 助手必须检索高度相关的判例法文档来增强 FM 回答；还必须识别法律概念之间的语义关系、精确法律术语以及引用信息；同时检索必须快速且结果精确。哪种方案能够满足这些要求？

- **A.** 配置 Amazon Bedrock Knowledge Base，使用默认 Vector Search。使用 Amazon Bedrock 扩展 Query，根据特定法律术语和引用提高法律文档检索效果。
- **B.** 使用 Amazon OpenSearch Service 部署 Hybrid Search Architecture，把 Vector Search 与 Keyword Search 结合起来，并使用 Amazon Bedrock Reranker Model 优化结果相关性。 **（最高票）**
- **C.** 为终端用户启用 Amazon Kendra Query Suggestion。使用 Amazon Bedrock 对搜索结果进行后处理，以识别文档中的语义相似性并生成精确结果。
- **D.** 使用 Amazon OpenSearch Service Vector Search 和 Amazon Bedrock Titan Embeddings 对法律文档建立索引和搜索，再使用自定义 AWS Lambda，把搜索结果与存储在 Amazon RDS 中的关键词 Filter 合并。

### English

A company is building a legal research AI assistant that uses Amazon Bedrock with an Anthropic Claude foundation model (FM). The AI assistant must retrieve highly relevant case law documents to augment the FM's responses. The AI assistant must identify semantic relationships between legal concepts, specific legal terminology, and citations. The AI assistant must perform quickly and return precise results. Which solution will meet these requirements?

- **A.** Configure an Amazon Bedrock knowledge base to use a default vector search configuration. Use Amazon Bedrock to expand queries to improve retrieval for legal documents based on specific terminology and citations.
- **B.** Use Amazon OpenSearch service to deploy a hybrid search architecture that combines vector search with keyword search. Apply an Amazon Bedrock reranker model to optimize result relevance. **(Most Voted)**
- **C.** Enable the Amazon Kendra query suggestion feature for end users. Use Amazon Bedrock to perform post-processing of search results to identify semantic similarity in the documents and to produce precise results.
- **D.** Use Amazon OpenSearch Service with vector search and Amazon Bedrock Titan embeddings to index and search legal documents. Use custom AWS Lambda functions to merge results with keyword-based filters that are stored in an Amazon RDS database.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 66 - Topic 1

### 中文

一家公司在多个业务部门部署了多套基于 Amazon Bedrock 的生成式 AI（GenAI）应用，分别用于客户服务、内容生成和文档分析。部分应用出现不可预测的 Token 消耗模式。公司需要一套完整的可观测性方案，能够实时查看多个模型的 Token 使用模式；为不同 Stakeholder Group 提供自定义 Dashboard；并对公司所有应用使用的 Foundation Model 的 Token 消耗提供告警。以下哪两个方案组合能够以**最低运维开销**满足要求？（选择两项。）

- **A.** 使用 Amazon CloudWatch Metric 作为数据源，创建自定义 Amazon QuickSight Dashboard，展示各 FM 的 Token 使用趋势和模式。
- **B.** 使用 Amazon CloudWatch Logs Insights 分析 Amazon Bedrock Invocation Log，识别 Token 消耗模式以及按应用进行 Usage Attribution。创建自定义 Query 找出高使用量场景，并把 Log Widget 添加到 Dashboard 以持续监控。 **（最高票）**
- **C.** 创建自定义 Amazon CloudWatch Dashboard，整合 Amazon Bedrock 原生 Token 和 Invocation CloudWatch Metric，并设置 CloudWatch Alarm 监控 Token 使用阈值。 **（最高票）**
- **D.** 使用 Amazon Bedrock 与 Amazon Managed Grafana 的 Zero-ETL Integration，创建展示公司各 FM Token 使用趋势和模式的 Dashboard。
- **E.** 实施 Amazon EventBridge Rule 捕获 Amazon Bedrock Model Invocation Event，把 Token 使用数据路由到以 Amazon OpenSearch Serverless 为目标的 Amazon Data Firehose Delivery Stream，再使用 OpenSearch Dashboard 分析使用模式。

### English

A company deploys multiple Amazon Bedrock based generative AI (GenAI) applications across multiple business units for customer service, content generation, and document analysis. Some applications show unpredictable token consumption patterns. The company requires a comprehensive observability solution that provides real-time visibility into token usage patterns across multiple models. The observability solution must support custom dashboards for multiple stakeholder groups and provide alerting capabilities for token consumption across all the foundational models that the company's applications use. Which combination of solutions will meet these requirements with the LEAST operational overhead? (Choose two.)

- **A.** Use Amazon CloudWatch metrics as data sources to create custom Amazon QuickSight dashboards that show token usage trends and usage patterns across FMs.
- **B.** Use Amazon CloudWatch Logs Insights to analyze Amazon Bedrock invocation logs for token consumption patterns and usage attribution by application. Create custom queries to identify high-usage scenarios. Add log widgets to dashboards to enable continuous monitoring. **(Most Voted)**
- **C.** Create custom Amazon CloudWatch dashboards that combine native Amazon Bedrock token and invocation CloudWatch metrics. Set up CloudWatch alarms to monitor token usage thresholds. **(Most Voted)**
- **D.** Create dashboards that show token usage trends and patterns across the company's FMs by using an Amazon Bedrock zero-ETL integration with Amazon Managed Grafana.
- **E.** Implement Amazon EventBridge rules to capture Amazon Bedrock model invocation events. Route token usage data to an Amazon Data Firehose delivery stream that targets Amazon OpenSearch Serverless. Use OpenSearch dashboards to analyze usage patterns.

**Correct Answer / 正确答案:** `BC`

**Community vote distribution / 社区投票分布:**

- BC (82%)

---

## Question 67 - Topic 1

### 中文

一家公司正在设计支持多种 AI Workload 的 Foundation Model（FM）解决方案。一部分 FM 必须按需实时调用；另一些 FM 则需要稳定、高吞吐访问用于 Batch Processing。解决方案还必须支持 Hybrid Deployment Pattern，并让工作负载同时运行在 Cloud Infrastructure 和 On-Premises Infrastructure 上，以满足数据驻留和合规要求。以下哪两个步骤组合能够满足这些要求？（选择两项。）

- **A.** 使用 AWS Lambda 编排低延迟 FM 推理，调用托管在 Amazon SageMaker AI Asynchronous Endpoint 上的 FM。
- **B.** 在 Amazon Bedrock 中配置 Provisioned Throughput，为高流量工作负载确保稳定性能。
- **C.** 将 FM 部署到 Amazon SageMaker AI Endpoint，并使用 Amazon SageMaker Neo 支持 Edge Deployment。使用 AWS Lambda 编排 FM，以支持 Hybrid Deployment。
- **D.** 使用带 Auto Scaling 的 Amazon Bedrock 处理不可预测的流量激增。
- **E.** 使用 Amazon SageMaker JumpStart 托管并调用 FM。

### English

A company is designing a solution that uses foundation models (FMs) to support multiple AI workloads. Some FMs must be invoked on demand and in real time. Other FMs require consistent high-throughput access for batch processing. The solution must support hybrid deployment patterns and run workloads across cloud infrastructure and on-premises infrastructure to comply with data residency and compliance requirements. Which combination of steps will meet these requirements? (Choose two.)

- **A.** Use AWS Lambda to orchestrate low-latency FM inference by invoking FMs hosted on Amazon SageMaker AI asynchronous endpoints.
- **B.** Configure provisioned throughput in Amazon Bedrock to ensure consistent performance for high-volume workloads.
- **C.** Deploy FMs to Amazon SageMaker AI endpoints with support for edge deployment by using Amazon SageMaker Neo. Orchestrate the FMs by using AWS Lambda to support hybrid deployment.
- **D.** Use Amazon Bedrock with auto-scaling to handle unpredictable traffic surges.
- **E.** Use Amazon SageMaker JumpStart to host and invoke the FMs.

**Correct Answer / 正确答案:** `BC`

**Community vote distribution / 社区投票分布:**

- BC (67%)
- BD (33%)

---

## Question 68 - Topic 1

### 中文

一家公司计划向欧洲和美洲多个国家运营的五个独立业务部门部署多套生成式 AI（GenAI）应用。每个应用都采用 Amazon Bedrock Retrieval Augmented Generation（RAG）模式，并拥有业务部门专属 Knowledge Base，存储数 TB 非结构化数据。公司必须为所有 GenAI 应用建立符合 Well-Architected 原则的标准化组件，统一安全控制、可观测性实践和部署模式。这些组件必须可复用、可版本化，并接受一致治理。哪种方案能够满足这些要求？

- **A.** 为 GenAI 应用配置 Amazon API Gateway REST API Endpoint。基于 AWS Well-Architected Generative AI Lens，把通用安全、可观测性和 RAG 模式部署为标准 AWS CloudFormation Template。部署完成后使用 CloudFormation Guard 验证每个业务部门的 Policy Compliance。
- **B.** 基于 AWS Well-Architected Generative AI Lens 创建标准化 AWS CloudFormation Template，实现安全、可观测性和 RAG 模式。建立集中式 Repository 进行版本控制，并把 CI/CD Pipeline 与 CloudFormation Guard 集成，在多个业务部门之间强制执行一致且可重复的部署。 **（最高票）**
- **C.** 使用 AWS Service Catalog，为每个业务部门定义标准化 Portfolio 和 Versioned Product。通过这些 Portfolio 强制执行基于 AWS Well-Architected Generative AI Lens 的安全、可观测性和 RAG 模式，并要求业务部门通过 Service Catalog Console 部署资源。
- **D.** 在共享设计文档中记录基于 AWS Well-Architected Generative AI Lens 的安全控制、可观测性要求和 RAG 模式。使用 Amazon Macie 强制部署，并把实施责任交给各业务部门。

### English

A company is planning to deploy multiple generative AI (GenAI) applications to five independent business units that operate in multiple countries in Europe and the Americas. Each application uses Amazon Bedrock Retrieval Augmented Generation (RAG) patterns with business unit-specific knowledge bases that store terabytes of unstructured data. The company must establish well-architected, standardized components for security controls, observability practices, and deployment patterns across all the GenAI applications. The components must be reusable, versioned, and governed consistently. Which solution will meet these requirements?

- **A.** Configure Amazon API Gateway REST API endpoints for the GenAI applications. Deploy common security, observability, and RAG patterns based on the AWS Well-Architected Generative AI Lens in standardized AWS CloudFormation templates. Use CloudFormation Guard after the deployment to validate policy compliance in each business unit.
- **B.** Create standardized AWS CloudFormation templates to implement security, observability, and RAG patterns based on the AWS Well-Architected Generative AI Lens. Establish a centralized repository that performs version control. Integrate a CI/CD pipeline with CloudFormation Guard to enforce consistent and repeatable deployments across business units. **(Most Voted)**
- **C.** Use AWS Service Catalog to define standardized portfolios and versioned products for each business unit. Use the portfolios to enforce security, observability, and RAG patterns based on the AWS Well-Architected Generative AI Lens. Require the business units to use the Service Catalog console to deploy resources.
- **D.** Document security controls, observability requirements, and RAG patterns based on the AWS Well-Architected Generative AI Lens in a shared design document. Use Amazon Macie to enforce deployment. Delegate implementation responsibility to each business unit.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (83%)
- C (17%)

---

## Question 69 - Topic 1

### 中文

一家公司升级了由 Amazon Bedrock 驱动、支持多语言客户服务的 Foundation Model（FM）。升级后，AI 助手在不同语言上的行为变得不一致：对语义完全相同的问题，某些语言会生成不同回答。公司需要建立方案，在未来模型更新时检测并处理类似问题。所有支持语言的评估必须在 45 分钟内完成；至少要并行处理 15,000 条测试对话；评估流程必须全自动并集成到 CI/CD Pipeline；如果质量阈值未达到，必须阻止部署。哪种解决方案能够满足要求？

- **A.** 创建分布式流量模拟框架，同时向助手发送多语言、高翻译负载的 Workload。使用 Amazon CloudWatch Metric 监控延迟、并发和吞吐量，并在生产发布前运行模拟以发现基础设施瓶颈。
- **B.** 在多个 AWS Region 中部署助手，使用 Amazon Route 53 Latency-Based Routing 和 AWS Global Accelerator 改善全球性能。将多语言对话日志存储在 Amazon S3 中，并每周执行一次发布后人工审计来检查一致性。
- **C.** 创建预处理 Pipeline，把所有传入消息标准化为统一格式后再发送给助手。对输出应用基于规则的检查以标记潜在幻觉，并把评估重点放在标准化后的文本上，简化跨语言测试。
- **D.** 建立语义完全相同的标准化多语言测试对话。使用 Amazon Bedrock Model Evaluation Job 并行运行这些测试，应用 Similarity 和 Hallucination Threshold，并将流程集成到 CI/CD Pipeline，阻止未通过评估的发布。 **（最高票）**

### English

A company upgraded its Amazon Bedrock powered foundation model (FM) that supports a multilingual customer service assistant. After the upgrade, the assistant exhibited inconsistent behavior across languages. The assistant began generating different responses in some languages when presented with identical questions. The company needs a solution to detect and address similar problems for future updates. The evaluation must be completed within 45 minutes for all supported languages. The evaluation must process at least 15,000 test conversations in parallel. The evaluation process must be fully automated and integrated into the CI/CD pipeline. The solution must block deployment if quality thresholds are not met. Which solution will meet these requirements?

- **A.** Create a distributed traffic simulation framework that sends translation-heavy workloads to the assistant in multiple languages simultaneously. Use Amazon CloudWatch metrics to monitor latency, concurrency, and throughput. Run simulations before production releases to identify infrastructure bottlenecks.
- **B.** Deploy the assistant in multiple AWS Regions with Amazon Route 53 latency-based routing and AWS Global Accelerator to improve global performance. Store multilingual conversation logs in Amazon S3. Perform weekly post-deployment audits to review consistency.
- **C.** Create a pre-processing pipeline that normalizes all incoming messages into a consistent format before sending the messages to the assistant. Apply rule-based checks to flag potential hallucinations in the outputs. Focus the evaluation on the normalized text to simplify testing across languages.
- **D.** Set up standardized multilingual test conversations with identical meaning. Run the test conversations in parallel by using Amazon Bedrock model evaluation jobs. Apply similarity and hallucination thresholds. Integrate the process into the CI/CD pipeline to block releases that fail. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 70 - Topic 1

### 中文

一家公司正在开发使用 Amazon Bedrock Foundation Model（FM）的生成式 AI（GenAI）应用，并集成了多个自定义 Tool。尽管用户流量保持稳定，应用仍出现了意外的 Token 消耗激增。公司需要利用 Amazon Bedrock Model Invocation Logging 监控 `InputTokenCount` 和 `OutputTokenCount` 指标，检测 Tool 使用中的异常模式，并定位具体是哪个 Tool Integration 导致异常 Token 消耗。同时，随着流量模式变化，检测阈值还必须能够自动调整。哪种解决方案能够满足要求？

- **A.** 使用 Amazon CloudWatch Logs 捕获 Model Invocation Log。基于 `InputTokenCount` 和 `OutputTokenCount` 创建 CloudWatch Dashboard，并为每个 Tool Integration 配置固定阈值的静态 CloudWatch Alarm。
- **B.** 将 Model Invocation Log 存入 Amazon S3。使用 AWS Glue 对日志进行 Catalog，再通过定时 Amazon Athena Query 分析 Token 消耗模式并生成 Tool 使用趋势报告。
- **C.** 使用 Amazon CloudWatch Logs 捕获 Model Invocation Log。创建 CloudWatch Metric Filter 提取 Tool-Specific Invocation Pattern，并对每个 Tool 的指标应用 CloudWatch Anomaly Detection Alarm，使 Baseline 能自动调整。 **（最高票）**
- **D.** 将 Model Invocation Log 存入 Amazon S3。创建 AWS Lambda 实时处理日志，再根据 Lambda 识别的 Token 消耗趋势手工更新 Amazon CloudWatch Alarm Threshold。

### English

A company is developing a generative AI (GenAI) application that uses Amazon Bedrock foundation models (FMs). The application has several custom tool integrations. The application has experienced unexpected token consumption surges despite consistent user traffic. The company needs a solution that uses Amazon Bedrock model invocation logging to monitor InputTokenCount metrics and OutputTokenCount metrics. The solution must detect unusual patterns in tool usage and identify which specific tool integrations cause abnormal token consumption. The solution must also automatically adjust thresholds as traffic patterns change. Which solution will meet these requirements?

- **A.** Use Amazon CloudWatch Logs to capture model invocation logs. Create CloudWatch dashboards based on InputTokenCount metrics and OutputTokenCount metrics. Configure static CloudWatch alarms with fixed thresholds for each tool integration.
- **B.** Store model invocation logs in an Amazon S3 bucket. Use AWS Glue to catalog the logs. Analyze token consumption patterns by using scheduled Amazon Athena queries that generate reports on tool usage trends.
- **C.** Use Amazon CloudWatch Logs to capture model invocation logs. Create CloudWatch metric filters to extract tool-specific invocation patterns. Apply CloudWatch anomaly detection alarms that adjust baselines for each tool's metrics. **(Most Voted)**
- **D.** Store model invocation logs in an Amazon S3 bucket. Create an AWS Lambda function to process logs in real time. Manually update Amazon CloudWatch alarm thresholds based on token consumption trends that the Lambda function identifies.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (86%)
- D (14%)

---

## Question 71 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 开发 AI 应用，所使用的 Foundation Model（FM）同时支持 Cross-Region Inference 和 Provisioned Throughput。应用必须为欧洲和北美用户持续提供低延迟服务。根据数据驻留法规，欧洲用户数据必须始终留在欧洲的 AWS Region 内。测试期间，当某个 Region 的流量峰值达到 Service Quota 时，应用会出现服务质量下降。公司需要在提高应用韧性的同时尽量降低运维复杂度。哪种方案能够满足要求？

- **A.** 在北美和欧洲 Region 分别部署独立 Amazon Bedrock 实例。实现自定义 Routing Layer，根据用户位置引导流量；配置 Amazon CloudWatch Alarm 监控各 Region 服务使用量，并在接近阈值时通过 Amazon SNS 发送邮件告警。
- **B.** 使用 Amazon Bedrock Cross-Region Inference Profile，在应用调用 `InvokeModel` API 时通过 Profile ID 指定 Geographic Code。分别配置 Amazon API Gateway HTTP API，将欧洲和北美用户导向相应的 Regional Endpoint。
- **C.** 部署 Multi-Region Amazon API Gateway HTTP API 和 AWS Lambda，并在 Lambda 中实现 Retry Logic 处理 Throttling。当主 Region 达到 Service Quota 时，让 Lambda 调用最近的次级 Region FM，并使用 Intelligent Routing 确保满足数据驻留要求。
- **D.** 在多个 Region 为 Amazon Bedrock 配置 Provisioned Throughput。在应用代码中实现 Failover Logic，在发生 Throttling 时切换 Region，并使用 AWS Global Accelerator 根据用户位置把流量路由到适当 Endpoint。

### English

A company is using Amazon Bedrock to develop an AI-powered application that uses a foundation model (FM) that supports cross-Region inference and provisioned throughput. The application must serve users in Europe and North America with consistently low latency. The application must comply with data residency regulations that require European user data to remain within Europe-based AWS Regions. During testing, the application experiences service degradation when Regional traffic spikes reach service quotas. The company needs a solution that maintains application resilience and minimizes operational complexity. Which solution will meet these requirements?

- **A.** Deploy separate Amazon Bedrock instances in North American and European Regions. Use a custom routing layer that directs traffic based on user location. Configure Amazon CloudWatch alarms to monitor Regional service usage. Use Amazon SNS to send email alerts to the company when usage approaches specified thresholds.
- **B.** Use Amazon Bedrock cross-Region inference profiles by specifying geographical codes in profile IDs when the application calls the InvokeModel API. Configure separate Amazon API Gateway HTTP APIs to direct European and North American users to the appropriate Regional endpoints.
- **C.** Deploy a multi-Region Amazon API Gateway HTTP API and AWS Lambda functions that implement retry logic to handle throttling. Configure the Lambda functions to call the FM in the nearest secondary Region when the application reaches service quotas in the primary Region. Use intelligent routing to ensure compliance with data residency requirements.
- **D.** Configure provisioned throughput for Amazon Bedrock in multiple Regions. Implement failover logic in the application code to switch between Regions when throttling occurs. Use AWS Global Accelerator to route traffic to the appropriate endpoints based on user location.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 72 - Topic 1

### 中文

一家国际公司正在构建使用 RAG 的 AI 助手，希望获得近实时、低延迟性能，并向多个地理区域提供服务。客户会通过 AI 助手使用专有数据，而这些专有数据不得离开客户所在的直接地理区域。哪种解决方案能够满足这些要求？

- **A.** 部署带 Cross-Region Model Inference Profile 的 Amazon Bedrock 模型。在公司运营的每个 AWS Region 中创建 Amazon S3 Bucket，并把 Knowledge Base 分别存储在对应 Bucket 中。每个 Region 配置 Amazon Kendra 与当地 Knowledge Base 交互，并配置 AWS Lambda 同时使用 Kendra 和 Amazon Bedrock 处理 AI 助手 Prompt。
- **B.** 在公司运营的每个 AWS Region 中部署 Amazon Bedrock 模型。配置 Amazon Bedrock Cross-Region Model Inference Profile。配置使用 Amazon Bedrock Knowledge Bases 的 Vector Database，并在每个运营 Region 的 Amazon S3 中分别存储 Knowledge Base。 **（最高票）**
- **C.** 使用 AWS Outposts，在公司运营的每个 AWS Region 中部署 Outpost。为各 Region 创建存储 Knowledge Base 的 Amazon S3 Bucket，并在每个 Outpost 上部署配置为 Vector Database 的 Amazon RDS。然后在中央 Region 部署带 Cross-Region Inference Profile 的 Amazon Bedrock 模型。
- **D.** 在公司运营的每个 AWS Local Zone 中，使用 Amazon S3 Express One Zone Storage Class 存储 Knowledge Base。每个 Local Zone 使用 Amazon RDS 部署 Vector Database，并在 Amazon EC2 上部署大型语言模型（LLM），再将 AI 助手 Prompt 路由到对应 Local Zone 的模型。

### English

An international company is building an AI assistant that uses RAG. The company wants the AI assistant to have near real-time, low-latency performance. The AI assistant must provide service to several geographic areas. The company's customers will use proprietary data with the AI assistant. The proprietary data must not leave the company's immediate geographic area. Which solution will meet these requirements?

- **A.** Deploy an Amazon Bedrock model with a cross-Region model inference profile. Create Amazon S3 buckets in each AWS Region the company operates in. Store a knowledge base in each respective S3 bucket. In each Region, configure Amazon Kendra to interact with the respective knowledge base. In each Region, configure an AWS Lambda function that uses Kendra and Amazon Bedrock to process AI assistant prompts.
- **B.** Deploy an Amazon Bedrock model in each AWS Region the company operates in. Configure an Amazon Bedrock cross-Region model inference profile. Configure a vector database that uses Amazon Bedrock Knowledge Bases. Store the knowledge bases in Amazon S3 in each Region the company operates in. **(Most Voted)**
- **C.** Use AWS Outposts to deploy an outpost in each AWS Region the company operates in. Create Amazon S3 buckets to store knowledge bases in each corresponding Region. Deploy Amazon RDS configured as a vector database to each outpost. Deploy an Amazon Bedrock model with a cross-Region inference profile in a central Region.
- **D.** Configure a knowledge base stored in the Amazon S3 Express One Zone storage class in each AWS Local Zone the company operates in. Use Amazon RDS to deploy a vector database in each Local Zone the company operates in. Deploy a large language model (LLM) to Amazon EC2 instances in each Local Zone. Configure the AI assistant to route prompts to the model in the respective Local Zone.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (62%)
- D (38%)

---

## Question 73 - Topic 1

### 中文

一家公司正在构建生成式 AI（GenAI）应用，用于处理金融报告并向分析师提供摘要。应用需要运行在两个计算环境中：第一个环境中，AWS Lambda 必须使用 Python SDK 按需分析报告；第二个环境中，Amazon EKS Container 必须使用 JavaScript SDK 按计划批量处理多份报告。两个环境必须使用相同 Foundation Model（FM），保持一致的身份认证方式，并在多轮交互中持续维护对话上下文。哪种解决方案能够满足这些要求？

- **A.** 在两个环境中使用 Amazon Bedrock `InvokeModel` API，并分别使用不同身份认证方式。将对话状态存储在 Amazon DynamoDB 中，并针对每种编程语言实现自定义 I/O Format Logic。
- **B.** 两个环境都直接使用 Amazon Bedrock `Converse` API，并通过 IAM Role 使用统一认证机制。将对话状态存储在 Amazon ElastiCache 中，并为模型参数创建特定于编程语言的 Wrapper。
- **C.** 创建集中式 Amazon API Gateway REST API Endpoint，使用 `InvokeModel` API 处理所有模型交互。在每个 Lambda 或 EKS Container 的进程内存中保存交互历史，并使用环境变量配置模型参数。
- **D.** 使用 Amazon Bedrock `Converse` API，并使用 IAM Role 完成身份认证。通过请求中的 `messages` Array 传入先前消息来维护对话上下文，并在不同环境中使用各语言对应的 SDK 建立一致 API Interface。 **（最高票）**

### English

A company is building a generative AI (GenAI) application that processes financial reports and provides summaries for analysts. The application must run two compute environments. In one environment, AWS Lambda function must use the Python SDK to analyze reports on demand. In the second environment, Amazon EKS containers must use the JavaScript SDK to batch process multiple reports on a schedule. The application must maintain conversational context throughout multi-tum interactions, use the same foundation model (FM) across environments, and ensure consistent authentication. Which solution will meet these requirements?

- **A.** Use the Amazon Bedrock InvokeModel API with a separate authentication method for each environment. Store conversation states in Amazon DynamoDB. Use custom I/O formatting logic for each programming language.
- **B.** Use the Amazon Bedrock Converse API directly in both environments with a common authentication mechanism that uses IAM roles. Store conversation states in Amazon ElastiCache. Creating programming language-specific wrappers for model parameters.
- **C.** Create a centralized Amazon API Gateway REST API endpoint that handles all model interactions by using the InvokeModel API. Store interaction history in application process memory in each Lambda function or EKS container. Use environment variables to configure model parameters.
- **D.** Use the Amazon Bedrock Converse API and IAM roles for authentication. Pass previous messages in the request messages array to maintain conversational context. Use programming language-specific SDKs to establish consistent API interfaces. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 74 - Topic 1

### 中文

一家公司正在构建 Serverless 应用，使用 AWS Lambda 帮助全球学生总结笔记。应用通过 Amazon Bedrock 使用 Anthropic Claude。公司观察到，每个时区的大部分流量都集中在当地晚间，用户在自己时区的高峰期经常遇到 Throttling Error。公司需要解决限流问题并保证应用持续运行，同时不能牺牲性能质量；此外，在低流量时段不希望承担固定小时成本。哪种解决方案能够满足这些要求？

- **A.** 创建自定义 Amazon CloudWatch Metric 监控模型错误，并把 Provisioned Throughput 设置为明显高于观测峰值流量的固定容量。
- **B.** 创建自定义 Amazon CloudWatch Metric 监控模型错误，并设置 Failover Mechanism，当错误超过指定阈值时把调用重定向到备用 AWS Region。
- **C.** 在 Amazon Bedrock 中启用 Invocation Logging。监控 `Invocations`、`InputTokenCount`、`OutputTokenCount` 和 `InvocationThrottles` 等关键指标，并把流量分散到 Cross-Region Inference Endpoint。 **（最高票）**
- **D.** 在 Amazon Bedrock 中启用 Invocation Logging。监控 `InvocationLatency`、`InvocationClientErrors` 和 `InvocationServerErrors` 指标，并把流量分散到同一模型的多个版本。

### English

A company is building a serverless application that uses AWS Lambda functions to help students around the world summarize notes. The application uses Anthropic Claude through Amazon Bedrock. The company observed that most of the traffic occurs during evenings in each time zone. Users report experiencing throttling errors during peak usage times in their times zones. The company needs to resolve the throttling issues by ensuring continuous operation of the application. The solution must maintain application performance quality. The company needs a solution that does not require a fixed hourly cost during low traffic periods. Which solution will meet these requirements?

- **A.** Create custom Amazon CloudWatch metrics to monitor model errors. Set provisioned throughput to a value that is safely higher than the peak traffic observed.
- **B.** Create custom Amazon CloudWatch metrics to monitor model errors. Set up a failover mechanism to redirect invocations to a backup AWS Region when the errors exceed a specified threshold.
- **C.** Enable invocation logging in Amazon Bedrock. Monitor key metrics such as Invocations, InputTokenCount, OutputTokenCount, and Invocation Throttles. Distribute traffic across cross-Region inference endpoints. **(Most Voted)**
- **D.** Enable invocation logging in Amazon Bedrock. Monitor InvocationLatency, InvocationClientErrors, and InvocationServerErrors metrics. Distribute traffic across multiple versions of the same model.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 75 - Topic 1

### 中文

一家公司正在开发新的 AI 应用，需要与多种专业 Tool 集成。这些 Tool 目前以 Model Context Protocol（MCP）Server 的形式运行在开发人员本地机器上，并且每次调用之间不保存状态。公司计划把每个 MCP Server 部署为 AWS Lambda 函数以支持生产应用。解决方案必须同时允许内部应用和经过授权的第三方合作伙伴访问，并采用严格的身份认证和授权控制。还需要执行哪些额外步骤，才能以**最低运维开销**满足要求？

- **A.** 使用 Lambda `Invoke` API 创建自定义 Lambda Invocation Transport。实施 IAM Authentication，并向授权用户和 Role 授予 `InvokeFunction` 权限。
- **B.** 通过 Amazon API Gateway REST API Endpoint 暴露 Lambda。使用 API Key 进行认证，并把需要访问 MCP Server 的应用改为使用普通 HTTP Request，而不是 MCP Protocol。
- **C.** 创建 Lambda Function URL，并启用自定义 Streamable HTTP Transport 和 SigV4。使用 AWS IAM Authentication，并向授权用户和 Role 授予 `InvokeFunctionUrl` 权限。 **（最高票）**
- **D.** 通过 Amazon API Gateway HTTP API Endpoint 暴露 Lambda，并使用 Streamable HTTP Transport。使用 Amazon Cognito 实现 OAuth Authentication，并让 API Gateway 验证 OAuth Token。

### English

A company is developing a new AI-powered application that needs to integrate with various specialized tools. These tools currently run as Model Context Protocol (MCP) servers on the local machines of developers and do not maintain states between invocations. The company plans to deploy each MCP server as an AWS Lambda function to support the company's production application. The solution must be accessible to both internal applications and authorized third-party partners. The solution must use strict authentication and authorization controls. Which additional steps will meet these requirements with the LEAST operational overhead?

- **A.** Create a custom Lambda invocation transport by using the Lambda Invoke API. Implement IAM authentication and grant InvokeFunction permissions to authorized users and roles.
- **B.** Expose the Lambda functions through Amazon API Gateway REST API endpoints. Implement API keys for authentication. Configure the applications that need to access the MCP servers to use standard HTTP requests instead of the MCP protocol.
- **C.** Create Lambda function URLs and enable a custom Streamable HTTP transport and SigV4. Implement AWS IAM authentication. Grant InvokeFunctionUrl permissions to authorized users and roles. **(Most Voted)**
- **D.** Expose the Lambda function through Amazon API Gateway HTTP API endpoints with the Streamable HTTP transport. Use Amazon Cognito to implement OAuth authentication. Configure API Gateway to validate OAuth tokens.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (62%)
- D (25%)
- A (12%)

---

## Question 76 - Topic 1

### 中文

一家公司拥有使用 Amazon Bedrock 实时回答客户问题的生成式 AI（GenAI）应用。在流量高峰期间，公司发现对 Foundation Model（FM）的 API 调用会间歇性失败。公司需要处理 Transient Error，并深入观察 FM 性能。方案必须在 Throttling 事件期间防止 Cascading Failure（级联故障）；还要支持跨服务边界的 Distributed Tracing，以定位延迟贡献来源；同时必须能够把性能问题与具体 FM 特性关联起来。哪种解决方案能够满足要求？

- **A.** 自定义实现重试机制，每次重试固定等待 1 秒。配置 Amazon CloudWatch Alarm 监控应用错误率和延迟指标。
- **B.** 将 AWS SDK 配置为 Standard Retry Mode，并使用带 Jitter 的 Exponential Backoff。使用 AWS X-Ray Tracing，并添加 Annotation 来识别和过滤不同服务组件。 **（最高票）**
- **C.** 对所有 FM 响应实现客户端缓存，并在应用代码中添加自定义日志，记录 API 调用耗时。
- **D.** 将 AWS SDK 配置为 Adaptive Retry Mode，并使用 AWS CloudTrail Distributed Tracing 监控 Throttling Event。

### English

A company has a generative AI (GenAI) application that uses Amazon Bedrock to provide real-time responses to customer queries. The company has noticed intermittent failures with API calls to foundation models (FMs) during peak traffic periods. The company needs a solution to handle transient errors and provide detailed observability into FM performance. The solution must prevent cascading failures during throttling events and provide distributed tracing across service boundaries to identify latency contributors. The solution must also enable correlation of performance issues with specific FM characteristics. Which solution will meet these requirements?

- **A.** Implement a custom retry mechanism with a fixed delay of 1 second between retries. Configure Amazon CloudWatch alarms to monitor the application's error rates and latency metrics.
- **B.** Configure the AWS SDK with standard retry mode and exponential backoff with jitter. Use AWS X-Ray tracing with annotations to identify and filter service components. **(Most Voted)**
- **C.** Implement client-side caching of all FM responses. Add custom logging statements in the application code to record API call durations.
- **D.** Configure the AWS SDK with adaptive retry mode. Use AWS CloudTrail distributed tracing to monitor throttling events.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (71%)
- D (14%)
- A (14%)

---

## Question 77 - Topic 1

### 中文

一家公司正在 AWS 上构建视频分析平台，使用 Amazon Rekognition 和 Amazon Bedrock 分析大型视频归档。平台必须符合预定义隐私标准，还必须保证 Model I/O 安全、控制 Foundation Model（FM）的访问模式，并提供“谁在什么时候访问了什么”的审计能力。哪种解决方案能够满足这些要求？

- **A.** 为 Amazon Bedrock Model API 调用配置 VPC Endpoint。实施 Amazon Bedrock Guardrails，过滤 Prompt 和响应中的有害或未授权内容。使用 Amazon Bedrock Trace Event 跟踪所有 Agent 和模型调用以用于审计，并把 Trace 导出到 Amazon CloudWatch Logs。将所有 Prompt 和输出存储在使用 SSE-KMS 的 Amazon S3 中。
- **B.** 使用 IAM Attribute-Based Access Control（ABAC），根据部门映射具体权限。为 Amazon Bedrock Model API 调用配置 VPC Endpoint。使用 IAM Condition Key 强制指定 `GuardrailIdentifier` 和 `ModelId`。配置 AWS CloudTrail 捕获 S3 Object 的 Management/Data Event 和 KMS Key 使用活动；启用 S3 Server Access Logging 记录视频归档的详细文件级交互；将所有 CloudTrail Log 发送到 AWS CloudTrail Lake；再设置 Amazon CloudWatch Alarm，检测 Amazon Bedrock、Amazon Rekognition 和 AWS KMS 的异常活动并告警。
- **C.** 使用 VPC Endpoint Policy 限制服务访问。使用 AWS Config 跟踪资源变化和安全规则合规性。使用 SSE-KMS 加密静态数据，把模型 I/O 存储在独立 Amazon S3 Bucket 中，并启用 S3 Server Access Logging 跟踪文件级交互。
- **D.** 配置 AWS CloudTrail Insights，分析跨账户 API 调用模式，检测 Amazon Bedrock、Amazon Rekognition、Amazon S3 和 AWS KMS 中的异常活动。部署 Amazon Macie 扫描并分类视频归档。使用 SSE-KMS 加密所有存储数据。配置 CloudTrail 捕获 KMS API 使用事件，并通过 Amazon EventBridge 处理 CloudTrail Insights 异常和 Macie Finding；使用 CloudWatch Alarm 在检测到潜在安全问题时触发自动通知和安全响应。

### English

A company is building a video analysis platform on AWS. The platform will analyze a large video archive by using Amazon Rekognition and Amazon Bedrock. The platform must comply with predefined privacy standards. The platform must also use secure model I/O, control foundation model (FM) access patterns, and provide an audit of who accessed what and when. Which solution will meet these requirements?

- **A.** Configure VPC endpoints for Amazon Bedrock model API calls. Implement Amazon Bedrock Guardrails to filter harmful or unauthorized content in prompts and responses. Use Amazon Bedrock trace events to track all agent and model invocations for auditing purposes. Export the traces to Amazon CloudWatch Logs as an audit record of model usage. Store all prompts and outputs in Amazon S3 with server-side encryption with AWS KMS keys (SSE-KMS).
- **B.** Define access control by using IAM with attribute-based controls to map departments to specific permissions. Configure VPC endpoints for Amazon Bedrock model API calls. Use IAM condition keys to enforce specific GuardrailIdentifier and ModelId values. Configure AWS CloudTrail to capture management and data events for S3 objects and KMS key usage activities. Enable S3 server access logging to record detailed file-level interactions with the video archives. Send all CloudTrail logs to AWS CloudTrail Lake. Set up Amazon CloudWatch alarms to detect and alert on unexpected activity from Amazon Bedrock, Amazon Rekognition, and AWS KMS.
- **C.** Restrict access to services by using VPC endpoint policies. Use AWS Config to track resource changes and compliance with security rules. Use server-side encryption with AWS KMS keys (SSE-KMS) to encrypt data at rest. Store the model's I/O in separate Amazon S3 buckets. Enable S3 server access logging to track file-level interactions.
- **D.** Configure AWS CloudTrail Insights to analyze API call patterns across accounts and detect anomalous activity in Amazon Bedrock, Amazon Rekognition, Amazon S3, and AWS KMS. Deploy Amazon Macie to scan and classify the video archive. Use server-side encryption with AWS KMS keys (SSE-KMS) to encrypt all stored data. Configure CloudTrail to capture KMS API usage events for audit purposes. Configure Amazon EventBridge rules to process CloudTrail Insights anomalies and Macie findings. Use CloudWatch alarms to trigger automated notifications and security responses when potential security issues are detected.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 78 - Topic 1

### 中文

一家保险公司已有 Amazon SageMaker AI 基础设施，用于支持一个 Web 应用，让客户预测自己的保险保费。训练 SageMaker AI 模型所需的客户数据存储在 Amazon S3 Bucket 中，而且数据集正在快速增长。公司希望持续重新训练模型：每当员工向 S3 Bucket 上传新的客户数据文件时，系统都必须自动重新训练模型，并把新模型重新部署到应用。哪种解决方案能够满足这些要求？

- **A.** 使用 AWS Glue 对每个上传文件运行 ETL Job。让 ETL Job 通过 AWS SDK 调用 SageMaker AI Model Endpoint，并使用该 Endpoint 的 Real-Time Inference，在更新后的客户数据集上重新训练模型后重新部署模型。
- **B.** 创建 AWS Lambda 和 Webhook Handler，在员工上传新文件时产生事件。配置 SageMaker Pipelines，在使用更新后的客户数据重新训练模型后重新部署模型。使用 Amazon EventBridge 创建 Event Bus，将 Lambda Event 作为 Source，将 SageMaker Pipelines 作为 Target。
- **C.** 创建带 AWS SDK Integration 的 AWS Step Functions Express Workflow，在员工向 S3 上传新文件时获取客户数据。使用 SageMaker Data Wrangler Flow 把数据从 S3 导出到 SageMaker Autopilot，再由 SageMaker Autopilot 在更新后的客户数据上重新训练后重新部署模型。
- **D.** 创建 AWS Step Functions Standard Workflow。第一个 State 调用 AWS Lambda，在员工向 S3 上传新文件时作出响应。使用 SageMaker Pipelines 中的 Pipeline，在更新后的客户数据上重新训练后重新部署模型；工作流下一个 State 在第一个 State 收到响应后运行该 Pipeline。 **（最高票）**

### English

An insurance company uses existing Amazon SageMaker AI infrastructure to support a web-based application that allows customers to predict what their insurance premiums will be. The company stores customer data that is used to train the SageMaker AI model in an Amazon S3 bucket. The dataset is growing rapidly. The company wants a solution to continuously re-train the model. The solution must automatically re-train and re-deploy the model to the application when an employee uploads a new customer data file to the S3 bucket. Which solution will meet these requirements?

- **A.** Use AWS Glue to run an ETL job on each uploaded file. Configure the ETL job to use the AWS SDK to invoke the Sage Maker AI model endpoint. Use real-time inference with the endpoint to re-deploy the model after it is re-trained on the updated customer dataset.
- **B.** Create an AWS Lambda function and webhook handlers to generate an event when an employee uploads a new file. Configure SageMaker Pipelines to re-deploy the model after it is re-trained on the updated customer dataset. Use Amazon EventBridge to create an event bus. Set the Lambda function event as the source and SageMaker Pipelines as the target.
- **C.** Create an AWS Step Functions Express workflow with AWS SDK integrations to retrieve the customer data from the S3 bucket when an employee uploads a new file to the S3 bucket. Use a SageMaker Data Wrangler flow to export the data from the S3 bucket to SageMaker Autopilot. Use SageMaker Autopilot to re-deploy the model after it has been re-trained on the updated customer dataset.
- **D.** Create an AWS Step Functions Standard workflow. Configure the first state to call an AWS Lambda function to respond when an employee uploads a new file to the S3 bucket. Use a pipeline in SageMaker Pipelines to re-deploy the model after it has been re-trained on the updated customer dataset. Use the next state in the workflow to run the pipeline when the first state receives a response. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (70%)
- B (30%)

---

## Question 79 - Topic 1

### 中文

一家公司使用 Amazon Bedrock 实现基于 Retrieval Augmented Generation（RAG）的系统，为用户提供医学信息。公司需要比较多种 Chunking Strategy、评估两个 Foundation Model（FM）的生成质量，并为部署强制执行质量阈值。哪种 Amazon Bedrock Evaluation 配置能够满足要求？

- **A.** 创建 Retrieve-Only Evaluation Job，使用受支持版本的 Anthropic Claude Sonnet 作为 Evaluator Model。配置 Context Relevance 和 Context Coverage 指标，并在独立 CI/CD Pipeline 中定义部署阈值。
- **B.** 创建 Retrieve-and-Generate Evaluation Job，使用自定义 Precision@k Metric，以及采用 1～5 分量表的 LLM-as-a-Judge Metric。把每种 Chunking Strategy 都包含在 Evaluation Dataset 中，并使用受支持版本的 Anthropic Claude Sonnet 评估两个 FM 的响应。 **（最高票）**
- **C.** 针对每个 Chunking Strategy 与 FM 组合分别创建独立 Evaluation Job。使用 Amazon Bedrock 内置 Correctness 和 Completeness 指标，并在批准部署前人工审核分数。
- **D.** 建立 Pipeline，使用多个 Retrieve-Only Evaluation Job 评估检索质量。再为两个 FM 分别创建 Evaluation Job，使用 Amazon Nova Pro 作为 LLM-as-a-Judge，并根据 Faithfulness 和 Citation Precision 指标评估。

### English

A company uses Amazon Bedrock to implement a Retrieval Augmented Generation (RAG)-based system to serve medical information to users. The company needs to compare multiple chunking strategies, evaluate the generation quality of two foundation models (FMs), and enforce quality thresholds for deployment. Which Amazon Bedrock evaluation configuration will meet these requirements?

- **A.** Create a retrieve-only evaluation job that uses a supported version of Anthropic Claude Sonnet as the evaluator model. Configure metrics for context relevance and context coverage. Define deployment thresholds in a separate CI/CD pipeline.
- **B.** Create a retrieve-and-generate evaluation job that uses custom precision at k metrics and an LLM-as-a-judge metric that uses a scale of 1-5. Include each chunking strategy in the evaluation dataset. Use a supported version of Anthropic Claude Sonnet to evaluate responses from both FMs. **(Most Voted)**
- **C.** Create a separate evaluation job for each chunking strategy and FM combination. Use Amazon Bedrock built-in metrics for correctness and completeness. Manually review scores before deployment approval.
- **D.** Set up a pipeline that uses multiple retrieve-only evaluation jobs to assess retrieval quality. Create separate evaluation jobs for both FMs that use Amazon Nova Pro as the LLM-as-a-judge model. Evaluate based on faithfulness and citation precision metrics.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 80 - Topic 1

### 中文

一个野生动物保护机构在全球运营多家动物园，并使用各种传感器、追踪器和音视频记录设备监测动物行为。机构希望推出一个能够摄取 Multimodal Data（多模态数据）的生成式 AI（GenAI）助手，用于研究动物行为。GenAI 助手必须支持自然语言查询、避免对动物行为作出缺乏依据的推测性解释，并保留审计日志以支持伦理研究审计。哪种解决方案能够满足要求？

- **A.** 将原始视频输入 Amazon Rekognition，以检测动物姿态和表情。使用 Amazon Data Firehose 把传感器和 GPS 数据流式写入 Amazon S3 Data Lake。使用存储在 AWS Systems Manager Parameter Store 中的基础 Template 向 Amazon Bedrock FM 发 Prompt。使用 IAM Policy 控制访问，并使用 AWS CloudTrail 记录审计日志。
- **B.** 使用 Amazon SageMaker Processing 和 Amazon Transcribe 预处理多模态数据。将摘要摄取到 Amazon Bedrock RAG Knowledge Base。应用 Amazon Bedrock Guardrails 限制推测性输出。使用 AWS AppConfig 管理 Prompt Template，并使用 AWS CloudTrail 记录研究活动以供审计。 **（最高票）**
- **C.** 使用 Amazon OpenSearch Serverless 为行为日志和 Telemetry Event 建立索引。使用 Amazon Comprehend 提取 Entity。使用 Amazon Bedrock 构建问答层，把研究摘要 Embedding 到 OpenSearch Serverless 文档中。使用 IAM 控制访问，并使用 AWS CloudTrail 记录用户与 AI 助手的交互。
- **D.** 配置 Amazon Q Business，在 Amazon S3、Amazon Kinesis 和 Amazon SageMaker Feature Store 之间联合数据。配置 Amazon EventBridge 调用 Data Ingestion Job，并使用自定义 AWS Lambda 在结果返回用户前过滤 LLM 输出以满足伦理合规。

### English

A wildlife conservation agency operates zoos globally. The agency uses various sensors, trackers, and audiovisual recorders to monitor animal behavior. The agency wants to launch a generative AI (GenAI) assistant that can ingest multimodal data to study animal behavior. The GenAI assistant must support natural language queries, avoid speculative behavioral interpretations, and maintain audit logs for ethical research audits. Which solution will meet these requirements?

- **A.** Ingest raw videos into Amazon Rekognition to detect animal postures and expressions. Use Amazon Data Firehose to stream sensor and GPS data into an Amazon S3 data lake. Prompt an Amazon Bedrock foundation model (FM) by using basic templates that are stored in AWS Systems Manager Parameter Store. Use IAM policies to control access. Use AWS CloudTrail for audit logging.
- **B.** Use Amazon SageMaker Processing and Amazon Transcribe to pre-process multimodal data. Ingest summaries into an Amazon Bedrock Retrieval Augmented Generation (RAG) knowledge base. Apply Amazon Bedrock guardrails to restrict speculative outputs. Use AWS AppConfig to manage prompt templates. Use AWS CloudTrail to log research activity for audits. **(Most Voted)**
- **C.** Use Amazon OpenSearch Serverless to index behavioral logs and telemetry events. Use Amazon Comprehend to extract entities. Use Amazon Bedrock to build a layer to answer questions. Embed study summaries into OpenSearch Serverless documents. Use IAM to control access. Use AWS CloudTrail to log user interactions with the AI assistant.
- **D.** Configure Amazon Q Business to federate data across Amazon S3, Amazon Kinesis, and Amazon SageMaker Feature Store. Configure Amazon EventBridge to invoke data ingestion jobs. Use custom AWS Lambda functions to filter large language model (LLM) outputs for ethical compliance before returning results to users.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 81 - Topic 1

### 中文

一家公司使用启用了全部功能的 AWS Organizations 来管理多个 AWS Account。员工会在多个账户中使用 Amazon Bedrock。公司必须阻止特定主题和专有信息被包含在发送给 Amazon Bedrock 模型的 Prompt 中，同时还必须确保员工只能使用经过批准的 Amazon Bedrock 模型。公司集中管理员工的 IAM Role。以下哪两个方案组合能够满足要求？（选择两项。）

- **A.** 为每个员工的 IAM Role 创建 IAM Permissions Boundary，要求调用 Amazon Bedrock 模型时必须指定经过批准的 Amazon Bedrock Guardrail Identifier；再创建 SCP，只允许员工使用经过批准的模型。
- **B.** 创建 SCP，只允许员工使用经过批准的模型，并要求员工在调用批准模型时必须指定 Guardrail Identifier。 **（最高票）**
- **C.** 创建 SCP：如果调用模型时没有指定集中部署的 Guardrail Identifier，则禁止员工调用模型。再为每个员工 IAM Role 创建 Permissions Boundary，只允许调用经过批准的模型。
- **D.** 使用 AWS CloudFormation 创建带 Block Filtering Policy 的自定义 Amazon Bedrock Guardrail，并使用 StackSets 将 Guardrail 部署到 Organization 中的每个 Account。 **（最高票）**
- **E.** 使用 AWS CloudFormation 创建带 Mask Filtering Policy 的自定义 Amazon Bedrock Guardrail，并使用 StackSets 将 Guardrail 部署到 Organization 中的每个 Account。

### English

A company uses an organization in AWS Organizations with all features enabled to manage multiple AWS accounts. Employees use Amazon Bedrock across multiple accounts. The company must prevent specific topics and proprietary information from being included in prompts to Amazon Bedrock models. The company must ensure that employees can use only approved Amazon Bedrock models. The company centrally manages IAM roles for employees. Which combination of solutions will meet these requirements? (Choose two.)

- **A.** Create an IAM permissions boundary for each employee's IAM role. Configure the permissions boundary to require an approved Amazon Bedrock guardrail identifier to invoke Amazon Bedrock models. Create an SCP that allows employees to use only approved models.
- **B.** Create an SCP that allows employees to use only approved models. Configure the SCP to require employees to specify a guardrail identifier in calls to invoke an approved model. **(Most Voted)**
- **C.** Create an SCP that prevents an employee from invoking a model if a centrally deployed guardrail identifier is not specified in a call to the model. Create a permissions boundary on each employee's IAM role that allows each employee to invoke only approved models.
- **D.** Use AWS CloudFormation to create a custom Amazon Bedrock guardrail that has a block filtering policy. Use stack sets to deploy the guardrail to each account in the organization. **(Most Voted)**
- **E.** Use AWS CloudFormation to create a custom Amazon Bedrock guardrail that has a mask filtering policy. Use stack sets to deploy the guardrail to each account in the organization.

**Correct Answer / 正确答案:** `BD`

**Community vote distribution / 社区投票分布:**

- BD (90%)
- D (10%)

---

## Question 82 - Topic 1

### 中文

一家公司正在为生成式 AI（GenAI）应用设计 API。应用使用托管模型服务上的 Foundation Model（FM）。API 必须通过 Streaming 降低延迟；必须强制执行 Token Limit 来控制计算资源消耗；还必须实现 Retry Logic，以处理模型 Timeout 和 Partial Response。哪种方案能够以**最低运维开销**满足这些要求？

- **A.** 将 Amazon API Gateway HTTP API 与 AWS Lambda 集成，由 Lambda 调用 Amazon Bedrock。使用 Lambda Response Streaming 流式发送响应，在 Lambda 内执行 Token Limit，并通过 Lambda 和 API Gateway 的 Timeout 配置实现模型超时重试。
- **B.** 将 Amazon API Gateway HTTP API 直接连接 Amazon Bedrock，通过客户端轮询模拟 Streaming。前端负责执行 Token Limit，并通过 API Gateway Integration Setting 配置 Retry Behavior。
- **C.** 将 Amazon API Gateway WebSocket API 连接到托管容器化 Inference Server 的 Amazon ECS Service。使用 WebSocket Protocol 流式传输响应，在 Amazon ECS 内执行 Token Limit，并通过 ECS Task Lifecycle Hook 和 Restart Policy 处理模型 Timeout。
- **D.** 将 Amazon API Gateway REST API 与 AWS Lambda 集成，由 Lambda 调用 Amazon Bedrock。使用 Lambda Response Streaming 流式传输响应，在 Lambda 内执行 Token Limit，并利用 Lambda 和 API Gateway Timeout 配置实现 Retry Logic。 **（最高票）**

### English

A company is designing an API for a generative AI (GenAI) application that uses a foundation model (FM) that is hosted on a managed model service. The API must stream responses to reduce latency, enforce token limits to manage compute resource usage, and implement retry logic to handle model timeouts and partial responses. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Integrate an Amazon API Gateway HTTP API with an AWS Lambda function to invoke Amazon Bedrock. Use Lambda response streaming to stream responses. Enforce token limits within the Lambda function. Implement retry logic for model timeouts by using Lambda and API Gateway timeout configurations.
- **B.** Connect an Amazon API Gateway HTTP API directly to Amazon Bedrock. Simulate streaming by using client-side polling. Enforce token limits on the frontend. Configure retry behavior by using API Gateway integration settings.
- **C.** Connect an Amazon API Gateway WebSocket API to an Amazon ECS service that hosts a containerized inference server. Stream responses by using the WebSocket protocol. Enforce token limits within Amazon ECS. Handle model timeouts by using ECS task lifecycle hooks and restart policies.
- **D.** Integrate an Amazon API Gateway REST API with an AWS Lambda function that invokes Amazon Bedrock. Use Lambda response streaming to stream responses. Enforce token limits within the Lambda function. Implement retry logic by using Lambda and API Gateway timeout configurations. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (63%)
- A (37%)

---

## Question 83 - Topic 1

### 中文

一家零售公司正在使用 Amazon Bedrock 开发客户服务 AI 助手。分析显示，70% 的客户咨询只是简单的商品问题，小模型就能有效处理；另外 30% 是复杂的退货政策问题，需要更强的高级推理能力。公司希望以低成本实现 Model Selection Framework，根据问题复杂度自动把咨询路由到合适模型，同时保持较高客户满意度并尽量降低响应延迟。哪种方案能够以**最少实施工作量**满足要求？

- **A.** 创建 Multi-Stage Architecture，先使用小型 Foundation Model（FM）分类每条咨询的复杂度。简单问题路由到更小、更便宜的模型，复杂问题路由到更大、更强的模型，并使用 AWS Lambda 实现路由逻辑。
- **B.** 使用 Amazon Bedrock Intelligent Prompt Routing 自动分析咨询。将简单商品问题路由到较小模型，将复杂退货政策问题路由到能力更强的大模型。 **（最高票）**
- **C.** 只使用一个 Amazon Bedrock 中型 Foundation Model（FM），按 On-Demand 计费。在 Prompt 中加入特殊指令，让同一个模型同时处理简单和复杂咨询。
- **D.** 为简单问题和复杂问题分别创建独立 Amazon Bedrock Endpoint。基于关键词检测实现 Rule-Based Routing，小模型使用 On-Demand，大模型使用 Provisioned Throughput。

### English

A retail company is using Amazon Bedrock to develop a customer service AI assistant. Analysis shows that 70% of customer inquiries are simple product questions that a smaller model can effectively handle. However, 30% of inquiries are complex return policy questions that require advanced reasoning. The company wants to implement a cost-effective model selection framework to automatically route customer inquiries to appropriate models based on inquiry complexity. The framework must maintain high customer satisfaction and minimize response latency. Which solution will meet these requirements with the LEAST implementation effort?

- **A.** Create a multi-stage architecture that uses a small foundation model (FM) to classify the complexity of each inquiry. Route simple inquiries to a smaller, more cost-effective model. Route complex inquiries to a larger, more capable model. Use AWS Lambda functions to handle the routing logic.
- **B.** Use Amazon Bedrock intelligent prompt routing to automatically analyze inquiries. Route simple product inquiries to smaller models, and route complex return policy inquiries to more capable larger models. **(Most Voted)**
- **C.** Implement a single-model solution that uses an Amazon Bedrock mid-sized foundation model (FM) with on-demand pricing. Include special instructions in model prompts to handle both simple and complex inquiries by using the same model.
- **D.** Create separate Amazon Bedrock endpoints for simple and complex inquiries. Implement a rule-based routing system based on keyword detection. Use on-demand pricing for the smaller model and provisioned throughput for the larger model.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 84 - Topic 1

### 中文

一家精品咖啡公司拥有一款移动应用，使用 Amazon Bedrock 和三阶段 Prompt Chain 生成个性化咖啡烘焙方案。Prompt Chain 会依次把用户输入转换成结构化 Metadata、检索相关咖啡烘焙日志，并为每位客户生成个性化烘焙建议。多个 AWS Region 的用户反馈：相同输入会得到不一致的烘焙建议；检索步骤推理缓慢；并且偶尔会出现不安全建议，例如使用过高水温冲煮。公司必须提高重复输入的输出稳定性、改善应用性能和输出安全性。更新后的方案必须保证相同输入达到 **99.5% 输出一致性**、推理延迟低于 1 秒，并使用经过验证的安全控制阻止不安全或幻觉型建议。哪种解决方案能够满足要求？

- **A.** 使用 Amazon Bedrock Provisioned Throughput 稳定推理延迟。应用带 Semantic Denial Rule 的 Amazon Bedrock Guardrails 阻止不安全输出。使用 Amazon Bedrock Prompt Management，通过 Approval Workflow 管理 Prompt。 **（最高票）**
- **B.** 使用 Amazon Bedrock Agents 管理 Prompt Chaining。将模型输入和输出记录到 Amazon CloudWatch Logs，并使用 CloudWatch 日志对 Prompt Version 执行 A/B Testing。
- **C.** 在 Amazon ElastiCache 中缓存 Prompt 结果。使用 AWS Lambda 预处理 Metadata 并跟踪端到端延迟，再使用 AWS X-Ray 识别和修复性能瓶颈。
- **D.** 使用 Amazon Kendra 提高烘焙日志检索准确率。将标准化 Prompt Metadata 存入 Amazon DynamoDB，并使用 AWS Step Functions 编排 Multi-Step Prompt。

### English

A specialty coffee company has a mobile app that generates personalized coffee roast profiles by using Amazon Bedrock with a three-stage prompt chain. The prompt chain converts user inputs into structured metadata, retrieves relevant logs for coffee roasts, and generates a personalized roast recommendation for each customer. Users in multiple AWS Regions report inconsistent roast recommendations for identical inputs, slow inference during the retrieval step, and unsafe recommendations such as brewing at excessively high temperatures. The company must improve the stability of outputs for repeated inputs. The company must also improve app performance and the safety of the app's outputs. The updated solution must ensure 99.5% output consistency for identical inputs and achieve inference latency of less than 1 second. The solution must also block unsafe or hallucinated recommendations by using validated safety controls. Which solution will meet these requirements?

- **A.** Deploy Amazon Bedrock with provisioned throughput to stabilize inference latency. Apply Amazon Bedrock guardrails that have semantic denial rules to block unsafe outputs. Use Amazon Bedrock Prompt Management to manage prompts by using approval workflows. **(Most Voted)**
- **B.** Use Amazon Bedrock Agents to manage chaining. Log model inputs and outputs to Amazon CloudWatch Logs. Use logs from Amazon CloudWatch to perform A/B testing for prompt versions.
- **C.** Cache prompt results in Amazon ElastiCache. Use AWS Lambda functions to pre-process metadata and to trace end-to-end latency. Use AWS X-Ray to identify and remediate performance bottlenecks.
- **D.** Use Amazon Kendra to improve roast log retrieval accuracy. Store normalized prompt metadata within Amazon DynamoDB. Use AWS Step Functions to orchestrate multistep prompts.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (100%)

---

## Question 85 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 开发生成式 AI（GenAI）应用，用于分析公司数据中的模式和关系。应用每天会在欧洲、北美和亚洲多个 AWS Region 中处理数百万个新数据点，然后将数据存入 Amazon S3。应用必须遵守当地的数据保护和存储法规；数据驻留和数据处理必须发生在**同一大洲**。应用还必须保留决策过程的审计追踪，并提供数据分类能力。哪种解决方案能够满足这些要求？

- **A.** 在每个 Region 部署应用并配置本地 IAM Policy。使用 Amazon Bedrock Cross-Region Inference 分发 Workload。使用 Amazon CloudWatch 记录 AI 决策流程和数据处理活动，并手工跟踪各 Region 的合规认证。
- **B.** 使用 AWS Organizations SCP 管理特定位置权限。使用 AWS CloudTrail Immutable Log 审计决策过程。把自定义模型导入 Amazon Bedrock 并部署到每个 Region。
- **C.** 使用 Amazon S3 Object Lock 和 Region-Specific S3 Bucket Policy。根据数据点的地理来源，在所在 Region 内预处理数据，然后再发送到 Amazon Bedrock。使用 Amazon Macie 进行数据分类，并使用 AWS CloudTrail Immutable Log 审计决策过程。 **（最高票）**
- **D.** 为每个 Region 创建独立 AWS Account 和各自合规框架。使用 Amazon SageMaker AI 配合自定义监控，跟踪模型性能和数据驻留合规性，并针对每个监管辖区人工生成报告。

### English

A company is developing a generative AI (GenAI) application by using Amazon Bedrock. The application will analyze patterns and relationships in the company's data. The application will process millions of new data points daily across AWS Regions in Europe, North America, and Asia before storing the data in Amazon S3. The application must comply with local data protection and storage regulations. Data residency and processing must occur within the same continent. The application must also maintain audit trails of the application's decision-making processes and provide data classification capabilities. Which solution will meet these requirements?

- **A.** Deploy the application in each Region with local IAM policies. Use Amazon Bedrock cross-Region inference to distribute the workload. Use Amazon CloudWatch to log AI decision-making processes and data processing activities. Manually track compliance certifications across Regions.
- **B.** Use SCPs with AWS Organizations to manage location-specific permissions. Use AWS CloudTrail immutable logs to audit the decision-making processes. Import a custom model into Amazon Bedrock and deploy the model to each Region.
- **C.** Use Amazon S3 Object Lock with Region-specific S3 bucket policies. Pre-process the data points within the Region based on geographic origin before sending the data points to Amazon Bedrock. Use Amazon Macie to classify the data. Use AWS CloudTrail immutable logs to audit the decision-making processes. **(Most Voted)**
- **D.** Create separate AWS accounts for each Region with individual compliance frameworks. Use Amazon SageMaker AI with custom monitoring to track model performance and compliance with data residency requirements. Create manual reports for each regulatory jurisdiction.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 86 - Topic 1

### 中文

一家金融服务公司正在部署生成式 AI（GenAI）应用，使用 Amazon Bedrock 帮助客服代表向客户提供个性化投资建议。公司必须实施全面治理方案，遵循 Responsible AI 实践并满足监管要求。方案必须检测并阻止推荐中的幻觉；必须为客户交互提供安全控制；还必须实时监控 Model Behavior Drift，并保留所有 Prompt-Response Pair 的审计追踪以供监管审查。公司必须在 60 天内完成部署；方案还必须与现有合规 Dashboard 集成，并在 200 ms 内响应客户。哪种方案能够以**最低运维开销**满足要求？

- **A.** 配置 Amazon Bedrock Guardrails，应用自定义 Content Filter 和 Toxicity Detection。使用 Amazon Bedrock Model Evaluation 检测幻觉。将 Prompt-Response Pair 存入 Amazon DynamoDB 以保留审计追踪，并设置 TTL。把 Amazon CloudWatch Custom Metric 集成到现有合规 Dashboard。 **（最高票）**
- **B.** 部署 Amazon Bedrock，并使用 AWS PrivateLink 安全访问应用。使用 AWS Lambda 自定义 Prompt Validation。将 Prompt-Response Pair 存储到 Amazon S3，并配置 S3 Lifecycle Policy。创建自定义 Amazon CloudWatch Dashboard 监控模型性能指标。
- **C.** 使用 Amazon Bedrock Agents 和 Amazon Bedrock Knowledge Bases 对回答进行 Grounding。使用 Amazon Bedrock Guardrails 强制内容安全。使用 Amazon OpenSearch Service 存储并索引 Prompt-Response Pair，并将 OpenSearch Service 与 Amazon QuickSight 集成，创建合规报告并检测模型行为漂移。
- **D.** 使用 Amazon SageMaker Model Monitor 检测模型行为漂移。使用 AWS WAF 过滤内容。将客户交互存储在加密 Amazon RDS 中，并使用 Amazon API Gateway 创建自定义 HTTP API 与合规 Dashboard 集成。

### English

A financial services company is deploying a generative AI (GenAI) application that uses Amazon Bedrock to assist customer service representatives to provide personalized investment advice to customers. The company must implement a comprehensive governance solution that follows responsible AI practices and meets regulatory requirements. The solution must detect and prevent hallucinations in recommendations. The solution must have safety controls for customer interactions. The solution must also monitor model behavior drift in real time and maintain audit trails of all prompt-response pairs for regulatory review. The company must deploy the solution within 60 days. The solution must integrate with the company's existing compliance dashboard and respond to customers within 200 ms. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Configure Amazon Bedrock guardrails to apply custom content filters and toxicity detection. Use Amazon Bedrock Model Evaluation to detect hallucinations. Store prompt-response pairs in Amazon DynamoDB to capture audit trails and set a TTL. Integrate Amazon CloudWatch custom metrics with the existing compliance dashboard. **(Most Voted)**
- **B.** Deploy Amazon Bedrock and use AWS PrivateLink to access the application securely. Use AWS Lambda functions to implement custom prompt validation. Store prompt-response pairs in an Amazon S3 bucket and configure S3 Lifecycle policies. Create custom Amazon CloudWatch dashboards to monitor model performance metrics.
- **C.** Use Amazon Bedrock Agents and Amazon Bedrock Knowledge Bases to ground responses. Use Amazon Bedrock Guardrails to enforce content safety. Use Amazon OpenSearch Service to store and index prompt-responses pairs. Integrate OpenSearch Service with Amazon QuickSight to create compliance reports and to detect model behavior drift.
- **D.** Use Amazon SageMaker Model Monitor to detect model behavior drift. Use AWS WAF to filter content. Store customer interactions in an encrypted Amazon RDS database. Use Amazon API Gateway to create custom HTTP APIs to integrate with the compliance dashboard.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**

- A (62%)
- C (38%)

---

## Question 87 - Topic 1

### 中文

一家公司使用 AWS Lake Formation 建立数据湖，其中包含跨多个 AWS Region、服务多个业务部门的 Database 和 Table。公司希望通过 Amazon Bedrock 使用 Foundation Model（FM）执行 Fraud Detection（欺诈检测）。FM 必须读取数据湖中的敏感金融数据，其中包含部分客户 Personally Identifiable Information（PII）。公司必须设计访问控制方案，防止 PII 出现在生产环境中；FM 只能访问经过授权、且特定列中的 PII 已被删除的数据子集；同时还必须捕获所有数据访问的审计追踪。哪种解决方案能够满足这些要求？

- **A.** 针对每个“业务部门 + Region”组合，在独立 Amazon S3 Bucket 中创建单独数据集。使用 S3 Bucket Policy，根据分配给 FM Training Instance 的 IAM Role 控制访问，并使用 S3 Access Log 跟踪数据访问。
- **B.** 配置 FM 使用 IAM Role 认证，并通过基于 LF-Tag Expression 的 Lake Formation Permission 控制访问。将业务部门和 Region 定义为 LF-Tag，并分配到 Database 和 Table。使用 AWS CloudTrail 收集完整数据访问审计追踪。 **（最高票）**
- **C.** 在 Lake Formation 中直接针对特定 Database 和 Table 向 IAM Principal 授权。创建自定义应用层，在把数据发送给 FM 前记录访问请求并进一步过滤敏感列。
- **D.** 配置 FM 从 AWS STS 请求临时凭证。通过由 API 生成的 Presigned S3 URL 访问数据，该 API 会应用业务部门和 Region Filter。使用 AWS CloudTrail 收集完整的数据访问审计追踪。

### English

A company uses AWS Lake Formation to set up a data lake that contains databases and tables for multiple business units across multiple AWS Regions. The company wants to use a foundation model (FM) through Amazon Bedrock to perform fraud detection. The FM must ingest sensitive financial data from the data lake. The data includes some customer personally identifiable information (PM). The company must design an access control solution that prevents PI I from appearing in a production environment. The FM must access only authorized data subsets that have PH redacted from specific data columns. The company must capture audit trails for all data access. Which solution will meet these requirements?

- **A.** Create a separate dataset in a separate Amazon S3 bucket for each business unit and Region combination. Configure S3 bucket policies to control access based on IAM roles that are assigned to FM training instances. Use S3 access logs to track data access.
- **B.** Configure the FM to authenticate by using IAM roles and Lake Formation permissions based on LF-Tag expressions. Define business units and Regions as LF-Tags that are assigned to databases and tables. Use AWS CloudTrail to collect comprehensive audit trails of data access. **(Most Voted)**
- **C.** Use direct IAM principal grants on specific databases and tables in Lake Formation. Create a custom application layer that logs access requests and further filters sensitive columns before sending data to the FM.
- **D.** Configure the FM to request temporary credentials from AWS STS. Access the data by using presigned S3 URLs that are generated by an API that applies business unit and Regional filters. Use AWS CloudTrail to collect comprehensive audit trails of data access.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 88 - Topic 1

### 中文

一家公司运行基于 Retrieval Augmented Generation（RAG）的应用，使用 Amazon Bedrock Knowledge Bases 处理监管合规查询，并通过 `RetrieveAndGenerateStream` API 返回结果。Knowledge Base 中包含超过 50,000 份监管文档、法律先例和政策更新。当前 RAG 响应质量不理想，因为初始检索经常返回**语义上相似但上下文实际上无关**的文档，进而导致模型幻觉和错误监管指导。公司需要提高 RAG 应用性能，使其返回更相关的文档。哪种方案能够以**最低运维开销**满足要求？

- **A.** 部署 Amazon SageMaker Endpoint 运行微调 Ranking Model。使用 Amazon API Gateway REST API 路由请求，并让应用通过 REST API 请求 Rerank 结果。
- **B.** 使用 Amazon Comprehend 分类文档并应用 Relevance Score。将 RAG 应用的 Reranking 流程与 Amazon Textract 集成进行文档分析，并使用 Amazon Neptune 执行 Graph-Based Relevance Calculation。
- **C.** 实现 Retrieval Pipeline：先调用 Amazon Bedrock Knowledge Bases `Retrieve` API 执行初始检索，再调用 Amazon Bedrock `Rerank` API 重新排序结果，最后调用 `InvokeModelWithResponseStream` 生成响应。
- **D.** 直接在 Amazon Bedrock Knowledge Bases 的 Reranking Configuration 中使用最新 Amazon Reranker Model，通过上下文评估改善文档相关性评分并重新排序结果。 **（最高票）**

### English

A company runs a Retrieval Augmented Generation (RAG) application that uses Amazon Bedrock Knowledge Bases to perform regulatory compliance queries. The application uses the RetrieveAndGenerateStream API. The application retrieves relevant documents from a knowledge base that contains more than 50,000 regulatory documents, legal precedents, and policy updates. The RAG application is producing suboptimal responses because the initial retrieval often returns semantically similar but contextually irrelevant documents. The poor responses are causing model hallucinations and incorrect regulatory guidance. The company needs to improve the performance of the RAG application so it returns more relevant documents. Which solution will meet this requirement with the LEAST operational overhead?

- **A.** Deploy an Amazon SageMaker endpoint to run a fine-tuned ranking model. Use an Amazon API Gateway REST API to route requests. Configure the application to make requests through the REST API to rerank the results.
- **B.** Use Amazon Comprehend to classify documents and apply relevance scores. Integrate the RAG application’s reranking process with Amazon Textract to run document analysis. Use Amazon Neptune to perform graph-based relevance calculations.
- **C.** Implement a retrieval pipeline that uses the Amazon Bedrock Knowledge Bases Retrieve API to perform initial document retrieval. Call the Amazon Bedrock Rerank API to rerank the results. Invoke the InvokeModelWithResponseStream operation to generate responses.
- **D.** Use the latest Amazon reranker model through the reranking configuration within Amazon Bedrock Knowledge Bases. Use the model to improve document relevance scoring and to reorder results based on contextual assessments. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 89 - Topic 1

### 中文

一家公司正在开发客户沟通平台，其中 AI 助手由 Amazon Bedrock Foundation Model（FM）驱动，用于总结客户消息并生成初步回复草稿。公司希望使用 Amazon Comprehend 实现 Layered Content Filtering（分层内容过滤），必须阻止冒犯性内容、保护客户隐私，并检测潜在的不适当建议请求，例如要求实施不道德行为、有害活动或操纵性行为。整体响应时间必须保持在可接受范围，因此所有预处理过滤器都必须在内容到达 FM 之前完成。哪种解决方案能够满足这些要求？

- **A.** 使用异步 API Call 并行处理。使用 Toxicity Detection 检测冒犯性内容，使用 Prompt Safety Classification 检测不适当建议请求，并执行 PII Detection 但不进行 Redaction。
- **B.** 使用 Custom Classification 构建 FM，用于检测冒犯性内容和不适当建议请求。只有当消息通过自定义分类器后，才将 PII Detection 作为第二层 Filter。
- **C.** 部署 Multi-Stage Process：先执行 Prompt Safety Classification，再只对安全 Prompt 执行 Toxicity Detection，最后以 Streaming Mode 执行 PII Detection。把被标记消息通过 Amazon EventBridge 路由给人工审核。
- **D.** 使用 Toxicity Detection，并把所有类别 Threshold 设置为 0.5。同时并行执行 Prompt Safety Classification 和带 Entity Redaction 的 PII Detection，并对 Filter Metric 应用 Amazon CloudWatch Alarm。 **（最高票）**

### English

A company is developing a customer communication platform that uses an AI assistant powered by an Amazon Bedrock foundation model (FM). The AI assistant summarizes customer messages and generates initial response drafts. The company wants to use Amazon Comprehend to implement layered content filtering. The layered content filtering must prevent sharing of offensive content, protect customer privacy, and detect potential inappropriate advice solicitation. Inappropriate advice solicitation includes requests for unethical practices, harmful activities, or manipulative behaviors. The solution must maintain acceptable overall response times, so all pre-processing filters must finish before the content reaches the FM. Which solution will meet these requirements?

- **A.** Use parallel processing with asynchronous API calls. Use toxicity detection for offensive content. Use prompt safety classification for inappropriate advice solicitation. Use personally identifiable information (PII) detection without redaction.
- **B.** Use custom classification to build an FM that detects offensive content and inappropriate advice solicitation. Apply personally identifiable information (PII) detection as a secondary filter only when messages pass the custom classifier.
- **C.** Deploy a multi-stage process. Configure the process to use prompt safety classification first, then toxicity detection on safe prompts only, and finally personally identifiable information (PII) detection in streaming mode. Route flagged messages through Amazon EventBridge for human review.
- **D.** Use toxicity detection with thresholds configured to 0.5 for all categories. Use parallel processing for both prompt safety classification and personally identifiable information (PII) detection with entity redaction. Apply Amazon CloudWatch alarms to filter metrics. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- A (44%)
- D (44%)
- C (11%)

---

## Question 90 - Topic 1

### 中文

一家 Software as a Service（SaaS）公司正在构建推荐模型，使用 Amazon SageMaker AI 支持一个向客户推荐航空舱位升级的应用。公司将通过 Amazon Bedrock Custom Model Import，把 SageMaker AI 模型托管到 Amazon Bedrock。不同航空公司会使用该应用向客户发送定制优惠。模型必须检查客户旅行历史，从而生成更相关的升级推荐。客户旅行历史数据存储在 Amazon RDS 中。公司必须确保在不同航空公司和不同客户群体中，应用都能提供一致、相关且准确的结果。哪种解决方案能够满足要求？

- **A.** 使用 Amazon Bedrock Knowledge Bases 实现 RAG，分析客户旅行历史并提供 Semantic Search。通过语义搜索获取相关预订模式、偏好和忠诚度信息，以生成个性化舱位升级建议。使用 Amazon Bedrock Guardrails 过滤内容，并通过 AWS Step Functions 和 AWS Lambda 编排验证工作流以减少幻觉。
- **B.** 实现 Text-to-SQL Transformation，并通过 SQL Validation 从 RDS 中准确获取相关预订模式、偏好和忠诚度信息。使用这些结果生成个性化舱位升级建议。应用 Amazon Bedrock Guardrails 过滤内容，并使用 AWS Step Functions 和 AWS Lambda 编排验证工作流减少幻觉。 **（最高票）**
- **C.** 使用 Amazon OpenSearch Service 对客户旅行历史 Embedding 执行 Vector Search，使应用能够基于相似度检索预订模式、偏好和忠诚度信息，从而生成个性化舱位升级建议。使用 Amazon Bedrock Guardrails 过滤响应，并通过 Confidence Scoring 和 Semantic Similarity Search 减少幻觉。
- **D.** 实现 Text-to-SQL Transformation 和 SQL Validation，从 RDS 中准确获取相关预订模式、偏好和忠诚度信息，生成个性化舱位升级建议。使用 Amazon Bedrock Guardrails 过滤响应，并通过 Confidence Scoring 和 Semantic Similarity Search 减少幻觉。

### English

A software as a service (SaaS) company is building a recommendation model that uses Amazon SageMaker AI to support an application that recommends airline cabin upgrades to customers. The company will host SageMaker AI models on Amazon Bedrock by using Amazon Bedrock Custom Model Import. Airline companies will use the application to send customized offers to customers. The model must examine the travel history of customers to help make more relevant recommendations. The company stores customer travel history data in an Amazon RDS database. The company must ensure that the application delivers consistent, relevant, and accurate results across multiple airlines and customer populations. Which solution will meet these requirements?

- **A.** Use Amazon Bedrock Knowledge Bases to implement a RAG architecture to analyze customer travel history data to give the application semantic search capabilities. Use the semantic search capabilities to retrieve relevant booking patterns, preferences, and loyalty information to generate personalized cabin upgrade recommendations. Apply Amazon Bedrock guardrails to filter content. Use AWS Step Functions and AWS Lambda functions to orchestrate validation workflows to reduce hallucinations.
- **B.** Implement text-to-SQL transformations with SQL validations to accurately retrieve relevant booking patterns, preferences, and loyalty information from the RDS database. Use the results to generate personalized cabin upgrade recommendations. Apply Amazon Bedrock guardrails to filter content. Use AWS Step Functions and AWS Lambda functions to orchestrate validation workflows to reduce hallucinations. **(Most Voted)**
- **C.** Use Amazon OpenSearch Service to implement vector searches of customer travel history embeddings. Use the vector searches to give the application the ability to perform similarity-based retrieval of booking patterns, preferences, and loyalty information to generate personalized cabin upgrade recommendations. Apply Amazon Bedrock guardrails to filter responses. Use confidence scoring and semantic similarity searches to reduce hallucinations.
- **D.** Implement text-to-SQL transformations with SQL validations to accurately retrieve relevant booking patterns, preferences, and loyalty information from the RDS database. Use the results to generate personalized cabin upgrade recommendations. Apply Amazon Bedrock guardrails to filter responses. Use confidence scoring and semantic similarity searches to reduce hallucinations.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (60%)
- D (40%)

---

## Question 91 - Topic 1

### 中文

一家公司正在使用 Amazon Bedrock 开发客户支持 AI 助手。AI 助手必须回答客户有关账户的问题，但响应中不得暴露个人信息。公司还必须遵守数据驻留政策，确保所有处理都发生在每位客户所在的同一个 AWS Region 内。在正式向客户开放之前，公司希望评估 AI 助手防止个人信息泄露的实际效果。哪种解决方案能够满足这些要求？

- **A.** 配置 Cross-Region Amazon Bedrock Guardrail，应用 Sensitive Information Filter。开发和测试阶段将 Guardrail 设置为 Detect Mode，生产部署时切换为 Block Mode。
- **B.** 配置 Amazon Bedrock Guardrail，应用 Sensitive Information Filter。开发和测试阶段设置为 Mask Mode，生产部署时切换为 Block Mode；并在公司运营的每个 Region 分别部署一份该 Guardrail。 **（最高票）**
- **C.** 配置 Amazon Bedrock Guardrail，应用 Content Filter 和 Topic Filter。在开发、测试和生产阶段始终使用 Detect Mode，并关闭 Amazon Bedrock 模型的 Invocation Logging。
- **D.** 配置 Cross-Region Amazon Bedrock Guardrail，应用一组 Content Filter 和 Word Filter。开发和测试阶段使用 Detect Mode，生产部署时切换为 Mask Mode。

### English

A company is using Amazon Bedrock to develop a customer support AI assistant. The AI assistant must respond to customer questions about their accounts. The AI assistant must not expose personal information in responses. The company must comply with data residency policies by ensuring that all processing occurs within the same AWS Region where each customer is located. The company wants to evaluate how effective the AI assistant is at preventing the exposure of personal information before the company makes the AI assistant available to customers. Which solution will meet these requirements?

- **A.** Configure a cross-Region Amazon Bedrock guardrail to apply sensitive information filters. Set the guardrail to detect mode during development and testing. Switch to block mode for production deployment.
- **B.** Configure an Amazon Bedrock guardrail to apply sensitive information filters. Set the guardrail to mask mode during development and testing. Switch to block mode for production deployment. Deploy a copy of the guardrail to each Region where the company operates. **(Most Voted)**
- **C.** Configure an Amazon Bedrock guardrail to apply content and topic filters. Set the guardrail to detect mode during development, testing, and production. Disable invocation logging for the Amazon Bedrock model.
- **D.** Configure a cross-Region Amazon Bedrock guardrail to apply a set of content and word filters. Set the guardrail to detect mode during development and testing. Switch to mask mode for production deployment.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 92 - Topic 1

### 中文

一所大学最近把一批档案文档、学术期刊和手稿数字化，并把数字文件存储在 AWS Lake Formation Data Lake 中。大学聘请一名 GenAI Developer，构建允许用户通过文本查询搜索这些数字文件的方案。系统必须返回与用户 Query 在语义上相似的期刊摘要；用户还必须能够根据摘要文本和相关 Metadata 搜索整个数字化集合。数字文件的 Metadata 中**没有关键词**，因此系统必须根据文本本身的相似性匹配摘要。数据湖中文件数量少于 100 万。哪种方案能够以**最低运维开销**满足要求？

- **A.** 使用 Amazon Bedrock 中的 Amazon Titan Embeddings 为数字文件创建向量表示，并把 Embedding 存储在 Amazon OpenSearch Service 的 OpenSearch Neural Plugin 中。
- **B.** 使用 Amazon Comprehend 从数字文件提取 Topic。把 Topic 和文件 Metadata 存入 Amazon Aurora PostgreSQL，再根据 Aurora 中的数据查询摘要 Metadata。
- **C.** 使用 Amazon SageMaker AI 部署 Sentence-Transformer Model，并用该模型为数字文件创建向量表示。把 Embedding 存入启用了 `pgvector` 扩展的 Amazon Aurora PostgreSQL。
- **D.** 使用 Amazon Bedrock 中的 Amazon Titan Embeddings 为数字文件创建向量表示，并把 Embedding 存入启用了 `pgvector` 扩展的 Amazon Aurora PostgreSQL Serverless。 **（最高票）**

### English

A university recently digitized a collection of archival documents, academic journals, and manuscripts. The university stores the digital files in an AWS Lake Formation data lake. The university hires a GenAI developer to build a solution to allow users to search the digital files by using text queries. The solution must return journal abstracts that are semantically similar to a user's query. Users must be able to search the digitized collection based on text and metadata that is associated with the journal abstracts. The metadata of the digitized files does not contain keywords. The solution must match similar abstracts to one another based on the similarity of their text. The data lake contains fewer than 1 million files. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Use Amazon Titan Embeddings in Amazon Bedrock to create vector representations of the digitized files. Store embeddings in the OpenSearch Neural Plugin for Amazon OpenSearch Service.
- **B.** Use Amazon Comprehend to extract topics from the digitized files. Store the topics and file metadata in an Amazon Aurora PostgreSQL database. Query the abstract metadata against the data in the Aurora database.
- **C.** Use Amazon SageMaker AI to deploy a sentence-transformer model. Use the model to create vector representations of the digitized files. Store embeddings in an Amazon Aurora PostgreSQL database that has the pgvector extension.
- **D.** Use Amazon Titan Embeddings in Amazon Bedrock to create vector representations of the digitized files. Store embeddings in an Amazon Aurora PostgreSQL Serverless database that has the pgvector extension. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (100%)

---

## Question 93 - Topic 1

### 中文

一家金融服务公司希望使用 Amazon Bedrock Foundation Model（FM）分析呼叫中心录音。通话结束后，呼叫中心会把录音以 MP3 文件形式存入 Amazon S3 Bucket，平均每个录音约 20 MB。每当新文件创建后，公司都需要尽快为录音生成**结构化格式的摘要和情感分析结果**。以下哪两个方案组合能够满足要求？（选择两项。）

- **A.** 使用 AWS Step Functions 编排录音处理工作流。配置步骤调用 Amazon Transcribe 把音频转换为文本、验证任务完成状态，再调用 AWS Lambda，让 Lambda 使用 Amazon Bedrock FM 处理文本并生成结构化分析输出。
- **B.** 使用 AWS Step Functions 编排工作流。调用 Amazon Transcribe 将音频转成文本并验证任务完成状态，然后直接调用 Amazon Bedrock FM，以 JSON 格式生成摘要和情感分析。
- **C.** 使用 AWS Step Functions 编排工作流。配置步骤调用 Amazon Transcribe 将音频转换为文本并验证任务完成状态，再调用 AWS Lambda 创建 Prompt，并使用 Amazon Bedrock FM 生成结构化分析输出。 **（最高票）**
- **D.** 配置源 S3 Bucket 向 Amazon EventBridge 发送事件。当 Bucket 中创建 Object 时，使用 EventBridge Rule 启动 Step Functions 工作流。 **（最高票）**
- **E.** 配置源 S3 Bucket，在创建 Object 时直接向 Step Functions 工作流发送 Notification。

### English

A financial services company wants to use Amazon Bedrock foundation models (FMs) to analyze call center recordings. When calls end, the call center stores recordings as MP3 files in an Amazon S3 bucket. The company needs to generate summaries and sentiment analysis for the recordings in a structured format as soon as new files are created. The recordings average 20 MB in size. Which combination of solutions will meet these requirements? (Choose two.)

- **A.** Use AWS Step Functions to orchestrate a workflow to process the recordings. Configure steps to invoke Amazon Transcribe to convert audio to text, validate job completion, and to invoke an AWS Lambda function to process the text by using Amazon Bedrock FMs to generate structured analysis output.
- **B.** Use AWS Step Functions to orchestrate a workflow to process the recordings. Configure steps to invoke Amazon Transcribe to convert audio to text, validate job completion, and to directly invoke Amazon Bedrock FMs to generate summaries and sentiment analysis in JSON format.
- **C.** Use AWS Step Functions to orchestrate a workflow to process the recordings. Configure steps to invoke Amazon Transcribe to convert audio to text, validate job completion, and to invoke an AWS Lambda function to create a prompt to invoke Amazon Bedrock FMs to generate structured analysis output. **(Most Voted)**
- **D.** Configure the source S3 bucket to send events to Amazon EventBridge. Create an EventBridge rule to invoke the Step Functions workflow when an object is created in the bucket. **(Most Voted)**
- **E.** Configure the source S3 bucket to send notifications to the Step Functions workflow when an object is created in the bucket.

**Correct Answer / 正确答案:** `CD`

**Community vote distribution / 社区投票分布:**

- CD (67%)
- AD (33%)

---

## Question 94 - Topic 1

### 中文

一家医疗器械公司希望把使用其设备进行医疗操作的报告输入 AI 助手。为了保护患者隐私，AI 助手只能向外科医生暴露患者 Personally Identifiable Information（PII），而对工程师则必须对 PII 进行 Redaction。AI 助手只能引用**最近 3 年内**的医疗报告。每份报告发布后，公司会立即把它存入 Amazon S3 Bucket。公司已经配置了 Amazon Bedrock Knowledge Base，AI 助手使用 Amazon Cognito 对用户进行身份认证。哪种解决方案能够满足这些要求？

- **A.** 在 S3 Bucket 上启用 Amazon Macie PII Detection。使用 S3 Trigger 调用 AWS Lambda，对报告中的 PII 进行 Redaction；Lambda 同时删除 Bucket 中过期文档，并调用 Knowledge Base Sync。
- **B.** 当新报告上传到 S3 时，调用 AWS Lambda 同步 S3 Bucket 和 Knowledge Base。使用第二个 Lambda 调用 Amazon Comprehend：如果用户属于 Engineer Cognito User Group，则检测并 Redact PII。配置 S3 Lifecycle 删除超过 3 年的报告。
- **C.** 在 Bucket 上配置 S3 Lifecycle，删除超过 3 年的报告。安排 AWS Lambda 每天同步 Bucket 与 Knowledge Base。用户与 AI 助手交互时，根据其 Cognito User Group 调用对应的 `ApplyGuardrail` 配置，在需要时对 Agent 响应中的 PII 进行 Redaction。 **（最高票）**
- **D.** 创建第二个 Knowledge Base。配置 S3 Lifecycle 删除超过 3 年的报告。新报告上传时调用 AWS Lambda，把 Bucket 与原始 Knowledge Base 同步；同时使用 Amazon Comprehend 检测并 Redact PII，再与第二个 Knowledge Base 同步。用户交互时根据 Cognito User Group 将模型重定向到对应 Knowledge Base。

### English

A medical device company wants to feed reports of medical procedures that used the company's devices into an AI assistant. To protect patient privacy, the AI assistant must expose patient personally identifiable information (PII) only to surgeons. The AI assistant must redact PII for engineers. The AI assistant must reference only medical reports that are less than 3 years old. The company stores reports in an Amazon S3 bucket as soon as each report is published. The company has already set up an Amazon Bedrock knowledge base. The AI assistant uses Amazon Cognito to authenticate users. Which solution will meet these requirements?

- **A.** Enable Amazon Macie PII detection on the S3 bucket. Use an S3 trigger to invoke an AWS Lambda function that redacts PII from the reports. Configure the Lambda function to delete outdated documents from the bucket and to invoke knowledge base syncing.
- **B.** Invoke an AWS Lambda function to sync the S3 bucket and the knowledge base when a new report is uploaded to the bucket. Use a second Lambda function to invoke Amazon Comprehend to detect and redact PII if a user is part of the engineer Cognito user group. Set up an S3 Lifecycle configuration to remove reports that are older than 3 years from the bucket.
- **C.** Set up an S3 Lifecycle configuration on the bucket to remove reports that are older than 3 years. Schedule an AWS Lambda function to run daily syncs between the bucket and the knowledge base. When users interact with the AI assistant, call the ApplyGuardrail configuration that matches the user's Cognito user group to redact PII from the agent’s responses if appropriate. **(Most Voted)**
- **D.** Create a second knowledge base. Set up an S3 Lifecycle configuration on the bucket to remove reports that are older than 3 years. Invoke an AWS Lambda function that syncs the bucket with the original knowledge base when a new report is uploaded to the bucket. Use Amazon Comprehend to detect and redact PII before syncing the bucket with the second knowledge base. When a user interacts with the AI assistant, redirect the model to the appropriate knowledge base depending on the user’s Cognito user group.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (57%)
- D (43%)

---

## Question 95 - Topic 1

### 中文

一家大型电商公司部署了 Foundation Model（FM）用于生成商品描述。工程团队使用 Amazon CloudWatch 监控 Token 使用量、延迟和错误率等技术指标；营销团队则在自己的系统中跟踪 Conversion Rate、Revenue Impact 等业务指标。公司需要统一的可观测性方案，把技术性能与业务结果关联起来；当运行指标表明性能退化时，必须自动向相关 Stakeholder 发出告警；同时要对技术和业务指标提供完整统一的可见性。哪种解决方案能够满足要求？

- **A.** 创建 CloudWatch Dashboard，同时包含技术指标和导入的业务指标。配置 CloudWatch Composite Alarm，把技术数据和业务数据组合起来，并使用 Amazon SNS 向 Stakeholder 发送通知。
- **B.** 使用 Amazon Managed Grafana，把来自 CloudWatch 的技术指标与外部来源的业务指标一起可视化。配置 Grafana Alert 调用 AWS Lambda，当指标超过预定义阈值时由 Lambda 自动修复问题。
- **C.** 使用 CloudWatch Metric Stream 把 CloudWatch 指标流式发送到 Amazon S3。创建 Amazon QuickSight Dashboard 展示组合后的技术和业务指标，并配置 Amazon EventBridge Rule，在指标超过预定义阈值时向 Stakeholder 发送通知。
- **D.** 配置 CloudWatch Custom Dashboard，把运行指标与导入的业务指标整合在一起。设置带 Anomaly Detection 的 CloudWatch Composite Alarm，并使用 Amazon SNS 创建 Alarm Action，当关联指标显示性能问题时通知 Stakeholder。 **（最高票）**

### English

A large ecommerce company has deployed a foundation model (FM) to generate product descriptions. The company's engineering team monitors technical metrics such as token usage, latency, and error rates by using Amazon CloudWatch. The company's marketing team tracks business metrics such as conversion rates and revenue impact in its own systems. The company needs a unified observability solution that correlates technical performance with business outcomes. The solution must provide automatic alerts to stakeholders when operational metrics indicate degradation. The solution must provide comprehensive visibility across both technical and business metrics. Which solution will meet these requirements?

- **A.** Create CloudWatch dashboards that include technical metrics and imported business metrics. Configure CloudWatch composite alarms that combine technical data and business data. Use Amazon SNS to set up notifications to stakeholders.
- **B.** Use Amazon Managed Grafana to visualize technical metrics from CloudWatch with business metrics from external sources. Configure Amazon Managed Grafana alerts to invoke AWS Lambda functions. Configure the Lambda functions to remediate issues automatically when metrics exceed predefined thresholds.
- **C.** Stream CloudWatch metrics to Amazon S3 by using CloudWatch metric streams. Create Amazon QuickSight dashboards to visualize the combined technical metrics and business metrics. Set up Amazon EventBridge rules to send notifications to stakeholders when metrics exceed predefined thresholds.
- **D.** Configure CloudWatch custom dashboards that integrate operational metrics with imported business metrics. Set up CloudWatch composite alarms with anomaly detection. Use Amazon SNS to create alarm actions to notify stakeholders when correlated metrics indicate performance issues. **(Most Voted)**

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**

- D (75%)
- A (25%)

---

## Question 96 - Topic 1

### 中文

一名 GenAI Developer 正在评估 Amazon Bedrock Foundation Model（FM），用于增强一家欧洲公司的内部业务应用。公司通过 AWS Control Tower 建立了 Multi-Account Landing Zone，并使用 SCP 规定账户只能使用 `eu-north-1` 和 `eu-west-1` Region。所有客户数据必须始终在批准 AWS Region 的私有网络中传输和处理。GenAI Developer 经过分析与测试选择了一个 FM，而该模型托管在 `eu-central-1` 和 `eu-west-3`。开发人员必须让公司员工能够访问该 FM，并确保所有发往 FM 的请求都通过私有网络，而且保持在模型实际所在的 Region 范围内。哪种解决方案能够满足要求？

- **A.** 在 `eu-north-1` 部署 AWS Lambda，并通过 Private Amazon API Gateway REST API 暴露给 VPC。在 `eu-central-1` 和 `eu-west-3` 为所选 FM 创建 VPC Endpoint，扩展现有 SCP 允许员工使用 FM，并将 REST API 与业务应用集成。
- **B.** 在 `eu-north-1` 的 Amazon EC2 上部署 FM，并在 EC2 前部署 Private Amazon API Gateway REST API。配置 Amazon Bedrock VPC Endpoint，并将 REST API 与业务应用集成。
- **C.** 配置 FM 通过 `eu.amazon.*` Endpoint 使用 Cross-Region Inference，确保调用始终留在欧洲。配置 Amazon Bedrock VPC Endpoint，并扩展现有 SCP，允许员工通过欧洲区域中模型可用的 Inference Profile 使用 FM。使用 Inference Profile 将 Amazon Bedrock 与业务应用集成。
- **D.** 在 `eu-north-1` 的 Amazon SageMaker AI 中部署 FM。配置 SageMaker AI VPC Endpoint，扩展现有 SCP 允许员工使用 SageMaker AI Endpoint，并将 SageMaker AI 中的 FM 与业务应用集成。

### English

A GenAI developer is evaluating Amazon Bedrock foundation models (FMs) to enhance a Europe-based company's internal business application. The company has a multi-account landing zone in AWS Control Tower. The company uses SCPs to allow its accounts to use only the eu-north-1 Region and the eu-west-1 Region. All customer data must remain in private networks within the approved AWS Regions. The GenAI developer selects an FM based on analysis and testing and hosts the model in the eu-central-1 Region and the eu-west-3 Region. The GenAI developer must enable access to the FM for the company’s employees. The GenAI developer must ensure that requests to the FM are private and remain with the same Regions as the FM. Which solution will meet these requirements?

- **A.** Deploy an AWS Lambda function that is exposed by a private Amazon API Gateway REST API to a VPC in eu-north-1. Create a VPC endpoint for the selected FM in eu-central-1 and eu-west-3. Extend existing SCPs to allow employees to use the FM. Integrate the REST API with the business application.
- **B.** Deploy the FM on Amazon EC2 instances in eu-north-1. Deploy a private Amazon API Gateway REST API in front of the EC2 instances. Configure an Amazon Bedrock VPC endpoint. Integrate the REST API with the business application.
- **C.** Configure the FM to use cross-Region inference through an eu.amazon.* endpoint to ensure that all calls remain within Europe. Configure an Amazon Bedrock VPC endpoint. Extend existing SCPs to allow employees to use the FM through inference profiles in Europe-based Regions where the FM is available. Use an inference profile to integrate Amazon Bedrock with the business application.
- **D.** Deploy the FM in Amazon SageMaker AI in eu-north-1. Configure a SageMaker AI VPC endpoint. Extend existing SCPs to allow employees to use the SageMaker AI endpoint. Integrate the FM in SageMaker AI with the business application.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**

- C (100%)

---

## Question 97 - Topic 1

### 中文

一家医疗保健公司正在开发处理医学查询的应用。应用必须通过减少 Semantic Dilution（语义稀释）来高准确率地回答复杂问题；还必须参考医学文档中的领域专用术语，以减少医学术语歧义。应用每分钟必须处理 1,000 条查询，响应时间低于 2 秒。哪种方案能够以**最低运维开销**满足要求？

- **A.** 使用 Amazon API Gateway 将传入查询路由到 Amazon Bedrock Agent。配置 Agent 使用 Anthropic Claude Model 执行 Query Decomposition，并使用 Amazon Titan Model 执行 Query Expansion。创建 Amazon Bedrock Knowledge Base 存储参考医学文档。
- **B.** 配置 Amazon Bedrock Knowledge Base 存储参考医学文档，并在 Knowledge Base 中启用 Query Decomposition。配置 Amazon Bedrock Flow，结合 Foundation Model（FM）和 Knowledge Base 支持应用。
- **C.** 使用 Amazon SageMaker AI 托管自定义 ML Model，分别执行 Query Decomposition 和 Query Expansion。配置 Amazon Bedrock Knowledge Bases 存储参考医学文档，并加密 Knowledge Base 中的文档。
- **D.** 创建 Amazon Bedrock Agent，编排多个 AWS Lambda 进行 Query Decomposition。创建 Amazon Bedrock Knowledge Base 存储参考医学文档，并使用 Agent 内置 Knowledge Base 能力。为 Agent 添加 Deep Research 和 Reasoning 能力以减少医学术语歧义。

### English

A healthcare company is developing an application to process medical queries. The application must answer complex queries with high accuracy by reducing semantic dilution. The application must refer to domain-specific terminology in medical documents to reduce ambiguity in medical terminology. The application must be able to respond to 1,000 queries each minute with response times less than 2 seconds. Which solution will meet these requirements with the LEAST operational overhead?

- **A.** Use Amazon API Gateway to route incoming queries to an Amazon Bedrock agent. Configure the agent to use an Anthropic Claude model to decompose queries and an Amazon Titan model to expand queries. Create an Amazon Bedrock knowledge base to store the reference medical documents.
- **B.** Configure an Amazon Bedrock knowledge base to store the reference medical documents. Enable query decomposition in the knowledge base. Configure an Amazon Bedrock flow that uses a foundation model (FM) and the knowledge base to support the application.
- **C.** Use Amazon SageMaker Al to host custom ML models for both query decomposition and query expansion. Configure Amazon Bedrock knowledge bases to store the reference medical documents. Encrypt the documents in the knowledge base.
- **D.** Create an Amazon Bedrock agent to orchestrate multiple AWS Lambda functions to decompose queries. Create an Amazon Bedrock knowledge base to store the reference medical documents. Use the agent's built-in knowledge base capabilities. Add deep research and reasoning capabilities to the agent to reduce ambiguity in the medical terminology.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**

- B (100%)

---

## Question 98 - Topic 1

### 中文

一家公司拥有使用 Amazon Bedrock 为客户咨询生成个性化响应的客户服务应用。公司需要建立 Quality Assurance（质量保证）流程，以便在每次更新时评估 Prompt 效果和模型配置。流程必须自动比较多个 Prompt Template 的输出、检测响应质量问题、提供量化指标，并允许人工审核员对响应提供反馈。任何未达到预定义质量阈值的配置都必须被阻止部署。哪种解决方案能够满足要求？
---

- **A.** 创建 AWS Lambda，把样本客户咨询发送到多个 Amazon Bedrock Model Configuration，并将响应存储在 Amazon S3。使用 Amazon QuickSight 可视化响应模式，每天人工审核输出，并使用 AWS CodePipeline 部署质量分数高于指定阈值的配置。
- **B.** 使用 Amazon Bedrock Evaluation Job，通过自定义 Prompt Dataset 比较模型输出。配置 AWS CodePipeline，在 Prompt Template 发生变化时运行 Evaluation Job，并只部署质量分数达到指定阈值的配置。
- **C.** 设置 Amazon CloudWatch Alarm，监控 Amazon Bedrock 的响应延迟和错误率。使用 Amazon EventBridge Rule 在指标超过阈值时通知公司，并在 AWS Systems Manager 中配置 Approval Workflow 执行人工质量检查。
- **D.** 使用 AWS Lambda 创建自动测试框架，对生产流量采样，并把重复请求路由给更新后的模型版本。使用 Amazon Comprehend Sentiment Analysis 比较结果，如果情感分数下降则阻止部署。

### English

A company has a customer service application that uses Amazon Bedrock to generate personalized responses to customer inquiries. The company needs to establish a quality assurance process to evaluate prompt effectiveness and model configurations across updates. The process must automatically compare outputs from multiple prompt templates, detect response quality issues, provide quantitative metrics, and allow human reviewers to give feedback on responses. The process must prevent configurations that do not meet a predefined quality threshold from being deployed. Which solution will meet these requirements?
---

- **A.** Create an AWS Lambda function that sends sample customer inquiries to multiple Amazon Bedrock model configurations and stores responses in Amazon S3. Use Amazon QuickSight to visualize response patterns. Manually review outputs daily. Use AWS CodePipeline to deploy the configurations that have quality scores above the specified quality threshold.
- **B.** Use Amazon Bedrock evaluation jobs to compare model outputs by using custom prompt datasets. Configure AWS CodePipeline to run the evaluation jobs when prompt templates change. Configure CodePipeline to deploy the configurations that have quality scores above the specified quality threshold.
- **C.** Set up Amazon CloudWatch alarms to monitor response latency and error rates from Amazon Bedrock. Use Amazon EventBridge rules to notify the company when latency and error rate metrics exceed thresholds. Configure an approval workflow in AWS Systems Manager to perform manual quality checks.
- **D.** Use AWS Lambda functions to create an automated testing framework that samples production traffic and routes duplicate requests to the updated model version. Use Amazon Comprehend sentiment analysis to compare results. Block deployment if sentiment scores decrease.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---


## Question 99 - Topic 1

### 中文

一家零售公司运行商品推荐应用，在公司网站上向客户推荐产品。应用使用 Amazon Bedrock，通过动态构造 Prompt 并发送给 Foundation Model（FM）生成推荐。一名 GenAI Developer 最近部署了应用更新，要求 FM 在响应中包含一条特定促销信息。但测试发现，这条促销信息并不会每次都出现；即使出现，有时也与前后文本衔接不自然。开发人员必须确保促销信息**始终出现在 FM 响应中**。哪种解决方案能够满足要求？
---

- **A.** 对 Prompt 使用 Amazon Bedrock Guardrails Filter，并把 Input Filter Strength 设置为 `HIGH`。
- **B.** 生成多个包含促销信息的响应变体，再使用 Reranker Model，根据与原始 Prompt 的相关性选出最连贯的版本。
- **C.** 先通过 Amazon Bedrock 运行 Prompt，再把响应交给 Amazon Bedrock Agents 添加促销信息，并使用原始 Prompt 和目标促销信息作为 Context 对结果执行 Rerank。
- **D.** 在发送给 FM 的 Prompt 中使用 Output Indicator，进一步强化“商品推荐必须包含新促销信息”的要求。

### English

A retail company runs an application that makes product recommendations to customers on the company’s website. The application uses Amazon Bedrock to generate recommendations by dynamically constructing prompts and sending them to foundation models (FMs). A GenAI developer has deployed an update to the application that instructs the FM to include a specific promotional message when the FM generates a response to prompts. When the developer tests the application, the promotional message does not always appear in the responses. When the promotional message does appear in the responses, it does not always flow with the rest of the text. The GenAI developer must ensure that the promotional message always appears in the FM responses. Which solution will meet this requirement?
---

- **A.** Use an Amazon Bedrock Guardrails filter on the prompt. Set the input filter strength to HIGH.
- **B.** Generate multiple response variants that include the promotional message in different ways. Use a reranker model to select the most coherent version based on relevance to the original prompt.
- **C.** Run the prompt through Amazon Bedrock. Process the response through Amazon Bedrock Agents to add the promotional message. Rerank the results by using the original prompt and the desired message as context.
- **D.** Reinforce the requirement to include the new promotional message within product recommendations by using an output indicator in prompts to the FM.

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**


---

## Question 100 - Topic 1

### 中文

一家公司正在创建使用 Amazon Bedrock Foundation Model（FM）的生成式 AI（GenAI）应用。应用必须使用 Microsoft Entra ID 进行身份认证；所有 FM API 调用必须始终走私有网络路径；不同部门只能访问指定 Model Family；公司还需要保留完整的模型交互审计追踪。哪种解决方案能够满足这些要求？
---

- **A.** 在 Microsoft Entra ID 与 IAM 之间配置 SAML Federation。创建部门专属 IAM Role，只允许所需 `ModelId`。为 Amazon Bedrock Runtime Service 创建 AWS PrivateLink Interface VPC Endpoint。启用 AWS CloudTrail 捕获 Amazon Bedrock API 调用，并配置 Amazon Bedrock Model Invocation Logging 记录详细模型交互。
- **B.** 在 IAM 中创建 Identity Provider（IdP）连接，通过 Microsoft Entra ID 认证。分配部门 Permission Set 控制特定 Model Family 访问权限。把 AWS Lambda 部署到带 NAT Gateway 的私有子网，通过 NAT 访问 Amazon Bedrock Public Endpoint。启用 CloudWatch Logs 捕获模型交互用于审计。
- **C.** 在 IAM 中创建 SAML IdP，通过 Microsoft Entra ID 认证。使用 IAM Permissions Boundary 限制部门 Role 对特定 Model Family 的访问。配置 Public Amazon Bedrock API Endpoint，并通过 VPC Routing 维持私有网络连接。配置 AWS CloudTrail 和 Amazon S3 Lifecycle Rule 管理模型交互审计日志。
- **D.** 在 Microsoft Entra ID 与 IAM 之间配置 OpenID Connect（OIDC）Federation。使用 Attribute-Based Access Control，把部门属性映射到模型访问权限；应用 SCP 根据部门限制 Amazon Bedrock FM Family 访问，并使用 Microsoft Entra ID 内置日志记录模型交互审计追踪。

### English

A company is creating a generative AI (GenAI) application that uses Amazon Bedrock foundation models (FMs). The application must use Microsoft Entra ID to authenticate. All FM API calls must stay on private network paths. Access to the application must be limited by department to specific model families. The company also needs a comprehensive audit trail of model interactions. Which solution will meet these requirements?
---

- **A.** Configure SAML federation between Microsoft Entra ID and IAM. Create department-specific IAM roles that allow only the required ModelId values. Create AWS PrivateLink interface VPC endpoints for Amazon Bedrock runtime services. Enable AWS CloudTrail to capture Amazon Bedrock API calls. Configure Amazon Bedrock model invocation logging to record detailed model interactions.
- **B.** Create an identity provider (IdP) connection in IAM to authenticate by using Microsoft Entra ID. Assign department permission sets to control access to specific model families. Deploy AWS Lambda functions in private subnets with a NAT gateway for egress to Amazon Bedrock public endpoints. Enable CloudWatch Logs to capture model interactions for auditing purposes.
- **C.** Create a SAML identity provider (IdP) in IAM to authenticate by using Microsoft Entra ID. Use IAM permissions boundaries to limit department roles’ access to specific model families. Configure public Amazon Bedrock API endpoints with VPC routing to maintain private network connectivity. Set up AWS CloudTrail with Amazon S3 Lifecycle rules to manage audit logs of model interactions.
- **D.** Configure OpenID Connect (OIDC) federation between Microsoft Entra ID and IAM. Use attribute-based access control to map department attributes to specific model access permissions. Apply SCP policies to restrict access to Amazon Bedrock FM families based on department. Use Microsoft Entra ID’s built-in logging capabilities to maintain an audit trail of model interactions.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 101 - Topic 1

### 中文

一家医疗保健公司正在开发 AI 应用，用于检索医学文献帮助医生开展研究。公司计划将应用部署到多个国家。应用需要为 50,000 份以文本为主的医学文档建立索引，这些文档包含英语、法语和德语的专业术语。应用必须针对医学术语提供很高的搜索准确率，同时公司希望尽量减少自定义代码开发和持续维护工作。哪种解决方案能够满足这些要求？
---

- **A.** 使用 Amazon Bedrock 中的 Amazon Titan Text Embeddings，直接处理所有文档的原始语言，不进行翻译。按医学章节级别应用 Semantic Chunking，以保留医学术语上下文，同时保持适中的向量维度。
- **B.** 使用 Amazon Titan Text Embeddings，在处理之前先把所有非英语文档翻译成英语，并基于逻辑章节实现 Document Chunking Strategy 以提高检索准确率。
- **C.** 使用 Amazon Titan Multimodal Embeddings，把文档页面转换成图片，并为图片生成文本 Caption，构建处理医学内容的 Multimodal Knowledge Base。
- **D.** 使用 Amazon Bedrock Knowledge Base 作为 Vector Store。使用 Amazon Kendra 执行多语言文档处理，并配置 Vector Store 对每条查询自动进行语言检测和翻译。

### English

A healthcare company is developing an AI application to retrieve medical literature to help doctors conduct research. The company wants to deploy the application to multiple countries. The application needs to index 50,000 text-heavy medical documents that contain specialized terminology in English, French, and German. The application must provide high search accuracy for medical terminology. The company wants a solution that requires minimal custom code development and ongoing maintenance. Which solution will meet these requirements?
---

- **A.** Use Amazon Titan Text Embeddings to process all documents in their original language without translation. Apply semantic chunking at the medical section level to preserve medical terminology context and to maintain moderate vector dimensions.
- **B.** Use Amazon Titan Text Embeddings to translate all non-English documents to English before processing. Implement document chunking strategies based on logical sections to improve retrieval accuracy.
- **C.** Use Amazon Titan Multimodal Embeddings to convert document pages to images. Generate text captions for the images to create a multimodal knowledge base that handles medical content.
- **D.** Use an Amazon Bedrock knowledge base as the vector store. Use Amazon Kendra to perform multilingual document processing. Configure the vector store to automatically handle language detection and translation for each query.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 102 - Topic 1

### 中文

一家出版公司正在开发 Chat Assistant，使用运行在 Amazon SageMaker AI 上的容器化大型语言模型（LLM）。架构由 Amazon API Gateway REST API、AWS Lambda 和托管 LLM 的 SageMaker AI Real-Time Endpoint 组成：API Gateway 将用户请求路由到 Lambda，再由 Lambda 调用 SageMaker Endpoint。用户反馈响应时间不稳定。分析显示，大量聊天在等待首个 Token 超过 2 秒后就被放弃。公司希望交互请求的 P95 延迟保持在 800 ms 以下。以下哪两个方案组合能够满足要求？（选择两项。）
---

- **A.** 在 Container 启动时预加载模型，并实施 Dynamic Batching，让多个用户请求在一次推理中共同处理。
- **B.** 为 SageMaker AI Endpoint 选择更大的 GPU Instance Type，把最小实例数设置为 0，继续逐请求处理，并在第一个请求到达时 Lazy Load 模型权重。
- **C.** 切换到 Multi-Model Endpoint，并使用 Lazy Loading，不进行 Request Batching。
- **D.** 将最小实例数设置为大于 0，并启用 Response Streaming。
- **E.** 所有请求都切换到 Amazon SageMaker Asynchronous Inference。把请求存储在 Amazon S3 中，并把最小实例数设置为 0。

### English

A publishing company is developing a chat assistant that uses a containerized large language model (LLM) that runs on Amazon SageMaker AI. The architecture consists of an Amazon API Gateway REST API that routes user requests to an AWS Lambda function. The Lambda function invokes a SageMaker AI real-time endpoint that hosts the LLM. Users report uneven response times. Analytics show that a high number of chats are abandoned after 2 seconds of waiting for the first token. The company wants a solution to ensure that p95 latency is under 800 ms for interactive requests to the chat assistant. Which combination of solutions will meet this requirement? (Choose two.)
---

- **A.** Enable model preload upon container startup. Implement dynamic batching to process multiple user requests together in a single inference pass.
- **B.** Select a larger GPU instance type for the SageMaker AI endpoint. Set the minimum number of instances to 0. Continue to perform per-request processing. Lazily load model weights on the first request.
- **C.** Switch to a multi-model endpoint. Use lazy loading without request batching.
- **D.** Set the minimum number of instances to greater than 0. Enable response streaming.
- **E.** Switch to Amazon SageMaker Asynchronous Inference for all requests. Store requests in an Amazon S3 bucket. Set the minimum number of instances to 0.

**Correct Answer / 正确答案:** `AD`

**Community vote distribution / 社区投票分布:**


---

## Question 103 - Topic 1

### 中文

一家公司运行使用多个 Amazon Bedrock Foundation Model（FM）生成文本的 GenAI 应用，为三个 AWS Region 的用户提供服务，应用本身运行在 AWS Lambda 上。公司需要根据响应质量和延迟指标实现 Dynamic Model Selection；必须能够在不修改或重新部署应用代码的情况下切换模型；还希望新模型从 20% 流量开始逐步发布，并在错误率超过 1% 时自动回滚。哪种解决方案能够满足这些要求？
---

- **A.** 使用 AWS AppConfig 为模型选择标准创建 Feature Flag。设置针对错误率的 Validation Rule。使用 AWS AppConfig Agent Lambda Extension，让函数运行时获取模型配置；定义 Linear Deployment Strategy，从 20% 流量开始逐步发布新模型。
- **B.** 将模型选择配置存储在 Amazon DynamoDB 中，并使用 Global Table 实现 Multi-Region Replication。配置 Amazon EventBridge Rule 监控 Amazon CloudWatch Error Metric，并调用 AWS Lambda 更新 DynamoDB 中的模型偏好。再为 Lambda 添加自定义代码，每次模型调用前查询 DynamoDB。
- **C.** 为每种模型配置创建 Lambda Function Version。使用 Lambda Alias 和 Routing Configuration，从 20% 流量开始逐步切换版本。实施 AWS CodeDeploy Deployment，监控各 Lambda Function Version 的 Amazon CloudWatch Metric，在错误率超过阈值时自动回滚。
- **D.** 创建 Amazon CloudWatch Dashboard 监控响应质量和延迟。Lambda 内自定义实现根据 Metric Threshold 选择模型，并使用存储在 AWS Systems Manager Parameter Store 中的环境变量定义可跨 Region 更新的模型选择参数。

### English

A company runs a GenAI application that uses multiple foundation models (FMs) from Amazon Bedrock to generate text. The application serves users across three AWS Regions. The application runs on AWS Lambda functions. The company needs to implement dynamic model selection based on response quality and latency metrics. The company must be able to switch between models without modifying or redeploying the application code. The company wants to gradually roll out new models, starting with 20% of traffic. The company needs a solution that will automatically roll back if error rates exceed 1%. Which solution will meet these requirements?
---

- **A.** Use AWS AppConfig to create feature flags for model selection criteria. Set up validation rules for error rates. Implement the AWS AppConfig Agent Lambda extension to retrieve model configurations when the functions run. Define linear deployment strategies to gradually release new models, starting with 20% of traffic.
- **B.** Store model selection configurations in Amazon DynamoDUse global tables to enable multi-Region replication. Configure Amazon EventBridge rules to monitor Amazon CloudWatch metrics for errors and invoke AWS Lambda functions that update the DynamoDB table with model preferences. Add custom code to the Lambda functions to query DynamoDB before each model invocation.
- **C.** Create Lambda function versions for each model configuration. Use Lambda aliases and set up routing configurations to gradually shift traffic between versions, starting with 20% of traffic. Implement AWS CodeDeploy deployments that monitor Amazon CloudWatch metrics for the Lambda function versions to automatically roll back deployments when error rales exceed thresholds.
- **D.** Create Amazon CloudWatch dashboards to monitor response quality and latency metrics. Implement custom logic in the Lambda functions to select models based on metric thresholds. Use environment variables that are stored in AWS Systems Manager Parameter Store to define model selection parameters that can be updated across Regions.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 104 - Topic 1

### 中文

一家金融服务公司正在开发 AI Search Assistant，帮助投资顾问快速检索投资数据。应用作为 AWS Lambda 运行，并使用 Amazon Bedrock Knowledge Base；该 Knowledge Base 使用 Amazon OpenSearch Serverless 作为数据源。应用 Agent 必须能够大规模管理 Collection，并自动为符合特定 Pattern 的 Collection 和 Index 分配访问权限。公司使用 Amazon Bedrock Tool 测试 Knowledge Base。Knowledge Base Sync 成功完成，但测试时 BedrockAgentRuntime API 返回 `400 Bad Authorization`，访问 OpenSearch Serverless 时返回 `403 Forbidden`。公司必须解决权限问题。以下哪两个方案组合能够满足要求？（选择两项。）
---

- **A.** 更新 Lambda Execution Role，加入 `bedrock:InvokeAgent` 权限，并向 Lambda Execution Role 添加 `aoss:APIAccessAll` 权限。
- **B.** 创建 OpenSearch Serverless Data Access Policy，其中包含基于 Pattern 的 Resource Rule。
- **C.** 为 OpenSearch Serverless 配置 VPC Endpoint Policy，并把该 Endpoint 添加到 Lambda 的 VPC Configuration。
- **D.** 配置 AWS Secrets Manager 存储 OpenSearch Serverless Credential，并授予 Lambda 获取这些 Credential 的权限。
- **E.** 为 OpenSearch Serverless Domain 启用 IAM Authentication，并向 Lambda Execution Role 添加 `es:ESHttp*` 权限。

### English

A financial services company is developing an AI-powered search assistant application to help investment advisors quickly retrieve investment data. The application runs as an AWS Lambda function. The company is using Amazon Bedrock to develop the application by using an Amazon Bedrock knowledge base that uses Amazon OpenSearch Serverless as its data source. The application agent must manage collections at scale by automatically assigning access permissions to collections and indexes that match a specific pattern. The company uses Amazon Bedrock tools to test the knowledge base. The knowledge base sync process finishes successfully. However, the test reveals a 400 Bad Authorization error from the BedrockAgentRuntime API and a 403 Forbidden error when the test attempts to access OpenSearch Serverless. The company must resolve the permissions issues. Which combination of solutions will meet this requirement? (Choose two.)
---

- **A.** Update the Lambda function execution role to include the bedrock:InvokeAgent permission. Add the aoss:APIAccessAll permission to the Lambda execution role.
- **B.** Create an OpenSearch Serverless data access policy that includes pattern-based resource rules.
- **C.** Configure a VPC endpoint policy for OpenSearch Serverless. Add the endpoint to the Lambda function’s VPC configuration.
- **D.** Configure AWS Secrets Manager to store OpenSearch Serverless credentials. Grant the Lambda function access to retrieve the credentials.
- **E.** Enable IAM authentication for the OpenSearch Serverless domain. Add the es:ESHttp* permission to the Lambda function execution role.

**Correct Answer / 正确答案:** `AB`

**Community vote distribution / 社区投票分布:**


---

## Question 105 - Topic 1

### 中文

一家金融服务公司正在开发 GenAI 应用，让业务分析师能够通过自然语言查询敏感客户数据。GenAI Developer 必须实现 Content Safety Framework，在进行 Text-to-SQL Transformation 的同时防止有害输出。方案必须检测并阻止自然语言 Prompt 中的 SQL Injection 尝试；验证生成 SQL 是否符合预定义 Schema；并为 Query Transformation 创建审计追踪。哪种解决方案能够满足要求？
---

- **A.** 使用 Amazon Bedrock 集成内容过滤系统。部署 Amazon GuardDuty 并配置自定义 SQL Threat Detection Rule。使用 AWS Lambda 根据存储在 AWS Systems Manager Parameter Store 中的 Schema Definition 检查生成 Query，并使用 Amazon EventBridge 把所有 Transformation 记录存入 Amazon S3。
- **B.** 实施 Multi-Stage Validation Pipeline：首先使用 Amazon Bedrock Guardrails 过滤输入；配置 AWS Lambda 搭配 SQL Parsing Library 验证 Query Syntax；再使用 Amazon RDS Data API 根据定义好的 Schema 验证 Query，并在 Pipeline 真正执行每条 Query 前收集 Audit Log。
- **C.** 配置 AWS CloudTrail 记录所有 Database API Call。实施遵循 Least Privilege 的 IAM Role，并使用 AWS WAF 过滤进入应用的恶意输入。
- **D.** 训练自定义 Amazon SageMaker AI Model，安全地执行 Text-to-SQL。配置 VPC Endpoint 隔离客户数据库，并将 Query History 存入 Amazon DynamoDB。

### English

A financial services company is developing a GenAI application that allows business analysts to query sensitive customer data by using natural language. A GenAI developer must implement a content safety framework that performs text-to-SQL transformations while preventing harmful outputs. The solution must detect and block SQL injection attempts in natural language prompts, validate generated SQL queries against predefined schemas, and create an audit trail of query transformations. Which solution will meet these requirements?
---

- **A.** Use Amazon Bedrock with an integrated content filtering system. Deploy Amazon GuardDuty with custom SQL threat detection rules. Use AWS Lambda functions to check generated queries against schema definitions in AWS Systems Manager Parameter Store. Use Amazon EventBridge to store records of all transformations in Amazon S3.
- **B.** Implement a multi-stage validation pipeline that uses Amazon Bedrock Guardrails to filter inputs. Configure AWS Lambda functions to use with SQL parsing libraries to validate query syntax. Use the Amazon RDS Data API to verify queries against defined schemas and collect audit logs before the pipeline runs each query.
- **C.** Configure AWS CloudTrail to log all database API calls. Implement IAM roles that have least privilege access to the application. Use AWS WAF to filter malicious inputs to the application.
- **D.** Train a custom Amazon SageMaker AI model to securely transform text to SQL. Attach VPC endpoints to isolate the customer data database. Store query history in Amazon DynamoDB.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 106 - Topic 1

### 中文

一家公司需要一个能够从多种内容源自动生成学习材料的系统。内容源包括文档文件（PDF、PowerPoint 和 Word）以及多媒体文件（录制视频）。系统每天必须处理超过 10,000 个内容源，高峰时有 500 个并发上传；还必须从文档和多媒体中提取关键概念，生成并存储上下文准确的摘要。生成的学习材料必须支持带版本控制的实时协作。哪种解决方案能够满足这些要求？
---

- **A.** 使用 Amazon Bedrock Data Automation（BDA）配合 AWS Lambda 编排文档处理。使用 Amazon Bedrock Knowledge Bases 处理所有多媒体内容。把内容存入支持 Replication 的 Amazon DocumentDB，通过 Amazon SNS Topic Subscription 进行协作，并使用 Amazon Bedrock Agents 跟踪变更。
- **B.** 使用 Amazon Bedrock Data Automation（BDA）配合 Foundation Model（FM）处理文档。将 BDA 与 Amazon Textract 集成用于 PDF 提取，与 Amazon Transcribe 集成用于多媒体。把处理后内容存入启用了 Versioning 的 Amazon S3，把 Metadata 存入 Amazon DynamoDB，并使用 AWS AppSync GraphQL Subscription 与 DynamoDB 支持实时协作。
- **C.** 使用 Amazon Bedrock Data Automation（BDA）配合 Amazon SageMaker AI Endpoint 托管内容提取和摘要模型。使用 Amazon Bedrock Guardrails 从所有文件类型提取内容。将文档存入 Amazon Neptune 进行 Time Series Analysis，并使用 Amazon Bedrock Chat 进行实时消息协作。
- **D.** 使用 Amazon Bedrock Data Automation（BDA）配合 AWS Lambda 批量处理内容文件。微调 Amazon Bedrock FM，对所有内容类型进行文档分类。使用启用了 Cluster Mode 和 Sharding 的 Amazon ElastiCache（Redis OSS）存储处理数据，并使用 Amazon Bedrock Prompt Management 进行版本控制。

### English

A company needs a system to automatically generate study materials from multiple content sources. The content sources include document files (PDF files, PowerPoint presentations, and Word documents) and multimedia files (recorded videos). The system must process more than 10,000 content sources daily with peak loads of 500 concurrent uploads. The system must also extract key concepts from document files and multimedia files and create and store contextually accurate summaries. The generated study materials must support real-time collaboration with version control. Which solution will meet these requirements?
---

- **A.** Use Amazon Bedrock Data Automation (BDA) with AWS Lambda functions to orchestrate document file processing. Use Amazon Bedrock Knowledge Bases to process all multimedia. Store the content in Amazon DocumentDB with replication. Collaborate by using Amazon SNS topic subscriptions. Track changes by using Amazon Bedrock Agents.
- **B.** Use Amazon Bedrock Data Automation (BDA) with foundation models (FMs) to process document files. Integrate BDA with Amazon Textract for PDF extraction and with Amazon Transcribe for multimedia files. Store the processed content in Amazon S3 with versioning enabled. Store the metadata in Amazon DynamoDCollaborate in real time by using AWS AppSync GraphQL subscriptions with DynamoDB.
- **C.** Use Amazon Bedrock Data Automation (BDA) with Amazon SageMaker AI endpoints to host content extraction and summarization models. Use Amazon Bedrock Guardrails to extract content from all file types. Store document files in Amazon Neptune for time series analysis. Collaborate by using Amazon Bedrock Chat for real-time messaging.
- **D.** Use Amazon Bedrock Data Automation (BDA) with AWS Lambda functions to process batches of content files. Fine-tune foundation models (FMs) in Amazon Bedrock to classify documents across all content types. Store the processed data in Amazon ElastiCache (Redis OSS) by using Cluster Mode with sharding. Use Amazon Bedrock Prompt Management for version control.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 107 - Topic 1

### 中文

一家医疗保健公司希望开发一个 Proof of Concept（PoC）应用，使用 Amazon Bedrock 自动总结医疗文档。公司只有 3 周时间验证应用准确率。应用必须符合公司的数据隐私政策，并包含用于评估摘要准确率和处理时间的指标。哪种解决方案能够满足这些要求？
---

- **A.** 创建包含 50～100 份匿名化患者记录的数据集。使用安全 Knowledge Base 实现 Retrieval Augmented Generation（RAG），并使用 Judge Model 对三个 Foundation Model（FM）的准确率指标进行评估。
- **B.** 使用患者记录 Fine-Tune 单个 Foundation Model（FM），把 FM 部署到 Amazon Bedrock，并使用 Amazon Bedrock AgentCore 将 FM 配置为 Agent，再让 500 名公司员工进行 User Testing。
- **C.** 选择当前可用的最强 AWS Foundation Model（FM），使用 Converse API 创建 Chat Interface。只基于 Stakeholder 的定性反馈，在 50～100 份真实患者记录上测试应用，并使用自定义 Web Interface 收集真实性能指标。
- **D.** 使用 Strands SDK 部署多个 Agent，分别连接多个包含专业医学文档的 Knowledge Base。比较 Agent 响应，并评估 Agent 与公司现有系统的集成效果。

### English

A healthcare company wants to develop a proof-of-concept application that uses Amazon Bedrock to automatically summarize medical documents. The company has 3 weeks to validate the application’s accuracy. The application must comply with the company’s data privacy policies. The application must include metrics to evaluate summarization accuracy and processing time. Which solution will meet these requirements?
---

- **A.** Create a dataset that includes 50-100 anonymized patient records. Implement Retrieval Augmented Generation (RAG) with a secure knowledge base. Use a judge model to evaluate accuracy metrics across three foundation models (FMs).
- **B.** Fine-tune a single foundation model (FM) on patient records. Deploy the FM on Amazon Bedrock. Use Amazon Bedrock AgentCore to configure the FM as an agent. Conduct user testing on 500 company staff members.
- **C.** Select the most powerful available AWS foundation model (FM). Create a chat interface by using Converse APIs. Test the application on 50-100 actual patient records by using only qualitative feedback from stakeholders. Use a custom web interface to gather real-world performance metrics.
- **D.** Use the Strands SDK to deploy multiple agents that connect to multiple knowledge bases that contain specialized medical documents. Compare the responses of the agents. Evaluate the integration of the agents with the company’s existing systems.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 108 - Topic 1

### 中文

一家公司希望为 AI 助手选择一个新的 Foundation Model（FM）。GenAI Developer 需要生成评估报告，帮助 Data Scientist 判断不同 FM 的质量和安全性。Data Scientist 提供了一组 Sample Prompt 用于评估，开发人员希望使用 Amazon Bedrock 自动化报告生成和评估。哪种解决方案能够满足要求？
---

- **A.** 把 Sample Prompt 合并到一个 JSON 文档中。使用该文档创建 Amazon Bedrock Knowledge Base。编写 Prompt，要求 FM 分别回答每个 Sample Prompt，再使用 `RetrieveAndGenerate` API 为每个模型生成报告。
- **B.** 把 Sample Prompt 合并到一个 JSONL 文档中并存储到 Amazon S3。创建使用 Judge Model 的 Amazon Bedrock Evaluation Job，指定一个 S3 Location 作为输入，另一个 S3 Location 作为输出。针对每个 FM 分别运行 Evaluation Job，并选择该 FM 作为 Generator。
- **C.** 把 Sample Prompt 合并到 JSONL 文档并存储在 Amazon S3。创建使用 Judge Model 的 Amazon Bedrock Evaluation Job，指定 S3 Location 为输入、Amazon QuickSight 为输出。针对每个 FM 运行 Evaluation Job，并选择该 FM 作为 Evaluator。
- **D.** 把 Sample Prompt 合并到一个 JSON 文档并据此创建 Amazon Bedrock Knowledge Base。创建采用 Retrieval and Response Generation Evaluation Type 的 Amazon Bedrock Evaluation Job，指定 Amazon S3 Bucket 为输出，并针对每个 FM 分别运行任务。

### English

A company wants to select a new FM for its AI assistant. A GenAI developer needs to generate evaluation reports to help a data scientist assess the quality and safety of various foundation models (FMs). The data scientist provides the GenAI developer with sample prompts for evaluation. The GenAI developer wants to use Amazon Bedrock to automate report generation and evaluation. Which solution will meet this requirement?
---

- **A.** Combine the sample prompts into a single JSON document. Create an Amazon Bedrock knowledge base with the document. Write a prompt that asks the FM to generate a response to each sample prompt. Use the RetrieveAndGenerate API to generate a report for each model.
- **B.** Combine the sample prompts into a single JSONL document. Store the document in an Amazon S3 bucket. Create an Amazon Bedrock evaluation job that uses a judge model. Specify the S3 location as input and a different S3 location as output. Run an evaluation job for each FM and select the FM as the generator.
- **C.** Combine the sample prompts into a single JSONL document. Store the document in an Amazon S3 bucket. Create an Amazon Bedrock evaluation job that uses a judge model. Specify the S3 location as input and Amazon Quick Sight as output. Run an evaluation job for each FM and select the FM as the evaluator.
- **D.** Combine the sample prompts into a single JSON document. Create an Amazon Bedrock knowledge base from the document. Create an Amazon Bedrock evaluation job that uses the retrieval and response generation evaluation type. Specify an Amazon S3 bucket as the output. Run an evaluation job for each FM.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 109 - Topic 1

### 中文

一家金融服务公司正在开发 NLP 应用，使用 AWS Lambda 调用 Amazon Bedrock Foundation Model（FM）处理客户交易咨询。Lambda 必须生成完整的交易分析响应，这些响应通常超过 4 MB，并且需要 15～20 秒才能生成完成。应用必须在 10 秒以内开始响应客户咨询，同时满足金融监管要求：完整记录需要保留 10 年。哪种解决方案能够满足这些要求？
---

- **A.** 实施 Response Streaming，在生成过程中显示部分响应。把 Lambda Execution Timeout 设置为 15 秒，并启用 AWS X-Ray Tracing 监控性能。
- **B.** 使用 Amazon SQS 实现 Asynchronous Invocation。把 Lambda Execution Timeout 设置为 30 秒，并使用 Amazon CloudWatch 监控响应时间。
- **C.** 使用 Amazon API Gateway 轮询应用，并与 Amazon ElastiCache 集成缓存响应。把 Lambda Execution Timeout 设置为 30 秒，并收集详细 Amazon CloudWatch Log 监控应用性能。
- **D.** 实施 Response Streaming，在应用生成响应时持续显示部分结果。把 Lambda Execution Timeout 设置为 30 秒，并把完整响应记录存储到 Amazon S3。

### English

A financial services company is developing a natural language processing (NLP) application that uses an AWS Lambda function to process customer trade inquiries by using Amazon Bedrock foundation models (FMs). The Lambda function must generate comprehensive trade analysis responses that typically exceed 4 MB and require 15-20 seconds to complete. The application must respond to customer inquiries in less than 10 seconds and comply with financial regulatory requirements to retain records for 10 years. Which solution will meet these requirements?
---

- **A.** Implement response streaming to display partial responses as the application generates them. Set a 15-second timeout for Lambda function execution time. Enable AWS X-Ray tracing to monitor performance.
- **B.** Use Amazon SQS queues to implement asynchronous invocations. Set a 30-second timeout for Lambda function execution time. Use Amazon CloudWatch to monitor response times.
- **C.** Use Amazon API Gateway to poll the application. Integrate the application with Amazon ElastiCache to cache responses. Set a 30-second timeout for Lambda function execution time. Collect detailed Amazon CloudWatch logs for application performance.
- **D.** Implement response streaming to display partial responses as the application generates them. Set a 30-second timeout for Lambda function execution time. Store complete response records in Amazon S3.

**Correct Answer / 正确答案:** `D`

**Community vote distribution / 社区投票分布:**


---

## Question 110 - Topic 1

### 中文

一所大学正在构建包含多个子应用的 AI 平台，包括 AI 助手、作业评分器和内部分析应用。大学正在使用不同 Foundation Model（FM）定义和测试多个 Prompt，希望比较每个 Prompt 的多个 Variant，并选择最适合指定场景的版本。大学需要 Prompt Version Control；必须能够测试 Prompt Variation，并收集 Prompt 变更和使用情况的审计追踪；同时还要保持一致性，并允许 Prompt 集成到主应用。以下哪两个方案组合能够以**最低运维开销**满足要求？（选择两项。）
---

- **A.** 使用 Amazon Bedrock Prompt Management 创建带版本控制的 Prompt，并为不同 Use Case 加入参数化变量。
- **B.** 将 Prompt 存储在 Amazon S3，并使用 AWS Step Functions 编排模型交互和服务集成。
- **C.** 使用 Amazon Bedrock Flows 创建结合 FM 和 AWS Service 的 Workflow。
- **D.** 配置 AWS Config 记录 Prompt 变化，并使用 AWS CloudTrail 跟踪 Prompt 使用情况。
- **E.** 配置 Amazon Bedrock Intelligent Prompt Routing。

### English

A university is building an AI-powered application that includes several sub-applications. The sub-applications include AI assistants, assignment graders, and internal analytics applications. The university is defining and testing multiple prompts by using various foundation models (FMs). The university wants to compare variants of each prompt and choose the variant that yield outputs that are best-suited for specified use cases. The university requires a version control solution for the prompts. The university must be able to test prompt variations and collect audit trails for prompt changes and usage. The solution must also maintain consistency while allowing the prompts to integrate into the main application. Which combination of solutions will meet these requirements with the LEAST operational overhead? (Choose two.)
---

- **A.** Use Amazon Bedrock Prompt Management to create versioned prompts. Include parameterized variables for each use case.
- **B.** Store prompts in Amazon S3. Use AWS Step Functions to orchestrate the model interactions and service integrations.
- **C.** Use Amazon Bedrock Flows to create workflows that combine FMs and AWS services.
- **D.** Configure AWS Config to record prompt changes. Use AWS CloudTrail to track prompt usage.
- **E.** Configure Amazon Bedrock intelligent prompt routing.

**Correct Answer / 正确答案:** `AC`

**Community vote distribution / 社区投票分布:**


---

## Question 111 - Topic 1

### 中文

一家公司正在构建一个由生成式 AI（GenAI）驱动的多云机密信息解析应用，使用 Amazon Bedrock 和 Agent Squad。该应用需要从多个来源解析 Secret，包括 Key Store 和 Hardware Security Module（HSM）。应用通过 AWS Lambda 函数从这些来源获取 Secret，并使用 AWS AppConfig 实现动态 Feature Gating。应用支持 Secret Chaining 和 Secret Drift 检测，也需要处理短生命周期及即将过期的 Secret。此外，应用使用 Prompt Flow 提供模板化指令，并通过 AWS Step Functions 编排多个 Agent 完成 Secret 解析、验证和 Drift Detection。
在测试过程中，公司发现多个问题：过期 Secret 无法及时刷新，导致 Agent 不能使用最新 Secret；系统虽然会针对 Secret Drift 发出告警，但 Agent 仍继续使用陈旧数据；应用中的 Prompt Flow 还会重复使用过时模板，从而引发级联故障。公司必须解决这些性能问题。哪种解决方案能够满足要求？
---

- **A.** 使用 Step Functions Map State 并行运行 Agent Workflow。通过 Lambda 函数输出传递更新后的 Secret Metadata。使用 AWS AppConfig 对所有 Prompt Flow 进行版本管理，以便对有问题的模板进行 Feature Gating 和 Rollback。
- **B.** 仅使用 Amazon Bedrock AgentCore。配置 Amazon Bedrock Guardrails 限制 Prompt Variation。使用内联 JSON Schema 定义单个 Agent 的 Workflow，并以此串联 Tool Call。
- **C.** 使用集中式 Amazon EventBridge Pipeline 调用每个 Event Agent。将中间 Prompt 存储在 Amazon DynamoDB 中，并通过基于 TTL 的 Backoff 和 Retry 来确定 Agent 的执行顺序。
- **D.** 使用 Amazon EventBridge Pipes，根据 Amazon CloudWatch Log Pattern 调用 Resolver。将响应 Metadata 存储在 Amazon DynamoDB 中，配置 TTL 和 Versioned Write。使用 Amazon Q Developer 动态生成 Fallback Prompt，再通过 Lambda Coordinator 对这些 Prompt 进行路由。

### English

A company is building a multicloud generative AI (GenAI)-powered secret resolution application that uses Amazon Bedrock and Agent Squad. The application resolves secrets from multiple sources, including key stores and hardware security modules (HSMs). The application uses AWS Lambda functions to retrieve secrets from the sources. The application uses AWS AppConfig to implement dynamic feature gating. The application supports secret chaining and detects secret drift. The application handles short-lived and expiring secrets. The application also supports prompt flows for templated instructions. The application uses AWS Step Functions to orchestrate agents to resolve the secrets and to manage secret validation and drift detection. The company finds multiple issues during application testing. The application does not refresh expired secrets in time for agents to use. The application sends alerts for secret drift, but agents still use stale data. Prompt flows within the application reuse outdated templates, which cause cascading failures. The company must resolve the performance issues. Which solution will meet this requirement?
---

- **A.** Use Step Functions Map states to run agent workflows in parallel. Pass updated secret metadata through Lambda function outputs. Use AWS AppConfig to version all prompt flows to gate and roll back faulty templates.
- **B.** Use Amazon Bedrock AgentCore only. Configure Amazon Bedrock guardrails to restrict prompt variation. Use an inline JSON schema for a single agent’s workflow definition to chain tool calls.
- **C.** Use a centralized Amazon EventBridge pipeline to invoke each event agent. Store intermediate prompts in Amazon DynamoDB. Resolve agent ordering by using TTL-based backoff and retries.
- **D.** Use Amazon EventBridge Pipes to invoke resolvers based on Amazon CloudWatch log patterns. Store response metadata in Amazon DynamoDB. Configure a TTL and versioned writes. Use Amazon Q Developer to dynamically generate fallback prompts that are routed through a Lambda coordinator.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 112 - Topic 1

### 中文

一家金融服务公司每天通过一个基于 Amazon Bedrock AgentCore 的 Multi-Agent GenAI 应用处理超过 10,000 条客户咨询。应用中的 Agent 会调用多个自定义 Tool。在使用高峰期，这些自定义 Tool 的失败率最高可达 40%，而且不同团队使用这些 Tool 时的表现并不一致。
GenAI Developer 必须实现一套 Observability 方案，对 Agent 之间的交互以及 Tool 的运行行为提供端到端可见性。方案必须使用 Amazon Bedrock 的内置能力，不能要求自行添加 Custom Instrumentation，同时还要尽量降低性能开销。哪种解决方案能够满足这些要求？
---

- **A.** 启用 AgentCore Observability 和 Trace Collection。使用 AWS X-Ray 捕获自定义 Tool 的 Distributed Trace。构建 Amazon CloudWatch Dashboard，在高峰期可视化 Error、Throttling 和 Latency 指标。
- **B.** 使用 Amazon CloudWatch Container Insights 监控 Agent。配置 AWS Lambda 函数轮询 Amazon Bedrock API 获取 Tool Usage Metric，并将结果写入 CloudWatch 用于生成告警。
- **C.** 构建自定义 ETL Pipeline，使用 AWS Lambda 处理来自 Amazon Bedrock 的 Amazon CloudWatch Log。将处理后的数据存入 Amazon DynamoDB，并使用 Amazon QuickSight 展示跨团队的性能模式。
- **D.** 启用 AgentCore Observability，并将 Trace Data 发送到 Amazon CloudWatch Logs。使用自定义 AWS Lambda 函数从日志中提取 Tool Performance Metric，再使用 Amazon Managed Grafana 展示趋势。

### English

A financial services company processes more than 10,000 customer inquiries every day through a multi-agent GenAI application that uses Amazon Bedrock AgentCore. The application agents invoke several custom tools. During peak usage periods, users report that the custom tools experience up to 40% failure rates. The tools perform inconsistently for different teams at the company. A GenAI developer must implement an observability solution that provides end-to-end visibility into agent interactions and tool behavior. The solution must use built-in Amazon Bedrock capabilities and must not require custom instrumentation. The GenAI developer needs a solution that requires minimal performance overhead. Which solution will meet these requirements?
---

- **A.** Enable AgentCore Observability and trace collection. Use AWS X-Ray to capture distributed traces for the custom tools. Build Amazon CloudWatch dashboards to visualize metrics for errors, throttling, and latency during peak usage periods.
- **B.** Use Amazon CloudWatch Container Insights to monitor the agents. Configure an AWS Lambda function to poll the Amazon Bedrock API for tool usage metrics. Configure the function to store results in CloudWatch to generate alerts.
- **C.** Build a custom ETL pipeline that uses AWS Lambda functions to process Amazon CloudWatch logs from Amazon Bedrock. Store the processed data in Amazon DynamoDB. Use Amazon Quick Sight to visualize cross-team performance patterns.
- **D.** Enable AgentCore Observability and send trace data to Amazon CloudWatch Logs. Use a custom AWS Lambda function to extract tool performance metrics from the logs. Use Amazon Managed Grafana to visualize trends.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 113 - Topic 1

### 中文

一家电商公司正在 Amazon Bedrock 中使用 Anthropic Claude Sonnet 模型生成产品推荐。AWS Lambda 函数会从 Amazon DynamoDB 获取客户购买数据、从 Amazon S3 获取产品评论、从 Amazon RDS 获取客户资料，然后通过 API Call 将这些数据直接发送给 Amazon Bedrock 模型。
最近，购买历史较多的客户开始收到不完整的推荐。Lambda 的 Amazon CloudWatch Logs 显示存在 Execution Timeout；Amazon Bedrock API Call 的 CloudWatch Logs 也显示间歇性错误。公司进一步检查后发现，一部分请求因 `context length exceeded` 而失败，另一些请求虽然成功完成，却似乎忽略了部分输入数据。
公司希望推荐系统在生成推荐时能够考虑所有客户数据，并希望使用 Amazon Bedrock Knowledge Bases 改善数据组织与检索。以下哪两个方案组合能够满足要求？（选择两项。）
---

- **A.** 实施 Chunking Strategy，将客户数据拆分成较小的 Segment。配置模型分别处理每个 Segment，最后再调用一次模型，将各部分响应综合成完整推荐。
- **B.** 调整 Prompt Structure，把最关键的信息放在 Context Window 的开头和结尾。实现 Token Counting Logic，当交互接近模型最大 Context Length 时截断较不重要的数据。
- **C.** 将 Claude Sonnet 替换为拥有更大 Context Window 的模型，并增加 Lambda 函数 Timeout，以适应更大输入带来的更长处理时间。
- **D.** 配置推荐系统使用 Converse API，并修改 `additionalModelRequestFields` 参数，把 Maximum Token Limit 提高到超过模型默认 Context Window Size。
- **E.** 使用 Knowledge Base 实现 RAG，对客户数据建立 Vector Embedding Index。根据当前客户上下文，在每次推荐请求中只检索语义上最相关的信息。

### English

An ecommerce company is using an Anthropic Claude Sonnet model in Amazon Bedrock to generate product recommendations. An AWS Lambda function retrieves customer purchase data from Amazon DynamoDB, product reviews from Amazon S3, and customer profile information from Amazon RDS. Then the function sends the data directly to the Amazon Bedrock model through API calls. Recently, customers who have extensive purchase histories have begun to receive incomplete recommendations. Amazon CloudWatch logs for the Lambda function show execution timeouts. CloudWatch logs for Amazon Bedrock API calls show intermittent errors. The company reviews the logs and finds that some requests are failing with context length exceeded errors. Other requests finish but appear to ignore portions of the input data. The company wants the recommendation system to consider all customer data when the system generates recommendations. The company wants to use Amazon Bedrock Knowledge Bases to improve data organization and retrieval. Which combination of solutions will meet these requirements? (Choose two.)
---

- **A.** Implement a chunking strategy that divides the customer data into smaller segments. Configure the model to process each segment separately. Invoke the model a final time to synthesize the individual responses into comprehensive recommendations.
- **B.** Modify the prompt structure to place the most critical information at the beginning and end of the context window. Implement token-counting logic to truncate less important data when the interaction approaches the model’s maximum context length.
- **C.** Replace Claude Sonnet with a model that has a larger context window capacity. Increase the Lambda function timeout to accommodate longer processing times for larger inputs.
- **D.** Configure the recommendation system to use the Converse API. Modify the additionalModelRequestFields parameter to increase the maximum token limit beyond the model’s default context window size.
- **E.** Implement RAG by using a knowledge base to index the customer data with vector embeddings. Retrieve only the most semantically relevant information for each recommendation request based on the current customer context.

**Correct Answer / 正确答案:** `AE`

**Community vote distribution / 社区投票分布:**


---

## Question 114 - Topic 1

### 中文

一家公司使用 Amazon Bedrock 开发了一个 Multimodal Content Analysis 应用。应用会把不同类型的内容（文本、图像和代码）路由到专门的 Foundation Model（FM）。应用需要处理多种 Routing Decision：仅依据文件扩展名进行的简单路由必须保持最低延迟；依据内容语义进行的复杂路由，则需要在选择 FM 之前先分析内容。应用还必须保留详细的执行历史，并在主要 FM 失败时支持 Fallback。哪种解决方案能够满足这些要求？
---

- **A.** 使用 AWS Lambda 函数实现全部 Routing Logic，并调用 Amazon Bedrock FM。通过 Conditional Statement 根据内容类型和语义确定合适的 FM。
- **B.** 创建 Hybrid Solution。基于文件扩展名的简单路由直接由 Application Code 处理；复杂的 Content-Based Routing 则使用 AWS Step Functions State Machine，并通过 JSONata 分析内容，再使用 `InvokeModel` API 调用专门的 FM。
- **C.** 为每种内容类型分别部署 AWS Step Functions Workflow，并在 AWS Lambda 中实现 Routing Logic。当需要 Fallback 到备用 FM 时，使用 Amazon EventBridge 协调不同 Workflow。
- **D.** 为每种内容类型分别创建 Amazon SQS Queue。配置 AWS Lambda Consumer 分析内容，并根据 Message Attribute 使用 AWS SDK 调用 Amazon Bedrock 中相应的 FM。

### English

A company developed a multimodal content analysis application by using Amazon Bedrock. The application routes different content types (text, images, and code) to specialized foundation models (FMs). The application needs to handle multiple types of routing decisions. Simple routing based on file extension must have minimal latency. Complex routing based on content semantics requires analysis before FM selection. The application must provide detailed history and support fallback options when primary FMs fail. Which solution will meet these requirements?
---

- **A.** Configure AWS Lambda functions that call Amazon Bedrock FMs for all routing logic. Use conditional statements to determine the appropriate FM based on content type and semantics.
- **B.** Create a hybrid solution. Handle simple routing based on file extensions in application code. Handle complex content-based routing by using an AWS Step Functions state machine with JSONata for content analysis and the InvokeModel API for specialized FMs.
- **C.** Deploy separate AWS Step Functions workflows for each content type with routing logic in AWS Lambda functions. Use Amazon EventBridge to coordinate between workflows when fallback to alternate FMs is required.
- **D.** Use Amazon SQS with different SQS queues for each content type. Configure AWS Lambda consumers that analyze content and invoke appropriate FMs based on message attributes by using Amazon Bedrock with an AWS SDK.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 115 - Topic 1

### 中文

一家公司正在开发使用 Amazon Bedrock Foundation Model（FM）的客户支持聊天助手。公司希望升级到更新版本的 FM，但需要先建立一套 Validation System 来检测响应中的 Semantic Drift，确保终端用户看到的性能和功能保持一致。
公司需要针对 500 个 Test Case 比较当前 FM 与新版 FM 的响应。方案必须能够检测响应含义的变化、生成量化的 Similarity Score、完成自动化验证，并记录详细结果以供后续历史对比。哪种解决方案的**运维复杂度最低**？
---

- **A.** 配置 Amazon CloudWatch Synthetics Canary，使用自定义 JavaScript 将相同 Prompt 分别发送给新旧 FM。把响应保存到 Amazon S3，再通过人工审核识别两个 FM 版本之间的语义差异。
- **B.** 配置 AWS Step Functions Workflow，将测试 Prompt 同时发送给新旧 FM。使用 Amazon Bedrock Embedding Model 计算 Cosine Similarity Score，并使用 Composite Key Schema 将结果存储到 Amazon DynamoDB。
- **C.** 使用 Amazon Bedrock Model Evaluation Job，基于这 500 个 Test Case 比较新版 FM 与当前 FM。配置 Evaluation 计算 Semantic Similarity Metric，并将结果存储到 Amazon S3 供历史比较。
- **D.** 构建自定义方案，使用 Amazon SageMaker AI 基于历史响应训练 Classifier Model，在将新版 FM 响应与既有模式比较时检测异常。

### English

A company is developing a customer support chat assistant that uses an Amazon Bedrock foundation model (FM). The company wants to update to a newer FM version but needs to implement a validation system to detect semantic drift in responses. The company wants to ensure that performance and functionality for end users remains consistent. The company needs a solution to compare responses between current and new FM versions for 500 test cases. The solution must detect changes in response meaning, generate quantitative similarity scores, complete validations, and log detailed results for historical comparison. Which solution will meet these requirements with the LEAST operational complexity?
---

- **A.** Configure Amazon CloudWatch Synthetics canaries that use custom JavaScript code to send identical prompts to both new and existing FM versions. Store responses in Amazon S3. Perform manual reviews to identify semantic differences between FM versions.
- **B.** Configure an AWS Step Functions workflow that sends test prompts to both new and existing FM versions. Use Amazon Bedrock embedding models to calculate cosine similarity scores. Store the results in Amazon DynamoDB with a composite key schema.
- **C.** Use Amazon Bedrock model evaluation jobs to compare the new FM version against the current version using the 500 test cases. Configure the evaluation to calculate semantic similarity metrics. Store results in Amazon S3 for historical comparison.
- **D.** Build a custom solution by using Amazon SageMaker AI to train a classifier model on historical responses to detect anomalies when the solution compares responses from the new FM version to previous patterns.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**


---

## Question 116 - Topic 1

### 中文

一家媒体公司正在使用 Amazon Bedrock 构建 AI 内容审核系统。系统首先使用一个小型、低延迟模型对文本进行分类；如果 Confidence Score 低于 `0.65`，再将请求升级给一个规模更大、成本更高的模型处理。
系统必须让高置信度结果接近实时返回；低置信度请求必须异步处理；还要能够应对突发流量。公司希望仅在确有必要时调用大模型以优化成本，并希望通过 Decoupled Component 提高系统韧性。哪种解决方案能够满足这些要求？
---

- **A.** 使用 Amazon API Gateway 同步调用小模型。如果小模型的 Confidence Score 低于 `0.65`，则同步调用大模型。使用 Provisioned Concurrency 应对流量高峰。
- **B.** 使用带 Parallel Branch 的 AWS Step Functions Workflow，让每个请求都同时运行小模型和大模型；当 Confidence Score 不一致时选择大模型结果。
- **C.** 将请求发送到 Amazon SQS Queue，并使用 AWS Fargate 处理消息。首先调用小模型；如果 Confidence Score 低于 `0.65`，则把请求放入第二个 SQS Queue，再异步使用大模型处理。
- **D.** 将两个模型都部署在 Amazon EC2 Instance 上并启用 Auto Scaling。使用自定义应用 Heuristic，根据 Phrase Length 和 Keyword Rule 将请求路由到合适的实例。

### English

A media company is building an AI-powered content moderation system by using Amazon Bedrock. The system first classifies text by using a small, low-latency model. Then the system escalates requests that have a confidence score below 0.65 to a larger, more expensive model. The system must respond in near real time for high-confidence results. The system must process low-confidence requests asynchronously. The system must scale to meet sudden spikes in demand. The company wants to optimize costs for the system by invoking the larger model only when required. The company wants to use decoupled components to achieve high resiliency for the system. Which solution will meet these requirements?
---

- **A.** Use Amazon API Gateway to invoke the small model synchronously. If the small model’s confidence score is below 0.65, synchronously call the larger model. Use provisioned concurrency to handle traffic spikes.
- **B.** Use an AWS Step Functions workflow that has parallel branches to run both the small model and the large model for every request. Choose the large model result when confidence score values differ.
- **C.** Send requests to an Amazon SQS queue. Use AWS Fargate to process messages. Invoke the small model first. If the confidence score is below 0.65, place the request in a second SQS queue to process asynchronously by using the large model.
- **D.** Deploy both models on Amazon EC2 instances and enable auto scaling. Use a custom application heuristic to route requests to the appropriate instance based on phrase length and keyword rules.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**


---

## Question 117 - Topic 1

### 中文

一家社交消息公司正在使用 Amazon Bedrock 构建 AI Chat Assistant。公司必须确保**每一次推理**都符合已经批准的安全策略。公司希望在调用模型之前阻止有害 Prompt；在模型进行 Streaming Output 时实时过滤输出；并将被标记的案例路由给人工审核。哪种解决方案能够满足这些要求？
---

- **A.** 配置 AWS Step Functions Workflow。先通过 AWS Lambda 调用 `ApplyGuardrail` API 对输入执行 Pre-Check；随后执行附加了 Guardrail 的 `InvokeModelWithResponseStream` API Step；再通过第二个 Lambda 调用 `ApplyGuardrail` API 对输出执行 Post-Check。把被标记的项目路由到 Amazon SQS Queue，并使用 `bedrock:GuardrailIdentifier` IAM Condition 强制要求使用 Guardrail。
- **B.** 配置 AWS Step Functions Workflow。推理前先调用 `ApplyGuardrail` API，然后使用 Guardrail 调用不支持 Streaming 的 `InvokeModel` API。把结果存入 Amazon S3，并在 Client UI 中隐藏有问题的 Token。
- **C.** 配置 AWS Step Functions Workflow。使用附加 Guardrail 的 `InvokeModelWithResponseStream` API 执行 In-Stream Filtering，再通过 AWS Lambda 调用 `ApplyGuardrail` API 对被标记案例进行 Post-Check；不执行 Pre-Inference Check。
- **D.** 配置 AWS Step Functions Workflow，同时包含使用 `ApplyGuardrail` API 的 Pre-Check、Post-Check 以及 `InvokeModelWithResponseStream` API。将 Guardrail 附加到检查步骤，并通过 Code Review 等流程控制而不是 IAM Enforcement 来确保始终应用 Guardrail。

### English

A social messaging company is building an AI chat assistant by using Amazon Bedrock. The company must ensure that every inference complies with an approved safety policy. The company wants to block harmful prompts before model invocations, filter streamed model outputs in real time, and route flagged cases for human review. Which solution will meet these requirements?
---

- **A.** Configure an AWS Step Functions workflow. Configure a step to use an AWS Lambda function to pre-check inputs by using the ApplyGuardrail API. Use an InvokeModelWithResponseStream API step that has the guardrail attached. Configure a second Lambda function step to post-check outputs by using the ApplyGuardrail API. Route flagged items to an Amazon SQS queue. Enforce guardrail use by using a bedrock:GuardrailIdentifier IAM condition.
- **B.** Configure an AWS Step Functions workflow. Configure a step to call the ApplyGuardrail API before inference. Then call the InvokeModel API without streaming and use the guardrail. Store the results in Amazon S3. Use the client UI to hide problematic tokens.
- **C.** Configure an AWS Step Functions workflow. Configure a step to use the InvokeModelWithResponseStream API that has the guardrail attached for in-stream filtering. Run an AWS Lambda post-check step by using the ApplyGuardrail API to check flagged cases. Do not perform pre-inference.
    - **D.** Configure an AWS Step Functions workflow that includes steps to perform pre-checks and post-checks by using the ApplyGuardrail API and the InvokeModelWithResponseStream API. Attach the guardrail to the check steps. Use process controls such as code reviews instead of IAM enforcement to ensure that guardrails are always applied.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---
## Question 118 - Topic 1

### 中文

一家金融服务公司使用 Amazon Bedrock 分析存储在 Amazon S3 存储桶中的客户数据。数据包含个人身份信息（PII）。公司必须对基础模型（FM）响应中的 PII 进行掩码处理。哪种解决方案能够以**最低的运维工作量**满足要求？
---

- **A.** 在 Amazon Bedrock 中创建 Guardrail 以过滤 PII 内容。定义 PII 类型，并将 Guardrail 操作设置为 `MASK`。配置 Amazon Bedrock，使其对每个 FM 响应应用该过滤器。
- **B.** 在调用 Amazon Bedrock 之前，使用 Amazon Comprehend 检测 S3 数据中的 PII 实体。配置 AWS Lambda 函数调用 Amazon Comprehend 的 `DetectPiiEntities` API，对检测到的 PII 进行掩码处理，并将处理后的数据存回原始 S3 存储桶。
- **C.** 使用 Amazon Macie 扫描 S3 存储桶中的 PII 数据。配置 AWS Lambda 函数将 PII 存入第二个 S3 存储桶，并使用 Amazon EventBridge 规则调用该 Lambda 函数。
- **D.** 配置 AWS Lambda 函数搜索 PII 数据。在 Lambda 函数代码中实现步骤，将 PII 存入第二个 S3 存储桶，并将非 PII 数据存入第三个 S3 存储桶。

### English

A financial services company uses Amazon Bedrock to analyze customer data that is stored in an Amazon S3 bucket. The data includes personally identifiable information (PII). The company must mask PII from foundation model (FM) responses. Which solution will meet this requirement with the LEAST operational effort?
---

- **A.** Create a guardrail in Amazon Bedrock to filter PII content. Define the PII type and set the guardrail action to `MASK`. Configure Amazon Bedrock to apply the filter to each FM response.
- **B.** Use Amazon Comprehend to detect PII entities in the S3 data before invoking Amazon Bedrock. Configure an AWS Lambda function to call the Amazon Comprehend `DetectPiiEntities` API to mask detected PII. Store the processed data back to the original S3 bucket.
- **C.** Use Amazon Macie to scan the S3 bucket for PII data. Configure an AWS Lambda function to store PII in a second S3 bucket. Use an Amazon EventBridge rule to invoke the Lambda function.
- **D.** Configure an AWS Lambda function to search for PII data. Implement a step in the Lambda function code to store the PII in a second S3 bucket and non-PII data into a third S3 bucket.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 119 - Topic 1

### 中文

一家医疗机构正在 Amazon Bedrock 上实施临床知识库应用，为医生提供医疗信息。在测试期间，质量团队发现生成式 AI（GenAI）模型偶尔会编造并非来自已批准临床指南的医疗治疗建议。

质量团队需要在应用发布前实现幻觉检测方案。该方案必须将模型响应与经过验证的医疗信息进行分析比较，并能够识别用户以不同方式提出相似问题时产生的语义不一致。哪种解决方案能够满足这些要求？
---

- **A.** 部署实时监控系统，使用 Amazon CloudWatch Logs Insights 分析响应模式，并根据预定义关键字标记事实错误。实现 AWS Lambda 函数，将响应与 Amazon DynamoDB 表中的已知答案进行比较。
- **B.** 创建包含临床指南中已验证问答对的参考数据集。使用 Amazon Bedrock Guardrails 执行输出差异比较和响应一致性分析，并使用 Amazon Bedrock 评估检测事实错误。
- **C.** 配置 Amazon Bedrock Guardrails，通过识别 GenAI 响应中的特定模式，使用自定义规则检测并阻止潜在的幻觉内容。使用 Amazon SageMaker Feature Store 维护已验证临床指南的存储库。
- **D.** 创建 Amazon Bedrock 自动模型评估任务，使用自定义提示数据集。设置异常检测告警以识别事实错误，并在检测到错误时使用 Amazon SNS 发送通知。

### English

A healthcare company is implementing a clinical knowledge base application on Amazon Bedrock that provides medical information to doctors. During testing, a quality team discovers that the generative AI (GenAI) model occasionally fabricates medical treatment recommendations that do not originate from the approved clinical guidelines. The quality team needs to implement a solution to detect the hallucinations before releasing the application. The solution must analyze model responses against verified medical information. The solution must identify semantic inconsistencies when users ask similar questions in different ways. Which solution will meet these requirements?
---

- **A.** Deploy a real-time monitoring system that uses Amazon CloudWatch Logs Insights to analyze response patterns and to flag factual inaccuracies based on predefined keywords. Implement AWS Lambda functions to compare responses with known answers from an Amazon DynamoDB table.
- **B.** Create a reference dataset with validated question-answer pairs from clinical guidelines. Implement output diffing by using Amazon Bedrock Guardrails for response consistency analysis. Use Amazon Bedrock evaluation to detect factual inaccuracies.
- **C.** Configure Amazon Bedrock Guardrails with custom rules to detect and block potentially hallucinated content by identifying specific patterns in the GenAI responses. Use Amazon SageMaker Feature Store to maintain a repository of verified clinical guidelines.
- **D.** Create an Amazon Bedrock automatic model evaluation job with a custom prompt dataset. Set up anomaly detection alarms to identify factual inaccuracies. Implement Amazon SNS notifications when inaccuracies are detected.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 120 - Topic 1

### 中文

一家公司部署了一个使用 Amazon Bedrock 基础模型（FM）和自定义提示的 GenAI 应用。公司必须实施自动化质量保证系统，根据预期响应验证 FM 输出，并检测随时间推移产生的回归问题。

该方案必须使用语义相似度评分比较超过 500 个黄金样例，支持定期安排测试规则，并且能够在 6 个月内扩展到超过 2,000 个测试用例而无需进行架构变更。公司希望生成质量指标可视化结果，并在检测到质量下降时收到告警。哪种解决方案能够满足这些要求？
---

- **A.** 使用 Amazon SageMaker Model Monitor 跟踪 FM 中的数据漂移。为漂移指标配置 Amazon CloudWatch 告警，并使用 SageMaker AI 处理任务运行 FM 输出的定期评估。
- **B.** 创建 AWS Lambda 函数调用 FM 处理测试用例，并将结果与预期输出比较。配置 Amazon EventBridge 计划规则运行测试，将自定义指标发布到 Amazon CloudWatch，以生成指标可视化和告警。
- **C.** 使用 AWS Step Functions 工作流运行测试用例并验证响应。将测试结果存储在 Amazon DynamoDB 中，并使用 Amazon CloudWatch 控制面板可视化结果。
- **D.** 配置 Amazon CloudWatch Synthetics Canary 调用 FM API。将响应与基线脚本比较，并根据 Canary 成功率创建 CloudWatch 告警。

### English

A company deploys a GenAI application that uses an Amazon Bedrock foundation model (FM) and custom prompts. The company must implement an automated quality assurance system to verify FM outputs against expected responses and detect regressions over time. The solution must compare outputs against more than 500 golden examples by using semantic similarity scoring. The solution must support regularly scheduled test rules. The solution must be able to scale to more than 2,000 test cases within 6 months without the need to make architecture changes. The company wants to generate visualizations for quality metrics. The company must receive alerts if the solution detects quality degradation. Which solution will meet these requirements?
---

- **A.** Use Amazon SageMaker Model Monitor to track data drift in the FM. Configure Amazon CloudWatch alarms for drift metrics. Use SageMaker AI processing jobs to run scheduled evaluations of FM outputs.
- **B.** Create AWS Lambda functions that invoke the FM on test cases. Compare results against expected outputs. Configure Amazon EventBridge scheduled rules to run the test executions. Publish custom metrics to Amazon CloudWatch to generate metric visualizations and alarms.
- **C.** Use AWS Step Functions workflows to run test cases and validate responses. Store the test results in Amazon DynamoDB. Use Amazon CloudWatch dashboards to visualize the results.
- **D.** Configure Amazon CloudWatch Synthetics canaries to invoke the FM APIs. Compare responses with baseline scripts. Create CloudWatch alarms based on canary success rates.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 121 - Topic 1

### 中文

一家金融服务公司需要为多语言 AI 助手构建零接触的推广工作流，自动将提示更新从开发环境移动到生产环境。只有满足若干标准时，工作流才能推广提示更新。

更新必须提高 AI 助手支持的全部五种语言的准确率；与旧版本提示相比，更新在相似查询上的方差必须更低；更新必须通过与投资风险和费用披露相关的所有监管合规检查。工作流必须在公司现有 CI/CD 流水线中运行，支持提示版本比较，生成可审计的评估报告，并且长期保持成本效益。哪种解决方案能够满足这些要求？
---

- **A.** 使用 Amazon SageMaker Canvas 以可视化方式比较提示输出。创建控制面板展示指标，并将结果导出到文档协作系统以供人工审核监管合规性。安排提示工程师和合规官员每周开会讨论改进。
- **B.** 使用 Amazon S3 对象版本实现提示版本控制。配置 AWS Lambda 函数手动对响应评分，将评估指标存入 Amazon DynamoDB，并构建自定义 Amazon QuickSight 控制面板，以可视化不同语言和产品类型的准确率。
- **C.** 使用 Amazon Bedrock Prompt Management 实现提示版本控制和提示 A/B 测试。使用 Amazon Bedrock Evaluations 和基于 LLM 的评判器自动评估准确率与一致性指标。创建 Amazon Bedrock Flow 以简化跨语言测试，并将 Flow 集成到现有 CI/CD 流水线。
- **D.** 构建使用 AWS Amplify 作为前端、AWS Lambda 作为后端的自定义评估框架。实现团队成员对响应质量进行投票的众包功能，并使用 Amazon Comprehend 分析响应情感和跨语言一致性。

### English

A financial services company needs to build a zero-touch promotion workflow for a multilingual AI assistant that automatically moves prompt updates from a development environment to a production environment. The workflow must promote prompt updates only if the updates meet several criteria. The updates must improve accuracy in all five languages that the AI assistant supports. The updates must show lower variance across similar queries than older prompt versions. The updates must pass all regulatory compliance checks for content related to investment risks and fee disclosures. The workflow must run inside the company’s existing CI/CD pipeline, support prompt version comparisons, generate auditable evaluation reports, and remain cost-effective over time. Which solution will meet these requirements?
---

- **A.** Use Amazon SageMaker Canvas to visually compare prompt outputs. Create a dashboard to display metrics. Export findings to a document collaboration system to review for regulatory compliance. Schedule weekly meetings between prompt engineers and compliance officers to discuss improvements.
- **B.** Implement prompt version control by using Amazon S3 object versions. Configure AWS Lambda functions to manually score responses. Store evaluation metrics in an Amazon DynamoDB table. Build a custom Amazon QuickSight dashboard to visualize accuracy across languages and product types.
- **C.** Use Amazon Bedrock Prompt Management to implement prompt version control and to perform A/B testing for prompts. Use Amazon Bedrock Evaluations and LLM-based judges to perform automated evaluations for accuracy and consistency metrics. Create a flow in Amazon Bedrock to streamline testing across languages. Integrate the flow with the existing CI/CD pipeline.
- **D.** Build a custom evaluation framework that uses AWS Amplify as the frontend and AWS Lambda functions as the backend. Implement crowdsourcing features in which team members vote on response quality. Use Amazon Comprehend to analyze sentiments in responses and to evaluate for consistency across languages.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**


---

## Question 122 - Topic 1

### 中文

一家金融公司正在构建 AI 驱动的推荐系统。系统使用实时市场数据和经过验证的内部文档。系统必须具备评估能力，收集明确的用户评分并实时检测有问题的响应；跟踪模型随时间推移的表现，并支持不同提示或模型变体的 A/B 测试。

系统必须编排多步骤工作流，这些工作流需要检索可信知识、验证输入并生成推荐。系统必须将所有用户反馈、标记事件和审计日志安全存储在符合合规要求的不可变存储库中。该存储库必须是一次写入、防篡改且具有篡改证据的存储，适合受监管的审计保留。哪种架构能够满足这些要求？
---

- **A.** 在 Amazon SageMaker JumpStart 上部署微调 FM。使用 Amazon Bedrock AgentCore 编排输入验证和检索工作流。将用户反馈和标记查询存储在 Amazon DynamoDB 中。对 AWS CloudTrail 日志运行计划的 Amazon Athena 查询以检测异常。将生成的输出存储在加密的 Amazon S3 存储桶中，并启用 S3 Object Lock。
- **B.** 配置 Amazon API Gateway 调用从 Amazon Kendra 检索信息的 Amazon Bedrock FM。将所有响应存储在加密的 Amazon S3 存储桶中，启用 S3 Object Lock，并使用 Amazon Macie 扫描已存储日志中的问题内容。
- **C.** 使用 Amazon Bedrock AgentCore 编排从精选 Amazon Kendra 索引的检索。配置 AWS Lambda 函数清理输入，配置函数检查生成后的事实一致性，并配置函数拆分 A/B 测试流量。将用户反馈存储在 Amazon DynamoDB 中；通过 AWS CloudTrail 写入审计日志；将日志存储在 Amazon S3 并启用 S3 Object Lock；使用 Amazon SageMaker Model Monitor 持续跟踪数据漂移和输出异常。
- **D.** 构建基于 Amazon Lex 的聊天式 AI 助手，通过别名将一定比例的查询路由到两个 Amazon SageMaker AI 端点以生成推荐。将交互记录到 Amazon CloudWatch Logs，按季度人工审核幻觉和提示注入，并将反馈加密存储在 Amazon RDS 中。

### English

A financial company is building an AI-powered recommendation system. The system uses real-time market data and verified internal documents. The system must include evaluation capabilities that collect explicit user ratings and detect problematic responses in real time. The system must include evaluation capabilities that track model performance over time and support A/B testing of different prompt or model variants. The system must orchestrate multi-step workflows. The workflows must retrieve trusted knowledge, validate inputs, and generate recommendations. The system must securely store all user feedback, flagged events, and audit logs in a compliance-ready immutable repository. The repository must be write-once, tamper-evident storage that is suitable for regulated audit retention. Which architecture will meet these requirements?
---

- **A.** Deploy a fine-tuned FM on Amazon SageMaker JumpStart. Use Amazon Bedrock AgentCore to orchestrate input validation and retrieval workflows. Store user feedback and flagged queries in Amazon DynamoDB. Run scheduled Amazon Athena queries on AWS CloudTrail logs for anomaly detection. Store generated outputs in encrypted Amazon S3 buckets. Enable S3 Object Lock.
- **B.** Configure Amazon API Gateway to invoke an Amazon Bedrock FM that retrieves from Amazon Kendra. Store all responses in encrypted Amazon S3 buckets. Enable S3 Object Lock. Use Amazon Macie to scan stored logs for problematic content.
- **C.** Use Amazon Bedrock AgentCore to orchestrate retrieval from curated Amazon Kendra indexes. Configure an AWS Lambda function to sanitize input. Configure a function to check post-generation fact consistency. Configure a function to split traffic for A/B testing. Store user feedback in Amazon DynamoDB. Write audit logs through AWS CloudTrail. Store the logs in Amazon S3. Enable S3 Object Lock. Use Amazon SageMaker Model Monitor to track data drift and output anomalies continuously.
- **D.** Build an Amazon Lex chat-based AI assistant with aliases that route a percentage of queries to two Amazon SageMaker AI endpoints for recommendations. Log interactions to Amazon CloudWatch Logs. Conduct quarterly manual audits for hallucination and prompt-injection detection. Store feedback in Amazon RDS with encryption.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**


---

## Question 123 - Topic 1

### 中文

一家零售公司拥有一个使用 Amazon Bedrock 基础模型（FM）的产品推荐系统。系统处理并搜索数百万个每小时更新的产品嵌入向量，公司还会定期添加新产品。

在购物高峰活动期间，当前解决方案在整个产品目录中执行相似性搜索时出现高延迟和偶发故障。公司必须解决性能问题。哪种解决方案能够满足要求？
---

- **A.** 使用多节点架构实施 Amazon OpenSearch Service。配置向量专用索引设置和内存断路器，并建立专用向量操作队列，以处理高峰期间的突发容量需求。
- **B.** 部署使用稀疏向量压缩的 Amazon DynamoDB 表。实施并行查询执行器处理搜索操作，并配置 DynamoDB Streams 和 AWS Lambda 函数实时维护向量索引。
- **C.** 设置使用自定义向量分区的 Amazon Bedrock 知识库。实现分层元数据结构，并将知识库集成到 Amazon EventBridge，以提供同步的目录更新并生成向量。
- **D.** 部署带有专用向量端点的 Amazon Neptune 集群。配置自定义相似度计算函数，并使用 Neptune 批量加载器 API 管理整个产品目录的向量更新。

### English

A retail company has a product recommendation system that uses a foundation model (FM) in Amazon Bedrock. The system processes and searches millions of product embeddings that are updated hourly. The company regularly adds new products. During peak shopping events, the current solution experiences high latency and occasional failures when the system performs similarity searches across the product catalog. The company must resolve the performance issues. Which solution will meet this requirement?
---

- **A.** Implement Amazon OpenSearch Service with a multi-node architecture. Configure vector-specific index settings and memory circuit breakers. Establish dedicated vector operation queues to handle surge capacity during peak usage events.
- **B.** Deploy an Amazon DynamoDB table that uses sparse vector compression. Implement parallel query executors to handle search operations. Configure DynamoDB Streams and AWS Lambda functions to maintain the vector index in real time.
- **C.** Set up an Amazon Bedrock knowledge base that uses custom vector partitioning. Implement hierarchical metadata structures. Integrate the knowledge base with Amazon EventBridge to provide synchronized catalog updates and to generate vectors.
- **D.** Deploy an Amazon Neptune cluster with specialized vector endpoints. Configure custom similarity computation functions. Use the Neptune bulk loader API to manage vector updates across the product catalog.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 124 - Topic 1

### 中文

一家医疗机构正在开发一个使用基础模型（FM）处理敏感患者数据并生成治疗摘要的应用。应用必须维护所有提示和补全内容的审计跟踪，并在整个处理生命周期内安全处理受保护健康信息（PHI）。公司必须跟踪包含原始患者数据的每一对提示和补全内容，但必须从存储记录中删除任何 PHI，同时还必须执行医疗行业特定的数据保留策略。哪种解决方案能够满足这些要求？
---

- **A.** 将提示-补全对存储在 Amazon S3 中并启用默认服务器端加密。部署 AWS Lambda 函数，使用 Amazon Comprehend Medical 扫描记录，并配置 Lambda 函数在规定的保留期限后删除 PHI 数据。
- **B.** 使用 Amazon Bedrock Prompt Management 和 Amazon Bedrock Flows 检测 PHI。配置 Amazon Bedrock Guardrails 使用敏感信息过滤器自动掩码 PHI。将日志存储在 Amazon S3 中，并配置适当的保留设置。
- **C.** 将提示-补全对存储在 Amazon S3 中。配置合规模式的 S3 Object Lock，并对 PHI 数据应用基于标签的 S3 Lifecycle 策略。配置 AWS Lambda 函数，在应用处理数据后重新删除患者信息。
- **D.** 将患者数据存储在 Amazon Bedrock 知识库中。使用 Amazon Comprehend Medical 识别 PHI，并配置 AWS Lambda 函数根据医疗政策管理数据保留。

### English

A healthcare company is developing an application that processes sensitive patient data and generates treatment summaries by using a foundation model (FM). The application must maintain an audit trail of all prompts and completions. The application must securely handle protected health information (PHI) data throughout the processing lifecycle. The company must track all prompt-completion pairs with original patient data, but the company must redact any PHI from stored records. The company must enforce healthcare-specific data retention policies. Which solution will meet these requirements?
---

- **A.** Store prompt-completion pairs in Amazon S3 and enable default server-side encryption. Deploy an AWS Lambda function that scans records by using Amazon Comprehend Medical. Configure the Lambda function to delete PHI data after a required retention period.
- **B.** Use Amazon Bedrock Prompt Management and Amazon Bedrock Flows to detect PHI. Configure Amazon Bedrock Guardrails to use sensitive information filters to mask PHI automatically. Store logs in Amazon S3 and configure appropriate retention settings.
- **C.** Store prompt-completion pairs in Amazon S3. Configure S3 Object Lock in compliance mode. Apply tag-based S3 Lifecycle policies to PHI data. Configure an AWS Lambda function to redact patient information after the application processes the data.
- **D.** Store patient data in an Amazon Bedrock knowledge base. Use Amazon Comprehend Medical to identify PHI. Configure an AWS Lambda function to manage data retention according to healthcare policies.

**Correct Answer / 正确答案:** `B`

**Community vote distribution / 社区投票分布:**


---

## Question 125 - Topic 1

### 中文

一家金融服务公司运行一个使用 Amazon Bedrock 基础模型（FM）并基于客户文档生成个性化投资建议的 AI 应用。该应用处理客户投资组合、财务报表和监管文档，以生成个性化建议。

由于数据质量较差，公司遇到模型性能不一致的问题。一些文档包含公司必须删除的个人身份信息（PII），另一些文档存在会使 FM 困惑的格式不一致。公司需要一种能够提供一致、高质量输入以优化 FM 的方案，同时满足金融法规和 PII 处理政策。哪种解决方案能够以**最低的运维开销**满足要求？
---

- **A.** 使用 Amazon Macie 自动检测并删除 PII。使用 AWS Lambda 函数和 Amazon Textract 强制执行标准文档格式，并创建能够适应单个文档架构的动态提示，以提高模型一致性。
- **B.** 使用 Amazon Comprehend 的自定义实体识别检测 PII。使用 AWS Step Functions 编排文档预处理工作流，并使用带条件逻辑的提示模板有效处理架构差异。
- **C.** 使用 Amazon Bedrock Guardrails 在推理期间过滤 PII。使用预处理 AWS Lambda 函数标准化文档格式，并使用少样本提示技术和标准化示例，提高模型在不同文档类型上的性能。
- **D.** 使用 Amazon Textract 分析文档结构并提取文本。使用 Amazon Comprehend 检测 PII 并分析情感；设计使用结构化元数据的提示工程策略，以改善 FM 推理并减少幻觉。

### English

A financial services company runs an AI application that uses Amazon Bedrock foundation models (FMs) to generate personalized investment recommendations based on customer documents. The AI application processes customer portfolios, financial statements, and regulatory documentation to create personalized recommendations. The company is experiencing inconsistent model performance because of poor data quality. Some documents contain personally identifiable information (PII) that the company must redact. Other documents contain formatting inconsistencies that confuse the FMs. The company needs a solution that ensures consistent, high-quality inputs to optimize FM. The solution must comply with financial regulations and PII handling policies. Which solution will meet these requirements with the LEAST operational overhead?
---

- **A.** Use Amazon Macie to automatically detect and redact PII. Use AWS Lambda functions and Amazon Textract to enforce standard document formatting. Create dynamic prompts that adapt to individual document schemas to improve model consistency.
- **B.** Use Amazon Comprehend to detect PII by using custom entity recognition. Use AWS Step Functions to orchestrate document preprocessing workflows. Use prompt templates with conditional logic to handle schema variations effectively.
- **C.** Use Amazon Bedrock Guardrails to filter out PII during inference. Use pre-processing AWS Lambda functions to standardize document formatting. Use few-shot prompting techniques and standardized examples to improve model performance across document types.
- **D.** Use Amazon Textract to analyze document structures and extract text. Use Amazon Comprehend to detect PII and analyze sentiment. Design prompt engineering strategies that use structured metadata to improve FM reasoning and reduce hallucinations.

**Correct Answer / 正确答案:** `C`

**Community vote distribution / 社区投票分布:**


---

## Question 126 - Topic 1

### 中文

一家全球性公司使用 AWS Organizations 管理 25 个 AWS 账户。公司的产品每天有 50,000 次用户交互，目前使用 Amazon Bedrock AgentCore 构建 AI 助手。

该 AI 助手必须访问分布在多个业务部门 AWS 账户中的客户数据，并代表特定用户，通过查询每个业务部门账户中的 Amazon DynamoDB 表执行操作。每个业务部门维护自己的 AWS 账户和 DynamoDB 表。

公司必须强制执行严格的数据访问安全边界，并收集完整的审计跟踪，显示每个操作使用了哪位用户的凭证。方案必须允许 AgentCore 应用跨多个账户访问资源，同时保留用户身份上下文，并支持公司下一年扩展到 40 多个 AWS 账户的计划。哪种解决方案能够以**最低的运维开销**满足要求？
---

- **A.** 配置 AI 助手使用出站凭证提供程序，通过运行 `AssumeRole` 操作动态获取临时凭证，以便访问跨账户资源。在每个业务部门账户中创建 IAM 角色，使其信任 AI 助手的执行角色，并授予访问 DynamoDB 表的权限。确保 `AssumeRole` 调用包含用于识别最终用户的会话标签。
- **B.** 在每个业务部门账户中创建独立的 AI 助手。部署运行请求路由逻辑的 Amazon API Gateway REST API，并创建 AWS Lambda 授权方，根据请求上下文中的客户业务部门标识符将用户查询定向到适当的 AI 助手。
- **C.** 使用 AWS Resource Access Manager（AWS RAM）将每个业务部门账户中的 DynamoDB 表共享到 AI 助手运行的中央账户。配置 AWS RAM 权限以允许跨账户访问 DynamoDB 表，并授予 AgentCore 执行角色直接访问共享资源的权限。
- **D.** 配置 SCP，明确允许 AI 助手的执行角色主体在所有成员账户中执行 `dynamodb:GetItem` 和 `dynamodb:Query` 操作。配置 AI 助手在其资源定义中直接引用跨账户 DynamoDB 表 ARN。

### English

A global company uses an organization in AWS Organizations to manage 25 AWS accounts. The company’s products have 50,000 daily user interactions. The company is using Amazon Bedrock AgentCore to build an AI assistant. The AI assistant must access customer data that is stored in multiple AWS accounts across several business units. The AI assistant needs to perform actions on behalf of specific users by querying Amazon DynamoDB tables that contain customer information in each business unit's AWS account. Each business unit maintains its own AWS account that hosts its own DynamoDB tables. The company must enforce strict data access security boundaries and must collect full audit trails that show which user’s credentials are used to perform each action. The solution must allow the AgentCore application to access resources across multiple accounts while preserving user identity context. The solution must support the company’s plan to expand to more than 40 AWS accounts in the next year. Which solution will meet these requirements with the LEAST operational overhead?
---

- **A.** Configure the AI assistant to use an outbound credential provider that runs `AssumeRole` operations to dynamically obtain temporary credentials so the AI assistant can access resources across accounts. Create an IAM role in each business unit account that has a trust relationship to the AI assistant’s execution role and permissions to access the DynamoDB tables. Ensure that `AssumeRole` calls include session tags to identify the end user.
- **B.** Create a separate AI assistant in each business unit account. Deploy an Amazon API Gateway REST API that runs request routing logic. Create AWS Lambda authorizers to direct user queries to the appropriate AI assistant based on the customer’s business unit identifier from the request context.
- **C.** Use AWS Resource Access Manager (AWS RAM) to share the DynamoDB tables from each business unit account to a central account where the AI assistant runs. Configure AWS RAM permissions to allow cross-account access to the DynamoDB tables. Grant the AgentCore execution role permissions to access the shared resources directly.
- **D.** Configure SCPs for the organization to explicitly allow the AI assistant’s execution role principal to perform `dynamodb:GetItem` and `dynamodb:Query` actions across all member accounts. Configure the AI assistant to reference cross-account DynamoDB table ARNs directly in their resource definitions.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:**


---

## Question 127 - Topic 1

### 中文

一家医院正在构建 AI 应用，帮助临床医生做出治疗决策。应用使用 Amazon Bedrock 分析患者病历并建议诊断。应用必须保持低于 500 毫秒的响应时间，以接入医院现有的实时临床工作流。为遵守隐私法规，应用必须记录所有 PII 处理决策以供审计，并以至少 99% 的准确率检测和删除响应中的 PII。

初始部署后，临床医生报告应用偶尔会在诊断摘要中包含原始病例历史中不存在的患者姓名和病历编号。调查发现，Amazon Comprehend Medical 能够以 95% 的准确率成功检测并删除输入中的 PII，应用会在将输入发送到 Amazon Bedrock 前用令牌替换所有检测到的实体。然而，应用仍会在约 3%-5% 的输出中生成患者身份信息。

公司需要一种方案，防止应用在满足其他所有运维要求的同时向用户显示 PII。哪种解决方案能够满足这些要求？
---

- **A.** 配置 Amazon Bedrock Guardrails，使用敏感信息过滤器检测并阻止模型输出中的 PII。
- **B.** 在将输入发送到 Amazon Bedrock 之前，使用正则表达式和自定义实体识别实现第二层 PII 检测，以检测 Amazon Comprehend Medical 遗漏的标识符。
- **C.** 在预处理期间删除病例历史中的详细医疗上下文，防止模型基于临床模式关联生成特定患者信息。
- **D.** 启用 Amazon Bedrock API 调用的会话隔离。清除请求之间的会话历史，防止患者信息在不同病例分析之间持久存在。

### English

A hospital is building an AI application to help medical clinicians to make treatment decisions. The application uses Amazon Bedrock to analyze patient case histories and suggest diagnoses. The application must maintain sub-500 ms response times to integrate with the hospital’s existing real-time clinical workflow. To comply with privacy regulations, the application must log all personally identifiable information (PII) handling decisions for audits. The application must detect and remove PII from responses with at least 99% accuracy. After initial deployment, clinicians report that diagnostic summaries from the application occasionally include patient names and medical record numbers that were not present in the original case history inputs. An investigation reveals that Amazon Comprehend Medical successfully detects and removes PII from inputs with 95% accuracy, and the application replaces all detected entities with tokens before it sends inputs to Amazon Bedrock. However, the application continues to generate patient-identifying information in approximately 3-5% of outputs. The company needs a solution to prevent the application from displaying PII in outputs while meeting all other operational requirements. Which solution will meet these requirements?
---

- **A.** Configure Amazon Bedrock Guardrails with sensitive information filters to detect and block PII in model outputs.
- **B.** Implement a secondary PII detection layer by using regular expressions and custom entity recognition to detect identifiers that Amazon Comprehend Medical misses before sending inputs to Amazon Bedrock.
- **C.** Remove detailed medical context from case histories during pre-processing to prevent the model from generating patient-specific information based on clinical pattern associations.
- **D.** Enable session isolation in Amazon Bedrock API calls. Clear conversation history between requests to prevent patient information from persisting across multiple case analyses.

**Correct Answer / 正确答案:** `A`

**Community vote distribution / 社区投票分布:** `A (100%)`


---
