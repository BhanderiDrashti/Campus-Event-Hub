package com.campuseventhub;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RegisterEventServlet")
public class RegisterEventServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false); if(session==null||session.getAttribute("student_id")==null){response.sendRedirect("login.jsp");return;}
        try(Connection con=DBConnection.getConnection()){
            int studentId=(Integer)session.getAttribute("student_id"); int eventId=Integer.parseInt(request.getParameter("event_id"));
            PreparedStatement check=con.prepareStatement("SELECT registration_id FROM event_registrations WHERE student_id=? AND event_id=?"); check.setInt(1,studentId);check.setInt(2,eventId);ResultSet r=check.executeQuery(); if(r.next()){response.sendRedirect("registrationReceipt?registration_id="+r.getInt(1));return;}
            PreparedStatement event=con.prepareStatement("SELECT capacity,registration_deadline,status FROM events WHERE event_id=?"); event.setInt(1,eventId);ResultSet er=event.executeQuery(); if(!er.next()){response.sendRedirect("studentEvents");return;}
            if(!"Upcoming".equals(er.getString("status")) || new java.util.Date().after(er.getDate("registration_deadline"))){response.setContentType("text/html;charset=UTF-8");response.getWriter().println("<script>alert('Registration is closed for this event.');window.location='studentEvents';</script>");return;}
            PreparedStatement count=con.prepareStatement("SELECT COUNT(*) FROM event_registrations WHERE event_id=? AND status IN ('Pending','Approved')");count.setInt(1,eventId);ResultSet cr=count.executeQuery();cr.next();if(cr.getInt(1)>=er.getInt("capacity")){response.setContentType("text/html;charset=UTF-8");response.getWriter().println("<script>alert('Event capacity is full.');window.location='studentEvents';</script>");return;}
            PreparedStatement ps=con.prepareStatement("INSERT INTO event_registrations(student_id,event_id,registration_date,phone,department,year,participation_type,message,status) VALUES(?,?,CURDATE(),?,?,?,?,?,'Pending')",PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1,studentId);ps.setInt(2,eventId);ps.setString(3,request.getParameter("phone"));ps.setString(4,request.getParameter("department"));ps.setString(5,request.getParameter("year"));ps.setString(6,request.getParameter("participation_type"));ps.setString(7,request.getParameter("message"));ps.executeUpdate();ResultSet keys=ps.getGeneratedKeys();int regId=keys.next()?keys.getInt(1):0;response.sendRedirect("registrationReceipt?registration_id="+regId);
        }catch(Exception e){response.setContentType("text/html;charset=UTF-8");response.getWriter().println("<h2>Registration Error</h2><p>"+e.getMessage()+"</p><a href='studentEvents'>Back</a>");}
    }
}
