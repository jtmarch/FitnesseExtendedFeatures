package com.ilionx.nl.properties;

public class PropertiesFixture {

    /**
     * Set the country for property loading.
     * | set country for property loading to | <country> |
     * @param country the country to set.
     */
    public void setCountryForPropertyLoadingTo(String country) {
        XmlPropertyProvider.setCountry(country);
    }

}
