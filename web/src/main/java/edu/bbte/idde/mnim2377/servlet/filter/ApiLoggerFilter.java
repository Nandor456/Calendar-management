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

@WebFilter("/*")
public class ApiLoggerFilter extends HttpFilter {
    private static final Logger logger = LoggerFactory.getLogger(ApiLoggerFilter.class);

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String method = req.getMethod();
        String uri = req.getRequestURI();

        logger.info("{} {}", method, uri);

        chain.doFilter(req, res);
    }
}
