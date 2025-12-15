package com.example.finalproject_gui.model;

public class Manager
{
    private String managerID;
    private String password;
    private InventoryManager inventory;

    public Manager(String managerID, String password, InventoryManager inventory)
    {
        this.managerID = managerID;
        this.password = password;
        this.inventory = inventory;
    }

    public boolean login(String managerID, String password)
    {
        return this.managerID.equals(managerID) && this.password.equals(password);
    }

    public String viewAllProducts()
    {
        return inventory.viewAllProducts();
    }

    public String viewAllOrders()
    {
        return inventory.viewAllOrders();
    }


    public String viewAllCustomers()
    {
        return inventory.viewAllCustomers();
    }

}
