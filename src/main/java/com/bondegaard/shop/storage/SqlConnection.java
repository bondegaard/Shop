package com.bondegaard.shop.storage;

import java.sql.Connection;

public abstract class SqlConnection {

    public abstract void connect();

    abstract void close();


    public abstract Connection getConnection();
}
