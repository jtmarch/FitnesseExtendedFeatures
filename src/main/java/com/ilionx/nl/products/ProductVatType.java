package com.ilionx.nl.products;

public enum ProductVatType {

    IRISH_VAR_23("Irish VAR 23%", 23),
    IRISH_VAT_23("Irish VAT 23%", 23);

    private String description;

    private double vat;

    ProductVatType(String description, double vat) {
        this.description = description;
        this.vat = vat;
    }

    public String getDescription() {
        return description;
    }

    public double getVat() {
        return vat;
    }
}
