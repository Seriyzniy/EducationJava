package main.java.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FileManagerServlet extends HttpServlet{
	private String pathToSave;
	private final String fileName = "yourFile";
	
	public FileManagerServlet(){
		System.out.println("\n[INFO] In CONSTRUCTOR Servlet \n");
		try {
			pathToSave = System.getenv("SAVE_FILE_PATH");
			
		} catch (Exception e){
			System.out.println("[Exception]: Exception on read ENV");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("\n[INFO] In POST method Servlet \n");
		try(BufferedReader requestReader = req.getReader();
			PrintWriter fileWriter = new PrintWriter(new File(pathToSave + fileName)))
		{
			String line;
			while((line = requestReader.readLine()) != null) {
				fileWriter.write(line);
			}
		} catch(FileNotFoundException e) {
			System.out.println("[Exception] Exception on OPNE file by path:" + pathToSave+fileName);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("\n[INFO] In GET method Servlet \n");
		try(BufferedReader fileReader = new BufferedReader(new FileReader(pathToSave + fileName));
			PrintWriter responseWriter = resp.getWriter())
		{
			String line;
			while((line = fileReader.readLine()) != null) {
				responseWriter.write(line);
			}
		} 
	}
}
