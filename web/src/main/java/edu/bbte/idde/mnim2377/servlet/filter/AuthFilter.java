package edu.bbte.idde.mnim2377.servlet.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter("/view/*")
public class AuthFilter extends HttpFilter {
    Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        logger.debug("AuthFilter: Checking user authentication.");
        String path = req.getRequestURI();
        if (path.endsWith("/view/login")) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("loggedIn") == null) {
            res.sendRedirect(req.getContextPath() + "/view/login");
            return;
        }
        logger.debug("User is authenticated, proceeding to requested resource.");
        chain.doFilter(req, res);
    }
}
