package edu.idde.mnim2377.data.dao;

import edu.idde.mnim2377.data.exception.DataException;
import edu.idde.mnim2377.data.model.User;
import edu.idde.mnim2377.database.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserDaoJdbc implements UserDao{


    @Override
    public List<User> findall() throws DataException{
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
        ) {
            if (resultSet == null) {
                throw new DataException("No user found");
            }
            while (resultSet.next()) {
                users.add(toModel(resultSet));
            }
            LoggingConfig.log("users: {}", users);
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(int id) throws DataException {
        return null;
    }

    @Override
    public User findByEmail(String email) throws DataException {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return toModel(resultSet);
            } else {
                throw new DataException("User not found with email: " + email);
            }
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }

    @Override
    public User create(User user) throws DataException {
        String sql = "INSERT INTO users (id, email, age, name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, user.getID());
            pstmt.setString(2, user.getEmail());
            pstmt.setInt(3, user.getAge());
            pstmt.setString(4, user.getName());
            pstmt.executeUpdate();
            LoggingConfig.log("User created successfully: {}", user);

            return user;
        } catch (SQLException e) {
            throw new DataException("Cant create User:" + e);
        }
    }

    @Override
    public void update(User user) throws DataException {

    }

    @Override
    public void delete(int id) throws DataException {

    }

    @Override
    public List<User> findByAge(int x, int y) throws DataException {
        String sql = "SELECT * FROM users WHERE age >= ? AND age <= ?";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ) {
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            ResultSet resultSet = preparedStatement.executeQuery();

            LoggingConfig.log("listed users by age");
            while (resultSet.next()) {
                users.add(toModel(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new DataException("Can't list users by age" + e);
        }
    }

    @Override
    public void deleteUserByEmail(String email) throws DataException {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection conn = DatabaseConfig.getDataSource().getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);)
        {
            preparedStatement.setString(1, email);
            int row = preparedStatement.executeUpdate();
            if (row == 0) {
                throw new DataException("Cannot find user with this email");
            }
            LoggingConfig.log("User delete successfully by email");
        }
         catch (SQLException e) {
            throw new DataException("Cant delete User by email" + e);
        }
    }

    private User toModel(ResultSet set) throws SQLException {
        return new User(
                set.getInt("id"),
                set.getString("email"),
                set.getInt("age"),
                set.getString("name")
        );
    }
}
