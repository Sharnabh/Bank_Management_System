package com.bms.bank_management_system;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;

import javafx.scene.control.MenuItem;

import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Main {
    @FXML
    public MenuItem logOutMenu;
    dbConnection connectDB = new dbConnection();
    Connection connectNow = connectDB.Connect();
    private String userId;


    @FXML
    private void initialize(){
        UserSession userSession = UserSession.getInstance();
        userId = userSession.getUserId();

        logOutMenu.setOnAction(event -> {
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(bmsStarter.class.getResource("userLogin.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Bank");
                stage.setScene(scene);
                stage.show();

                Stage thisStage = (Stage) logOutMenu.getParentPopup().getOwnerWindow();
                thisStage.close();
                userSession.setUserId(null);

            } catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        });
    }

    private void createStage(String file, String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(bmsStarter.class.getResource("menuItems/" + file));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setScene(scene);
        stage.show();
    }

    public void setAccountMenu() throws IOException {
        createStage("accountCreation.fxml", "Create Account");
    }
    public void setLoanMenu() throws IOException {
        createStage("loanCreation.fxml", "Apply for Loan");
    }
    public void setDepositMenu() throws IOException {
        createStage("deposit.fxml", "Deposit");
    }
    public void setWithdrawMenu() throws IOException {
        createStage("withdraw.fxml", "Withdraw");
    }
    public void setTransferMenu() throws IOException {
        createStage("transfer.fxml", "Transfer Funds");
    }
    public void setSavingsMenu() throws IOException {
        createStage("balanceCheck.fxml", "Transfer Funds");
    }


}
