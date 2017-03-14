import java.io.*;
import java.net.*;
import java.util.Date;

public class Laurasprutswerkserver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		ServerSocket serverSocket = new ServerSocket(5482);
		while(true)
		{
			Socket clientSocket = serverSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));			
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
			String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today; 
			clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));


			// on ferme les flux.
			System.err.println("Connectie met client gesloten");
			out.close();
			inFromClient.close();
			clientSocket.close();
		}
	}
}

