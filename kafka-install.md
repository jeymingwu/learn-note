# Kafka 安装与配置

## 1. JDK 的安装与配置

1.1. 下载 JDK 1.8 安装包；示例： jdk-8u181-linux-x64.tar.gz

1.2. 解压安装包；示例：tar zxvf jdk-8u181-linux-x64.tar.gz (解压后生成 jdk1.8.0_181 文件夹)

1.3  环境配置：
+  修改/etc/profile文件；
+  修改完成后执行： source /etc/profile 命令使配置生效；
+  通过 java –version 命令： 验证 JDK 是否已经安装配置成功
    
```
export JAVA_HOME=/opt/jdk1.8.0_181
export JRE_HOME=$JAVA_HOME/jre
export PATH=$PATH:$JAVA_HOME/bin
export CLASSPATH=./://$JAVA_HOME/lib:$JRE_HOME/lib
```    

## 2. ZooKeeper 的安装与配置

2.1  ZooKeeper：Kafka 集群的必要组件；实施对元数据信息的管理（包括集群、broker、主题、分区等内容）；

2.2  ZooKeeper：开源分布式协调服务；分布式应用程序可以基于 ZooKeeper 实现诸如数据发布\订阅、负载均衡、命名服务、分布式协调\通知、集群管理、Master 选举、 配置维护等功能；

2.3  ZooKeeper： 三个角色：leader、follower 和 observer；   
+  observer： 不参与投票；
+  默认情况下只有 leader 和 follower ；
    
2.4 安装步骤：
+  下载安装包；示例：zookeeper-3.4.12.tar.gz
+  解压安装包；
+  环境配置：
    +  修改/etc/profile文件；
    +  修改完成后执行： source /etc/profile 命令使配置生效；
    +  修改 ZooKeeper 的配置文件：进入$ZOOKEEPER_HOME/conf 目录，并将 zoo_sample.cfg 文件修改为 zoo.cfg；

```
// 1.修改/etc/profile文件，修改后 source /etc/profile 命令使配置生效
export ZOOKEEPER_HOME=/opt/zookeeper-3.4.12
export PATH=$PATH:$ZOOKEEPER_HOME/bin

// 2.修改 ZooKeeper 的配置文件
cd conf
cp zoo_sample.cfg zoo.cfg

// zoo.cfg 文件内容
# ZooKeeper 服务器心跳时间，单位为 ms
tickTime=2000
# 允许 follower 连接并同步到 leader 的初始化连接时间， 以 tickTime 的倍数来表示
initLimit=10
# leader 与follower 心跳检测最大容忍时间，响应超过syncLimit*tickTime，leader 认为
# follower“死掉”，从服务器列表中删除 follower
syncLimit=5
# 数据目录
dataDir=/tmp/zookeeper/data
# 日志目录
dataLogDir=/tmp/zookeeper/log
# ZooKeeper 对外服务端口
clientPort=2181

默认情况下，Linux 系统中没有/tmp/zookeeper/data 和/tmp/zookeeper/log 这两个目录，所以
接下来还要创建这两个目录：
mkdir -p /tmp/zookeeper/data
mkdir -p /tmp/zookeeper/log

// 3.在${dataDir}目录（也就是/tmp/zookeeper/data）下创建一个 myid 文件，并写入一
     个数值，比如 0。myid 文件里存放的是服务器的编号。

// 4.启动 Zookeeper 服务
zkServer.sh start

// 查看 Zookeeper 服务状态
zkServer.sh status

// 5.集群模式配置
//  首先在这 3 台机器的/etc/hosts 文件中添加 3 台集群的 IP 地址与机器域名的映射，示例如下（3 个 IP 地址分别对应 3 台机器）：
192.168.0.2 node1
192.168.0.3 node2
192.168.0.4 node3

// 在这 3 台机器的 zoo.cfg 文件中添加以下配置：
server.0=192.168.0.2:2888:3888
server.1=192.168.0.3:2888:3888
server.2=192.168.0.4:2888:3888
//  server.A=B:C:D
//  A 是一个数字，代表服务器的编号（必须唯一），就是前面所说的 myid 文件里面的值。
//  B 代表服务器的 IP 地址。
//  C 表示服务器与集群中的 leader 服务器交换信息的端口。
//  D 表示选举时服务器相互通信的端口。
// 在这 3 台机器上各自执行 zkServer.sh start 命令来启动服务。
```

## 3. kafka 的安装与配置

3.1. 下载 Kafka broker 安装包；示例： kafka_2.11-2.0.0.tgz

3.2. 解压安装包；

3.3  环境配置：
+  修改/etc/profile文件；
+  修改完成后执行： source /etc/profile 命令使配置生效；
+  修改配置
    
```
// 1.修改/etc/profile文件，修改后 source /etc/profile 命令使配置生效
export KAFKA_HOME=/opt/kafka-2.11_2.0.0
export PATH=$PATH:KAFKA_HOME/bin

// 2.修改 Kafka 的配置文件 $KAFKA_HOME/config/server.properties
// 单机模式
# broker 的编号，如果集群中有多个 broker，则每个 broker 的编号需要设置的不同
broker.id=0
# broker 对外提供的服务入口地址
listeners=PLAINTEXT://localhost:9092
# 存放消息日志文件的地址
log.dirs=/tmp/kafka-logs
# Kafka 所需的 ZooKeeper 集群地址，为了方便演示，我们假设 Kafka 和 ZooKeeper 都安装在本机
zookeeper.connect=localhost:2181/kafka

// 集群模式:需要对单机模式的配置文件做相应的修改
// 确保集群中每个 broker 的 broker.id 配置参数的值不一样 
//  listeners 配置参数也需要修改为与 broker 对应的 IP 地址或域名

// 3.启动kafka
bin/kafka-server-start.sh config/server.properties

// 后台启动 （启动命令中加入-daemon 参数或&字符）
bin/kafka-server-start.sh –daemon config/server.properties
bin/kafka-server-start.sh config/server.properties &

// 可通过 jps 命令查看 Kafka 服务进程是否已经启动
jps -l
```