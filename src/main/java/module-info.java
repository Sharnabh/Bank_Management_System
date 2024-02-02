module com.bms.bank_management_system {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires mysql.connector.j;

    opens com.bms.bank_management_system to javafx.fxml;
    exports com.bms.bank_management_system;
    exports com.bms.bank_management_system.menuItems;
    opens com.bms.bank_management_system.menuItems to javafx.fxml;
}