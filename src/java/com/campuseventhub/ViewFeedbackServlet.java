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

@WebServlet("/viewFeedback")
public class ViewFeedbackServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin_id") == null) {
            response.sendRedirect("admin-login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        StringBuilder html = new StringBuilder("<!DOCTYPE html><html><head><meta charset='UTF-8'>"
                + "<meta name='viewport' content='width=device-width, initial-scale=1'><title>Event Feedback</title><style>"
                + "body{font-family:Arial;background:#f4f6f9;margin:0;padding:25px;color:#263445}.wrap{max-width:1100px;margin:auto}"
                + ".box{background:white;padding:18px;border-radius:12px;box-shadow:0 3px 12px #ddd;overflow-x:auto}"
                + "table{width:100%;border-collapse:collapse;min-width:700px}th{background:#263445;color:white}th,td{padding:12px;border:1px solid #e1e5e9;text-align:left}"
                + ".stars{color:#d99a00;font-weight:bold}.back{display:inline-block;margin:18px 0;padding:10px 16px;background:#263445;color:white;text-decoration:none;border-radius:6px}"
                + ".empty{text-align:center;color:#777;padding:25px}</style></head><body><div class='wrap'><h1>Event Feedback</h1>"
                + "<p>Ratings and comments submitted by students for completed events.</p><div class='box'><table>"
                + "<tr><th>Event</th><th>Student</th><th>Rating</th><th>Comment</th><th>Feedback Date</th></tr>");

        String sql = "SELECT f.*,s.name,e.event_name FROM feedback f "
                + "JOIN students s ON f.student_id=s.student_id JOIN events e ON f.event_id=e.event_id "
                + "ORDER BY f.feedback_date DESC,f.feedback_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            boolean found = false;
            while (rs.next()) {
                found = true;
                int rating = rs.getInt("rating");
                html.append("<tr><td>").append(esc(rs.getString("event_name"))).append("</td><td>")
                    .append(esc(rs.getString("name"))).append("</td><td class='stars'>")
                    .append(rating).append(" / 5</td><td>").append(esc(rs.getString("comment")))
                    .append("</td><td>").append(rs.getDate("feedback_date")).append("</td></tr>");
            }
            if (!found) html.append("<tr><td colspan='5' class='empty'>No feedback submitted yet.</td></tr>");
        } catch (Exception e) {
            html.append("<tr><td colspan='5'>Database error: ").append(esc(e.getMessage())).append("</td></tr>");
        }
        html.append("</table></div><a class='back' href='admin-dashboard.jsp'>Back to Admin Dashboard</a></div></body></html>");
        response.getWriter().print(html.toString());
    }

    private String esc(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
