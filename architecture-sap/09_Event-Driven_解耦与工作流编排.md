# 09 Event-Driven 解耦与工作流编排

## AWS 架构图

![09 Event-Driven 解耦与工作流编排 架构图](diagrams/09-event-driven.png)

## 关键架构决策

| 架构需求 | 推荐设计 |
|---|---|
| 服务间缓冲 / 削峰 | SQS；根据 backlog 扩缩 worker |
| 一条事件分发多个消费者 | SNS fan-out / EventBridge rules |
| 复杂顺序、重试、补偿 | Step Functions |
| 长达 1 小时任务 | 不要用 Lambda；SQS + EC2/ECS workers |

## 架构设计要点

- EventBridge 更适合事件路由；SQS 更适合 durable queue/backpressure；SNS 更适合 pub/sub fan-out。
- 长任务首先检查 Lambda 最大执行时长，超过边界要转容器/EC2/Batch。
- SQS 事件源映射失败时，消息返回源队列；达到 `maxReceiveCount` 后由 SQS redrive policy 移入 DLQ，不是 Lambda 直接把事件发送到该 DLQ。
- 批处理消费者应启用 partial batch response，并以幂等写入防止至少一次投递造成重复副作用。
