package com.ilionx.nl.products;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProductsFixtureTest {

    @Test
    public void testAddToCart(){
        ProductsFixture productsFixture = new ProductsFixture();
        productsFixture.addProductsWithProductIdToShoppingCart("0740", 7);
        System.out.println(productsFixture.getTotalPrice());
    }

}
