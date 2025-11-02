package edu.bbte.idde.mnim2377.data.dao;

import edu.bbte.idde.mnim2377.config.DataSourceProvider;
import edu.bbte.idde.mnim2377.data.exception.DataException;
import edu.bbte.idde.mnim2377.data.exception.DatabaseException;
import edu.bbte.idde.mnim2377.data.model.Calendar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresCalendarDao implements CalendarDao{
    @Override
    public List<Calendar> findAll() {
        String sql = "SELECT * FROM calendar";
        List<Calendar> calendars = new ArrayList<>();
        try(Connection conn = DataSourceProvider.getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                calendars.add(mapResultSetToCalendar(rs));
            }
            return calendars;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }

    }

    @Override
    public Calendar getCalendarById(String id) throws DataException {
        String sql = "SELECT * FROM calendar WHERE id = ?";

        try (Connection conn = DataSourceProvider.getDataSource().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCalendar(rs);
                } else {
                    throw new DataException("No calendar found with ID: " + id);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch calendar with ID: " + id, e);
        }
    }

    @Override
    public void create(Calendar calendar) {
        String sql = "INSERT INTO calendar (id, address, location, date, is_online) VALUES (?, ?, ?, ?, ?)";
        try(Connection conn = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, calendar.getId());
            pstmt.setString(2, calendar.getAddress());
            pstmt.setString(3, calendar.getLocation());
            pstmt.setDate(4, Date.valueOf(calendar.getDate()));
            pstmt.setBoolean(5, calendar.getOnline());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }

    @Override
    public void update(Calendar calendar) throws DataException {
        String sql = "UPDATE calendar SET address = ?, location = ?, date = ?, is_online = ? WHERE id = ?";
        try(Connection conn = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, calendar.getAddress());
            pstmt.setString(2, calendar.getLocation());
            pstmt.setDate(3, Date.valueOf(calendar.getDate()));
            pstmt.setBoolean(4, calendar.getOnline());
            pstmt.setString(5, calendar.getId());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Cant update: ID:" + calendar.getId() + "cant be found");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }

    }

    @Override
    public void delete(String id) throws DataException {
        String sql = "DELETE FROM calendar WHERE id = ?";
        try(Connection conn = DataSourceProvider.getDataSource().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataException("Cant delete: ID:" + id + "cant be found");
            }
        } catch (SQLException e) {
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
