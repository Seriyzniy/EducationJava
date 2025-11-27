package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
	@Override
    protected void doGet(
	    HttpServletRequest req, 
	    HttpServletResponse resp) throws ServletException, IOException 
	{
		resp.setContentType("text/html;charset=UTF-8");

        HttpServletMapping mapping = req.getHttpServletMapping();

        PrintWriter writer = resp.getWriter();
        writer
	        .println("<h3>Marshutization info:</h3>");
	        
        writer.println("<ul>");
        writer.println("<li>Servlet name: " 
		        + mapping.getServletName() + "</li>"
		    );
        
        writer.println("<li>Pattern: " + mapping.getPattern() + "</li>");
        
        writer.println("<li>Match value: " 
		        + mapping.getMatchValue() + "</li>");
        
        writer.println("<li>Mapping type: " 
		        + mapping.getMappingMatch() + "</li>");
    
        writer.println("</ul>");
    }
}
	
