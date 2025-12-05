# Birthday 生日提醒系统

## 项目简介

Birthday 是一个基于 Spring Boot 的生日提醒管理系统，帮助用户管理好友生日信息并提供定时提醒功能。

## 技术栈

- **框架**: Spring Boot 3.4.12
- **Java 版本**: 17
- **数据库**: PostgreSQL (生产环境) / H2 (测试环境)
- **ORM**: Spring Data JPA
- **安全**: Spring Security
- **对象映射**: MapStruct 1.5.5.Final
- **简化代码**: Lombok
- **构建工具**: Maven

## 项目层次结构

┌─────────────────────────────────┐
│           Controller            │
│   接收请求、返回数据给前端        │
└──────────────▲──────────────────┘
│ 调用
┌──────────────┴──────────────────┐
│             Service              │
│  业务逻辑、校验、定时任务、流程控制 │
└──────────────▲──────────────────┘
│ 调用
┌──────────────┴──────────────────┐
│            Repository            │
│   只负责数据库操作（增删改查）       │
└──────────────▲──────────────────┘
│ 读取/写入
┌──────────────┴──────────────────┐
│   Entity ←→ Mapper ←→ DTO       │
│  Entity：数据库模型                │
│  DTO：给 Controller/前端使用的模型 │
│  Mapper：转换（MapStruct）         │
└──────────────────────────────────┘

本项目采用经典的三层架构模式，各层职责清晰：

```
com.example.birthday
├── entity/          # 实体层 - 数据库映射
├── repository/      # 数据访问层 - 数据库操作
├── dto/             # 数据传输对象层 - 前后端交互
├── mapper/          # 对象映射层 - Entity与DTO转换
├── service/         # 业务逻辑层 - 核心业务处理
├── controller/      # 控制器层 - 接口暴露
├── config/          # 配置层 - 系统配置
└── util/            # 工具类层 - 通用工具
```

### 1. Entity 层（实体层）

定义数据库表结构对应的 Java 实体类，使用 JPA 注解进行 ORM 映射。

#### User (用户表)
- **表名**: `users`
- **主要字段**:
  - `id`: UUID，主键
  - `email`: 邮箱，唯一且非空
  - `passwordHash`: 密码哈希值
  - `createdAt`: 创建时间

#### Friend (好友表)
- **表名**: `friends`
- **主要字段**:
  - `id`: UUID，主键
  - `name`: 好友姓名（非空）
  - `birthday`: 生日日期（非空）
  - `gender`: 性别
  - `importance`: 重要程度（1-5 级）
  - `likes`: 喜好
  - `note`: 备注
  - `remind`: 是否开启提醒
  - `remindDays`: 提前几天提醒（默认 3 天）
  - `userId`: 所属用户 ID（外键）
  - `createdAt/updatedAt`: 创建/更新时间

#### Reminder (提醒记录表)
- **表名**: `reminder_log`
- **主要字段**:
  - `id`: Long，主键（自增）
  - `friend`: 关联的好友实体（多对一关系）
  - `remindTime`: 提醒时间
  - `type`: 提醒方式（email、sms 等）
  - `message`: 提醒内容
  - `sent`: 是否已发送

### 2. Repository 层（数据访问层）⭐

**Repository 层是数据持久化的核心**，负责与数据库的直接交互，封装所有数据访问逻辑。

#### 核心功能

1. **基础 CRUD 操作**
   - 继承 `JpaRepository` 自动获得增删改查功能
   - 无需手动编写 SQL 语句

2. **自定义查询方法**
   - 使用 Spring Data JPA 方法命名规则自动生成查询
   - 支持复杂的查询条件组合

#### ReminderRepository

```java
public interface ReminderRepository extends JpaRepository<Reminder, Long>
```

**功能说明**:
- **基础功能**: 继承自 `JpaRepository<Reminder, Long>`，自动提供：
  - `save()` - 保存提醒记录
  - `findById()` - 根据 ID 查询
  - `findAll()` - 查询所有记录
  - `deleteById()` - 删除记录
  - 等等...

- **自定义查询方法**:
  1. `findByFriendId(Long friendId)` 
     - 查询指定好友的所有提醒记录
     - 用于显示某个好友的提醒历史
  
  2. `findByRemindTimeBeforeAndSentFalse(LocalDateTime now)`
     - 查询需要触发但还未发送的提醒
     - 参数说明：
       - `RemindTimeBefore`: 提醒时间早于指定时间
       - `SentFalse`: 未发送状态
     - 用途：定时任务扫描需要发送的提醒

#### UserRepository

```java
public class UserRepository
```

**功能说明**:
- 负责用户数据的持久化操作
- 未来可扩展的查询方法：
  - 根据邮箱查询用户
  - 用户认证相关查询
  - 用户统计信息

