package Client;
import java.io.*;
import java.net.*;

public class HeadRequest {

	/**
	 * Launch the head request. The response received from the HTTP server is displayed on the terminal. 
	 * It is also saved in a HTMLfile locally.
	 * 
	 * @param 	hostAddress
	 * 			The hostAddress of the address to connect with.
	 * @param	afterSlash
	 * 			The address after the slash of the address to connect with.
	 * @param	port
	 * 			The number of the port to connect with. 
	 * @post	The head of the requested site will be printed in the terminal.
	 * @post	The head of the requested site will be saved in a HTMLfile.
	 * 			The conventions for the file are "HEAD" + hostAddress + ".html".
	 * @throws	UnknownHostException
	 * @throws	IOException
	 */
	public static void head(String hostAddress, String afterSlash, int port) throws UnknownHostException, IOException {

		//Create socket
		Socket s = new Socket(hostAddress, port);
		
		//Request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		String sentence = "HEAD "+ afterSlash + " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "\r\n";
		pw.print(sentence);
		pw.flush();
		
		//Respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		//Create htmlfile
		File saveFile = new File("HEAD" + hostAddress + ".html");
		
		//Printwriter to htmlfile
		PrintWriter out = new PrintWriter(saveFile);
		String commandLineString;

		//Print the respons
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			out.println(commandLineString);
			out.flush();
		}

		//Close
		br.close();
		out.close();
		pw.close();
		s.close();
	}
}