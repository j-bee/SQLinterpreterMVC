import java.io.*;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;
import javax.naming.*;
 
public class MySQLServlet extends HttpServlet {
	
	private ConnectionProvider cprov;
	private DAOClass sqlExecutor;
	
	public void init() throws ServletException {
		
		try{
			
		InitialContext ctx = new InitialContext();
		DataSource dbPool = (DataSource)ctx.lookup("java:comp/env/jdbc/ebookDB");
		if(dbPool==null) {throw new ServletException("Unknown DataSource 'jdbc/ebookDB'");}
		
		cprov = new ConnectionProvider(dbPool);
		sqlExecutor = new DAOClass(cprov);
		//inicjalizacja DatabaseConnectionPool
			
		}catch (NamingException ex) {
         ex.printStackTrace();
      } 
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
		
		PrintWriter out = response.getWriter();
		String[] columns = null;
		String sqlquery = null;
		List<Book> resultList = null;
		int sqlUpdateCount = -1;
		String sqlUpdateMessage = null;
		String errorMsg = null;
		
		
		try{
		sqlquery = request.getParameter("sqlquery");	
		}catch(Exception ex) {}
		
		Boolean isSelect = sqlExecutor.isSelect(sqlquery);
		
		
		if(isSelect != null) {
			
		if(isSelect) {
			
		try{	
		columns = sqlExecutor.getQueryParams(sqlquery);
		resultList = sqlExecutor.getBookList(columns, sqlquery);
		request.setAttribute("columns", columns);
		request.setAttribute("resultList", resultList);
		}catch(Exception ex) {
			errorMsg = "Invalid request parameters!";
			request.setAttribute("errorMsg", errorMsg);}
			
		}
		else {
		sqlUpdateCount = sqlExecutor.getAffectedRowsCount();
		if(sqlUpdateCount == -1) sqlUpdateMessage = "There was a problem processing your statement";
		else sqlUpdateMessage = "Query ok, " + sqlUpdateCount + " rows affected.";
		
		request.setAttribute("sqlMessage", sqlUpdateMessage);}		
		
		}
		
		else { // isSelect = null;
		errorMsg = "There was an error: \n" + sqlExecutor.getErrorMsg();
		request.setAttribute("errorMsg", errorMsg);
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("view.jsp");
		dispatcher.forward(request, response);
		
		
		}//end doPost()
	
	
}
 
