# Redis 深度历险核心原理与应用实践

## 第一篇：基础和应用篇

### 1.1 简介

+ Redis：Remote Dictionary Service 远程字典服务
+ 存储中间件
+ 优点：性能强悍、文档完整、源码简洁易懂、支持丰富客户端；
+ 用途：
    + 缓存；（与 Memcache 对比更易理解、使用和控制）
    + 分布式锁；
+ 细分应用范围：
    1. 记录帖子的点赞数、评论数、点击数、浏览数等；（hash）
    2. 记录用户帖子的列表（排序），便于快速显示用户的帖子列表；（zset）
    3. 记录帖子的标题、摘要、作者、封面等信息，用于帖子列表页的展示；（hash）
    4. 记录帖子点赞用户 ID 列表、评论 ID 列表、用于显示和去重计数；（zset）
    5. 缓存近期热帖内容（帖子内容的空间占用空间较大），减少数据库的压力；（hash）
    6. 记录帖子的相关文章 ID，根据内容推介相关帖子；（list）
    7. 若帖子的 ID 是整数递增的，可使用 Redis 来分配帖子的 ID；（计数器）
    8. 收藏集和帖子之间的关系；（zset）
    9. 记录热榜帖子 ID 列表、总热榜和分类热榜；（zset）
    10. 缓存用户历史行为，过滤恶意行为；（zset、hash）
 
### 1.2 基本数据结构

#### string 字符串

+ Redis 中所有数据结构均以**唯一的 key 字符串作名称**，通过这唯一的 key 获取对应的 value 数据；不同类型的数据结构的差异在于**value 结构的不同**；
+ 最简单的数据结构，用途十分广泛，常见于缓存用户信息；
+ 内部结构：
    + 字符数组；
    + 内容及大小可变；
    + 结构类似 Java 中的 ArrayList 集合；
    + 采用预分配冗余空间的方式来减少内存的频繁分配；
    + 实际分配空间 capacity > 实际字符串长度 len；
    + 拓展逻辑：
        + 当 len < 1MB，扩容成倍扩展；
        + 当 len > 1MB，每次仅扩 1MB；
        + 字符串的最大长度为 512MB；
+ 拓展：字符串由多个字节组成，每个字节又由 8 个 bit 组成； --> 一个字符串由很多 bit 组合 --> bitmap 位图
        
```shell script
# 键值对
> set key value #设值，成功返“OK”
> get key #取值，若成功返回该value，失败返“nil”
> exists key #判断该值是否存在，若存在返“1”，不存在返“0”
> del key #删除该值，成功返“1”，失败返“0”

# 批量键值对
> mset key1 value1 key2 value2 key3 value3 # 批量设值
> mget key1 key2 key3 #批量取值，返回一个列表

# 过期和set命令拓展
> set key value 
> expire key time #time为数字，单位为秒，表示多少秒后过期
> setex key time value #等价于 set + expire 
> setnx key value #若 key 不存在则设值，若存在则不作处理

# 计数
> incr key #key 自增
> incrby key number #为key的值加number
> decr key #key 自减
> decrby key number #为key的值减number
```

#### list 列表

+ 内部结构：
    + 类似 Java 中的 LinkedList，**双向链表**；
    + 插入和删除速度快；索引定位慢，需遍历；
    + 当列表弹出最后一个元素，该数据结构自动删除，内存被回收；
    + 实际上，底层不是简单的 LinkedList，而是“快速链表”的数据结构（quicklist）；
       + 元素少：使用一块连续的内存存储——ziplist（无需双向指针，节省空间）；
       + 元素较多：改用 quicklist（链表和ziplist结合，减少使用双向指针）；
+ 常用于“异步队列”；

```shell script
# 队列：先进先出（常用于消息排队和异步逻辑处理，确保元素的访问顺序）
> rpush key value1 value2 value3 #value 进队
> llen key #统计 key 队列中元素的个数
> lpop key #value 出队

# 栈：后进先出
> rpop key #value3 出队（队列与栈区别在于元素出队的顺序）

# 慢操作（索引元素需全表遍历，所以慢）
> lindex key index #显示 key 列表中第 index 个元素
> lrange key start_key end_index #显示从 start_index 到 end_index 的元素
> ltrim key start_index end_index #保留 start_index 到 end_index 的元素，区间外截断
```

#### hash 字典

