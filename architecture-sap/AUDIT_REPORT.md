# architecture-sap 架构审计报告

审计日期：2026-09-02
范围：`architecture-sap` 的 20 份场景文档及对应 SVG/PNG 架构图。

## 结论

20 个场景的主题选择是合理的，但原稿存在多处“服务名正确、连接语义不正确”的问题。此次整改把数据面、控制面、复制/治理关系拆开，并去掉了可能在 SAP 复习中造成错误记忆的直接连线。

**题号验证状态：NOT VERIFIED。** 当前工作区未包含文档所引用的 `aws-sap-c02-529-questions副本.md`，因此本次只审计架构语义，不声称已逐题复核代表题号。

## 整改清单

| # | 场景 | 关键审计结论与整改 |
|---|---|---|
| 01 | 多账户治理 | CloudTrail 原始日志改到独立 Log Archive/S3；Config Aggregator 与 Security Hub 分离；明确 SCP 只限制、不授予权限。 |
| 02 | 共享网络 | TGW attachment 改为 VPC 级；集中防火墙补充 inspection VPC、对称路由和 appliance mode；IPAM 改为组织委派管理。 |
| 03 | 混合网络 | DXGW→TGW 明确使用 transit VIF；Resolver inbound/outbound 拆成两条相反方向的 DNS 路径。 |
| 04 | PrivateLink | 补齐 interface endpoint→endpoint service→NLB→provider service；S3/DynamoDB gateway endpoint 与 NAT/Public egress 分离。 |
| 05 | 全球入口 | 删除 Global Accelerator→API Gateway 的错误直连；CloudFront S3 origin 补 OAC；区分 CDN/L7 与 L4 Anycast。 |
| 06 | Multi-Region DR | 故障顺序改为先提升数据层、再启动并验证应用、最后切流量；明确 Aurora Global Database 非计划切换可能有非零 RPO。 |
| 07 | 三层高可用 | Aurora reader 才是 replica auto scaling 目标；EFS 与 S3 拆分；会话优先外置。 |
| 08 | Serverless API | Cognito 明确为 User Pool authorizer；区分 REST API AWS service integration 与 HTTP API 能力边界。 |
| 09 | 事件驱动 | SQS→Lambda 失败由源队列 redrive 到 DLQ；补 partial batch response 与幂等；Step Functions 状态与日志分流。 |
| 10 | 容器平台 | Fargate 改为 ECS capacity provider/EKS Fargate profile；ECS 与 EKS 拆成独立 lane 和 target group。 |
| 11 | 数据湖 | Glue Catalog 改为元数据存储；Lake Formation 治理 Catalog + 注册 S3；OpenSearch 增加真实摄取层。 |
| 12 | 数据库迁移 | 新流程使用 DMS Schema Conversion + full load/CDC；Aurora Global Database 与普通 RDS 跨 Region replica 分开。 |
| 13 | 文件迁移 | 增加 on-prem DataSync Agent 和网络路径；Storage Gateway 保留为持续混合访问；FSx Lustre 使用 DRA。 |
| 14 | HPC/Batch | 删除不必要的外置 SQS；使用 Batch Job Queue/Scheduler/Compute Environment；容量保证与折扣分开。 |
| 15 | 安全治理 | CloudTrail→S3、GuardDuty/Macie→Security Hub、Config→Aggregator；删除笼统 WAF/Shield→Security Hub 原始信号线。 |
| 16 | 身份联合 | 外部 workforce IdP 明确使用 SAML 2.0；SCIM 只负责用户/组 provisioning；Permission Set 与工作负载 AssumeRole 分开。 |
| 17 | IaC 治理 | StackSets 明确为 CloudFormation 组织部署能力；Service Catalog 补 launch constraint role；运营自动化独立成 lane。 |
| 18 | Backup/DR | AWS Backup、DRS、MGN 拆成三条独立路径；DRS staging area 与 recovery instances 补齐。 |
| 19 | 成本/FinOps | Savings Plans/RI 是折扣、ODCR 是容量；成本链路改为激活标签→Data Exports/CUR→S3→Athena/QuickSight。 |
| 20 | 运维 | CloudWatch Agent 与 EC2 原生指标边界明确；Patch policies/Quick Setup 用于组织 patch；lifecycle hook 补 CompleteLifecycleAction。 |

## 图稿规范

- 使用 AWS 官方 Architecture Service Icons，不用通用彩色圆角框替代服务身份。
- AWS Cloud、Region、VPC、Account/OU 使用不同层级边界；不要把逻辑服务误画成 Region 内必部署资源。
- 实线表示主要请求/数据路径；虚线表示治理、复制、健康检查或控制关系。
- 外部系统和 on-prem 组件放在 AWS Cloud 边界外；多 Region 场景必须画出独立 Region 边界。
- 每张图底部保留图例和一条“考试边界”短注，避免仅凭连线记忆错误职责。

## 主要官方依据

- [AWS Architecture Icons](https://aws.amazon.com/architecture/icons/)
- [AWS Control Tower logging](https://docs.aws.amazon.com/controltower/latest/userguide/about-logging.html)
- [How AWS Transit Gateway works](https://docs.aws.amazon.com/vpc/latest/tgw/how-transit-gateways-work.html)
- [Gateway endpoints](https://docs.aws.amazon.com/vpc/latest/privatelink/gateway-endpoints.html)
- [API Gateway endpoint types](https://docs.aws.amazon.com/apigateway/latest/developerguide/api-gateway-api-endpoint-types.html)
- [Aurora Global Database disaster recovery](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/aurora-global-database-disaster-recovery.html)
- [Using Lambda with Amazon SQS](https://docs.aws.amazon.com/lambda/latest/dg/with-sqs.html)
- [DMS Schema Conversion](https://docs.aws.amazon.com/dms/latest/userguide/schema-conversion.html)
- [External identity providers for IAM Identity Center](https://docs.aws.amazon.com/singlesignon/latest/userguide/manage-your-identity-source-idp.html)
- [AWS Backup concepts](https://docs.aws.amazon.com/aws-backup/latest/devguide/whatisbackup.html)
- [Elastic Disaster Recovery core concepts](https://docs.aws.amazon.com/guidance/latest/deploying-cross-region-disaster-recovery-with-aws-elastic-disaster-recovery/core-concepts.html)
- [What are Savings Plans?](https://docs.aws.amazon.com/savingsplans/latest/userguide/what-is-savings-plans.html)
- [CloudWatch Agent configuration](https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/CloudWatch-Agent-Configuration-File-Details.html)
