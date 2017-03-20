package Client;
import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
* This class handles a GET request.
* 
* @author Laura Vranken
* @author Arne Vlietinck
*/
public class GetRequest {
	
	/**
	 * Launch the get request. The response received from the HTTP server is displayed on the terminal. 
	 * It is also saved in a HTMLfile locally.
	 * 
	 * @param 	hostAddress
	 * 			The hostAddress of the address to connect with.
	 * @param	afterSlash
	 * 			The address after the slash of the address to connect with.
	 * @param	port
	 * 			The number of the port to connect with. 
	 * @post	The get response of the requested site will be printed in the terminal.
	 * @post	The get response (without header) of the requested site will be saved in a HTMLfile.
	 * 			The conventions for the file are "GET" + hostAddress + ".html".
	 * @throws	UnknownHostException
	 * @throws	IOException
	 */
	public static void get(String hostAddress, String afterSlash, int port) throws UnknownHostException, IOException {

		//verbinding leggen met site.
		Socket s = new Socket(hostAddress, port);
		
		//request
		PrintWriter pw = new PrintWriter(s.getOutputStream());
		String sentence = "GET "+afterSlash+ " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
		pw.print(sentence);
		pw.flush();

		//respons
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

		//Create htmlfile
		File saveFile = new File("GET" + hostAddress + ".html");
		
		//Printwriter to htmlfile
		PrintWriter out = new PrintWriter(saveFile);
		String commandLineString;

		//Print the respons, looking for the html file (without header)
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

		//Close
		br.close();
		out.close();		

		//Save image in file
		searchImage(saveFile,hostAddress);

		//Close
		pw.close();
		s.close();
	}

	/**
	 * The image is searched in the HTMLfile created in the get function.
	 * 
	 * @param 	fileToParse
	 * 			The local file to parse.
	 * @param	hostAddress
	 * 			The hostAddress of the website.
	 * @post	If there is a Image found the method will print "Image Found" and "The src is: " + src.
	 * 			| if (src.length() != 0) {
	 *			| 	System.out.println("Image Found");
	 *			|	System.out.println("The src is: " + src);
     *			| } 
	 * @post	If the length of the image src is not 0 then the image will be get. 
	 * 			| if (src.length() != 0) {
	 *			| 	getImages(src, site);
     *			| } 
	 * @throws	IOException
	 */
	private static void searchImage(File fileToParse, String hostAddress) throws IOException {
		//Parse the file.
		Document doc = Jsoup.parse(fileToParse,"UTF-8", hostAddress);
		
		//Get all elements with img tag 
		Elements images = doc.select("img");
		
		for (Element el : images) {
			//For each element get the src url
			String src = el.attr("src");
			if (src.length() != 0) {
				System.out.println("Image Found");
				System.out.println("The src is: " + src);
				getImages(src, hostAddress);
			} 	
		}
	}
	
	/**
	 * Return the image name with extension.
	 * 
	 * @param 	src
	 * 			The src of the image.
	 * @param	hostAddress
	 * 			The hostAddress of the website.
	 * @post	The name is set to the imageName.
	 * 			| String name = getImageName(src);
	 * 
	 */
	private static void getImages(String src, String hostAddress) throws IOException{

		String name = getImageName(src);
		
		//Get image
		Socket socket = new Socket(hostAddress, 80);
		DataOutputStream bw = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
		String sentence = "GET "+ src + " HTTP/1.1"+"\r\n" + "host: " + hostAddress + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
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
	
	/**
	 * Return the image name with extension.
	 * 
	 * @param 	src
	 * 			The src of the image.
	 * @return	The name of the image without "/", but with the extension.
	 */
	private static String getImageName(String src){
		
		//Exctract the name of the image from the src attribute
		int indexname = src.lastIndexOf("/");
		if (indexname == src.length()) {
			src = src.substring(1, indexname);
		}
		indexname = src.lastIndexOf("/");
		//Name without "/", but with extension. e.g. TestImage.png
		String name = src.substring(indexname+1, src.length());
		return name;
	}
			
}

