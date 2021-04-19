FROM openjdk:8-alpine

RUN set -xe && apk --no-cache add ttf-dejavu fontconfig

# 设置时区
ADD Shanghai /usr/share/zoneinfo/Asia/
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

VOLUME /tmp

#设置字符集
ENV LANG en_US.UTF-8
ARG JAR_FILE
ADD $JAR_FILE app.jar

ENTRYPOINT ["sh","-c","java -server $JAVA_OPTS -jar /app.jar"]