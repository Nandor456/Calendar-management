package edu.idde.mnim2377.thymeleaf;

import edu.idde.mnim2377.data.dao.DaoFactory;
import edu.idde.mnim2377.data.dao.LoggingConfig;
import edu.idde.mnim2377.data.model.User;
import edu.idde.mnim2377.service.UserService;
import edu.idde.mnim2377.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/leaf/users")
public class ThymeleafAPI extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserServiceImpl(DaoFactory.getInstance().getUserDao());
        ThymeleafEngineFactory.buildEngine(getServletContext());

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        List<User> users = userService.getUsers();
        Map<String, Object> model = new ConcurrentHashMap<>();
        model.put("users", users);
        ThymeleafEngineFactory.process(req, res, "index.html", model);
    }
}
