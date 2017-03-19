package Client;
import java.io.*;
import java.net.*;

public class HeadRequest {

	public static void head(String[] address, int port) throws UnknownHostException, IOException {

		String hostaddress = address[0];
		String afterSlash = address[1];

		//verbinding leggen met site.
		Socket s = new Socket(hostaddress, port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("HEAD " + afterSlash + " HTTP/1.1");
		pw.println("Host: " + hostaddress);
		pw.println("");
		pw.flush();
		
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		
		
		//Aanmaak html file
		File saveFile = new File("HEAD" + hostaddress + ".html");
		//printwriter to htmlfile
		PrintWriter out = new PrintWriter(saveFile);
		String commandLineString;

		//printen respons
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			out.println(commandLineString);
			out.flush();
		}

		//sluiten na uitschrijven
		br.close();
		out.close();

		//later sluiten
		pw.close();
		s.close();
	}
}