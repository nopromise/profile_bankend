## 项目

## 简单springboot接口项目

## 通过 [Dockerfile](Dockerfile)   打成本地镜像并推送的阿里云
```
FROM eclipse-temurin:26-jre

WORKDIR /app

COPY target/profile_bankend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

## 通过 [compose-test.yaml](compose-test.yaml) 来到处启动和运行此项目

```
services:
  postgres:
    image: 'postgres:16'
    environment:
      - 'POSTGRES_DB=mydatabase'
      - 'POSTGRES_PASSWORD=secret'
      - 'POSTGRES_USER=myuser'
    ports:
      - '5432:5432'
    volumes:
      - pgdata:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U myuser -d mydatabase"]
      interval: 5s
      timeout: 3s
      retries: 10

  app:
#    build:
#      context: .
#      dockerfile: Dockerfile
    image: crpi-0b73awcs10m9dovb.cn-qingdao.personal.cr.aliyuncs.com/finn_test/profile_bankend:1.2
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      - DB_URL=jdbc:postgresql://postgres:5432/mydatabase
      - DB_USER=myuser
      - DB_PASSWORD=secret
    ports:
      - '8082:8082'

volumes:
  pgdata:

```
