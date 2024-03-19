# 第三章 Spark集群部署使用

## 3.1 安装部署高可用Spark

### 3.1.1 解压Spark压缩包

- 进入到/usr/local/src目录下，输入以下命令：

```shell
cd /usr/local/src
```

- 解压spark压缩包，输入命令：

```shell
tar -zxvf spark-2.1.1-bin-hadoop2.7.tgz -C /opt
```

<img src="第三章Spark集群部署使用.assets/image-20240317230540606.png" alt="image-20240317230540606" style="zoom:80%;" />

### 3.1.2 重命名

- 进入到/opt目录下

```shell
cd /opt
```

- 将目录名为spark-2.1.1-bin-hadoop2.7 ，重命名为spark

```shell
mv spark-2.1.1-bin-hadoop2.7 spark 
```

<img src="第三章Spark集群部署使用.assets/image-20240317230813602.png" alt="image-20240317230813602" style="zoom:80%;" />

### 3.1.3 添加环境变量

- 编辑/etc/profile文件，输入以下命令

```shell
vi /etc/profile
```

- (**注意**：通过快捷建shift + gg，可以快速到达文件最末端。通过快捷键shift+zz快速保存)
- 添加spark的环境变量，如下图所示

```shell
# set spark environment
export SPARK_HOME=/usr/local/src/spark
export PATH=$PATH:$SPARK_HOME/bin:$PATH
```

<img src="第三章Spark集群部署使用.assets/image-20240317231054140.png" alt="image-20240317231054140" style="zoom:80%;" />

### 3.1.4 使环境变量生效

- 输入命令

```shell
source /etc/profile
```

- 使spark环境变量生效

### 3.1.5 验证Spark命令

- 输入命令

```shell
`echo $SPARK_HOME `
```

<img src="第三章Spark集群部署使用.assets/image-20240317231311135.png" alt="image-20240317231311135" style="zoom:80%;" />

### 3.1.6编辑spark配置文件

​	首先进入到**spark配置文件目录**下，输入命令

```shell
cd /opt/spark/conf
```

<img src="第三章Spark集群部署使用.assets/image-20240317231425040.png" alt="image-20240317231425040" style="zoom:80%;" />

#### 1.spark-env.sh文件

- **编辑文件spark-env.sh，输入命名**

```shell
vi spark-env.sh
```

​	**在文件最后一行添加以下配置项**

```shell
#java的路径
JAVA_HOME=/opt/jdk1.8.0_111
#spark集群中主节点的 IP 地址或者名称
SPARK_MASTER_IP=master
#spark集群中主节点的 端口号。
SPARK_MASTER_PORT=7077
#scala的路径
SCALA_HOME=/opt/scala-2.12.17

#下面几个参数完成spark和hadoop的挂接，使spark可以提交作业到hadoop的yarn运行
#完成 spark 和hadoop 的挂接
#hadoop的路径
HADOOP_HOME=/opt/hadoop-2.7.6
SPARK_DIST_CLASSPATH=$(/opt/hadoop-2.7.6/bin/hadoop classpath)
#下面几个参数配置将spark设置为hadoop的yarn运行
HADOOP_CONF_DIR=/opt/hadoop-2.7.6/etc/hadoop
SPARK_YARN_USER_ENV="CLASSPATH=/opt/hadoop-2.7.6/etc/hadoop"
YARN_CONF_DIR=/opt/hadoop-2.7.6/etc/hadoop
```

<img src="第三章Spark集群部署使用.assets/image-20240317233131166.png" alt="image-20240317233131166" style="zoom:80%;" />

#### 2.slaves文件

- **复制文件slaves.template，并命名为slaves，输入命令**

