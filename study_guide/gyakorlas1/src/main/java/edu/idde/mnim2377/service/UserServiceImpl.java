package edu.idde.mnim2377.service;

import edu.idde.mnim2377.data.dao.UserDao;
import edu.idde.mnim2377.data.dao.UserDaoJdbc;
import edu.idde.mnim2377.data.exception.DataException;
import edu.idde.mnim2377.data.model.User;
import edu.idde.mnim2377.service.exception.ServiceException;

import java.util.List;

public class UserServiceImpl implements UserService{
    UserDao userDao;

    public UserServiceImpl(UserDao dao) {
        userDao = dao;
    }
    @Override
    public List<User> getUsers() {
        return userDao.findall();
    }

    @Override
    public User createUser(User user) throws ServiceException {
        try {
            return userDao.create(user);
        } catch (DataException e) {
            throw new ServiceException("Service exc:" + e);
        }
    }

    @Override
    public void updateUser(User user) throws ServiceException {
        try {
            userDao.update(user);
        } catch (DataException e) {
            throw new ServiceException("Service exc:" + e);
        }
    }

    @Override
    public void deleteUser(int id) throws ServiceException {
        try {
            userDao.delete(id);
        } catch (DataException e) {
            throw new ServiceException("Service exc:" + e);
        }
    }

    @Override
    public List<User> getUsersByAge(int x, int y) throws ServiceException {
        try {
            return userDao.findByAge(x, y);
        } catch (DataException e) {
            throw new ServiceException("Service exc:" + e);
        }
    }

    @Override
    public void promoteUser(int id) throws ServiceException {
    }

    @Override
    public User getUserByEmail(String email) throws ServiceException {
        try {
            return userDao.findByEmail(email);
        } catch (DataException e) {
            throw new ServiceException(e);
        }
    }
}
