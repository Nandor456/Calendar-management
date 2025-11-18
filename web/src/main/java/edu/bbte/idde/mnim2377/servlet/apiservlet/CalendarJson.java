package edu.bbte.idde.mnim2377.servlet.apiservlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.backend.service.exception.ServiceException;
import edu.bbte.idde.mnim2377.servlet.dto.RequestCalendar;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/calendars")
public class CalendarJson extends HttpServlet {
    private transient CalendarServiceImplementation service;
    private transient ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(CalendarJson.class);
    private transient Validator validator;


    @Override
    public void init() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        CalendarDao calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            logger.info("Validator initialized successfully");
        } catch (ValidationException e) {
            logger.error("Failed to initialize validator", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        if (id == null) {
            logger.debug("/calendars endpoint called");
            // write the actual list as JSON using the mapper
            mapper.writeValue(resp.getWriter(), service.getAllCalendars());
            return;
        }

        logger.debug("Fetching calendar with id: {}", id);

        try {
            Calendar calendar = service.getCalendarById(id);

            logger.info("Successfully retrieved calendar: {}", calendar);
            // write the calendar object directly as JSON
            mapper.writeValue(resp.getWriter(), calendar);

        } catch (ServiceException e) {
            logger.error("Error while fetching calendar with id: {}", id, e);
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(), "Not Found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        logger.debug("Received request to delete calendar with id: {}", id);
        if (id == null || id.isEmpty()) {
            logger.warn("Request received without id parameter");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(resp.getWriter(), "Missing id parameter");
            return;
        }
        resp.setContentType("application/json");
        try {
            service.deleteCalendar(id);
            logger.info("Successfully deleted calendar with id: {}", id);
            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), "Successfully deleted calendar: " + id);

        } catch (ServiceException e) {
            logger.error("Error deleting calendar with id: {}", id, e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(resp.getWriter(), "Failed to delete calendar: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String id = req.getParameter("id");
        res.setContentType("application/json");
        logger.debug("Received request to update calendar with id: {}", id);
        if (id == null || id.isEmpty()) {
            logger.warn("Request received without id parameter");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getWriter(), "Missing id parameter");
            return;
        }
        try {
            RequestCalendar data = mapper.readValue(req.getInputStream(), RequestCalendar.class);

            if (validator != null) {
                Set<ConstraintViolation<RequestCalendar>> violations = validator.validate(data);
                if (!violations.isEmpty()) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    String errors = violations.stream()
                            .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                            .collect(Collectors.joining(", "));
                    mapper.writeValue(res.getWriter(), "Validation failed: " + errors);
                    return;
                }
            }

            Calendar calendar = service.getCalendarById(id);
            if (calendar == null) {
                logger.warn("Calendar with id {} not found", id);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                mapper.writeValue(res.getWriter(), "Calendar not found with id: " + id);
                return;
            }

            Calendar newCalendar = new Calendar(calendar.getId(), data.address, data.location, data.date, data.online);
            service.updateCalendar(newCalendar);

            logger.info("Successfully updated calendar with id: {}", id);
            res.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(res.getWriter(), "Successfully updated calendar: " + id);
        } catch (ServiceException e) {
            logger.error("Error updating calendar with id: {}", id, e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            mapper.writeValue(res.getWriter(), "Failed to update calendar: " + e.getMessage());
        } catch (JsonMappingException e) {
            // JSON structure issues
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getWriter(), "Invalid JSON structure");
            logger.error("JSON mapping error", e);
        } catch (JsonParseException e) {
            // Malformed JSON
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getWriter(), "Malformed JSON");
            logger.error("JSON parse error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");

        try {
            RequestCalendar data = mapper.readValue(req.getInputStream(), RequestCalendar.class);

            if (validator != null) {
                Set<ConstraintViolation<RequestCalendar>> violations = validator.validate(data);
                if (!violations.isEmpty()) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    mapper.writeValue(res.getWriter(), "Validation error: "
                            + violations.iterator().next().getMessage());
                    return;
                }
            }

            logger.debug("Adding new calendar with data: {}", data);
            Calendar calendar = new Calendar(data.address, data.location, data.date, data.online);
            service.addCalendar(calendar);
            res.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(res.getWriter(), "Calendar created successfully");

        } catch (MismatchedInputException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String field = "unknown";
            if (e.getPath() != null && !e.getPath().isEmpty() && e.getPath().getFirst() != null) {
                field = e.getPath().getFirst().getFieldName();
            }
            mapper.writeValue(res.getWriter(), "Invalid data type for field: "
                    + field + ". " + e.getOriginalMessage());
            logger.error("Type mismatch error", e);
        } catch (JsonMappingException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getWriter(), "Invalid JSON structure: " + e.getOriginalMessage());
            logger.error("JSON mapping error", e);
        } catch (JsonParseException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            mapper.writeValue(res.getWriter(), "Malformed JSON: " + e.getOriginalMessage());
            logger.error("JSON parse error", e);
        }
    }
}