```shell
cp slaves.template slaves`
```

<img src="第三章Spark集群部署使用.assets/image-20240317233245590.png" alt="image-20240317233245590" style="zoom:80%;" />

- **打开文件slaves，输入命名**

```shell
vi slaves
```

​	**将文件中localhost改为以下配置**

<img src="第三章Spark集群部署使用.assets/image-20240317233347041.png" alt="image-20240317233347041" style="zoom:80%;" />

### 3.1.7 spark目录

1. 将master节点上的spark目录复制到其他节点上去（slave1和slave2）

​	远程复制命令：

```shell
scp -r /opt/spark slave1:/opt
scp -r /opt/spark slave2:/opt
```

2. 将master节点上的/etc/profile文件复制到其他节点上去（slave1和slave2）

```shell
scp /etc/profile slave1:/etc/profile
scp /etc/profile slave2:/etc/profile
```

3. 在其他节点上（slave1和slave2）使/etc/profile文件生效，在slave1和slave2上执行命令

```shell
source /etc/profile
```

4. 在其他节点上（slave1和slave2）验证spark命令，在slave1和slave2上执行命令

```shell
echo $SPARK_HOME
```

## 3.2 Spark集群运行模式

​	**Spark 的常用运行模式有 Local(也称单节点模式），Standalone(集群模式），Spark on Yarn(运行在Yarn上）。**

### 3.1.1 Spark local模式运行

1. Spark单机运行模式，一般用于开发测试。运行该模式非常简单，只需要把Spark的安装包解压后，改一些常用的配置即可使用，而不用启动Spark的Master、Worker守护进程，也不用启动Hadoop的各服务（除非需要用到HDFS）。

​	在**master**节点上执行以下命令

```shell
spark-submit  \
--class org.apache.spark.examples.SparkPi \
--master local[1] \
--driver-memory 1g  \
--executor-memory 1g  \
--executor-cores 1 /opt/spark/examples/jars/spark-examples_2.11-2.1.1.jar 40
```

<img src="第三章Spark集群部署使用.assets/image-20240317234234508.png" alt="image-20240317234234508" style="zoom:80%;" />

​	参数说明：

| 参数名                  | 参数说明                                                     |
| ----------------------- | ------------------------------------------------------------ |
| spark-submit            | 表示提交任务到spark集群或yarn执行。                          |
| --class                 | 应用程序的主类，仅针对 java 或 scala 应用，本例指            /opt/spark/examples/jars/spark-examples_2.11-2.1.1.jar中的类。 |
| --master                | master 的地址，提交任务到哪里执行，可以选择spark、yarn、local，如果是spark集群，则输入地址如spark://master:7077。 |
| -deploy-mode            | 在本地 (client) 启动 driver 或在 cluster 上启动，默认是 client。 |
| --driver-memory         | Driver内存，默认 1G                                          |
| --executor-memory       | 每个 executor/计算节点的内存，默认是1G                       |
| --executor-core         | 每个 executor 的核数，在yarn或者standalone下使用。           |
| <Application-jar>       | 即表示表示执行的类所在的jar路径，例如本例：/opt/spark/examples/jars/spark-examples_2.11-2.1.1.jar |
| [application-arguments] | 传入jar的主类的参数，例如本例：40                            |

### 3.2.2 Spark Standalone模式

​	构建一个由Master+Slave构成的Spark集群。和单机运行的模式不同，这里必须在执行应用程序前，先启动Spark的Master和Worker守护进程。这种运行模式，可以使用Spark的8080 来观察资源和应用程序的执行情况了。

#### 1. 启动Spark集群

​	①在master节点输入命令

```shell
/opt/spark/sbin/start-all.sh
```

<img src="第三章Spark集群部署使用.assets/image-20240317234535638.png" alt="image-20240317234535638" style="zoom:80%;" />

<img src="第三章Spark集群部署使用.assets/image-20240317234559416.png" alt="image-20240317234559416" style="zoom:80%;" />

<img src="第三章Spark集群部署使用.assets/image-20240317234639676.png" alt="image-20240317234639676" style="zoom:80%;" />

#### 2.基于standalone下的client模式

​	①client模式下：适用于交互、调试，希望立即看到任务的输出。

- 在master节点下输入命令：

```shell
spark-submit  \
--class org.apache.spark.examples.SparkPi \
--master spark://master:7077 \
--deploy-mode client  \
--driver-memory 512M  \
--executor-memory 512M  \
--executor-cores 1 /opt/spark/examples/jars/spark-examples_2.11-2.1.1.jar 40

```

<img src="C:\Users\samrt\AppData\Roaming\Typora\typora-user-images\image-20220410183202349.png" alt="image-20220410183202349" style="zoom:80%;" />

- 通过网页端观察资源和应用程序的执行情况（http://master:8080/）

<img src="C:\Users\samrt\AppData\Roaming\Typora\typora-user-images\image-20220410183109775.png" alt="image-20220410183109775" style="zoom:80%;" />

#### 3. 基于standalone的cluster集群模式

​	①cluster模式下:适用于生产环境

- 在master节点下输入命令：

```shell
/usr/local/src/spark/bin/spark-submit  \
--class org.apache.spark.examples.SparkPi \
--master spark://master:7077 \
--deploy-mode cluster  \
--driver-memory 1g  \
--executor-memory 1g  \
--executor-cores 1 /opt/spark/examples/jars/spark-examples_2.11-2.1.1.jar 40
```

<img src="C:\Users\samrt\AppData\Roaming\Typora\typora-user-images\image-20220410184248067.png" alt="image-20220410184248067" style="zoom:80%;" />

- cluster模式下结果查看

<img src="C:\Users\samrt\AppData\Roaming\Typora\typora-user-images\image-20220410185552172.png" alt="image-20220410185552172" style="zoom:80%;" />



### 3.2.3 停止spark集群

1. 在master节点输入以下命令停止spark集群

```shell
/usr/local/src/spark/sbin/stop-all.sh 
```





