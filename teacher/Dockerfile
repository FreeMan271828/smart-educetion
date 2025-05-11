FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# 复制Maven配置文件
COPY ../.mvn/ ./.mvn/
COPY ../mvnw ../mvnw.cmd ./
# 添加执行权限
RUN chmod +x mvnw
COPY ../pom.xml ./

# 复制所需模块的pom.xml以利用Docker缓存层
COPY auth/pom.xml ./auth/
COPY common/pom.xml ./common/
COPY ./pom.xml ./teacher/

# 先下载所有依赖
RUN ./mvnw dependency:go-offline -B

# 复制所需模块的源代码
COPY auth/src ./auth/src
COPY common/src ./common/src
COPY ./src ./teacher/src

# 编译打包teacher模块（会自动构建依赖的auth和common模块）
RUN ./mvnw clean package -pl teacher -am -DskipTests

# 创建运行阶段镜像
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# 复制teacher模块的JAR文件
COPY --from=build /app/teacher/target/*.jar app.jar

# 暴露端口
EXPOSE 8081

# 启动teacher服务
ENTRYPOINT ["java", "-jar", "app.jar"]