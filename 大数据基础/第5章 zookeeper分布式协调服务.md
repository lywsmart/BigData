# 第5章zookeeper集群部署

## 1.1 上传zookeeper压缩包

将zookeeper-3.4.8.tar.gz从windows拷贝到master节点/opt/software目录下，如下图：

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927162756307.png" alt="image-20220927162756307" style="zoom:80%;" />

## 1.2 解压zookeeper压缩包

- 在master节点上，解压/opt/software目录下的压缩包zookeeper-3.4.8.tar.gz到/usr/local/src目录下：

  ```shell
  tar -zxvf /opt/software/zookeeper-3.4.8.tar.gz -C /usr/local/src/
  ```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927163105875.png" style="zoom:80%;" />

## 1.3 重命名操作

- 在master节点上，将目录名为zookeeper-3.4.8，重命名为zookeeper

```
mv /usr/local/src/zookeeper-3.4.8/ /usr/local/src/zookeeper
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927163300930.png" style="zoom:80%;" />

## 1.4 添加环境变量

- 在master节点上，编辑/etc/profile文件，输入以下命令

```
vi /etc/profile
(注意：通过快捷建shift + gg，可以快速到达文件最末端。通过快捷键shift+zz快速保存)
```

- 添加zookeeper的环境变量，如下图所示

```shell
export ZOOKEEPER_HOME=/usr/local/src/zookeeper
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927163618025.png" style="zoom:80%;" />

## 1.5 使环境变量生效

- 输入命令

```shell
source /etc/profile
#使zookeeper环境变量生效
```


![](F:/城职学院/1_我的工作/1_大数据技术与应用专业群/大数据专业/课程资料/2_大数据高级应用/20级/第三周/C208DEF3-5126-40d4-B407-9358198247E5.png)

## 1.6 验证zookeeper环境变量是否成功

- 输入命令

```shell
echo $ZOOKEEPER_HOME
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927165712095.png" style="zoom:80%;" />

## 1.7 编辑zookeeper配置文件

### 1.myid文件

```shell
#在zookeeper安装目录下创建data目录
mkdir /usr/local/src/zookeeper/data
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927170228289.png" alt="image-20220927170228289" style="zoom:80%;" />

```shell
#进入到data目录下
cd /usr/local/src/zookeeper/data/
#创建myid文件
vi myid
#myid文件中写入编号1，如下图所示：
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927170649250.png" alt="image-20220927170649250" style="zoom:80%;" />

```shell
#说明：myid文件用来标识服务器/节点的编号，必须填写且不同节点不能重复，在后续的zoo.cfg中需要用到。本例约定master是1，slave1是2，slave2是3。
```

### 2.zoo.cfg文件

在master节点修改zookeeper配置文件zoo.cfg文件

```shell
#首写，复制zoo_sample.cfg配置文件，改为zoo.cfg
cp /usr/local/src/zookeeper/conf/zoo_sample.cfg /usr/local/src/zookeeper/conf/zoo.cfg
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927171343084.png" alt="image-20220927171343084" style="zoom:80%;" />

