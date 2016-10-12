import java.io.*;
import java.util.concurrent.atomic.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class User implements Serializable, HttpSessionBindingListener {

	private int id;
	private String username;
	private String email;
    private String password;
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public int getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	 @Override
    public void valueBound(HttpSessionBindingEvent event) {
		
        if(username!=null) {
			
			HttpSession session = event.getSession();
			ServletContext servletContext = session.getServletContext();
			AtomicInteger userCounter = (AtomicInteger) servletContext.
			getAttribute("userCounter");
	
			userCounter.incrementAndGet();
			System.out.println("Print: logged in users: " + userCounter.get());
			}
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
		
        if(username!=null) {
			
			HttpSession session = event.getSession();
			ServletContext servletContext = session.getServletContext();
			AtomicInteger userCounter = (AtomicInteger) servletContext.
			getAttribute("userCounter");
			
			userCounter.decrementAndGet();
			System.out.println("Print: logged in users: " + userCounter.get());
			}
    }
	
}
