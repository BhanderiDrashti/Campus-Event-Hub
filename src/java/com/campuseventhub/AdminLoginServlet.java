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
import javax.servlet.http.HttpSession;

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                HttpSession session = request.getSession();

                session.setAttribute("admin_id", rs.getInt("admin_id"));
                session.setAttribute("admin_username", rs.getString("username"));

                response.sendRedirect("admin-dashboard.jsp");

            } else {

                out.println("<html>");
                out.println("<head>");
                out.println("<title>Admin Login Failed</title>");

                out.println("<style>");

                out.println("body {");
                out.println("font-family: Arial;");
                out.println("background-color: #f4f6f8;");
                out.println("text-align: center;");
                out.println("padding-top: 100px;");
                out.println("}");

                out.println(".error {");
                out.println("background-color: white;");
                out.println("width: 400px;");
                out.println("margin: auto;");
                out.println("padding: 30px;");
                out.println("border-radius: 10px;");
                out.println("box-shadow: 0 4px 12px rgba(0,0,0,0.15);");
                out.println("}");

                out.println("h2 {");
                out.println("color: #e74c3c;");
                out.println("}");

                out.println("a {");
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

                out.println("<div class='error'>");

                out.println("<h2>Invalid Admin Login</h2>");

                out.println("<p>Username or password is incorrect.</p>");

                out.println("<a href='admin-login.jsp'>Try Again</a>");

                out.println("</div>");

                out.println("</body>");

                out.println("</html>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>Database Error</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("</body>");
            out.println("</html>");

            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }
}