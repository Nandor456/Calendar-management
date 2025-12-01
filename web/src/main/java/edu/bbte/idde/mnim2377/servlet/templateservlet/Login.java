package edu.bbte.idde.mnim2377.servlet.templateservlet;

import edu.bbte.idde.mnim2377.servlet.thymeleaf.ThymeleafEngineFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/view/login")
public class Login extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    @Override
    public void init() {
        ThymeleafEngineFactory.buildEngine(getServletContext());
        logger.info("Login servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        logger.debug("GET /login - Displaying login form");

        Map<String, Object> model = new ConcurrentHashMap<>();
        ThymeleafEngineFactory.process(req, resp, "login.html", model);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        logger.debug("POST /login - Processing login");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Map<String, Object> model = new ConcurrentHashMap<>();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            logger.warn("Login attempt with empty credentials");
            model.put("error", "Username and password are required");
            ThymeleafEngineFactory.process(req, resp, "login.html", model);
            return;
        }

        if (authenticateUser(username, password)) {
            logger.info("Successful login for user: {}", username);

            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("loggedIn", true);

            resp.sendRedirect(req.getContextPath() + "/view/calendars");
        } else {
            logger.warn("Failed login attempt for user: {}", username);
            model.put("error", "Invalid username or password");
            model.put("username", username);
            model.put("password", "");
            ThymeleafEngineFactory.process(req, resp, "login.html", model);
        }
    }

    private boolean authenticateUser(String username, String password) {
        return "admin".equals(username) && "password".equals(password);
    }
}
