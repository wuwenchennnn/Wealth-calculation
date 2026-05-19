# 人生财富宿命测算后端

## 环境要求

- JDK 17
- Maven 3.8+
- MySQL 8.0

## 初始化数据库

在 MySQL 中执行：

```sql
source sql/schema.sql;
```

或复制 `sql/schema.sql` 内容到数据库客户端执行。

## 配置说明

项目现在使用两个后端配置文件：

- `src/main/resources/application.yml`：正式发版/GitHub 版本，敏感项使用环境变量。
- `src/main/resources/application-dev.yml`：本地开发版本，可以直接写本地数据库密码、DeepSeek Key、微信配置等明文信息。

`application-dev.yml` 已加入 `.gitignore`，不要提交到 GitHub。

`application.yml` 中使用环境变量的敏感项：

- `DB_URL`：数据库连接地址
- `DB_USERNAME`：数据库账号
- `DB_PASSWORD`：数据库密码
- `DEEPSEEK_API_KEY`：DeepSeek API 密钥
- `WECHAT_APP_ID`：微信小程序 AppID
- `WECHAT_APP_SECRET`：微信小程序密钥

本地使用 dev 配置启动：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

正式环境使用默认配置，并通过环境变量注入敏感项。

## 启动

```bash
mvn spring-boot:run
```

默认地址：`http://localhost:8080`

## 接口

### 城市生活成本

`GET /api/city/cost?city=北京`

### AI测算

`POST /api/calculate`

```json
{
  "openid": "mock_openid",
  "city": "北京",
  "age": 25,
  "salary": 12000,
  "money": 50000,
  "consume": "普通型",
  "workType": "稳定上班"
}
```

### 广告解锁

`POST /api/calculate/unlock`

```json
{
  "recordId": 1,
  "openid": "mock_openid",
  "adReceipt": "reward_xxx"
}
```

### 历史记录

`GET /api/calculate/records?openid=mock_openid`

## 合规说明

报告固定标注娱乐参考，不提供金融、投资、理财、迷信、命理建议。前端不保存任何 AI 密钥。
