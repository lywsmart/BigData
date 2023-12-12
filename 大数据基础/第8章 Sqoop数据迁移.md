# 第 8 章 sqoop数据迁移

​	在实际开发开发中：

- 有时候需要将HDFS或Hive上的数据[导出]()到关系型数据库中，如MySQL、Oracle等。
- 或者将关系型数据库中的数据[导入]()到Hadoop，如HDFS、Hive等。

​	如果通过手动的方式进行数据迁移的话，就会显得非常麻烦。

​	为此，我们可以使用`Sqoop`工具进行[数据迁移]()。

​	本章将针对Sqoop工具的安装和使用进行详细讲解。

## 8.1 Sqoop的安装与部署

### 8.1.1 上传Sqoop压缩包

将sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz 从windows拷贝到master节点/opt/software目录下，如下图：

<img src="第8章 sqoop数据迁移.assets/image-20231208095000229.png" alt="image-20231208095000229" style="zoom:80%;" />

### 8.1.2 解压sqoop压缩包

- 在master节点上，解压/opt/software目录下的压缩包sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz到/usr/local/src目录下：

  ```shell
  tar -zxvf /opt/software/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz -C /usr/local/src/
  ```

<img src="第8章 sqoop数据迁移.assets/image-20231208095130699.png" alt="image-20231208095130699" style="zoom:80%;" />

### 8.1.3 重命名操作

- 在master节点上，将目录名为sqoop-1.4.7.bin__hadoop-2.6.0，重命名为sqoop

```
mv /usr/local/src/sqoop-1.4.7.bin__hadoop-2.6.0 /usr/local/src/sqoop
```

<img src="第8章 sqoop数据迁移.assets/image-20231208095405925.png" alt="image-20231208095405925" style="zoom:80%;" />

### 8.1.4 添加环境变量

- 在master节点上，编辑/etc/profile文件，输入以下命令

```shell
vi /etc/profile
(注意：通过快捷建shift + gg，可以快速到达文件最末端。通过快捷键shift+zz快速保存)
```

- 添加sqoop的环境变量，如下图所示

```shell
export SQOOP_HOME=/usr/local/src/sqoop
export PATH=$PATH:$SQOOP_HOME/bin
export CLASSPATH=$CLASSPATH:$SQOOP_HOME/lib
```

<img src="第8章 sqoop数据迁移.assets/image-20231208095705315.png" alt="image-20231208095705315" style="zoom:80%;" />

### 8.1.5 使环境变量生效

- 输入命令

```shell
source /etc/profile
#使sqoop环境变量生效
```

<img src="第8章 sqoop数据迁移.assets/image-20231208095753223.png" alt="image-20231208095753223" style="zoom:80%;" />



### 8.1.6 验证sqoop环境变量是否成功

- 输入命令

```shell
echo $SQOOP_HOME
```

<img src="第8章 sqoop数据迁移.assets/image-20231208102424729.png" alt="image-20231208102424729" style="zoom:80%;" />

### 8.1.7 编辑sqoop配置文件

`sqoop-env.sh`文件

```shell
#在sqoop安装目录下复制配置文件
cp /usr/local/src/sqoop/conf/sqoop-env-template.sh  /usr/local/src/sqoop/conf/sqoop-env.sh
```

![image-20231211193722383](./第8章 Sqoop数据迁移.assets/image-20231211193722383.png)

​	编辑sqoop-env.sh文件

```shell
vi /usr/local/src/sqoop/conf/sqoop-env.sh
```

![image-20231211193805595](./第8章 Sqoop数据迁移.assets/image-20231211193805595.png)

```shell
#编辑sqoop-env.sh文件，在文件末尾添加hadoop和hive的安装路径
export HADOOP_COMMON_HOME=/usr/local/src/hadoop
export HADOOP_MAPRED_HOME=/usr/local/src/hadoop
export HIVE_HOME=/usr/local/src/hive
```

<img src="第8章 sqoop数据迁移.assets/image-20231208102911116.png" alt="image-20231208102911116" style="zoom:80%;" />

### 8.1.8 sqoop连接测试

#### (1) 连接mysql数据库测试

1. 将master节点上的 mysql 数据库驱动程序复制到 sqoop 安装目录下

```shell
cp /opt/software/mysql-connector-java-5.1.45.jar /usr/local/src/sqoop/lib/
```

<img src="第8章 sqoop数据迁移.assets/image-20231208103330597.png" alt="image-20231208103330597" style="zoom:80%;" />

2. mysql数据库驱动已经放入到sqoop安装目录下，接下来测试sqoop与mysql连接是否能成功

```shell
#输入连接命令
sqoop list-databases \
--connect jdbc:mysql://127.0.0.1:3306/ \
--username root \
--password Password123$
```

命令说明：

- --connect:指定连接的关系数据库
- --username:用于指定数据库的用户名
- --password：用于指定连接数据库的密码。
- list-databases：显示所有数据库名称，相当于 show databases;

