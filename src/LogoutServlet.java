import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

 
public class LogoutServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
				   
			HttpSession session = request.getSession(false);
			if(session != null) {
			session.invalidate();
			} 

			response.sendRedirect("login.jsp");
			}
	
}
