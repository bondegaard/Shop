package com.bondegaard.shop.storage.tables;

import com.bondegaard.shop.Main;
import com.bondegaard.shop.storage.SqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriesTable {

    private SqlConnection sqlConnection;
    private Connection connection;

    public CategoriesTable() {
        sqlConnection = Main.getInstance().getSqlConnection();
        connection = sqlConnection.getConnection();
    }

    //Checks if player is created in the database
    public boolean playerWithUuidExists(String uuid) {
        try {
            //Connection is closed.
            if (connection.isClosed())
                sqlConnection.connect();
            //Query all players with that UUID.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE uuid=?");
            statement.setString(1,uuid);
            ResultSet results = statement.executeQuery();
            //If results has a value, then the player exists
            if (results.next()) return true;

        } catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }
    public boolean playerWithNameExists(String username) {
        try {
            //Connection is closed.
            if (connection.isClosed())
                sqlConnection.connect();
            //Query all players with that UUID.
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE username=?");
            statement.setString(1,username);
            ResultSet results = statement.executeQuery();
            //If results has a value, then the player exists
            if (results.next()) return true;

        } catch (Exception err) {
            err.printStackTrace();
        }
        return false;
    }
    public void createPlayer(String uuid, String username) {
        try {
            if (connection.isClosed())
                sqlConnection.connect();

            if (!playerWithUuidExists(uuid)) {
                PreparedStatement insert = connection.prepareStatement("INSERT INTO `players` (`uuid`,`username`) VALUES (?,?)");
                insert.setString(1, uuid);
                insert.setString(2, username);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