<img src="第8章 sqoop数据迁移.assets/image-20231208103939469.png" alt="image-20231208103939469" style="zoom:80%;" />

#### (2) 连接Hive数据仓库测试

1. 将master节点上的 hive 驱动程序复制到 sqoop 安装目录下

```shell
cp /usr/local/src/hive/lib/hive-common-2.0.0.jar /usr/local/src/sqoop/lib/
```

<img src="第8章 sqoop数据迁移.assets/image-20231208115834470.png" alt="image-20231208115834470" style="zoom:80%;" />



## 8.2 Sqoop概述

​	`Sqoop`是一款开源工具，最早是作为Hadoop的一个第三方模块存在，后来为了让使用者能够快速部署，也为了让开发人员能够更快速的迭代开发，将其独立成为一个顶级开源项目。



### 8.2.1 Sqoop简介

​	`Sqoop`主要用于在[Hadoop]()和[关系型数据库]()之间传输数据。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211104203852.png" alt="image-20231211104203852" style="zoom: 50%;" />





### 8.2.2 Sqoop原理

​	Sqoop是关系型数据库与Hadoop间进行数据同步的工具，其底层利用[MapReduce]()实现数据的导入和导出。

​	在数据同步的过程中，MapReduce通常[只涉及MapTask]()的处理，并不会涉及ReduceTask的处理，这是因为数据同步时，[只涉及数据的读取与加载]()，并不会涉及到数据合并的操作。



#### (1) 数据导入

​	数据导入是指通过Sqoop将[关系型数据库的数据导入到Hadoop]()。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211104421686.png" alt="image-20231211104421686" style="zoom:50%;" />

Sqoop实现数据导入共分为6个步骤，具体如下。

1. 客户端向[Sqoop发送数据]()导入的命令。
2. Sqoop通过[JDBC连接关系型数据库]()，获取所需表的[元数据信息]()。
3. Sqoop根据获取的元数据信息生成一个与[表同名的记录容器类（Java类）]()来完成序列化操作，记录表的每一行数据。
4. Sqoop根据生成的记录容器类启动一个[MapReduce程序]()。
5. MapReudce程序可以通过[多个MapTask进行并行计算]()，从关系型数据库的表读取数据，这时会使用Sqoop生成的记录容器类进行[反序列化]()操作。
6. MapReudce程序可以通过多个MapTask进行并行计算，将[关系型数据库]()读取的数据加载到[HDFS、Hive表或HBase表]()。



#### (2) 数据导出

​	数据导出是指通过Sqoop将[Hadoop的数据导出到关系型数据库]()。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211104828223.png" alt="image-20231211104828223" style="zoom:50%;" />

Sqoop实现数据导出共分为6个步骤，具体如下。

1. 客户端向[Sqoop发送数据导出]()的命令。
2. Sqoop通过[JDBC连接关系型数据库]()，获取所需表的[元数据信息]()。
3. Sqoop根据获取的元数据信息生成一个与表同名的记录容器类（Java类）来完成序列化操作，记录表的每一行数据。
4. Sqoop根据生成的记录容器类启动一个MapReduce程序。
5. MapReudce程序中的多个MapTask进行并行计算，从HDFS、Hive的表或HBase的表读取数据，这时会使用Sqoop生成的记录容器类进行反序列化操作。
6. MapReudce程序中的多个MapTask进行并行计算，将读取的数据加载到关系型数据库的表，这时同样会使用Sqoop生成的记录容器类进行反序列化操作。







## 8.3 Sqoop命令介绍

​	Sqoop使用起来非常简单，它提供了一系列命令来操作Sqoop，如数据的[导入和导出操作]()。可以通过执行“sqoop help”命令查看Sqoop提供的所有命令。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211165009531.png" alt="image-20231211165009531" style="zoom:50%;" />



​	如果想要了解某个命令包含的所有参数，可以参照下列语法格式进行查看。

