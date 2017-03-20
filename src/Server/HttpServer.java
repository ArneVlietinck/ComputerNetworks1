package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * This class starts a multithreaded server.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 */
public class HttpServer {

	/**
	 * The main function starts the multithreaded server.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		MultiThreadedServer server = new MultiThreadedServer(7788);
		new Thread(server).start();

		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//System.out.println("Stopping Server");
		//		server.stop();
	}
}

/**
 * Create a server socket and make connection with the client socket.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 *
 */
class MultiThreadedServer implements Runnable{

	private int port;
	private ServerSocket serverSocket = null;
	private Thread runningThread = null;
	private String statusCode = "";

	/**
	 * Save the port number.
	 * @param port
	 * 		|| the port on which the server works.
	 */
	public MultiThreadedServer(int port){
		this.port = port;
	}

	/**
	 * Synchronise the running thread.
	 * Create the server socket and make connection with a client socket.
	 */
	@Override
	public void run(){
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		this.openServerSocket();

		while(true){
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				statusCode = "500 Server Error";
				e.printStackTrace();
			}

			new Thread(new WorkerRunnable(
					clientSocket)).start();
		}
	}

	/**
	 * Make a new server socket on a given port number.
	 */
	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			statusCode = "500 Server Error";
			e.printStackTrace();
		}
	}
}

/**
 * Execute the clients request by retrieving it and redirecting to the right class: GET, HEAD, PUT, POST.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 *
 */
class WorkerRunnable implements Runnable{

	private Socket clientSocket = null;

	/**
	 * Save the client socket.
	 * @param clientSocket
	 * 		|| socket to connect with client.
	 */
	public WorkerRunnable(Socket clientSocket){
		this.clientSocket = clientSocket;
	}

	/**
	 * Read the input request from the client, parse it into the HTTP command and path.
	 * The HTTP command will be worked out in one of the specified classes.
	 */
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