```shell
#编辑zoo.cfg文件
vi /usr/local/src/zookeeper/conf/zoo.cfg
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927171553482.png" alt="image-20220927171553482" style="zoom:80%;" />

**参数说明：**

- tickTime：心跳间隔，毫秒每次，时间单元。ZK中所有时间都是以这个时间单元为基础，进行整数倍配置的。。客户端与服务器或者服务器与服务器之间维持心跳，也就是每个tickTime时间就会发送一次心跳。通过心跳不仅能够用来监听机器的工作状态，还可以通过心跳来控制Flower跟Leader的通信时间，默认情况下FL的会话时常是心跳间隔的两倍。

- initLimit：集群中的follower服务器(F)与leader服务器(L)之间初始连接时能容忍的最多心跳数（tickTime的数量），如果超过这个次数还没连接上，则认为F不在线。Follower在启动过程中，会从Leader同步所有最新数据，然后确定自己能够对外服务的起始状态。Leader允许F在 initLimit 时间内完成这个工作。通常情况下，我们不用太在意这个参数的设置。如果ZK集群的数据量确实很大了，F在启动的时候，从Leader上同步数据的时间也会相应变长，因此在这种情况下，有必要适当调大这个参数了。(No Java system property)

- syncLimit：集群中flower服务器（F）跟leader（L）服务器之间的请求和答应最多能容忍的心跳数，如果超过整个次数，则认为F不在线。在运行过程中，Leader负责与ZK集群中所有机器进行通信，例如通过一些心跳检测机制，来检测机器的存活状态。如果L发出心跳包在syncLimit之后，还没有从F那里收到响应，那么就认为这个F已经不在线了。注意：不要把这个参数设置得过大，否则可能会掩盖一些问题。

- dataDir：存储快照文件snapshot的目录，本例需要配置。默认情况下，事务日志也会存储在这里。建议同时配置参数dataLogDir, 事务日志的写性能直接影响zk性能。

- dataLogDir：事务日志输出目录，本例需要配置。尽量给事务日志的输出配置单独的磁盘或是挂载点，这将极大的提升ZK性能。

- clientPort：客户端连接server的端口，即对外服务端口，一般设置为2181。zookeeper会监听这个端口，接收客户端的请求访问！
- maxClientCnxns：单个客户端与单台服务器之间的连接数的限制，是ip级别的，默认是60，如果设置为0，那么表明不作任何限制。请注意这个限制的使用范围，仅仅是单台客户端机器与单台ZK服务器之间的连接数限制，不是针对指定客户端IP，也不是ZK集群的连接数限制，也不是单台ZK对所有客户端的连接数限制。
- autopurge.purgeInterval：3.4.0及之后版本，ZK提供了自动清理事务日志和快照文件的功能，这个参数指定了清理频率，单位是小时，需要配置一个1或更大的整数，默认是0，表示不开启自动清理功能。
- autopurge.snapRetainCount：这个参数和上面的参数搭配使用，这个参数指定了需要保留的文件数目。默认是保留3个。

- server.x=[hostname]:nnnnn[:nnnnn]：这里的x是一个数字，与myid文件中的id是一致的。右边可以配置两个端口，第一个端口用于F和L之间的数据同步和其它通信，默认是2888;第二个端口用于Leader选举过程中投票通信,默认是3888。

```shell
#在zoo.cfg中添加并更改如下配置
#修改原有参数，需要手动创建指定目录
dataDir=/usr/local/src/zookeeper/data
#增加dataLogDir参数，需要手动创建指定目录
dataLogDir=/usr/local/src/zookeeper/logs
#增加通信的节点
server.1=master:2888:3888
server.2=slave1:2888:3888
server.3=slave2:2888:3888
#说明：server.2和server.3中的数字2,3约定分别代表slave1和slave2，后续的操作需要手动修改slave1和slave2的myid文件。
配置完后的zoo.cfg参数如下图
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927172422123.png" alt="image-20220927172422123" style="zoom:80%;" />

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927172445269.png" alt="image-20220927172445269" style="zoom:80%;" />

```shell
#根据上面配置文件内容，手动创建logs目录
mkdir /usr/local/src/zookeeper/logs
```

## 1.8 分发zookeeper目录

1. 将master节点上的/etc/profile文件分发给slave1节点、slave2节点上

```shell
#复制给slave1
scp /etc/profile slave1:/etc/
#复制给slave2
scp /etc/profile slave2:/etc/
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927173010455.png" alt="image-20220927173010455" style="zoom:80%;" />

2. 将master节点上的zookeeper目录分发给slave1节点、slave2节点

```shell
#复制给slave1
scp -r /usr/local/src/zookeeper/ slave1:/usr/local/src/
#复制给slave2
scp -r /usr/local/src/zookeeper/ slave2:/usr/local/src/
```

## 1.9 slave节点/etc/profile生效

```shell
#在slave1上使环境变量生效,输入以下命令
source /etc/profile
#在slave2上使环境变量生效,输入以下命令
source /etc/profile
```

## 1.10 修改slave节点myid文件

```shell
#在slave1上更改myid文件
vi /usr/local/src/zookeeper/data/myid 
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927232130277.png" alt="image-20220927232130277" style="zoom:80%;" />

```shell
#在slave2上更改myid文件
vi /usr/local/src/zookeeper/data/myid 
```

<img src="第5章 zookeeper分布式协调服务.assets/image-20220927232254254.png" alt="image-20220927232254254" style="zoom:80%;" />

## 1.11 关闭防火墙

```shell
#分别关闭三台虚拟机的防火墙
#在master节点上关闭防火墙
systemctl stop firewalld
#在slave1节点上关闭防火墙
systemctl stop firewalld
#在slave2节点上关闭防火墙
systemctl stop firewalld
```

## 1.12 启动zookeeper集群

```shell
#分别在三台虚拟机
#在master节点输入启动命令：
zkServer.sh start
#在slave1节点输入启动命令：
zkServer.sh start
#在slave2节点输入启动命令：
zkServer.sh start
```

出现如下图表示**zookeeper集群搭建成功**，**一个leader**，**两个follower**。

```shell
#分别在三台虚拟机
#在maser节点输入状态产看命令：
zkServer.sh status
#在slave1节点输入状态产看命令：
zkServer.sh status
#在slave2节点输入状态产看命令：
zkServer.sh status
```



<img src="第5章 zookeeper分布式协调服务.assets/image-20221008173233050.png" alt="image-20221008173233050" style="zoom:80%;" />

<img src="第5章 zookeeper分布式协调服务.assets/image-20221008173247810.png" alt="image-20221008173247810" style="zoom:80%;" />

<img src="第5章 zookeeper分布式协调服务.assets/image-20221008173302466.png" alt="image-20221008173302466" style="zoom:80%;" />