package edu.bbte.idde.mnim2377.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/calendars")
public class GetAllCalendars extends HttpServlet {
    DaoFactory daoFactory;
    CalendarDao calendarDao;
    CalendarServiceImplementation service;
    ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(GetAllCalendars.class);


    public GetAllCalendars() {
        daoFactory = DaoFactory.getInstance();
        calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    private String getAllCalendarsJSON() {
        List<Calendar> list = service.getAllCalendars();
        String jsonResult = "";
        try {
            jsonResult = mapper.writeValueAsString(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("/calendars endpoint called");
        resp.getWriter().print(getAllCalendarsJSON());
    }
}
