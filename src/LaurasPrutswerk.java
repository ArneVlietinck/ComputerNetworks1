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
		test.getHTMLcode("http://www.google.com");
		
	}
}

class Test{
	public void getHTMLcode(String address) throws UnknownHostException, IOException{
		//string splitsen
		String newAddress = address;
		newAddress = newAddress.replace("http://","");
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
		Socket socket = new Socket(hostAddress, port);
		//request
		PrintWriter pw = new PrintWriter(socket.getOutputStream());
		pw.println("GET "+afterSlash+" HTTP/1.1");
		pw.println("Host: "+hostAddress);
		pw.println("");
		pw.flush();
		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//printwriter to textfile
		PrintWriter out = new PrintWriter("lol.txt");
		String t;
		String lol = "";
		//printen respons
		while(!(t = br.readLine()).isEmpty()){
			System.out.println(t);
		}
		System.out.println("");
		while(br.ready()){
			System.out.println(br.readLine());
			out.println(t);
			out.flush();
		}

		
		br.close();
		out.close();
		
//		if(newAddress != ""){
//			this.getHTMLcode(newAddress);
//		}
		//sluiten na uitschrijven
		
		
		pw.close();
		
		//images uithalen?


		//later sluiten
		
		socket.close();
	}

}