```shell
sqoop help command
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211164957935.png" alt="image-20231211164957935" style="zoom:67%;" />



### 8.3.1 **常用命令列举**

| **序号** | **命令**          | **类**              | **说明**                                                     |
| -------- | ----------------- | ------------------- | ------------------------------------------------------------ |
| 1        | import            | ImportTool          | 将数据导入到集群                                             |
| 2        | export            | ExportTool          | 将集群数据导出                                               |
| 3        | codegen           | CodeGenTool         | 获取数据库中某张表数据生成Java并打包Jar                      |
| 4        | create-hive-table | CreateHiveTableTool | 创建Hive表                                                   |
| 5        | eval              | EvalSqlTool         | 查看SQL执行结果                                              |
| 6        | import-all-tables | ImportAllTablesTool | 导入某个数据库下所有表到HDFS中                               |
| 7        | job               | JobTool             | 用来生成一个sqoop的任务，生成后，该任务并不执行，除非使用命令执行该任务。 |
| 8        | list-databases    | ListDatabasesTool   | 列出所有数据库名                                             |
| 9        | list-tables       | ListTablesTool      | 列出某个数据库下所有表                                       |
| 10       | merge             | MergeTool           | 将HDFS中不同目录下面的数据合在一起，并存放在指定的目录中     |
| 11       | metastore         | MetastoreTool       | 记录sqoop job的元数据信息，如果不启动metastore实例，则默认的元数据存储目录为：~/.sqoop，如果要更改存储目录，可以在配置文件sqoop-site.xml中进行更改。 |
| 12       | help              | HelpTool            | 打印sqoop帮助信息                                            |
| 13       | version           | VersionTool         | 打印sqoop版本信息                                            |



### **8.3.2** **命令**&参数详解

刚才列举了一些Sqoop的常用命令，对于不同的命令，有不同的参数，让我们来一一列举说明。

首先来我们来介绍一下公用的参数，所谓公用参数，就是大多数命令都支持的参数。

#### **(1)** **公用参数：数据库连接**

| **序号** | **参数**             | **说明**               |
| -------- | -------------------- | ---------------------- |
| 1        | --connect            | 连接关系型数据库的URL  |
| 2        | --connection-manager | 指定要使用的连接管理类 |
| 3        | --driver             | Hadoop根目录           |
| 4        | --help               | 打印帮助信息           |
| 5        | --password           | 连接数据库的密码       |
| 6        | --username           | 连接数据库的用户名     |
| 7        | --verbose            | 在控制台打印出详细信息 |

#### **(2)** **公用参数：**import

| **序号** | **参数**                        | **说明**                                                     |
| -------- | ------------------------------- | ------------------------------------------------------------ |
| 1        | --enclosed-by <char>            | 给字段值前加上指定的字符                                     |
| 2        | --escaped-by <char>             | 对字段中的双引号加转义符                                     |
| 3        | --fields-terminated-by <char>   | 设定每个字段是以什么符号作为结束，默认为逗号                 |
| 4        | --lines-terminated-by <char>    | 设定每行记录之间的分隔符，默认是\n                           |
| 5        | --mysql-delimiters              | Mysql默认的分隔符设置，字段之间以逗号分隔，行之间以\n分隔，默认转义符是\，字段值以单引号包裹。 |
| 6        | --optionally-enclosed-by <char> | 给带有双引号或单引号的字段值前后加上指定字符。               |

#### **(3)** **公用参数：**export

| **序号** | **参数**                              | **说明**                                   |
| -------- | ------------------------------------- | ------------------------------------------ |
| 1        | --input-enclosed-by <char>            | 对字段值前后加上指定字符                   |
| 2        | --input-escaped-by <char>             | 对含有转移符的字段做转义处理               |
| 3        | --input-fields-terminated-by <char>   | 字段之间的分隔符                           |
| 4        | --input-lines-terminated-by <char>    | 行之间的分隔符                             |
| 5        | --input-optionally-enclosed-by <char> | 给带有双引号或单引号的字段前后加上指定字符 |

#### **(4)** **公用参数：**hive

| **序号** | **参数**                        | **说明**                                                  |
| -------- | ------------------------------- | --------------------------------------------------------- |
| 1        | --hive-delims-replacement <arg> | 用自定义的字符串替换掉数据中的\r\n和\013 \010等字符       |
| 2        | --hive-drop-import-delims       | 在导入数据到hive时，去掉数据中的\r\n\013\010这样的字符    |
| 3        | --map-column-hive <arg>         | 生成hive表时，可以更改生成字段的数据类型                  |
| 4        | --hive-partition-key            | 创建分区，后面直接跟分区名，分区字段的默认类型为string    |
| 5        | --hive-partition-value <v>      | 导入数据时，指定某个分区的值                              |
| 6        | --hive-home <dir>               | hive的安装目录，可以通过该参数覆盖之前默认配置的目录      |
| 7        | --hive-import                   | 将数据从关系数据库中导入到hive表中                        |
| 8        | --hive-overwrite                | 覆盖掉在hive表中已经存在的数据                            |
| 9        | --create-hive-table             | 默认是false，即，如果目标表已经存在了，那么创建任务失败。 |
| 10       | --hive-table                    | 后面接要创建的hive表,默认使用MySQL的表名                  |
| 11       | --table                         | 指定关系数据库的表名                                      |



公用参数介绍完之后，我们来按照命令介绍命令对应的特有参数。



## 8.4 Sqoop数据的导入

​	[掌握Sqoop数据的导入]()，能够独立完成数据准备

### 8.4.1 数据准备

​	在MySQL中创建数据库[sqoop_db]()。

​	并且在该数据库中创建数据表[emp]()、[emp_add]()和[emp_conn]()，然后向这3个表中插入数据。

​	接下来，我们通过一个SQL脚本文件[sqoop_db.sql]()来完成创建数据表和向表中插入数据的相关操作。



1. 准备[sqoop_db.sql]()的SQL脚本文件文件

   ​	打开[sqoop_db.sql]()

   ```shell
   vi /opt/software/sqoop_db.sql
   ```

   <img src="./第8章 Sqoop数据迁移.assets/image-20231211170847486.png" alt="image-20231211170847486" />

   ​	复制以下语句到文件中

   ```mysql
   -- 使用数据库sqoop_db
   USE sqoop_db;
   
   -- 创建数据表emp的SQL语句如下
   DROP TABLE IF EXISTS `emp`;
   CREATE TABLE `emp` (
     `id` int(11) NOT NULL,
     `name` varchar(100) DEFAULT NULL,
     `deg` varchar(100) DEFAULT NULL,
     `salary` int(11) DEFAULT NULL,
     `dept` varchar(10) DEFAULT NULL,
     PRIMARY KEY (`id`)
   );
   
   -- 向数据表emp中插入数据
   INSERT INTO `emp` VALUES ('1201', 'xiaozhang', 'manager', '50000', 'TP');
   INSERT INTO `emp` VALUES ('1202', 'xiaosan', 'Proof reader', '50000', 'TP');
   INSERT INTO `emp` VALUES ('1203', 'xiaosi', 'php dev', '30000', 'AC');
   INSERT INTO `emp` VALUES ('1204', 'xiaowu', 'php dev', '30000', 'AC');
   INSERT INTO `emp` VALUES ('1205', 'xiaoer', 'admin', '20000', 'TP');
   INSERT INTO `emp` VALUES ('1206', 'xiaoliu', 'manager', '55000', 'AC');
   INSERT INTO `emp` VALUES ('1207', 'xiaoya', 'Proof reader', '52000', 'TP');
   INSERT INTO `emp` VALUES ('1208', 'xiaojia', 'php dev', '31000', 'AC');
   INSERT INTO `emp` VALUES ('1209', 'xiaoxiao', 'php dev', '31000', 'AC');
   INSERT INTO `emp` VALUES ('1210', 'xiaoming', 'admin', '21000', 'TP');
   INSERT INTO `emp` VALUES ('1211', 'xiaohong', 'manager', '52000', 'TP');
   INSERT INTO `emp` VALUES ('1212', 'xiaogang', 'Proof reader', '51000', 'AC');
   INSERT INTO `emp` VALUES ('1213', 'xiaobin', 'php dev', '32000', 'TP');
   INSERT INTO `emp` VALUES ('1214', 'xiaodong', 'php dev', '31000', 'AC');
   INSERT INTO `emp` VALUES ('1215', 'xiaozhao', 'admin', '22000', 'TP');
   INSERT INTO `emp` VALUES ('1216', 'xiaoli', 'manager', '53000', 'AC');
   INSERT INTO `emp` VALUES ('1217', 'xiaofang', 'Proof reader', '53000', 'TP');
   INSERT INTO `emp` VALUES ('1218', 'xiaoxin', 'php dev', '33000', 'AC');
   INSERT INTO `emp` VALUES ('1219', 'xiaoyan', 'php dev', '34000', 'AC');
   INSERT INTO `emp` VALUES ('1220', 'xiaomei', 'admin', '23000', 'TP');
   
   -- 创建数据表emp_add
   DROP TABLE IF EXISTS `emp_add`;
   CREATE TABLE `emp_add` (
     `id` int(11) NOT NULL,
     `hno` varchar(100) DEFAULT NULL,
     `street` varchar(100) DEFAULT NULL,
     `city` varchar(100) DEFAULT NULL,
     PRIMARY KEY (`id`));	
   
   -- 向数据表emp_add中插入数据
   INSERT INTO `emp_add` VALUES ('1201', '288A', 'Guangminglu', 'Guangzhou');
   INSERT INTO `emp_add` VALUES ('1202', '108I', 'Chenggonglu', 'Beijing');
   INSERT INTO `emp_add` VALUES ('1203', '144Z', 'Dadaolu', 'Shenzhen');
   INSERT INTO `emp_add` VALUES ('1204', '78B', 'Xingfulu', 'Beijing');
   INSERT INTO `emp_add` VALUES ('1205', '720X', 'Wenxinlu', 'Beijing');
   INSERT INTO `emp_add` VALUES ('1206', '333C', 'Huayuanjie', 'Shanghai');
   INSERT INTO `emp_add` VALUES ('1207', '456D', 'Changanjie', 'Chongqing');
   INSERT INTO `emp_add` VALUES ('1208', '555E', 'Zijincheng', 'Beijing');
   INSERT INTO `emp_add` VALUES ('1209', '789F', 'Tiananmen', 'Beijing');
   INSERT INTO `emp_add` VALUES ('1210', '666G', 'Nanjinglu', 'Shanghai');
   INSERT INTO `emp_add` VALUES ('1211', '999H', 'Beijinglu', 'Guangzhou');
   INSERT INTO `emp_add` VALUES ('1212', '111I', 'Zhongshanlu', 'Shanghai');
   INSERT INTO `emp_add` VALUES ('1213', '222J', 'Renminlu', 'Chongqing');
   INSERT INTO `emp_add` VALUES ('1214', '333K', 'Xihulu', 'Hangzhou');
   INSERT INTO `emp_add` VALUES ('1215', '444L', 'Huashanlu', 'Xian');
   INSERT INTO `emp_add` VALUES ('1216', '555M', 'Shijiedadao', 'Shanghai');
   INSERT INTO `emp_add` VALUES ('1217', '666N', 'Huangpudaolu', 'Guangzhou');
   INSERT INTO `emp_add` VALUES ('1218', '777O', 'Chaoyanglu', 'Beijing');
   INSERT INTO `emp_add` VALUES ('1219', '888P', 'Wuyilu', 'Wuhan');
   INSERT INTO `emp_add` VALUES ('1220', '123Q', 'Changchenglu', 'Beijing');
   
   -- 创建数据表emp_conn
   DROP TABLE IF EXISTS `emp_conn`;
   CREATE TABLE `emp_conn` (
     `id` int(100) NOT NULL,
     `phno` varchar(100) DEFAULT NULL,
     `email` varchar(100) DEFAULT NULL,
     PRIMARY KEY (`id`));
     
   -- 向数据表emp_conn中插入数据  
   INSERT INTO `emp_conn` VALUES ('1201', '2356742', '11@tp.com');
   INSERT INTO `emp_conn` VALUES ('1202', '1661663', '12@tp.com');
   INSERT INTO `emp_conn` VALUES ('1203', '8887776', '13@ac.com');
   INSERT INTO `emp_conn` VALUES ('1204', '9988774', '14@ac.com');
   INSERT INTO `emp_conn` VALUES ('1205', '1231231', '15@tp.com');
   INSERT INTO `emp_conn` VALUES ('1206', '7654321', '16@tp.com');
   INSERT INTO `emp_conn` VALUES ('1207', '9876543', '17@ac.com');
   INSERT INTO `emp_conn` VALUES ('1208', '5432167', '18@ac.com');
   INSERT INTO `emp_conn` VALUES ('1209', '6789098', '19@tp.com');
   INSERT INTO `emp_conn` VALUES ('1210', '3456789', '20@tp.com');
   INSERT INTO `emp_conn` VALUES ('1211', '8765432', '21@ac.com');
   INSERT INTO `emp_conn` VALUES ('1212', '2345678', '22@ac.com');
   INSERT INTO `emp_conn` VALUES ('1213', '4567890', '23@tp.com');
   INSERT INTO `emp_conn` VALUES ('1214', '5678901', '24@tp.com');
   INSERT INTO `emp_conn` VALUES ('1215', '7890123', '25@ac.com');
   INSERT INTO `emp_conn` VALUES ('1216', '9870123', '26@ac.com');
   INSERT INTO `emp_conn` VALUES ('1217', '6543210', '27@tp.com');
   INSERT INTO `emp_conn` VALUES ('1218', '1234567', '28@tp.com');
   INSERT INTO `emp_conn` VALUES ('1219', '8765430', '29@ac.com');
   INSERT INTO `emp_conn` VALUES ('1220', '4321098', '30@ac.com');
   ```

   <img src="./第8章 Sqoop数据迁移.assets/image-20231211170445405.png" alt="image-20231211170445405" style="zoom:50%;" />

2. 打开MySQL

   ```shell
   mysql -uroot -pPassword123$
   ```

   <img src="./第8章 Sqoop数据迁移.assets/image-20231211165022579.png" alt="image-20231211165022579" style="zoom: 67%;" />

3. 创建数据库[sqoop_db]()

   ```mysql
   mysql> create database sqoop_db;
   ```

   <img src="./第8章 Sqoop数据迁移.assets/image-20231211165037106.png" alt="image-20231211165037106" />

4. 进入[sqoop_db]()

   ```mysql
   mysql> use sqoop_db;
   ```

   ![image-20231211165051488](./第8章 Sqoop数据迁移.assets/image-20231211165051488.png)

5. 执行SQL脚本文件sqoop_db.sql，命令如下。

   ```mysql
   mysql> source /opt/software/sqoop_db.sql;
   ```

   <img src="./第8章 Sqoop数据迁移.assets/image-20231211170623021.png" alt="image-20231211170623021" />

6. 查看数据库sqoop_db的所有表。

   ```mysql
   mysql> show tables;
   ```

   <img src="./第8章 Sqoop数据迁移.assets/image-20231211171033375.png" alt="image-20231211171033375" style="zoom:80%;" />

7. MySQL开启远程访问权限（[Hive已经做过，无需再做]()）

   ​	赋予root用户对所有数据库所有表的所有权限，且任何地址都能建立连接“%”（即具有远程连接功能），并具有授予权。

   ```mysql
   mysql>grant all privileges on *.* to 'root'@'%'identified by 'Password123$' with grant option;
   ```

   ​	刷新权限

   ```mysql
   mysql>flush privileges;
   ```

   

8. 退出mysql

   ```mysql
   mysql>exit;
   ```

   ​	

### 8.4.2 Sqoop列出MySQL表单

​	用sqoop命令列出数据库[sqoop_db]()中的所有表格

```shell
sqoop list-tables \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$
```

**sqoop list-tables**是列出表单命令

​	其中每个选项的意义为：

- **--connect** 	指定JDBC连接MySQL的URL地址
- **--username**      指定登录MySQL的用户名
- **--password**       指定登录MySQL的密码

<img src="./第8章 Sqoop数据迁移.assets/image-20231211172035417.png" alt="image-20231211172035417" style="zoom: 67%;" />



### 8.4.3 MySQL导入HDFS

​	将MySQL的数据库[sqoop_db中数据表emp]()的数据导入到HDFS的[/sqoop](/sqoop)目录，命令如下。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp \
--columns id,name,deg,salary,dept \
--target-dir /sqoop \
--num-mappers 1
```

