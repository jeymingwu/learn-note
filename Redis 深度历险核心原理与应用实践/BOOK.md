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

+ Redis 中所有数据结构均以**唯一的 key 字符串作名称**，通过这唯一的 key 获取对应的 value 数据；不同类型的数据结构的差异在于** value 结构的不同**；
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
> expire key time #time为数字，表示多少秒后过期
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

# 计数（与 string 一样可对 hash 中单个子 key 进行操作）
> hlen hash #统计 hash 中有多少个元素
> hincr hash key #hash 中 key 的 value 自增
> hincrby hash key number #hash 中 key 的 value 加上 number
> hdecr hash key #hash 中 key 的 value 自减
> hdecrby hash key number #hash 中 key 的 value 减去 number
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

### [1.3 分布式锁]()

### [1.4 延时队列]()

### [1.5 位图]()

### [1.6 HyperLogLog]()

### [1.7 布隆过滤器]()

### [1.8 简单限流]()

### [1.9 漏斗限流]()

### [1.10 GeoHash]()

### [1.11 scan]()

### [1.12 相关命令指南]()

## [第二篇：原理篇]()

## [第三篇：集群篇]()

## [第四篇：拓展篇]()

## [第五篇：源码篇]()