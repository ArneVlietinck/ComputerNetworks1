import java.io.*;
import java.net.*;

public class HttpRequest {
	
	//path: URL path e.g. "google.com"
	public String path;
	
	
	public HttpRequest(HTTPCommands httpCommand, String path) throws UnknownHostException, IOException{
		if(path != null){
			this.path = path; 
		}
		
		// port: Port number (default 80)
		int port = 80;
		
		
		path = "google.com";
		Socket socket = new Socket(InetAddress.getByName(path), port);
		
		//Get input and output streams for the socket
	    OutputStream out = socket.getOutputStream();
	    InputStream in = socket.getInputStream();
	}

	
	/////Getter & Setters/////
	
	public String getPath(){
		return path;
	}
}
