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

		String hostAddress = address[0];
		String afterSlash = address[1];

		//verbinding leggen met site.
		Socket s = new Socket(hostAddress, port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		String sentence = "PUT "+afterSlash+ " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Content-Type: text/plain" + "\r\n" + "\r\n";
		System.out.print("Give Body:\r\n");

		BufferedReader inputTerminal = new BufferedReader(new InputStreamReader(System.in));
		String contentTerminal;
		if((contentTerminal = inputTerminal.readLine()) != null){
			sentence += contentTerminal;
		}
		System.out.println(sentence);
		pw.print(sentence);
		pw.flush();

		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		//printwriter to htmlfile
		PrintWriter out = new PrintWriter("PUT"+ hostAddress +".html");
		String commandLineString;

		//printen respons
		String htmlString = "";
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			out.println(commandLineString);
			out.flush();
			htmlString += commandLineString;
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
