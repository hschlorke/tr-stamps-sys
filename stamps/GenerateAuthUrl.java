package com.tirerack.stamps;

public class GenerateAuthUrl {
	private static final String testUrl = "https://signin.testing.stampsendicia.com";
	private static final String testAuthorizationURL = testUrl + "/authorize";  
	private static final String clientID = "QWhRJFGeS2xKIf5lToM40YND96vC6ofj"; //identifies the integrated app connecting to SERA
	public static String clientSecret = "KtXYU7ojiqIk14DsOYZtZH10NszyZsFIrknuQKgFIYSqDIgkr03-YAMFlrYb4AwR";      //used to securely validate the Client ID
	private static final String redirectURI = "https://www.tirerack.com/"; //used to generate authorization code, must match redirect URI used in /authorize call

	//production endpoints
	//public static String url = "https://signin.stampsendicia.com"
	//public static String authorizationURL = url + "/authorize";
	
	String authorizationRedirect = getAuthGrantType();
	
	
	/* Running this will return the URL needed to get the auth code.
	 * Paste this URL into a browser and then grab the text after code=.
	 * This is how the refresh token is generated.  */
	public static String getAuthGrantType(){
	    return testAuthorizationURL + "?client_id=" + GenerateAuthUrl.clientID + "&response_type=code&redirect_uri=" + GenerateAuthUrl.redirectURI + "&scope=offline_access";
	}
}
