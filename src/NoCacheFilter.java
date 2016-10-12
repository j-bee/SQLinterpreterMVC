import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        chain.doFilter(req, res);
    }

	
	@Override
	public void init(FilterConfig config) {}
	
	@Override
	public void destroy() {}
   
}