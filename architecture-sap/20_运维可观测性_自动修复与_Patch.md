# 20 运维可观测性、自动修复与 Patch

## AWS 架构图

![20 运维可观测性、自动修复与 Patch 架构图](diagrams/20-operations-observability.png)

## 关键架构决策

| 架构需求 | 推荐设计 |
|---|---|
| 混合服务器统一 patch 报告 | Systems Manager Patch Manager / compliance |
| ASG 终止前执行动作 | Lifecycle Hook + EventBridge + Lambda + SSM SendCommand |
| 自动修复配置偏差 | Config/EventBridge → SSM Automation/Lambda |
| 集中运维事件 | CloudWatch + OpsCenter / EventBridge |

## 架构设计要点

- CloudWatch 是运行时 telemetry；CloudTrail 是 API audit；Config 是资源配置历史/合规。
- Auto Scaling lifecycle hook 可以在 terminate 前留出时间执行日志归档等清理动作。
- EC2 原生指标不包含完整 guest OS 内存、磁盘占用与应用日志；这些数据需要 CloudWatch Agent 或应用自身上报。
- 组织级 patch 优先使用 Systems Manager patch policies/Quick Setup 或 State Manager association，而不是逐台临时 Run Command。
- 生命周期清理完成后必须调用 `CompleteLifecycleAction`，否则实例会停留到 hook timeout。
