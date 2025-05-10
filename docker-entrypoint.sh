#!/bin/sh
set -e

# 根据环境变量选择要启动的服务
if [ "$SERVICE" = "auth" ]; then
    echo "Starting Auth Service..."
    java -jar auth.jar
elif [ "$SERVICE" = "student" ]; then
    echo "Starting Student Service..."
    java -jar student.jar
elif [ "$SERVICE" = "teacher" ]; then
    echo "Starting Teacher Service..."
    java -jar teacher.jar
elif [ "$SERVICE" = "all" ] || [ -z "$SERVICE" ]; then
    # 默认启动所有服务
    echo "Starting all services..."
    
    # 使用后台进程启动前两个服务
    java -jar auth.jar &
    AUTH_PID=$!
    echo "Auth Service started with PID $AUTH_PID"
    
    java -jar student.jar &
    STUDENT_PID=$!
    echo "Student Service started with PID $STUDENT_PID"
    
    # 前台启动最后一个服务（以保持容器运行）
    echo "Starting Teacher Service..."
    java -jar teacher.jar
else
    echo "Unknown service: $SERVICE"
    echo "Valid options: auth, student, teacher, all"
    exit 1
fi 