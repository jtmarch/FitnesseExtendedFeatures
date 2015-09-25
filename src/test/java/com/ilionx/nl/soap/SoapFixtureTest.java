package com.ilionx.nl.soap;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.net.ServerSocket;

@RunWith(MockitoJUnitRunner.class)
public class SoapFixtureTest extends TestCase {

    private static final String SERVER = "127.0.0.1";

    private static SoapFixture soapFixture;

    private static Endpoint endpoint;
    private static int port;

    @InjectMocks
    private static SomeWebservice sut = new SomeWebservice();

    @BeforeClass
    public static void beforeClass() throws Exception {
        soapFixture = new SoapFixture();
        port = findFreePort();
        endpoint = Endpoint.publish("http://" + SERVER + ":" + port + "/someWebservice", sut);
    }

    @AfterClass
    public static void afterClass() throws Exception {
        endpoint.stop();
    }

    @Test
    public void testSendXmlTo() throws Exception {
        soapFixture.addXmlFile("soap/exampleSoapMessage.xml");
        soapFixture.sendSoapMessageTo("http://" + SERVER + ":" + port + "/someWebservice");
        Assert.assertNotNull(soapFixture.response());
        Assert.assertTrue(soapFixture.response().length() > 0);
    }

    @Test
    public void testXml() throws Exception {
        soapFixture.addXml("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:soap=\"http://soap.nl.ilionx.com/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <soap:doSomething>\n" +
                "            <!--Optional:-->\n" +
                "            <value>?</value>\n" +
                "        </soap:doSomething>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>");
        soapFixture.sendSoapMessageTo("http://" + SERVER + ":" + port + "/someWebservice");
        Assert.assertNotNull(soapFixture.response());
        Assert.assertTrue(soapFixture.response().length() > 0);
    }

    @Test
    public void testSendFreeMarkerTemplateTo() throws Exception {
        soapFixture.setSoapTemplateFile("soap/exampleFreemarkerMessage.ftl");
        String inputField = "Hello world!!";
        soapFixture.setTemplateParameterWithValue("valueForTemplate", inputField);
        soapFixture.sendSoapMessageTo("http://" + SERVER + ":" + port + "/someWebservice");
        Assert.assertNotNull(soapFixture.response());
        Assert.assertTrue(soapFixture.response().length() > 0);
        soapFixture.checkResponseElementHasValue("return", inputField);
    }

    public void testHeaders() throws Exception {

    }

    public void testResetHeaders() throws Exception {

    }

    public void testAddHeaderWithValue() throws Exception {

    }

    private static int findFreePort() {
        try {
            ServerSocket server = new ServerSocket(0);
            int port = server.getLocalPort();
            server.close();
            return port;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
