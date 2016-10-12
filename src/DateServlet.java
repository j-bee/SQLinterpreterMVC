import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DateServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
		
		Date date = new Date();
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		out.write(date.toString());
		
		
		}
	
	
}
 
