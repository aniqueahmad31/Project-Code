package com.example.finalproject_gui.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InventoryManager implements Serializable
{
    private static InventoryManager instance;
    private List<Product> products;
    private List<Customer> customers;
    private List<Order> orders;
    private int nextOrderIdCounter = 101;

    // FIXED: Changed from absolute paths to relative paths so it works on any PC
    private String productFile = "products.txt";
    private String orderFile = "orders_history.txt";
    private String dataFile = "orders_data.txt";
    private String customerFile = "customers.txt";
    private String customerViewFile = "customers_view.txt";

    private InventoryManager()
    {
        products = new ArrayList<Product>();
        customers = new ArrayList<Customer>();
        orders = new ArrayList<Order>();
        loadProductsFromFile();
        loadCustomersFromFile();
        loadOrdersFromDataFile();
        this.nextOrderIdCounter = orders.size() + 101;
    }

    public static InventoryManager getInstance()
    {
        if (instance == null)
        {
            instance = new InventoryManager();
        }
        return instance;
    }

    public List<Product> getProducts()
    {
        return products;
    }

    public void loadProductsFromFile()
    {
        products.clear();
        File file = new File(productFile);

        // FIXED: Debugging print to show where it looks for the file
        System.out.println("[DEBUG] Loading products from: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("Error: Product file not found at " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;

            while ((line = br.readLine()) != null)
            {
                // FIXED: added .trim() to handle empty spaces in file
                if(line.trim().isEmpty()) continue;

                String[] productData = line.split(",");

                if (productData.length == 8)
                {
                    try {
                        // FIXED: Added .trim() to all inputs to prevent parsing errors
                        String id = productData[0].trim();
                        String name = productData[1].trim();
                        String desc = productData[2].trim();
                        double price = Double.parseDouble(productData[3].trim());
                        int qty = Integer.parseInt(productData[4].trim());

                        try {
                            // Try parsing as Electronic (Warranty is an int)
                            int warrantyMonth = Integer.parseInt(productData[6].trim());

                            ElectronicProduct product = new ElectronicProduct(
                                    id, name, desc,
                                    price, qty,
                                    productData[5].trim(), // Brand
                                    warrantyMonth,
                                    productData[7].trim() // Model
                            );
                            products.add(product);

                        } catch (NumberFormatException e) {
                            // If parsing fails, it must be Clothing (Color is a String)
                            ClothingProduct product = new ClothingProduct(
                                    id, name, desc,
                                    price, qty,
                                    productData[5].trim(), // Size
                                    productData[6].trim(), // Color
                                    productData[7].trim()  // Fabric
                            );
                            products.add(product);
                        }
                    } catch (Exception e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
            System.out.println("Successfully loaded " + products.size() + " products.");
        }
        catch (IOException e)
        {
            System.err.println("Error reading products from file: " + e.getMessage());
        }
    }

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
            System.err.println("Error saving products to file: " + e.getMessage());
        }
    }

    public void addProduct(Product p)
    {
        products.add(p);
        saveProductsToFile();
    }

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

    public String viewAllProducts()
    {
        if (products.isEmpty())
        {
            return "No products available in inventory.\n";
        }

        StringBuilder result = new StringBuilder();

        for (Product p : products)
        {
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

    public String displayClothingInfo(ClothingProduct p)
    {
        return "ID: " + p.getProductId() + " | Name: " + p.getName() + " | Desc: " + p.getDescription() +
                " | Price: Rs." + p.getPrice() + "\n | Qty: " + p.getQuantity() + " | Size: " + p.getSize() +
                " | Color: " + p.getColor() + " | Fabric: " + p.getFabricType() +"\n";
    }

    public String displayElectronicInfo(ElectronicProduct p)
    {
        return "ID: " + p.getProductId() + " | Name: " + p.getName() + " | Desc: " + p.getDescription() +
                " | Price: Rs." + p.getPrice() + "\n | Qty: " + p.getQuantity() + " | Brand: " + p.getBrand() +
                " | Warranty: " + p.getWarrantyMonth() + " Months | Model: " + p.getModel() + "\n";
    }

    public void saveOrderToHistory(Order o)
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(orderFile, true)))
        {
            bw.write("Order Date: " + new Date() + "\n");

            bw.write(o.displayOrder());
            bw.write("\n=================================================\n\n");
        }
        catch (IOException e)
        {
            System.err.println("Error saving order history: " + e.getMessage());
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
            System.err.println("Error updating order file: " + e.getMessage());
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
            System.err.println("Error saving order data: " + e.getMessage());
        }
    }

    public void loadOrdersFromDataFile()
    {
        File file = new File(dataFile);
        if(!file.exists()) {
            // FIXED: Create file if missing
            try { file.createNewFile(); } catch(IOException e) {}
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] data = line.split(",");
                if (data.length >= 2)
                {
                    String orderID = data[0].trim();
                    String customerID = data[1].trim();
                    String productsString = "";
                    if (data.length > 2) {
                        productsString = data[2].trim();
                    }

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
                            if (item.isEmpty()) continue;
                            String[] pData = item.split(":");
                            // FIXED: check length to avoid crash
                            if(pData.length < 2) continue;

                            String pID = pData[0].trim();
                            int qty = Integer.parseInt(pData[1].trim());

                            Product originalProduct = searchProductById(pID);
                            if (originalProduct != null)
                            {
                                Product copy = null;

                                // Fixed: Removed originalProduct.makeCopy(qty) and restored manual copying logic
                                if(originalProduct instanceof ElectronicProduct) {
                                    ElectronicProduct ep = (ElectronicProduct) originalProduct;
                                    copy = new ElectronicProduct(ep.getProductId(), ep.getName(), ep.getDescription(), ep.getPrice(), qty, ep.getBrand(), ep.getWarrantyMonth(), ep.getModel());
                                } else if(originalProduct instanceof ClothingProduct) {
                                    ClothingProduct cp = (ClothingProduct) originalProduct;
                                    copy = new ClothingProduct(cp.getProductId(), cp.getName(), cp.getDescription(), cp.getPrice(), qty, cp.getSize(), cp.getColor(), cp.getFabricType());
                                }


                                if (copy != null) {
                                    order.addItem(copy);
                                    tempCart.getcartItems().add(copy);
                                }
                            }
                        }

                        Bill bill = new Bill();
                        bill.calculateDiscount(tempCart);
                        double cartTotal = tempCart.calculateTotal();
                        tempCart.setTotalCost(cartTotal);
                        tempCart.setTotalItems(order.getProducts().stream().mapToInt(Product::getQuantity).sum());

                        bill.calculateBill(tempCart);
                        order.setBill(bill);

                        orders.add(order);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("Error loading orders from data file: " + e.getMessage());
        }
    }

    public void loadCustomersFromFile()
    {
        customers.clear();
        File file = new File(customerFile);
        if(!file.exists()) {
            try { file.createNewFile(); } catch(IOException e) {}
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if(line.trim().isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 3)
                {
                    Customer c = new Customer();
                    // FIXED: Added trim to prevent login errors with spaces
                    c.setUserID(data[0].trim());
                    c.setEmail(data[1].trim());
                    c.setPassword(data[2].trim());

                    if(data.length > 3)
                    {
                        String val = data[3].trim();
                        c.setName(val.equals("null") ? null : val);
                    }

                    if(data.length > 4)
                    {
                        String val = data[4].trim();
                        c.setAddress(val.equals("null") ? null : val);
                    }

                    if(data.length > 5)
                    {
                        String val = data[5].trim();
                        c.setPhone(val.equals("null") ? null : val);
                    }

                    customers.add(c);
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("Error loading customers: " + e.getMessage());
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
            System.err.println("Error generating customer report: " + e.getMessage());
        }
    }

    public void saveCustomersToFile()
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(customerFile)))
        {
            for (Customer c : customers)
            {
                String name = c.getName() == null ? "null" : c.getName();
                String address = c.getAddress() == null ? "null" : c.getAddress();
                String phone = c.getPhone() == null ? "null" : c.getPhone();

                bw.write(c.getUserID() + "," + c.getEmail() + "," + c.getPassword() + "," + name + "," + address + "," + phone + "\n");
            }
            generateCustomerReport();
        }
        catch (IOException e)
        {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    public void addCustomer(Customer c)
    {
        customers.add(c);
        saveCustomersToFile();
    }

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

    public void addOrder(Order o)
    {
        orders.add(o);
        nextOrderIdCounter = orders.size() + 101;
        saveOrderToHistory(o);
        saveOrdersToDataFile();
    }

    public boolean removeOrder(String orderID)
    {
        for (int i = 0; i < orders.size(); i++)
        {
            Order o = orders.get(i);
            if (o.getOrderID().equalsIgnoreCase(orderID))
            {
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

    public int getNextOrderIdCounter() {
        return nextOrderIdCounter;
    }

    public String getNextCustomerId()
    {
        int maxId = 100;
        for (Customer c : customers) {
            try {
                if (c.getUserID().startsWith("C")) {
                    int id = Integer.parseInt(c.getUserID().substring(1));
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            } catch (NumberFormatException ignored) {}
        }
        return "C" + (maxId + 1);
    }
}