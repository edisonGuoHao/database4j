# database4j
这是一个完全基于内存的数据库：
1. 不适用任何第三方数据库。
2. 支持基础增删改查功能。
3. 支持多线程读写操作。
4. 支持简单的模糊查询功能。
5. 支持多张表，多个字段，多组数据存储。

但是任然有很多的不足：
1. 查询效率的问题，并没有使用特殊的数据结构，查询效率比较慢（不快）。
2. 只能存储String类型的数据。
3. 不建议存储大型数据库。
4. 没有持久化功能。
