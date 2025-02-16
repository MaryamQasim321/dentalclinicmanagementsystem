package com.example.dentalclinicmanagementsystem;

import javafx.beans.property.*;

public class PriceHistory {
    private final IntegerProperty historyId;
    private final IntegerProperty productId;
    private final StringProperty productName;
    private final DoubleProperty oldPrice;
    private final DoubleProperty newPrice;
    private final StringProperty changeDate;

    public PriceHistory(int historyId, int productId, String productName, double oldPrice, double newPrice, String changeDate) {
        this.historyId = new SimpleIntegerProperty(historyId);
        this.productId = new SimpleIntegerProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.oldPrice = new SimpleDoubleProperty(oldPrice);
        this.newPrice = new SimpleDoubleProperty(newPrice);
        this.changeDate = new SimpleStringProperty(changeDate);
    }

    public int getHistoryId() {
        return historyId.get();
    }

    public IntegerProperty historyIdProperty() {
        return historyId;
    }

    public int getProductId() {
        return productId.get();
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public double getOldPrice() {
        return oldPrice.get();
    }

    public DoubleProperty oldPriceProperty() {
        return oldPrice;
    }

    public double getNewPrice() {
        return newPrice.get();
    }

    public DoubleProperty newPriceProperty() {
        return newPrice;
    }

    public String getChangeDate() {
        return changeDate.get();
    }

    public StringProperty changeDateProperty() {
        return changeDate;
    }
}
