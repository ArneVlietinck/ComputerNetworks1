import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LaurasPrutswerk {

	public static void main(String[] args) throws UnknownHostException, IOException {
		//splitsen: http://stackoverflow.com/questions/8694984/remove-part-of-string
		//if argument == GET voer dit uit, splits de link naar host en afterslash zonder de https//:!
		Test test = new Test();
		test.getHTMLcode("http://www.kittens.com");

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
		System.out.println("Domain: "+afterSlash);
		System.out.println("URI: " + hostAddress);
		String sentence = "GET "+afterSlash+ " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
		System.out.println(sentence);
		pw.print(sentence);
		pw.flush();

		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		//printwriter to htmlfile
		PrintWriter out = new PrintWriter(hostAddress+".html");

		//printen respons
		String commandLineString;
		String htmlString = "";
		boolean startHtmlFound = false;
		while((commandLineString = br.readLine()) != null){
			System.out.println(commandLineString);
			if (commandLineString.indexOf("html>") != -1){
				startHtmlFound = true;
			}
			if(startHtmlFound){
				out.println(commandLineString);
				out.flush();
				htmlString += commandLineString;
				//System.out.println(commandLineString);
			}
		}
		//System.out.println(htmlString);
		
		//sluiten na uitschrijven
		br.close();
		out.close();
				
		File siteFile = new File(hostAddress+ ".html");
		saveImage(siteFile,hostAddress);

		

		//later sluiten
		pw.close();
		socket.close();
	}

	private static void saveImage(File fileToParse, String site) throws IOException {
		//Connect to the website and get the html
		Document doc = Jsoup.parse(fileToParse,"UTF-8", site);
		//Get all elements with img tag 
		Elements images = doc.select("img");
		System.out.println(images.size());
		for (Element el : images) {
			//for each element get the src url
			String src = el.attr("src");
			System.out.println("Image Found!");
			System.out.println("src attribute is : "+src);
			if (src.length() != 0) {
				getImages(src);
	        } 	
		}
	}
		private static void getImages(String src) throws IOException{
			//Exctract the name of the image from the src attribute
			int indexname = src.lastIndexOf("/");
			if (indexname == src.length()) {
				src = src.substring(1, indexname);
			}
			indexname = src.lastIndexOf("/");
			String name = src.substring(indexname, src.length());
			System.out.println("name:" + name);
			System.out.println("YES");
			///Vanaf hier verderdoen :) 

		}
	
}