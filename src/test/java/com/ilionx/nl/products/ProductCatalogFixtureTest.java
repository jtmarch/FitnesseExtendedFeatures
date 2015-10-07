package com.ilionx.nl.products;

import com.ilionx.nl.xml.XmlReader;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;

@RunWith(JUnit4.class)
public class ProductCatalogFixtureTest {

    @Test
    public void testAddProductToCatalog(){
        ProductCatalogFixture productCatalogFixture = new ProductCatalogFixture();

        int amountOfProducts = productCatalogFixture.getNrOfProducts();

        productCatalogFixture.addProductWithProductId("0741", "New Product", 68.70, "Irish VAR 23%", "Eur", "Keg", "1");

        try {
            Thread.sleep(2000);
        }catch (Exception e){}

        Dataroot products = new XmlReader<Dataroot>().read("products/products-new.xml", new Dataroot());
        ArrayList<Product> productlist = (ArrayList)products.getProducts();
        int newSize = productlist.size();

        Assert.assertTrue(amountOfProducts == newSize -1);


    }





}
