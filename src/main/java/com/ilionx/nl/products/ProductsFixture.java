package com.ilionx.nl.products;

import com.ilionx.nl.xml.XmlReader;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsFixture {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsFixture.class);

    private Map<String, Product> productMap = new HashMap<>();

    private List<String> shoppingCart = new ArrayList<>();

    public ProductsFixture() {
        Dataroot products = new XmlReader<Dataroot>().read("products/products.xml", new Dataroot());
        for (Product product : products.getProducts()) {
            productMap.put(product.getProductId(), product);
        }
        LOG.info("productMap size:" +productMap.keySet().size());
        LOG.info("shopping cart size:" + shoppingCart.size());
    }

    /**
     * | Add | 7 | Products With ProductId | 0740 | To ShoppingCart |
     * @param productId
     * @param nrOfItems
     */
    public void addProductsWithProductIdToShoppingCart( int nrOfItems, String productId){
        for(int x=0;x<nrOfItems;x++){
            addToCart(productId);
        }
    }

    /**
     * | ensure | that the total price of the products in the shopping cart is | <i>expression</i> |
     * @param price
     */
    public boolean thatTheTotalPriceOfTheProductsInTheShoppingCartIs(double price){
        if(getTotalPrice() == price){
            return true;
        }
        return false;
    }

    /**
     * | ensure | that the total vat of the products in the shopping cart is | <i>expression</i> |
     * @param vat
     */
    public boolean thatTheTotalVatOfTheProductsInTheShoppingCartIs(double vat){
        if(getTotalVat() == vat){
            return true;
        }
        return false;
    }

    /**
     * | ensure | that the total amount of the products in the shopping cart is | <i>expression</i> |
     * @param amount
     */
    public boolean thatTheTotalAmountOfProductsInTheShoppingCartIs(int amount){
        if(shoppingCart.size() == amount ){
            return true;
        }
        return false;
    }

    public int getNrOfProducts(String productId){
        int count = 0;
        for(String id : shoppingCart){
            if(id.equalsIgnoreCase(productId)){
                count++;
            }
        }
        return count;
    }

    public void addToCart(String id) {
        shoppingCart.add(id);
    }

    public double getTotalPrice(){
        return getTotalPrice(null);
    }

    public double getTotalVat(){return getTotalVat(null);}

    public double getTotalPrice(String requestedProductId){
        LOG.info("product keys: " + productMap.keySet().toString() );
        LOG.info("shopping cart keys: " + shoppingCart.toString() );

        double priceTotal = 0.0;

        for(String id : shoppingCart){
            if(requestedProductId == null || requestedProductId.equalsIgnoreCase(id)){
                Product currentProduct = productMap.get(id);
                if(null != currentProduct){
                    priceTotal += currentProduct.getProductPrice();
                }
            }
        }
        return priceTotal;
    }

    public double getTotalVat(String requestedProductId){
        double vatTotal = 0.0;
        for(String id : shoppingCart){
            if(requestedProductId == null || requestedProductId.equalsIgnoreCase(id)){
                double price = productMap.get(id).getProductPrice();
                String taxClass = productMap.get(id).getProductTaxClass();
                for(ProductVatType productVatType: ProductVatType.values()){
                    if(taxClass.equalsIgnoreCase(productVatType.getDescription())){
                        vatTotal += (price/100) * productVatType.getVat();
                    }
                }
            }
        }
        return vatTotal;
    }

}
