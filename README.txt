CAMPUS EVENT HUB - ADVANCED JAVA PROJECT

Technology: Java Servlet, JSP, HTML, CSS, JavaScript, MySQL
Server: Apache Tomcat using javax.servlet imports
Database: MySQL / XAMPP

DATABASE SETUP
1. Start Apache and MySQL in XAMPP.
2. If your MySQL port is 3307, keep DBConnection.java as provided.
3. Open phpMyAdmin.
4. Import database.sql. It creates campus_event_db and all required tables.
5. If your MySQL uses another port, change the port in DBConnection.java.

DEFAULT ADMIN
Username: admin
Password: admin123

DEMO STUDENT
Email: student@gmail.com
Password: 1234

MAIN FEATURES
- Student registration and login
- Student profile update
- Event search and category filter
- Event details page
- Detailed event registration form
- Capacity and registration deadline checking
- My registrations
- Cancel registration
- Printable registration receipt
- Admin dashboard statistics
- Add/edit/delete events
- Approve/reject registrations
- Attendance marking
- Event reports and feedback reports
- Student feedback and rating
- Printable participation certificate after approved attendance

NETBEANS
Open NetBeans -> File -> Open Project -> select CampusEventHub folder.
Run the project on the same Tomcat version used by your existing project.

IMPORTANT
This project intentionally uses javax.servlet.* because the existing project is configured for the javax/Tomcat environment.
