package edu.bbte.idde.mnim2377.servlet.templateservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.servlet.thymeleaf.ThymeleafEngineFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet({"/view/calendars", "/view/calendars/*"})
public class GetCalendars extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GetCalendars.class);

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
        ThymeleafEngineFactory.buildEngine(getServletContext());
    }

    private void showAllCalendars(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        List<Calendar> list = service.getAllCalendars();
        Map<String, Object> model = new ConcurrentHashMap<>();

        model.put("calendars", list);
        ThymeleafEngineFactory.process(req, resp, "allCalendarsView.html", model);
    }

    private void showSingleCalendar(HttpServletRequest req, HttpServletResponse resp, String id)
            throws IOException {
        Map<String, Object> model = new ConcurrentHashMap<>();

        if (id == null || id.isEmpty()) {
            logger.warn("Request received without id parameter");
            model.put("errorMessage", "No id parameter provided");
            ThymeleafEngineFactory.process(req, resp, "errorView.html", model);
            return;
        }

        logger.debug("Fetching calendar with id: {}", id);
        try {
            Calendar calendar = service.getCalendarById(id);

            logger.info("Successfully retrieved calendar: {}", calendar);

            model.put("calendar", calendar);
            ThymeleafEngineFactory.process(req, resp, "calendarByIdView.html", model);
        } catch (ServiceException e) {
            logger.error("Error while fetching calendar with id: {}", id, e);
            model.put("errorMessage", e);
            ThymeleafEngineFactory.process(req, resp, "errorView.html", model);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            showAllCalendars(req, resp);
        } else {
            String id = pathInfo.substring(1);
            showSingleCalendar(req, resp, id);
        }

    }
}