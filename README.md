# CertForge

CertForge 是一个运行在 localhost 的本地认证刷题系统。当前内置题库为 **AWS Certified Generative AI Developer - Professional（AIP-C01）**，使用 Spring Boot + Thymeleaf + H2 file database 构建，不需要单独启动 Node.js 前端。

## 项目简介

题库 Markdown 是 source of truth。应用启动时读取 `src/main/resources/question-bank/AWS_AIP-C01_bilingual.md`，解析中英文题干、选项、单选/多选答案、社区投票与 `Most Voted` 标记；用户的星标与答题记录另存于本地 H2 数据库。

根目录的 `AWS_AIP-C01_中文版.md` 现在是完整中英双语版；网页顶部可以自由切换中文和 English，切换会同步影响题干、选项、Topic、页面标签和操作按钮。AWS 技术名称按原文保留。

## 功能

- Dashboard：总题数、已答、当前正确/错误、正确率、星标、剩余题数
- Study Mode / 背题模式：答案和社区投票直接可见，支持全文搜索、Topic、题型、星标、错题筛选
- Review Mode / 即时答题：检查前隐藏答案，提交后 server-side 严格判分并立即反馈；首次提交锁定，重试会创建新 attempt
- Exam Mode / 模拟考试：随机抽题、保存并跳题、题号导航、标记复查、统一提交、逐题复盘
- Wrong Answers / 错题：保留历史错误，显示错误次数、最后选择、最近答题时间和本地 mastery heuristic
- Starred / 星标：状态持久化到 H2
- Language switch / 语言切换：中英文会话级切换，支持当前页面直接切换
- Light/Dark mode、响应式布局、清晰 focus 状态和 Review 键盘快捷键

## Screenshot Placeholder

运行应用后可在 `/`、`/study`、`/review` 和 `/exam` 查看界面。

## 技术栈

- Java 21
- Spring Boot 4.1.1、Spring MVC、Thymeleaf
- Spring Data JPA、H2 file database
- HTML、CSS、Vanilla JavaScript
- Maven、JUnit 5、Spring MVC Test

## 目录结构

```text
src/main/java/com/certforge
├── controller   Dashboard / Study / Review / Wrong / Exam
├── domain       题目、session、attempt、progress 领域模型
├── dto          不同页面的安全视图 DTO
├── parser       Markdown parser 与显式解析失败模型
├── repository   JPA repositories
└── service      题库、选题、判分、进度、考试服务
src/main/resources
├── question-bank/AWS_AIP-C01_bilingual.md
├── messages_zh.properties / messages_en.properties
├── static/css/app.css
├── static/js/app.js
└── templates/
```

## 如何加入 Markdown 题库

默认题库路径为：

```text
src/main/resources/question-bank/AWS_AIP-C01_bilingual.md
```

双语题库使用每题两个小节：`### 中文` 与 `### English`。每个小节包含同一组 A–F 选项；答案和投票使用双语标签，例如 `Correct Answer / 正确答案`。也可以通过 `certforge.question-bank` 配置指定另一个 classpath resource。

如果上游英文题库或中文译本更新，可以运行 `pwsh -NoProfile -File scripts/merge-question-bank.ps1`，脚本会按题号重新校验并生成根目录双语文件及 classpath 资源。

应用启动日志会打印：

```text
Question bank loaded
Parsed: ..., Failed: ..., Single choice: ..., Multiple choice: ...
```

解析失败会包含题号、原因和 source fragment，不会被静默忽略。

## 如何启动

```bash
mvn test
mvn spring-boot:run
```

访问：<http://localhost:8080>

## Study Mode

背题模式直接展示当前语言的正确答案和题库已有的社区投票。搜索同时匹配中英文题干和选项文本；没有投票数据时显示 `No community vote data`，不会生成虚假比例。

## Review Mode

检查前的 `QuestionViewDto` 不包含 `correctAnswers`，浏览器页面也不会预置答案。点击 Check Answer 后由 server-side 做集合比较，并按当前语言返回 Correct/Incorrect、正确答案和社区投票。多选题要求选择集合与答案集合完全相同，例如 `ADF` 只能由 A、D、F 一起选中才正确。

Review 快捷键：`1`–`6` 选择 A–F，`Enter` 检查，`N`/右方向键下一题，`P`/左方向键上一题，`S` 收藏。焦点在输入框时不会触发快捷键。

## Exam Mode

考试提交前只返回题干和选项，服务器 session 保存选择；不会把正确答案放入客户端 JavaScript 或隐藏字段。提交后统一评分，并逐题展示 Your Answer、Correct Answer 和社区投票。

## Wrong Question Mode

`/wrong` 根据持久化历史筛选曾经答错的题目，不删除历史错误。最近连续两次答对会标记为 `MASTERED`，这是本地 heuristic，不是经过科学验证的间隔重复算法。

## 数据存储

H2 使用 file mode，默认保存到：

```text
./data/certforge
```

应用重启后星标、答题历史、错误次数和当前进度仍然保留。`data/` 以及 Maven `target/` 不提交到 Git；题库 Markdown 会提交。

## 测试

测试覆盖 parser fixture（中英文标签、单选、多选、4/6 选项、社区投票、Most Voted、缺失投票、Malformed question）、严格集合判分、JPA 持久化与 MVC/答案泄漏边界。

```bash
mvn test
```

## Future Improvements

- 支持多个 `QuestionBankMetadata` 题库和题库切换
- 为 Markdown 可选 explanation 增加安全渲染
- 更丰富的 session 历史与复习计划
- 进一步的浏览器端无障碍自动化测试

当前明确不包含 authentication、OAuth、AWS deployment、云数据库、LLM explanation、移动端、Docker、Redis、Kafka 和 multi-user 功能。
