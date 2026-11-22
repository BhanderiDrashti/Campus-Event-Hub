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

@WebServlet("/studentEvents")
public class StudentViewEventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false);if(session==null||session.getAttribute("student_id")==null){response.sendRedirect("login.jsp");return;}
        response.setContentType("text/html;charset=UTF-8"); String search=request.getParameter("search"),category=request.getParameter("category"); if(search==null)search="";if(category==null)category="";
        String sql="SELECT e.*, (SELECT COUNT(*) FROM event_registrations er WHERE er.event_id=e.event_id AND er.status IN ('Pending','Approved')) registered FROM events e WHERE e.status<>'Completed' AND e.event_name LIKE ? AND e.category LIKE ? ORDER BY e.event_date,e.event_time";
        StringBuilder b=new StringBuilder();b.append("<!DOCTYPE html><html><head><title>Available Events</title><style>body{font-family:Arial;background:#f4f6f8;margin:0;padding:25px}.top{background:#007bff;color:white;padding:20px;border-radius:10px}.filter{background:white;padding:18px;margin:20px 0;border-radius:10px}.filter input,.filter select{padding:10px;margin-right:8px}.filter button{padding:10px 18px;background:#007bff;color:white;border:0;border-radius:5px}table{width:100%;background:white;border-collapse:collapse}th{background:#2c3e50;color:white;padding:11px}td{padding:10px;text-align:center;border:1px solid #ddd}.btn{padding:7px 12px;background:#28a745;color:white;text-decoration:none;border-radius:5px}.details{background:#007bff}.back{display:inline-block;margin-top:20px;padding:10px 18px;background:#555;color:white;text-decoration:none;border-radius:5px}</style></head><body><div class='top'><h1>Available Campus Events</h1><p>Search events and register online.</p></div><form class='filter' method='get'><input name='search' placeholder='Search event name' value='"+esc(search)+"'><select name='category'><option value=''>All Categories</option>");
        String[] cats={"Technical","Cultural","Sports","Workshop","Seminar","Other"};for(String c:cats)b.append("<option "+(c.equals(category)?"selected":"")+">"+c+"</option>");b.append("</select><button>Search</button></form><table><tr><th>Event</th><th>Category</th><th>Date</th><th>Venue</th><th>Seats</th><th>Deadline</th><th>Action</th></tr>");
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){ps.setString(1,"%"+search+"%");ps.setString(2,"%"+category+"%");ResultSet rs=ps.executeQuery();boolean found=false;while(rs.next()){found=true;int seats=rs.getInt("capacity")-rs.getInt("registered");b.append("<tr><td>"+esc(rs.getString("event_name"))+"</td><td>"+esc(rs.getString("category"))+"</td><td>"+rs.getDate("event_date")+"</td><td>"+esc(rs.getString("venue"))+"</td><td>"+Math.max(seats,0)+" / "+rs.getInt("capacity")+"</td><td>"+rs.getDate("registration_deadline")+"</td><td><a class='btn details' href='eventDetails?event_id="+rs.getInt("event_id")+"'>Details</a></td></tr>");}if(!found)b.append("<tr><td colspan='7'>No events found.</td></tr>");}catch(Exception e){b.append("<tr><td colspan='7'>Error: "+e.getMessage()+"</td></tr>");}b.append("</table><a class='back' href='studentDashboard.jsp'>Back to Dashboard</a></body></html>");response.getWriter().print(b.toString());
    }
    private String esc(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");}
}
