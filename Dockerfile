# Step 1: 使用官方Maven容器作为构建环境，确保Maven镜像的Java版本与项目一致
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
# 复制pom.xml和源码到/app目录
COPY pom.xml .
COPY src ./src
# 执行Maven构建命令，打包应用为jar
RUN mvn -f pom.xml clean package

# Step 2: 使用官方OpenJDK镜像作为运行环境
FROM openjdk:17-slim
WORKDIR /app
# 从构建阶段复制构建好的jar文件
COPY --from=build /app/target/*.jar app.jar
# 指定容器启动时运行Java应用
ENTRYPOINT ["java","-jar","app.jar"]
