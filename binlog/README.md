### 创建数据库
pingBack
```sql
CREATE TABLE `test`.`pingBack`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `commenttime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
```
kafkapatient
```sql
CREATE TABLE `test`.`kafkapatient`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `commenttime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
```
### 启动kafka创建topic
启动zookeeper
/usr/local/Cellar/kafka/2.2.1/bin/zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties &
启动kafka
/usr/local/Cellar/kafka/2.2.1/bin/kafka-server-start /usr/local/etc/kafka/server.properties &
创建topic
./bin/kafka-run-class.sh  kafka.admin.TopicCommand --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic mysql-kafka-pingBack
### kafka监听binlog
新建source/sink配置文件，并放置在kafka config目录下
vim quickstart-mysql.properties
```text
name=mysql-b-source-pingBack
connector.class=io.confluent.connect.jdbc.JdbcSourceConnector
tasks.max=1
connection.url=jdbc:mysql://localhost:3306/test?user=root&password=root
# timestamp+incrementing 时间戳自增混合模式
mode=timestamp+incrementing
# 时间戳 commenttime
timestamp.column.name=commenttime
# 自增字段  id
incrementing.column.name=id
# 白名单表  pingBack
table.whitelist=pingBack
# topic前缀   mysql-kafka-
topic.prefix=mysql-kafka-
```
vim quickstart-mysql-sink.properties
```text
name=mysql-b-source-pingBack
connector.class=io.confluent.connect.jdbc.JdbcSourceConnector
tasks.max=1
connection.url=jdbc:mysql://localhost:3306/test?user=root&password=root
# timestamp+incrementing 时间戳自增混合模式
mode=timestamp+incrementing
# 自增字段  id
timestamp.column.name=commenttime
incrementing.column.name=id
# 白名单表  pingBack
table.whitelist=pingBack
# topic前缀   mysql-kafka-
topic.prefix=mysql-kafka-
```
启动
./bin/connect-standalone  /usr/local/etc/kafka/connect-standalone.properties  ./config/source/sink/quickstart-mysql.properties  ./config/source/sink/quickstart-mysql-sink.properties
由于端口冲突，可以修改/usr/local/etc/kafka/connect-standalone.properties
最后一行添加：
rest.port=8003
需要下载 mysql-connector-java-5.1.46.jar

### 创建消费者，从kafka拉取信息

### [官方文档](https://docs.confluent.io/2.0.0/connect/connect-jdbc/docs/index.html#examples)