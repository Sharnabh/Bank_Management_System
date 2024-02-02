package com.bms.bank_management_system.menuItems;

import com.bms.bank_management_system.UserSession;
import com.bms.bank_management_system.alert;
import com.bms.bank_management_system.bmsStarter;
import com.bms.bank_management_system.dbConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

public class AccountCreation {

    @FXML
    Button submitBtn, verifyBtn;
    @FXML
    TextField balanceField;
    @FXML
    ChoiceBox type;
    @FXML
    Label uid;
    @FXML
    private PasswordField pinField;
    String[] accTypes;
    String userID;
    dbConnection connectDB = new dbConnection();
    Connection connectNow = connectDB.Connect();
    @FXML
    private void initialize(){
        UserSession userSession = UserSession.getInstance();
        userID = userSession.getUserId();
        uid.setText(userID);

        accTypes = new String[]{"Savings", "Checking"};
        type.setItems(FXCollections.observableArrayList(accTypes));

    }


    public void generateAccountNumber(int accountId, String accountNumber) throws SQLException {
        String updateSql = "UPDATE account SET account_number = ? WHERE account_id = ?";
        PreparedStatement updateStmt = connectNow.prepareStatement(updateSql);
        updateStmt.setString(1, accountNumber);
        updateStmt.setInt(2, accountId);
        updateStmt.executeUpdate();
    }

    String accountNumber;
    public void createAccount() throws IOException {
        String AccountType = (String) type.getValue();
        double balance = Double.parseDouble(balanceField.getText());
        int accountId = 0;

        try{
            PreparedStatement preparedStatement = connectNow.prepareStatement("INSERT INTO account (user_id, account_type, balance) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, Integer.parseInt(userID));
            preparedStatement.setString(2, AccountType);
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(balance));
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                accountId = rs.getInt(1);
            }
            if(AccountType.equals("Savings")){
                accountNumber = "SA" + String.format("%03d", accountId);
                generateAccountNumber(accountId, accountNumber);
            } else{
                accountNumber = "CA" + String.format("%03d", accountId);
                generateAccountNumber(accountId, accountNumber);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new alert("Note your " + AccountType + " Account Number", accountNumber);

        Stage stage = (Stage) submitBtn.getScene().getWindow();
        stage.close();
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
