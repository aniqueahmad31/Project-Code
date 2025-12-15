package com.example.finalproject_gui.model;

public class Customer extends User
{
    private String address;
    private String phone;

    public void setAddress(String address)
    {
        if(address != null && !address.matches("[a-zA-Z0-9 ,.#-]+"))
        {
            throw new IllegalArgumentException("Invalid Address! Only letters, numbers, spaces, comma, period, dash, and # allowed.");
        }
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }

    public void setPhone(String phone)
    {
        if(phone != null && !phone.matches("03\\d{9}"))
        {
            throw new IllegalArgumentException("Invalid Phone Number! Must start with 03 and be 11 digits long.");
        }
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }

    public Customer()
    {
        super();
        this.address = null;
        this.phone = null;
    }

    public Customer(String userID, String name, String email, String password, String address, String phone)
    {
        super(userID, name, email, password);
        this.address = address;
        this.phone = phone;
    }

    public boolean hasDetails()
    {
        return (getName() != null && !getName().equals("null") &&
                address != null && !address.equals("null") &&
                phone != null && !phone.equals("null"));
    }

    @Override
    public String displayUser()
    {
        return super.displayUser() +
                (hasDetails() ? "\nAddress: " + address + "\nPhone: " + phone : "");
    }
}