利用Canal中间件使MySQL与Redis保持数据同步
采用责任链模式，压测过，线程安全。

此项目不光可以同步Redis,还可同步 ES、MongoDB、Solr.....套路都是一样的。

###安装 Canal Server

下载地址：https://github.com/alibaba/canal/releases

```linux
wget https://github.com/alibaba/canal/releases/download/canal-1.1.2/canal.deployer-1.1.2.tar.gz
```

下载后解压缩

编辑conf/example/instance.properties
```properties
#Canal 伪装成子节点的 id，与 master id 不一样
canal.instance.mysql.slaveId = 99
#要订阅的数据库地址
canal.instance.master.address = 127.0.0.1:3306 
canal.instance.master.journal.name =
canal.instance.master.position =
canal.instance.master.timestamp =

#Canal 账户，数据库会专门开通一个账户给 Canal 连接
canal.instance.dbUsername = canal
canal.instance.dbPassword = canal

```

#### 启动、停止 Canal 服务

```linux
sh bin/start.sh
sh bin/restart.sh
sh bin/stop.sh
```

### 安装 Redis

[http://zhengyk.cn/2017/09/01/redis/Redis01/](http://zhengyk.cn/2017/09/01/redis/Redis01/)

### 项目配置信息

```yml
spring: 
  #Redis配置
  redis:
    host: localhost
    port: 7000
    password: Yakai2018@
    database: 0
    jedis:
      pool:
        #最大连接数（负值表示没有限制）
        max-active: 100
        #最大空闲链接
        max-idle: 10
        #最小空闲链接
        min-idle: 5
        #最大阻塞时间 负值表示不限制
        max-wait: -1ms
        
# canal相关配置
canal:
  host: localhost
  port: 11111
  destination: example
  username:
  password:
  subscribe: test.blog
  batchSize: 1000
# subscribe 过滤规则
# 1）  所有：.*   or  .*\\..*
# 2）  "test"库下所有表： test\\..*
# 3）  "test"下的以"sys"打头的表：test\\.sys.*
# 4）  "test"下的具体一张表：test.blog   blog表
# 5）  多个规则组合使用：test\\..*,test.sys_user,test.sys_role (逗号分隔)        
```

运行启动项目即可，当我们对数据库进行增、删、改的操作时，Redis 也会相应变化。

