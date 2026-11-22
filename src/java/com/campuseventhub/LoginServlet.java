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

@WebServlet(name="LoginServlet", urlPatterns={"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        String email=request.getParameter("email"), password=request.getParameter("password");
        try(Connection con=DBConnection.getConnection()){
            PreparedStatement ps=con.prepareStatement("SELECT * FROM students WHERE email=? AND password=?");
            ps.setString(1,email); ps.setString(2,password); ResultSet rs=ps.executeQuery();
            if(rs.next()){
                HttpSession session=request.getSession();
                session.setAttribute("student_id",rs.getInt("student_id"));
                session.setAttribute("student_name",rs.getString("name"));
                session.setAttribute("student_email",rs.getString("email"));
                response.sendRedirect("studentDashboard.jsp");
            }else{
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().println("<script>alert('Invalid Email or Password!');window.location='login.jsp';</script>");
            }
        }catch(Exception e){response.setContentType("text/html;charset=UTF-8");response.getWriter().println("<h2>Login Error</h2><p>"+e.getMessage()+"</p>");}
    }
    protected void doGet(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{processRequest(r,s);}
    protected void doPost(HttpServletRequest r,HttpServletResponse s)throws ServletException,IOException{processRequest(r,s);}
}
