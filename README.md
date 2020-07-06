# 这是一个基于springboot的畅购商城学习项目
> [视频链接](https://www.bilibili.com/video/BV1N7411k7bP)

## 所使用的相关包名
- changgou-common：公共模块
- changgou-eureka：eureka模块
- changgou-gateway：网关模块
- changgou-service
- changgou-service-api
- changgou-web

## 自我理解
### `changgou-service`/`changgou-service-api` 模块
`changgou-service-api` -> `changgou-service-goods-api`: 包含有数据库的pojo类，和数据库数据结构相关
`changgou-service` -> `changgou-service-goods`: 操作数据库
> 同样的，这样个模块下类似命名的子模块都以此类推

### `dao`,`service`,`controller`的关系

```mermaid
graph LR
    subgraph one
    id[dao] --> id1[service]
    id1[service] -.注解:Autoware.-> id2[controller]
    end
id1[service] --继承--> id3((service.impl))
id3 -.注解:Autowired.-> id
id3 --实际自动注入--> id2
id4[Mapper] --继承--> id
```

`dao` -> `service` -> `controller`

---
## 工程结构
![工程结构](https://gitee.com/Maiiiiiid/picture_bed/raw/master/手机上传/-71264b5e7f75c1ed.jpg)