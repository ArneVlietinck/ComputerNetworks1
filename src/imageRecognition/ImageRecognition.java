package imageRecognition;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 * This class handles image recognition.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 */
public class ImageRecognition {

	/**
	 * Search images in a HTMLfile.
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
	public static void searchImage(File fileToParse, String hostAddress) throws IOException {
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
	 * @post	The image is saved in a local file.
	 * 			The conventions for the file are "Image_" + name.
	 * @throws	IOException
	 * @source	http://stackoverflow.com/questions/33576510/how-to-download-an-image-with-a-java-socket-http-1-1-request
	 */
	public static void getImages(String src, String hostAddress) throws IOException{

		String name = src;
		String imageName = getImageName(src);

		//Get image
		Socket socket = new Socket(hostAddress, 80);
		DataOutputStream bw = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
		String sentence = "GET "+ src + " HTTP/1.1" + "\r\n" + "host: " + hostAddress + "\r\n" + "Connection: close" + "\r\n" + "\r\n";
		System.out.println(sentence);
		bw.writeBytes(sentence);
		bw.flush();

		//Create image file
		
		//Gives the relative path
		Path currentRelativePath = Paths.get("");
		String currentDirectory = currentRelativePath.toAbsolutePath().toString();
		//System.out.println("Current relative path is: " + currentDirectory);
		
		//Make directories for imageFile
		String directoryName = name.replace(imageName, "");
		File directories = new File(currentDirectory+directoryName);
		directories.mkdirs();
		
		//Make imageFile in the created directories
		File saveImage = new File(currentDirectory+name);
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
	 * @return	The name of the image wit "/" and with the extension.
	 */
	public static String getImageName(String src){

		//Exctract the name of the image from the src attribute
		int indexname = src.lastIndexOf("/");
		if (indexname == src.length()) {
			src = src.substring(1, indexname);
		}
		indexname = src.lastIndexOf("/");
		//Name with "/", but with extension. e.g. /TestImage.png
		String name = src.substring(indexname, src.length());
		return name;
	}

	public static void searchImageServer(File fileToParse, Socket socket) throws IOException {
		//Parse the file.
		Document doc =Jsoup.parse(fileToParse, "UTF-8");

		//Get all elements with img tag 
		Elements images = doc.select("img");

		for (Element el : images) {
			//For each element get the src url
			String src = el.attr("src");
			if (src.length() != 0) {
				System.out.println("Image Found");
				System.out.println("The src is: " + src);
				sendImage(src, socket);
			} 	
		}
	}

	public static void sendImage(String src, Socket socket) throws IOException{
		
		//Gives the relative path
		Path currentRelativePath = Paths.get("");
		String currentDirectory = currentRelativePath.toAbsolutePath().toString();
		
		//ophalen
		String name = src;
		String imageName = getImageName(src);
		String fullpath = currentDirectory+name;
		File readImage = new File(fullpath);
		InputStream imageInput = new FileInputStream(readImage);
		//doorsturen
		DataOutputStream toSocket =new DataOutputStream(socket.getOutputStream());

		byte[] buffer = new byte[2048];
		int bytesRead;
		while((bytesRead = imageInput.read(buffer))!= -1){
			toSocket.write(buffer,0,bytesRead);
			toSocket.flush();
		}
		imageInput.close();
		toSocket.close();
	}
}