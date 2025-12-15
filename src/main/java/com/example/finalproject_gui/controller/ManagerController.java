package com.example.finalproject_gui.controller;

import com.example.finalproject_gui.model.ClothingProduct;
import com.example.finalproject_gui.model.ElectronicProduct;
import com.example.finalproject_gui.model.InventoryManager;
import com.example.finalproject_gui.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    @FXML private VBox managerMenuVBox;
    @FXML private TextArea mainDisplayArea;

    // FXML buttons jo FXML file mein hain
    @FXML private Button viewProductsBtn;
    @FXML private Button viewOrdersBtn;
    @FXML private Button viewCustomersBtn;
    @FXML private Button logoutBtn;

    // Existing buttons (Assuming these are still used if no FXML was provided for them)
    @FXML private Button addProductBtn;
    @FXML private Button removeProductBtn;
    @FXML private Button viewReportsBtn;
    @FXML private Button viewAllProductsBtn; // Note: This should ideally be mapped to viewProductsBtn

    private InventoryManager inventory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = InventoryManager.getInstance();
        mainDisplayArea.setText("Welcome to the Manager Dashboard. Use the menu on the left.");
    }

    @FXML
    private void handleViewProducts(ActionEvent event) {
        mainDisplayArea.setText(inventory.viewAllProducts());
    }

    @FXML
    private void handleViewOrders(ActionEvent event) {
        mainDisplayArea.setText("--- ALL ORDER HISTORY ---\n\n" + inventory.viewAllOrders());
    }

    @FXML
    private void handleViewCustomers(ActionEvent event) {
        mainDisplayArea.setText("--- REGISTERED CUSTOMERS ---\n\n" + inventory.viewAllCustomers());
    }

    @FXML
    private void handleViewAllProducts(ActionEvent event) {
        handleViewProducts(event); // Re-use the new method
    }

    @FXML
    private void handleAddProduct(ActionEvent event) {
        Dialog<String> productTypeDialog = new ChoiceDialog<>("Electronic", "Clothing");
        productTypeDialog.setTitle("Add Product");
        productTypeDialog.setHeaderText("Select Product Type:");
        productTypeDialog.setContentText("Type:");

        Optional<String> typeResult = productTypeDialog.showAndWait();

        if (typeResult.isPresent()) {
            String type = typeResult.get();
            try {
                if (type.equals("Electronic")) {
                    showElectronicProductDialog();
                } else {
                    showClothingProductDialog();
                }
                mainDisplayArea.setText("Product added successfully. View All Products to verify.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add product: " + e.getMessage());
            }
        }
    }

    private void showElectronicProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Electronic Product");
        dialog.setHeaderText("Enter details for the new Electronic Product:");

        ButtonType confirmButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField descField = new TextField();
        TextField priceField = new TextField();
        TextField qtyField = new TextField();
        TextField brandField = new TextField();
        TextField warrantyField = new TextField();
        TextField modelField = new TextField();

        grid.addRow(0, new Label("ID:"), idField);
        grid.addRow(1, new Label("Name:"), nameField);
        grid.addRow(2, new Label("Description:"), descField);
        grid.addRow(3, new Label("Price (Rs.):"), priceField);
        grid.addRow(4, new Label("Quantity:"), qtyField);
        grid.addRow(5, new Label("Brand:"), brandField);
        grid.addRow(6, new Label("Warranty (Months):"), warrantyField);
        grid.addRow(7, new Label("Model:"), modelField);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                try {
                    return new ElectronicProduct(
                            idField.getText(), nameField.getText(), descField.getText(),
                            Double.parseDouble(priceField.getText()), Integer.parseInt(qtyField.getText()),
                            brandField.getText(), Integer.parseInt(warrantyField.getText()), modelField.getText()
                    );
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Price, Quantity, or Warranty must be numbers.");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> inventory.addProduct(product));
    }

    private void showClothingProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add Clothing Product");
        dialog.setHeaderText("Enter details for the new Clothing Product:");

        ButtonType confirmButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField descField = new TextField();
        TextField priceField = new TextField();
        TextField qtyField = new TextField();
        TextField sizeField = new TextField();
        TextField colorField = new TextField();
        TextField fabricField = new TextField();

        grid.addRow(0, new Label("ID:"), idField);
        grid.addRow(1, new Label("Name:"), nameField);
        grid.addRow(2, new Label("Description:"), descField);
        grid.addRow(3, new Label("Price (Rs.):"), priceField);
        grid.addRow(4, new Label("Quantity:"), qtyField);
        grid.addRow(5, new Label("Size:"), sizeField);
        grid.addRow(6, new Label("Color:"), colorField);
        grid.addRow(7, new Label("Fabric Type:"), fabricField);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                try {
                    return new ClothingProduct(
                            idField.getText(), nameField.getText(), descField.getText(),
                            Double.parseDouble(priceField.getText()), Integer.parseInt(qtyField.getText()),
                            sizeField.getText(), colorField.getText(), fabricField.getText()
                    );
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Price or Quantity must be numbers.");
                    return null;
                }
            }
            return null;
        });

        Optional<Product> result = dialog.showAndWait();
        result.ifPresent(product -> inventory.addProduct(product));
    }

    @FXML
    private void handleRemoveProduct(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Remove Product");
        dialog.setHeaderText("Enter Product ID to remove:");
        dialog.setContentText("Product ID:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String id = result.get().trim();
            Product p = inventory.removeProduct(id);

            if (p != null) {
                mainDisplayArea.setText("Successfully removed product: " + p.getName() + " (ID: " + id + ")");
            } else {
                mainDisplayArea.setText("Product with ID " + id + " not found.");
            }
        }
    }

    @FXML
    private void handleViewReports(ActionEvent event) {

        Dialog<String> reportTypeDialog = new ChoiceDialog<>("View All Customers", "View All Orders");
        reportTypeDialog.setTitle("View Reports");
        reportTypeDialog.setHeaderText("Select the report you want to view:");
        reportTypeDialog.setContentText("Report Type:");

        Optional<String> result = reportTypeDialog.showAndWait();

        if (result.isPresent()) {
            String selection = result.get();
            if (selection.equals("View All Customers")) {
                handleViewCustomers(event);
            } else if (selection.equals("View All Orders")) {
                handleViewOrders(event);
            }
        } else {
            mainDisplayArea.setText("Report generation cancelled.");
        }
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