# Spring Cloud 微服务示例项目

## 项目介绍

这是一个基于Spring Cloud的微服务示例项目，演示了服务注册与发现、远程调用等核心功能。项目使用Nacos作为服务注册中心，并配置为集群模式。

## 技术栈

- Spring Boot 3.4.4
- Spring Cloud 2024.0.1
- Spring Cloud Alibaba 2023.0.3.2
- Nacos 3.0.0（服务注册与配置中心）
- OpenFeign（服务间调用）
- Sentinel（服务保护）

## 项目结构

```
cloud-demo/
├── model/                 # 共享模型模块
├── services/              # 微服务模块
│   ├── service-product/   # 产品服务
│   └── service-order/     # 订单服务
└── pom.xml                # 父项目POM
```

## 功能特性

- 服务注册与发现：使用Nacos作为服务注册中心
- 远程服务调用：使用OpenFeign实现服务间通信
- 负载均衡：集成Spring Cloud LoadBalancer
- 服务保护：集成Sentinel实现服务熔断降级
- 配置中心：使用Nacos作为配置中心

## 快速开始

### 前置条件

- JDK 17+
- Maven 3.6+
- Nacos 3.0.0+

### 启动步骤

1. 启动Nacos服务（集群模式）

   ```
   cd nacos/bin
   startup.cmd -m cluster
   ```
2. 启动服务

   ```
   # 启动产品服务
   cd services/service-product
   mvn spring-boot:run

   # 启动订单服务
   cd services/service-order
   mvn spring-boot:run
   ```
3. 访问服务

   - 产品服务：http://localhost:8089/product/{id}
   - 订单服务：http://localhost:8095/order?productId=1&userId=1

## Nacos集群配置

项目使用Nacos集群模式，配置了外部MySQL数据库存储配置和服务信息，确保高可用性。

## 项目说明

本项目为实验一，展示了Spring Cloud微服务架构的基本组件和使用方式。


