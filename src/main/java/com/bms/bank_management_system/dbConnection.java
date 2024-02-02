package com.bms.bank_management_system;

import java.sql.Connection;
import java.sql.DriverManager;
import io.github.cdimascio.dotenv.Dotenv;

public class dbConnection {

    public Connection databaseLink;

    public Connection Connect(){
        Dotenv dotenv = Dotenv.load();
        String db_name = dotenv.get("DB_NAME");
        String db_user = dotenv.get("DB_USER");
        String db_pass = dotenv.get("DB_PASSWORD");
        String url = "jdbc:mysql://localhost/" + db_name;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, db_user, db_pass);
        } catch (Exception e){
            e.printStackTrace();;
            e.getCause();
        }
        return databaseLink;
    }


}
