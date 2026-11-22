
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% if (session.getAttribute("student_id") == null) { response.sendRedirect("login.jsp"); return; } %>
<!DOCTYPE html>
<html>
<head>

    <title>Student Dashboard - Campus Event Hub</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
        }

        /* Navbar */
        .navbar {
            background-color: #007bff;
            color: white;
            padding: 18px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h2 {
            margin: 0;
        }

        .logout {
            color: white;
            text-decoration: none;
            background-color: #dc3545;
            padding: 9px 18px;
            border-radius: 5px;
        }

        .logout:hover {
            background-color: #b02a37;
        }

        /* Main Container */
        .container {
            padding: 35px;
            max-width: 1100px;
            margin: auto;
        }

        /* Welcome Box */
        .welcome {
            background-color: white;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0px 3px 10px #d0d0d0;
            margin-bottom: 30px;
        }

        .welcome h1 {
            margin-top: 0;
            color: #333;
        }

        .welcome p {
            color: #666;
            font-size: 16px;
        }

        .student-info {
            margin-top: 20px;
            padding: 15px;
            background-color: #f1f7ff;
            border-left: 5px solid #007bff;
            border-radius: 5px;
        }

        /* Four dashboard cards in one horizontal row */
        .cards {
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 18px;
            align-items: stretch;
        }
        .card {
            background: white;
            min-width: 0;
            padding: 25px 18px;
            text-align: center;
            border-radius: 12px;
            box-shadow: 0 3px 10px #d0d0d0;
            transition: 0.3s;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 15px #bdbdbd;
        }
        .card-icon { font-size: 38px; margin-bottom: 10px; }
        .card h3 { color: #333; margin: 8px 0; font-size: 19px; }
        .card p { color: #666; min-height: 58px; line-height: 1.5; font-size: 14px; }
        .card a {
            display: inline-block;
            margin-top: 10px;
            text-decoration: none;
            color: white;
            background-color: #007bff;
            padding: 10px 15px;
            border-radius: 5px;
            font-weight: bold;
        }
        .card a:hover { background-color: #0056b3; }

        /* Footer */
        .footer {
            text-align: center;
            padding: 20px;
            color: #777;
            margin-top: 30px;
        }

        /* Mobile */
        @media (max-width: 1000px) {
            .cards { grid-template-columns: repeat(2, minmax(0, 1fr)); }
        }

        @media (max-width: 600px) {
            .navbar { padding: 15px; }
            .navbar h2 { font-size: 20px; }
            .container { padding: 20px; }
            .cards { grid-template-columns: 1fr; }
            .card p { min-height: 0; }
        }

    </style>

</head>

<body>

    <!-- Navbar -->
    <div class="navbar">

        <h2>Campus Event Hub</h2>

        <a class="logout" href="studentLogout">Logout</a>

    </div>


    <!-- Main Content -->
    <div class="container">

        <!-- Welcome Section -->
        <div class="welcome">

            <h1>
                Welcome,
                <%= session.getAttribute("student_name") %>!
            </h1>

            <p>
                Welcome to your Student Dashboard.
                Manage your college events and registrations from here.
            </p>

            <div class="student-info">

                <p>
                    <strong>Name:</strong>
                    <%= session.getAttribute("student_name") %>
                </p>

                <p>
                    <strong>Email:</strong>
                    <%= session.getAttribute("student_email") %>
                </p>

            </div>

        </div>


        <!-- Dashboard Cards -->
        <div class="cards">
            <div class="card">
                <div class="card-icon">📅</div>
                <h3>View Events</h3>
                <p>See available college events and register for your favourite event.</p>
                <a href="studentEvents">View Events</a>
            </div>

            <div class="card">
                <div class="card-icon">👤</div>
                <h3>My Profile</h3>
                <p>View and update your personal and academic details.</p>
                <a href="studentProfile">My Profile</a>
            </div>

            <div class="card">
                <div class="card-icon">📋</div>
                <h3>My Registrations</h3>
                <p>Check your registered events, status and registration receipt.</p>
                <a href="myEvents">My Registrations</a>
            </div>

            <div class="card">
                <div class="card-icon">🎓</div>
                <h3>Certificates</h3>
                <p>View and print certificates for events you attended.</p>
                <a href="myCertificates">My Certificates</a>
            </div>
        </div>

    <!-- Footer -->
    <div class="footer">

        <p>© 2026 Campus Event Hub | Student Dashboard</p>

    </div>

</body>
</html>
