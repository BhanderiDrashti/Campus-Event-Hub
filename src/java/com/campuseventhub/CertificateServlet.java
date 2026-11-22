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

@WebServlet("/certificate")
public class CertificateServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("student_id") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        String sql = "SELECT s.name,e.event_name,e.event_date,e.venue,er.attended,er.status "
                + "FROM event_registrations er JOIN students s ON er.student_id=s.student_id "
                + "JOIN events e ON er.event_id=e.event_id "
                + "WHERE er.registration_id=? AND er.student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(request.getParameter("registration_id")));
            ps.setInt(2, (Integer) session.getAttribute("student_id"));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getBoolean("attended")
                        && "Approved".equals(rs.getString("status"))) {

                    String student = escape(rs.getString("name"));
                    String event = escape(rs.getString("event_name"));
                    String date = String.valueOf(rs.getDate("event_date"));
                    String venue = escape(rs.getString("venue"));

                    response.getWriter().println(
                        "<!DOCTYPE html><html><head><meta charset='UTF-8'>"
                        + "<meta name='viewport' content='width=device-width, initial-scale=1'>"
                        + "<title>Certificate of Participation</title><style>"
                        + "body{font-family:Georgia,serif;background:#eef1f5;padding:25px;margin:0}"
                        + ".certificate{max-width:1000px;min-height:600px;margin:20px auto;background:#fff;"
                        + "border:14px double #b08d36;outline:2px solid #243b53;outline-offset:-28px;"
                        + "padding:65px 45px;text-align:center;box-sizing:border-box}"
                        + ".top{color:#9b7628;letter-spacing:5px;font-size:14px;font-weight:bold}"
                        + "h1{font-size:42px;color:#243b53;margin:25px 0 12px}"
                        + ".subtitle{font-size:20px;color:#555}"
                        + ".student{font-size:38px;color:#9b7628;font-weight:bold;margin:28px 0;"
                        + "border-bottom:1px solid #c9ad69;display:inline-block;padding:0 35px 10px}"
                        + ".event{font-size:27px;color:#243b53;font-weight:bold;margin:18px 0}"
                        + ".details{font-size:17px;line-height:1.9;color:#555}"
                        + ".seal{margin:28px auto 0;border:2px solid #b08d36;border-radius:50%;width:95px;height:95px;"
                        + "display:flex;align-items:center;justify-content:center;color:#9b7628;font-size:13px;font-weight:bold}"
                        + ".actions{text-align:center;margin:20px}.actions button,.actions a{border:0;border-radius:6px;"
                        + "padding:11px 18px;margin:5px;background:#243b53;color:white;text-decoration:none;cursor:pointer}"
                        + "@media(max-width:650px){.certificate{padding:55px 20px;min-height:0}h1{font-size:30px}.student{font-size:27px}.event{font-size:22px}}"
                        + "@media print{body{background:white;padding:0}.certificate{margin:0;max-width:none;min-height:100vh;break-inside:avoid}.actions{display:none}}"
                        + "</style></head><body><div class='certificate'>"
                        + "<div class='top'>CAMPUS EVENT HUB</div>"
                        + "<h1>Certificate of Participation</h1>"
                        + "<p class='subtitle'>This certificate is proudly presented to</p>"
                        + "<div class='student'>" + student + "</div>"
                        + "<p class='details'>In recognition of successful participation in</p>"
                        + "<div class='event'>" + event + "</div>"
                        + "<p class='details'>Held on <b>" + date + "</b>"
                        + (venue.isEmpty() ? "" : " at <b>" + venue + "</b>")
                        + "<br>We appreciate your enthusiasm and contribution.</p>"
                        + "<div class='seal'>CERTIFIED<br>PARTICIPANT</div></div>"
                        + "<div class='actions'><button onclick='window.print()'>Print Certificate</button>"
                        + "<a href='myCertificates'>Back to My Certificates</a></div></body></html>");
                } else {
                    response.getWriter().println("<h2>Certificate is available only for approved registrations "
                        + "with attendance marked Present.</h2><p>Contact the admin if you attended the event "
                        + "but the certificate is not showing.</p><a href='myCertificates'>Back to My Certificates</a>");
                }
            }
        } catch (NumberFormatException e) {
            response.getWriter().println("<h2>Invalid registration ID.</h2><a href='myCertificates'>Back</a>");
        } catch (Exception e) {
            response.getWriter().println("<h2>Unable to generate certificate: " + escape(e.getMessage()) + "</h2>");
        }
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;")
                    .replace(">", "&gt;").replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
