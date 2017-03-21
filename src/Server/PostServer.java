package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * This server class responds to the post request of the client. It listens to the given text and saves it in the specified file.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 *
 */
public class PostServer {
	/**
	 * This function listens to the given text and saves it in the specified file.
	 * 
	 * @param inFromClient
	 * 			The buffered inputstream which the server receives from the client.
	 * @param path
	 * 			The path of the file which has to be retrieved.
	 * @throws IOException
	 */
	public static void post(BufferedReader inFromClient, String path, String http) throws IOException{

		//if path not specified, return index page.
		if (path.isEmpty()){
			path = "post.txt";
		}

		//info client uitlezen
		String s;
		PrintWriter out = new PrintWriter(path);

		while ((s = inFromClient.readLine()) != null) {
			System.out.println(s);
			out.println(s);
			out.flush();
			if (s.isEmpty()) {
				break;
			}
		}

		//close connection when HTTP/1.0 is used.
		if (http == "1.0"){
			out.close();
		}
	}
}
