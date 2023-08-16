<h1>
数据治理平台后端项目
    <h3>这是一个使用 Spring Boot 框架构建的数据治理平台后端项目，用于处理和管理数据资源的后台逻辑。</h3>
</h1>

## 项目介绍

本项目旨在创建一个功能强大的数据治理平台，能够方便地管理、处理和可视化各种数据资源的元数据信息。

## 功能特点
使用 Spring Boot 框架，提供快速搭建和开发后端服务。
定义 RESTful API，用于前端界面与后端逻辑的交互。
数据库存储使用 MongoDB，提供持久化存储。
项目包名为 com.istlab.datagovernanceplatform，按照包名约定划分模块。

## MongoDB配置
在 application.properties 文件中配置 MongoDB 连接信息：
```
spring.data.mongodb.host= 127.0.0.1
spring.data.mongodb.port = 27017
spring.data.mongodb.database= governance
```


## 目录结构
```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── istlab/
│   │   │   │   │   ├── datagovernanceplatform/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   │   ├── DataSourceController.java
│   │   │   │   │   │   │   ├── MetadataController.java
│   │   │   │   │   │   │   └── ...
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── pojo/
│   │   │   │   │   │   │   ├── po/
│   │   │   │   │   │   │   │── vo/
│   │   │   │   │   │   │   │── dto/
│   │   │   │   │   │   │   │── domain/
│   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   │   ├── DataResourceRepository.java
│   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   │   ├── DataSourceService.java
│   │   │   │   │   │   │   ├── GraphService.java
│   │   │   │   │   │   │   └── ...
│   │   │   │   │   │   │
│   │   │   │   │   │   ├── utils/
│   │   │   │   │   │   │   ├── ResultUtils.java
│   │   │   │   │   │   │   ├── ...
│   │   │   │   │   │   │
│   │   │   │   │   │   └── DataGovernancePlatformApplication.java
│   │   │   │   │   │   
│   │   │   │   │   └── ...
│   │   │   │   │
│   │   │   │   └── ...
│   │   │   │
│   │   │   └── ...
│   │   │
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── ...
│   │   │
│   ├── test/
│   │   └── ...
├── pom.xml
└── ...
```


## 开始使用
用Intellij IDEA 运行

## 许可证
[MIT](http://opensource.org/licenses/MIT)


