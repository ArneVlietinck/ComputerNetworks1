import java.io.*;
import java.net.*;


public class HttpRequest {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
		String httpCommand = "GET";
		
		switch(httpCommand){
			case "HEAD": HeadRequest.head();
					break;
			case "GET": 
				GetRequest.get("stackoverflow.com", 80)	;
					break;
			case "PUT": PutRequest.put();
					break;
			case "POST": PostRequest.post();
					break;
		}
		
		
	}
}
