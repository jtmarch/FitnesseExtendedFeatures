package com.ilionx.nl.properties;

import java.io.IOException;
import java.util.Properties;

public class PropertiesProvider {

    private Properties properties;

    private String propertyName;

    public PropertiesProvider(final String file) {
        properties = new Properties();
        try {
            properties.load(PropertiesProvider.class.getClassLoader().getResourceAsStream("properties/" + file + ".properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty() {
        return properties.getProperty(propertyName);
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }
}
