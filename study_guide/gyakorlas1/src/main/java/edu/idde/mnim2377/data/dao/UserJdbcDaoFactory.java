package edu.idde.mnim2377.data.dao;

public class UserJdbcDaoFactory extends DaoFactory{
    private UserDao userDao;

    @Override
    public UserDao getUserDao() {
        if (userDao == null) {
            return new UserDaoJdbc();
        }
        return userDao;
    }
}
