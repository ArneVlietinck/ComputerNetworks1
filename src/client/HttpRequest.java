package client;
import java.io.*;
import java.net.*;

/**
 * This class starts a HTTP client. 
 * It supports the basic HEAD, GET, PUT and POST commands.
 * 
 * @author Laura Vranken
 * @author Arne Vlietinck
 */
public class HttpRequest {

	/**
	 * The main function starts the HTTPclient.
	 * 
	 * @param	args
	 * 			The arguments given in the terminal.
	 * @post	httpCommand is set to the first argument.
	 * 			| httpCommand = args[0]
	 * @post	uri is set to the second argument.
	 * 			| uri = args[1]
	 * @post	port is set to the third argument if presented otherwise it is set to the default portnumber (80).
	 * 			| if(args.length==2){
	 * 			| port =80
	 * 			| }else{
	 * 			| port = 80;
	 * 			| }
	 * @throws 	IllegalArgumentException
	 * 			The given number of arguments is not valid.
	 * 			| if(!isValidNumberOfArguments(args))
	 * @throws 	IOException 
	 * @throws 	UnknownHostException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, UnknownHostException, IOException {
		String httpCommand;
		String uri;
		int port;

		if(!isValidNumberOfArguments(args)){
			throw new IllegalArgumentException("Illegal number of arguments. Please give the HTTPCommand, URI and (optional) Port");
		}else if(args.length==2){
			httpCommand = args[0];
			uri = args[1];
			port = 80;
		}else{
			httpCommand = args[0];
			uri = args[1];
			port = Integer.parseInt(args[2]);
		}

		//string splitsen
		String[] address = splitsUri(uri);
		
		//check HttpCommand
		checkHTTPCommand(httpCommand, address, port);
	}

	/**
	 * Return an array with the hostAddress and afterSlash.
	 * 	e.g. http://www.google.com/example then the hostAddress is "www.google.com" 
	 * 	and the afterSlash "/example".
	 * 
	 * @param 	uri
	 * 			The full uri of the internetpage. 
	 * @return	A new array of strings containing hostAddress and afterSlash.
	 * 			If there is no afterSlash, it returns / as afterSlash.
	 */
	public static String[] splitsUri(String uri){

		String newAddress = uri;
		if(newAddress.contains("https://")){
			newAddress = newAddress.replace("https://","");
		}
		newAddress = newAddress.replace("http://","");
		int startIndex = newAddress.indexOf("/");
		String afterSlash;
		if(startIndex == -1){
			afterSlash = "/";
		}else{
			afterSlash = newAddress.substring(startIndex, newAddress.length());
		}
		String hostAddress = newAddress.replace(afterSlash, "");
		return new String[]{hostAddress, afterSlash};	
	}

	/**
	 * Check and launch the corresponding HTTPCommand.  
	 * 
	 * @param 	httpCommand
	 * 			The httpCommand to launch.
	 * @param	address
	 * 			The address array contains the hostAddress and afterSlash.
	 * @param	port
	 * 			The number of the port to connect with. 
	 * @post	If the httpCommand is "HEAD",
	 * 			the headRequest is launched.
	 * 			| case "HEAD": HeadRequest.head(address[0], address[1], port);
	 * @post	If the httpCommand is "GET",
	 * 			the getRequest is launched.
	 * 			| case "GET": GetRequest.get(address[0], address[1], port);
	 * @post	If the httpCommand is "PUT",
	 * 			the putRequest is launched.
	 * 			| case "PUT": PutRequest.put(address[0], address[1], port);
	 * @post	If the httpCommand is "POST",
	 * 			the postRequest is launched.
	 * 			| case "POST": PostRequest.post(address[0], address[1], port);
	 * @post	If the httpCommand is not "HEAD", "GET", "PUT" or "POST",
	 * 			the system prints "This HTTPCommand is not supported".
	 * 			| default: System.out.println("This HTTPCommand is not supported.");
	 */
	public static void checkHTTPCommand(String httpCommand, String[] address, int port) throws UnknownHostException, IOException{
		switch(httpCommand){
		case "HEAD": HeadRequest.head(address[0], address[1], port);
		break;

		case "GET": GetRequest.get(address[0], address[1], port);
		break;

		case "PUT": PutRequest.put(address[0], address[1], port);
		break;

		case "POST": PostRequest.post(address[0], address[1], port);
		break;

		default: System.out.println("This HTTPCommand is not supported.");
		}
	}
	
	/**
	 * Return whether the given number of arguments is a valid number for the HTTPClient.
	 * 
	 * @param  args
	 *         The number of arguments to check.
	 * @return True if the number of arguments are 2 of 3.
	 *         | result == (args.length == 2 || args.length == 3)
	 */
	public static boolean isValidNumberOfArguments(String[] args){
		if(args.length>3||args.length<2){
			return false;
		}
		return true;
	}
}