**sqoop import** 是导入命令

​	其中每个选项的意义为：

- **`--connect`** 	指定JDBC连接MySQL的URL地址
- **`--username`**      指定登录MySQL的用户名
- **`--password`**       指定登录MySQL的密码
- **`--table`**                指定MySQL中的表
- **`--columns`**          指定表的字段
- **`--target-dir`**       指定数据导入到HDFS的目录，该目录无须手动创建
- **`--num-mappers`**    指定MapTask的个数



<img src="./第8章 Sqoop数据迁移.assets/image-20231211172549236.png" alt="image-20231211172549236" />

​	

​	导入过程只调用了[MapTask]()的，而没有调用[ReduceTask]()。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211172642736.png" alt="image-20231211172642736" style="zoom:50%;" />



​	通过HDFS的WEB UI界面，查看[MySQL]()数据导入到HDFS的结果文件。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211172946617.png" alt="image-20231211172946617" />



​	在虚拟机master执行“hdfs dfs -cat /sqoop/part-m-00000”命令查看结果文件内容。

```shell
hdfs dfs -cat /sqoop/part-m-00000
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211173138145.png" alt="image-20231211173138145" style="zoom:67%;" />



### 8.4.4 MySQL增量导入到HDFS

#### (1) 准备工作

​	向MySQL新增一条记录（[请自己打开MySQL]()）

​	向数据表emp插入一条数据，登录虚拟机Master的MySQL，执行如下命令。

```mysql
mysql> insert into sqoop_db.emp 
    -> values ('1221','lcvc','java dev','15000','AC');
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211191428704.png" alt="image-20231211191428704" style="zoom:80%;" />



