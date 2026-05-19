# 人生财富宿命测算小程序

一个面向微信小程序场景的趣味财富测算项目。用户填写城市、年龄、收入、存款、消费档位、工作状态等信息后，系统会调用后端 AI 能力生成《人生财富宿命报告》，并支持广告解锁完整版、生成分享海报等玩法。

本项目包含两个主要工程：

- `my-v3-project`：UniApp + Vue 3 微信小程序前端
- `wealth-backend`：Spring Boot 后端服务

## 项目功能

### 小程序前端

- 首页展示项目介绍和测算入口
- 表单页填写 6 项财富画像信息
- 城市下拉选择，支持已收录城市和“其他城市”兜底
- AI 测算加载页，展示测算过程动效
- 报告页展示摘要报告和完整报告
- 激励视频广告解锁完整 6 大模块报告
- 插屏广告、Banner 广告基础接入
- Canvas 生成财富报告分享海报
- 海报支持保存到相册
- 游客模式访问，无需微信授权登录

### 后端服务

- 城市生活成本数据查询
- 已收录城市列表接口
- 未收录城市使用“其他城市”成本兜底
- DeepSeek/OpenAI-compatible AI 报告生成
- AI 调用失败时自动回退本地 mock 报告
- 用户每日测算次数限制
- 请求频率限制
- 测算记录落库
- 广告解锁完整报告
- Swagger/OpenAPI 接口文档
- 本地开发配置和正式环境配置分离

## 技术栈

### 前端

- UniApp
- Vue 3
- Vite
- uview-plus
- 微信小程序 Canvas

### 后端

- Java 17
- Spring Boot 2.7
- MyBatis-Plus
- MySQL 8
- Hutool
- FastJSON2
- Springdoc OpenAPI

## 目录结构

```text
.
├── my-v3-project/          # 微信小程序前端
│   ├── src/
│   │   ├── api/            # 接口请求
│   │   ├── components/     # 通用组件
│   │   ├── config/         # 前端配置
│   │   ├── pages/          # 小程序页面
│   │   └── utils/          # 工具方法
│   └── package.json
│
├── wealth-backend/         # Spring Boot 后端
│   ├── sql/                # 数据库初始化脚本
│   ├── src/main/java/      # Java 源码
│   ├── src/main/resources/ # 后端配置和 Mapper XML
│   └── pom.xml
│
└── README.md
```

## 本地启动

### 1. 初始化数据库

进入 MySQL 后执行：

```sql
source wealth-backend/sql/schema.sql;
```

或复制 `wealth-backend/sql/schema.sql` 内容到数据库客户端执行。

### 2. 配置后端本地开发文件

后端支持两个配置文件：

- `wealth-backend/src/main/resources/application.yml`：正式/GitHub 版本，敏感项使用环境变量
- `wealth-backend/src/main/resources/application-dev.yml`：本地开发明文版本，已加入 `.gitignore`

本地开发时，在 `application-dev.yml` 中填写自己的数据库密码、DeepSeek Key、微信 AppID/AppSecret。

### 3. 启动后端

```bash
cd wealth-backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

默认后端地址：

```text
http://localhost:8080
```

### 4. 启动前端

```bash
cd my-v3-project
npm install
npm run dev:mp-weixin
```

然后用微信开发者工具打开：

```text
my-v3-project/dist/dev/mp-weixin
```

## 环境变量与敏感配置

正式发版时，后端 `application.yml` 中以下配置通过环境变量注入：

- `DB_URL`：数据库连接地址
- `DB_USERNAME`：数据库账号
- `DB_PASSWORD`：数据库密码
- `DEEPSEEK_API_KEY`：DeepSeek API 密钥
- `WECHAT_APP_ID`：微信小程序 AppID
- `WECHAT_APP_SECRET`：微信小程序 AppSecret

前端广告位可通过 `.env.local` 配置：

```env
VITE_REWARD_AD_UNIT_ID=你的激励广告ID
VITE_INTERSTITIAL_AD_UNIT_ID=你的插屏广告ID
VITE_BANNER_AD_UNIT_ID=你的Banner广告ID
```

请不要提交以下内容到公开仓库：

- `.env`
- `.env.local`
- `application-dev.yml`
- `project.private.config.json`
- 任何真实 API Key、数据库密码、微信 AppSecret

## 主要接口

### 城市列表

```text
GET /api/city/list
```

### 城市生活成本

```text
GET /api/city/cost?city=北京
```

### AI 测算

```text
POST /api/calculate
```

请求示例：

```json
{
  "openid": "guest_xxx",
  "city": "北京",
  "age": 25,
  "salary": 12000,
  "money": 50000,
  "consume": "普通型",
  "workType": "稳定上班"
}
```

### 广告解锁完整报告

```text
POST /api/calculate/unlock
```

请求示例：

```json
{
  "recordId": 1,
  "openid": "guest_xxx",
  "adReceipt": "reward_xxx"
}
```

## 合规说明

本项目生成内容仅用于趣味娱乐展示，不构成任何金融、投资、理财建议。报告中应持续保留娱乐参考提示，避免引导用户做出真实投资或借贷决策。

# 使用规范

1. 本项目源码仅面向个人学习、技术参考、非商业改造使用。
2. **本人原作者拥有唯一商用授权权限，可自由用于商业项目、盈利开发、项目变现。**
3. 任何第三方个人/团队/企业，**严禁将本项目代码、衍生代码用于任何商业用途、盈利项目、付费售卖、接单开发、线上运营变现**。
4. 允许查看、克隆、本地修改学习，禁止对外商用分发与盈利使用。
5. 未经作者书面许可，一律视为侵权商用行为。
