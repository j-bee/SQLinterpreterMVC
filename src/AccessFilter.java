import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccessFilter implements Filter {
	@Override
	public void init(FilterConfig config) {
		System.out.println("Print: Filter initialized!");
	}
	
	@Override
	public void destroy() {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

		String requestPath = request.getRequestURI();
		String username = null;
		
		if(session!=null) username = (String) session.getAttribute("username");

		
		if((username == null) && (isNotAccessibleByDefault(requestPath))) {
			
			System.out.println("Print: Access denied: " + requestPath + "\n"
			+ "Filter redirecting to login page...");
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		}
		
        else {
			System.out.println("Print: Filter allowed access ->  " + 
			requestPath);
            chain.doFilter(req, res);
        }
    }
	
	
	public static boolean isNotAccessibleByDefault(String requestPath) {
		
		String[] accessiblePages = {"login.jsp", "register.jsp", "reg.do", "login.do", ".css", "loginResult.jsp", "confirmationView.jsp", "userconfirm"};
		
		for(int i = 0; i<accessiblePages.length; i++) {
			if(requestPath.endsWith(accessiblePages[i])) {

				return false;
			}
		}
		return true;
	}
 
}