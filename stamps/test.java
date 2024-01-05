package com.tirerack.stamps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

/*
 * Testing class to see if information will come back from stamps. 
 */

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import com.ibm.as400.access.IFSFileOutputStream;
import com.tirerack.stamps.StampsUtil;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;

import spoolWriter.SpoolWriter;
import spoolWriter.SpoolWriterBackupp;
import util.SpoolWriterUtil;

//import spoolWriter.SpoolWriter;
//import util.SpoolWriterUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

public class test {
	private static final String TEST_URL = "https://api.testing.stampsendicia.com";
	private static final String LABEL_PATH = "/sera/v1/labels";
	public static String fullJSONPayload = "";
	private static Object captureJSON;
	public static String WhCompanyName = "Test Tire Rack";
	public static String WhName = "Test Hannah";
	public static String WhAddress1 = "7101 Vorden Parkway";
	public static String WhAddress2 = "Order Parcel";
	public static String WhCity = "South Bend";
	public static String WhState = "IN";
	public static String WhPostalCode = "46628";
	public static String WhPhone = "5551234567";
	public static String companyName = "Test";
	public static String name = "Testing Test";
	public static String address1 = "CMR 400 BOX 248";
	public static String address2 = "";
	public static String address3 = "";
	public static String city = "APO";
	public static String state = "AE";
	public static String postalCode = "09049";
	public static String phone = "5557894561";
	public static String serviceType = "usps_parcel_select";
	public static double weight = 36.1;
	public static String weightUnit = "pound";
	public static double length = 15.49;
	public static double width = 28.512345;
	public static double height = 18.99;
	public static String contentsType = "merchandise";
	public static String nonDeliveryOpt = "return_to_sender";
	public static String itemDesc = "Tire";
	public static int quantity = 1;
	public static double amount = 80.00;
	public static String currency = "usd";
	public static int itemWeight = 36;
	public static String countryOfOrigin = "IT";
	public static String shipDate = "11/28/2023";
	public static boolean isReturnLabel = false;
	public static String labelSize = "4X6";
	public static String labelFormat = "pdf";
	public static String labelOutputType = "url";
	public static boolean isTestLabel = false;
	public static String packType = "package";
	public static String contentsDesc = "string";
	public static String StmpsUser = "";
	public static String StmpsPassword = "";
	public static String trackingNo = "";
	public static double freight;
	public static String freightCharge ;
	public static String filename = "TESTHS9";
	public static String status = "";
	public static int responseCode;
	public static String orderNumber = "Test1234";
	public static String responseCodeString = "";
	public static int weightInt;
	public static int lengthInt;
	public static int widthInt;
	public static int heightInt;
	

	public static void main(String[] args) throws IOException {

		// returns URL needed to generate auth code
		// System.out.println(GenerateAuthUrl.getAuthGrantType());
		// System.out.println(AccessToken.getAccessToken());
		System.out.println(testLabel());
		//System.out.println(returnTrackingNo());
		//System.out.println(returnFreightCharge());
		//System.out.println(base64test());
		System.out.println(returnPayload());
		

	}

