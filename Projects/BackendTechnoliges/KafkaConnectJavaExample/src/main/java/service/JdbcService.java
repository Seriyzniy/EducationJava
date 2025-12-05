package service;

import model.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcService {
    private final String DB_URL = "jdbc:postgresql://localhost:5532/testdb";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "postgres";

    private Connection connection;

    public static void main(String[] args) {
        JdbcService service = new JdbcService();

        int resultSqlCode = service.saveUserInfo(
                new UserInfo("Hizenberg", 22));

        if(resultSqlCode != 0) {
            System.out.println("Success save user!");
        } else {
            System.out.println("Error save user! The count of changes row = " +resultSqlCode);
        }
    }


    public JdbcService() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            throw new RuntimeException(e);
        }
    }

    public int saveUserInfo(UserInfo userInfo) {
        try{
            connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Get connection to Postgres error");
            throw new RuntimeException(e);
        }

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO userinfo(uname, age) VALUES(?,?)");

            preparedStatement.setString(1, userInfo.getUname());
            preparedStatement.setInt(2, userInfo.getAge());


            return preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Update error");
            throw new RuntimeException(e);
        }
    }
}
