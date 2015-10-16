package com.xebia.incubator.xebium;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AspectTest {

    @Test
    public void testAspect(){
        Assert.assertEquals("kip", someMethod("param:somevalue"));
    }

    public String someMethod(String arg) {
        return arg;
    }


}
