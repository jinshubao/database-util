# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run

This is a multi-module Maven project using **Java 17** and **JavaFX 17**.

- **Compile all modules:** `mvn compile`
- **Run the application:** `cd database-gui && mvn compile javafx:run -am`
- **Package:** `mvn package`
- **Tests:** No test framework is currently configured. Test directories exist but are empty.

The application entry point is `com.jean.database.MainApplication` in the `database-gui` module.

## Technology Stack

- Java 17, JavaFX 17 (controls + fxml)
- Maven
- AtlantaFX 2.0.0 (JavaFX theme, `PrimerLight`)
- RichTextFX (SQL editor syntax highlighting)
- Druid 1.2.27 (connection pool, MySQL module)
- Lettuce 7.2.0 (Redis client)
- SLF4J + Log4j2 (logging)

## Module Architecture

| Module | Role |
|--------|------|
| `database-api` | Core interfaces, base classes, utilities, and the task/thread pool system |
| `database-gui` | JavaFX application entry point and main controller |
| `database-sql` | JDBC abstraction layer (metadata, connection config, base tree items) |
| `database-mysql` | MySQL implementation (most complete) |
| `database-redis` | Redis implementation (single-node functional, cluster skeleton) |
| `database-oracle` | Skeleton only — UI present but backend commented out |
| `database-mongo` | Skeleton only — hardcoded connection, no real features |

The project does **not** use JPMS (`module-info.java`). It runs on the traditional classpath.

## SPI Plugin Mechanism

Database modules are discovered at runtime via Java's `ServiceLoader`:

1. Each provider module registers itself in `src/main/resources/META-INF/services/com.jean.database.api.IDatabaseProvider` with its fully-qualified `IDatabaseProvider` implementation class.
2. `database-gui` uses `ServiceLoader.load(IDatabaseProvider.class)` to discover providers, sorts them by `getOrder()`, injects a `ViewContext`, and calls `init()`.
3. The GUI module explicitly depends on all provider modules in its `pom.xml` so they are on the classpath.

Provider order constants:
- MySQL: 10000
- Oracle: 20000
- Redis: 30000
- MongoDB: 40000

## Key Cross-Module Patterns

### Async Tasks (Mandatory for I/O)

All blocking operations must run on the background thread pool via `TaskManger.execute()`:

```java
TaskManger.execute(new BaseTask<Result>() {
    @Override
    protected Result call() throws Exception {
        // blocking work here
    }
    @Override
    protected void succeeded() {
        // update JavaFX UI here (guaranteed FX thread)
    }
});
```

`BaseTask` auto-generates a UUID, sets lifecycle messages ("开始执行", "执行成功", etc.), and automatically shows an error dialog on failure. `TaskManger` is a singleton `ThreadPoolExecutor` with daemon threads.

### Tree Items and Lazy Loading

All tree nodes extend `BaseTreeItem<T>` (or `BaseDatabaseItem<T>` for SQL). They implement `IContextMenu`, `IMouseAction`, `IRefreshable`, and `ICloseable`. Children are **not** loaded in constructors; double-clicking or selecting "Open" from the context menu triggers a `BaseTask` that fetches metadata and populates children asynchronously.

### Controller Context Injection

FXML controllers receive data through `ControllerContext` (a typed attribute map built with a builder pattern), injected via `FxmlControllerFactory.load()`. Controllers typically extend `DefaultController`.

### Resource Bundles

`FxmlUtils` loads resource bundles with `EncodingResourceBundleControl` to force UTF-8. Default locale is `Locale.SIMPLIFIED_CHINESE`.

## Adding a New Database Module

1. Create a new Maven module with `database-api` as a dependency.
2. Implement `IDatabaseProvider` (extend `DefaultDatabaseProvider` for convenience). Set a unique `getOrder()` value.
3. Register the provider class in `META-INF/services/com.jean.database.api.IDatabaseProvider`.
4. Add the new module as a compile-scope dependency in `database-gui/pom.xml`.
5. Implement tree items extending `BaseTreeItem<T>` and use `BaseTask` + `TaskManger.execute()` for all I/O.
