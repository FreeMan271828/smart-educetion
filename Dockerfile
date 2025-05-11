FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 复制Maven配置文件
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd ./
# 添加执行权限
RUN chmod +x mvnw
COPY pom.xml ./

# 复制所有模块的pom.xml，这有助于利用Docker缓存层
COPY auth/pom.xml ./auth/
COPY common/pom.xml ./common/
COPY student/pom.xml ./student/
COPY teacher/pom.xml ./teacher/

# 先下载所有依赖
RUN ./mvnw dependency:go-offline -B

# 复制源代码
COPY auth/src ./auth/src
COPY common/src ./common/src
COPY student/src ./student/src
COPY teacher/src ./teacher/src

# 编译打包整个项目
RUN ./mvnw clean package -DskipTests

# 创建运行阶段镜像
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 复制所有模块的JAR文件
COPY --from=build /app/auth/target/*.jar auth.jar
COPY --from=build /app/student/target/*.jar student.jar
COPY --from=build /app/teacher/target/*.jar teacher.jar

# 添加启动脚本
COPY docker-entrypoint.sh ./
RUN chmod +x docker-entrypoint.sh

# 暴露各个模块的端口
EXPOSE 8080 8081 8082

# 使用启动脚本启动所有服务
ENTRYPOINT ["./docker-entrypoint.sh"] 