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

## 3.2 练习

1. 在linux系统/home目录下创建文件test.txt，添加以下内容

```
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