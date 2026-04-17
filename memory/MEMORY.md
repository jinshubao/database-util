# 项目长期记忆

## 项目概要

**database-util** 是一个多数据库可视化管理工具（类似 Navicat/DBeaver），基于 JavaFX 17 + Maven 多模块架构。

## 技术栈

- Java 17 + JavaFX 17 + FXML
- UI 主题：AtlantaFX 2.0.0（PrimerLight）
- SQL 编辑器：RichTextFX（CodeArea + 语法高亮）
- 连接池：Alibaba Druid 1.2.27
- MySQL 驱动：mysql-connector-java 8.0.33
- MongoDB：mongo-java-driver 3.12.14
- Redis：Lettuce 7.2.0 + commons-pool2 2.13.0
- 日志：SLF4J + Log4j2 2.17.1
- 插件机制：Java SPI（ServiceLoader）

## 模块结构

```
database-mgr-maven（父 POM）
├── database-api      ← 核心接口/抽象层
├── database-gui      ← GUI 入口，启动类 MainApplication
├── database-sql      ← SQL 公共抽象层（JDBC 元数据）
├── database-mysql    ← MySQL 实现（最完整）
├── database-oracle   ← Oracle 框架（未完成）
├── database-mongo    ← MongoDB（最初级）
└── database-redis    ← Redis 单机+集群
```

## 核心接口

- `IDatabaseProvider`：Provider 注册接口（SPI）
- `IConnectionConfiguration`：连接配置接口
- `ViewContext`：主界面操作门面
- `BaseTreeItem<T>`：树节点基类（含 IContextMenu/IMouseAction/IRefreshable/ICloseable）
- `SQLMetadataProvider`：JDBC 元数据抽象类
- `BaseTask<V>`：异步任务基类，统一使用 TaskManger.execute() 执行

## 架构特点（SPI 插件机制）

1. 每个数据库模块在 `src/main/resources/META-INF/services/com.jean.database.api.IDatabaseProvider` 注册
2. `database-gui` 依赖所有数据库模块，运行时通过 `ServiceLoader` 自动发现
3. `ProviderManager` 负责加载和管理所有 Provider
4. 启动时 `MainController` 遍历所有 Provider 并调用 `init()`

## 启动命令


[Maven JavaFX 运行流程](maven_javafx_workflow.md) — 修改代码后必须先 install 再启动，否则加载的是旧 jar

```bash
cd e:/my-workspace/database-util/database-gui
mvn compile javafx:run -am
```

`-am` 参数确保依赖模块（database-api, database-sql, database-mysql 等）先被编译。

## 模块成熟度

| 模块 | 成熟度 |
|------|--------|
| database-api | 完整 |
| database-gui | 完整 |
| database-sql | 完整 |
| database-mysql | 最完整（连接/浏览/查询/DDL） |
| database-redis | 较完整（单机+集群） |
| database-oracle | 框架骨架，未完成 |
| database-mongo | 最初级，仅占位 |

## 已知问题

- database-oracle 的 pom.xml 错误引用了 mysql-connector-java 而非 ojdbc
- database-oracle 的 init() 核心逻辑被注释，未实现
- database-mongo 功能几乎为空，硬编码了 127.0.0.1:21017
- MySQLMetadataProvider.getTableDDL 中有 `System.out.println` 调试代码未清理

- [表格复制数据格式扩展](copy_format_future.md) — 当前统一CSV，未来需支持Tab/SQL INSERT/UPDATE/JSON等

## 已修复的 Bug（2026-04-08）

### 1. 右侧 TabPane（"通用"和"DDL"）不显示
**根因**：`MySQLObjectTabController.java` 中 FXML 注入字段（`root`、`splitPane` 等）缺少 `@FXML` 注解，导致注入失败为 null，触发 NullPointerException。

**修复**：为 `MySQLObjectTabController` 中所有 FXML 注入字段添加 `@FXML` 注解，并添加 `import javafx.fxml.FXML`。

**涉及文件**：`database-mysql/src/main/java/com/jean/database/mysql/MySQLObjectTabController.java`

### 2. 执行 SQL 时 `Not on FX application thread` 异常
**根因**：`MySQLQueryTabController.executeSql()` 在后台线程执行 SQL，但 `processResultSet()` 和其他 UI 操作直接修改 TabPane、TextArea 等 JavaFX 组件。

**修复**：重构为使用 `BaseTask` + `TaskManger.execute()` 模式，统一异步调用：
- `ExecuteSqlTask` 继承 `BaseTask<ExecuteSqlResult>`
- `call()` 在后台线程执行 SQL
- `succeeded()` 在 FX Application Thread 更新 UI

**涉及文件**：`database-mysql/src/main/java/com/jean/database/mysql/MySQLQueryTabController.java`

### 3. Maven 多模块编译后代码不生效
**根因**：`javafx:run` 从本地 Maven 仓库读取 classpath，修改代码后需要先 install。

**修复**：使用 `-am` 参数（also-make），Maven 会先编译所有依赖模块：
```bash
mvn compile javafx:run -am
```

## 代码规范

1. **异步任务**：统一继承 `BaseTask`，通过 `TaskManger.execute(task)` 执行
2. **UI 更新**：在 `BaseTask.succeeded()` 方法中进行，JavaFX 自动在 Application Thread 执行
3. **错误处理**：`BaseTask.failed()` 方法统一处理，展示错误对话框


