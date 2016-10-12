import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

 
public class LoginConfirmationServlet extends HttpServlet {
	

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
	
		System.out.println("Print: checking login results...");
		
		
		String loginSuccess = (String) request.getAttribute("loginSuccess");
		System.out.println("Print: " + loginSuccess);
		
		if (loginSuccess.equals("true")) response.sendRedirect("jdbcMVC.jsp");
		else {
			response.sendRedirect("loginResult.jsp");
			System.out.println("Print: going to loginResult.jsp - - ");
		}
		
		
		}//end doGet()
	
	
}
 