#### (2) 增量导入

​	在进行增量导入操作时，必须在import命令中指定[--check-column]()参数，用来检查数据表的字段，从而确定哪些字段的数据需要执行增量导入。

​	例如：

1. 在执行[append模式]()增量导入时，通常会将--check-column参数指定为拥有[自增且唯一约束的字段]()，如主键ID；
2. 而执行[lastmodified模式]()增量导入时，通常会将--check-column参数指定为[日期时间类型的字段]()，如date或timestamp类型的字段。



​	通过Sqoop的import命令实现[基于append模式]()的增量导入操作，将表emp新增的数据导入到HDFS的/sqoop目录，命令如下。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp \
--columns id,name,deg,salary,dept \
--target-dir /sqoop \
--num-mappers 1 \
--incremental append \
--check-column id \
--last-value 1220
```

​	其中每个选项的意义为：

- **`--incremental`** [append]()   	指定增量导入模式为append模式
- **`--check-column`**                     指定字段实现增量导入
- **`--last-value`**                              指定根据字段的指定值进行增量导入

<img src="./第8章 Sqoop数据迁移.assets/image-20231211202338157.png" alt="image-20231211202338157" style="zoom:67%;" />

​	可以看到它会去找到[last-value]()

![image-20231211202735436](./第8章 Sqoop数据迁移.assets/image-20231211202735436.png)



 	在HDFS的Web UI，访问sqoop目录，查看生成新的结果文件。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211195753231.png" alt="image-20231211195753231" style="zoom: 50%;" />



​		执行“hdfs dfs -cat /sqoop/part-m-00001”命令查看该结果文件的内容。

```shell
hdfs dfs -cat /sqoop/part-m-00001
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211202943025.png" alt="image-20231211202943025" style="zoom:80%;" />



