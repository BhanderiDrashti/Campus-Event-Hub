
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
if (session.getAttribute("admin_id") == null) {
    response.sendRedirect("admin-login.jsp");
    return;
}

int students = 0;
int events = 0;
int registrations = 0;
int pending = 0;
int approved = 0;
int feedback = 0;

try (Connection c = com.campuseventhub.DBConnection.getConnection()) {

    PreparedStatement p;
    ResultSet r;

    p = c.prepareStatement("SELECT COUNT(*) FROM students");
    r = p.executeQuery();
    if (r.next()) students = r.getInt(1);
    r.close();
    p.close();

    p = c.prepareStatement("SELECT COUNT(*) FROM events");
    r = p.executeQuery();
    if (r.next()) events = r.getInt(1);
    r.close();
    p.close();

    p = c.prepareStatement("SELECT COUNT(*) FROM event_registrations");
    r = p.executeQuery();
    if (r.next()) registrations = r.getInt(1);
    r.close();
    p.close();

    p = c.prepareStatement("SELECT COUNT(*) FROM event_registrations WHERE status='Pending'");
    r = p.executeQuery();
    if (r.next()) pending = r.getInt(1);
    r.close();
    p.close();

    p = c.prepareStatement("SELECT COUNT(*) FROM event_registrations WHERE status='Approved'");
    r = p.executeQuery();
    if (r.next()) approved = r.getInt(1);
    r.close();
    p.close();

    p = c.prepareStatement("SELECT COUNT(*) FROM feedback");
    r = p.executeQuery();
    if (r.next()) feedback = r.getInt(1);
    r.close();
    p.close();

} catch (Exception e) {
    out.println("Database Error: " + e.getMessage());
}
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Admin Dashboard | Campus Event Hub</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f4f6f9;
        }

        .nav {
            background: #2c3e50;
            color: white;
            padding: 18px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
        }

        .nav h2 {
            margin: 0;
        }

        .nav a {
            color: white;
            text-decoration: none;
            background: #e74c3c;
            padding: 10px 18px;
            border-radius: 6px;
        }

        .nav a:hover {
            background: #c0392b;
        }

        .wrap {
            max-width: 1200px;
            margin: auto;
            padding: 30px 20px;
        }

        .welcome {
            background: white;
            padding: 25px;
            border-radius: 12px;
            margin-bottom: 25px;
            box-shadow: 0 3px 10px #ddd;
        }

        .welcome h1 {
            color: #2c3e50;
            margin-top: 0;
        }

        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
            gap: 18px;
            margin-bottom: 30px;
        }

        .stat {
            background: white;
            padding: 22px;
            border-radius: 12px;
            text-align: center;
            box-shadow: 0 3px 10px #ddd;
        }

        .stat h2 {
            font-size: 30px;
            margin: 5px;
            color: #2980b9;
        }

        .stat p {
            color: #666;
            margin-bottom: 0;
        }

        .cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
            gap: 20px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 3px 10px #ddd;
            transition: 0.3s;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 15px #ccc;
        }

        .card h2 {
            color: #2c3e50;
            font-size: 21px;
        }

        .card p {
            color: #666;
            line-height: 1.6;
        }

        .card a {
            display: inline-block;
            background: #2c3e50;
            color: white;
            text-decoration: none;
            padding: 10px 16px;
            border-radius: 5px;
            margin-top: 8px;
        }

        .card a:hover {
            background: #1a252f;
        }

        .footer {
            text-align: center;
            color: #777;
            padding: 25px;
            margin-top: 20px;
        }

        @media(max-width: 600px) {
            .nav {
                padding: 15px;
                gap: 15px;
            }

            .wrap {
                padding: 20px 12px;
            }

            .welcome {
                padding: 18px;
            }
        }
    </style>
</head>

<body>

    <!-- Navigation -->
    <div class="nav">
        <h2>Campus Event Hub - Admin</h2>
        <a href="adminLogout">Logout</a>
    </div>

    <div class="wrap">

        <!-- Welcome Section -->
        <div class="welcome">
            <h1>
                Welcome, <%= session.getAttribute("admin_username") %>!
            </h1>
            <p>Manage events, registrations, attendance, feedback and reports.</p>
        </div>

        <!-- Statistics -->
        <div class="stats">

            <div class="stat">
                <h2><%= students %></h2>
                <p>Total Students</p>
            </div>

            <div class="stat">
                <h2><%= events %></h2>
                <p>Total Events</p>
            </div>

            <div class="stat">
                <h2><%= registrations %></h2>
                <p>Total Registrations</p>
            </div>

            <div class="stat">
                <h2><%= pending %></h2>
                <p>Pending</p>
            </div>

            <div class="stat">
                <h2><%= approved %></h2>
                <p>Approved</p>
            </div>

            <div class="stat">
                <h2><%= feedback %></h2>
                <p>Total Feedback</p>
            </div>

        </div>

        <!-- Admin Management Cards -->
        <div class="cards">

            <div class="card">
                <h2>➕ Add Event</h2>
                <p>Create a new event with capacity and registration deadline.</p>
                <a href="add-event.jsp">Add Event</a>
            </div>

            <div class="card">
                <h2>📅 Manage Events</h2>
                <p>View, edit and delete campus events.</p>
                <a href="viewEvents">View Events</a>
            </div>

            <div class="card">
                <h2>👥 View Registrations</h2>
                <p>View all student registrations and attendance details.</p>
                <a href="viewRegistrations">View Registrations</a>
            </div>

            <div class="card">
                <h2>⭐ Event Feedback</h2>
                <p>Read student ratings and comments for campus events.</p>
                <a href="viewFeedback">View Feedback</a>
            </div>

            <div class="card">
                <h2>📊 Reports</h2>
                <p>View event-wise registration and feedback reports.</p>
                <a href="adminReport">Open Reports</a>
            </div>

        </div>

    </div>

    <!-- Footer -->
    <div class="footer">
        © 2026 Campus Event Hub | Admin Panel
    </div>

</body>
</html>
