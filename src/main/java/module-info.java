module com.example.finalproject_gui {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.finalproject_gui;
    opens com.example.finalproject_gui to javafx.fxml;

    exports com.example.finalproject_gui.controller;
    opens com.example.finalproject_gui.controller to javafx.fxml;

    exports com.example.finalproject_gui.model;
}