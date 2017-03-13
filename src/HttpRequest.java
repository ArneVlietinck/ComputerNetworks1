import java.io.*;
import java.net.*;

public class HttpRequest {
	
//	//path: URL path e.g. "google.com"
//	public String path;
//	
//	
//	public HttpRequest(HTTPCommands httpCommand, String path) throws UnknownHostException, IOException{
//		if(path != null){
//			this.path = path; 
//		}
//		
//		// port: Port number (default 80)
//		int port = 80;
//		
//		
//		path = "google.com";
//		Socket socket = new Socket(InetAddress.getByName(path), port);
//		
//		//Get input and output streams for the socket
//	    OutputStream out = socket.getOutputStream();
//	    InputStream in = socket.getInputStream();
//	}
//
//	
//	/////Getter & Setters/////
//	
//	public String getPath(){
//		return path;
//	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub

		
		//if argument == GET voer dit uit, splits de link!
		
		int port = 80;
		String address = "example.com";
		
		//verbinding leggen met site.
		Socket s = new Socket(InetAddress.getByName(address), port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET / HTTP/1.1");
		pw.println("Host: "+address);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		//printwriter to textfile
		PrintWriter out = new PrintWriter("test.html");
		String t;
		//printen respons
		while((t = br.readLine()) != null){
			System.out.println(t);
			out.println(t);
		}
		//sluiten na uitschrijven
		br.close();
		out.close();
		
		//images uithalen?
		
		
		//later sluiten
		pw.close();
		s.close();
	}
}
