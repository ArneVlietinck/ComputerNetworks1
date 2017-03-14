import java.io.*;
import java.net.*;
import java.util.Date;

public class Laurasprutswerkserver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ServerSocket serverSocket = new ServerSocket(5474);
		while(true)
		{
			Socket clientSocket = serverSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			String clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);


			String s;
			while ((s = inFromClient.readLine()) != null) {
				//System.out.println(s);
				if (s.isEmpty()) {
					break;
				}
			}

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
			
			
			TestServer TestServer = new TestServer();
			
			Date today = new Date(); 	
			out.println("Date: "+today);
			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type: text/html");
			//contentLength headers
			out.println("\r\n");
			//		    out.println("<title>LOL</title>");
			//		    out.println("<p> Hello world </p>");
			out.flush();		


			// on ferme les flux.
			System.err.println("Connectie met client gesloten");
			out.close();
			inFromClient.close();
			clientSocket.close();
			serverSocket.close();
		}
	}
}

class TestServer{

	
}

