# Database Manager

一个基于 JavaFX 17 的多数据库可视化管理工具（类似 Navicat / DBeaver）。

## 功能列表

### 状态说明

| 状态 | 说明 |
|------|------|
| ✅ 已完成 | 功能已实现并可用 |
| ⚙️ 开发中 | 部分实现，需要完善 |
| ❌ 未实现 | 尚未开始开发 |

---

## MySQL 模块 ✅ 已完成

### 连接管理
- ✅ 创建连接（支持 host/port/username/password/database）
- ✅ 保存连接配置
- ✅ 连接测试
- ✅ 关闭连接
- ✅ 删除连接

### 数据库浏览
- ✅ 树形结构展示（连接 > 数据库 > 表类型 > 表）
- ✅ 双击打开连接
- ✅ 刷新节点
- ✅ 上下文菜单

### SQL 查询
- ✅ SQL 编辑器（支持语法高亮）
- ✅ 执行 SQL（支持多语句）
- ✅ 结果集展示（表格形式）
- ✅ 执行信息展示（耗时、影响行数）
- ✅ SQL 历史记录
- ✅ EXPLAIN 执行计划
- ✅ SQL 格式化

### 表操作
- ✅ 查看表 DDL（DDL 标签页）
- ✅ 查看表数据
- ⚠️ 表数据编辑（仅查看，未实现编辑）
- ⚠️ 表结构编辑（仅查看 DDL，未实现修改）

### 缺失功能
- ❌ 表数据编辑（双击单元格编辑）
- ❌ 新增/删除表
- ❌ 修改表结构
- ❌ 索引管理
- ❌ 外键管理
- ❌ 导入/导出数据（CSV/Excel）
- ❌ SQL 智能提示（表名/列名补全）

---

## Redis 模块 ✅ 已完成

### 连接管理
- ✅ 创建连接（支持单机/集群模式）
- ✅ 连接测试
- ✅ 关闭连接
- ✅ 删除连接

### 单机模式
- ✅ 浏览所有数据库（db0, db1, ...）
- ✅ 浏览 Key 列表（支持扫描）
- ✅ 查看 Key 类型、TTL、大小
- ✅ 查看 Key Value（STRING/LIST/SET/ZSET/HASH）
- ✅ 新增 Key
- ✅ 删除 Key
- ✅ 修改 Key Value
- ✅ 清空数据库（FLUSHDB）
- ✅ 刷新 Key 列表

### 集群模式
- ⚠️ 基础框架（已创建类结构）
- ❌ 完整的集群支持

### Console
- ✅ 命令行界面
- ✅ 支持命令：PING, GET, SET, DEL, EXISTS, DBSIZE, FLUSHDB, INFO, TYPE, TTL, EXPIRE, LLEN, SCARD, HLEN, ZCOUNT

### 缺失功能
- ❌ Key 搜索/过滤（按前缀/正则）
- ❌ 批量操作 Key
- ❌ TTL 批量修改
- ❌ 连接配置持久化
- ❌ 发布/订阅功能
- ❌ 内存分析
- ❌ 慢查询日志

---

## Oracle 模块 ❌ 未实现

### 状态
- 仅有框架骨架
- ❌ 依赖配置错误（引用了 mysql-connector 而非 ojdbc）
- ❌ 连接功能未实现
- ❌ 数据库浏览未实现
- ❌ SQL 查询未实现

### 待完成
- [ ] 修复 pom.xml 依赖（添加 ojdbc）
- [ ] 实现 OracleMetadataProvider
- [ ] 实现 Oracle 连接逻辑
- [ ] 实现树形浏览
- [ ] 实现 SQL 查询
- [ ] 支持 PL/SQL

---

## MongoDB 模块 ❌ 未实现

### 状态
- 仅有占位类
- ❌ 硬编码连接（127.0.0.1:21017）
- ❌ 无连接配置界面
- ❌ 无功能实现

### 待完成
- [ ] 实现连接配置
- [ ] 实现数据库/集合浏览
- [ ] 实现文档查看（JSON 格式）
- [ ] 实现 CRUD 操作
- [ ] 实现索引管理
- [ ] 实现聚合管道

---

## 公共功能（database-api / database-gui）

### 已实现
- ✅ 多数据库模块支持（SPI 插件机制）
- ✅ 树形结构展示
- ✅ Tab 页面管理
- ✅ FXML 视图加载
- ✅ 异步任务执行
- ✅ 错误提示对话框
- ✅ 资源图标管理

### 待完成
- ❌ 连接配置持久化（保存到文件）
- ❌ 连接导入/导出
- ❌ 深色主题
- ❌ 快捷键支持
- ❌ SQL 查询历史保存
- ❌ 书签功能

---

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 17 | 编程语言 |
| JavaFX 17 | GUI 框架 |
| Maven | 项目管理 |
| AtlantaFX 2.0.0 | JavaFX 主题 |
| RichTextFX | SQL 编辑器语法高亮 |
| Druid 1.2.27 | 数据库连接池 |
| Lettuce 7.2.0 | Redis 客户端 |
| SLF4J + Log4j2 | 日志框架 |
| SPI | 插件机制 |

---

## 项目结构

```
database-mgr-maven/
├── database-api/       # 核心接口和抽象
├── database-gui/        # GUI 入口
├── database-sql/       # SQL 公共抽象层
├── database-mysql/      # MySQL 实现
├── database-oracle/     # Oracle 实现
├── database-mongo/      # MongoDB 实现
└── database-redis/      # Redis 实现
```

---

## 启动方式

```bash
cd database-gui
mvn compile javafx:run -am
```

`-am` 参数确保依赖模块先被编译。

---

## 插件机制

各数据库模块通过 Java SPI 机制注册到系统中。在 `src/main/resources/META-INF/services/` 目录下创建 `com.jean.database.api.IDatabaseProvider` 文件，填入实现类的全限定名即可。

---

## 开发说明

### 添加新的数据库模块

1. 创建新模块，依赖 `database-api`
2. 实现 `IDatabaseProvider` 接口
3. 在 `src/main/resources/META-INF/services/com.jean.database.api.IDatabaseProvider` 中注册
4. `database-gui` 会自动发现并加载

### 异步任务

所有可能阻塞 UI 的操作都应使用 `BaseTask` + `TaskManger.execute()`：

```java
TaskManger.execute(new BaseTask<Result>() {
    @Override
    protected Result call() throws Exception {
        // 后台执行
        return result;
    }

    @Override
    protected void succeeded() {
        // 在 FX Application Thread 更新 UI
    }
});
```

---

## 许可证

MIT License
