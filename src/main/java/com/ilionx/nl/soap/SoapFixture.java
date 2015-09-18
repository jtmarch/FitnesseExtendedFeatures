package com.ilionx.nl.soap;

import java.io.IOException;
import java.util.List;

public class SoapFixture {
	
	private FileToStringReader fileReader = new FileToStringReader();
	private SoapConnection connection = new SoapConnection();
	
	private String xml;
	private String response;

    /**
     *
     * | add xml file | path |
     * @param path as xml file path
     */
	public void addXmlFile(String path) {
		this.xml = this.fileReader.getFileContentAsString(path);
	}

    /**
     * | add xml | file |
     * @param xml
     */
	public void addXml(String xml) {
		this.xml = xml;
	}

    /**
     * | send xml to | url |
     * @param url
     * @throws IOException
     */
	public void sendXmlTo(String url) throws IOException {
		this.response = this.connection.call(url, this.xml);
	}

    /**
     * | show | xml |
     * @return
     */
	public String xml() {
		return this.xml;
	}

    /**
     * | show | response |
     * @return
     */
	public String response() {
		return this.response;
	}

    /**
     * | check response element | x | has value | y |
     * @param element
     * @param value
     * @return
     */
	public boolean checkResponseElementHasValue(String element, String value) {
		boolean elementHasValue = false;
		String[] elements = this.response.split("<");
		for (int counter = 0; counter < elements.length; counter++) {
			if (elements[counter].contains(element)) {
				int endOfElement = elements[counter].indexOf(">") + 1;
				String foundValue = elements[counter].substring(endOfElement).trim();
				if (foundValue.equals(value)) {
					elementHasValue = true;
				}
			}
		}
		return elementHasValue;
	}

    /**
     * | show | headers |
     * @return
     */
	public List<SoapHeader> headers() {
		return this.connection.getHeaders();
	}

    /**
     * | do | reset headers |
     */
	public void resetHeaders() {
		this.connection.resetHeaders();
	}

    /**
     * | add header | x | with value | y |
     * @param name
     * @param value
     */
	public void addHeaderWithValue(String name, String value) {
		this.connection.addHeader(new SoapHeader(name, value));
	}
	
}