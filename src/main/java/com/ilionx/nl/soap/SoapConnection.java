package com.ilionx.nl.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SoapConnection {
	
	private List<SoapHeader> headers = new ArrayList<>();
	
	public SoapConnection() {
		this.headers.add(new SoapHeader("Content-Type", "text/xml; charset=utf-8"));
	}
	
	public List<SoapHeader> getHeaders() {
		return this.headers;
	}

	public void resetHeaders() {
		this.headers.clear();
	}
	
	public void addHeader(SoapHeader header) {
		for (SoapHeader existingHeader : this.headers) {
			if (existingHeader.equals(header)) {
				return;
			}
		}
		this.headers.add(header);
	}
	
	public String call(String url, String xml) throws IOException {
		HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
			
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	
		byte[] buffer = xml.getBytes();
		outputStream.write(buffer);
		byte[] byteArrayOutputstream = outputStream.toByteArray();
	
		httpConnection.setRequestProperty("Content-Length", String.valueOf(byteArrayOutputstream.length));
		for (SoapHeader header : this.headers) {
			httpConnection.setRequestProperty(header.getName(), header.getValue());
		}
		httpConnection.setRequestMethod("POST");
		httpConnection.setDoOutput(true);
		httpConnection.setDoInput(true);

		OutputStream outputstream = httpConnection.getOutputStream();
		outputstream.write(byteArrayOutputstream);
		outputstream.close();
	
		InputStreamReader inputStreamReader = new InputStreamReader(httpConnection.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	
		StringBuilder responseMessageBuilder = new StringBuilder("");
		String response;
		while ((response = bufferedReader.readLine()) != null) {
			responseMessageBuilder.append(response);
		}
		
		return responseMessageBuilder.toString();
	}

}