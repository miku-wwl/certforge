# 04 PrivateLink、VPC Endpoint 与私有服务访问

## AWS 架构图

![04 PrivateLink、VPC Endpoint 与私有服务访问 架构图](diagrams/04-privatelink-endpoints.png)

## 关键架构决策

| 架构需求 | 推荐设计 |
|---|---|
| 访问第三方 AWS-hosted SaaS 且不能经过公网 | Interface Endpoint + PrivateLink |
| S3 大流量从私有子网访问 | S3 Gateway VPC Endpoint，降低 NAT 成本 |
| 只暴露单个服务而非整张网络 | PrivateLink 优于 VPC Peering |
| AWS API 私网访问 | Interface VPC Endpoint + endpoint policy / SG |

## 架构设计要点

- Gateway Endpoint 主要用于 S3/DynamoDB；Interface Endpoint 基于 ENI/PrivateLink。
- PrivateLink 是服务级连接，不需要交换整张 VPC 路由表，因此常用于 SaaS 和跨账户服务。
- S3/DynamoDB Gateway Endpoint 是路由表目标，不经过 PrivateLink、NAT Gateway 或 Internet Gateway。
- NAT Gateway 只保留给与这些 endpoint 无关的公网出站流量。
