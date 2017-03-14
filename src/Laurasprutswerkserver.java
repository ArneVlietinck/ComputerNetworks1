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
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));			String clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			//			String capsSentence = clientSentence.toUpperCase() + '\n';
			//			System.out.println("capsSe" + capsSentence);
			//			outToClient.writeBytes(capsSentence);

			String s;
			while ((s = inFromClient.readLine()) != null) {
				System.out.println(s);
				if (s.isEmpty()) {
					break;
				}
			}

			Date today = new Date();
			out.write("HTTP/1.0 200 OK\r\n");
			out.write("Date: Fri, 31 Dec 1999 23:59:59 GMT\r\n");
			out.write("Server: Apache/0.8.4\r\n");
			out.write("Content-Type: text/html\r\n");
			out.write("Content-Length: 59\r\n");
			out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
			out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
			out.write("\r\n");
			out.write("<TITLE>Exemple</TITLE>");
			out.write("<P>Ceci est une page d'exemple.</P>");

			// on ferme les flux.
			System.err.println("Connexion avec le client terminée");
			out.close();
			inFromClient.close();
			clientSocket.close();
		}
	}
}

