package com.ilionx.nl.products;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProductsFixtureTest {

    @Test
    public void testAddToCart(){
        ProductsFixture productsFixture = new ProductsFixture();
        productsFixture.addProductsWithProductIdToShoppingCart(10, "0740");
        double totalPrice =  productsFixture.getTotalPrice();
        double totalVat =  productsFixture.getTotalVat();
        int amountOfProducts = productsFixture.getNrOfProducts("0740");

        Assert.assertTrue(amountOfProducts == 10);
        Assert.assertTrue(totalPrice == 687.40);
        Assert.assertEquals(totalVat, 158.10d,0.01d);
    }





}
