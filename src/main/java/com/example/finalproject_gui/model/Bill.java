package com.example.finalproject_gui.model;

public class Bill
{
    private double subtotal;
    private double tax;
    private double discount;
    private double totalAmount;

    public void setSubtotal(double subtotal)
    {
        this.subtotal = subtotal;
    }

    public double getSubtotal()
    {
        return subtotal;
    }

    public void setTax(double tax)
    {
        this.tax = tax;
    }

    public double getTax()
    {
        return tax;
    }

    public void setDiscount(double discount)
    {
        this.discount = discount;
    }

    public double getDiscount()
    {
        return discount;
    }

    public void setTotalAmount(double totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount()
    {
        return totalAmount;
    }

    public Bill()
    {
        this.subtotal = 0;
        this.tax = 0;
        this.discount = 0;
        this.totalAmount = 0;
    }

    public double calculateDiscount(Cart cart)
    {
        double total = cart.getTotalCost();

        if (total > 20000)
        {
            discount = total * 0.10;
        }
        else if (total > 10000)
        {
            discount = total * 0.05;
        }
        else
        {
            discount = 0;
        }
        this.setDiscount(discount);
        return discount;
    }

    public void calculateBill(Cart cart)
    {
        subtotal = cart.calculateTotal(); // Using the correct method
        tax = subtotal * 0.16;
        totalAmount = subtotal + tax - discount;
        this.setSubtotal(subtotal);
        this.setTax(tax);
        this.setTotalAmount(totalAmount);
    }

    public String displayBill()
    {
        String result = "------ BILL DETAILS ------\n";
        result += "Subtotal: Rs. " + subtotal + "\n";
        result += "Tax (16%): Rs. " + tax + "\n";
        result += "Discount: Rs. " + discount + "\n";
        result += "Total Payable: Rs. " + totalAmount + "\n";
        return result;
    }
}