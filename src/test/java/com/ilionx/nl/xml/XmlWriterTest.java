package com.ilionx.nl.xml;

import com.ilionx.nl.products.Dataroot;
import com.ilionx.nl.products.Product;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

public class XmlWriterTest extends TestCase {

    @Test
    public void testWrite() throws Exception {
        Dataroot dataroot = new Dataroot();
        Product product = new Product();
        product.setId("Prd34");
        dataroot.getProducts().add(product);

        new XmlWriter<Dataroot>().write("src/test/resources/products-result.xml", dataroot);
    }

}
