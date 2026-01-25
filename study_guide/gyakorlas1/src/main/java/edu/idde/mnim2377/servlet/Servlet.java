package edu.idde.mnim2377.servlet;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.idde.mnim2377.data.dao.DaoFactory;
import edu.idde.mnim2377.data.model.User;
import edu.idde.mnim2377.service.UserService;
import edu.idde.mnim2377.service.UserServiceImpl;
import edu.idde.mnim2377.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class Servlet extends HttpServlet {
    private UserService userService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserServiceImpl(DaoFactory.getInstance().getUserDao());
        mapper = new ObjectMapper();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        try {
            if (req.getParameter("email") != null) {
                User user = userService.getUserByEmail(req.getParameter("email"));
                mapper.writeValue(res.getWriter(), user);
            } else {
                List<User> users = userService.getUsers();
                mapper.writeValue(res.getWriter(), users);
            }
        } catch (ServiceException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getWriter(), e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

