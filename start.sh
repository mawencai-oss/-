#!/bin/bash

# 启动后端服务
echo "启动后端服务..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!
echo "后端服务已启动，PID: $BACKEND_PID"

# 等待后端启动
sleep 5

# 启动前端开发服务器
echo "启动前端开发服务器..."
cd ../frontend
npm run dev

# 当前端退出时，关闭后端
kill $BACKEND_PID
echo "后端服务已关闭" 