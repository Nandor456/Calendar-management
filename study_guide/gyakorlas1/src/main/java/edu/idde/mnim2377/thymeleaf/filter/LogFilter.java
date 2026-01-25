package edu.idde.mnim2377.thymeleaf.filter;

import edu.idde.mnim2377.data.dao.LoggingConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")  // Minden URL-en lefut
public class LogFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String method = req.getMethod();
        String uri = req.getRequestURI();

        LoggingConfig.log(method + " - " + uri);

        chain.doFilter(req, res);
    }
}


