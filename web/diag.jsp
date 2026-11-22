<%@ page import="java.io.*, java.net.*, java.sql.*" %>
<html>
<body>
<h2>System & Database Diagnostics</h2>
<pre>
<%
out.println("Java Version: " + System.getProperty("java.version"));
out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
out.println("Free Memory: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + " MB");
out.println("Total Memory: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB");
out.println("Max Memory: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " MB");

out.println("
--- Port 3306 Socket Test ---");
try (Socket s = new Socket()) {
    s.connect(new InetSocketAddress("127.0.0.1", 3306), 2000);
    out.println("SUCCESS: Port 3306 is open and listening!");
} catch (Exception e) {
    out.println("FAILED to connect to 127.0.0.1:3306: " + e);
}

out.println("
--- Running Processes (ps aux) ---");
try {
    Process p = Runtime.getRuntime().exec("ps aux");
    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
        out.println(line);
    }
} catch (Exception e) {
    out.println("Error running ps aux: " + e);
}

out.println("
--- MariaDB Error Log (/var/log/mysql/error.log) ---");
File errLog = new File("/var/log/mysql/error.log");
if (errLog.exists()) {
    try (BufferedReader br = new BufferedReader(new FileReader(errLog))) {
        String line;
        while ((line = br.readLine()) != null) {
            out.println(line);
        }
    } catch (Exception e) {
        out.println("Error reading error.log: " + e);
    }
} else {
    out.println("/var/log/mysql/error.log does not exist");
}
%>
</pre>
</body>
</html>
