package com.bms.bank_management_system.menuItems;

import com.bms.bank_management_system.UserSession;
import com.bms.bank_management_system.alert;
import com.bms.bank_management_system.dbConnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.*;

public class LoanCreation {

    @FXML
    Button submitBtn, verifyBtn;
    @FXML
    TextField amountField;
    @FXML
    ChoiceBox type, timeType;
    @FXML
    Label uid, rateLabel, emiLabel;
    @FXML
    private PasswordField pinField;
    String[] loanTypes, intrest;
    String userID;
    dbConnection connectDB = new dbConnection();
    Connection connectNow = connectDB.Connect();
    @FXML
    private void initialize(){
        UserSession userSession = UserSession.getInstance();
        userID = userSession.getUserId();
        uid.setText(userID);

        loanTypes = new String[]{"Personal", "Auto", "Home"};
        type.setItems(FXCollections.observableArrayList(loanTypes));

        intrest = new String[]{"3", "6", "9", "12"};
        timeType.setItems(FXCollections.observableArrayList(intrest));
    }


    public void generateAccountNumber(int accountId, String accountNumber) throws SQLException {
        String updateSql = "UPDATE account SET account_number = ? WHERE account_id = ?";
        PreparedStatement updateStmt = connectNow.prepareStatement(updateSql);
        updateStmt.setString(1, accountNumber);
        updateStmt.setInt(2, accountId);
        updateStmt.executeUpdate();
    }

    int accountId;
    public void createAccount() {
        double amount = Double.parseDouble(amountField.getText());
        accountId = 0;

        String accountNumber;
        try {
            PreparedStatement preparedStatement = connectNow.prepareStatement("INSERT INTO account (user_id, account_type, balance) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, Integer.parseInt(userID));
            preparedStatement.setString(2, "loan");
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(amount));
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                accountId = rs.getInt(1);
            }
            accountNumber = "LA" + String.format("%03d", accountId);
            generateAccountNumber(accountId, accountNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        createLoanAcc();
        new alert("Note your Loan Account Number", accountNumber);
    }

    int intrestRate = 0;
    public void getIntrestBtn(){
        String LoanType = (String) type.getValue();
        if(LoanType.equals("Personal")){
            rateLabel.setText("10%");
            intrestRate = 10;
        } else if (LoanType.equals("Auto")) {
            rateLabel.setText("8%");
            intrestRate = 8;
        }else {
            rateLabel.setText("5%");
            intrestRate = 5;
        }
    }

    double amount = 0;
    public void getEMI(){
        String Stime = (String) timeType.getValue();
        int time = Integer.parseInt(Stime);
        int P = Integer.parseInt(amountField.getText());
        amount = (double) ((P * intrestRate * time) /100) + P;
        amount = (double) Math.round(amount * 100) / 100;
        emiLabel.setText(String.valueOf(Math.round(amount/time * 100)/100));
    }

    public void createLoanAcc(){
        String LoanType = (String) type.getValue();
        String Stime = (String) timeType.getValue();
        int time = Integer.parseInt(Stime);
        String P = amountField.getText();

        try{
            PreparedStatement preparedStatement = connectNow.prepareStatement("INSERT INTO loan (account_id, loan_type, amount, interest_rate, start_date, time, monthly_payment) VALUES (?, ?, ?, ?, CURRENT_DATE, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, accountId);
            preparedStatement.setString(2, LoanType);
            preparedStatement.setBigDecimal(3, BigDecimal.valueOf(amount));
            preparedStatement.setInt(4, intrestRate);
            preparedStatement.setInt(5, time);
            preparedStatement.setBigDecimal(6, BigDecimal.valueOf(amount/time));
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
