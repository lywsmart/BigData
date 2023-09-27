# 第3章 HDFS分布式文件系统

## 3.1 HDFS 的shell操作

### 3.1.1 ls命令

ls命令用于查看指定路径的当前目录结构，类似于linux的ls命令

```shell
#语法格式如下
hadoop fs –ls [-d] [-h] [-R] <args>
参数说明：
-d:将目录显示为普通文件
-h:使用便于操作人员读取的单位信息格式
-R:递归显示所有子目录的信息
```

- **练习1：显示HDFS的根目录的列表信息**

```shell
#输入下面指令
hadoop fs -ls /
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211444772.png" alt="image-20230920211444772" style="zoom:80%;" />

- **练习2：递归显示HDFS的根目录的列表信息**

```shell
#输入下面指令
hadoop fs -ls -R /
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211458953.png" alt="image-20230920211458953" style="zoom:80%;" />

### 3.1.2 mkdir命令

mkdir命令用于在指定路径下创建子目录，其中创建的路径可以采用URI格式进行指定，与linux的mkdir命令相同，可以创建多级目录。

```shell
#语法格式如下
hadoop fs –mkdir [-p] <paths>
参数说明：
-p:表示创建子目录先检查路径是否存在，如果不存在，则创建相应的各级目录
```

**练习1：在hdfs上创建目录/hdfs/test/test1**

```shell
#输入下面指令
hadoop fs -mkdir /hdfs/test/test1
#结果如下图，因为本范例的hdfs目录和test目录都不存在
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211525238.png" alt="image-20230920211525238" style="zoom:80%;" />

```shell
#正确命令
hadoop fs -mkdir  -p /hdfs/test/test1
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211535955.png" alt="image-20230920211535955" style="zoom:80%;" />

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211545826.png" alt="image-20230920211545826" style="zoom:80%;" />

### 3.1.3 put命令

put命令用于将本地系统的文件或文件夹复制到HDFS上

```shell
#语法格式如下
hadoop fs –put [-f] [-p] <paths>
参数说明：
-f:覆盖目标文件
-p:保留访问和修改时间、权限
```

**练习1：将本地的/opt/software/jdk-8u152-linux-x64.tar.gz传输到hadoop的mytest文件夹中**

```shell
#输入下面指令
hadoop fs -put /opt/software/jdk-8u212-linux-x64.tar.gz /mytest
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211604214.png" alt="image-20230920211604214" style="zoom:80%;" />

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211618663.png" alt="image-20230920211618663" style="zoom:80%;" />

### 3.1.4 cat命令

将路径指定文件的内容输出到控制台。

```shell
#语法格式如下
hadoop fs –cat <paths>
```

**练习1：输出hadoop的/mytest/word.txt文件内容**

```shell
#输入下面指令
 hadoop fs -cat /mytest/word.txt
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211703332.png" alt="image-20230920211703332" style="zoom:80%;" />

### 3.1.5 rm命令

删除指定的文件。只删除非空目录和文件。如要递归删除请参照rmr指令（已过时）或是使用rm –r格式删除

```shell
#语法格式如下
hadoop fs –rm [-r] <paths>
参数说明：
-r:递归删除时使用，不使用该参数无法删除非空目录
```

**练习1：删除hadoop上的/mytest/jdk-8u212-linux-x64.tar.gz文件**

```shell
#输入下面指令
hadoop fs -rm  /mytest/jdk-8u212-linux-x64.tar.gz
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211714133.png" alt="image-20230920211714133" style="zoom:80%;" />

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211724533.png" alt="image-20230920211724533" style="zoom:80%;" />

**练习2：删除多个目录：删除hadoop上/mytest目录、/ mytest_output**

```shell
#输入下面指令
hadoop fs -rm -r /mytest /mytest_output
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211734303.png" alt="image-20230920211734303" style="zoom:80%;" />

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211743033.png" alt="image-20230920211743033" style="zoom:80%;" />

