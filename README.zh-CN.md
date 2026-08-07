# pac4j-uniauth

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-17-orange)](https://github.com/easy-4-java/pac4j-uniauth) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](./LICENSE)

pac4j-uniauth 是 pac4j 5.0.x 的扩展组件，用于将校园统一身份认证中心（Uniauth）接入 pac4j 安全流程。

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 功能与状态](#2-功能与状态)
- [3. 环境要求与兼容性](#3-环境要求与兼容性)
- [4. 架构与模块](#4-架构与模块)
- [5. 安装](#5-安装)
- [6. 快速开始](#6-快速开始)
- [7. 配置](#7-配置)
- [8. 核心用法 / API](#8-核心用法--api)
- [9. 测试与构建](#9-测试与构建)
- [10. 版本线与分支](#10-版本线与分支)
- [11. 贡献与许可](#11-贡献与许可)

## 1. 项目概述

**pac4j-uniauth** 是 [pac4j](https://www.pac4j.org) 5.0.x 的扩展组件，用于将校园统一身份认证中心（Uniauth）接入 pac4j 安全流程。它构建在 `io.github.easy4j:pac4j-biz` 之上，后者提供了通用的 `Token` / `Signature` 凭证、客户端与用户资料机制。

支持两种认证模式：

| 模式                                  | 流程                                                                 |
| :------------------------------------ | :------------------------------------------------------------------- |
| Token 模式（`UniauthToken*`）          | 认证中心下发用户令牌，组件通过 profile URL 换取用户资料               |
| Signature 模式（`UniauthSignature*`）  | 认证中心对用户数据签名，组件校验签名并解析载荷                        |

解析出的用户资料携带校园系统常用的统一人员字段：`userid`（用户 id）、`pid`（统一人员编号，作为资料 id）、`xm`（姓名）、`ptype`（人员类型：学生/教工）、`csrq`（出生日期）、`flag`（0 未初始化密码，1 已初始化密码）。

典型场景：

| 场景                     | 说明                                                     |
| :----------------------- | :------------------------------------------------------- |
| 校园门户单点登录          | 一个认证中心，多个业务应用                                |
| OAuth 式令牌交换         | 认证中心令牌 → 通过 `profileUrl` 获取用户资料             |
| 签名用户数据接入         | 使用前校验携带签名的用户数据载荷                          |

## 2. 功能与状态

| 能力                                               | 状态           | 说明                                                                                |
| :------------------------------------------------- | :------------- | :---------------------------------------------------------------------------------- |
| Token 凭证类型（`UniauthToken`）                    | 已实现         | 包装原始令牌字符串                                                                    |
| Token 认证器（`UniauthTokenAuthenticator`）         | 已实现         | 需要 `profileUrl`；用令牌换取用户资料                                                  |
| Token 资料定义（`UniauthTokenProfileDefinition`）   | 已实现         | 解析 `{"status":"fail"}` 错误与 `pinfo` 载荷为 `UniauthTokenProfile`                   |
| Token 客户端（`UniauthTokenClient`）                | 已实现         | 参数名 / 登录地址等接线继承自 `TokenClient`（pac4j-biz）                               |
| Signature 凭证 / 认证器 / 客户端                     | 部分实现       | `UniauthSignatureAuthenticator` / `UniauthSignatureClient` 已存在                     |
| Signature 资料解析                                   | 已知限制       | `UniauthSignatureProfileDefinition.extractUserProfile(...)` 解析后当前返回 `null`（见第 8 节） |
| 单元测试                                           | 暂无           | `src/test` 仅有占位类 `TokenExample`                                                  |

## 3. 环境要求与兼容性

| 要求     | 版本                              |
| :------- | :-------------------------------- |
| JDK      | 8+                                |
| Maven    | 3.0+（已内置 wrapper）             |
| pac4j    | 5.0.x                             |
| pac4j-biz| `2.0.x.x.20260630-SNAPSHOT`         |

easy4j 项目的版本线：

| 分支           | JDK  | 版本模式   | 说明                            |
| :------------- | :--- | :--------- | :------------------------------ |
| `feature/1.0.x` | 8    | `1.0.x.*`  | 本文档对应分支                   |
| `feature/2.0.x` | 17   | `2.0.x.*`  | JDK 17 版本线                   |
| `feature/3.0.x` | 21   | `3.0.x.*`  | JDK 21 版本线                   |

## 4. 架构与模块

```text
 认证中心跳转 / verify_request
              |
              v
  UniauthTokenClient (org.pac4j.ext.uniauth)
              |
              v
  UniauthTokenAuthenticator (profileUrl)
              |
              v
  profileUrl -> JSON { status | pinfo }
              |
              v
  UniauthTokenProfile  (userid/pid/xm/ptype/csrq/flag)
              |
              v
     pac4j profile store
```

单模块 Maven 项目（`jar` 打包），包含两个类族：

| 类族           | 类型                                                                                |
| :------------- | :---------------------------------------------------------------------------------- |
| Token 模式     | `UniauthToken`、`UniauthTokenAuthenticator`、`UniauthTokenClient`、`UniauthTokenProfile`、`UniauthTokenProfileDefinition` |
| Signature 模式 | `UniauthSignature`、`UniauthSignatureAuthenticator`、`UniauthSignatureClient`、`UniauthSignatureProfile`、`UniauthSignatureProfileDefinition` |

## 5. 安装

制品发布在阿里云私服与 GitHub Releases，**尚未发布到 Maven Central**。

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>pac4j-uniauth</artifactId>
    <version>2.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

```groovy
implementation 'io.github.easy4j:pac4j-uniauth:2.0.x.x.20260630-SNAPSHOT'
```

## 6. 快速开始

Token 模式（典型用法）：

```java
// 1) 认证器：通过该 URL 用令牌换取用户资料
UniauthTokenAuthenticator authenticator =
        new UniauthTokenAuthenticator("https://auth.example.edu.cn/oauth2/profile");

// 2) 客户端：从 "verify_request" 请求参数读取令牌
UniauthTokenClient client = new UniauthTokenClient("verify_request", authenticator);
client.setName("uniauth");
client.setLoginUrl("https://auth.example.edu.cn/oauth/authorize");
```

预期结果：认证中心跳转回传 `verify_request` 后，认证器请求 profile URL；若响应含 `"status": "fail"`，则抛出携带认证中心 `msg` 的 `HttpCommunicationException`；否则将 `pinfo` 解析为 `UniauthTokenProfile`，且 `profile.getId()` 返回 `pid`。

Signature 模式（典型用法）：

```java
UniauthSignatureAuthenticator signatureAuthenticator = new UniauthSignatureAuthenticator();
UniauthSignatureClient signatureClient = new UniauthSignatureClient("signature", signatureAuthenticator);
```

## 7. 配置

无属性文件，全部通过编程方式配置：

| 配置项                    | 设置方式                                                              | 默认值           |
| :------------------------ | :-------------------------------------------------------------------- | :--------------- |
| Profile URL               | `UniauthTokenAuthenticator(String profileUrl)`                        | —（必填）        |
| 请求参数名                 | `TokenClient(String parameterName, ...)` / `SignatureClient(String signatureParamName, ...)` | — |
| 登录地址                   | 继承的 `setLoginUrl(String)`                                          | —                |
| GET/POST 支持             | 继承的 `setSupportGetRequest(boolean)` / `setSupportPostRequest(boolean)` | pac4j 默认值 |

## 8. 核心用法 / API

Profile JSON 契约（按 `UniauthTokenProfileDefinition.extractUserProfile` 的实现）：

```json
{
  "status": "success",
  "pinfo": {
    "userid": "10001",
    "pid": "P20200001",
    "xm": "Zhang San",
    "ptype": "student",
    "csrq": "2000-01-01",
    "flag": "1"
  }
}
```

错误响应（抛出携带 `msg` 的 `HttpCommunicationException`）：

```json
{ "status": "fail", "msg": "system certificate check failed" }
```

已知限制：

| 限制                                                                                          | 影响                       |
| :-------------------------------------------------------------------------------------------- | :------------------------- |
| `UniauthSignatureProfileDefinition.extractUserProfile(payload, signature)` 解析后返回 `null`（代码目前以 `return null` 结尾） | Signature 资料不会继续传递 |
| `UniauthSignatureProfileDefinition` 依赖 fastjson 解析                                         | 载荷须为 fastjson 可解析的 JSON |

## 9. 测试与构建

```bash
./mvnw clean verify
```

- 仓库内置 Maven wrapper（`mvnw`）。
- 已配置 JaCoCo 行覆盖率 90% 门禁（`haltOnFailure=false`）。
- 目前没有单元测试——仅有占位类 `TokenExample`，覆盖率门禁在本分支实际上未被有效执行（已知缺口）。

## 10. 版本线与分支

| 分支           | JDK  | 版本模式   | 维护说明                          |
| :------------- | :--- | :--------- | :-------------------------------- |
| `feature/1.0.x` | 8    | `1.0.x.*`  | 当前分支                          |
| `feature/2.0.x` | 17   | `2.0.x.*`  | JDK 17 版本线                     |
| `feature/3.0.x` | 21   | `3.0.x.*`  | JDK 21 版本线                     |

制品通过阿里云 Maven 私服与 GitHub Releases 分发。请按 JDK 基线选择对应分支。

## 11. 贡献与许可

欢迎贡献——尤其是单元测试以及 Signature 资料解析问题的修复。较大改动请先提交 issue 讨论。

本项目基于 [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0) 许可。
