# Infinity Scaffold - 前后端开发脚手架

一个集成了现代化前后端技术栈的开发脚手架，帮助开发者快速搭建项目。

## 技术栈

### 后端

- **核心框架**：Spring Boot 2.7.x
- **ORM框架**：MyBatis Plus 3.5.x
- **权限认证**：Sa-Token 1.34.x
- **接口文档**：Knife4j 4.1.x
- **缓存中间件**：Redis
- **其他工具**：Lombok, HuTool

### 前端

- **核心框架**：Vue 3.3.x (组合式API + TypeScript)
- **构建工具**：Vite 5.x
- **状态管理**：Pinia 2.x
- **路由管理**：Vue Router 4.x
- **UI组件库**：Element Plus
- **CSS工具**：Tailwind CSS
- **图表库**：ECharts
- **HTTP工具**：Axios (配置了拦截器)

## 功能特性

- 基于Sa-Token的认证授权系统
- 集成MyBatis Plus，支持自动生成代码
- 前端使用Vue 3 + TypeScript，提供更好的类型体验
- 整合Element Plus和Tailwind CSS，快速构建漂亮的界面
- 接口文档自动生成
- 跨域支持
- 文件上传功能

## 项目结构

```
infinity-scaffold/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/infinity/scaffold/
│   │   │   │   ├── common/     # 通用类
│   │   │   │   ├── config/     # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── entity/     # 实体类
│   │   │   │   ├── mapper/     # Mapper接口
│   │   │   │   ├── service/    # 服务接口及实现
│   │   │   │   ├── exception/  # 异常处理
│   │   │   │   └── InfinityScaffoldApplication.java # 启动类
│   │   │   └── resources/      # 配置文件
│   │   └── test/               # 测试代码
│   └── pom.xml                 # Maven配置
└── frontend/                   # 前端项目
    ├── src/
    │   ├── api/               # API请求
    │   ├── assets/            # 静态资源
    │   ├── components/        # 公共组件
    │   ├── config/            # 配置文件
    │   ├── router/            # 路由配置
    │   ├── store/             # Pinia状态管理
    │   ├── utils/             # 工具函数
    │   ├── views/             # 页面
    │   ├── App.vue            # 根组件
    │   └── main.ts            # 入口文件
    ├── public/                # 公共静态资源
    ├── vite.config.ts        # Vite配置
    ├── tailwind.config.js     # Tailwind CSS配置
    ├── tsconfig.json         # TypeScript配置
    └── package.json          # NPM配置
```

## 快速开始

### 后端

1. 确保已安装JDK 8+, Maven, MySQL, Redis
2. 创建数据库 `infinity_scaffold`
3. 修改 `application.yml` 中的数据库和Redis配置
4. 运行Spring Boot应用：

```bash
cd backend
mvn spring-boot:run
```

### 前端

1. 确保已安装Node.js 16+
2. 安装依赖并启动开发服务器：

```bash
cd frontend
npm install
npm run dev
```

3. 构建生产版本：

```bash
npm run build
```

## 访问项目

- 前端开发服务器: `http://localhost:3000`
- API文档: `http://localhost:8080/api/doc.html`
- API前缀: `http://localhost:8080/api`

## 默认账户

- 用户名：admin
- 密码：123456 