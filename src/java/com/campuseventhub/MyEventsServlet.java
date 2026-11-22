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

@WebServlet("/myEvents")
public class MyEventsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
        HttpSession s=request.getSession(false);if(s==null||s.getAttribute("student_id")==null){response.sendRedirect("login.jsp");return;}int sid=(Integer)s.getAttribute("student_id");response.setContentType("text/html;charset=UTF-8");
        String sql="SELECT er.*,e.event_name,e.event_date,e.event_time,e.venue,e.status event_status FROM event_registrations er JOIN events e ON er.event_id=e.event_id WHERE er.student_id=? ORDER BY e.event_date";
        StringBuilder b=new StringBuilder("<!DOCTYPE html><html><head><title>My Events</title><style>body{font-family:Arial;background:#f4f6f8;padding:25px}table{width:100%;border-collapse:collapse;background:white}th{background:#2c3e50;color:white;padding:11px}td{padding:10px;border:1px solid #ddd;text-align:center}.btn{padding:6px 10px;border-radius:5px;text-decoration:none;color:white;background:#007bff;margin:2px;display:inline-block}.cancel{background:#dc3545}.feedback{background:#6f42c1}.certificate{background:#198754}.approved{color:green;font-weight:bold}.rejected{color:red;font-weight:bold}.pending{color:#d68910;font-weight:bold}.cancelled{color:#777;font-weight:bold}.back{display:inline-block;margin-top:20px;padding:10px 18px;background:#555;color:white;text-decoration:none;border-radius:5px}</style></head><body><h1>My Registered Events</h1><table><tr><th>ID</th><th>Event</th><th>Date</th><th>Venue</th><th>Status</th><th>Actions</th></tr>");
        try(Connection con=DBConnection.getConnection();PreparedStatement ps=con.prepareStatement(sql)){ps.setInt(1,sid);ResultSet rs=ps.executeQuery();boolean found=false;while(rs.next()){found=true;String st=rs.getString("status");String cls=st.toLowerCase();b.append("<tr><td>"+rs.getInt("registration_id")+"</td><td>"+esc(rs.getString("event_name"))+"</td><td>"+rs.getDate("event_date")+"</td><td>"+esc(rs.getString("venue"))+"</td><td class='"+cls+"'>"+st+"</td><td><a class='btn' href='registrationReceipt?registration_id="+rs.getInt("registration_id")+"'>Receipt</a>");if("Pending".equals(st)||"Approved".equals(st))b.append("<a class='btn cancel' href='cancelRegistration?registration_id="+rs.getInt("registration_id")+"' onclick=\"return confirm('Cancel this registration?')\">Cancel</a>");if("Completed".equals(rs.getString("event_status"))&&"Approved".equals(st))b.append("<a class='btn feedback' href='feedback?event_id="+rs.getInt("event_id")+"'>Feedback</a>");if("Approved".equals(st)&&rs.getBoolean("attended"))b.append("<a class='btn certificate' href='certificate?registration_id="+rs.getInt("registration_id")+"'>Certificate</a>");b.append("</td></tr>");}if(!found)b.append("<tr><td colspan='6'>No registrations yet.</td></tr>");}catch(Exception e){b.append("<tr><td colspan='6'>Error: "+e.getMessage()+"</td></tr>");}b.append("</table><a class='back' href='studentDashboard.jsp'>Back to Dashboard</a></body></html>");response.getWriter().print(b.toString());
    }
    private String esc(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");}
}
