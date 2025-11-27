import java.util.List;
import java.util.Random;

public class MyMain {
	static List<String> PHRASES = List.of(
			"Freedom and peace",
			"Forever young",
			"Dare to dream",
			"Beauty and the Beast",
			"Blind rage",
			"Aspire to inspire"
			);
			
	
    public static void main(String[] args){
    	String dbUser = System.getenv("JAVA_HOME");
    	
    	System.out.println(dbUser);
    }
}