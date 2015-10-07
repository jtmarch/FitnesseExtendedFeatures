package com.ilionx.nl.products;

import com.ilionx.nl.xml.XmlReader;
import com.ilionx.nl.xml.XmlWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ProductCatalogFixture {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCatalogFixture.class);

    private Map<String, Product> productMap = new HashMap<>();
    private Dataroot dataroot = new Dataroot();

    public ProductCatalogFixture() {
        //Populate XML Object from file
        dataroot = new XmlReader<Dataroot>().read("products/products.xml", new Dataroot());

        //Populate product list from XML Object
        for (Product product : dataroot.getProducts()) {
            productMap.put(product.getProductId(), product);
        }
        LOG.info("productMap size:" + productMap.keySet().size());
    }

    /**
     * |id|productId|name|price|taxClass|currency|unit|scale|
     *
     * @param productId       Product id of new product
     * @param productName     Name of new product
     * @param productPrice    Price of new product in euros
     * @param productTaxClass Tax class of new product
     * @param productCurrency Currency  of new product
     * @param productUnit     Unit  of new product
     * @param productScale    Scale  of new product
     */
    public void idProductIdNamePriceTaxClassCurrencyUnitScale(String productId, String productName, double productPrice,
                                                              String productTaxClass, String productCurrency, String productUnit, String productScale) {
        if (productMap.containsKey(productId)) {
            updateProductWithProductId(productId, productName, productPrice, productTaxClass,
                    productCurrency, productUnit, productScale);

        } else {
            addProductWithProductId(productId, productName, productPrice, productTaxClass,
                    productCurrency, productUnit, productScale);
        }

    }


    public void fillDatarootFromProductMap() {
        //Clear xml object
        dataroot = new Dataroot();

        //Populate xml object with current product list
        for (Product product : productMap.values()) {
            dataroot.getProducts().add(product);
        }
    }

    /**
     * | Add Product With ProductId | 0740 | name | 68.70 | Irish VAT 23% | Eur | Keg | 1 |
     *
     * @param productId       Product id of new product
     * @param productName     Name of new product
     * @param productPrice    Price of new product in euros
     * @param productTaxClass Tax class of new product
     * @param productCurrency Currency  of new product
     * @param productUnit     Unit  of new product
     * @param productScale    Scale  of new product
     */
    public void addProductWithProductId(String productId, String productName, double productPrice,
                                                 String productTaxClass, String productCurrency, String productUnit, String productScale) {
        //Get new id for product
        int id = productMap.size();

        //Create new product
        Product product = new Product();
        product.setId(Integer.toString(id));
        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductPrice(productPrice);
        product.setProductTaxClass(productTaxClass);
        product.setProductCurrency(productCurrency);
        product.setProductUnit(productUnit);
        product.setProductScale(productScale);

        //Add product to product list
        productMap.put(product.getProductId(), product);

        updateProductCatalogXML();
    }

    /**
     * | Update | Product With ProductId | 0740 | in Catalog | With ProductName | name | With ProductPrice | 68.70
     * With ProductTaxClass | Irish VAT 23% | With ProductCurrency | Eur | With ProductUnit | Keg
     * With ProductScale | 1
     *
     * @param productId       Product id of new product
     * @param productName     Name of new product
     * @param productPrice    Price of new product in euros
     * @param productTaxClass Tax class of new product
     * @param productCurrency Currency  of new product
     * @param productUnit     Unit  of new product
     * @param productScale    Scale  of new product
     */
    public void updateProductWithProductId(String productId, String productName, double productPrice,
                                           String productTaxClass, String productCurrency, String productUnit, String productScale) {
        //Update product list in memory
        Product product = productMap.get(productId);

        product.setProductName(productName);
        product.setProductPrice(productPrice);
        product.setProductTaxClass(productTaxClass);
        product.setProductCurrency(productCurrency);
        product.setProductUnit(productUnit);
        product.setProductScale(productScale);

        updateProductCatalogXML();
    }

    /**
     * | Remove | Product With ProductId | 0740 | in Catalog
     *
     * @param productId Product id of new product
     */
    public void removeProductWithProductId(String productId) {

        //Update product list in memory
        productMap.remove(productId);

        updateProductCatalogXML();
    }

    public int getNrOfProducts() {
        return productMap.keySet().size();
    }

    public void updateProductCatalogXML() {
        //Write product list to XML Object
        fillDatarootFromProductMap();

        //Write XML Object to file
        new XmlWriter<Dataroot>()
                .createFile("src/test/resources/products/products-new.xml")
                .write("src/test/resources/products/products-new.xml", dataroot);
    }


}
