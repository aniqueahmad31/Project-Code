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

    User()
    {
        this.userID = "";
        this.name = null;
        this.email = "";
        this.password = "";
    }

    User(String userID, String name, String email, String password)
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

class Customer extends User
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

    Customer()
    {
        super();
        this.address = null;
        this.phone = null;
    }

    Customer(String userID, String name, String email, String password, String address, String phone)
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
