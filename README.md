# Data Structures Assignment 2: Graph Implementations

这是我在数据结构课程中完成的 Assignment 2。项目实现并测试了三种无向图表示：Edge List、Adjacency List 和 Adjacency Matrix。

## 项目内容

- `graph.impl.EdgeListGraph`：使用全局边表保存图。
- `graph.impl.AdjacencyListGraph`：每个顶点维护自己的关联边表。
- `graph.impl.AdjacencyMatrixGraph`：使用可扩容的邻接矩阵，并同步维护顶点和边集合。
- `EdgeListTest.java`、`AdjacencyListTest.java`、`AdjacencyMatrixTest.java`：覆盖插入、删除、替换、邻接查询、端点查询、关联边遍历和矩阵扩容。

## 运行测试

需要 JDK 8 或更高版本：

```bash
./run_tests.sh
```

脚本会将所有 Java 源文件编译到临时目录，并运行三个测试程序；任何测试输出 `incorrect` 时脚本都会失败。

也可以手动运行：

```bash
BUILD_DIR="$(mktemp -d)"
find src -name '*.java' -print0 | xargs -0 javac -d "$BUILD_DIR"
java -cp "$BUILD_DIR" EdgeListTest
java -cp "$BUILD_DIR" AdjacencyListTest
java -cp "$BUILD_DIR" AdjacencyMatrixTest
```

`Assignment-2.pdf` 是课程作业说明。编译生成的 `.class` 文件、IDE 配置和重复的 zip 文件不纳入仓库。
