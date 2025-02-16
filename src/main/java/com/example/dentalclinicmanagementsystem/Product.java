package com.example.dentalclinicmanagementsystem;

public class Product {
    private final String productName;
    private final double pricePerUnit;
    private final int quantityAvailable;
    private final String lastUpdated;

    public Product(String productName, double pricePerUnit, int quantityAvailable, String lastUpdated) {
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.quantityAvailable = quantityAvailable;
        this.lastUpdated = lastUpdated;
    }

    public String getProductName() {
        return productName;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}

