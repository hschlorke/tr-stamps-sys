package com.tirerack.stamps;

import java.io.ByteArrayInputStream;
import spoolWriter.SpoolWriterBackup;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFileOutputStream;

import spoolWriter.SpoolWriter;
import util.SpoolWriterUtil;

public class CreateLabel {
	public static String trackingNo = "";
	public static double freight;
	public static String freightCharge = "" ;
	public static String fullJSONPayload = "";
	public static String status = "";
	public static String responseCodeString = "";
	public static int weightInt;
	public static int itemWeightInt;
	public static int lengthInt;
	public static int widthInt;
	public static int heightInt;
	
	public static void main(String[] args) throws IOException {

	}
	
	public static String label(String StmpsAccess, String filename, String WhCompanyName, String WhName, String WhAddress1, String WhAddress2, String WhCity, String WhState,
							   String WhPostalCode, String WhPhone, String name, String address1, String address2, String address3, String city, 
							   String state, String postalCode, String phone, String serviceType, String packType, float weight, String weightUnit, 
							   float length, float width, float height, String countryOfOrigin, String contentsType, String contentsDesc,
							   String nonDeliveryOpt, String itemDesc, int quantity, float amount, String currency, int itemWeight,
							   String shipDate, String labelSize, String labelFormat, String labelOutputType, String labelURL, String orderNumber, String IFSPath,
							   String IFSLogPath) {
		
		JSONObject labelData;
		String labelDataString = "";
		String decodedLabel = "";
		//USPS always rounds weights and dimensions up, API having some issues with trailing 0's after decimal
		weightInt = (int)Math.ceil(weight);
		itemWeightInt = (int)Math.ceil(weight);
		lengthInt = (int)Math.ceil(length);
		widthInt = (int)Math.ceil(width);
		heightInt = (int)Math.ceil(height);
		try {
			String accessToken = StmpsAccess;
			// System.out.println(accessToken);
			String idempotencyKey = StampsUtil.getUUID();
			// System.out.println(idempotencyKey);
			URL url = new URL(labelURL);
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
			//System.out.println(payload);
			try {
				byte[] input = payload.getBytes();
				os.write(input, 0, input.length);
			} finally {
				os.flush();
				os.close();
			}
			

			int responseCode = conn.getResponseCode();
			//System.out.println(responseCode);

			if (responseCode != StampsUtil.OKReply1) {
				responseCodeString = Integer.toString(responseCode);
				JSONObject responseJSON = new JSONObject(StampsUtil.getError(conn));
				String responseString = responseJSON.toString();
				String filePath1 = "/STAMPS/LOGS/TEST/" + filename + ".txt"; //test
				//String filePath1 = "/STAMPS/LOGS/" + filename + ".txt"; //live
				String logString =  "Order number: " + orderNumber + "\n"
						           + "Parcel number: " + filename + "\n"
						           + "Error: " + responseString;
						           ;
			    byte[] logByte = logString.getBytes();
				String ibmI = "TRPROD";
				AS400 machine = new AS400(ibmI);
				IFSFileOutputStream file = new IFSFileOutputStream(machine , IFSLogPath);
				file.write(logByte);
				file.close();
				return (responseCodeString);
				
			}

			JSONObject responseJSON = new JSONObject(StampsUtil.getResponse(conn));
			fullJSONPayload = responseJSON.toString().trim();
			JSONArray labels = new JSONArray(responseJSON.getJSONArray("labels"));
			labelData = labels.getJSONObject(0);
			labelDataString = labelData.getString("label_data");
			byte[] decodedBytes = Base64.getDecoder().decode(labelDataString);
			decodedLabel = new String(decodedBytes);

			JSONObject freightData = responseJSON.getJSONObject("shipment_cost");
			freight = freightData.getDouble("total_amount");
			freightCharge = Double.toString(freight);
			
			trackingNo = new String(responseJSON.getString("tracking_number"));
			
			status = SpoolWriter.writeSpool(decodedBytes, "TRPROD", filename, IFSPath);
			//System.out.println("Spool Status: " + status);
			
			String filePath1 = "/STAMPS/LOGS/TEST/" + filename + ".txt"; //test
			//String filePath1 = "/STAMPS/LOGS/" + filename + ".txt"; //live
			String logString =  "Order number: " + orderNumber + "\n"
					           + "Parcel number: " + filename + "\n"
					           + "JSON Payload: " + fullJSONPayload;
					           ;
		    byte[] logByte = logString.getBytes();
			String ibmI = "TRPROD";
			AS400 machine = new AS400(ibmI);
			IFSFileOutputStream file = new IFSFileOutputStream(machine , IFSLogPath);
			file.write(logByte);
			file.close();
			
			//Code for sending to the system as ZPL
//			String status = SpoolWriter.writeSpool(decodedLabel.getBytes(SpoolWriterUtil.EBCIDC), "TRPROD","QUSRSYS","GETTOWORK","*OUTQ","*SCS", "TEST", "STAMPLBL", "*YES", StmpsUser, StmpsPassword, "*YES");
//			System.out.println("Spool Status: " + status);

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
	
	public static String returnPayload() {
		return fullJSONPayload;
	}
	}
	
	