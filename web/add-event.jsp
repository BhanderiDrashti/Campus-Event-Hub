<% if (session.getAttribute("admin_id") == null) { response.sendRedirect("admin-login.jsp"); return; } %>
<!DOCTYPE html><html><head><title>Add Event</title><style>
body{font-family:Arial;background:#f4f6f8;margin:0;padding:35px}.box{max-width:600px;margin:auto;background:white;padding:30px;border-radius:12px;box-shadow:0 4px 15px #ccc}h1{text-align:center;color:#2c3e50}label{display:block;margin-top:13px;font-weight:bold}input,textarea,select{width:100%;padding:10px;margin-top:6px;border:1px solid #ccc;border-radius:6px;box-sizing:border-box}textarea{height:100px}button{width:100%;margin-top:20px;padding:12px;border:0;border-radius:6px;background:#2c3e50;color:white;font-size:16px}.back{display:block;text-align:center;margin-top:15px;color:#2c3e50}</style></head><body>
<div class="box"><h1>Add New Event</h1><form action="addEvent" method="post">
<label>Event Name</label><input name="event_name" required>
<label>Description</label><textarea name="description" required></textarea>
<label>Event Date</label><input type="date" name="event_date" required>
<label>Event Time</label><input type="time" name="event_time" required>
<label>Venue</label><input name="venue" required>
<label>Category</label><select name="category" required><option value="">Select</option><option>Technical</option><option>Cultural</option><option>Sports</option><option>Workshop</option><option>Seminar</option><option>Other</option></select>
<label>Maximum Capacity</label><input type="number" name="capacity" min="1" value="100" required>
<label>Registration Deadline</label><input type="date" name="registration_deadline" required>
<label>Status</label><select name="status"><option>Upcoming</option><option>Ongoing</option><option>Completed</option></select>
<button>Add Event</button></form><a class="back" href="admin-dashboard.jsp">Back to Dashboard</a></div></body></html>
