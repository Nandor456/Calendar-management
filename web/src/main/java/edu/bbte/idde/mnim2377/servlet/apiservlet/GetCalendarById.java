package edu.bbte.idde.mnim2377.servlet.apiservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/calendarById")
public class GetCalendarById extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GetCalendarById.class);

    DaoFactory daoFactory;
    CalendarDao calendarDao;
    CalendarServiceImplementation service;
    ObjectMapper mapper;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
        logger.info("GetCalendarById servlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.debug("/calendarById/:id endpoint called");
        String id = req.getParameter("id");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (id == null || id.isEmpty()) {
            logger.warn("Request received without id parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("{\"error\": \"Missing id parameter\"}");
            return;
        }

        logger.debug("Fetching calendar with id: {}", id);

        try {
            Calendar calendar = service.getCalendarById(id);

            logger.info("Successfully retrieved calendar: {}", calendar);
            String jsonResult = mapper.writeValueAsString(calendar);
            resp.getWriter().print(jsonResult);

        } catch (ServiceException e) {
            logger.error("Error while fetching calendar with id: {}", id, e);
            resp.setStatus(404);
            resp.getWriter().print("{\"error\": \"Not Found\"}");
        }
    }
}