
package com.campuseventhub;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/updateRegistrationStatus")
public class UpdateRegistrationStatusServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin_id") == null) {
            response.sendRedirect("admin-login.jsp");
            return;
        }

        String status = request.getParameter("status");

        if (!"Approved".equals(status) && !"Rejected".equals(status)) {
            response.sendRedirect("viewRegistrations");
            return;
        }

        try {

            int registrationId = Integer.parseInt(
                    request.getParameter("registration_id"));

            String sql = "UPDATE event_registrations "
                    + "SET status=? "
                    + "WHERE registration_id=? AND status='Pending'";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, status);
                ps.setInt(2, registrationId);

                ps.executeUpdate();
            }

            response.sendRedirect("viewRegistrations");

        } catch (NumberFormatException e) {

            response.sendRedirect("viewRegistrations");

        } catch (Exception e) {

            response.setContentType("text/html;charset=UTF-8");

            response.getWriter().println(
                    "<h2>Unable to update registration: "
                    + e.getMessage()
                    + "</h2><a href='viewRegistrations'>Back</a>");
        }
    }
}
