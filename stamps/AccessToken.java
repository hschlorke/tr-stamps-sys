package com.tirerack.stamps;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;


/**********************************************************************************************
 * Class to generate authorization tokens from the refresh token.
 * The refresh token is stored in a physical file in case we need to generate a new one.
 * If a new refresh token needs generated, use getAuthGrantType in GenerateAuthUrl
 * to grab the needed URL. There is also a document in project folder G869 detailing
 * how to generate a new refresh token.
 *********************************************************************************************/
public class AccessToken {
	
	private static final String GRANT_TYPE = "refresh_token";
	private static final String TEST_URL = "https://signin.testing.stampsendicia.com";
    private static final String ACCESS_TOKEN_PATH = "/oauth/token";
    private static final String CLIENT_ID = "QWhRJFGeS2xKIf5lToM40YND96vC6ofj"; 
	private static final String CLIENT_SECRET = "KtXYU7ojiqIk14DsOYZtZH10NszyZsFIrknuQKgFIYSqDIgkr03-YAMFlrYb4AwR";


    public static String getAccessToken(String accessURL, String grantType, String clientID, String clientSecret, String StmpsRefresh){
		String accessToken = "";
		try{
			URL url = new URL(accessURL);
			//System.out.println(url);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String data = "{"
					+ "\"grant_type\":\""+grantType+"\","
					+ "\"client_id\":\""+clientID+"\","
					+ "\"client_secret\":\""+clientSecret+"\","
					+ "\"refresh_token\":\""+StmpsRefresh+"\""
					+"}";

			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			try {
				byte[] input = data.getBytes("utf-8");
				os.write(input, 0, input.length);
			} finally {
				os.flush();
				os.close();
			}
	
			int responseCode = conn.getResponseCode();
		
			if (responseCode != StampsUtil.OKReply) {
				
				return("No token.");	
			}
	
			JSONObject responseJSON = new JSONObject(StampsUtil.getResponse(conn));
			accessToken = responseJSON.getString("access_token"); 
			
		} catch (Exception e){
			accessToken = e.toString().trim();
			
		}
		
		return accessToken;

	}
}
	

