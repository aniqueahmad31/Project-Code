import java.io.*;
import java.util.*;

public class InventoryManager
{
    private List<Product> products;
    private List<Customer> customers;
    private List<Order> orders;
    private String productFile = "products.txt";
    private String orderFile = "orders_history.txt";
    private String dataFile = "orders_data.txt";
    private String customerFile = "customers.txt";
    private String customerViewFile = "customers_view.txt";

    public InventoryManager()
    {
        products = new ArrayList<Product>();
        customers = new ArrayList<Customer>();
        orders = new ArrayList<Order>();
        loadProductsFromFile();
        loadCustomersFromFile();
        loadOrdersFromDataFile();
    }

    // Loading products from file
    public void loadProductsFromFile()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(productFile)))
        {
            String line;

            while ((line = br.readLine()) != null)
            {
                String[] productData = line.split(",");

                if (productData.length == 8)
                {
                    ClothingProduct product = new ClothingProduct(
                            productData[0], productData[1], productData[2],
                            Double.parseDouble(productData[3]), Integer.parseInt(productData[4]),
                            productData[5], productData[6], productData[7]
                    );
                    products.add(product);
                }
                else if (productData.length == 9)
                {
                    ElectronicProduct product = new ElectronicProduct(
                            productData[0], productData[1], productData[2],
                            Double.parseDouble(productData[3]), Integer.parseInt(productData[4]),
                            productData[5], Integer.parseInt(productData[6]), productData[7]
                    );
                    products.add(product);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading products from file: " + e.getMessage());
        }
    }

    // Saving products to file
    public void saveProductsToFile()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(productFile)))
        {
            for (Product p : products)
            {
                if (p instanceof ClothingProduct)
                {
                    ClothingProduct cp = (ClothingProduct) p;
                    bw.write(cp.getProductId() + "," + cp.getName() + "," + cp.getDescription() + "," +
                            cp.getPrice() + "," + cp.getQuantity() + "," + cp.getSize() + "," +
                            cp.getColor() + "," + cp.getFabricType() + "\n");
                }
                else if (p instanceof ElectronicProduct)
                {
                    ElectronicProduct ep = (ElectronicProduct) p;
                    bw.write(ep.getProductId() + "," + ep.getName() + "," + ep.getDescription() + "," +
                            ep.getPrice() + "," + ep.getQuantity() + "," + ep.getBrand() + "," +
                            ep.getWarrantyMonth() + "," + ep.getModel() + "\n");
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }

    // Add product to inventory and save to file
    public void addProduct(Product p)
    {
        products.add(p);
        saveProductsToFile();
    }

    // Remove product from inventory and save to file
    public Product removeProduct(String id)
    {
        for (Product p : products)
        {
            if (p.getProductId().equalsIgnoreCase(id))
            {
                products.remove(p);
                saveProductsToFile();
                return p;
            }
        }
        return null;
    }

    // Display all products
    public String viewAllProducts()
    {
        if (products.isEmpty())
        {
            return "No products available in inventory.\n";
        }

        StringBuilder result = new StringBuilder();

        for (Product p : products)
        {
            // Display product info based on type
            if (p instanceof ElectronicProduct)
            {
                result.append(displayElectronicInfo((ElectronicProduct) p)).append("\n");
            }
            else if (p instanceof ClothingProduct)
            {
                result.append(displayClothingInfo((ClothingProduct) p)).append("\n");
            }

        }

        return result.toString();
    }

    // Search product by ID
    public Product searchProductById(String id)
    {
        for (Product p : products)
        {
            if (p.getProductId().equalsIgnoreCase(id))
            {
                return p;
            }
        }
        return null;
    }

    // Display information for ClothingProduct
    private String displayClothingInfo(ClothingProduct p)
    {
        return "ID: " + p.getProductId() + " | Name: " + p.getName() + " | Desc: " + p.getDescription() +
                " | Price: Rs." + p.getPrice() + "\n | Qty: " + p.getQuantity() + " | Size: " + p.getSize() +
                " | Color: " + p.getColor() + " | Fabric: " + p.getFabricType() +"\n";
    }

    // Display information for ElectronicProduct
    private String displayElectronicInfo(ElectronicProduct p)
    {
        return "ID: " + p.getProductId() + " | Name: " + p.getName() + " | Desc: " + p.getDescription() +
                " | Price: Rs." + p.getPrice() + "\n | Qty: " + p.getQuantity() + " | Brand: " + p.getBrand() +
                " | Warranty: " + p.getWarrantyMonth() + " Months | Model: " + p.getModel() + "\n";
    }

    // Save Every order to history file
    public void saveOrderToHistory(Order o)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderFile, true)))
        {
            bw.write("Order Date: " + new java.util.Date() + "\n");

            bw.write(o.displayOrder());
            bw.write("\n=================================================\n\n");
        }
        catch (IOException e)
        {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    public void updateOrderFile()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderFile)))
        {
            for (Order o : orders)
            {
                bw.write("Order Date: " + new Date() + "\n");
                bw.write(o.displayOrder());
                bw.write("\n=================================================\n\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error updating order file: " + e.getMessage());
        }
    }

    public void saveOrdersToDataFile()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile)))
        {
            for (Order o : orders)
            {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getOrderID()).append(",");
                sb.append(o.getCustomer().getUserID()).append(",");

                List<Product> orderProducts = o.getProducts();
                for (int i = 0; i < orderProducts.size(); i++)
                {
                    Product p = orderProducts.get(i);
                    sb.append(p.getProductId()).append(":").append(p.getQuantity());

                    if (i < orderProducts.size() - 1)
                    {
                        sb.append(";");
                    }
                }
                bw.write(sb.toString() + "\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error saving order data: " + e.getMessage());
        }
    }

    public void loadOrdersFromDataFile()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(",");
                if (data.length == 3)
                {
                    String orderID = data[0];
                    String customerID = data[1];
                    String productsString = data[2];

                    Customer customer = searchCustomerById(customerID);

                    if (customer != null)
                    {
                        Order order = new Order();
                        order.setOrderID(orderID);
                        order.setCustomer(customer);

                        Cart tempCart = new Cart();

                        String[] prodItems = productsString.split(";");
                        for (String item : prodItems)
                        {
                            String[] pData = item.split(":");
                            String pID = pData[0];
                            int qty = Integer.parseInt(pData[1]);

                            Product originalProduct = searchProductById(pID);
                            if (originalProduct != null)
                            {
                                Product copy = null;

                                if(originalProduct instanceof ElectronicProduct) {
                                    ElectronicProduct ep = (ElectronicProduct) originalProduct;
                                    copy = new ElectronicProduct(ep.getProductId(), ep.getName(), ep.getDescription(), ep.getPrice(), qty, ep.getBrand(), ep.getWarrantyMonth(), ep.getModel());
                                } else if(originalProduct instanceof ClothingProduct) {
                                    ClothingProduct cp = (ClothingProduct) originalProduct;
                                    copy = new ClothingProduct(cp.getProductId(), cp.getName(), cp.getDescription(), cp.getPrice(), qty, cp.getSize(), cp.getColor(), cp.getFabricType());
                                }

                                if (copy != null) {
                                    order.addItem(copy);
                                    tempCart.addToCart(copy, qty);
                                    copy.setQuantity(qty);
                                }
                            }
                        }

                        Bill bill = new Bill();
                        bill.calculateDiscount(tempCart);
                        bill.calculateBill(tempCart);
                        order.setBill(bill);

                        orders.add(order);
                    }
                }
            }
        }
        catch (Exception e)
        {
        }
    }

    public void loadCustomersFromFile()
    {
        try (BufferedReader br = new BufferedReader(new FileReader(customerFile)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(",");
                if (data.length >= 3)
                {
                    Customer c = new Customer();
                    c.setUserID(data[0]);
                    c.setEmail(data[1]);
                    c.setPassword(data[2]);

                    if(data.length > 3)
                    {
                        if(data[3].equals("null"))
                            c.setName(null);
                        else
                            c.setName(data[3]);
                    }

                    if(data.length > 4)
                    {
                        if(data[4].equals("null"))
                            c.setAddress(null);
                        else
                            c.setAddress(data[4]);
                    }

                    if(data.length > 5)
                    {
                        if(data[5].equals("null"))
                            c.setPhone(null);
                        else
                            c.setPhone(data[5]);
                    }

                    customers.add(c);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    public void generateCustomerReport()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customerViewFile)))
        {
            bw.write("========== REGISTERED CUSTOMERS LIST ==========\n\n");

            for (Customer c : customers)
            {
                bw.write("User ID  : " + c.getUserID() + "\n");
                bw.write("Name     : " + (c.getName() != null ? c.getName() : "Not Set") + "\n");
                bw.write("Email    : " + c.getEmail() + "\n");
                bw.write("Phone    : " + (c.getPhone() != null ? c.getPhone() : "Not Set") + "\n");
                bw.write("Address  : " + (c.getAddress() != null ? c.getAddress() : "Not Set") + "\n");
                bw.write("-----------------------------------------------\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error generating customer report: " + e.getMessage());
        }
    }

    public void saveCustomersToFile()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customerFile)))
        {
            for (Customer c : customers)
            {
                String name = c.getName();
                if(name == null)
                {
                    name = "null";
                }

                String address = c.getAddress();
                if(address == null)
                {
                    address = "null";
                }

                String phone = c.getPhone();
                if(phone == null)
                {
                    phone = "null";
                }

                bw.write(c.getUserID() + "," + c.getEmail() + "," + c.getPassword() + "," + name + "," + address + "," + phone + "\n");
            }
            generateCustomerReport();
        }
        catch (IOException e)
        {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    // Add customer
    public void addCustomer(Customer c)
    {
        customers.add(c);
        saveCustomersToFile();
    }

    // Find customer by email
    public Customer findCustomerByEmail(String email)
    {
        for (Customer c : customers)
        {
            if (c.getEmail().equalsIgnoreCase(email))
            {
                return c;
            }
        }
        return null;
    }

    // Search customer by ID
    public Customer searchCustomerById(String id)
    {
        for (Customer c : customers)
        {
            if (id.equalsIgnoreCase(c.getUserID()))
            {
                return c;
            }
        }
        return null;
    }

    // Display all customers
    public String viewAllCustomers()
    {
        if (customers.isEmpty())
        {
            return "No customers have registered yet.\n";
        }

        StringBuilder result = new StringBuilder();

        for (Customer c : customers)
        {
            result.append(c.displayUser()).append("\n");
            result.append("---------------------------\n");
        }

        return result.toString();
    }

    // Add order
    public void addOrder(Order o)
    {
        orders.add(o);
        saveOrderToHistory(o);
        saveOrdersToDataFile();
    }

    // Remove order by ID
    public boolean removeOrder(String orderID)
    {
        for (int i = 0; i < orders.size(); i++)
        {
            Order o = orders.get(i);
            if (o.getOrderID().equalsIgnoreCase(orderID))
            {
                // Return products back to inventory
                for (Product p : o.getProducts())
                {
                    Product invProduct = searchProductById(p.getProductId());
                    if (invProduct != null)
                    {
                        invProduct.setQuantity(invProduct.getQuantity() + p.getQuantity());
                    }
                }

                orders.remove(i);
                updateOrderFile();
                saveProductsToFile();
                saveOrdersToDataFile();
                return true;
            }
        }
        return false;
    }

    // View all orders
    public String viewAllOrders()
    {
        if (orders.isEmpty())
        {
            return "No orders have been placed yet.\n";
        }

        StringBuilder result = new StringBuilder();

        for (Order o : orders)
        {
            result.append(o.displayOrder()).append("\n");
        }

        return result.toString();
    }

    // View orders by a specific customer
    public String viewOrdersByCustomer(String userID)
    {
        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (Order o : orders)
        {
            if (o.getCustomer().getUserID().equals(userID))
            {
                result.append(o.displayOrder()).append("\n");
                found = true;
            }
        }

        if (!found)
        {
            result.append("You have not placed any orders yet.\n");
        }

        return result.toString();
    }
}
