import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.*;
 
public class UserConfirmationServlet extends HttpServlet {
	
	private ConnectionProvider cprov;
	private DAOClass sqlExecutor;
	
	public void init() throws ServletException {
		
		try{
			
		InitialContext ctx = new InitialContext();
		DataSource dbPool = (DataSource)ctx.lookup("java:comp/env/jdbc/usersDB");
		if(dbPool==null) {throw new ServletException("Unknown DataSource 'jdbc/usersDB'");}
		
		cprov = new ConnectionProvider(dbPool);
		sqlExecutor = new DAOClass(cprov);
			
		}catch (NamingException ex) {
         ex.printStackTrace();
      } 
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
	

		String confResult = null;
		String querystring = request.getQueryString();
		int ldHCode = -1;
		
		System.out.println("Print: confirmation login runs... Query String: " + querystring);
		String[] params = querystring.split("&");
		if(params.length!=3) confResult = "invalid parameter";
		
		else{
			String username = params[0].substring(params[0].lastIndexOf('=')+1);
			String uuid = params[1].substring(params[1].lastIndexOf('=')+1);
			
			try{
			ldHCode = Integer.parseInt(params[2].substring(params[2].lastIndexOf('=')+1));
			}catch(NumberFormatException ex) {
				System.err.println("Print: NumberFormatException: " + ex.getMessage());
			}
			
			confResult = sqlExecutor.confirmUser(username, uuid);
			
			if(confResult.equals("OK")) {
				
			Map<Integer, ScheduledExecutorService> linkDestroyerMap = 
			(Map<Integer, ScheduledExecutorService>) getServletContext().getAttribute("linkDestroyerMap");
			
			ScheduledExecutorService linkDestroyer = linkDestroyerMap.get(ldHCode);
			if(ldHCode!=linkDestroyer.hashCode()) System.out.println("Print: FATAL ERROR: HASHCODE COLLISION");
			
			
				if(linkDestroyer!=null) {
			linkDestroyer.shutdownNow();
		
			try{
			linkDestroyer.awaitTermination(0, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				System.err.println("Print: InterruptedException: " + e.getMessage());
			}
			finally {linkDestroyerMap.remove(ldHCode);
			System.out.println("Print: linkDestroyer removed, hashcode: " + ldHCode);
			}
				}//(linkDestroyer!=null)
			}//(confResult.equals("OK"))
			
		}
		
		request.setAttribute("confResult", confResult);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/confirmationView.jsp");
		dispatcher.forward(request, response);
		
		
		//response.sendRedirect("confirmationView.jsp");
	
		}//end doGet()
	
	
}
 