### 3.1.6 get命令

复制文件到本地文件系统。

```shell
#语法格式如下
hadoop fs -get [-ignorecrc] [-crc] <src> <localdst>
参数说明：
-ignorecrc:选项复制CRC校验失败的文件
-crc:选项复制文件以及CRC信息。
```

**练习1：将hadoop上的word.txt文件下载到linux的/home下**

```shell
#输入下面指令
hadoop fs -get /word.txt /home
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211751354.png" alt="image-20230920211751354" style="zoom:80%;" />

```shell
#进入到/home目录下
cd /home
#查看word.txt文件是否下载成功
ls
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211802027.png" alt="image-20230920211802027" style="zoom:80%;" />



### 3.1.7 cp命令

将文件从源路径复制到目标路径。这个命令允许有多个源路径，此时目标路径必须是一个目录。

```shell
#语法格式如下
hadoop fs -cp URI [URI …] <dest>
```

**练习1：将hadoop上的/word.txt复制一份到目录/hdfs/test/test1**

```shell
#输入下面指令
hadoop fs -cp /word.txt /hdfs/test/test1
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211810587.png" alt="image-20230920211810587" style="zoom:80%;" />

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211822274.png" alt="image-20230920211822274" style="zoom:80%;" />

### 3.1.8 mv命令

将文件从源路径移动到目标路径。这个命令允许有多个源路径，此时目标路径必须是一个目录。不允许在不同的文件系统间移动文件。

```shell
#语法格式如下
hadoop fs -mv URI [URI …] <dest>
```

**练习1：将hadoop上的/word.txt，重命名为/word2.txt**

```shell
#输入下面指令
 hadoop fs -mv /word.txt /word2.txt
```

<img src="第3章 HDFS分布式文件系统.assets/image-20230920211831633.png" alt="image-20230920211831633" style="zoom:80%;" />



<img src="第3章 HDFS分布式文件系统.assets/image-20230920211843361.png" alt="image-20230920211843361" style="zoom:80%;" />

## 3.2 hadoop命令练习

1. 在linux系统/home目录下创建文件test.txt，添加以下内容

```tex
hello world
hello java
hello hadoop
hello world
hello java
```

1. 在hadoop上面创建文件夹/home/test目录
2. 将linux系统/home下的文件test.txt上传到hadoop上目录/home/test中
3. 复制hadoop上目录/home/test中test.txt文件到hadoop根目录/下，并重新命名为test1.txt
4. 查看hadoop上根目录/下的test1.txt文件的前三行内容
5. 下载hadoop上目录/home/test中test.txt文件到linux系统/opt/software目录中
6. 删除hadoop上目录/home/test，上传hadoop根目录/下的test1.txt文件



## 3.3 Hadoop配置项

官方文档：https://hadoop.apache.org/docs/current/hadoop-project-dist/

民间第三方中文文档：https://hadoop.org.cn/docs/      

​		https://hadoop.apache.org/docs/r1.0.4/cn/

### 3.3.1 常用配置文件解析

Hadoop有多个配置文件，这里列举最重要的几个。

**所有配置文件都在$HADOOP_HOME/etc/hadoop目录下。**

配置目录可以被重新安置在文件系统的其他地方（$HADOOP_HOME的外面，以便于升级），只要启动守护进程时使用--config选项（或等价的，使用HADOOP_CONF_DIR环境变量集）说明这个目录在本地文件系统的位置就可以了。



| 文件名称                   | 格式          |                             描述                             |
| -------------------------- | ------------- | :----------------------------------------------------------: |
| [hadoop-env.sh]()          | Bash脚本      |             脚本中要用到的环境变量，以运行Hadoop             |
| mapred-env.sh              | Bash脚本      | 脚本中要用到的环境变量，以运行MapReduce（覆盖hadoop-env.sh中设置的变量） |
| yarn-env.sh                | Bash脚本      | 脚本中要用到的环境变量，以运行YARN（覆盖hadoop-env.sh中设置的变量） |
| [core-site.xml]()          | Hadoop配置XML | Hadoop Core的配置项，例如HDFS、MapReduce和YARN常用的I/O设置等 |
| [hdfs-site.xml]()          | Hadoop配置XML | Hadoop守护进程的配置项，包括namenode、辅助namenode和datanode等 |
| [mapred-site.xml]()        | Hadoop配置XML |        MapReduce守护进程的配置项，包括作业历史服务器         |
| [yarn-site.xml]()          | Hadoop配置XML | YARN守护进程的配置项，包括资源管理器、web应用代理服务器和节点管理器 |
| [slaves]()                 | 纯文本        |        运行datanode和节点管理器的机器列表（每行一个）        |
| hadoop-metrics2.properties | Java属性      |               控制如何在Hadoop上发布度量的属性               |
| log4j.properties           | Java属性      | 系统日志文件、namenode审计日志、任务JVM进程的任务日志的属性  |
| hadoop-policy.xml          | Hadoop配置XML |         安全模式下运行Hadoop时的访问控制列表的配置项         |



### 3.3.2 core-site.xml配置文件

官方文档：https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/core-default.xml

core-site.xml文件是Hadoop的核心配置文件，它包含了Hadoop的基本设置，如Hadoop运行时的文件系统（fs.defaultFS）和Hadoop临时目录（hadoop.tmp.dir）等。

|      配置参数       |                             说明                             |
| :-----------------: | :----------------------------------------------------------: |
|   fs.default.name   |                    用于指定NameNode的地址                    |
|   hadoop.tmp.dir    |              Hadoop运行时产生文件的临时存储目录              |
| io.file.buffer.size |         缓冲区大小，实际工作中根据服务器性能动态调整         |
|  fs.trash.interval  | 开启hdfs的垃圾桶机制，删除掉的数据可以从垃圾桶中回收，单位分钟* |



自选是否要修改：

```xml
<!-- Put site-specific property overrides in this file. -->

