package edu.bbte.idde.mnim2377.servlet.apiservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/calendars/delete/*")
public class DeleteCalendarById extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteCalendarById.class);

    DaoFactory daoFactory;
    CalendarDao calendarDao;
    CalendarServiceImplementation service;
    ObjectMapper mapper;

    @Override
    public void init() {
        daoFactory = DaoFactory.getInstance();
        calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
        mapper = new ObjectMapper();
        logger.info("DeleteCalendarById servlet initialized");
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        logger.debug("Received request to delete calendar with id: {}", id);
        if (id == null || id.isEmpty()) {
            logger.warn("Request received without id parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing id parameter\"}");
            return;
        }
        resp.setContentType("application/json");
        try {
            service.deleteCalendar(id);
            logger.info("Successfully deleted calendar with id: {}", id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"Successfully deleted calendar: " + id + "\"}");

        } catch (ServiceException e) {
            logger.error("Error deleting calendar with id: {}", id, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Failed to delete calendar: " + e.getMessage() + "\"}");
        }
    }
}
