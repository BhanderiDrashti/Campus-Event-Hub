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

@WebServlet("/eventDetails")
public class EventDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false);if(session==null||session.getAttribute("student_id")==null){response.sendRedirect("login.jsp");return;}
        int id=Integer.parseInt(request.getParameter("event_id"));response.setContentType("text/html;charset=UTF-8");
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement("SELECT e.*, (SELECT COUNT(*) FROM event_registrations er WHERE er.event_id=e.event_id AND er.status IN ('Pending','Approved')) AS registered FROM events e WHERE e.event_id=?")){ps.setInt(1,id);ResultSet rs=ps.executeQuery();if(rs.next()){
            int seats=rs.getInt("capacity")-rs.getInt("registered"); String status=rs.getString("status"); boolean open="Upcoming".equals(status)&&!new java.util.Date().after(rs.getDate("registration_deadline"))&&seats>0;
            response.getWriter().println("<!DOCTYPE html><html><head><title>Event Details</title><style>body{font-family:Arial;background:#f4f6f8;padding:35px}.box{max-width:800px;margin:auto;background:white;padding:35px;border-radius:14px;box-shadow:0 4px 15px #ccc}.tag{display:inline-block;padding:6px 12px;background:#e8f2ff;border-radius:20px}.row{padding:12px;border-bottom:1px solid #eee}.btn{display:inline-block;margin-top:20px;padding:12px 20px;background:#007bff;color:white;text-decoration:none;border-radius:6px}.back{margin-left:10px;background:#555}.closed{color:#d00;font-weight:bold}</style></head><body><div class='box'><span class='tag'>"+safe(rs.getString("category"))+"</span><h1>"+safe(rs.getString("event_name"))+"</h1><p>"+safe(rs.getString("description"))+"</p><div class='row'><b>Date:</b> "+rs.getDate("event_date")+"</div><div class='row'><b>Time:</b> "+rs.getTime("event_time")+"</div><div class='row'><b>Venue:</b> "+safe(rs.getString("venue"))+"</div><div class='row'><b>Status:</b> "+status+"</div><div class='row'><b>Available Seats:</b> "+Math.max(seats,0)+" / "+rs.getInt("capacity")+"</div><div class='row'><b>Registration Deadline:</b> "+rs.getDate("registration_deadline")+"</div>");
            if(open) response.getWriter().println("<a class='btn' href='eventRegistrationForm?event_id="+id+"'>Register Now</a>"); else response.getWriter().println("<p class='closed'>Registration is currently closed.</p>");
            response.getWriter().println("<a class='btn back' href='studentEvents'>Back to Events</a></div></body></html>");}}
        catch(Exception e){response.getWriter().println("<h2>Error: "+e.getMessage()+"</h2>");}
    }
    private String safe(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");}
}