<configuration>
    <!-- 
		用于设置Hadoop的文件系统，由URI指定
		默认协议file:///属于本地路径，由于之后要用HDFS，因此需要修改为hdfs://主机名(ip):端口（内部的一个通讯）
	-->
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://master:9000</value>
    </property>

    <!-- 配置Hadoop存储数据目录,默认/tmp/hadoop-${user.name} -->
    <property>
       <name>hadoop.tmp.dir</name>
       <value>file:/usr/local/src/hadoop/tmp</value>
    </property>

    <!--  缓冲区大小，实际工作中根据服务器性能动态调整 -->
    <property>
       <name>io.file.buffer.size</name>
       <value>131072</value>
    </property>

    <!--  开启hdfs的垃圾桶机制，删除掉的数据可以从垃圾桶中回收，单位分钟 -->
    <property>
       <name>fs.trash.interval</name>
       <value>10080</value>
    </property>
</configuration>

```

#### 练习1：开启垃圾桶回收机制，设置10080分钟后自动删除文件。并尝试删除文件查看效果

**只有本次练习会提供答案，其他的练习请同学们自己写命令**

先尝试删除文件，执行

```shell
hadoop fs -rm -r 文件名
```

查看删除效果

![image-20230927131816511](./第3章 HDFS分布式文件系统.assets/image-20230927131816511.png)



修改core-site.xml文件，增加垃圾桶回收机制

```shell
vi /usr/local/src/hadoop/etc/hadoop/core-site.xml 
```

![image-20230927132010701](./第3章 HDFS分布式文件系统.assets/image-20230927132010701.png)



将修改好的文件分发给其他主机：

```shell
scp /usr/local/src/hadoop/etc/hadoop/core-site.xml slave1:/usr/local/src/hadoop/etc/hadoop/