	public static String testLabel() {
		JSONObject labelData;
		//String errorMessage = "";
		String labelDataString = "";
		String decodedLabel = "";
		weightInt = (int)Math.ceil(weight);
		lengthInt = (int)Math.ceil(length);
		widthInt = (int)Math.ceil(width);
		heightInt = (int)Math.ceil(height);
		System.out.println(weightInt + " " + lengthInt + " " + widthInt + " " + heightInt);
		
	
		try {
			String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlA3Z3pLRGdfRENlVzcyeXZ3cnpQcCJ9.eyJodHRwczovL3N0YW1wc2VuZGljaWEuY29tL2ludGVncmF0aW9uX2lkIjoiYzAxMjg5YTItODM4ZS00YWY2LWJjY2MtM2E4YzFjMDdjYTk4IiwiaHR0cHM6Ly9zdGFtcHNlbmRpY2lhLmNvbS91c2VyX2lkIjozNzIwMzE0LCJpc3MiOiJodHRwczovL3NpZ25pbi50ZXN0aW5nLnN0YW1wc2VuZGljaWEuY29tLyIsInN1YiI6ImF1dGgwfDM3MjAzMTQiLCJhdWQiOiJodHRwczovL2FwaS5zdGFtcHNlbmRpY2lhLmNvbSIsImlhdCI6MTY5NjYxODA1MywiZXhwIjoxNjk2NjE4OTUzLCJhenAiOiJRV2hSSkZHZVMyeEtJZjVsVG9NNDBZTkQ5NnZDNm9maiIsInNjb3BlIjoib2ZmbGluZV9hY2Nlc3MifQ.wfjheY_OhYQwMgq9rgROp8LHB8Uy5XUa6DaHYpbqZh_22rtON7yI3vOShYk1Krxdo4H_KR_VVkMyZgCMh3jHBJB5_cfHvlSFt0ao7wmYQL8roExchB2gRs3BKcUKtqjEsWqp-Cz3Q7vFFWpSao1d8y4K8n4rppugrm9_2dyLwD9KSNFgE2rA_a3dawJKootqGqgHRG9zeQHd1qQdf7-yb4V-bJvEzA5jxaTYkFWQxHp97_ypMK06tUQaS1svSHuKx1SmQHdSyGOMF6-w2QS4T6McrZ-D2XE3C8hfl7eWBRt55bxqkZVdn2EMVTJ6M9kMH_6Yqvyj_TuWGhVRkt1tug";
			
			String idempotencyKey = StampsUtil.getUUID();
			// System.out.println(idempotencyKey);
			URL url = new URL(TEST_URL + LABEL_PATH);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setRequestProperty("Idempotency-key", idempotencyKey);
			
			

			String payload = "{" + "\"from_address\":{" + "\"company_name\":\"" + WhCompanyName + "\"," + "\"name\":\""
					+ WhName + "\"," + "\"address_line1\":\"" + WhAddress1 + "\"," + "\"address_line2\":\"" + WhAddress2 + "\"," + "\"city\":\"" + WhCity + "\","
					+ "\"state_province\":\"" + WhState + "\"," + "\"postal_code\":\"" + WhPostalCode + "\"," + "\"phone\":\"" + WhPhone + "\""
					+ "}," + "\"to_address\":{"  + "\"name\":\"" + name
					+ "\"," + "\"address_line1\":\"" + address1 + "\"," + "\"address_line2\":\"" + address2 + "\","
					+ "\"address_line3\":\"" + address3 + "\"," + "\"city\":\"" + city + "\","
					+ "\"state_province\":\"" + state + "\"," + "\"postal_code\":\"" + postalCode + "\"," + "\"phone\":\"" + phone + "\""+ "},"
					+ "\"service_type\":\"" + serviceType + "\"," + "\"package\":{" + "\"packing_type\":\""
					+ packType + "\"," + "\"weight\":" + weight + "," + "\"weight_unit\":\"" + weightUnit + "\"," + "\"length\":" + length + ","
					+ "\"width\":" + width + "," + "\"height\":" + height + ""
					+ "}," + "\"customs\":{" + "\"contents_type\":\"" + contentsType + "\","
					+ "\"contents_description\":\"" + contentsDesc + "\"," + "\"non_delivery_option\":\""
					+ nonDeliveryOpt + "\"," + "\"customs_items\":[{" + "\"item_description\":\"" + itemDesc
					+ "\"," + "\"quantity\":" + quantity + "," + "\"unit_value\":{" + "\"amount\":" + amount + ","
					+ "\"currency\":\"" + currency + "\"" + "}," + "\"item_weight\":" + itemWeight + ","
					+ "\"weight_unit\":\"" + weightUnit + "\"," + "\"country_of_origin\":\"" + countryOfOrigin + "\""
					+ "}]}," + "\"ship_date\":\"" + shipDate + "\","
					+ "\"label_options\":{" + "\"label_size\":\"" + labelSize + "\"," + "\"label_format\":\""
					+ labelFormat + "\"," + "\"label_output_type\":\"" + labelOutputType + "\"" + "}"
					+ "}";
				
			conn.setDoOutput(true);
			OutputStream os = conn.getOutputStream();
			System.out.println(payload);
			try {
				byte[] input = payload.getBytes();
				os.write(input, 0, input.length);
			} finally {
				os.flush();
				os.close();
			}
			
			responseCode = conn.getResponseCode();
			//System.out.println(responseCode);
			
			
			if (responseCode != StampsUtil.OKReply1) {
				JSONObject responseJSON = new JSONObject(StampsUtil.getError(conn));
				//System.out.println(responseJSON.toString());
				String responseString = responseJSON.toString();
				String filePath1 = "/STAMPS/LOGS/TEST/" + filename + ".txt"; //test
				//String filePath1 = "/STAMPS/LOGS/" + filename + ".txt"; //live
				String logString =  "Order number: " + orderNumber + "\n"
						           + "Parcel number: " + filename + "\n"
						           + "Error: " + responseString ;
						           
			    byte[] logByte = logString.getBytes();
				String ibmI = "TRPROD";
				AS400 machine = new AS400(ibmI);
				IFSFileOutputStream file = new IFSFileOutputStream(machine , filePath1);
				file.write(logByte);
				file.close();
				return (responseCodeString);	
			}

			JSONObject responseJSON = new JSONObject(StampsUtil.getResponse(conn));
			fullJSONPayload = responseJSON.toString();
			System.out.println(fullJSONPayload);
			JSONArray labels = new JSONArray(responseJSON.getJSONArray("labels"));
			labelData = labels.getJSONObject(0);
			labelDataString = labelData.getString("label_data");
			
			byte[] decodedBytes = Base64.getDecoder().decode(labelDataString);
			decodedLabel = new String(decodedBytes);
		    	//String status = SpoolWriterBackupp.writeSpool(decodedLabel.getBytes(SpoolWriterUtil.EBCIDC), "TRPROD","QGPL","ZEBTEST","*OUTQ","*SCS", "TEST1", "STAMPLBL", "*YES", StmpsUser, StmpsPassword, "*NO");
		    	//System.out.println("Spool Status: " + status);
			//String IFSPath = "/STAMPS/TEST/" + filename + ".pdf";
			//status = SpoolWriter.writeSpool(decodedBytes, "TRPROD", filename, IFSPath);
			//System.out.println("Spool Status: " + status);
			
			trackingNo = new String(responseJSON.getString("tracking_number"));
			//System.out.println(trackingNo);
			JSONObject freightData = responseJSON.getJSONObject("shipment_cost");
			freight = freightData.getDouble("total_amount");
			freightCharge = Double.toString(freight);
			
			String filePath1 = "/STAMPS/LOGS/TEST/" + filename + ".txt";
			String logString =  "Order number: " + orderNumber + "\n"
					           + "Parcel number: " + filename + "\n"
					           + "JSON Payload: " + fullJSONPayload;
					           ;
		    byte[] logByte = logString.getBytes();
			String ibmI = "TRPROD";
			AS400 machine = new AS400(ibmI);
			IFSFileOutputStream file = new IFSFileOutputStream(machine , filePath1);
			file.write(logByte);
			
			file.close();

		} catch (Exception e) {
			labelDataString = e.toString().trim();
			//System.out.println(labelDataString);

		}
		return decodedLabel;
	}
	
	public static String returnTrackingNo(){		
		return trackingNo;
	}
	
	public static String returnFreightCharge(){		
		return freightCharge;
	}
	
	public static String returnPayload() 
	 {
	   
		return fullJSONPayload;
	}
	
} 