#### FriendRepository

```java
public class FriendRepository
```

**功能说明**:
- 负责好友信息的数据库操作
- 未来可扩展的查询方法：
  - 根据用户 ID 查询好友列表
  - 根据生日月份查询
  - 根据重要程度排序
  - 即将生日的好友查询

### 3. DTO 层（数据传输对象）

DTO（Data Transfer Object）用于前后端数据传输，与 Entity 相比更加轻量，只包含业务需要的字段。

- **UserDTO**: 用户信息传输对象
- **FriendDTO**: 好友信息传输对象
- **ReminderDTO**: 提醒记录传输对象

**优势**:
- 隐藏数据库实体的内部结构
- 减少网络传输数据量
- 提高安全性（避免敏感字段暴露）

### 4. Mapper 层（对象映射）

使用 MapStruct 框架实现 Entity 与 DTO 之间的自动转换。

- **UserMapper**: User ↔ UserDTO 转换
- **FriendMapper**: Friend ↔ FriendDTO 转换
- **ReminderMapper**: Reminder ↔ ReminderDTO 转换

**优势**:
- 自动生成映射代码，避免手写转换逻辑
- 编译时生成，性能优于反射
- 类型安全

### 5. Service 层（业务逻辑层）

处理核心业务逻辑，协调 Repository 和 Mapper，实现业务规则。

**职责**:
- 调用 Repository 进行数据操作
- 使用 Mapper 进行对象转换
- 实现复杂的业务逻辑
- 事务管理

### 6. Controller 层（控制器层）

对外暴露 RESTful API 接口，处理 HTTP 请求。

**职责**:
- 接收前端请求
- 参数校验
- 调用 Service 层处理业务
- 返回响应结果

## 数据流向

```
Client → Controller → Service → Repository → Database
   ↓         ↓          ↓           ↓
  DTO    ←  Mapper  ←  Entity  ←  持久化数据
```

**请求流程**:
1. 前端发送 DTO 数据到 Controller
2. Controller 调用 Service 处理业务
3. Service 使用 Mapper 将 DTO 转为 Entity
4. Service 调用 Repository 操作数据库
5. Repository 返回 Entity 给 Service
6. Service 使用 Mapper 将 Entity 转为 DTO
7. Controller 将 DTO 返回给前端

## Repository 层设计原则

### 1. 单一职责
每个 Repository 只负责一个实体的数据访问，职责清晰。

### 2. 接口隔离
通过继承 `JpaRepository` 接口，获得标准的 CRUD 操作，同时可根据业务需求自定义查询方法。

### 3. 方法命名规范
Spring Data JPA 支持通过方法名自动生成查询：
- `findBy...` - 查询
- `countBy...` - 统计
- `deleteBy...` - 删除
- `existsBy...` - 判断存在

### 4. 查询优化
- 只查询需要的字段
- 使用索引优化查询性能
- 避免 N+1 查询问题

## 核心业务场景

### 1. 用户注册与登录
- User Entity 存储用户信息
- UserRepository 提供用户数据访问
- Spring Security 提供认证授权

### 2. 好友管理
- Friend Entity 存储好友信息
- FriendRepository 提供好友数据操作
- 支持添加、编辑、删除好友

### 3. 生日提醒
- Reminder Entity 记录提醒日志
- ReminderRepository 查询待发送提醒
- 定时任务扫描并发送提醒

## 项目特点

1. **分层清晰**: 严格遵循分层架构，各层职责明确
2. **易于扩展**: Repository 层可轻松添加新的查询方法
3. **类型安全**: 使用 MapStruct 保证类型安全的对象转换
4. **代码简洁**: Lombok 减少样板代码
5. **数据库无关**: JPA 抽象层，便于切换数据库

## 开发建议

### Repository 层开发规范
1. 所有 Repository 接口继承 `JpaRepository`
2. 自定义查询方法遵循 Spring Data JPA 命名规范
3. 复杂查询使用 `@Query` 注解编写 JPQL
4. 避免在 Repository 中写业务逻辑

### 命名规范示例
```java
// 单条件查询
List<Friend> findByUserId(UUID userId);

// 多条件查询
List<Friend> findByUserIdAndRemindTrue(UUID userId);

// 排序查询
List<Friend> findByUserIdOrderByBirthdayAsc(UUID userId);

// 分页查询
Page<Friend> findByUserId(UUID userId, Pageable pageable);
```

## 启动项目

```bash
# 编译项目
mvn clean install

# 运行项目
mvn spring-boot:run
```

## 未来规划

- [ ] 完善 UserRepository 和 FriendRepository 的查询方法
- [ ] 实现定时任务扫描和发送提醒
- [ ] 添加邮件/短信提醒功能
- [ ] 实现 RESTful API
- [ ] 添加单元测试和集成测试
