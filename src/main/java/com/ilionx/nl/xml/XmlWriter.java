package com.ilionx.nl.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

public class XmlWriter<T extends Object> {

    public void write(final String file, final T object) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();

            OutputStream outputStream = new FileOutputStream(new File(file));

            marshaller.marshal(object, outputStream);
        } catch (JAXBException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }


    public XmlWriter<T> createFile(String fileName){

        try {

            File file = new File(fileName);

            if (file.createNewFile()){
                System.out.println("File is created!");
            }else{
                System.out.println("File already exists.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

}
