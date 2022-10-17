package com.bondegaard.shop.storage.tables;

import com.bondegaard.shop.storage.SqlConnection;
import com.bondegaard.shop.utils.Logger;
import lombok.Getter;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TableHandler {
    private SqlConnection sqlConnection;

    @Getter
    private List<String> tables = new ArrayList<>();

    public TableHandler(SqlConnection sqlConnection) {
        this.sqlConnection = sqlConnection;
        initTables();
    }



    private void initTables() {
        try {
            DatabaseMetaData databaseMetaData = sqlConnection.getConnection().getMetaData();
            ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] {"TABLE"});

            while (resultSet.next())
                tables.add(resultSet.getString("TABLE_NAME"));

            if (!tables.contains("Categories")) createCategories();

        } catch (SQLException ex) {
            Logger.warn("[Shop] Could not initialize SQL Tables");
        }
    }

    private void createCategories() throws SQLException {
        Statement stmt = sqlConnection.getConnection().createStatement();
        String sql = "CREATE TABLE Categories " +
                "(id INTEGER not NULL, " +
                " shops VARCHAR(255), " +
                " gui VARCHAR(255), " +
                " PRIMARY KEY ( id ))";
        stmt.executeUpdate(sql);
        Logger.warn("[Shop] Created Categories Table!");
    }
}
