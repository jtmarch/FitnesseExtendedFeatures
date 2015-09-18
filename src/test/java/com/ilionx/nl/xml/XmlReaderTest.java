package com.ilionx.nl.xml;

import com.ilionx.nl.products.Dataroot;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

public class XmlReaderTest extends TestCase {

    @Test
    public void testRead() throws Exception {
        Dataroot products = new XmlReader<Dataroot>().read("products/products.xml", new Dataroot());
        Assert.assertEquals(3, products.getProducts().size());
    }

}
