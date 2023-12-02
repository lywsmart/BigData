### 1. 工业机器数据分析

#### **一、案例背景**

   **你是一家大型制造公司的数据分析师。该公司拥有数百台机器，这些机器在生产过程中生成大量的运行数据。这些数据包括每台机器的运行时间、停机时间、故障次数、产出数量等。数据以每小时为单位收集，并存储在Hadoop文件系统中。**

#### **二、数据集**

工业数据样本如图所示：

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126161236869.png" alt="image-20231126161236869" style="zoom:80%;" />

其中字段说明如下：

| 字段名       | 说明                           |
| ------------ | :----------------------------- |
| machine_id   | 机器的唯一标识符               |
| timestamp    | 数据记录的时间戳               |
| running_time | 机器的运行时间（小时）         |
| downtime     | 机器的停机时间（小时）         |
| failures     | 在该时间段内机器发生的故障次数 |
| output       | 产出数量                       |

#### **三、Hive查询任务**

1. 哪台机器的平均停机时间最长？
2. 哪台机器的故障率最高？
3. 总产出量最高的前三台机器是哪些？
4. 哪些机器在过去一个月内故障次数最多？
5. 过去一年内，哪个季度的平均产出量最高？

#### **四、实验步骤**

1. 首先将industrial_machine_data.csv数据上传到master节点/root/目录下，如下图所示

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126163234663.png" alt="image-20231126163234663" style="zoom:80%;" />

2. 在maseter节点启动hive

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126163621547.png" alt="image-20231126163621547" style="zoom:80%;" />

3. 在hive默认数据库中defalut创建一张**machine**表，建表语句如下：

```sql
CREATE TABLE IF NOT EXISTS machine (
    machine_id STRING,
    time STRING,
    running_time DOUBLE,
    downtime DOUBLE,
    failures INT,
    output DOUBLE
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ','
STORED AS TEXTFILE;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126170228940.png" alt="image-20231126170228940" style="zoom:80%;" />

4. 加载数据industrial_machine_data.csv到表machine里面

```sql
load data local inpath " /root/industrial_machine_data.csv" into table machine;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126165342984.png" alt="image-20231126165342984" style="zoom:80%;" />

5. 查看表machine数据

```sql
select * from machine
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126170438220.png" alt="image-20231126170438220" style="zoom:80%;" />

6. 完成案例任务

（1）哪台机器的平均停机时间最长？

```sql
SELECT machine_id, AVG(downtime) AS average_downtime
FROM machine
GROUP BY machine_id
ORDER BY average_downtime DESC
LIMIT 1;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126171153428.png" alt="image-20231126171153428" style="zoom:80%;" />

(2)哪台机器的故障率最高？

```sql
SELECT machine_id, AVG(failures / running_time) AS average_failure_rate
FROM machine
GROUP BY machine_id
ORDER BY average_failure_rate DESC
LIMIT 1;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126171932518.png" alt="image-20231126171932518" style="zoom:80%;" />

(3)总产出量最高的前三台机器是哪些？

```sql
SELECT machine_id, SUM(output) AS total_output
FROM machine
GROUP BY machine_id
ORDER BY total_output DESC
LIMIT 3;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126172327159.png" alt="image-20231126172327159" style="zoom:80%;" />

(4)找出过去一个月内故障次数最多的机器？

