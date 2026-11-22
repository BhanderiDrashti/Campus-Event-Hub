package com.campuseventhub;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewEvents")
public class ViewEventsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head>");

        out.println("<title>View Events</title>");

        out.println("<style>");
        
        out.println(".delete {");
out.println("display: inline-block;");
out.println("background-color: #e74c3c;");
out.println("color: white;");
out.println("padding: 8px 12px;");
out.println("text-decoration: none;");
out.println("border-radius: 5px;");
out.println("margin: 3px;");
out.println("}");

out.println(".delete:hover {");
out.println("background-color: #c0392b;");
out.println("}");

        out.println("body {");
        out.println("font-family: Arial, sans-serif;");
        out.println("background-color: #f4f6f8;");
        out.println("margin: 0;");
        out.println("padding: 30px;");
        out.println("}");

        out.println("h1 {");
        out.println("text-align: center;");
        out.println("color: #2c3e50;");
        out.println("}");

        out.println("table {");
        out.println("width: 95%;");
        out.println("margin: 30px auto;");
        out.println("border-collapse: collapse;");
        out.println("background-color: white;");
        out.println("box-shadow: 0 4px 10px rgba(0,0,0,0.1);");
        out.println("}");

        out.println("th {");
        out.println("background-color: #2c3e50;");
        out.println("color: white;");
        out.println("padding: 12px;");
        out.println("}");

        out.println("td {");
        out.println("padding: 12px;");
        out.println("text-align: center;");
        out.println("border: 1px solid #ddd;");
        out.println("}");

        out.println(".edit {");
out.println("display: inline-block;");
out.println("background-color: #3498db;");
out.println("color: white;");
out.println("padding: 8px 12px;");
out.println("text-decoration: none;");
out.println("border-radius: 5px;");
out.println("margin: 3px;");
out.println("}");

out.println(".edit:hover {");
out.println("background-color: #2980b9;");
out.println("}");

        out.println(".back {");
        out.println("display: inline-block;");
        out.println("margin-top: 20px;");
        out.println("padding: 10px 20px;");
        out.println("background-color: #2c3e50;");
        out.println("color: white;");
        out.println("text-decoration: none;");
        out.println("border-radius: 5px;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<h1>Campus Events</h1>");

        out.println("<table>");

        out.println("<tr>");

        out.println("<th>Event ID</th>");
        out.println("<th>Event Name</th>");
        out.println("<th>Description</th>");
        out.println("<th>Date</th>");
        out.println("<th>Time</th>");
        out.println("<th>Venue</th>");
        out.println("<th>Category</th>");
        out.println("<th>Status</th>");
        out.println("<th>Action</th>");

        out.println("</tr>");

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM events ORDER BY event_id ASC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int eventId = rs.getInt("event_id");

                out.println("<tr>");

                out.println("<td>" + eventId + "</td>");

                out.println("<td>"
                        + rs.getString("event_name")
                        + "</td>");

                out.println("<td>"
                        + rs.getString("description")
                        + "</td>");

                out.println("<td>"
                        + rs.getDate("event_date")
                        + "</td>");

                out.println("<td>"
                        + rs.getTime("event_time")
                        + "</td>");

                out.println("<td>"
                        + rs.getString("venue")
                        + "</td>");

                out.println("<td>"
                        + rs.getString("category")
                        + "</td>");

                out.println("<td>"
                        + rs.getString("status")
                        + "</td>");

                out.println("<td>");

                out.println("<a class='edit' "
                        + "href='editEvent?action=form&event_id="
                        + eventId
                        + "'>Edit</a>");
                
                out.println("<a class='delete' "
                        + "href='deleteEvent?event_id="
                        + eventId
                        + "' "
                        + "onclick=\"return confirm('Are you sure you want to delete this event?');\">"
                        + "Delete</a>");

                out.println("</td>");

                out.println("</tr>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<tr>");

            out.println("<td colspan='9'>");

            out.println("Error: " + e.getMessage());

            out.println("</td>");

            out.println("</tr>");

            e.printStackTrace();
        }

        out.println("</table>");

        out.println("<center>");

        out.println("<a class='back' href='admin-dashboard.jsp'>");

        out.println("← Back to Admin Dashboard");

        out.println("</a>");

        out.println("</center>");

        out.println("</body>");

        out.println("</html>");
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}