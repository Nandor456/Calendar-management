package edu.bbte.idde.mnim2377.servlet.apiservlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/calendars/update/*")
public class UpdateCalendarById extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(UpdateCalendarById.class);

    private transient CalendarServiceImplementation service;
    private transient ObjectMapper mapper;
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
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String id = req.getParameter("id");
        res.setContentType("application/json"); // Use setContentType instead of setHeader
        logger.debug("Received request to update calendar with id: {}", id);
        if (id == null || id.isEmpty()) {
            logger.warn("Request received without id parameter");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Missing id parameter\"}");
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
                    res.getWriter().write("{\"error\": \"Validation failed: " + errors + "\"}");
                    return;
                }
            }

            Calendar calendar = service.getCalendarById(id);
            if (calendar == null) {
                logger.warn("Calendar with id {} not found", id);
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().write("{\"error\": \"Calendar not found with id: " + id + "\"}");
                return;
            }

            Calendar newCalendar = new Calendar(calendar.getId(), data.address, data.location, data.date, data.online);
            service.updateCalendar(newCalendar);

            logger.info("Successfully updated calendar with id: {}", id);
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().write("{\"message\": \"Successfully updated calendar: " + id + "\"}");
        } catch (ServiceException e) {
            logger.error("Error updating calendar with id: {}", id, e);
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().write("{\"error\": \"Failed to update calendar: " + e + "\"}");
        } catch (JsonMappingException e) {
            // JSON structure issues
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Invalid JSON structure\"}");
            logger.error("JSON mapping error", e);
        } catch (JsonParseException e) {
            // Malformed JSON
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Malformed JSON\"}");
            logger.error("JSON parse error", e);
        }
    }
}
