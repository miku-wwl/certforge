# AWS SAP-C02 经典架构总览（20 场景）

本目录把 SAP-C02 常见架构决策归纳为 20 个经典场景。每个场景包含：

- 一张 PNG 架构图；
- 一份可编辑 SVG；
- 一篇 Markdown，包含核心架构、代表题号、考试决策点和审计要点。

## 审计状态

- 架构语义、服务边界和关键数据流已于 2026-09-02 按 AWS 官方文档完成审计与整改；详见 [AUDIT_REPORT.md](./AUDIT_REPORT.md)。
- 图稿统一采用 AWS 官方架构图视觉语言：AWS Cloud/Region 边界、官方服务图标、白底服务卡片、实线数据流和虚线控制/复制流。
- **题号映射为 NOT VERIFIED**：当前工作区没有原始 `aws-sap-c02-529-questions副本.md`，所以表中的代表题号只保留为历史索引，不能视为本次审计已重新核对的题库证据。

| # | 场景 | 代表题 |
|---|---|---|
| 01 | [Organizations 多账户治理与 Landing Zone](./01_Organizations_多账户治理与_Landing_Zone.md) | Q3, Q31, Q32, Q38, Q57, Q64… |
| 02 | [共享网络 Transit Gateway、VPC Sharing 与 IPAM](./02_共享网络_Transit_Gateway_VPC_Sharing_与_IPAM.md) | Q11, Q30, Q53, Q118, Q131, Q181… |
| 03 | [混合网络 Direct Connect、VPN 与 Route 53 Resolver](./03_混合网络_Direct_Connect_VPN_与_Route53_Resolver.md) | Q1, Q17, Q35, Q65, Q77, Q93… |
| 04 | [PrivateLink、VPC Endpoint 与私有服务访问](./04_PrivateLink_VPC_Endpoint_与私有服务访问.md) | Q12, Q40, Q115, Q172, Q218, Q230… |
| 05 | [全球入口 CloudFront、Global Accelerator、Route 53 与 WAF](./05_全球入口_CloudFront_Global_Accelerator_Route53_与_WAF.md) | Q5, Q10, Q16, Q28, Q36, Q52… |
| 06 | [Multi-Region 容灾与自动 Failover](./06_Multi-Region_容灾与自动_Failover.md) | Q2, Q8, Q69, Q135, Q148, Q160… |
| 07 | [高可用三层 Web 与数据库伸缩](./07_高可用三层_Web_与数据库伸缩.md) | Q4, Q9, Q16, Q33, Q37, Q45… |
| 08 | [Serverless API 与无服务器数据层](./08_Serverless_API_与无服务器数据层.md) | Q2, Q22, Q27, Q33, Q45, Q48… |
| 09 | [Event-Driven 解耦与工作流编排](./09_Event-Driven_解耦与工作流编排.md) | Q14, Q42, Q44, Q68, Q94, Q95… |
| 10 | [ECS、EKS、Fargate 容器平台](./10_ECS_EKS_Fargate_容器平台.md) | Q7, Q45, Q54, Q63, Q68, Q93… |
| 11 | [数据湖、分析与查询平台](./11_数据湖_分析与查询平台.md) | Q43, Q46, Q79, Q92, Q101, Q114… |
| 12 | [数据库迁移、复制与现代化](./12_数据库迁移_复制与现代化.md) | Q37, Q48, Q50, Q72, Q80, Q81… |
| 13 | [文件存储与大规模数据迁移](./13_文件存储与大规模数据迁移.md) | Q23, Q35, Q62, Q93, Q102, Q138… |
| 14 | [HPC、Batch、FSx for Lustre 与 Spot](./14_HPC_Batch_FSx_for_Lustre_与_Spot.md) | Q23, Q25, Q42, Q134, Q152, Q170… |
| 15 | [安全基线、检测与集中审计](./15_安全基线_检测与集中审计.md) | Q44, Q57, Q90, Q91, Q97, Q101… |
| 16 | [身份联合、IAM Identity Center 与跨账户访问](./16_身份联合_IAM_Identity_Center_与跨账户访问.md) | Q21, Q70, Q118, Q200, Q258, Q261… |
| 17 | [IaC、StackSets、Service Catalog 与自动化治理](./17_IaC_StackSets_Service_Catalog_与自动化治理.md) | Q13, Q14, Q19, Q26, Q30, Q37… |
| 18 | [AWS Backup、Elastic Disaster Recovery 与迁移恢复](./18_AWS_Backup_Elastic_Disaster_Recovery_与迁移恢复.md) | Q62, Q76, Q102, Q148, Q160, Q169… |
| 19 | [成本优化、容量采购与 FinOps](./19_成本优化_容量采购_与_FinOps.md) | Q25, Q29, Q34, Q41, Q79, Q83… |
| 20 | [运维可观测性、自动修复与 Patch](./20_运维可观测性_自动修复与_Patch.md) | Q13, Q14, Q54, Q65, Q86, Q109… |

> 说明：这是复习用架构集，不是 AWS 官方考试蓝图的章节映射，也不是对任何考试原题的官方解释。
