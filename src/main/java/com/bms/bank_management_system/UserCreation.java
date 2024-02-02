package com.bms.bank_management_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserCreation {

    @FXML
    private PasswordField passField, cpassField, pinField, cpinField;
    @FXML
    private TextField fnameField, lnameField, emailField;
    @FXML
    private Button regBtn, toLoginBtn;

    public void setRegBtn() {
        if(passField.getText().equals(cpassField.getText()) && (!passField.getText().isBlank() && !cpassField.getText().isBlank()) &&
                pinField.getText().equals(cpinField.getText()) && (!pinField.getText().isBlank() && !cpinField.getText().isBlank())){
            if(isValidEmail(emailField.getText()) && isFourDigits(pinField.getText())){
                registerUser();
            } else if (!isFourDigits(pinField.getText())) {
                new alert("Enter valid Pin of length 4");
            } else
                new alert("Enter valid Email");
        } else if (fnameField.getText().isBlank() || lnameField.getText().isBlank() || emailField.getText().isBlank()) {
            new alert("Fields cannot be Blank !");
        } else if (passField.getText().isBlank() || cpassField.getText().isBlank()) {
            new alert("Password cannot be Blank !");
        } else if (pinField.getText().isBlank() || cpinField.getText().isBlank()) {
            new alert("Pin cannot be Blank !");
        } else{
            new alert("Password or Pin doesn't match");
        }
    }

    public void registerUser(){
        String Fname = fnameField.getText();
        String lname = lnameField.getText();
        String email = emailField.getText();
        String password = passField.getText();
        String pin = pinField.getText();

        dbConnection connectDB = new dbConnection();
        Connection connectNow = connectDB.Connect();

        String insertPrompt = "INSERT INTO user(first_name, last_name, email, password, pin_code) VALUES";
        String valuePrompt = "('" + Fname + "', '" + lname + "', '" + email + "', '" + password + "', '" + pin + "')";
        String command = insertPrompt + valuePrompt;

        String insert = "SELECT user_id FROM user WHERE email = '"+ email + "'";
        String uid = "";

        try {
            Statement statement = connectNow.createStatement();
            statement.executeUpdate(command);
            ResultSet rs =statement.executeQuery(insert);
            if(rs.next()){
                uid = rs.getString(1);
            }
            new alert("User creation Successful.", "PLease note the user ID : " + uid);
            setToLoginBtn();
        } catch (SQLException e) {
            new alert("Email already Registered");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isFourDigits(String number) {
        String regex = "^[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public void setToLoginBtn() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("userLogin.fxml"));
        Stage stage = (Stage) toLoginBtn.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}
