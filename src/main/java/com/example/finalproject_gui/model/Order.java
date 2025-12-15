package com.example.finalproject_gui.model;

import java.util.ArrayList;
import java.util.List;

public class Order
{
    private String orderID;
    private Customer customer;
    private List<Product> products;
    private Bill bill;

    public void setOrderID(String orderID)
    {
        this.orderID = orderID;
    }

    public String getOrderID()
    {
        return this.orderID;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Customer getCustomer()
    {
        return this.customer;
    }

    public List<Product> getProducts()
    {
        return this.products;
    }

    public void setBill(Bill bill)
    {
        this.bill = bill;
    }

    public Bill getBill()
    {
        return this.bill;
    }

    public Order()
    {
        this.orderID = "null";
        this.customer = null;
        products = new ArrayList<>();
    }

    public Order(String orderID, Customer customer, List<Product> products)
    {
        this.orderID = orderID;
        this.customer = customer;
        this.products = products;
    }

    public void addItem(Product p)
    {
        this.products.add(p);
    }

    public String displayOrder()
    {
        String result = "------ ORDER DETAILS ------\n";
        result += "Order ID: " + orderID + "\n";
        result += customer.displayUser() + "\n\n";
        result += "Products Purchased:\n";

        for (Product p : products)
        {
            result += p.getName() + " | Rs. " + p.getPrice() + " | Quantity: " + p.getQuantity() + "\n";
        }
        result += "\n";

        result += bill.displayBill();

        result += "---------------------------\n";
        return result;
    }
}