### 8.4.5 MySQL导入到Hive

​	MySQL导入Hive是指通过Sqoop的[import]()命令，将MySQL中指定数据表的数据[导入到Hive的指定表]()进行存储。

#### (1) 准备工作

​	将Hive安装目录中lib目录下的[hive-common-2.0.0.jar]()复制到Sqoop安装目录的[lib]()目录下。([8.1.8已做过，无需再做]())

```shell
cp /usr/local/src/hive/lib/hive-common-2.0.0.jar /usr/local/src/sqoop/lib/
```

​	打开hive，并创建[hive_sqoop_dp]()

```shell
hive
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211203556062.png" alt="image-20231211203556062" style="zoom:50%;" />

```hive
hive> create database hive_sqoop_db;
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211204021515.png" alt="image-20231211204021515" />

#### (2) 导入到Hive

​	将MySQL中数据表emp的数据，导入到Hive中数据库[hive_sqoop_dp]()的表[emp_sqoop]()。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp \
--columns id,name,deg,salary,dept \
--hive-table hive_sqoop_db.emp_sqoop \
--create-hive-table \
--hive-import \
--num-mappers 1
```

​	其中各个选项的意义为：

- **`--hive-table`**                    指定Hive中的数据库hive_sqoop_db，以及数据库中的表emp_sqoop
- **`--create-hive-table`**      指定[自动]()在Hive中创建表emp_sqoop（根据关系型数据库的表结构）
- **`--hive-import`**                 实现将MySQL中指定数据表的数据映射到Hive的指定表中



<img src="./第8章 Sqoop数据迁移.assets/image-20231211204418107.png" alt="image-20231211204418107" style="zoom:67%;" />

​	我们可以进入hive查看[hive_sqoop_db.emp_sqoop]()，发现数据已经被导入到Hive中

<img src="./第8章 Sqoop数据迁移.assets/image-20231211204606058.png" alt="image-20231211204606058" />

​	同时，该表的字段名与数据类型也是和MySQL的表是一致的。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211204720603.png" alt="image-20231211204720603" style="zoom: 80%;" />





​	接下来，我们完成对另外两个表的导入。

​	将MySQL中数据表emp_add的数据，导入到Hive中数据库[hive_sqoop_dp]()的表[emp_add_sqoop]()。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp_add \
--columns id,hno,street,city \
--hive-table hive_sqoop_db.emp_add_sqoop \
--create-hive-table \
--hive-import \
--num-mappers 1
```

