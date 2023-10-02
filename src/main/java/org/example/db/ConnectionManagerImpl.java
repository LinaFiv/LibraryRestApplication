package org.example.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManagerImpl implements ConnectionManager {

    private String url;
    private String username;
    private String password;

    public ConnectionManagerImpl() {
    }

    public ConnectionManagerImpl(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Properties properties = new Properties();

        try (InputStream fileInputStream = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(fileInputStream);
            String driver = (String) properties.get("driver");
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (url == null && username == null && password == null) {
            String url = (String) properties.get("url");
            String username = (String) properties.get("username");
            String password = (String) properties.get("password");
        }

        return DriverManager.getConnection(url, username, password);
    }
}
