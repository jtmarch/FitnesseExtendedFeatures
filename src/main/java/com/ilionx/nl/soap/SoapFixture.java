package com.ilionx.nl.soap;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SoapFixture {

    private static final Logger LOG = LoggerFactory.getLogger(SoapFixture.class);

    private FileToStringReader fileReader = new FileToStringReader();
    private SoapConnection connection = new SoapConnection();

    private String xml;
    private String response;
    private String templatePath;
    private HashMap<String, Object> modelMap = new HashMap<>();

    /**
     * | add xml file | path |
     *
     * @param path as xml file path
     */
    public void addXmlFile(String path) {
        this.xml = this.fileReader.getFileContentAsString(path);
    }

    /**
     * | set soap template file | path |
     *
     * @param path as freemarker template file path
     */
    public void setSoapTemplateFile(String path) {
        this.templatePath = path;
    }

    /**
     * | set template parameter | name | with value | value |
     *
     * @param name as the name as it occurs in the freemarker template
     * @param value as the value for the given parameter name
     */
    public void setTemplateParameterWithValue(String name, String value) {
        this.modelMap.put(name, value);
    }

    /**
     * | add xml | file |
     *
     * @param xml
     */
    public void addXml(String xml) {
        this.xml = xml;
    }

    /**
     * | send soap message to | url |
     *
     * @param url
     * @throws IOException
     */
    public void sendSoapMessageTo(String url) throws IOException, TemplateException {
        if (templatePath != null) {
            this.xml = getXmlFromFreemarkerTemplate();
        }

        this.response = this.connection.call(url, this.xml);
    }

    /**
     * | show | xml |
     *
     * @return
     */
    public String xml() {
        return this.xml;
    }

    /**
     * | show | response |
     *
     * @return
     */
    public String response() {
        return this.response;
    }

    /**
     * | ensure | response element | x | has value | y |
     *
     * @param element
     * @param value
     * @return
     */
    public boolean responseElementHasValue(String element, String value) {
        boolean elementHasValue = false;
        String[] elements = this.response.split("<");
        List<String> allElements = new ArrayList<>();
        for(int x=0;x<element.length();x++){
            allElements.addAll(Arrays.asList(elements[x].split("&lt;")));
        }

        for (String elementStr : allElements) {
            if (elementStr.contains(element)) {
                int endOfElement = elementStr.indexOf(">") + 1;
                if(endOfElement == 0){
                    endOfElement = elementStr.indexOf("&gt;") + 4;
                }
                String foundValue = elementStr.substring(endOfElement).trim();
                LOG.info("Value found: {} ", foundValue);
                if (foundValue.equals(value)) {
                    elementHasValue = true;
                }
            }
        }
        return elementHasValue;
    }

    /**
     * | show | headers |
     *
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
     *
     * @param name
     * @param value
     */
    public void addHeaderWithValue(String name, String value) {
        this.connection.addHeader(new SoapHeader(name, value));
    }

    private String getXmlFromFreemarkerTemplate() throws IOException, TemplateException {
        Configuration freemarkerCfg = new Configuration(Configuration.VERSION_2_3_22);
        freemarkerCfg.setDefaultEncoding("UTF-8");
        freemarkerCfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarkerCfg.setTemplateLoader(new FreemarkerTemplateLoader());
        Template template = freemarkerCfg.getTemplate(templatePath);
        StringWriter stringWriter = new StringWriter();
        template.process(modelMap, stringWriter);
        return stringWriter.toString();
    }

}