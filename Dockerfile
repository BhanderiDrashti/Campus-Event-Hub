FROM tomcat:9.0-jdk17-temurin

# Install MariaDB
RUN apt-get update && apt-get install -y mariadb-server mariadb-client && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/local/tomcat

# Remove default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy webapp assets and source
COPY web/ /usr/local/tomcat/webapps/ROOT/
COPY database.sql /app/database.sql
COPY src/ /app/src/
COPY docker-entrypoint.sh /docker-entrypoint.sh

RUN chmod +x /docker-entrypoint.sh

# Compile Java servlets into WEB-INF/classes
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes &&     find /app/src/java -name "*.java" > /tmp/sources.txt &&     javac -cp "/usr/local/tomcat/lib/*:/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*"           -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes           @/tmp/sources.txt

EXPOSE 8080

CMD ["/docker-entrypoint.sh"]
