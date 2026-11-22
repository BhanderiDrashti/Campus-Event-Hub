<%@ page import="java.io.*, java.net.*, java.sql.*" %>
<html><body><pre>
<%
out.println("Java: " + System.getProperty("java.version"));
out.println("Free Memory: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + " MB");
out.println("Total Memory: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " MB");
out.println("--- Socket Test 127.0.0.1:3306 ---");
try (Socket s = new Socket()) {
    s.connect(new InetSocketAddress("127.0.0.1", 3306), 2000);
    out.println("SUCCESS: Port 3306 is OPEN!");
} catch (Exception e) {
    out.println("FAILED on 127.0.0.1:3306: " + e.getMessage());
}
out.println("--- Processes ---");
try {
    Process p = Runtime.getRuntime().exec("ps -ef");
    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    String l;
    while ((l = r.readLine()) != null) out.println(l);
} catch (Exception e) { out.println(e); }
out.println("--- Log file /tmp/mariadb.log ---");
File f = new File("/tmp/mariadb.log");
if (f.exists()) {
    BufferedReader br = new BufferedReader(new FileReader(f));
    String l;
    while ((l = br.readLine()) != null) out.println(l);
} else { out.println("/tmp/mariadb.log not found"); }
%>
</pre></body></html>
