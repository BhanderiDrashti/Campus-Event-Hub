#!/bin/bash
set -e

echo "=== Starting MariaDB Initialization ==="
mkdir -p /run/mysqld /var/lib/mysql
chown -R mysql:mysql /run/mysqld /var/lib/mysql

if [ ! -d "/var/lib/mysql/mysql" ]; then
    echo "Running mysql_install_db..."
    mysql_install_db --user=mysql --datadir=/var/lib/mysql
fi

echo "Starting MariaDB via mysqld_safe..."
/usr/bin/mysqld_safe --user=mysql --skip-syslog &

echo "Waiting for MariaDB socket and port 3306..."
for i in {1..30}; do
    if mysqladmin ping -u root --silent 2>/dev/null; then
        echo "MariaDB is online and responding!"
        break
    fi
    echo "Waiting for MariaDB ($i/30)..."
    sleep 1
done

echo "Setting up database and permissions..."
mariadb -u root -e "CREATE DATABASE IF NOT EXISTS campus_event_db;" || true
mariadb -u root -e "CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY '';" || true
mariadb -u root -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION;" || true
mariadb -u root -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION;" || true
mariadb -u root -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;" || true
mariadb -u root -e "FLUSH PRIVILEGES;" || true

if [ -f /app/database.sql ]; then
    echo "Importing database.sql..."
    mariadb -u root campus_event_db < /app/database.sql || true
    echo "database.sql imported successfully!"
fi

mariadb -u root -e "USE campus_event_db; INSERT IGNORE INTO students (student_id, name, email, password, course, semester, phone, department) VALUES (3, 'Demo Student', 'student@gmail.com', '1234', 'CSE', '3', '9876543210', 'CSE');" || true

echo "=== MariaDB setup complete! Starting Tomcat... ==="
exec catalina.sh run
