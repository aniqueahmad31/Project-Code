package com.example.finalproject_gui.model;

import java.util.ArrayList;
import java.util.List;

public class Cart
{
    private List<Product> cartItems;
    private int totalItems;
    private double totalCost;

    public List<Product> getcartItems()
    {
        return cartItems;
    }

    public void setTotalItems(int totalItems)
    {
        this.totalItems = totalItems;
    }

    public int getTotalItems()
    {
        return totalItems;
    }

    public void setTotalCost(double totalCost)
    {
        this.totalCost = totalCost;
    }

    public double getTotalCost()
    {
        return totalCost;
    }

    public Cart()
    {
        cartItems = new ArrayList<>();
        totalItems = 0;
        totalCost = 0.0;
    }

    public double calculateTotal() {
        double total = 0.0;
        for (Product p : cartItems) {
            total += p.getPrice() * p.getQuantity();
        }
        return total;
    }

    public boolean addToCart(Product product, int selectedQuantity)
    {
        if (product == null)
        {
            return false;
        }

        if (selectedQuantity > 0 && selectedQuantity <= product.getQuantity())
        {
            Product cartProduct;

            if (product instanceof ClothingProduct)
            {
                ClothingProduct cp = (ClothingProduct) product;
                cartProduct = new ClothingProduct(cp.getProductId(), cp.getName(),
                        cp.getDescription(), cp.getPrice(), selectedQuantity,
                        cp.getSize(), cp.getColor(), cp.getFabricType());
            }
            else if (product instanceof ElectronicProduct)
            {
                ElectronicProduct ep = (ElectronicProduct) product;
                cartProduct = new ElectronicProduct(ep.getProductId(), ep.getName(),
                        ep.getDescription(), ep.getPrice(), selectedQuantity,
                        ep.getBrand(), ep.getWarrantyMonth(), ep.getModel());
            }
            else
            {
                cartProduct = new Product(product.getProductId(), product.getName(),
                        product.getDescription(), product.getPrice(), selectedQuantity);
            }

            cartItems.add(cartProduct);
            totalItems += selectedQuantity;
            totalCost += product.getPrice() * selectedQuantity;
            product.updateStock(selectedQuantity);

            return true;
        }
        else
        {
            return false;
        }
    }

    public String removeFromCart(String id, int removeQuantity, Product inventoryProduct)
    {
        boolean found = false;
        String message = "";

        for (int i = 0; i < cartItems.size(); i++)
        {
            Product p = cartItems.get(i);
            if (p.getProductId().equalsIgnoreCase(id))
            {
                found = true;

                if (removeQuantity > 0 && removeQuantity <= p.getQuantity())
                {
                    p.setQuantity(p.getQuantity() - removeQuantity);

                    if (p.getQuantity() == 0)
                    {
                        cartItems.remove(i);
                        i--;
                    }

                    totalItems -= removeQuantity;
                    totalCost -= p.getPrice() * removeQuantity;
                    inventoryProduct.setQuantity(inventoryProduct.getQuantity() + removeQuantity);

                    message = id + " | " + p.getName() + " | Rs." + p.getPrice()
                            + " | Quantity Removed: " + removeQuantity + " from your cart.";
                }
                else
                {
                    message = "Invalid quantity to remove.";
                }
                break;
            }
        }

        if (!found)
        {
            message = "Product " + id + " not found in your cart.";
        }

        return message;
    }

    public String viewCart()
    {
        String result = "";

        if (cartItems.isEmpty())
        {
            result = "Your Cart is Empty.";
        }
        else
        {
            for (Product p : cartItems)
            {
                result += p.getName() + " | Rs." + p.getPrice()
                        + " | Quantity: " + p.getQuantity() + "\n\n";
            }

            result += "Total Items in Cart: " + totalItems + "\n";
            result += "Total Cost: " + calculateTotal() + "\n";
        }

        return result;
    }

    public void clearCart()
    {
        cartItems.clear();
        totalItems = 0;
        totalCost = 0.0;
    }

    // Fix for CustomerController checkout error
    public Order checkout(Customer customer, InventoryManager inventory) throws IllegalArgumentException {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cannot checkout an empty cart.");
        }

        Order newOrder = new Order();
        newOrder.setOrderID("O" + inventory.getNextOrderIdCounter());
        newOrder.setCustomer(customer);

        Bill bill = new Bill();

        for (Product item : cartItems) {
            newOrder.addItem(item);
        }

        bill.calculateDiscount(this);
        bill.calculateBill(this);
        newOrder.setBill(bill);

        inventory.addOrder(newOrder);

        clearCart();

        return newOrder;
    }
}