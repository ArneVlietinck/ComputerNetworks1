import java.io.*;
import java.net.*;
import java.util.Date;

public class Laurasprutswerkserver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ServerSocket serverSocket = new ServerSocket(5479);
		while(true)
		{
			Socket clientSocket = serverSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			String clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);


			String s;
			while ((s = inFromClient.readLine()) != null) {
				System.out.println(s);
				if (s.isEmpty()) {
					break;
				}
			}
			Date today = new Date(); 
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
			File file = new File("lol.txt");
			BufferedReader htmlFile = new BufferedReader(new FileReader(file));
			
			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type: text/html");
			out.println("Content-Length: "+file.length());
			out.println("Date: "+today);
			out.println("");
//			out.println("<title>LOL</title>");
//			out.println("<p> Hello world </p>");
			out.flush();		
			
			String t;
			while ((t = htmlFile.readLine()) != null) {
				out.println(t);
				out.flush();
			}
			htmlFile.close();
			
			// on ferme les flux.
			System.err.println("Connectie met client gesloten");
			out.close();
			inFromClient.close();
			clientSocket.close();
			//serverSocket.close();
		}
	}
}


