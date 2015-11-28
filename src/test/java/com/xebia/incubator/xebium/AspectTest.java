package com.xebia.incubator.xebium;

import com.ilionx.nl.properties.XmlPropertyProvider;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AspectTest {

    private XmlPropertyProvider xmlPropertyProvider = new XmlPropertyProvider();

    @Test
    public void testAspect(){
        Assert.assertEquals("https://nl.heineken.com/mijn-account/inloggen?returnUrl=http%3a%2f%2fnl.heineken.com%2fshop", someMethod("#heineken://dataroot/browserUrl"));

        xmlPropertyProvider.setCountry("nederland");
        Assert.assertEquals("https://nl.heineken.com/mijn-account-nederland/inloggen?returnUrl=http%3a%2f%2fnl.heineken.com%2fshop", someMethod("#heineken://dataroot/browserUrl"));

        xmlPropertyProvider.setCountry("ierland");
        Assert.assertEquals("https://nl.heineken.com/mijn-account/inloggen?returnUrl=http%3a%2f%2fnl.heineken.com%2fshop", someMethod("#heineken://dataroot/browserUrl"));
    }

    public String someMethod(String arg) {
        return arg;
    }


}
