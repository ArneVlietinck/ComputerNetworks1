import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket(5479);
		while(true){
			Socket clientSocket = serverSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			String clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);

			//clientSentence parsen:
			int index = clientSentence.indexOf(" ");
			String command = clientSentence.substring(0, index);
			System.out.println(command);


			switch(command){
			case "HEAD": HeadServer.head(clientSocket, inFromClient);
			break;

			case "GET": GetServer.get(clientSocket, inFromClient);
			break;

			case "PUT": PutServer.put();
			break;

			case "POST": PostServer.post();
			break;
			}	
			inFromClient.close();
			clientSocket.close();
			//serverSocket.close();
		}
		
	}
}
