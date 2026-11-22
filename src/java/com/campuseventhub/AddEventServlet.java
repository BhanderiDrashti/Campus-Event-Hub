package com.campuseventhub;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/addEvent")
public class AddEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false);
        if(session==null||session.getAttribute("admin_id")==null){response.sendRedirect("admin-login.jsp");return;}
        try(Connection con=DBConnection.getConnection()){
            String sql="INSERT INTO events(event_name,description,event_date,event_time,venue,category,capacity,registration_deadline,status) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,request.getParameter("event_name")); ps.setString(2,request.getParameter("description"));
            ps.setDate(3,Date.valueOf(request.getParameter("event_date"))); ps.setTime(4,Time.valueOf(request.getParameter("event_time")+":00"));
            ps.setString(5,request.getParameter("venue")); ps.setString(6,request.getParameter("category"));
            ps.setInt(7,Integer.parseInt(request.getParameter("capacity"))); ps.setDate(8,Date.valueOf(request.getParameter("registration_deadline"))); ps.setString(9,request.getParameter("status"));
            ps.executeUpdate(); response.sendRedirect("viewEvents");
        }catch(Exception e){response.setContentType("text/html;charset=UTF-8");response.getWriter().println("<h2>Error: "+e.getMessage()+"</h2><a href='add-event.jsp'>Back</a>");}
    }
}
