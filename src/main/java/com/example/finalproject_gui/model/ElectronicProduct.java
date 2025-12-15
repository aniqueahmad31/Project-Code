package com.example.finalproject_gui.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class ElectronicProduct extends Product
{
    private final StringProperty brand;
    private final IntegerProperty warrantyMonth;
    private final StringProperty model;

    public void setBrand(String brand)
    {
        this.brand.set(brand);
    }

    public String getBrand()
    {
        return this.brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setWarrantyMonth(int warrantyMonth)
    {
        this.warrantyMonth.set(warrantyMonth);
    }

    public int getWarrantyMonth()
    {
        return this.warrantyMonth.get();
    }

    public IntegerProperty warrantyMonthProperty() {
        return warrantyMonth;
    }

    public void setModel(String model)
    {
        this.model.set(model);
    }

    public String getModel()
    {
        return this.model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public ElectronicProduct()
    {
        super();
        this.brand = new SimpleStringProperty("null");
        this.warrantyMonth = new SimpleIntegerProperty(0);
        this.model = new SimpleStringProperty("null");
    }

    public ElectronicProduct(String productId, String name, String description, double price, int quantity, String brand, int warrantyMonth, String model)
    {
        super(productId, name, description, price, quantity);
        this.brand = new SimpleStringProperty(brand);
        this.warrantyMonth = new SimpleIntegerProperty(warrantyMonth);
        this.model = new SimpleStringProperty(model);
    }

    @Override
    public boolean updateStock(int amount)
    {
        return super.updateStock(amount);
    }

    @Override
    public String displayInfo()
    {
        return super.displayInfo() + "\nBrand: " + getBrand() +
                "\nWarranty Month: " + getWarrantyMonth() + "\nModel: " + getModel() +
                "\n" + "-----------------------------" + "\n";
    }
}