scp /usr/local/src/hadoop/etc/hadoop/core-site.xml slave2:/usr/local/src/hadoop/etc/hadoop/
```

再次尝试删除文件

```shell
hadoop fs -rm -r 文件名
```

<img src="./第3章 HDFS分布式文件系统.assets/image-20230927132214013.png" alt="image-20230927132214013" style="zoom:150%;" />

文件并没有直接删除，而是移动到了[hdfs://master:9000/user/root/.Trash/Current](hdfs://master:9000/user/root/.Trash/Current)位置下，我们可以通过命令查看被删除的文件

```shell
hadoop fs -ls /user/root/.Trash/Current/
```





### 3.3.3 hadoop-env.sh配置文件

用于定义hadoop运行环境相关的配置信息。

比如配置JAVA_HOME环境变量、为hadoop的JVM指定特定的选项、指定日志文件所在的目录路径以及master和slave文件的位置等

|      属性       |                             含义                             |
| :-------------: | :----------------------------------------------------------: |
|    JAVA_HOME    | 需要设置Hadoop系统的Java安装的位置。方法一是在hadoop-env.sh文件中设置JAVA_HOME项；方法二是在shell中设置JAVA_HOME环境变量。相比之下，方法一更好，因为只需操作一次就能够保证整个集群使用同一版本的Java |
| HADOOP_LOG_DIR  | 系统日志文件存放目录，默认在$HADOOP_HOME/logs。建议修改默认设置，使之独立于Hadoop的安装目录，这样即使Hadoop升级之后安装路径发生变化，也不会影响日志文件的位置。 |
| HADOOP_SSH_OPTS |                        设置SSH选项。                         |



#### 练习1：查看hadoop-env.sh环境变量文件，思考指定日志文件所在的目录路径以及master和slave文件的位置是在哪里定义的？

![image-20230927135102417](./第3章 HDFS分布式文件系统.assets/image-20230927135102417.png)

![image-20230927135232920](./第3章 HDFS分布式文件系统.assets/image-20230927135232920.png)

### 3.3.4 hdfs-site.xml配置文件

官方文档：https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml

|            属性             |                             含义                             |
| :-------------------------: | :----------------------------------------------------------: |
|    dfs.namenode.name.dir    |       *namenode存储hdfs名字的空间的[元数据]()文件目录*       |
|    dfs.datanode.data.dir    |        datanode上的一个[数据块的物理的存储]()位置文件        |
|       dfs.replication       |             指定HDFS保存数据副本的数量，默认为3              |
|        dfs.blocksize        | 设置一个数据块(block)的大小,默认为128mb（134217728），单位为字节 |
|      dfs.http.address       | 定义namenode界面的web访问地址，默认为50070，设置方式为master:50070 |
| dfs.namenode.checkpoint.dir | 辅助NameNode存放检查点的目录列表。在所列每个目录中均存放一份检查点文件的副本。 |



#### 练习1:将副本的复制数设置为1

#### 练习2:将数据块的大小设置为256mb。 即268435456字节



### 3.3.5 mapred-site.xml配置文件

Hadoop默认只有个模板文件mapred-site.xml.template,需要使用该文件复制出来一份mapred-site.xml文件

yarn 的 web 地址 和 history 的 web 地址以及指定我们的 [mapreduce](https://so.csdn.net/so/search?q=mapreduce&spm=1001.2101.3001.7020) 运行在 yarn 集群上

|                属性                 |                             含义                             |
| :---------------------------------: | :----------------------------------------------------------: |
|      mapreduce.framework.name       |   MapReduce 程序运行在 Yarn 上，表示MapReduce使用yarn框架    |
|    mapreduce.jobhistory.address     |   设置MR历史任务的主机和端口。默认为10020，即master:10020    |
| mapreduce.jobhistory.webapp.address | 设置web访问历史任务的主机和端口。默认为19888，即master:19888 |



自选是否要修改：

```xml
<!-- Put site-specific property overrides in this file. -->