+ 内部结构：
    + 类似 Java 中的 HashMap，数组+链表的无序字典；
    + 与 HashMap 的差异：
        + Redis 字典值（key）只能是字符串；
        + HashMap rehash （重新散列）需一次性全部完成；
        + Redis rehash 采用渐进式 rehash 策略；（追求高性能，不阻塞服务）
            + 同时保留新旧两个 hash 结构；
            + 查询时同时查询两个 hash 结构；
            + 后续定时任务或 hash 操作中，循序渐进将旧 hash 的内容迁移至新的 hash 结构中；
            + 完成迁移后，旧的 hash 结构自动被删除，内存被回收；
    + 与 string 字符串的差异：
        + string 字符串需要一次性全部序列化整个对象；（整体获取）
        + hash 字典可以根据每个字段单独获取，但存储消耗高于 string 结构； （可分段按需获取）
+ 常用于存储用户信息等；

```shell script
# string 设值
> set hash "key1:value1;key2:value2;key3:value3"

# hash 设值
> hset hash key1 value1
> hset hash key2 value2
> hset hash key3 value3
> hmset hash key1 value1 key2 value2 key3 value3 #批量设值，等价于上面

# 取值
> hget hash key1
> hgetall hash #entries(), key 和 value 间隔出现
> hexists hash key #判断 hash 中是否存在 key

# 计数（与 string 一样可对 hash 中单个子 key 进行操作）
> hlen hash #统计 hash 中有多少个元素
> hincr hash key #hash 中 key 的 value 自增
> hincrby hash key number #hash 中 key 的 value 加上 number
> hdecr hash key #hash 中 key 的 value 自减
> hdecrby hash key number #hash 中 key 的 value 减去 number

# 删除数据
> hdel hash key #删除 hash 下的 key
> del hash #删除整个 hash
```

#### set 集合

+ 内部结构：
    + 类似 Java 中 HashSet，键值对无序且唯一；
    + 内部实现相当于一个特殊的 hash 字典，只是 hash 中所有 value 均为 NULL；
    + 当 set 集合中最后一个元素被移除后，set 结构自动删除，内存被回收；
+ 常用于去重；

```shell script
# 设值
> sadd key value1
> sadd key value2 value3 #可一次添加多个元素

> smembers key #遍历所有元素

> spop key #随机弹出一个元素

> sismember key value1 #判断某个元素是否存在

> scard key #获取当前 set 的元素的个数

# 集合运算
> sdiff set1 set2 #集合的差集
> sinter set1 set2 #集合的交集
> sunion set1 set2 #集合的并集
```

#### zset 有序集合

+ 内部结构：
    + 类似 Java 中 SortedSet 和 HashMap 的结合体；
        + set：内部 value 唯一；
        + sort：每个 value 均赋予一个 score，代表 value 的**排序权重**；
    + 内部实现：跳跃列表；
    + 当 zset 集合中最后一个元素被移除后，结构被删除，内存被回收；
+ 常用于存储需要排序的列表，例如分数排行版等；

```shell script
# 基本操作
> zadd key socre1 value1 #往有序集合 key 中添加元素 value1
> zadd key socre2 value2
> zcard key #统计有序集和 key 中元素的个数
> zscore key value #获取指定 value 的 score 值
> zrank key value #获取指定 value 的排名
> zrem key value #移除value

#遍历
> zrange key start_index end_index #在 start_index 和 end_index 区间中正序遍历key有序集合
> zrevrange key start_index end_index #在 start_index 和 end_index 区间中逆序遍历key有序集合
> zrangebyscore key start_score end_score #根据score分值区间 (start_score, end_score) 遍历key有序集合
> zrangebyscore key -inf socre withscores #根据score分值区间 (-∞, score] 遍历key有序集合
```    

#### 基本数据结构总结
+ 基本数据结构：
    + string
    + list
    + hash
        + set
        + zset
+ 容器型数据结构：list、set、hash、zset；
+ 规则：
    + create if not exists：容器不存在则创建；
    + drop if no elements：容器中没有元素则销毁容器，释放内存；
+ 过期时间：
    + 所有数据结构均可以设置过期时间；
    + 若过期时间到了，则该对象自动被删除；
    + 若没有设置过期时间，该对象永久存在；
    + 过期时间以对象为单位；如 hash 中某个 key 设置了过期时间，若到期则整个 key 均被删除；

```shell script
# 过期时间
> set key value
> expire key 30 #对象key在30秒后过期
> ttl key #查询对象key的生存时间，-1则表示永久存在
> set key value #若已设置对象key的过期时间，重新set后过期时间会消失
```

### 1.3 分布式锁

