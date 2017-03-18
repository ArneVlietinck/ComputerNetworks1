package Client;
import java.io.*;
import java.net.*;

public class HeadRequest {

	public static void head(String[] address, int port) throws UnknownHostException, IOException {

		String newAddress;
		String hostaddress = address[0];
		String afterSlash = address[1];
		
		//verbinding leggen met site.
		Socket s = new Socket(InetAddress.getByName(hostaddress), port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("HEAD "+afterSlash+" HTTP/1.1");
		pw.println("Host: "+hostaddress);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		//printwriter to htmlfile
		PrintWriter out = new PrintWriter("testHEAD.html");
		String commandLineString;
		
		//printen respons
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			//TODO: Waarom zit deze extra lijn erin met nog eens de locatie maar dan zonder "Location: "
			if (commandLineString.contains("Location:")){
				newAddress = commandLineString.replace("Location: ","");
				System.out.println(newAddress);
			}
			out.println(commandLineString);
			out.println("");
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
