package com.ilionx.nl.properties;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class XmlPropertyProvider {

    public String getXmlPropertyByXpath(final String fileName, final String xpath) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            InputStream fileInputStream = XmlPropertyProvider.class.getClassLoader().getResourceAsStream("properties/" + fileName + ".xml");
            Document dDoc = builder.parse(fileInputStream);

            XPath xPath = XPathFactory.newInstance().newXPath();
            System.out.println("Evaluating xpath: " + xpath);
            Node node = (Node) xPath.evaluate(xpath, dDoc, XPathConstants.NODE);
            return node.getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
