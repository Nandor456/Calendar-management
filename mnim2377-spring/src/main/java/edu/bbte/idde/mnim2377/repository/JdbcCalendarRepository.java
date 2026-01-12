package edu.bbte.idde.mnim2377.repository;

import edu.bbte.idde.mnim2377.model.Calendar;
import edu.bbte.idde.mnim2377.model.Event;
import edu.bbte.idde.mnim2377.repository.exception.RepositoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("jdbc")
@Slf4j
public class JdbcCalendarRepository implements CalendarRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Calendar> calendarRowMapper = (rs, rowNum) -> {
        return new Calendar(
                UUID.fromString(rs.getString("id")),
                rs.getString("address"),
                rs.getString("location"),
                rs.getDate("date").toLocalDate(),
                rs.getBoolean("is_online")
        );
    };

    private final RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
        LocalTime startTime = rs.getTime("start_time") != null
                ? rs.getTime("start_time").toLocalTime()
                : null;
        LocalTime endTime = rs.getTime("end_time") != null
                ? rs.getTime("end_time").toLocalTime()
                : null;
        return new Event(
                UUID.fromString(rs.getString("id")),
                rs.getString("title"),
                startTime,
                endTime,
                null
        );
    };

    public JdbcCalendarRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Event> findEventsByCalendarId(UUID calendarId) {
        String sql = "SELECT * FROM event WHERE calendar_id = ?";
        return jdbcTemplate.query(sql, eventRowMapper, calendarId);
    }

    private void upsertEventsForCalendar(Calendar calendar) {
        List<Event> events = calendar.getEvents();
        UUID calendarId = calendar.getId();

        if (events == null || events.isEmpty()) {
            deleteEventsForCalendar(calendarId);
            return;
        }

        List<UUID> eventIds = persistEvents(calendar, events);
        deleteEventsMissingFromAggregate(calendarId, eventIds);
    }

    private void deleteEventsForCalendar(UUID calendarId) {
        jdbcTemplate.update("DELETE FROM event WHERE calendar_id = ?", calendarId);
    }

    private List<UUID> persistEvents(Calendar calendar, List<Event> events) {
        UUID calendarId = calendar.getId();

        List<UUID> ids = new java.util.ArrayList<>();
        for (Event event : events) {
            if (event == null) {
                continue;
            }
            UUID eventId = ensureEventId(event);
            event.setCalendar(calendar);

            upsertSingleEvent(calendarId, event);
            ids.add(eventId);
        }
        return ids;
    }

    private UUID ensureEventId(Event event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        return event.getId();
    }

    private void upsertSingleEvent(UUID calendarId, Event event) {
        String updateSql = "UPDATE event SET title = ?, start_time = ?, end_time = ?, calendar_id = ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(
                updateSql,
                event.getTitle(),
                toSqlTime(event.getStartTime()),
                toSqlTime(event.getEndTime()),
                calendarId,
                event.getId()
        );

        if (rowsAffected > 0) {
            return;
        }

        String insertSql = "INSERT INTO event (id, title, start_time, end_time, calendar_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(
                insertSql,
                event.getId(),
                event.getTitle(),
                toSqlTime(event.getStartTime()),
                toSqlTime(event.getEndTime()),
                calendarId
        );
    }

    private java.sql.Time toSqlTime(LocalTime time) {
        return time != null ? java.sql.Time.valueOf(time) : null;
    }

    private void deleteEventsMissingFromAggregate(UUID calendarId, List<UUID> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            deleteEventsForCalendar(calendarId);
            return;
        }

        StringBuilder placeholders = new StringBuilder();
        for (int i = 0; i < eventIds.size(); i++) {
            if (i > 0) {
                placeholders.append(',');
            }
            placeholders.append('?');
        }

        Object[] params = new Object[1 + eventIds.size()];
        params[0] = calendarId;
        int idx = 1;
        for (UUID eventId : eventIds) {
            params[idx++] = eventId;
        }

        String deleteSql = "DELETE FROM event WHERE calendar_id = ? AND id NOT IN (" + placeholders + ')';
        jdbcTemplate.update(deleteSql, params);
    }

    @Override
    public List<Calendar> findAll() {
        String sql = "SELECT * FROM calendar";
        log.info("Fetching all calendars from database");
        List<Calendar> calendars = jdbcTemplate.query(sql, calendarRowMapper);
        log.info("Successfully fetched {} calendars", calendars.size());
        return calendars;
    }

    @Override
    public Calendar create(Calendar calendar) {
        String sql = "INSERT INTO calendar (id, address, location, date, is_online) VALUES (?, ?, ?, ?, ?)";
        log.info("Creating calendar with ID: {}", calendar.getId());
        jdbcTemplate.update(sql,
                calendar.getId(),
                calendar.getAddress(),
                calendar.getLocation(),
                java.sql.Date.valueOf(calendar.getDate()),
                calendar.getOnline()
        );

        // Persist aggregate children
        upsertEventsForCalendar(calendar);

        log.info("Successfully created calendar with ID: {}", calendar.getId());
        return calendar;
    }

    @Override
    public void update(Calendar calendar) throws RepositoryException {
        String sql = "UPDATE calendar SET address = ?, location = ?, date = ?, is_online = ? WHERE id = ?";
        log.info("Updating calendar with ID: {}", calendar.getId());
        int rowsAffected = jdbcTemplate.update(sql,
                calendar.getAddress(),
                calendar.getLocation(),
                java.sql.Date.valueOf(calendar.getDate()),
                calendar.getOnline(),
                calendar.getId()
        );
        if (rowsAffected == 0) {
            log.warn("No calendar found with ID: {}", calendar.getId());
            throw new RepositoryException("No calendar found with ID: " + calendar.getId());
        }

        // Sync aggregate children
        upsertEventsForCalendar(calendar);
    }

    @Override
    public void deleteById(UUID id) throws RepositoryException {
        // Delete children first to avoid FK issues
        jdbcTemplate.update("DELETE FROM event WHERE calendar_id = ?", id);

        String sql = "DELETE FROM calendar WHERE id = ?";
        log.info("Deleting calendar with ID: {}", id);
        int rowsAffected = jdbcTemplate.update(sql, id);
        if (rowsAffected == 0) {
            log.warn("No calendar found with ID: {}", id);
            throw new RepositoryException("No calendar found with ID: " + id);
        }
    }

    @Override
    public Optional<Calendar> findById(UUID id) {
        String sql = "SELECT * FROM calendar WHERE id = ?";
        log.info("Fetching calendar with ID: {}", id);
        List<Calendar> calendars = jdbcTemplate.query(sql, calendarRowMapper, id);
        if (calendars.isEmpty()) {
            log.warn("No calendar found with ID: {}", id);
            return Optional.empty();
        } else {
            log.info("Successfully fetched calendar with ID: {}", id);
            return Optional.of(calendars.getFirst());
        }
    }

    @Override
    public Optional<Calendar> findWithEventsById(UUID id) {
        Optional<Calendar> calendarOpt = findById(id);
        if (calendarOpt.isEmpty()) {
            return Optional.empty();
        }

        Calendar calendar = calendarOpt.get();
        List<Event> events = findEventsByCalendarId(id);
        for (Event event : events) {
            event.setCalendar(calendar);
        }
        calendar.setEvents(events);
        return Optional.of(calendar);
    }

    @Override
    public List<Calendar> findByDate(LocalDate date) {
        String sql = "SELECT * FROM calendar WHERE date = ?";
        log.info("Fetching calendars with date: {}", date);
        List<Calendar> calendars = jdbcTemplate.query(sql, calendarRowMapper, java.sql.Date.valueOf(date));
        log.info("Successfully fetched {} calendars with date: {}", calendars.size(), date);
        return calendars;
    }
}
