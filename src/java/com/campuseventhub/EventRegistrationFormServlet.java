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

@WebServlet("/eventRegistrationForm")
public class EventRegistrationFormServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false);if(session==null||session.getAttribute("student_id")==null){response.sendRedirect("login.jsp");return;}
        int id=Integer.parseInt(request.getParameter("event_id"));response.setContentType("text/html;charset=UTF-8");
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement("SELECT * FROM events WHERE event_id=?")){ps.setInt(1,id);ResultSet rs=ps.executeQuery();if(rs.next()){
            response.getWriter().println("<!DOCTYPE html><html><head><title>Event Registration</title><style>body{font-family:Arial;background:#f4f6f8;padding:30px}.box{max-width:650px;margin:auto;background:white;padding:30px;border-radius:12px;box-shadow:0 4px 15px #ccc}input,select,textarea{width:100%;padding:10px;margin:6px 0 12px;box-sizing:border-box}button{width:100%;padding:12px;background:#007bff;color:white;border:0;border-radius:6px;font-size:16px}.info{background:#eef7ff;padding:15px;border-left:5px solid #007bff;margin-bottom:20px}.back{display:block;text-align:center;margin-top:15px}</style></head><body><div class='box'><h1>Event Registration</h1><div class='info'><h2>"+safe(rs.getString("event_name"))+"</h2><p>"+safe(rs.getString("description"))+"</p><p><b>Date:</b> "+rs.getDate("event_date")+" &nbsp; <b>Time:</b> "+rs.getTime("event_time")+"</p><p><b>Venue:</b> "+safe(rs.getString("venue"))+" &nbsp; <b>Category:</b> "+safe(rs.getString("category"))+"</p><p><b>Capacity:</b> "+rs.getInt("capacity")+" &nbsp; <b>Deadline:</b> "+rs.getDate("registration_deadline")+"</p></div><form action='RegisterEventServlet' method='post'><input type='hidden' name='event_id' value='"+id+"'><label>Phone Number</label><input name='phone' required><label>Department</label><input name='department' required><label>Year</label><select name='year' required><option value=''>Select Year</option><option>1st Year</option><option>2nd Year</option><option>3rd Year</option><option>4th Year</option></select><label>Participation Type</label><select name='participation_type'><option>Participant</option><option>Volunteer</option><option>Audience</option></select><label>Message / Special Requirement</label><textarea name='message' rows='4'></textarea><button>Submit Registration</button></form><a class='back' href='eventDetails?event_id="+id+"'>Back to Event Details</a></div></body></html>");}}
        catch(Exception e){response.getWriter().println("<h2>Error: "+e.getMessage()+"</h2>");}
    }
    private String safe(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");}
}
