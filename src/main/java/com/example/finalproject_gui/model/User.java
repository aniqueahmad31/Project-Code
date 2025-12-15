package com.example.finalproject_gui.model;

public class User
{
    private String userID;
    private String name;
    private String email;
    private String password;

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setName(String name)
    {
        if (name != null && !name.matches("[a-zA-Z\\s]+"))
        {
            throw new IllegalArgumentException("Invalid Name! Only letters and spaces allowed.");
        }
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setEmail(String email)
    {
        if(email == null || !email.endsWith("@gmail.com"))
        {
            throw new IllegalArgumentException("Email must end with @gmail.com");
        }
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setPassword(String password)
    {
        if(password == null || password.length() < 4)
        {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public User()
    {
        this.userID = "";
        this.name = null;
        this.email = "";
        this.password = "";
    }

    public User(String userID, String name, String email, String password)
    {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String displayUser()
    {
        return "User ID: " + userID + "\nName: " + name + "\nEmail: " + email;
    }
}