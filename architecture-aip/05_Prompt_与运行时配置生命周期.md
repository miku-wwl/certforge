# 05 Prompt 与运行时配置生命周期

> 系列：AWS AIP-C01 117 题经典架构
>
> 分析范围：`AWS_AIP-C01_中文版.md` 的 Q1–Q117；题号与正确方案按本地题库归纳。

## AWS 架构图

![05 Prompt 与运行时配置生命周期架构图](diagrams/05-prompt-config-lifecycle.png)

可编辑源图：[05-prompt-config-lifecycle.svg](diagrams/05-prompt-config-lifecycle.svg)

## 两条控制线

```mermaid
flowchart TD
    subgraph PromptPlane[Prompt 控制面]
        P1[Prompt Management 模板/变量] --> P2[版本与 Variant]
        P2 --> P3[评估与审批]
        P3 --> P4[已批准 Prompt ARN/版本]
    end

    subgraph RuntimePlane[运行时配置面]
        C1[AppConfig Feature Flag] --> C2[Validation Rule]
        C2 --> C3[Deployment Strategy]
        C3 --> C4[AppConfig Agent/Extension]
        C4 --> L[Lambda Router]
        L --> M1[FM A]
        L --> M2[FM B]
    end

    P4 --> L
    P1 --> CT[CloudTrail 审计]
    C1 --> CT
```

## 三个服务不要混淆

| 需求 | 应选能力 | 代表题号 |
|---|---|---|
| Prompt 模板、变量、版本、Variant | Bedrock Prompt Management | Q5、Q9、Q11、Q84、Q110 |
| 无需重新部署代码就切模型/参数 | AWS AppConfig + Agent/Extension | Q4、Q49、Q59、Q103 |
| 多步骤调用、分支、重试、回滚流程 | Step Functions | Q22、Q53、Q111 |

Q103 是运行时配置发布的经典完整题：Feature Flag + Validation Rule + Lambda Extension + Linear Deployment Strategy + 自动回滚。

---

## 系列导航

- 上一篇：[04 Guardrails 纵深防御](./04_Guardrails_纵深防御.md)
- 系列总览：[01 企业级托管 RAG](./01_企业级托管_RAG.md)
- 下一篇：[06 评估驱动的发布门禁](./06_评估驱动的发布门禁.md)
