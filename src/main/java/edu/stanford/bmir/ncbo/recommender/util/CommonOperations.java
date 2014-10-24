package edu.stanford.bmir.ncbo.recommender.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 13/06/2014 10:33:44
 */
public class CommonOperations {
	/**
	 * @param url
	 * @param maxCallsPerSecond
	 * @return The response obtained when invoking the url
	 */
	@SuppressWarnings("deprecation")
	public static String callService(String url, int maxCallsPerSecond) {
		try {
			Thread.sleep(1000 / maxCallsPerSecond);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String out = "";
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(url);
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 404) {
				System.out.println("WARNING: 404 code (resource not found)");
				return null;
			}
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
			StringBuilder builder = new StringBuilder();
			String aux = "";
			while ((aux = br.readLine()) != null) {
				builder.append(aux);
			}
			out = builder.toString();
			httpClient.getConnectionManager().shutdown();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
}
