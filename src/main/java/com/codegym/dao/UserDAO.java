package com.codegym.dao;

import com.codegym.model.User;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "admin";

    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?;";
    private static final String SELECT_ALL_USERS = "select * from users;";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    private static final String SEARCH_BY_COUNTRY = "select * from users where country like ? '%'";
    private static final String SORT_BY_NAME = "select * from users order by name";
    private static  final String SQL_INSERT = "insert into employee (name, salary, created_date) values(?, ?, ?)";
    private static final String SQL_UPDATE = "update employee set salary = ? where name = ?";
    private static final String SQL_TABLE_CREATE = "create table employee" +
            "(" +
            "id serial, " +
            "name varchar(100) not null, " +
            "salary numeric(15, 2) not null," +
            "created_date timestamp," +
            "primary key (id)" +
            ")";
    private static final String SQL_TABLE_DROP = "drop table if exists employee";
    public UserDAO() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insertUser(User user) {
        System.out.println(INSERT_USERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();
             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        // using try-with-resources to avoid closing resources (boiler plate code)
        List<User> users = new ArrayList<>();
        // Step 1: Establishing a Connection
        try (Connection connection = getConnection();

             // Step 2:Create a statement using connection object

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getCountry());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    @Override
    public List<User> searchByCountry(String country) {
        List<User> newUser = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BY_COUNTRY)) {
            preparedStatement.setString(1, country);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country1 = resultSet.getString("country");
                User user = new User(id, name, email, country1);
                newUser.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUser;
    }

    @Override
    public List<User> sortBYName() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SORT_BY_NAME)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                User user = new User(id, name, email, country);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public User getUserById(int id) {
        User user = null;
        String query = "{call get_user_by_id(?)}";
        try (Connection connection = getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    @Override
    public void insertUserStore(User user) {
        String query = "{call insert_user(?,?,?)}";
        try (Connection connection = getConnection();
             CallableStatement callableStatement = connection.prepareCall(query)) {
            callableStatement.setString(1, user.getName());
            callableStatement.setString(2, user.getEmail());
            callableStatement.setString(3, user.getCountry());
            System.out.printf("da tao moi");
            callableStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void addUserTransaction(User user, int[] permission) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatementAssignment = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(INSERT_USERS_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            int rowAffected = preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            System.out.println("rs: "+resultSet.getInt(1));
            System.out.println("rs: "+resultSet.getInt(0));
            System.out.println("rs: "+resultSet.getInt(2));
            int userId = 0;
            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }

            if (rowAffected == 1) {
                String sqlPivot = "insert into user_permision(user_id, permision_id)" + "values(?,?)";
                preparedStatementAssignment = connection.prepareStatement(sqlPivot);
                for (int permisionId : permission) {
                    preparedStatementAssignment.setInt(1, userId);
                    preparedStatementAssignment.setInt(2, permisionId);
                    preparedStatementAssignment.executeUpdate();
                }
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException ex) {
            try {
                if (connection != null)

                    connection.rollback();

            } catch (SQLException e) {

                System.out.println(e.getMessage());

            }

            System.out.println(ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (preparedStatementAssignment != null) {
                    preparedStatementAssignment.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void insertUpdateWithoutTransaction() {
        try (Connection connection = getConnection();
        Statement statement = connection.createStatement();
        PreparedStatement preparedStatementInsert = connection.prepareStatement(SQL_INSERT);
        PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE)){
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);

            preparedStatementInsert.setString(1, "quynh");
            preparedStatementInsert.setBigDecimal(2, new BigDecimal(10));
            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatementInsert.execute();

            preparedStatementInsert.setString(1, "ngan");
            preparedStatementInsert.setBigDecimal(2, new BigDecimal(20));
            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatementInsert.execute();

            preparedStatementUpdate.setBigDecimal(2, new BigDecimal("999.99"));
            preparedStatementUpdate.setString(2, "quynh");
            preparedStatementUpdate.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUpdateUseTransaction() {
        try (Connection connection = getConnection();

             Statement statement = connection.createStatement();

             PreparedStatement preparedStatementInsert = connection.prepareStatement(SQL_INSERT);

             PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE)) {

            statement.execute(SQL_TABLE_DROP);

            statement.execute(SQL_TABLE_CREATE);

            // start transaction block

            connection.setAutoCommit(false); // default true

            // Run list of insert commands

            preparedStatementInsert.setString(1, "Quynh");

            preparedStatementInsert.setBigDecimal(2, new BigDecimal(10));

            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatementInsert.execute();



            preparedStatementInsert.setString(1, "Ngan");

            preparedStatementInsert.setBigDecimal(2, new BigDecimal(20));

            preparedStatementInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            preparedStatementInsert.execute();



            // Run list of update commands



            // below line caused error, test transaction

            // org.postgresql.util.PSQLException: No value specified for parameter 1.

            preparedStatementUpdate.setBigDecimal(1, new BigDecimal(999.99));



            //preparedStatementUpdate.setBigDecimal(1, new BigDecimal(999.99));

            preparedStatementUpdate.setString(2, "Quynh");

            preparedStatementUpdate.execute();



            // end transaction block, commit changes

            connection.commit();

            // good practice to set it back to default true

            connection.setAutoCommit(false);



        } catch (Exception e) {

            System.out.println(e.getMessage());

            e.printStackTrace();

        }
    }
}
