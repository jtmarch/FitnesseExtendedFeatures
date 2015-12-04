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

    private static final String COUNTRY = "country";
    private static final String COMMON_COUNTRY_DIR = "common";

    /**
     * Set the System.property <i>country</i> to a certain value. This property is used
     * to determine the directory to search for XML files.
     * @param country the country to set.
     */
    public static void setCountry(final String country) {
        System.setProperty(COUNTRY, country);
    }

    /**
     * Get a property value from a xml file called <i>fileName</i> using the
     * <i>System.getProperty("country")</i> for the directory and the <i>xpath</i> expression
     * to find the element in the xml file.
     * @param fileName the filename in the common dir
     * @param xpath the xpath expression used to find the element in the xml file
     * @return the found value.
     */
    public String getXmlPropertyByXpath(final String fileName, final String xpath) {
        String country = System.getProperty(COUNTRY);
        return getPropertyFromXml(fileName, xpath, country);
    }

    /**
     * Ignore the System property named country and use the common dir instead.
     * @param fileName the filename in the common dir
     * @param xpath the xpath expression used to find the element in the xml file
     * @return the found value.
     */
    public String getXmlPropertyByXpathFromCommon(final String fileName, final String xpath) {
        return getPropertyFromXml(fileName, xpath, COMMON_COUNTRY_DIR);
    }

    private String getPropertyFromXml(final String fileName, final String xpath, final String country) {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            InputStream fileInputStream = XmlPropertyProvider.class.getClassLoader().getResourceAsStream("properties/" + country + "/" + fileName + ".xml");
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
