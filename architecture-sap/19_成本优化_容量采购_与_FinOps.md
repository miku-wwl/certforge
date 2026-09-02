# 19 成本优化、容量采购与 FinOps

## AWS 架构图

![19 成本优化、容量采购与 FinOps 架构图](diagrams/19-cost-finops.png)

## 关键架构决策

| 架构需求 | 推荐设计 |
|---|---|
| 稳定长期基线 | Savings Plans / Reserved Capacity |
| 可重试弹性任务 | Spot + Auto Scaling |
| 关键 SLA 容量保证 | On-Demand Capacity Reservations |
| 组织成本拆分 | 启用 cost allocation tags + CUR + Athena/QuickSight |

## 架构设计要点

- 业务不接受长期承诺时不要选 Savings Plans/RI。
- Spot 节省最多，但必须结合中断容忍度和 SLA 判断。
- Savings Plans/RI 提供使用折扣，不提供容量保证；ODCR 提供指定 AZ 的 EC2 容量，符合条件的 Savings Plans 折扣可另行应用于其用量。
- 成本标签必须激活后才进入账单维度；明细分析链路应是 Data Exports/CUR → S3 → Athena/QuickSight。
