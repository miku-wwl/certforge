# 06 Multi-Region 容灾与自动 Failover

## AWS 架构图

![06 Multi-Region 容灾与自动 Failover 架构图](diagrams/06-multi-region-dr.png)

## 关键架构决策

| 架构需求 | 推荐设计 |
|---|---|
| Serverless API 跨 Region | 第二套 API Gateway/Lambda + Route 53 failover + DynamoDB Global Tables |
| 低成本且 RTO < 15 分钟 | Pilot Light / Warm Standby + 自动提升 DB + 扩容 + DNS failover |
| 关系型数据库跨 Region | Cross-Region read replica / Aurora Global Database，故障时 promote |
| 对象跨 Region | S3 Cross-Region Replication + CloudFront origin group |

## 架构设计要点

- 先根据业务 RTO/RPO 和预算判断 DR 模式：Backup/Restore → Pilot Light → Warm Standby → Active/Active。
- “已有备 Region ASG min=max=0 + DB read replica”通常是在暗示 pilot-light 自动化切换。
- 自动恢复顺序应是“提升数据库 → 启动并验证应用 → 最后切 DNS/流量”，不能先把用户导向未就绪 Region。
- Aurora Global Database 非计划故障转移为异步复制，可能出现非零 RPO；只有计划内 switchover 才能以数据同步为前提达到 RPO 0。