+ 本质：“**占坑**”——设值；若该值存在则设值失败（获取锁失败），若该值为空则设值成功（成功获取锁）；悲观锁；
+ Redis 分布式锁实际情况：
    + 加锁：setnx xxx-lock true;（setnx：set if not exists）
    + 释放锁：del xxx-lock;
    + 弊端：若应用在执行过程中出现异常无法调用 del 语句，导致该锁一直被占用，无法释放，造成死锁；
+ 改良方案一：
    + 加锁后给该值设置一个过期时间，那么即使应用运行出现异常也能保证锁能自动释放；
    + 加锁：setnx xxx-lock true;
    + 过期时间：expire xxx-lock time;
    + 释放锁：del xxx-lock;
    + 弊端：因 setnx 和 expire 不是原子操作，难以保证 setnx 执行后 expire 能顺利执行；若 setnx 执行后因某些情况导致 expire 无法执行，那么死锁也可能会出现；
+ 改良方案二：
    + 社区方案：引入插件——分布式锁 library，但实现方式复杂；
    + Redis 2.8 后方案：新增 set 指令的扩展参数，是 setnx 和 expire 可一起执行；
    + 加锁及设置过期时间： set xxx-lock true ex time nx;
    + 语句解析：SET key value [EX seconds] [PX milliseconds] [NX|XX]
        + EX seconds：设置键的过期时间为 second 秒；
        + PX milliseconds：设置键的过期时间为 milliseconds 毫秒；
        + NX：只在键不存在时，才对键进行设置操作；
        + XX：只在键存在时，才对键进行设置操作；
        + SET 语句操作成功完成后返回 “OK”，否则 “nil”；
+ 涉及的问题：
    + 超时问题：Redis 分布式锁不适用于执行时间长的任务；
    + 锁被其他线程释放：为 value 参数设置一个随机数，释放时先比较随机数是否一致；
    + 问题延伸——比较随机数和释放锁不是原子操作：可使用 Lua 脚本保证连续多个指令的原子性执行；
    + 锁可重入性问题：
        + 可重入性：该锁支持同一个线程多次加锁；
        + 实现：需对客户端 set 方法进行包装，使用线程 ThreadLocal 变量存储当前持有锁的计数；
        + [实现Demo](./src/main/java/com/example/redis/RedisWithReentrantLock.java)

### 1.4 延时队列

+ Redis 可充当**简易版**的消息队列中间件；
+ Redis 异步消息队列常用 list 列表，rpush / lpush 操作入队，rpop / lpop 操作出队；
+ 不足地方：不可靠，没有 ack 保证；不支持消息的多播机制；
+ 问题一：队列为空时，客户端一直轮询陷入 pop 的死循环
    + 弊端：客户端一直轮询消耗资源，拉高 Redis 的 QPS；
    + 解决：让线程睡眠，提高客户端轮询的间隔；
+ 问题二：线程睡眠导致消息延迟增大
    + 弊端：若有多个消息，每消费一个消息均需要睡眠等待，导致不必要延迟；
    + 解决：**阻塞读**，blpop / brpop，队列没消息时休眠，一旦有数据后立即唤醒，延迟几乎为0；
+ 问题三：长时间空闲连接被自动断开
    + 弊端：若客户端长时间休眠，超过某个时间限值后，Redis 客户端连接变成闲置连接，闲置过久后服务器主动断开连接，导致再次 blpop / brpop 时抛出异常；
    + 解决：捕捉异常后**重连**；
+ 锁冲突 —— 分布式锁加锁失败处理策略：
    + 直接抛出异常，通知用户稍后处理；（直接舍弃）
    + sleep 一会，然后重试；（等待）
    + 将请求转移至消息队列，排队等待重试；（排队）
+ [延时队列的实现 Demo](./src/main/java/com/example/redis/RedisDelayingQueue.java)
    + 可使用 zset（有序集合）来实现；value - 消息，score - 消息到期处理时间；
    + 具体操作：多个线程轮询 zset 获取到期的任务；

### 1.5 位图

+ 常用于**存储较常规整齐的数据**，如一年的签到记录等；
+ 最小单位：比特 bit；大小自动扩展；设置某个偏移位置若超出现有内容范围则自动将位数组填充0；
+ 位图不是特殊的数据结构，实际上是普通的字符串，也就是**byte数组**（注意：位数组的顺序和字符的位顺序相反）；
+ 可直接使用 get/set 获取整个位图内容（**整存整取**）；也可以使用 getbit/setbit 将 byte 数组当成“位数组”处理（**零存零取**）；
+ 统计与查找：
    + 统计指令：bitcount key [start] [end]：统计指定范围内 1 的个数；
    + 查找指令：bitpos key [0/1] [start] [end]：查询指定范围内出现第一个 0 或 1 的位置；
    + 注意：start 和 end 是字节索引，以 8 的倍数出现；
