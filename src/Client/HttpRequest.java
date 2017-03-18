package Client;
import java.io.*;
import java.net.*;


public class HttpRequest {

	public static void main(String[] args) throws UnknownHostException, IOException, IllegalArgumentException {
		String httpCommand;
		String uri;
		int port;

		if(args.length>3||args.length<2){
			throw new IllegalArgumentException("Illegal number of arguments. Please give the HTTPCommand, URI and (optional) Port");
		}else if(args.length==2){
			httpCommand = args[0];
			uri = args[1];
			port = 80;
		}
		httpCommand = args[0];
		uri = args[1];
		port = Integer.parseInt(args[2]);

		//string splitsen
		String[] address = splitsUri(uri);

		switch(httpCommand){
		case "HEAD": HeadRequest.head(address, port);
		break;

		case "GET": GetRequest.get(address, port);
		break;

		case "PUT": PutRequest.put();
		break;

		case "POST": PostRequest.post();
		break;
		
		//TODO: check how to throw illegalArgument.
		default: throw new IllegalArgumentException("Illegal HTTPCommand");
		}
	}

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


}
