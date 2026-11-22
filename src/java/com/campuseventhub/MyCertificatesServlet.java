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

@WebServlet("/myCertificates")
public class MyCertificatesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int studentId = (Integer) session.getAttribute("student_id");
        response.setContentType("text/html;charset=UTF-8");

        String sql = "SELECT er.registration_id, e.event_name, e.event_date, e.venue "
                + "FROM event_registrations er JOIN events e ON er.event_id=e.event_id "
                + "WHERE er.student_id=? AND er.status='Approved' AND er.attended=1 "
                + "ORDER BY e.event_date DESC";

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
            .append("<meta name='viewport' content='width=device-width, initial-scale=1'>")
            .append("<title>My Certificates</title><style>")
            .append("body{font-family:Arial;background:#f4f6f8;margin:0;padding:25px;color:#263238}")
            .append(".wrap{max-width:950px;margin:25px auto;background:white;padding:28px;border-radius:12px;box-shadow:0 3px 12px #ddd}")
            .append("h1{margin-top:0;color:#243b53}.sub{color:#667085}")
            .append("table{width:100%;border-collapse:collapse;margin-top:22px}")
            .append("th,td{padding:13px;border-bottom:1px solid #e5e7eb;text-align:left}")
            .append("th{background:#243b53;color:white}.btn{display:inline-block;background:#198754;color:white;text-decoration:none;padding:9px 13px;border-radius:6px}")
            .append(".back{display:inline-block;margin-top:22px;background:#52606d;color:white;text-decoration:none;padding:10px 15px;border-radius:6px}")
            .append(".empty{padding:18px;background:#fff8e1;border-left:4px solid #f0ad4e;margin-top:20px;line-height:1.6}")
            .append("@media(max-width:650px){.wrap{padding:16px;overflow-x:auto}table{min-width:600px}}")
            .append("</style></head><body><div class='wrap'>")
            .append("<h1>🎓 My Certificates</h1>")
            .append("<p class='sub'>Certificates are available after your registration is approved and attendance is marked by the admin.</p>");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                StringBuilder rows = new StringBuilder();
                while (rs.next()) {
                    found = true;
                    rows.append("<tr><td>").append(escape(rs.getString("event_name")))
                        .append("</td><td>").append(rs.getDate("event_date"))
                        .append("</td><td>").append(escape(rs.getString("venue")))
                        .append("</td><td><a class='btn' target='_blank' href='certificate?registration_id=")
                        .append(rs.getInt("registration_id"))
                        .append("'>View / Print Certificate</a></td></tr>");
                }
                if (found) {
                    html.append("<table><tr><th>Event Name</th><th>Event Date</th><th>Venue</th><th>Certificate</th></tr>")
                        .append(rows).append("</table>");
                } else {
                    html.append("<div class='empty'><b>No certificates available yet.</b><br>")
                        .append("After the admin approves your registration and marks your attendance as Present, ")
                        .append("your certificate will appear here. Your registration receipt is available separately ")
                        .append("under My Registrations.</div>");
                }
            }
        } catch (Exception e) {
            html.append("<div class='empty'>Unable to load certificates: ")
                .append(escape(e.getMessage())).append("</div>");
        }

        html.append("<a class='back' href='studentDashboard.jsp'>Back to Dashboard</a>")
            .append("</div></body></html>");
        response.getWriter().print(html.toString());
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;")
                    .replace(">", "&gt;").replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