+ 魔术指令：bitfield
    + Redis 3.2 后新增指令，可一次操作多个位（之前需使用管道），但最多 64 个连续位（超过 64 个连续位需使用多个子指令）；
    + bitfield 一次执行多个子指令；
    + 三个子指令：
        + get
        + set
        + incrby：对指定范围的位进行自增操作；可能出现溢出；
            + Redis 默认处理溢出的方法：折返（出现溢出，溢出部分丢弃）；
            + 溢出策略子指令：overflow
                + wrap 折返（默认）：出现溢出，溢出部分丢弃；
                + fail 失败：报错不执行；
                + sat 截断：超过范围就停留在最大或最少值；

```shell script
# 统计
> bitcount key #全部字符中 1 的位数
> bitcount key 0 0 #第一个字符中 1 的位数
> bitcount key 0 1 #前两个字符中 1 的位数

# 索引
> bitpos key 0 #第一个 0 位
> bitpos key 1 #第一个 1 位
> bitpos key 1 1 1 #从第二个字符算起，第一个 1 位
> bitpos key 1 2 2 #从第三个字符算起，第一个 1 位

# bitfield 魔术指令
> bitfield key get u4 0 #从第一个位开始取 4 个位，结果是无符号数（u）
> bitfield key get u3 2 #从第三个位开始取 3 个位，结果是无符号数（u）
> bitfield key get i4 0 #从第一个位开始取 4 个位，结果是有符号数（i）
> bitfield key get u4 0 get u3 2 get i4 0 #等价上三条语句

> bitfield key set u8 8 97 #从第 9 个位开始，将接下来 8 个位用无符号数 97 替换

> bitfield key incrby u4 2 1 #从第三个位开始，对接下来的 4 位无符号数 + 1
> bitfield key overflow sat incrby u4 2 1
> bitfield key overflow fail incrby u4 2 1
```

### 1.6 HyperLogLog

+ 使用场景：
    + 统计 PV （网站访问量）：Redis 计数器，key 为网站地址 + 时间后缀，value 为访问次数；每次点击累加一次；
    + 精准统计 UV （独立访客）：set 去重集合，将用户唯一标识放入集合，再统计集合元素的个数；不适用于访问量非常大的页面（浪费存储空间）；
    + 大致统计 UV：HyperLogLog；
+ Redis 高级数据结构；提供不精确的去重计数方案，标准误差 0.81%；
+ 基本操作：
    + pfadd：增加计数
    + pfcount：获取计数
    + pfmerge：将多个 pf 计数值累加形成新的 pf 值；
+ 实现原理：todo

### 1.7 布隆过滤器

+ Redis 高级数据结构；Redis 4.0 后以插件的形式提供；存在误判概率，有参数可设置；
+ 特点：判断某元素是否存在于集合时，若判定存在，则可能存在；若判定不存在，则一定不存在；（误差：将不存在误判成存在）
+ 与 HyperLogLog 的区别：HyperLogLog 只有累加统计，不能判断元素是否存在；布隆过滤器整好相反；
+ 使用例子：新闻客户端向用户推介其未阅读的文章；
    + 服务器记录用户所有历史记录，推介时将从中筛选；（当用户量大，每个用户的阅读记录多，系统性能跟不上；）
    + 将历史数据存储在关系数据库中；（频繁的 exists 查询，当系统并发量高时，数据库难以抵御压力）
    + 将历史数据存储至缓存中；（存储空间随时间线性增长，浪费大量存储空间）
    + 使用布隆过滤器判断；（**专门解决**此类问题，去重虽存在误判，但可节省大量存储空间）
+ Docker 镜像：redislabs/rebloom
+ Java 客户端支持的 Jar 包：RedisLabs 提供的 JReBloom（基于 Redis 3.0 以上）、letture；
+ 基本操作：
    + bf.add [key] [value]：添加元素；
    + bf.exists [key] [value]：查询元素是否存在；
    + bf.madd [key] [value1] [value2] ……：可批量添加元素；
    + bf.mexists [key] [value1] [value2] ……:可批量查询元素是否存在；
