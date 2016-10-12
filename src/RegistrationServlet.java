import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.*;
 
public class RegistrationServlet extends HttpServlet {
	
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
		
		String email = request.getParameter("email");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String uuid = UUIDGenerator.generateUUID();
		
		String registerStatus = sqlExecutor.insertNewUser(username, email, password, uuid);
		request.setAttribute("registerStatus", registerStatus);
		
		//jeśli kłopot z wysłaniem maila - usuń użytkownika z DB
		//na razie zakładam że mail będzie działał...
		
		if(registerStatus.equals("OK")) {
			
			System.out.println("Print: register status - " + registerStatus);
			System.out.println("Print: uuid - " + uuid);
			
			try{
			ScheduledExecutorService linkDestroyer = sqlExecutor.initAndRunLinkDestroyer(username);
			Map<Integer, ScheduledExecutorService> linkDestroyerMap = 
			(Map<Integer, ScheduledExecutorService>) getServletContext().getAttribute("linkDestroyerMap");
			
			int ldHCode = linkDestroyer.hashCode();
			linkDestroyerMap.put(ldHCode, linkDestroyer);
			System.out.println("Print: LinkDestroyer put in map, hashcode: " + ldHCode);
			
			MailSender.sendConfirmationMail(username, email, password, uuid, ldHCode);
			}catch(Exception ex) {
				System.out.println("Print: REGISTRATION ERROR - " + ex.getMessage());
			}
			
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("registrationResult.jsp");
		dispatcher.forward(request, response);
		
		
		}//end doPost()
	
	
}
 
