# CertForge

CertForge 是一个运行在 localhost 的本地 SRE 学习系统，使用 Spring Boot、Thymeleaf 和 H2 file database 构建，不需要单独启动 Node.js 前端。

当前题库是 Pushpay Senior SRE 基础知识 Study Edition：140 个主题，每个主题 5 道简答题，共 700 道题。源文件是 `src/main/resources/question-bank/SRE_foundations_140x5.md`，启动时由 Markdown parser 读取；根目录的 `04_ZH_QA_study_edition_140x5_700.md` 是同一份中文源材料的工作副本。

## 功能

- Dashboard：题库规模、学习进度、星标和本地数据管理
- Study Mode / 背题模式：浏览题目并直接查看参考答案，支持搜索、主题、简答题、星标和错题筛选
- Review Mode / 即时答题：先自行作答，再展开参考答案；简答题自检不会伪造正确率或答题记录
- Wrong Answers / 错题：保留有评分的即时练习错误历史
- Starred / 星标：状态持久化到 H2
- Language switch / 语言切换：中英文页面界面切换（SRE 题库内容为中文）
- Light/Dark mode、响应式布局和 Review 键盘快捷键

原 AWS AIP Exam Mode 已下线。旧的 `/exam` 书签和 POST 端点会无状态重定向到 `/study`；应用不再创建考试 session 或考试成绩。

## 技术栈

- Java 21
- Spring Boot、Spring MVC、Thymeleaf
- Spring Data JPA、H2 file database
- HTML、CSS、Vanilla JavaScript
- Maven、JUnit 5、Spring MVC Test

## 目录结构

```text
src/main/java/com/certforge
├── controller   Dashboard / Study / Review / Wrong / retired Exam redirect
├── domain       题目、session、attempt、progress 领域模型
├── dto          页面视图 DTO
├── parser       Markdown parser 与显式解析失败模型
├── repository   JPA repositories
└── service      题库、选题、判分、进度和数据导出服务
src/main/resources
├── question-bank/SRE_foundations_140x5.md
├── messages_zh.properties / messages_en.properties
├── static/css/app.css
├── static/js/app.js
└── templates/
```

## 如何启动

```bash
mvn test
mvn spring-boot:run
```

访问 <http://localhost:8080>。可用页面是 `/`、`/study`、`/review`、`/wrong` 和 `/starred`。

## Markdown 题库格式

SRE 源文件按主题和题号组织：

```markdown
# 01 — Git 内部原理
## Q1 — .git 目录
### Q1.1 — 基础定义
**问题：** ...
**参考答案：**
...
```

Parser 会为每个 `### Qx.y` 生成一个短答题，并检查题干、参考答案和完整题数。应用启动日志会打印解析成功数和失败数；任何失败都包含题号、原因和 source fragment，不会被静默忽略。

## Review 自检规则

简答题点击“查看参考答案”后显示 Markdown 参考答案，不进行自动文本匹配，也不写入正确/错误 attempt。这样 Dashboard 的正确率只代表有明确选项答案的练习记录。

## 数据存储

H2 默认保存到：

```text
./data/certforge
```

星标、答题历史和进度会在重启后保留。`data/` 和 Maven `target/` 不提交到 Git。

## 测试

```bash
mvn test
```

测试覆盖 choice Markdown 兼容解析、SRE 短答题解析、严格选择题判分、MVC 页面边界、Exam 下线重定向和 CSV 导入导出。
