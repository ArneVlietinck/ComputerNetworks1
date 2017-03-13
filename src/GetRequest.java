import java.io.*;
import java.net.*;

	public static void get(String address, int port) throws UnknownHostException, IOException {

		//if argument == GET voer dit uit, splits de link naar host en afterslash zonder de https//:!
		String newAddress = "";
		String Hostaddress = "www.example.com";
		String AfterSlash = "";

		//verbinding leggen met site.
		Socket s = new Socket(InetAddress.getByName(Hostaddress), port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		pw.println("GET /"+AfterSlash+" HTTP/1.1");
		pw.println("Host: "+Hostaddress);
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

