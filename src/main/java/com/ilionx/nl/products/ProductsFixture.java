package com.ilionx.nl.products;

import com.ilionx.nl.xml.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsFixture {

    private static final Logger LOG = LoggerFactory.getLogger(ProductsFixture.class);

    private final Map<String, Product> productMap = new HashMap<>();

    private List<String> shoppingCart = new ArrayList<>();

    public ProductsFixture() {
        Dataroot products = new XmlReader<Dataroot>().read("products/products.xml", new Dataroot());
        for (Product product : products.getProducts()) {
            productMap.put(product.getProductId(), product);
            LOG.info("Added product with productId {} and price {}", product.getProductId(), product.getProductPrice());
        }
    }

    /**
     * | Add | 7 | Products With ProductId | 0740 | To ShoppingCart |
     * @param productId
     * @param nrOfItems
     */
    public void addProductsWithProductIdToShoppingCart(String productId, int nrOfItems){
        for(int x=0;x<nrOfItems;x++){
            addToCart(productId);
        }
    }

    /**
     * | ensure | that the total price of the products in the shopping cart is | <i>expression</i> |
     * @param price
     */
    public boolean thatTheTotalPriceOfTheProductsInTheShoppingCartIs(double price){
        LOG.info("The total number of prodycts in the map is: {}", productMap.keySet().size());
        if(getTotalPrice() == price){
            return true;
        }
        return false;
    }

    public void addToCart(String id) {
        shoppingCart.add(id);
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

    public double getTotalPrice(){
        return getTotalPrice(null);
    }

    public double getTotalVat(){
        return getTotalVat(null);
    }

    public double getTotalPrice(String requestedProductId){
        double priceTotal = 0.0;
        for(String id : shoppingCart){
            if(requestedProductId == null || requestedProductId.equalsIgnoreCase(id)){
                priceTotal += productMap.get(id).getProductPrice();
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
