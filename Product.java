public class Product
{
    private String productId;
    private String name;
    private String description;
    private double price;
    private int quantity;

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    public String getProductId()
    {
        return this.productId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getPrice()
    {
        return this.price;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public Product()
    {
        this.productId = "null";
        this.name = "null";
        this.description = "null";
        this.price = 0;
        this.quantity = 0;
    }

    public Product(String productId, String name, String description, double price, int quantity)
    {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String displayInfo()
    {
        return "Product ID: " + productId + "\nName: " + name +
                "\nDescription: " + description + "\nPrice: " + price + "\nQuantity: " + quantity;
    }

    public boolean updateStock(int amount)
    {
        if (quantity >= amount)
        {
            this.quantity -= amount;
            return true;
        }
        else
        {
            return false;
        }

    }
}

class ClothingProduct extends Product
{
    private String size;
    private String color;
    private String fabricType;

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getSize()
    {
        return this.size;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getColor()
    {
        return this.color;
    }

    public void setFabricType(String fabricType)
    {
        this.fabricType = fabricType;
    }

    public String getFabricType()
    {
        return this.fabricType;
    }

    ClothingProduct()
    {
        super();
        this.size = "null";
        this.color = "null";
        this.fabricType = "null";
    }

    ClothingProduct(String productId, String name, String description, double price, int quantity, String size, String color, String fabricType)
    {
        super(productId, name, description, price, quantity);
        this.size = size;
        this.color = color;
        this.fabricType = fabricType;
    }

    @Override
    public boolean updateStock(int amount)
    {
        return super.updateStock(amount);
    }

    @Override
    public String displayInfo()
    {
        return  super.displayInfo() + "\nSize: " + size +
                "\nColor: " + color + "\nFabricType: " + fabricType +
                "\n" + "-----------------------------" + "\n";
    }
}

class ElectronicProduct extends Product
{
    private String brand;
    private int warrantyMonth;
    private String model;

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public String getBrand()
    {
        return this.brand;
    }

    public void setWarrantyMonth(int warrantyMonth)
    {
        this.warrantyMonth = warrantyMonth;
    }

    public int getWarrantyMonth()
    {
        return this.warrantyMonth;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String getModel()
    {
        return this.model;
    }

    ElectronicProduct()
    {
        super();
        this.brand = "null";
        this.warrantyMonth = 0;
        this.model = "null";
    }

    ElectronicProduct(String productId, String name, String description, double price, int quantity, String brand, int warrantyMonth, String model)
    {
        super(productId, name, description, price, quantity);
        this.brand = brand;
        this.warrantyMonth = warrantyMonth;
        this.model = model;
    }

    @Override
    public boolean updateStock(int amount)
    {
        return super.updateStock(amount);
    }

    @Override
    public String displayInfo()
    {
        return super.displayInfo() + "\nBrand: " + brand +
                "\nWarranty Month: " + warrantyMonth + "\nModel: " + model +
                "\n" + "-----------------------------" + "\n";
    }
}