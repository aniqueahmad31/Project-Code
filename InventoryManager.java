import java.util.ArrayList;
import java.util.List;

public class InventoryManager
{
    private List<Product> products;
    private List<Customer> customers;
    private List<Order> orders;

    InventoryManager()
    {
        products = new ArrayList<Product>();
        customers = new ArrayList<Customer>();
        orders = new ArrayList<Order>();
    }

    public void addProduct(Product p)
    {
        products.add(p);
    }

    public Product removeProduct(String id)
    {
        for(Product p : products)
        {
            if (p.getProductId().equalsIgnoreCase(id))
            {
                products.remove(p);
                return p;
            }
        }
        return null;
    }

    public String viewAllProducts()
    {
        if(products.isEmpty())
        {
            return "No products available in inventory.\n";
        }

        String result = "";
        for (Product p : products)
        {
            result += p.displayInfo() + "\n";
        }
        return result;
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

    public void addCustomer(Customer c)
    {
        customers.add(c);
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
        if(customers.isEmpty())
        {
            return "No customers have registered yet.\n";
        }

        String result = "";
        for (Customer c : customers)
        {
            result += c.displayUser() + "\n";
            result += "---------------------------\n";
        }
        return result;
    }

    public void addOrder(Order o)
    {
        orders.add(o);
    }

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
                return true;
            }
        }
        return false;
    }

    public String viewAllOrders()
    {
        if(orders.isEmpty())
        {
            return "No orders have been placed yet.\n";
        }

        String result = "";
        for (Order o : orders)
        {
            result += o.displayOrder() + "\n";
        }
        return result;
    }

    public String viewOrdersByCustomer(String userID)
    {
        String result = "";
        boolean found = false;
        for (Order o : orders)
        {
            if (o.getCustomer().getUserID().equals(userID))
            {
                result += o.displayOrder() + "\n";
                found = true;
            }
        }

        if (!found)
        {
            result = "You have not placed any orders yet.\n";
        }

        return result;
    }
}
