FROM maven:3.6.1-jdk-8 as maven_builder
WORKDIR /app
ADD . /app

RUN mvn clean package

FROM tomcat:8.5.43-jdk8
COPY --from=maven_builder /app/easymanager-webapp/target/easymanager.war /usr/local/tomcat/webapps
ADD ./tomcat-users.xml /usr/local/tomcat/conf/

RUN echo 'export JAVA_OPTS="$JAVA_OPTS -Dconnection_driver_class=${connection_driver_class} -Dconnection_url=${connection_url} -Dconnection_username=${connection_username} -Dconnection_password=${connection_password} -Dconnection_dialect=${hibernate_dialect} -Dconnection_show_sql=${hibernate_show_sql}"' > bin/setenv.sh

#run with
#docker run -p 8080:8080 -e connection_url=jdbc:mysql://<IP>:3306/easymanager -e connection_username=test -e connection_password=test easymanager