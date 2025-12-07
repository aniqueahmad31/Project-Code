
import java.util.Scanner;

public class OnlineShopApp
{
    public static int getValidInt(Scanner input)
    {
        while (true)
        {
            try
            {
                return Integer.parseInt(input.nextLine().trim());
            }
            catch (Exception e)
            {
                System.out.print("Invalid Input! Enter a number: ");
            }
        }
    }

    static int orderIdCounter = 100;
    static int userIdCounter = 100;

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        InventoryManager inventory = new InventoryManager();
        Manager manager = new Manager("M101", "admin123", inventory);
        Cart cart = new Cart();
        Customer currentCustomer = null;
        Order customerOrder = null;
        Bill bill = null;
        Product product;

        // Sample products
        ClothingProduct p1 = new ClothingProduct("P101","Men’s Cotton Shirt","Casual light blue cotton",1500,15,"M","Blue","Cotton Shirt");
        inventory.addProduct(p1);
        ElectronicProduct p2 = new ElectronicProduct("P201","Dell Inspiron Laptop","Core i5, 8GB RAM, 512GB SSD",95000,5,"Dell",12,"Inspiron 15");
        inventory.addProduct(p2);

        int choice;

        System.out.println("Welcome to Online Shop!\n");

        do
        {
            System.out.println("==== MAIN MENU ====");
            System.out.println("1. Customer Login / Sign Up");
            System.out.println("2. Manager Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = getValidInt(input);
            System.out.println();

            if (choice == 1)
            {
                System.out.print("Enter Email: ");
                String emailInput = input.nextLine().trim();
                Customer existingCustomer = inventory.findCustomerByEmail(emailInput);

                if (existingCustomer != null)
                {
                    currentCustomer = existingCustomer;
                    System.out.println("\nWelcome Back " + existingCustomer.getName() + "!");

                    while (true)
                    {
                        System.out.print("Enter Password: ");
                        String pass = input.nextLine().trim();
                        if(pass.equals(existingCustomer.getPassword()))
                        {
                            System.out.println();
                            break;
                        }
                        else
                        {
                            System.out.println("Incorrect password! Try again.");
                        }
                    }
                }
                else
                {
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(emailInput);
                    newCustomer.setUserID("C" + userIdCounter);
                    userIdCounter++;

                    System.out.print("\nWelcome! Please create your password: ");
                    newCustomer.setPassword(input.nextLine().trim());

                    inventory.addCustomer(newCustomer);
                    currentCustomer = newCustomer;

                    System.out.println();
                }

                // Customer Menu
                int customerChoice;
                do
                {
                    System.out.println("==== AVAILABLE PRODUCTS ====");
                    System.out.println(inventory.viewAllProducts());

                    System.out.println("==== CUSTOMER MENU ====");
                    System.out.println("1. Add Products To Cart");
                    System.out.println("2. Remove Products From Cart");
                    System.out.println("3. View My Cart");
                    System.out.println("4. Place Order");
                    System.out.println("5. View My Orders");
                    System.out.println("6. Remove Order");
                    System.out.println("7. Forgot Password");
                    System.out.println("8. Logout");
                    System.out.print("Enter your choice: ");
                    customerChoice = getValidInt(input);
                    System.out.println();

                    if(customerChoice == 1)
                    {
                        System.out.print("Enter Product ID to purchase: ");
                        String id = input.nextLine();
                        product = inventory.searchProductById(id);
                        if(product != null)
                        {
                            System.out.println(product.getName() + " | Rs." + product.getPrice());
                            System.out.print("Enter quantity: ");
                            int qty = getValidInt(input);
                            if(cart.addToCart(product, qty))
                            {
                                System.out.println(product.getName() + " added to cart!");
                                System.out.println();
                            }
                            else
                            {
                                System.out.println("Not enough quantity in inventory.");
                                System.out.println();
                            }
                        }
                        else
                        {
                            System.out.println("Product not found!");
                        }
                    }
                    else if(customerChoice == 2)
                    {
                        System.out.print("Enter Product ID to remove: ");
                        String id = input.nextLine();
                        System.out.print("Enter quantity to remove: ");
                        int qty = getValidInt(input);
                        Product invProduct = inventory.searchProductById(id);
                        if(invProduct != null)
                        {
                            System.out.println(cart.removeFromCart(id, qty, invProduct));
                            System.out.println();
                        }
                        else
                        {
                            System.out.println("Product not found in inventory.");
                            System.out.println();
                        }
                    }
                    else if(customerChoice == 3)
                    {
                        System.out.println(cart.viewCart());
                    }
                    else if(customerChoice == 4)
                    {
                        if(cart.getcartItems().isEmpty())
                        {
                            System.out.println("Cart empty! Cannot place order.");
                        }
                        else
                        {
                            if(currentCustomer.hasDetails())
                            {
                                System.out.print("Do you want to use your previous details? (yes/no): ");
                                String usePrev = input.nextLine().trim().toLowerCase();
                                if(usePrev.equals("no"))
                                {
                                    System.out.println("Please Write Following Details! ");
                                    while(true)
                                    {
                                        try
                                        {
                                            System.out.print("Name: ");
                                            currentCustomer.setName(input.nextLine());
                                            break;
                                        }
                                        catch(IllegalArgumentException e){System.out.println(e.getMessage());}
                                    }
                                    while(true)
                                    {
                                        try
                                        {
                                            System.out.print("Address: ");
                                            currentCustomer.setAddress(input.nextLine());
                                            break;
                                        }
                                        catch(IllegalArgumentException e){System.out.println(e.getMessage());}
                                    }
                                    while(true)
                                    {
                                        try
                                        {
                                            System.out.print("Phone: ");
                                            currentCustomer.setPhone(input.nextLine());
                                            break;
                                        }
                                        catch(IllegalArgumentException e){System.out.println(e.getMessage());}
                                    }
                                }
                                else
                                {
                                    System.out.println("Using previous details.");
                                }
                            }
                            else
                            {
                                // Customer details not set yet, input them
                                System.out.println("Please Write Following Details! ");
                                while(true)
                                {
                                    try
                                    {
                                        System.out.print("Name: ");
                                        currentCustomer.setName(input.nextLine());
                                        break;
                                    }
                                    catch(IllegalArgumentException e){System.out.println(e.getMessage());}
                                }
                                while(true)
                                {
                                    try
                                    {
                                        System.out.print("Address: ");
                                        currentCustomer.setAddress(input.nextLine());
                                        break;
                                    }
                                    catch(IllegalArgumentException e){System.out.println(e.getMessage());}
                                }
                                while(true)
                                {
                                    try
                                    {
                                        System.out.print("Phone: ");
                                        currentCustomer.setPhone(input.nextLine());
                                        break;
                                    }
                                    catch(IllegalArgumentException e){System.out.println(e.getMessage());}
                                }
                            }

                            customerOrder = new Order();

                            Customer orderCustomer = new Customer();
                            orderCustomer.setUserID(currentCustomer.getUserID());
                            orderCustomer.setName(currentCustomer.getName());
                            orderCustomer.setEmail(currentCustomer.getEmail());
                            orderCustomer.setAddress(currentCustomer.getAddress());
                            orderCustomer.setPhone(currentCustomer.getPhone());
                            orderCustomer.setPassword(currentCustomer.getPassword());

                            customerOrder.setCustomer(orderCustomer);
                            customerOrder.getProducts().addAll(cart.getcartItems());


                            bill = new Bill();
                            bill.calculateDiscount(cart);
                            bill.calculateBill(cart);
                            customerOrder.setBill(bill);
                            customerOrder.setOrderID("Order" + orderIdCounter);
                            orderIdCounter++;

                            inventory.addOrder(customerOrder);
                            cart.clearCart();

                            System.out.println("Order Confirmed! Your Order ID: " + customerOrder.getOrderID() + "\n");
                        }
                    }
                    else if(customerChoice == 5)
                    {
                        System.out.print("Enter your password to view orders: ");
                        String pass = input.nextLine().trim();
                        if(pass.equals(currentCustomer.getPassword()))
                        {
                            System.out.println(inventory.viewOrdersByCustomer(currentCustomer.getUserID()));
                        }
                        else
                        {
                            System.out.println("Incorrect password!.");
                            System.out.println();
                        }
                    }
                    else if(customerChoice == 6)
                    {
                        System.out.print("Enter Order ID to remove: ");
                        String id = input.nextLine();
                        if(inventory.removeOrder(id))
                            System.out.println("Order removed!");
                        else
                            System.out.println("Order not found!");
                    }
                    else if (customerChoice == 7)
                    {
                        System.out.print("Enter your registered email: ");
                        String forgotEmail = input.nextLine().trim();
                        Customer cust = inventory.findCustomerByEmail(forgotEmail);

                        if(cust != null)
                        {
                            while(true)
                            {
                                System.out.print("Enter your new password: ");
                                String newPass = input.nextLine().trim();

                                System.out.print("Confirm your new password: ");
                                String confirmPass = input.nextLine().trim();

                                if(newPass.equals(confirmPass))
                                {
                                    cust.setPassword(newPass);
                                    System.out.println("Password updated successfully!");
                                    System.out.println();
                                    break;
                                }
                                else
                                {
                                    System.out.println("Passwords do not match. Try again.");
                                }
                            }
                        }
                        else
                        {
                            System.out.println("Email not found in our records!");
                            System.out.println();
                        }

                    }
                } while(customerChoice != 8);

            }
            else if(choice == 2)
            {
                System.out.print("Enter Manager ID: ");
                String id = input.nextLine();
                System.out.print("Enter Password: ");
                String pass = input.nextLine();

                if(manager.login(id, pass))
                {
                    int managerChoice;
                    do
                    {
                        // Manager Menu
                        System.out.println("==== MANAGER MENU ====");
                        System.out.println("1. View All Products");
                        System.out.println("2. View All Orders");
                        System.out.println("3. View All Customers");
                        System.out.println("4. Logout");
                        System.out.print("Enter your choice: ");
                        managerChoice = getValidInt(input);

                        if(managerChoice == 1)
                            System.out.println(manager.viewAllProducts());
                        else if(managerChoice == 2)
                            System.out.println(manager.viewAllOrders());
                        else if(managerChoice == 3)
                            System.out.println(manager.viewAllCustomers());
                    } while(managerChoice != 4);

                    System.out.println("Manager Logged Out!\n");
                }
                else
                {
                    System.out.println("Invalid Manager ID or Password.\n");
                }
            }

        } while(choice != 3);

        System.out.println("Thank You For Visiting!");
        input.close();
    }
}
