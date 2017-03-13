import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LaurasPrutswerk {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//splitsen: http://stackoverflow.com/questions/8694984/remove-part-of-string
		//if argument == GET voer dit uit, splits de link naar host en afterslash zonder de https//:!
		Test test = new Test();
		test.getHTMLcode("https://www.example.com");
		
	}
}

class Test{
	public void getHTMLcode(String address) throws UnknownHostException, IOException{
		//string splitsen
		String newAddress = address;
		newAddress = newAddress.replace("https://","");
		int startIndex = newAddress.indexOf("/");
		String afterSlash;
		if(startIndex == -1){
			afterSlash = "/";
		}else{
			afterSlash = newAddress.substring(startIndex, newAddress.length());
		}
		String hostAddress = newAddress.replace(afterSlash, "");
		

		//verbinding leggen met site.
		int port = 80;
		Socket s = new Socket(InetAddress.getByName(hostAddress), port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET "+afterSlash+" HTTP/1.1");
		pw.println("Host: "+hostAddress);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		//printwriter to textfile
		PrintWriter out = new PrintWriter("test.html");
		String t;
		//printen respons
		while((t = br.readLine()) != null){
			System.out.println(t);
			if (t.contains("Location:")){
				newAddress = t.replace("Location: ","");
				System.out.println(newAddress);
			}
			out.println(t);
			out.println("");
			out.flush();
		}
		//sluiten na uitschrijven
		br.close();
		out.close();

		//images uithalen?


		//later sluiten
		pw.close();
		s.close();
	}

}
