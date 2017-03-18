package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class PutRequest {

	public static void put(String[] address, int port) throws UnknownHostException, IOException {
		
		String newAddress;
		String hostaddress = address[0];
		String afterSlash = address[1];
		
		System.out.print("Give Body: ");
		BufferedReader inputTerminal = new BufferedReader(new InputStreamReader(System.in));
	    String content = inputTerminal.readLine();

		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		//System.out.println("Give Body: ");
		//String content = inFromUser.readLine();
		int length = content.length();
		

		//verbinding leggen met site.
		Socket s = new Socket(InetAddress.getByName(hostaddress), port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("PUT "+afterSlash+" HTTP/1.1");
		pw.println("Host: "+hostaddress);
		pw.println("Content-Type: text/plain");
		pw.println("Content-Length: " + length);
		pw.println("");
		pw.println(content);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		//printwriter to htmlfile
		PrintWriter out = new PrintWriter("testPUT.html");
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
		inputTerminal.close();
		out.close();

		//later sluiten
		pw.close();
		s.close();
	}
}
