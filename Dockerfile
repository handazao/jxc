FROM java:8

MAINTAINER jxc

ENV PROJECT_HOME /root/jxc/
ENV JAVA_OPTS="-Xms256m -Xmx256m -Xss1m -Xmn128m"

RUN mkdir -p "$PROJECT_HOME"
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \&& echo 'Asia/Shanghai' >/etc/timezone

VOLUME /tmp

ADD target/jxc-0.1.jar jxc.jar

ENTRYPOINT ["java", "-Xmx1024m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/jxc.jar"]

EXPOSE 8080
