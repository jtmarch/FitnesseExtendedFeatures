package com.ilionx.nl.soap;

public class SoapHeader {

	private final String name;
	private final String value;

	public SoapHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null) {
			return false;
		}
		if (otherObject == this) {
			return true;
		}
		if (!(otherObject instanceof SoapHeader)) {
			return false;
		}
		SoapHeader otherHeader = (SoapHeader) otherObject;
		if (otherHeader.getName().equals(this.name) && otherHeader.getValue().equals(this.value)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.name + ": " + this.value;
	}

}