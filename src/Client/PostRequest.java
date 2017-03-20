package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class handles a POST request.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 */
public class PostRequest {
	
	/**
	 * Launch the post request. The response received from the HTTP server is displayed on the command line. 
	 * It is also saved in a HTMLfile locally.
	 * 
	 * @param 	hostAddress
	 * 			The hostAddress of the address to connect with.
	 * @param	afterSlash
	 * 			The address after the slash of the address to connect with.
	 * @param	port
	 * 			The number of the port to connect with. 
	 * @post	The input in the command line is read and is used as body for the post request.
	 * @post	The post response of the requested site will be printed in the command line.
	 * @post	The post response of the requested site will be saved in a HTMLfile.
	 * 			The conventions for the file are "POST" + hostAddress + ".html".
	 * @throws	UnknownHostException
	 * @throws	IOException
	 */
	public static void post(String hostAddress, String afterSlash, int port) throws UnknownHostException, IOException {

		//Create socket
		Socket s = new Socket(hostAddress, port);
		
		//Request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		String sentence = "POST "+afterSlash+ " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Content-Type: text/plain" + "\r\n" + "Content-Length: ";
		System.out.print("Give Body:\r\n");
		
		//Input terminal
		BufferedReader inputTerminal = new BufferedReader(new InputStreamReader(System.in));
		
		//Check the input
		String contentTerminal;
		if((contentTerminal = inputTerminal.readLine()) != null){
			int length = contentTerminal.length();
			sentence += length;
			sentence += "\r\n\r\n";
			sentence += contentTerminal;
		}
		System.out.println(sentence);
		pw.print(sentence);
		pw.flush();

		//Respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		//Printwriter to HTMLfile
		PrintWriter out = new PrintWriter("POST"+ hostAddress +".html");
		String commandLineString;

		//Print respons
		String htmlString = "";
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			out.println(commandLineString);
			out.flush();
			htmlString += commandLineString;
		}

		//Close
		br.close();
		inputTerminal.close();
		out.close();
		pw.close();
		s.close();
	}
}
