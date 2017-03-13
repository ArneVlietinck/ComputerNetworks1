import java.io.*;
import java.net.*;

public class GetRequest {
	public static void get(String[] address, int port) throws UnknownHostException, IOException {

		String newAddress = "";
		String hostaddress = address[0];
		String afterSlash =address[1];
		
		//verbinding leggen met site.
		Socket s = new Socket(InetAddress.getByName(hostaddress), port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET "+afterSlash+" HTTP/1.1");
		pw.println("Host: "+hostaddress);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		//printwriter to textfile
		PrintWriter out = new PrintWriter("test1.html");
		String commandLineString;
		
		//printen respons
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
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

		//images uithalen?
		//JSOUP?
		

		//later sluiten
		pw.close();
		s.close();
	}
}

