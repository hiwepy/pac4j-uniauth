# pac4j-uniauth

[English](./README.md) | [简体中文](./README.zh-CN.md)

![Java](https://img.shields.io/badge/Java-8-orange)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

## Table of Contents

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

---

## 1. Project Overview

**pac4j-uniauth** is a [pac4j](https://www.pac4j.org) 5.0.x extension that integrates campus unified identity
authentication ("Uniauth") centers into the pac4j security flow. It is built on top of
`io.github.easy4j:pac4j-biz`, which provides the generic `Token` / `Signature` credential, client and profile
machinery.

Two authentication modes are supported:

| Mode                                  | Flow                                                                 |
| :------------------------------------ | :------------------------------------------------------------------- |
| Token mode (`UniauthToken*`)          | The identity center returns a user token; the library exchanges it for the user profile via a profile URL |
| Signature mode (`UniauthSignature*`)  | The identity center signs the user data; the library validates the signature and parses the payload      |

The resulting profile carries the unified person identity fields used by campus systems:
`userid` (user id), `pid` (unified person number, used as the profile id), `xm` (name),
`ptype` (person type: student/staff), `csrq` (date of birth) and `flag` (0 = password not initialized,
1 = initialized).

Typical scenarios:

| Scenario                          | Description                                                     |
| :-------------------------------- | :-------------------------------------------------------------- |
| SSO into campus portal            | One identity center, many business applications                 |
| OAuth-style token exchange        | Token from the auth center → profile via `profileUrl`           |
| Signed user data ingestion        | Signature-carrying user payload validated before use            |

## 2. Features & Status

| Capability                                        | Status           | Notes                                                                              |
| :------------------------------------------------ | :--------------- | :--------------------------------------------------------------------------------- |
| Token credential type (`UniauthToken`)            | Implemented      | Wraps the raw token string                                                         |
| Token authenticator (`UniauthTokenAuthenticator`) | Implemented      | Requires `profileUrl`; exchanges token for the user profile                        |
| Token profile definition (`UniauthTokenProfileDefinition`) | Implemented    | Parses `{"status":"fail"}` errors and the `pinfo` payload into `UniauthTokenProfile` |
| Token client (`UniauthTokenClient`)               | Implemented      | Inherits parameter-name / login-URL wiring from `TokenClient` (pac4j-biz)          |
| Signature credential/authenticator/client         | Partially        | `UniauthSignatureAuthenticator`/`UniauthSignatureClient` exist                      |
| Signature profile extraction                      | Known limitation | `UniauthSignatureProfileDefinition.extractUserProfile(...)` currently returns `null` after parsing (see Section 8) |
| Unit tests                                        | Not present      | Only a placeholder `TokenExample` in `src/test`                                    |

## 3. Requirements & Compatibility

| Requirement | Version                 |
| :---------- | :---------------------- |
| JDK         | 8+                      |
| Maven       | 3.0+ (wrapper included) |
| pac4j       | 5.0.x                   |
| pac4j-biz   | `1.0.x.20260630-SNAPSHOT` |

Version lines of the easy4j project:

| Branch        | JDK  | Version pattern | Notes                       |
| :------------ | :--- | :-------------- | :-------------------------- |
| `feature/1.0.x` | 8    | `1.0.x.*`       | This README, current branch |
| `feature/2.0.x` | 17   | `2.0.x.*`       | JDK 17 line                 |
| `feature/3.0.x` | 21   | `3.0.x.*`       | JDK 21 line                 |

## 4. Architecture & Modules

```text
 auth-center redirect / verify_request
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

Single-module Maven project (`jar` packaging) with two class families:

| Family              | Types                                                                 |
| :------------------ | :-------------------------------------------------------------------- |
| Token mode          | `UniauthToken`, `UniauthTokenAuthenticator`, `UniauthTokenClient`, `UniauthTokenProfile`, `UniauthTokenProfileDefinition` |
| Signature mode      | `UniauthSignature`, `UniauthSignatureAuthenticator`, `UniauthSignatureClient`, `UniauthSignatureProfile`, `UniauthSignatureProfileDefinition` |

## 5. Installation

Artifacts are published to the aliyun repository and GitHub Releases; they are **not** on Maven Central yet.

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>pac4j-uniauth</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

```groovy
implementation 'io.github.easy4j:pac4j-uniauth:1.0.x.20260630-SNAPSHOT'
```

## 6. Quick Start

Token mode (typical):

```java
// 1) Authenticator: exchange the token for the user profile at this URL
UniauthTokenAuthenticator authenticator =
        new UniauthTokenAuthenticator("https://auth.example.edu.cn/oauth2/profile");

// 2) Client: read the token from the "verify_request" request parameter
UniauthTokenClient client = new UniauthTokenClient("verify_request", authenticator);
client.setName("uniauth");
client.setLoginUrl("https://auth.example.edu.cn/oauth/authorize");
```

Expected result: after the identity center redirects back with `verify_request`, the authenticator fetches the
profile JSON; when the response contains `"status": "fail"` an `HttpCommunicationException` carries the
`msg` from the auth center; otherwise the `pinfo` object is parsed into `UniauthTokenProfile` and
`profile.getId()` returns `pid`.

Signature mode (typical):

```java
UniauthSignatureAuthenticator signatureAuthenticator = new UniauthSignatureAuthenticator();
UniauthSignatureClient signatureClient = new UniauthSignatureClient("signature", signatureAuthenticator);
```

## 7. Configuration

There is no properties file; the extension is configured programmatically:

| Setting                | How                                                    | Default          |
| :--------------------- | :----------------------------------------------------- | :--------------- |
| Profile URL            | `UniauthTokenAuthenticator(String profileUrl)`         | — (required)     |
| Request parameter name | `TokenClient(String parameterName, ...)` / `SignatureClient(String signatureParamName, ...)` | — |
| Login URL              | inherited `setLoginUrl(String)`                        | —                |
| GET/POST support       | inherited `setSupportGetRequest(boolean)` / `setSupportPostRequest(boolean)` | pac4j defaults |

## 8. Core Usage / API

Profile JSON contract (as implemented in `UniauthTokenProfileDefinition.extractUserProfile`):

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

Error response (`HttpCommunicationException` is thrown with `msg`):

```json
{ "status": "fail", "msg": "system certificate check failed" }
```

Known limitations:

| Limitation                                                                              | Impact                                   |
| :-------------------------------------------------------------------------------------- | :--------------------------------------- |
| `UniauthSignatureProfileDefinition.extractUserProfile(payload, signature)` returns `null` after parsing (code currently ends with `return null`) | Signature profile is not propagated      |
| `UniauthSignatureProfileDefinition` depends on fastjson for parsing                     | Payload must be fastjson-compatible JSON |

## 9. Testing & Build

```bash
./mvnw clean verify
```

- Maven wrapper (`mvnw`) is committed to the repository.
- JaCoCo is configured with a line-coverage rule of 90% (`haltOnFailure=false`).
- No unit tests exist yet — only the placeholder `TokenExample`; the coverage gate is therefore not
  effectively enforced on this branch (known gap).

## 10. Versioning & Branches

| Branch        | JDK  | Version pattern | Maintenance                          |
| :------------ | :--- | :-------------- | :----------------------------------- |
| `feature/1.0.x` | 8    | `1.0.x.*`       | Current branch                       |
| `feature/2.0.x` | 17   | `2.0.x.*`       | JDK 17 line                          |
| `feature/3.0.x` | 21   | `3.0.x.*`       | JDK 21 line                          |

Artifacts are distributed via the aliyun Maven repository and GitHub Releases. Use the branch matching your
JDK baseline.

## 11. Contributing & License

Contributions are welcome — especially unit tests and fixes for the signature-profile extraction issue.
Please open an issue before larger changes.

This project is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
