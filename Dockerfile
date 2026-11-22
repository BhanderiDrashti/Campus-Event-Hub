FROM tomcat:9.0-jdk17-temurin

# Install MariaDB
RUN apt-get update && apt-get install -y mariadb-server && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/local/tomcat

# Remove default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy webapp assets and source
COPY web/ /usr/local/tomcat/webapps/ROOT/
COPY database.sql /app/database.sql
COPY src/ /app/src/

# Compile Java servlets into WEB-INF/classes
RUN mkdir -p /usr/local/tomcat/webapps/ROOT/WEB-INF/classes &&     find /app/src/java -name "*.java" > /tmp/sources.txt &&     javac -cp "/usr/local/tomcat/lib/*:/usr/local/tomcat/webapps/ROOT/WEB-INF/lib/*"           -d /usr/local/tomcat/webapps/ROOT/WEB-INF/classes           @/tmp/sources.txt

# Create startup script to launch MariaDB, import DB, and start Tomcat
RUN echo '#!/bin/bash\n\
service mariadb start\n\
mariadb -e "CREATE DATABASE IF NOT EXISTS campus_event_db;"\n\
mariadb campus_event_db < /app/database.sql\n\
exec catalina.sh run\n' > /entrypoint.sh && chmod +x /entrypoint.sh

EXPOSE 8080

CMD ["/entrypoint.sh"]
