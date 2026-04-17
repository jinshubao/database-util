---
name: 表格复制数据格式扩展
description: 未来需支持多种复制数据格式（CSV、Tab分隔、SQL语句等），当前统一为CSV格式
type: project
---

当前所有表格（数据表和查询结果）的复制功能统一通过 Ctrl+C 调用 `TableCellFactory.copySelectedCellsAsCsv()`，输出 CSV 格式。

未来需要支持更多复制格式，例如：
- CSV 格式（当前默认）
- Tab 制表符分隔（粘贴到 Excel 自动分列）
- SQL INSERT 语句
- SQL UPDATE 语句
- JSON 格式

**Why:** 用户在不同场景下需要不同的数据格式。例如贴到 Excel 时 Tab 分隔比分列更自然；生成测试数据时需要 INSERT 语句。

**How to apply:** 当后续用户提到"复制格式"、"导出"、"INSERT语句"等需求时，应在此记忆基础上扩展复制功能，而非从零设计。
