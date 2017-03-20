package Client;
import java.io.*;
import java.net.*;

import imageRecognition.ImageRecognition;

/**
* This class handles a GET request.
* 
* @author Laura Vranken
* @author Arne Vlietinck
*/
public class GetRequest {
	
	/**
	 * Launch the get request. The response received from the HTTP server is displayed on the terminal. 
	 * It is also saved in a HTMLfile locally.
	 * 
	 * @param 	hostAddress
	 * 			The hostAddress of the address to connect with.
	 * @param	afterSlash
	 * 			The address after the slash of the address to connect with.
	 * @param	port
	 * 			The number of the port to connect with. 
	 * @post	The get response of the requested site will be printed in the terminal.
	 * @post	The get response (without header) of the requested site will be saved in a HTMLfile.
	 * 			The conventions for the file are "GET" + hostAddress + ".html".
	 * @throws	UnknownHostException
	 * @throws	IOException
	 */
	public static void get(String hostAddress, String afterSlash, int port) throws UnknownHostException, IOException {

		//Create socket
		Socket s = new Socket(hostAddress, port);
		
		//Request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		String sentence = "GET "+afterSlash+ " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
		pw.print(sentence);
		pw.flush();

		//Respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		//Create htmlfile
		File saveFile = new File("GET" + hostAddress + ".html");
		
		//Printwriter to htmlfile
		PrintWriter out = new PrintWriter(saveFile);
		String commandLineString;

		//Print the respons, looking for the html file (without header)
		String htmlString = "";
		boolean startHtmlFound = false;
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			if (commandLineString.indexOf("html>") != -1){
				startHtmlFound = true;
			}
			if(startHtmlFound){
				out.println(commandLineString);
				out.flush();
				htmlString += commandLineString;
			}
		}

		//Close
		br.close();
		out.close();		

		//Search & save image in file
		ImageRecognition.searchImage(saveFile,hostAddress);

		//Close
		pw.close();
		s.close();
	}


			
}

