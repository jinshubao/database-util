---
name: Maven JavaFX 运行流程
description: 本项目修改代码后启动的正确流程，避免加载旧 jar 的陷阱
type: project
---

修改代码后，不能直接 `cd database-gui && mvn javafx:run`，因为 `javafx-maven-plugin` 会从本地 Maven 仓库 (`~/.m2/repository`) 加载各模块的旧 jar，而不是 `target/classes` 里的最新编译结果。

**正确流程：**
1. `mvn install -DskipTests`
2. `cd database-gui && mvn javafx:run -q`

**Why:** 这是一个多模块 Maven 项目，`database-gui` 通过 `pom.xml` 中的 `<dependency>` 引用其他模块（如 `database-mysql`）。`javafx-maven-plugin` 在运行时依赖解析阶段会去本地仓库找这些 jar，不会自动读取上游模块的 `target/classes`。因此必须用 `mvn install` 把最新编译结果写入本地仓库后，启动才能加载到修改后的代码。

**How to apply:** 每次修改 `database-api`、`database-sql`、`database-mysql` 等被 `database-gui` 依赖的模块后，启动前都先执行 `mvn install -DskipTests`。
