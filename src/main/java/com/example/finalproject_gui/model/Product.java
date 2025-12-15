package com.example.finalproject_gui.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final StringProperty productId;
    private final StringProperty name;
    private final StringProperty description;
    private final DoubleProperty price;
    private final IntegerProperty quantity;

    public Product(String productId, String name, String description, double price, int quantity) {
        this.productId = new SimpleStringProperty(productId);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleDoubleProperty(price);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public Product() {
        this("", "", "", 0, 0);
    }

    public StringProperty productIdProperty() { return productId; }
    public StringProperty nameProperty() { return name; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty quantityProperty() { return quantity; }

    public String getProductId() { return productId.get(); }
    public void setProductId(String productId) { this.productId.set(productId); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    public boolean updateStock(int amount) {
        if (getQuantity() >= amount) {
            setQuantity(getQuantity() - amount);
            return true;
        } else {
            return false;
        }
    }

    public String displayInfo() {
        return "Product ID: " + getProductId() + "\nName: " + getName() +
                "\nDescription: " + getDescription() + "\nPrice: " + getPrice() + "\nQuantity: " + getQuantity();
    }
}