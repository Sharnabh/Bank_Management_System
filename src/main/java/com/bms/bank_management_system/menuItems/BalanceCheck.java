package com.bms.bank_management_system.menuItems;

import com.bms.bank_management_system.UserSession;
import com.bms.bank_management_system.alert;
import com.bms.bank_management_system.dbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class BalanceCheck {

    @FXML
    Button submitBtn, verifyBtn;
    @FXML
    TextField acNoField;
    @FXML
    private PasswordField pinField;
    @FXML
    Label uid;
    String userID;
    dbConnection connectDB = new dbConnection();
    Connection connectNow = connectDB.Connect();
    @FXML
    private void initialize(){
        UserSession userSession = UserSession.getInstance();
        userID = userSession.getUserId();
        uid.setText(userID);
    }

    public boolean search( String acNo){
        try {
            Statement statement = connectNow.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(1) FROM account WHERE account_number = '" + acNo + "'");
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            if (count == 0) {
                return false;
            }
            else return true;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOutput(String acNo){
        try {
            Statement statement = connectNow.createStatement();
            ResultSet rs = statement.executeQuery("SELECT balance FROM account WHERE account_number = '" + acNo + "'");
            int output = 0;
            if (rs.next()) {
                output = rs.getInt(1);
            }
            return String.valueOf(output);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void depositAccount(){
        String acNo = acNoField.getText();

        if (!search(acNo)){
            new alert("Account not Found");
        }else{
            new alert("Your Current Balance", getOutput(acNo));

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        }

    }
    public void verify() {
        int pin = Integer.parseInt(pinField.getText());
        int count;
        try {
            Statement statement = connectNow.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(1) FROM user WHERE user_id = " + userID + " AND pin_code = " + pin);
            count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (count == 1) {
            submitBtn.setDisable(false);
            verifyBtn.setDisable(true);
        }else {
            new alert("Enter Valid Pin");
        }

    }

}
