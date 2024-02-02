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

import java.math.BigDecimal;
import java.sql.*;

public class Withdraw {

    @FXML
    Button submitBtn, verifyBtn;
    @FXML
    TextField acNoField, amountField;
    @FXML
    Label uid;
    @FXML
    private PasswordField pinField;
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
            ResultSet rs = statement.executeQuery("SELECT COUNT(1) FROM account WHERE account_number = '" + acNo + "' AND user_id = " + Integer.parseInt(userID));
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count != 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOutput(String acNo, String out){
        try {
            Statement statement = connectNow.createStatement();
            ResultSet rs = statement.executeQuery("SELECT " + out + " FROM account WHERE account_number = '" + acNo + "'");
            int output = 0;
            if (rs.next()) {
                output = rs.getInt(1);
            }
            return String.valueOf(output);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    int accountId = 0;
    public void withdrawAccount(){
        String amount = amountField.getText();
        String acNo = acNoField.getText();
        int check = Integer.parseInt(getOutput(acNo, "balance"));

        if (!search(acNo)){
            new alert("Account not Found");
        } else if (check <= Integer.parseInt(amount)) {
            new alert("Not Sufficient Funds");
        } else{
            try{
                PreparedStatement preparedStatement = connectNow.prepareStatement("UPDATE account SET balance = balance - ? WHERE account_number = ?");
                preparedStatement.setInt(1, Integer.parseInt(amount));
                preparedStatement.setString(2, acNo);
                preparedStatement.executeUpdate();

                accountId = Integer.parseInt(getOutput(acNo, "account_id"));

                setTransaction(Integer.parseInt(amount));

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            new alert("Your Current Balance", getOutput(acNo, "balance"));

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.close();
        }

    }

    public void setTransaction(int amount){

        try{
            PreparedStatement preparedStatement = connectNow.prepareStatement("INSERT INTO transaction (account_id, transaction_type, amount) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, accountId);
            preparedStatement.setString(2, "withdrawal");
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(amount));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
