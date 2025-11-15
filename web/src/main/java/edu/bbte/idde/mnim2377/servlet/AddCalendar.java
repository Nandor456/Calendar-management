package edu.bbte.idde.mnim2377.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addCalendar")
public class AddCalendar extends HttpServlet {
    DaoFactory daoFactory;
    CalendarDao calendarDao;
    CalendarServiceImplementation service;

    public AddCalendar() {
        daoFactory = DaoFactory.getInstance();
        calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) {

        Calendar calendar;
    }
}
