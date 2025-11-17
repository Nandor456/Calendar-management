package edu.bbte.idde.mnim2377.servlet.apiservlet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.bbte.idde.mnim2377.backend.data.dao.CalendarDao;
import edu.bbte.idde.mnim2377.backend.data.dao.DaoFactory;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import edu.bbte.idde.mnim2377.backend.service.CalendarServiceImplementation;
import edu.bbte.idde.mnim2377.servlet.dto.RequestCalendar;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

@WebServlet("/addCalendar2")
public class AddCalendar extends HttpServlet {
    DaoFactory daoFactory;
    CalendarDao calendarDao;
    CalendarServiceImplementation service;
    ObjectMapper mapper;
    ValidatorFactory factory;
    Validator validator;
    private static final Logger logger = LoggerFactory.getLogger(AddCalendar.class);



    @Override
    public void init() {
        logger.info("Initializing AddCalendar servlet");
        //validator = factory.getValidator();
        daoFactory = DaoFactory.getInstance();
        calendarDao = daoFactory.getCalendarDao();
        service = new CalendarServiceImplementation(calendarDao);
        try {
            factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
            logger.info("Validator initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize validator", e);
            validator = null; // Set to null so we can handle it in doPost
        }
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.debug("/addCalendar endpoint called");
        res.setContentType("application/json");

        try {
            RequestCalendar data = mapper.readValue(req.getInputStream(), RequestCalendar.class);

            if (validator != null) {
                Set<ConstraintViolation<RequestCalendar>> violations = validator.validate(data);
                if (!violations.isEmpty()) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    res.getWriter().write("{\"error\": \"Validation error: " + violations.iterator().next().getMessage() + "\"}");
                    return;
                }
            }

            logger.debug("Adding new calendar with data: {}", data);
            Calendar calendar = new Calendar(data.address, data.location, data.date, data.online);
            service.addCalendar(calendar);
            res.setStatus(HttpServletResponse.SC_CREATED);
            res.getWriter().write("{\"message\": \"Calendar created successfully\"}");

        } catch (MismatchedInputException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Invalid data type for field: " + e.getPath().get(0).getFieldName() + ". " + e.getOriginalMessage() + "\"}");
            logger.error("Type mismatch error", e);
        } catch (JsonMappingException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Invalid JSON structure: " + e.getOriginalMessage() + "\"}");
            logger.error("JSON mapping error", e);
        } catch (JsonParseException e) {
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            res.getWriter().write("{\"error\": \"Malformed JSON: " + e.getOriginalMessage() + "\"}");
            logger.error("JSON parse error", e);
        }
    }
}
