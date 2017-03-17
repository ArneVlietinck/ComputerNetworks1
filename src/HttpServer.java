import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

	public static void main(String[] args) throws IOException {
		MultiThreadedServer server = new MultiThreadedServer(7789);
		new Thread(server).start();

		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Stopping Server");
		//		server.stop();
	}
}

class MultiThreadedServer implements Runnable{

	private int port;
	private ServerSocket serverSocket = null;
	private Thread runningThread = null;

	public MultiThreadedServer(int port){
		this.port = port;
	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		this.openServerSocket();

		while(true){
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

			new Thread(new WorkerRunnable(
					clientSocket, "Multithreaded Server")).start();
			System.out.println("geen idee");
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port", e);
		}
	}
}


class WorkerRunnable implements Runnable{

	private Socket clientSocket = null;

	public WorkerRunnable(Socket clientSocket, String serverText){
		this.clientSocket = clientSocket;
	}

	@Override
	public void run(){
		BufferedReader inFromClient = null;
		String clientSentence = "";
		try {
			inFromClient = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);

			//clientSentence parsen:
			int index = clientSentence.indexOf(" ");
			String command = clientSentence.substring(0, index);
			System.out.println(command);

			//path parsen
			String path;
			int endindex = clientSentence.indexOf(" ", index+1);
			path = clientSentence.substring(index+2,endindex);


			switch(command){
			case "HEAD": HeadServer.head(clientSocket, inFromClient, path);
				break;

			case "GET": GetServer.get(clientSocket, inFromClient, path);
				break;

			case "PUT": PutServer.put(inFromClient, path);
				break;

			case "POST": PostServer.post(inFromClient, path);
				break;
			}
			
		}catch (IOException e) {
			e.printStackTrace();
		}

		//inFromClient.close();
		//clientSocket.close();
		//serverSocket.close();
	}
}