​	



​	将MySQL中数据表emp_conn的数据，导入到Hive中数据库[sqoop_dp]()的表[emp_conn_sqoop]()。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp_conn \
--columns id,phno,email  \
--hive-table hive_sqoop_db.emp_conn_sqoop \
--create-hive-table \
--hive-import \
--num-mappers 3
```

​	第3个导入时，我把**--num-mappers**改成了3，所以该导入会调用3个MapTask。

<img src="./第8章 Sqoop数据迁移.assets/image-20231211205538168.png" alt="image-20231211205538168" style="zoom:67%;" />

​	同时，由于有3个MatTask进行数据加载，所以我们最终的数据被分成了3部分

<img src="./第8章 Sqoop数据迁移.assets/image-20231211210737431.png" alt="image-20231211210737431" style="zoom: 50%;" />





###  8.4.6 MySQL过滤导入HDFS

​	MySQL过滤导入HDFS是指对MySQL指定表的数据[进行过滤]()，将符合要求的数据导入到HDFS的[指定目录]()。

​	这是因为在实际开发过程中，有些时候开发人员只会针对MySQL指定表的[部分数据]()进行导入操作，从而避免对全表进行导入操作非常耗时。



Sqoop提供了两种对表的数据进行过滤的方式。

1. 一种是在import命令中添加参数[--where]()。
2. 另一种是在import命令中添加参数[--query]()

<img src="./第8章 Sqoop数据迁移.assets/image-20231211211026149.png" alt="image-20231211211026149" style="zoom: 80%;" />

#### (1) --where过滤

​	MySQL中表emp_add的字段[city的值等于beijing]()的数据导入到HDFS的[/sqoop/city_beijing](/sqoop/city_beijing)目录，具体命令如下。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp_add \
--columns id,hno,street,city \
--where "city = 'beijing'" \
--target-dir /sqoop/city_beijing \
--num-mappers 1
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211211239914.png" alt="image-20231211211239914" style="zoom:67%;" />



<img src="./第8章 Sqoop数据迁移.assets/image-20231211211327839.png" alt="image-20231211211327839" style="zoom:67%;" />

​	在虚拟机master执行“hdfs dfs -cat /sqoop/city_beijing/part-m-00000”命令查看结果文件的数据。

```shell
hdfs dfs -cat /sqoop/city_beijing/part-m-00000
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211211501620.png" alt="image-20231211211501620" />



#### (2) --query过滤

​	查询MySQL中数据表emp的字段[id、name和deg]()，并且[过滤掉字段id的值小于1215]()的数据。

​	将最终的查询结果导入到HDFS的[/sqoop/id_1203](/sqoop/id_1203)目录，具体命令如下。

