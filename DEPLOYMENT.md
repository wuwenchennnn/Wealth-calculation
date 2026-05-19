# 人生财富宿命测算项目说明

本仓库新增两个独立子项目，原微信云开发示例保留不动：

- `wealth-backend`：SpringBoot 2.7.x + JDK 17 + MySQL 8 + MyBatis-Plus 后端
- `uniapp-wealth-app`：UniApp + Vue3 微信小程序前端

## 启动顺序

1. 执行 `wealth-backend/sql/schema.sql` 初始化 MySQL。
2. 修改 `wealth-backend/src/main/resources/application.yml` 中数据库和 AI 配置。
3. 在 `wealth-backend` 目录运行 `mvn spring-boot:run`。
4. 修改 `uniapp-wealth-app/src/config/index.js` 中 `API_BASE_URL`。
5. 在 `uniapp-wealth-app` 目录运行 `npm install` 和 `npm run dev:mp-weixin`。
6. 使用微信开发者工具导入 `dist/dev/mp-weixin` 测试。

## 合规定位

产品为青年趣味娱乐测算，不提供金融投资建议，不涉及封建迷信、风水、鬼神、命理等表达。
