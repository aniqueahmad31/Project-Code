package com.example.finalproject_gui.controller;

import com.example.finalproject_gui.model.Cart;
import com.example.finalproject_gui.model.Customer;
import com.example.finalproject_gui.model.InventoryManager;
import com.example.finalproject_gui.model.Product;
import com.example.finalproject_gui.model.ElectronicProduct;
import com.example.finalproject_gui.model.ClothingProduct;
import com.example.finalproject_gui.model.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML private VBox customerMenuVBox;
    @FXML private VBox customerDetailsVBox;
    @FXML private TextArea mainDisplayArea;
    @FXML private Label welcomeLabel;

    @FXML private Button viewProductsBtn;
    @FXML private Button searchProductBtn;
    @FXML private Button addToCartBtn;
    @FXML private Button viewCartBtn;
    @FXML private Button checkoutBtn;
    @FXML private Button viewOrdersBtn;
    @FXML private Button logoutBtn;
    @FXML private Button removeFromCartBtn;

    private Customer currentCustomer;
    private InventoryManager inventory;
    private Cart currentCart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void initData(Customer customer, InventoryManager manager) {
        this.currentCustomer = customer;
        this.inventory = manager;
        this.currentCart = new Cart();
        welcomeLabel.setText("Welcome, " + (customer.getName() != null ? customer.getName() : customer.getEmail()) + "!");

        handleViewProducts(null);
    }

    @FXML
    private void handleViewProducts(ActionEvent event) {
        mainDisplayArea.setText(inventory.viewAllProducts());
    }

    @FXML
    private void handleSearchProduct(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Product");
        dialog.setHeaderText("Enter Product ID to search:");
        dialog.setContentText("Product ID:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String id = result.get().trim();
            Product p = inventory.searchProductById(id);

            if (p != null) {
                if (p instanceof ElectronicProduct) {
                    mainDisplayArea.setText("Product Found:\n" + inventory.displayElectronicInfo((ElectronicProduct) p));
                } else if (p instanceof ClothingProduct) {
                    mainDisplayArea.setText("Product Found:\n" + inventory.displayClothingInfo((ClothingProduct) p));
                }
            } else {
                mainDisplayArea.setText("Product with ID " + id + " not found.");
            }
        }
    }

    @FXML
    private void handleAddToCart(ActionEvent event) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Add to Cart");
        idDialog.setHeaderText("Enter Product ID:");
        idDialog.setContentText("ID:");
        Optional<String> idResult = idDialog.showAndWait();

        if (idResult.isPresent()) {
            String id = idResult.get().trim();
            Product p = inventory.searchProductById(id);

            if (p == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product ID not found.");
                return;
            }

            TextInputDialog qtyDialog = new TextInputDialog("1");
            qtyDialog.setTitle("Add to Cart");
            qtyDialog.setHeaderText("Enter Quantity for " + p.getName() + ": (Max: " + p.getQuantity() + ")");
            qtyDialog.setContentText("Quantity:");
            Optional<String> qtyResult = qtyDialog.showAndWait();

            if (qtyResult.isPresent()) {
                try {
                    int qty = Integer.parseInt(qtyResult.get().trim());
                    if (qty <= 0 || qty > p.getQuantity()) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity or out of stock.");
                        return;
                    }

                    currentCart.addToCart(p, qty);
                    showAlert(Alert.AlertType.INFORMATION, "Success", qty + "x " + p.getName() + " added to cart.");

                    handleViewCart(null);

                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity input.");
                }
            }
        }
    }

    @FXML
    private void handleRemoveFromCart(ActionEvent event) {
        if (currentCart.getcartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Remove Failed", "Your cart is already empty.");
            return;
        }

        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Remove From Cart");
        idDialog.setHeaderText("Enter Product ID to remove:");
        idDialog.setContentText("ID:");
        Optional<String> idResult = idDialog.showAndWait();

        if (idResult.isPresent()) {
            String id = idResult.get().trim();
            Product inventoryProduct = inventory.searchProductById(id);

            if (inventoryProduct == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Product ID not found in inventory.");
                return;
            }

            int currentCartQty = currentCart.getcartItems().stream()
                    .filter(p -> p.getProductId().equalsIgnoreCase(id))
                    .mapToInt(Product::getQuantity)
                    .sum();

            if (currentCartQty == 0) {
                showAlert(Alert.AlertType.WARNING, "Error", "Product " + id + " is not in your cart.");
                return;
            }

            TextInputDialog qtyDialog = new TextInputDialog("1");
            qtyDialog.setTitle("Remove From Cart");
            qtyDialog.setHeaderText("Enter Quantity to remove for " + inventoryProduct.getName() + ": (Max in Cart: " + currentCartQty + ")");
            qtyDialog.setContentText("Quantity:");
            Optional<String> qtyResult = qtyDialog.showAndWait();

            if (qtyResult.isPresent()) {
                try {
                    int qty = Integer.parseInt(qtyResult.get().trim());

                    if (qty <= 0 || qty > currentCartQty) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity to remove.");
                        return;
                    }

                    String message = currentCart.removeFromCart(id, qty, inventoryProduct);

                    showAlert(Alert.AlertType.INFORMATION, "Success", message);

                    handleViewCart(null);

                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid quantity input.");
                }
            }
        }
    }

    @FXML
    private void handleViewCart(ActionEvent event) {
        if (currentCart.getcartItems().isEmpty()) {
            mainDisplayArea.setText("Your cart is empty.");
        } else {
            mainDisplayArea.setText(currentCart.viewCart());
        }
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        if (currentCart.getcartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Checkout Failed", "Your cart is empty. Please add items first.");
            return;
        }

        // --- STEP 1: Handle Customer Details ---
        boolean useExistingDetails = false;
        if (currentCustomer.hasDetails()) {
            Alert detailsConfirm = new Alert(Alert.AlertType.CONFIRMATION);
            detailsConfirm.setTitle("Delivery Details");
            detailsConfirm.setHeaderText("Use existing details for delivery?");
            detailsConfirm.setContentText("Current Details:\nName: " + currentCustomer.getName() + "\nAddress: " + currentCustomer.getAddress() + "\nPhone: " + currentCustomer.getPhone());

            ButtonType yesButton = new ButtonType("Use Existing");
            ButtonType newButton = new ButtonType("Enter New");
            detailsConfirm.getButtonTypes().setAll(yesButton, newButton, ButtonType.CANCEL);

            Optional<ButtonType> detailsResult = detailsConfirm.showAndWait();

            if (detailsResult.isPresent()) {
                if (detailsResult.get() == yesButton) {
                    useExistingDetails = true;
                } else if (detailsResult.get() == newButton) {
                    // Proceed to collect new details below
                } else {
                    return; // Cancel checkout
                }
            } else {
                return; // Cancel checkout
            }
        }

        if (!useExistingDetails) {
            if (!collectCustomerDetails()) {
                return; // Cancelled or failed to input details
            }
        }

        // Save the updated/confirmed details
        inventory.saveCustomersToFile();

        // --- STEP 2: Final Checkout Confirmation ---
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Final Confirmation");
        confirmAlert.setHeaderText("Confirm Order: Total Cost Rs." + String.format("%.2f", currentCart.calculateTotal()));
        confirmAlert.setContentText("Delivery Address: " + currentCustomer.getAddress() + "\nPhone: " + currentCustomer.getPhone() + "\n\nDo you want to proceed with the purchase?");

        Optional<ButtonType> finalResult = confirmAlert.showAndWait();

        if (finalResult.isPresent() && finalResult.get() == ButtonType.OK) {
            try {
                Order newOrder = currentCart.checkout(currentCustomer, inventory);

                mainDisplayArea.setText("--- CHECKOUT COMPLETE ---\n\n" + newOrder.displayOrder() + "\n\nThank you for your purchase!");

                handleViewProducts(null);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Checkout Error", "An error occurred during checkout: " + e.getMessage());
            }
        }
    }

    /**
     * Helper method to collect Name, Address, and Phone in sequence using TextInputDialogs.
     * @return true if details were successfully collected, false if cancelled.
     */
    private boolean collectCustomerDetails() {
        // Collect Name
        Optional<String> nameResult = showDetailDialog("Name", "Enter your Full Name:", currentCustomer.getName());
        if (!nameResult.isPresent()) return false;
        try {
            currentCustomer.setName(nameResult.get().trim());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", e.getMessage());
            return collectCustomerDetails(); // Retry name
        }

        // Collect Address
        Optional<String> addressResult = showDetailDialog("Address", "Enter your Delivery Address:", currentCustomer.getAddress());
        if (!addressResult.isPresent()) return false;
        try {
            currentCustomer.setAddress(addressResult.get().trim());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", e.getMessage());
            return collectCustomerDetails(); // Retry address (resets name too, simple implementation)
        }

        // Collect Phone
        Optional<String> phoneResult = showDetailDialog("Phone", "Enter your Phone Number (e.g., 03xxxxxxxxx):", currentCustomer.getPhone());
        if (!phoneResult.isPresent()) return false;
        try {
            currentCustomer.setPhone(phoneResult.get().trim());
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", e.getMessage());
            return collectCustomerDetails(); // Retry phone (resets all, simple implementation)
        }

        showAlert(Alert.AlertType.INFORMATION, "Details Saved", "Delivery details updated successfully.");
        return true;
    }

    private Optional<String> showDetailDialog(String title, String header, String currentValue) {
        TextInputDialog dialog = new TextInputDialog(currentValue != null && !currentValue.equals("null") ? currentValue : "");
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(title + ":");
        return dialog.showAndWait();
    }


    @FXML
    private void handleViewOrders(ActionEvent event) {
        mainDisplayArea.setText("--- YOUR ORDER HISTORY ---\n\n" + inventory.viewOrdersByCustomer(currentCustomer.getUserID()));
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/finalproject_gui/LoginView.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 700, 500);

        stage.setScene(scene);
        stage.setTitle("Login / Register");
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}