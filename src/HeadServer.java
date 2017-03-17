import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class HeadServer {
	public static void head(Socket clientSocket, BufferedReader inFromClient, String path) throws IOException{
		
		if (path.isEmpty()){
			path = "index.txt";
		}
		
		//info client uitlezen
		String s;
		while ((s = inFromClient.readLine()) != null) {
			System.out.println(s);
			if (s.isEmpty()) {
				break;
			}
		}
		
		//file uitlezen
		Date today = new Date(); 
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		File file = new File(path);
		
		//headers doorsturen
		out.println("HTTP/1.1 200 OK");
		out.println("Content-Type: text/html");
		out.println("Content-Length: "+file.length());
		out.println("Date: "+today);
		out.println("");
		out.flush();
		
		//sluiten
		out.close();
	}
}
