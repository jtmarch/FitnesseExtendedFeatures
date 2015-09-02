package com.ilionx.nl.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class XmlReader<T extends Object> {

    public T read(final String file, final T clazz) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clazz.getClass());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            InputStream xmlFile = this.getClass().getClassLoader().getResourceAsStream(file);

            T result = (T) unmarshaller.unmarshal(xmlFile);
            return result;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