+ 自定义参数：需在 bf.add 命令执行前使用 bf.reserve 指令显式创建；（不创建则使用默认配置；若 key 存在则报错）
    + bf.reserve 三个参数：
        + key
        + error_rate：错误率；数值越小所需的存储空间越大；默认：0.01；
        + initial_size：预计放入元素的数量；当**小于**实际放入元素数量时，错误率上升；默认100；
    + 注意事项：
        + 使用前需精确估算元素的数量，initial_size 数值不宜过小也不宜过大（过小错误率上升，过大浪费空间）；
        + 在不需要十分精确的场景下可将 error_rate 调高；
+ 布隆过滤器原理：（类似 Java 中 HashMap 的原理）
    + 内部组成：一个大型的位数组 + 几个不一样的**无偏 hash 函数**（可将元素的 hash 算得比较均匀，让元素随机被 hash 映射到位数组中）；
    + 首先，添加元素时，让多个 hash 函数对 value 进行 hash 运算得到多个整数索引值；
    + 然后，将多个整数索引值与位数组长度取模，得到数组下标，并将其置1；
    + 最后，比较 value 是否存在，同样先 hash 运算再取模，得到的数组下标在与位数组的比较，若不全为 1 说明不存在；
    + 所以：当实际元素数量 > 初始值，需尽快重建位数组；（新建再迁移旧数据） 
    
![Redis 布隆过滤器原理图](./img/redis-rebloom-principle.png)

Redis 布隆过滤器原理图（其中 f、g、h 表示无偏 hash 函数）

