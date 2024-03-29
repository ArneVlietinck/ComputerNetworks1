package Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import imageRecognition.ImageRecognition;
/**
 * This server class responds to the get request of the client. It retrieves the requested page.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 *
 */
public class GetServer {
	/**
	 * This function retrieves the requested html file.
	 * 
	 * @param clientSocket
	 * 			The socket where the server is connected to.
	 * @param inFromClient
	 * 			The buffered inputstream which the server receives from the client.
	 * @param path
	 * 			The path of the file which has to be retrieved.
	 * @throws IOException
	 */
	public static void get(Socket clientSocket, BufferedReader inFromClient, PrintWriter out, String path, String http) throws IOException{

		String statusCode = "";

		//if path not specified, return index page.
		if (path.isEmpty()){
			path = "index.txt";
		}

		boolean noHeader = false;
		//info client inlezen
		String s;
		while ((s = inFromClient.readLine()) != null) {
			noHeader = true;
			System.out.println(s);
			if (s.isEmpty()) {
				break;
			}
		}
		
		
		if(noHeader == false && http == "1.0"){
			out.println("HTTP/1.0 400 Bad Request");
		}

		//file ophalen
		Date today = new Date(); 
		File file = null;
		try{
			file = new File(path);
			BufferedReader htmlFile = new BufferedReader(new FileReader(file));
			
			statusCode = "200 OK";

			//headers doorsturen
			out.println("HTTP/"+http+ " "+statusCode);
			out.println("Content-Type: text/html");
			out.println("Content-Length: "+file.length());
			out.println("Date: "+today);
			out.println("is-modiefied-since: " + file.lastModified());
			out.println("");
			out.flush();	
			
			
			//file uitlezen en doorsturen
			String t;
			while ((t = htmlFile.readLine()) != null) {
				out.println(t);
				out.flush();
			}
			
		}catch(Exception e){
			//page not found
			statusCode = "404 Not Found";
			out.println("HTTP/"+http+ " "+statusCode);
			out.println("");
			out.flush();
		}

		//close connection when HTTP/1.0 is used.
		if (http == "1.0"){
			out.close();
		}
		
	}
}
