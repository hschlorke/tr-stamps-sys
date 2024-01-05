package com.tirerack.stamps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class StampsUtil {

	public static final String empty = " ";
	//200 is a general success response
	public static final Integer OKReply = 200;
	//201 is a successful label creation response
	public static final Integer OKReply1 = 201;
	
	
	public static String getResponse(HttpsURLConnection connection) throws IOException
	{	
		String reply = empty;

		BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine = "";
		StringBuffer response = new StringBuffer();
		while((inputLine = input.readLine()) != null){
			response.append(inputLine);
		}
		input.close();
		
		reply = response.toString();

		return reply;
	}
	
	public static String getError(HttpsURLConnection connection) throws IOException
	{	
		String reply = empty;

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		String inputLine = "";
		StringBuffer response = new StringBuffer();
		while((inputLine = br.readLine()) != null){
			response.append(inputLine);
		}
		br.close();
		
		reply = response.toString();

		return reply;
    }
	
	// Need to send a new uuid with each label request
	static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// System.out.println(uuid);
		return uuid;
	}
}
