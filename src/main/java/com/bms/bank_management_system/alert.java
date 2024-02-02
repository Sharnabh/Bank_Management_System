package com.bms.bank_management_system;

import javafx.scene.control.Alert;

public class alert {
    public  alert(String head ,String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(head);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    public  alert(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }
}