+ 空间占用估计：
    + 预计放入元素的数量 n，错误率 f，hash 函数最佳数量 k
    + k = 0.7 / (l / n)
    + f = 0.6185 ^ (1 / n)
    + 推导过程：略；
    + 计算器：[https://krisives.github.io/bloom-calculator/](https://krisives.github.io/bloom-calculator/)
+ 使用场景：
    + 爬虫系统对 URL 的去重；
    + NoSQL 数据库的使用，可降低数据库 IO 请求数量；
    + 邮件系统中过滤垃圾邮件；

### 1.8 简单限流

+ 限流：“断尾求生”、“弃卒保车”；
    + 控制流量：在分布式领域中，若系统处理能力有限，阻止计划外计划外的请求继续对系统施压；
    + 控制用户行为，避免垃圾请求；（如某个行为在规定时间内被允许的次数）
+ Redis 实现简单限流策略：
    + 常见简单限流策略：系统限定用户某个行为在指定时间内只能允许发生 N 次；
    + Redis 解决方案：
        + 限流需求中存在一个滑动时间窗口（定宽）；
        + 数据结构：zset 有序集合；
            + key：行为
            + value：无意义，需唯一（区分）
            + score：记录时间窗口
        + [实现 Demo](./src/main/java/com/example/redis/SimpleRateLimiter.java)
        + 缺点：因需记录窗口内所有的行为记录，若量很大，则会消耗大量存储空间；（如 60s 内操作不得超过 100 万次）

### 1.9 漏斗限流

+ 漏斗限流：常见限流方法之一；
    + 漏斗：容量有限；若堵住漏嘴一直灌水就会变满，直至装不进；若放开漏嘴则水会流走，又可继续灌水；
        + 漏嘴流水速率 >= 灌水速率：漏斗永远装不满；
        + 漏嘴流水速率 < 灌水速率：漏斗满了需暂停灌水，直至腾出一部分空间；
    + 引申：
        + 漏斗剩余空间 -> 当前行为可持续进行的数量；
        + 漏嘴流水速率 -> 系统允许该行为的最大频率；
    + [漏斗限流Demo](./src/main/java/com/example/redis/FunnelRateLimiter.java)
    + Demo 缺陷：单线程应用；多线程、分布式不适用；因为无法保证其过程的原子性（从 hash 中取值，在内存中运算，再回填 hash 结构），可加锁处理，但加锁失败后需重试（性能下降）或放弃（影响用户体验）；
+ **Redis-cell**：Redis 限流模块
    + Redis 4.0 提供，限流 Redis 模块；模块使用了漏斗算法，并提供原子的限流指令；
    + 只有一条指令：cl.throttle [key] [capacity] [rate-divisor] [rate-dividend]  [optional]
        + key：关键字
        + capacity：漏斗用量
        + rate-dividend\rate-divisor：表示每 rate-dividend 秒最多执行 rate-divisor 次；
        + optional：可选项，表示灌水多少，默认为1；
        
### 1.10 GeoHash

+ 需求：用坐标计算附近的元素；
+ 方案一：数据库存储并计算
    + 地图元素的位置使用二维的经纬度表示，经度范围 [-180, 180]，纬度范围 [-90, 90]，纬度正负以赤道为界，北正南负，经度以本初子母线为界，东正西负；
    + 距离不远的两点位置：勾股定理可得之间的距离；（附近的元素符合可使用）（但需注意地球是一个椭圆，经纬度坐标密度不一致，要求精确需按一定的系数加权后处理）
    + 关系型数据库存储：元素 id，经度 x，纬度 y；
    + 计算方式：
        1. 遍历计算**所有元素**与目标元素的距离然后再进行排序；（计算量太大，性能无法满足）
        2. 矩形区域限定元素，对区域内元素进行全量距离计算再排序；
            + 优点：明显减少计算量；
            + 缺点：若查询请求过多，性能应付不来；特别是高并发场景下；
            + SQL 语句：SELECT * FROM table where x0 - r < x < x0 + r AND y0 - r < y < y0 + r
            + （为满足高性能矩形区域算法，数据表需把经纬度坐标加上**双向符合索引**，可最大优化查询性能）

![矩形区域限定元素](./img/relational-db-position.png) 

矩形区域限定元素           

+ 方案二：GeoHash 算法
    + 通用的地理位置距离排序算法；
    + 算法核心：将二维的经纬度数据映射到一维整数，在线上获取附近的点；
    + 算法具体：所有元素坐标都放置唯一的方格中，方格越小，坐标越精准；然后对方格进行整数编码，越靠近目标的方格编码越小；简单的编码方案 —— 切蛋糕法（二刀法）；
    + GeoHash 算法继续对整数做 base32 编码变成一个字符串；
    + Redis 中使用 52 位整数编码，使用 zset 有序集合存储（value - 元素的 key，score - GeoHash 的 52 位整数值）；
    
![切蛋糕法（二刀法）编码](./img/geohash-position.jpg)

切蛋糕法（二刀法）编码

+ Geo 指令
    + Redis 3.2 版本后提供；
    + 6 个指令：
        + 增加：geoadd
        + 距离：geodist
        + 获取元素位置：geopos
        + 获取元素的 hash 值：geohash
        + 获取附近的元素：geodiusbymember、georadius
    + 注意事项：
        + 一个地图应用，若使用 Redis 存储，无论数据多少，所有数据将存储至一个 zset 集合中；
        + 若 Redis 使用集群，集合可能由一个节点迁移至另一个节点，若单个 key 数量过大，对迁移工作会造成很大影响，可能出现卡顿现象，影响线上服务正常运行；单个 key 对应数据量不宜超过 1 MB；
        + Geo 数据使用单独 Redis 实例部署；
        + 若数据过大，需将 Geo 数据拆分，降低单个 zset 集合大小；（可按国家、地区、省份、市区等维度拆分）

```shell script
# 增加
> geoadd key x0 y0 id0 
> geoadd key x1 y1 id1 x2 y2 id2 #一次可添加多个

# 距离
> geodist key id0 id1 km #距离可选 m、km、ml、ft（米、千米、英里、尺）

# 获取元素位置
> geopos key id0

# 获取元素 hash 值
> geohash key id0

# 获取附近的元素
> georadiusbymember key [id] [distance-number] [distance-unit] [show-option] count [number] [asc/desc]
# id
# distance-number：附近距离
# distance-unit：附近距离的单位；（m、km、ml、ft）
# show-option：可选项；withcoord、withdist、withhash；用来携带附加参数；在结果中输出坐标、距离、hash值；
# number：最多显示的个数
# asc/desc：升序/降序 输出显示

> georadiusbymember key id0 20 km count 3 asc # 显示范围 20 公里内最多 3 个元素按距离正排（不排除自己）（输出id）

# 根据坐标值查询附近的元素
> georadius key [x] [y] [distance-number] [distance-unit] [show-option] count [number] [asc/desc]
```

### 1.11 scan

+ 场景：从海量的 key 中找出满足特定前缀的 key 列表；
+ 方法一：keys 指令
    + 列出所有瞒住特定正则字符串规则的 key；
    + 优点：使用简单；
    + 缺点：
        + 没有 offset、limit 参数的限制，一次性显示所有满足条件的 key；
        + 核心算法是遍历，时间复杂度 O(n)；若实例中 key 的数量很多，就会造成 Redis 服务卡顿，其他所有读写指令会被延后或超时报错；（Redis 单线程，顺序执行所有指令）
+ 方法二：scan 指令
    + Redis 2.8 后新增；主要解决 keys 遍历的问题；
    + 特点：
        1. 功能与 keys 指令一致； 
        2. 复杂度 O(n)，但通过游标分布进行，不会阻塞线程；
        3. 提供 limit 参数，可控制每次返回结果的最大条数（可多可少，数量不一定，但一定小于最大条数）；
        4. 服务器不需保存游标状态， 游标唯一的状态是 scan 返回客户端的游标整数；
        5. 返回结果可能有重复，需客户端去重；
        6. 遍历过程中若有数据修改，不确定改动后的数据是否被遍历；
        7. 结果返回空并不意味遍历结束，遍历结果游标值为 0；
    + scan 命令：scan [cursor] match [key] count [limit]
        + cursor：游标整数值
        + key：需查询 key 的正则模式
        + limit：遍历参数（单次遍历字典槽位数量）
    + scan 遍历顺序：**高位进位加法**，目的是避免字典扩容和缩容时遍历重复和遗漏；
    + scan 其他功能：可对指定的容器集合进行遍历；
        + zscan：遍历 zset；
        + hscan：遍历 hash；
        + sscan：遍历 set；
+ 字典结构：
    + 所有 key 均存储其中；
    + 类似 Java 中 HashMap；（一维数组 + 二维链表）
    + 第一维数组的大小 2^n，扩容一次，大小加倍，2^(n + 1)；
    + scan 指令返回的第一维数组的“位置索引” （又称“槽”），其中 limit 参数就表示需要遍历的槽的位数； 
+ 如何查找容量很大的对象及其造成的影响
    + 问题造成的影响：
        + 集群环境下迁移造成服务卡顿；
        + 扩容时，申请一块更大的内存，内存分配造成服务卡顿；
        + 对象删除时，内存一次性回收造成服务卡顿；
    + 结论：尽量避免容量大的对象产生；
    + 定位大容量对象：
        + scan 指令：使用 type 指令获取 key 的类型，然后根据其数据结构获取其 len 或 size，最后输出较大的；缺点需脚本运行，步骤比较繁琐；
        + redis-cli 指令：可直接使用的扫描大对象指令；“redis-cli -h 127.0.0.1 -p 7001 --bigkeys [-i 0.1]”，可添加 -i 休眠选项，不会造成大幅提升 ops； 

### 1.12 相关命令总结

+ 通用：
    + 判断 key 是否存在：exists [key]
    + 删除对象：del [key]
    + 设值对象过期时间：expire [key] [second]
    + 遍历符合的 key：keys [pattern]
    + 查看对象使用的数据结构：object encoding [key]
    + 查看内存使用情况：info memory
+ string
    + 设值：set [key] [value]
    + 批量设值：mset [key1] [value1] [key1] [value1] ……
    + 取值：get [key]
    + 批量取值：mget [key1] [key2] ……
    + 设值 + 过期：setex [key] [second] [value]
    + 自增：incr [key]
    + 自减：decr [key]
    + 加法：incrby [key] [number]
    + 减法：decrby [key] [number]
+ list
    + 左进队：lpush [key] [value1] [value2] ……（可批量）
    + 左出队：lpop [key]
    + 右进队：rpush [key] [value1] [value2] ……
    + 右出队：rpop [key]
    + 求队列长度：llen [key]
    + 显示第 index 个元素：lindex [key] [index]
    + 显示从 start 到 end 的元素：lrange key [start] [end]
    + 保留 start 到 end 区间的元素，其余截断：ltrim key [start] [end]
+ hash
    + 设值：hset [key] [field1] [value1] 
    + 批量设值：hmset [key] [field1] [value1] [field2] [value2] ……
    + 取值：hget [key] [field1]
    + 批量取值：hmset [key] [field1] [field2] ……
    + 求集合容量：hlen [key]
    + 字段和数值交替遍历：hgetall [key]
    + 字段自增：hincr [key] [field]
    + 字段自减：hdecr [key] [field]
    + 字段加法：hincrby [key] [field] [number]
    + 字段减法：hdecrby [key] [field] [number]
+ set
    + 设值：sadd [key] [value1] [value2] …… （可批量）
    + 随机弹出一个元素：spop [key]
    + 遍历所有元素：smenbers [key]
    + 判断某元素是否存在：sismember [key] [value]
    + 统计集合中元素的个数：scard [key] 
+ zset
    + 设值：zadd [key] [score] [value]
    + 获取指定 value 的 score：zscore [key] [value]
    + 获取指定 value 的排名：zrank [key] [value]
    + 统计集合中元素的个数：zcard [key]
    + 移除指定 value：zrem [key] [value]
    + 在 start 和 end 区间中正序遍历：zrange [key] [start] [end]
    + 在 start 和 end 区间中逆序遍历：zrevrange [key] [start] [end]
    + 根据 score 分值区间 （start，end）遍历：zrangebyscore [key] [start] [end]
    + 根据 socre 分值区间 (-∞, score] 遍历：zrangebyscore [key] -inf socre withscores
+ 分布式锁
    + 加锁：setnx [key] [value]
    + 加锁改良：set value [EX second] [PX millisecond] [NX|XX]
        + EX second：过期时间为 second 秒
        + PX millisecond：过期时间为 millisecond 毫秒
        + NX：只在键不存在时设值
        + XX：只在键存在时设值
+ 延迟队列
    + 入队：rpush / lpush [key] [value1] [value2] …… 
    + 出队：rpop / lpop [key]
    + 出队（阻塞读）：brpop / blpop [key]
+ 位图
    + 整存整取
        + 设值：set [key] [value]
        + 取值：get [key]
    + 零存零取：
        + 设值：bitset [key] [value]
        + 取值：bitget [key]
    + 统计指定范围内 1 的个数：gitcount [key] [start] [end]
    + 统计指定范围内第一个出现 0 或 1 的位置：bitpos [key] [0/1] [start] [end]
    + 魔术指令：
        + bitfield：可一次执行多个子指令（最多64个）
            + get
            + set
            + 自增：incrby
                + 溢出处理：overflow
                    + wrap 折返：溢出部分直接丢弃
                    + fail 失败：报错不执行
                    + sat 截断：超过最值仅保留最值
                    
```shell script
# bitfield 魔术指令
> bitfield key get u4 0 #从第一个位开始取 4 个位，结果是无符号数（u）
> bitfield key get u3 2 #从第三个位开始取 3 个位，结果是无符号数（u）
> bitfield key get i4 0 #从第一个位开始取 4 个位，结果是有符号数（i）
> bitfield key get u4 0 get u3 2 get i4 0 #等价上三条语句

> bitfield key set u8 8 97 #从第 9 个位开始，将接下来 8 个位用无符号数 97 替换

> bitfield key incrby u4 2 1 #从第三个位开始，对接下来的 4 位无符号数 + 1
> bitfield key overflow sat incrby u4 2 1
> bitfield key overflow fail incrby u4 2 1
```

+ HyperLogLog
    + 增加计数：pfadd [key] [value]
    + 获取计数：pfcount [key]
    + 将多个 pf 计数值累加形成新的 pf 值：pfmerge
+ 布隆过滤器
    + 添加元素：bf.add [key] [value]
    + 批量添加元素：bf.madd [key] [value1] [value2] ……
    + 判断元素是否存在：bf.exists [key] [value]
    + 批量判断元素是否存在：bf.mexists [key] [value1] [value2] ……
    + 自定义创建：bf.reserve [key] [error_rate] [initial_size] 
+ 简单限流
+ 漏斗限流
    + cl.throttle [key] [capacity] [rate-divisor] [rate-dividend]  [optional]
        + key：关键字
        + capacity：漏斗用量
        + rate-dividend\rate-divisor：表示每 rate-dividend 秒最多执行 rate-divisor 次；
        + optional：可选项，表示灌水多少，默认为1；
+ Geohash
    + 增加：geoadd [key] [x] [y] [id] [x1] [y1] [id1] ……（可批量）
    + 获取两点间的距离：geodist [key] [id0] [id1] [m/km/ml/ft]
    + 获取元素位置：geopos [key] [id]
    + 获取元素的 hash 值：geohash [key] [id]
    + 获取附近的元素：
        + georadiusbymember key [id] [distance-number] [distance-unit] [show-option] count [number] [asc/desc]
            + id
            + distance-number：附近距离
            + distance-unit：附近距离的单位；（m、km、ml、ft）
            + show-option：可选项；withcoord、withdist、withhash；用来携带附加参数；在结果中输出坐标、距离、hash值；
            + number：最多显示的个数
            + asc/desc：升序/降序 输出显示
        + georadius key [x] [y] [distance-number] [distance-unit] [show-option] count [number] [asc/desc]
+ scan
    + 遍历符合的 key：scan [cursor] match [key] count [limit]
        + cursor：整数游标
        + key：匹配 key 的正则表达式
        + limit：单词遍历槽的数量
    + 遍历 zset：zscan [cursor] match [key] count [limit]
    + 遍历 hash：hscan [cursor] match [key] count [limit]
    + 遍历 set：sscan [cursor] match [key] count [limit]
    + redis-cli 扫描大容量对象：redis-cli -h [address] -p [port] --bigkeys [-i 0.1] （隔 0.1 秒扫描一次）