package edu.bbte.idde.mnim2377.servlet.templateservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/logout")
public class Logout extends HttpServlet {

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse resp)
            throws java.io.IOException {
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/view/login");
    }
}
