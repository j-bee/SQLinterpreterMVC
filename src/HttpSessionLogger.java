import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class HttpSessionLogger implements HttpSessionListener, ServletContextListener {

    public void sessionCreated(HttpSessionEvent event) {
		
		HttpSession session = event.getSession();
		String username = (String) session.getAttribute("username");
		
        System.out.printf("Session ID %s created at %s%n", session.getId(), new Date());
		System.out.println("CreatedSession username: " + username);
		session.setMaxInactiveInterval(20); 
		System.out.println("MaxInactiveInterval: " + session.getMaxInactiveInterval());
		

		
    }
	
    public void sessionDestroyed(HttpSessionEvent event) {
		
		HttpSession session = event.getSession();
		String username = (String) session.getAttribute("username");
		
        System.out.printf("Session ID %s destroyed at %s%n", session.getId(), new Date());
		System.out.println("DestroyedSession username: " + username);
		
    }
	
	
	public void contextInitialized(ServletContextEvent sce) {
		
		ServletContext servletContext = sce.getServletContext();
		servletContext.setAttribute("userCounter", new AtomicInteger());
		System.out.println("Print: initializing userCounter");
		
		//czy to jest dobry sposób?
		Map<Integer, ScheduledExecutorService> linkDestroyerMap = new HashMap<Integer, ScheduledExecutorService>();
		servletContext.setAttribute("linkDestroyerMap", linkDestroyerMap);
	}
	
	public void contextDestroyed(ServletContextEvent sce) {}

	
}