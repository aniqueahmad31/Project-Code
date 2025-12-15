package com.example.finalproject_gui.controller;

import com.example.finalproject_gui.model.Customer;
import com.example.finalproject_gui.model.InventoryManager;
import javafx.application.Platform;
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

public class LoginController implements Initializable {

    @FXML private VBox leftMenuVBox;
    @FXML private VBox centerManagerVBox;

    @FXML private TextField managerIdField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private InventoryManager inventory;

    private static final String MANAGER_ID = "M101";
    private static final String MANAGER_PASSWORD = "admin123";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inventory = InventoryManager.getInstance();

        // Ensure centerManagerVBox is hidden initially
        if (centerManagerVBox != null) {
            centerManagerVBox.setVisible(false);
        }
    }

    // MANAGER LOGIN
    @FXML
    private void handleManagerLogin(ActionEvent event) throws IOException {
        // Show the login form if it's hidden
        if (centerManagerVBox != null && !centerManagerVBox.isVisible()) {
            leftMenuVBox.setVisible(false);
            centerManagerVBox.setVisible(true);
            errorLabel.setText("Enter Manager ID and Password.");
            return;
        }

        // Validate credentials
        String managerId = managerIdField.getText();
        String password = passwordField.getText();

        if (managerId.equals(MANAGER_ID) && password.equals(MANAGER_PASSWORD)) {
            // Load Manager Dashboard
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/finalproject_gui/ManagerView.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 700);

            stage.setScene(scene);
            stage.setTitle("Manager Dashboard");
            stage.show();

        } else {
            errorLabel.setText("Invalid Manager ID or Password.");
            managerIdField.clear();
            passwordField.clear();
        }
    }

    // BACK BUTTON TO MAIN MENU
    @FXML
    private void handleBackToMenu(ActionEvent event) {
        if (centerManagerVBox != null) centerManagerVBox.setVisible(false);
        if (leftMenuVBox != null) leftMenuVBox.setVisible(true);

        managerIdField.clear();
        passwordField.clear();
        errorLabel.setText("");
    }

    // CUSTOMER LOGIN
    @FXML
    private void handleCustomerLogin(ActionEvent event) throws IOException {
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Customer Login / Sign Up");
        emailDialog.setHeaderText("Enter your registered email or a new email to sign up.");
        emailDialog.setContentText("Email:");

        Optional<String> result = emailDialog.showAndWait();
        if (result.isPresent()) {
            String emailInput = result.get().trim();
            Customer existingCustomer = inventory.findCustomerByEmail(emailInput);

            if (existingCustomer != null) {
                handleExistingCustomerLogin(event, existingCustomer);
            } else {
                handleNewCustomerSignup(event, emailInput);
            }
        }
    }

    private void handleExistingCustomerLogin(ActionEvent event, Customer customer) throws IOException {
        String enteredPassword;
        boolean authenticated = false;

        while (!authenticated) {
            Dialog<ButtonType> passwordDialog = new Dialog<>();
            passwordDialog.setTitle("Customer Login");
            passwordDialog.setHeaderText("Welcome back, " + customer.getEmail() + "!");

            ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
            ButtonType resetButtonType = new ButtonType("Reset Password", ButtonBar.ButtonData.OTHER);
            passwordDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, resetButtonType, ButtonType.CANCEL);

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Password");

            GridPane grid = new GridPane();
            grid.add(new Label("Password:"), 0, 0);
            grid.add(passwordField, 1, 0);
            passwordDialog.getDialogPane().setContent(grid);

            Platform.runLater(passwordField::requestFocus);

            Optional<ButtonType> result = passwordDialog.showAndWait();

            if (result.isPresent() && result.get() == loginButtonType) {
                enteredPassword = passwordField.getText().trim();

                if (enteredPassword.equals(customer.getPassword())) {
                    authenticated = true;
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Login successful! Welcome.");
                    switchToCustomerView(event, customer, inventory);
                    return;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect password! Try again or use 'Reset Password'.");
                }
            } else if (result.isPresent() && result.get() == resetButtonType) {
                if (resetPassword(customer)) {
                    showAlert(Alert.AlertType.INFORMATION, "Password Reset", "Password changed successfully! Please log in with your new password.");
                }
            } else {
                return;
            }
        }
    }

    private boolean resetPassword(Customer customer) {
        Dialog<ButtonType> resetDialog = new Dialog<>();
        resetDialog.setTitle("Reset Password");
        resetDialog.setHeaderText("Enter your new password (4+ chars).");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        resetDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New Password");

        GridPane grid = new GridPane();
        grid.add(new Label("New Password:"), 0, 0);
        grid.add(newPasswordField, 1, 0);
        resetDialog.getDialogPane().setContent(grid);

        Node confirmButton = resetDialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        newPasswordField.textProperty().addListener((obs, old, newV) ->
                confirmButton.setDisable(newV.trim().length() < 4)
        );

        Optional<ButtonType> result = resetDialog.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            String newPass = newPasswordField.getText().trim();
            if (newPass.length() >= 4) {
                customer.setPassword(newPass);
                inventory.saveCustomersToFile();
                return true;
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Password too short! Reset failed.");
                return false;
            }
        }
        return false;
    }

    private void handleNewCustomerSignup(ActionEvent event, String emailInput) throws IOException {
        Dialog<ButtonType> signupDialog = new Dialog<>();
        signupDialog.setTitle("Customer Sign Up");
        signupDialog.setHeaderText("Welcome! Create your password for email: " + emailInput);

        ButtonType confirmButtonType = new ButtonType("Sign Up", ButtonBar.ButtonData.OK_DONE);
        signupDialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (4+ chars)");

        GridPane grid = new GridPane();
        grid.add(new Label("Create Password:"), 0, 0);
        grid.add(passwordField, 1, 0);
        signupDialog.getDialogPane().setContent(grid);

        Node confirmButton = signupDialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        passwordField.textProperty().addListener((obs, old, newV) ->
                confirmButton.setDisable(newV.trim().length() < 4)
        );

        Optional<ButtonType> result = signupDialog.showAndWait();

        if (result.isPresent() && result.get() == confirmButtonType) {
            String newPass = passwordField.getText().trim();
            if (newPass.length() >= 4) {
                Customer newCustomer = new Customer();
                newCustomer.setEmail(emailInput);
                newCustomer.setUserID(inventory.getNextCustomerId());
                newCustomer.setPassword(newPass);

                inventory.addCustomer(newCustomer);
                showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "Account created! Logging you in.");
                switchToCustomerView(event, newCustomer, inventory);
            } else {
                showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Password too short! Sign Up failed.");
            }
        }
    }

    private void switchToCustomerView(ActionEvent event, Customer customer, InventoryManager manager) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/finalproject_gui/CustomerView.fxml"));
        Parent root = fxmlLoader.load();

        CustomerController customerController = fxmlLoader.getController();
        customerController.initData(customer, manager);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 700);

        stage.setScene(scene);
        stage.setTitle("Customer Dashboard");
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
}
