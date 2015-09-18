package com.ilionx.nl.soap;

import java.io.*;

public class FileToStringReader {
	
	public String getFileContentAsString(String path) {
		StringBuilder strBuilder = new StringBuilder();
		try {
            InputStream xmlFile = this.getClass().getClassLoader().getResourceAsStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(xmlFile));
			String line;
			while((line = reader.readLine()) != null) {
				strBuilder.append(line);
			}
			reader.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return strBuilder.toString();
	}
	
}