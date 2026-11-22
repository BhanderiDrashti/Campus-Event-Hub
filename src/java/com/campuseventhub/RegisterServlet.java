package com.campuseventhub;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="RegisterServlet", urlPatterns={"/RegisterServlet"})
public class RegisterServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String name=request.getParameter("name"), email=request.getParameter("email"), password=request.getParameter("password");
        String course=request.getParameter("course"), semester=request.getParameter("semester");
        String phone=request.getParameter("phone"), department=request.getParameter("department");
        try (Connection con=DBConnection.getConnection()) {
            String sql="INSERT INTO students(name,email,password,course,semester,phone,department) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,name); ps.setString(2,email); ps.setString(3,password); ps.setString(4,course); ps.setString(5,semester); ps.setString(6,phone); ps.setString(7,department);
            ps.executeUpdate();
            out.println("<script>alert('Registration Successful! Please login.');window.location='login.jsp';</script>");
        } catch(Exception e) {
            out.println("<script>alert('Registration failed: " + e.getMessage().replace("'", "") + "');window.location='register.jsp';</script>");
        }
    }
    protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{processRequest(r,s);}
    protected void doPost(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{processRequest(r,s);}
}
