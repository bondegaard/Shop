package com.bondegaard.shop.storage;

import com.bondegaard.shop.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Storage extends SqlConnection {

    private Connection connection;
    @Getter
    private final String driver;


    public H2Storage() {

        //Take credentials from config
        FileConfiguration config = Main.getInstance().getConfig();
        this.driver = config.getString("database.h2.driver");

        //Establish connection
        connect();
        startTimer();
    }


    private void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (connection.isClosed()) {
                        System.out.println("[Shop] Detected the connection is closed!");
                        System.out.println("[Shop] Attempting to reconnect...");
                        connect();
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }


            }
        }.runTaskTimer(Main.getInstance(),0L,20L);
    }


    @Override
    public void connect() {
        try {
            Class.forName(driver);
            String jdbcURL = "jdbc:h2:-/shop";

            Connection connection = DriverManager.getConnection(jdbcURL);

            if (!connection.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
            System.out.println("[Shop] Successfully connected to H2 database");
            System.out.println("[Shop] "+driver);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    @Override
    public void close() {
        try {
            connection.close();
            System.out.println("[Shop] Successfully closed the MariaDB connection");

        } catch (Exception err) {
            System.out.println("[Shop] Error closing the MariaDB connection");
            err.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

}
