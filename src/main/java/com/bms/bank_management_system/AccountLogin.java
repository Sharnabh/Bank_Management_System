package com.bms.bank_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccountLogin {

    @FXML
    private TextField emailField, userField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button loginBtn, toRegBtn;
    dbConnection connectDB = new dbConnection();
    Connection connect = connectDB.Connect();

    public boolean validate(){

        String verifyLogin = "SELECT count(1) FROM user WHERE user_id = '" + userField.getText() + "' AND password = '" + passField.getText() + "' AND email = '" + emailField.getText() + "'";

        try{
            Statement statement = connect.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    UserSession us = UserSession.getInstance();
                    us.setUserId(userField.getText());
                    return true;
                }else{
                    new alert("Invalid Credentials");
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }

    public void setLoginBtn() throws IOException {
        if(!userField.getText().isEmpty() && !emailField.getText().isEmpty() && !passField.getText().isEmpty()){
            if(validate()){
                Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
                Stage stage = (Stage) loginBtn.getScene().getWindow();
                stage.setFullScreen(true);
                stage.setScene(new Scene(root));
            }
        }
        else{
            new alert("Enter Username and Password");
        }
    }

    public void setToRegBtn() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("userCreation.fxml"));
        Stage stage = (Stage) toRegBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
