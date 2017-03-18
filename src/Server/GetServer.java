package Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class GetServer {
	public static void get(Socket clientSocket, BufferedReader inFromClient, String path) throws IOException{
		
		if (path.isEmpty()){
			path = "index.txt";
		}
		
		//info client inlezen
		String s;
		while ((s = inFromClient.readLine()) != null) {
			System.out.println(s);
			if (s.isEmpty()) {
				break;
			}
		}
		
		//file ophalen
		Date today = new Date(); 
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
		File file = new File(path);
		BufferedReader htmlFile = new BufferedReader(new FileReader(file));
		
		//headers doorsturen
		out.println("HTTP/1.1 200 OK");
		out.println("Content-Type: text/html");
		out.println("Content-Length: "+file.length());
		out.println("Date: "+today);
		out.println("");
		out.flush();		
		
		//file uitlezen en doorsturen
		String t;
		while ((t = htmlFile.readLine()) != null) {
			out.println(t);
			out.flush();
		}
		
		//verbindingen sluiten
		htmlFile.close();
		out.close();
	}
}
