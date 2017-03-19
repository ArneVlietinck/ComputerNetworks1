package Client;
import java.io.*;
import java.net.*;

public class GetRequest {
	public static void get(String[] address, int port) throws UnknownHostException, IOException {

		String newAddress;
		String hostaddress = address[0];
		String afterSlash = address[1];
		
		//verbinding leggen met site.
		Socket s = new Socket(hostaddress, port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET "+afterSlash+" HTTP/1.1");
		pw.println("Host: "+hostaddress);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		//printwriter to htmlfile
		PrintWriter out = new PrintWriter(afterSlash+".html");
		String commandLineString;
		
		//printen respons
		while((commandLineString = br.readLine()) != null){
			out.println(commandLineString);
			out.flush();
		}


		//sluiten na uitschrijven
		br.close();
		out.close();

		//images uithalen?
		//JSOUP?
		

		//later sluiten
		pw.close();
		s.close();
	}
}
