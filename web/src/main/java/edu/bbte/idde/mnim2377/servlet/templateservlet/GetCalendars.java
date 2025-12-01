package edu.bbte.idde.mnim2377.servlet.templateservlet;

import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceNotFoundException;
import edu.bbte.idde.mnim2377.servlet.thymeleaf.ThymeleafEngineFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet({"/view/calendars", "/view/calendars/*"})
public class GetCalendars extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GetCalendars.class);

    private transient CalendarServiceImplementation service;

    @Override
    public void init() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        CalendarDao calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
        logger.info("GetCalendarById servlet initialized");
        ThymeleafEngineFactory.buildEngine(getServletContext());
    }

    private void showAllCalendars(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        List<Calendar> list = service.getAllCalendars();
        Map<String, Object> model = new ConcurrentHashMap<>();

        model.put("calendars", list);
        ThymeleafEngineFactory.process(req, resp, "calendarsView.html", model);
    }

    private void showSingleCalendar(HttpServletRequest req, HttpServletResponse resp, String idParam)
            throws IOException {
        Map<String, Object> model = new ConcurrentHashMap<>();

        if (idParam == null || idParam.isEmpty()) {
            logger.warn("Request received without id parameter");
            model.put("errorMessage", "No id parameter provided");
            ThymeleafEngineFactory.process(req, resp, "errorView.html", model);
            return;
        }
        UUID id;
        try {
            id = UUID.fromString(idParam);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid UUID format: {}", idParam);
            model.put("errorMessage", "Invalid UUID format: " + idParam);
            ThymeleafEngineFactory.process(req, resp, "errorView.html", model);
            return;
        }
        logger.debug("Fetching calendar with id: {}", id);
        try {
            Calendar calendar = service.getCalendarById(id);
            logger.info("Successfully retrieved calendar: {}", calendar);
            model.put("calendars", calendar);
            ThymeleafEngineFactory.process(req, resp, "calendarsView.html", model);
        } catch (ServiceNotFoundException e) {
            logger.warn("Calendar not found: {}", id);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            model.put("errorMessage", e.getMessage());
            ThymeleafEngineFactory.process(req, resp, "errorView.html", model);
        } catch (ServiceException e) {
            logger.error("Error while fetching calendar with id: {}", id, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            model.put("errorMessage", e.getMessage());
            ThymeleafEngineFactory.process(req, resp, "errorView.html", model);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            showAllCalendars(req, resp);
        } else {
            String idSegment = pathInfo.substring(1);
            showSingleCalendar(req, resp, idSegment);
        }

    }
}