# SecondClass
## 项目结构：
### sc-api：后端   
### sc-view：前端

------
接口文档：
https://www.apifox.cn/apidoc/shared-45fd8b65-d6ae-49cf-a9ca-01ceb151cb10/api-49052833
------
### 后端启动：

**sc-api目录**

1. 从代码仓库克隆下来 Git clone https://github.com/SXSQ1710/SecondClass.git

2. 修改配置文件：src\main\resources\application.yml

3. 更新maven依赖

4. 启动redis

   > redis启动方式（windows）：到redis文件夹目录下打开命令窗口输入
   >
   > --> redis-server.exe redis.windows.conf

5. 直接运行,启动项目

------

> 系统说明：因为使用到了redis做缓存和消息队列，所有项目启动的前提一定是要本地启动了redis服务，否则项目的消息队列的消费者线程会一直循环报错，并且redis版本要5及以上。

### 前端启动：
1. sc-view路径下输入cmd打开命令窗口
2. 输入 npm install 初始化项目（需要安装npm）
3. 输入 npm run dev 启动项目
4. 访问出现的url地址
