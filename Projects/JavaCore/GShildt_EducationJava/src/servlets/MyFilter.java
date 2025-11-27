package servlets;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class MyFilter implements Filter{

	@Override
	public void doFilter(
			ServletRequest req, 
			ServletResponse resp, 
			FilterChain filterChain) throws IOException, ServletException 
	{
		System.out.println("Log: In filter");
		
		Optional<String> userName = Optional.ofNullable(req.getParameter("name"));
		
		if(userName.isEmpty()) {
			resp.setContentType("text/plain");
			ServletOutputStream writerResponce = resp.getOutputStream();
			writerResponce.println("Ebat loshara, input your name");
			return;
		}
		
		filterChain.doFilter(req, resp);
	}
}