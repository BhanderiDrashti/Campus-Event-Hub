package com.campuseventhub;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/editEvent")
public class EditEventServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false); if(session==null||session.getAttribute("admin_id")==null){response.sendRedirect("admin-login.jsp");return;}
        int id=Integer.parseInt(request.getParameter("event_id")); response.setContentType("text/html;charset=UTF-8"); PrintWriter out=response.getWriter();
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement("SELECT * FROM events WHERE event_id=?")){
            ps.setInt(1,id); ResultSet rs=ps.executeQuery();
            if(rs.next()){
                out.println("<!DOCTYPE html><html><head><title>Edit Event</title><style>body{font-family:Arial;background:#f4f6f8;padding:35px}.box{max-width:600px;margin:auto;background:white;padding:30px;border-radius:12px}input,textarea,select{width:100%;padding:10px;margin:7px 0 12px;box-sizing:border-box}button{width:100%;padding:12px;background:#2c3e50;color:white;border:0;border-radius:6px}.back{display:block;text-align:center;margin:15px}</style></head><body><div class='box'><h1>Edit Event</h1><form action='editEvent' method='post'>");
                out.println("<input type='hidden' name='event_id' value='"+id+"'>");
                out.println("<label>Event Name</label><input name='event_name' value='"+safe(rs.getString("event_name"))+"' required>");
                out.println("<label>Description</label><textarea name='description' required>"+safe(rs.getString("description"))+"</textarea>");
                out.println("<label>Date</label><input type='date' name='event_date' value='"+rs.getDate("event_date")+"' required>");
                out.println("<label>Time</label><input type='time' name='event_time' value='"+rs.getTime("event_time").toString().substring(0,5)+"' required>");
                out.println("<label>Venue</label><input name='venue' value='"+safe(rs.getString("venue"))+"' required>");
                out.println("<label>Category</label><select name='category'>"); String[] cats={"Technical","Cultural","Sports","Workshop","Seminar","Other"}; for(String c:cats) out.println("<option "+(c.equals(rs.getString("category"))?"selected":"")+">"+c+"</option>"); out.println("</select>");
                out.println("<label>Capacity</label><input type='number' name='capacity' min='1' value='"+rs.getInt("capacity")+"' required>");
                out.println("<label>Registration Deadline</label><input type='date' name='registration_deadline' value='"+rs.getDate("registration_deadline")+"' required>");
                out.println("<label>Status</label><select name='status'>"); String[] sts={"Upcoming","Ongoing","Completed"}; for(String s:sts) out.println("<option "+(s.equals(rs.getString("status"))?"selected":"")+">"+s+"</option>"); out.println("</select><button>Update Event</button></form><a class='back' href='viewEvents'>Back</a></div></body></html>");
            }
        }catch(Exception e){out.println("<h2>Error: "+e.getMessage()+"</h2>");}
    }
    protected void doPost(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession session=request.getSession(false); if(session==null||session.getAttribute("admin_id")==null){response.sendRedirect("admin-login.jsp");return;}
        try(Connection con=DBConnection.getConnection()){
            String sql="UPDATE events SET event_name=?,description=?,event_date=?,event_time=?,venue=?,category=?,capacity=?,registration_deadline=?,status=? WHERE event_id=?";
            PreparedStatement ps=con.prepareStatement(sql); ps.setString(1,request.getParameter("event_name")); ps.setString(2,request.getParameter("description")); ps.setDate(3,Date.valueOf(request.getParameter("event_date"))); ps.setTime(4,Time.valueOf(request.getParameter("event_time")+":00")); ps.setString(5,request.getParameter("venue")); ps.setString(6,request.getParameter("category")); ps.setInt(7,Integer.parseInt(request.getParameter("capacity"))); ps.setDate(8,Date.valueOf(request.getParameter("registration_deadline"))); ps.setString(9,request.getParameter("status")); ps.setInt(10,Integer.parseInt(request.getParameter("event_id"))); ps.executeUpdate(); response.sendRedirect("viewEvents");
        }catch(Exception e){response.setContentType("text/html;charset=UTF-8");response.getWriter().println("<h2>Error: "+e.getMessage()+"</h2>");}
    }
    private String safe(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;");}
}
