import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.*;
 
public class LoginServlet extends HttpServlet {
	
	private ConnectionProvider cprov;
	private DAOClass sqlExecutor;
	
	public void init() throws ServletException {
		
		try{
			
		InitialContext ctx = new InitialContext();
		DataSource dbPool = (DataSource)ctx.lookup("java:comp/env/jdbc/usersDB");
		if(dbPool==null) {throw new ServletException("Unknown DataSource 'jdbc/usersDB'");}
		
		cprov = new ConnectionProvider(dbPool);
		sqlExecutor = new DAOClass(cprov);
		//inicjalizacja DatabaseConnectionPool
			
		}catch (NamingException ex) {
         ex.printStackTrace();
      } 
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
	
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		String loginStatus = sqlExecutor.loginUser(username, password);
		request.setAttribute("loginStatus", loginStatus);
		
		if(loginStatus.equals("OK") && (username!=null)) {
		
		System.out.println("Print: Login servlet successful!");
		
		HttpSessionLogger sessionCounter = new HttpSessionLogger();		
		request.setAttribute("sessionCounter", sessionCounter);

		HttpSession session = request.getSession(false);
		System.out.printf("Session ID %s created at %s%ngoes to  -- %s -- %n", request.getSession().getId(), new Date(), username);
		System.out.println("MaxInactiveInterval: " + session.getMaxInactiveInterval());
		
		session.setAttribute("username", username);
		User user = new User();
		user.setUsername("username");
		session.setAttribute("user", user);
		
		response.sendRedirect("jdbcMVC.jsp");

		}
		
		else {
		System.out.println("Print: login failed . . . ");
		System.out.println("Print: login status: " + loginStatus);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/loginResult.jsp");
		dispatcher.forward(request, response);
		}

		}//end doPost()
	
	
}
 
