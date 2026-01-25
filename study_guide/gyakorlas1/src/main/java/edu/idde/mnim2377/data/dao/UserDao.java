package edu.idde.mnim2377.data.dao;

import edu.idde.mnim2377.data.exception.DataException;
import edu.idde.mnim2377.data.model.User;
import java.util.List;

public interface UserDao {
 List<User> findall() throws DataException;
 User findById(int id) throws DataException;
 User findByEmail(String email) throws DataException;
 User create(User user) throws DataException;
 void update(User user) throws DataException;
 void delete(int id) throws DataException;
 List<User> findByAge(int x, int y) throws DataException;
 void deleteUserByEmail(String email) throws DataException;
}