```sql
-- 假设当前日期是2023-01-01
SELECT machine_id, SUM(failures) AS total_failures
FROM machine
WHERE time >= '2022-12-01' AND time < '2023-01-01'
GROUP BY machine_id
ORDER BY total_failures DESC
LIMIT 1;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126172633273.png" alt="image-20231126172633273" style="zoom:80%;" />

(5)过去一年内，哪个季度的平均产出量最高？

```sql
SELECT CONCAT(YEAR(time), 'Q', CEIL(MOTHN(time)/3) AS quarter, AVG(output) AS average_output
FROM machine
GROUP BY CONCAT(YEAR(time), 'Q', QUARTER(time))
ORDER BY quarter;
```

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231126173150038.png" alt="image-20231126173150038" style="zoom:80%;" />



### 2. 某电商网站用户行为分析

#### 一、案例背景

​	你是一家电子商务网站的数据分析师。该网站每天有成千上万的用户访问和交易，这些活动产生了海量的用户行为数据。这些数据涵盖了用户的浏览路径、搜索查询、点击行为、购物车操作、购买历史等。

​	所有这些数据都以实时流的形式被捕捉，并存储在一个大型的数据仓库中。

​	你的任务是通过这些数据来分析用户行为模式，提供个性化的购物体验，优化用户的转化率，以及预测市场趋势。



#### 二、数据集

​	本实训的数据是采集电商网站的用户行为数据，主要包含用户的4种行为：[搜索]()、[点击]()、[下单]()和[支付]()。

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127091821316.png" alt="image-20231127091821316" />

- 数据采用"_"分割字段
- 每一行表示用户的一个行为，所以每一行只能是[四种行为中的一种]()
- 如果点击的品类id和产品id是[-1表示这次不是点击]()
- 针对下单行为，一次可以下单多个产品，所以品类id和产品id都是多个，id之间使用[逗号(,)]()分割。
- 如果本次不是下单行为，则他们相关数据用null来表示。
- 支付行为[和下单行为格式类似]()



数据详细字段说明

| **编号** |      **字段名称**      | **字段类型** |          **字段含义**          |
| :------: | :--------------------: | :----------: | :----------------------------: |
|    1     |       event_date       |    String    |       用户点击行为的日期       |
|    2     |        user_id         |     Long     |            用户的ID            |
|    3     |       session_id       |    String    |          Session的ID           |
|    4     |        page_id         |     Long     |          某个页面的ID          |
|    5     |      action_time       |    String    |          动作的时间点          |
|    6     |     search_keyword     |    String    |        用户搜索的关键词        |
|    7     | [click_category_id]()  |     Long     |   [点击某一个商品品类的ID]()   |
|    8     |    click_product_id    |     Long     |         某一个商品的ID         |
|    9     | [order_category_ids]() |    Array     | [一次订单中所有品类的ID集合]() |
|    10    |   order_product_ids    |    Array     |   一次订单中所有商品的ID集合   |
|    11    |  [pay_category_ids]()  |    Array     | [一次支付中所有品类的ID集合]() |
|    12    |    pay_product_ids     |    Array     |   一次支付中所有商品的ID集合   |
|    13    |        city_id         |     Long     |            城市  id            |





#### 三、Hive查询任务

<img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127093727154.png" alt="image-20231127093727154" style="zoom: 80%;" />

​	需求说明：品类是指产品的分类，大型电商网站品类分多级，咱们的项目中品类只有一级，不同的公司可能对热门的定义不一样。

​	我们按照[每个品类]()的点击、下单、支付的量（次数）来统计热门品类。

```
		鞋         点击数 下单数 支付数

		衣服       点击数 下单数 支付数

		电脑       点击数 下单数 支付数
```

​	例如，[综合排名 = 点击数*20% + 下单数*30% + 支付数*50%]()



​	本项目需求优化为：[先按照点击数排名，靠前的就排名高；如果点击数相同，再比较下单数；下单数再相同，就比较支付数。]()



#### 四、实验步骤

1. 首先将[user_visit_action.txt]()数据上传到master节点[/root/](/root/)目录下，如下图所示

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127094029963.png" />

2. 在maseter节点启动hive

   ​	执行命令

   ```hive
   hive
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127094146204.png" alt="image-20231127094146204" />

3. 创建一个user_action_db数据库，并进入该数据库

   ​	创建user_action_db：

   ```hive
   create database if not exists user_action_db;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127094456895.png" alt="image-20231127094456895" />

   ​	进入user_action_db：

   ```hive
   use user_action_db;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127094618185.png" alt="image-20231127094618185" />

4. 在用户行为数据库user_action_db中创建一张**user_behavior**表，建表语句如下：

   ```hive
   CREATE TABLE user_behavior (
       event_date DATE,
       user_id BigInt,
       session_id STRING,
       page_id BigInt,
       action_time TIMESTAMP,
       search_keyword STRING,
       click_category_id BigInt,
       click_product_id BigInt,
       order_category_ids ARRAY<String>,
       order_product_ids ARRAY<String>,
       pay_category_ids ARRAY<String>,
       pay_product_ids ARRAY<String>,
       city_id BigInt
   )
   ROW FORMAT DELIMITED
   FIELDS TERMINATED BY '_'
   COLLECTION ITEMS TERMINATED BY ','
   STORED AS TEXTFILE;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127094927093.png" alt="image-20231127094927093" style="zoom:67%;" />

   [PS:用ARRAY\<String>的目的是方便转换空值]()

   

5. 加载数据user_visit_action.txt到表user_behavior里面

   ```hive
   load data local inpath "/opt/software/user_visit_action.txt" into table user_behavior;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127095311691.png" alt="image-20231127095311691" />

6. 查看表user_behavior数据

   ```hive
   select * from user_behavior limit 50;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127095503265.png" alt="image-20231127095503265" />

7. [将null字符串数据转换成Hive的NULL类型（重难点）]()

   ```hive
   INSERT OVERWRITE TABLE user_behavior
   SELECT 
       event_date,
       user_id,
       session_id,
       page_id,
       action_time,
       CASE WHEN search_keyword = 'null' THEN NULL ELSE search_keyword END,
       CASE WHEN click_category_id = -1 THEN NULL ELSE click_category_id END,
       CASE WHEN click_product_id = -1 THEN NULL ELSE click_product_id END,
       CASE WHEN order_category_ids[0] = 'null' THEN ARRAY() ELSE order_category_ids END,
       CASE WHEN order_product_ids[0] = 'null' THEN ARRAY() ELSE order_product_ids END,
       CASE WHEN pay_category_ids[0] = 'null' THEN ARRAY() ELSE pay_category_ids END,
       CASE WHEN pay_product_ids[0] = 'null' THEN ARRAY() ELSE pay_product_ids END,
       city_id
   FROM user_behavior;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127100535471.png" alt="image-20231127100535471" style="zoom:67%;" />

   再次查询user_behavior表：

   ```hive
   select * from user_behavior limit 50;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127100622606.png" alt="image-20231127100622606" style="zoom:67%;" />

8. 查询热门品类

   根据以下规则对[品类]()进行排名

   ```
   品类是指产品的分类，大型电商网站品类分多级，咱们的项目中品类只有一级，不同的公司可能对热门的定义不一样。
   
   我们按照每个品类的点击、下单、支付的量（次数）来统计热门品类。
   
   综合排名 = 点击数*20% + 下单数*30% + 支付数*50%
   
   本项目需求优化为：先按照点击数排名，靠前的就排名高；如果点击数相同，再比较下单数；下单数再相同，就比较支付数。
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127091821316.png" alt="image-20231127091821316" style="zoom:67%;" />

   

   (1)使用 Hive 的 `LATERAL VIEW` 和 `explode` 以及`统计函数`。计算出[点击]()、[下单]()、[支付]()的数量，并分别存入三个临时表。

   ​	[PS：其实这里可以直接用子查询完成，但代码复杂，不方便教学，故使用临时表方式]()

   

   ​	创建品类点击统计临时表

   ```hive
   CREATE TEMPORARY TABLE temp_click_count AS
   SELECT 
       click_category_id,
       count(*) as click_count
   FROM 
       user_behavior
   GROUP BY
   	click_category_id;
   ```

   ​	创建品类下单统计临时表

   ```hive
   CREATE TEMPORARY TABLE temp_order_count AS
   SELECT 
       order_category_id,
       count(*) as order_count
   FROM 
       user_behavior
       LATERAL VIEW explode(order_category_ids) t AS order_category_id
   GROUP BY
   	order_category_id;
   ```

   ​	创建品类支付统计临时表

   ```hive
   CREATE TEMPORARY TABLE temp_pay_count AS
   SELECT 
       pay_category_id,
       count(*) as pay_count
   FROM 
       user_behavior
       LATERAL VIEW explode(pay_category_ids) t AS pay_category_id
   GROUP BY
   	pay_category_id;
   ```

   

   (2) 连接三个统计表，并根据排名规则统计出热门品类

   ```hive
   SELECT 
   	click_category_id
       (click_count * 0.2 + order_count * 0.3 + pay_count * 0.5) as rank,
       click_count,
       order_count,
       pay_count    
   FROM 
       temp_click_count
   INNER JOIN 
   	temp_order_count
   	ON
   		temp_click_count.click_category_id = temp_order_count.order_category_id
   INNER JOIN
   	temp_pay_count
   	ON
   		temp_click_count.click_category_id = temp_pay_count.pay_category_id
   ORDER BY
   	rank DESC
   	;
   ```

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127112822542.png" alt="image-20231127112822542" style="zoom:50%;" />

   最终热门品类的排名如下：

   <img src="/media/smart/ssd/github/BigData/大数据基础/第7章 Hive数据仓库.assets/image-20231127112907185.png" alt="image-20231127112907185" style="zoom:67%;" />

   我们可以从查询结果得知，[7号品类的商品是最热门的]()。