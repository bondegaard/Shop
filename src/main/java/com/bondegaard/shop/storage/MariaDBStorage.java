package com.bondegaard.shop.storage;

import com.bondegaard.shop.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class MariaDBStorage extends SqlConnection {

    private Connection connection;
    @Getter
    private final String host;
    @Getter
    private final String port;
    @Getter
    private final String database;
    @Getter
    private final String username;
    @Getter
    private final String password;
    private final String connectionString;

    public MariaDBStorage()  {

        //Take credentials from config
        FileConfiguration config = Main.getInstance().getConfig();
        this.host = config.getString("database.mariadb.host");
        this.database = config.getString("database.mariadb.database");
        this.username = config.getString("database.mariadb.username");
        this.password = config.getString("database.mariadb.password");
        this.port = config.getString("database.mariadb.port");
        this.connectionString = config.getString("database.mariadb.driver")
                .replace("{HOST}", host)
                .replace("{PORT}", port)
                .replace("{DATABASE}", database);

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
            MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
            dataSource.setPassword(password);
            dataSource.setUser(username);
            dataSource.setUrl(connectionString);

            connection = dataSource.getConnection();
            if (!connection.isValid(1000)) {
                throw new SQLException("Could not establish database connection.");
            }
            System.out.println("[Shop] Successfully connected to MariaDB database");
            System.out.println("[Shop] "+connectionString);

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
            System.out.println("[PremiumCore] Error closing the MariaDB connection");
            err.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

}
