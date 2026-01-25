package edu.idde.mnim2377.service;

import edu.idde.mnim2377.data.model.User;
import edu.idde.mnim2377.service.exception.ServiceException;

import java.util.List;

public interface UserService {
    //CRUD
    List<User> getUsers();
    User getUserByEmail(String email) throws ServiceException;
    User createUser(User user) throws ServiceException;

    void updateUser(User user) throws ServiceException;
    void deleteUser(int id) throws ServiceException;

    //BusinessLogic
    void promoteUser(int id) throws ServiceException;

    List<User> getUsersByAge(int x, int y) throws ServiceException;
}
