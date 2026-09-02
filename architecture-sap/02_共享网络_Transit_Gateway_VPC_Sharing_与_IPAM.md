# 02 共享网络 Transit Gateway、VPC Sharing 与 IPAM

## AWS 架构图

![02 共享网络 Transit Gateway、VPC Sharing 与 IPAM 架构图](diagrams/02-shared-network-tgw.png)

## 关键架构决策

| 架构需求 | 推荐设计 |
|---|---|
| 成员账户只能部署资源，不能管网络 | VPC Sharing + AWS RAM |
| 大量 VPC 互联 | Transit Gateway，避免全网状 VPC Peering |
| 组织级共享 TGW | RAM 分享 TGW，StackSets 自动创建 VPC/Attachment |
| 地址空间治理 | VPC IPAM 统一 CIDR 分配与审计 |

## 架构设计要点

- Shared VPC 的网络资源由拥有者账户管理；参与账户在共享子网中创建自己的工作负载。
- TGW attachment 属于 VPC，不是共享子网本身；共享子网和 TGW 互联是两个独立设计点。
- TGW route table 可以把 Prod、NonProd、Shared Services 做逻辑隔离；集中状态防火墙还要配 inspection VPC、对称路由和 appliance mode。
- IPAM 适合由组织委派管理员统一管理 CIDR 池、分配与合规性。
