package edu.idde.mnim2377.thymeleaf.listener;

import edu.idde.mnim2377.data.dao.DaoFactory;
import edu.idde.mnim2377.data.dao.LoggingConfig;
import edu.idde.mnim2377.data.model.User;
import edu.idde.mnim2377.service.UserService;
import edu.idde.mnim2377.service.UserServiceImpl;
import edu.idde.mnim2377.service.exception.ServiceException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class Weblistener implements ServletContextListener {

    private UserService userService;

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        userService = new UserServiceImpl(DaoFactory.getInstance().getUserDao());

        User testUser = new User("teszt@example.com", 25, "testName");

        try {
            userService.createUser(testUser);
        } catch (ServiceException e) {
            // Any real DB/config issue should be visible, but shouldn't necessarily crash startup
            LoggingConfig.log("Weblistener init failed: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LoggingConfig.log("WAR undeployed – cleanup if needed");
    }
}
