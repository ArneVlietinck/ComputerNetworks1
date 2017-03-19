package Client;
import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class GetRequest {
	public static void get(String[] address, int port) throws UnknownHostException, IOException {

		String hostAddress = address[0];
		String afterSlash = address[1];

		//verbinding leggen met site.
		Socket s = new Socket(hostAddress, port);
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		//System.out.println("Domain: "+afterSlash);
		//System.out.println("URI: " + hostAddress);
		String sentence = "GET "+afterSlash+ " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
		//System.out.println(sentence);
		pw.print(sentence);
		pw.flush();

		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));


		//Aanmaak html file
		File saveFile = new File("GET" + hostAddress + ".html");
		//printwriter to htmlfile
		PrintWriter out = new PrintWriter(saveFile);

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
			}
		}

		//sluiten na uitschrijven
		br.close();
		out.close();		

		//Save image in file
		saveImage(saveFile,hostAddress);

		//later sluiten
		pw.close();
		s.close();
	}

	private static void saveImage(File fileToParse, String site) throws IOException {
		//Parse the file.
		Document doc = Jsoup.parse(fileToParse,"UTF-8", site);
		//Get all elements with img tag 
		Elements images = doc.select("img");
		//System.out.println(images.size());
		for (Element el : images) {
			//for each element get the src url
			String src = el.attr("src");
			System.out.println("Image Found");
			System.out.println("The src is: "+src);
			if (src.length() != 0) {
				getImages(src, site);
			} 	
		}
	}

	private static void getImages(String src, String site) throws IOException{
		//Exctract the name of the image from the src attribute
		int indexname = src.lastIndexOf("/");
		if (indexname == src.length()) {
			src = src.substring(1, indexname);
		}
		indexname = src.lastIndexOf("/");
		String name = src.substring(indexname+1, src.length());
		//Name is alles na de "/" wat dus ook de extensie inhoudt. 
		//System.out.println("name:" + name);

		//Image opslaan
		Socket socket = new Socket(site, 80);
		DataOutputStream bw = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
		String sentence = "GET "+ src + " HTTP/1.1"+"\r\n" + "host: " + site + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
		System.out.println(sentence);
		bw.writeBytes(sentence);
		bw.flush();

		//Aanmaak image file
		File saveImage = new File("Image_" + name);
		//printwriter to image file
		OutputStream imageOutput = new FileOutputStream(saveImage);


		// Initialize the stream.
		final InputStream inputStream = socket.getInputStream();

		// Header end flag.
		boolean headerEnded = false;

		byte[] bytes = new byte[2048];
		int length;
		while ((length = inputStream.read(bytes)) != -1) {
			// If the end of the header had already been reached, write the bytes to the file as normal.
			if (headerEnded)
				imageOutput.write(bytes, 0, length);

			// This locates the end of the header by comparing the current byte as well as the next 3 bytes
			// with the HTTP header end "\r\n\r\n" (which in integer representation would be 13 10 13 10).
			// If the end of the header is reached, the flag is set to true and the remaining data in the
			// currently buffered byte array is written into the file.
			else {
				for (int i = 0; i < 2045; i++) {
					if (bytes[i] == 13 && bytes[i + 1] == 10 && bytes[i + 2] == 13 && bytes[i + 3] == 10) {
						headerEnded = true;
						imageOutput.write(bytes, i+4, 2048-i-4);
						break;
					}
				}
			}
		}
		inputStream.close();
		imageOutput.close();
		socket.close();
	}
}