<configuration>
    	<!-- 指定 MapReduce 程序运行在 Yarn 上，表示MapReduce使用yarn框架 -->
        <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
        </property>
		<!-- 设置历史任务的主机和端口 -->
		<property>
			<name>mapreduce.jobhistory.address</name>
			<value>master:10020</value>
		</property>
		<!-- 设置网页访问历史任务的主机和端口 -->
		<property>
			<name>mapreduce.jobhistory.webapp.address</name>
			<value>master:19888</value>
		</property>
</configuration>
```



#### 练习1：修改mapred-site.xml设置历史任务服务器端口，在web端查看历史人物

修改过程略

**开启历史服务器命令（在master上执行）**

```shell
mr-jobhistory-daemon.sh start historyserver
```

 ![image-20230927135831137](./第3章 HDFS分布式文件系统.assets/image-20230927135831137.png)

在web端通过19888端口查看历史任务

<img src="./第3章 HDFS分布式文件系统.assets/image-20230927135939029.png" alt="image-20230927135939029" style="zoom: 50%;" />



### 3.3.6 yarn-site.xml配置文件

```xml
<property>
    <name>yarn.resourcemanager.hostname</name>
    <value>master</value>
</property>
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>
```







## 3.4 Hadoop故障排错

我们的集群可能会存在节点启动失败或者运行失败的情况，我们就需要通过Log文件来进行故障排错。

首先，我们来看一下健康的集群应该满足的节点：

- master节点，如下图：

<img src="./第3章 HDFS分布式文件系统.assets/image-20220925172338030.png" alt="image-20220925172338030" style="zoom: 80%;" />

- slave1节点，如下图：

<img src="./第3章 HDFS分布式文件系统.assets/image-20220925172417707.png" alt="image-20220925172417707" style="zoom:80%;" />

- slave2节点，如下图：

<img src="./第3章 HDFS分布式文件系统.assets/image-20220925172448841.png" alt="image-20220925172448841" style="zoom:80%;" />





假设我们的集群现在的master、slave1、slave2的DataNode无法启动，我们需要查看日志文件

![image-20230927134828453](./第3章 HDFS分布式文件系统.assets/image-20230927134828453.png)



### 3.4.1 查看日志文件

进入默认的日志文件路径：

```shell
cd /usr/local/src/hadoop/logs/
```

 ![image-20230927133053105](./第3章 HDFS分布式文件系统.assets/image-20230927133053105.png)



找到出错的节点的日志文件，比如我们现在的master、slave1、slave2的DataNode无法启动，我们就来查看对应的日志文件

![image-20230927133203897](./第3章 HDFS分布式文件系统.assets/image-20230927133203897.png)

![image-20230927133223442](./第3章 HDFS分布式文件系统.assets/image-20230927133223442.png)

![image-20230927133235877](./第3章 HDFS分布式文件系统.assets/image-20230927133235877.png)



以slave1的为例，查看对应的日志文件

```shell
vi /usr/local/src/hadoop/logs/hadoop-root-datanode-slave1.log
```

大写G跳到文件最后一行，找到错误原因

![image-20230927133521876](./第3章 HDFS分布式文件系统.assets/image-20230927133521876.png)

**[ps：遇到不知道报错原因的情况下，找到Error，Fatal，Exception等单词，把对应的句子复制到百度寻找答案]()**





这里提供最基本的查找方法

![image-20230927133804173](./第3章 HDFS分布式文件系统.assets/image-20230927133804173.png)

问题是mapred-site.xml文件的第25行，第1列有问题，不符合xml格式





### 3.4.2 解决问题

修改错误文件，注意三台都需要进行检查

注意：检查三台机子的文件是否都出错了

 ![image-20230927133904853](./第3章 HDFS分布式文件系统.assets/image-20230927133904853.png)

找到错误并修改错误



错误文件

![image-20230927134009088](./第3章 HDFS分布式文件系统.assets/image-20230927134009088.png)



### 3.4.3 重新初始化

**[注意：这个方法是下策，真正的处理方法是重启后修改每个集群的cluster-id]()**



**① 关闭服务（在master上执行命令）**

```shell
stop-all.sh
```



**② 删除三台机子的数据和元数据**

```shell
rm -rf /usr/local/src/hadoop/dfs
```

![image-20230927134659104](./第3章 HDFS分布式文件系统.assets/image-20230927134659104.png)



**③ 重新初始化 (在master上执行)**

```shell
hdfs namenode -format
```

 ![image-20230927134412343](./第3章 HDFS分布式文件系统.assets/image-20230927134412343.png)

 ![image-20230927134422423](./第3章 HDFS分布式文件系统.assets/image-20230927134422423.png)



**④ 启动服务 (在master上执行)**

```shell
start-all.sh
```







## 3.5 Hadoop更多运维命令

hadoop的系统命令都是存放[/usr/local/src/hadoop/sbin](/usr/local/src/hadoop/sbin)目录下的，我们用的[start-all.sh]()以及[stop-all.sh]()也是放在该目录下

![image-20230927140243754](./第3章 HDFS分布式文件系统.assets/image-20230927140243754.png)



除了开启所有服务和关闭所有服务的命令以外，我们还有很多更加特定的运维命令



### 3.5.1 开启/关闭dfs服务

|     命令     |                 功能                 |
| :----------: | :----------------------------------: |
| start-dfs.sh | 启动namenode，datanode，启动文件系统 |
| stop-dfs.sh  |             关闭文件系统             |

#### 练习1：关闭所有dfs服务

#### 练习2：开启所有dfs服务



### 3.5.2 开启/关闭yarn服务

|     命令      |                 功能                 |
| :-----------: | :----------------------------------: |
| start-yarn.sh | 启动namenode，datanode，启动文件系统 |
| stop-yarn.sh  |             关闭文件系统             |

#### 练习1：关闭所有yarn服务

#### 练习2：开启所有yarn服务





### 3.5.3 单独启动NameNode或DataNode

#### ① hadoop-daemon.sh

**[启动/关闭单节点的Namenode或DataNode]()**

```shell
hadoop-daemon.sh start 或 stop  namenode（datanode）
```

 

```shell
[root@master ~] hadoop-daemon.sh start namenode  # 启动master上的namenode

