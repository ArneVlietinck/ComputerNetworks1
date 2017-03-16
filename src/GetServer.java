import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class GetServer {
	public static void get(Socket clientSocket, BufferedReader inFromClient) throws IOException{
		
		String s;
		while ((s = inFromClient.readLine()) != null) {
			System.out.println(s);
			if (s.isEmpty()) {
				break;
			}
		}
		
		Date today = new Date(); 
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		File file = new File("test.txt");
		BufferedReader htmlFile = new BufferedReader(new FileReader(file));
		
		out.println("HTTP/1.1 200 OK");
		out.println("Content-Type: text/html");
		out.println("Content-Length: "+file.length());
		out.println("Date: "+today);
		out.println("");
		out.flush();		
		
		String t;
		while ((t = htmlFile.readLine()) != null) {
			out.println(t);
			out.flush();
		}
		htmlFile.close();
		out.close();
	}
}
