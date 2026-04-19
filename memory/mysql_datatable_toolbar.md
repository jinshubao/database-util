---
name: MySQL 数据表格已有工具栏
description: MySQL 数据表格（MySQLDataTableTab）已内置工具栏，包含新增/删除/刷新/提交按钮，无需重复添加
type: project
---

`MySQLDataTableTab.java`（`database-mysql` 模块）已内置完整的工具栏功能，包含四个按钮：

- **⟳ 刷新** — 重新加载当前页数据，有未提交改动时会弹窗确认
- **+ 新增** — 在表格末尾添加一条空行，标记为 addedRows
- **- 删除** — 删除选中的行（支持多选），标记为 deletedRows；未选中时自动禁用
- **↑ 提交** — 将 added/deleted/modified 的改动生成 INSERT/UPDATE/DELETE SQL 并提交事务

**Why:** 用户曾提出"给数据表格上方增加一个工具栏，包含新增和删除按钮"，但实际上该功能已在 `MySQLDataTableTab:67-82` 实现。新增行、删除行、分页加载、脏数据追踪、批量提交等完整 CRUD 功能都已具备。

**How to apply:** 当用户再次提到"给表格加工具栏/新增/删除按钮"时，应首先确认是指 MySQL 数据表格（已有）还是其他模块（Redis/Oracle/MongoDB 的表格目前没有类似工具栏）。避免在已有功能上重复开发。
