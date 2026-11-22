
package com.campuseventhub;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/viewRegistrations")
public class ViewRegistrationsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin_id") == null) {
            response.sendRedirect("admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html><head>")
            .append("<meta charset='UTF-8'>")
            .append("<meta name='viewport' content='width=device-width, initial-scale=1'>")
            .append("<title>Student Registrations</title>")

            .append("<style>")
            .append("body{font-family:Arial;background:#f4f6f9;margin:0;padding:25px;color:#263445}")
            .append(".wrap{max-width:1200px;margin:auto}")
            .append("h1{color:#263445}")
            .append(".tablebox{overflow-x:auto;background:white;border-radius:12px;padding:15px;box-shadow:0 3px 12px #ddd}")
            .append(".note{background:#eaf4ff;padding:14px;border-left:4px solid #2784c7;border-radius:6px}")
            .append(".back{display:inline-block;margin:18px 0;padding:10px 16px;background:#263445;color:white;text-decoration:none;border-radius:6px}")
            .append(".table{width:100%;border-collapse:collapse;min-width:850px}")
            .append(".table th{background:#263445;color:white}")
            .append(".table th,.table td{padding:11px;border:1px solid #e1e5e9;text-align:center}")
            .append(".status{font-weight:bold}")
            .append(".present{color:green}")
            .append(".absent{color:#a66b00}")
            .append(".approve,.reject,.att{display:inline-block;padding:7px 10px;color:white;text-decoration:none;border-radius:5px;margin:3px}")
            .append(".approve{background:#198754}")
            .append(".reject{background:#dc3545}")
            .append(".att{background:#6f42c1}")
            .append(".muted{color:#667085}")
            .append("</style></head><body>")

            .append("<div class='wrap'>")
            .append("<h1>Student Event Registrations</h1>")
            .append("<p class='note'>Admin can approve or reject pending registrations and update attendance for approved students.</p>")

            .append("<div class='tablebox'>")
            .append("<table class='table'>")
            .append("<tr>")
            .append("<th>ID</th>")
            .append("<th>Student</th>")
            .append("<th>Phone</th>")
            .append("<th>Department / Year</th>")
            .append("<th>Event</th>")
            .append("<th>Event Date</th>")
            .append("<th>Participation</th>")
            .append("<th>Status</th>")
            .append("<th>Attendance</th>")
            .append("<th>Action</th>")
            .append("</tr>");

        String sql = "SELECT er.*,s.name,s.email,e.event_name,e.event_date "
                + "FROM event_registrations er "
                + "JOIN students s ON er.student_id=s.student_id "
                + "JOIN events e ON er.event_id=e.event_id "
                + "ORDER BY er.registration_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;

            while (rs.next()) {

                found = true;

                int id = rs.getInt("registration_id");
                boolean attended = rs.getBoolean("attended");

                String status = rs.getString("status");

                html.append("<tr>")
                    .append("<td>").append(id).append("</td>")

                    .append("<td>").append(esc(rs.getString("name")))
                    .append("<br><span class='muted'>")
                    .append(esc(rs.getString("email")))
                    .append("</span></td>")

                    .append("<td>").append(esc(rs.getString("phone"))).append("</td>")

                    .append("<td>").append(esc(rs.getString("department")))
                    .append(" / ").append(esc(rs.getString("year"))).append("</td>")

                    .append("<td>").append(esc(rs.getString("event_name"))).append("</td>")

                    .append("<td>").append(rs.getDate("event_date")).append("</td>")

                    .append("<td>").append(esc(rs.getString("participation_type"))).append("</td>")

                    .append("<td class='status'>").append(esc(status)).append("</td>")

                    .append("<td class='")
                    .append(attended ? "present" : "absent")
                    .append("'>")
                    .append(attended ? "Present" : "Absent")
                    .append("</td><td>");

                if ("Pending".equals(status)) {

                    html.append("<a class='approve' href='updateRegistrationStatus?registration_id=")
                        .append(id)
                        .append("&status=Approved' ")
                        .append("onclick=\"return confirm('Approve this registration?')\">")
                        .append("Approve</a>")

                        .append("<a class='reject' href='updateRegistrationStatus?registration_id=")
                        .append(id)
                        .append("&status=Rejected' ")
                        .append("onclick=\"return confirm('Reject this registration?')\">")
                        .append("Reject</a>");

                } else if ("Approved".equals(status)) {

                    html.append("<a class='att' href='markAttendance?registration_id=")
                        .append(id)
                        .append("' onclick=\"return confirm('Change attendance status?')\">")
                        .append(attended ? "Mark Absent" : "Mark Present")
                        .append("</a>");

                } else {

                    html.append("<span class='muted'>No action available</span>");
                }

                html.append("</td></tr>");
            }

            if (!found) {
                html.append("<tr><td colspan='10'>No registrations found.</td></tr>");
            }

        } catch (Exception e) {

            html.append("<tr><td colspan='10'>Database error: ")
                .append(esc(e.getMessage()))
                .append("</td></tr>");
        }

        html.append("</table></div>")
            .append("<a class='back' href='admin-dashboard.jsp'>")
            .append("Back to Admin Dashboard</a>")
            .append("</div></body></html>");

        response.getWriter().print(html.toString());
    }

    private String esc(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
