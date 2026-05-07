# ==========================================
# 阶段一：构建阶段 (命名为 builder)
# ==========================================
# 使用包含 Maven 和 JDK 的重量级镜像
## 给这个临时的构建容器打了个标签，叫 builder（名字你可以随便起，叫 kitchen 也行）。
## 为什么需要：因为到了后面的第二阶段，我们需要从这个阶段“偷”东西，有了名字才好精确锁定目标。
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# 设置工作目录 创建并进入一个工作目录（相当于 mkdir /build && cd /build）
## 这指定了接下来的命令（比如 COPY 和 RUN）都在容器内部的 /build 文件夹下执行。
WORKDIR /build

# 先只拷贝 pom.xml (利用 Docker 缓存机制，只要 pom 没变，就不重新下载依赖)
COPY pom.xml .
# 这一步会下载所有依赖，耗时较长，但会被缓存
# Docker 构建镜像是分层缓存的。一般我们在写代码时，修改 Java 源码的频率极高，但修改 pom.xml 增删依赖的频率很低
# 我们先 COPY pom.xml .，然后跑这一步。只要你的 pom.xml 不变，Docker 以后每次构建都会直接使用这一步的缓存，
# 不用重新下载半个互联网的依赖，你的构建速度会从几分钟直接飙升到几秒钟！
RUN mvn dependency:go-offline

# 拷贝真正的源代码
COPY src ./src

# 执行打包命令，跳过测试以加快速度
RUN mvn clean package -DskipTests

# ==========================================
# 阶段二：运行阶段 (最终生成的纯净镜像)
# ==========================================
# 换成极其轻量的 JRE 镜像 (不包含 Maven，甚至不能 javac 编译)
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 【核心魔法】：从上面的 builder 阶段，把打好的 jar 包复制过来！
# 跨阶段复制文件（多阶段构建的绝对核心魔法！）。
# --from=builder：告诉 Docker，不要从我的宿主机（你的 Mac）拷文件了，去刚才那个名字叫 builder 的旧阶段里找
## /build/target/*.jar：去 builder 阶段的“案板”上，把刚才打包好的 .jar 文件拿过来。
# app.jar：把它放到当前阶段的 /app 目录下，并重命名为 app.jar。
# 把“做好的菜”端到“干净的餐桌”上，而“厨房”里的刀具、菜叶子（Maven、JDK、源码）全部被 Docker 丢弃。这就是镜像从 500MB 瘦身到 100MB 的秘密
COPY --from=builder /build/target/*.jar app.jar

# 设置时区（可选，但在国内很常用）
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 声明端口
EXPOSE 8081

# 启动参数
# 指定容器启动时的“绝对默认命令”。
# 原理解析：当这个最终镜像被 docker run 或者 docker-compose up 启动时，它会自动在容器内部的 /app 目录下执行 java -jar app.jar。

#为什么用 ENTRYPOINT 而不是 CMD：
 #ENTRYPOINT 的意思是：“这个容器生来就是为了跑这个 Java 程序的，不接受反驳。”
 #它是不可被轻易覆盖的，符合微服务单一职责的原则。如果使用 CMD，别人在启动容器时加上别的命令，你的 Java 程序可能就不会启动了。
ENTRYPOINT ["java", "-jar", "app.jar"]