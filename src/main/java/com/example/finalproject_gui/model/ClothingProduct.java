package com.example.finalproject_gui.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClothingProduct extends Product
{
    private final StringProperty size;
    private final StringProperty color;
    private final StringProperty fabricType;

    public void setSize(String size)
    {
        this.size.set(size);
    }

    public String getSize()
    {
        return this.size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public void setColor(String color)
    {
        this.color.set(color);
    }

    public String getColor()
    {
        return this.color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public void setFabricType(String fabricType)
    {
        this.fabricType.set(fabricType);
    }

    public String getFabricType()
    {
        return this.fabricType.get();
    }

    public StringProperty fabricTypeProperty() {
        return fabricType;
    }

    public ClothingProduct()
    {
        super();
        this.size = new SimpleStringProperty("null");
        this.color = new SimpleStringProperty("null");
        this.fabricType = new SimpleStringProperty("null");
    }

    public ClothingProduct(String productId, String name, String description, double price, int quantity, String size, String color, String fabricType)
    {
        super(productId, name, description, price, quantity);
        this.size = new SimpleStringProperty(size);
        this.color = new SimpleStringProperty(color);
        this.fabricType = new SimpleStringProperty(fabricType);
    }

    @Override
    public boolean updateStock(int amount)
    {
        return super.updateStock(amount);
    }

    @Override
    public String displayInfo()
    {
        return  super.displayInfo() + "\nSize: " + getSize() +
                "\nColor: " + getColor() + "\nFabricType: " + getFabricType() +
                "\n" + "-----------------------------" + "\n";
    }
}