[root@slave1 ~] hadoop-daemon.sh stop datanode  # 启动slave1上的datanode
```



#### ② hadoop-daemons.sh

**[启动/关闭所有节点的Namenode或DataNode]()**

```shell
hadoop-daemons.sh start 或 stop  namenode（datanode）
```



只需要在master上执行

```shell
hadoop-daemons.sh start datanode  # 启动所有节点上的namenode
```





#### 练习1：关闭master的namenode

#### 练习2：开启master的namenode

#### 练习3：关闭slave2的datanode

#### 练习4：启动所有节点的datanode



### 3.5.4 hdfs系统检查工具fsck

|          命令           |              功能               |
| :---------------------: | :-----------------------------: |
|     hdfs fsck -move     |    移动受损文件到/lost+found    |
|    hdfs fsck -delete    |         删除受损文件。          |
| hdfs fsck -openforwrite |      打印出写打开的文件。       |
|    hdfs fsck -files     |     打印出正被检查的文件。      |
|    hdfs fsck -blocks    |       打印出块信息报告。        |
|  hdfs fsck -locations   |    打印出每个块的位置信息。     |
|    hdfs fsck -racks     | 打印出data-node的网络拓扑结构。 |



#### 练习1：打印文件的文件信息，块信息，块位置

先将word.txt文件上传hdfs系统上的/mytest/目录

输入命令

```shell
hdfs fsck /mytest/word.txt -files -blocks -locations
```

