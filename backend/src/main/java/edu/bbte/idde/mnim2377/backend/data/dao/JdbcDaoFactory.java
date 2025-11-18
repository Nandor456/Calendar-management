package edu.bbte.idde.mnim2377.backend.data.dao;

public class JdbcDaoFactory extends DaoFactory {
    private CalendarDao calendarDao;

    @Override
    public CalendarDao getCalendarDao() {
        if (calendarDao == null) {
            calendarDao = new JdbcDao();
        }
        return calendarDao;
    }
}

