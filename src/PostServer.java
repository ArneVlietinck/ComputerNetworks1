import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PostServer {
	public static void post(BufferedReader inFromClient, String path) throws IOException{

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
		out.close();
	}
}
