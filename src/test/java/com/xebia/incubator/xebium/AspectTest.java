package com.xebia.incubator.xebium;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AspectTest {

    @Test
    public void testAspect(){
        Assert.assertEquals("https://nl.heineken.com/mijn-account/inloggen?returnUrl=http%3a%2f%2fnl.heineken.com%2fshop", someMethod("param://dataroot/browserUrl"));
    }

    public String someMethod(String arg) {
        return arg;
    }


}
