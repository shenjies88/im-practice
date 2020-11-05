# IM应用实践

## 环境说明

- `JDK 8`
- `gradle 6.6.1`
- `SpringBoot 2.3.5.RELEASE`
- `Spring Cloud Hoxton.SR8`
- `Zookeeper 3.4.12`

## 架构说明

以`zookeeper`作为`netty`实例的注册中心，实现水平扩展，`router`模块负载均衡返回`netty`实例

## 模块说明

- `im-common`一些工具类以及公共类
- `im-netty` `netty`服务
- `im-router` `netty`服务的负载均衡返回
- `im-backend`业务处理后端

## TODO

- [X] 私聊
- [ ] 群聊
- [ ] 心跳检测