```shell
sqoop import \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--target-dir /sqoop/id_1215 \
--query 'select id,name,deg from emp where id > 1215 and $CONDITIONS' \
--num-mappers 1
```

​	其中[--query]()的查询语句后面必须加上`and $CONDITIONS`

<img src="./第8章 Sqoop数据迁移.assets/image-20231211212303435.png" alt="image-20231211212303435" />



​	在虚拟机master执行“hdfs dfs -cat /sqoop/id_1215/part-m-00000”命令查看结果文件的数据。

```shell
hdfs dfs -cat /sqoop/id_1215/part-m-00000
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231211212332474.png" alt="image-20231211212332474" />





#### (3) 使用--query参数的注意事项

1. 如果在import命令中没有指定参数[--num-mappers]()，那么必须在命令中添加[--split-by]()参数，并且指定参数值为MySQL的数据表中具有唯一约束的字段，如主键ID。

   [--split-by]()参数的作用是根据指点字段的值将查询结果交由[多个MapTask]()进行处理实现[导入数据]()的操作。

   

2. 如果参数--query指定的SELECT语句中包含WHERE子句，那么必须通过关键字[AND与$CONDITIONS]()占位符连接。

   

3. 如果参数--query指定的SELECT语句用[双引号（" "）]()包裹，而不是单引号（' '）包裹，那么$CONDITIONS占位符前需要添加“\”，即\$CONDITIONS。







## 8.5 Sqoop数据的导出

​	Sqoop数据的导出是导入的反向操作，也就是将Hadoop的数据[导出]()到关系型数据库的数据表中。

​	在执行导出操作之前，[关系型数据库的指定数据表必须已经存在]()，否则导出操作会执行失败。



​	在上一章节，我们已经学习了如何用Hive对数据集进行分析，并得到对应的分析结果。

​	比如我们对[emp_add]()进行分析，分析每个城市有多少名员工。

<img src="./第8章 Sqoop数据迁移.assets/image-20231212095335073.png" alt="image-20231212095335073" />

<img src="./第8章 Sqoop数据迁移.assets/image-20231212095417722.png" alt="image-20231212095417722" />

​	但这个结果并不能被使用，因为[Hive不是数据库]()，目前主流的应用程序都是[连接关系型数据库]()的，所以我们要把分析的结果[导出]()到MySQL中。



### (1) Hive分析

​	[进入Hive略]()

​	对[emp_add]()进行分析，分析每个城市有多少名员工

​	将Hive分析的结果存放进一个表 [emp_city_count]()

```HIVE
CREATE TABLE hive_sqoop_db.emp_city_count
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '|' 
LINES TERMINATED BY '\n' 
STORED AS TEXTFILE
LOCATION '/sqoop/hive_count/' -- 指定存放路径
AS
SELECT
     city,
     count(*) as city_count
FROM
     hive_sqoop_db.emp_add_sqoop
GROUP BY city;
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231212102234672.png" alt="image-20231212102234672" style="zoom:50%;" />



​	查完Web UI，在指定的HDFS路径产生了相对应的数据文件。

<img src="./第8章 Sqoop数据迁移.assets/image-20231212102351052.png" alt="image-20231212102351052" style="zoom:80%;" />



### (2) 在MySQL中创建对应表单

​	[进入MySQL略]()

```mysql
CREATE TABLE sqoop_db.emp_city_conut (
    `city` varchar(255),
    `city_count` int,
    PRIMARY KEY (`city`)
    )
    ;
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231212101005708.png" alt="image-20231212101005708" />





### (3) 导出数据

​	通过`sqoop export`命令将Hive的分析结果导出到MySQL中

```shell
sqoop export \
--connect jdbc:mysql://master:3306/sqoop_db \
--username root \
--password Password123$ \
--table emp_city_conut \
--columns city,city_count \
--export-dir /sqoop/hive_count/ \
--input-fields-terminated-by "|" \
--update-mode allowinsert \
--num-mappers 3
```

​	其中每个选项的意义为：

- **`--export-dir`**：这个参数指定了导出数据的目录或路径。
- **`--input-fields-terminated-by`**：这个参数用于指定输入文件中字段之间的分隔符。
- **`--update-mode allowinsert`**：如果目标MySQL表中已经存在对应的记录，则更新这些记录；如果不存在，则插入新记录。

<img src="./第8章 Sqoop数据迁移.assets/image-20231212105146991.png" alt="image-20231212105146991" style="zoom:67%;" />



### (4) 查看MySQL

​	在虚拟机Master登录MySQL，并执行“select * from sqoop_db.emp_city_conut;”命令查询数据表[emp_city_conut]()的数据。

```hive
select * from sqoop_db.emp_city_conut;
```

<img src="./第8章 Sqoop数据迁移.assets/image-20231212103126299.png" alt="image-20231212103126299" style="zoom:67%;" />



​	分析结果已经被导入到MySQL中，接下来就可以被应用程序所使用（[数据可视化课程学习内容]()）
