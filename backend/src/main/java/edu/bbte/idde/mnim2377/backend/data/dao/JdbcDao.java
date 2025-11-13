package edu.bbte.idde.mnim2377.backend.data.dao;

import edu.bbte.idde.mnim2377.backend.config.DataSourceProvider;
import edu.bbte.idde.mnim2377.backend.data.exception.DataException;
import edu.bbte.idde.mnim2377.backend.data.exception.DatabaseException;
import edu.bbte.idde.mnim2377.backend.data.model.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcDao implements CalendarDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDao.class);

    @Override
    public List<Calendar> findAll() {
        String sql = "SELECT * FROM calendar";
        List<Calendar> calendars = new ArrayList<>();
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                calendars.add(mapResultSetToCalendar(rs));
            }
            logger.info("Successfully fetched {} calendars", calendars.size());
            return calendars;
        } catch (SQLException e) {
            logger.error("Failed to fetch all calendars", e);
            throw new DatabaseException("Failed to connect to database", e);
        }

    }

    @Override
    public Calendar getCalendarById(String id) throws DataException {
        String sql = "SELECT * FROM calendar WHERE id = ?";
        logger.debug("Fetching calendar with ID: {}", id);

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    logger.info("Successfully fetched calendar with ID: {}", id);
                    return mapResultSetToCalendar(rs);
                } else {
                    logger.warn("No calendar found with ID: {}", id);
                    throw new DataException("No calendar found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to fetch calendar with ID: {}", id, e);
            throw new DatabaseException("Failed to fetch calendar with ID: " + id, e);
        }
    }

    @Override
    public void create(Calendar calendar) {
        logger.debug("Creating calendar with ID: {}", calendar.getId());
        String sql = "INSERT INTO calendar (id, address, location, date, is_online) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, calendar.getId());
            pstmt.setString(2, calendar.getAddress());
            pstmt.setString(3, calendar.getLocation());
            pstmt.setDate(4, Date.valueOf(calendar.getDate()));
            pstmt.setBoolean(5, calendar.isOnline());
            pstmt.executeUpdate();

            logger.info("Successfully created calendar with ID: {}", calendar.getId());

        } catch (SQLException e) {
            logger.error("Failed to create calendar with ID: {}", calendar.getId(), e);
            throw new DatabaseException("Failed to connect to database", e);
        }
    }

    @Override
    public void update(Calendar calendar) throws DataException {
        logger.debug("Updating calendar with ID: {}", calendar.getId());
        String sql = "UPDATE calendar SET address = ?, location = ?, date = ?, is_online = ? WHERE id = ?";
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, calendar.getAddress());
            pstmt.setString(2, calendar.getLocation());
            pstmt.setDate(3, Date.valueOf(calendar.getDate()));
            pstmt.setBoolean(4, calendar.isOnline());
            pstmt.setString(5, calendar.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                logger.warn("Cannot update: calendar with ID {} not found", calendar.getId());
                throw new DataException("Cant update: ID:" + calendar.getId() + "cant be found");
            }
            logger.info("Successfully updated calendar with ID: {}", calendar.getId());
        } catch (SQLException e) {
            logger.error("Failed to update calendar with ID: {}", calendar.getId(), e);
            throw new DatabaseException("Failed to connect to database", e);
        }

    }

    @Override
    public void delete(String id) throws DataException {
        logger.debug("Deleting calendar with ID: {}", id);
        String sql = "DELETE FROM calendar WHERE id = ?";
        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                logger.warn("Cannot delete: calendar with ID {} not found", id);
                throw new DataException("Cant delete: ID:" + id + "cant be found");
            }
            logger.info("Successfully deleted calendar with ID: {}", id);

        } catch (SQLException e) {
            logger.error("Failed to delete calendar with ID: {}", id, e);
            throw new DatabaseException("Failed to connect to database", e);
        }
    }

    private Calendar mapResultSetToCalendar(ResultSet rs) throws SQLException {
        return new Calendar(
                rs.getString("id"),
                rs.getString("address"),
                rs.getString("location"),
                rs.getDate("date").toLocalDate(),
                rs.getBoolean("is_online")
        );
    }
}
