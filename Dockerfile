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

# Create startup script to launch MariaDB, grant root TCP permissions, import DB, and start Tomcat
RUN echo '#!/bin/bash\n\
service mariadb start\n\
for i in {1..30}; do\n\
    if mariadb -e "SELECT 1;" >/dev/null 2>&1; then break; fi\n\
    sleep 1\n\
done\n\
mariadb -e "CREATE DATABASE IF NOT EXISTS campus_event_db;"\n\
mariadb -e "CREATE USER IF NOT EXISTS \"root\"@\"%\" IDENTIFIED BY \"\";" || true\n\
mariadb -e "ALTER USER \"root\"@\"localhost\" IDENTIFIED BY \"\";" || true\n\
mariadb -e "GRANT ALL PRIVILEGES ON *.* TO \"root\"@\"localhost\" WITH GRANT OPTION;" || true\n\
mariadb -e "GRANT ALL PRIVILEGES ON *.* TO \"root\"@\"127.0.0.1\" WITH GRANT OPTION;" || true\n\
mariadb -e "GRANT ALL PRIVILEGES ON *.* TO \"root\"@\"%\" WITH GRANT OPTION;" || true\n\
mariadb -e "FLUSH PRIVILEGES;" || true\n\
mariadb campus_event_db < /app/database.sql\n\
mariadb -e "USE campus_event_db; INSERT IGNORE INTO students (student_id, name, email, password, course, semester, phone, department) VALUES (3, \"Demo Student\", \"student@gmail.com\", \"1234\", \"CSE\", \"3\", \"9876543210\", \"CSE\");" || true\n\
exec catalina.sh run\n' > /entrypoint.sh && chmod +x /entrypoint.sh

EXPOSE 8080

CMD ["/entrypoint.sh"]
